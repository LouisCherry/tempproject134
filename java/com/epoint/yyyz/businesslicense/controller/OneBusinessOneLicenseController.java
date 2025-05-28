package com.epoint.yyyz.businesslicense.controller;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspimaterial.inter.IAuditSpIMaterial;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspintegratedcompany.domain.AuditSpIntegratedCompany;
import com.epoint.basic.auditsp.auditspintegratedcompany.inter.IAuditSpIntegratedCompany;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.auditsp.auditspitask.inter.IAuditSpITask;
import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.basic.auditsp.auditspphase.inter.IAuditSpPhase;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.cert.basic.certinfo.inter.ICertInfo;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.composite.auditsp.handleproject.inter.ITAHandleProject;
import com.epoint.composite.auditsp.handlespimaterial.inter.IHandleSPIMaterial;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.yyyz.businesslicense.api.IBusinessLicenseBaseinfo;
import com.epoint.yyyz.businesslicense.api.IBusinessLicenseExtension;
import com.epoint.yyyz.businesslicense.api.entity.BusinessLicenseBaseinfo;
import com.epoint.yyyz.businesslicense.api.entity.BusinessLicenseExtension;

/**
 * 一业一证业务接口
 * @description
 * @author shibin
 * @date  2020年5月18日 上午11:31:17
 */
