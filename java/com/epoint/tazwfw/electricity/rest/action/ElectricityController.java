package com.epoint.tazwfw.electricity.rest.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.auditproject.util.FlowsnUtil;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditorga.auditworkingday.inter.IAuditOrgaWorkingDay;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectoperation.domain.AuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectoperation.inter.IAuditProjectOperation;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspimaterial.domain.AuditSpIMaterial;
import com.epoint.basic.auditsp.auditspimaterial.inter.IAuditSpIMaterial;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspintegratedcompany.domain.AuditSpIntegratedCompany;
import com.epoint.basic.auditsp.auditspintegratedcompany.inter.IAuditSpIntegratedCompany;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.auditsp.auditspitask.inter.IAuditSpITask;
import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.basic.auditsp.auditspphase.inter.IAuditSpPhase;
import com.epoint.basic.auditsp.auditsptask.domain.AuditSpTask;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.composite.auditsp.handlespimaterial.inter.IHandleSPIMaterial;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.workingday.api.IWorkingdayService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.jnycsl.utils.WavePushInterfaceUtils;
import com.epoint.tazwfw.electricity.rest.service.ElectricityConstant;
import com.epoint.zhenggai.api.ZhenggaiService;

@RestController
@RequestMapping("/web/enterprise")
public class ElectricityController
{
    /**
     * 日志
     */
    transient Logger log = Logger.getLogger(ElectricityController.class);
    
    @Autowired
    private ZhenggaiService zhenggaiImpl;
    
    @Autowired
    private IAuditProject auditProjectServcie;

    private static String ElectricCombo = ConfigUtil.getConfigValue("taelectric", "ElectricCombo");

