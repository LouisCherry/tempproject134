package com.epoint.zwdt.zwdtrest.project;

import java.lang.invoke.MethodHandles;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.xm.similarity.text.CosineSimilarity;
import org.xm.similarity.text.TextSimilarity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.authenticator.identity.Authenticator;
import com.epoint.authenticator.module.OAuthCheckTokenInfo;
import com.epoint.authenticator.utils.CheckTokenUtil;
import com.epoint.basic.auditflowsn.handleflowsn.inter.IHandleFlowSn;
import com.epoint.basic.auditonlineuser.auditcollection.inter.IAuditOnlineCollection;
import com.epoint.basic.auditonlineuser.auditonlineevaluat.domain.AuditOnlineEvaluat;
import com.epoint.basic.auditonlineuser.auditonlineevaluat.inter.IAuditOnlineEvaluat;
import com.epoint.basic.auditonlineuser.auditonlineindividual.domain.AuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineindividual.inter.IAuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlinemessages.inter.IAuditOnlineMessages;
import com.epoint.basic.auditonlineuser.auditonlineproject.domain.AuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineproject.inter.IAuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectdocsnap.domain.AuditProjectDocSnap;
import com.epoint.basic.auditproject.auditprojectdocsnap.inter.IAuditProjectDocSnap;
import com.epoint.basic.auditproject.auditprojectsparetime.domain.AuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
import com.epoint.basic.auditsp.auditspbasetask.domain.AuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetask.inter.IAuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetaskr.domain.AuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspbasetaskr.inter.IAuditSpBasetaskR;
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
import com.epoint.basic.auditsp.auditspselectedoption.domain.AuditSpSelectedOption;
import com.epoint.basic.auditsp.auditspselectedoption.inter.IAuditSpSelectedOptionService;
import com.epoint.basic.auditsp.auditspsharematerial.domain.AuditSpShareMaterial;
import com.epoint.basic.auditsp.auditspsharematerial.inter.IAuditSpShareMaterial;
import com.epoint.basic.auditsp.auditspsharematerialrelation.domain.AuditSpShareMaterialRelation;
import com.epoint.basic.auditsp.auditspsharematerialrelation.inter.IAuditSpShareMaterialRelation;
import com.epoint.basic.auditsp.auditspshareoption.api.IAuditSpShareoption;
import com.epoint.basic.auditsp.auditspshareoption.domain.AuditSpShareoption;
import com.epoint.basic.auditsp.auditsptask.domain.AuditSpTask;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
import com.epoint.basic.auditsp.audityjscsb.domain.AuditYjsCsb;
import com.epoint.basic.auditsp.audityjscsb.inter.IAuditYjsCsbService;
import com.epoint.basic.auditsp.element.domain.AuditSpElement;
import com.epoint.basic.auditsp.element.inter.IAuditSpElementService;
import com.epoint.basic.auditsp.option.domain.AuditSpOption;
import com.epoint.basic.auditsp.option.inter.IAuditSpOptionService;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.element.domain.AuditTaskElement;
import com.epoint.basic.audittask.element.inter.IAuditTaskElementService;
import com.epoint.basic.audittask.faq.domain.AuditTaskFaq;
import com.epoint.basic.audittask.faq.inter.IAuditTaskFaq;
import com.epoint.basic.audittask.fee.domain.AuditTaskChargeItem;
import com.epoint.basic.audittask.fee.inter.IAuditTaskChargeItem;
import com.epoint.basic.audittask.material.domain.AuditMaterialLibrary;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditMaterialLibrary;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.option.domain.AuditTaskOption;
import com.epoint.basic.audittask.option.inter.IAuditTaskOptionService;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.basic.certinfo.inter.ICertInfo;
import com.epoint.cert.basic.onlinecloud.cloudfiles.domain.AuditOnlineCloudFiles;
import com.epoint.cert.basic.onlinecloud.cloudfiles.inter.IAuditOnlineCloudFiles;
import com.epoint.cert.commonutils.SqlUtils;
import com.epoint.cert.external.ICertAttachExternal;
import com.epoint.cert.external.ICertInfoExternal;
import com.epoint.common.util.*;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.common.zwdtutil.ZwdtUserAuthValid;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.composite.auditsp.handlespimaterial.inter.IHandleSPIMaterial;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.json.JsonUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.web.WebUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.workflow.service.common.util.WorkflowKeyNames9;
import com.epoint.zwdt.zwdtrest.project.api.IJnProjectService;

/**
 * 套餐相关接口
 * 
 * @作者 xli
 * @version [F9.3, 2017年10月28日]
 */