@RestController
@RequestMapping("/OneBusinessOneLicense")
public class OneBusinessOneLicenseController
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 基本信息API
     */
    @Autowired
    private IBusinessLicenseBaseinfo baseinfoService;

    /**
     * 扩展信息API
     */
    @Autowired
    private IBusinessLicenseExtension extensionService;

    /**
     * 扩展信息API
     */
    @Autowired
    private ICertInfo certInfoService;

    /**
     * 保存申报信息
     * @description
     * @author shibin
     * @date  2020年5月18日 上午11:36:34
     */
    @RequestMapping(value = "/submitApplicationInfo", method = RequestMethod.POST)
    public String submitApplicationInfo(@RequestBody String params) {

        String industryName = "";
        String industryCode = "";
        String regionCode = "";
        String applyNo = "";
        String serialNo = "";
        String serviceObj = "";
        String formsInfo = "";
        String materialsInfo = "";
        Date applyDate;

        JSONObject dataJson = new JSONObject();
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject obj = (JSONObject) jsonObject.get("params");
            if (ZwdtConstant.SysValidateData.equals(token)) {

                log.info("=======开始调用submitApplicationInfo接口======="
                        + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));

                JSONObject applycationObject = obj.getJSONObject("applycation");
                JSONObject formObject = obj.getJSONObject("form");
                JSONArray materialObject = obj.getJSONArray("material");
                JSONArray univMaterialObject = obj.getJSONArray("univMaterial");

                industryName = applycationObject.getString("industryName");
                industryCode = applycationObject.getString("industryCode");
                regionCode = applycationObject.getString("regcode");
                applyNo = applycationObject.getString("applyNo");
                serialNo = applycationObject.getString("serialNo");
                applyDate = applycationObject.getDate("applyDate");
                JSONArray selectItem = applycationObject.getJSONArray("item");
                JSONObject applyerObject = formObject.getJSONObject("SUB_YYYZ_DWJBXX");

                BusinessLicenseBaseinfo baseinfo = new BusinessLicenseBaseinfo();
                baseinfo.setOperatedate(new Date());
                baseinfo.setRowguid(UUID.randomUUID().toString());
                baseinfo.setIndustryName(industryName);
                baseinfo.setIndustryCode(industryCode);
                baseinfo.setRegionCode(regionCode);
                baseinfo.setApplyNo(applyNo);
                baseinfo.setSerialNo(serialNo);
                baseinfo.setServiceObj("10");
                baseinfo.setApplydate(applyDate);
                baseinfo.setApplyername(applyerObject.getString("ENTNAME"));
                baseinfo.setOperatedate(new Date());
                baseinfoService.insert(baseinfo);

                BusinessLicenseExtension extension = new BusinessLicenseExtension();
                extension.setOperatedate(new Date());
                extension.setRowguid(UUID.randomUUID().toString());
                extension.setBaseinfoGuid(baseinfo.getRowguid());
                if (selectItem != null) {
                    extension.setSelectItem(selectItem.toJSONString());
                }
                if (materialObject != null) {
                    extension.setMaterialsInfo(materialObject.toJSONString());
                }
                if (univMaterialObject != null) {
                    extension.setUnivMaterial(univMaterialObject.toJSONString());
                }
                if (formObject != null) {
                    extension.setFormsInfo(formObject.toJSONString());
                }
                extensionService.insert(extension);

                // 引入接口api
                IAuditTask iAuditTask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
                IAuditSpIntegratedCompany iAuditSpIntegratedCompany = ContainerFactory.getContainInfo()
                        .getComponent(IAuditSpIntegratedCompany.class);
                IAuditSpBusiness iAuditSpBusiness = ContainerFactory.getContainInfo()
                        .getComponent(IAuditSpBusiness.class);
                IAuditSpInstance iAuditSpInstance = ContainerFactory.getContainInfo()
                        .getComponent(IAuditSpInstance.class);
                IAuditSpPhase iAuditSpPhase = ContainerFactory.getContainInfo().getComponent(IAuditSpPhase.class);
                IAuditSpISubapp iAuditSpISubapp = ContainerFactory.getContainInfo().getComponent(IAuditSpISubapp.class);
                IAuditSpTask iAuditSpTask = ContainerFactory.getContainInfo().getComponent(IAuditSpTask.class);
                IAuditSpITask iAuditSpITask = ContainerFactory.getContainInfo().getComponent(IAuditSpITask.class);
                IAuditSpIMaterial iAuditSpIMaterial = ContainerFactory.getContainInfo()
                        .getComponent(IAuditSpIMaterial.class);
                IHandleSPIMaterial iHandleSPIMaterial = ContainerFactory.getContainInfo()
                        .getComponent(IHandleSPIMaterial.class);

                // 初始化套餐
                // 2.1 保存基本信息
                AuditSpIntegratedCompany dataBean = new AuditSpIntegratedCompany();// 套餐式开公司实体对象
                String yewuGuid = UUID.randomUUID().toString().replace("-", "");
                dataBean.setRowguid(yewuGuid);
                dataBean.setOperatedate(new Date());
                dataBean.setOperateusername("一业一证对接");
                dataBean.setCompanyname(applyerObject.getString("ENTNAME"));// 企业名称--用户名称
                dataBean.setZzjgdmz(applyerObject.getString("UNISICID"));// 统一社会信用代码--证件号码
                dataBean.setContactperson(applyerObject.getString("LEREP"));// 联系人
                dataBean.setContactphone(applyerObject.getString("TEL"));// 联系电话--联系人电话
                dataBean.setCapital(null);// 注册资本（万元）
                dataBean.setLegalpersonemail("");// 法人Email
                dataBean.setContactpostcode("");// 邮编
                dataBean.setFundssource("");// 经费来源 选择
                dataBean.setAddress(applyerObject.getString("OPLOC"));// 经营场所地址
                dataBean.setCompanytype("");// 企业类型 选择
                dataBean.setScope("");// 经营范围
                dataBean.setLegalpersonduty("");// 法人职务 请选择
                iAuditSpIntegratedCompany.addAuditSpIntegratedCompany(dataBean);

                // 获取bussinessGuid
                String businessGuid = ConfigUtil.getConfigValue("businesslicense", industryCode);
                if (StringUtil.isBlank(businessGuid)) {
                    businessGuid = "7e5177b9-7f23-4457-ad32-604d18493b26";
                }
                // 2.2.1 生成主题实例信息
                AuditSpBusiness auditSpBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessGuid)
                        .getResult();

                String biGuid = iAuditSpInstance.addBusinessInstance(businessGuid, yewuGuid, "",
                        dataBean.getCompanyname(), "", auditSpBusiness.getBusinessname(), regionCode, "3").getResult();

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

                    iAuditSpISubapp.updateSubapp(subappGuid, ZwfwConstant.LHSP_Status_DYS, null);

                    //                        // 2.2.3 生成事项实例信息
                    //                        List<AuditSpTask> auditSpTasks = iAuditSpTask.getAllAuditSpTaskByPhaseguid(phaseGuid)
                    //                                .getResult();
                    //                        for (AuditSpTask auditSpTask : auditSpTasks) {
                    //                            AuditTask auditTask = iAuditTask.selectUsableTaskByTaskID(auditSpTask.getTaskid())
                    //                                    .getResult();
                    //                            iAuditSpITask.addTaskInstance(businessGuid, biGuid, phaseGuid, auditTask.getRowguid(),
                    //                                    auditTask.getTaskname(), subappGuid,
                    //                                    auditTask.getOrdernum() == null ? 0 : auditTask.getOrdernum());
                    //                        }
                    //
                    //                        // 2.2.4 生成材料实例信息
                    //                        iHandleSPIMaterial.initSubappMaterial(subappGuid, businessGuid, biGuid, phaseGuid, "", "");
                    //
                    //                        JSONObject dataInfoObj = null;
                    //                        JSONArray selectItemArray = JSONArray.parseArray(selectItem);
                    //                        initProject("", subappGuid, selectItemArray, "", biGuid, businessGuid, dataInfoObj,
                    //                                regionCode);
                }

                baseinfo.setBusinessGuid(businessGuid);
                baseinfo.setBiGuid(biGuid);
                baseinfoService.update(baseinfo);

                dataJson.put("state", "200");// 业务标识
                dataJson.put("error", "");// 业务标识
                dataJson.put("bizId", yewuGuid);// 业务标识
                dataJson.put("receiveId", serialNo);// 业务流水号

                log.info("=======结束调用submitApplicationInfo接口======="
                        + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                return dataJson.toJSONString();

            }
            else {
                dataJson.put("state", "300");// 业务标识
                dataJson.put("error", "token验证失败！");// 业务标识
                dataJson.put("bizId", "");// 业务标识
                dataJson.put("receiveId", serialNo);// 业务流水号

                return dataJson.toJSONString();
            }
        }
        catch (Exception e) {
            log.info("=======结束调用submitApplicationInfo接口======="
                    + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            log.info("=======submitApplicationInfo异常信息：======" + e.getMessage());
            return JsonUtils.zwdtRestReturn("0", "获取信息失败！" + e.getMessage(), "");
        }
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
        ITAHandleProject iTAHandleProject = ContainerFactory.getContainInfo().getComponent(ITAHandleProject.class);
        IAuditOrgaWindowYjs iAuditOrgaWindow = ContainerFactory.getContainInfo().getComponent(IAuditOrgaWindowYjs.class);
        // HandleElectricityForm handleForm = new HandleElectricityForm();

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
                            .InitIntegratedProject(task.getTaskguid(), "", "一业一证接口同步", "", windowGuid, windowName,
                                    centerGuid, biGuid, subappGuid, businessGuid, ZwfwConstant.APPLY_WAY_CKDJ)
                            .getResult();
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

}