    /**
     * 1、获取申报信息
     * 
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/chain/getApplicationInfo", method = RequestMethod.POST)
    public String getApplicationInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getApplicationInfo接口=======");
            // 1、入参转化为JSON对象
            log.info("=======params=======" + params);
            JSONObject json = JSON.parseObject(params);
            String flowId = json.getString("flowId");
            String bussinessGuid = ConfigUtil.getConfigValue("taelectric", flowId);
            if (StringUtil.isBlank(bussinessGuid)) {
                bussinessGuid = ElectricCombo;
            }
            IAuditSpTask iAuditSpTask = ContainerFactory.getContainInfo().getComponent(IAuditSpTask.class);
            IAuditTask iAuditTask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            IOuService ouService = ContainerFactory.getContainInfo().getComponent(IOuService.class);
            IAuditTaskMaterial auditTaskMaterialService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditTaskMaterial.class);
            if (StringUtil.isNotBlank(bussinessGuid)) {
                // 2、根据套餐guid查找相应信息
                List<AuditSpTask> sptaskList = iAuditSpTask.getAllAuditSpTaskByBusinessGuid(bussinessGuid).getResult();
                List<AuditTask> taskList = new ArrayList<AuditTask>();
                for (AuditSpTask auditSpTask : sptaskList) {
                    AuditTask auditTask = iAuditTask.getUseTaskAndExtByTaskid(auditSpTask.getTaskid()).getResult();
                    taskList.add(auditTask);
                }
                // 3.封装数据
                JSONObject dataJson = new JSONObject();
                dataJson.put("state", ElectricityConstant.HTTPOK);
                dataJson.put("error", "");
                JSONObject dataInfoJson = new JSONObject();
                dataInfoJson.put("flowId", flowId);// 流转规则ID
                dataInfoJson.put("formId", "");// 表单ID
                JSONArray itemArray = new JSONArray();
                for (AuditTask auditTask : taskList) {
                    FrameOu ouInfo = ouService.getOuByOuGuid(auditTask.getOuguid());
                    List<AuditTaskMaterial> materialList = auditTaskMaterialService
                            .selectTaskMaterialListByTaskGuid(auditTask.getRowguid(), true).getResult();
                    JSONObject dataItemJson = new JSONObject();
                    JSONArray resourceArray = new JSONArray();
                    // 封装事项信息
                    dataItemJson.put("itemId", auditTask.getRowguid());// 事项ID
                    dataItemJson.put("itemName", auditTask.getTaskname());// 事项名称
                    dataItemJson.put("itemCode", auditTask.getItem_id());// 事项编码
                    dataItemJson.put("formId", "");// 事项表单id
                    dataItemJson.put("orgCode", ouInfo.getOucode());// 部门code
                    dataItemJson.put("orgName", ouInfo.getOuname());// 部门名称
                    // 封装材料信息
                    for (AuditTaskMaterial auditTaskMaterial : materialList) {
                        JSONObject dataResourceJson = new JSONObject();
                        dataResourceJson.put("resourceCode", auditTaskMaterial.getMaterialid());// 材料编码
                        dataResourceJson.put("resourceType", "Apply");// 材料类型
                        dataResourceJson.put("resourceName", auditTaskMaterial.getMaterialname());// 材料名称
                        resourceArray.add(dataResourceJson);
                    }
                    dataItemJson.put("resourceArray", resourceArray);
                    itemArray.add(dataItemJson);
                }
                dataInfoJson.put("itemArray", itemArray);
                dataJson.put("info", dataInfoJson);
                return dataJson.toString();
            }
            else {
                log.info("=======未找到相应套餐服务=======flowId=" + flowId);
                JSONObject dataJson = new JSONObject();
                dataJson.put("state", ElectricityConstant.HTTPERROR);
                dataJson.put("error", "未找到相应套餐服务");
                return dataJson.toString();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======结束调用getApplicationInfo接口=======");
            log.info("=======getApplicationInfo接口参数：params【" + params + "】=======");
            log.info("=======getApplicationInfo异常信息：" + e.getMessage() + "=======");
            JSONObject dataJson = new JSONObject();
            dataJson.put("state", ElectricityConstant.HTTPERROR);
            dataJson.put("error", e.getMessage());
            return dataJson.toString();
        }
    }

    /**
     * 2、提交申报信息
     * 
     * @param params
     * @param request
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/chain/submitApplicationInfoApp", method = RequestMethod.POST)
    public String submitApplicationInfoApp(@RequestBody String params, @Context HttpServletRequest request) {
        EpointFrameDsManager.begin(null);
        try {
            log.info("=======开始调用submitApplicationInfoApp接口=======");
            // 1、入参转化为JSON对象
            log.info("<<<<<<=======params信息=======>>>>>>" + params);
            JSONObject json = JSON.parseObject(params);
            String flowId = json.getString("flowId");// 流转规则Id，地市对应一个ID--areacode
            String receiveId = json.getString("receiveId");// 业务流水号AuditSpInstance-YeWuGuid
            JSONArray selectItemArray = json.getJSONArray("selectItem");// 所有需要办理事项的id数据组
            String serviceObj = "0".equals(json.getString("serviceObj")) ? "企业" : "个人";// 服务对象0企业；1个人；
            JSONObject dataInfoObj = json.getJSONObject("dataInfo");// 采集信息项，json格式
            JSONObject applyObj = json.getJSONObject("applyObj");// 申报对象基本信息
            JSONArray materialArray = json.getJSONArray("material");// 办理事项各部门需要的材料集合
            // 返回json数据
            JSONObject dataJson = new JSONObject();
            // 获取bussinessGuid
            String businessGuid = ConfigUtil.getConfigValue("taelectric", flowId);
            if (StringUtil.isBlank(businessGuid)) {
                businessGuid = ElectricCombo;
            }
            // 区划code
            String areacode = flowId.substring(0,6);
            // 中心guid ----- 事项所在窗口的中心
            String centerGuid = "";
            // 引入接口api
            IAuditSpIntegratedCompany iAuditSpIntegratedCompany = ContainerFactory.getContainInfo()
                    .getComponent(IAuditSpIntegratedCompany.class);
            IAuditTask iAuditTask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            IAuditSpBusiness iAuditSpBusiness = ContainerFactory.getContainInfo().getComponent(IAuditSpBusiness.class);
            IAuditSpInstance iAuditSpInstance = ContainerFactory.getContainInfo().getComponent(IAuditSpInstance.class);
            IAuditSpPhase iAuditSpPhase = ContainerFactory.getContainInfo().getComponent(IAuditSpPhase.class);
            IAuditSpISubapp iAuditSpISubapp = ContainerFactory.getContainInfo().getComponent(IAuditSpISubapp.class);
            IAuditSpTask iAuditSpTask = ContainerFactory.getContainInfo().getComponent(IAuditSpTask.class);
            IAuditSpITask iAuditSpITask = ContainerFactory.getContainInfo().getComponent(IAuditSpITask.class);
            IAuditSpIMaterial iAuditSpIMaterial = ContainerFactory.getContainInfo()
                    .getComponent(IAuditSpIMaterial.class);
            IHandleSPIMaterial iHandleSPIMaterial = ContainerFactory.getContainInfo()
                    .getComponent(IHandleSPIMaterial.class);
            IHandleProject iHandleProject = ContainerFactory.getContainInfo().getComponent(IHandleProject.class);
            IAuditOrgaWindowYjs iAuditOrgaWindow = ContainerFactory.getContainInfo().getComponent(IAuditOrgaWindowYjs.class);
            // 2、初始化套餐办件 如果ywguid为空说明未初始化AuditSpInstance
            if (StringUtil.isBlank(receiveId)) {
                // 初始化套餐
                // 2.1 保存基本信息
                AuditSpIntegratedCompany dataBean = new AuditSpIntegratedCompany();// 套餐式开公司实体对象
                String yewuGuid = UUID.randomUUID().toString().replace("-", "");
                dataBean.setRowguid(yewuGuid);
                dataBean.setOperatedate(new Date());
                dataBean.setOperateusername("电力对接");
                dataBean.setCompanyname(dataInfoObj.getString("consName"));// 企业名称--用户名称
                dataBean.setZzjgdmz(applyObj.getString("certNo"));// 统一社会信用代码--证件号码
                dataBean.setContactperson(dataInfoObj.getString("agentName"));// 联系人
                dataBean.setContactphone(applyObj.getString("phone"));// 联系电话--联系人电话
                dataBean.setCapital(null);// 注册资本（万元）
                dataBean.setLegalpersonemail("");// 法人Email
                dataBean.setContactpostcode("");// 邮编
                dataBean.setFundssource("");// 经费来源 选择
                dataBean.setAddress(dataInfoObj.getString("elecAddr"));// 用电地址
                dataBean.setCompanytype("");// 企业类型 选择
                dataBean.setScope("");// 经营范围
                dataBean.setLegalpersonduty("");// 法人职务 请选择
                iAuditSpIntegratedCompany.addAuditSpIntegratedCompany(dataBean);
                // 2.2 初始化实例信息
                // 2.2.1 生成主题实例信息
                AuditSpBusiness auditSpBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessGuid)
                        .getResult();
                String biGuid = iAuditSpInstance
                        .addBusinessInstance(businessGuid, yewuGuid, "", dataBean.getCompanyname(), "",
                                auditSpBusiness.getBusinessname(), areacode, auditSpBusiness.getBusinesstype())
                        .getResult();
                String phaseGuid = "";
                List<AuditSpPhase> listPhase = iAuditSpPhase.getSpPaseByBusinedssguid(businessGuid).getResult();
                if (listPhase != null && listPhase.size() > 0) {
                    for (AuditSpPhase auditSpPhase : listPhase) {
                        if (!"1".equals(auditSpPhase.get("isbigphase"))) {
                            phaseGuid = auditSpPhase.getRowguid();
                            break;
                        }
                    }
                    // 2.2.2 生成子申报信息
                    String subappGuid = iAuditSpISubapp.addSubapp(businessGuid, biGuid, phaseGuid,
                            dataBean.getCompanyname(), dataBean.getCompanyname(), "", ZwfwConstant.APPLY_WAY_CKDJ)
                            .getResult();

                    // 2.2.3 生成事项实例信息
                    List<AuditSpTask> auditSpTasks = iAuditSpTask.getAllAuditSpTaskByPhaseguid(phaseGuid).getResult();
                    for (AuditSpTask auditSpTask : auditSpTasks) {
                        AuditTask auditTask = iAuditTask.selectUsableTaskByTaskID(auditSpTask.getTaskid()).getResult();
                        if (auditTask != null) {
                        	iAuditSpITask.addTaskInstance(businessGuid, biGuid, phaseGuid, auditTask.getRowguid(),
                                    auditTask.getTaskname(), subappGuid,
                                    auditTask.getOrdernum() == null ? 0 : auditTask.getOrdernum());
                        }
                    }

                    // 2.2.4 生成材料实例信息
                    iHandleSPIMaterial.initSubappMaterial(subappGuid, businessGuid, biGuid, phaseGuid, "", "");
                    // 获取材料附件更新材料信息
                    List<AuditSpIMaterial> auditSpIMaterialList = iAuditSpIMaterial
                            .getSpIMaterialBySubappGuid(subappGuid).getResult();

                    for (AuditSpIMaterial auditSpIMaterial : auditSpIMaterialList) {
                        for (int i = 0; i < materialArray.size(); i++) {
                            JSONObject materialObj = materialArray.getJSONObject(i);
                            if (auditSpIMaterial.getMaterialguid().equals(materialObj.getString("resourceCode"))) {
                                handleFile(auditSpIMaterial.getCliengguid(), materialObj.getString("fileName"),
                                        materialObj.getString("filePath"));

                                // 设置材料提交状态
                                auditSpIMaterial.setStatus("20");
                                iAuditSpIMaterial.updateSpIMaterial(auditSpIMaterial);
                            }

                        }
                    }
                    initProject("", subappGuid, selectItemArray, centerGuid, biGuid, businessGuid, dataInfoObj, flowId);
                    // 2.1、处理表单基本信息
                    //HandleElectricityForm handleForm = new HandleElectricityForm();
                    //handleForm.handleBasic(biGuid, dataInfoObj, flowId);
                    dataJson.put("state", ElectricityConstant.HTTPOK);
                    dataJson.put("error", "");
                    dataJson.put("bizId", yewuGuid);// 业务标识
                    dataJson.put("receiveId", yewuGuid);// 业务流水号
                }
                else {
                    log.info("=======未配置主题阶段！=======");
                    dataJson.put("state", ElectricityConstant.HTTPERROR);
                    dataJson.put("error", "未配置主题阶段");
                    dataJson.put("bizId", "");// 业务标识
                    dataJson.put("receiveId", "");// 业务流水号
                }
            }
            else {
                // 直接生成办件信息
                // 根据ywguid查询subappGuid 和biGuid
                AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByYeWuGuid(receiveId).getResult();
                String biGuid = auditSpInstance.getRowguid();
                String subappGuid = "";
                subappGuid = iAuditSpISubapp.getSubappByBIGuid(biGuid).getResult().get(0).getRowguid();
                initProject(receiveId, subappGuid, selectItemArray, centerGuid, biGuid, businessGuid, dataInfoObj,
                        flowId);
                dataJson.put("state", ElectricityConstant.HTTPOK);
                dataJson.put("error", "");
                dataJson.put("bizId", receiveId);// 业务标识
                dataJson.put("receiveId", receiveId);// 业务流水号

            }
            log.info("=======结束调用submitApplicationInfoApp接口=======");
            EpointFrameDsManager.commit();
            return dataJson.toString();
        }
        catch (Exception e) {
            EpointFrameDsManager.rollback();
            e.printStackTrace();
            log.info("=======submitApplicationInfoApp接口参数：params【" + params + "】=======");
            log.info("=======submitApplicationInfoApp异常信息：" + e.getMessage() + "=======");
            log.info("=======结束调用submitApplicationInfoApp接口=======");
            JSONObject dataJson = new JSONObject();
            dataJson.put("state", ElectricityConstant.HTTPERROR);
            dataJson.put("error", e.getMessage());
            return dataJson.toString();
        }
        finally {
            EpointFrameDsManager.close();
        }
    }

    /**
     * 3、查询链条业务办件状态
     * 
     * @param params
     * @param request
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/getBusinessState", method = RequestMethod.POST)
    public String getBusinessState(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getBusinessState接口=======");
            log.info("=======params=======" + params);
            // 1、入参转化为JSON对象
            JSONObject json = JSON.parseObject(params);
            // 业务标识
            String bizId = json.getString("bizId");
            // 业务流水号 两个传一个AuditSpInstance-YeWuGuid
            String receiveId = json.getString("receiveId");
            IAuditTask iAuditTask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            IAuditSpInstance iAuditSpInstance = ContainerFactory.getContainInfo().getComponent(IAuditSpInstance.class);
            IAuditSpISubapp iAuditSpISubapp = ContainerFactory.getContainInfo().getComponent(IAuditSpISubapp.class);
            IAuditSpITask iAuditSpITask = ContainerFactory.getContainInfo().getComponent(IAuditSpITask.class);
            ICodeItemsService iCodeItemsService = ContainerFactory.getContainInfo()
                    .getComponent(ICodeItemsService.class);
            IAuditProject iAuditProject = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
            IAuditOrgaWorkingDay iWorkingdayService = ContainerFactory.getContainInfo()
                    .getComponent(IAuditOrgaWorkingDay.class);
            IAuditProjectOperation iAuditProjectOperation = ContainerFactory.getContainInfo()
                    .getComponent(IAuditProjectOperation.class);
            if (StringUtil.isNotBlank(receiveId)) {
                // 2、根据套餐申报主键查询申报信息AuditSpInstance
                AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByYeWuGuid(receiveId).getResult();
                // 3.封装数据
                JSONObject dataJson = new JSONObject();

                if (auditSpInstance != null) {
                    List<AuditSpISubapp> auditSpISubappList = iAuditSpISubapp
                            .getSubappByBIGuid(auditSpInstance.getRowguid()).getResult();
                    AuditSpISubapp auditSpISubapp = null;
                    if (auditSpISubappList != null && auditSpISubappList.size() > 0) {
                        auditSpISubapp = auditSpISubappList.get(0);
                    }
                    dataJson.put("state", ElectricityConstant.HTTPOK);
                    dataJson.put("error", "");
                    dataJson.put("applySubject", auditSpInstance.getItemname());// 业务名称
                    dataJson.put("acceptTime",
                            EpointDateUtil.convertDate2String(auditSpInstance.getCreatedate(), "yyyy-MM-dd hh:mm:ss"));// 受理时间
                    dataJson.put("status",
                            iCodeItemsService.getItemTextByCodeName("一链办理状态", auditSpISubapp.getStatus()));// 整体办理状态
                    List<AuditSpITask> auditSpITaskList = iAuditSpITask.getSpITaskByBIGuid(auditSpInstance.getRowguid())
                            .getResult();
                    JSONArray infoArray = new JSONArray();
                    for (AuditSpITask auditSpITask : auditSpITaskList) {
                        AuditTask auditTask = iAuditTask.getAuditTaskByGuid(auditSpITask.getTaskguid(), true)
                                .getResult();

                        if (StringUtil.isNotBlank(auditSpITask.getProjectguid())) {
                            AuditProject auditProject = iAuditProject.getAuditProjectByRowGuid(
                                    auditSpITask.getProjectguid(), auditSpInstance.getAreacode()).getResult();
                            JSONObject infoObject = new JSONObject();
                            if(auditProject != null) {
                            	infoObject.put("itemId", auditTask.getRowguid());// 事项id
                            	infoObject.put("itemName", auditTask.getTaskname());// 事项名称
                            	infoObject.put("orgName", auditTask.getOuname());// 部门名称
	                            String status = "";
                            	if (auditProject.getStatus() == ZwfwConstant.BANJIAN_STATUS_ZCBJ
                            			|| auditProject.getStatus() == ElectricityConstant.BANJIAN_STATUS_ZGBJ) {
                            		status = ElectricityConstant.DLBJ;// 正常办结、整改办结--99办结
                            	}
                            	else if (auditProject.getStatus() == ZwfwConstant.BANJIAN_STATUS_YSL) {
                            		status = ElectricityConstant.DLSL;// 已受理--5受理
                            	}
                            	else if (auditProject.getStatus() == ZwfwConstant.BANJIAN_STATUS_SPBTG
                            			|| auditProject.getStatus() == ZwfwConstant.BANJIAN_STATUS_BYSL
                            			|| auditProject.getStatus() == ZwfwConstant.BANJIAN_STATUS_YCZZ) {
                            		status = ElectricityConstant.DLBH;// 审核不通过、不予受理、异常终止--4驳回
                            	}
                            	else if (auditProject.getStatus() >= ZwfwConstant.BANJIAN_STATUS_YJJ) {
                            		status = ElectricityConstant.DLZB;// 已接件 --11在办
                            	}
                            	else {
                            		status = ElectricityConstant.DLWB;// --10未办
                            	}
                            	if(!ElectricityConstant.DLWB.equals(status)) {
                            		infoObject.put("receiveTime", EpointDateUtil
                            				.convertDate2String(auditProject.getReceivedate(), "yyyy-MM-dd hh:mm:ss"));// 受理时间
                            		String finishTime = "";
                            		if (StringUtil.isNotBlank(auditProject.getBanjiedate())) {
                            			finishTime = EpointDateUtil.convertDate2String(auditProject.getBanjiedate(),
                            					"yyyy-MM-dd hh:mm:ss");
                            		}
                            		infoObject.put("finishTime", finishTime);// 办结时间
                            		// 承诺办结时间
                            		String centerGuid = ConfigUtil.getConfigValue("taelectric", "centerGuid");
                            		Date workingDayWithOfficeSet = iWorkingdayService.getWorkingDayWithOfficeSet(
                            				centerGuid, auditProject.getReceivedate(), auditTask.getPromise_day()).getResult();
                            		infoObject.put("proTime",
                            				EpointDateUtil.convertDate2String(workingDayWithOfficeSet, "yyyy-MM-dd hh:mm:ss"));// 承诺办结时间
                            		
                            		infoObject.put("userName", StringUtil.isNotBlank(auditProject.getBanjieusername())
                            				? auditProject.getBanjieusername() : " ");// 办结处理人
                            		// 获取办结处理意见
                            		SqlConditionUtil sql = new SqlConditionUtil();
                            		sql.eq("projectguid", auditProject.getRowguid());
                            		sql.eq("operatetype", ElectricityConstant.FINISHTYPE);
                            		AuditProjectOperation auditProjectOperation = iAuditProjectOperation
                            				.getAuditOperationByCondition(sql.getMap()).getResult();
                            		infoObject.put("opinion", StringUtil.isNotBlank(auditProjectOperation)
                            				? auditProjectOperation.getRemarks() : "无");// 办理意见
                            	}
                            	// 办件状态：99办结 5受理 4驳回 11在办 10未办
                            	infoObject.put("status", status);
                            	infoArray.add(infoObject);
                            }
                        }
                    }
                    dataJson.put("info", infoArray);
                }
                else {
                    dataJson.put("state", ElectricityConstant.HTTPERROR);
                    dataJson.put("error", "未找到相应业务数据");
                }

                log.info("=======结束调用getBusinessState接口=======");

                return dataJson.toString();
            }
            else {
                log.info("=======业务标识为空=======flowId=" + "");
                JSONObject dataJson = new JSONObject();
                dataJson.put("state", ElectricityConstant.HTTPERROR);
                dataJson.put("error", "未找到相应套餐服务");
                log.info("=======结束调用getBusinessState接口=======");
                return dataJson.toString();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getBusinessState接口参数：params【" + params + "】=======");
            log.info("=======getBusinessState异常信息：" + e.getMessage() + "=======");
            log.info("=======结束调用getBusinessState接口=======");
            JSONObject dataJson = new JSONObject();
            dataJson.put("state", ElectricityConstant.HTTPERROR);
            dataJson.put("error", e.getMessage());
            return dataJson.toString();
        }
    }

    /**
     * 附件处理
     * 
     * @param auditSpIMaterial
     * @param materialObj
     */
    public void handleFile(String cliengGuid, String fileName, String filePathNo) {
        IAttachService iAttachService = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
        String filePath = ConfigUtil.getConfigValue("taelectric", "filePath");
        String URL = filePath + filePathNo;
        URL url;
        try {
            url = new URL(URL);
            HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
            int code = urlCon.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) {
                throw new Exception("文件读取失败");
            }
            // 读文件流
            InputStream in = urlCon.getInputStream();
            String name = getFileName(URL);
            String fileType = name.substring(name.lastIndexOf(".") + 1, name.length());
            FrameAttachInfo attach = new FrameAttachInfo();
            attach.setAttachGuid(UUID.randomUUID().toString());
            attach.setCliengGuid(cliengGuid);
            attach.setAttachFileName(fileName.substring(0, fileName.lastIndexOf(".")) + "." + fileType);
            attach.setContentType(fileType);
            attach.setUploadDateTime(new Date());
            attach.setAttachStorageGuid(attach.getAttachGuid());
            iAttachService.addAttach(attach, in);
            log.info("========结束=========");
        }
        catch (

        Exception e) {
            e.printStackTrace();
        }
    }

    /** 
     *  从下载文件url中获取文件名
     *  @param urlStr
     *  @return    
     */
    public String getFileName(String urlStr) {
        String fileName = null;
        try {
            URL url = new URL(urlStr);
            URLConnection uc = url.openConnection();
            fileName = uc.getHeaderField("Content-Disposition");
            fileName = new String(fileName.getBytes("ISO-8859-1"), "GBK");
            fileName = URLDecoder.decode(fileName.substring(fileName.indexOf("filename=") + 9), "UTF-8");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }

    /**
     * 初始化生成办件
     * 
     * @param ywGuid
     * @param subappGuid
     * @param selectItemArray
     * @param centerGuid
     * @param biGuid
     * @param businessGuid
     * @param dataInfoObj
     * @throws Exception
     */
    public void initProject(String ywGuid, String subappGuid, JSONArray selectItemArray, String centerGuid,
            String biGuid, String businessGuid, JSONObject dataInfoObj, String flowId) throws Exception {
        // 引入接口api
        IAuditTask iAuditTask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
        IAuditSpISubapp iAuditSpISubapp = ContainerFactory.getContainInfo().getComponent(IAuditSpISubapp.class);
        IAuditSpITask iAuditSpITask = ContainerFactory.getContainInfo().getComponent(IAuditSpITask.class);
        IHandleSPIMaterial iHandleSPIMaterial = ContainerFactory.getContainInfo()
                .getComponent(IHandleSPIMaterial.class);
        IHandleProject iTAHandleProject = ContainerFactory.getContainInfo().getComponent(IHandleProject.class);
        IAuditOrgaWindowYjs iAuditOrgaWindow = ContainerFactory.getContainInfo().getComponent(IAuditOrgaWindowYjs.class);
        //HandleElectricityForm handleForm = new HandleElectricityForm();

        String windowName = "";
        String windowGuid = "";
        List<AuditSpITask> listTask = iAuditSpITask.getTaskInstanceBySubappGuid(subappGuid).getResult();
        for (AuditSpITask task : listTask) {
            AuditTask auditTask = iAuditTask.getAuditTaskByGuid(task.getTaskguid(), true).getResult();
            // 2.3 生成办件（申请） // 未考虑办件补办再上报
            for (int i = 0; i < selectItemArray.size(); i++) {
                JSONObject itemObj = selectItemArray.getJSONObject(i);
                String itemId = itemObj.getString("itemId");
                if (auditTask.getItem_id().equals(itemId)) {
                    List<AuditOrgaWindow> auditOrgaWindowList = iAuditOrgaWindow
                            .getWindowListByTaskId(auditTask.getTask_id()).getResult();
                    if (auditOrgaWindowList != null && auditOrgaWindowList.size() > 0) {
                        // 取事项所在窗口的中心guid
                        centerGuid = auditOrgaWindowList.get(0).getCenterguid();
                        windowGuid = auditOrgaWindowList.get(0).getRowguid();
                        windowName = auditOrgaWindowList.get(0).getWindowname();
                    }
                    // 如果中心为空，则为默认的市级中心guid
                    if (StringUtil.isBlank(centerGuid)) {
                        centerGuid = ConfigUtil.getConfigValue("taelectric", "centerGuid");
                    }

                    // 先生成办件，再初始化材料
                    String projectGuid = iTAHandleProject
                            .InitIntegratedProject(task.getTaskguid(), "", "电力接口同步", "", windowGuid, windowName,
                                    centerGuid, biGuid, subappGuid, businessGuid, ZwfwConstant.APPLY_WAY_CKDJ)
                            .getResult();
                    AuditProject auditProject = auditProjectServcie.getAuditProjectByRowGuid(projectGuid, null).getResult();
                    String unid = zhenggaiImpl.getunidbyTaskid(auditProject.getTask_id());
                    
                    // 请求接口获取受理编码
        			if (StringUtil.isNotBlank(unid)) {
        				String result = FlowsnUtil.createReceiveNum(unid,task.getRowguid());
        				if (!"error".equals(result)) {
        					log.info("========================>获取受理编码成功！" + result);
        					auditProject.setFlowsn(result);
        					auditProjectServcie.updateProject(auditProject);
        				} else {
        					log.info("========================>获取受理编码失败！");
        					return;
        				}
        			}
                    
                    /*// 构造获取受理编码的请求入参
                    String params2Get = "?itemId=" + unid + "&applyFrom=" + 1;
                    // 请求接口获取受理编码
                    try {
                        JSONObject jsonObj = WavePushInterfaceUtils.createReceiveNum(params2Get, "01");
                        if (jsonObj != null && "200".equals(jsonObj.getString("state"))) {
                            log.info("电力调用生成办件编号===>获取受理编码成功！" + jsonObj.getString("receiveNum") + "#####"
                                    + jsonObj.getString("password"));
                            String pwd = jsonObj.getString("password");
                            String receiveNum = jsonObj.getString("receiveNum");
                            auditProject.setFlowsn(receiveNum);
                            //更新窗口登记都为外网申报
                            //auditProject.setApplyway(10);
                            auditProject.set("pwd", pwd);
                            auditProject.set("is_lczj",4);
                            auditProjectServcie.updateProject(auditProject);
                        }
                        else {
                            log.info("电力调用生成办件编号===>获取受理编码失败！");
                            return;
                        }
                    }
                    catch (IOException e) {
                        log.info("接口请求报错！电力调用生成办件编号===>" + e.getMessage());
                        e.printStackTrace();
                    }*/
                    iAuditSpITask.updateProjectGuid(subappGuid, task.getTaskguid(), projectGuid);
                    // 再进行材料初始化
                    iHandleSPIMaterial.initProjctMaterial(businessGuid, subappGuid, task.getTaskguid(), projectGuid);
                    // 2.4 初始化表单信息
                    //handleForm.handleForm(ywGuid, biGuid, projectGuid, itemId, dataInfoObj, flowId);
                }
            }
        }
        iAuditSpISubapp.updateSubapp(subappGuid, ZwfwConstant.LHSP_Status_YSJ, null);
    }

    /**
     * 调用电力接口推送办件状态及信息
     * 
     * @param auditProject
     * @param auditTask
     */
    public void pushStatusToEc(AuditProject auditProject, AuditTask auditTask ,String OPINION) {
        try {
            log.info("========调用电力接口推送办件状态及信息========");
            int count = 0;
            String targetURL = ConfigUtil.getConfigValue("taelectric", "targetURL");
            URL targetUrl = new URL(targetURL);
            HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("POST");
            httpConnection.setRequestProperty("Content-Type", "application/json");
            // 封装参数
            String params = handleParams(auditProject, auditTask, OPINION);

            OutputStream outputStream = httpConnection.getOutputStream();
            outputStream.write(params.getBytes("UTF-8"));
            outputStream.flush();
            if (httpConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                // 接口通信失败
                log.info("========接口通信失败========");
                throw new RuntimeException("FAILED : HTTP ERROR CODE : " + httpConnection.getResponseCode());
            }
            else {
                // 接口通信成功
                log.info("========接口通信成功========");
                BufferedReader responseBuffer = new BufferedReader(
                        new InputStreamReader((httpConnection.getInputStream()), "UTF-8"));
                String output;
                StringBuffer resultBuff = new StringBuffer();
                while ((output = responseBuffer.readLine()) != null) {
                    resultBuff.append(output);
                }
                String result = resultBuff.toString();
                JSONObject resultJson = JSONObject.parseObject(result);
                if (ElectricityConstant.HTTPERROR.equals(resultJson.getString("state"))) {
                    // 接口调用失败
                    count++;
                    log.info("========接口调用失败！========失败次数： " + count + " 次！");
                    if (count < 3) {
                        // 尝试嵌套调用3次，如果3次全部失败，则抛出异常
                        pushStatusToEc(auditProject, auditTask, OPINION);
                    }
                    else {
                        throw new Exception("========接口调用失败！========ERROR:" + resultJson.getString(""));
                    }

                }
                httpConnection.disconnect();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 封装参数
     * 
     * @param auditProject
     * @param auditTask
     * @return
     */
    private String handleParams(AuditProject auditProject, AuditTask auditTask, String OPINION) {
        //String filePath = ConfigUtil.getConfigValue("taelectric", "filePath");
        IAuditSpInstance iAuditSpInstance = ContainerFactory.getContainInfo().getComponent(IAuditSpInstance.class);
        IOuService ouService = ContainerFactory.getContainInfo().getComponent(IOuService.class);
        IAuditProjectOperation iAuditProjectOperation = ContainerFactory.getContainInfo()
                .getComponent(IAuditProjectOperation.class);
        AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(auditProject.getBiguid()).getResult();
        String ouCode = "";
        FrameOu ouInfo = ouService.getOuByOuGuid(auditTask.getOuguid());
        if (ouInfo != null) {
            ouCode = ouInfo.getOucode();
        }

        log.info("=======auditProject========" + auditProject);
        int projectStatus = auditProject.getStatus();
        String opinion = "";
        JSONObject paramsJson = new JSONObject();
        paramsJson.put("bizId", auditSpInstance.getYewuguid());// 业务编码
        paramsJson.put("receiveId", auditSpInstance.getYewuguid());// 业务流水号
        paramsJson.put("tag", "0");

        String acceptTime = "";
        String acceptPerson = "";

        log.info("=======projectStatus========" + projectStatus);
        if ("90".equals(String.valueOf(projectStatus))) {
            acceptPerson = auditProject.getBanjieusername();
            if (StringUtil.isNotBlank(auditProject.getBanjiedate())) {
                acceptTime = EpointDateUtil.convertDate2String(auditProject.getBanjiedate(), "yyyy-MM-dd hh:mm:ss");
            }
            else {
                acceptTime = EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd hh:mm:ss");
            }
        }
        else {
            acceptTime = EpointDateUtil.convertDate2String(auditProject.getAcceptuserdate(), "yyyy-MM-dd hh:mm:ss");
            acceptPerson = auditProject.getAcceptusername();
        }
        paramsJson.put("reviewTime", acceptTime);// 审核时间
        paramsJson.put("reviewerName", acceptPerson);// 审核人姓名
        log.info("=======reviewTime========" + acceptTime);
        String status = "";// 办件状态

        if (projectStatus == ZwfwConstant.BANJIAN_STATUS_ZCBJ
                || projectStatus == ElectricityConstant.BANJIAN_STATUS_ZGBJ) {
            status = ElectricityConstant.DLBJ;// 正常办结、整改办结--99办结
        }
        else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_YSL) {
            status = ElectricityConstant.DLSL;// 已受理--5受理
        }
        else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_SPBTG || projectStatus == ZwfwConstant.BANJIAN_STATUS_BYSL
                || projectStatus == ZwfwConstant.BANJIAN_STATUS_YCZZ) {
            status = ElectricityConstant.DLBH;// 审核不通过、不予受理、异常终止--4驳回
        }
        else if (projectStatus >= ZwfwConstant.BANJIAN_STATUS_YJJ) {
            status = ElectricityConstant.DLZB;// 已接件 --11在办
        }
        else {
            status = ElectricityConstant.DLWB;// --10未办
        }
        paramsJson.put("status", status);// 办件状态：99办结， 5受理， 4驳回， 11在办， 10未办
        paramsJson.put("itemId", auditTask.getRowguid());// 事项id
        paramsJson.put("itemCode", auditTask.getItem_id());// 事项code
        paramsJson.put("itemRecord", "");// 可证号/备案号
        paramsJson.put("orgCode", ouCode);// 部门编号
        paramsJson.put("orgName", auditTask.getOuname());// 部门名称
        // 环节信息
        SqlConditionUtil sql = new SqlConditionUtil();
        sql.eq("projectguid", auditProject.getRowguid());
        if (projectStatus == ZwfwConstant.BANJIAN_STATUS_ZCBJ
                || projectStatus == ElectricityConstant.BANJIAN_STATUS_ZGBJ) {
            // 正常办结
            sql.eq("operatetype", ZwfwConstant.OPERATE_BJ);
        }
        else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_SPBTG) {
            sql.eq("operatetype", ZwfwConstant.OPERATE_SPBTG);
        }
        else if (projectStatus == ZwfwConstant.BANJIAN_STATUS_BYSL) {
            sql.eq("operatetype", ZwfwConstant.OPERATE_BYSL);
        }
        else if (projectStatus >= ZwfwConstant.BANJIAN_STATUS_YSL && projectStatus < ZwfwConstant.BANJIAN_STATUS_ZCBJ) {
            sql.eq("operatetype", ZwfwConstant.OPERATE_SL);
        }
        AuditProjectOperation auditProjectOperation = iAuditProjectOperation.getAuditOperationByCondition(sql.getMap())
                .getResult();
        if (auditProjectOperation != null) {
            opinion = auditProjectOperation.getRemarks();
        }
        //环节DOC,先上传网盘 TODO 暂不传附件
        JSONObject operateJson = new JSONObject();
        JSONArray operateDocArray = new JSONArray();
        JSONObject operateDocJson = new JSONObject();
        operateDocJson.put("docid", "");
        operateDocJson.put("name", "");
        operateDocArray.add(operateDocJson);
        if(StringUtil.isNotBlank(OPINION)){
            operateJson.put("opinion", OPINION);
        }else{
            operateJson.put("opinion", opinion);
        }
        operateJson.put("doc", operateDocArray);
        paramsJson.put("linkopinioninfo", operateJson);

        return paramsJson.toString();
    }

    /**
     * 初始化部门 [一句话功能简述] [功能详细描述]
     * 
     * @param params
     * @param request
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/initProjectList", method = RequestMethod.POST)
    public String initProjectList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用initProjectList接口=======");
            JSONObject dataJson = new JSONObject();
            String biGuid = request.getParameter("biGuid");
            IAuditSpITask iAuditSpITask = ContainerFactory.getContainInfo().getComponent(IAuditSpITask.class);
            IAuditProject iAuditProject = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
            IAuditTask iAuditTask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
            IAuditSpInstance iAuditSpInstance = ContainerFactory.getContainInfo().getComponent(IAuditSpInstance.class);
            IAuditSpISubapp iAuditSpISubapp = ContainerFactory.getContainInfo().getComponent(IAuditSpISubapp.class);
            AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biGuid).getResult();
            List<AuditSpISubapp> auditSpISubappList = iAuditSpISubapp.getSubappByBIGuid(biGuid).getResult();
            // 套餐式只有一个子申报
            Date finishDate = null;
            if (auditSpISubappList != null && auditSpISubappList.size() > 0) {
                finishDate = auditSpISubappList.get(0).getFinishdate();
            }
            List<AuditSpITask> auditSpITaskList = iAuditSpITask.getSpITaskByBIGuid(biGuid).getResult();
            List<Record> rcList = new ArrayList<>();
            for (AuditSpITask auditSpITask : auditSpITaskList) {
                Record record = new Record();
                AuditProject auditProject = iAuditProject.getAuditProjectByRowGuid(auditSpITask.getProjectguid(), "")
                        .getResult();
                AuditTask auditTask = iAuditTask.getAuditTaskByGuid(auditSpITask.getTaskguid(), true).getResult();
                String ouName = "";
                String receivedate = "";
                String banjiedate = "";
                String projectStatus = "";
                if (auditProject != null) {
                    receivedate = EpointDateUtil.convertDate2String(StringUtil.isNotBlank(auditProject.getReceivedate())
                            ? auditProject.getReceivedate() : auditProject.getOperatedate(), "yyyy年MM月dd日");
                    if (StringUtil.isNotBlank(auditProject.getBanjiedate())) {
                        banjiedate = EpointDateUtil.convertDate2String(auditProject.getBanjiedate(), "yyyy年MM月dd日");
                    }

                    if (auditProject.getStatus() >= ZwfwConstant.BANJIAN_STATUS_ZCBJ) {
                        projectStatus = "matter-item completed";// 正常办结、整改办结--99办结
                    }
                    else if (auditProject.getStatus() == ZwfwConstant.BANJIAN_STATUS_YJJ) {
                        projectStatus = "matter-item accepted-items";// 已接件
                    }
                    else if (auditProject.getStatus() > ZwfwConstant.BANJIAN_STATUS_YJJ && auditProject.getStatus() < ZwfwConstant.BANJIAN_STATUS_ZCBJ) {
                        projectStatus = "matter-item handling";// 11在办
                    }

                }
                else {
                    projectStatus = "matter-item not-launched";// 11未发起
                }
                ouName = auditTask.getOuname();
                record.set("ouname", ouName);
                record.set("projectname", auditTask.getTaskname());
                record.set("promisetime", auditTask.getPromise_day() + "个工作日");
                record.set("ouname", ouName);
                record.set("receivedate", receivedate);
                record.set("banjiedate", banjiedate);
                record.set("status", projectStatus);
                rcList.add(record);
            }
            // 办理用时
            String spstarttime = EpointDateUtil.convertDate2String(auditSpInstance.getCreatedate(), "yyyy年MM月dd日");
            long diff = new Date().getTime() - auditSpInstance.getCreatedate().getTime();// 这样得到的差值是微秒级别
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            String totltTime = days + "天" + hours + "小时" + minutes + "分";
            String finishTime = "";
            String ifend = "0";
            if (StringUtil.isNotBlank(finishDate)) {
                finishTime = EpointDateUtil.convertDate2String(auditSpInstance.getCreatedate(), "yyyy年MM月dd日");
                ifend = "1";
            }
            dataJson.put("ifend", ifend);
            dataJson.put("totlttime", totltTime);
            dataJson.put("spstarttime", spstarttime);
            dataJson.put("finishtime", finishTime);
            dataJson.put("projectlist", rcList);
            dataJson.put("state", "1");
            log.info("=======结束调用initProjectList接口=======");
            return dataJson.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======结束调用initProjectList接口=======");
            log.info("=======initProjectList接口参数：params【" + params + "】=======");
            log.info("=======initProjectList异常信息：" + e.getMessage() + "=======");
            JSONObject dataJson = new JSONObject();
            dataJson.put("state", "0");
            dataJson.put("error", e.getMessage());
            return dataJson.toString();
        }
    }
}
