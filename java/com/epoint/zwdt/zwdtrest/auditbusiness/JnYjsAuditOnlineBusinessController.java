package com.epoint.zwdt.zwdtrest.auditbusiness;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import com.epoint.auditspoptiontownship.api.IAuditSpOptiontownshipService;
import com.epoint.auditspoptiontownship.api.entity.AuditSpOptiontownship;
import com.epoint.authenticator.identity.Authenticator;
import com.epoint.authenticator.module.OAuthCheckTokenInfo;
import com.epoint.authenticator.utils.CheckTokenUtil;
import com.epoint.basic.auditflowsn.handleflowsn.inter.IHandleFlowSn;
import com.epoint.basic.auditonlineuser.auditonlinecompanygrant.domain.AuditOnlineCompanyGrant;
import com.epoint.basic.auditonlineuser.auditonlinecompanygrant.inter.IAuditOnlineCompanyGrant;
import com.epoint.basic.auditonlineuser.auditonlineindividual.domain.AuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineindividual.inter.IAuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlinemessages.inter.IAuditOnlineMessages;
import com.epoint.basic.auditonlineuser.auditonlineproject.domain.AuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineproject.inter.IAuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditproject.auditprojectoperation.inter.IAuditProjectOperation;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyBaseinfo;
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
import com.epoint.basic.auditsp.element.domain.AuditSpElement;
import com.epoint.basic.auditsp.element.inter.IAuditSpElementService;
import com.epoint.basic.auditsp.option.domain.AuditSpOption;
import com.epoint.basic.auditsp.option.inter.IAuditSpOptionService;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.element.domain.AuditTaskElement;
import com.epoint.basic.audittask.element.inter.IAuditTaskElementService;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.option.domain.AuditTaskOption;
import com.epoint.basic.audittask.option.inter.IAuditTaskOptionService;
import com.epoint.basic.authentication.UserSession;
import com.epoint.common.util.*;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.composite.auditsp.handlespimaterial.inter.IHandleSPIMaterial;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.classpath.ClassPathUtil;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.web.WebUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.workflow.service.common.util.WorkflowKeyNames9;
import com.epoint.xmz.wjw.api.ICxBusService;
import com.epoint.xmz.yjsczcapplyer.api.IYjsCzcApplyerService;
import com.epoint.xmz.zjxl.util.AesDemoUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/jnYjsZwdtBusiness")
public class JnYjsAuditOnlineBusinessController {

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 主题api
     */
    @Autowired
    private IAuditSpBusiness iAuditSpBusiness;

    @Autowired
    private IAttachService attachService;

    @Autowired
    private IYjsCzcApplyerService iYjsCzcApplyerService;

    /**
     * 企业授权API
     */
    @Autowired
    private IAuditOnlineCompanyGrant iAuditOnlineCompanyGrant;

    /**
     * 法人API
     */
    @Autowired
    private IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo;

    @Autowired
    private IConfigService configServicce;

    @Autowired
    private ICxBusService iCxBusService;

    /**
     * 微信消息推送相关API
     */
    @Autowired
    private IAuditOnlineMessages iAuditOnlineMessages;

    /**
     * 办件操作API
     */
    @Autowired
    private IHandleProject iHandleProject;

    /**
     * 办件API
     */
    @Autowired
    private IAuditOnlineProject iAuditOnlineProject;

    /**
     * 子申报API
     */
    @Autowired
    private IAuditSpISubapp iAuditSpISubapp;

    /**
     * 主题实例API
     */
    @Autowired
    private IAuditSpInstance iAuditSpInstance;

    @Autowired
    private IAuditSpITask auditSpITaskService;

    /**
     * 问卷相关api
     */
    @Autowired
    private IAuditSpShareoption iauditspshareoption;

    @Autowired
    private ICodeItemsService codeItemsService;

    /**
     * 流水号API
     */
    @Autowired
    private IHandleFlowSn iHandleFlowSn;

    /**
     * 附件API
     */
    @Autowired
    private IAttachService iAttachService;

    /**
     * 网上用户
     */
    @Autowired
    private IAuditOnlineRegister iAuditOnlineRegister;

    /**
     * 阶段事项API
     */
    @Autowired
    private IAuditSpTask iAuditSpTask;

    @Autowired
    private IAuditSpBasetask basetaskService;

    /**
     * 材料API
     */
    @Autowired
    private IAuditSpIMaterial iAuditSpIMaterial;

    @Autowired
    private IAuditSpBasetaskR basetaskRService;

    /**
     * 共享材料关系API
     */
    @Autowired
    private IAuditSpShareMaterialRelation iAuditSpShareMaterialRelation;

    @Autowired
    private IAuditSpSelectedOptionService iAuditSpSelectedOptionService;

    /**
     * 主题共享材料API
     */
    @Autowired
    private IAuditSpShareMaterial iAuditSpShareMaterial;

    @Autowired
    private IAuditTask auditTaskService;

    @Autowired
    private IAuditTaskElementService iaudittaskelementservice;

    @Autowired
    private IAuditTaskOptionService iaudittaskoptionservice;

    /**
     * 材料API
     */
    @Autowired
    private IHandleSPIMaterial iHandleSPIMaterial;
    /**
     * 关联事项API
     */
    @Autowired
    private IAuditSpBasetaskR iAuditSpBasetaskR;

    @Autowired
    private IAuditSpPhase iAuditSpPhase;

    /**
     * 事项API
     */
    @Autowired
    private IAuditTask iAuditTask;

    /**
     * 办件操作API
     */
    @Autowired
    private IAuditProjectOperation iAuditProjectOperation;

    /**
     * 事项材料API
     */
    @Autowired
    private IAuditTaskMaterial iAuditTaskMaterial;


    /**
     * 代码项API
     */
    @Autowired
    private ICodeItemsService iCodeItemsService;

    /**
     * 门户用户API
     */
    @Autowired
    private IAuditOnlineIndividual iAuditOnlineIndividual;

    /**
     * 事项情形API
     */
    @Autowired
    private IAuditSpElementService iAuditSpElementService;

    /**
     * 套餐式开公司API
     */
    @Autowired
    private IAuditSpIntegratedCompany iAuditSpIntegratedCompany;

    /**
     * 情形选项API
     */
    @Autowired
    private IAuditSpOptionService iAuditSpOptionService;

    @Autowired
    IAuditSpOptiontownshipService iauditspoptiontownshipservice;