@RestController
@RequestMapping("/zwdtBusiness")
public class AuditOnlineBusinessController
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    /**
     * 门户用户API
     */
    @Autowired
    private IAuditOnlineIndividual iAuditOnlineIndividual;
    /**
     * 网上用户
     */
    @Autowired
    private IAuditOnlineRegister iAuditOnlineRegister;
    /**
     * 主题API
     */
    @Autowired
    private IAuditSpBusiness iAuditSpBusiness;

    @Autowired
    private IJnProjectService iJnProjectService;
    /**
     * 阶段API
     */
    @Autowired
    private IAuditSpPhase iAuditSpPhase;
    /**
     * 阶段事项API
     */
    @Autowired
    private IAuditSpTask iAuditSpTask;
    /**
     * 关联事项API
     */
    @Autowired
    private IAuditSpBasetaskR iAuditSpBasetaskR;
    /**
     * 事项API
     */
    @Autowired
    private IAuditTask iAuditTask;
    /**
     * 事项材料API
     */
    @Autowired
    private IAuditTaskMaterial iAuditTaskMaterial;
    /**
     * 事项收费API
     */
    @Autowired
    private IAuditTaskChargeItem iAuditTaskChargeItem;
    /**
     * 事项常见问题API
     */
    @Autowired
    private IAuditTaskFaq iAuditTaskFaq;
    /**
     * 代码项API
     */
    @Autowired
    private ICodeItemsService iCodeItemsService;
    /**
     * 附件API
     */
    @Autowired
    private IAttachService iAttachService;
    /**
     * 主题共享材料API
     */
    @Autowired
    private IAuditSpShareMaterial iAuditSpShareMaterial;
    /**
     * 主题实例API
     */
    @Autowired
    private IAuditSpInstance iAuditSpInstance;
    /**
     * 子申报API
     */
    @Autowired
    private IAuditSpISubapp iAuditSpISubapp;
    /**
     * 套餐式开公司API
     */
    @Autowired
    private IAuditSpIntegratedCompany iAuditSpIntegratedCompany;
    /**
     * 阶段事项实例API
     */
    @Autowired
    private IAuditSpITask iAuditSpITask;
    /**
     * 材料API
     */
    @Autowired
    private IHandleSPIMaterial iHandleSPIMaterial;
    /**
     * 材料API
     */
    @Autowired
    private IAuditSpIMaterial iAuditSpIMaterial;
    /**
     * 共享材料关系API
     */
    @Autowired
    private IAuditSpShareMaterialRelation iAuditSpShareMaterialRelation;
    /**
     * 办件API
     */
    @Autowired
    private IAuditOnlineProject iAuditOnlineProject;
    /**
     * 办件操作API
     */
    @Autowired
    private IHandleProject iHandleProject;
    /**
     * 办件API
     */
    @Autowired
    private IAuditProject iAuditProject;
    /**
     * 办件文书快照API
     */
    @Autowired
    private IAuditProjectDocSnap iAuditProjectDocSnap;
    /**
     * 流水号API
     */
    @Autowired
    private IHandleFlowSn iHandleFlowSn;
    /**
     * 评价API
     */
    @Autowired
    private IAuditOnlineEvaluat iAuditOnlineEvaluat;
    /**
     * 政务大厅收藏API
     */
    @Autowired
    private IAuditOnlineCollection iAuditOnlineCollection;

    /**
     * 一件事API
     */
    @Autowired
    private IAuditYjsCsbService iAuditYjsCsbService;
    /**
     * 微信消息推送相关API
     */
    @Autowired
    private IAuditOnlineMessages iAuditOnlineMessages;

    /**
     * 事项情形API
     */
    @Autowired
    private IAuditSpElementService iAuditSpElementService;

    /**
     * 情形选项API
     */
    @Autowired
    private IAuditSpOptionService iAuditSpOptionService;

    /**
     * 办件剩余时间API
     */
    @Autowired
    private IAuditProjectSparetime iAuditProjectSparetime;

    @Autowired

    private IAuditMaterialLibrary iAuditMaterialLibrary;

    /**
     * 政务云盘文件API
     */
    @Autowired
    private IAuditOnlineCloudFiles iAuditOnlineCloudFiles;
    /**
     * 证照实例API
     */
    @Autowired
    private ICertInfo iCertInfo;
    /**
     * 证照基本信息API
     */
    @Autowired
    private ICertInfoExternal iCertInfoExternal;

    /**
     * 证照库附件API
     */
    @Autowired
    private ICertAttachExternal iCertAttachExternal;

    @Autowired
    private IAuditSpIMaterial iauditspimaterial;

    @Autowired
    private IAuditSpITask auditSpITaskService;
    @Autowired
    private IAuditTask iaudittask;
    @Autowired
    private IHandleSPIMaterial ihandlespimaterial;
    /**
     * 问卷相关api
     */
    @Autowired
    private IAuditSpShareoption iauditspshareoption;

    @Autowired
    private IAuditTaskElementService iaudittaskelementservice;
    @Autowired
    private IAuditSpSelectedOptionService iAuditSpSelectedOptionService;
    @Autowired
    private IAuditTaskOptionService iaudittaskoptionservice;
    /**
     * 标准事项api
     */
    @Autowired
    private IAuditSpBasetask iAuditSpBasetask;

    /**
     * 系统参数API
     */
    @Autowired
    private IConfigService iConfigService;

    @Autowired
    private IAuditOrgaArea iAuditorgaAtra;

    /**
     * 获取套餐列表接口(套餐列表页面调用)
     * 
     * @param params
     *            传入的参数
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getBusinessList", method = RequestMethod.POST)
    public String getBusinessList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getBusinessList接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 1、接口的入参转化为JSON对象
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取辖区编码
                String areaCode = obj.getString("areacode");
                // 1.2、获取套餐类型
                String businessType = obj.getString("businesstype");
                // 1.3、获取当前页
                String currentPage = obj.getString("currentpage");
                // 1.4、获取分页数量
                String pageSize = obj.getString("pagesize");
                // 2、查询辖区下启用的主题
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.eq("BUSINESSTYPE", "2"); // 主题分类：一般并联审批（套餐）
                sqlConditionUtil.eq("DEL", "0"); // 主题状态：启用
                sqlConditionUtil.eq("AREACODE", areaCode); // 辖区编码
                sqlConditionUtil.setOrderDesc("ORDERNUMBER"); // 按排序字段降序
                if (StringUtil.isNotBlank(businessType)) {
                    sqlConditionUtil.eq("BUSINESSKIND", businessType); // 辖区编码
                }
                log.info("=======查询条件=======" + sqlConditionUtil.getMap());
                PageData<AuditSpBusiness> auditSpBusinessesPage = iAuditSpBusiness.getAuditSpBusinessByPage(
                        sqlConditionUtil.getMap(), Integer.parseInt(currentPage) * Integer.parseInt(pageSize),
                        Integer.parseInt(pageSize), "", "").getResult();
                List<AuditSpBusiness> auditSpBusinesses = auditSpBusinessesPage.getList();
                // 3、定义返回的主题数据
                List<JSONObject> businessJsonList = new ArrayList<JSONObject>();
                for (AuditSpBusiness auditSpBusiness : auditSpBusinesses) {
                    List<AuditSpTask> auditSpTaskList = iAuditSpTask
                            .getAllAuditSpTaskByBusinessGuid(auditSpBusiness.getRowguid()).getResult();
                    if (auditSpTaskList.size() > 0) {
                        JSONObject bussinessJson = new JSONObject();
                        bussinessJson.put("businessguid", auditSpBusiness.getRowguid()); // 主题标识
                        bussinessJson.put("businessname", auditSpBusiness.getBusinessname());// 主题名称
                        bussinessJson.put("note", auditSpBusiness.getNote()); // 主题说明
                        bussinessJson.put("iswssb", auditSpBusiness.getIswssb());// 是否允许网上申报
                        // 套餐背景图
                        bussinessJson.put("imgclass", auditSpBusiness.getStr("backgroundimg"));// 图标
                        businessJsonList.add(bussinessJson);
                    }

                }
                // 6、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("totalcount", auditSpBusinessesPage.getRowCount());
                dataJson.put("businessList", businessJsonList);
                log.info("=======结束调用getBusinessList接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取套餐式申报列表成功", dataJson.toString());
            }
            else {
                log.info("=======结束调用getBusinessList接口=======");
                return JsonUtils.zwdtRestReturn("0", "身份验证失败", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "获取套餐式申报列表失败：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/getBusinessAreaList", method = RequestMethod.POST)
    public String getBusinessAreaList(@RequestBody String params) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            // 1.1、获取事项编码
            String areacode = obj.getString("areacode");
            String businessguid = obj.getString("businessguid");
            AuditOrgaArea orgarea = iAuditorgaAtra.getAreaByAreacode(areacode).getResult();
            String areaname = orgarea.getXiaquname();
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.setOrder("ordernum", "desc");
            sql.in("citylevel", "1,2");
            List<AuditOrgaArea> list = iAuditorgaAtra.selectAuditAreaList(sql.getMap()).getResult();
            List<JSONObject> jsonList = new ArrayList<>();
            AuditSpBusiness business = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessguid).getResult();

            List<String> areaList = list.stream().map(AuditOrgaArea::getXiaqucode).collect(Collectors.toList());

            // business.setAreacode("370890");
            for (AuditOrgaArea area : list) {
                if ("车管所".equals(area.getXiaquname())) {
                    continue;
                }
                JSONObject json = new JSONObject();
                json.put("areacode", area.getXiaqucode());
                json.put("areaname", area.getXiaquname());
                if (business != null && !business.isEmpty()) {
                    if (StringUtil.isNotBlank(business.getAreacode()) && business.getAreacode().endsWith("00")) {
                        json.put("style", "color:#0085C3;");
                    }
                    else if (areaList.contains(business.getAreacode())) {
                        if (area.getXiaqucode().equals(business.getAreacode())) {
                            json.put("style", "color:#0085C3;");
                        }
                        else {
                            json.put("style", "cursor: not-allowed;color:#666666;");
                        }
                    }
                }
                else {
                    json.put("style", "cursor: not-allowed;color:#666666;");
                }

                jsonList.add(json);
            }
            JSONObject returnJson = new JSONObject();
            returnJson.put("items", jsonList);
            returnJson.put("areaname", areaname);
            return JsonUtils.zwdtRestReturn("1", "获取辖区事项列表成功", returnJson.toString());

        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "获取辖区事项列表异常", "");
        }
    }

    @RequestMapping(value = "/getYjsBusinessAreaList", method = RequestMethod.POST)
    public String getYjsBusinessAreaList(@RequestBody String params) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            JSONObject obj = (JSONObject) jsonObject.get("params");
            // 1.1、获取事项编码
            String areacode = obj.getString("areacode");
            if (StringUtil.isBlank(areacode)) {
                areacode = "370800";
            }

            if ("370801".equals(areacode)) {
                areacode = "370800";
            }
            String businessguid = obj.getString("businessguid");
            AuditOrgaArea orgarea = iAuditorgaAtra.getAreaByAreacode(areacode).getResult();
            String areaname = orgarea.getXiaquname();

            List<AuditOrgaArea> list = iJnProjectService.selectAuditAreaList();
            List<JSONObject> jsonList = new ArrayList<>();
            AuditSpBusiness business = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessguid).getResult();

            // business.setAreacode("370890");
            for (AuditOrgaArea area : list) {
                if ("车管所".equals(area.getXiaquname())) {
                    continue;
                }
                SqlConditionUtil sqlnew = new SqlConditionUtil();
                sqlnew.eq("businessname", business.getBusinessname());
                sqlnew.eq("del", "0");
                sqlnew.eq("areacode", area.getXiaqucode());
                AuditSpBusiness businessnew = iJnProjectService.getAuditBusinessByName(business.getBusinessname(),
                        area.getXiaqucode());

                if (businessnew != null) {
                    // 1.1、获取套餐标识
                    String businessGuid = businessnew.getRowguid();
                    JSONObject json = new JSONObject();
                    // 2、调用接口查询套餐下情形
                    SqlConditionUtil sqlutil = new SqlConditionUtil();
                    // 根节点问题
                    sqlutil.rightLike("preoptionguid", "start");
                    if (StringUtil.isNotBlank(businessGuid)) {
                        sqlutil.eq("businessguid", businessGuid);
                    }

                    sqlutil.setOrder("ordernum desc,operatedate", "asc");
                    // 要素
                    List<AuditSpElement> auditSpElements = iAuditSpElementService.findListByCondition(sqlutil.getMap())
                            .getResult();

                    if (auditSpElements.isEmpty()) {
                        json.put("haselement", "0");
                    }
                    else {
                        json.put("haselement", "1");
                    }

                    json.put("areacode", area.getXiaqucode());
                    json.put("areaname", area.getXiaquname());
                    json.put("businessguid", businessnew.getRowguid());
                    json.put("businessname", businessnew.getBusinessname());
                    if (StringUtil.isNotBlank(businessnew.getStr("thirdurl"))) {
                        json.put("thirdurl", businessnew.getStr("thirdurl"));
                    }
                    else {
                        json.put("thirdurl", "0");
                    }

                    json.put("subappguid", UUID.randomUUID().toString());
                    json.put("style", "color:#0085C3;");

                    json.put("isggyjs", businessnew.getStr("isggyjs")); // 是否工改一件事
                    json.put("phaseid", businessnew.getStr("phaseid")); // 跳转阶段
                    List<AuditSpPhase> phaselist = iAuditSpPhase.getSpPaseByBusinedssguid(businessnew.getRowguid()).getResult();
                    if(phaselist.size()>0&&!phaselist.isEmpty()){
                        AuditSpPhase phase = phaselist.get(0);
                        json.put("phaseguid",phase.getRowguid());
                    }
                    jsonList.add(json);
                }

            }
            JSONObject returnJson = new JSONObject();
            // 排序 按地区层级排序
            if (jsonList != null && !jsonList.isEmpty()) {
                jsonList = jsonList.stream().sorted(Comparator.comparing(e -> e.getLong("areacode")))
                        .collect(Collectors.toList());
            }
            returnJson.put("items", jsonList);
            returnJson.put("areaname", areaname);
            return JsonUtils.zwdtRestReturn("1", "获取辖区事项列表成功", returnJson.toString());

        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "获取辖区事项列表异常", "");
        }
    }

    /**
     * 获取套餐详细信息接口（套餐查看详情时调用）
     * 
     * @param params
     *            传入参数
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getBusinessInfo", method = RequestMethod.POST)
    public String getBusinessInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getBusinessInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取主题标识
                String businessguid = obj.getString("businessguid");
                // 1.2、获取用户信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                // 2、获取主题基本信息
                AuditSpBusiness auditSpBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessguid)
                        .getResult();
                if (auditSpBusiness != null) {
                    // 3、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("businessguid", businessguid);// 主题标识
                    dataJson.put("businessname", auditSpBusiness.getBusinessname());// 主题名称
                    dataJson.put("note", auditSpBusiness.getNote());// 主题说明
                    dataJson.put("businessurl", WebUtil.getRequestCompleteUrl(request)
                            + "/epointzwmhwz/pages/eventdetail/spintegratedetail?businessguid=" + businessguid);// 二维码内容
                    dataJson.put("promiseday", (StringUtil.isBlank(auditSpBusiness.getPromise_day()) ? "0"
                            : auditSpBusiness.getPromise_day().toString()) + "个工作日");// 承诺期限
                    dataJson.put("linktel", auditSpBusiness.getLink_tel());// 咨询电话
                    dataJson.put("supervisetel", auditSpBusiness.getSupervise_tel());// 投诉电话
                    dataJson.put("handleaddress", auditSpBusiness.getTransact_addr());// 办公地点
                    dataJson.put("handletime", auditSpBusiness.getTransact_time());// 办公时间
                    dataJson.put("iswssb", auditSpBusiness.getIswssb());// 是否允许网上申报
                    String handleaddress = auditSpBusiness.getTransact_addr();
                    String handletime = auditSpBusiness.getTransact_time();
                    dataJson.put("addressandtime", handleaddress + " " + handletime);// 办公时间和地点
                    dataJson.put("daoxcnum",
                            StringUtil.isBlank(auditSpBusiness.getDao_xc_num()) ? ""
                                    : iCodeItemsService.getItemTextByCodeName("现场次数",
                                            String.valueOf(auditSpBusiness.getDao_xc_num())));// 现场次数
                    String taskoutimg = ""; // 办理流程图地址
                    String taskOutImgGuid = auditSpBusiness.getTaskoutimgguid();
                    if (StringUtil.isNotBlank(taskOutImgGuid)) {
                        FrameAttachStorage frameAttachStorage = iAttachService.getAttach(taskOutImgGuid);
                        if (frameAttachStorage != null) {
                            taskoutimg = WebUtil.getRequestCompleteUrl(request)
                                    + "/rest/auditattach/readAttach?attachguid=" + frameAttachStorage.getAttachGuid();
                        }
                    }
                    dataJson.put("taskoutimg", taskoutimg);
                    // 套餐背景图
                    String taskTCoutImgGuid = auditSpBusiness.getTaskoutimgguid();
                    String taskTCoutImg = ""; // 办理流程图地址
                    if (StringUtil.isNotBlank(taskTCoutImgGuid)) {
                        FrameAttachInfo attachInfoDetail = iAttachService.getAttachInfoDetail(taskTCoutImgGuid);
                        if (attachInfoDetail != null) {
                            taskTCoutImg = WebUtil.getRequestCompleteUrl(request)
                                    + "/rest/zwdtAttach/readAttach?attachguid=" + attachInfoDetail.getAttachGuid()
                                    + "&attachto=blsp";
                        }
                    }
                    dataJson.put("tasktcoutimg", taskTCoutImg);
                    if (auditOnlineRegister != null) {
                        if (isCollect(businessguid, auditOnlineRegister.getAccountguid())) {
                            dataJson.put("iscollect", "1");// 返回该套餐已收藏标识
                        }
                    }
                    dataJson.put("acceptcondition", auditSpBusiness.getStr("acceptcondition")); // 受理条件
                    log.info("=======结束调用getBusinessInfo接口=======");
                    return JsonUtils.zwdtRestReturn("1", "主题基本信息获取成功", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "主题套餐不存在！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getBusinessInfo接口参数：params【" + params + "】=======");
            log.info("=======getBusinessInfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "主题基本信息获取失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取套餐相关扩展信息，收费项目、常见问题、设定依据（查看套餐详情时调用）
     * 
     * @param params
     *            请求参数
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getBusinessExtendInfo", method = RequestMethod.POST)
    public String getBusinessExtendInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getBusinessExtendInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取主题标识
                String businessGuid = obj.getString("businessguid");
                // 2、获取主题信息
                AuditSpBusiness auditSpBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessGuid)
                        .getResult();
                if (auditSpBusiness != null) {
                    // 3、获取主题下第一个阶段的信息
                    AuditSpPhase auditSpPhase = iAuditSpPhase.getSpPaseByBusinedssguid(auditSpBusiness.getRowguid())
                            .getResult().get(0);
                    if (auditSpPhase != null) {
                        // 4、获取阶段下配置的事项信息
                        List<AuditSpTask> auditSpTaskList = iAuditSpTask
                                .getAllAuditSpTaskByPhaseguid(auditSpPhase.getRowguid()).getResult();
                        // 5、定义返回JSON对象
                        JSONObject dataJson = new JSONObject();
                        List<JSONObject> settingList = new ArrayList<JSONObject>(); // 法律依据
                        List<JSONObject> taskChargeItemList = new ArrayList<JSONObject>(); // 收费项目
                        List<JSONObject> taskfaqList = new ArrayList<JSONObject>(); // 常见问题
                        for (AuditSpTask auditSpTask : auditSpTaskList) {
                            // 查询并遍历标准事项关联关系表
                            SqlConditionUtil sql = new SqlConditionUtil();
                            sql.eq("basetaskguid", auditSpTask.getStr("basetaskguid"));
                            List<AuditSpBasetaskR> listr = iAuditSpBasetaskR
                                    .getAuditSpBasetaskrByCondition(sql.getMap()).getResult();
                            for (AuditSpBasetaskR auditSpBasetaskR : listr) {
                                AuditTask auditTask = iAuditTask.selectUsableTaskByTaskID(auditSpBasetaskR.getTaskid())
                                        .getResult();

                                if (auditTask != null) {
                                    // 6.1、获取阶段下各个事项的收费标准数据
                                    List<AuditTaskChargeItem> auditTaskChargeItems = iAuditTaskChargeItem
                                            .selectAuditTaskChargeItemByTaskGuid(auditTask.getRowguid(), true)
                                            .getResult();
                                    for (AuditTaskChargeItem auditTaskChargeItem : auditTaskChargeItems) {
                                        JSONObject chargeItemJson = new JSONObject();
                                        chargeItemJson.put("chargeitemname", auditTaskChargeItem.getItemname());// 收费项目名称
                                        chargeItemJson.put("chargeitemguid", auditTaskChargeItem.getRowguid());// 收费项目标识
                                        chargeItemJson.put("unit", auditTaskChargeItem.getUnit());// 单位
                                        chargeItemJson.put("chargeitemstand",
                                                auditTaskChargeItem.getChargeitem_stand());// 收费标准
                                        taskChargeItemList.add(chargeItemJson);
                                    }
                                    // 6.2、获取阶段下各个事项的常见问题
                                    List<AuditTaskFaq> auditTaskFaqs = iAuditTaskFaq
                                            .selectAuditTaskFaqByTaskId(auditTask.getTask_id()).getResult();
                                    if (auditTaskFaqs != null && auditTaskFaqs.size() > 0) {
                                        Collections.sort(auditTaskFaqs, new Comparator<AuditTaskFaq>() // 对常见问题进行倒序排列
                                        {
                                            @Override
                                            public int compare(AuditTaskFaq b1, AuditTaskFaq b2) {
                                                Integer faqsOrder1 = b1.getOrdernum() == null ? 0 : b1.getOrdernum();
                                                Integer faqsOrder2 = b2.getOrdernum() == null ? 0 : b2.getOrdernum();
                                                int order = faqsOrder2.compareTo(faqsOrder1);
                                                return order;
                                            }
                                        });
                                        for (AuditTaskFaq auditTaskFaq : auditTaskFaqs) {
                                            JSONObject faqJson = new JSONObject();
                                            faqJson.put("question", auditTaskFaq.getQuestion());// 问题
                                            faqJson.put("answer", auditTaskFaq.getAnswer());// 回答
                                            taskfaqList.add(faqJson);
                                        }
                                    }
                                    else {
                                        JSONObject faqJson = new JSONObject();
                                        faqJson.put("question", "无");// 问题
                                    }
                                    // 6.3、获取阶段下各个事项的设定依据
                                    JSONObject settingJson = new JSONObject();
                                    settingJson.put("setting",
                                            StringUtil.isBlank(auditTask.getBy_law()) ? "" : auditTask.getBy_law());// 设定依据
                                    settingList.add(settingJson);
                                }
                            }

                        }
                        dataJson.put("chargeitemsList", taskChargeItemList);
                        dataJson.put("faqsList", taskfaqList);
                        dataJson.put("settingList", settingList);
                        log.info("=======结束调用getBusinessExtendInfo接口=======");
                        return JsonUtils.zwdtRestReturn("1", "主题下事项信息获取成功", dataJson.toString());
                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", "此套餐阶段查询失败！", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "此套餐不存在！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getBusinessExtendInfo接口参数：params【" + params + "】=======");
            log.info("=======getBusinessExtendInfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "主题下事项信息获取失败：" + e.getMessage(), "");
        }
    }

    /**
     * 保存套餐基本信息接口(套餐申报页面点击保存按钮时调用)
     * 
     * @param params
     *            请求参数
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/saveBusinessInfo", method = RequestMethod.POST)
    public String saveBusinessInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用saveBusinessInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取企业名称
                String companyName = obj.getString("companyname");
                // 1.2、获取组织机构代码证
                String zzjgdmz = obj.getString("zzjgdmz");
                // 1.3、获取联系人
                String contactPerson = obj.getString("contactperson");
                // 1.4、获取注册资本（万元）
                String capital = obj.getString("capital");
                // 1.5、获取联系电话
                String contactPhone = obj.getString("contactphone");
                // 1.6、获取邮编
                String contactPostCode = obj.getString("contactpostcode");
                // 1.7、获取地址
                String address = obj.getString("address");
                // 1.8、法人Email
                String legalPersonEmail = obj.getString("legalpersonemail");
                // 1.9、获取经营范围
                String scope = obj.getString("scope");
                // 1.10、获取业务标识
                String yewuGuid = obj.getString("yewuguid");
                // 1.11、获取套餐主题标识
                String businessGuid = obj.getString("businessguid");
                // 1.12、获取辖区编码
                String areaCode = obj.getString("areacode");
                // 1.13、获取企业类型
                String companyType = obj.getString("companytype");
                // 1.14、获取资金来源
                String fundsSource = obj.getString("fundssource");
                // 1.15、获取法人类型
                String legalPersonDuty = obj.getString("legalpersonduty");
                // 1.16、主题实例标识
                String biGuid = obj.getString("biguid");
                // 1.17、主题实例标识
                String subAppGuid = obj.getString("subappguid");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 2.1、获取用户个人信息
                    AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
                            .getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
                    // 3、获取主题基本信息
                    AuditSpBusiness auditSpBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessGuid)
                            .getResult();
                    if (auditSpBusiness != null) {
                        // 3.1、申报时以套餐配置的辖区为准，如果为空则用当前的辖区编码
                        areaCode = StringUtil.isNotBlank(auditSpBusiness.getAreacode()) ? auditSpBusiness.getAreacode()
                                : areaCode;
                        // 4、保存或更新企业基本信息
                        AuditSpIntegratedCompany auditSpIntegratedCompany = iAuditSpIntegratedCompany
                                .getAuditSpIntegratedCompanyByGuid(yewuGuid).getResult();
                        if (auditSpIntegratedCompany == null) {
                            // 4.1、若不存在企业信息则新增企业数据
                            auditSpIntegratedCompany = new AuditSpIntegratedCompany();
                            auditSpIntegratedCompany.setRowguid(yewuGuid);
                            auditSpIntegratedCompany.setOperatedate(new Date());
                            auditSpIntegratedCompany.setCompanyname(companyName); // 企业名称
                            auditSpIntegratedCompany.setZzjgdmz(zzjgdmz); // 组织结构代码证
                            auditSpIntegratedCompany.setContactperson(contactPerson); // 联系人
                            if (StringUtil.isNotBlank(capital)) {
                                auditSpIntegratedCompany.setCapital(Double.parseDouble(capital)); // 注册资本
                            }
                            else {
                                auditSpIntegratedCompany.setCapital(null);
                            }
                            auditSpIntegratedCompany.setContactcertnum(auditOnlineRegister.getIdnumber());
                            auditSpIntegratedCompany.setContactphone(contactPhone);// 联系电话
                            auditSpIntegratedCompany.setContactpostcode(contactPostCode); // 邮编
                            auditSpIntegratedCompany.setAddress(address); // 地址
                            auditSpIntegratedCompany.setLegalpersonemail(legalPersonEmail); // 法人Email
                            auditSpIntegratedCompany.setScope(scope); // 经营范围
                            auditSpIntegratedCompany.setCompanytype(companyType); // 企业类型
                            auditSpIntegratedCompany.setFundssource(fundsSource); // 资金来源
                            auditSpIntegratedCompany.setLegalpersonduty(legalPersonDuty); // 法人类型
                            iAuditSpIntegratedCompany.addAuditSpIntegratedCompany(auditSpIntegratedCompany);
                            // 4.2、初始化实例信息
                            // 4.2.1、生成主题实例信息
                            biGuid = iAuditSpInstance.addBusinessInstanceAndBiguid(biGuid, businessGuid, yewuGuid,
                                    auditOnlineIndividual.getApplyerguid(), companyName, ZwfwConstant.APPLY_WAY_NETSBYS,
                                    auditSpBusiness.getBusinessname(), areaCode, auditSpBusiness.getBusinesstype())
                                    .getResult();
                            // 4.2.2、生成子申报信息
                            String phaseGuid = "";
                            List<AuditSpPhase> auditSpPhases = iAuditSpPhase.getSpPaseByBusinedssguid(businessGuid)
                                    .getResult();
                            if (auditSpPhases != null && auditSpPhases.size() > 0) {
                                phaseGuid = auditSpPhases.get(0).getRowguid();
                            }
                            AuditSpISubapp auditSpISubapp = new AuditSpISubapp();
                            if (StringUtil.isBlank(subAppGuid)) {
                                subAppGuid = UUID.randomUUID().toString();
                            }
                            auditSpISubapp.setApplyerguid(auditOnlineIndividual.getApplyerguid());
                            auditSpISubapp.setApplyername(companyName);
                            auditSpISubapp.setApplyerway(ZwfwConstant.APPLY_WAY_NETSBYS);
                            auditSpISubapp.setBiguid(biGuid);
                            auditSpISubapp.setBusinessguid(businessGuid);
                            auditSpISubapp.setCreatedate(new Date());
                            auditSpISubapp.setRowguid(subAppGuid);
                            auditSpISubapp.setPhaseguid(phaseGuid);
                            auditSpISubapp.setStatus(ZwfwConstant.LHSP_Status_DPS);
                            auditSpISubapp.setSubappname(companyName);
                            auditSpISubapp.setInitmaterial(ZwfwConstant.CONSTANT_STR_ONE);
                            /*
                             * subAppGuid = iAuditSpISubapp.addSubapp(auditSpISubapp). getResult();
                             */
                            iAuditSpISubapp.addSubapp(auditSpISubapp).getResult();
                            // 4.2.3、生成事项实例信息
                            List<AuditSpTask> auditSpTasks = iAuditSpTask.getAllAuditSpTaskByPhaseguid(phaseGuid)
                                    .getResult();
                            List<AuditSpITask> auditSpITasks = iAuditSpITask.getSpITaskByBIGuid(biGuid).getResult();
                            if (auditSpITasks.size() == 0) {
                                for (AuditSpTask auditSpTask : auditSpTasks) {
                                    // 查询并遍历标准事项关联关系表
                                    SqlConditionUtil sql = new SqlConditionUtil();
                                    sql.eq("basetaskguid", auditSpTask.getStr("basetaskguid"));
                                    List<AuditSpBasetaskR> listr = iAuditSpBasetaskR
                                            .getAuditSpBasetaskrByCondition(sql.getMap()).getResult();
                                    for (AuditSpBasetaskR auditSpBasetaskR : listr) {
                                        AuditTask auditTask = iAuditTask
                                                .selectUsableTaskByTaskID(auditSpBasetaskR.getTaskid()).getResult();
                                        if (auditTask != null) {
                                            iAuditSpITask.addTaskInstance(businessGuid, biGuid, phaseGuid,
                                                    auditTask.getRowguid(), auditTask.getTaskname(), subAppGuid,
                                                    auditTask.getOrdernum() == null ? 0 : auditTask.getOrdernum());
                                        }
                                    }
                                }
                            }

                            // 4.2.4、 初始化套餐式办件信息
                            iHandleProject.InitOnlineProjectForBusiness(businessGuid,
                                    auditOnlineIndividual.getApplyerguid(), areaCode, companyName, biGuid, subAppGuid,
                                    yewuGuid);
                        }
                        else {
                            // 获取套餐实例
                            AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByYeWuGuid(yewuGuid)
                                    .getResult();
                            if (auditSpInstance != null) {
                                if (!auditSpInstance.getApplyerguid().equals(auditOnlineIndividual.getApplyerguid())) {
                                    return JsonUtils.zwdtRestReturn("0", "UserValid身份验证失败！", "");
                                }
                            }
                            // 5、如果已存在企业基本信息，更新基本信息
                            auditSpIntegratedCompany.setRowguid(yewuGuid);
                            auditSpIntegratedCompany.setOperatedate(new Date());
                            auditSpIntegratedCompany.setCompanyname(companyName); // 企业名称
                            auditSpIntegratedCompany.setZzjgdmz(zzjgdmz); // 组织结构代码证
                            auditSpIntegratedCompany.setContactperson(contactPerson); // 联系人
                            if (StringUtil.isNotBlank(capital)) {
                                auditSpIntegratedCompany.setCapital(Double.parseDouble(capital)); // 注册资本
                            }
                            else {
                                auditSpIntegratedCompany.setCapital(null);
                            }
                            auditSpIntegratedCompany.setContactcertnum(auditOnlineRegister.getIdnumber());
                            auditSpIntegratedCompany.setContactphone(contactPhone);// 联系电话
                            auditSpIntegratedCompany.setContactpostcode(contactPostCode); // 邮编
                            auditSpIntegratedCompany.setAddress(address); // 地址
                            auditSpIntegratedCompany.setLegalpersonemail(legalPersonEmail); // 法人Email
                            auditSpIntegratedCompany.setScope(scope); // 经营范围
                            auditSpIntegratedCompany.setCompanytype(companyType); // 企业类型
                            auditSpIntegratedCompany.setFundssource(fundsSource); // 资金来源
                            auditSpIntegratedCompany.setLegalpersonduty(legalPersonDuty); // 法人类型
                            iAuditSpIntegratedCompany.updateAuditSpIntegratedCompany(auditSpIntegratedCompany);
                            // 5.1、更新套餐实例的申请人信息
                            if (auditSpInstance != null) {
                                auditSpInstance.setApplyername(companyName);
                                iAuditSpInstance.updateAuditSpInstance(auditSpInstance);
                                // 5.2、更新子申报申请人信息
                                AuditSpISubapp auditSpISubapp = iAuditSpISubapp
                                        .getSubappByBIGuid(auditSpInstance.getRowguid()).getResult().get(0);
                                auditSpISubapp.setSubappname(companyName);
                                auditSpISubapp.setApplyername(companyName);
                                iAuditSpISubapp.updateAuditSpISubapp(auditSpISubapp);
                                // 5.3、更新AUDIT_ONLINE_PROJECT办件申请人信息
                                Map<String, String> updateFieldMap = new HashMap<String, String>(16);
                                Map<String, String> updateDateFieldMap = new HashMap<String, String>(16);
                                SqlConditionUtil conditionsql = new SqlConditionUtil();
                                updateFieldMap.put("applyername=", companyName);
                                updateDateFieldMap.put("applydate=",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                                conditionsql.eq("sourceguid", auditSpInstance.getRowguid());
                                iAuditOnlineProject.updateOnlineProject(updateFieldMap, updateDateFieldMap,
                                        conditionsql.getMap());
                            }
                        }
                    }
                }
                JSONObject dataJson = new JSONObject();
                dataJson.put("biGuid", biGuid);
                dataJson.put("subappguid", subAppGuid);

                log.info("=======结束调用saveBusinessInfo接口=======");
                return JsonUtils.zwdtRestReturn("1", "保存成功！", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "主题申报基本信息保存异常：" + e.getMessage(), "");
        }
    }

    /**
     * 提交套餐办件接口（套餐申报提交调用）
     *
     * @param params
     *            请求参数
     * @param request
     *            HTTP请求
     * @return
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/private/submitBusinessInfo", method = RequestMethod.POST)
    public String submitBusinessInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用submitBusinessInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取主题标识
                String businessGuid = obj.getString("businessguid");
                // 1.2、获取业务标识
                String yewuGuid = obj.getString("yewuguid");
                // 1.3、获取辖区标识
                String areacCode = obj.getString("areacode");
                // 1.4、获取子申报标识
                String subAppGuid = obj.getString("subappguid");
                // 1.5、获取主题实例标识
                String biGuid = obj.getString("biguid");
                // 1.6、获取阶段标识
                String phaseGuid = obj.getString("phaseguid");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 2、获取主题实例信息
                    AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByYeWuGuid(yewuGuid).getResult();
                    if (StringUtil.isBlank(biGuid)) {
                        biGuid = auditSpInstance.getRowguid();
                    }
                    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理开始<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    // 3、获取用户注册信息
                    ZwdtUserAuthValid zwdtUserAuthValid = new ZwdtUserAuthValid();
                    if (!zwdtUserAuthValid.userValid(auditOnlineRegister, biGuid, ZwdtConstant.USERVALID_SPI_INS)) {
                        return JsonUtils.zwdtRestReturn("0", "UserValid身份验证失败！", "");
                    }
                    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    if (StringUtil.isBlank(subAppGuid)) {
                        List<AuditSpISubapp> auditSpISubapps = iAuditSpISubapp.getSubappByBIGuid(biGuid).getResult();
                        if (!auditSpISubapps.isEmpty()) {
                            AuditSpISubapp auditSpISubapp = auditSpISubapps.get(0);
                            subAppGuid = auditSpISubapp.getRowguid();
                            phaseGuid = auditSpISubapp.getPhaseguid();
                        }
                    }
                    // 4、根据子申报标识获取材料实例信息
                    List<AuditSpIMaterial> auditSpIMaterials = iAuditSpIMaterial.getSpIMaterialBySubappGuid(subAppGuid)
                            .getResult();
                    // 4.1、若子申报对应的材料实例信息没有，则需要初始化
                    if (auditSpIMaterials == null || auditSpIMaterials.size() == 0) {
                        iHandleSPIMaterial.initTcSubappMaterial(subAppGuid, businessGuid, biGuid, phaseGuid, "", "");
                    }
                    // 5、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("biguid", biGuid);
                    log.info("=======结束调用submitBusinessInfo接口=======");
                    return JsonUtils.zwdtRestReturn("1", "保存成功！", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "主题申报基本信息提交异常：" + e.getMessage(), "");
        }
    }

    /**
     * 保存套餐基本信息接口(套餐申报页面点击保存按钮时调用)
     *
     * @param params
     *            请求参数
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/saveBirthBusinessInfo", method = RequestMethod.POST)
    public String saveBirthBusinessInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用saveBirthBusinessInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取新生儿姓名
                String babyName = obj.getString("babyname");
                // 1.2、获取新生儿出生日期
                Date babyBirthday = obj.getDate("babybirthday");
                // 1.3、获取新生儿性别
                String babySex = obj.getString("babysex");
                // 1.4、获取新生儿户口跟随方
                String babyHouseholdFollower = obj.getString("babyhouseholdfollower");
                // 1.5、获取新生儿籍贯
                String babyBirthPlace = obj.getString("babybirthplace");
                // 1.6、获取新生儿民族
                String babyNation = obj.getString("babynation");
                // 1.7、获取父亲姓名
                String fatherName = obj.getString("fathername");
                // 1.8、父亲身份号码
                String fatherIdNumber = obj.getString("fatheridnumber");
                // 1.9、获取父亲民族
                String fatherNation = obj.getString("fathernation");
                // 1.10、获取父亲籍贯
                String fatherBirthPlace = obj.getString("fatherbirthplace");
                // 1.11、获取父亲户籍地址
                String fatherDomicileAddress = obj.getString("fatherdomicileaddress");
                // 1.12、获取父亲现住地址
                String fatherNowAddress = obj.getString("fathernowaddress");
                // 1.13、获取母亲姓名
                String motherName = obj.getString("mothername");
                // 1.14、获取母亲身份号码
                String motherIdNumber = obj.getString("motheridnumber");
                // 1.15、获取母亲民族
                String motherNation = obj.getString("mothernation");
                // 1.16、获取母亲籍贯
                String motherBirthPlace = obj.getString("motherbirthplace");
                // 1.17、获取母亲户籍地址
                String motherDomicileAddress = obj.getString("motherdomicileaddress");
                // 1.18、获取母亲现住地址
                String motherNowAddress = obj.getString("mothernowaddress");
                // 1.19、获取是否选择证件送达
                String isChooseDocdeLivery = obj.getString("ischoosedocdelivery");
                // 1.20、获取证件送达地址
                String docdeLiveryAddress = obj.getString("docdeliveryaddress");
                // 1.21、获取接收办理结果通知的手机号码
                String receivePhoneNumber = obj.getString("receivephonenumber");
                // 1.22、获取出生登记申请
                String birthRegisterApply = obj.getString("birthregisterapply");
                // 1.23、获取民族成份确认
                String nationConfirm = obj.getString("nationconfirm");
                // 1.24、获取是否申请办理新生儿预防接种证
                String isApplyVaccination = obj.getString("isapplyvaccination");
                // 1.25、获取是否申请办理母子健康服务手册
                String isApplyHealthServices = obj.getString("isapplyhealthservices");
                // 1.26、获取是否申请办理新户口簿
                String isApplyNewBooklet = obj.getString("isapplynewbooklet");
                // 1.27、获取是否申请办理新生儿社保卡
                String isApplysocialSecurityCards = obj.getString("isapplysocialsecuritycards");
                // 1.28、获取社保卡合作银行
                String sscCreditUnion = obj.getString("ssccreditunion");
                // 1.29、获取社保卡领取方式
                String sscGetWay = obj.getString("sscgetway");
                // 1.30、获取城乡居民（新生儿）医保
                String medicalInsurance = obj.getString("medicalinsurance");
                // 1.31、获取业务标识
                String yewuGuid = obj.getString("yewuguid");
                // 1.32、获取套餐主题标识
                String businessGuid = obj.getString("businessguid");
                // 1.33、获取辖区编码
                String areaCode = obj.getString("areacode");
                // 1.34、主题实例标识
                String biGuid = obj.getString("biguid");
                // 1.35、主题实例标识
                String subappGuid = obj.getString("subappguid");

                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 2.1、获取用户个人信息
                    AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
                            .getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
                    // 3、获取主题基本信息
                    AuditSpBusiness auditSpBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessGuid)
                            .getResult();
                    if (auditSpBusiness != null) {
                        // 3.1、申报时以套餐配置的辖区为准，如果为空则用当前的辖区编码
                        areaCode = StringUtil.isNotBlank(auditSpBusiness.getAreacode()) ? auditSpBusiness.getAreacode()
                                : areaCode;
                        // 4、保存或更新基本信息
                        AuditYjsCsb auditYjsCsb = iAuditYjsCsbService.find(yewuGuid);
                        if (auditYjsCsb == null) {
                            // 4.1、若不存在信息则新增数据
                            auditYjsCsb = new AuditYjsCsb();
                            auditYjsCsb.setRowguid(yewuGuid);
                            auditYjsCsb.setOperatedate(new Date());
                            auditYjsCsb.setBabyname(babyName); // 新生儿姓名
                            auditYjsCsb.setBabybirthday(babyBirthday); // 新生儿姓名
                            auditYjsCsb.setBabysex(babySex); // 新生儿性别
                            auditYjsCsb.setBabyhouseholdfollower(babyHouseholdFollower); // 新生儿户口跟随方
                            auditYjsCsb.setBabybirthplace(babyBirthPlace); // 新生儿籍贯
                            auditYjsCsb.setBabynation(babyNation); // 新生儿民族
                            auditYjsCsb.setFathername(fatherName); // 父亲姓名
                            auditYjsCsb.setFatheridnumber(fatherIdNumber); // 父亲身份号码
                            auditYjsCsb.setFathernation(fatherNation); // 父亲民族
                            auditYjsCsb.setFatherbirthplace(fatherBirthPlace); // 父亲籍贯
                            auditYjsCsb.setFatherdomicileaddress(fatherDomicileAddress); // 父亲户籍地址
                            auditYjsCsb.setFathernowaddress(fatherNowAddress); // 父亲现住地址
                            auditYjsCsb.setMothername(motherName); // 母亲姓名
                            auditYjsCsb.setMotheridnumber(motherIdNumber); // 母亲身份号码
                            auditYjsCsb.setMothernation(motherNation); //
                            auditYjsCsb.setMotherbirthplace(motherBirthPlace); // 母亲籍贯
                            auditYjsCsb.setMotherdomicileaddress(motherDomicileAddress); // 母亲户籍地址
                            auditYjsCsb.setMothernowaddress(motherNowAddress); // 母亲现住地址
                            auditYjsCsb.setIschoosedocdelivery(isChooseDocdeLivery); // 是否选择证件送达
                            auditYjsCsb.setDocdeliveryaddress(docdeLiveryAddress); // 证件送达地址
                            auditYjsCsb.setReceivephonenumber(receivePhoneNumber); // 接收办理结果通知的手机号码
                            auditYjsCsb.setBirthregisterapply(birthRegisterApply); // 出生登记申请
                            auditYjsCsb.setNationconfirm(nationConfirm); // 民族成份确认
                            auditYjsCsb.setIsapplyvaccination(isApplyVaccination); // 是否申请办理新生儿预防接种证
                            auditYjsCsb.setIsapplyhealthservices(isApplyHealthServices); // 是否申请办理母子健康服务手册
                            auditYjsCsb.setIsapplynewbooklet(isApplyNewBooklet); // 是否申请办理新户口簿
                            auditYjsCsb.setIsapplysocialsecuritycards(isApplysocialSecurityCards); // 是否申请办理新生儿社保卡
                            auditYjsCsb.setSsccreditunion(sscCreditUnion); // 社保卡合作银行
                            auditYjsCsb.setSscgetway(sscGetWay); // 社保卡领取方式
                            auditYjsCsb.setMedicalinsurance(medicalInsurance); // 城乡居民（新生儿）医保
                            iAuditYjsCsbService.insert(auditYjsCsb);
                            // 4.2、初始化实例信息
                            // 4.2.1、生成主题实例信息
                            biGuid = iAuditSpInstance.addBusinessInstanceAndBiguid(biGuid, businessGuid, yewuGuid,
                                    auditOnlineIndividual.getApplyerguid(), fatherName, ZwfwConstant.APPLY_WAY_NETSBYS,
                                    auditSpBusiness.getBusinessname(), areaCode, auditSpBusiness.getBusinesstype())
                                    .getResult();
                            // 4.2.2、生成子申报信息
                            String phaseGuid = "";
                            List<AuditSpPhase> auditSpPhases = iAuditSpPhase.getSpPaseByBusinedssguid(businessGuid)
                                    .getResult();
                            if (auditSpPhases != null && auditSpPhases.size() > 0) {
                                phaseGuid = auditSpPhases.get(0).getRowguid();
                            }
                            AuditSpISubapp recordSubapp = new AuditSpISubapp();
                            if (StringUtil.isBlank(subappGuid)) {
                                subappGuid = UUID.randomUUID().toString();
                            }
                            recordSubapp.setApplyerguid(auditOnlineIndividual.getApplyerguid());
                            recordSubapp.setApplyername(auditOnlineIndividual.getClientname());
                            recordSubapp.setApplyerway(ZwfwConstant.APPLY_WAY_NETSBYS);
                            recordSubapp.setBiguid(biGuid);
                            recordSubapp.setBusinessguid(businessGuid);
                            recordSubapp.setCreatedate(new Date());
                            recordSubapp.setRowguid(subappGuid);
                            recordSubapp.setPhaseguid(phaseGuid);
                            recordSubapp.setStatus(ZwfwConstant.LHSP_Status_DPS);
                            recordSubapp.setSubappname(babyName);
                            recordSubapp.setInitmaterial(ZwfwConstant.CONSTANT_STR_ONE);
                            iAuditSpISubapp.addSubapp(recordSubapp);
                            // 4.2.3、生成事项实例信息
                            List<AuditSpTask> auditSpTasks = iAuditSpTask.getAllAuditSpTaskByPhaseguid(phaseGuid)
                                    .getResult();
                            List<AuditSpITask> auditSpITasks = iAuditSpITask.getSpITaskByBIGuid(biGuid).getResult();
                            if (auditSpITasks.size() == 0) {
                                for (AuditSpTask auditSpTask : auditSpTasks) {
                                    // 查询并遍历标准事项关联关系表
                                    SqlConditionUtil sql = new SqlConditionUtil();
                                    sql.eq("basetaskguid", auditSpTask.getStr("basetaskguid"));
                                    List<AuditSpBasetaskR> listr = iAuditSpBasetaskR
                                            .getAuditSpBasetaskrByCondition(sql.getMap()).getResult();
                                    for (AuditSpBasetaskR auditSpBasetaskR : listr) {
                                        AuditTask auditTask = iAuditTask
                                                .selectUsableTaskByTaskID(auditSpBasetaskR.getTaskid()).getResult();
                                        if (auditTask != null) {
                                            iAuditSpITask.addTaskInstance(businessGuid, biGuid, phaseGuid,
                                                    auditTask.getRowguid(), auditTask.getTaskname(), subappGuid, 0);
                                        }
                                    }

                                }
                            }

                            // 4.2.4、 初始化套餐式办件信息
                            iAuditOnlineProject.initOnlineProjectForBusiness(businessGuid,
                                    auditOnlineIndividual.getApplyerguid(), areaCode,
                                    auditOnlineIndividual.getClientname(), biGuid, subappGuid, yewuGuid,
                                    auditSpBusiness.getBusinessname());
                        }
                        else {
                            // 获取套餐实例
                            AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByYeWuGuid(yewuGuid)
                                    .getResult();
                            if (auditSpInstance != null) {
                                if (!auditSpInstance.getApplyerguid().equals(auditOnlineIndividual.getApplyerguid())) {
                                    return JsonUtils.zwdtRestReturn("0", "UserValid身份验证失败！", "");
                                }
                            }
                            // 5、如果已存在基本信息，更新基本信息
                            auditYjsCsb.setOperatedate(new Date());
                            auditYjsCsb.setBabyname(babyName); // 新生儿姓名
                            auditYjsCsb.setBabybirthday(babyBirthday); // 新生儿姓名
                            auditYjsCsb.setBabysex(babySex); // 新生儿性别
                            auditYjsCsb.setBabyhouseholdfollower(babyHouseholdFollower); // 新生儿户口跟随方
                            auditYjsCsb.setBabybirthplace(babyBirthPlace); // 新生儿籍贯
                            auditYjsCsb.setBabynation(babyNation); // 新生儿民族
                            auditYjsCsb.setFathername(fatherName); // 父亲姓名
                            auditYjsCsb.setFatheridnumber(fatherIdNumber); // 父亲身份号码
                            auditYjsCsb.setFathernation(fatherNation); // 父亲民族
                            auditYjsCsb.setFatherbirthplace(fatherBirthPlace); // 父亲籍贯
                            auditYjsCsb.setFatherdomicileaddress(fatherDomicileAddress); // 父亲户籍地址
                            auditYjsCsb.setFathernowaddress(fatherNowAddress); // 父亲现住地址
                            auditYjsCsb.setMothername(motherName); // 母亲姓名
                            auditYjsCsb.setMotheridnumber(motherIdNumber); // 母亲身份号码
                            auditYjsCsb.setMothernation(motherNation); //
                            auditYjsCsb.setMotherbirthplace(motherBirthPlace); // 母亲籍贯
                            auditYjsCsb.setMotherdomicileaddress(motherDomicileAddress); // 母亲户籍地址
                            auditYjsCsb.setMothernowaddress(motherNowAddress); // 母亲现住地址
                            auditYjsCsb.setIschoosedocdelivery(isChooseDocdeLivery); // 是否选择证件送达
                            auditYjsCsb.setDocdeliveryaddress(docdeLiveryAddress); // 证件送达地址
                            auditYjsCsb.setReceivephonenumber(receivePhoneNumber); // 接收办理结果通知的手机号码
                            auditYjsCsb.setBirthregisterapply(birthRegisterApply); // 出生登记申请
                            auditYjsCsb.setNationconfirm(nationConfirm); // 民族成份确认
                            auditYjsCsb.setIsapplyvaccination(isApplyVaccination); // 是否申请办理新生儿预防接种证
                            auditYjsCsb.setIsapplyhealthservices(isApplyHealthServices); // 是否申请办理母子健康服务手册
                            auditYjsCsb.setIsapplynewbooklet(isApplyNewBooklet); // 是否申请办理新户口簿
                            auditYjsCsb.setIsapplysocialsecuritycards(isApplysocialSecurityCards); // 是否申请办理新生儿社保卡
                            auditYjsCsb.setSsccreditunion(sscCreditUnion); // 社保卡合作银行
                            auditYjsCsb.setSscgetway(sscGetWay); // 社保卡领取方式
                            auditYjsCsb.setMedicalinsurance(medicalInsurance); // 城乡居民（新生儿）医保
                            iAuditYjsCsbService.update(auditYjsCsb);
                            // 5.1、更新套餐实例的申请人信息
                            if (auditSpInstance != null) {
                                auditSpInstance.set("applyername", fatherName);
                                iAuditSpInstance.updateAuditSpInstance(auditSpInstance);
                                // 5.2、更新子申报申请人信息
                                List<AuditSpISubapp> auditSpISubappList = iAuditSpISubapp
                                        .getSubappByBIGuid(auditSpInstance.getRowguid()).getResult();
                                AuditSpISubapp auditSpISubapp = null;
                                if (auditSpISubappList != null && auditSpISubappList.size() > 0) {
                                    auditSpISubapp = auditSpISubappList.get(0);
                                }
                                auditSpISubapp.set("subappname", babyName);
                                auditSpISubapp.set("applyername", fatherName);
                                auditSpISubapp.set("initmaterial", ZwfwConstant.CONSTANT_STR_ONE);
                                iAuditSpISubapp.updateAuditSpISubapp(auditSpISubapp);
                                // 5.3、更新AUDIT_ONLINE_PROJECT办件申请人信息
                                Map<String, String> updateFieldMap = new HashMap<String, String>(16);
                                Map<String, String> updateDateFieldMap = new HashMap<String, String>(16);
                                SqlConditionUtil conditionsql = new SqlConditionUtil();
                                updateFieldMap.put("applyername=", fatherName);
                                updateDateFieldMap.put("applydate=",
                                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                                conditionsql.eq("sourceguid", auditSpInstance.getStr("rowguid"));
                                iAuditOnlineProject.updateOnlineProject(updateFieldMap, updateDateFieldMap,
                                        conditionsql.getMap());
                            }
                        }
                    }
                }
                log.info("=======结束调用saveBirthBusinessInfo接口=======");
                return JsonUtils.zwdtRestReturn("1", "保存成功！", "");
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "主题申报基本信息保存异常", "");
        }
    }

    /**
     * 保存套餐基本信息接口(套餐申报页面点击保存按钮时调用)
     *
     * @param params
     *            请求参数
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getBirthBusinessInfo", method = RequestMethod.POST)
    public String getBirthBusinessInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getBirthBusinessInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取业务标识
                String birthGuid = obj.getString("yewuguid");
                // 1.2、获取实例标识
                String biGuid = obj.getString("projectguid");
                // 1.2、获取套餐标识
                String businessGuid = obj.getString("businessguid");
                JSONObject dataJson = new JSONObject();
                if (StringUtil.isBlank(birthGuid) && StringUtil.isNotBlank(biGuid)) {
                    AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biGuid).getResult();
                    birthGuid = auditSpInstance.getYewuguid();
                    dataJson.put("itemname", auditSpInstance.getItemname());
                    dataJson.put("areacode", auditSpInstance.getAreacode());
                    // 获取主题信息
                    AuditSpBusiness auditSpBusiness = iAuditSpBusiness
                            .getAuditSpBusinessByRowguid(auditSpInstance.getBusinessguid()).getResult();
                    dataJson.put("note", auditSpBusiness.getNote());

                    // 5.1、获取办件满意度
                    AuditOnlineEvaluat auditOnlineEvaluat = iAuditOnlineEvaluat.selectEvaluatByClientIdentifier(biGuid)
                            .getResult();
                    if (auditOnlineEvaluat != null) {
                        String satisfied = "";// 满意度
                        String evaluateContent = "";// 评价内容
                        if (auditOnlineEvaluat != null) {
                            satisfied = iCodeItemsService.getItemTextByCodeName("满意度",
                                    auditOnlineEvaluat.getSatisfied());
                            evaluateContent = StringUtil.isBlank(auditOnlineEvaluat.getEvaluatecontent()) ? ""
                                    : auditOnlineEvaluat.getEvaluatecontent();
                        }
                        dataJson.put("satisfied", satisfied);
                        dataJson.put("evaluatecontent", evaluateContent);
                    }
                    // 5.2、默认无需显示提交按钮(考虑到大厅页面没有关闭，审批系统直接受理情况)
                    String materialstatus = "1";
                    List<AuditSpIMaterial> auditSpIMaterials = iAuditSpIMaterial
                            .getSpIMaterialByBIGuid((auditSpInstance.getRowguid())).getResult();
                    if (auditSpIMaterials != null && auditSpIMaterials.size() > 0) {
                        for (AuditSpIMaterial auditSpIMaterial : auditSpIMaterials) {
                            // 5.2.1、如果材料中存在需要补正的材料，则需要显示提交按钮
                            if ("1".equals(auditSpIMaterial.getIsbuzheng())) {
                                materialstatus = "0";
                                break;
                            }
                        }
                    }
                    dataJson.put("materialstatus", materialstatus);

                }
                if (StringUtil.isNotBlank(businessGuid)) {
                    AuditSpBusiness auditSpBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessGuid)
                            .getResult();
                    if (auditSpBusiness != null) {
                        dataJson.put("businessname", auditSpBusiness.getStr("businessname"));
                        dataJson.put("note", auditSpBusiness.getStr("note"));
                    }
                }
                // 2、获取表单实例信息
                AuditYjsCsb birthBusinessInfo = iAuditYjsCsbService.find(birthGuid);
                if (birthBusinessInfo != null) {
                    dataJson.put("rowguid", birthBusinessInfo.getRowguid());
                    dataJson.put("babyname", birthBusinessInfo.getBabyname()); // 新生儿姓名
                    dataJson.put("babybirthday", StringUtil.isBlank(birthBusinessInfo.getBabybirthday()) ? ""
                            : EpointDateUtil.convertDate2String(birthBusinessInfo.getBabybirthday(), "yyyy-MM-dd")); // 新生儿出生日期
                    dataJson.put("babybirthplace", birthBusinessInfo.getBabybirthplace()); // 新生儿籍贯

                    dataJson.put("fathername", birthBusinessInfo.getFathername()); // 父亲姓名
                    dataJson.put("fatheridnumber", birthBusinessInfo.getFatheridnumber()); // 父亲身份号码
                    dataJson.put("fatherbirthplace", birthBusinessInfo.getFatherbirthplace()); // 父亲籍贯
                    dataJson.put("fatherdomicileaddress", birthBusinessInfo.getFatherdomicileaddress()); // 父亲户籍地址
                    dataJson.put("fathernowaddress", birthBusinessInfo.getFathernowaddress()); // 父亲现住地址
                    dataJson.put("mothername", birthBusinessInfo.getMothername()); // 母亲姓名
                    dataJson.put("motheridnumber", birthBusinessInfo.getMotheridnumber()); // 母亲身份号码
                    dataJson.put("motherbirthplace", birthBusinessInfo.getMotherbirthplace()); // 母亲籍贯
                    dataJson.put("motherdomicileaddress", birthBusinessInfo.getMotherdomicileaddress()); // 母亲户籍地址
                    dataJson.put("mothernowaddress", birthBusinessInfo.getMothernowaddress()); // 母亲现住地址
                    dataJson.put("docdeliveryaddress", birthBusinessInfo.getDocdeliveryaddress()); // 证件送达地址
                    dataJson.put("receivephonenumber", birthBusinessInfo.getReceivephonenumber()); // 接收办理结果通知的手机号码
                }
                // 返回一些用户信息，此处仅作出生一件事示例，即返回身份证，区别男女
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    String idNumber = auditOnlineRegister.getIdnumber();
                    if (StringUtil.isNotBlank(idNumber)) {
                        idNumber = idNumber.substring(16, 17);
                        int result = Integer.parseInt(idNumber) % 2;
                        if (ZwfwConstant.CONSTANT_INT_ONE == result) {
                            // 奇数代表男，则返回为父亲身份证
                            if (birthBusinessInfo == null
                                    || (StringUtil.isBlank(birthBusinessInfo.getFatheridnumber()))) {
                                dataJson.put("fathername", auditOnlineRegister.getUsername()); // 父亲姓名
                                dataJson.put("fatheridnumber", auditOnlineRegister.getIdnumber()); // 父亲身份号码
                            }
                        }
                        else {
                            if (birthBusinessInfo == null
                                    || StringUtil.isBlank(birthBusinessInfo.getMotheridnumber())) {
                                dataJson.put("mothername", auditOnlineRegister.getUsername()); // 母亲姓名
                                dataJson.put("motheridnumber", auditOnlineRegister.getIdnumber()); // 母亲身份号码
                            }
                        }
                    }
                }
                List<CodeItems> sexCode = iCodeItemsService.listCodeItemsByCodeName("出生一件事_性别");
                List<CodeItems> famCode = iCodeItemsService.listCodeItemsByCodeName("出生一件事_父母");
                List<CodeItems> unionCode = iCodeItemsService.listCodeItemsByCodeName("出生一件事_社保卡合作银行");
                List<CodeItems> sendCode = iCodeItemsService.listCodeItemsByCodeName("出生一件事_社保卡领取方式");
                List<CodeItems> shifouCode = iCodeItemsService.listCodeItemsByCodeName("出生一件事_是否");
                List<CodeItems> ybCode = iCodeItemsService.listCodeItemsByCodeName("出生一件事_城乡居民（新生儿）医保");
                List<CodeItems> mzCode = iCodeItemsService.listCodeItemsByCodeName("民族");
                List<JSONObject> babyNationList = new ArrayList<>();
                JSONObject babyNationjson = new JSONObject();
                babyNationjson.put("itemvalue", "");
                babyNationjson.put("itemtext", "请选择");
                babyNationjson.put("isselected", 1);
                babyNationList.add(babyNationjson);
                for (CodeItems codeItems : mzCode) {
                    JSONObject objJson = new JSONObject();
                    objJson.put("itemvalue", codeItems.getItemValue());
                    objJson.put("itemtext", codeItems.getItemText());
                    if (birthBusinessInfo != null
                            && codeItems.getItemValue().equals(birthBusinessInfo.getStr("babyNation"))) {
                        objJson.put("isselected", 1);
                        babyNationList.get(0).put("isselected", 0);
                    }
                    babyNationList.add(objJson);
                }
                dataJson.put("babynationlist", babyNationList); // 新生儿民族
                List<JSONObject> fatherNationList = new ArrayList<>();
                JSONObject fatherNationjson = new JSONObject();
                fatherNationjson.put("itemvalue", "");
                fatherNationjson.put("itemtext", "请选择");
                fatherNationjson.put("isselected", 1);
                fatherNationList.add(fatherNationjson);
                for (CodeItems codeItems : mzCode) {
                    JSONObject objJson = new JSONObject();
                    objJson.put("itemvalue", codeItems.getItemValue());
                    objJson.put("itemtext", codeItems.getItemText());
                    if (birthBusinessInfo != null
                            && codeItems.getItemValue().equals(birthBusinessInfo.getFathernation())) {
                        objJson.put("isselected", 1);
                        fatherNationList.get(0).put("isselected", 0);
                    }
                    fatherNationList.add(objJson);
                }
                dataJson.put("fathernationlist", fatherNationList); // 父亲民族
                List<JSONObject> motherNationList = new ArrayList<>();
                JSONObject motherNationjson = new JSONObject();
                motherNationjson.put("itemvalue", "");
                motherNationjson.put("itemtext", "请选择");
                motherNationjson.put("isselected", 1);
                motherNationList.add(motherNationjson);
                for (CodeItems codeItems : mzCode) {
                    JSONObject objJson = new JSONObject();
                    objJson.put("itemvalue", codeItems.getItemValue());
                    objJson.put("itemtext", codeItems.getItemText());
                    if (birthBusinessInfo != null
                            && codeItems.getItemValue().equals(birthBusinessInfo.getMothernation())) {
                        objJson.put("isselected", 1);
                        motherNationList.get(0).put("isselected", 0);

                    }
                    motherNationList.add(objJson);
                }
                dataJson.put("mothernationlist", motherNationList); //
                List<JSONObject> babysexList = new ArrayList<>();
                for (CodeItems codeItems : sexCode) {
                    JSONObject objJson = new JSONObject();
                    objJson.put("itemvalue", codeItems.getItemValue());
                    objJson.put("itemtext", codeItems.getItemText());
                    if (birthBusinessInfo != null && codeItems.getItemValue().equals(birthBusinessInfo.getBabysex())) {
                        objJson.put("ischecked", 1);
                    }
                    babysexList.add(objJson);
                }
                dataJson.put("babysexlist", babysexList); // 新生儿性别 男女
                List<JSONObject> babyhouseholdfollowerList = new ArrayList<>();
                for (CodeItems codeItems : famCode) {
                    JSONObject objJson = new JSONObject();
                    objJson.put("itemvalue", codeItems.getItemValue());
                    objJson.put("itemtext", codeItems.getItemText());
                    if (birthBusinessInfo != null
                            && codeItems.getItemValue().equals(birthBusinessInfo.getBabyhouseholdfollower())) {
                        objJson.put("ischecked", 1);
                    }
                    babyhouseholdfollowerList.add(objJson);
                }
                dataJson.put("babyhouseholdfollowerlist", babyhouseholdfollowerList); // 新生儿户口跟随方
                List<JSONObject> isChooseDocdeLiveryList = new ArrayList<>();
                for (CodeItems codeItems : shifouCode) {
                    JSONObject objJson = new JSONObject();
                    objJson.put("itemvalue", codeItems.getItemValue());
                    objJson.put("itemtext", codeItems.getItemText());
                    if (birthBusinessInfo != null
                            && codeItems.getItemValue().equals(birthBusinessInfo.getIschoosedocdelivery())) {
                        objJson.put("ischecked", 1);
                    }
                    isChooseDocdeLiveryList.add(objJson);
                }
                dataJson.put("ischoosedocdeliverylist", isChooseDocdeLiveryList); // 是否选择证件送达
                dataJson.put("ischoosedocdelivery",
                        birthBusinessInfo == null ? "0" : birthBusinessInfo.getIschoosedocdelivery()); // 是否选择证件送达
                List<JSONObject> birthRegisterApplyList = new ArrayList<>();
                for (CodeItems codeItems : famCode) {
                    JSONObject objJson = new JSONObject();
                    objJson.put("itemvalue", codeItems.getItemValue());
                    objJson.put("itemtext", codeItems.getItemText());
                    if (birthBusinessInfo != null
                            && codeItems.getItemValue().equals(birthBusinessInfo.getBirthregisterapply())) {
                        objJson.put("ischecked", 1);
                    }
                    birthRegisterApplyList.add(objJson);
                }
                dataJson.put("birthregisterapplylist", birthRegisterApplyList); // 出生登记申请
                List<JSONObject> nationConfirmList = new ArrayList<>();
                for (CodeItems codeItems : famCode) {
                    JSONObject objJson = new JSONObject();
                    objJson.put("itemvalue", codeItems.getItemValue());
                    objJson.put("itemtext", codeItems.getItemText());
                    if (birthBusinessInfo != null
                            && codeItems.getItemValue().equals(birthBusinessInfo.getNationconfirm())) {
                        objJson.put("ischecked", 1);
                    }
                    nationConfirmList.add(objJson);
                }
                dataJson.put("nationconfirmlist", nationConfirmList); // 民族成份确认
                List<JSONObject> isApplyVaccinationList = new ArrayList<>();
                for (CodeItems codeItems : shifouCode) {
                    JSONObject objJson = new JSONObject();
                    objJson.put("itemvalue", codeItems.getItemValue());
                    objJson.put("itemtext", codeItems.getItemText());
                    if (birthBusinessInfo != null
                            && codeItems.getItemValue().equals(birthBusinessInfo.getIsapplyvaccination())) {
                        objJson.put("ischecked", 1);
                    }
                    isApplyVaccinationList.add(objJson);
                }
                dataJson.put("isapplyvaccinationlist", isApplyVaccinationList); // 是否申请办理新生儿预防接种证
                List<JSONObject> isApplyHealthServicesList = new ArrayList<>();
                for (CodeItems codeItems : shifouCode) {
                    JSONObject objJson = new JSONObject();
                    objJson.put("itemvalue", codeItems.getItemValue());
                    objJson.put("itemtext", codeItems.getItemText());
                    if (birthBusinessInfo != null
                            && codeItems.getItemValue().equals(birthBusinessInfo.getIsapplyhealthservices())) {
                        objJson.put("ischecked", 1);
                    }
                    isApplyHealthServicesList.add(objJson);
                }
                dataJson.put("isapplyhealthserviceslist", isApplyHealthServicesList); // 是否申请办理母子健康服务手册
                List<JSONObject> isApplyNewBookletList = new ArrayList<>();
                for (CodeItems codeItems : shifouCode) {
                    JSONObject objJson = new JSONObject();
                    objJson.put("itemvalue", codeItems.getItemValue());
                    objJson.put("itemtext", codeItems.getItemText());
                    if (birthBusinessInfo != null
                            && codeItems.getItemValue().equals(birthBusinessInfo.getIsapplynewbooklet())) {
                        objJson.put("ischecked", 1);
                    }
                    isApplyNewBookletList.add(objJson);
                }
                dataJson.put("isapplynewbookletlist", isApplyNewBookletList); // 是否申请办理新户口簿
                List<JSONObject> isApplysocialSecurityCardsList = new ArrayList<>();
                for (CodeItems codeItems : shifouCode) {
                    JSONObject objJson = new JSONObject();
                    objJson.put("itemvalue", codeItems.getItemValue());
                    objJson.put("itemtext", codeItems.getItemText());
                    if (birthBusinessInfo != null
                            && codeItems.getItemValue().equals(birthBusinessInfo.getIsapplysocialsecuritycards())) {
                        objJson.put("ischecked", 1);
                    }
                    isApplysocialSecurityCardsList.add(objJson);
                }
                dataJson.put("isapplysocialsecuritycardslist", isApplysocialSecurityCardsList); // 是否申请办理新生儿社保卡
                List<JSONObject> sscCreditUnionList = new ArrayList<>();
                for (CodeItems codeItems : unionCode) {
                    JSONObject objJson = new JSONObject();
                    objJson.put("itemvalue", codeItems.getItemValue());
                    objJson.put("itemtext", codeItems.getItemText());
                    if (birthBusinessInfo != null
                            && codeItems.getItemValue().equals(birthBusinessInfo.getSsccreditunion())) {
                        objJson.put("ischecked", 1);
                    }
                    sscCreditUnionList.add(objJson);
                }
                dataJson.put("ssccreditunionlist", sscCreditUnionList); // 社保卡合作银行
                List<JSONObject> sscGetWayList = new ArrayList<>();
                for (CodeItems codeItems : sendCode) {
                    JSONObject objJson = new JSONObject();
                    objJson.put("itemvalue", codeItems.getItemValue());
                    objJson.put("itemtext", codeItems.getItemText());
                    if (birthBusinessInfo != null
                            && codeItems.getItemValue().equals(birthBusinessInfo.getSscgetway())) {
                        objJson.put("ischecked", 1);
                    }
                    sscGetWayList.add(objJson);
                }
                dataJson.put("sscgetwaylist", sscGetWayList); // 社保卡领取方式
                List<JSONObject> medicalInsuranceList = new ArrayList<>();
                for (CodeItems codeItems : ybCode) {
                    JSONObject objJson = new JSONObject();
                    objJson.put("itemvalue", codeItems.getItemValue());
                    objJson.put("itemtext", codeItems.getItemText());
                    if (birthBusinessInfo != null
                            && codeItems.getItemValue().equals(birthBusinessInfo.getMedicalinsurance())) {
                        objJson.put("ischecked", 1);
                    }
                    medicalInsuranceList.add(objJson);
                }
                dataJson.put("medicalinsurancelist", medicalInsuranceList); // 城乡居民（新生儿）医保

                log.info("=======结束调用getBirthBusinessInfo接口=======");
                return JsonUtils.zwdtRestReturn("1", "查询成功！", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "主题申报基本信息查询异常", "");
        }
    }

    /**
     * 获取套餐补正材料信息的接口（套餐详细页面调用）
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getBusinessInitMaterialList", method = RequestMethod.POST)
    public String getBusinessInitMaterialList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getBusinessInitMaterialList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取主题实例标识
                String biGuid = obj.getString("projectguid");
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理开始<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 1.2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    ZwdtUserAuthValid zwdtUserAuthValid = new ZwdtUserAuthValid();
                    if (!zwdtUserAuthValid.userValid(auditOnlineRegister, biGuid, ZwdtConstant.USERVALID_SPI_INS)) {
                        return JsonUtils.zwdtRestReturn("0", "UserValid身份验证失败！", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                }
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 2、获取子申报信息
                AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByBIGuid(biGuid).getResult().get(0);
                // 3、获取主题中所有的事项实例信息
                List<AuditSpITask> auditSpITasks = iAuditSpITask.getSpITaskByBIGuid(biGuid).getResult();
                // 4、获取子申报材料信息中的补正材料信息
                List<JSONObject> materialJsonList = new ArrayList<JSONObject>();
                List<AuditSpIMaterial> auditSpIMaterials = iAuditSpIMaterial
                        .getSpIPatchMaterialBySubappGuid(auditSpISubapp.getRowguid()).getResult();
                // 4.2、获取补正材料信息返回JSON
                for (AuditSpIMaterial auditSpIMaterial : auditSpIMaterials) {
                    // 4.2.1、获取补正材料基本信息
                    JSONObject materialJson = new JSONObject();
                    materialJson.put("projectmaterialguid", auditSpIMaterial.getRowguid());// 材料标识
                    materialJson.put("projectmaterialname", auditSpIMaterial.getMaterialname());// 材料名称
                    materialJson.put("status", auditSpIMaterial.getStatus()); // 材料提交状态
                    materialJson.put("taskmaterialguid", auditSpIMaterial.getMaterialguid());// 事项材料标识
                    materialJson.put("clientguid", auditSpIMaterial.getCliengguid());
                    materialJson.put("submittype",
                            iCodeItemsService.getItemTextByCodeName("提交方式", auditSpIMaterial.getSubmittype()));
                    String necessity = "0";
                    if (ZwfwConstant.NECESSITY_SET_YES.equals(auditSpIMaterial.getNecessity())) {
                        necessity = "1";
                    }
                    materialJson.put("necessary", necessity); // 是否必须
                    // 4.2.2、判断材料列表上传按钮的显示内容（0:未上传、1：已上传、2：已引用证照库、3: 在线填表、4:已填表）
                    int count = iAttachService.getAttachCountByClientGuid(auditSpIMaterial.getCliengguid());
                    int showButton = 0;
                    String needLoad = "0"; // 是否需要上传到证照库 0 不需要 1 需要
                    // 4.2.3、如果该补正材料不是共享材料,则获取事项材料的信息
                    if ("0".equals(auditSpIMaterial.getShared())) {
                        materialJson.put("sharematerialiguid", ""); // 获取共享材料实例关联标识
                        AuditTaskMaterial auditTaskMaterial = null;
                        for (AuditSpITask auditSpITask : auditSpITasks) {
                            // 4.2.3.1、通过事项标识及材料业务唯一标识获取到事项中的材料信息
                            auditTaskMaterial = iAuditTaskMaterial.selectTaskMaterialByTaskGuidAndMaterialId(
                                    auditSpITask.getTaskguid(), auditSpIMaterial.getMaterialguid()).getResult();
                            if (auditTaskMaterial != null) {
                                break;
                            }
                        }
                        if (auditTaskMaterial != null) {
                            // 4.2.3.2、获取材料空白表格标识
                            if (StringUtil.isNotBlank(auditTaskMaterial.getTemplateattachguid())) {
                                int tempAttachCount = iAttachService
                                        .getAttachCountByClientGuid(auditTaskMaterial.getTemplateattachguid());
                                if (tempAttachCount > 0) {
                                    materialJson.put("templateattachguid", auditTaskMaterial.getTemplateattachguid());
                                }
                            }
                            // 4.2.3.3、获取材料填报示例标识
                            if (StringUtil.isNotBlank(auditTaskMaterial.getExampleattachguid())) {
                                int exampleAttachCount = iAttachService
                                        .getAttachCountByClientGuid(auditTaskMaterial.getExampleattachguid());
                                if (exampleAttachCount > 0) {
                                    materialJson.put("exampleattachguid", auditTaskMaterial.getExampleattachguid());
                                }
                            }
                            // 4.2.3.4、获取材料来源渠道
                            String materialSource;
                            if (StringUtil.isBlank(auditTaskMaterial.getFile_source())) {
                                materialSource = "申请人自备";
                            }
                            else {
                                materialSource = iCodeItemsService.getItemTextByCodeName("来源渠道",
                                        auditTaskMaterial.getFile_source());
                            }
                            materialJson.put("materialsource",
                                    StringUtil.isBlank(materialSource) ? "申请人自备" : materialSource);
                            // 4.2.3.5、判断按钮显示方式（0:未上传、1：已上传、2：已引用证照库、3:
                            // 在线填表、4:已填表）
                            if (auditTaskMaterial.getType() == WorkflowKeyNames9.MaterialType_Form) {
                                // 4.2.3.5.1、如果材料类型为表单，有附件则显示已填表，没有附件则显示在线填表
                                showButton = count > 0 ? 4 : 3;
                            }
                            else if (auditTaskMaterial.getType() == WorkflowKeyNames9.MaterialType_Attach) {
                                // 4.2.3.5.2、如果材料类型为附件，有附件则显示已上传，没有附件则显示未上传
                                showButton = count > 0 ? 1 : 0;
                            }
                        }
                        // 4.2.3.5.3、获取材料填报须知
                        materialJson.put("hasfileexplian",
                                StringUtil.isBlank(auditTaskMaterial.getFile_explian()) ? 0 : 1);
                        if (StringUtil.isNotBlank(auditTaskMaterial.getFile_explian())) {
                            materialJson.put("fileexplian", auditTaskMaterial.getFile_explian());
                        }
                    }
                    // 4.2.4、如果该补正材料是共享材料,则获取共享材料的信息
                    if ("1".equals(auditSpIMaterial.getStr("shared"))) {
                        materialJson.put("sharematerialiguid", auditSpIMaterial.getCertinfoinstanceguid()); // 共享材料实例关联标识
                        // 4.2.4.1、获取材料与共享材料的对应关系
                        String materialSourceText = "";
                        AuditSpShareMaterial auditSpShareMaterial = iAuditSpShareMaterial
                                .getAuditSpShareMaterialByShareMaterialGuid(auditSpIMaterial.getBusinessguid(),
                                        auditSpIMaterial.getMaterialguid())
                                .getResult();
                        if (auditSpShareMaterial != null) {
                            String file_source = auditSpShareMaterial.getFile_source();
                            materialSourceText = iCodeItemsService.getItemTextByCodeName("来源渠道", file_source);
                            // 4.2.3.7、获取材料填报须知
                            materialJson.put("hasfileexplian",
                                    StringUtil.isBlank(auditSpShareMaterial.getNote()) ? 0 : 1);
                            if (StringUtil.isNotBlank(auditSpShareMaterial.getNote())) {
                                materialJson.put("fileexplian", auditSpShareMaterial.getNote());
                            }
                        }
                        if (StringUtil.isBlank(materialSourceText)) {
                            materialSourceText = "申请人自备";
                        }
                        materialJson.put("materialsource", materialSourceText);
                        // 4.2.3.6、有附件则显示已上传，没有附件则显示未上传
                        showButton = count > 0 ? 1 : 0;
                        // 4.2.3.7、获取材料填报须知
                        CertInfo certInfo = iCertInfoExternal
                                .getCertInfoByRowguid(auditSpIMaterial.getCertinfoinstanceguid());
                        if (certInfo != null) {
                            // 4.1.7.2.2、获取证照实例对应的附件数量
                            List<JSONObject> rsInfos = iCertAttachExternal.getAttachList(certInfo.getCertcliengguid(),
                                    ZwdtConstant.CERTAPPKEY);
                            int countRs = 0;
                            if (rsInfos != null && rsInfos.size() > 0) {
                                countRs = rsInfos.size();
                            }
                            // 4.1.7.2.3、数量不一致
                            if (count != countRs) {
                                showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YSC);
                            }
                            // 4.1.7.2.4、数量一致的情况
                            else {
                                String cliengTag = "";
                                List<FrameAttachInfo> spMaterialAttachInfos = iAttachService
                                        .getAttachInfoListByGuid(auditSpIMaterial.getCliengguid());
                                spMaterialAttachInfos
                                        .sort((a, b) -> a.getAttachLength().compareTo(b.getAttachLength()));
                                if (rsInfos != null && rsInfos.size() > 0) {
                                    rsInfos.sort((a, b) -> a.getLong("size").compareTo(b.getLong("size")));
                                    for (int i = 0; i < rsInfos.size(); i++) {
                                        // 3.1.7.2.4.1、大小不同说明附件改动过
                                        cliengTag = spMaterialAttachInfos.get(i).getCliengTag();
                                        if ((StringUtil.isBlank(cliengTag) || "本地上传".equals(cliengTag)
                                                || "云盘上传".equals(cliengTag) || "手机上传".equals(cliengTag))
                                                && rsInfos.get(i).getLong("size").longValue() != spMaterialAttachInfos
                                                        .get(i).getAttachLength().longValue()) {
                                            showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YSC);
                                            break;
                                        }
                                        else {
                                            // 3.1.7.2.4.2、若文件未发生改变，则按钮显示维持原样
                                            if (StringUtil.isNotBlank(certInfo.getMaterialtype())) {
                                                String materialType = certInfo.getMaterialtype();
                                                if (Integer.parseInt(materialType) == 1) {
                                                    showButton = Integer
                                                            .parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYZZK);// 已引用证照库
                                                }
                                                else if (Integer.parseInt(materialType) == 2) {
                                                    showButton = Integer
                                                            .parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYPW); // 已引用批文
                                                }
                                                else {
                                                    showButton = Integer
                                                            .parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYCL); // 已引用材料
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else {
                            // 4.1.7.3、如果没有附件，则标识为未上传
                            showButton = count > 0 ? 1 : 0;
                        }
                    }
                    materialJson.put("needload", needLoad);
                    materialJson.put("showbutton", showButton);
                    materialJsonList.add(materialJson);
                }
                // 4.2.5、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("materiallist", materialJsonList);
                log.info("=======结束调用getBusinessInitMaterialList接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取初始化材料列表成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getBusinessInitMaterialList接口参数：params【" + params + "】=======");
            log.info("=======getBusinessInitMaterialList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取初始化材料列表失败：" + e.getMessage(), "");
        }
    }

    /**
     * 检查补正材料是否都已提交
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/checkBZMaterialIsSubmit", method = RequestMethod.POST)
    public String checkBZMaterialIsSubmit(@RequestBody String params) {
        try {
            log.info("=======开始调用checkBZMaterialIsSubmit接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取材料的列表数据
                String materialList = obj.getString("materiallist");
                int noSubmitNum = 0;// 补正材料没有提交的个数
                // 2、将材料信息转换为弱类型的实体集合
                List<Record> records = JsonUtil.jsonToList(materialList, Record.class);
                for (Record record : records) {
                    // 2.1、获取办件材料的提交状态
                    String status = record.getStr("status");
                    // 2.2、获取材料实例标识
                    String projectmaterialguid = record.getStr("projectmaterialguid");
                    // 2.3、获取材料实例信息
                    AuditSpIMaterial auditSpIMaterial = iAuditSpIMaterial.getSpIMaterialByrowguid(projectmaterialguid)
                            .getResult();
                    if (auditSpIMaterial != null) {
                        int intstatus = Integer.parseInt(status);
                        String isRongque = auditSpIMaterial.getAllowrongque(); // 材料容缺状态
                        String paperUnnecrsstity = iConfigService.getFrameConfigValue("AS_CHECK_MATERIAL"); // 纸质必要材料是否要提交
                        paperUnnecrsstity = StringUtil.isBlank(paperUnnecrsstity) ? ZwfwConstant.CONSTANT_STR_ZERO
                                : paperUnnecrsstity;
                        // 2.4、必要非容缺材料未提交：
                        if (ZwfwConstant.NECESSITY_SET_YES.equals(auditSpIMaterial.getNecessity())
                                && !ZwdtConstant.STRING_YES.equals(isRongque)
                                && intstatus != ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC
                                && intstatus != ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC) {
                            // 2.4.1、如果外网不需要提交纸质材料，需要判断材料提交方式
                            if (ZwfwConstant.CONSTANT_STR_ONE.equals(paperUnnecrsstity)) {
                                // 如果材料提交方式是“提交电子文件”，或是“提交电子或纸质文件”：
                                if ("10".equals(auditSpIMaterial.getSubmittype())
                                        || "40".equals(auditSpIMaterial.getSubmittype())) {
                                    noSubmitNum++;
                                }
                            }
                            // 2.4.2、如果外网需要提交纸质材料
                            else {
                                noSubmitNum++;
                            }
                        }
                    }
                }
                // 3、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("nosubmitnum", noSubmitNum);
                log.info("=======结束调用checkBZMaterialIsSubmit接口=======");
                return JsonUtils.zwdtRestReturn("1", "检查补正材料是否都提交成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======checkBZMaterialIsSubmit接口参数：params【" + params + "】=======");
            log.info("=======checkBZMaterialIsSubmit异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "检查补正材料是否都提交失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取套餐材料信息的接口（继续申报至套餐申报页面调用、套餐详细页面调用）
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getInitBusinessMaterialList", method = RequestMethod.POST)
    public String getInitBusinessMaterialList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getInitBusinessMaterialList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、主题实例标识
                String biGuid = obj.getString("biGuid");
                String subAppGuid = obj.getString("subappguid");
                String oldprojectguid = obj.getString("oldprojectguid");// 旧的办件标识
                String businessguid = obj.getString("businessguid");// 主题标识
                String areacode = obj.getString("areacode");
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理开始<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 1.2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    /*
                     * ZwdtUserAuthValid zwdtUserAuthValid = new ZwdtUserAuthValid(); if
                     * (!zwdtUserAuthValid.userValid(auditOnlineRegister, biGuid,
                     * ZwdtConstant.USERVALID_SPI_INS)) { return JsonUtils.zwdtRestReturn("0",
                     * "UserValid身份验证失败！", ""); }
                     */
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                }
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 2、获取子申报信息
                AuditSpISubapp auditSpISubapp = null;
                List<AuditSpISubapp> auditSpISubapps = iAuditSpISubapp.getSubappByBIGuid(biGuid).getResult();
                if (auditSpISubapps != null && !auditSpISubapps.isEmpty()) {
                    auditSpISubapp = auditSpISubapps.get(0);
                }
                // 3、获取材料实例信息
                List<AuditSpIMaterial> auditSpIMaterials = new ArrayList<>();
                if (StringUtil.isBlank(subAppGuid) && auditSpISubapps != null) {
                    subAppGuid = auditSpISubapp.getRowguid();
                }
                auditSpIMaterials = iAuditSpIMaterial.getSpIMaterialBySubappGuid(subAppGuid).getResult();
                // 4、获取事项实例信息
                List<AuditSpITask> auditSpITasks = iAuditSpITask.getSpITaskByBIGuid(biGuid).getResult();
                List<JSONObject> materialJsonList = new ArrayList<JSONObject>();

                // oldprojectguid不为空时对返回的list过滤
                List<String> guids = null;
                if (StringUtil.isNotBlank(oldprojectguid)) {
                    AuditProject auditProject = iAuditProject
                            .getAuditProjectByRowGuid("rowguid,taskguid,task_id", oldprojectguid, areacode).getResult();
                    if (auditProject != null) {
                        List<String> materialids = new ArrayList<>();
                        List<AuditTaskMaterial> taskMateriallList = iAuditTaskMaterial
                                .getUsableMaterialListByTaskguid(auditProject.getTaskguid()).getResult();

                        if (!taskMateriallList.isEmpty()) {
                            for (AuditTaskMaterial auditTaskMaterial : taskMateriallList) {
                                materialids.add(auditTaskMaterial.getMaterialid());
                            }
                            guids = iAuditSpIMaterial.getSpIMaterialsByIDS(businessguid, subAppGuid, materialids)
                                    .getResult();
                        }
                    }
                    else {
                        throw new RuntimeException("未查询到办件!");
                    }

                }
                // 5、获取材料基本信息返回JSON
                for (AuditSpIMaterial auditSpIMaterial : auditSpIMaterials) {
                    if (guids != null) {
                        if (!guids.contains(auditSpIMaterial.getRowguid())) {
                            continue;
                        }
                    }
                    // 5.1、获取材料基本信息
                    JSONObject materialJson = new JSONObject();
                    materialJson.put("projectmaterialguid", auditSpIMaterial.getRowguid()); // 材料标识
                    materialJson.put("projectmaterialname", auditSpIMaterial.getMaterialname()); // 材料名称
                    materialJson.put("clientguid", auditSpIMaterial.getCliengguid());
                    materialJson.put("status", auditSpIMaterial.getStatus()); // 材料状态
                    materialJson.put("taskmaterialguid", auditSpIMaterial.getMaterialguid());// 事项材料主键
                    materialJson.put("ordernum", auditSpIMaterial.getOrdernum());
                    materialJson.put("submittype",
                            iCodeItemsService.getItemTextByCodeName("提交方式", auditSpIMaterial.getSubmittype()));
                    String necessity = "0";
                    if (ZwfwConstant.NECESSITY_SET_YES.equals(auditSpIMaterial.getNecessity())) {
                        necessity = "1";
                    }
                    materialJson.put("necessary", necessity); // 是否必须
                    // 5.1.1、判断材料列表上传按钮的显示内容（0:未上传、1：已上传、2：已引用证照库、3:
                    // 在线填表、4:已填表）
                    int count = iAttachService.getAttachCountByClientGuid(auditSpIMaterial.getCliengguid());
                    int showButton = 0;
                    // 5.1.2、如果该材料不是共享材料,则获取事项材料的信息
                    if ("0".equals(auditSpIMaterial.getShared())) {
                        materialJson.put("sharematerialiguid", ""); // 共享材料实例关联标识
                        AuditTaskMaterial auditTaskMaterial = null;
                        for (AuditSpITask auditSpITask : auditSpITasks) {
                            // 5.1.2.1、通过事项标识及材料业务唯一标识获取到事项中的材料信息
                            auditTaskMaterial = iAuditTaskMaterial.selectTaskMaterialByTaskGuidAndMaterialId(
                                    auditSpITask.getTaskguid(), auditSpIMaterial.getMaterialguid()).getResult();
                            if (auditTaskMaterial != null) {
                                break;
                            }
                        }
                        if (auditTaskMaterial != null) {
                            // 5.1.2.2、获取材料空白表格标识
                            if (StringUtil.isNotBlank(auditTaskMaterial.getTemplateattachguid())) {
                                /*
                                 * int templateAttachCount = iAttachService .getAttachCountByClientGuid(
                                 * auditTaskMaterial .getTemplateattachguid());
                                 */
                                List<FrameAttachInfo> frameAttachInfos = iAttachService
                                        .getAttachInfoListByGuid(auditTaskMaterial.getTemplateattachguid());
                                if (frameAttachInfos.size() > 0) {
                                    materialJson.put("templateattachguid", frameAttachInfos.get(0).getAttachGuid());
                                }
                            }
                            // 5.1.2.3、获取材料填报示例标识
                            if (StringUtil.isNotBlank(auditTaskMaterial.getExampleattachguid())) {
                                /*
                                 * int exampleAttachCount = iAttachService .getAttachCountByClientGuid(
                                 * auditTaskMaterial .getExampleattachguid());
                                 */
                                List<FrameAttachInfo> frameAttachInfos = iAttachService
                                        .getAttachInfoListByGuid(auditTaskMaterial.getExampleattachguid());
                                if (frameAttachInfos.size() > 0) {
                                    materialJson.put("exampleattachguid", frameAttachInfos.get(0).getAttachGuid());
                                }
                            }
                            // 5.1.2.4、获取材料来源渠道
                            String materialSource;
                            if (StringUtil.isBlank(auditTaskMaterial.getFile_source())) {
                                materialSource = "申请人自备";
                            }
                            else {
                                materialSource = iCodeItemsService.getItemTextByCodeName("来源渠道",
                                        auditTaskMaterial.getFile_source());
                            }
                            materialJson.put("materialsource",
                                    StringUtil.isBlank(materialSource) ? "申请人自备" : materialSource);
                            // 5.1.2.5、判断按钮显示方式（0:未上传、1：已上传、2：已引用证照库、3:
                            // 在线填表、4:已填表）
                            if (auditTaskMaterial.getType() == WorkflowKeyNames9.MaterialType_Form) {
                                // 5.1.2.5.1、如果材料类型为表单，有附件则显示已填表，没有附件则显示在线填表
                                showButton = count > 0 ? 4 : 3;
                            }
                            else if (auditTaskMaterial.getType() == WorkflowKeyNames9.MaterialType_Attach) {
                                // 5.1.2.5.2、如果材料类型为附件，有附件则显示已上传，没有附件则显示未上传
                                showButton = count > 0 ? 1 : 0;
                            }
                            // 5.1.2.5、获取材料填报须知
                            materialJson.put("hasfileexplian",
                                    StringUtil.isBlank(auditTaskMaterial.getFile_explian()) ? 0 : 1);
                            if (StringUtil.isNotBlank(auditTaskMaterial.getFile_explian())) {
                                materialJson.put("fileexplian", auditTaskMaterial.getFile_explian());
                            }
                        }
                    }
                    // 5.1.3、如果该材料是共享材料,则获取共享材料的信息
                    if ("1".equals(auditSpIMaterial.getShared())) {

                        String materialSourceText = "";
                        AuditSpShareMaterial auditSpShareMaterial = iAuditSpShareMaterial
                                .getAuditSpShareMaterialByShareMaterialGuid(auditSpIMaterial.getMaterialguid(),
                                        auditSpIMaterial.getBusinessguid())
                                .getResult();
                        if (auditSpShareMaterial != null) {
                            String file_source = auditSpShareMaterial.getStr("file_source");
                            materialSourceText = iCodeItemsService.getItemTextByCodeName("来源渠道", file_source);
                        }
                        if (StringUtil.isBlank(materialSourceText)) {
                            materialSourceText = "申请人自备";
                        }
                        materialJson.put("materialsource", materialSourceText);
                        // 5.1.3.6、有附件则显示已上传，没有附件则显示未上传
                        showButton = count > 0 ? 1 : 0;
                        // 5.1.3.7、获取材料填报须知
                        if (auditSpShareMaterial != null && StringUtil.isNotBlank(auditSpShareMaterial.getNote())) {
                            // 5.1.3.7、获取材料填报须知
                            materialJson.put("hasfileexplian",
                                    StringUtil.isBlank(auditSpShareMaterial.getNote()) ? 0 : 1);
                            materialJson.put("fileexplian", auditSpShareMaterial.getNote());
                        }
                        CertInfo certInfo = iCertInfoExternal
                                .getCertInfoByRowguid(auditSpIMaterial.getCertinfoinstanceguid());
                        if (certInfo != null) {
                            materialJson.put("sharematerialiguid", auditSpIMaterial.getCertinfoinstanceguid()); // 共享材料实例关联标识
                            String materialType = certInfo.getMaterialtype();
                            String needLoad = ZwdtConstant.CERTLEVEL_C.equals(certInfo.getCertlevel()) ? "1" : "0";
                            materialJson.put("needload", needLoad);
                            if (count > 0) {
                                if (Integer.parseInt(materialType) == 1) {
                                    // 5.1.3.4.1、已引用证照库
                                    showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYZZK);
                                }
                                else if (Integer.parseInt(materialType) == 2) {
                                    // 5.1.3.4.2、已引用批文
                                    showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYPW);
                                }
                                else {
                                    // 5.1.3.4.3、已引用材料
                                    showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYCL);
                                }
                            }
                        }
                        /*
                         * else { // 5.1.3.、如果没有附件，则标识为未上传 showButton = Integer.parseInt(ZwdtConstant.
                         * PROJECTMATERIAL_BUTTON_WSC); }
                         */
                    }
                    materialJson.put("isshared", auditSpIMaterial.getShared());
                    materialJson.put("showbutton", showButton);
                    materialJsonList.add(materialJson);
                    // 6、对返回的材料列表进行排序
                    Collections.sort(materialJsonList, new Comparator<JSONObject>()
                    {
                        @Override
                        public int compare(JSONObject o1, JSONObject o2) {
                            Integer necessary1 = Integer.parseInt(o1.get("necessary").toString());
                            Integer necessary2 = Integer.parseInt(o2.get("necessary").toString());
                            int comNecessity = necessary2.compareTo(necessary1);
                            int ret = comNecessity;
                            if (comNecessity == 0) {
                                Integer ordernum1 = o1.get("ordernum") == null ? 0
                                        : Integer.parseInt(o1.get("ordernum").toString());
                                Integer ordernum2 = o2.get("ordernum") == null ? 0
                                        : Integer.parseInt(o2.get("ordernum").toString());
                                ret = ordernum2.compareTo(ordernum1);
                            }
                            return ret;
                        }
                    });

                }
                // 5.1.4、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("materiallist", materialJsonList);
                log.info("=======结束调用getInitBusinessMaterialList接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取初始化材料列表成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }

        }
        catch (

        Exception e) {
            e.printStackTrace();
            log.info("=======getInitBusinessMaterialList接口参数：params【" + params + "】=======");
            log.info("=======getInitBusinessMaterialList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取初始化材料列表失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取套餐详情接口
     *
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getBusinessDetail", method = RequestMethod.POST)
    public String getBusinessDetail(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getBusinessDetail接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取主题实例标识
                String biGuid = obj.getString("projectguid");
                // 1.2、获取辖区编码
                // String areaCode = obj.getString("areacode");
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理开始<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 1.3、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    ZwdtUserAuthValid zwdtUserAuthValid = new ZwdtUserAuthValid();
                    if (!zwdtUserAuthValid.userValid(auditOnlineRegister, biGuid, ZwdtConstant.USERVALID_SPI_INS)) {
                        return JsonUtils.zwdtRestReturn("0", "UserValid身份验证失败！", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                }
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 2、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                // 3、获取主题实例信息
                AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biGuid).getResult();
                dataJson.put("itemname", auditSpInstance.getItemname());
                dataJson.put("areacode", auditSpInstance.getAreacode());
                // 3.1、 获取主题信息
                AuditSpBusiness auditSpBusiness = iAuditSpBusiness
                        .getAuditSpBusinessByRowguid(auditSpInstance.getBusinessguid()).getResult();
                // 根据主题处理地址判断用户类型
                String handleUrl = auditSpBusiness.getHandleURL();
                String userType = ZwfwConstant.APPLAYERTYPE_QY;
                if ("epointzwfw/auditsp/auditindividual".equals(handleUrl)) {
                    userType = ZwfwConstant.APPLAYERTYPE_GR;
                }
                dataJson.put("userType", userType); // 申请人类型
                dataJson.put("note", auditSpBusiness.getNote());
                // 4、获取企业信息
                AuditSpIntegratedCompany auditSpIntegratedCompany = iAuditSpIntegratedCompany
                        .getAuditSpIntegratedCompanyByGuid(auditSpInstance.getYewuguid()).getResult();
                if (auditSpIntegratedCompany != null) {
                    // 5、返回企业基本信息
                    dataJson.put("companyname", auditSpIntegratedCompany.getCompanyname()); // 企业名称
                    dataJson.put("address", auditSpIntegratedCompany.getAddress()); // 地址
                    dataJson.put("zzjgdmz", auditSpIntegratedCompany.getZzjgdmz()); // 组织机构代码
                    dataJson.put("contactperson", auditSpIntegratedCompany.getContactperson()); // 联系人
                    dataJson.put("contactphone", auditSpIntegratedCompany.getContactphone()); // 联系电话
                    dataJson.put("contactpostcode", auditSpIntegratedCompany.getContactpostcode()); // 邮编
                    dataJson.put("scope", auditSpIntegratedCompany.getScope()); // 经营范围
                    dataJson.put("capital", auditSpIntegratedCompany.getCapital()); // 注册资金
                    dataJson.put("legalpersonemail", auditSpIntegratedCompany.getLegalpersonemail()); // 注册资金
                    dataJson.put("fundssource",
                            StringUtil.isBlank(auditSpIntegratedCompany.getFundssource()) ? ""
                                    : iCodeItemsService.getItemTextByCodeName("资金来源",
                                            auditSpIntegratedCompany.getFundssource()));
                    dataJson.put("companytype",
                            StringUtil.isBlank(auditSpIntegratedCompany.getCompanytype()) ? ""
                                    : iCodeItemsService.getItemTextByCodeName("企业类型代码",
                                            auditSpIntegratedCompany.getCompanytype()));
                    dataJson.put("legalpersonduty",
                            StringUtil.isBlank(auditSpIntegratedCompany.getLegalpersonduty()) ? ""
                                    : iCodeItemsService.getItemTextByCodeName("法人职务",
                                            auditSpIntegratedCompany.getLegalpersonduty()));
                    dataJson.put("qrcontent",
                            WebUtil.getRequestCompleteUrl(request)
                                    + "/epointzwmhwz/pages/carveout/spintegratedetail?businessguid="
                                    + auditSpInstance.getBusinessguid());// 二维码内容
                    // 5.1、获取办件满意度
                    AuditOnlineEvaluat auditOnlineEvaluat = iAuditOnlineEvaluat.selectEvaluatByClientIdentifier(biGuid)
                            .getResult();
                    if (auditOnlineEvaluat != null) {
                        String satisfied = "";// 满意度
                        String evaluateContent = "";// 评价内容
                        if (auditOnlineEvaluat != null) {
                            satisfied = iCodeItemsService.getItemTextByCodeName("满意度",
                                    auditOnlineEvaluat.getSatisfied());
                            evaluateContent = StringUtil.isBlank(auditOnlineEvaluat.getEvaluatecontent()) ? ""
                                    : auditOnlineEvaluat.getEvaluatecontent();
                        }
                        dataJson.put("satisfied", satisfied);
                        dataJson.put("evaluatecontent", evaluateContent);
                    }
                    // 5.2、默认无需显示提交按钮(考虑到大厅页面没有关闭，审批系统直接受理情况)
                    String materialstatus = "1";
                    List<AuditSpIMaterial> auditSpIMaterials = iAuditSpIMaterial
                            .getSpIMaterialByBIGuid((auditSpInstance.getRowguid())).getResult();
                    if (auditSpIMaterials != null && auditSpIMaterials.size() > 0) {
                        for (AuditSpIMaterial auditSpIMaterial : auditSpIMaterials) {
                            // 5.2.1、如果材料中存在需要补正的材料，则需要显示提交按钮
                            if ("1".equals(auditSpIMaterial.getIsbuzheng())) {
                                materialstatus = "0";
                                break;
                            }
                        }
                    }
                    dataJson.put("materialstatus", materialstatus);
                }
                log.info("=======结束调用getBusinessDetail接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取办件详情成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "获取办件详情异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取套餐已提交的材料接口
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getSubmitBusinessMaterialList", method = RequestMethod.POST)
    public String getSubmitBusinessMaterialList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getSubmitBusinessMaterialList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取主题实例标识
                String biGuid = obj.getString("projectguid");
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理开始<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 1.2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    ZwdtUserAuthValid zwdtUserAuthValid = new ZwdtUserAuthValid();
                    if (!zwdtUserAuthValid.userValid(auditOnlineRegister, biGuid, ZwdtConstant.USERVALID_SPI_INS)) {
                        return JsonUtils.zwdtRestReturn("0", "UserValid身份验证失败！", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                }
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 2、获取子申报信息
                List<AuditSpISubapp> auditSpISubapps = iAuditSpISubapp.getSubappByBIGuid(biGuid).getResult();
                AuditSpISubapp auditSpISubapp = auditSpISubapps.get(0);
                String subAppGuid = auditSpISubapp.getRowguid();
                // 3、获取事项材料实例信息
                List<JSONObject> materialJsonList = new ArrayList<JSONObject>();
                List<AuditSpIMaterial> auditSpIMaterials = iAuditSpIMaterial.getSpIMaterialBySubappGuid(subAppGuid)
                        .getResult();
                for (AuditSpIMaterial auditSpIMaterial : auditSpIMaterials) {
                    // 3.1、获取材料已提交的材料
                    String materialStatus = auditSpIMaterial.get("STATUS");
                    if (ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER == Integer.parseInt(materialStatus)
                            || ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC == Integer.parseInt(materialStatus)
                            || ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC == Integer
                                    .parseInt(materialStatus)) {
                        JSONObject materialJson = new JSONObject();
                        materialJson.put("projectmaterialname", auditSpIMaterial.get("MATERIALNAME")); // 材料名称
                        materialJson.put("projectmaterialguid", auditSpIMaterial.getRowguid()); // 材料标识
                        materialJson.put("necessary",
                                auditSpIMaterial.get("NECESSITY").equals(ZwfwConstant.NECESSITY_SET_YES) ? 1 : 0); // 是否必需
                        materialJson.put("ordernum", auditSpIMaterial.getStr("ordernum"));
                        if ("0".equals(auditSpIMaterial.getStr("shared"))) {
                            // 4.1.1、如果材料是非共享材料
                            List<AuditSpITask> auditSpITasks = iAuditSpITask
                                    .getTaskInstanceBySubappGuid(auditSpIMaterial.getSubappguid()).getResult();
                            Record auditTaskMaterial = null;
                            for (Record auditSpITask : auditSpITasks) {
                                auditTaskMaterial = iAuditTaskMaterial
                                        .selectTaskMaterialByTaskGuidAndMaterialId(auditSpITask.getStr("taskguid"),
                                                auditSpIMaterial.getMaterialguid())
                                        .getResult();
                                if (auditTaskMaterial != null) {
                                    break;
                                }
                            }
                            if (auditTaskMaterial != null) {
                                String materialsource;
                                if (auditTaskMaterial == null
                                        || StringUtil.isBlank(auditTaskMaterial.getStr("file_source"))) {
                                    materialsource = "申请人自备";
                                }
                                else {
                                    materialsource = iCodeItemsService.getItemTextByCodeName("来源渠道",
                                            auditTaskMaterial.getStr("file_source"));
                                }
                                materialJson.put("materialsource", materialsource);
                                materialJson.put("submittype", iCodeItemsService.getItemTextByCodeName("提交方式",
                                        String.valueOf(auditTaskMaterial.getStr("submittype"))));
                                materialJson.put("ordernum", auditTaskMaterial.getStr("ordernum"));
                            }
                        }
                        else {
                            // 4.1.2、如果材料是共享材料
                            // 设置共享材料来源
                            AuditSpShareMaterial auditSpShareMaterial = iAuditSpShareMaterial
                                    .getAuditSpShareMaterialByShareMaterialGuid(auditSpIMaterial.getMaterialguid(),
                                            auditSpIMaterial.getBusinessguid())
                                    .getResult();
                            String spMaterialSource = "";
                            if (auditSpShareMaterial == null
                                    || StringUtil.isBlank(auditSpShareMaterial.getStr("file_source"))) {
                                spMaterialSource = "申请人自备";
                            }
                            else {
                                spMaterialSource = iCodeItemsService.getItemTextByCodeName("来源渠道",
                                        auditSpShareMaterial.getStr("file_source"));
                            }
                            materialJson.put("materialsource", spMaterialSource);// 来源
                                                                                 // （共享材料默认为申请人自备）
                            materialJson.put("ordernum", auditSpIMaterial.getStr("ordernum"));
                            materialJson.put("submittype", iCodeItemsService.getItemTextByCodeName("提交方式",
                                    String.valueOf(auditSpIMaterial.getStr("submittype"))));
                        }
                        materialJsonList.add(materialJson);
                    }
                }
                // 4、对返回的材料列表进行排序
                Collections.sort(materialJsonList, new Comparator<JSONObject>()
                {
                    @Override
                    public int compare(JSONObject o1, JSONObject o2) {
                        Integer necessary1 = Integer.parseInt(o1.get("necessary").toString());
                        Integer necessary2 = Integer.parseInt(o2.get("necessary").toString());
                        int comNecessity = necessary2.compareTo(necessary1);
                        int ret = comNecessity;
                        if (comNecessity == 0) {
                            Integer ordernum1 = o1.get("ordernum") == null ? 0
                                    : Integer.parseInt(o1.get("ordernum").toString());
                            Integer ordernum2 = o2.get("ordernum") == null ? 0
                                    : Integer.parseInt(o2.get("ordernum").toString());
                            ret = ordernum2.compareTo(ordernum1);
                        }
                        return ret;
                    }
                });
                JSONObject dataJson = new JSONObject();
                dataJson.put("materiallist", materialJsonList);
                dataJson.put("materialcount", materialJsonList.size());
                log.info("=======结束调用getSubmitBusinessMaterialList接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取已提交材料列表成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getSubmitBusinessMaterialList接口参数：params【" + params + "】=======");
            log.info("=======getSubmitBusinessMaterialList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取已提交材料列表失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取材料对应的附件列表接口（点开上传按钮后页面调用）
     *
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getMaterialAttachListbyClientguid", method = RequestMethod.POST)
    public String getMaterialAttachListbyClientguid(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getMaterialAttachListbyClientguid接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取材料对应附件的业务标识
                String clientguid = obj.getString("clientguid");
                // 1.2、获取上传方式（0:云盘 1:本地上传 2:手机上传 为空显示所有）
                String uploadtype = obj.getString("uploadtype");
                List<JSONObject> attachJsonList = new ArrayList<JSONObject>();
                // 2、获取办件材料对应的附件
                List<FrameAttachInfo> frameAttachInfos = iAttachService.getAttachInfoListByGuid(clientguid);
                for (FrameAttachInfo frameAttachInfo : frameAttachInfos) {
                    String cliengTag = frameAttachInfo.getCliengTag();
                    if (StringUtil.isBlank(uploadtype) || uploadtype.equals(cliengTag)) {
                        JSONObject attachJson = new JSONObject();
                        FrameAttachStorage frameAttachStorage = iAttachService
                                .getAttach(frameAttachInfo.getAttachGuid());
                        if (frameAttachStorage != null) {
                            // 3、设置附件信息
                            attachJson.put("attachicon",
                                    WebUtil.getRequestCompleteUrl(request) + "/rest/auditattach/readAttach?attachguid="
                                            + frameAttachStorage.getAttachGuid() + "&attachto=blsp"); // 附件图标
                            // 附件名称超过20个字省略
                            String filename = frameAttachInfo.getAttachFileName().substring(0,
                                    frameAttachInfo.getAttachFileName().lastIndexOf("."));
                            filename = filename.length() > 17 ? filename.substring(0, 17) + "..." : filename;
                            attachJson.put("slattachname", filename + frameAttachInfo.getContentType()); // 附件名称
                            attachJson.put("attachname", frameAttachInfo.getAttachFileName()); // 附件名称
                            attachJson.put("attachsize", AttachUtil.convertFileSize(frameAttachInfo.getAttachLength())); // 附件大小
                            attachJson.put("attachsource", "dzzzglxt".equals(frameAttachInfo.getCliengTag()) ? "电子证照引用"
                                    : frameAttachInfo.getCliengTag()); // 附件来源
                            // 如果是内网附件
                            if (StringUtil.isBlank(frameAttachInfo.getCliengTag())) {
                                attachJson.put("attachsource", "内网"); // 附件来源
                            }
                            attachJson.put("attachguid", frameAttachInfo.getAttachGuid()); // 附件唯一标识
                            attachJsonList.add(attachJson);
                        }
                    }
                }
                // 4、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("attachlist", attachJsonList);
                log.info("=======结束调用getMaterialAttachListbyClientguid接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取上传的附件列表成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getMaterialAttachListbyClientguid接口参数：params【" + params + "】=======");
            log.info("=======getMaterialAttachListbyClientguid异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取上传的附件列表异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取材料合格标准的接口（点开上传按钮后页面调用）
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getMaterialstandardByGuid", method = RequestMethod.POST)
    public String getMaterialstandardByGuid(@RequestBody String params) {
        try {
            log.info("=======开始调用getMaterialstandardByGuid接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取事项材料标识
                String taskMaterialGuid = obj.getString("taskmaterialguid");
                // 1.2、获取材料实例标识
                String projectMaterialGuid = obj.getString("projectmaterialguid");
                // 1.3、获取主题标识
                String biGuid = obj.getString("biguid");
                // 2、获取材料实例标识
                AuditSpIMaterial auditSpIMaterial = iAuditSpIMaterial.getSpIMaterialByrowguid(projectMaterialGuid)
                        .getResult();
                // 3、 获取事项实例信息
                List<AuditSpITask> auditSpITasks = iAuditSpITask.getSpITaskByBIGuid(biGuid).getResult();
                AuditTaskMaterial auditTaskMaterial = null;
                for (AuditSpITask auditSpITask : auditSpITasks) {
                    auditTaskMaterial = iAuditTaskMaterial
                            .selectTaskMaterialByTaskGuidAndMaterialId(auditSpITask.getTaskguid(), taskMaterialGuid)
                            .getResult();
                    if (auditTaskMaterial != null) {
                        break;
                    }
                }
                // 4、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("projectmaterialname", auditSpIMaterial.getMaterialname());// 材料名称
                dataJson.put("projectmaterialguid", projectMaterialGuid);// 办件材料主键
                // 5、获取材料合格标准
                if (auditTaskMaterial != null) {
                    dataJson.put("materialstantard", StringUtil.isBlank(auditTaskMaterial.get("standard")) ? "无"
                            : auditTaskMaterial.get("standard"));
                }
                else {
                    dataJson.put("materialstantard", "无");
                }
                log.info("=======结束调用getMaterialstandardByGuid接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取材料合格标准成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getMaterialstandardByGuid接口参数：params【" + params + "】=======");
            log.info("=======getMaterialstandardByGuid异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取材料合格标准异常：" + e.getMessage(), "");
        }
    }

    /**
     * 判断材料是否上传的接口
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/checkMaterialIsUploadByClientguid", method = RequestMethod.POST)
    public String checkMaterialIsUploadByClientguid(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用checkMaterialIsUploadByClientguid接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                AuditOnlineRegister auditOnlineRegister = getOnlineRegister(request);
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取套餐办件材料附件业务标识
                String clientGuid = obj.getString("clientguid");
                // 1.2、获取套餐办件材料标识
                String projectMaterialGuid = obj.getString("projectmaterialguid");
                // 1.3、获取共享材料实例标识
                String shareMaterialIGuid = obj.getString("sharematerialiguid");
                // 1.4、获取材料提交状态
                String status = obj.getString("status");
                // 1.5、获取材料类型
                String type = obj.getString("type");
                // 1.6、获取材料对应附件是否需要上传到证照库（只有C级证照才需要）
                String needLoad = obj.getString("needload");
                // 1.7、获取主题实例标识
                String biGuid = obj.getString("biGuid");
                // 2、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                // 3、获取子申报信息
                AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByBIGuid(biGuid).getResult().get(0);
                // 4、获取材料实例信息
                List<AuditSpIMaterial> auditSpIMaterials = iAuditSpIMaterial
                        .getSpIMaterialBySubappGuid(auditSpISubapp.getRowguid()).getResult();
                // 5、获取办件材料对应的附件数量
                int attachCount = iAttachService.getAttachCountByClientGuid(clientGuid);
                for (AuditSpIMaterial auditSpIMaterial : auditSpIMaterials) {
                    if (auditSpIMaterial.getRowguid().equals(projectMaterialGuid)) {
                        // 5、获取套餐办件材料对应的附件数量
                        int count = iAttachService.getAttachCountByClientGuid(clientGuid);
                        int intStatus = Integer.parseInt(status);
                        if (count > 0) {
                            // 5.1、若材料有对应的附件，说明电子材料已提交
                            switch (intStatus) {
                                // 5.1.1、材料原先的状态为未提交更新为电子已提交
                                case ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT:
                                    intStatus = ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC;
                                    break;
                                // 5.1.2、材料原先的状态为纸质已提交更新为电子和纸质都提交
                                case ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER:
                                    intStatus = ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC;
                                    break;
                                default:
                                    break;
                            }
                        }
                        else {
                            // 5.2、若材料没有对应的附件，说明电子材料没有提交
                            switch (intStatus) {
                                // 5.2.1、材料原先的状态为电子已提交更新为未提交
                                case ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC:
                                    intStatus = ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT;
                                    break;
                                // 5.2.2、材料原先的状态为电子和纸质都提交更新为纸质已提交
                                case ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC:
                                    intStatus = ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER;
                                    break;
                                default:
                                    break;
                            }
                        }
                        auditSpIMaterial.setStatus(String.valueOf(intStatus));
                        auditSpIMaterial.setCliengguid(clientGuid);
                        iAuditSpIMaterial.updateSpIMaterial(auditSpIMaterial);
                        // 6、判断材料列表上传按钮的显示内容（0:未上传、1：已上传、2：已引用证照库、3: 在线填表、4:已填表）
                        int showButton = 0;
                        String cliengTag = "";
                        if (StringUtil.isBlank(shareMaterialIGuid)) {
                            // 6.1、如果没有从证照库引用附件，则为普通附件及填表
                            if (String.valueOf(WorkflowKeyNames9.MaterialType_Form).equals(type)) {
                                // 6.1.1、如果材料类型为表单，有附件则显示已填表，没有附件则显示在线填表
                                showButton = count > 0 ? 4 : 3;
                            }
                            else {
                                // 6.1.2、如果材料类型为附件，有附件则显示已上传，没有附件则显示未上传
                                showButton = count > 0 ? 1 : 0;
                            }
                        }
                        else {
                            // 6.2、如果关联了证照库
                            if (ZwdtConstant.NEEDLOAD_Y.equals(needLoad)) {
                                showButton = attachCount > 0 ? 1 : 0;
                                // 6.2.1 如果为C级证照，若没有相关证照实例，则新增，若有，则更新
                                // 6.2.2、更新证照库版本
                                CertInfo certInfo = iCertInfoExternal
                                        .getCertInfoByRowguid(auditSpIMaterial.getCertinfoinstanceguid());
                                List<JSONObject> rsInfos = null;
                                if (certInfo != null) {
                                    rsInfos = iCertAttachExternal.getAttachList(certInfo.getCertcliengguid(),
                                            ZwdtConstant.CERTAPPKEY);
                                }
                                int countRs = 0;
                                if (rsInfos != null && rsInfos.size() > 0) {
                                    countRs = rsInfos.size();
                                }
                                // 5.2.2.1、数量不一致
                                if (attachCount > 0 && attachCount != countRs) {
                                    certInfo.setCertcliengguid(clientGuid);
                                    shareMaterialIGuid = iCertInfoExternal.submitCertInfo(certInfo, null,
                                            ZwdtConstant.MATERIALTYPE_CERT, ZwdtConstant.CERTAPPKEY);
                                    // 5.2.2.1.1、关联到办件材料
                                    auditSpIMaterial.setCertinfoinstanceguid(shareMaterialIGuid);
                                    iAuditSpIMaterial.updateSpIMaterial(auditSpIMaterial);
                                }
                                else if (attachCount == countRs) {
                                    // 5.2.2.2、数量一致继续比较
                                    List<FrameAttachInfo> spMaterialAttachInfos = iAttachService
                                            .getAttachInfoListByGuid(auditSpIMaterial.getCliengguid());
                                    spMaterialAttachInfos
                                            .sort((a, b) -> a.getAttachLength().compareTo(b.getAttachLength()));
                                    boolean isModify = false;
                                    if (rsInfos != null && rsInfos.size() > 0) {
                                        rsInfos.sort((a, b) -> a.getLong("size").compareTo(b.getLong("size")));
                                        for (int i = 0; i < rsInfos.size(); i++) {
                                            // 如果是核对过的cliengtag不为空，若为空且大小不同表示已修改
                                            cliengTag = spMaterialAttachInfos.get(i).getCliengTag();
                                            if ((StringUtil.isBlank(cliengTag) || "本地上传".equals(cliengTag)
                                                    || "云盘上传".equals(cliengTag) || "手机上传".equals(cliengTag))
                                                    && rsInfos.get(i).getLong("size")
                                                            .longValue() != spMaterialAttachInfos.get(i)
                                                                    .getAttachLength().longValue()) {
                                                isModify = true;
                                                break;
                                            }
                                            else {
                                                // 若文件未发生改变，则按钮显示维持原样
                                                CertInfo certInfo2 = iCertInfoExternal
                                                        .getCertInfoByRowguid(shareMaterialIGuid);
                                                if (certInfo2 != null) {
                                                    if (StringUtil.isNotBlank(certInfo2.getMaterialtype())) {
                                                        String materialType = certInfo2.getMaterialtype();
                                                        if (Integer.parseInt(materialType) == 1) {
                                                            showButton = Integer.parseInt(
                                                                    ZwdtConstant.PROJECTMATERIAL_BUTTON_YYZZK);// 已引用证照库
                                                        }
                                                        else if (Integer.parseInt(materialType) == 2) {
                                                            showButton = Integer
                                                                    .parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYPW); // 已引用批文
                                                        }
                                                        else {
                                                            showButton = Integer
                                                                    .parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YYCL); // 已引用材料
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    else {
                                        isModify = true;
                                    }
                                    if (isModify) {
                                        certInfo.setCertcliengguid(clientGuid);
                                        shareMaterialIGuid = iCertInfoExternal.submitCertInfo(certInfo, null,
                                                ZwdtConstant.MATERIALTYPE_CERT, ZwdtConstant.CERTAPPKEY);
                                        // 5.2.2.2.2、关联到办件材料
                                        auditSpIMaterial.setCertinfoinstanceguid(shareMaterialIGuid);
                                        iAuditSpIMaterial.updateSpIMaterial(auditSpIMaterial);
                                    }
                                }
                            }
                            else {
                                // 5.2.3、如果不需要更新证照库，有附件则则根据共享材料的类别显示已引用共享材料，没有附件则显示未未上传
                                if (attachCount > 0) {
                                    CertInfo certInfo = iCertInfoExternal.getCertInfoByRowguid(shareMaterialIGuid);
                                    if (certInfo != null) {
                                        // 5.2.3.1、比较证照实例里的附件和上传的附件
                                        List<JSONObject> rsInfos = iCertAttachExternal
                                                .getAttachList(certInfo.getCertcliengguid(), ZwdtConstant.CERTAPPKEY);
                                        int countRs = 0;
                                        if (rsInfos != null && rsInfos.size() > 0) {
                                            countRs = rsInfos.size();
                                        }
                                        if (attachCount != countRs) {
                                            showButton = Integer.parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YSC);
                                        }
                                        // 5.2.3.1.1、数量一致比较详细的附件内容
                                        else if (attachCount == countRs) {
                                            List<FrameAttachInfo> spMaterialAttachInfos = iAttachService
                                                    .getAttachInfoListByGuid(auditSpIMaterial.getCliengguid());
                                            spMaterialAttachInfos
                                                    .sort((a, b) -> a.getAttachLength().compareTo(b.getAttachLength()));
                                            if (rsInfos != null && rsInfos.size() > 0) {
                                                rsInfos.sort((a, b) -> a.getLong("size").compareTo(b.getLong("size")));
                                                for (int i = 0; i < rsInfos.size(); i++) {
                                                    // 5.2.3.1.2、AttachStorageGuid不同说明附件改动过
                                                    cliengTag = spMaterialAttachInfos.get(i).getCliengTag();
                                                    if ((StringUtil.isBlank(cliengTag) || "本地上传".equals(cliengTag)
                                                            || "云盘上传".equals(cliengTag) || "手机上传".equals(cliengTag))
                                                            && rsInfos.get(i).getLong("size")
                                                                    .longValue() != spMaterialAttachInfos.get(i)
                                                                            .getAttachLength().longValue()) {
                                                        showButton = Integer
                                                                .parseInt(ZwdtConstant.PROJECTMATERIAL_BUTTON_YSC);
                                                        break;
                                                    }
                                                    else {
                                                        // 5.2.3.1.3、若文件未发生改变，则按钮显示维持原样
                                                        CertInfo certInfo2 = iCertInfoExternal
                                                                .getCertInfoByRowguid(shareMaterialIGuid);
                                                        if (certInfo2 != null) {
                                                            if (StringUtil.isNotBlank(certInfo2.getMaterialtype())) {
                                                                String materialType = certInfo2.getMaterialtype();
                                                                if (Integer.parseInt(materialType) == 1) {
                                                                    showButton = Integer.parseInt(
                                                                            ZwdtConstant.PROJECTMATERIAL_BUTTON_YYZZK);// 已引用证照库
                                                                }
                                                                else if (Integer.parseInt(materialType) == 2) {
                                                                    showButton = Integer.parseInt(
                                                                            ZwdtConstant.PROJECTMATERIAL_BUTTON_YYPW); // 已引用批文
                                                                }
                                                                else {
                                                                    showButton = Integer.parseInt(
                                                                            ZwdtConstant.PROJECTMATERIAL_BUTTON_YYCL); // 已引用材料
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    else {
                                        showButton = count > 0 ? 1 : 0;
                                    }
                                }
                                else {
                                    // 5.2.3.2、未上传
                                    showButton = count > 0 ? 1 : 0;
                                }
                            }
                        }
                        dataJson.put("showbutton", showButton);
                        dataJson.put("needload", needLoad);
                        dataJson.put("status", intStatus);
                        dataJson.put("sharematerialiguid", shareMaterialIGuid);
                    }
                }
                log.info("=======结束调用checkMaterialIsUploadByClientguid接口=======");
                return JsonUtils.zwdtRestReturn("1", "判断是否上传材料成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======checkMaterialIsUploadByClientguid接口参数：params【" + params + "】=======");
            log.info("=======checkMaterialIsUploadByClientguid异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "判断是否上传材料异常：" + e.getMessage(), "");
        }
    }

    /**
     * 检查必要材料是否都上传（套餐办件申报提交时调用）
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/checkAllMaterialIsSubmit", method = RequestMethod.POST)
    public String checkAllMaterialIsSubmit(@RequestBody String params) {
        try {
            log.info("=======开始调用checkAllMaterialIsSubmit接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取材料的提交状态
                String taskMaterialStatusArr = obj.getString("statusarray");
                // 1.2、获取材料的标识
                String taskMaterialGuidsArr = obj.getString("taskmaterialarray");
                int noSubmitNum = 0;// 必要材料没有提交的个数
                int materialCount = 0;
                if (StringUtil.isNotBlank(taskMaterialGuidsArr) && StringUtil.isNotBlank(taskMaterialStatusArr)) {
                    // 2、将传递的材料标识和材料状态的字符串首尾的[]去除，然后组合成数组
                    taskMaterialStatusArr = taskMaterialStatusArr.replace("[", "").replace("]", "");
                    taskMaterialGuidsArr = taskMaterialGuidsArr.replace("[", "").replace("]", "");
                    String[] taskMaterialGuids = taskMaterialGuidsArr.split(","); // 材料标识数组
                    String[] taskMaterialStatus = taskMaterialStatusArr.split(","); // 材料状态数组
                    materialCount = taskMaterialGuids.length;
                    for (int i = 0; i < materialCount; i++) {
                        String materialGuid = taskMaterialGuids[i];
                        String materialStatus = taskMaterialStatus[i];
                        materialGuid = materialGuid.replaceAll("\"", "");
                        materialStatus = materialStatus.replaceAll("\"", "");
                        // 3、获取材料实例信息
                        AuditSpIMaterial auditSpIMaterial = iAuditSpIMaterial.getSpIMaterialByrowguid(materialGuid)
                                .getResult();
                        if (auditSpIMaterial != null) {
                            String isRongque = auditSpIMaterial.getAllowrongque(); // 材料容缺状态
                            // 如果是必要非容缺材料
                            if (ZwfwConstant.NECESSITY_SET_YES.equals(auditSpIMaterial.getNecessity())
                                    && !ZwdtConstant.STRING_YES.equals(isRongque)) {
                                int status = Integer.parseInt(materialStatus); // 套餐办件材料表当前材料提交的状态
                                int count = iAttachService.getAttachCountByClientGuid(auditSpIMaterial.getCliengguid());
                                String paperUnnecrsstity = iConfigService.getFrameConfigValue("AS_CHECK_MATERIAL"); // 纸质必要材料是否要提交
                                paperUnnecrsstity = StringUtil.isBlank(paperUnnecrsstity)
                                        ? ZwfwConstant.CONSTANT_STR_ZERO
                                        : paperUnnecrsstity;
                                // 必要非容缺材料未提交或不存在附件：
                                if ((status != ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC
                                        && status != ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC)
                                        || count <= 0) {
                                    // 3.1、如果外网不需要提交纸质材料，需要判断材料提交方式
                                    if (ZwfwConstant.CONSTANT_STR_ONE.equals(paperUnnecrsstity)) {
                                        // 如果材料提交方式是“提交电子文件”，或是“提交电子或纸质文件”：
                                        if ("10".equals(auditSpIMaterial.getSubmittype())
                                                || "40".equals(auditSpIMaterial.getSubmittype())) {
                                            noSubmitNum++;
                                        }
                                    }
                                    // 3.2、如果外网需要提交纸质材料
                                    else {
                                        noSubmitNum++;
                                    }
                                }
                            }
                        }
                    }
                }
                // 4、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("nosubmitnum", noSubmitNum);
                log.info("=======结束调用checkAllMaterialIsSubmit接口=======");
                return JsonUtils.zwdtRestReturn("1", "检查材料是否都提交成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======checkAllMaterialIsSubmit接口参数：params【" + params + "】=======");
            log.info("=======checkAllMaterialIsSubmit异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "检查材料是否都提交失败：" + e.getMessage(), "");
        }
    }

    /**
     * 提交套餐办件接口
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/submitMaterial", method = RequestMethod.POST)
    public String submitMaterial(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用submitMaterial接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取主题标识
                // String businessGuid = obj.getString("businessguid");
                // 1.2、获取主题实例标识
                String biGuid = obj.getString("biGuid");
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理开始<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 1.3、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    ZwdtUserAuthValid zwdtUserAuthValid = new ZwdtUserAuthValid();
                    if (!zwdtUserAuthValid.userValid(auditOnlineRegister, biGuid, ZwdtConstant.USERVALID_SPI_INS)) {
                        return JsonUtils.zwdtRestReturn("0", "UserValid身份验证失败！", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                }
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 2、获取子申报标识
                String subAppGuid = iAuditSpISubapp.getSubappByBIGuid(biGuid).getResult().get(0).getRowguid();
                // 3、更新AUDIT_ONLINE_PROJECT状态为外网申报已提交
                Map<String, String> updateFieldMap = new HashMap<String, String>(16);
                Map<String, String> updateDateFieldMap = new HashMap<String, String>(16);// 要更新的时间类型字段
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                String flowsn = iHandleFlowSn.getFlowsn("套餐编号", "TC").getResult();
                updateFieldMap.put("flowsn=", flowsn);
                updateFieldMap.put("status=", String.valueOf(ZwfwConstant.BANJIAN_STATUS_WWYTJ));// 外网申报已提交
                updateDateFieldMap.put("applydate=",
                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                sqlConditionUtil.eq("sourceguid", biGuid);
                iAuditOnlineProject.updateOnlineProject(updateFieldMap, updateDateFieldMap, sqlConditionUtil.getMap());
                // 4、更新子申报状态为待预审
                iAuditSpISubapp.updateSubapp(subAppGuid, ZwfwConstant.LHSP_Status_DYS, null);
                AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biGuid).getResult();
                AuditSpBusiness auditSpBusiness = iAuditSpBusiness
                        .getAuditSpBusinessByRowguid(auditSpInstance.getBusinessguid()).getResult();
                // 向指定用户发送待办
                String handleUrl = "/" + auditSpBusiness.getHandleURL() + "/handleintegratedinquirydetail?biGuid="
                        + biGuid + "&subappGuid=" + subAppGuid;
                String title = "【一件事预审】" + auditSpInstance.getItemname() + "(" + auditSpInstance.getApplyername() + ")";
                iAuditOnlineMessages.sendMsg("统一收件人员", title, auditSpInstance.getApplyerguid(),
                        auditSpInstance.getApplyername(), auditSpInstance.getAreacode(), handleUrl, subAppGuid,
                        "zwfwMsgurl", null);
                log.info("=======结束调用submitMaterial接口=======");
                return JsonUtils.zwdtRestReturn("1", "保存成功！", "");
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "主题申报基本信息提交异常：" + e.getMessage(), "");
        }
    }

    /**
     * 提交补正材料接口
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/submitBusinessMaterial", method = RequestMethod.POST)
    public String submitBusinessMaterial(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用submitBusinessMaterial接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取主题实例标识
                String biGuid = obj.getString("projectguid");
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理开始<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 1.2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    ZwdtUserAuthValid zwdtUserAuthValid = new ZwdtUserAuthValid();
                    if (!zwdtUserAuthValid.userValid(auditOnlineRegister, biGuid, ZwdtConstant.USERVALID_SPI_INS)) {
                        return JsonUtils.zwdtRestReturn("0", "UserValid身份验证失败！", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                }
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 2、获取子申报信息
                List<AuditSpISubapp> auditSpISubapps = iAuditSpISubapp.getSubappByBIGuid(biGuid).getResult();
                AuditSpISubapp auditSpISubapp = auditSpISubapps.get(0);
                String subAppGuid = auditSpISubapp.getRowguid();
                // 3、更新子申报状态为已受理
                iAuditSpISubapp.updateSubapp(subAppGuid, ZwfwConstant.LHSP_Status_YSJ, null);
                // 4、更新所有的材料状态为已补正
                iAuditSpIMaterial.updateSpIMaterialPatchStatus(subAppGuid);
                // 5、更新外网办件状态为已受理
                if (ZwfwConstant.APPLY_WAY_NETSBYS.equals(auditSpISubapp.getApplyerway())) {
                    Map<String, String> updateFieldMap = new HashMap<String, String>(16);
                    Map<String, String> updateDateFieldMap = new HashMap<String, String>(16);// 更新日期类型的字段
                    updateFieldMap.put("status=", String.valueOf(ZwfwConstant.BANJIAN_STATUS_YSL));
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("sourceguid", auditSpISubapp.getBiguid());
                    sql.eq("applyerguid", auditSpISubapp.getApplyerguid());
                    iAuditOnlineProject.updateOnlineProject(updateFieldMap, updateDateFieldMap, sql.getMap());
                }
                log.info("=======结束调用submitBusinessMaterial接口=======");
                return JsonUtils.zwdtRestReturn("1", "提交材料成功", "");
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======submitBusinessMaterial接口参数：params【" + params + "】=======");
            log.info("=======submitBusinessMaterial异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "提交材料失败：" + e.getMessage(), "");
        }
    }

    /**
     * 套餐评价接口
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/private/evaluateBusiness", method = RequestMethod.POST)
    public String evaluateBusiness(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用evaluateBusiness接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取办件标识
                String projectGuid = obj.getString("projectguid");
                // 1.2、获取评价等级
                String evaluateLevel = obj.getString("evaluatelevel");
                // 1.3、获取评价内容
                String evaluateNote = obj.getString("evaluatenote");
                // 2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理开始<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    ZwdtUserAuthValid zwdtUserAuthValid = new ZwdtUserAuthValid();
                    if (!zwdtUserAuthValid.userValid(auditOnlineRegister, projectGuid,
                            ZwdtConstant.USERVALID_SPI_INS)) {
                        return JsonUtils.zwdtRestReturn("0", "UserValid身份验证失败！", "");
                    }
                    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    // 3、新增评价数据
                    AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
                            .getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
                    AuditOnlineEvaluat auditOnlineEvaluat = new AuditOnlineEvaluat();
                    auditOnlineEvaluat.setRowguid(UUID.randomUUID().toString());
                    auditOnlineEvaluat.setClientidentifier(projectGuid); // 评价数据
                                                                         // 关联业务标识
                    auditOnlineEvaluat.setClienttype(ZwdtConstant.EVALUATEOBJECT_BUSINESS);// 评价对象
                                                                                           // 10：办件、20：套餐、30：排队号
                    auditOnlineEvaluat.setEvaluatecontent(evaluateNote); // 评价内容
                    auditOnlineEvaluat.setEvaluatedate(new Date()); // 评价时间
                    auditOnlineEvaluat.setEvaluatetype(ZwdtConstant.EVALUATETYPE_ONLINE);// 评价方式
                                                                                         // 10：网上申报、
                                                                                         // 20：短信、30：评价器
                    auditOnlineEvaluat.setEvaluateuserguid(auditOnlineRegister.getAccountguid());// 评价用户
                    auditOnlineEvaluat.setEvaluateusername(auditOnlineIndividual.getClientname());
                    auditOnlineEvaluat.setSatisfied(evaluateLevel); // 评价等级
                                                                    // 1：非常满意、2：满意、3：一般、4：不满意、5：非常不满意
                    iAuditOnlineEvaluat.addAuditOnineEvaluat(auditOnlineEvaluat);
                    // 4、更新是否已评价字段
                    Map<String, String> updateFieldMap = new HashMap<String, String>(16);
                    Map<String, String> updateDateFieldMap = new HashMap<String, String>(16);// 要更新的时间类型字段
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    updateFieldMap.put("is_evaluat=", String.valueOf(ZwfwConstant.CONSTANT_STR_ONE));
                    sqlConditionUtil.eq("sourceguid", projectGuid);
                    iAuditOnlineProject.updateOnlineProject(updateFieldMap, updateDateFieldMap,
                            sqlConditionUtil.getMap());
                    log.info("=======结束调用evaluateBusiness接口=======");
                    return JsonUtils.zwdtRestReturn("1", "评价成功", "");
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======evaluateBusiness接口参数：params【" + params + "】=======");
            log.info("=======evaluateBusiness异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "评价失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取套餐办件中所有纸质材料接口（申报告知页面调用）
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getPaperMaterial", method = RequestMethod.POST)
    public String getPaperMaterial(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getPaperMaterial接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取主题实例标识
                String biGuid = obj.getString("biguid");
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理开始<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 1.2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    ZwdtUserAuthValid zwdtUserAuthValid = new ZwdtUserAuthValid();
                    if (!zwdtUserAuthValid.userValid(auditOnlineRegister, biGuid, ZwdtConstant.USERVALID_SPI_INS)) {
                        return JsonUtils.zwdtRestReturn("0", "UserValid身份验证失败！", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                }
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 2、获取子申报信息
                AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByBIGuid(biGuid).getResult().get(0);
                // 3、获取子申报对应的材料实例信息
                List<AuditSpIMaterial> auditSpIMaterials = iAuditSpIMaterial
                        .getSpIMaterialBySubappGuid(auditSpISubapp.getRowguid()).getResult();
                List<JSONObject> materialJsonList = new ArrayList<JSONObject>();
                for (AuditSpIMaterial auditSpIMaterial : auditSpIMaterials) {
                    // 4、返回材料信息
                    int submitType = Integer.parseInt(auditSpIMaterial.getSubmittype());
                    // 4.1、需要返回的是提交方式包含纸质方式的材料
                    if (submitType != WorkflowKeyNames9.SubmitType_Submit
                            && submitType != WorkflowKeyNames9.SubmitType_Submit_Or_PaperSubmit) {
                        JSONObject materialJson = new JSONObject();
                        materialJson.put("projectmaterialguid", auditSpIMaterial.getRowguid());// 材料标识
                        materialJson.put("projectmaterialname", auditSpIMaterial.getMaterialname());// 材料名称
                        String necessity = "0";
                        if (ZwfwConstant.NECESSITY_SET_YES.equals(auditSpIMaterial.getNecessity())) {
                            necessity = "1";
                        }
                        materialJson.put("necessary", necessity);// 是否必须
                        if ("0".equals(auditSpIMaterial.getShared())) {
                            // 4.1.1、如果材料是非共享材料
                            List<AuditSpITask> auditSpITasks = iAuditSpITask
                                    .getTaskInstanceBySubappGuid(auditSpIMaterial.getSubappguid()).getResult();
                            AuditTaskMaterial auditTaskMaterial = null;
                            for (AuditSpITask auditSpITask : auditSpITasks) {
                                auditTaskMaterial = iAuditTaskMaterial.selectTaskMaterialByTaskGuidAndMaterialId(
                                        auditSpITask.getTaskguid(), auditSpIMaterial.getMaterialguid()).getResult();
                                if (auditTaskMaterial != null) {
                                    break;
                                }
                            }
                            if (auditTaskMaterial != null) {
                                String materialsource;
                                if (auditTaskMaterial == null
                                        || StringUtil.isBlank(auditTaskMaterial.getFile_source())) {
                                    materialsource = "申请人自备";
                                }
                                else {
                                    materialsource = iCodeItemsService.getItemTextByCodeName("来源渠道",
                                            auditTaskMaterial.getFile_source());
                                }
                                materialJson.put("materialsource", materialsource);
                                materialJson.put("submittype", iCodeItemsService.getItemTextByCodeName("提交方式",
                                        String.valueOf(auditTaskMaterial.getSubmittype())));
                                materialJson.put("ordernum", auditTaskMaterial.getOrdernum());
                            }
                        }
                        else {
                            // 4.1.2、如果材料是共享材料
                            // 设置共享材料来源
                            AuditSpShareMaterial auditSpShareMaterial = iAuditSpShareMaterial
                                    .getAuditSpShareMaterialByShareMaterialGuid(auditSpIMaterial.getMaterialguid(),
                                            auditSpIMaterial.getBusinessguid())
                                    .getResult();
                            String spMaterialSource = "";
                            if (StringUtil.isBlank(auditSpShareMaterial.getFile_source())) {
                                spMaterialSource = "申请人自备";
                            }
                            else {
                                spMaterialSource = iCodeItemsService.getItemTextByCodeName("来源渠道",
                                        auditSpShareMaterial.getFile_source());
                            }
                            materialJson.put("materialsource", spMaterialSource);// 来源
                                                                                 // （共享材料默认为申请人自备）
                            materialJson.put("ordernum", auditSpIMaterial.getOrdernum());
                            materialJson.put("submittype", iCodeItemsService.getItemTextByCodeName("提交方式",
                                    String.valueOf(auditSpIMaterial.getSubmittype())));
                        }
                        materialJsonList.add(materialJson);
                    }
                }
                // 5、对返回的材料列表进行排序
                Collections.sort(materialJsonList, new Comparator<JSONObject>()
                {
                    @Override
                    public int compare(JSONObject o1, JSONObject o2) {
                        Integer necessary1 = Integer.parseInt(o1.get("necessary").toString());
                        Integer necessary2 = Integer.parseInt(o2.get("necessary").toString());
                        int comNecessity = necessary2.compareTo(necessary1);
                        int ret = comNecessity;
                        if (comNecessity == 0) {
                            Integer ordernum1 = o1.get("ordernum") == null ? 0
                                    : Integer.parseInt(o1.get("ordernum").toString());
                            Integer ordernum2 = o2.get("ordernum") == null ? 0
                                    : Integer.parseInt(o2.get("ordernum").toString());
                            ret = ordernum2.compareTo(ordernum1);
                        }
                        return ret;
                    }
                });
                // 6、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("materiallist", materialJsonList);
                log.info("=======结束调用getPaperMaterial接口=======");
                return JsonUtils.zwdtRestReturn("1", "套餐中纸质相关信息成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getPaperMaterial接口参数：params【" + params + "】=======");
            log.info("=======getPaperMaterial异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "套餐中纸质相关信息失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取套餐办理过程中产生的文书接口（套餐详细页面调用） 循环查询该套餐下的所有办件，并查询出所有文书。与办件不同的是，套餐查询出的文书有前缀套餐名。
     *
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/getBusinessProjectDoclist", method = RequestMethod.POST)
    public String getBusinessProjectDoclist(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getBusinessProjectDoclist接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取主题实例标识
                String biGuid = obj.getString("projectguid");
                // 1.2、获取辖区编码
                String areaCode = obj.getString("areacode");
                // 1.3、获取主题标识
                String businessGuid = obj.getString("businessguid");
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理开始<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 1.4、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    ZwdtUserAuthValid zwdtUserAuthValid = new ZwdtUserAuthValid();
                    if (!zwdtUserAuthValid.userValid(auditOnlineRegister, biGuid, ZwdtConstant.USERVALID_SPI_INS)) {
                        return JsonUtils.zwdtRestReturn("0", "UserValid身份验证失败！", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                }
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 2、获取主题基本信息
                AuditSpBusiness auditSpBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessGuid)
                        .getResult();
                if (auditSpBusiness != null) {
                    if (StringUtil.isNotBlank(auditSpBusiness.getAreacode())) {
                        areaCode = auditSpBusiness.getAreacode();
                    }
                    // 3、获取此主题实例关联的所有办件
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    sqlConditionUtil.eq("biguid", biGuid);
                    // sqlConditionUtil.eq("areacode", areaCode);
                    List<AuditProject> auditProjects = iAuditProject
                            .getAuditProjectListByCondition(sqlConditionUtil.getMap()).getResult();
                    List<JSONObject> docJsonList = new ArrayList<JSONObject>();
                    for (AuditProject auditProject : auditProjects) {
                        // 4、获取办件下的文书快照信息
                        List<AuditProjectDocSnap> auditProjectDocSnaps = iAuditProjectDocSnap
                                .selectDocSnapByProjectGuid(auditProject.getRowguid()).getResult();
                        for (AuditProjectDocSnap auditProjectDocSnap : auditProjectDocSnaps) {
                            JSONObject docJson = new JSONObject();
                            // 5、文书名称默认需要事项名称
                            String taskName = auditProject.getProjectname();
                            taskName = taskName.length() > 45 ? taskName.substring(0, 45) + "..." : taskName;
                            String docTitle = iCodeItemsService.getItemTextByCodeName("文书类型",
                                    String.valueOf(auditProjectDocSnap.getDoctype()));
                            docJson.put("docname", taskName + "_" + docTitle);
                            docJson.put("doctitle", auditProject.getProjectname() + "_" + docTitle);
                            // 6、判断审批系统中文书使用NTKO插件或者是HTML形式
                            if (StringUtil.isBlank(auditProjectDocSnap.getDocattachguid())) {
                                // 6.1、HTML形式
                                docJson.put("doccontent", "doc?rowguid=" + auditProjectDocSnap.getRowguid());
                                docJson.put("type", 1);
                            }
                            else {
                                // 6.2、NTKO形式
                                String url = ConfigUtil.getConfigValue("officeweb365url");
                                FrameAttachStorage frameAttachStorage = iAttachService
                                        .getAttach(auditProjectDocSnap.getDocattachguid());
                                String attachguid = "";
                                String documentType = "";
                                String fname = "";
                                if (frameAttachStorage != null) {
                                    attachguid = frameAttachStorage.getAttachGuid();
                                    documentType = frameAttachStorage.getDocumentType();
                                    fname = attachguid + documentType;
                                    url = url + "fname=" + fname + "&furl=" + WebUtil.getRequestCompleteUrl(request)
                                            + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                            + attachguid;
                                    docJson.put("doccontent", url);
                                }
                                docJson.put("type", 0);
                            }
                            docJsonList.add(docJson);
                        }
                    }
                    // 7、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("doclist", docJsonList);
                    int totalcount = docJsonList == null ? 0 : docJsonList.size();
                    dataJson.put("totalcount", totalcount);
                    log.info("=======结束调用getBusinessProjectDoclist接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取流程中产生文书成功", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "此主题不存在！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getBusinessProjectDoclist接口参数：params【" + params + "】=======");
            log.info("=======getBusinessProjectDoclist异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取流程中产生文书失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取文书内容的接口
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getDocdetail", method = RequestMethod.POST)
    public String getDocdetail(@RequestBody String params) {
        try {
            log.info("=======开始调用getDocdetail接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String rowguid = obj.getString("rowguid");
                // 2、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                // 3、获取文书快照信息
                AuditProjectDocSnap auditProjectDocSnap = iAuditProjectDocSnap.selectDocSnapByRowGuid(rowguid)
                        .getResult();
                if (StringUtil.isNotBlank(auditProjectDocSnap.getDochtml())) {
                    dataJson.put("docdetail", auditProjectDocSnap.getDochtml());
                }
                else {
                    dataJson.put("docdetail", auditProjectDocSnap.getDocattachguid());
                }
                log.info("=======结束调用getDocdetail接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取文书信息成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getDocdetail接口参数：params【" + params + "】=======");
            log.info("=======getDocdetail异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取文书信息失败：" + e.getMessage(), "");
        }
    }

    /**
     * 办件结果详情的接口
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getBusinessProjectResult", method = RequestMethod.POST)
    public String getBusinessProjectResult(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getProjectResult接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1、获取办件标识
                String biGuid = obj.getString("projectguid");
                // 1.2、获取区域编码
                String areaCode = obj.getString("areacode");
                // 获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 2、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    List<JSONObject> resultlist = new ArrayList<JSONObject>();
                    //
                    List<AuditSpITask> auditSpITasks = iAuditSpITask.getSpITaskByBIGuid(biGuid).getResult();

                    if (auditSpITasks.size() > 0) {
                        for (AuditSpITask auditSpITask : auditSpITasks) {
                            if (StringUtil.isNotBlank(auditSpITask.getProjectguid())) {
                                // 3、获取办件
                                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                                sqlConditionUtil.eq("rowguid", auditSpITask.getProjectguid());
                                String fields = " rowguid,taskguid,projectname,status,charge_when,operatedate,applyertype,applyername,applyeruserguid,biguid,Certrowguid ";
                                AuditProject auditProject = iAuditProject
                                        .getAuditProjectByRowGuid(fields, auditSpITask.getProjectguid(), areaCode)
                                        .getResult();
                                if (auditProject != null) {
                                    // 4、获取证照实例
                                    CertInfo certInfo = iCertInfoExternal
                                            .getCertInfoByRowguid(auditProject.getCertrowguid());
                                    if (certInfo != null) {
                                        log.info("Certrowguid：" + auditProject.getCertrowguid());
                                        // 5、获取结果对应的附件
                                        List<JSONObject> attachList = iCertAttachExternal
                                                .getAttachList(certInfo.getCertcliengguid(), ZwdtConstant.CERTAPPKEY);
                                        if (attachList != null && attachList.size() > 0) {
                                            for (JSONObject attachJson : attachList) {
                                                JSONObject resultJson = new JSONObject();
                                                resultJson.put("resultattachname", attachJson.get("attachname"));
                                                resultJson.put("resultattachurl",
                                                        WebUtil.getRequestCompleteUrl(request)
                                                                + "/rest/zwdtAttach/readCertAttach?attachguid="
                                                                + attachJson.getString("attachguid") + "&filename="
                                                                + attachJson.get("attachname"));// 附件图标
                                                resultlist.add(resultJson);
                                            }
                                        }

                                        // 5、获取结果对应的附件
                                        List<FrameAttachInfo> attachCpoyList = iAttachService
                                                .getAttachInfoListByGuid(certInfo.getCopycertcliengguid());
                                        log.info("Copycertcliengguid：" + certInfo.getCopycertcliengguid());
                                        if (attachCpoyList != null && attachCpoyList.size() > 0) {
                                            for (FrameAttachInfo attach : attachCpoyList) {
                                                log.info("办件结果文件：" + attach.getAttachFileName());
                                                JSONObject resultJson = new JSONObject();
                                                resultJson.put("resultattachname", attach.getAttachFileName());
                                                resultJson.put("resultattachurl",
                                                        WebUtil.getRequestCompleteUrl(request)
                                                                + "/rest/zwdtAttach/readCertAttach?attachguid="
                                                                + attach.getAttachGuid() + "&filename="
                                                                + attach.getAttachFileName());// 附件图标
                                                resultlist.add(resultJson);
                                            }
                                        }

                                    }
                                    else {
                                        List<FrameAttachInfo> listattach = iAttachService
                                                .getAttachInfoListByGuid(auditProject.getRowguid());
                                        // 对于存储到本地附件库的结果，返回一个值供前台切换路径
                                        for (FrameAttachInfo attachInfo : listattach) {
                                            JSONObject resultJson = new JSONObject();
                                            resultJson.put("resultattachname", attachInfo.getAttachFileName());
                                            resultJson.put("resultattachurl",
                                                    WebUtil.getRequestCompleteUrl(request)
                                                            + "/rest/zwdtAttach/readCertAttach?attachguid="
                                                            + attachInfo.getAttachGuid() + "&filename="
                                                            + attachInfo.getAttachFileName());// 附件图标
                                            resultlist.add(resultJson);
                                        }
                                    }
                                    dataJson.put("resultlist", resultlist);
                                }
                            }
                        }
                    }

                    log.info("=======结束调用getProjectResult接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取办件结果详情成功", dataJson.toString());

                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getProjectResult接口参数：params【" + params + "】=======");
            log.info("=======getProjectResult异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取办件结果详情异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取套餐流转信息（套餐详细页面调用）
     *
     * @param params
     *            接口的入参
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/private/getBusinessProjectFlowlist", method = RequestMethod.POST)
    public String getBusinessProjectFlowlist(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getBusinessProjectFlowlist接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取主题实例标识
                String biGuid = obj.getString("projectguid");
                // 1.2、获取辖区编码
                String areacode = obj.getString("areacode");
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理开始<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    ZwdtUserAuthValid zwdtUserAuthValid = new ZwdtUserAuthValid();
                    if (!zwdtUserAuthValid.userValid(auditOnlineRegister, biGuid, ZwdtConstant.USERVALID_SPI_INS)) {
                        return JsonUtils.zwdtRestReturn("0", "UserValid身份验证失败！", "");
                    }
                    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    // 2、获取用户个人信息
                    AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
                            .getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
                    // 3、获取AUDIT_ONLINE_PROJECT表数据
                    AuditOnlineProject auditOnlineProject = iAuditOnlineProject
                            .getOnlineProjectByApplyerGuid(biGuid, auditOnlineIndividual.getApplyerguid()).getResult();
                    // 4、定义流转球的JSON数据
                    // 4.1、网上申请圆球设置为正在处理状态 0:已完成 1：正在处理 2：未处理 （各个状态同下）
                    JSONObject wssqJson = new JSONObject();
                    wssqJson.put("nodename", "网上申请");
                    wssqJson.put("nodestatus", "1");
                    wssqJson.put("order", "1");
                    // 4.2、受理
                    JSONObject slJson = new JSONObject();
                    slJson.put("nodename", "受理");
                    slJson.put("nodestatus", "2");
                    slJson.put("order", "2");
                    // 4.3、办理
                    JSONObject blJson = new JSONObject();
                    blJson.put("nodename", "审批中");
                    blJson.put("nodestatus", "2");
                    blJson.put("order", "3");
                    // 4.4、办结
                    JSONObject bjJson = new JSONObject();
                    bjJson.put("nodename", "办结");
                    bjJson.put("nodestatus", "2");
                    bjJson.put("order", "4");
                    // 4、获取主题实例下对应的办件
                    SqlConditionUtil conditionMapsql = new SqlConditionUtil();
                    conditionMapsql.eq("biguid", biGuid);
                    conditionMapsql.eq("areacode", areacode);
                    // 5、获取主题实例下对应的办件信息
                    List<AuditProject> auditProjects = iAuditProject
                            .getAuditProjectListByCondition(conditionMapsql.getMap()).getResult();
                    if (auditProjects != null && auditProjects.size() > 0) {
                        // 5.1、如果有办件说明已受理
                        wssqJson.put("nodestatus", "0");
                        slJson.put("nodestatus", "0");
                        boolean issp = false; // 是否在审批中
                        for (AuditProject auditProject : auditProjects) {
                            int projectStatus = auditProject.getStatus();
                            if (projectStatus < ZwfwConstant.BANJIAN_STATUS_ZCBJ) {
                                // 5.2、如果有一个办件状态是小于90（正常办结），则是审批中
                                issp = true;
                                break;
                            }
                        }
                        if (issp) {
                            // 5.3、审批中 （办理处理中、办结未处理）
                            blJson.put("nodestatus", "1");
                            bjJson.put("nodestatus", "2");
                        }
                        else {
                            // 5.4、已办结 （办理已完成、办结已完成）
                            blJson.put("nodestatus", "0");
                            bjJson.put("nodestatus", "1");
                        }
                    }
                    else {
                        // 5.5、如果没有办件说明受理处理中
                        wssqJson.put("nodestatus", "0");
                        slJson.put("nodestatus", "1");
                        blJson.put("nodestatus", "2");
                        bjJson.put("nodestatus", "2");
                    }
                    // 6、获取办件状态
                    String status = auditOnlineProject.getStatus();
                    if (StringUtil.isNotBlank(status)) {
                        // 6.1、如果办件状态大于正常办结
                        if (Integer.parseInt(status) >= ZwfwConstant.BANJIAN_STATUS_ZCBJ) {
                            wssqJson.put("nodestatus", "0");
                            slJson.put("nodestatus", "0");
                            blJson.put("nodestatus", "0");
                            bjJson.put("nodestatus", "0");
                        }
                    }
                    // 7、定义返回JSON对象
                    List<JSONObject> nodeJsonList = new ArrayList<JSONObject>();
                    nodeJsonList.add(wssqJson);
                    nodeJsonList.add(slJson);
                    nodeJsonList.add(blJson);
                    nodeJsonList.add(bjJson);
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("nodelist", nodeJsonList);
                    log.info("=======结束调用getBusinessProjectFlowlist接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取办件流转信息成功", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getBusinessProjectFlowlist接口参数：params【" + params + "】=======");
            log.info("=======getBusinessProjectFlowlist异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取办件流转信息失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取扫码材料列表
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getScanBusinessMaterialList", method = RequestMethod.POST)
    public String getScanBusinessMaterialList(@RequestBody String params) {
        try {
            log.info("=======开始调用getScanBusinessMaterialList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                AuditTaskMaterial material = null;
                // 1.1、主题实例标识
                String biGuid = obj.getString("biGuid");
                // 1.2、子申报标识
                String subappguid = obj.getString("subappguid");
                String taskmaterialrowguid = obj.getString("taskmaterialrowguid");
                if (StringUtil.isNotBlank(taskmaterialrowguid)) {
                    material = iAuditTaskMaterial.getAuditTaskMaterialByRowguid(taskmaterialrowguid).getResult();
                }

                // 2、获取子申报信息 如果subappguid为空，则代表是套餐扫码上传，否则代表是并联审批扫码上传
                if (StringUtil.isBlank(subappguid)) {
                    AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByBIGuid(biGuid).getResult().get(0);
                    subappguid = auditSpISubapp.getRowguid();
                }
                // 3、获取材料实例信息
                List<AuditSpIMaterial> auditSpIMaterials = iAuditSpIMaterial.getSpIMaterialBySubappGuid(subappguid)
                        .getResult();
                // 4、获取事项实例信息
                List<AuditSpITask> auditSpITasks = iAuditSpITask.getSpITaskByBIGuid(biGuid).getResult();
                List<JSONObject> materialJsonList = new ArrayList<JSONObject>();
                // 5、获取材料基本信息返回JSON
                for (AuditSpIMaterial auditSpIMaterial : auditSpIMaterials) {
                    // 5.1、获取材料基本信息
                    JSONObject materialJson = new JSONObject();
                    String materialname = "";
                    materialJson.put("projectmaterialguid", auditSpIMaterial.getRowguid()); // 材料标识
                    materialJson.put("status", auditSpIMaterial.getStatus()); // 材料状态
                    materialJson.put("taskmaterialguid", auditSpIMaterial.getMaterialguid());// 事项材料主键
                    materialJson.put("clientguid", auditSpIMaterial.getCliengguid());
                    materialJson.put("ordernum", auditSpIMaterial.getOrdernum());
                    materialJson.put("submittype",
                            iCodeItemsService.getItemTextByCodeName("提交方式", auditSpIMaterial.getSubmittype()));
                    // 5.1.1、如果该材料不是共享材料,则获取事项材料的信息
                    if ("0".equals(auditSpIMaterial.getShared())) {
                        materialJson.put("sharematerialiguid", ""); // 共享材料实例关联标识
                        AuditTaskMaterial auditTaskMaterial = null;
                        for (AuditSpITask auditSpITask : auditSpITasks) {
                            // 5.1.2、通过事项标识及材料业务唯一标识获取到事项中的材料信息
                            auditTaskMaterial = iAuditTaskMaterial.selectTaskMaterialByTaskGuidAndMaterialId(
                                    auditSpITask.getTaskguid(), auditSpIMaterial.getMaterialguid()).getResult();
                            if (auditTaskMaterial != null) {
                                materialname = auditTaskMaterial.getStr("materialname");
                            }
                        }
                    }
                    // 5.1.3、如果该材料是共享材料,则获取共享材料的信息
                    if ("1".equals(auditSpIMaterial.getShared())) {
                        materialJson.put("sharematerialiguid", auditSpIMaterial.getCertinfoinstanceguid()); // 共享材料实例关联标识
                        // 5.1.3.1、获取材料与共享材料的对应关系
                        List<AuditSpShareMaterialRelation> auditSpShareMaterialRelations = iAuditSpShareMaterialRelation
                                .selectByShareMaterialGuid(auditSpIMaterial.getBusinessguid(),
                                        auditSpIMaterial.getMaterialguid())
                                .getResult();
                        for (AuditSpShareMaterialRelation auditSpShareMaterialRelation : auditSpShareMaterialRelations) {
                            AuditTaskMaterial auditTaskMaterial = null;
                            for (AuditSpITask auditSpITask : auditSpITasks) {
                                // 5.1.3.2、通过事项标识及材料业务唯一标识获取到事项中的材料信息
                                auditTaskMaterial = iAuditTaskMaterial
                                        .selectTaskMaterialByTaskGuidAndMaterialId(auditSpITask.getTaskguid(),
                                                auditSpShareMaterialRelation.getMaterialid())
                                        .getResult();
                                if (auditTaskMaterial != null) {
                                    materialname = auditTaskMaterial.getStr("materialname");
                                }
                            }
                        }
                    }
                    AuditSpISubapp auditSpSubapp = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
                    Record auditSpBusiness = iAuditSpBusiness
                            .getAuditSpBusinessByRowguid(auditSpSubapp.getBusinessguid()).getResult();
                    if (ZwfwConstant.CONSTANT_STR_TWO.equals(auditSpBusiness.getStr("businesstype"))) {
                        materialname = auditSpIMaterial.getStr("materialname");
                    }
                    materialJson.put("projectmaterialname", materialname); // 材料名称
                    materialJsonList.add(materialJson);
                    // 6、对返回的材料列表进行排序
                    Collections.sort(materialJsonList, new Comparator<JSONObject>()
                    {
                        @Override
                        public int compare(JSONObject o1, JSONObject o2) {
                            Integer ordernum1 = o1.get("ordernum") == null ? 0
                                    : Integer.parseInt(o1.get("ordernum").toString());
                            Integer ordernum2 = o2.get("ordernum") == null ? 0
                                    : Integer.parseInt(o2.get("ordernum").toString());
                            int ret = ordernum2.compareTo(ordernum1);
                            return ret;
                        }
                    });

                }
                // 5.1.4、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("materiallist", materialJsonList);
                if (material != null) {
                    dataJson.put("taskmaterialname", material.getMaterialname()); // 材料标识
                }
                log.info("=======结束调用getScanBusinessMaterialList接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取初始化扫码材料列表成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }

        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getScanBusinessMaterialList接口参数：params【" + params + "】=======");
            log.info("=======getScanBusinessMaterialList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取初始化扫码材料列表失败：" + e.getMessage(), "");
        }
    }

    /**
     * 验证套餐是否可编辑接口
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/checkEditBusiness", method = RequestMethod.POST)
    public String checkEditBusiness(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用checkEditBusiness接口=======");
            // 1、接口的入参转化为JSON对象
            // 在待提交列表中，打开多个待提交套餐，同一个套餐可以提交多次
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取主题实例标识
                String biguid = obj.getString("biguid");
                boolean check = true;
                List<AuditSpISubapp> auditSpISubappList = iAuditSpISubapp.getSubappByBIGuid(biguid).getResult();
                AuditSpISubapp auditSpISubapp = null;
                if (auditSpISubappList != null && auditSpISubappList.size() > 0) {
                    auditSpISubapp = auditSpISubappList.get(0);
                }
                if (auditSpISubapp != null) {
                    String status = auditSpISubapp.getStatus();
                    if (StringUtil.isNotBlank(status) && ZwfwConstant.LHSP_Status_DYS.equals(status)) {
                        // 待预审状态不允许再提交
                        check = false;
                    }
                }
                JSONObject data = new JSONObject();
                data.put("check", check);
                log.info("=======结束调用checkEditBusiness接口=======");
                return JsonUtils.zwdtRestReturn("1", "验证成功", data.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======checkEditBusiness接口参数：params【" + params + "】=======");
            log.info("=======checkEditBusiness异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "验证失败", "");
        }
    }

    /**
     * 查询套餐下情形问题
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getBusinessElement", method = RequestMethod.POST)
    public String getBusinessElement(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getBusinessElement接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            Boolean isduoxuan = false;// 默认或者多可选选项
            Boolean isDefault = false;// 控制事项选项显隐
            Boolean isdisplay = false;// 控制显影
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取套餐标识
                String businessGuid = obj.getString("businessguid");
                // 2、调用接口查询套餐下情形
                SqlConditionUtil sql = new SqlConditionUtil();
                // 根节点问题
                sql.rightLike("preoptionguid", "start");
                if (StringUtil.isNotBlank(businessGuid)) {
                    sql.eq("businessguid", businessGuid);
                }

                sql.setOrder("ordernum desc,operatedate", "asc");
                // 要素
                List<AuditSpElement> auditSpElements = iAuditSpElementService.findListByCondition(sql.getMap())
                        .getResult();
                List<JSONObject> elementJsonList = new ArrayList<JSONObject>();
                // 查找必办事项的情形问题
                sql.clear();
                sql.eq("businessguid", businessGuid);
                sql.eq("elementguid", "root");

                // 查询共享默认选项表
                List<String> defaultsel = getDefaultAndShareOptionguid(businessGuid);
                if (auditSpElements != null && auditSpElements.size() > 0) {
                    for (AuditSpElement auditSpElement : auditSpElements) {
                        // 2.1、定义存储要素信息的json
                        JSONObject elementJson = new JSONObject();
                        // 2.2、查询问题下的选项
                        List<AuditSpOption> auditSpOptions = iAuditSpOptionService
                                .findListByElementIdWithoutNoName(auditSpElement.getRowguid()).getResult();

                        if (auditSpOptions != null && auditSpOptions.size() > 1) {
                            // 2.3、定义存储要素选项信息的List

                            isDefault = false;// 控制事项选项显隐
                            isdisplay = false;
                            // 共享默认表存在2个选项以上的是可选选项，否则是默认选项
                            isduoxuan = iauditspshareoption.spelementdefauleocanselect(auditSpElement.getRowguid(),
                                    businessGuid);

                            List<JSONObject> optionJsonList = new ArrayList<JSONObject>();
                            for (AuditSpOption auditSpOption : auditSpOptions) {
                                // 2.4、定义存储要素选项信息的json
                                if (isduoxuan && (!defaultsel.contains(auditSpOption.getRowguid()))) {
                                    continue;
                                }
                                JSONObject optionJson = new JSONObject();
                                optionJson.put("optionname", auditSpOption.getOptionname()); // 选项名称
                                optionJson.put("optionguid", auditSpOption.getRowguid()); // 选项标识
                                optionJson.put("task_id", auditSpOption.getTaskid()); // 选项标识

                                optionJson.put("sharevalue", iauditspshareoption
                                        .getSelectValueByOptionGuid(auditSpOption.getRowguid(), businessGuid));
                                if ((defaultsel.contains(auditSpOption.getRowguid()) && (!isduoxuan))) {

                                    optionJson.put("isselected", true);

                                    isDefault = true;
                                }
                                if (defaultsel.contains(auditSpOption.getRowguid()) && (!isduoxuan)) {
                                    isdisplay = true;
                                }
                                if (StringUtil.isNotBlank(auditSpOption.getOptionnote()) && (!isdisplay)) {
                                    optionJson.put("hasoptionnote", true);
                                    optionJson.put("optionnote", auditSpOption.getOptionnote());
                                }
                                optionJsonList.add(optionJson);
                            }
                            elementJson.put("optionlist", optionJsonList); // 要素选项列表

                            elementJson.put("elementquestion", auditSpElement.getElementname()); // 要素问题
                            elementJson.put("elementguid", auditSpElement.getRowguid()); // 要素唯一标识
                            elementJson.put("isdefault", isDefault);
                            elementJson.put("isdisplay", isdisplay);
                            if (StringUtil.isNotBlank(auditSpElement.getMultiselect())
                                    && ZwfwConstant.CONSTANT_STR_ZERO.equals(auditSpElement.getMultiselect())) {
                                elementJson.put("type", "radio");
                                elementJson.put("multitype", "单选");
                                elementJson.put("elementname", "【单选】" + auditSpElement.getElementname()); // 要素问题
                            }
                            else {
                                elementJson.put("type", "checkbox");
                                elementJson.put("multitype", "多选");
                                elementJson.put("elementname", "【多选】" + auditSpElement.getElementname()); // 要素问题
                            }
                            if (StringUtil.isNotBlank(auditSpElement.getElementnote()) && (!isdisplay)) {
                                elementJson.put("haselementnote", true);
                                elementJson.put("elementnote", auditSpElement.getElementnote());
                            }
                            elementJson.put("multiselect", auditSpElement.getMultiselect());
                            elementJsonList.add(elementJson);
                        }
                    }
                }

                // 必办事项的事项情形问题,找出elementguid 是root的选项的taskid
                List<AuditSpOption> auditSpOptions = iAuditSpOptionService.findListByCondition(sql.getMap())
                        .getResult();
                List<AuditTaskElement> audittaskelements = new ArrayList<AuditTaskElement>();
                if (auditSpOptions.size() > 0) {
                    AuditSpOption auditspoption = auditSpOptions.get(0);

                    String tasklists = "";
                    if (StringUtil.isNotBlank(auditspoption.getTaskid())) {
                        tasklists = auditspoption.getTaskid();
                    }
                    SqlConditionUtil sqltask = new SqlConditionUtil();
                    sqltask.isBlankOrValue("draft", ZwfwConstant.CONSTANT_STR_ZERO);
                    List<String> tasklist = new ArrayList<>();
                    if (StringUtil.isNotBlank(tasklists) && tasklists.contains(";")) {
                        tasklist = Arrays.asList(tasklists.split(";"));
                    }
                    else {
                        tasklist.add(tasklists);
                    }
                    sqltask.in("taskid", StringUtil.joinSql(tasklist));
                    sqltask.rightLike("preoptionguid", "start");
                    sqltask.setOrderDesc("ordernum");
                    sqltask.setOrderAsc("operatedate");
                    audittaskelements = iaudittaskelementservice.findListByCondition(sqltask.getMap()).getResult();
                    if (audittaskelements != null && audittaskelements.size() > 0) {
                        for (AuditTaskElement audittaskelement : audittaskelements) {
                            // 定义存储要素信息的Json
                            JSONObject subElementJson = new JSONObject();
                            isDefault = false;
                            isdisplay = false;

                            // 2.1、查询要素选项信息

                            List<AuditTaskOption> audittaskoptions = iaudittaskoptionservice
                                    .findListByElementIdWithoutNoName(audittaskelement.getRowguid()).getResult();
                            // 判断是可选选项还是默认选项
                            isduoxuan = iauditspshareoption.taskelementdefauleocanselect(audittaskelement.getRowguid(),
                                    businessGuid);
                            if (audittaskoptions != null && audittaskoptions.size() > 1) {

                                // 定义存储要素选项信息的List
                                List<JSONObject> optionJsonList = new ArrayList<JSONObject>();
                                for (AuditTaskOption audittaskoption : audittaskoptions) {
                                    if (isduoxuan && (!defaultsel.contains(audittaskoption.getRowguid()))) {
                                        continue;
                                    }
                                    // 定义存储要素选项的Json
                                    JSONObject optionJson = new JSONObject();
                                    optionJson.put("optionname", audittaskoption.getOptionname());
                                    optionJson.put("optionguid", audittaskoption.getRowguid());
                                    optionJson.put("sharevalue", iauditspshareoption
                                            .getSelectValueByOptionGuid(audittaskoption.getRowguid(), businessGuid));
                                    if ((defaultsel.contains(audittaskoption.getRowguid()) && (!isduoxuan))) {

                                        optionJson.put("isselected", true);

                                        isDefault = true;
                                    }
                                    // 只有一个默认选项隐藏问题
                                    if (defaultsel.contains(audittaskoption.getRowguid()) && (!isduoxuan)) {
                                        isdisplay = true;
                                    }

                                    optionJsonList.add(optionJson);
                                }
                                subElementJson.put("optionlist", optionJsonList); // 要素选项列表
                                subElementJson.put("elementquestion", audittaskelement.getElementquestion()); // 要素问题

                                subElementJson.put("elementguid", audittaskelement.getRowguid()); // 要素唯一标识
                                subElementJson.put("preoptionguid", audittaskelement.getPreoptionguid()); // 要素唯一标识
                                // subElementJson.put("task_id",
                                // audittaskelement.getTaskid()); // 要素唯一标识
                                if (StringUtil.isNotBlank(audittaskelement.getMultiselect())
                                        && ZwfwConstant.CONSTANT_STR_ZERO.equals(audittaskelement.getMultiselect())) {
                                    subElementJson.put("type", "radio");
                                    subElementJson.put("multitype", "单选");
                                    subElementJson.put("elementname", "【单选】" + audittaskelement.getElementname()); // 要素问题
                                }
                                else {
                                    subElementJson.put("type", "checkbox");
                                    subElementJson.put("multitype", "多选");
                                    subElementJson.put("elementname", "【多选】" + audittaskelement.getElementname()); // 要素问题
                                }
                                subElementJson.put("isdefault", isDefault);
                                subElementJson.put("isdisplay", isdisplay);
                                subElementJson.put("multiselect", audittaskelement.getMultiselect());
                                elementJsonList.add(subElementJson);
                            }
                        }
                    }
                }
                JSONObject dataJson = new JSONObject();
                JSONObject shareJson = new JSONObject();
                dataJson.put("elementlist", elementJsonList);
                // 20200120界面加载第一层问题时先查询所有共享的选项
                List<AuditSpShareoption> auditspshareoptionlist = iauditspshareoption
                        .getlistBybusinessguid(businessGuid).getResult();
                if (auditspshareoptionlist.size() > 0) {

                    Map<String, List<AuditSpShareoption>> shareselarr = auditspshareoptionlist.stream()
                            .filter(a -> a.getSelecttype().equals(ZwfwConstant.CONSTANT_STR_TWO))
                            .collect(Collectors.groupingBy(a -> a.getSharevalue()));
                    for (String key : shareselarr.keySet()) {
                        List<AuditSpShareoption> value = shareselarr.get(key);
                        if (value != null && value.size() > 0) {
                            shareJson.put(key,
                                    value.stream().map(a -> a.getOptionguid()).collect(Collectors.joining(";")));

                        }
                    }
                }
                dataJson.put("sharesel", shareJson);

                log.info("=======结束调用getBusinessElement接口=======");
                return JsonUtils.zwdtRestReturn("1", "查询成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getBusinessElement接口参数：params【" + params + "】=======");
            log.info("=======getBusinessElement异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "查询失败", "");
        }
    }

    /**
     * 查询情形下事项情形问题
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getBusinessElementByOption", method = RequestMethod.POST)
    public String getBusinessElementByOption(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getBusinessElementByOption接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String optionguid = obj.getString("optionguid");// 当前选项guid
                String businessguid = obj.getString("businessguid");// 套餐guid
                String selectedoptions = obj.getString("optionsselected");// 所有已经选择的选项
                String task_id = obj.getString("task_id");// 配了事项情形选项会关联task_id
                Boolean isDefault = false;// 控制共享选项不可选
                Boolean isdisplay = false;// 控制默认选项显隐
                Boolean isduoxuan = false;// 默认选项是否是多选选项

                // 查询默认选项集合
                List<String> defaultsel = getDefaultAndShareOptionguid(businessguid);
                // 根据已经选择的选项查询共享选项,查出所有共享选项标识,根据标识查出所有要选中的选项guid集合
                List<Record> seloptions = (List<Record>) JSONArray.parseArray(selectedoptions, Record.class);
                // seloptions.add(e)
                List<String> numlist = new ArrayList<>();
                Map<String, String> sharesel = new HashMap<>();

                if (seloptions != null && seloptions.size() > 0) {
                    for (Record record : seloptions) {
                        String opguid = record.getStr("optionguid");
                        AuditSpShareoption share = new AuditSpShareoption();
                        share = iauditspshareoption.findauditspshareopinion(opguid, ZwfwConstant.CONSTANT_STR_TWO,
                                businessguid);
                        if (share != null && StringUtil.isNotBlank(share.getSharevalue())) {
                            // 已选选项的sharevalue
                            if (!sharesel.containsKey(share.getSharevalue())) {
                                sharesel.put(share.getSharevalue(), opguid);
                                numlist.add(share.getSharevalue());
                            }

                        }
                    }
                }

                // 查询是否选在套餐子问题
                SqlConditionUtil sqlcase = new SqlConditionUtil();
                // 2、拼接查询条件
                if (StringUtil.isNotBlank(optionguid) && optionguid.indexOf(',') != -1) {
                    // 2.1 如果多选则需要分割
                    if (optionguid == "") {
                        sqlcase.eq("preoptionguid", "preoptionguid");
                    }
                    else {
                        String guids[] = optionguid.split(",");
                        String options = null;
                        List<String> list = new ArrayList<>();
                        if (guids != null && guids.length > 0) {
                            for (int i = 0; i < guids.length; i++) {
                                list.add(guids[i]);
                            }
                            options = "'" + StringUtil.join(list, "','") + "'";
                            sqlcase.in("preoptionguid", options);

                        }
                    }
                }
                else {
                    if (StringUtil.isNotBlank(optionguid)) {
                        sqlcase.eq("preoptionguid", optionguid);

                    }
                    else {
                        sqlcase.eq("preoptionguid", "preoptionguid");
                    }
                }
                sqlcase.isBlankOrValue("draft", "1");
                sqlcase.setOrder("ordernum desc,operatedate", "asc");
                // 2.2、 查询套餐问题

                List<AuditSpElement> auditSpElements = iAuditSpElementService.findListByCondition(sqlcase.getMap())
                        .getResult();
                List<JSONObject> subElementJsonList = new ArrayList<JSONObject>();
                if (auditSpElements != null && auditSpElements.size() > 0) {
                    for (AuditSpElement auditSpElement : auditSpElements) {
                        // 定义存储要素信息的Json
                        JSONObject subElementJson = new JSONObject();

                        // 3、查询要素选项信息
                        List<AuditSpOption> auditSpOptions = iAuditSpOptionService
                                .findListByElementIdWithoutNoName(auditSpElement.getRowguid()).getResult();
                        // 判断默认选项还是可选选项
                        isduoxuan = iauditspshareoption.spelementdefauleocanselect(auditSpElement.getRowguid(),
                                businessguid);
                        isdisplay = false;
                        isDefault = false;
                        if (auditSpOptions != null && auditSpOptions.size() > 1) {
                            // 3.1、定义存储要素选项信息的List
                            List<JSONObject> optionJsonList = new ArrayList<JSONObject>();
                            for (AuditSpOption auditSpOption : auditSpOptions) {
                                if (isduoxuan && (!defaultsel.contains(auditSpOption.getRowguid()))) {
                                    continue;
                                }
                                // 3.2、定义存储要素选项的Json
                                JSONObject optionJson = new JSONObject();
                                optionJson.put("optionname", auditSpOption.getOptionname()); // 选项名称
                                optionJson.put("optionguid", auditSpOption.getRowguid()); // 选项标识
                                optionJson.put("task_id", auditSpOption.getTaskid());
                                optionJson.put("sharevalue", iauditspshareoption
                                        .getSelectValueByOptionGuid(auditSpOption.getRowguid(), businessguid));

                                AuditSpShareoption share = new AuditSpShareoption();
                                share = iauditspshareoption.findauditspshareopinion(auditSpOption.getRowguid(),
                                        ZwfwConstant.CONSTANT_STR_TWO, businessguid);
                                if ((defaultsel.contains(auditSpOption.getRowguid()) && (!isduoxuan))) {
                                    optionJson.put("isselected", true);
                                    isDefault = true;

                                }
                                else if (share != null && numlist.contains(share.getSharevalue())) {
                                    optionJson.put("isselected", true);
                                    if ((!sharesel.isEmpty()) && !sharesel.get(share.getSharevalue())
                                            .contains(auditSpOption.getRowguid())) {
                                        isDefault = true;
                                    }
                                }

                                // 只有一个默认选项隐藏问题
                                if (defaultsel.contains(auditSpOption.getRowguid()) && (!isduoxuan)) {
                                    isdisplay = true;
                                }
                                if (StringUtil.isNotBlank(auditSpOption.getOptionnote()) && (!isdisplay)) {
                                    optionJson.put("hasoptionnote", true);
                                    optionJson.put("optionnote", auditSpOption.getOptionnote());
                                }
                                optionJsonList.add(optionJson);
                            }
                            subElementJson.put("optionlist", optionJsonList); // 要素选项列表
                            subElementJson.put("elementquestion", auditSpElement.getElementquestion()); // 要素问题
                            subElementJson.put("elementguid", auditSpElement.getRowguid()); // 要素唯一标识
                            subElementJson.put("preoptionguid", auditSpElement.getPreoptionguid()); // 前置要素唯一标识
                            if (StringUtil.isNotBlank(auditSpElement.getMultiselect())
                                    && ZwfwConstant.CONSTANT_STR_ZERO.equals(auditSpElement.getMultiselect())) {
                                subElementJson.put("type", "radio");
                                subElementJson.put("multitype", "单选");
                                subElementJson.put("elementname", "【单选】" + auditSpElement.getElementname()); // 要素问题
                            }
                            else {
                                subElementJson.put("type", "checkbox");
                                subElementJson.put("multitype", "多选");
                                subElementJson.put("elementname", "【多选】" + auditSpElement.getElementname()); // 要素问题
                            }
                            if (StringUtil.isNotBlank(auditSpElement.getElementnote()) && (!isdisplay)) {
                                subElementJson.put("haselementnote", true);
                                subElementJson.put("elementnote", auditSpElement.getElementnote());
                            }
                            subElementJson.put("isdefault", isDefault);
                            subElementJson.put("isdisplay", isdisplay);
                            subElementJson.put("multiselect", auditSpElement.getMultiselect());
                            subElementJsonList.add(subElementJson);
                        }
                    }
                }

                // 查出事项的问题有关联事项，查询事项情形

                List<AuditTaskElement> audittaskelements = new ArrayList<AuditTaskElement>();
                // if (tasklist.size() > 0) {
                SqlConditionUtil sqltask = new SqlConditionUtil();

                sqltask.isBlankOrValue("draft", ZwfwConstant.CONSTANT_STR_ZERO);
                if (StringUtil.isBlank(task_id)) {
                    // task_id为空标识是事项情形下的子问题
                    if (StringUtil.isNotBlank(optionguid) && optionguid.indexOf(',') != -1) {
                        String guids[] = optionguid.split(",");
                        String options = null;
                        List<String> list = new ArrayList<>();
                        if (guids != null && guids.length > 0) {
                            for (int i = 0; i < guids.length; i++) {
                                list.add(guids[i]);

                            }
                            options = "'" + StringUtil.join(list, "','") + "'";

                            sqltask.in("preoptionguid", options);

                        }
                    }
                    else {
                        sqltask.eq("preoptionguid", optionguid);
                    }

                }
                else {
                    // task_id不为空标识是套餐情形下的事项问题
                    // 如果关联了事项获事项问题
                    List<String> tasklist = new ArrayList<>();
                    if (StringUtil.isNotBlank(task_id) && task_id.contains(";")) {
                        tasklist = Arrays.asList(task_id.split(";"));
                    }
                    else {
                        tasklist.add(task_id);
                    }
                    sqltask.in("taskid", StringUtil.joinSql(tasklist));
                    sqltask.rightLike("preoptionguid", "start");
                }

                sqltask.setOrderDesc("ordernum");
                sqltask.setOrderAsc("operatedate");

                audittaskelements = iaudittaskelementservice.findListByCondition(sqltask.getMap()).getResult();

                // }

                if (audittaskelements != null && audittaskelements.size() > 0) {
                    for (AuditTaskElement audittaskelement : audittaskelements) {
                        // 定义存储要素信息的Json
                        JSONObject subElementJson = new JSONObject();
                        isDefault = false;
                        isdisplay = false;
                        // 2.1、查询要素选项信息
                        List<AuditTaskOption> audittaskoptions = iaudittaskoptionservice
                                .findListByElementIdWithoutNoName(audittaskelement.getRowguid()).getResult();
                        // 判断是可选选项还是默认选项
                        isduoxuan = iauditspshareoption.taskelementdefauleocanselect(audittaskelement.getRowguid(),
                                businessguid);
                        if (audittaskoptions != null && audittaskoptions.size() > 1) {

                            // 定义存储要素选项信息的List
                            List<JSONObject> optionJsonList = new ArrayList<JSONObject>();
                            for (AuditTaskOption audittaskoption : audittaskoptions) {
                                if (isduoxuan && (!defaultsel.contains(audittaskoption.getRowguid()))) {
                                    continue;
                                }
                                // 定义存储要素选项的Json
                                JSONObject optionJson = new JSONObject();
                                optionJson.put("optionname", audittaskoption.getOptionname());
                                optionJson.put("optionguid", audittaskoption.getRowguid());
                                optionJson.put("sharevalue", iauditspshareoption
                                        .getSelectValueByOptionGuid(audittaskoption.getRowguid(), businessguid));

                                optionJson.put("task_id", "");
                                // 是否存在默认选项

                                AuditSpShareoption share = new AuditSpShareoption();
                                share = iauditspshareoption.findauditspshareopinion(audittaskoption.getRowguid(),
                                        ZwfwConstant.CONSTANT_STR_TWO, businessguid);
                                if ((defaultsel.contains(audittaskoption.getRowguid()) && (!isduoxuan))) {

                                    optionJson.put("isselected", true);

                                }
                                else if (share != null && numlist.contains(share.getSharevalue())) {
                                    optionJson.put("isselected", true);
                                    if ((!sharesel.isEmpty()) && !sharesel.get(share.getSharevalue())
                                            .contains(audittaskoption.getRowguid())) {
                                        isDefault = true;
                                    }
                                }
                                // 只有一个默认选项隐藏问题
                                if (defaultsel.contains(audittaskoption.getRowguid()) && (!isduoxuan)) {
                                    isdisplay = true;
                                }
                                if (StringUtil.isNotBlank(audittaskoption.getOptionnote()) && (!isdisplay)) {
                                    optionJson.put("hasoptionnote", true);
                                    optionJson.put("optionnote", audittaskoption.getOptionnote());
                                }
                                optionJsonList.add(optionJson);
                            }
                            subElementJson.put("optionlist", optionJsonList); // 要素选项列表
                            subElementJson.put("elementquestion", audittaskelement.getElementquestion()); // 要素问题
                            subElementJson.put("elementname", audittaskelement.getElementname()); // 要素问题
                            subElementJson.put("elementguid", audittaskelement.getRowguid()); // 要素唯一标识
                            subElementJson.put("preoptionguid", audittaskelement.getPreoptionguid()); // 要素唯一标识
                            subElementJson.put("task_id", audittaskelement.getTaskid()); // 要素唯一标识
                            if (StringUtil.isNotBlank(audittaskelement.getMultiselect())
                                    && ZwfwConstant.CONSTANT_STR_ZERO.equals(audittaskelement.getMultiselect())) {
                                subElementJson.put("type", "radio");
                                subElementJson.put("multitype", "单选");
                                subElementJson.put("elementname", "【单选】" + audittaskelement.getElementname()); // 要素问题
                            }
                            else {
                                subElementJson.put("type", "checkbox");
                                subElementJson.put("multitype", "多选");
                                subElementJson.put("elementname", "【多选】" + audittaskelement.getElementname()); // 要素问题
                            }
                            if (StringUtil.isNotBlank(audittaskelement.getElementnote()) && (!isdisplay)) {
                                subElementJson.put("haselementnote", true);
                                subElementJson.put("elementnote", audittaskelement.getElementnote());
                            }
                            subElementJson.put("isdefault", isDefault);
                            subElementJson.put("isdisplay", isdisplay);
                            subElementJson.put("multiselect", audittaskelement.getMultiselect());
                            subElementJsonList.add(subElementJson);
                        }
                    }
                }
                JSONObject dataJson = new JSONObject();
                dataJson.put("subelementlist", subElementJsonList);
                log.info("=======结束调用getBusinessElementByOption接口=======");
                return JsonUtils.zwdtRestReturn("1", "查询成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getBusinessElementByOption接口参数：params【" + params + "】=======");
            log.info("=======getBusinessElementByOption异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "查询失败", "");
        }
    }

    /**
     * Step3查询材料列表
     *
     * @param params
     *            接口的入参
     * @return
     */
    /**
     * Step3查询材料列表
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getElementBusinessMaterialList", method = RequestMethod.POST)
    public String getElementBusinessMaterialList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getElementBusinessMaterialList接口=======");
            // 1、接口的入参转化为JSON对象
            // 在待提交列表中，打开多个待提交套餐，同一个套餐可以提交多次

            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取主题标识
                String businessGuid = obj.getString("businessguid");
                // 1.3、获取用户选项信息
                String subappGuid = obj.getString("subappguid");
                // 1.4、获取用户选项信息
                String biGuid = obj.getString("biguid");
                // 1.5、获取辖区编码
                String areaCode = obj.getString("areacode");

                String phaseGuid = obj.getString("phaseGuid");
                AuditOnlineRegister auditOnlineRegister = getOnlineRegister(request);

                // 所有选择 否 的guid列表
                List<String> optionlist = new ArrayList<>();
                // 所有事项id列表
                List<String> taskidlist = new ArrayList<>();
                // 将选择得数据添加到selected option表中去
                if (StringUtil.isBlank(phaseGuid)) {
                    List<AuditSpPhase> auditSpPhases = iAuditSpPhase.getSpPaseByBusinedssguid(businessGuid).getResult();
                    if (auditSpPhases != null && auditSpPhases.size() > 0) {
                        phaseGuid = auditSpPhases.get(0).getRowguid();
                    }
                }
                AuditSpSelectedOption auditspselectedoption = iAuditSpSelectedOptionService
                        .getSelectedoptionsBySubappGuid(subappGuid).getResult();
                String selectedoptions = "";
                // 按照传过来的数据进行解析，取出optionguidlist
                if (auditspselectedoption != null
                        && StringUtil.isNotBlank(auditspselectedoption.getSelectedoptions())) {
                    selectedoptions = auditspselectedoption.getSelectedoptions();
                    // 清空spimaterial表
                    iauditspimaterial.deleteSpIMaterialBySubappguid(subappGuid);
                    // 清空spitask表
                    auditSpITaskService.deleteSpITaskBySubappGuid(subappGuid);
                    JSONObject jsonObjectop = JSONObject.parseObject(selectedoptions);
                    List<Map<String, Object>> jsona = (List<Map<String, Object>>) jsonObjectop.get("selectedoptions");
                    for (Map<String, Object> map : jsona) {
                        List<Map<String, Object>> maplist = (List<Map<String, Object>>) map.get("optionlist");
                        for (Map<String, Object> map2 : maplist) {
                            if (StringUtil.isNotBlank(String.valueOf(map2.get("optionguid")))) {
                                optionlist.add(String.valueOf(map2.get("optionguid")));
                            }
                        }
                    }
                    List<AuditSpOption> seloptionlist = new ArrayList<>();
                    // 查找所有选项关联的事项
                    SqlConditionUtil sqlc = new SqlConditionUtil();
                    if (optionlist.size() > 0) {
                        sqlc.in("rowguid", StringUtil.joinSql(optionlist));
                        sqlc.eq("businessGuid", businessGuid);
                        seloptionlist.addAll(iAuditSpOptionService.findListByCondition(sqlc.getMap()).getResult());
                    }
                    sqlc.clear();
                    sqlc.eq("businessGuid", businessGuid);
                    sqlc.eq("elementguid", "root");
                    seloptionlist.addAll(iAuditSpOptionService.findListByCondition(sqlc.getMap()).getResult());

                    List<String> taskids = new ArrayList<>();
                    for (AuditSpOption auditSpOption : seloptionlist) {
                        if (StringUtil.isNotBlank(auditSpOption.getTaskid())) {
                            String[] taskid = auditSpOption.getTaskid().split(";");
                            for (String string : taskid) {
                                if (!taskids.contains(string)) {
                                    taskids.add(string);
                                }

                            }
                        }
                    }
                    if (taskids != null && taskids.size() > 0) {
                        for (String taskid : taskids) {
                            Record auditTask = iaudittask.selectUsableTaskByTaskID(taskid).getResult();
                            if (auditTask != null) {
                                auditSpITaskService.addTaskInstance(businessGuid, biGuid, phaseGuid,
                                        auditTask.get("rowguid"), auditTask.get("taskname"), subappGuid,
                                        auditTask.get("ordernum") == null ? 0 : auditTask.get("ordernum"),
                                        auditTask.get("areacode"), "");
                            }
                        }
                        /*
                         * List<AuditSpIMaterial> materials = ihandlespimaterial
                         * .initNewIntegratedMaterial(subappGuid, businessGuid, biGuid, phaseGuid, "",
                         * "") .getResult();
                         */

                        List<AuditSpIMaterial> materials = iHandleSPIMaterial.initIntegratedMaterial(subappGuid,
                                businessGuid, biGuid, phaseGuid, "", auditOnlineRegister.getIdnumber(), areaCode)
                                .getResult();
                        List<JSONObject> materialList = new ArrayList<JSONObject>();
                        if (materials != null && materials.size() > 0) {
                            for (Record record : materials) {
                                JSONObject material = new JSONObject();
                                material.put("materialname", record.getStr("materialname")); // 材料名称
                                materialList.add(material);
                            }
                        }
                        JSONObject dataJson = new JSONObject();
                        dataJson.put("materiallist", materialList);

                        log.info("=======结束调用getElementBusinessMaterialList接口=======");
                        return JsonUtils.zwdtRestReturn("1", "材料初始化成功！", dataJson.toString());
                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", "请选择要办理的事项！", "");
                    }

                }
                else {

                    return JsonUtils.zwdtRestReturn("0", "请选择事项情形！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getElementBusinessMaterialList接口参数：params【" + params + "】=======");
            log.info("=======getElementBusinessMaterialList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "验证失败", "");
        }
    }

    /**
     * 根据情形问题答案查询事项列表
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getAuditSpTaskBySelectedOption", method = RequestMethod.POST)
    public String getAuditSpTaskBySelectedOption(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getAuditSpTaskBySelectedOption接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取子申报实例guid
                String subappGuid = obj.getString("subappguid");
                // 1.2、获取用户选项信息
                String selectedOptions = obj.getString("selectedoptions");
                // 1.3、获取辖区编码
                String areaCode = obj.getString("areacode");
                // 1.4、获取辖区编码
                String businessGuid = obj.getString("businessguid");
                if (StringUtil.isNotBlank(selectedOptions)) {
                    selectedOptions = this.getJsonToSelect(selectedOptions);
                    List<Record> taskElements = iHandleSPIMaterial
                            .initTasklistBySelectedOptions(selectedOptions, subappGuid, areaCode, businessGuid)
                            .getResult();
                    List<JSONObject> taskJsonList = new ArrayList<JSONObject>();
                    if (taskElements != null && !taskElements.isEmpty()) {
                        for (Record record : taskElements) {

                            JSONObject taskJson = new JSONObject();
                            taskJson.put("taskname", record.getStr("taskname"));
                            taskJson.put("taskid", record.getStr("taskid"));
                            taskJsonList.add(taskJson);
                        }
                    }
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("tasklist", taskJsonList);
                    log.info("=======结束调用getAuditSpTaskBySelectedOption接口=======");
                    return JsonUtils.zwdtRestReturn("1", "查询成功", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "请选择情形！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getAuditSpTaskBySelectedOption接口参数：params【" + params + "】=======");
            log.info("=======getAuditSpTaskBySelectedOption异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "验证失败", "");
        }
    }

    /**
     * 获取流程图
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getBusinessFlowData", method = RequestMethod.POST)
    public String getBusinessFlowData(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getBusinessFlowData接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取套餐标识
                String businessGuid = obj.getString("businessguid");
                if (StringUtil.isNotBlank(businessGuid)) {
                    AuditSpBusiness business = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessGuid).getResult();
                    if (business != null) {
                        if (StringUtil.isNotBlank(business.getStr("Flowdata"))) {
                            log.info("=======结束调用getBusinessFlowData接口=======");
                            return JsonUtils.zwdtRestReturn("1", "查询成功",
                                    JSONObject.parseObject(business.getStr("Flowdata")).toString());
                        }
                        else {
                            return JsonUtils.zwdtRestReturn("0", "该套餐暂未配置流程图", "");
                        }
                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", "未获取到套餐", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "套餐标识为空", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getBusinessFlowData接口参数：params【" + params + "】=======");
            log.info("=======getBusinessFlowData异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "验证失败", "");
        }
    }

    /**
     * 获取流程图
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getBusinessTaskStatus", method = RequestMethod.POST)
    public String getBusinessTaskStatus(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getBusinessTaskStatus接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取套餐实例标识
                String biGuid = obj.getString("biguid");
                if (StringUtil.isNotBlank(biGuid)) {
                    List<AuditSpITask> spitasklist = null;
                    spitasklist = iAuditSpITask.getSpITaskByBIGuid(biGuid).getResult();
                    if (spitasklist != null && !spitasklist.isEmpty()) {
                        List<AuditProject> listproject = new ArrayList<>();
                        // 根据spitasklist查找办件
                        JSONObject taskmap = new JSONObject();
                        for (AuditSpITask auditspitask : spitasklist) {
                            if (StringUtil.isNotBlank(auditspitask.getProjectguid())) {
                                // 查询办件
                                AuditProject ap = iAuditProject.getAuditProjectByRowGuid(
                                        "rowguid,task_id,status,projectname,ouname,banjiedate,operatedate,ouguid,tasktype",
                                        auditspitask.getStr("Projectguid"), auditspitask.getStr("Areacode"))
                                        .getResult();
                                if (ap != null) {
                                    listproject.add(ap);
                                }
                                if (listproject != null && !listproject.isEmpty()) {
                                    // 开始遍历办件
                                    AuditProjectSparetime sptime;
                                    String basetaskguid = "";
                                    SqlConditionUtil sqlc = new SqlConditionUtil();
                                    for (AuditProject auditProject : listproject) {
                                        sqlc.clear();
                                        sptime = iAuditProjectSparetime
                                                .getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
                                        // 根据taskid获取到标准事项
                                        sqlc.eq("taskid", auditProject.getStr("Task_id"));
                                        List<AuditSpBasetaskR> listtastr = iAuditSpBasetaskR
                                                .getAuditSpBasetaskrByCondition(sqlc.getMap()).getResult();
                                        if (listtastr.size() == 0) {
                                            continue;
                                        }
                                        for (AuditSpBasetaskR auditSpBasetaskR : listtastr) {
                                            AuditSpBasetask auditSpBasetask = iAuditSpBasetask
                                                    .getAuditSpBasetaskByrowguid(auditSpBasetaskR.getBasetaskguid())
                                                    .getResult();
                                            if (auditSpBasetask != null && auditSpBasetask.getBusinessType()
                                                    .equals(ZwfwConstant.CONSTANT_STR_TWO)) {
                                                basetaskguid = auditSpBasetask.getRowguid();
                                            }
                                        }
                                        if (taskmap.get(basetaskguid) == null) {

                                            JSONArray arrjson = new JSONArray();
                                            String status = "1";
                                            int projectstatus = auditProject.getStatus();
                                            if (projectstatus == ZwfwConstant.BANJIAN_STATUS_YJJ) {
                                                status = "2";
                                            }
                                            else if (ZwfwConstant.BANJIAN_STATUS_YJJ < projectstatus
                                                    && projectstatus < ZwfwConstant.BANJIAN_STATUS_ZCBJ) {
                                                status = "3";
                                            }
                                            else if (projectstatus >= ZwfwConstant.BANJIAN_STATUS_ZCBJ) {
                                                status = "4";
                                            }
                                            arrjson.add(status);
                                            if (auditProject.getTasktype() == Integer
                                                    .parseInt(ZwfwConstant.ITEMTYPE_JBJ)) {
                                                arrjson.add("0");
                                            }
                                            else {
                                                if (sptime != null) {
                                                    int spare = sptime.getSpareminutes();
                                                    // 剩余时间小于一天即将超时，小于等于0已经超时
                                                    if (spare > 0 && spare <= 1440) {
                                                        arrjson.add("1");
                                                    }
                                                    else if (spare <= 0) {
                                                        arrjson.add("2");
                                                    }
                                                    else {
                                                        arrjson.add("0");
                                                    }
                                                }
                                                else {
                                                    arrjson.add("0");
                                                }
                                            }
                                            taskmap.put(basetaskguid, arrjson);
                                        }
                                        else {
                                            JSONArray arrjson = (JSONArray) taskmap.get(basetaskguid);
                                            String status = "1";
                                            int projectstatus = auditProject.getStatus();
                                            if (projectstatus == ZwfwConstant.BANJIAN_STATUS_YJJ) {
                                                status = "2";
                                            }
                                            else if (ZwfwConstant.BANJIAN_STATUS_YJJ < projectstatus
                                                    && projectstatus < ZwfwConstant.BANJIAN_STATUS_ZCBJ) {
                                                status = "3";
                                            }
                                            else if (projectstatus == ZwfwConstant.BANJIAN_STATUS_ZCBJ) {
                                                status = "4";
                                            }
                                            if ("4".equals(status)) {
                                                arrjson.set(0, status);
                                            }
                                            // 比较时间
                                            if (sptime != null) {
                                                int spare = sptime.getSpareminutes();
                                                // 剩余时间小于一天即将超时，小于等于0已经超时
                                                if (spare > 0 && spare <= 1440
                                                        && ZwfwConstant.CONSTANT_STR_ZERO.equals(arrjson.get(1))) {
                                                    arrjson.set(1, "1");
                                                }
                                                else if (spare <= 0) {
                                                    arrjson.set(1, "2");
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        log.info("=======结束调用getBusinessTaskStatus接口=======");
                        return JsonUtils.zwdtRestReturn("1", "查询成功",
                                JSONObject.parseObject(taskmap.toString()).toString());
                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", "未获取到套餐", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "套餐标识为空", "");
                }

            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getBusinessTaskStatus接口参数：params【" + params + "】=======");
            log.info("=======getBusinessTaskStatus异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "验证失败", "");
        }
    }

    /**
     * 申报须知接口(套餐申报须知调用)
     *
     * @param params
     *            请求参数
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/declareBusinessNotice", method = RequestMethod.POST)
    public String declareBusinessNotice(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用declareBusinessNotice接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取主题标识
                String businessGuid = obj.getString("businessguid");
                // 1.2、获取业务标识
                String yeWuGuid = obj.getString("yewuguid");
                // 1.3、获取子申报标识
                String subappGuid = obj.getString("subappguid");
                // 2、获取主题信息
                AuditSpBusiness auditSpBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessGuid)
                        .getResult();
                if (auditSpBusiness != null) {
                    // 4、获取主题的第一个阶段信息
                    AuditSpPhase auditSpPhase = iAuditSpPhase.getSpPaseByBusinedssguid(auditSpBusiness.getRowguid())
                            .getResult().get(0);
                    if (auditSpPhase != null) {
                        // 5、定义返回JSON对象
                        JSONObject dataJson = new JSONObject();
                        // 5.1、获取套餐主题基本信息
                        dataJson.put("businessguid", businessGuid);// 获取主题标识
                        dataJson.put("businessname", auditSpBusiness.getBusinessname());// 获取主题名称
                        dataJson.put("businessurl", WebUtil.getRequestCompleteUrl(request)
                                + "/epointzwmhwz/pages/eventdetail/spintegratedetail?businessguid=" + businessGuid);// 二维码内容
                        dataJson.put("note", auditSpBusiness.getNote()); // 获取主题说明
                        dataJson.put("areacode", auditSpBusiness.getAreacode()); // 获取辖区编码
                        dataJson.put("acceptcondition", auditSpBusiness.getStr("acceptcondition"));
                        dataJson.put("iswssb", auditSpBusiness.getIswssb());
                        // 返回表单页面目录层级
                        String folderStr = auditSpBusiness.getHandleURL();
                        String folder = "onlinedeclaration";

                        // 初始化申报人类型为法人
                        String userType = ZwfwConstant.APPLAYERTYPE_QY;
                        if (StringUtil.isNotBlank(folderStr)) {
                            if (folderStr.lastIndexOf("/") != -1) {
                                folderStr = folderStr.substring(folderStr.lastIndexOf("/") + 1);
                                if (!"auditspintegrated".equals(folderStr)) {
                                    folder = folderStr;
                                    if ("auditindividual".equals(folderStr)) {
                                        userType = ZwfwConstant.APPLAYERTYPE_GR; // 如果主题处理地址为auditindividual，则类型为个人
                                    }
                                }
                            }
                        }
                        dataJson.put("folder", folder);
                        dataJson.put("userType", userType);
                        log.info("yeWuguid=" + yeWuGuid);

                        // 5.2、获取企业基本信息
                        AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                        JSONObject applyerJson = new JSONObject(); // 申请人基本信息
                        AuditSpIntegratedCompany auditSpIntegratedCompany = iAuditSpIntegratedCompany
                                .getAuditSpIntegratedCompanyByGuid(yeWuGuid).getResult();
                        if (auditSpIntegratedCompany != null) {
                            applyerJson.put("companyname", auditSpIntegratedCompany.getCompanyname());// 获取企业名称
                            applyerJson.put("zzjgdmz", auditSpIntegratedCompany.getZzjgdmz()); // 获取组织结构代码证
                            applyerJson.put("contactperson", auditSpIntegratedCompany.getContactperson()); // 获取联系人
                            applyerJson.put("contactphone", auditSpIntegratedCompany.getContactphone());// 获取联系电话
                            applyerJson.put("capital", auditSpIntegratedCompany.getCapital()); // 获取注册资本
                            applyerJson.put("legalpersonemail", auditSpIntegratedCompany.getLegalpersonemail()); // 获取法人Email
                            applyerJson.put("address", auditSpIntegratedCompany.getAddress()); // 获取地址
                            applyerJson.put("scope", auditSpIntegratedCompany.getScope()); // 获取经营范围
                            applyerJson.put("contactpostcode", auditSpIntegratedCompany.getContactpostcode()); // 获取邮编
                        }
                        // 如果企业信息为空,查询获取个人信息
                        else {
                            if (ZwfwConstant.APPLAYERTYPE_GR.equals(userType) && auditOnlineRegister != null) {
                                AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
                                        .getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
                                if (auditOnlineIndividual != null) {
                                    applyerJson.put("companyname", auditOnlineIndividual.getClientname());// 获取用户名称
                                    applyerJson.put("zzjgdmz", auditOnlineIndividual.getIdnumber()); // 获取身份证号码
                                    applyerJson.put("contactperson", auditOnlineIndividual.getContactperson()); // 获取联系人
                                    applyerJson.put("contactphone", auditOnlineIndividual.getContactmobile());// 获取联系电话
                                    applyerJson.put("legalpersonemail", auditOnlineIndividual.getContactemail()); // 获取联系人Email
                                    applyerJson.put("address", auditOnlineIndividual.getDeptaddress()); // 获取地址
                                    applyerJson.put("contactpostcode", ""); // 获取邮编
                                }
                            }
                        }

                        log.info(applyerJson);

                        // 5.3、获取套餐涉及到事项的受理条件
                        List<JSONObject> handleConditionJsonList = new ArrayList<JSONObject>();// 事项受理条件
                        List<AuditSpTask> auditSpTaskList = iAuditSpTask
                                .getAllAuditSpTaskByPhaseguid(auditSpPhase.getRowguid()).getResult();
                        for (AuditSpTask auditSpTask : auditSpTaskList) {
                            // 查询并遍历标准事项关联关系表
                            SqlConditionUtil sql = new SqlConditionUtil();
                            sql.eq("basetaskguid", auditSpTask.getStr("basetaskguid"));
                            List<AuditSpBasetaskR> listr = iAuditSpBasetaskR
                                    .getAuditSpBasetaskrByCondition(sql.getMap()).getResult();
                            for (AuditSpBasetaskR auditSpBasetaskR : listr) {
                                AuditTask auditTask = iAuditTask.selectUsableTaskByTaskID(auditSpBasetaskR.getTaskid())
                                        .getResult();
                                if (auditTask != null) {
                                    JSONObject handleConditionJson = new JSONObject();
                                    handleConditionJson.put("handlecondition",
                                            StringUtil.isBlank(auditTask.getAcceptcondition()) ? ""
                                                    : auditTask.getAcceptcondition()); // 受理条件
                                    handleConditionJsonList.add(handleConditionJson);
                                }
                            }

                        }
                        // 5.4、获取套餐中包含事项的材料（事项普通材料关联共享材料只保留一份，审批结果和事项材料关联了共享材料则不需要显示）
                        List<JSONObject> taskMaterialJsonList = new ArrayList<JSONObject>(); // 事项材料信息
                        if (StringUtil.isBlank(subappGuid)) {
                            List<AuditTaskMaterial> taskMaterialList = new ArrayList<AuditTaskMaterial>();
                            // 5.4.1、获取主题下所有共享材料中除审批结果的共享材料
                            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                            sqlConditionUtil.eq("businessguid", businessGuid);
                            sqlConditionUtil.eq("status", "1"); // 共享材料状态：启用
                            // 5.4.1.1、获取主题下启用的共享材料
                            List<AuditSpShareMaterial> auditSpShareMaterials = iAuditSpShareMaterial
                                    .selectAuditSpShareMaterialList(sqlConditionUtil.getMap()).getResult();
                            if (auditSpShareMaterials != null && auditSpShareMaterials.size() > 0) {
                                for (AuditSpShareMaterial auditSpShareMaterial : auditSpShareMaterials) {
                                    boolean flag = true;
                                    // 5.4.1.2、获取关联到共享材料的事项材料(除去审批结果)
                                    List<AuditSpShareMaterialRelation> auditSpShareMaterialRelations = iAuditSpShareMaterialRelation
                                            .selectByShareMaterialGuid(businessGuid,
                                                    auditSpShareMaterial.getSharematerialguid())
                                            .getResult();
                                    if (auditSpShareMaterialRelations != null
                                            && auditSpShareMaterialRelations.size() > 0) {
                                        for (AuditSpShareMaterialRelation auditSpShareMaterialRelation : auditSpShareMaterialRelations) {
                                            // 5.4.1.3、去除关联到共享材料中有关联审批结果的数据
                                            if ("20".equals(auditSpShareMaterialRelation.getMaterialtype())) {
                                                flag = false;
                                                break;
                                            }
                                        }
                                    }
                                    else {
                                        flag = false;
                                    }
                                    if (flag) {
                                        // 5.4.1.4、返回已去除审批结果的共享材料信息
                                        JSONObject materialJson = new JSONObject();
                                        materialJson.put("materialname", auditSpShareMaterial.getMaterialname());// 材料名称
                                        materialJson.put("materialguid", auditSpShareMaterial.getRowguid());// 材料标识
                                        materialJson.put("necessary", ZwfwConstant.NECESSITY_SET_YES
                                                .equals(auditSpShareMaterial.getNecessity()) ? "1" : "0");// 是否必需
                                        materialJson.put("templateattachguid", "");// 空白模板文件（共享材料无）
                                        materialJson.put("exampleattachguid", "");// 填报示例文件（共享材料无）
                                        materialJson.put("submittype", iCodeItemsService.getItemTextByCodeName("提交方式",
                                                auditSpShareMaterial.getSubmittype()));// 提交方式
                                        String spMaterialSource = "";
                                        if (StringUtil.isBlank(auditSpShareMaterial.getFile_source())) {
                                            spMaterialSource = "申请人自备";
                                        }
                                        else {
                                            spMaterialSource = iCodeItemsService.getItemTextByCodeName("来源渠道",
                                                    auditSpShareMaterial.getFile_source());
                                        }
                                        materialJson.put("materialsource", spMaterialSource);// 来源
                                                                                             // （共享材料默认为申请人自备）
                                        taskMaterialJsonList.add(materialJson);
                                    }
                                }
                            }
                            // 5.4.2、获取套餐中的非审批结果的共享材料（多个事项材料共用）
                            for (AuditSpTask auditSpTask : auditSpTaskList) {
                                // 5.4.2.1、获取事项基本信息
                                AuditTask auditTask = iAuditTask.getUseTaskAndExtByTaskid(auditSpTask.getTaskid())
                                        .getResult();
                                if (auditTask != null) {
                                    // 5.4.2.2、获取关联到共享材料的事项材料
                                    List<AuditSpShareMaterialRelation> auditSpShareMaterialRelationList = iAuditSpShareMaterialRelation
                                            .selectAuditSpShareMaterialByBusinessGuid(businessGuid).getResult();
                                    for (AuditSpShareMaterialRelation auditSpShareMaterialRelation : auditSpShareMaterialRelationList) {
                                        AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial
                                                .selectTaskMaterialByTaskGuidAndMaterialId(auditTask.getRowguid(),
                                                        auditSpShareMaterialRelation.getMaterialid())
                                                .getResult();
                                        if (auditTaskMaterial != null) {
                                            taskMaterialList.add(auditTaskMaterial);
                                        }
                                    }
                                }
                                else {
                                    // 查询并遍历标准事项关联关系表
                                    SqlConditionUtil sql = new SqlConditionUtil();
                                    sql.eq("basetaskguid", auditSpTask.getStr("basetaskguid"));
                                    List<AuditSpBasetaskR> listr = iAuditSpBasetaskR
                                            .getAuditSpBasetaskrByCondition(sql.getMap()).getResult();
                                    for (AuditSpBasetaskR auditSpBasetaskR : listr) {
                                        AuditTask auditTaskBase = iAuditTask
                                                .selectUsableTaskByTaskID(auditSpBasetaskR.getTaskid()).getResult();
                                        if (auditTaskBase != null) {
                                            // 5.4.2.2、获取关联到共享材料的事项材料
                                            List<AuditSpShareMaterialRelation> auditSpShareMaterialRelationList = iAuditSpShareMaterialRelation
                                                    .selectAuditSpShareMaterialByBusinessGuid(businessGuid).getResult();
                                            for (AuditSpShareMaterialRelation auditSpShareMaterialRelation : auditSpShareMaterialRelationList) {
                                                AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial
                                                        .selectTaskMaterialByTaskGuidAndMaterialId(
                                                                auditTaskBase.getRowguid(),
                                                                auditSpShareMaterialRelation.getMaterialid())
                                                        .getResult();
                                                if (auditTaskMaterial != null) {
                                                    taskMaterialList.add(auditTaskMaterial);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            // 5.4.3、主题中涉及到所有事项的所有材料
                            List<AuditTaskMaterial> gobalMaterialList = new ArrayList<AuditTaskMaterial>();// 事项中去除共享材料剩余的材料
                            List<AuditTaskMaterial> tempMaterialList = new ArrayList<AuditTaskMaterial>();
                            for (AuditSpTask auditSpTask : auditSpTaskList) {
                                AuditTask auditTask = iAuditTask.getUseTaskAndExtByTaskid(auditSpTask.getTaskid())
                                        .getResult();
                                if (auditTask != null) {
                                    // 5.4.3.1、主题中涉及到所有事项的所有材料都添加到集合中
                                    List<AuditTaskMaterial> auditTaskMaterials = iAuditTaskMaterial
                                            .selectTaskMaterialListByTaskGuid(auditTask.getRowguid(), true).getResult();
                                    gobalMaterialList.addAll(auditTaskMaterials);
                                    tempMaterialList.addAll(auditTaskMaterials);
                                }
                                else {
                                    // 查询并遍历标准事项关联关系表
                                    SqlConditionUtil sql = new SqlConditionUtil();
                                    sql.eq("basetaskguid", auditSpTask.getStr("basetaskguid"));
                                    List<AuditSpBasetaskR> listr = iAuditSpBasetaskR
                                            .getAuditSpBasetaskrByCondition(sql.getMap()).getResult();
                                    for (AuditSpBasetaskR auditSpBasetaskR : listr) {
                                        AuditTask auditTask1 = iAuditTask
                                                .selectUsableTaskByTaskID(auditSpBasetaskR.getTaskid()).getResult();
                                        if (auditTask1 != null) {
                                            List<AuditTaskMaterial> auditTaskMaterials = iAuditTaskMaterial
                                                    .selectTaskMaterialListByTaskGuid(auditTask1.getRowguid(), true)
                                                    .getResult();
                                            gobalMaterialList.addAll(auditTaskMaterials);
                                            tempMaterialList.addAll(auditTaskMaterials);
                                        }
                                    }
                                }
                            }
                            // 5.4.3.2、将所有事项中的所有材料中去除关联共享材料的事项材料
                            for (AuditTaskMaterial gAuditTaskMaterial : tempMaterialList) {
                                for (AuditTaskMaterial lAuditTaskMaterial : taskMaterialList) {
                                    if (lAuditTaskMaterial.getRowguid().equals(gAuditTaskMaterial.getRowguid())) {
                                        gobalMaterialList.remove(gAuditTaskMaterial);
                                    }
                                }
                            }
                            // 5.4.4、获取事项中没有关联到共享材料的普通材料信息
                            for (AuditTaskMaterial auditTaskMaterial : gobalMaterialList) {
                                JSONObject materialJson = new JSONObject();
                                materialJson.put("materialname", auditTaskMaterial.getMaterialname());// 材料名称
                                materialJson.put("materialguid", auditTaskMaterial.getRowguid());// 材料标识
                                materialJson.put("necessary", ZwfwConstant.NECESSITY_SET_YES
                                        .equals(String.valueOf(auditTaskMaterial.getNecessity())) ? "1" : "0");// 是否必需
                                materialJson.put("ordernum", auditTaskMaterial.getOrdernum());
                                materialJson.put("submittype", iCodeItemsService.getItemTextByCodeName("提交方式",
                                        String.valueOf(auditTaskMaterial.getSubmittype())));// 提交方式
                                String templateClientGuid = auditTaskMaterial.getTemplateattachguid();// 空白模板文件
                                if (StringUtil.isNotBlank(templateClientGuid)) {
                                    int templateAttachCount = iAttachService
                                            .getAttachCountByClientGuid(templateClientGuid);
                                    if (templateAttachCount > 0) {
                                        materialJson.put("templateattachguid", templateClientGuid);
                                    }
                                }
                                String exampleClientGuid = auditTaskMaterial.getExampleattachguid();// 填报示例文件
                                if (StringUtil.isNotBlank(exampleClientGuid)) {
                                    int exampleAttachCount = iAttachService
                                            .getAttachCountByClientGuid(exampleClientGuid);
                                    if (exampleAttachCount > 0) {
                                        materialJson.put("exampleattachguid", exampleClientGuid);
                                    }
                                }
                                String materialsource;
                                if (StringUtil.isBlank(auditTaskMaterial.getFile_source())) {
                                    materialsource = "申请人自备";
                                }
                                else {
                                    materialsource = iCodeItemsService.getItemTextByCodeName("来源渠道",
                                            auditTaskMaterial.getFile_source());
                                }
                                materialJson.put("materialsource",
                                        StringUtil.isBlank(materialsource) ? "申请人自备" : materialsource);// 来源
                                taskMaterialJsonList.add(materialJson);
                            }
                        }
                        else {
                            List<AuditSpIMaterial> auditSpIMaterials = iAuditSpIMaterial
                                    .getSpIMaterialBySubappGuid(subappGuid).getResult();
                            if (auditSpIMaterials != null && !auditSpIMaterials.isEmpty()) {
                                for (AuditSpIMaterial auditSpIMaterial : auditSpIMaterials) {
                                    JSONObject auditSpIMaterialJson = new JSONObject();
                                    auditSpIMaterialJson.put("materialname", auditSpIMaterial.getMaterialname()); // 材料名称
                                    auditSpIMaterialJson.put("necessary", auditSpIMaterial.getNecessity()); // 必要性
                                    auditSpIMaterialJson.put("submittype", iCodeItemsService
                                            .getItemTextByCodeName("提交方式", auditSpIMaterial.getSubmittype()));// 提交方式
                                    taskMaterialJsonList.add(auditSpIMaterialJson);
                                }
                            }
                        }
                        // 5.4.4、获取资金来源下拉列表
                        List<JSONObject> zjlyJsonList = new ArrayList<JSONObject>();
                        List<CodeItems> zjlyCodeItems = iCodeItemsService.listCodeItemsByCodeName("资金来源");
                        JSONObject zjlyExampleJson = new JSONObject();
                        zjlyExampleJson.put("optionvalue", "");
                        zjlyExampleJson.put("optiontext", "请选择");
                        zjlyJsonList.add(0, zjlyExampleJson);
                        for (CodeItems codeItems : zjlyCodeItems) {
                            JSONObject zjlyJson = new JSONObject();
                            zjlyJson.put("optiontext", codeItems.getItemText());
                            zjlyJson.put("optionvalue", codeItems.getItemValue());
                            if (auditSpIntegratedCompany != null
                                    && codeItems.getItemValue().equals(auditSpIntegratedCompany.getFundssource())) {
                                zjlyJson.put("isselected", "true");
                            }
                            zjlyJsonList.add(zjlyJson);
                        }
                        // 5.4.5、获取法人职务下拉列表
                        List<JSONObject> frzwJsonList = new ArrayList<JSONObject>();
                        List<CodeItems> frzwCodeItems = iCodeItemsService.listCodeItemsByCodeName("法人职务");
                        JSONObject frzwExampleJson = new JSONObject();
                        frzwExampleJson.put("optionvalue", "");
                        frzwExampleJson.put("optiontext", "请选择");
                        frzwJsonList.add(0, frzwExampleJson);
                        for (CodeItems codeItems : frzwCodeItems) {
                            JSONObject frzwJson = new JSONObject();
                            frzwJson.put("optiontext", codeItems.getItemText());
                            frzwJson.put("optionvalue", codeItems.getItemValue());
                            if (auditSpIntegratedCompany != null
                                    && codeItems.getItemValue().equals(auditSpIntegratedCompany.getLegalpersonduty())) {
                                frzwJson.put("isselected", "true");
                            }
                            frzwJsonList.add(frzwJson);
                        }
                        // 5.4.6、获取企业类型下拉列表
                        List<JSONObject> qylxJsonList = new ArrayList<JSONObject>();
                        List<CodeItems> qylxCodeItems = iCodeItemsService.listCodeItemsByCodeName("企业类型代码");
                        JSONObject qylxExampleJson = new JSONObject();
                        qylxExampleJson.put("optionvalue", "");
                        qylxExampleJson.put("optiontext", "请选择");
                        qylxJsonList.add(0, qylxExampleJson);
                        for (CodeItems codeItems : qylxCodeItems) {
                            JSONObject qylxJson = new JSONObject();
                            qylxJson.put("optiontext", codeItems.getItemText());
                            qylxJson.put("optionvalue", codeItems.getItemValue());
                            if (auditSpIntegratedCompany != null
                                    && codeItems.getItemValue().equals(auditSpIntegratedCompany.getCompanytype())) {
                                qylxJson.put("isselected", "true");
                            }
                            qylxJsonList.add(qylxJson);
                        }
                        // 5.4.7、对返回的材料列表进行排序
                        Collections.sort(taskMaterialJsonList, new Comparator<JSONObject>()
                        {
                            @Override
                            public int compare(JSONObject b1, JSONObject b2) {
                                Integer necessaryb1 = Integer.parseInt(b1.get("necessary").toString());
                                Integer necessaryb2 = Integer.parseInt(b2.get("necessary").toString());
                                // 5.4.7.1、优先对比材料必要性（必要在前）
                                int comNecessity = necessaryb2.compareTo(necessaryb1);
                                int ret = comNecessity;
                                // 5.4.7.2、材料必要性一致的情况下对比排序号（排序号降序排）
                                if (comNecessity == 0) {
                                    Integer ordernumb1 = b1.get("ordernum") == null ? 0 : (Integer) b1.get("ordernum");
                                    Integer ordernumb2 = b2.get("ordernum") == null ? 0 : (Integer) b2.get("ordernum");
                                    ret = ordernumb2.compareTo(ordernumb1);
                                }
                                return ret;
                            }
                        });
                        dataJson.put("applerinfo", applyerJson);// 企业申报信息
                        dataJson.put("taskMaterialList", taskMaterialJsonList);// 材料列表（排除了审批结果材料、并联审批全局共享材料中重复的材料）
                        dataJson.put("handleconditionList", handleConditionJsonList);// 所有事项的审批条件
                        dataJson.put("jflyoptionList", zjlyJsonList);// 资金来源代码项
                        dataJson.put("qylxoptionList", qylxJsonList);// 企业类型代码项
                        dataJson.put("frzwoptionList", frzwJsonList);// 法人职务代码项
                        log.info("=======结束调用declareBusinessNotice接口=======");
                        return JsonUtils.zwdtRestReturn("1", "主题申报须知信息获取成功", dataJson.toString());
                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", "此套餐阶段查询失败！", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "此套餐不存在！", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "主题申报须知信息获取失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取流程图
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/checkBussinesElement", method = RequestMethod.POST)
    public String checkBussinesElement(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用checkBussinesElement接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取套餐标识
                String businessGuid = obj.getString("businessguid");
                if (StringUtil.isNotBlank(businessGuid)) {
                    // 判断是否拥有情形
                    Boolean isHasElement = iAuditSpElementService.checkByBusinessguid(businessGuid).getResult();
                    String isHasElementFlag = isHasElement ? "1" : "0";
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("ishaselementflag", isHasElementFlag);
                    if (!isHasElement) {
                        // 没有情形的话判断一下跳那个文件夹
                        AuditSpBusiness auditSpBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessGuid)
                                .getResult();

                        String folderStr = auditSpBusiness.getHandleURL();
                        String folder = "onlinedeclaration";
                        if (StringUtil.isNotBlank(folderStr)) {
                            if (folderStr.lastIndexOf("/") != -1) {
                                folderStr = folderStr.substring(folderStr.lastIndexOf("/") + 1);
                                if (!"auditspintegrated".equals(folderStr)
                                        && !"auditspbornintegrated".equals(folderStr)) {
                                    folder = folderStr;
                                }
                            }
                        }
                        dataJson.put("folder", folder);
                    }
                    log.info("=======结束调用checkBussinesElement接口=======");
                    return JsonUtils.zwdtRestReturn("1", "判断成功", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "套餐标识为空", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======checkBussinesElement接口参数：params【" + params + "】=======");
            log.info("=======checkBussinesElement异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "判断失败", "");
        }
    }

    /**
     * 获取流程图
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getSdqYjsBusinessGuid", method = RequestMethod.POST)
    public String getSdqYjsBusinessGuid(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getSdqYjsBusinessGuid接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取套餐标识
                String areacode = obj.getString("areacode");
                String businessname = obj.getString("businessname");
                if (StringUtil.isNotBlank(areacode)) {
                    // 判断是否拥有情形
                    String yjsBusinessGuid = iJnProjectService.getSdqYjsBusinessGuid(areacode, businessname);
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("yjsbusinessguid", yjsBusinessGuid);

                    log.info("=======结束调用getSdqYjsBusinessGuid接口=======");
                    return JsonUtils.zwdtRestReturn("1", "判断成功", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "辖区标识为空", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            log.info("=======getSdqYjsBusinessGuid接口参数：params【" + params + "】=======");
            log.info("=======getSdqYjsBusinessGuid异常信息：" + e.getMessage() + "=======", e);
            return JsonUtils.zwdtRestReturn("0", "判断失败", "");
        }
    }

    public List<String> getDefaultAndShareOptionguid(String businessguid) {
        List<String> defaultsel = new ArrayList<>();
        Map<String, String> sharesel = new HashMap<>();
        List<AuditSpShareoption> auditspshareoptionlist = iauditspshareoption.getlistBybusinessguid(businessguid)
                .getResult();
        if (auditspshareoptionlist.size() > 0) {
            defaultsel = auditspshareoptionlist.stream()
                    .filter(a -> a.getSelecttype().equals(ZwfwConstant.CONSTANT_STR_ONE))
                    .map(AuditSpShareoption::getOptionguid).collect(Collectors.toList());
            Map<String, List<AuditSpShareoption>> shareselarr = auditspshareoptionlist.stream()
                    .filter(a -> a.getSelecttype().equals(ZwfwConstant.CONSTANT_STR_TWO))
                    .collect(Collectors.groupingBy(a -> a.getSharevalue()));
            /*
             * for (String key : shareselarr.keySet()) { List<AuditSpShareoption> value =
             * shareselarr.get(key); if (value != null && value.size() > 0) {
             * sharesel.put(key, value.stream().map(a ->
             * a.getOptionguid()).collect(Collectors.joining(";"))); } }
             */
        }
        return defaultsel;
    }

    private String getJsonToSelect(String str) {
        JSONObject elementsjosn = new JSONObject();
        JSONArray newarray = JSONArray.parseArray(str);
        JSONArray answerarry = new JSONArray();
        JSONArray selectarry = new JSONArray();
        if (newarray != null && newarray.size() > 0) {
            for (Object obj : newarray) {
                JSONObject jsonobj = JSONObject.parseObject(obj.toString());
                JSONArray selectobjchildarray = new JSONArray();
                String answer = jsonobj.getString("answer");
                JSONObject selectobj = new JSONObject();
                if (StringUtil.isNotBlank(jsonobj.getString("optionguid"))
                        && jsonobj.getString("optionguid").contains(",")) {
                    String strarr[] = jsonobj.getString("optionguid").split(",");
                    for (String childoptionguid : strarr) {
                        JSONObject selectobjchild = new JSONObject();
                        selectobjchild.put("optionguid", childoptionguid);
                        selectobjchildarray.add(selectobjchild);
                    }
                }
                else {
                    JSONObject selectobjParent = new JSONObject();
                    selectobjParent.put("optionguid", jsonobj.get("optionguid"));
                    selectobjchildarray.add(selectobjParent);
                }
                if (StringUtil.isNotBlank(jsonobj.get("children"))) {
                    // 如果包含子项
                    JSONArray jsonChildArray = JSONArray.parseArray(jsonobj.get("children").toString());
                    if (jsonChildArray != null && jsonChildArray.size() > 0) {
                        for (Object childobj : jsonChildArray) {
                            JSONObject childobjjson = JSONObject.parseObject(childobj.toString());
                            answer += "/" + childobjjson.get("element") + "(" + childobjjson.get("answer") + ")";
                            if (StringUtil.isNotBlank(childobjjson.get("optionguid"))
                                    && childobjjson.getString("optionguid").contains(",")) {
                                String strarr[] = childobjjson.getString("optionguid").split(",");
                                for (String childoptionguid : strarr) {
                                    JSONObject selectobjchild = new JSONObject();
                                    selectobjchild.put("optionguid", childoptionguid);
                                    selectobjchildarray.add(selectobjchild);
                                }
                            }
                            else {
                                JSONObject selectobjchild = new JSONObject();
                                selectobjchild.put("optionguid", childobjjson.get("optionguid"));
                                selectobjchildarray.add(selectobjchild);
                                // 如果还有三级情形
                                JSONArray childobjjsonArray = null;
                                boolean isHasChildren = StringUtil.isNotBlank(childobjjson.get("children"));
                                while (isHasChildren) {
                                    if (StringUtil.isNotBlank(childobjjson.get("children"))) {
                                        childobjjsonArray = JSONArray
                                                .parseArray(childobjjson.get("children").toString());
                                    }
                                    if (childobjjsonArray != null && childobjjsonArray.size() > 0) {
                                        for (Object childobjarray : childobjjsonArray) {
                                            JSONObject childobjjsonnew = JSONObject
                                                    .parseObject(childobjarray.toString());
                                            if (StringUtil.isNotBlank(childobjjsonnew.get("optionguid"))
                                                    && childobjjsonnew.getString("optionguid").contains(",")) {
                                                String strarr[] = childobjjsonnew.getString("optionguid").split(",");
                                                for (String childoptionguid : strarr) {
                                                    JSONObject selectobjchildnew = new JSONObject();
                                                    selectobjchildnew.put("optionguid", childoptionguid);
                                                    selectobjchildarray.add(selectobjchildnew);
                                                }
                                            }
                                            else {
                                                JSONObject selectobjchildnew2 = new JSONObject();
                                                selectobjchildnew2.put("optionguid", childobjjsonnew.get("optionguid"));
                                                selectobjchildarray.add(selectobjchildnew2);

                                            }
                                            answer += "/" + childobjjsonnew.get("element") + "("
                                                    + childobjjsonnew.get("answer") + ")";
                                            // 判断还有没有子
                                            childobjjson = childobjjsonnew;
                                            isHasChildren = StringUtil.isNotBlank(childobjjson.get("children"));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                jsonobj.remove("children");
                jsonobj.remove("optionguid");
                jsonobj.put("answer", answer);
                answerarry.add(jsonobj);
                selectobj.put("optionlist", selectobjchildarray);
                selectarry.add(selectobj);
            }
            elementsjosn.put("selectedoptions", selectarry);
            elementsjosn.put("elementanswers", answerarry);
        }
        return elementsjosn.toString();
    }

    /**
     * 办件详情的接口
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getBusinessProjectDetail", method = RequestMethod.POST)
    public String getBusinessProjectDetail(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getBusinessProjectDetail接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取办件标识
                String biGuid = obj.getString("biguid");
                // 1.2、获取事项标识
                String baseTaskGuid = obj.getString("basetaskguid");
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 2、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    String taskId = "";
                    if (StringUtil.isNotBlank(baseTaskGuid)) {
                        SqlConditionUtil sql = new SqlConditionUtil();
                        sql.eq("basetaskguid", baseTaskGuid);
                        List<AuditSpBasetaskR> baseTasks = iAuditSpBasetaskR
                                .getAuditSpBasetaskrByCondition(sql.getMap()).getResult();
                        if (baseTasks != null && !baseTasks.isEmpty()) {
                            taskId = baseTasks.get(0).getTaskid();
                        }
                    }
                    // 3、获取办件信息
                    String fields = "rowguid,taskguid";
                    AuditProject auditProject = new AuditProject();
                    if (StringUtil.isNotBlank(taskId) && StringUtil.isNotBlank(biGuid)) {
                        SqlConditionUtil sql = new SqlConditionUtil();
                        sql.eq("task_id", taskId);
                        sql.eq("biguid", biGuid);
                        sql.setOrderDesc("operatedate");

                        List<AuditProject> auditPorjects = iAuditProject
                                .getAuditProjectListByCondition(fields, sql.getMap()).getResult();
                        /*
                         * List<AuditProject> auditPorjects2 = iAuditProject
                         * .getAuditProjectPageData(fields, sql.getMap(), 0, 10, "",
                         * "").getResult().getList();
                         */

                        if (auditPorjects != null && !auditPorjects.isEmpty()) {
                            auditProject = auditPorjects.get(0);
                        }
                        else {
                            return JsonUtils.zwdtRestReturn("0", "暂未办理！", "");
                        }
                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", "请检查标准事项是否配置！", "");
                    }
                    if (auditProject != null) {
                        if (StringUtil.isNotBlank(biGuid)) {
                            AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biGuid).getResult();
                            if (auditSpInstance != null && StringUtil.isNotBlank(auditSpInstance.getBusinessguid())) {
                                AuditSpBusiness auditSpBusiness = iAuditSpBusiness
                                        .getAuditSpBusinessByRowguid(auditSpInstance.getBusinessguid()).getResult();
                                // 返回表单页面目录层级
                                String folderStr = auditSpBusiness.getStr("handleurl");
                                String folder = "myspace";
                                if (StringUtil.isNotBlank(folderStr)) {
                                    if (folderStr.lastIndexOf("/") != -1) {
                                        folderStr = folderStr.substring(folderStr.lastIndexOf("/") + 1);
                                        if (!"auditspintegrated".equals(folderStr)) {
                                            folder = folderStr;
                                        }
                                    }
                                }
                                dataJson.put("folder", folder);
                            }

                        }

                        dataJson.put("projectguid", auditProject.getRowguid()); // 办件标识
                        dataJson.put("taskguid", auditProject.getTaskguid()); // 办件标识

                        log.info("=======结束调用getBusinessProjectDetail接口=======");
                        return JsonUtils.zwdtRestReturn("1", "获取办件详情成功", dataJson.toString());
                    }
                    else {
                        return JsonUtils.zwdtRestReturn("0", "该办件不存在！", "");
                    }
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息异常", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======getBusinessProjectDetail接口参数：params【" + params + "】=======");
            log.info("=======getBusinessProjectDetail异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取办件详情异常：" + e.getMessage(), "");
        }
    }

    /**
     * 材料上传页面按钮排序
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/setFirstButton", method = RequestMethod.POST)
    public String setFirstButton(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用setFirstButton接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取办件标识
                String projectGuid = obj.getString("projectguid");
                // 1.2、获取材料标识
                String taskMaterialGuid = obj.getString("taskmaterialguid");
                // 1.3、获取材料标识
                String projectMaterialGuid = obj.getString("projectmaterialguid");
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 比对各方式材料数量，哪个多返回哪一个标志位 0 证照 1 云盘 2手机 3证照
                    // 获取云盘文件数量
                    SqlUtils sqlUtils = new SqlUtils();
                    sqlUtils.eq("accountguid", auditOnlineRegister.getAccountguid());
                    // sqlUtils.setOrder("uploaddate", "desc");
                    List<AuditOnlineCloudFiles> auditOnlineCloudFiles = iAuditOnlineCloudFiles
                            .getAllAuditOnlineCloudFiles(sqlUtils.getMap());
                    int cloudFilesCount = auditOnlineCloudFiles == null ? 0 : auditOnlineCloudFiles.size();
                    // 获取历史材料数量
                    SqlConditionUtil sqlc = new SqlConditionUtil();
                    sqlc.in("ownerno", auditOnlineRegister.getIdnumber());
                    sqlc.eq("firstflag", ZwfwConstant.CONSTANT_STR_ONE);
                    sqlc.nq("projectguid", projectGuid);
                    List<AuditMaterialLibrary> pagedataLibrarys = iAuditMaterialLibrary
                            .getAuditMaterialLibraryListByPage(sqlc.getMap(), 0, 0, "", "").getResult().getList();
                    int historyFilesCount = pagedataLibrarys == null ? 0 : pagedataLibrarys.size();
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("buttonflag", cloudFilesCount >= historyFilesCount ? 1 : 3);
                    // 返回对应文件的数量
                    if (cloudFilesCount >= historyFilesCount) {
                        // 返回云盘文件数量
                        dataJson.put("filenum", cloudFilesCount);
                    }
                    else {
                        // 返回历史材料数量，并返回满足匹配度的材料数量，默认80%
                        dataJson.put("filenum", historyFilesCount);
                        int upfilenum = 0;
                        TextSimilarity similarity = new CosineSimilarity();
                        DecimalFormat df = new DecimalFormat("0.0000");
                        String materialName = "";
                        if (StringUtil.isNotBlank(taskMaterialGuid)) {
                            Record auditTaskMaterial = iAuditTaskMaterial
                                    .getAuditTaskMaterialByRowguid(taskMaterialGuid).getResult();
                            if (auditTaskMaterial != null) {
                                materialName = auditTaskMaterial.getStr("materialname");
                            }
                            else {
                                Record auditSpIMaterial = iAuditSpIMaterial.getSpIMaterialByrowguid(projectMaterialGuid)
                                        .getResult();
                                if (auditSpIMaterial != null) {
                                    materialName = auditSpIMaterial.getStr("materialname");
                                }
                            }
                        }
                        // 5、 遍历所有首次提交的材料，计算相似度
                        if (ValidateUtil.isNotBlankCollection(pagedataLibrarys)) {
                            for (Record record : pagedataLibrarys) {
                                try {
                                    Double doubleSimilarity = similarity.getSimilarity(materialName,
                                            record.getStr("MaterialName"));
                                    if (doubleSimilarity >= 0.8) {
                                        upfilenum++;
                                    }
                                }
                                catch (Exception ex) {
                                    ex.printStackTrace();
                                    record.put("Similarity", 0);
                                }
                            }
                            dataJson.put("upfilenum", upfilenum);
                        }
                    }

                    return JsonUtils.zwdtRestReturn("1", "获取按钮排序成功", dataJson.toString());
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息异常", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======setFirstButton接口参数：params【" + params + "】=======");
            log.info("=======setFirstButton异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取按钮排序异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取一件事事项列表
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/getProjectList", method = RequestMethod.POST)
    public String getProjectList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getProjectList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取一件事实例标识
                String biguid = obj.getString("projectguid");
                // 获取一件事实例标识
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    JSONObject dataJson = new JSONObject();
                    List<Record> projectList = new ArrayList<>();
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("biguid", biguid);
                    List<AuditProject> list = iAuditProject.getAuditProjectListByCondition(sql.getMap()).getResult();
                    Integer index = 1;
                    if (!list.isEmpty()) {
                        for (AuditProject auditProject : list) {
                            Record record = new Record();
                            record.put("index", index);
                            record.put("oldprojectguid", auditProject.getRowguid());
                            record.put("projectname", auditProject.getProjectname());
                            record.put("backreason", auditProject.get("backreason"));
                            record.put("status", iCodeItemsService.getItemTextByCodeName("办件状态",
                                    String.valueOf(auditProject.getStatus())));
                            AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
                                    .getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
                            AuditOnlineProject auditOnlineProject = iAuditOnlineProject
                                    .getOnlineProjectByApplyerGuid(biguid, auditOnlineIndividual.getApplyerguid())
                                    .getResult();
                            if (auditOnlineProject != null) {
                                String type = auditOnlineProject.getType();
                                // 如果是普通办件，返回普通办件的JSON数据
                                if (ZwdtConstant.ONLINEPROJECTTYPE_PROJECT.equals(type)) {
                                    String projectGuid = auditOnlineProject.getSourceguid();
                                    // 获取事项基本信息
                                    AuditTask auditTask = iAuditTask
                                            .getAuditTaskByGuid(auditOnlineProject.getTaskguid(), true).getResult();
                                    if (auditTask != null) {
                                        record.put("projectform", auditTask.getStr("projectform"));
                                        record.put("projectguid", projectGuid); // 办件标识
                                        record.put("taskguid", auditOnlineProject.getTaskguid()); // 事项标识
                                        record.put("centerguid", auditOnlineProject.getCenterguid()); // 中心标识
                                        record.put("type", Integer.parseInt(auditOnlineProject.getType())); // 类型
                                                                                                            // 0：普通办件
                                                                                                            // 1：套餐
                                        record.put("applydate", EpointDateUtil.convertDate2String(
                                                auditOnlineProject.getApplydate(), EpointDateUtil.DATE_FORMAT)); // 办件申请时间
                                        record.put("applyertype", auditOnlineProject.getApplyertype()); // 办件申请人类型
                                        record.put("taskcaseguid", auditOnlineProject.getTaskcaseguid()); // 办件多情形标识
                                    }
                                }
                                else if (ZwdtConstant.ONLINEPROJECTTYPE_BUSINESS.equals(auditOnlineProject.getType())) {
                                    // 如果是套餐，返回套餐的JSON数据
                                    String biGuid = auditOnlineProject.getSourceguid();
                                    // 获取套餐的基本信息拼接JSON
                                    record.put("biguid", biGuid); // 主题实例标识
                                    record.put("projectguid", biGuid); // 主题实例标识
                                    record.put("taskguid", auditOnlineProject.getTaskguid()); // 主题标识
                                    record.put("centerguid", auditOnlineProject.getCenterguid()); // 中心标识
                                    record.put("type", Integer.parseInt(auditOnlineProject.getType())); // 类型
                                                                                                        // 0：普通办件
                                                                                                        // 1：套餐
                                    record.put("applyertype", auditOnlineProject.getApplyertype()); // 办件申请人类型
                                    record.put("taskcaseguid", auditOnlineProject.getTaskcaseguid()); // 办件多情形标识
                                    // 获取业务标识、子申报标识、阶段标识
                                    AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biGuid)
                                            .getResult();
                                    record.put("yewuguid",
                                            auditSpInstance == null ? "" : auditSpInstance.getYewuguid());
                                    List<AuditSpISubapp> auditSpISubapps = iAuditSpISubapp.getSubappByBIGuid(biGuid)
                                            .getResult();
                                    if (auditSpISubapps != null && !auditSpISubapps.isEmpty()) {
                                        AuditSpISubapp auditSpISubapp = auditSpISubapps.get(0);
                                        record.put("subappguid", auditSpISubapp.getRowguid());
                                        record.put("phaseguid", auditSpISubapp.getPhaseguid());
                                    }
                                }
                            }
                            projectList.add(record);
                            index++;
                        }
                        dataJson.put("projectList", projectList);
                    }

                    return JsonUtils.zwdtRestReturn("1", "获取事项列表成功", dataJson.toString());

                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息异常", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (

        Exception e) {
            e.printStackTrace();
            log.info("=======getProjectList接口参数：params【" + params + "】=======");
            log.info("=======getProjectList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "getProjectList异常：" + e.getMessage(), "");
        }
    }

    /**
     * 撤销申请按钮
     *
     * @param params
     *            接口的入参
     * @return
     */
    @RequestMapping(value = "/deleteProject", method = RequestMethod.POST)
    public String deleteProject(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用deleteProject接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取一件事实例标识
                String biguid = obj.getString("biguid");
                // 1.2、获取单办件实例
                String projectguid = obj.getString("projectguid");
                String areacode = obj.getString("areacode");
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 一件事办件
                    AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
                            .getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
                    AuditOnlineProject auditOnlineProject = iAuditOnlineProject
                            .getOnlineProjectByApplyerGuid(biguid, auditOnlineIndividual.getApplyerguid()).getResult();
                    if (auditOnlineProject != null) {
                        auditOnlineProject.setStatus(String.valueOf(ZwfwConstant.BANJIAN_STATUS_YSL));// 一件事状态改为办理中
                        iAuditOnlineProject.updateProject(auditOnlineProject);
                    }
                    // 单办件
                    AuditProject auditProject = iAuditProject
                            .getAuditProjectByRowGuid("rowguid,status", projectguid, areacode).getResult();
                    if (auditProject != null) {
                        auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_CXSQ);// 单办件状态改为撤销申请
                        iAuditProject.updateProject(auditProject);
                    }
                    return JsonUtils.zwdtRestReturn("1", "撤销申请成功", "");
                }
                else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息异常", "");
                }
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======deleteProject接口参数：params【" + params + "】=======");
            log.info("=======deleteProject异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "deleteProject异常：" + e.getMessage(), "");
        }
    }

    /******************************************
     * 暂时无用接口
     *********************************************/
    /**
     * 套餐是否在用 套餐详情页面的申报按钮点击时，会调用此接口查询该套餐是否可以申报。现已不使用。
     *
     * @param params
     *            请求参数
     * @param request
     *            HTTP请求
     * @return
     */
    @RequestMapping(value = "/isUsed", method = RequestMethod.POST)
    public String isUsed(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用isUsed接口=======");
            // 1、接口传入参数转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 2、根据主键查询该套餐主题
                SqlConditionUtil conditionsql = new SqlConditionUtil();
                conditionsql.eq("BUSINESSTYPE", "2");
                conditionsql.eq("DEL", "0");
                conditionsql.eq("ROWGUID", obj.getString("businessguid"));
                String isUsed = "";// 全局变量，是否禁用
                boolean falg = false;
                AuditSpBusiness auditSpBusiness = new AuditSpBusiness();
                List<AuditSpBusiness> auditSpBusinessInfos = iAuditSpBusiness
                        .getAllAuditSpBusiness(conditionsql.getMap()).getResult();
                if (auditSpBusinessInfos != null && auditSpBusinessInfos.size() > 0) {
                    // 2.1套餐主题存在，且没有逻辑删除，标记为启用
                    auditSpBusiness = auditSpBusinessInfos.get(0);
                    isUsed = ZwfwConstant.CONSTANT_STR_ZERO;
                }
                else {
                    // 2.2套餐主题不存在，标记为禁用
                    isUsed = ZwfwConstant.CONSTANT_STR_ONE;
                }
                // 3、只有所有阶段中配置了事项，且事项都是应该都是审核通过的。
                List<AuditSpTask> auditSpTaskList = new ArrayList<AuditSpTask>();
                if (auditSpBusiness != null) {
                    // 3.1查询该主题阶段
                    List<AuditSpPhase> auditSpPhaseInfo = iAuditSpPhase
                            .getSpPaseByBusinedssguid(auditSpBusiness.getRowguid()).getResult();
                    if (auditSpPhaseInfo != null && auditSpPhaseInfo.size() > 0) {
                        // 3.2有主题阶段，默认取第一阶段。套餐只有一个阶段。
                        auditSpTaskList = iAuditSpTask
                                .getAllAuditSpTaskByPhaseguid(auditSpPhaseInfo.get(0).getRowguid()).getResult();
                        // 3.3查询该阶段下的所有事项
                        if (auditSpTaskList != null && auditSpTaskList.size() > 0) {
                            // 3.4根据TaskID循环所有事项，只查询在用的事项
                            for (AuditSpTask auditSpTask : auditSpTaskList) {
                                // 查询并遍历标准事项关联关系表
                                SqlConditionUtil sql = new SqlConditionUtil();
                                sql.eq("basetaskguid", auditSpTask.getStr("basetaskguid"));
                                List<AuditSpBasetaskR> listr = iAuditSpBasetaskR
                                        .getAuditSpBasetaskrByCondition(sql.getMap()).getResult();
                                for (AuditSpBasetaskR auditSpBasetaskR : listr) {
                                    AuditTask auditTask = iAuditTask
                                            .selectUsableTaskByTaskID(auditSpBasetaskR.getTaskid()).getResult();
                                    if (auditTask != null) {
                                        // 3.5只要该套餐中有一个事项是在用的，则该套餐可以申报。反之，如果该套餐的事项全部不在用，则该套餐不能申报。
                                        falg = true;
                                    }
                                }
                            }
                        }
                        else {
                            // 3.6没有事项
                            return JsonUtils.zwdtRestReturn("0", "该套餐没有配置事项", "");
                        }
                    }
                    else {
                        // 3.7没有阶段
                        return JsonUtils.zwdtRestReturn("0", "该套餐没有配置阶段", "");
                    }
                }
                else {
                    // 没有主题
                    return JsonUtils.zwdtRestReturn("0", "查无此套餐", "");
                }
                // 定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                if ("0".equals(isUsed) && falg) {
                    // 4、通过
                    dataJson.put("flag", "1");
                }
                else {
                    dataJson.put("flag", "0");
                }
                log.info("=======结束调用isUsed接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取套餐是否可用信息成功", dataJson.toString());
            }
            else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("=======isUsed接口参数：params【" + params + "】=======");
            log.info("=======isUsed异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取套餐是否可用信息失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取工改一件事列表
     * 
     * @param params
     * @return
     */
    @RequestMapping(value = "/getggyjslist", method = RequestMethod.POST)
    public String getggyjslist(@RequestBody String params) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(params);
            SqlConditionUtil conditionUtil = new SqlConditionUtil();
            conditionUtil.eq("isggyjs", ZwfwConstant.CONSTANT_STR_ONE);
            conditionUtil.eq("areacode", "370800");
            String orderCloumn = "ordernumber";
            String ordertype = "asc";
            List<AuditSpBusiness> auditSpBusinessList = iAuditSpBusiness
                    .getAuditSpBusinessPageData(conditionUtil.getMap(), 0, 100, orderCloumn, ordertype).getResult()
                    .getList();

            JSONObject returnJson = new JSONObject();
            JSONArray auditSpBusinessArray = new JSONArray();
            Set<String> keys = new HashSet<>();
            if (StringUtil.isNotBlank(auditSpBusinessList) && !auditSpBusinessList.isEmpty()) {
                for (AuditSpBusiness auditSpBusiness : auditSpBusinessList) {
                    if (!keys.contains(auditSpBusiness.getBusinessname())) {
                        JSONObject auditSpBusinessObject = new JSONObject();
                        auditSpBusinessObject.put("rowguid", auditSpBusiness.getRowguid());
                        auditSpBusinessObject.put("businessname", auditSpBusiness.getBusinessname());
                        auditSpBusinessArray.add(auditSpBusinessObject);
                        keys.add(auditSpBusiness.getBusinessname());
                    }
                }
            }
            returnJson.put("list", auditSpBusinessArray);

            return JsonUtils.zwdtRestReturn("1", "工改一件事列表获取成功", returnJson.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "获取工改一件事列表异常", "");
        }
    }

    /**
     * 获取用户唯一标识
     * 
     * @param httpServletRequest
     * @return
     */
    private AuditOnlineRegister getOnlineRegister(HttpServletRequest httpServletRequest) {
        AuditOnlineRegister auditOnlineRegister;
        OAuthCheckTokenInfo oAuthCheckTokenInfo = CheckTokenUtil.getCheckTokenInfo(httpServletRequest);
        if (oAuthCheckTokenInfo != null) {
            // 手机端
            // 通过登录名获取用户
            auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(oAuthCheckTokenInfo.getLoginid())
                    .getResult();
        }
        else {
            // PC端
            String accountGuid = ZwdtUserSession.getInstance("").getAccountGuid();
            if (StringUtil.isNotBlank(accountGuid)) {
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByAccountguid(accountGuid).getResult();
            }
            else {
                // 通过登录名获取用户
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(Authenticator.getCurrentIdentity())
                        .getResult();
            }
        }
        return auditOnlineRegister;
    }

    /**
     * 获取该套餐是否被收藏
     * 
     * @param clientIdentifier
     * @param accountGuid
     * @return
     */
    public boolean isCollect(String clientIdentifier, String accountGuid) {
        SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
        sqlConditionUtil.eq("clientidentifier", clientIdentifier);
        sqlConditionUtil.eq("accountguid", accountGuid);
        int count = iAuditOnlineCollection.getAuditCollectionCount(sqlConditionUtil.getMap()).getResult();
        if (count > 0) {
            return true;
        }
        return false;
    }

}