    /**
     * 一件事列表获取接口
     *
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/getYjsList", method = RequestMethod.POST)
    public String getYjsList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getYjsList接口=======");
            // 入参
            JSONObject object = JSONObject.parseObject(params);
            JSONObject paramsObject = object.getJSONObject("params");
            String type = paramsObject.getString("type");
            String keyWord = paramsObject.getString("keyword");

            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
            // 我要开店
            if ("wykd".equals(type)) {
                sqlConditionUtil.eq("businesskind", "10");
            } else if ("ggfw".equals(type)) {
                sqlConditionUtil.eq("is_ggfw", "1");
            }
            // 主题状态：启用
            sqlConditionUtil.eq("del", "0");
            // 主题分类：一般并联审批（套餐）
            sqlConditionUtil.eq("BUSINESSTYPE", "2");
            // 按排序字段降序
            sqlConditionUtil.setOrderDesc("ORDERNUMBER");
            sqlConditionUtil.like("businessname", keyWord);

            List<AuditSpBusiness> auditSpBusinessInfos = iAuditSpBusiness
                    .getAllAuditSpBusiness(sqlConditionUtil.getMap()).getResult();

            List<JSONObject> businessList = new ArrayList<JSONObject>();
            for (AuditSpBusiness auditSpBusiness : auditSpBusinessInfos) {
                List<AuditSpTask> auditSpTaskList = iAuditSpTask
                        .getAllAuditSpTaskByBusinessGuid(auditSpBusiness.getRowguid()).getResult();
                if (auditSpTaskList != null && !auditSpTaskList.isEmpty()) {
                    JSONObject businessJson = new JSONObject();
                    if ("0".equals(auditSpBusiness.get("is_ggfw"))) {
                        businessJson.put("is_ggfw", true);
                    } else {
                        businessJson.put("is_ggfw", false);
                    }
                    businessJson.put("businessname", auditSpBusiness.getBusinessname());
                    businessJson.put("introduction", auditSpBusiness.getStr("introduction"));
                    if (StringUtil.isNotBlank(auditSpBusiness.getStr("introduction"))
                            && auditSpBusiness.getStr("introduction").length() > 30) {
                        businessJson.put("istoolong", false);
                    } else {
                        businessJson.put("istoolong", true);
                    }
                    businessJson.put("note", auditSpBusiness.getNote());
                    businessJson.put("former_handleday_count",
                            auditSpBusiness.getInt("former_handleday_count") == null ? 0
                                    : auditSpBusiness.getInt("former_handleday_count"));
                    businessJson.put("now_handleday_count", auditSpBusiness.getInt("now_handleday_count") == null ? 0
                            : auditSpBusiness.getInt("now_handleday_count"));
                    businessJson.put("former_materials_count",
                            auditSpBusiness.getInt("former_materials_count") == null ? 0
                                    : auditSpBusiness.getInt("former_materials_count"));
                    businessJson.put("now_materials_count", auditSpBusiness.getInt("now_materials_count") == null ? 0
                            : auditSpBusiness.getInt("now_materials_count"));

                    // 我要开店
                    if ("wykd".equals(type)) {
                        businessJson.put("url", "./condition_choose?businessguid=" + auditSpBusiness.getRowguid()
                                + "&subappguid=" + UUID.randomUUID().toString());
                    } else if ("ggfw".equals(type)) {
                        String taskId = "";
                        String taskguid = "";
                        // 第一条标准事项
                        AuditSpBasetask basetask = basetaskService
                                .getAuditSpBasetaskByrowguid(auditSpTaskList.get(0).getBasetaskguid()).getResult();
                        // 标准事项关联的事项
                        if (basetask != null) {
                            sqlConditionUtil.clear();
                            sqlConditionUtil.eq("basetaskguid", basetask.getRowguid());
                            List<AuditSpBasetaskR> taskrlist = basetaskRService
                                    .getAuditSpBasetaskrByCondition(sqlConditionUtil.getMap()).getResult();
                            if (taskrlist != null && !taskrlist.isEmpty()) {
                                taskId = taskrlist.get(0).getTaskid();
                                AuditTask auditTask = auditTaskService.getUseTaskAndExtByTaskid(taskId).getResult();
                                if (auditTask != null) {
                                    taskguid = auditTask.getRowguid();
                                }
                            }
                        }

                        if ("出生一件事".equals(auditSpBusiness.getBusinessname())) {
                            businessJson.put("url", "./condition_choose?businessguid=" + auditSpBusiness.getRowguid()
                                    + "&subappguid=" + UUID.randomUUID().toString());
                        } else {
                            if ("企业登记档案网上查询".equals(auditSpBusiness.getBusinessname())) {
                                businessJson.put("url", "http://58.211.227.180:8880/Account/Login?ReturnUrl=%2f");
                            } else {
                                businessJson.put("url",
                                        "../../../epointzwmhwz/pages/eventdetail/personaleventdetail_new?taskguid="
                                                + taskguid + "&taskid=" + taskId);
                            }
                        }
                    }

                    // 主题图标
                    businessJson.put("iconsrc", "./images/index/icon7.png");
                    businessJson.put("iconsrc_h", "./images/index/icon7-h.png");
                    if (StringUtil.isNotBlank(auditSpBusiness.getStr("businessiconclientguid"))) {
                        List<FrameAttachInfo> frameAttachInfoList = attachService
                                .getAttachInfoListByGuid(auditSpBusiness.getStr("businessiconclientguid"));
                        if (frameAttachInfoList != null && !frameAttachInfoList.isEmpty()) {
                            String url = request.getRequestURL().substring(0, request.getRequestURL().indexOf("/rest/"))
                                    + "/rest/auditattach/readAttach?attachguid=";
                            businessJson.put("iconsrc", url + frameAttachInfoList.get(0).getAttachGuid());
                        }
                    }

                    if (StringUtil.isNotBlank(auditSpBusiness.getStr("activeiconclientguid"))) {
                        List<FrameAttachInfo> frameAttachInfoList = attachService
                                .getAttachInfoListByGuid(auditSpBusiness.getStr("activeiconclientguid"));
                        if (frameAttachInfoList != null && !frameAttachInfoList.isEmpty()) {
                            String url = request.getRequestURL().substring(0, request.getRequestURL().indexOf("/rest/"))
                                    + "/rest/auditattach/readAttach?attachguid=";
                            businessJson.put("iconsrc_h", url + frameAttachInfoList.get(0).getAttachGuid());
                        }

                    }

                    businessList.add(businessJson);
                }
            }
            // 定义返回JSON对象
            JSONObject dataJson = new JSONObject();
            dataJson.put("businessList", businessList);
            log.info("=======结束调用getYjsList接口=======");
            return JsonUtils.zwdtRestReturn("1", "获取一件事列表成功", dataJson.toString());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return JsonUtils.zwdtRestReturn("0", "获取一件事列表失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取电子表单信息
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getYjsEformInfoNew", method = RequestMethod.POST)
    public String getYjsEformInfoNew(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getYjsEformInfoNew接口=======");
            // 1、接口传入参数转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String businessguid = obj.getString("businessguid");
                String subappGuid = obj.getString("subappguid");
                String biguid = obj.getString("biguid");
                JSONObject dataJson = new JSONObject();
                // 1.1、获取用户信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                // 2、主题基本信息
                AuditSpBusiness auditSpBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessguid)
                        .getResult();

                JSONObject formInfo = new JSONObject();

                if (auditSpBusiness != null) {
                    formInfo.put("formid", auditSpBusiness.getStr("yjsformid"));
                    formInfo.put("yewuGuid", subappGuid);
                    formInfo.put("eformCommonPage", "jnzwdt/epointsform/eformpage");
                    formInfo.put("epointUrl", configServicce.getFrameConfigValue("epointsformurlwt"));

                    if ("216".equals(auditSpBusiness.getStr("yjsformid"))) {
                        String username = auditOnlineRegister.getUsername();
                        String mobile = auditOnlineRegister.getMobile();
                        String Idnumber = auditOnlineRegister.getIdnumber();
                        JSONObject commondata = new JSONObject();
                        commondata.put("cyryxm", username);
                        commondata.put("lxdh", mobile);
                        commondata.put("zjhm", Idnumber);
                        commondata.put("cjr", username);
                        formInfo.put("commondata", commondata);
                    }

                    if ("670".equals(auditSpBusiness.getStr("yjsformid"))) {
                        String username = auditOnlineRegister.getUsername();
                        String mobile = auditOnlineRegister.getMobile();
                        String Idnumber = auditOnlineRegister.getIdnumber();
                        JSONObject commondata = new JSONObject();
                        commondata.put("jshqsxm", username);
                        commondata.put("sjhm", mobile);
                        commondata.put("sfzh", Idnumber);
                        formInfo.put("commondata", commondata);
                    }

                    dataJson.put("formInfo", formInfo);
                }

                dataJson.put("usertype", auditSpBusiness.getStr("issqlb"));

                // 2.1、申请人信息字段
                JSONObject applyerInfo = new JSONObject();

                if (auditOnlineRegister != null) {
                    AuditOnlineIndividual individual = iAuditOnlineIndividual
                            .getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
                    if (individual != null) {
                        applyerInfo.put("applyerName", individual.getClientname());
                        applyerInfo.put("idNumber", individual.getIdnumber());
                        applyerInfo.put("contactTel", individual.getContactmobile());
                        applyerInfo.put("address", individual.getDeptaddress());
                    }
                    dataJson.put("applyerInfo", applyerInfo);
                }

                log.info(dataJson);

                String result = "";
                AesDemoUtil aes = null;
                try {
                    aes = new AesDemoUtil();

                    result = aes.encrypt(dataJson.toString().getBytes("UTF-8"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return JsonUtils.zwdtRestReturn("1", "获取电子表单信息成功", result);
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getYjsEformInfoNew接口参数：params【" + params + "】=======");
            log.info("=======getYjsEformInfoNew异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取电子表单信息失败：" + e.getMessage(), "");
        }

    }

    /**
     * 获取电子表单信息
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getYjsEformInfo", method = RequestMethod.POST)
    public String getYjsEformInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getYjsEformInfo接口=======");
            // 1、接口传入参数转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String businessguid = obj.getString("businessguid");
                String subappGuid = obj.getString("subappguid");
                String biguid = obj.getString("biguid");
                JSONObject dataJson = new JSONObject();
                // 1.1、获取用户信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                // 2、主题基本信息
                AuditSpBusiness auditSpBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessguid)
                        .getResult();
                JSONObject formInfo = new JSONObject();

                if (auditSpBusiness != null) {
                    formInfo.put("formid", auditSpBusiness.getStr("yjsformid"));
                    formInfo.put("yewuGuid", subappGuid);
                    formInfo.put("eformCommonPage", "jnzwdt/epointsform/eformpage");
                    formInfo.put("epointUrl", configServicce.getFrameConfigValue("epointsformurlwt"));

                    if ("216".equals(auditSpBusiness.getStr("yjsformid"))) {
                        String username = auditOnlineRegister.getUsername();
                        String mobile = auditOnlineRegister.getMobile();
                        String Idnumber = auditOnlineRegister.getIdnumber();
                        JSONObject commondata = new JSONObject();
                        commondata.put("cyryxm", username);
                        commondata.put("lxdh", mobile);
                        commondata.put("zjhm", Idnumber);
                        commondata.put("cjr", username);
                        formInfo.put("commondata", commondata);
                    }

                    if ("670".equals(auditSpBusiness.getStr("yjsformid"))) {
                        String username = auditOnlineRegister.getUsername();
                        String mobile = auditOnlineRegister.getMobile();
                        String Idnumber = auditOnlineRegister.getIdnumber();
                        JSONObject commondata = new JSONObject();
                        commondata.put("jshqsxm", username);
                        commondata.put("sjhm", mobile);
                        commondata.put("sfzh", Idnumber);
                        formInfo.put("commondata", commondata);
                    }

                }

                dataJson.put("formInfo", formInfo);

                dataJson.put("usertype", auditSpBusiness.getStr("issqlb"));

                // 2.1、申请人信息字段
                JSONObject applyerInfo = new JSONObject();

                if (auditOnlineRegister != null) {
                    AuditOnlineIndividual individual = iAuditOnlineIndividual
                            .getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
                    if (individual != null) {
                        applyerInfo.put("applyerName", individual.getClientname());
                        applyerInfo.put("idNumber", individual.getIdnumber());
                        applyerInfo.put("contactTel", individual.getContactmobile());
                        applyerInfo.put("address", individual.getDeptaddress());
                    }
                }
                dataJson.put("applyerInfo", applyerInfo);

                log.info(dataJson);
                return JsonUtils.zwdtRestReturn("1", "获取一件事表单信息成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getYjsEformInfo接口参数：params【" + params + "】=======");
            log.info("=======getYjsEformInfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取电子表单信息失败：" + e.getMessage(), "");
        }

    }

    /**
     * 主题基本信息的接口
     *
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/getYjsInfo", method = RequestMethod.POST)
    public String getYjsInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getYjsInfo接口=======");
            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");

            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String businessguid = obj.getString("businessguid");
                String biGuid = obj.getString("biguid");
                // 2、主题基本信息
                AuditSpBusiness auditSpBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessguid)
                        .getResult();
                if (auditSpBusiness == null) {
                    auditSpBusiness = new AuditSpBusiness();
                }
                // 3、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                List<AuditSpPhase> result = iAuditSpPhase.getSpPaseByBusinedssguid(businessguid).getResult();
                if(result.size()>0&&!result.isEmpty()){
                    AuditSpPhase auditSpPhase = result.get(0);
                    if(auditSpPhase!=null){
                        dataJson.put("phaseguid",auditSpPhase.getRowguid());
                    }
                }

                dataJson.put("businessname", auditSpBusiness.getBusinessname());// 主题名称
                dataJson.put("note", auditSpBusiness.getNote());// 主题说明
                dataJson.put("userType", auditSpBusiness.getStr("issqlb"));// 主题说明
                dataJson.put("linktel", auditSpBusiness.getLink_tel());// 咨询电话
                dataJson.put("handleaddress", auditSpBusiness.getTransact_addr());// 办公地点
                dataJson.put("handletime", auditSpBusiness.getTransact_time());// 办公时间
                dataJson.put("areacode", auditSpBusiness.getAreacode());
                dataJson.put("transactaddr", auditSpBusiness.getTransact_addr());
                dataJson.put("issqsb", auditSpBusiness.getStr("issqsb"));// 是否跳转双全双百
                dataJson.put("thirdurl", auditSpBusiness.getStr("thirdurl"));//跳转的第三方链接地址
                dataJson.put("isggyjs", auditSpBusiness.getStr("isggyjs")); // 是否工改一件事
                dataJson.put("phaseid", auditSpBusiness.getStr("phaseid")); // 跳转阶段
                JSONObject businessinfo = new JSONObject();
                businessinfo.put("syfw", auditSpBusiness.getStr("syfw") == null ? "" : auditSpBusiness.getStr("syfw"));
                businessinfo.put("sbtj", auditSpBusiness.getStr("sbtj") == null ? "" : auditSpBusiness.getStr("sbtj"));
                businessinfo.put("sjsx", auditSpBusiness.getStr("sjsx") == null ? "" : auditSpBusiness.getStr("sjsx"));
                businessinfo.put("bllc", auditSpBusiness.getStr("bllc") == null ? "" : auditSpBusiness.getStr("bllc"));
                businessinfo.put("blfy", auditSpBusiness.getStr("blfy") == null ? "" : auditSpBusiness.getStr("blfy"));
                businessinfo.put("blfs", auditSpBusiness.getStr("blfs") == null ? "" : auditSpBusiness.getStr("blfs"));
                businessinfo.put("linktel", auditSpBusiness.getLink_tel() == null ? "" : auditSpBusiness.getLink_tel());
                dataJson.put("businessinfo", businessinfo);

                String manageguid = auditSpBusiness.getStr("management");

                FrameAttachInfo frameAttachInfos = iAttachService.getAttachInfoDetail(manageguid);

                if (frameAttachInfos != null) {
                    dataJson.put("manageurl",
                            "http://jizwfw.sd.gov.cn/jnzwdt/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                    + frameAttachInfos.getAttachGuid());
                    dataJson.put("managename", frameAttachInfos.getAttachFileName());

                }

                dataJson.put("acceptcondition", auditSpBusiness.getAcceptCondition());
                dataJson.put("serviceobject",
                        auditSpBusiness.getStr("serviceobject") == null ? "" : auditSpBusiness.getStr("serviceobject"));

                dataJson.put("handledept", auditSpBusiness.get("handle_dept"));
                if (StringUtil.isNotBlank(auditSpBusiness.get("declaration_notice"))) {
                    dataJson.put("declarationNotice",
                            auditSpBusiness.getStr("declaration_notice").replace("。", "<br / >"));
                }
                dataJson.put("separatetime",
                        auditSpBusiness.getInt("separatetime") == null ? "" : auditSpBusiness.getInt("separatetime"));
                dataJson.put("togethertime",
                        auditSpBusiness.getInt("togethertime") == null ? "" : auditSpBusiness.getInt("togethertime"));
                dataJson.put("separatesubmit", auditSpBusiness.getInt("separatesubmit") == null ? ""
                        : auditSpBusiness.getInt("separatesubmit"));
                dataJson.put("togethersubmit", auditSpBusiness.getInt("togethersubmit") == null ? ""
                        : auditSpBusiness.getInt("togethersubmit"));
                dataJson.put("taskoutimg", auditSpBusiness.getTaskoutimgguid());
                dataJson.put("promise_day", auditSpBusiness.getPromise_day());
                dataJson.put("announcement", auditSpBusiness.getStr("announcement"));
                dataJson.put("formid", auditSpBusiness.getStr("formid"));

                dataJson.put("ifnewpage", auditSpBusiness.getStr("ifnewpage"));

                AuditOnlineRegister register = getOnlineRegister(request);
                if (register != null) {
                    dataJson.put("companyname", register.getUsername());
                    dataJson.put("zzjgdmz", register.getIdnumber());
                    dataJson.put("contactphone", register.getMobile());
                }

                // 办件编号
                if (StringUtil.isNotBlank(biGuid)) {
                    AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                    AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
                            .getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
                    AuditOnlineProject auditOnlineProject = iAuditOnlineProject
                            .getOnlineProjectByApplyerGuid(biGuid, auditOnlineIndividual.getApplyerguid()).getResult();
                    AuditSpInstance instance = iAuditSpInstance.getDetailByBIGuid(biGuid).getResult();
                    if (instance != null) {
                        AuditSpIntegratedCompany company = iAuditSpIntegratedCompany
                                .getAuditSpIntegratedCompanyByGuid(instance.getYewuguid()).getResult();
                        if (company != null) {
                            dataJson.put("address", company.getAddress());
                        }
                    }
                    if (auditOnlineProject != null) {
                        dataJson.put("flown", auditOnlineProject.getFlowsn());
                    }
                }

                log.info("=======结束调用getYjsInfo接口=======");
                return JsonUtils.zwdtRestReturn("1", "主题基本信息获取成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getYjsInfo接口参数：params【" + params + "】=======");
            log.info("=======getYjsInfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "主题基本信息获取失败：" + e.getMessage(), "");
        }
    }

    /**
     * 查询套餐下情形问题
     *
     * @param params  接口的入参
     * @param request
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

                // 必办事项的事项情形问题,找出elementguid 是root的选项的taskid
                List<AuditSpOption> auditSpOptions1 = iAuditSpOptionService.findListByCondition(sql.getMap())
                        .getResult();
                List<AuditTaskElement> audittaskelements = new ArrayList<AuditTaskElement>();
                if (!auditSpOptions1.isEmpty()) {
                    AuditSpOption auditspoption = auditSpOptions1.get(0);

                    String tasklists = "";
                    if (StringUtil.isNotBlank(auditspoption.getTaskid())) {
                        tasklists = auditspoption.getTaskid();
                    }
                    SqlConditionUtil sqltask = new SqlConditionUtil();
                    sqltask.isBlankOrValue("draft", ZwfwConstant.CONSTANT_STR_ZERO);
                    List<String> tasklist = new ArrayList<>();
                    if (StringUtil.isNotBlank(tasklists) && tasklists.contains(";")) {
                        tasklist = Arrays.asList(tasklists.split(";"));
                    } else {
                        tasklist.add(tasklists);
                    }
                    sqltask.in("taskid", StringUtil.joinSql(tasklist));
                    sqltask.rightLike("preoptionguid", "start");
                    sqltask.setOrderDesc("ordernum");
                    sqltask.setOrderAsc("operatedate");
                    audittaskelements = iaudittaskelementservice.findListByCondition(sqltask.getMap()).getResult();
                    if (audittaskelements != null && !audittaskelements.isEmpty()) {
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
                                } else {
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

                if (auditSpElements != null && !auditSpElements.isEmpty()) {
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
                            } else {
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

                JSONObject dataJson = new JSONObject();
                JSONObject shareJson = new JSONObject();
                dataJson.put("elementlist", elementJsonList);
                // 20200120界面加载第一层问题时先查询所有共享的选项
                List<AuditSpShareoption> auditspshareoptionlist = iauditspshareoption
                        .getlistBybusinessguid(businessGuid).getResult();
                if (!auditspshareoptionlist.isEmpty()) {

                    Map<String, List<AuditSpShareoption>> shareselarr = auditspshareoptionlist.stream()
                            .filter(a -> a.getSelecttype().equals(ZwfwConstant.CONSTANT_STR_TWO))
                            .collect(Collectors.groupingBy(a -> a.getSharevalue()));
                    for (Map.Entry<String, List<AuditSpShareoption>> entry : shareselarr.entrySet()) {
                        List<AuditSpShareoption> value = shareselarr.get(entry.getKey());
                        if (value != null && !value.isEmpty()) {
                            shareJson.put(entry.getKey(),
                                    value.stream().map(a -> a.getOptionguid()).collect(Collectors.joining(";")));

                        }
                    }
                }
                dataJson.put("sharesel", shareJson);

                log.info("=======结束调用getBusinessElement接口=======");
                return JsonUtils.zwdtRestReturn("1", "查询成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getBusinessElement接口参数：params【" + params + "】=======");
            log.info("=======getBusinessElement异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "查询失败", "");
        }
    }

    /**
     * 查询情形下事项情形问题
     *
     * @param params 接口的入参
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
                List<Record> seloptions = JSONArray.parseArray(selectedoptions, Record.class);
                // seloptions.add(e)
                List<String> numlist = new ArrayList<>();
                Map<String, String> sharesel = new HashMap<>();

                if (seloptions != null && !seloptions.isEmpty()) {
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
                    if ("".equals(optionguid)) {
                        sqlcase.eq("preoptionguid", "preoptionguid");
                    } else {
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
                } else {
                    if (StringUtil.isNotBlank(optionguid)) {
                        sqlcase.eq("preoptionguid", optionguid);

                    } else {
                        sqlcase.eq("preoptionguid", "preoptionguid");
                    }
                }
                sqlcase.isBlankOrValue("draft", "1");
                sqlcase.setOrder("ordernum desc,operatedate", "asc");
                // 2.2、 查询套餐问题

                List<AuditSpElement> auditSpElements = iAuditSpElementService.findListByCondition(sqlcase.getMap())
                        .getResult();
                List<JSONObject> subElementJsonList = new ArrayList<JSONObject>();
                if (auditSpElements != null && !auditSpElements.isEmpty()) {
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

                                } else if (share != null && numlist.contains(share.getSharevalue())) {
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
                            } else {
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
                    } else {
                        sqltask.eq("preoptionguid", optionguid);
                    }

                } else {
                    // task_id不为空标识是套餐情形下的事项问题
                    // 如果关联了事项获事项问题
                    List<String> tasklist = new ArrayList<>();
                    if (StringUtil.isNotBlank(task_id) && task_id.contains(";")) {
                        tasklist = Arrays.asList(task_id.split(";"));
                    } else {
                        tasklist.add(task_id);
                    }
                    sqltask.in("taskid", StringUtil.joinSql(tasklist));
                    sqltask.rightLike("preoptionguid", "start");
                }

                sqltask.setOrderDesc("ordernum");
                sqltask.setOrderAsc("operatedate");

                audittaskelements = iaudittaskelementservice.findListByCondition(sqltask.getMap()).getResult();

                // }

                if (audittaskelements != null && !audittaskelements.isEmpty()) {
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

                                } else if (share != null && numlist.contains(share.getSharevalue())) {
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
                            } else {
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
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getBusinessElementByOption接口参数：params【" + params + "】=======");
            log.info("=======getBusinessElementByOption异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "查询失败", "");
        }
    }

    /**
     * 根据情形问题答案查询事项列表
     *
     * @param params 接口的入参
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
                selectedOptions = this.getJsonToSelect(selectedOptions);
                // 清空spitask表
                auditSpITaskService.deleteSpITaskBySubappGuid(subappGuid);
                List<JSONObject> taskJsonList = new ArrayList<JSONObject>();
                String optionFormIds = "";
                if (selectedOptions.length() <= 2) {

                } else {
                    List<Record> taskElements = iHandleSPIMaterial
                            .initTasklistBySelectedOptions(selectedOptions, subappGuid, areaCode, businessGuid)
                            .getResult();

                    if (taskElements != null && !taskElements.isEmpty()) {
                        for (Record record : taskElements) {
                            optionFormIds = record.getStr("optionfromid");
                            JSONObject taskJson = new JSONObject();
                            taskJson.put("taskname", record.getStr("taskname"));
                            taskJson.put("taskid", record.getStr("taskid"));
                            taskJsonList.add(taskJson);
                        }
                    }
                }

                // 处理知识库 formids重复问题 WZJ
                if (StringUtil.isNotBlank(optionFormIds)) {
                    String[] formids = optionFormIds.split(",");
                    List<String> list = new ArrayList();
                    // 遍历数组往集合里存元素
                    for (int i = 0; i < formids.length; i++) {
                        // 如果集合里面没有相同的元素才往里存
                        if (!list.contains(formids[i])) {
                            list.add(formids[i]);
                        }
                    }

                    // toArray()方法会返回一个包含集合所有元素的Object类型数组
                    optionFormIds = String.join(",", list);
                }
                System.out.println("====20230338====optionFormIds" + optionFormIds);
                JSONObject dataJson = new JSONObject();
                dataJson.put("tasklist", taskJsonList);
                dataJson.put("optionFormIds", optionFormIds);
                log.info("=======结束调用getAuditSpTaskBySelectedOption接口=======");
                return JsonUtils.zwdtRestReturn("1", "查询成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getAuditSpTaskBySelectedOption接口参数：params【" + params + "】=======");
            log.info("=======getAuditSpTaskBySelectedOption异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "验证失败", "");
        }
    }

    /*  *//**
     * 查询材料列表
     *
     * @param params
     *            接口的入参
     * @return
     *//*
     * @RequestMapping(value = "/getElementBusinessMaterialList",
     * method = RequestMethod.POST) public String
     * getElementBusinessMaterialList(@RequestBody String
     * params, @Context HttpServletRequest request) { try {
     * log.info("=======开始调用getElementBusinessMaterialList接口=======");
     * // 1、接口的入参转化为JSON对象 // 在待提交列表中，打开多个待提交套餐，同一个套餐可以提交多次
     *
     * JSONObject jsonObject = JSONObject.parseObject(params); String
     * token = jsonObject.getString("token"); if
     * (ZwdtConstant.SysValidateData.equals(token)) { JSONObject obj =
     * (JSONObject) jsonObject.get("params"); // 1.1、获取主题标识 String
     * businessGuid = obj.getString("businessguid"); // 1.3、获取用户选项信息
     * String subappGuid = obj.getString("subappguid"); // 1.4、获取用户选项信息
     * String biGuid = obj.getString("biguid"); // 1.5、获取辖区编码 String
     * areaCode = obj.getString("areacode");
     *
     * String phaseGuid = obj.getString("phaseGuid");
     * AuditOnlineRegister auditOnlineRegister =
     * getOnlineRegister(request);
     *
     * // 所有选择 否 的guid列表 // List<String> optionlist = new
     * ArrayList<>(); // 所有事项id列表 // List<String> taskidlist = new
     * ArrayList<>(); // 将选择得数据添加到selected option表中去 if
     * (StringUtil.isBlank(phaseGuid)) { List<AuditSpPhase>
     * auditSpPhases =
     * iAuditSpPhase.getSpPaseByBusinedssguid(businessGuid).getResult()
     * ; if (auditSpPhases != null && !auditSpPhases.isEmpty()) {
     * phaseGuid = auditSpPhases.get(0).getRowguid(); } }
     * AuditSpSelectedOption auditspselectedoption =
     * iAuditSpSelectedOptionService
     * .getSelectedoptionsBySubappGuid(subappGuid).getResult(); String
     * selectedoptions = ""; // 按照传过来的数据进行解析，取出optionguidlist if
     * (auditspselectedoption != null &&
     * StringUtil.isNotBlank(auditspselectedoption.getSelectedoptions()
     * )) { selectedoptions =
     * auditspselectedoption.getSelectedoptions(); // 清空spimaterial表
     * iAuditSpIMaterial.deleteSpIMaterialBySubappguid(subappGuid); //
     * 清空spitask表
     * auditSpITaskService.deleteSpITaskBySubappGuid(subappGuid);
     * List<String> taskids = new ArrayList<>();
     *
     * List<Record> taskElements = iHandleSPIMaterial
     * .initTasklistBySelectedOptions(selectedoptions, subappGuid,
     * areaCode, businessGuid) .getResult(); if (taskElements != null
     * && !taskElements.isEmpty()) { for (Record record : taskElements)
     * { taskids.add(record.getStr("taskid")); } } if (taskids != null
     * && !taskids.isEmpty()) { // 重新插入spitask表 for (String taskid :
     * taskids) { Record auditTask =
     * iAuditTask.selectUsableTaskByTaskID(taskid).getResult(); if
     * (auditTask != null) {
     * auditSpITaskService.addTaskInstance(businessGuid, biGuid,
     * phaseGuid, auditTask.get("rowguid"), auditTask.get("taskname"),
     * subappGuid, auditTask.get("ordernum") == null ? 0 :
     * auditTask.get("ordernum"), auditTask.get("areacode"), ""); } }
     *
     * List<AuditSpIMaterial> materials = iHandleSPIMaterial
     * .initIntegratedMaterial(subappGuid, businessGuid, biGuid,
     * phaseGuid, "", "", areaCode) .getResult(); List<JSONObject>
     * materialList = new ArrayList<JSONObject>(); if (materials !=
     * null && !materials.isEmpty()) { int index = 1; for
     * (AuditSpIMaterial record : materials) { // 对应的事项材料
     * AuditTaskMaterial material = null; SqlConditionUtil
     * sqlConditionUtil = new SqlConditionUtil();
     * sqlConditionUtil.eq("materialid", record.getMaterialguid());
     * sqlConditionUtil.in("taskguid",
     * "select rowguid from audit_task  where (IS_HISTORY is null or IS_HISTORY ='0')  and IS_EDITAFTERIMPORT='1' and IS_ENABLE='1' "
     * );
     *
     * List<AuditTaskMaterial> taskMaterialList = iAuditTaskMaterial
     * .selectMaterialListByCondition(sqlConditionUtil.getMap()).
     * getResult(); if
     * (ValidateUtil.isNotBlankCollection(taskMaterialList)) { material
     * = taskMaterialList.get(0);
     *
     * }
     *
     * // 共享材料 else { AuditSpShareMaterial spShareMaterial =
     * iAuditSpShareMaterial
     * .getAuditSpShareMaterialByRowguid(record.getMaterialguid()).
     * getResult(); if (spShareMaterial != null) {
     * List<AuditSpShareMaterialRelation> spShareMaterialRList =
     * iAuditSpShareMaterialRelation
     * .selectByShareMaterialGuid(businessGuid,
     * spShareMaterial.getSharematerialguid()) .getResult(); if
     * (ValidateUtil.isNotBlankCollection(spShareMaterialRList)) {
     * sqlConditionUtil.clear(); sqlConditionUtil.eq("materialid",
     * spShareMaterialRList.get(0).getMaterialid());
     * sqlConditionUtil.in("taskguid",
     * "select rowguid from audit_task  where (IS_HISTORY is null or IS_HISTORY ='0')  and IS_EDITAFTERIMPORT='1' and IS_ENABLE='1' "
     * ); List<AuditTaskMaterial> shareTaskMaterialList =
     * iAuditTaskMaterial
     * .selectMaterialListByCondition(sqlConditionUtil.getMap())
     * .getResult();
     *
     * if (ValidateUtil.isNotBlankCollection(shareTaskMaterialList)) {
     * material = shareTaskMaterialList.get(0); } } } }
     *
     * JSONObject materialJson = new JSONObject();
     * materialJson.put("index", index++);// 序号
     *
     * // 是否必需 if ("10".equals(record.getNecessity())) {
     * materialJson.put("necessaity", "必需");
     * materialJson.put("necessaity_class", "necessary"); } else if
     * ("20".equals(record.getNecessity())) {
     * materialJson.put("necessaity", "非必需");
     * materialJson.put("necessaity_class", "unnecessary"); }
     *
     * if ("1".equals(record.getAllowrongque())) {
     * materialJson.put("allowrongque", "是");
     * materialJson.put("necessaity", "非必需");
     * materialJson.put("necessaity_class", "unnecessary"); //
     * materialJson.put("necessaity_class", // "necessary"); } else {
     * materialJson.put("allowrongque", "否"); //
     * materialJson.put("necessaity_class", // "unnecessary"); }
     *
     * // 材料名称 materialJson.put("materialname",
     * record.getStr("materialname")); materialJson.put("beizhu",
     * material.getFile_explian()); if (material != null) { // 来源渠道 if
     * (StringUtil.isNotBlank(material.getFile_source())) {
     * materialJson.put("source",
     * codeItemsService.getItemTextByCodeName("来源渠道",
     * material.getFile_source())); } else { materialJson.put("source",
     * ""); }
     *
     * String url = request.getRequestURL().substring(0,
     * request.getRequestURL().indexOf("/rest/")) +
     * "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=";
     * // 空白表格 if
     * (StringUtil.isNotBlank(material.getTemplateattachguid())) {
     * List<FrameAttachInfo> frameAttachInfos = iAttachService
     * .getAttachInfoListByGuid(material.getTemplateattachguid()); if
     * (!frameAttachInfos.isEmpty()) {
     * materialJson.put("templateattach", url +
     * frameAttachInfos.get(0).getAttachGuid());
     * materialJson.put("templateattachtext", "空白表格"); } }
     *
     * // 示例表格 if
     * (StringUtil.isNotBlank(material.getExampleattachguid())) {
     * List<FrameAttachInfo> frameAttachInfos = iAttachService
     * .getAttachInfoListByGuid(material.getExampleattachguid()); if
     * (!frameAttachInfos.isEmpty()) {
     * materialJson.put("exampleattachguid",
     * frameAttachInfos.get(0).getAttachGuid());
     * materialJson.put("exampleattach", url +
     * frameAttachInfos.get(0).getAttachGuid());
     * materialJson.put("exampleattachtext", "示例表格"); } } }
     *
     * // 提交方式 materialJson.put("submittype",
     * codeItemsService.getItemTextByCodeName("提交方式",
     * record.getSubmittype()));
     *
     * materialList.add(materialJson); } } JSONObject dataJson = new
     * JSONObject(); dataJson.put("materiallist", materialList);
     *
     * log.info("=======结束调用getElementBusinessMaterialList接口=======");
     * return JsonUtils.zwdtRestReturn("1", "材料初始化成功！",
     * dataJson.toString()); } else { return
     * JsonUtils.zwdtRestReturn("0", "请选择要办理的事项！", ""); }
     *
     * } else {
     *
     * return JsonUtils.zwdtRestReturn("0", "请选择事项情形！", ""); } } else {
     * return JsonUtils.zwdtRestReturn("0", "身份验证失败！", ""); } } catch
     * (Exception e) { e.printStackTrace();
     * log.info("=======getElementBusinessMaterialList接口参数：params【" +
     * params + "】=======");
     * log.info("=======getElementBusinessMaterialList异常信息：" +
     * e.getMessage() + "======="); return
     * JsonUtils.zwdtRestReturn("0", "验证失败", ""); } }
     */

    /**
     * 查询材料列表
     *
     * @param params 接口的入参
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

                // 所有选择 否 的guid列表
                // List<String> optionlist = new ArrayList<>();
                // 所有事项id列表
                // List<String> taskidlist = new ArrayList<>();
                // 将选择得数据添加到selected option表中去
                if (StringUtil.isBlank(phaseGuid)) {
                    List<AuditSpPhase> auditSpPhases = iAuditSpPhase.getSpPaseByBusinedssguid(businessGuid).getResult();
                    if (auditSpPhases != null && !auditSpPhases.isEmpty()) {
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
                    iAuditSpIMaterial.deleteSpIMaterialBySubappguid(subappGuid);
                    // 清空spitask表
                    auditSpITaskService.deleteSpITaskBySubappGuid(subappGuid);
                    List<String> taskids = new ArrayList<>();
                    List<Record> taskElements = iHandleSPIMaterial
                            .initTasklistBySelectedOptions(selectedoptions, subappGuid, areaCode, businessGuid)
                            .getResult();
                    if (taskElements != null && !taskElements.isEmpty()) {
                        for (Record record : taskElements) {
                            if (StringUtil.isNotBlank(record.getStr("optionguid"))) {
                                taskids.add(record.getStr("optionguid") + "_" + record.getStr("taskid"));
                            } else {
                                taskids.add(record.getStr("taskid"));
                            }
                        }
                    }
                    if (taskids != null && !taskids.isEmpty()) {
                        // 重新插入spitask表
                        for (String taskid : taskids) {
                            Record auditTask = iAuditTask.selectUsableTaskByTaskID(taskid.split("_")[1]).getResult();
                            List<String> list = new ArrayList<>();
                            if (auditTask != null) {
                                // 判断节点在audit_sp_optiontownship中是否有市级事项数据
                                SqlConditionUtil sqlutil = new SqlConditionUtil();
                                sqlutil.eq("optionguid", taskid.split("_")[0]);
                                sqlutil.eq("taskid", taskid.split("_")[1]);
                                sqlutil.eq("townshipcode", auditTask.get("areacode"));
                                List<AuditSpOptiontownship> shiplist = iauditspoptiontownshipservice
                                        .findListByCondition(sqlutil.getMap());
                                if (ValidateUtil.isNotBlankCollection(shiplist)) {
                                    // 防止出现重复的数据
                                    if (!list.contains(auditTask.get("areacode") + "_" + taskid.split("_")[1])) {
                                        auditSpITaskService.addTaskInstance(businessGuid, biGuid, phaseGuid,
                                                auditTask.get("rowguid"), auditTask.get("taskname"), subappGuid,
                                                auditTask.get("ordernum") == null ? 0 : auditTask.get("ordernum"),
                                                auditTask.get("areacode"), "", "", "");
                                        list.add(auditTask.get("areacode") + "_" + taskid.split("_")[1]);
                                    }
                                }
                                // 根据选项的optionguid以及事项的taskid判断事项是否下放到乡镇
                                sqlutil.clear();
                                shiplist.clear();
                                sqlutil.eq("optionguid", taskid.split("_")[0]);
                                sqlutil.eq("taskid", taskid.split("_")[1]);
                                sqlutil.eq("length(townshipcode)", "9");
                                shiplist = iauditspoptiontownshipservice.findListByCondition(sqlutil.getMap());
                                if (ValidateUtil.isNotBlankCollection(shiplist)) {
                                    for (AuditSpOptiontownship ship : shiplist) {
                                        if (!list.contains(ship.getTownshipcode() + "_" + taskid.split("_")[1])) {
                                            auditSpITaskService.addTaskInstance(businessGuid, biGuid, phaseGuid,
                                                    auditTask.get("rowguid"), auditTask.get("taskname"), subappGuid,
                                                    auditTask.get("ordernum") == null ? 0 : auditTask.get("ordernum"),
                                                    auditTask.get("areacode"), "", "", ship.getTownshipcode());
                                            list.add(ship.getTownshipcode() + "_" + taskid.split("_")[1]);
                                        }
                                    }
                                }
                                /*
                                 * auditSpITaskService.addTaskInstance(
                                 * businessGuid, biGuid, phaseGuid,
                                 * auditTask.get("rowguid"),
                                 * auditTask.get("taskname"), subappGuid,
                                 * auditTask.get("ordernum") == null ? 0 :
                                 * auditTask.get("ordernum"),
                                 * auditTask.get("areacode"), "");
                                 */
                            }
                        }

                        List<AuditSpIMaterial> materials = iHandleSPIMaterial
                                .initIntegratedMaterial(subappGuid, businessGuid, biGuid, phaseGuid, "", "", areaCode)
                                .getResult();
                        List<JSONObject> materialList = new ArrayList<JSONObject>();
                        if (materials != null && !materials.isEmpty()) {
                            int index = 1;
                            for (AuditSpIMaterial record : materials) {
                                // 对应的事项材料
                                AuditTaskMaterial material = null;
                                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                                sqlConditionUtil.eq("materialid", record.getMaterialguid());
//                                sqlConditionUtil.in("taskguid",
//                                        "select rowguid from audit_task  where (IS_HISTORY is null or IS_HISTORY ='0')  and IS_EDITAFTERIMPORT='1' and IS_ENABLE='1' ");

                                List<AuditTaskMaterial> taskMaterialList = iAuditTaskMaterial
                                        .selectMaterialListByCondition(sqlConditionUtil.getMap()).getResult();
                                log.info("事项材料信息==============" + taskMaterialList);

                                if (ValidateUtil.isNotBlankCollection(taskMaterialList)) {
                                    material = taskMaterialList.get(0);
                                    for (AuditTaskMaterial auditTaskMaterial : taskMaterialList) {
                                        if ("1".equals(auditTaskMaterial.getStr("isxtendfield"))) {
                                            material = auditTaskMaterial;
                                        }
                                    }
                                }

                                // 共享材料
                                else {
                                    AuditSpShareMaterial spShareMaterial = iAuditSpShareMaterial
                                            .getAuditSpShareMaterialByRowguid(record.getMaterialguid()).getResult();
                                    if (spShareMaterial != null) {
                                        List<AuditSpShareMaterialRelation> spShareMaterialRList = iAuditSpShareMaterialRelation
                                                .selectByShareMaterialGuid(businessGuid,
                                                        spShareMaterial.getSharematerialguid())
                                                .getResult();
                                        if (ValidateUtil.isNotBlankCollection(spShareMaterialRList)) {
                                            String taskid = spShareMaterialRList.get(0).getTaskid();
                                            AuditTask result = iAuditTask.getUseTaskAndExtByTaskid(taskid).getResult();
                                            String taskRowguid = result.getRowguid();
                                            sqlConditionUtil.clear();
                                            sqlConditionUtil.eq("materialid", spShareMaterialRList.get(0).getMaterialid());
                                            sqlConditionUtil.eq("taskguid", taskRowguid);
//                                            sqlConditionUtil.in("taskguid",
//                                                    "select rowguid from audit_task  where (IS_HISTORY is null or IS_HISTORY ='0')  and IS_EDITAFTERIMPORT='1' and IS_ENABLE='1' ");
                                            List<AuditTaskMaterial> shareTaskMaterialList = iAuditTaskMaterial
                                                    .selectMaterialListByCondition(sqlConditionUtil.getMap())
                                                    .getResult();
                                            log.info("共享材料材料信息=============" + shareTaskMaterialList);

                                            if (ValidateUtil.isNotBlankCollection(shareTaskMaterialList)) {
                                                material = shareTaskMaterialList.get(0);
                                            }
                                        }
                                        log.info("共享材料获取到的材料信息============" + material);
                                    }
                                }

                                JSONObject materialJson = new JSONObject();
                                materialJson.put("index", index++);// 序号

                                // 是否必需
                                if ("10".equals(record.getNecessity())) {
                                    materialJson.put("necessaity", "必需");
                                    materialJson.put("necessaity_class", "necessary");
                                } else if ("20".equals(record.getNecessity())) {
                                    materialJson.put("necessaity", "非必需");
                                    materialJson.put("necessaity_class", "unnecessary");
                                }

                                if ("1".equals(record.getAllowrongque())) {
                                    materialJson.put("allowrongque", "是");
                                    materialJson.put("necessaity", "非必需");
                                    materialJson.put("necessaity_class", "unnecessary");
                                    // materialJson.put("necessaity_class",
                                    // "necessary");
                                } else {
                                    materialJson.put("allowrongque", "否");
                                    // materialJson.put("necessaity_class",
                                    // "unnecessary");
                                }

                                // 材料名称
                                materialJson.put("materialname", record.getStr("materialname"));
                                if (material != null) {
                                    materialJson.put("beizhu", material.getFile_explian());
                                    // 来源渠道
                                    if (StringUtil.isNotBlank(material.getFile_source())) {
                                        materialJson.put("source", codeItemsService.getItemTextByCodeName("来源渠道",
                                                material.getFile_source()));
                                    } else {
                                        materialJson.put("source", "");
                                    }

                                    String url = request.getRequestURL().substring(0,
                                            request.getRequestURL().indexOf("/rest/"))
                                            + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=";
                                    // 空白表格
                                    if (StringUtil.isNotBlank(material.getTemplateattachguid())) {
                                        List<FrameAttachInfo> frameAttachInfos = iAttachService
                                                .getAttachInfoListByGuid(material.getTemplateattachguid());
                                        if (!frameAttachInfos.isEmpty()) {
                                            materialJson.put("templateattachguid",
                                                    frameAttachInfos.get(0).getAttachGuid());
                                            materialJson.put("templateattachtext", "空白表格");
                                        }
                                    }

                                    // 示例表格
                                    if (StringUtil.isNotBlank(material.getExampleattachguid())) {
                                        List<FrameAttachInfo> frameAttachInfos = iAttachService
                                                .getAttachInfoListByGuid(material.getExampleattachguid());
                                        if (!frameAttachInfos.isEmpty()) {
                                            materialJson.put("exampleattachguid",
                                                    frameAttachInfos.get(0).getAttachGuid());
                                            materialJson.put("exampleattachtext", "示例表格");
                                        }
                                    }

                                    //判断是否可以电子证照获取  by饶少亮
                                    if ("1".equals(material.getStr("isxtendfield"))) {
                                        materialJson.put("materialsource", "已关联电子证照，可免提交");
                                        materialJson.put("isxtendfield", 1);
                                        materialJson.put("isshuoming", 0);
                                    } else {
                                        materialJson.put("hidden", "hidden");
                                        materialJson.put("isxtendfield", 0);
                                        materialJson.put("isshuoming", 1);
                                    }
                                }
                                // 提交方式
                                materialJson.put("submittype",
                                        codeItemsService.getItemTextByCodeName("提交方式", record.getSubmittype()));
                                log.info("==============");

                                materialList.add(materialJson);
                            }
                        }
                        JSONObject dataJson = new JSONObject();
                        dataJson.put("materiallist", materialList);

                        log.info("=======结束调用getElementBusinessMaterialList接口=======");
                        return JsonUtils.zwdtRestReturn("1", "材料初始化成功！", dataJson.toString());
                    } else {
                        return JsonUtils.zwdtRestReturn("0", "请选择要办理的事项！", "");
                    }

                } else {

                    return JsonUtils.zwdtRestReturn("0", "请选择事项情形！", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getElementBusinessMaterialList接口参数：params【" + params + "】=======");
            log.info("=======getElementBusinessMaterialList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "验证失败", "");
        }
    }

    /**
     * 判断材料是否上传的接口
     *
     * @return
     * @params params
     */
    @SuppressWarnings("unused")
    @RequestMapping(value = "/checkMaterialIsUploadByClientguid", method = RequestMethod.POST)
    public String checkYJSMaterialIsUploadByClientguid(@RequestBody String params) {
        try {
            log.info("=======开始调用checkMaterialIsUploadByClientguid接口=======");
            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String clientguid = obj.getString("clientguid");// 附件guid
                String projectmaterialguid = obj.getString("projectmaterialguid");// 办件材料主键
                String sharematerialiguid = obj.getString("sharematerialiguid");// 共享材料实例主键
                String status = obj.getString("status");// 材料状态
                String type = obj.getString("type");// 材料类型
                // 2、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                int count = iAttachService.getAttachCountByClientGuid(clientguid);// 若初始化后从共享库同步附件到材料
                // 3、更新材料状态
                String biGuid = obj.getString("biGuid");
                System.out.println("checkMaterialIsUploadByClientguid==>biGuid:" + biGuid + "==>projectmaterialguid:"
                        + projectmaterialguid + "==>clientguid:" + clientguid + "==>count:" + count);

                // 2、定义返回JSON对象
                AuditSpIMaterial auditSpIMaterial = iAuditSpIMaterial.getSpIMaterialByrowguid(projectmaterialguid)
                        .getResult();
                int newstatus = Integer.parseInt(status);
                if (count > 0) {
                    switch (Integer.parseInt(status)) {
                        // 未提交=》电子
                        case ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT:
                            newstatus = ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC;
                            break;
                        // 纸质=》电子和纸质
                        case ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER:
                            newstatus = ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC;
                            break;
                        default:
                            break;
                    }
                } else {
                    switch (Integer.parseInt(status)) {
                        // 电子=》未提交
                        case ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC:
                            newstatus = ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT;
                            break;
                        // 纸质和电子=》纸质
                        case ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC:
                            newstatus = ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER;
                            break;
                        default:
                            break;
                    }
                    sharematerialiguid = "";
                }
                auditSpIMaterial.setStatus(String.valueOf(newstatus));
                iAuditSpIMaterial.updateSpIMaterial(auditSpIMaterial);

                // 5、判断按钮显示
                int showbutton = 0;
                if (StringUtil.isBlank(sharematerialiguid)) {
                    showbutton = count > 0 ? 1 : 0;
                } else {
                    List<FrameAttachInfo> list = iAttachService
                            .getAttachInfoListByGuid(auditSpIMaterial.getCliengguid());
                    showbutton = list.size() > 0 ? 1 : 0;
                    for (FrameAttachInfo frameAttachInfo : list) {
                        if ("dzzzglxt".equals(frameAttachInfo.getCliengTag())) {
                            showbutton = count > 0 ? 2 : 0;
                            break;
                        }
                    }
                    /*
                     * List<AuditSpShareMaterialRelation> list =
                     * iAuditSpShareMaterialRelation
                     * .selectByShareMaterialGuid(auditSpIMaterial.
                     * getBusinessguid(), auditSpIMaterial.getMaterialguid())
                     * .getResult(); if (list.isEmpty()) { showbutton = count >
                     * 0 ? 1 : 0; } else { showbutton = count > 0 ? 2 : 0; }
                     */
                }
                dataJson.put("showbutton", showbutton);
                dataJson.put("status", newstatus);
                dataJson.put("sharematerialiguid", sharematerialiguid);

                /*
                 * AuditSpISubapp auditSpISubapp =
                 * iAuditSpISubapp.getSubappByBIGuid(biGuid).getResult().get(0);
                 * List<AuditSpIMaterial> listMaterial = new
                 * ArrayList<AuditSpIMaterial>(); listMaterial =
                 * iAuditSpIMaterial.getSpIMaterialBySubappGuid(auditSpISubapp.
                 * getRowguid()).getResult();
                 *
                 * for (AuditSpIMaterial auditSpIMaterial : listMaterial) { int
                 * newstatus = Integer.parseInt(status); if (count > 0) { switch
                 * (Integer.parseInt(status)) { // 未提交=》电子 case
                 * ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT: newstatus =
                 * ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC; break; //
                 * 纸质=》电子和纸质 case ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER:
                 * newstatus =
                 * ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC;
                 * break; default: break; } } else { switch
                 * (Integer.parseInt(status)) { // 电子=》未提交 case
                 * ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC: newstatus =
                 * ZwfwConstant.PROJECTMATERIAL_STATUS_UNSUBMIT; break; //
                 * 纸质和电子=》纸质 case
                 * ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC:
                 * newstatus = ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER; break;
                 * default: break; } sharematerialiguid = ""; }
                 * auditSpIMaterial.setStatus(String.valueOf(newstatus));
                 * iAuditSpIMaterial.updateSpIMaterial(auditSpIMaterial);
                 *
                 * // 5、判断按钮显示 int showbutton = 0; if
                 * (StringUtil.isBlank(sharematerialiguid)) { // 表格：10 附件 20 if
                 * ("10".equals(type)) { // 0:未上传 1：已上传2：已引用证照库 3: 在线填表 4:已填表
                 * showbutton = count > 0 ? 4 : 3; } else { showbutton = count >
                 * 0 ? 1 : 0; } if (WorkflowKeyNames9.SubmitType_PaperSubmit ==
                 * Integer .parseInt(auditSpIMaterial.getSubmittype())) {
                 * showbutton = count > 0 ? 5 : 6; } else { showbutton = count >
                 * 0 ? 1 : 0; } } else { showbutton = count > 0 ? 2 : 0; }
                 * dataJson.put("showbutton", showbutton);
                 * dataJson.put("status", newstatus);
                 * dataJson.put("sharematerialiguid", sharematerialiguid); }
                 */
                log.info("=======结束调用checkMaterialIsUploadByClientguid接口=======");
                return JsonUtils.zwdtRestReturn("1", "判断是否上传材料成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            log.info("=======checkMaterialIsUploadByClientguid接口参数：params【" + params + "】=======");
            log.info("=======checkMaterialIsUploadByClientguid异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "判断是否上传材料异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取套餐材料信息的接口（继续申报至套餐申报页面调用、套餐详细页面调用）
     *
     * @param params 接口的入参
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
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理开始<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 1.2、获取用户注册信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    /*
                     * ZwdtUserAuthValid zwdtUserAuthValid = new
                     * ZwdtUserAuthValid(); if
                     * (!zwdtUserAuthValid.userValid(auditOnlineRegister,
                     * biGuid, ZwdtConstant.USERVALID_SPI_INS)) { return
                     * JsonUtils.zwdtRestReturn("0", "UserValid身份验证失败！", ""); }
                     */
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                }
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 2、获取子申报信息
                AuditSpISubapp auditSpISubapp = null;
                List<AuditSpISubapp> auditSpISubapps = iAuditSpISubapp.getSubappByBIGuid(biGuid).getResult();
                log.info("实例化实体1：" + auditSpISubapps);
                if (auditSpISubapps != null && !auditSpISubapps.isEmpty()) {
                    auditSpISubapp = auditSpISubapps.get(0);
                }
                log.info("实例化实体2：" + auditSpISubapp);
                String businessguid = auditSpISubapp.getBusinessguid();
                // 3、获取材料实例信息
                List<AuditSpIMaterial> auditSpIMaterials = new ArrayList<>();
                if (StringUtil.isBlank(subAppGuid)) {
                    subAppGuid = auditSpISubapp.getRowguid();
                }
                // 2.3 如果材料实例没有初始化，则初始化材料
                if (!ZwfwConstant.CONSTANT_STR_ONE.equals(auditSpISubapp.getInitmaterial())) {
                    iHandleSPIMaterial.initSubappMaterial(subAppGuid, businessguid, biGuid, auditSpISubapp.getPhaseguid(), null, null).getResult();
                }
                auditSpIMaterials = iAuditSpIMaterial.getSpIMaterialBySubappGuid(subAppGuid).getResult();
                List<CodeItems> codeitems = iCodeItemsService.listCodeItemsByCodeName("保税区一表填写材料名称");
                // 4、获取事项实例信息
                List<AuditSpITask> auditSpITasks = auditSpITaskService.getSpITaskByBIGuid(biGuid).getResult();
                List<JSONObject> materialJsonList = new ArrayList<JSONObject>();
                // 5、获取材料基本信息返回JSON
                for (AuditSpIMaterial auditSpIMaterial : auditSpIMaterials) {
                    // 5.1、获取材料基本信息
                    JSONObject materialJson = new JSONObject();
                    materialJson.put("projectmaterialguid", auditSpIMaterial.getRowguid()); // 材料标识
                    materialJson.put("projectmaterialname", auditSpIMaterial.getMaterialname()); // 材料名称
                    materialJson.put("clientguid", auditSpIMaterial.getCliengguid());
                    materialJson.put("status", auditSpIMaterial.getStatus()); // 材料状态
                    materialJson.put("shangchuan", "上传"); // 材料状态
                    if ("20".equals(auditSpIMaterial.getStatus())) {
                        materialJson.put("istijiao", true); // 材料状态
                        materialJson.put("shangchuan", "已上传"); // 材料状态
                    }
                    materialJson.put("taskmaterialguid", auditSpIMaterial.getMaterialguid());// 事项材料主键

                    materialJson.put("ordernum", auditSpIMaterial.getOrdernum());
                    materialJson.put("submittype",
                            iCodeItemsService.getItemTextByCodeName("提交方式", auditSpIMaterial.getSubmittype()));
                    String necessity = "0";
                    if (ZwfwConstant.NECESSITY_SET_YES.equals(auditSpIMaterial.getNecessity())) {
                        necessity = "1";
                    }
                    materialJson.put("necessary", necessity); // 是否必须

                    if ("0".equals(necessity)) {
                        materialJson.put("necessityclass", "unnecessary");
                        materialJson.put("necessitytext", "非必要");
                    } else {
                        materialJson.put("necessityclass", "necessary");
                        materialJson.put("necessitytext", "必要");
                    }

                    // materialJson.put("allowrongque",
                    // auditSpIMaterial.getAllowrongque()); // 是否容缺

                    if ("1".equals(auditSpIMaterial.getAllowrongque())) {
                        materialJson.put("allowrongque", "是");
                        materialJson.put("necessityclass", "unnecessary");
                        materialJson.put("necessitytext", "非必要");
                        // materialJson.put("necessaity_class",
                        // "necessary");
                    } else {
                        materialJson.put("allowrongque", "否");
                        // materialJson.put("necessaity_class",
                        // "unnecessary");
                    }

                    // 是否容缺
                    // 5.1.1、判断材料列表上传按钮的显示内容（0:未上传、1：已上传、2：已引用证照库、3: 在线填表、4:已填表）
                    int count = iAttachService.getAttachCountByClientGuid(auditSpIMaterial.getCliengguid());
                    int showButton = 0;

                    AuditTaskMaterial material = null;
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    sqlConditionUtil.eq("materialid", auditSpIMaterial.getMaterialguid());
                    sqlConditionUtil.in("taskguid",
                            "select rowguid from audit_task  where (IS_HISTORY is null or IS_HISTORY ='0')  and IS_EDITAFTERIMPORT='1' and IS_ENABLE='1' ");

                    List<AuditTaskMaterial> taskMaterialList = iAuditTaskMaterial
                            .selectMaterialListByCondition(sqlConditionUtil.getMap()).getResult();
                    if (ValidateUtil.isNotBlankCollection(taskMaterialList)) {
                        material = taskMaterialList.get(0);
                    }
                    if (material != null) {
                        materialJson.put("beizhu", material.getFile_explian());
                    } else {
                        materialJson.put("beizhu", "");
                    }
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
                            String url = request.getRequestURL().substring(0, request.getRequestURL().indexOf("/rest/"))
                                    + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=";
                            // 5.1.2.2、获取材料空白表格标识
                            if (StringUtil.isNotBlank(auditTaskMaterial.getTemplateattachguid())) {
                                List<FrameAttachInfo> frameAttachInfos = iAttachService
                                        .getAttachInfoListByGuid(auditTaskMaterial.getTemplateattachguid());
                                if (!frameAttachInfos.isEmpty()) {
                                    materialJson.put("templateattachguid", frameAttachInfos.get(0).getAttachGuid());
                                    materialJson.put("templateattach", frameAttachInfos.get(0).getAttachGuid());
                                    materialJson.put("templateattachtext", "空白表格");
                                }
                            }
                            // 5.1.2.3、获取材料填报示例标识
                            if (StringUtil.isNotBlank(auditTaskMaterial.getExampleattachguid())) {
                                List<FrameAttachInfo> frameAttachInfos = iAttachService
                                        .getAttachInfoListByGuid(auditTaskMaterial.getExampleattachguid());
                                if (!frameAttachInfos.isEmpty()) {
                                    materialJson.put("exampleattachguid", frameAttachInfos.get(0).getAttachGuid());
                                    materialJson.put("exampleattach", frameAttachInfos.get(0).getAttachGuid());
                                    materialJson.put("exampleattachtext", "示例表格");
                                }
                            }
                            // 5.1.2.4、获取材料来源渠道
                            String materialSource;
                            if (StringUtil.isBlank(auditTaskMaterial.getFile_source())) {
                                materialSource = "申请人自备";
                            } else {
                                materialSource = iCodeItemsService.getItemTextByCodeName("来源渠道",
                                        auditTaskMaterial.getFile_source());
                            }
                            materialJson.put("taskmaterialrowguid", auditTaskMaterial.getRowguid());
                            materialJson.put("materialsource",
                                    StringUtil.isBlank(materialSource) ? "申请人自备" : materialSource);
                            //判断是否可以电子证照获取  by饶少亮
                            if ("1".equals(auditTaskMaterial.getStr("isxtendfield"))) {
                                materialJson.put("materialsource", "已关联电子证照，可免提交");
                                materialJson.put("isxtendfield", 1);
                                materialJson.put("isshuoming", 0);
                            } else {
                                materialJson.put("hidden", "hidden");
                                materialJson.put("isxtendfield", 0);
                                materialJson.put("isshuoming", 1);
                            }
                            // 5.1.2.5、判断按钮显示方式（0:未上传、1：已上传、2：已引用证照库、3:
                            // 在线填表、4:已填表）
                            if (auditTaskMaterial.getType() != null) {
                                if (WorkflowKeyNames9.MaterialType_Form == auditTaskMaterial.getType()) {
                                    // 5.1.2.5.1、如果材料类型为表单，有附件则显示已填表，没有附件则显示在线填表
                                    showButton = count > 0 ? 4 : 3;
                                } else if (WorkflowKeyNames9.MaterialType_Attach == auditTaskMaterial.getType()) {
                                    // 5.1.2.5.2、如果材料类型为附件，有附件则显示已上传，没有附件则显示未上传
                                    showButton = count > 0 ? 1 : 0;
                                }
                            } else {
                                // 材料类型为空，统一显示为已上传或者未上传
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
                        if (auditSpShareMaterial != null) {
                            List<AuditSpShareMaterialRelation> spShareMaterialRList = iAuditSpShareMaterialRelation
                                    .selectByShareMaterialGuid(businessguid,
                                            auditSpShareMaterial.getSharematerialguid())
                                    .getResult();
                            if (ValidateUtil.isNotBlankCollection(spShareMaterialRList)) {
                                sqlConditionUtil.clear();
                                String taskid = spShareMaterialRList.get(0).getTaskid();
                                String taskRowguid = iAuditTask.getUseTaskAndExtByTaskid(taskid).getResult().getRowguid();
                                sqlConditionUtil.eq("materialid", spShareMaterialRList.get(0).getMaterialid());
                                sqlConditionUtil.eq("taskguid", taskRowguid);
                                List<AuditTaskMaterial> shareTaskMaterialList = iAuditTaskMaterial
                                        .selectMaterialListByCondition(sqlConditionUtil.getMap())
                                        .getResult();
                                log.info("共享材料材料信息=============" + shareTaskMaterialList);
                                if (ValidateUtil.isNotBlankCollection(shareTaskMaterialList)) {
                                    material = shareTaskMaterialList.get(0);
                                    materialJson.put("taskmaterialrowguid", material.getRowguid());
                                }
                            }
                            log.info("共享材料获取到的材料信息============" + material);
                        }

                        materialJson.put("materialsource", materialSourceText);
                        if ("通过数据共享获取".equals(materialSourceText)) {
                            //判断是否可以电子证照获取  by饶少亮
                            materialJson.put("materialsource", "已关联电子证照，可免提交");
                            materialJson.put("isxtendfield", 1);
                            materialJson.put("isshuoming", 0);
                        } else {
                            materialJson.put("hidden", "hidden");
                            materialJson.put("isxtendfield", 0);
                            materialJson.put("isshuoming", 1);
                        }

                        // 5.1.3.6、有附件则显示已上传，没有附件则显示未上传
                        showButton = count > 0 ? 1 : 0;
                        // 5.1.3.7、获取材料填报须知
                        if (auditSpShareMaterial != null && StringUtil.isNotBlank(auditSpShareMaterial.getNote())) {
                            // 5.1.3.7、获取材料填报须知
                            materialJson.put("hasfileexplian",
                                    StringUtil.isBlank(auditSpShareMaterial.getNote()) ? 0 : 1);
                            materialJson.put("fileexplian", auditSpShareMaterial.getNote());
                        }
                    }

                    materialJson.put("isshared", auditSpIMaterial.getShared());
                    materialJson.put("showbutton", showButton);
                    materialJsonList.add(materialJson);
                    // 6、对返回的材料列表进行排序
                    Collections.sort(materialJsonList, new Comparator<JSONObject>() {
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

                    int index = 1;
                    for (JSONObject record : materialJsonList) {
                        record.put("index", index++);
                    }
                    // 2020/10/28 edit by ccyu
                    // 判断是否是一表填写的材料
                    if (codeitems != null && !codeitems.isEmpty()) {
                        String ybid = iCodeItemsService.getItemValueByCodeID(codeitems.get(0).getCodeId().toString(),
                                auditSpIMaterial.getMaterialname());
                        if (StringUtil.isNotBlank(ybid)) {
                            // 查询是否是生成的
                            String cliengguid = auditSpIMaterial.getCliengguid();
                            boolean issc = false;
                            List<FrameAttachInfo> attachInfoList = attachService.getAttachInfoListByGuid(cliengguid);
                            if (attachInfoList != null && !attachInfoList.isEmpty()) {
                                for (FrameAttachInfo attachinfo : attachInfoList) {
                                    if (attachinfo.getAttachFileName().contains("一表生成")) {
                                        issc = true;
                                        break;
                                    }
                                }
                            }
                            if (issc) {
                                materialJson.put("shangchuan", "已生成");
                                materialJson.put("issc", 1);
                            }
                            materialJson.put("ybid", ybid);
                        }
                    }
                }
                // 5.1.4、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("materiallist", materialJsonList);
                log.info("=======结束调用getInitBusinessMaterialList接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取初始化材料列表成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getInitBusinessMaterialList接口参数：params【" + params + "】=======");
            log.info("=======getInitBusinessMaterialList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取初始化材料列表失败：" + e.getMessage(), "");
        }
    }

    /**
     * 验证套餐是否可编辑接口
     *
     * @param params 接口的入参
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
                if (auditSpISubappList != null && !auditSpISubappList.isEmpty()) {
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
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======checkEditBusiness接口参数：params【" + params + "】=======");
            log.info("=======checkEditBusiness异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "验证失败", "");
        }
    }

    /**
     * 验证套餐是否可编辑接口
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/ybSaveAttach", method = RequestMethod.POST)
    public String ybSaveAttach(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用ybSaveAttach接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            // 定义返回值
            JSONObject data = new JSONObject();
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String subAppGuid = obj.getString("subappguid");
                JSONObject ybbasic = obj.getJSONObject("ybbasic");
                Record record = new Record();
                record.set("dwmc", ybbasic.getString("dwmc"));
                record.set("fddbr", ybbasic.getString("fddbr"));
                record.set("dz", ybbasic.getString("dz"));
                record.set("yb", ybbasic.getString("yb"));
                record.set("yyzz", ybbasic.getString("yyzz"));
                record.set("ggmj", ybbasic.getString("ggmj"));
                record.set("cz", ybbasic.getString("cz"));
                record.set("chang", ybbasic.getString("chang"));
                record.set("gao", ybbasic.getString("gao"));
                record.set("hou", ybbasic.getString("hou"));
                record.set("fbdd", ybbasic.getString("fbdd"));
                record.set("lb", ybbasic.getString("lb"));
                record.set("fbsj", ybbasic.getString("fbsj"));
                record.set("jymj", ybbasic.getString("jymj"));
                record.set("fddbrdh", ybbasic.getString("fddbrdh"));
                record.set("jyxm", ybbasic.getString("jyxm"));
                record.put("lxr", ybbasic.getString("companyname"));
                record.put("lxdh", ybbasic.getString("contactphone"));
                record.put("jyx", "");
                record.put("ggzzs", "");
                // 渲染广告类别
                if (StringUtil.isNotBlank(record.getStr("lb"))) {
                    if ("1".equals(record.getStr("lb"))) {
                        record.put("jyx", "√");
                    } else {
                        record.put("ggzzs", "√");
                    }
                }
                List<CodeItems> codeitems = iCodeItemsService.listCodeItemsByCodeName("保税区一表填写材料名称");
                List<AuditSpIMaterial> list = iAuditSpIMaterial.getSpIMaterialBySubappGuid(subAppGuid).getResult();
                if (list != null && !list.isEmpty() && codeitems != null && !codeitems.isEmpty()) {
                    String licenseName = ClassPathUtil.getClassesPath() + "license.xml";
                    String deployWarPath = null;
                    for (AuditSpIMaterial auditSpIMaterial : list) {
                        String cliengguid = auditSpIMaterial.getCliengguid();
                        // 判断是否是一表填写的材料
                        String ybid = iCodeItemsService.getItemValueByCodeID(codeitems.get(0).getCodeId().toString(),
                                auditSpIMaterial.getMaterialname());
                        if (StringUtil.isNotBlank(ybid)) {
                            // 1表示广告申请书 2表示卫生许可证
                            if ("1".equals(ybid)) {
                                deployWarPath = ClassPathUtil.getDeployWarPath()
                                        + "zjgzwdt/pages/bsqzwdt/ybword/ggsz.doc";
                            } else if ("2".equals(ybid)) {
                                deployWarPath = ClassPathUtil.getDeployWarPath()
                                        + "zjgzwdt/pages/bsqzwdt/ybword/xzxk.docx";
                            }
                            try {
                                new License().setLicense(licenseName);
                                // 先删除原先附件
                                iAttachService.deleteAttachByGuid(cliengguid);
                                InputStream is = new FileInputStream(deployWarPath);
                                Document document = mergeWord(is, record);
                                saveWord(document, cliengguid, auditSpIMaterial.getMaterialname() + "【一表生成】.docx");
                                // 更新材料状态
                                if ("10".equals(auditSpIMaterial.getStatus())) {
                                    auditSpIMaterial.setStatus("20");
                                    iAuditSpIMaterial.updateSpIMaterial(auditSpIMaterial);
                                } else if ("15".equals(auditSpIMaterial.getStatus())) {
                                    auditSpIMaterial.setStatus("25");
                                    iAuditSpIMaterial.updateSpIMaterial(auditSpIMaterial);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }

                log.info("=======结束调用ybSaveAttach接口=======");
                return JsonUtils.zwdtRestReturn("1", "验证成功", data.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======ybSaveAttach接口参数：params【" + params + "】=======");
            log.info("=======ybSaveAttach异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "验证失败", "");
        }
    }

    /**
     * 合并word模版
     *
     * @throws Exception
     */
    public Document mergeWord(InputStream is, Record record) throws Exception {
        Document doc = null;
        if (is != null) {
            doc = new Document(is);
            doc.getMailMerge().execute(record.getColumnNames(), record.getColumnValues());
        } else {
            throw new Exception("word模版内容为空！");
        }
        return doc;
    }

    /**
     * 将文档保存到附件库
     *
     * @param doc
     * @param cligenGuid
     * @return
     */
    public void saveWord(Document doc, String cligenGuid, String fileName) throws Exception {
        // 保存到附件库
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // 保存成word
        doc.save(outputStream, SaveFormat.DOCX);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        String attachguid = UUID.randomUUID().toString();
        long size = inputStream.available();
        AttachUtil.saveFileInputStream(attachguid, cligenGuid, fileName, "docx", "", size, inputStream,
                UserSession.getInstance().getUserGuid(), UserSession.getInstance().getDisplayName());
    }

    @RequestMapping(value = "/saveBusinessInfoNew", method = RequestMethod.POST)
    public String saveBusinessInfoNew(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用saveBusinessInfoNew接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String data = jsonObject.getString("data");
            AesDemoUtil aes = null;
            try {
                aes = new AesDemoUtil();
                data = aes.decrypt(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject datanew = JSONObject.parseObject(data);
            String token = datanew.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) datanew.get("params");
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
                String isbr = obj.getString("isbr");
                String malladdress = obj.getString("malladdress");

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
                            auditSpIntegratedCompany.set("isbr", isbr);
                            auditSpIntegratedCompany.set("malladdress", malladdress);
                            auditSpIntegratedCompany.setCompanyname(companyName); // 企业名称
                            auditSpIntegratedCompany.setZzjgdmz(zzjgdmz); // 组织结构代码证
                            auditSpIntegratedCompany.setContactperson(contactPerson); // 联系人
                            if (StringUtil.isNotBlank(capital)) {
                                auditSpIntegratedCompany.setCapital(Double.parseDouble(capital)); // 注册资本
                            } else {
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
                            if (auditSpPhases != null && !auditSpPhases.isEmpty()) {
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
                             * subAppGuid =
                             * iAuditSpISubapp.addSubapp(auditSpISubapp).
                             * getResult();
                             */
                            iAuditSpISubapp.addSubapp(auditSpISubapp).getResult();
                            // 4.2.3、生成事项实例信息
                            List<AuditSpTask> auditSpTasks = iAuditSpTask.getAllAuditSpTaskByPhaseguid(phaseGuid)
                                    .getResult();
                            List<AuditSpITask> auditSpITasks = auditSpITaskService.getSpITaskByBIGuid(biGuid)
                                    .getResult();
                            if (auditSpITasks.isEmpty()) {
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
                                            auditSpITaskService.addTaskInstance(businessGuid, biGuid, phaseGuid,
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
                        } else {
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
                            auditSpIntegratedCompany.set("isbr", isbr);
                            auditSpIntegratedCompany.set("malladdress", malladdress);
                            if (StringUtil.isNotBlank(capital)) {
                                auditSpIntegratedCompany.setCapital(Double.parseDouble(capital)); // 注册资本
                            } else {
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
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "主题申报基本信息保存异常：" + e.getMessage(), "");
        }
    }

    /**
     * 保存套餐基本信息接口(套餐申报页面点击保存按钮时调用)
     *
     * @param params  请求参数
     * @param request HTTP请求
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
                String isbr = obj.getString("isbr");
                String malladdress = obj.getString("malladdress");

                String optionfromid = obj.getString("optionfromid");

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
                            auditSpIntegratedCompany.set("isbr", isbr);
                            auditSpIntegratedCompany.set("malladdress", malladdress);
                            auditSpIntegratedCompany.setCompanyname(companyName); // 企业名称
                            auditSpIntegratedCompany.setZzjgdmz(zzjgdmz); // 组织结构代码证
                            auditSpIntegratedCompany.setContactperson(contactPerson); // 联系人
                            if (StringUtil.isNotBlank(capital)) {
                                auditSpIntegratedCompany.setCapital(Double.parseDouble(capital)); // 注册资本
                            } else {
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
                            if (auditSpPhases != null && !auditSpPhases.isEmpty()) {
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
                            auditSpISubapp.setInitmaterial("0");
                            auditSpISubapp.set("optionfromid", optionfromid);
                            /*
                             * subAppGuid =
                             * iAuditSpISubapp.addSubapp(auditSpISubapp).
                             * getResult();
                             */
                            iAuditSpISubapp.addSubapp(auditSpISubapp).getResult();
                            // 4.2.3、生成事项实例信息
                            List<AuditSpTask> auditSpTasks = iAuditSpTask.getAllAuditSpTaskByPhaseguid(phaseGuid)
                                    .getResult();
                            List<AuditSpITask> auditSpITasks = auditSpITaskService.getSpITaskByBIGuid(biGuid)
                                    .getResult();
                            if (auditSpITasks.isEmpty()) {
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
                                            auditSpITaskService.addTaskInstance(businessGuid, biGuid, phaseGuid,
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
                        } else {
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
                            auditSpIntegratedCompany.set("isbr", isbr);
                            auditSpIntegratedCompany.set("malladdress", malladdress);
                            if (StringUtil.isNotBlank(capital)) {
                                auditSpIntegratedCompany.setCapital(Double.parseDouble(capital)); // 注册资本
                            } else {
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
                                List<AuditSpISubapp> auditSpISubapps = iAuditSpISubapp
                                        .getSubappByBIGuid(auditSpInstance.getRowguid()).getResult();
                                if (auditSpISubapps != null && !auditSpISubapps.isEmpty()) {
                                    AuditSpISubapp auditSpISubapp = auditSpISubapps.get(0);
                                    auditSpISubapp.setSubappname(companyName);
                                    auditSpISubapp.setApplyername(companyName);
                                    iAuditSpISubapp.updateAuditSpISubapp(auditSpISubapp);
                                }
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
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "主题申报基本信息保存异常：" + e.getMessage(), "");
        }
    }

    /**
     * 提交套餐办件接口（套餐申报提交调用）
     *
     * @param params  请求参数
     * @param request HTTP请求
     * @return
     */
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
                    // ZwdtUserAuthValid zwdtUserAuthValid = new
                    // ZwdtUserAuthValid();
                    // if (!zwdtUserAuthValid.userValid(auditOnlineRegister,
                    // biGuid, ZwdtConstant.USERVALID_SPI_INS)) {
                    // return JsonUtils.zwdtRestReturn("0", "UserValid身份验证失败！",
                    // "");
                    // }
                    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    if (StringUtil.isBlank(subAppGuid)) {
                        List<AuditSpISubapp> auditSpISubapps = iAuditSpISubapp.getSubappByBIGuid(biGuid).getResult();
                        if (auditSpISubapps == null || auditSpISubapps.isEmpty()) {
                            return JsonUtils.zwdtRestReturn("0", "未获取到子申报实例信息！", "");
                        }
                        AuditSpISubapp auditSpISubapp = auditSpISubapps.get(0);
                        subAppGuid = auditSpISubapp.getRowguid();
                        phaseGuid = auditSpISubapp.getPhaseguid();
                    }
                    // 4、根据子申报标识获取材料实例信息
                    List<AuditSpIMaterial> auditSpIMaterials = iAuditSpIMaterial.getSpIMaterialBySubappGuid(subAppGuid)
                            .getResult();
                    // 4.1、若子申报对应的材料实例信息没有，则需要初始化
                    if (auditSpIMaterials == null || auditSpIMaterials.isEmpty()) {
                        iHandleSPIMaterial.initIntegratedMaterial(subAppGuid, businessGuid, biGuid, phaseGuid, "",
                                auditOnlineRegister.getIdnumber(), areacCode);
                    }

                    AuditSpBusiness auditSpBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessGuid)
                            .getResult();

                    // 5、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("biguid", biGuid);
                    log.info("=======结束调用submitBusinessInfo接口=======");
                    return JsonUtils.zwdtRestReturn("1", "保存成功！", dataJson.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "主题申报基本信息提交异常：", "");
        }
    }

    /**
     * 检查必要材料是否都上传（套餐办件申报提交时调用）
     *
     * @param params 接口的入参
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
                            if (ZwfwConstant.NECESSITY_SET_YES.equals(auditSpIMaterial.getNecessity())
                                    && !ZwdtConstant.STRING_YES.equals(isRongque)) {
                                int status = Integer.parseInt(materialStatus); // 套餐办件材料表当前材料提交的状态
                                // 必要材料如果未提交，则数量+1
                                if (status != ZwfwConstant.PROJECTMATERIAL_STATUS_ELECTRONIC
                                        && status != ZwfwConstant.PROJECTMATERIAL_STATUS_PAPER_AND_ELECTRONIC) {
                                    noSubmitNum++;
                                } else {
                                    // 再判断是否存在附件
                                    int count = iAttachService
                                            .getAttachCountByClientGuid(auditSpIMaterial.getCliengguid());
                                    if (count <= 0) {
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
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======checkAllMaterialIsSubmit接口参数：params【" + params + "】=======");
            log.info("=======checkAllMaterialIsSubmit异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "检查材料是否都提交失败：" + e.getMessage(), "");
        }
    }

    /**
     * 提交套餐办件接口
     *
     * @param params 接口的入参
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
                AuditOnlineIndividual auditOnlineIndividual = new AuditOnlineIndividual();
                // if (auditOnlineRegister != null) {
                // ZwdtUserAuthValid zwdtUserAuthValid = new
                // ZwdtUserAuthValid();
                // if (!zwdtUserAuthValid.userValid(auditOnlineRegister, biGuid,
                // ZwdtConstant.USERVALID_SPI_INS)) {
                // return JsonUtils.zwdtRestReturn("0", "UserValid身份验证失败！", "");
                // }
                // auditOnlineIndividual = iAuditOnlineIndividual
                // .getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
                // }
                // else {
                // return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                // }
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                long startTime = System.currentTimeMillis();
                // 2、获取子申报标识
                String subAppGuid = obj.getString("subappguid");
                // 3、更新AUDIT_ONLINE_PROJECT状态为外网申报已提交
                Map<String, String> updateFieldMap = new HashMap<String, String>(16);
                Map<String, String> updateDateFieldMap = new HashMap<String, String>(16);// 要更新的时间类型字段
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                String flowsn = iHandleFlowSn.getFlowsn("套餐编号", "TC").getResult();
                long endTime = System.currentTimeMillis();
                log.info("推送一件事运行时间为：" + (endTime - startTime) / 1000 + "秒");

                updateFieldMap.put("flowsn=", flowsn);
                updateFieldMap.put("status=", String.valueOf(ZwfwConstant.BANJIAN_STATUS_WWYTJ));// 外网申报已提交
                updateDateFieldMap.put("applydate=",
                        EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
                sqlConditionUtil.eq("sourceguid", biGuid);
                iAuditOnlineProject.updateOnlineProject(updateFieldMap, updateDateFieldMap, sqlConditionUtil.getMap());
                long endTime1 = System.currentTimeMillis();
                log.info("推送一件事运行时间为1：" + (endTime1 - startTime) / 1000 + "秒");

                // 4、更新子申报状态为待预审
                iAuditSpISubapp.updateSubapp(subAppGuid, ZwfwConstant.LHSP_Status_DYS, null);
                AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(biGuid).getResult();
                AuditSpBusiness auditSpBusiness = iAuditSpBusiness
                        .getAuditSpBusinessByRowguid(auditSpInstance.getBusinessguid()).getResult();
                long endTime2 = System.currentTimeMillis();
                log.info("推送一件事运行时间为2：" + (endTime2 - startTime) / 1000 + "秒");
                // 5、插入各事项的办件的操作记录
                // 5.1、获取已初始化的事项实例，综管初始化事项实例的方法中已添加了初始化projectguid
                List<AuditSpITask> auditSpITasks = auditSpITaskService.getSpITaskByBIGuid(biGuid).getResult();
                long endTime3 = System.currentTimeMillis();
                log.info("推送一件事运行时间为3：" + (endTime3 - startTime) / 1000 + "秒");
                /*
                 * if (auditSpITasks != null && !auditSpITasks.isEmpty()) { for
                 * (AuditSpITask auditSpITask : auditSpITasks) { if
                 * (StringUtil.isNotBlank(auditSpITask.getProjectguid())) { //
                 * 5.2、判断是否已经插入 AuditProjectOperation auditProjectOperation =
                 * iAuditProjectOperation
                 * .getAuditOperation(auditSpITask.getProjectguid(),
                 * String.valueOf(ZwfwConstant.BANJIAN_STATUS_WWYTJ))
                 * .getResult(); if (auditProjectOperation == null ||
                 * auditProjectOperation.isEmpty()) { auditProjectOperation =
                 * new AuditProjectOperation();
                 * auditProjectOperation.setRowguid(UUID.randomUUID().toString()
                 * );
                 * auditProjectOperation.setAreaCode(auditSpITask.getAreacode())
                 * ; auditProjectOperation.setApplyerName(auditOnlineIndividual.
                 * getClientname());
                 * auditProjectOperation.setTaskGuid(auditSpITask.getTaskguid())
                 * ; auditProjectOperation.setOperatedate(new Date());
                 * auditProjectOperation.setOperateusername(
                 * auditOnlineIndividual.getClientname());
                 * auditProjectOperation.setOperateUserGuid(
                 * auditOnlineIndividual.getApplyerguid());
                 * auditProjectOperation.setOperateType(String.valueOf(
                 * ZwfwConstant.BANJIAN_STATUS_WWYTJ));
                 * auditProjectOperation.setProjectGuid(auditSpITask.
                 * getProjectguid());
                 * iAuditProjectOperation.addProjectOperation(
                 * auditProjectOperation); } } } }
                 */
                long endTime4 = System.currentTimeMillis();
                log.info("推送一件事运行时间为：" + (endTime4 - startTime) / 1000 + "秒");
                // 向指定用户发送待办
                String handleUrl = "/" + auditSpBusiness.getHandleURL() + "/handleintegratedinquirydetail?biGuid="
                        + biGuid + "&subappGuid=" + subAppGuid;
                String title = "【一件事预审】" + auditSpInstance.getItemname() + "(" + auditSpInstance.getApplyername() + ")";
                iAuditOnlineMessages.sendMsg("统一收件人员", title, auditSpInstance.getApplyerguid(),
                        auditSpInstance.getApplyername(), auditSpInstance.getAreacode(), handleUrl, subAppGuid,
                        "zwfwMsgurl", null);
                log.info("=======结束调用submitMaterial接口=======");
                return JsonUtils.zwdtRestReturn("1", "保存成功！", "");
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "主题申报基本信息提交异常：" + e.getMessage(), "");
        }
    }

    /**
     * 获取套餐办件中所有纸质材料接口（申报告知页面调用）
     *
     * @param params 接口的入参
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

                String subappguid = obj.getString("subappguid");

                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理开始<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 1.2、获取用户注册信息
                // AuditOnlineRegister auditOnlineRegister =
                // this.getOnlineRegister(request);
                // if (auditOnlineRegister != null) {
                // ZwdtUserAuthValid zwdtUserAuthValid = new
                // ZwdtUserAuthValid();
                // if (!zwdtUserAuthValid.userValid(auditOnlineRegister, biGuid,
                // ZwdtConstant.USERVALID_SPI_INS)) {
                // return JsonUtils.zwdtRestReturn("0", "UserValid身份验证失败！", "");
                // }
                // }
                // else {
                // return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                // }
                // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>安全性问题处理结束<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                // 2、获取子申报信息
                AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(subappguid).getResult();
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
                        materialJson.put("allowrongque", auditSpIMaterial.getAllowrongque());// 是否容缺
                        if ("0".equals(auditSpIMaterial.getShared())) {
                            // 4.1.1、如果材料是非共享材料
                            List<AuditSpITask> auditSpITasks = auditSpITaskService
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
                                } else {
                                    materialsource = iCodeItemsService.getItemTextByCodeName("来源渠道",
                                            auditTaskMaterial.getFile_source());
                                }
                                materialJson.put("materialsource", materialsource);
                                materialJson.put("submittype", iCodeItemsService.getItemTextByCodeName("提交方式",
                                        String.valueOf(auditTaskMaterial.getSubmittype())));
                                materialJson.put("ordernum", auditTaskMaterial.getOrdernum());
                            }
                        } else {
                            // 4.1.2、如果材料是共享材料
                            // 设置共享材料来源
                            AuditSpShareMaterial auditSpShareMaterial = iAuditSpShareMaterial
                                    .getAuditSpShareMaterialByShareMaterialGuid(auditSpIMaterial.getMaterialguid(),
                                            auditSpIMaterial.getBusinessguid())
                                    .getResult();
                            String spMaterialSource = "";
                            if (StringUtil.isBlank(auditSpShareMaterial.getFile_source())) {
                                spMaterialSource = "申请人自备";
                            } else {
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
                // Collections.sort(materialJsonList, new
                // Comparator<JSONObject>()
                // {
                // @Override
                // public int compare(JSONObject o1, JSONObject o2) {
                // Integer necessary1 =
                // Integer.parseInt(o1.get("necessary").toString());
                // Integer necessary2 =
                // Integer.parseInt(o2.get("necessary").toString());
                // int comNecessity = necessary2.compareTo(necessary1);
                // int ret = comNecessity;
                // if (comNecessity == 0) {
                // Integer ordernum1 = o1.get("ordernum") == null ? 0
                // : Integer.parseInt(o1.get("ordernum").toString());
                // Integer ordernum2 = o2.get("ordernum") == null ? 0
                // : Integer.parseInt(o2.get("ordernum").toString());
                // ret = ordernum2.compareTo(ordernum1);
                // }
                // return ret;
                // }
                // });

                int index = 1;
                for (JSONObject record : materialJsonList) {
                    record.put("index", index++);
                    if ("0".equals(record.getString("necessary"))) {
                        record.put("necessityclass", "unnecessary");
                        record.put("necessitytext", "非必要");
                    } else {
                        record.put("necessityclass", "necessary");
                        record.put("necessitytext", "必要");
                    }
                }

                // 6、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("materiallist", materialJsonList);
                log.info("=======结束调用getPaperMaterial接口=======");
                return JsonUtils.zwdtRestReturn("1", "套餐中纸质相关信息成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getPaperMaterial接口参数：params【" + params + "】=======");
            log.info("=======getPaperMaterial异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "套餐中纸质相关信息失败：" + e.getMessage(), "");
        }
    }


    /**
     * 根据businessguid与系统参数fszlyjs_businessguid比较，若一致则isfszlyjs=1否则为0
     *
     * @param params
     * @param request
     * @return
     */
    @RequestMapping(value = "/isfszl")
    public String isfszl(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用isfszl接口=======");
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject resultData = new JSONObject();
                JSONObject data = jsonObject.getJSONObject("params");
                String businessguid = data.getString("businessguid");
                if (StringUtil.isBlank(businessguid)) {
                    return JsonUtils.zwdtRestReturn("0", "缺少参数businessguid", "");
                }
                String fszlyjs_businessguid = ConfigUtil.getFrameConfigValue("fszlyjs_businessguid");
                resultData.put("isfszlyjs", businessguid.equals(fszlyjs_businessguid) ? 1 : 0);

                return JsonUtils.zwdtRestReturn("1", " 判断是否是放射治疗成功！", resultData.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "token验证失败", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======isfszl异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "判断是否是放射治疗失败：" + e.getMessage(), "");
        } finally {
            log.info("=======结束调用isfszl接口=======");
        }
    }


    @RequestMapping(value = "/eformToWordOrPdf")
    public String eformToWordOrPdf(@RequestBody String params, @Context HttpServletRequest request) {
        String eformUrl = ConfigUtil.getFrameConfigValue("eformIntranetUrl");
        String epointsformurlwt = ConfigUtil.getFrameConfigValue("epointsformurlwt");
        try {
            JSONObject backData = new JSONObject();
            log.info("调用表单/rest/epointsform/eformToWordOrPdf接口入参：" + params);
            String post = HttpUtil.post(eformUrl + "/rest/epointsform/eformToWordOrPdf", params);
            log.info("表单返回值:" + post);
            if (StringUtil.isNotBlank(post)) {
                JSONObject json = JSONObject.parseObject(post);
                JSONObject custom = json.getJSONObject("custom");
                if (custom != null) {
                    String attachUrl = custom.getString("attachUrl");
                    if (StringUtil.isNotBlank(attachUrl)) {
                        String replace = attachUrl.replace(eformUrl + "/", epointsformurlwt);
                        backData.put("attachUrl", replace);
                    }
                }
            }
            return JsonUtils.zwdtRestReturn("1", " 成功！！", backData.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "系统出错：" + e.getMessage(), "");
        }
    }


    @RequestMapping(value = "/getPageData")
    public String getPageData(@RequestBody String params, @Context HttpServletRequest request) {
        String eformUrl = ConfigUtil.getFrameConfigValue("eformIntranetUrl");
        if(StringUtil.isBlank(eformUrl)){
            eformUrl = configServicce.getFrameConfigValue("epointsformurlwt");
        }
        try {
            JSONObject backData = new JSONObject();
            log.info("调用表单sform/getPageData接口入参：" + params);
            JSONObject par = JSONObject.parseObject(params).getJSONObject("params");
            HashMap<String, Object> map = new HashMap<>();
            map.put("Content-Type", "application/x-www-form-urlencoded");
            map.put("params", par.toJSONString());
            String post = HttpUtil.post(eformUrl + "/rest/sform/getPageData", map);
            log.info("表单返回值:" + post);
            if (StringUtil.isNotBlank(post)) {
                JSONObject json = JSONObject.parseObject(post);
                JSONObject custom = json.getJSONObject("custom");
                if (custom != null) {
                    JSONObject recordData = custom.getJSONObject("recordData");
                    if (recordData != null && !recordData.isEmpty()) {
                        JSONArray mainList = recordData.getJSONArray("mainList");
                        if (mainList != null && !mainList.isEmpty()) {
                            backData.put("recordData", recordData);
                        }
                    }
                }
            }
            return JsonUtils.zwdtRestReturn("1", " 成功！！", backData.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("0", "系统出错：" + e.getMessage(), "");
        }
    }


    public List<String> getDefaultAndShareOptionguid(String businessguid) {
        List<String> defaultsel = new ArrayList<>();
        List<AuditSpShareoption> auditspshareoptionlist = iauditspshareoption.getlistBybusinessguid(businessguid)
                .getResult();
        if (!auditspshareoptionlist.isEmpty()) {
            defaultsel = auditspshareoptionlist.stream()
                    .filter(a -> a.getSelecttype().equals(ZwfwConstant.CONSTANT_STR_ONE))
                    .map(AuditSpShareoption::getOptionguid).collect(Collectors.toList());
        }
        return defaultsel;
    }

    private String getJsonToSelect(String str) {
        JSONObject elementsjosn = new JSONObject();
        JSONArray newarray = JSONArray.parseArray(str);
        JSONArray answerarry = new JSONArray();
        JSONArray selectarry = new JSONArray();
        if (newarray != null && !newarray.isEmpty()) {
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
                } else {
                    JSONObject selectobjParent = new JSONObject();
                    selectobjParent.put("optionguid", jsonobj.get("optionguid"));
                    selectobjchildarray.add(selectobjParent);
                }
                if (StringUtil.isNotBlank(jsonobj.get("children"))) {
                    // 如果包含子项
                    JSONArray jsonChildArray = JSONArray.parseArray(jsonobj.get("children").toString());
                    if (jsonChildArray != null && !jsonChildArray.isEmpty()) {
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
                            } else {
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
                                    if (childobjjsonArray != null && !childobjjsonArray.isEmpty()) {
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
                                            } else {
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
        } else {
            // PC端
            String accountGuid = ZwdtUserSession.getInstance("").getAccountGuid();
            if (StringUtil.isNotBlank(accountGuid)) {
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByAccountguid(accountGuid).getResult();
            } else {
                // 通过登录名获取用户
                auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(Authenticator.getCurrentIdentity())
                        .getResult();
            }
        }
        return auditOnlineRegister;
    }

    /**
     * 获取我的企业（前提：一个账号只对应最多一家企业）
     *
     * @param auditOnlineRegister
     * @return
     */
    private AuditRsCompanyBaseinfo getMyCompany(AuditOnlineRegister auditOnlineRegister) {
        if (ZwfwConstant.APPLAYERTYPE_QY.equals(auditOnlineRegister.getUsertype())) {
            // 1、法定代表人
            if (StringUtil.isNotBlank(auditOnlineRegister.getCompanyidnumber())) {
                String fieldName = "creditcode";
                if (auditOnlineRegister.getCompanyidnumber().length() != 18) {
                    fieldName = "organcode";
                }
                return iAuditRsCompanyBaseinfo.getCompanyByOneField(fieldName, auditOnlineRegister.getCompanyidnumber())
                        .getResult();
            }
            // 2、被授权用户
            com.epoint.common.util.SqlConditionUtil sqlConditionUtil = new com.epoint.common.util.SqlConditionUtil();
            sqlConditionUtil.eq("bsquserguid", auditOnlineRegister.getAccountguid());
            sqlConditionUtil.isBlankOrValue("isrelieve", "0"); // 是否解除授权：0-否，1-是
            // sqlConditionUtil.eq("m_isactive", "1"); // 企业空间已激活
            List<AuditOnlineCompanyGrant> companyGrantList = iAuditOnlineCompanyGrant
                    .selectCompanyGrantByConditionMap(sqlConditionUtil.getMap()).getResult();
            if (companyGrantList != null && !companyGrantList.isEmpty()) {
                for (AuditOnlineCompanyGrant companyGrant : companyGrantList) {
                    AuditRsCompanyBaseinfo company = iAuditRsCompanyBaseinfo
                            .getCompanyByOneField("companyid", companyGrant.getCompanyid()).getResult();
                    if (company != null) {
                        return company;
                    }
                }
            }
        }
        return null;
    }

}
