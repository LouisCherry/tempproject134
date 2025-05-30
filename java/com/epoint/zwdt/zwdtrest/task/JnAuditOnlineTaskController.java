package com.epoint.zwdt.zwdtrest.task;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.authenticator.identity.Authenticator;
import com.epoint.authenticator.module.OAuthCheckTokenInfo;
import com.epoint.authenticator.utils.CheckTokenUtil;
import com.epoint.basic.auditonlineuser.auditcollection.domain.AuditOnlineCollection;
import com.epoint.basic.auditonlineuser.auditcollection.inter.IAuditOnlineCollection;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.auditqueue.auditqueue.inter.IAuditQueue;
import com.epoint.basic.auditqueue.auditqueuetasktype.domain.AuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktype.inter.IAuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktypetask.inter.IAuditQueueTasktypeTask;
import com.epoint.basic.auditqueue.auditqueueyuyuetime.domain.AuditQueueYuyuetime;
import com.epoint.basic.auditqueue.auditqueueyuyuetime.inter.IAuditQueueYuyueTime;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.delegate.domain.AuditTaskDelegate;
import com.epoint.basic.audittask.delegate.inter.IAuditTaskDelegate;
import com.epoint.basic.audittask.dict.domain.AuditTaskDict;
import com.epoint.basic.audittask.dict.inter.IAuditTaskDict;
import com.epoint.basic.audittask.element.domain.AuditTaskElement;
import com.epoint.basic.audittask.element.inter.IAuditTaskElementService;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.faq.domain.AuditTaskFaq;
import com.epoint.basic.audittask.faq.inter.IAuditTaskFaq;
import com.epoint.basic.audittask.fee.domain.AuditTaskChargeItem;
import com.epoint.basic.audittask.fee.inter.IAuditTaskChargeItem;
import com.epoint.basic.audittask.hottask.domain.AuditTaskHottask;
import com.epoint.basic.audittask.hottask.inter.IAuditTaskHottask;
import com.epoint.basic.audittask.material.domain.AuditTaskCase;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterialCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterialCase;
import com.epoint.basic.audittask.option.domain.AuditTaskOption;
import com.epoint.basic.audittask.option.inter.IAuditTaskOptionService;
import com.epoint.basic.audittask.result.domain.AuditTaskResult;
import com.epoint.basic.audittask.result.inter.IAuditTaskResult;
import com.epoint.common.util.*;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditorga.handleframeou.IHandleFrameOU;
import com.epoint.composite.auditqueue.handlequeue.inter.IHandleQueue;
import com.epoint.composite.auditresource.handletask.inter.IHandleTask;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.convert.ChineseToPinyin;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.web.WebUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.search.inteligentsearch.sdk.domain.SearchCondition;
import com.epoint.search.inteligentsearch.sdk.domain.SearchUnionCondition;
import com.epoint.xmz.jntaskrelation.api.IJnTaskRelationService;
import com.epoint.xmz.jntaskrelation.api.entity.JnTaskRelation;
import com.epoint.xmz.jnvisitrecord.api.IJnVisitRecordService;
import com.epoint.xmz.jnvisitrecord.api.entity.JnVisitRecord;
import com.epoint.xmz.xmztaskguideconfig.api.IXmzTaskguideConfigService;
import com.epoint.xmz.xmztaskguideconfig.api.entity.XmzTaskguideConfig;
import com.epoint.zwdt.zwdtrest.service.ZwdtService;
import com.epoint.zwdt.zwdtrest.space.api.ISpaceAcceptService;
import com.epoint.zwdt.zwdtrest.task.api.IJnAppRestService;
import com.epoint.zwdt.zwdtrest.task.audittaskpopinfo.api.IAuditTaskPopInfoService;
import com.epoint.zwdt.zwdtrest.task.audittaskpopinfo.api.entity.AuditTaskPopInfo;
import com.epoint.zwdt.zwdtrest.task.keyword.api.IAuditTaskKeywordService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.io.ByteArrayOutputStream;
import java.lang.invoke.MethodHandles;
import java.net.URLDecoder;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 事项相关接口
 *
 * @version [F9.3, 2017年11月9日]
 * @作者 WST
 */
@RestController
@RequestMapping("/jnzwdtTask")
public class JnAuditOnlineTaskController {

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    /**
     * 部门对应API
     */
    @Autowired
    private IHandleFrameOU iHandleFrameOU;
    /**
     * 中心系统参数API
     */
    @Autowired
    private IHandleConfig iHandleConfig;

    @Autowired
    private ICodeItemsService iCodeItemsService;
    /**
     * 事项下放相关信息
     */
    @Autowired
    private IAuditTaskDelegate iAuditTaskDelegate;

    /**
     * 事项要素相关
     */
    @Autowired
    private IAuditTaskElementService iAuditTaskElementService;
    /**
     * /** 事项队列分类相关api
     */
    @Autowired
    IAuditQueueTasktypeTask iAuditQueueTasktypeTask;
    /**
     * 事项队列相关api
     */
    @Autowired
    IAuditQueueTasktype iAuditQueueTasktype;
    /**
     * 附件API
     */
    @Autowired
    private IAttachService iAttachService;
    /**
     * 区域API
     */
    @Autowired
    private IAuditOrgaArea iAuditOrgaArea;

    @Autowired
    private IJnVisitRecordService iJnVisitRecordService;

    /**
     * 事项基本信息API
     */
    @Autowired
    private IAuditTask iAuditTask;
    @Autowired
    private IAuditTaskPopInfoService iAuditTaskPopInfoService;
    /**
     * 热门事项API
     */
    @Autowired
    private IAuditTaskHottask iAuditTaskHottask;
    /**
     * 队列相关api
     */
    @Autowired
    IAuditQueue iAuditQueue;
    /**
     * 事项扩展信息API
     */
    @Autowired
    private IAuditTaskExtension iAuditTaskExtension;
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
     * 办理队列相关api
     */
    @Autowired
    IHandleQueue iHandleQueue;
    @Autowired
    IJnTaskRelationService iJnTaskRelationService;
    /**
     * 事项常见问题API
     */
    @Autowired
    private IAuditTaskFaq iAuditTaskFaq;
    /**
     * 事项分类定义API
     */
    @Autowired
    private IAuditTaskDict iAuditTaskDict;
    /**
     * 事项情形API
     */
    @Autowired
    private IAuditTaskCase iAuditTaskCase;
    /**
     * 材料多情形API
     */
    @Autowired
    private IAuditTaskMaterialCase iAuditTaskMaterialCase;
    /**
     * 事项组合服务API
     */
    @Autowired
    private IHandleTask iHandleTask;
    /**
     * 事项办理结果API
     */
    @Autowired
    private IAuditTaskResult iAuditTaskResultService;

    /**
     * 事项要素选项相关
     */
    @Autowired
    private IAuditTaskOptionService iAuditTaskOptionService;

    /**
     * 套餐主题API
     */
    @Autowired
    private IAuditSpBusiness iAuditSpBusiness;
    /**
     * 政务大厅注册用户API
     */
    @Autowired
    private IAuditOnlineRegister iAuditOnlineRegister;
    /**
     * 政务大厅收藏API
     */
    @Autowired
    private IAuditOnlineCollection iAuditOnlineCollection;
    /**
     * 中心API
     */
    @Autowired
    private IAuditOrgaServiceCenter iAuditOrgaServiceCenter;
    /**
     * 窗口API
     */
    @Autowired
    private IAuditOrgaWindow iAuditOrgaWindow;
    /**
     * 济宁个性化接口
     */
    @Autowired
    private IJnAppRestService iJnAppRestService;
    /**
     * 预约时间API
     */
    @Autowired
    private IAuditQueueYuyueTime iAuditQueueYuyueTime;

    /**
     * 部门API
     */
    @Autowired
    private IOuService ouService;

    @Autowired
    private ISpaceAcceptService iSpaceAcceptService;

    @Autowired
    private IAuditTaskKeywordService iAuditTaskKeywordService;

    @Autowired
    private IConfigService iConfigService;

    @Autowired
    private IXmzTaskguideConfigService iXmzTaskguideConfigService;

    private ZwdtService zwdtService = new ZwdtService();

    /**
     * 事项基本信息的接口
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getTaskBasicInfo", method = RequestMethod.POST)
    public String getTaskBasicInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getTaskBasicInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取事项标识
                String taskGuid = obj.getString("taskguid");
                // taskGuid = "6c253cd4-2552-4f6f-a36a-1bfbd8eef8c4";
                log.info("=======getTaskBasicInfo接口-taskguid：" + taskGuid + "=======");
                // 1.2、获取事项情形标识
                String taskCaseGuid = obj.getString("taskcaseguid");
                // 1.3、获取是否需要事项全部信息 0：基本信息 1：所有信息
                String isNeedAll = obj.getString("isneedall");
                // 1.4、获取用户信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                // 1.5、获取事项信息
                AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, true).getResult();
                String znyd_task = ConfigUtil.getFrameConfigValue("znyd_task");
                if (auditTask != null) {

                    // 2、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    // 3、获取事项基本信息
                    dataJson.put("taskguid", taskGuid);// 事项标识
                    dataJson.put("taskid", auditTask.getTask_id());// 事项业务唯一标识
                    dataJson.put("taskname", auditTask.getTaskname());// 事项名称
                    dataJson.put("itemid", auditTask.getItem_id());// 权利编码
                    if (StringUtil.isNotBlank(znyd_task) && znyd_task.contains(auditTask.getItem_id())) {
                        dataJson.put("znyd", ZwfwConstant.CONSTANT_STR_ONE);
                    }
                    if(auditTask.getIs_history() == 1 || auditTask.getIs_enable() == 0) {
                        dataJson.put("isexpire", "1");
                    }
                    
                    JSONObject taskElementJson = new JSONObject();

                    AuditTaskExtension auditTaskExtension = iAuditTaskExtension.getTaskExtensionByTaskGuid(taskGuid, true).getResult();

                    List<AuditOrgaWindow> auditOrgaWindows = iAuditOrgaWindow.getWindowListByTaskId(auditTask.getTask_id()).getResult();

                    // xz 依申请
                    String businesstype = auditTask.getStr("businesstype");
                    Integer webApplyType = auditTaskExtension.getWebapplytype();
                    String onlineHandle = "0";
                    if (StringUtil.isNotBlank(businesstype)) {
                        if ("1".equals(businesstype)) {
                            onlineHandle = "1";
                        } else {
                            // 是否配置窗口 和 是否网办
                            if (auditTaskExtension != null && auditOrgaWindows != null && !auditOrgaWindows.isEmpty()) {
                                if (webApplyType != null) {
                                    onlineHandle = webApplyType == 0 ? "0" : "1";
                                }
                            }
                        }
                    } else {
                        // 是否配置窗口 和 是否网办
                        if (auditTaskExtension != null && auditOrgaWindows != null && !auditOrgaWindows.isEmpty()) {
                            if (webApplyType != null) {
                                onlineHandle = webApplyType == 0 ? "0" : "1";
                            }
                        }
                    }
                    taskElementJson.put("onlinehandle", Integer.parseInt(onlineHandle));

                    dataJson.put("taskelement", taskElementJson);
                    dataJson.put("qrcontent", WebUtil.getRequestCompleteUrl(request)
                            + "/epointzwmhwz/pages/mobile/html/apptaskdetail?taskguid=" + taskGuid);// 二维码内容
                    if (auditOnlineRegister != null) {
                        if (isCollect(auditTask.getTask_id(), auditOnlineRegister.getAccountguid())) {
                            dataJson.put("iscollect", "1");// 返回改事项已收藏标识
                        }
                    }
                    // 3.3、获取材料多情形
                    List<AuditTaskCase> auditTaskCases = iAuditTaskCase.selectTaskCaseByTaskGuid(taskGuid).getResult();
                    List<JSONObject> caseConditionList = new ArrayList<JSONObject>();
                    if (auditTaskCases != null && !auditTaskCases.isEmpty()) {
                        for (AuditTaskCase auditTaskCase : auditTaskCases) {
                            if (StringUtil.isNotBlank(auditTaskCase.getCasename())) {
                                JSONObject caseConditionJson = new JSONObject();
                                caseConditionJson.put("taskcaseguid", auditTaskCase.getRowguid());// 多情形标识
                                caseConditionJson.put("casename", auditTaskCase.getCasename());// 多情形名称
                                caseConditionList.add(caseConditionJson);
                            }
                        }
                    }
                    dataJson.put("taskcasecount", auditTaskCases.size());// 多情形列表数据
                    dataJson.put("casecondition", caseConditionList);
                    // 3.4、判断是否需要事项的所有信息（事项扩展信息、事项材料、事项收费信息、事项常见问题、事项受理条件）
                    if ("1".equals(isNeedAll)) {
                        JSONObject taskBasicJson = new JSONObject();
                        XmzTaskguideConfig xmzTaskguideConfig = iXmzTaskguideConfigService.getXmzTaskguidByTaskId(auditTask.getRowguid());
                        if (xmzTaskguideConfig != null && StringUtil.isNotBlank(xmzTaskguideConfig.getStr("guidecliengguid"))) {
                            String ewmurl = WebUtil.getRequestCompleteUrl(request) + "/epointzwmhwz/pages/mobile/html/filelist?guidecliengguid=" + xmzTaskguideConfig.getStr("guidecliengguid");
                            //生成二维码字节流
                            QRCodeWriter writer = new QRCodeWriter();
                            ewmurl = new String(ewmurl.getBytes("UTF-8"), "ISO-8859-1");//如果不想更改源码，则将字符串转换成ISO-8859-1编码
                            BitMatrix matrix = writer.encode(ewmurl, BarcodeFormat.QR_CODE, 250, 250);
                            ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
                            MatrixToImageWriter.writeToStream(matrix, "PNG", imageStream);
                            byte[] tagInfo = imageStream.toByteArray();

                            //将byte[]转换为base64
                            String baseStr = Base64Util.encode(tagInfo);
                            taskBasicJson.put("ewmimg", "data:image/jpg;base64," + baseStr);
                        }
                        taskBasicJson.put("taskremark",
                                StringUtil.isNotBlank(auditTask.getStr("taskremark")) ? auditTask.getStr("taskremark")
                                        : "无");
                        //事项说明
                        taskBasicJson.put("handleremark", StringUtil.isNotBlank(auditTaskExtension.getStr("handleremark")) ?
                                auditTaskExtension.getStr("handleremark") : "无");
                        // 3.4.1、获取事项扩展信息
                        taskBasicJson.put("itemid", auditTask.getItem_id()); // 事项编码
                        taskBasicJson.put("qlkind", iCodeItemsService.getItemTextByCodeName("审批类别",
                                String.valueOf(auditTask.getShenpilb()))); // 事项审批类别
                        //// 是否禁用
                        taskBasicJson.put("is_enable", auditTask.getIs_enable());
                        //// 是否网厅展示事项
                        taskBasicJson.put("iswtshow", auditTask.get("iswtshow"));

                        if (StringUtil.isNotBlank(auditTask.getOuname())) {
                            FrameOu frameOu = ouService.getOuByOuGuid(auditTask.getOuguid());
                            if (frameOu != null) {
                                taskBasicJson.put("implementsubject", frameOu.getOuname()); // 实施主体
                            } else {
                                taskBasicJson.put("implementsubject", auditTask.getOuname()); // 实施主体
                            }
                        } else {
                            taskBasicJson.put("implementsubject", "无");
                        }

                        if (StringUtil.isNotBlank(auditTask.getStr("AGENT_NAME"))) {
                            taskBasicJson.put("agentname", auditTask.getStr("AGENT_NAME")); // 承办机构
                        } else {
                            taskBasicJson.put("agentname", "无");
                        }

                        if (StringUtil.isNotBlank(auditTask.getCatalogcode())) {
                            taskBasicJson.put("catalogcode", auditTask.getCatalogcode()); // 基本编码
                        } else {
                            taskBasicJson.put("catalogcode", "无");
                        }

                        if (StringUtil.isNotBlank(auditTask.getVersion())) {
                            taskBasicJson.put("version", auditTask.getVersion()); // 事项版本
                        } else {
                            taskBasicJson.put("version", "无");
                        }

                        if (StringUtil.isNotBlank(auditTask.getCharge_flag())) {
                            taskBasicJson.put("chargeflag", iCodeItemsService.getItemTextByCodeName("是否",
                                    String.valueOf(auditTask.getCharge_flag()))); // 是否收费
                        } else {
                            taskBasicJson.put("chargeflag", "无");
                        }

                        if (StringUtil.isNotBlank(auditTask.getStr("ORG_DECIDE_EMAIL"))) {
                            taskBasicJson.put("orgdecideemail", auditTask.getStr("ORG_DECIDE_EMAIL")); // 联办机构
                        } else {
                            taskBasicJson.put("orgdecideemail", "-");
                        }

                        // 通办范围
                        String CROSSSCOPE = auditTask.getStr("CROSS_SCOPE");
                        String crosscope = "-";
                        if (StringUtil.isNotBlank(CROSSSCOPE)) {
                            String[] applyertypearr = CROSSSCOPE.split(";");
                            for (int i = 0; i < applyertypearr.length; ++i) {
                                crosscope = this.iCodeItemsService.getItemTextByCodeName("通办范围", applyertypearr[i])
                                        + ";";
                            }
                            crosscope = crosscope.substring(0, crosscope.length() - 1);
                        }
                        if (StringUtil.isNotBlank(crosscope)) {
                            taskBasicJson.put("crosscope", crosscope);
                        } else {
                            taskBasicJson.put("crosscope", "-");
                        }

                        // 是否一次办好（1-是，2-否）
                        String isycbh = auditTask.getStr("ispyc");
                        if (StringUtil.isNotBlank(isycbh)) {
                            isycbh = this.iCodeItemsService.getItemTextByCodeName("是否", isycbh);
                        } else {
                            isycbh = "";
                        }
                        taskBasicJson.put("isycbh", isycbh);

                        String POWER_SOURCE = auditTask.getStr("POWER_SOURCE");
                        if (StringUtil.isNotBlank(POWER_SOURCE)) {
                            POWER_SOURCE = this.iCodeItemsService.getItemTextByCodeName("权力来源", POWER_SOURCE);
                        } else {
                            POWER_SOURCE = "无";
                        }
                        taskBasicJson.put("qlstate", POWER_SOURCE);

                        // 审批类别
                        String shenpilb = auditTask.getShenpilb();
                        dataJson.put("shenpilb", shenpilb);

                        // 3.4.1.2、获取申请人类型
                        String SERVICEOBJECT = auditTask.getStr("SERVICE_OBJECT");// 服务对象
                        String managementObj = "";
                        if (StringUtil.isNotBlank(SERVICEOBJECT)) {
                            String[] applyertypearr = SERVICEOBJECT.split(";");
                            for (int i = 0; i < applyertypearr.length; ++i) {
                                managementObj = this.iCodeItemsService.getItemTextByCodeName("服务对象", applyertypearr[i])
                                        + ";";
                            }
                            managementObj = managementObj.substring(0, managementObj.length() - 1);
                        }
                        taskBasicJson.put("managementobj", managementObj);
                        taskBasicJson.put("type",
                                iCodeItemsService.getItemTextByCodeName("办件类型", String.valueOf(auditTask.getType())));// 办件类型
                        Integer anticipateday = auditTask.getAnticipate_day();//法定期限天数
                        String anticipatetype = iCodeItemsService.getItemTextByCodeName("国标_时限单位", auditTask.getStr("ANTICIPATE_TYPE"));
                        if (StringUtil.isBlank(anticipatetype)) {
                            if (anticipateday == 500) {
                                taskBasicJson.put("anticipateday", "无");// 法定期限
                            } else {
                                taskBasicJson.put("anticipateday", auditTask.getAnticipate_day() + "个工作日");// 法定期限
                            }
                        } else {
                            if ("4".equals(auditTask.getStr("ANTICIPATE_TYPE"))) {
                                taskBasicJson.put("anticipateday", "当场");
                            } else {
                                taskBasicJson.put("anticipateday", auditTask.getAnticipate_day() + "个" + anticipatetype);
                            }
                        }

                        Integer promiseday = auditTask.getPromise_day();//承诺期限天数
                        String promisetype = iCodeItemsService.getItemTextByCodeName("国标_时限单位", auditTask.getPromise_type());
                        if (StringUtil.isBlank(promisetype)) {
                            if (promiseday == 500) {
                                taskBasicJson.put("promiseday", "无");// 承诺期限
                            } else {
                                taskBasicJson.put("promiseday", auditTask.getPromise_day() + "个工作日");// 承诺期限
                            }
                        } else {
                            if ("4".equals(auditTask.getPromise_type())) {
                                taskBasicJson.put("promiseday", "当场");
                            } else {
                                taskBasicJson.put("promiseday", auditTask.getPromise_day() + "个" + promisetype);
                            }
                        }

                        taskBasicJson.put("linktel", auditTask.getLink_tel());// 咨询电话
                        taskBasicJson.put("supervisetel", auditTask.getSupervise_tel());// 投诉电话

                        if (StringUtil.isBlank(auditTask.getTransact_addr())) {
                            taskBasicJson.put("handleaddress", "无");
                        } else {
                            taskBasicJson.put("handleaddress", auditTask.getTransact_addr());// 办公地点
                        }
                        if (StringUtil.isBlank(auditTask.getTransact_time())) {
                            taskBasicJson.put("handletime", "无");
                        } else {
                            taskBasicJson.put("handletime", auditTask.getTransact_time());// 办公时间
                            // 为了兼容之前的前台页面暂时不删除
                        }

                        String addresstime = auditTask.getStr("ACCEPT_ADDRESS_INFO");
                        List<Record> acceptaddinfos = new ArrayList<Record>();
                        if (StringUtil.isNotBlank(addresstime)) {
                            acceptaddinfos = syncbanlididiansj(addresstime);// 受理地点信息
                        }
                        if (acceptaddinfos == null || acceptaddinfos.isEmpty()) {
                            Record record = new Record();
                            record.set("address", "无");
                            record.set("officehour", "无");
                            acceptaddinfos.add(record);
                        }

                        List<Record> liucheng = new ArrayList<Record>();
                        if (auditTaskExtension != null) {
                            log.info("=======getTaskBasicInfo接口-taskguidex.rowguid：" + auditTaskExtension.getRowguid()
                                    + "=======");
                            // 3.1、事项是否可以网上申报
                            // Integer webApplyType = auditTaskExtension == null
                            // ? 0
                            // : (auditTaskExtension.getWebapplytype() == null ?
                            // 0
                            // : auditTaskExtension.getWebapplytype());

                            // 3.2、事项是否可以网上预约
                            if (isAppoint(auditTask.getTask_id())) {
                                taskElementJson.put("appointment",
                                        auditTaskExtension == null ? "0" : auditTaskExtension.getReservationmanagement());// 事项是否可以网上预约
                            }

                            // 3.4.1.1、获取行使层级
                            String useLevel = auditTaskExtension.getUse_level();
                            String exerciseClass = "";
                            if (StringUtil.isNotBlank(useLevel)) {
                                String[] userLevelArr = useLevel.split(";");
                                for (int i = 0; i < userLevelArr.length; i++) {
                                    exerciseClass = iCodeItemsService.getItemTextByCodeName("行使层级", userLevelArr[i])
                                            + ";";
                                }
                                exerciseClass = exerciseClass.substring(0, exerciseClass.length() - 1);
                            }
                            taskBasicJson.put("exerciseclass", exerciseClass);

                            // 数量限制
                            //许可类事项取别的字段
                            String type = auditTask.getShenpilb();
                            String limitnum = "";
                            if ("01".equals(type)) {
                                limitnum = auditTaskExtension.getStr("IS_HASNUMLIMIT");
                            } else {
                                limitnum = auditTaskExtension.getStr("LIMIT_NUMBER");
                            }
                            if (StringUtil.isNotBlank(limitnum)) {
                                taskBasicJson.put("limitnum", limitnum);
                            } else {
                                taskBasicJson.put("limitnum", "-");
                            }

                            String banliliucheng = auditTask.getStr("in_flow_info");
                            String folder_code = auditTask.getStr("folder_code");
                            if (StringUtil.isNotBlank(banliliucheng)) {
                                liucheng = syncbanliliucheng(banliliucheng, type, folder_code);// 办理流程
                            }

                            // xz 是否特殊程序
                            String procedure = auditTaskExtension.getStr("IS_PROCEDURE");
                            // xz 特别程序种类名称
                            String procedurecontent = auditTaskExtension.getStr("PROCEDURE_NAME");
                            if (StringUtil.isNotBlank(procedure)) {
                                procedure = this.iCodeItemsService.getItemTextByCodeName("是否", procedure);
                            } else {
                                procedure = "";
                            }
                            taskBasicJson.put("procedure", procedure);

                            if ("是".equals(procedure)) {
                                taskBasicJson.put("hasprocedure", "1");
                                String[] contents = procedurecontent.split(",");
                                if (contents.length >= 2) {
                                    String procedureshixian = contents[0];
                                    taskBasicJson.put("procedureshixian", procedureshixian);
                                    String proceduretype = contents[1];
                                    if ("2".equals(proceduretype) && contents.length > 2) {
                                        taskBasicJson.put("hasproceduretype", "非延长审批");
                                        String procedurecont = "";
                                        for (int i = 0; i < contents.length; i++) {
                                            if (0 != i && 1 != i) {
                                                procedurecont += contents[i] + ",";
                                            }
                                        }
                                        procedurecont = procedurecont.substring(0, procedurecont.length() - 1);
                                        taskBasicJson.put("proceduretypecont", procedurecont);
                                    } else {
                                        taskBasicJson.put("hasproceduretype", "延长审批");
                                    }
                                }
                            } else {
                                taskBasicJson.put("hasprocedure", "0");
                            }

                            taskBasicJson.put("dao_xc_num", auditTask.getStr("applyermin_count"));// 到办事窗口的最少次数

                            // 是否支持网上支付
                            taskBasicJson.put("Onlinepayment", auditTaskExtension.getOnlinepayment());
                            // 通办范围
                            taskBasicJson.put("operationscope", auditTask.getStr("CROSS_SCOPE"));
                            // 是否支持物流快递
                            taskBasicJson.put("isexpress", auditTask.getStr("IS_DELIVERY"));
                            //材料邮寄收件人
                            if(StringUtils.isNotBlank(auditTaskExtension.getStr("ADDRESSEE"))){
                                taskBasicJson.put("ADDRESSEE", auditTaskExtension.getStr("ADDRESSEE"));
                            }else{
                                taskBasicJson.put("ADDRESSEE", "-");
                            }

                            //申请材料收件人联系电话
                            if(StringUtils.isNotBlank(auditTaskExtension.getStr("addresseePhone"))){
                                taskBasicJson.put("addresseePhone", auditTaskExtension.getStr("addresseePhone"));
                            }else{
                                taskBasicJson.put("addresseePhone",  "-");
                            }
                            //申请人材料邮寄地址
                            if(StringUtils.isNotBlank(auditTaskExtension.getStr("transactADDR"))){
                                taskBasicJson.put("transactADDR", auditTaskExtension.getStr("transactADDR"));
                            }else{
                                taskBasicJson.put("transactADDR", "-");
                            }

                            // 扩展字段，电子证照相关字段
                            String ExtendField = auditTaskExtension.getStr("ExtendField");

                            // 是否中介服务
                            if (StringUtil.isNotBlank(ExtendField) && !"0".equals(ExtendField)) {
                                Boolean isextendfield = JSON.isValidObject(ExtendField);
                                List<Record> zhongjielist = new ArrayList<Record>();
                                if (isextendfield) {
                                    JSONObject extend = JSONObject.parseObject(ExtendField);
                                    JSONArray zhongjies = extend.getJSONArray("zhongjieList");
                                    if (zhongjies != null) {
                                        for (int i = 0; i < zhongjies.size(); i++) {
                                            JSONObject zhongjie = zhongjies.getJSONObject(i);
                                            Record zjie = new Record();
                                            zjie.set("name", zhongjie.getString("name"));
                                            zjie.set("code", zhongjie.getString("code"));

                                            JSONArray typeAndReqs = zhongjie.getJSONArray("typeAndReq");
                                            String typeAndReq = "";
                                            for (int j = 0; j < typeAndReqs.size(); j++) {
                                                JSONArray typereq = typeAndReqs.getJSONArray(j);
                                                for (int k = 0; k < typereq.size(); k++) {
                                                    typeAndReq += iCodeItemsService.getItemTextByCodeName("中介机构服务类别",
                                                            typereq.getString(k)) + "：";
                                                }
                                                typeAndReq += "。";
                                            }
                                            zjie.set("typeAndReq", typeAndReq);

                                            JSONArray purchasingTargets = zhongjie.getJSONArray("purchasingTarget");

                                            if (purchasingTargets != null && !purchasingTargets.isEmpty()) {
                                                if ("1".equals(purchasingTargets.getString(0))) {
                                                    zjie.set("purchasingTarget", "财政预算单位");
                                                } else {
                                                    zjie.set("purchasingTarget", "非财政预算单位");
                                                }

                                            }
                                            String serviceTime = zhongjie.getString("serviceTime");
                                            if ("1".equals(serviceTime)) {
                                                zjie.set("serviceTime", "双方协商约定");
                                            } else if ("2".equals(serviceTime)) {
                                                zjie.set("serviceTime", "明确时限");
                                            }

                                            zjie.set("serviceTimeTxt", zhongjie.getString("serviceTimeTxt"));
                                            zjie.set("serviceTimeTxtUnit", zhongjie.getString("serviceTimeTxtUnit"));
                                            zjie.set("intermediaryServiceOrganization",
                                                    zhongjie.getString("intermediaryServiceOrganization"));

                                            String intermediaryServicesCharges = zhongjie
                                                    .getString("intermediaryServicesCharges");
                                            if ("1".equals(intermediaryServicesCharges)) {
                                                intermediaryServicesCharges = "行政事业性收费";
                                            } else if ("2".equals(intermediaryServicesCharges)) {
                                                intermediaryServicesCharges = "经营服务性收费（政府定价）";
                                            } else if ("3".equals(intermediaryServicesCharges)) {
                                                intermediaryServicesCharges = "经营服务性收费（政府指导定价）";
                                            } else if ("4".equals(intermediaryServicesCharges)) {
                                                intermediaryServicesCharges = "经营服务性收费（市场调节价）";
                                            }
                                            zjie.set("intermediaryServicesCharges", intermediaryServicesCharges);
                                            zjie.set("agcyResult", zhongjie.getString("agcyResult"));

                                            // JSONArray intermediateResults =
                                            // zhongjie
                                            // .getJSONArray("intermediateResults");

                                            // zjie.set("intermediateResults",
                                            // zhongjie.getString("intermediateResults"));
                                            zhongjielist.add(zjie);
                                        }
                                    }
                                    dataJson.put("zhongjielist", zhongjielist);

                                }
                            }

                            // 行使内容
                            String uselevel = auditTaskExtension.getUse_level_c();
                            if (StringUtil.isBlank(uselevel)) {
                                uselevel = "无";
                            }
                            taskBasicJson.put("uselevel", uselevel);

                            // 网办深度
                            String WEBAPPLYDEPTH = auditTaskExtension.getStr("wangbanshendu");
                            if (StringUtil.isBlank(WEBAPPLYDEPTH)) {
                                taskBasicJson.put("webapplydepth", "-");
                            } else {
                                WEBAPPLYDEPTH = WEBAPPLYDEPTH.replaceAll("\\^", ",");
                                String shendu = iCodeItemsService.getItemTextByCodeName("网上办理深度", WEBAPPLYDEPTH);
                                taskBasicJson.put("webapplydepth", shendu);
                            }
                            // 是否中介服务
                            if (StringUtil.isNotBlank(auditTaskExtension.getStr("IS_AGENCYORGAN"))
                                    && !"0".equals(auditTaskExtension.getStr("IS_AGENCYORGAN"))) {
                                String serverprojectinfo = auditTaskExtension.getStr("server_project_info");
                                if (StringUtil.isNotBlank(serverprojectinfo)) {
                                    //解析中介服务，暂时只保留 中介服务项目名称
                                    taskBasicJson.put("isagencyorgan", syncZjfwinfo(serverprojectinfo));
                                } else {
                                    taskBasicJson.put("isagencyorgan", "本事项暂无中介服务");
                                }
                            } else {
                                taskBasicJson.put("isagencyorgan", "本事项暂无中介服务");
                            }

                            // 是否进驻大厅
                            taskBasicJson.put("ifjzhall", auditTaskExtension.getIf_jz_hall());
                            // 是否存在特别程序 未同步
                            taskBasicJson.put("isprocedure", auditTaskExtension.getStr("IS_PROCEDURE"));

                            // 办理结果类型
                            String resultType = iCodeItemsService.getItemTextByCodeName("审批结果类型", auditTaskExtension.getStr("resulttype"));
                            if (StringUtil.isNotBlank(resultType)) {
                                taskBasicJson.put("resulttype", resultType);
                            } else {
                                taskBasicJson.put("resulttype", "-");
                            }

                            // 办理结果
                            AuditTaskResult auditTaskResult = iAuditTaskResultService
                                    .getAuditResultByTaskGuid(auditTask.getRowguid(), false).getResult();
                            if (auditTaskResult != null) {
                                // 办理结果名称
                                if (StringUtil.isNotBlank(auditTaskResult.getResultname())) {
                                    taskBasicJson.put("resultname", auditTaskResult.getResultname());
                                    taskBasicJson.put("finishfilename", auditTaskResult.getResultname());// 审批结果名称
                                } else {
                                    taskBasicJson.put("resultname", "无");
                                }
                                // 送达方式
                                if (StringUtil.isNotBlank(auditTaskResult.getStr("servicemode"))) {
                                    String servicemode = iCodeItemsService.getItemTextByCodeName("国标_办理结果送达方式",
                                            auditTaskResult.getStr("servicemode"));
                                    taskBasicJson.put("servicemode", servicemode);
                                } else {
                                    taskBasicJson.put("servicemode", "无");
                                }

                                String finishproductsamples = auditTaskResult.getStr("resultCliengguid");
                                if (StringUtil.isNotBlank(finishproductsamples)) {
                                    List<FrameAttachInfo> frameAttachInfos = iAttachService
                                            .getAttachInfoListByGuid(finishproductsamples);
                                    if (frameAttachInfos != null && !frameAttachInfos.isEmpty()) {
                                        taskBasicJson.put("finishproductsamples",
                                                frameAttachInfos.get(0).getAttachFileName());// 结果样本名称
                                        taskBasicJson.put("finishproductguid", finishproductsamples);// 审批结果标识
                                    }
                                }
                            } else {
                                String s = auditTaskExtension.getStr("RESULT_INFO");
                                if (s != null && s.length() > 30) {
                                    String result = syncResultInfo(auditTaskExtension.getStr("RESULT_INFO"));
                                    String[] strs = result.split(";");
                                    // 办理结果名称
                                    taskBasicJson.put("resultname", strs[0].substring(0, strs[0].length() - 1));
                                    // 送达方式
                                    taskBasicJson.put("servicemode",
                                            strs[2].substring(0, strs[2].length() - 1).replace("^", "、")
                                                    .replace("1", "窗口领取").replace("2", "公告送达").replace("3", "邮寄送达")
                                                    .replace("4", "网站下载").replace("5", "其他"));
                                    if (StringUtil.isBlank(taskBasicJson.get("servicemode"))) {
                                        taskBasicJson.put("servicemode", "无");
                                    }
                                } else {
                                    taskBasicJson.put("resultname", "无");
                                    // 办理结果类型
                                    taskBasicJson.put("resulttype", "无");
                                    // 送达方式
                                    taskBasicJson.put("servicemode", "无");
                                }

                                String finishproductsamples = auditTaskExtension.getFinishproductsamples();
                                if (StringUtil.isNotBlank(finishproductsamples)) {
                                    List<FrameAttachInfo> frameAttachInfos = iAttachService
                                            .getAttachInfoListByGuid(finishproductsamples);
                                    if (frameAttachInfos != null && !frameAttachInfos.isEmpty()) {
                                        taskBasicJson.put("finishproductsamples",
                                                frameAttachInfos.get(0).getAttachFileName());// 结果样本名称
                                        taskBasicJson.put("finishproductguid", finishproductsamples);// 审批结果标识
                                    }
                                }
                            }

                            // 办理形式
                            if (StringUtil.isNotBlank(auditTaskExtension.getStr("HANDLE_FORM"))) {

                                taskBasicJson.put("handleform",
                                        auditTaskExtension.getStr("HANDLE_FORM").replace("^", "、").replace("0", "窗口办理")
                                                .replace("2", "网上办理").replace("3", "网上窗口均可办理").replace("4", "上门服务")
                                                .replace("5", "快递申请"));
                            } else {
                                taskBasicJson.put("handleform", "无");
                            }
                            // 权限划分
                            if (StringUtil.isNotBlank(auditTaskExtension.getPowerdeline())) {
                                taskBasicJson.put("powerdeline", auditTaskExtension.getPowerdeline());
                            } else {
                                taskBasicJson.put("powerdeline", "无");
                            }

                            // 实施主体性质
                            if (StringUtil.isNotBlank(auditTaskExtension.getSubjectnature())) {
                                taskBasicJson.put("subjectnature", auditTaskExtension.getSubjectnature());
                            } else {
                                taskBasicJson.put("subjectnature", "无");
                            }
                            // 办理方式
                            if (StringUtil.isNotBlank(auditTaskExtension.getStr("HANDLINGMODE"))) {
                                taskBasicJson.put("handlingmode", auditTaskExtension.getStr("HANDLINGMODE"));
                            } else {
                                taskBasicJson.put("handlingmode", "无");
                            }
                            // 咨询方式
                            if (StringUtil.isNotBlank(auditTaskExtension.getStr("CONSULT_URL"))) {
                                taskBasicJson.put("consulturl",
                                        auditTaskExtension.getStr("CONSULT_URL").replace("^", "、"));
                            } else {
                                taskBasicJson.put("consulturl", "无");
                            }

                            String complainType = auditTaskExtension.getStr("COMPLAIN_TYPE");
                            // 监督投诉方式
                            if (StringUtil.isNotBlank(complainType)) {
                                taskBasicJson.put("complaintype", complainType.replace("^", "、"));
                            } else {
                                taskBasicJson.put("complaintype", "无");
                            }
                            // 法律救济
                            String complaininfo = auditTaskExtension.getStr("COMPLAIN_INFO");
                            if (StringUtil.isNotBlank(complaininfo) && complaininfo.contains("version")) {
                                if (complaininfo.contains("<nodes>")) {
                                    taskBasicJson.put("complaininfo", synccomplaininfo(complaininfo));
                                } else {
                                    if (complaininfo.length() > 97) {
                                        taskBasicJson.put("complaininfo",
                                                complaininfo.substring(97).replace("<TYPE>2</TYPE>", "<br>行政复议")
                                                        .replace("<TYPE>3</TYPE>", "<br>行政诉讼").replace("<NAME>", "<br>部门：")
                                                        .replace("<ADDRESS>", "<br>地址：").replace("<PHONE>", "<br>电话：")
                                                        .replace("</NAME>", "").replace("</ADDRESS>", "")
                                                        .replace("</PHONE>", "").replace("</COMPLAIN>", "")
                                                        .replace("<COMPLAIN>", "").replace("</COMPLAINS>", "")
                                                        .replace("</DATAAREA>", ""));
                                    } else {
                                        taskBasicJson.put("complaininfo", complaininfo);
                                    }
                                }
                            } else {
                                taskBasicJson.put("complaininfo", "无");
                            }

                            /*// 中介服务基本信息
                            String serverprojectinfo = auditTaskExtension.getStr("server_project_info");
                            if (StringUtil.isNotBlank(serverprojectinfo) && serverprojectinfo.contains("version")
                                    && serverprojectinfo.length() > 97) {
                                taskBasicJson.put("serverprojectinfo",
                                        serverprojectinfo.substring(97).replace("<CHANGE_FLAG>0</CHANGE_FLAG>", "")
                                                .replace("<SERVEPROJECTS>", "").replace("<SERVEPROJECT>", "")
                                                .replace("<NAME>", "<br>中介服务项目名称：").replace("<BASIC_LAW>", "<br>法律依据：")
                                                .replace("<RESULT_NAME>", "<br>结果名称：")
                                                .replace("<REMARK>", "<br>中介服务项目名称：").replace("</REMARK>", "")
                                                .replace("</RESULT_NAME>", "").replace("</NAME>", "")
                                                .replace("</BASIC_LAW>", "").replace("</SERVEPROJECT>", "")
                                                .replace("</SERVEPROJECTS>", "").replace("</DATAAREA>", ""));
                            } else {
                                taskBasicJson.put("serverprojectinfo", "无");
                            }*/

                            // 引导视频
                            if (StringUtil.isNotBlank(auditTaskExtension.get("pcspydguid"))) {
                                List<FrameAttachInfo> frameAttachInfos = iAttachService
                                        .getAttachInfoListByGuid(auditTaskExtension.getStr("pcspydguid"));
                                if (frameAttachInfos != null && !frameAttachInfos.isEmpty()) {
                                    taskBasicJson.put("pcspydurl", WebUtil.getRequestCompleteUrl(request)
                                            + "/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid="
                                            + frameAttachInfos.get(0).getAttachGuid());
                                }
                            }

                        }
                        // 3.4.1.3 获取办理情况
                        List<JSONObject> taskHandleJsonList = new ArrayList<JSONObject>();

                        // 标志当前循环中的窗口是否是第一个进入循环的市区县的窗口。
                        Boolean firstWindow = true;
                        if (auditOrgaWindows != null && !auditOrgaWindows.isEmpty()) {
                            for (AuditOrgaWindow auditOrgaWindow : auditOrgaWindows) {
                                AuditOrgaServiceCenter auditOrgaServiceCenter = iAuditOrgaServiceCenter
                                        .findAuditServiceCenterByGuid(auditOrgaWindow.getCenterguid()).getResult();
                                String areaCode = "";
                                String windowAddress = "";
                                if(auditOrgaServiceCenter!=null){
                                    areaCode = auditOrgaServiceCenter.getBelongxiaqu();
                                    // 3.4.1.3.1 拼接窗口详细信息
                                    windowAddress = auditOrgaServiceCenter.getOuname() + " "
                                            + auditOrgaWindow.getAddress() + " " + auditOrgaWindow.getWindowname() + " "
                                            + auditOrgaWindow.getWindowno();
                                }

                                // 3.4.1.3.2 获取排队等待人数
                                String taskTypeGuid = iAuditQueueTasktypeTask.getTaskTypeguidbyTaskIDandCenterGuid(
                                        auditTask.getTask_id(), auditOrgaWindow.getCenterguid()).getResult();
                                // 否存在限号标志位
                                int leftQueueNum = -1;
                                if (StringUtil.isNotBlank(taskTypeGuid)) {
                                    AuditQueueTasktype auditQueueTasktype = iAuditQueueTasktype
                                            .getTasktypeByguid(taskTypeGuid).getResult();
                                    if (auditQueueTasktype != null) {
                                        // 判断是否存在限号
                                        if (StringUtil.isNotBlank(auditQueueTasktype.getXianhaonum())) {
                                            leftQueueNum = Integer.parseInt(auditQueueTasktype.getXianhaonum())
                                                    - iAuditQueue.getCountByTaskGuid(taskTypeGuid).getResult();
                                        }
                                    }
                                }
                                // 剩余叫号数 -1代表没有限制
                                String leftNum = leftQueueNum == -1 ? "无限制" : leftQueueNum + "个";
                                // 3.4.1.3.3 将市区县中心窗口信息作为list的第一个返回
                                if (areaCode.length() == 6 && firstWindow) {
                                    firstWindow = false;
                                    JSONObject taskBaseHandleJson = new JSONObject();
                                    taskBaseHandleJson.put("windowaddress", windowAddress); // 中心窗口地址
                                    taskBaseHandleJson.put("handleaddress", auditTask.getTransact_addr());// 办公地点
                                    taskBaseHandleJson.put("handletime", auditTask.getTransact_time());// 办公时间
                                    taskBaseHandleJson.put("linktel", auditTask.getSupervise_tel());// 投诉电话
                                    taskBaseHandleJson.put("waitnum",
                                            iHandleQueue.getTaskWaitNum(taskTypeGuid, true).getResult());
                                    taskBaseHandleJson.put("leftqueuenum", leftNum);
                                    taskHandleJsonList.add(0, taskBaseHandleJson);
                                    break;
                                } else {
                                    JSONObject taskHandleJson = new JSONObject();
                                    AuditTaskDelegate auditTaskDelegate = iAuditTaskDelegate
                                            .findByTaskIDAndAreacode(auditTask.getTask_id(), areaCode).getResult();
                                    if (auditTaskDelegate != null) {
                                        taskHandleJson.put("windowaddress", windowAddress);// 中心窗口地址
                                        // 如果是乡镇法定事项，读取audittask内的信息
                                        if (ZwfwConstant.TASKDELEGATE_TYPE_XZFD
                                                .equals(auditTaskDelegate.getDelegatetype())) {
                                            taskHandleJson.put("handleaddress", auditTask.getTransact_addr());// 办公地点
                                            taskHandleJson.put("handletime", auditTask.getTransact_time());// 办公时间
                                            taskHandleJson.put("linktel", auditTask.getSupervise_tel());// 投诉电话
                                            taskHandleJson.put("waitnum",
                                                    iHandleQueue.getTaskWaitNum(taskTypeGuid, true).getResult()); // 等待人数
                                            taskHandleJson.put("leftqueuenum", leftNum); // 剩余取号数
                                            taskHandleJsonList.add(taskHandleJson);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        taskBasicJson.put("addressandtimelist", taskHandleJsonList);
                        dataJson.put("taskbasic", taskBasicJson);
                        dataJson.put("liuchengJson", liucheng);
                        taskBasicJson.put("acceptaddinfos", acceptaddinfos);
                        // 3.4.2、获取事项受理条件
                        dataJson.put("handlecondition", StringUtil.isBlank(auditTask.getAcceptcondition()) ? ""
                                : auditTask.getAcceptcondition());// 受理条件
                        String taskOutImgGuid = auditTask.getTaskoutimgguid();
                        if (StringUtil.isNotBlank(taskOutImgGuid)) {
                            List<FrameAttachInfo> frameAttachInfos = iAttachService
                                    .getAttachInfoListByGuid(taskOutImgGuid);
                            if (frameAttachInfos != null && !frameAttachInfos.isEmpty()) {
                                taskOutImgGuid = frameAttachInfos.get(0).getAttachGuid();// 办理流程
                                dataJson.put("taskoutimg", WebUtil.getRequestCompleteUrl(request)
                                        + "/rest/auditattach/readAttach?attachguid=" + taskOutImgGuid);
                            }
                        }
                        // 3.4.3、获取事项设定依据
                        dataJson.put("setting", StringUtil.isBlank(auditTask.getBy_law()) ? "" : auditTask.getBy_law());
                        // 3.4.4、获取事项结果查询
                        dataJson.put("result", auditTask.getQuery_method());
                        List<JSONObject> taskMaterialList = new ArrayList<JSONObject>();
                        // 3.4.5、获取事项材料信息
                        List<AuditTaskMaterial> auditTaskMaterials = iAuditTaskMaterial
                                .selectTaskMaterialListByTaskGuid(taskGuid, true).getResult();
                        // 3.4.5.1、对事项材料进行排序
                        Collections.sort(auditTaskMaterials, new Comparator<AuditTaskMaterial>() {
                            @Override
                            public int compare(AuditTaskMaterial b1, AuditTaskMaterial b2) {
                                // 3.4.5.1、优先对比材料必要性（必要在前）
                                int comNecessity = b1.getNecessity().compareTo(b2.getNecessity());
                                int ret = comNecessity;
                                // 3.4.5.1、材料必要性一致的情况下对比排序号（排序号降序排）
                                if (comNecessity == 0) {
                                    Integer ordernum1 = b1.getOrdernum() == null ? 0 : b1.getOrdernum();
                                    Integer ordernum2 = b2.getOrdernum() == null ? 0 : b2.getOrdernum();
                                    ret = ordernum2.compareTo(ordernum1);
                                }
                                return ret;
                            }
                        });
                        // 3.4.5.2、拼接材料返回JSON
                        for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
                            JSONObject materialJson = new JSONObject();
                            String materialGuid = auditTaskMaterial.getRowguid();
                            materialJson.put("materialguid", materialGuid);// 材料标识
                            materialJson.put("materialname", auditTaskMaterial.getMaterialname());// 材料名称
                            // 3.4.5.2.1、获取填报示例对应附件标识
                            if (StringUtil.isNotBlank(auditTaskMaterial.getExampleattachguid())) {
                                int exampleAttachCount = iAttachService
                                        .getAttachCountByClientGuid(auditTaskMaterial.getExampleattachguid());
                                if (exampleAttachCount > 0) {
                                    materialJson.put("exampleattachguid", auditTaskMaterial.getExampleattachguid());
                                }
                            }
                            // 3.4.5.2.2、获取空白模板对应附件标识
                            if (StringUtil.isNotBlank(auditTaskMaterial.getTemplateattachguid())) {
                                int templateAttachCount = iAttachService
                                        .getAttachCountByClientGuid(auditTaskMaterial.getTemplateattachguid());
                                if (templateAttachCount > 0) {
                                    materialJson.put("templateattachguid", auditTaskMaterial.getTemplateattachguid());
                                }
                            }
                            materialJson.put("submittype", iCodeItemsService.getItemTextByCodeName("提交方式",
                                    String.valueOf(auditTaskMaterial.getSubmittype()))); // 材料提交方式
                            // 3.4.5.2.3、获取材料来源渠道
                            String materialsource;
                            if (StringUtil.isBlank(auditTaskMaterial.getFile_source())) {
                                materialsource = "申请人自备";
                            } else {
                                materialsource = iCodeItemsService.getItemTextByCodeName("来源渠道",
                                        auditTaskMaterial.getFile_source());
                            }
                            materialJson.put("materialsource", materialsource);
                            // 3.4.5.2.4、获取事项材料必要性
                            String necessary = "0";
                            if (StringUtil.isNotBlank(taskCaseGuid)) {
                                AuditTaskMaterialCase auditTaskMaterialCase = iAuditTaskMaterialCase
                                        .selectTaskMaterialCaseByGuid(taskCaseGuid, materialGuid).getResult();
                                if (auditTaskMaterialCase != null) {
                                    necessary = ZwfwConstant.NECESSITY_SET_YES
                                            .equals(String.valueOf(auditTaskMaterialCase.getNecessity())) ? "1" : "0";// 是否必需
                                }
                            } else {
                                necessary = ZwfwConstant.NECESSITY_SET_YES
                                        .equals(String.valueOf(auditTaskMaterial.getNecessity())) ? "1" : "0";// 是否必需
                            }
                            int pagenum; // 份数
                            if (StringUtil.isBlank(auditTaskMaterial.getPage_num())
                                    || 0 == auditTaskMaterial.getPage_num()) {
                                pagenum = 0;
                            } else {
                                pagenum = auditTaskMaterial.getPage_num();
                            }
                            int copynum; // 复印件份数
                            if (StringUtil.isBlank(auditTaskMaterial.getStr("copy_num"))) {
                                copynum = 0;
                            } else {
                                copynum = Integer.parseInt(auditTaskMaterial.getStr("copy_num"));
                            }
                            if (pagenum == 0 && copynum == 0) {
                                materialJson.put("matertype", "");
                            } else if (pagenum != 0 && copynum == 0) {
                                materialJson.put("matertype", "原件");
                            } else if (pagenum == 0 && copynum != 0) {
                                materialJson.put("matertype", "复印件");
                            } else if (pagenum != 0 && copynum != 0) {
                                materialJson.put("matertype", "原件和复印件");
                            }
                            materialJson.put("necessary", necessary);
                            String Submittype = auditTaskMaterial.getSubmittype();

                            if ("10".equals(Submittype)) {
                                materialJson.put("pagenum", "电子"); // 份数
                            } else if ("20".equals(Submittype)) {
                                materialJson.put("pagenum", "纸质" + pagenum + "份"); // 份数
                            } else {
                                materialJson.put("pagenum", "电子或纸质"); // 份数
                            }

                            materialJson.put("standard", StringUtil.isBlank(auditTaskMaterial.getStandard()) ? "无"
                                    : auditTaskMaterial.getStandard());// 受理标准
                            materialJson.put("fileexplain",
                                    StringUtil.isBlank(auditTaskMaterial.getFile_explian()) ? "无"
                                            : auditTaskMaterial.getFile_explian());
                            materialJson.put("publisher",
                                    StringUtil.isBlank(auditTaskMaterial.getFile_source_explain()) ? "无"
                                            : auditTaskMaterial.getFile_source_explain());// 填报须知

                            // 扩展字段，电子证照相关字段
                            String isxtendfield = auditTaskMaterial.getStr("isxtendfield");

                            if ("1".equals(isxtendfield)) {
                                materialJson.put("materialsource", "已关联电子证照，可免提交");
                                materialJson.put("pagenum", "");
                                materialJson.put("matertype", "");
                                materialJson.put("isxtendfield", 1);
                                materialJson.put("isshuoming", 0);
                            } else {
                                materialJson.put("isxtendfield", 0);
                                materialJson.put("isshuoming", 1);
                            }

                            taskMaterialList.add(materialJson);
                        }
                        dataJson.put("taskmaterials", taskMaterialList);
                        // 4、获取事项收费标准
                        String chargeiteminfo = auditTask.getStr("CHARGEITEM_INFO");
                        if (StringUtil.isNotBlank(chargeiteminfo) && chargeiteminfo.contains("version")) {
                            if (chargeiteminfo.contains("<nodes>")) {
                                dataJson.put("chargeitems", syncchargeiteminfo(chargeiteminfo));
                            } else {
                                dataJson.put("chargeitems", "无");
                            }
                        } else {
                            dataJson.put("chargeitems", "无");
                        }

                        // 5、获取事项常见问题
                        List<JSONObject> taskFaqList = new ArrayList<JSONObject>();
                        List<AuditTaskFaq> auditTaskFaqs = iAuditTaskFaq
                                .selectAuditTaskFaqByTaskId(auditTask.getTask_id()).getResult();
                        if (auditTaskFaqs != null && !auditTaskFaqs.isEmpty()) {
                            Collections.sort(auditTaskFaqs, new Comparator<AuditTaskFaq>() // 对常见问题进行倒序排列
                            {
                                @Override
                                public int compare(AuditTaskFaq b1, AuditTaskFaq b2) {
                                    Integer faqsOrder1 = b1.getOrdernum() == null ? 0 : b1.getOrdernum();
                                    Integer faqsOrder2 = b2.getOrdernum() == null ? 0 : b2.getOrdernum();
                                    return faqsOrder2.compareTo(faqsOrder1);
                                }
                            });
                            for (AuditTaskFaq auditTaskFaq : auditTaskFaqs) {
                                JSONObject faqJson = new JSONObject();
                                faqJson.put("faqguid", auditTaskFaq.getRowguid()); // 常见问题标识
                                faqJson.put("question", auditTaskFaq.getQuestion());// 问题
                                faqJson.put("answer", auditTaskFaq.getAnswer());// 回答
                                taskFaqList.add(faqJson);
                            }
                        }
                        dataJson.put("faqs", taskFaqList);
                    }
                    log.info("=======结束调用getTaskBasicInfo接口=======");
                    return JsonUtils.zwdtRestReturn("1", "事项基本信息获取成功", dataJson.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "该事项不存在！", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getTaskBasicInfo接口参数：params【" + params + "】=======");
            log.info("=======getTaskBasicInfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "事项基本信息获取失败：" + e.getMessage(), "");
        }
    }


    @RequestMapping(value = "/getTaskByInnercode", method = RequestMethod.POST)
    public String getTaskByInnercode(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getTaskByInnercode接口=======");
            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject dataJson = new JSONObject();
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String itemid = obj.getString("itemcode");//
                String inner_code = obj.getString("inner_code");//
                AuditTask task = iJnAppRestService.getTaskByInnercode(inner_code).getResult();
                dataJson.put("taskguid", task.getRowguid());// 事项标识
                dataJson.put("taskid", task.getTask_id());// 事项业务唯一标识
                dataJson.put("taskname", task.getTaskname());// 事项名称
                dataJson.put("itemid", task.getItem_id());// 权利编码
                dataJson.put("areacode", task.getAreacode());// 权利编码
                dataJson.put("zj_url", task.getStr("zj_url"));
                dataJson.put("is_turn", task.getStr("is_turn"));
                log.info("=======结束调用getTaskByInnercode接口=======");
                return JsonUtils.zwdtRestReturn("1", "事项基本信息获取成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "token验证失败", jsonObject.toString());
            }
        } catch (Exception e) {
            log.info("=======getTaskByInnercode接口参数：params【" + params + "】=======");
            log.info("=======getTaskByInnercode异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取taskinfo失败：" + e.getMessage(), "");
        }
    }

    /**
     * 根据taskid获取实施taskguid
     */
    @RequestMapping(value = "/getTaskInfoByItemid", method = RequestMethod.POST)
    public String getTaskInfoByItemid(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getTaskInfoByItemid接口=======");
            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject dataJson = new JSONObject();
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取事项标识
                String itemid = obj.getString("itemcode");//
                String inner_code = obj.getString("inner_code");//
                // 1.2、获取事项情形标识
                String taskCaseGuid = obj.getString("taskcaseguid");
                // 1.3、获取是否需要事项全部信息 0：基本信息 1：所有信息
                String isNeedAll = obj.getString("isneedall");
                // 1.4、获取事项申报人类别，10：法人，20：个人
                String roleType = StringUtil.isBlank(obj.getString("roletype")) ? ZwdtConstant.ONLINEUSERTYPE_PERSON
                        : obj.getString("roletype");
                String taskGuid = iJnAppRestService.getTaskguidByInnercode(inner_code).getResult();
                AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, true).getResult();
                if (auditTask != null) {
                    AuditTaskExtension auditTaskExtension = iAuditTaskExtension
                            .getTaskExtensionByTaskGuid(taskGuid, true).getResult();
                    // 2、定义返回JSON对象

                    // 3、获取事项基本信息
                    dataJson.put("taskguid", taskGuid);// 事项标识
                    dataJson.put("taskid", auditTask.getTask_id());// 事项业务唯一标识
                    dataJson.put("taskname", auditTask.getTaskname());// 事项名称
                    dataJson.put("itemid", auditTask.getItem_id());// 权利编码
                    JSONObject taskElementJson = new JSONObject();
                    // 3.1、事项是否可以网上申报
                    Integer webApplyType = auditTaskExtension.getWebapplytype();
                    String onlineHandle = "0";
                    if (webApplyType != null) {
                        onlineHandle = webApplyType == 0 ? "0" : "1";
                    }
                    taskElementJson.put("onlinehandle", Integer.parseInt(onlineHandle));
                    // 3.2、事项是否可以网上预约
                    taskElementJson.put("appointment", auditTaskExtension.getReservationmanagement());
                    dataJson.put("taskelement", taskElementJson);
                    if (ZwdtConstant.ONLINEUSERTYPE_PERSON.equals(roleType)) {
                        dataJson.put("qrcontent", WebUtil.getRequestCompleteUrl(request)
                                + "/epointzwmhwz/pages/eventdetail/personaleventdetail?taskguid=" + taskGuid);// 二维码内容
                    } else {
                        dataJson.put("qrcontent", WebUtil.getRequestCompleteUrl(request)
                                + "/epointzwmhwz/pages/legal/personaleventdetail?taskguid=" + taskGuid);// 二维码内容
                    }
                    // 3.3、获取材料多情形
                    List<AuditTaskCase> auditTaskCases = iAuditTaskCase.selectTaskCaseByTaskGuid(taskGuid).getResult();
                    List<JSONObject> caseConditionList = new ArrayList<JSONObject>();
                    if (auditTaskCases != null && auditTaskCases.size() > 0) {
                        dataJson.put("taskcasecount", auditTaskCases.size());// 多情形列表数据
                        for (AuditTaskCase auditTaskCase : auditTaskCases) {
                            JSONObject caseConditionJson = new JSONObject();
                            caseConditionJson.put("taskcaseguid", auditTaskCase.getRowguid());// 多情形标识
                            caseConditionJson.put("casename", auditTaskCase.getCasename());// 多情形名称
                            caseConditionList.add(caseConditionJson);
                        }
                    }
                    dataJson.put("casecondition", caseConditionList);
                    // 3.4、判断是否需要事项的所有信息（事项扩展信息、事项材料、事项收费信息、事项常见问题、事项受理条件）
                    if ("1".equals(isNeedAll)) {
                        JSONObject taskBasicJson = new JSONObject();
                        // 3.4.1、获取事项扩展信息
                        taskBasicJson.put("itemid", auditTask.getItem_id()); // 事项编码
                        taskBasicJson.put("qlkind", iCodeItemsService.getItemTextByCodeName("审批类别",
                                String.valueOf(auditTask.getShenpilb()))); // 事项审批类别
                        taskBasicJson.put("implementsubject", auditTask.getOuname()); // 实施主体
                        // 3.4.1.1、获取行使层级
                        String useLevel = auditTaskExtension.getUse_level();
                        String exerciseClass = "";
                        if (StringUtil.isNotBlank(useLevel)) {
                            String[] userLevelArr = useLevel.split(";");
                            for (int i = 0; i < userLevelArr.length; i++) {
                                exerciseClass = iCodeItemsService.getItemTextByCodeName("行使层级", userLevelArr[i]) + ";";
                            }
                            exerciseClass = exerciseClass.substring(0, exerciseClass.length() - 1);
                        }
                        taskBasicJson.put("exerciseclass", exerciseClass);
                        // 3.4.1.2、获取申请人类型
                        String SERVICEOBJECT = auditTask.getStr("SERVICE_OBJECT");
                        String managementObj = "";
                        if (StringUtil.isNotBlank(SERVICEOBJECT)) {
                            String[] applyertypearr = SERVICEOBJECT.split(";");
                            for (int i = 0; i < applyertypearr.length; ++i) {
                                managementObj = this.iCodeItemsService.getItemTextByCodeName("服务对象", applyertypearr[i])
                                        + ";";
                            }
                            managementObj = managementObj.substring(0, managementObj.length() - 1);
                        }
                        taskBasicJson.put("managementobj", managementObj);// 办理对象
                        taskBasicJson.put("type",
                                iCodeItemsService.getItemTextByCodeName("办件类型", String.valueOf(auditTask.getType())));// 办件类型
                        Integer anticipateday = auditTask.getAnticipate_day();
                        if (anticipateday == 500) {
                            taskBasicJson.put("anticipateday", "无");// 法定期限
                        } else {
                            taskBasicJson.put("anticipateday", auditTask.getAnticipate_day() + "工作日");// 法定期限
                        }
                        taskBasicJson.put("promiseday", auditTask.getPromise_day() + "工作日");// 承诺期限
                        taskBasicJson.put("linktel", auditTask.getLink_tel());// 咨询电话
                        taskBasicJson.put("supervisetel", auditTask.getSupervise_tel());// 投诉电话
                        taskBasicJson.put("handleaddress", auditTask.getTransact_addr());// 办公地点
                        taskBasicJson.put("handletime", auditTask.getTransact_time());// 办公时间
                        dataJson.put("taskbasic", taskBasicJson);
                        String acceptcondition = auditTask.getAcceptcondition();
                        if (StringUtil.isNotBlank(acceptcondition)) {
                            String str1 = "<b class=\"red\">";
                            String str3 = "</b><b>";
                            String str2 = "</b><br>";
                            acceptcondition = acceptcondition.replace(str1, "").replace(str3, "").replace(str2, "");
                            // 3.4.2、获取事项受理条件
                            dataJson.put("handlecondition", acceptcondition);// 受理条件
                        } else {
                            dataJson.put("handlecondition", "符合相关规定，提交相关申请材料");// 受理条件
                        }
                        String taskOutImgGuid = auditTask.getTaskoutimgguid();
                        if (StringUtil.isNotBlank(taskOutImgGuid)) {
                            List<FrameAttachInfo> frameAttachInfos = iAttachService
                                    .getAttachInfoListByGuid(taskOutImgGuid);
                            if (frameAttachInfos != null && frameAttachInfos.size() > 0) {
                                taskOutImgGuid = frameAttachInfos.get(0).getAttachGuid();// 办理流程
                                dataJson.put("taskoutimg", WebUtil.getRequestCompleteUrl(request)
                                        + "/rest/auditattach/readAttach?attachguid=" + taskOutImgGuid);
                            }
                        }
                        // 3.4.3、获取事项设定依据
                        dataJson.put("setting", StringUtil.isBlank(auditTask.getBy_law()) ? "" : auditTask.getBy_law());
                        // 3.4.4、获取事项结果查询
                        dataJson.put("result", auditTask.getQuery_method());
                        List<JSONObject> taskMaterialList = new ArrayList<JSONObject>();
                        // 3.4.5、获取事项材料信息
                        List<AuditTaskMaterial> auditTaskMaterials = iAuditTaskMaterial
                                .selectTaskMaterialListByTaskGuid(taskGuid, true).getResult();
                        // 3.4.5.1、对事项材料进行排序
                        Collections.sort(auditTaskMaterials, new Comparator<AuditTaskMaterial>() {
                            @Override
                            public int compare(AuditTaskMaterial b1, AuditTaskMaterial b2) {
                                // 3.4.5.1、优先对比材料必要性（必要在前）
                                int comNecessity = b1.getNecessity().compareTo(b2.getNecessity());
                                int ret = comNecessity;
                                // 3.4.5.1、材料必要性一致的情况下对比排序号（排序号降序排）
                                if (comNecessity == 0) {
                                    Integer ordernum1 = b1.getOrdernum() == null ? 0 : b1.getOrdernum();
                                    Integer ordernum2 = b2.getOrdernum() == null ? 0 : b2.getOrdernum();
                                    ret = ordernum2.compareTo(ordernum1);
                                }
                                return ret;
                            }
                        });
                        // 3.4.5.2、拼接材料返回JSON
                        for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
                            JSONObject materialJson = new JSONObject();
                            String materialGuid = auditTaskMaterial.getRowguid();
                            materialJson.put("materialguid", materialGuid);// 材料标识
                            materialJson.put("materialname", auditTaskMaterial.getMaterialname());// 材料名称
                            // 3.4.5.2.1、获取填报示例对应附件标识
                            if (StringUtil.isNotBlank(auditTaskMaterial.getExampleattachguid())) {
                                int exampleAttachCount = iAttachService
                                        .getAttachCountByClientGuid(auditTaskMaterial.getExampleattachguid());
                                if (exampleAttachCount > 0) {
                                    materialJson.put("exampleattachguid", auditTaskMaterial.getExampleattachguid());
                                }
                            }

                            // 3.4.5.2.2、获取空白模板对应附件标识
                            if (StringUtil.isNotBlank(auditTaskMaterial.getTemplateattachguid())) {
                                int templateAttachCount = iAttachService
                                        .getAttachCountByClientGuid(auditTaskMaterial.getTemplateattachguid());
                                if (templateAttachCount > 0) {
                                    materialJson.put("templateattachguid", auditTaskMaterial.getTemplateattachguid());
                                }
                            }
                            materialJson.put("submittype", iCodeItemsService.getItemTextByCodeName("提交方式",
                                    String.valueOf(auditTaskMaterial.getSubmittype()))); // 材料提交方式
                            // 3.4.5.2.3、获取材料来源渠道
                            String materialsource;
                            if (StringUtil.isBlank(auditTaskMaterial.getFile_source())) {
                                materialsource = "申请人自备";
                            } else {
                                materialsource = iCodeItemsService.getItemTextByCodeName("来源渠道",
                                        auditTaskMaterial.getFile_source());
                            }
                            materialJson.put("materialsource", materialsource);
                            // 3.4.5.2.4、获取事项材料必要性
                            String necessary = "0";
                            if (StringUtil.isNotBlank(taskCaseGuid)) {
                                AuditTaskMaterialCase auditTaskMaterialCase = iAuditTaskMaterialCase
                                        .selectTaskMaterialCaseByGuid(taskCaseGuid, materialGuid).getResult();
                                if (auditTaskMaterialCase != null) {
                                    necessary = ZwfwConstant.NECESSITY_SET_YES
                                            .equals(String.valueOf(auditTaskMaterialCase.getNecessity())) ? "1" : "0";// 是否必需
                                }
                            } else {
                                necessary = ZwfwConstant.NECESSITY_SET_YES
                                        .equals(String.valueOf(auditTaskMaterial.getNecessity())) ? "1" : "0";// 是否必需
                            }
                            materialJson.put("necessary", necessary);
                            taskMaterialList.add(materialJson);
                        }
                        dataJson.put("taskmaterials", taskMaterialList);
                        // 4、获取事项收费标准
                        List<JSONObject> taskChargeItemList = new ArrayList<JSONObject>();
                        List<AuditTaskChargeItem> auditTaskChargeItems = iAuditTaskChargeItem
                                .selectAuditTaskChargeItemByTaskGuid(taskGuid, true).getResult();
                        if (auditTaskChargeItems != null && auditTaskChargeItems.size() > 0) {
                            for (AuditTaskChargeItem auditTaskChargeItem : auditTaskChargeItems) {
                                JSONObject chargeItemJson = new JSONObject();
                                chargeItemJson.put("chargeitemname", auditTaskChargeItem.getItemname());// 收费项目名称
                                chargeItemJson.put("chargeitemguid", auditTaskChargeItem.getRowguid());// 收费项目标识
                                chargeItemJson.put("unit", auditTaskChargeItem.getUnit());// 收费单位
                                chargeItemJson.put("chargeitemstand", auditTaskChargeItem.getChargeitem_stand());// 收费标准
                                taskChargeItemList.add(chargeItemJson);
                            }
                            dataJson.put("chargeitems", taskChargeItemList);
                        } else {
                            dataJson.put("chargeitems", "无");
                        }
                        dataJson.put("chargeitems", taskChargeItemList);
                        // 5、获取事项常见问题
                        List<JSONObject> taskFaqList = new ArrayList<JSONObject>();
                        List<AuditTaskFaq> auditTaskFaqs = iAuditTaskFaq
                                .selectAuditTaskFaqByTaskId(auditTask.getTask_id()).getResult();
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
                        if (auditTaskFaqs != null && auditTaskFaqs.size() > 0) {
                            for (AuditTaskFaq auditTaskFaq : auditTaskFaqs) {
                                JSONObject faqJson = new JSONObject();
                                faqJson.put("faqguid", auditTaskFaq.getRowguid()); // 常见问题标识
                                faqJson.put("question", auditTaskFaq.getQuestion());// 问题
                                faqJson.put("answer", auditTaskFaq.getAnswer());// 回答
                                taskFaqList.add(faqJson);
                            }
                        }
                        dataJson.put("faqs", taskFaqList);
                    }
                    log.info("=======结束调用getTaskBasicInfo接口=======");
                    return JsonUtils.zwdtRestReturn("1", "事项基本信息获取成功", dataJson.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "该事项不存在！", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }

        } catch (Exception e) {
            log.info("=======getTaskInfoByItemid接口参数：params【" + params + "】=======");
            log.info("=======getTaskInfoByItemid异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取taskinfo失败：" + e.getMessage(), "");
        }
    }

    /**
     * 根据taskid获取实施taskguid
     */
    @RequestMapping(value = "/getTaskGuidByTaskid", method = RequestMethod.POST)
    public String getTaskGuidByTaskid(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getTaskGuidByTaskid接口=======");
            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String task_id = obj.getString("task_id");// 收藏记录标识
                JSONObject dataJson = new JSONObject();
                AuditTask aduittask = iAuditTask.getUseTaskAndExtByTaskid(task_id).getResult();
                if (aduittask != null) {
                    dataJson.put("taskguid", aduittask.getRowguid());
                }
                log.info("=======结束调用getTaskGuidByTaskid接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取信息成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }

        } catch (Exception e) {
            log.info("=======getTaskGuidByTaskid接口参数：params【" + params + "】=======");
            log.info("=======getTaskGuidByTaskid异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取taskid失败：" + e.getMessage(), "");
        }
    }

    /**
     * 全文检索热词的接口
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getQwjsHotWordsByCategoryNum", method = RequestMethod.POST)
    public String getQwjsHotWordsByCategoryNum(@RequestBody String params) {
        try {
            log.info("=======开始调用getQwjsHotWordsByCategoryNum接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                String isNeedQwjs = ConfigUtil.getConfigValue("isNeedQwjs");
                if ("0".equals(isNeedQwjs)) {
                    // 1.1、使用全文检索时
                    JSONObject obj = (JSONObject) jsonObject.get("params");
                    // 1.1.1、获取热词个数
                    String count = obj.getString("count");
                    // 1.1.2、获取栏目号
                    String categoryNums = ConfigUtil.getConfigValue("inteligentSearchCategoryNums");
                    // 1.1.3、获取全文检索接口地址
                    String restIpConfig = ConfigUtil.getConfigValue("inteligentSearchRestIpConfig");
                    // 1.1.4、获取全文检索热词
                    String hotWords = iHandleTask.getQwsyHotWordsBycategory(restIpConfig, categoryNums, count)
                            .getResult();
                    // 1.1.5、解析返回的数据
                    String[] resultArray = null;
                    JSONArray jsonArray = JSONObject.parseObject(hotWords).getJSONArray("result");
                    String result = jsonArray.toJSONString();
                    if (StringUtil.isNotBlank(result)) {
                        if (result.startsWith("[")) {
                            result = result.substring(1);
                        }
                        if (result.endsWith("]")) {
                            result = result.substring(0, result.length() - 1);
                        }
                        resultArray = result.split(",");
                    }
                    // 1.1.6、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("resultarray", resultArray);
                    log.info("=======结束调用getQwjsHotWordsByCategoryNum接口=======");
                    return JsonUtils.zwdtRestReturn("1", "热词返回成功！", dataJson.toString());
                } else {
                    // 1.2、没有使用全文检索
                    String[] resultArray = new String[0];
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("resultarray", resultArray);
                    log.info("=======结束调用getQwjsHotWordsByCategoryNum接口=======");
                    return JsonUtils.zwdtRestReturn("1", "热词返回成功！", dataJson.toString());
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getQwjsHotWordsByCategoryNum接口参数：params【" + params + "】=======");
            log.info("=======getQwjsHotWordsByCategoryNum异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "热词返回失败：" + e.getMessage(), "");
        }
    }

    /**
     * 根据区域标签获取区域编码
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getAreacodeByVname", method = RequestMethod.POST)
    public String getAreacodeByVname(@RequestBody String params) {
        try {
            log.info("=======开始调用getAreacodeByVname接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取区域标签（区域表中的IndividuationFold字段）
                String vname = obj.getString("vname");//
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.eq("individuationfold", vname);
                // 2、根据区域标签获取区域信息
                AuditOrgaArea auditOrgaArea = iAuditOrgaArea.getAuditArea(sqlConditionUtil.getMap()).getResult();
                String areaCode = auditOrgaArea == null ? "" : auditOrgaArea.getXiaqucode();
                JSONObject dataJson = new JSONObject();
                dataJson.put("areacode", areaCode);
                log.info("=======结束调用getAreacodeByVname接口=======");
                return JsonUtils.zwdtRestReturn("1", "将vname转换成areacode成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getAreacodeByVname接口参数：params【" + params + "】=======");
            log.info("=======getAreacodeByVname异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "将vname转换成areacode失败：" + e.getMessage(), "");
        }
    }

    /**
     * 查询区域下事项的接口（AI客服用）
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getAllTaskByCondition", method = RequestMethod.POST)
    public String getAllTaskByCondition(@RequestBody String params) {
        try {
            log.info("=======开始调用getAllTaskByCondition接口=======");
            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取输入的查询条件
                String keyWord = obj.getString("searchcondition");
                // 1.2、获取区域编码
                String areaCode = obj.getString("areacode");
                // 2、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                // 3、判断是否使用全文检索（0：查询全文检索 1：查询数据库）
                String isNeedQwjs = ConfigUtil.getConfigValue("isNeedQwjs");
                if ("1".equals(isNeedQwjs)) {
                    // 2022/06/19 阳佳 先查询audit_task_keyword
                    List<String> list = iAuditTaskKeywordService.findListByKeyWord(keyWord, areaCode);
                    List<AuditTask> auditTasks = new ArrayList<>();
                    if (ValidateUtil.isNotBlankCollection(list)) {
                        // 根据taskid找到最新的事项
                        SqlConditionUtil sqlUtils = new SqlConditionUtil();
                        sqlUtils.eq("is_enable", ZwfwConstant.CONSTANT_STR_ONE);
                        sqlUtils.eq("is_editafterimport", ZwfwConstant.CONSTANT_STR_ONE);
                        sqlUtils.isBlankOrValue("is_history", ZwfwConstant.CONSTANT_STR_ZERO);
                        sqlUtils.in("task_id", StringUtil.joinSql(list));
                        auditTasks = iAuditTask.getAllTask(sqlUtils.getMap()).getResult();
                    }
                    // 3.1、查询数据库方式
                    // 3.1.1、根据查询条件获取该区域下的事项信息
                    if (ValidateUtil.isBlankCollection(auditTasks)) {
                        auditTasks = iAuditTask.getAllUsableTaskByMap(keyWord, areaCode).getResult();
                    }
                    int totalCount = auditTasks.size();
                    List<JSONObject> taskJsonList = new ArrayList<JSONObject>();
                    for (AuditTask auditTask : auditTasks) {
                        JSONObject taskJson = new JSONObject();
                        taskJson.put("taskname", auditTask.getTaskname());// 事项名称
                        taskJson.put("taskguid", auditTask.getRowguid());// 事项标识
                        taskJson.put("ouname", auditTask.getOuname());// 部门名称
                        taskJson.put("applyertype", auditTask.getApplyertype());// 事项类别，10：法人，20：个人
                        taskJsonList.add(taskJson);
                    }
                    // 3.1.2、根据查询条件获取该区域下的套餐信息
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    List<AuditSpBusiness> auditSpBusinesses = new ArrayList<>();
                    // 2022/06/20 阳佳 先查询audit_task_keyword表
                    if (ValidateUtil.isNotBlankCollection(list)) {
                        sqlConditionUtil.in("rowguid", StringUtil.joinSql(list));
                        auditSpBusinesses = iAuditSpBusiness.getAllAuditSpBusiness(sqlConditionUtil.getMap())
                                .getResult();
                    }
                    if (ValidateUtil.isBlankCollection(auditSpBusinesses)) {
                        sqlConditionUtil.eq("businesstype", "2"); // 一般并联审批(套餐)
                        sqlConditionUtil.eq("del", "0"); // 非禁用
                        sqlConditionUtil.eq("areacode", areaCode);
                        sqlConditionUtil.like("businessname", keyWord);
                        auditSpBusinesses = iAuditSpBusiness.getAllAuditSpBusiness(sqlConditionUtil.getMap())
                                .getResult();
                    }
                    List<JSONObject> businessJsonList = new ArrayList<JSONObject>();
                    totalCount = totalCount + auditSpBusinesses.size();
                    for (AuditSpBusiness auditSpBusiness : auditSpBusinesses) {
                        JSONObject businessJson = new JSONObject();
                        businessJson.put("businessname", auditSpBusiness.getBusinessname());
                        businessJson.put("businessguid", auditSpBusiness.getRowguid());
                        businessJsonList.add(businessJson);
                    }
                    dataJson.put("tasklist", taskJsonList);
                    dataJson.put("businesslist", businessJsonList);
                    dataJson.put("totalcount", totalCount);
                } else {
                    // 3.2、全文检索模式
                    // 3.2.1、检索事项
                    String searchWordsRangsTask = "taskname";// 搜索关键字的范围：事项名称
                    List<SearchCondition> searchConditionList = new ArrayList<SearchCondition>();// 额外的拼接的条件
                    if (StringUtil.isNotBlank(areaCode)) {
                        SearchCondition searchCondition = new SearchCondition();
                        searchCondition.setFieldName("areacode");
                        searchCondition.setEqual(areaCode);
                        searchConditionList.add(searchCondition);
                    }
                    // 3.2.1.1、获取系统参数中配置的事项分类号
                    String categoryNumsTask = iHandleConfig.getFrameConfig("categoryNum_Task", "").getResult();
                    // 3.2.1.2、获取全文检索的接口地址
                    String restIpConfig = ConfigUtil.getConfigValue("inteligentSearchRestIpConfig");
                    // 3.2.1.3、获取全文检索的查询结果
                    String auditTasks = iHandleTask.AIgetData(restIpConfig, categoryNumsTask, keyWord,
                            searchConditionList, searchWordsRangsTask).getResult();
                    // 3.2.1.4、先判断返回的数据是不是json格式
                    if (isGoodJson(auditTasks)) {
                        // 3.2.1.5、解析全文检索返回的事项数据
                        JSONObject taskJsonResult = JSONObject.parseObject(auditTasks).getJSONObject("result");
                        String taskTotalCount = String.valueOf(taskJsonResult.get("totalcount"));// 总记录数
                        int taskCount = Integer.parseInt(taskTotalCount);
                        List<JSONObject> taskJsonList = new ArrayList<JSONObject>();
                        if (taskCount > 0) {
                            JSONArray jsonArray = (JSONArray) taskJsonResult.get("records");
                            int size = jsonArray.size();
                            for (int i = 0; i < size; i++) {
                                JSONObject data = (JSONObject) jsonArray.get(i);
                                JSONObject taskJson = new JSONObject();
                                taskJson.put("taskname", data.getString("taskname"));// 事项名称
                                taskJson.put("taskguid", data.getString("taskguid"));// 事项标识
                                taskJson.put("applyertype", data.getString("applyertype"));// 事项类别，10：法人，20：个人
                                taskJsonList.add(taskJson);
                            }
                        }
                        // 3.2.2、检索套餐
                        String searchWordsRangsBusiness = "businessname";// 搜索关键字的范围：主题名称
                        // 3.2.2.1、获取系统参数中配置的套餐分类号
                        String categoryNumsBusiness = iHandleConfig.getFrameConfig("categoryNum_Business", "")
                                .getResult();
                        // 3.2.2.2、解析全文检索返回的套餐数据
                        String auditBusinesses = iHandleTask.AIgetData(restIpConfig, categoryNumsBusiness, keyWord,
                                searchConditionList, searchWordsRangsBusiness).getResult();
                        JSONObject businessJsonResult = JSONObject.parseObject(auditBusinesses).getJSONObject("result");
                        String businessTotalCount = String.valueOf(businessJsonResult.get("totalcount"));// 总记录数
                        int businessCount = Integer.parseInt(businessTotalCount);
                        List<JSONObject> businessJsonList = new ArrayList<JSONObject>();
                        if (businessCount > 0) {
                            JSONArray jsonArray = (JSONArray) businessJsonResult.get("records");
                            int size = jsonArray.size();
                            for (int i = 0; i < size; i++) {
                                JSONObject data = (JSONObject) jsonArray.get(i);
                                JSONObject businessJson = new JSONObject();
                                businessJson.put("businessname", data.getString("businessname"));// 事项名称
                                businessJson.put("businessguid", data.getString("businessguid"));// 事项标识
                                businessJsonList.add(businessJson);
                            }
                        }
                        dataJson.put("tasklist", taskJsonList);
                        dataJson.put("businesslist", businessJsonList);
                        dataJson.put("totalcount", taskCount + businessCount);
                    } else {
                        // 返回错误判断
                        String message = "";
                        if ("time connected out".equals(auditTasks)) {
                            message = "全文检索服务连接超时，请检查相应配置！";
                        } else {
                            message = auditTasks;
                        }
                        return JsonUtils.zwdtRestReturn("0", message, "");
                    }
                }
                log.info("=======结束调用getAllTaskByCondition接口=======");
                return JsonUtils.zwdtRestReturn("1", "通过条件获取事项成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getAllTaskByCondition接口参数：params【" + params + "】=======");
            log.info("=======getAllTaskByCondition异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "通过条件获取事项获取失败：" + e.getMessage(), "");
        }
    }

    /**
     * 根据搜索条件获取事项信息的接口（个人办事\法人办事搜索不展示大小项、头部搜索框）
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getTaskListByCondition", method = RequestMethod.POST)
    public String getTaskListByCondition(@RequestBody String params) {
        try {
            log.info("=======开始调用getTaskListByCondition接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取事项当前页码
                String currentPageTask = obj.getString("currentpage");
                // 1.2、获取事项每页数目
                String pageSizeTask = obj.getString("pagesize");
                // 1.3、获取搜索条件
                String keyWord = obj.getString("searchcondition");
                // 1.4、获取是否在线办理
                String onlineHandle = obj.getString("onlinehandle");
                // 1.5、获取区域编码
                String areaCode = obj.getString("areacode");
                // 1.6、获取主题分类标识
                String dictId = obj.getString("dictid");
                // 1.7、获取部门标识
                String ouGuid = obj.getString("ouguid");
                // 1.8、获取申请人类型
                String applyerType = obj.getString("applyertype");
                // 1.9、获取主题当前页码
                String currentPageBusiness = obj.getString("currentpagebusiness");
                // 1.10、获取主题每页数目
                String pageSizeBusiness = obj.getString("pagesizebusiness");

                // 2、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                // 3、判断是否使用全文检索（0：查询全文检索 1：查询数据库）
                String isNeedQwjs = ConfigUtil.getConfigValue("isNeedQwjs");
                if ("1".equals(isNeedQwjs)) {
                    // 3.1、查询数据库方式
                    // 3.1.1、根据查询条件获取该区域下的事项信息
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    // 3.1.1.1、拼接事项查询条件
                    sqlConditionUtil.eq("iszwmhwz", "1");// 外网门户网站查询的条件
                    if (StringUtil.isNotBlank(areaCode)) {
                        sqlConditionUtil.eq("areacode", areaCode);// 区域编码
                    }
                    if (StringUtil.isNotBlank(ouGuid)) {
                        sqlConditionUtil.eq("ouguid", ouGuid);// 部门标识
                    }
                    if (StringUtil.isNotBlank(applyerType)) {
                        sqlConditionUtil.like("applyertype", applyerType);// 申请人类型
                    }
                    if (StringUtil.isNotBlank(dictId)) {
                        sqlConditionUtil.eq("dict_id", dictId);// 主题分类标识
                    }
                    if (StringUtil.isNotBlank(keyWord)) {
                        sqlConditionUtil.like("taskname", keyWord);// 事项名称
                    }
                    /*
                     * if (ZwfwConstant.CONSTANT_STR_ONE.equals(onlineHandle)) {
                     * sqlConditionUtil.in("b.webapplytype",
                     * ZwfwConstant.WEB_APPLY_TYPE_YS + "," +
                     * ZwfwConstant.WEB_APPLY_TYPE_SL); // 是否在线办理 }
                     */
                    // sqlConditionUtil.setSelectFields("a.*");
                    // sqlConditionUtil.setLeftJoinTable("Audit_Task_Extension
                    // b", "a.rowguid", "b.taskguid");
                    sqlConditionUtil.eq("ISTEMPLATE", "0"); // 非模板事项
                    sqlConditionUtil.eq("IS_EDITAFTERIMPORT", "1");// 审核通过后事项
                    sqlConditionUtil.eq("IS_ENABLE", "1");// 启用事项
                    // sqlConditionUtil.eq("IF_JZ_HALL","1");
                    // sqlConditionUtil.eq("b.IF_JZ_HALL", "1");

                    int firstResultTask = Integer.parseInt(currentPageTask) * Integer.parseInt(pageSizeTask);
                    // 3.1.1.2、获取分页的事项信息
                    PageData<AuditTask> pageDataTask = iAuditTask.getAuditTaskPageData(sqlConditionUtil.getMap(),
                            firstResultTask, Integer.parseInt(pageSizeTask), null, null).getResult();
                    int totalTaskCount = pageDataTask.getRowCount();// 查询事项的数量
                    List<AuditTask> auditTasks = pageDataTask.getList();// 查询事项的信息
                    List<JSONObject> taskJsonList = new ArrayList<JSONObject>(); // 返回的事项JSON列表
                    for (AuditTask auditTask : auditTasks) {
                        // 3.1.1.3、获取事项基本信息拼接成返回的JSON
                        JSONObject taskJson = new JSONObject();
                        AuditTaskExtension auditTaskExtension = iAuditTaskExtension
                                .getTaskExtensionByTaskGuid(auditTask.getRowguid(), true).getResult();
                        taskJson.put("taskname", auditTask.getTaskname());// 事项名称
                        taskJson.put("taskguid", auditTask.getRowguid());// 事项标识
                        taskJson.put("ouname", auditTask.getOuname());// 部门名称
                        String timeAndAddress = (StringUtil.isBlank(auditTask.getTransact_addr()) ? ""
                                : auditTask.getTransact_addr())
                                + (StringUtil.isBlank(auditTask.getTransact_time()) ? ""
                                : auditTask.getTransact_time()); // 办理地点和办理时间
                        String byLaw = StringUtil.isBlank(auditTask.getBy_law()) ? "" : auditTask.getBy_law();
                        taskJson.put("addressandtime", timeAndAddress + byLaw);// 办理地点和办理时间+法律依据
                        taskJson.put("itemid", auditTask.getItem_id());// 事项编码
                        taskJson.put("applyertype", auditTask.getApplyertype());// 申请人类型
                        String qlKind = auditTask.getShenpilb();
                        if (StringUtil.isNotBlank(qlKind)) {
                            qlKind = "行政" + iCodeItemsService.getItemTextByCodeName("审批类别", qlKind);
                        }
                        taskJson.put("qlkind", qlKind);// 事项权力类型
                        String useLevel = auditTaskExtension.getUse_level();// 行使层级
                        String exerciseClass = "";
                        if (StringUtil.isNotBlank(useLevel)) {
                            String[] userlevelarr = useLevel.split(";");
                            for (int i = 0; i < userlevelarr.length; i++) {
                                exerciseClass = iCodeItemsService.getItemTextByCodeName("行使层级", userlevelarr[i]) + ";";
                            }
                            exerciseClass = exerciseClass.substring(0, exerciseClass.length() - 1);
                        }
                        String if_jz_hall = auditTaskExtension.getIf_jz_hall();
                        taskJson.put("if_jz_hall",
                                StringUtil.isNotBlank(if_jz_hall) ? if_jz_hall : ZwfwConstant.CONSTANT_STR_ZERO);
                        taskJson.put("exerciseclass", exerciseClass);
                        String SERVICEOBJECT = auditTask.getStr("SERVICE_OBJECT");
                        String managementObj = "";
                        if (StringUtil.isNotBlank(SERVICEOBJECT)) {
                            String[] applyertypearr = SERVICEOBJECT.split(";");
                            for (int i = 0; i < applyertypearr.length; ++i) {
                                managementObj = this.iCodeItemsService.getItemTextByCodeName("服务对象", applyertypearr[i])
                                        + ";";
                            }
                            managementObj = managementObj.substring(0, managementObj.length() - 1);
                        }
                        taskJson.put("managementobj", managementObj);
                        taskJson.put("type",
                                iCodeItemsService.getItemTextByCodeName("办件类型", String.valueOf(auditTask.getType())));// 办件类型
                        Integer anticipateday = auditTask.getAnticipate_day();
                        if (anticipateday == 500) {
                            taskJson.put("anticipateday", "无");// 法定期限
                        } else {
                            taskJson.put("anticipateday", auditTask.getAnticipate_day() + "工作日");// 法定期限
                        }
                        taskJson.put("promiseday",
                                auditTask.getPromise_day() == null ? "" : auditTask.getPromise_day() + "工作日");// 承诺期限
                        taskJson.put("linktel", auditTask.getLink_tel());// 咨询电话
                        taskJson.put("supervisetel", auditTask.getSupervise_tel());// 监督投诉电话
                        taskJsonList.add(taskJson);
                    }
                    dataJson.put("tasklist", taskJsonList);
                    dataJson.put("totalcount", totalTaskCount);
                    // 3.1.2、根据查询条件获取该区域下的套餐信息(头部搜索框)
                    if (StringUtil.isNotBlank(currentPageBusiness) && StringUtil.isNotBlank(pageSizeBusiness)) {
                        SqlConditionUtil sqlConditionUtilBusiness = new SqlConditionUtil();
                        sqlConditionUtilBusiness.eq("businesstype", "2"); // 一般并联审批（套餐）
                        sqlConditionUtilBusiness.eq("del", "0"); // 非禁用的套餐
                        if (StringUtil.isNotBlank(areaCode)) {
                            sqlConditionUtilBusiness.eq("areacode", areaCode);
                        }
                        if (StringUtil.isNotBlank(keyWord)) {
                            sqlConditionUtilBusiness.like("businessname", keyWord);
                        }
                        int firstResultBusiness = Integer.parseInt(currentPageBusiness)
                                * Integer.parseInt(pageSizeBusiness);
                        // 3.1.2.1、获取分页的套餐信息
                        PageData<AuditSpBusiness> auditSpBusinessPageData = iAuditSpBusiness
                                .getAuditSpBusinessByPage(sqlConditionUtilBusiness.getMap(), firstResultBusiness,
                                        Integer.parseInt(pageSizeBusiness), "ordernumber", "desc")
                                .getResult();
                        int totalBusinessCount = auditSpBusinessPageData.getRowCount();// 查询套餐的数量
                        List<AuditSpBusiness> auditSpBusinesses = auditSpBusinessPageData.getList();// 查询套餐的信息
                        List<JSONObject> businessJsonList = new ArrayList<JSONObject>();
                        for (AuditSpBusiness auditSpBusiness : auditSpBusinesses) {
                            JSONObject businessJson = new JSONObject();
                            businessJson.put("businessname", auditSpBusiness.getBusinessname()); // 套餐名称
                            businessJson.put("businessguid", auditSpBusiness.getRowguid()); // 套餐标识
                            businessJson.put("promiseday", (auditSpBusiness.getPromise_day() == null ? "0"
                                    : auditSpBusiness.getPromise_day().toString()) + "工作日"); // 承诺期限
                            businessJson.put("linktel", auditSpBusiness.getLink_tel()); // 联系电话
                            businessJson.put("supervise_tel", auditSpBusiness.getSupervise_tel()); // 监督电话
                            businessJson.put("transact_time", auditSpBusiness.getTransact_time()); // 办理时间
                            businessJson.put("transact_addr", auditSpBusiness.getTransact_addr()); // 办理地点
                            businessJson.put("note", auditSpBusiness.getNote()); // 备注信息
                            businessJson.put("daoxcnum",
                                    iCodeItemsService.getItemTextByCodeName("现场次数",
                                            StringUtil.isBlank(auditSpBusiness.getDao_xc_num()) ? ""
                                                    : auditSpBusiness.getDao_xc_num()));// 到现场次数
                            businessJsonList.add(businessJson);
                        }
                        dataJson.put("businesslist", businessJsonList);
                        dataJson.put("businesstotalcount", totalBusinessCount);
                    }

                } else {
                    // 3.2、查询全文检索方式
                    // 3.2.1、检索事项
                    String searchWordsRangsTask = "taskname";// 搜索关键字的范围：事项名称
                    List<SearchCondition> searchConditionTask = new ArrayList<SearchCondition>();
                    List<SearchUnionCondition> searchUnionConditionTask = new ArrayList<SearchUnionCondition>();

                    // 3.2.1.2、拼接事项相关的查询条件
                    if (ZwfwConstant.CONSTANT_STR_ONE.equals(onlineHandle)) {
                        // 3.2.1.2.1、事项是否在线办理
                        SearchCondition searchCondition = new SearchCondition();
                        searchCondition.setFieldName("webapplytype");
                        searchCondition.setEqual(onlineHandle);
                        searchCondition.setIsLike(true);
                        searchConditionTask.add(searchCondition);
                    }
                    if (StringUtil.isNotBlank(applyerType)) {
                        // 3.2.1.2.2、事项申请人类型
                        SearchCondition searchCondition = new SearchCondition();
                        searchCondition.setFieldName("applytype");
                        searchCondition.setEqual(applyerType);
                        searchCondition.setIsLike(true);
                        searchConditionTask.add(searchCondition);
                    }
                    if (StringUtil.isNotBlank(ouGuid)) {
                        // 3.2.1.2.3、事项部门标识
                        SearchCondition searchCondition = new SearchCondition();
                        searchCondition.setFieldName("ouguid");
                        searchCondition.setEqual(ouGuid);
                        searchConditionTask.add(searchCondition);
                    }
                    if (StringUtil.isNotBlank(dictId)) {
                        // 3.2.1.2.4、事项的主题分类标识
                        SearchCondition searchCondition = new SearchCondition();
                        searchCondition.setFieldName("taskdic");
                        searchCondition.setEqual(dictId);
                        searchConditionTask.add(searchCondition);
                    }
                    if (StringUtil.isNotBlank(areaCode)) {
                        // 3.2.1.2.5、事项的区域编码
                        SearchCondition searchCondition = new SearchCondition();
                        searchCondition.setFieldName("areacode");
                        searchCondition.setEqual(areaCode);
                        searchConditionTask.add(searchCondition);
                    }
                    // 3.2.1.3、获取系统参数中配置的事项分类号
                    String categoryNumsTask = iHandleConfig.getFrameConfig("categoryNum_Task", "").getResult();
                    // 3.2.1.4、获取全文检索接口地址
                    String restIpConfig = ConfigUtil.getConfigValue("inteligentSearchRestIpConfig");
                    String auditTasks = iHandleTask.getData(restIpConfig, categoryNumsTask, keyWord,
                            searchConditionTask, searchUnionConditionTask, searchWordsRangsTask,
                            String.valueOf(Integer.parseInt(currentPageTask) * Integer.parseInt(pageSizeTask)),
                            pageSizeTask).getResult();
                    // 3.2.1.5、解析返回的事项数据
                    JSONObject taskJsonResult = JSONObject.parseObject(auditTasks).getJSONObject("result");
                    String taskTotalCount = String.valueOf(taskJsonResult.get("totalcount"));// 总记录数
                    int taskCount = Integer.parseInt(taskTotalCount);
                    List<JSONObject> taskJsonList = new ArrayList<JSONObject>();
                    if (taskCount > 0) {
                        JSONArray jsonArray = (JSONArray) taskJsonResult.get("records");
                        int size = jsonArray.size();
                        for (int i = 0; i < size; i++) {
                            JSONObject data = (JSONObject) jsonArray.get(i);
                            JSONObject taskJson = new JSONObject();
                            taskJson.put("taskname", data.getString("taskname"));// 事项名称
                            taskJson.put("taskguid", data.getString("taskguid"));// 事项标识
                            taskJson.put("ouname", data.getString("ouname"));// 部门名称
                            taskJson.put("addressandtime", data.getString("addressandtime"));// 办理地点和时间,法律依据
                            taskJson.put("addressandtime", "");
                            taskJson.put("itemid", data.getString("itemid"));
                            taskJson.put("qlkind", data.getString("qlkind"));// 权力类型
                            taskJson.put("exerciseclass", data.getString("exerciseclass"));// 行使层级
                            taskJson.put("managementobj", data.getString("managementobj"));// 办理对象
                            taskJson.put("type", data.getString("type"));// 办理类型
                            taskJson.put("anticipateday", data.getString("anticipateday") == null ? ""
                                    : data.getString("anticipateday") + "工作日");// 法定期限
                            taskJson.put("promiseday", data.getString("promiseday") + "工作日");// 承诺期限
                            taskJson.put("linktel", data.getString("linktel"));// 咨询电话
                            taskJson.put("supervisetel", data.getString("supervisetel"));// 监督投诉电话
                            taskJsonList.add(taskJson);
                        }
                    }
                    dataJson.put("tasklist", taskJsonList);
                    dataJson.put("totalcount", taskCount);
                    // 3.2.2、检索套餐数据(顶部搜索框)
                    if (StringUtil.isNotBlank(currentPageBusiness) && StringUtil.isNotBlank(pageSizeBusiness)) {
                        String searchWordsRangsBusiness = "businessname";// 搜索关键字的范围
                        // 3.2.2.1、拼接套餐相关的查询条件
                        List<SearchCondition> searchConditionBusinesses = new ArrayList<SearchCondition>();// 额外的拼接的条件
                        if (StringUtil.isNotBlank(areaCode)) {
                            SearchCondition searchCondition = new SearchCondition();
                            searchCondition.setFieldName("areacode");
                            searchCondition.setEqual(areaCode);
                            searchConditionBusinesses.add(searchCondition);
                        }
                        // 3.2.2.2、获取系统参数中配置的套餐分类号
                        String categoryNumsBusiness = iHandleConfig.getFrameConfig("categoryNum_Business", "")
                                .getResult();
                        // 3.2.2.3、获取全文检索查询的套餐结果信息
                        String auditBusinesses = iHandleTask.getData(restIpConfig, categoryNumsBusiness, keyWord,
                                searchConditionBusinesses, searchUnionConditionTask, searchWordsRangsBusiness,
                                String.valueOf(
                                        Integer.parseInt(currentPageBusiness) * Integer.parseInt(pageSizeBusiness)),
                                pageSizeBusiness).getResult();
                        // 3.2.2.4、解析返回的套餐数据
                        JSONObject businessJsonResult = JSONObject.parseObject(auditBusinesses).getJSONObject("result");
                        String businessTotalCount = String.valueOf(businessJsonResult.get("totalcount"));// 总记录数
                        int businessCount = Integer.parseInt(businessTotalCount);
                        List<JSONObject> businessList = new ArrayList<JSONObject>();
                        if (businessCount > 0) {
                            JSONArray jsonArray = (JSONArray) businessJsonResult.get("records");
                            int size = jsonArray.size();
                            for (int i = 0; i < size; i++) {
                                JSONObject data = (JSONObject) jsonArray.get(i);
                                JSONObject businessJson = new JSONObject();
                                businessJson.put("businessname", data.getString("businessname"));// 主题名称
                                businessJson.put("businessguid", data.getString("businessguid"));// 主题标识
                                businessJson.put("promiseday", (StringUtil.isBlank(data.getString("promiseday")) ? "0"
                                        : data.getString("promiseday").toString()) + "工作日");// 承诺时限
                                businessJson.put("linktel", data.getString("linktel"));// 联系电话
                                businessJson.put("supervise_tel", data.getString("supervisetel"));// 监督电话
                                businessJson.put("transact_time", data.getString("transacttime"));// 办理时间
                                businessJson.put("transact_addr", data.getString("transact_addr"));// 办理地点
                                businessJson.put("note", data.getString("note"));// 备注
                                businessJson.put("daoxcnum",
                                        iCodeItemsService.getItemTextByCodeName("现场次数", data.getString("daoxcnum")));// 到现场次数
                                businessList.add(businessJson);
                            }
                        }
                        dataJson.put("businesslist", businessList);
                        dataJson.put("businesstotalcount", businessCount);
                    }
                }
                log.info("=======结束调用getTaskListByCondition接口=======");
                return JsonUtils.zwdtRestReturn("1", "通过条件获取事项成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getTaskListByCondition接口参数：params【" + params + "】=======");
            log.info("=======getTaskListByCondition异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "通过条件获取事项获取失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取事项列表的接口（主题分类/部门分类可展示大小项、非查询）
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getTaskList", method = RequestMethod.POST)
    public String getTaskList(@RequestBody String params) {
        try {
            log.info("=======开始调用getTaskList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取当前页码
                String currentPage = obj.getString("currentpage");
                // 1.2、获取每页数目
                String pageSize = obj.getString("pagesize");
                // 1.3、获取部门标识
                String ouGuid = obj.getString("ouguid");
                // 1.4、获取主题分类标识
                String dictId = obj.getString("dictid");
                // 1.5、获取申请人类型
                String applyerType = obj.getString("usertype");
                // 1.6、获取搜索内容
                String keyWord = obj.getString("searchcondition");
                // 1.7、获取是否在线办理
                String onlineHandle = obj.getString("onlinehandle");
                // 1.8、获取区域编码
                String areaCode = obj.getString("areacode");
                // 2、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                // 3、判断是否使用全文检索（0：查询全文检索 1：查询数据库）
                String isNeedQwjs = ConfigUtil.getConfigValue("isNeedQwjs");
                if ("1".equals(isNeedQwjs)) {
                    // 3.1、查询数据库方式
                    // 3.1.1、根据查询条件获取该区域下的事项信息
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    // 3.1.1.1、拼接事项查询条件
                    sqlConditionUtil.eq("iszwmhwz", "1");// 外网门户网站查询的条件
                    if (StringUtil.isNotBlank(areaCode)) {
                        sqlConditionUtil.eq("areacode", areaCode);// 区域编码
                    }
                    if (ZwfwConstant.CONSTANT_STR_ONE.equals(onlineHandle)) {
                        sqlConditionUtil.in("webapplytype",
                                ZwfwConstant.WEB_APPLY_TYPE_YS + "," + ZwfwConstant.WEB_APPLY_TYPE_SL); // 是否在线办理
                    }
                    if (StringUtil.isNotBlank(keyWord)) {
                        sqlConditionUtil.like("taskname", keyWord);// 事项名称
                    }
                    if (StringUtil.isNotBlank(applyerType)) {
                        sqlConditionUtil.like("applyertype", applyerType);// 申请人类型
                    }
                    if (StringUtil.isNotBlank(ouGuid)) {
                        sqlConditionUtil.eq("ouguid", ouGuid);// 部门标识
                    }
                    if (StringUtil.isNotBlank(dictId)) {
                        sqlConditionUtil.eq("dict_id", dictId);// 主题分类标识
                    }
                    sqlConditionUtil.eq("ISTEMPLATE", "0"); // 非模板事项
                    sqlConditionUtil.eq("IS_EDITAFTERIMPORT", "1");// 审核通过后事项
                    sqlConditionUtil.eq("IS_ENABLE", "1");// 启用事项
                    // 3.1.1.2、获取主项事项
                    int firstResultTask = Integer.parseInt(currentPage) * Integer.parseInt(pageSize);
                    PageData<AuditTask> fatherTaskPageData = iHandleTask.getAuditTaskListByCondition(true, null,
                                    sqlConditionUtil.getMap(), firstResultTask, Integer.parseInt(pageSize), "ordernum", "desc")
                            .getResult();
                    List<AuditTask> fatherAuditTaskList = fatherTaskPageData.getList();
                    int totalCount = fatherTaskPageData.getRowCount();
                    List<JSONObject> fatherTaskJsonList = new ArrayList<JSONObject>();
                    for (AuditTask fatherAuditTask : fatherAuditTaskList) {
                        JSONObject fatherTaskJson = new JSONObject();
                        fatherTaskJson.put("taskname", fatherAuditTask.getTaskname());// 事项名称
                        fatherTaskJson.put("taskguid", fatherAuditTask.getRowguid());// 事项标识
                        fatherTaskJson.put("ouname", fatherAuditTask.getOuname());// 部门名称
                        // 3.1.1.2.1、获取子项事项
                        PageData<AuditTask> childTaskPageData = iHandleTask.getAuditTaskListByCondition(false,
                                        fatherAuditTask.getItem_id(), sqlConditionUtil.getMap(), 0, 50, "ordernum", "desc")
                                .getResult();
                        List<JSONObject> childTaskJsonList = new ArrayList<JSONObject>();
                        if (childTaskPageData != null) {
                            for (AuditTask childAuditTask : childTaskPageData.getList()) {
                                JSONObject childTaskJson = new JSONObject();
                                childTaskJson.put("taskname", childAuditTask.getTaskname());// 事项名称
                                childTaskJson.put("taskguid", childAuditTask.getRowguid());// 事项标识
                                childTaskJson.put("ouname", childAuditTask.getOuname());// 部门名称
                                childTaskJsonList.add(childTaskJson);
                            }
                            fatherTaskJson.put("haschildtask", "1");
                        }
                        fatherTaskJson.put("childtasklist", childTaskJsonList);
                        fatherTaskJson.put("childtaskcount", childTaskJsonList.size());
                        fatherTaskJsonList.add(fatherTaskJson);
                    }
                    dataJson.put("totalcount", totalCount);
                    dataJson.put("tasklist", fatherTaskJsonList);
                } else {
                    // 3.2、查询全文检索方式
                    String searchWords = StringUtil.isBlank(keyWord) ? "" : keyWord;// 搜索的关键字
                    String searchWordsRangs = "taskname";// 搜索关键字的范围
                    List<SearchCondition> searchConditions = new ArrayList<SearchCondition>();
                    // 3.2.1、拼接事项相关的查询条件
                    if (ZwfwConstant.CONSTANT_STR_ONE.equals(onlineHandle)) {
                        // 3.2.1.1、事项是否在线办理
                        SearchCondition searchCondition = new SearchCondition();
                        searchCondition.setFieldName("webapplytype");
                        searchCondition.setEqual(onlineHandle);
                        searchCondition.setIsLike(true);
                        searchConditions.add(searchCondition);
                    }
                    if (StringUtil.isNotBlank(ouGuid)) {
                        // 3.2.1.2、事项部门标识
                        SearchCondition searchCondition = new SearchCondition();
                        searchCondition.setFieldName("ouguid");
                        searchCondition.setEqual(ouGuid);
                        searchConditions.add(searchCondition);
                    }
                    if (StringUtil.isNotBlank(dictId)) {
                        // 3.2.1.3、事项主题分类标识
                        SearchCondition searchCondition = new SearchCondition();
                        searchCondition.setFieldName("taskdic");
                        searchCondition.setEqual(dictId);
                        searchConditions.add(searchCondition);
                    }
                    if (StringUtil.isNotBlank(areaCode)) {
                        // 3.2.1.4、事项主题编码
                        SearchCondition searchCondition = new SearchCondition();
                        searchCondition.setFieldName("areacode");
                        searchCondition.setEqual(areaCode);
                        searchConditions.add(searchCondition);
                    }
                    // 申请人类型
                    if (StringUtil.isNotBlank(applyerType)) {
                        // 3.2.1.5、申请人类型
                        SearchCondition searchCondition = new SearchCondition();
                        searchCondition.setFieldName("applytype");
                        searchCondition.setEqual(applyerType);
                        searchCondition.setIsLike(true);
                        searchConditions.add(searchCondition);
                    }
                    // 3.2.2、获取事项大小项显示方式（0：按编码 1：按right_category配置 2：不区分大小项）
                    String taskCategory = iHandleConfig.getFrameConfig("AS_Task_Category", "").getResult();
                    List<SearchCondition> fatherSearchConditions = searchConditions;
                    List<SearchCondition> childSearchConditions = searchConditions;
                    if ("0".equals(taskCategory)) {
                        // 3.2.2.1、主项事项拼接额外的条件
                        SearchCondition searchCondition = new SearchCondition();
                        searchCondition.setFieldName("isfathertask");
                        searchCondition.setEqual("1");
                        fatherSearchConditions.add(searchCondition);
                    }
                    // 3.2.2.2、获取系统参数中配置的事项分类号
                    String categoryNumsTask = iHandleConfig.getFrameConfig("categoryNum_Task", "").getResult();
                    // 3.2.2.3、获取全文检索的接口地址
                    String restIpConfig = ConfigUtil.getConfigValue("inteligentSearchRestIpConfig");
                    // 3.2.2.4、获取主项事项信息
                    String fatherAudittTasks = iHandleTask.getDataByExtraCondition(restIpConfig, categoryNumsTask,
                                    searchWords, fatherSearchConditions, searchWordsRangs,
                                    String.valueOf(Integer.parseInt(currentPage) * Integer.parseInt(pageSize)), pageSize)
                            .getResult();
                    // 3.2.2.5、解析返回的主项数据
                    JSONObject taskJsonResult = JSONObject.parseObject(fatherAudittTasks).getJSONObject("result");
                    String taskTotalCount = String.valueOf(taskJsonResult.get("totalcount"));// 总记录数
                    int taskCount = Integer.parseInt(taskTotalCount);
                    List<JSONObject> taskJsonList = new ArrayList<JSONObject>();
                    if (taskCount > 0) {
                        JSONArray jsonArray = (JSONArray) taskJsonResult.get("records");
                        int size = jsonArray.size();
                        for (int i = 0; i < size; i++) {
                            JSONObject data = (JSONObject) jsonArray.get(i);
                            String itemId = data.getString("itemid");
                            JSONObject taskJson = new JSONObject();
                            taskJson.put("taskname", data.getString("taskname"));// 事项名称
                            taskJson.put("taskguid", data.getString("taskguid"));// 事项标识
                            taskJson.put("ouname", data.getString("ouname"));// 部门名称
                            if ("0".equals(taskCategory)) {
                                // 3.2.2.6、如果需要区分大小项，则给子项拼接额外的条件
                                if (StringUtil.isNotBlank(itemId)) {
                                    SearchCondition searchCondition1 = new SearchCondition();
                                    searchCondition1.setFieldName("fatheritem");
                                    searchCondition1.setEqual(itemId);
                                    childSearchConditions.add(searchCondition1);
                                    SearchCondition searchCondition2 = new SearchCondition();
                                    searchCondition2.setFieldName("isfathertask");
                                    searchCondition2.setEqual("0");
                                    childSearchConditions.add(searchCondition2);
                                }
                                // 3.2.2.7、获取子事项的信息数据
                                String childAuditTasks = iHandleTask
                                        .getDataByExtraCondition(restIpConfig, categoryNumsTask, searchWords,
                                                childSearchConditions, searchWordsRangs, "0", "50")
                                        .getResult();
                                // 3.2.2.8、解析返回的子事项数据
                                JSONObject chilstaskJsonResult = JSONObject.parseObject(childAuditTasks)
                                        .getJSONObject("result");
                                String childTaskTotalCount = String.valueOf(chilstaskJsonResult.get("totalcount"));// 记录数
                                int childTaskCount = Integer.parseInt(childTaskTotalCount);
                                if (childTaskCount > 0) {
                                    JSONArray childjsonArray = (JSONArray) chilstaskJsonResult.get("records");
                                    int childsize = childjsonArray.size();
                                    List<JSONObject> childTaskList = new ArrayList<JSONObject>();
                                    for (int j = 0; j < childsize; j++) {
                                        JSONObject childData = (JSONObject) childjsonArray.get(j);
                                        JSONObject childTaskJson = new JSONObject();
                                        childTaskJson.put("taskname", childData.getString("taskname"));// 事项名称
                                        childTaskJson.put("taskguid", childData.getString("taskguid"));// 事项标识
                                        childTaskJson.put("ouname", childData.getString("ouname"));// 部门名称
                                        childTaskList.add(childTaskJson);
                                    }
                                    taskJson.put("childtasklist", childTaskList);
                                    taskJson.put("childtaskcount", childTaskList.size());
                                }
                            }
                            taskJsonList.add(taskJson);
                        }
                        dataJson.put("totalcount", taskCount);
                        dataJson.put("tasklist", taskJsonList);
                    }
                }
                log.info("=======结束调用getTaskList接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取事项列表成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getTaskList接口参数：params【" + params + "】=======");
            log.info("=======getTaskList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取事项列表失败：" + e.getMessage(), "");
        }
    }

    /**
     * 热门事项的接口
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getHotTaskList", method = RequestMethod.POST)
    public String getHotTaskList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getHotTaskList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取当前页码
                String currentPage = obj.getString("currentpage");
                // 1.2、获取每页数目
                String pageSize = obj.getString("pagesize");
                // 1.3、获取用户类型
                String applyerType = obj.getString("usertype");
                // 1.4、获取区域编码
                String areaCode = obj.getString("areacode");
                // 2、拼接查询条件
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                if (StringUtil.isNotBlank(areaCode)) {
                    // 2.1、热门事项区域编码
                    sqlConditionUtil.eq("area", areaCode);
                }
                if (StringUtil.isNotBlank(applyerType)) {
                    // 2.1、热门事项申请人类型
                    sqlConditionUtil.like("b.applyertype", applyerType);
                }
                // 3、查询到热门事项的信息(热门事项不考虑大小项筛选，数据筛选由审批系统处理)
                PageData<AuditTaskHottask> pageData = iAuditTaskHottask.getAuditHottaskPageData(
                        sqlConditionUtil.getMap(), Integer.parseInt(currentPage) * Integer.parseInt(pageSize),
                        Integer.parseInt(pageSize), "ordernum", "desc").getResult();
                List<AuditTaskHottask> auditTaskHottasks = pageData.getList();
                int totalCount = pageData.getRowCount();
                // 4、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                List<JSONObject> taskJsonList = new ArrayList<JSONObject>();
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                for (AuditTaskHottask auditTaskHottask : auditTaskHottasks) {
                    AuditTask auditTask = iAuditTask.selectUsableTaskByTaskID(auditTaskHottask.getTaskid()).getResult();
                    AuditTaskExtension auditTaskExtension = iAuditTaskExtension
                            .getTaskExtensionByTaskGuid(auditTask.getRowguid(), true).getResult();
                    JSONObject taskJson = new JSONObject();
                    taskJson.put("taskname", auditTask.getTaskname());// 事项名称
                    taskJson.put("taskguid", auditTask.getRowguid());// 事项标识
                    taskJson.put("taskid", auditTask.getTask_id());// 事项标识
                    taskJson.put("ouname", auditTask.getOuname());// 部门名称
                    taskJson.put("usescope", auditTaskExtension.get("usescope"));// 适用范围
                    String WEBAPPLYDEPTH = "信息发布";
                    // 判断如果用户已收藏该事项后，收藏灯需要点亮
                    if (auditOnlineRegister != null) {
                        String clientIdentifier = auditTask.getTask_id();
                        if (isCollect(clientIdentifier, auditOnlineRegister.getAccountguid())) {
                            taskJson.put("iscollect", "1");// 返回改事项已收藏标识
                        }
                        taskJson.put("islogin", "1");// 是否登录
                    }
                    taskJson.put("applyertype", auditTask.getApplyertype());// 部门名称
                    if (auditTaskExtension != null) {
                        Integer webApplyType = auditTaskExtension.getWebapplytype() == null ? 0
                                : auditTaskExtension.getWebapplytype();
                        String onlineHandle = "0";
                        switch (webApplyType) {
                            case 2:
                                WEBAPPLYDEPTH = "在线核验";
                                break;
                            case 1:
                                WEBAPPLYDEPTH = "在线预审";
                                break;
                            default:
                                WEBAPPLYDEPTH = "信息发布";
                                break;
                        }
                        if (webApplyType != null) {
                            onlineHandle = webApplyType == 0 ? "0" : "1";
                        }
                        String shendu = auditTaskExtension.getStr("wangbanshendu");
                        String businesstype = auditTask.getStr("businesstype");
                        if (StringUtil.isNotBlank(businesstype)) {
                            if (StringUtil.isNotBlank(shendu)) {
                                if (shendu.contains("5") || shendu.contains("6") || shendu.contains("7")) {
                                    WEBAPPLYDEPTH = "全程网办";
                                } else if (shendu.contains("4")) {
                                    WEBAPPLYDEPTH = "在线核验";
                                } else if (shendu.contains("3") || shendu.contains("2")) {
                                    WEBAPPLYDEPTH = "在线预审";
                                } else {
                                    WEBAPPLYDEPTH = "信息发布";
                                }
                            }
                        } else {
                            WEBAPPLYDEPTH = "信息发布";
                        }

                        taskJson.put("onlinehandle", Integer.parseInt(onlineHandle));// 事项是否可以网上申报
                        if (isAppoint(auditTask.getTask_id())) {
                            taskJson.put("appointment", auditTaskExtension.getReservationmanagement());// 事项是否可以网上预约
                        } else {
                            taskJson.put("appointment", "0");
                        }
                    }
                    taskJson.put("depth", WEBAPPLYDEPTH);// 办理深度
                    String daoxcnum = auditTaskExtension == null ? "0" : auditTaskExtension.getDao_xc_num();
                    if (ZwfwConstant.CONSTANT_STR_ONE.equals(daoxcnum)) {
                        taskJson.put("daoxcnum", "1");// 到现场次数
                    } else {
                        taskJson.put("daoxcnum", "0");// 到现场次数
                    }
                    // 获取事项下放信息
                    taskJson.put("town", "0");// 是否下放到乡镇
                    taskJson.put("community", "0");// 是否下放到社区
                    List<AuditTaskDelegate> auditTaskDelegates = iAuditTaskDelegate
                            .selectDelegateByTaskID(auditTask.getTask_id()).getResult();
                    if (auditTaskDelegates != null && auditTaskDelegates.size() > 0) {
                        for (AuditTaskDelegate auditTaskDelegate : auditTaskDelegates) {
                            // 使用乡镇维护的数据
                            if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditTaskDelegate.getStatus())) {
                                String xzAreacode = auditTaskDelegate.getAreacode();
                                if (xzAreacode.length() == 9) {
                                    taskJson.put("town", "1");// 是否下放到乡镇
                                } else if (xzAreacode.length() == 12) {
                                    taskJson.put("community", "1");// 是否下放到社区
                                }
                            }
                        }
                    }
                    taskJsonList.add(taskJson);
                }
                dataJson.put("tasklist", taskJsonList);
                dataJson.put("totalcount", totalCount);
                log.info("=======结束调用getHotTaskList接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取热门事项成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getHotTaskList接口参数：params【" + params + "】=======");
            log.info("=======getHotTaskList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取热门事项失败,详细信息请查看日志", "");
        }
    }

    /**
     * 收藏事项或套餐的接口
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/private/collectionTask", method = RequestMethod.POST)
    public String collectionTask(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用collectionTask接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取套餐标识
                String businessGuid = obj.getString("businessguid");
                // 1.2、获取事项标识
                String taskGuid = obj.getString("taskguid");
                // 1.3、获取收藏的类型（1：事项、2：办件、3：套餐、4：答疑、5攻略、6其他）
                String clientType = obj.getString("clienttype");
                // 1.4、获取用户信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 2、收藏时需要判断是否已经收藏过
                    String clientIdentifier = ""; // 收藏的业务标识
                    String clientTitle = ""; // 收藏标题
                    if (ZwdtConstant.AUDIT_COllECTION_TYPE_TASK.equals(clientType)) {
                        // 2.1、收藏事项
                        AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, true).getResult();
                        clientIdentifier = auditTask.getTask_id();
                        clientTitle = auditTask.getTaskname();
                    } else if (ZwdtConstant.AUDIT_COllECTION_TYPE_BUSINESSTASK.equals(clientType)) {
                        // 2.2、收藏套餐
                        AuditSpBusiness auditSpBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessGuid)
                                .getResult();
                        clientIdentifier = auditSpBusiness.getRowguid();
                        clientTitle = auditSpBusiness.getBusinessname();
                    }
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    sqlConditionUtil.eq("clientidentifier", clientIdentifier);
                    sqlConditionUtil.eq("accountguid", auditOnlineRegister.getAccountguid());
                    String msg = "";
                    int count = iAuditOnlineCollection.getAuditCollectionCount(sqlConditionUtil.getMap()).getResult();
                    if (count > 0) {
                        // 2.3、如果已经收藏则返回信息
                        msg = "已收藏,请勿重复收藏！";
                    } else {
                        // 2.4、如果没有收藏则新增收藏记录
                        AuditOnlineCollection auditOnlineCollection = new AuditOnlineCollection();
                        auditOnlineCollection.setRowguid(UUID.randomUUID().toString());
                        auditOnlineCollection.setClientidentifier(clientIdentifier);
                        auditOnlineCollection.setClienttitle(clientTitle);
                        auditOnlineCollection.setAccountguid(auditOnlineRegister.getAccountguid());
                        auditOnlineCollection.setInserttime(new Date());
                        auditOnlineCollection.setClienttype(clientType);
                        iAuditOnlineCollection.addCollection(auditOnlineCollection);
                        msg = "收藏成功！";
                    }
                    // 3、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("msg", msg);
                    log.info("=======结束调用collectionTask接口=======");
                    return JsonUtils.zwdtRestReturn("1", msg, dataJson.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======collectionTask接口参数：params【" + params + "】=======");
            log.info("=======collectionTask异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "收藏事项失败：" + e.getMessage(), "");
        }
    }

    /**
     * 删除收藏事项的接口
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/private/deleteCollectionTask", method = RequestMethod.POST)
    public String deleteCollectionTask(@RequestBody String params) {
        try {
            log.info("=======开始调用deleteCollectionTask接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取收藏标识
                String collectionGuid = obj.getString("collectionguid");
                // 2、删除收藏记录
                iAuditOnlineCollection.deleteCollectionByRowguid(collectionGuid);
                log.info("=======结束调用deleteCollectionTask接口=======");
                return JsonUtils.zwdtRestReturn("1", "删除收藏事项成功", "");
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======deleteCollectionTask接口参数：params【" + params + "】=======");
            log.info("=======deleteCollectionTask异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "删除收藏事项失败：" + e.getMessage(), "");
        }
    }

    /**
     * 主题分类的接口
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getTaskKindsByThemes", method = RequestMethod.POST)
    public String getTaskKindsByThemes(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getTaskKindsByThemes接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取申请人类型
                String applyerType = obj.getString("usertype");
                // 1.2、获取判断PC端使用还是微信端使用 (0：PC端，1：微信端)
                String isPcUse = obj.getString("ispcuse");
                // 2、获取主题分类
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.eq("LENGTH(no)", "4");
                String basePath = "";
                // 2.1、获取请求地址(http:/IP:端口/应用名称)
                String urlRoot = WebUtil.getRequestCompleteUrl(request);
                if (ZwdtConstant.WXUSERPIC.equals(isPcUse)) {
                    // 2.2、微信端获取微信端图片文件夹地址
                    basePath = urlRoot + "/epointzwmhwz/css/images/wxthemes/";
                    if (ZwfwConstant.APPLAYERTYPE_GR.equals(applyerType)) {
                        sqlConditionUtil.eq("SUBSTRING(no, 1,2)", "B1");
                    } else if (ZwfwConstant.APPLAYERTYPE_QY.equals(applyerType)) {
                        sqlConditionUtil.eq("SUBSTRING(no, 1,2)", "B2");
                    }
                } else {
                    // 2.3、如果是PC端需要获取个人或企业的主题图片地址及
                    if (ZwfwConstant.APPLAYERTYPE_GR.equals(applyerType)) {
                        basePath = urlRoot + "/epointzwmhwz/pages/eventdetail/images/";
                        sqlConditionUtil.eq("SUBSTRING(no, 1,2)", "B1");
                    } else if (ZwfwConstant.APPLAYERTYPE_QY.equals(applyerType)) {
                        basePath = urlRoot + "/epointzwmhwz/pages/legal/images/";
                        sqlConditionUtil.eq("SUBSTRING(no, 1,2)", "B2");
                    }
                }
                // 3、获取主题分类数据
                List<JSONObject> dictJsonList = new ArrayList<JSONObject>();
                List<AuditTaskDict> auditTaskDicts = iAuditTaskDict.getAuditTaskDictList(sqlConditionUtil.getMap())
                        .getResult();
                for (AuditTaskDict auditTaskDict : auditTaskDicts) {
                    JSONObject dictObject = new JSONObject();
                    dictObject.put("dictname", auditTaskDict.getClassname()); // 主题类别名称
                    dictObject.put("dictid", auditTaskDict.getRowguid()); // 主题类别标识
                    dictObject.put("dictno", auditTaskDict.getNo()); // 主题类别编号
                    dictObject.put("taskclass", auditTaskDict.getStr("taskclass")); // 浪潮主题分类
                    dictObject.put("dictrul", basePath + auditTaskDict.getNo() + ".png");// 主题对应的图片
                    dictJsonList.add(dictObject);
                }
                // 4、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("dictlist", dictJsonList);
                return JsonUtils.zwdtRestReturn("1", "主题分类列表获取成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getTaskKindsByThemes接口参数：params【" + params + "】=======");
            log.info("=======getTaskKindsByThemes异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "主题分类列表获取失败：" + e.getMessage(), "");
        }
    }

    /**
     * 部门分类的接口
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getTaskKindsByOu", method = RequestMethod.POST)
    public String getTaskKindsByOu(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getTaskKindsByOu接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取区域编码
                String areaCode = obj.getString("areacode");
                // 1.2、获取申请人类型（10:企业、20：个人）
                String applyerType = obj.getString("usertype");
                // 1.3 济宁复工复产
                String is_fugongfuchan = obj.getString("is_fugongfuchan");
                // 1.4 事项名称
                String taskname = obj.getString("taskname");

                String isyc = obj.getString("isyc");

                String xzql = obj.getString("xzql");

                // 2、获取事项所属的部门
                // 2022/06/20 阳佳 判断关键字是否存在于audit_task_keyword中
                List<String> list = iAuditTaskKeywordService.findListByKeyWord(taskname, areaCode);
                List<Record> records = iJnAppRestService
                        .selectOuByApplyertype(applyerType, is_fugongfuchan, areaCode, taskname, isyc, xzql, list)
                        .getResult();
                List<JSONObject> ouJsonList = new ArrayList<JSONObject>();
                // 3、获取部门图片的地址（微信端用）
                String urlRoot = WebUtil.getRequestCompleteUrl(request) + "/epointzwmhwz/css/images/oupic/";
                for (Record record : records) {
                    // 3.1、取部门扩展表里的areacode
                    boolean flag = false;
                    String ouguid = record.get("ouguid");
                    FrameOuExtendInfo frameOuExtendInfo = ouService.getFrameOuExtendInfo(ouguid);
                    if (frameOuExtendInfo != null) {
                        // 3.1.1、若辖区编码一致，则说明不是乡镇级部门
                        if (areaCode.equals(frameOuExtendInfo.get("areacode"))) {
                            flag = true;
                        }
                    }
                    if (flag) {
                        JSONObject ouObject = new JSONObject();
                        String ouname = record.get("ouname");
                        String ounamePin = "";
                        if (StringUtil.isNotBlank(ouname)) {
                            ounamePin = ChineseToPinyin.getPingYin(ouname); // 将获取的部门汉语名称转换成拼音
                        }
                        ouObject.put("ouname", record.get("OUSHORTNAME"));
                        ouObject.put("ouguid", record.get("ouguid"));
                        ouObject.put("ouurl", urlRoot + ounamePin + ".png");
                        ouJsonList.add(ouObject);
                    }
                }

                // 新增网厅访问量统计
                JnVisitRecord record = new JnVisitRecord();
                record.setOperatedate(new Date());
                record.setRowguid(UUID.randomUUID().toString());
                record.setRecordtotal("1");
                iJnVisitRecordService.insert(record);

                // 4、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("oulist", ouJsonList);
                log.info("=======结束调用getTaskKindsByOu接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取部门列表成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getTaskKindsByOu接口参数：params【" + params + "】=======");
            log.info("=======getTaskKindsByOu异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取部门列表失败：" + e.getMessage(), "");
        }
    }

    /**
     * 部门分类的接口
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getTaskKindsByOuYC", method = RequestMethod.POST)
    public String getTaskKindsByOuYC(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getTaskKindsByOuYC接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取区域编码
                String areaCode = obj.getString("areacode");
                // 1.2、获取申请人类型（10:企业、20：个人）
                String applyerType = obj.getString("usertype");
                // 1.3 到现场次数
                String Dao_xc_num = obj.getString("Dao_xc_num");
                // 1.3 是否一次办好
                String ISPYC = obj.getString("ISPYC");
                // 1.3 是否全市通办
                String Operationscope = obj.getString("Operationscope");
                // 1.3 是否支持物流
                String If_express = obj.getString("If_express");
                // 1.3 是否支持物流
                String mpmb = obj.getString("mpmb");
                // 1.3 是否支持便民服务
                String bianminfuwu = obj.getString("bianminfuwu");
                // 1.3 是否支持物流
                String CHARGE_FLAG = obj.getString("CHARGE_FLAG");
                // 1.3 是否马上办
                String mashangban = obj.getString("mashangban");
                // 1.3 是否全网通办
                String wangshangban = obj.getString("wangshangban");
                // 1.3 是否6+1类
                String sixshenpilb = obj.getString("sixshenpilb");
                // 是否依申请
                String yishenqing = obj.getString("ysq");
                // 是否跨省通办
                String kstbsx = obj.getString("kstbsx");
                // 是否跨省通办
                String qstb = obj.getString("qstb");
                // 1.3 是否全程通办
                String qctb = obj.getString("qctb");
                // 1.4 是否公共服务
                String ggfw = obj.getString("ggfw");
                // 1.3 是否公共服务
                String xzxk = obj.getString("xzxk");
                // 是否依申请
                String isyc = obj.getString("isyc");
                if ("1".equals(isyc)) {
                    yishenqing = "1";
                }
                // 2、获取事项所属的部门
                List<Record> records = iJnAppRestService.selectOuByApplyertypeYC(applyerType, Dao_xc_num, ISPYC,
                        Operationscope, If_express, mpmb, CHARGE_FLAG, mashangban, wangshangban, sixshenpilb,
                        bianminfuwu, yishenqing, qctb, ggfw, xzxk, areaCode, kstbsx, qstb).getResult();
                List<JSONObject> ouJsonList = new ArrayList<JSONObject>();
                // 3、获取部门图片的地址（微信端用）
                String urlRoot = WebUtil.getRequestCompleteUrl(request) + "/epointzwmhwz/css/images/oupic/";
                for (Record record : records) {
                    // 3.1、取部门扩展表里的areacode
                    boolean flag = false;
                    String ouguid = record.get("ouguid");
                    FrameOuExtendInfo frameOuExtendInfo = ouService.getFrameOuExtendInfo(ouguid);
                    if (frameOuExtendInfo != null) {
                        // 3.1.1、若辖区编码一致，则说明不是乡镇级部门
                        if (areaCode.equals(frameOuExtendInfo.get("areacode"))) {
                            flag = true;
                        }
                    }
                    if (flag) {
                        JSONObject ouObject = new JSONObject();
                        String ouname = record.get("ouname");
                        String ounamePin = "";
                        if (StringUtil.isNotBlank(ouname)) {
                            ounamePin = ChineseToPinyin.getPingYin(ouname); // 将获取的部门汉语名称转换成拼音
                        }
                        ouObject.put("ouname", record.get("OUSHORTNAME"));
                        ouObject.put("ouguid", record.get("ouguid"));
                        ouObject.put("ouurl", urlRoot + ounamePin + ".png");
                        ouJsonList.add(ouObject);
                    }
                }
                // 4、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("oulist", ouJsonList);
                log.info("=======结束调用getTaskKindsByOuYC接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取部门列表成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getTaskKindsByOuYC接口参数：params【" + params + "】=======");
            log.info("=======getTaskKindsByOuYC异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取部门列表失败：" + e.getMessage(), "");
        }
    }

    /**
     * 区域下中心列表的接口（咨询投诉用）
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getCenterListByAreaCode", method = RequestMethod.POST)
    public String getCenterListByAreaCode(@RequestBody String params) {
        try {
            log.info("=======开始调用getCenterListByAreaCode接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取区域编码
                String areaCode = obj.getString("areacode");
                // 1.2、获取事项标识
                String taskGuid = obj.getString("taskguid");
                // 1.3、获取套餐标识
                String businessguid = obj.getString("businessguid");
                if (StringUtil.isNotBlank(taskGuid)) {
                    // 如果传了taskguid，以事项的areacode为准，如果没传则用前台传来的areacode
                    AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, false).getResult();
                    if (auditTask != null) {
                        if (StringUtil.isNotBlank(auditTask.getAreacode())) {
                            areaCode = auditTask.getAreacode();
                        }
                    }
                }
                if (StringUtil.isNotBlank(businessguid)) {
                    // 如果传了businessguid，以套餐的areacode为准，如果没传则用前台传来的areacode
                    AuditSpBusiness auditSpBusiness = iAuditSpBusiness.getAuditSpBusinessByRowguid(businessguid)
                            .getResult();
                    if (auditSpBusiness != null) {
                        areaCode = auditSpBusiness.getAreacode();
                    }
                }
                // 2、获取区域下的中心信息
                List<JSONObject> centerJsonList = new ArrayList<JSONObject>();
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.eq("belongxiaqu", areaCode);
                List<AuditOrgaServiceCenter> auditOrgaServiceCenters = iAuditOrgaServiceCenter
                        .getAuditOrgaServiceCenterByCondition(sqlConditionUtil.getMap()).getResult();

                if (auditOrgaServiceCenters != null && auditOrgaServiceCenters.size() > 0) {
                    if ("370800".equals(areaCode)) {
                        JSONObject centerJson = new JSONObject();
                        centerJson.put("centerguid", "46db0d30-b3ea-4d9c-8a66-771731e4b33a");
                        centerJson.put("centername", "济宁市政务服务中心"); // 中心名称
                        centerJsonList.add(centerJson);
                    } else {
                        for (AuditOrgaServiceCenter auditOrgaServiceCenter : auditOrgaServiceCenters) {
                            JSONObject centerJson = new JSONObject();
                            centerJson.put("centerguid", auditOrgaServiceCenter.getRowguid()); // 中心标识
                            centerJson.put("centername", auditOrgaServiceCenter.getCentername()); // 中心名称
                            centerJsonList.add(centerJson);
                        }
                    }
                } else {
                    JSONObject centerJson = new JSONObject();
                    centerJson.put("centerguid", ""); // 中心标识
                    centerJson.put("centername", "请选择"); // 中心名称
                    centerJsonList.add(centerJson);
                }
                // 3、获取区域下的窗口部门
                List<FrameOu> frameOus = zwdtService.getOuListByAreacode(areaCode);
                AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, true).getResult();
                List<JSONObject> ouJsonList = new ArrayList<JSONObject>();
                List<JSONObject> taskJsonList = new ArrayList<JSONObject>();
                String ouGuid = "";
                // 4、判断咨询投诉是针对事项或者是针对中心的
                // 4.1、如果事项标识为空，则咨询投诉不是针对事项的
                JSONObject ouJson1 = new JSONObject();
                JSONObject taskJson1 = new JSONObject();
                if (auditTask != null) {
                    ouJson1.put("ouname", auditTask.getOuname());
                    ouJson1.put("ouguid", auditTask.getOuguid());
                    ouJson1.put("isselected", "true");
                    taskJson1.put("taskname", auditTask.getTaskname());
                    taskJson1.put("taskguid", auditTask.getRowguid());
                    taskJson1.put("isselected", "true");
                } else {
                    ouJson1.put("ouname", "请选择");
                    ouJson1.put("ouguid", "");
                    ouJson1.put("isselected", "true");
                    taskJson1.put("taskname", "请选择");
                    taskJson1.put("taskguid", "");
                    taskJson1.put("isselected", "true");
                }
                ouJsonList.add(0, ouJson1);
                taskJsonList.add(0, taskJson1);
                for (int i = 0; i < frameOus.size(); i++) {
                    FrameOu frameOu = frameOus.get(i);
                    if (!frameOu.getOuguid().equals(ouGuid)) {
                        JSONObject ouJson = new JSONObject();
                        ouJson.put("ouname", frameOu.getOushortName());// 部门名称
                        ouJson.put("ouguid", frameOu.getOuguid());// 部门标识
                        if (StringUtil.isNotBlank(taskGuid)) {
                            // 4.3、如果事项标识不为空，则此部门默认被选择
                            if (auditTask.getOuguid().equals(frameOu.getOuguid())) {
                                ouJson.put("isselected", "true");
                            }
                        }
                        ouJsonList.add(ouJson);
                    }
                }
                // 5、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("oulist", ouJsonList);
                dataJson.put("tasklist", taskJsonList);
                dataJson.put("centerlist", centerJsonList);
                log.info("=======结束调用getCenterListByAreaCode接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取区域下中心成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getCenterListByAreaCode接口参数：params【" + params + "】=======");
            log.info("=======getCenterListByAreaCode异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取区域下中心失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取事项所在中心的接口
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getCenterListByTaskId", method = RequestMethod.POST)
    public String getCenterListByTaskId(@RequestBody String params) {
        try {
            log.info("=======开始调用getCenterListByTaskId接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取事项业务唯一标识
                String taskId = obj.getString("taskid");
                // 2、获取事项所在窗口，窗口所在的中心
                List<Record> centerGuids = iAuditOrgaWindow.selectCenterGuidsByTaskId(taskId).getResult();
                List<JSONObject> centerJsonList = new ArrayList<JSONObject>();
                if (centerGuids != null && centerGuids.size() > 0) {
                    // 2.1、如果有中心数据则返回
                    for (Record record : centerGuids) {
                        AuditOrgaServiceCenter auditOrgaServiceCenter = iAuditOrgaServiceCenter
                                .findAuditServiceCenterByGuid(record.get("centerGuid")).getResult();
                        JSONObject areaJson = new JSONObject();
                        areaJson.put("centerguid", auditOrgaServiceCenter.getRowguid()); // 中心标识
                        areaJson.put("centername", auditOrgaServiceCenter.getCentername()); // 中心名称
                        centerJsonList.add(areaJson);
                    }
                } else {
                    // 2.2、如果没有中心数据则返回无
                    JSONObject centerJson = new JSONObject();
                    centerJson.put("centerguid", "");
                    centerJson.put("centername", "无");
                    centerJsonList.add(centerJson);
                }
                // 3、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("centerlist", centerJsonList);
                log.info("=======结束调用getCenterListByTaskId接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取事项所在中心成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getCenterListByTaskId接口参数：params【" + params + "】=======");
            log.info("=======getCenterListByTaskId异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取区域下中心失败：" + e.getMessage(), "");
        }
    }

    /**
     * 常见问题详细接口
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getTaskFaqDetail", method = RequestMethod.POST)
    public String getTaskFaqDetail(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getTaskFaqDetail接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取常见问题标识
                String faqGuid = obj.getString("faqguid");
                // 2、获取事项常见问题
                AuditTaskFaq auditTaskFaq = iAuditTaskFaq.getAuditTaskFaqByRowguid(faqGuid).getResult();
                // 3、定义返回的JSON数据
                JSONObject dataJson = new JSONObject();
                dataJson.put("question", auditTaskFaq.getQuestion());// 问题
                dataJson.put("answer", auditTaskFaq.getAnswer());// 回答
                return JsonUtils.zwdtRestReturn("1", "常见问题详细信息获取成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getTaskFaqDetail接口参数：params【" + params + "】=======");
            log.info("=======getTaskFaqDetail异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "常见问题信息获取失败：" + e.getMessage(), "");
        }
    }

    /********************************************
     * 暂时无用接口
     **********************************************/
    /**
     * 获取用户是否收藏事项接口（暂时无用）
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/private/getTaskIsCollect", method = RequestMethod.POST)
    public String getTaskIsCollect(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getTaskIsCollect接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取事项标识
                String taskGuid = obj.getString("taskguid");// 事项标识
                // 1.2、获取用户信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 2、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    // 3、判断事项是否收藏
                    AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, true).getResult();
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    sqlConditionUtil.eq("clientidentifier", auditTask.getTask_id()); // 事项业务唯一标识
                    sqlConditionUtil.eq("accountguid", auditOnlineRegister.getAccountguid()); // 用户唯一标识
                    // 4、默认未收藏
                    String msg = "未收藏";
                    String code = "0";
                    int count = iAuditOnlineCollection.getAuditCollectionCount(sqlConditionUtil.getMap()).getResult();
                    if (count > 0) {
                        msg = "已收藏";
                        code = "1";
                    }
                    dataJson.put("msg", msg);
                    dataJson.put("code", code);
                    log.info("=======结束调用getTaskIsCollect接口=======");
                    return JsonUtils.zwdtRestReturn("1", msg, dataJson.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户信息失败！", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getTaskIsCollect接口参数：params【" + params + "】=======");
            log.info("=======getTaskIsCollect异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取用户是否收藏事项失败：" + e.getMessage(), "");
        }
    }

    /**
     * 判断该申请人是否能申请该事项的接口
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/checkApplyerCanDeclare", method = RequestMethod.POST)
    public String checkApplyerCanDeclare(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用checkApplyerCanDeclare接口=======");
            // 1、入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取事项标识
                String taskGuid = obj.getString("taskguid");
                // 1.2、获取用户信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                if (auditOnlineRegister != null) {
                    // 2、获取事项基本信息/扩展信息
                    AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, true).getResult();
                    if (StringUtil.isBlank((auditTask.getApplyertype()))) {
                        log.info("=======结束调用checkApplyerCanDeclare接口=======");
                        return JsonUtils.zwdtRestReturn("0", "该事项不允许在线申报！", "");
                    } else {
                        if (auditTask.getApplyertype().contains(auditOnlineRegister.getUsertype())) {
                            log.info("=======结束调用checkApplyerCanDeclare接口=======");
                            return JsonUtils.zwdtRestReturn("1", "可以申报！", "");
                        } else {
                            log.info("=======结束调用checkApplyerCanDeclare接口=======");
                            return JsonUtils.zwdtRestReturn("0", "申请人类型和事项类型不匹配，不允许申报！", "");
                        }
                    }
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户失败！", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======checkApplyerCanDeclare接口参数：params【" + params + "】=======");
            log.info("=======checkApplyerCanDeclare异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "通过条件获取事项获取失败：" + e.getMessage(), "");
        }
    }

    /**
     * 常见问题分页列表接口
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getTaskFaqPageList", method = RequestMethod.POST)
    public String getTaskFaqPageList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getTaskFaqPageList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取当前页码
                String currentPage = obj.getString("currentpage");
                // 1.2、获取每页数目
                String pageSize = obj.getString("pagesize");

                // 1.2、获取每页数目
                String areacode = obj.getString("areacode");

                List<String> list = new ArrayList<String>();
                List<AuditTask> tasks = zwdtService.getTaskListByAreacode(areacode);
                for (AuditTask task : tasks) {
                    list.add("'" + task.getTask_id() + "'");
                }

                String question = obj.getString("question");

                String ouguid = obj.getString("ouguid");
                // 2、获取事项的常见问题
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                if (StringUtil.isNotBlank(question)) {
                    sqlConditionUtil.like("question", question);
                }
                if (StringUtil.isNotBlank(areacode) && (StringUtil.isBlank(ouguid) || "0".equals(ouguid))) {
                    String strs = list.toString();
                    sqlConditionUtil.in("taskid", strs.replace("[", "").replace("]", ""));
                }

                if (StringUtil.isNotBlank(ouguid) && !"0".equals(ouguid)) {
                    List<String> list1 = new ArrayList<String>();
                    List<AuditTask> tasklist = zwdtService.getTaskListByOuguid(ouguid);
                    for (AuditTask task : tasklist) {
                        list1.add("'" + task.getTask_id() + "'");
                    }
                    if (list1 != null && list1.size() > 0) {
                        sqlConditionUtil.in("taskid", list1.toString().replace("[", "").replace("]", ""));
                    } else {
                        sqlConditionUtil.in("taskid", "");
                    }
                }
                sqlConditionUtil.nq("question", "无");
                List<AuditTaskFaq> auditTaskFaqs = iAuditTaskFaq.selectAuditTaskFaqByPage(sqlConditionUtil.getMap(),
                        Integer.parseInt(currentPage) * Integer.parseInt(pageSize), Integer.parseInt(pageSize),
                        "ordernum", "desc").getResult();
                List<JSONObject> taskfaqList = new ArrayList<JSONObject>();
                if (auditTaskFaqs != null && auditTaskFaqs.size() > 0) {
                    for (AuditTaskFaq auditTaskFaq : auditTaskFaqs) {
                        JSONObject faqJson = new JSONObject();
                        faqJson.put("faqguid", auditTaskFaq.getRowguid());// 常见问题标识
                        faqJson.put("question", auditTaskFaq.getQuestion());// 问题
                        faqJson.put("ouname",
                                iAuditTask.getCopyTaskByTaskId(auditTaskFaq.getTaskid(), "1").getResult().getOuname());// 部门名称
                        faqJson.put("ouguid",
                                iAuditTask.getCopyTaskByTaskId(auditTaskFaq.getTaskid(), "1").getResult().getOuguid());// 部门ID
                        faqJson.put("answer", auditTaskFaq.getAnswer());// 回答
                        taskfaqList.add(faqJson);
                    }
                } else {
                    JSONObject faqJson = new JSONObject();
                    faqJson.put("question", "无问题");// 问题
                }
                JSONObject dataJson = new JSONObject();
                dataJson.put("faqs", taskfaqList);
                return JsonUtils.zwdtRestReturn("1", "常见问题分页列表获取成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getTaskFaqPageList接口参数：params【" + params + "】=======");
            log.info("=======getTaskFaqPageList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "常见问题分页列表获取失败：" + e.getMessage(), "");
        }
    }

    /**
     * 在线交流页面获取事项基本信息
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getTaskByOnlineContact", method = RequestMethod.POST)
    public String getTaskByOnlineContact(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getTaskByOnlineContact接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1、获取当前页码
                String taskguid = obj.getString("taskguid");
                // 2.1、获取事项的基本信息
                AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskguid, true).getResult();
                // 2.2、获取事项的所有材料信息
                List<AuditTaskMaterial> auditTaskMaterialList = iAuditTaskMaterial
                        .selectTaskMaterialListByTaskGuid(taskguid, true).getResult();
                // 2.1、获取事项的基本信息
                AuditTaskExtension auditTaskExtension = iAuditTaskExtension.getTaskExtensionByTaskGuid(taskguid, true)
                        .getResult();
                // 3、定义返回的JSON
                JSONObject dataJson = new JSONObject();
                if (auditTask != null) {
                    dataJson.put("rowguid", auditTask.getRowguid());// 常见问题标识
                    dataJson.put("taskname", auditTask.getTaskname());// 事项名称
                    dataJson.put("ouname", auditTask.getOuname());// 所属部门
                    dataJson.put("promiseday", auditTask.getPromise_day() + "工作日");// 承诺时间
                    dataJson.put("anticipateday", auditTask.getAnticipate_day() + "工作日");// 法定期限
                    dataJson.put("transacttime", auditTask.getTransact_time());// 办理时间
                    dataJson.put("transactaddr", auditTask.getTransact_addr());// 办理地点
                    dataJson.put("linktel", auditTask.getLink_tel());// 联系电话
                    dataJson.put("itemid", auditTask.getItem_id());// 权力编码
                    dataJson.put("qlkind",
                            iCodeItemsService.getItemTextByCodeName("审批类别", String.valueOf(auditTask.getShenpilb()))); // 事项审批类别
                    // 获取行使层级
                    String useLevel = auditTaskExtension.getUse_level();
                    String exerciseClass = "";
                    if (StringUtil.isNotBlank(useLevel)) {
                        String[] userLevelArr = useLevel.split(";");
                        for (int i = 0; i < userLevelArr.length; i++) {
                            exerciseClass = iCodeItemsService.getItemTextByCodeName("行使层级", userLevelArr[i]) + ";";
                        }
                        exerciseClass = exerciseClass.substring(0, exerciseClass.length() - 1);
                    }
                    dataJson.put("exerciseclass", exerciseClass);
                    String SERVICEOBJECT = auditTask.getStr("SERVICE_OBJECT");
                    String managementObj = "";
                    if (StringUtil.isNotBlank(SERVICEOBJECT)) {
                        String[] applyertypearr = SERVICEOBJECT.split(";");
                        for (int i = 0; i < applyertypearr.length; ++i) {
                            managementObj = this.iCodeItemsService.getItemTextByCodeName("服务对象", applyertypearr[i])
                                    + ";";
                        }
                        managementObj = managementObj.substring(0, managementObj.length() - 1);
                    }
                    dataJson.put("managementobj", managementObj);// 办理对象

                }
                // 材料信息
                List<JSONObject> materiallistObject = new ArrayList<JSONObject>();
                if (auditTaskMaterialList != null && auditTaskMaterialList.size() > 0) {
                    for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterialList) {
                        JSONObject maJson = new JSONObject();
                        maJson.put("materialname", auditTaskMaterial.getMaterialname());
                        materiallistObject.add(maJson);
                    }
                }
                dataJson.put("materialnamelist", materiallistObject);
                return JsonUtils.zwdtRestReturn("1", "事项基本信息表获取成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getTaskByOnlineContact接口参数：params【" + params + "】=======");
            log.info("=======getTaskByOnlineContact异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "事项基本信息获取失败：" + e.getMessage(), "");
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

    public static boolean isGoodJson(String json) {
        try {
            JSONObject.parseObject(json);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }

    /**
     * ========================================================
     * 以下为静态化测试接口（仅供测试使用）
     * =========================================================
     */

    /**
     * 事项基本信息的接口（仅供测试用）
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getTaskBasicInfoByStatic", method = RequestMethod.POST)
    public String getTaskBasicInfoByStatic(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getTaskBasicInfoByStatic接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取事项标识
                String taskGuid = obj.getString("taskguid");
                AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, true).getResult();
                if (auditTask != null) {
                    AuditTaskExtension auditTaskExtension = iAuditTaskExtension
                            .getTaskExtensionByTaskGuid(taskGuid, true).getResult();
                    // 2、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    // 3、获取事项基本信息
                    dataJson.put("taskguid", taskGuid);// 事项标识
                    dataJson.put("taskid", auditTask.getTask_id());// 事项业务唯一标识
                    dataJson.put("taskname", auditTask.getTaskname());// 事项名称
                    dataJson.put("itemid", auditTask.getItem_id());// 权利编码
                    // 3.4、判断是否需要事项的所有信息（事项扩展信息、事项材料、事项收费信息、事项常见问题、事项受理条件）
                    JSONObject taskBasicJson = new JSONObject();
                    // 3.4.1、获取事项扩展信息
                    taskBasicJson.put("itemid", auditTask.getItem_id()); // 事项编码
                    taskBasicJson.put("qlkind",
                            iCodeItemsService.getItemTextByCodeName("审批类别", String.valueOf(auditTask.getShenpilb()))); // 事项审批类别
                    taskBasicJson.put("implementsubject", auditTask.getOuname()); // 实施主体
                    // 3.4.1.1、获取行使层级
                    String useLevel = auditTaskExtension.getUse_level();
                    String exerciseClass = "";
                    if (StringUtil.isNotBlank(useLevel)) {
                        String[] userLevelArr = useLevel.split(";");
                        for (int i = 0; i < userLevelArr.length; i++) {
                            exerciseClass = iCodeItemsService.getItemTextByCodeName("行使层级", userLevelArr[i]) + ";";
                        }
                        exerciseClass = exerciseClass.substring(0, exerciseClass.length() - 1);
                    }
                    taskBasicJson.put("exerciseclass", exerciseClass);
                    // 3.4.1.2、获取申请人类型
                    String SERVICEOBJECT = auditTask.getStr("SERVICE_OBJECT");
                    String managementObj = "";
                    if (StringUtil.isNotBlank(SERVICEOBJECT)) {
                        String[] applyertypearr = SERVICEOBJECT.split(";");
                        for (int i = 0; i < applyertypearr.length; ++i) {
                            managementObj = this.iCodeItemsService.getItemTextByCodeName("服务对象", applyertypearr[i])
                                    + ";";
                        }
                        managementObj = managementObj.substring(0, managementObj.length() - 1);
                    }
                    taskBasicJson.put("managementobj", managementObj);// 办理对象
                    taskBasicJson.put("type",
                            iCodeItemsService.getItemTextByCodeName("办件类型", String.valueOf(auditTask.getType())));// 办件类型
                    taskBasicJson.put("anticipateday", auditTask.getAnticipate_day() + "工作日");// 法定期限
                    taskBasicJson.put("promiseday", auditTask.getPromise_day() + "工作日");// 承诺期限
                    taskBasicJson.put("linktel", auditTask.getLink_tel());// 咨询电话
                    taskBasicJson.put("supervisetel", auditTask.getSupervise_tel());// 投诉电话
                    taskBasicJson.put("handleaddress", auditTask.getTransact_addr());// 办公地点
                    taskBasicJson.put("handletime", auditTask.getTransact_time());// 办公时间
                    dataJson.put("taskbasic", taskBasicJson);
                    log.info("=======结束调用getTaskBasicInfoByStatic接口=======");
                    return JsonUtils.zwdtRestReturn("1", "事项基本信息获取成功", dataJson.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "该事项不存在！", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getTaskBasicInfoByStatic接口参数：params【" + params + "】=======");
            log.info("=======getTaskBasicInfoByStatic异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "事项基本信息获取失败：" + e.getMessage(), "");
        }
    }

    /**
     * 部门分类的接口（仅供测试用）
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getTaskKindsByOuStatic", method = RequestMethod.POST)
    public String getTaskKindsByOuStatic(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getTaskKindsByOuStatic接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取区域编码
                String areaCode = obj.getString("areacode");
                // 1.2、获取申请人类型（10:企业、20：个人）
                String applyerType = obj.getString("usertype");
                // 2、获取事项所属的部门
                List<Record> records = iAuditTask.selectOuByApplyertype(applyerType, areaCode).getResult();
                List<JSONObject> ouJsonList = new ArrayList<JSONObject>();
                // 3、获取部门图片的地址（微信端用）
                for (Record record : records) {
                    JSONObject ouObject = new JSONObject();
                    ouObject.put("ouname", record.get("ouname"));
                    ouObject.put("ouguid", record.get("ouguid"));
                    ouJsonList.add(ouObject);
                }
                // 4、定义返回JSON对象
                /*
                 * JSONObject dataJson = new JSONObject();
                 * dataJson.put("oulist", ouJsonList);
                 */
                log.info("=======结束调用getTaskKindsByOuStatic接口=======");
                /*
                 * return JsonUtils.zwdtRestReturn("1", "获取部门列表成功",
                 * dataJson.toString());
                 */
                return "{" + ouJsonList.toString() + "}";
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getTaskKindsByOuStatic接口参数：params【" + params + "】=======");
            log.info("=======getTaskKindsByOuStatic异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取部门列表失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取到主题分类的相关信息（仅供测试用）
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getTaskthemeStatic", method = RequestMethod.POST)
    public String getTaskthemeStatic(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getTaskthemeStatic接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取申请人类型
                String applyerType = obj.getString("usertype");
                // 1.2、获取判断PC端使用还是微信端使用 (0：PC端，1：微信端)
                String isPcUse = obj.getString("ispcuse");
                // 2、获取主题分类
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.eq("LENGTH(no)", "4");
                String basePath = "";
                // 2.1、获取请求地址(http:/IP:端口/应用名称)
                String urlRoot = WebUtil.getRequestCompleteUrl(request);
                if (ZwdtConstant.WXUSERPIC.equals(isPcUse)) {
                    // 2.2、微信端获取微信端图片文件夹地址
                    basePath = urlRoot + "/epointzwmhwz/css/images/wxthemes/";
                } else {
                    // 2.3、如果是PC端需要获取个人或企业的主题图片地址及
                    if (ZwfwConstant.APPLAYERTYPE_GR.equals(applyerType)) {
                        basePath = urlRoot + "/epointzwmhwz/pages/eventdetail/images/";
                        sqlConditionUtil.eq("SUBSTRING(no, 1,2)", "B1");
                    } else if (ZwfwConstant.APPLAYERTYPE_QY.equals(applyerType)) {
                        basePath = urlRoot + "/epointzwmhwz/pages/legal/images/";
                        sqlConditionUtil.eq("SUBSTRING(no, 1,2)", "B2");
                    }
                }
                // 3、获取主题分类数据
                List<JSONObject> dictJsonList = new ArrayList<JSONObject>();
                List<AuditTaskDict> auditTaskDicts = iAuditTaskDict.getAuditTaskDictList(sqlConditionUtil.getMap())
                        .getResult();
                for (AuditTaskDict auditTaskDict : auditTaskDicts) {
                    JSONObject dictObject = new JSONObject();
                    dictObject.put("dictname", auditTaskDict.getClassname()); // 主题类别名称
                    dictObject.put("dictid", auditTaskDict.getRowguid()); // 主题类别标识
                    dictObject.put("dictno", auditTaskDict.getNo()); // 主题类别编号
                    dictObject.put("dictrul", basePath + auditTaskDict.getNo() + ".png");// 主题对应的图片
                    dictJsonList.add(dictObject);
                }
                // 4、定义返回JSON对象
                /*
                 * JSONObject dataJson = new JSONObject();
                 * dataJson.put("dictlist", dictJsonList); return
                 * JsonUtils.zwdtRestReturn("1", "主题分类列表获取成功",
                 * dataJson.toString());
                 */
                log.info("=======结束调用getTaskthemeStatic接口=======");
                return "{" + dictJsonList.toString() + "}";
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getTaskthemeStatic接口参数：params【" + params + "】=======");
            log.info("=======getTaskthemeStatic异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取主题信息失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取辖区编码和站点之间的关联关系（仅供测试用）
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getXiaqucodeStatic", method = RequestMethod.POST)
    public String getXiaqucodeStatic(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getXiaqucodeStatic接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                // 2、获取辖区编码
                List<JSONObject> dictJsonList = new ArrayList<JSONObject>();
                List<AuditOrgaArea> auditOrgaAreas = iAuditOrgaArea.selectAuditAreaList(sqlConditionUtil.getMap())
                        .getResult();
                for (AuditOrgaArea auditOrgaArea : auditOrgaAreas) {
                    // 这边要判断下siteguid 是否为空的情况，如果是空的话，就不用拼接
                    if (StringUtil.isNotBlank(auditOrgaArea.getSiteGuid())) {
                        JSONObject dictObject = new JSONObject();
                        dictObject.put("siteguid", auditOrgaArea.getSiteGuid()); // 站点guid
                        dictObject.put("xiaqucode", auditOrgaArea.getXiaqucode()); // 辖区编码
                        dictJsonList.add(dictObject);
                    }
                }
                /*
                 * // 3、定义返回JSON对象 JSONObject dataJson = new JSONObject();
                 * dataJson.put(null, dictJsonList);
                 */
                log.info("=======结束调用getXiaqucodeStatic接口=======");
                return "{" + dictJsonList.toString() + "}";
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            log.info("=======getXiaqucodeStatic接口参数：params【" + params + "】=======");
            log.info("=======getXiaqucodeStatic异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取辖区信息列表失败：" + e.getMessage(), "");
        }
    }

    /**
     * 事项基本信息的接口
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getTaskBasicInfoDetail", method = RequestMethod.POST)
    public String getTaskBasicInfoDetail(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getTaskBasicInfoDetail接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取事项标识
                String taskGuid = obj.getString("taskguid");
                // 1.2、获取事项情形标识
                String taskCaseGuid = obj.getString("taskcaseguid");
                // 1.3、获取是否需要事项全部信息 0：基本信息 1：所有信息
                String isNeedAll = obj.getString("isneedall");
                // 1.4、获取用户信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                // 1.5、获取事项信息
                AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, true).getResult();
                if (auditTask != null) {
                    AuditTaskExtension auditTaskExtension = iAuditTaskExtension
                            .getTaskExtensionByTaskGuid(taskGuid, true).getResult();
                    // 2、定义返回JSON对象
                    JSONObject dataJson = new JSONObject();
                    // 3、获取事项基本信息
                    dataJson.put("taskguid", taskGuid);// 事项标识
                    dataJson.put("taskid", auditTask.getTask_id());// 事项业务唯一标识
                    dataJson.put("taskname", auditTask.getTaskname());// 事项名称
                    dataJson.put("itemid", auditTask.getItem_id());// 权利编码

                    JSONObject taskElementJson = new JSONObject();
                    // 3.1、事项是否可以网上申报
                    Integer webApplyType = auditTaskExtension == null ? 0
                            : (auditTaskExtension.getWebapplytype() == null ? 0 : auditTaskExtension.getWebapplytype());
                    String onlineHandle = "0";
                    if (webApplyType != null) {
                        onlineHandle = webApplyType == 0 ? "0" : "1";
                    }
                    taskElementJson.put("onlinehandle", Integer.parseInt(onlineHandle));
                    // 3.2、事项是否可以网上预约
                    if (isAppoint(auditTask.getTask_id())) {
                        taskElementJson.put("appointment",
                                auditTaskExtension == null ? "0" : auditTaskExtension.getReservationmanagement());// 事项是否可以网上预约
                    }
                    dataJson.put("taskelement", taskElementJson);
                    dataJson.put("qrcontent", WebUtil.getRequestCompleteUrl(request)
                            + "/epointzwmhwz/pages/mobile/html/apptaskdetail?taskguid=" + taskGuid);// 二维码内容
                    if (auditOnlineRegister != null) {
                        if (isCollect(auditTask.getTask_id(), auditOnlineRegister.getAccountguid())) {
                            dataJson.put("iscollect", "1");// 返回改事项已收藏标识
                        }
                    }
                    // 3.3、获取材料多情形
                    List<AuditTaskCase> auditTaskCases = iAuditTaskCase.selectTaskCaseByTaskGuid(taskGuid).getResult();
                    List<JSONObject> caseConditionList = new ArrayList<JSONObject>();
                    if (auditTaskCases != null && auditTaskCases.size() > 0) {
                        for (AuditTaskCase auditTaskCase : auditTaskCases) {
                            if (StringUtil.isNotBlank(auditTaskCase.getCasename())) {
                                JSONObject caseConditionJson = new JSONObject();
                                caseConditionJson.put("taskcaseguid", auditTaskCase.getRowguid());// 多情形标识
                                caseConditionJson.put("casename", auditTaskCase.getCasename());// 多情形名称
                                caseConditionList.add(caseConditionJson);
                            }
                        }
                    }
                    dataJson.put("taskcasecount", auditTaskCases.size());// 多情形列表数据
                    dataJson.put("casecondition", caseConditionList);
                    // 3.4、判断是否需要事项的所有信息（事项扩展信息、事项材料、事项收费信息、事项常见问题、事项受理条件）
                    if ("1".equals(isNeedAll)) {
                        JSONObject taskBasicJson = new JSONObject();
                        // 3.4.1、获取事项扩展信息
                        taskBasicJson.put("itemid", auditTask.getItem_id()); // 事项编码
                        taskBasicJson.put("qlkind", iCodeItemsService.getItemTextByCodeName("审批类别",
                                String.valueOf(auditTask.getShenpilb()))); // 事项审批类别
                        taskBasicJson.put("implementsubject", auditTask.getOuname()); // 实施主体
                        // 3.4.1.1、获取行使层级
                        String useLevel = auditTaskExtension.getUse_level();
                        String exerciseClass = "";
                        if (StringUtil.isNotBlank(useLevel)) {
                            String[] userLevelArr = useLevel.split(";");
                            for (int i = 0; i < userLevelArr.length; i++) {
                                exerciseClass = iCodeItemsService.getItemTextByCodeName("行使层级", userLevelArr[i]) + ";";
                            }
                            exerciseClass = exerciseClass.substring(0, exerciseClass.length() - 1);
                        }
                        taskBasicJson.put("exerciseclass", exerciseClass);
                        // 3.4.1.2、获取申请人类型
                        String SERVICEOBJECT = auditTask.getStr("SERVICE_OBJECT");
                        String managementObj = "";
                        if (StringUtil.isNotBlank(SERVICEOBJECT)) {
                            String[] applyertypearr = SERVICEOBJECT.split(";");
                            for (int i = 0; i < applyertypearr.length; ++i) {
                                managementObj = this.iCodeItemsService.getItemTextByCodeName("服务对象", applyertypearr[i])
                                        + ";";
                            }
                            managementObj = managementObj.substring(0, managementObj.length() - 1);
                        }
                        taskBasicJson.put("managementobj", managementObj);// 办理对象
                        taskBasicJson.put("type",
                                iCodeItemsService.getItemTextByCodeName("办件类型", String.valueOf(auditTask.getType())));// 办件类型
                        taskBasicJson.put("anticipateday", auditTask.getAnticipate_day() + "工作日");// 法定期限
                        taskBasicJson.put("promiseday", auditTask.getPromise_day() + "工作日");// 承诺期限
                        taskBasicJson.put("linktel", auditTask.getLink_tel());// 咨询电话
                        taskBasicJson.put("supervisetel", auditTask.getSupervise_tel());// 投诉电话

                        taskBasicJson.put("handleaddress", auditTask.getTransact_addr());// 办公地点
                        taskBasicJson.put("handletime", auditTask.getTransact_time());// 办公时间
                        // 为了兼容之前的前台页面暂时不删除
                        taskBasicJson.put("limit_num", auditTask.getLimit_num());// 数量限制
                        if (auditTaskExtension != null) {
                            taskBasicJson.put("dao_xc_num", auditTaskExtension.getDao_xc_num());// 到办事窗口的最少次数
                            taskBasicJson.put("finishfilename", auditTaskExtension.getFinishfilename());// 审批结果名称
                            String finishproductsamples = auditTaskExtension.getFinishproductsamples();
                            if (StringUtil.isNotBlank(finishproductsamples)) {
                                List<FrameAttachInfo> frameAttachInfos = iAttachService
                                        .getAttachInfoListByGuid(finishproductsamples);
                                if (frameAttachInfos != null && !frameAttachInfos.isEmpty()) {
                                    taskBasicJson.put("finishproductsamples",
                                            frameAttachInfos.get(0).getAttachFileName());// 结果样本名称
                                    taskBasicJson.put("finishproductguid", finishproductsamples);// 审批结果标识
                                }
                            }
                        }
                        // 3.4.1.3 获取办理情况
                        List<JSONObject> taskHandleJsonList = new ArrayList<JSONObject>();
                        List<AuditOrgaWindow> auditOrgaWindows = iAuditOrgaWindow
                                .getWindowListByTaskId(auditTask.getTask_id()).getResult();
                        // 标志当前循环中的窗口是否是第一个进入循环的市区县的窗口。
                        Boolean firstWindow = true;
                        if (auditOrgaWindows != null && auditOrgaWindows.size() > 0) {
                            for (AuditOrgaWindow auditOrgaWindow : auditOrgaWindows) {
                                AuditOrgaServiceCenter auditOrgaServiceCenter = iAuditOrgaServiceCenter
                                        .findAuditServiceCenterByGuid(auditOrgaWindow.getCenterguid()).getResult();
                                String areaCode = auditOrgaServiceCenter.getBelongxiaqu();
                                // 3.4.1.3.1 拼接窗口详细信息
                                String windowAddress = auditOrgaServiceCenter.getOuname() + " "
                                        + auditOrgaWindow.getAddress() + " " + auditOrgaWindow.getWindowname() + " "
                                        + auditOrgaWindow.getWindowno();
                                // 3.4.1.3.2 获取排队等待人数
                                String taskTypeGuid = iAuditQueueTasktypeTask.getTaskTypeguidbyTaskIDandCenterGuid(
                                        auditTask.getTask_id(), auditOrgaWindow.getCenterguid()).getResult();
                                // 否存在限号标志位
                                int leftQueueNum = -1;
                                if (StringUtil.isNotBlank(taskTypeGuid)) {
                                    AuditQueueTasktype auditQueueTasktype = iAuditQueueTasktype
                                            .getTasktypeByguid(taskTypeGuid).getResult();
                                    if (StringUtil.isNotBlank(auditQueueTasktype)) {
                                        // 判断是否存在限号
                                        if (StringUtil.isNotBlank(auditQueueTasktype.getXianhaonum())) {
                                            leftQueueNum = Integer.parseInt(auditQueueTasktype.getXianhaonum())
                                                    - iAuditQueue.getCountByTaskGuid(taskTypeGuid).getResult();
                                        }
                                    }
                                }
                                // 剩余叫号数 -1代表没有限制
                                String leftNum = leftQueueNum == -1 ? "无限制" : String.valueOf(leftQueueNum) + "个";
                                // 3.4.1.3.3 将市区县中心窗口信息作为list的第一个返回
                                if (areaCode.length() == 6 && firstWindow) {
                                    firstWindow = false;
                                    JSONObject taskBaseHandleJson = new JSONObject();
                                    taskBaseHandleJson.put("windowaddress", windowAddress); // 中心窗口地址
                                    taskBaseHandleJson.put("handleaddress", auditTask.getTransact_addr());// 办公地点
                                    taskBaseHandleJson.put("handletime", auditTask.getTransact_time());// 办公时间
                                    taskBaseHandleJson.put("linktel", auditTask.getLink_tel());// 投诉电话
                                    taskBaseHandleJson.put("waitnum",
                                            iHandleQueue.getTaskWaitNum(taskTypeGuid, true).getResult());
                                    taskBaseHandleJson.put("leftqueuenum", leftNum);
                                    taskHandleJsonList.add(0, taskBaseHandleJson);
                                    break;
                                } else {
                                    JSONObject taskHandleJson = new JSONObject();
                                    AuditTaskDelegate auditTaskDelegate = iAuditTaskDelegate
                                            .findByTaskIDAndAreacode(auditTask.getTask_id(), areaCode).getResult();
                                    if (auditTaskDelegate != null) {
                                        taskHandleJson.put("windowaddress", windowAddress);// 中心窗口地址
                                        // 如果是乡镇法定事项，读取audittask内的信息
                                        if (ZwfwConstant.TASKDELEGATE_TYPE_XZFD
                                                .equals(auditTaskDelegate.getDelegatetype())) {
                                            taskHandleJson.put("handleaddress", auditTask.getTransact_addr());// 办公地点
                                            taskHandleJson.put("handletime", auditTask.getTransact_time());// 办公时间
                                            taskHandleJson.put("linktel", auditTask.getLink_tel());// 投诉电话
                                            taskHandleJson.put("waitnum",
                                                    iHandleQueue.getTaskWaitNum(taskTypeGuid, true).getResult()); // 等待人数
                                            taskHandleJson.put("leftqueuenum", leftNum); // 剩余取号数
                                            taskHandleJsonList.add(taskHandleJson);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        taskBasicJson.put("addressandtimelist", taskHandleJsonList);
                        dataJson.put("taskbasic", taskBasicJson);

                        // 3.4.2、获取事项受理条件
                        dataJson.put("handlecondition", StringUtil.isBlank(auditTask.getAcceptcondition()) ? ""
                                : auditTask.getAcceptcondition());// 受理条件
                        String taskOutImgGuid = auditTask.getTaskoutimgguid();
                        if (StringUtil.isNotBlank(taskOutImgGuid)) {
                            List<FrameAttachInfo> frameAttachInfos = iAttachService
                                    .getAttachInfoListByGuid(taskOutImgGuid);
                            if (frameAttachInfos != null && frameAttachInfos.size() > 0) {
                                taskOutImgGuid = frameAttachInfos.get(0).getAttachGuid();// 办理流程
                                dataJson.put("taskoutimg", WebUtil.getRequestCompleteUrl(request)
                                        + "/rest/auditattach/readAttach?attachguid=" + taskOutImgGuid);
                            }
                        }
                        // 3.4.3、获取事项设定依据
                        dataJson.put("setting", StringUtil.isBlank(auditTask.getBy_law()) ? "" : auditTask.getBy_law());
                        // 3.4.4、获取事项结果查询
                        dataJson.put("result", auditTask.getQuery_method());
                        List<JSONObject> taskMaterialList = new ArrayList<JSONObject>();
                        // 3.4.5、获取事项材料信息
                        List<AuditTaskMaterial> auditTaskMaterials = iAuditTaskMaterial
                                .selectTaskMaterialListByTaskGuid(taskGuid, true).getResult();
                        // 3.4.5.1、对事项材料进行排序
                        Collections.sort(auditTaskMaterials, new Comparator<AuditTaskMaterial>() {
                            @Override
                            public int compare(AuditTaskMaterial b1, AuditTaskMaterial b2) {
                                // 3.4.5.1、优先对比材料必要性（必要在前）
                                int comNecessity = b1.getNecessity().compareTo(b2.getNecessity());
                                int ret = comNecessity;
                                // 3.4.5.1、材料必要性一致的情况下对比排序号（排序号降序排）
                                if (comNecessity == 0) {
                                    Integer ordernum1 = b1.getOrdernum() == null ? 0 : b1.getOrdernum();
                                    Integer ordernum2 = b2.getOrdernum() == null ? 0 : b2.getOrdernum();
                                    ret = ordernum2.compareTo(ordernum1);
                                }
                                return ret;
                            }
                        });
                        // 3.4.5.2、拼接材料返回JSON
                        for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
                            JSONObject materialJson = new JSONObject();
                            String materialGuid = auditTaskMaterial.getRowguid();
                            materialJson.put("materialguid", materialGuid);// 材料标识
                            materialJson.put("materialname", auditTaskMaterial.getMaterialname());// 材料名称
                            // 3.4.5.2.1、获取填报示例对应附件标识
                            if (StringUtil.isNotBlank(auditTaskMaterial.getExampleattachguid())) {
                                int exampleAttachCount = iAttachService
                                        .getAttachCountByClientGuid(auditTaskMaterial.getExampleattachguid());
                                if (exampleAttachCount > 0) {
                                    materialJson.put("exampleattachguid", auditTaskMaterial.getExampleattachguid());
                                }
                            }
                            // 3.4.5.2.2、获取空白模板对应附件标识
                            if (StringUtil.isNotBlank(auditTaskMaterial.getTemplateattachguid())) {
                                int templateAttachCount = iAttachService
                                        .getAttachCountByClientGuid(auditTaskMaterial.getTemplateattachguid());
                                if (templateAttachCount > 0) {
                                    materialJson.put("templateattachguid", auditTaskMaterial.getTemplateattachguid());
                                }
                            }
                            materialJson.put("submittype", iCodeItemsService.getItemTextByCodeName("提交方式",
                                    String.valueOf(auditTaskMaterial.getSubmittype()))); // 材料提交方式
                            // 3.4.5.2.3、获取材料来源渠道
                            String materialsource;
                            if (StringUtil.isBlank(auditTaskMaterial.getFile_source())) {
                                materialsource = "申请人自备";
                            } else {
                                materialsource = iCodeItemsService.getItemTextByCodeName("来源渠道",
                                        auditTaskMaterial.getFile_source());
                            }
                            materialJson.put("materialsource", materialsource);
                            // 3.4.5.2.4、获取事项材料必要性
                            String necessary = "0";
                            if (StringUtil.isNotBlank(taskCaseGuid)) {
                                AuditTaskMaterialCase auditTaskMaterialCase = iAuditTaskMaterialCase
                                        .selectTaskMaterialCaseByGuid(taskCaseGuid, materialGuid).getResult();
                                if (auditTaskMaterialCase != null) {
                                    necessary = ZwfwConstant.NECESSITY_SET_YES
                                            .equals(String.valueOf(auditTaskMaterialCase.getNecessity())) ? "1" : "0";// 是否必需
                                }
                            } else {
                                necessary = ZwfwConstant.NECESSITY_SET_YES
                                        .equals(String.valueOf(auditTaskMaterial.getNecessity())) ? "1" : "0";// 是否必需
                            }
                            materialJson.put("necessary", necessary);
                            int pagenum;
                            if (StringUtil.isBlank(auditTaskMaterial.getPage_num())) {
                                pagenum = 0;
                            } else {
                                pagenum = auditTaskMaterial.getPage_num();
                            }
                            int copynum;
                            if (StringUtil.isBlank(auditTaskMaterial.getStr("copy_num"))) {
                                copynum = 0;
                            } else {
                                copynum = Integer.parseInt(auditTaskMaterial.getStr("copy_num"));
                            }
                            if (pagenum == 0 && copynum == 0) {
                                materialJson.put("matertype", "");
                            } else if (pagenum != 0 && copynum == 0) {
                                materialJson.put("matertype", "原件");
                            } else if (pagenum == 0 && copynum != 0) {
                                materialJson.put("matertype", "复印件");
                            } else if (pagenum != 0 && copynum != 0) {
                                materialJson.put("matertype", "原件和复印件");
                            }
                            String Submittype = auditTaskMaterial.getSubmittype();

                            if ("10".equals(Submittype)) {
                                materialJson.put("pagenum", "电子"); // 份数
                            } else if ("20".equals(Submittype)) {
                                materialJson.put("pagenum", "纸质" + pagenum + "份"); // 份数
                            } else {
                                materialJson.put("pagenum", "电子或纸质"); // 份数
                            }

                            materialJson.put("standard", StringUtil.isBlank(auditTaskMaterial.getStandard()) ? "无"
                                    : auditTaskMaterial.getStandard());// 受理标准
                            materialJson.put("fileexplain",
                                    StringUtil.isBlank(auditTaskMaterial.getFile_explian()) ? "无"
                                            : auditTaskMaterial.getFile_explian());// 填报须知
                            materialJson.put("publisher",
                                    StringUtil.isBlank(auditTaskMaterial.getFile_source_explain()) ? "无"
                                            : auditTaskMaterial.getFile_source_explain());// 填报须知

                            String isxtendfield = auditTaskMaterial.getStr("isxtendfield");

                            if ("1".equals(isxtendfield)) {
                                materialJson.put("materialsource", "已关联电子证照，可免提交");
                                materialJson.put("pagenum", "");
                                materialJson.put("matertype", "");
                                materialJson.put("isxtendfield", 1);
                                materialJson.put("isshuoming", 0);
                            } else {
                                materialJson.put("isxtendfield", 0);
                                materialJson.put("isshuoming", 1);
                            }

                            taskMaterialList.add(materialJson);
                        }
                        dataJson.put("taskmaterials", taskMaterialList);
                        dataJson.put("isChargeitem", auditTask.getCharge_flag());
                        // 4、获取事项收费标准
                        List<JSONObject> taskChargeItemList = new ArrayList<JSONObject>();
                        List<AuditTaskChargeItem> auditTaskChargeItems = iAuditTaskChargeItem
                                .selectAuditTaskChargeItemByTaskGuid(taskGuid, true).getResult();
                        if (auditTaskChargeItems != null && auditTaskChargeItems.size() > 0) {
                            for (AuditTaskChargeItem auditTaskChargeItem : auditTaskChargeItems) {
                                JSONObject chargeItemJson = new JSONObject();
                                chargeItemJson.put("chargeitemname", auditTaskChargeItem.getItemname());// 收费项目名称
                                chargeItemJson.put("chargeitemguid", auditTaskChargeItem.getRowguid());// 收费项目标识
                                chargeItemJson.put("unit", auditTaskChargeItem.getUnit());// 收费单位
                                chargeItemJson.put("chargeitemstand", auditTaskChargeItem.getChargeitem_stand());// 收费标准
                                taskChargeItemList.add(chargeItemJson);
                            }
                            dataJson.put("chargeitems", taskChargeItemList);
                        } else {
                            dataJson.put("chargeitems", "无");
                        }

                        // 5、获取事项常见问题
                        List<JSONObject> taskFaqList = new ArrayList<JSONObject>();
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
                                faqJson.put("faqguid", auditTaskFaq.getRowguid()); // 常见问题标识
                                faqJson.put("question", auditTaskFaq.getQuestion());// 问题
                                faqJson.put("answer", auditTaskFaq.getAnswer());// 回答
                                taskFaqList.add(faqJson);
                            }
                        }
                        dataJson.put("faqs", taskFaqList);
                    }
                    log.info("=======结束调用getTaskBasicInfoDetail接口=======");
                    return JsonUtils.zwdtRestReturn("1", "事项基本信息获取成功", dataJson.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "该事项不存在！", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getTaskBasicInfoDetail接口参数：params【" + params + "】=======");
            log.info("=======getTaskBasicInfoDetail异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "事项基本信息获取失败：" + e.getMessage(), "");
        }
    }

    // 判断事项是否允许预约
    public boolean isAppoint(String taskId) {
        boolean isAppointment = false;
        // 判断事项是否分配到相关排队分类
        List<String> taskTypeGuidList = iAuditQueueTasktypeTask.getTaskTypeguidbyTaskID(taskId).getResult();
        if (taskTypeGuidList != null && !taskTypeGuidList.isEmpty()) {
            // 获取该事项下的所有中心
            for (String taskTypeGuid : taskTypeGuidList) {
                AuditQueueTasktype tasktype = iAuditQueueTasktype.getTasktypeByguid(taskTypeGuid).getResult();
                if (tasktype != null) {
                    // 如果事项允许预约，判断该中心是否配置预约时间
                    if (QueueConstant.Common_yes_String.equals(tasktype.getIs_yuyue())) {
                        AuditOrgaServiceCenter center = iAuditOrgaServiceCenter
                                .findAuditServiceCenterByGuid(tasktype.getCenterguid()).getResult();
                        if (center != null) {
                            // 判断是否已设置预约时间
                            List<AuditQueueYuyuetime> yuyuetimelist = iAuditQueueYuyueTime
                                    .getYuyuetimeList(taskTypeGuid, "1", tasktype.getCenterguid()).getResult();
                            // 如果不存在个性化预约，获取全局预约时间信息
                            if ((yuyuetimelist == null) || yuyuetimelist.size() == 0) {
                                yuyuetimelist = iAuditQueueYuyueTime.getYuyuetimeList("", "0", tasktype.getCenterguid())
                                        .getResult();
                            }
                            if ((yuyuetimelist != null) && yuyuetimelist.size() > 0) {
                                isAppointment = true;
                            }
                        }
                    }
                }

            }
        }
        return isAppointment;
    }

    /**
     * 判断是否收藏 [功能详细描述]
     *
     * @param clientIdentifier
     * @param accountGuid
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public boolean isCollect(String clientIdentifier, String accountGuid) {
        SqlConditionUtil fathersqlConditionUtil = new SqlConditionUtil();
        fathersqlConditionUtil.eq("clientidentifier", clientIdentifier);
        fathersqlConditionUtil.eq("accountguid", accountGuid);
        int fathercount = iAuditOnlineCollection.getAuditCollectionCount(fathersqlConditionUtil.getMap()).getResult();
        if (fathercount > 0) {
            return true;
        }
        return false;
    }

    /**
     * 同步办件结果
     */
    public String syncResultInfo(String resutltinfo) {
        String names = "";
        String types = "";
        String modes = "";
        // 解析XML
        Document document = null;
        try {
            document = DocumentHelper.parseText(resutltinfo);
        } catch (DocumentException e) {
            log.info("====>>>解析办理结果失败");
        }
        Element root = document.getRootElement();
        Element nodeList = root.element("RESULTS");
        List<Element> s = nodeList.elements("RESULT");
        for (Element p : s) {
            names += p.element("RESULT_NAME").getStringValue() + ",";
            types += p.element("RESULT_TYPE").getStringValue() + ",";
            modes += p.element("SERVICE_MODE").getStringValue() + ",";
        }
        return names + ";" + types + ";" + modes;
    }

    /**
     * 根据搜索条件获取事项信息的接口（个人办事\法人办事搜索可展示大小项）
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getTaskListByCondition1", method = RequestMethod.POST)
    public String getTaskListByCondition1(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getTaskListByCondition1接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                JSONObject returnJson = handleGetTaskList(obj, request, "getTaskListByCondition");
                log.info("=======结束调用getTaskListByCondition1接口=======");
                return JsonUtils.zwdtRestReturn("1", "通过条件获取事项成功", returnJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getTaskListByCondition1接口参数：params【" + params + "】=======");
            log.info("=======getTaskListByCondition1异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "通过条件获取事项获取失败：" + e.getMessage(), "");
        }
    }

    /**
     * 根据搜索条件获取事项信息的接口（搜索页）
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getTaskListByCondition2", method = RequestMethod.POST)
    public String getTaskListByCondition2(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getTaskListByCondition2接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                JSONObject returnJson = handleGetTaskListNew(obj, request);
                log.info("=======结束调用getTaskListByCondition2接口=======");
                return JsonUtils.zwdtRestReturn("1", "通过条件获取事项成功", returnJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getTaskListByCondition2接口参数：params【" + params + "】=======");
            log.info("=======getTaskListByCondition2异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "通过条件获取事项获取失败：" + e.getMessage(), "");
        }
    }

    /**
     * 根据搜索条件获取事项信息的接口（搜索页）
     * 【提供爱山东，勿改动入参返回】
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getTaskListByCondition3", method = RequestMethod.POST)
    public String getTaskListByCondition3(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getTaskListByCondition3接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                JSONObject returnJson = handleGetTaskListSearch(obj);
                log.info("=======结束调用getTaskListByCondition3接口=======");
                return JsonUtils.zwdtRestReturn("1", "通过条件获取事项成功", returnJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getTaskListByCondition3接口参数：params【" + params + "】=======");
            log.info("=======getTaskListByCondition3异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "通过条件获取事项获取失败：" + e.getMessage(), "");
        }
    }


     /**
     * 根据搜索条件获取事项信息的接口（搜索页）
     * 【提供中汇软件】
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getTaskListByCondition4", method = RequestMethod.POST)
    public String getTaskListByCondition4(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getTaskListByCondition4接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 获取事项当前页码
                String currentPageTask = obj.getString("currentpage");
                // 获取事项每页数目
                String pageSizeTask = obj.getString("pagesize");
                // 获取搜索条件
                String keyWord = obj.getString("searchcondition");
                // 获取区域编码
                String areaCode = obj.getString("areacode");
                String field = "rowguid,item_id,taskname,ouname,ouguid,AREACODE,task_id";
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.eq("businesstype","1");
                if(StringUtils.isNotBlank(areaCode)){
                    sqlConditionUtil.eq("AREACODE",areaCode);
                }
                if(StringUtils.isNotBlank(keyWord)){
                    sqlConditionUtil.like("taskname",keyWord);
                }
                PageData<AuditTask> audittasks = iAuditTask.getAuditEnableTaskPageData(field, sqlConditionUtil.getMap(), Integer.parseInt(currentPageTask) * Integer.parseInt(pageSizeTask), Integer.parseInt(pageSizeTask), "ordernum", "desc").getResult();
                List<AuditTask> taskJsonList = audittasks.getList();
                int taskCount = audittasks.getRowCount();
                JSONObject returnJson = new JSONObject();
                returnJson.put("tasklist", taskJsonList);
                returnJson.put("totalcount", taskCount);
                log.info("=======结束调用getTaskListByCondition4接口=======");
                return JsonUtils.zwdtRestReturn("1", "通过条件获取事项成功", returnJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getTaskListByCondition3接口参数：params【" + params + "】=======");
            log.info("=======getTaskListByCondition3异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "通过条件获取事项获取失败：" + e.getMessage(), "");
        }
    }



    /**
     * 通用方法获取事项列表
     *
     * @param obj      接口传入参数
     * @param request  请求头数据
     * @param function 方法名称
     * @return
     */
    public JSONObject handleGetTaskList(JSONObject obj, HttpServletRequest request, String function) {
        // 1.1、获取事项当前页码
        String currentPageTask = obj.getString("currentpage");
        // 1.2、获取事项每页数目
        String pageSizeTask = obj.getString("pagesize");
        // 1.3、获取搜索条件
        String keyWord = obj.getString("searchcondition");
        // 1.4、获取是否在线办理
        String onlineHandle = obj.getString("onlinehandle");
        // 1.5、获取区域编码
        String areaCode = obj.getString("areacode");
        // 1.6、获取主题分类标识
        String dictId = obj.getString("dictid");
        // 1.7、获取部门标识
        String ouGuid = obj.getString("ouguid");
        // 1.11 审批类别
        String splb = obj.getString("splb");
        // 1.11 主题分类
        String ztfl = obj.getString("ztfl");
        String is_fugongfuchan = obj.getString("is_fugongfuchan");
        // 1.3 到现场次数
        String Dao_xc_num = obj.getString("Dao_xc_num");
        // 1.3 是否一次办好
        String ISPYC = obj.getString("ISPYC");
        // 1.3 是否全市通办
        String Operationscope = obj.getString("Operationscope");
        // 1.3 是否支持物流
        String If_express = obj.getString("If_express");
        // 1.3 是否支持物流
        String mpmb = obj.getString("mpmb");
        // 1.3 是否支持物流
        String bianminfuwu = obj.getString("bianminfuwu");
        // 1.3 是否收费
        String CHARGE_FLAG = obj.getString("CHARGE_FLAG");
        // 1.3 是否转向秒批秒办展示
        String turnmpmb = obj.getString("is_turnmpmb");
        // 1.3 是否马上办
        String mashangban = obj.getString("mashangban");
        // 1.3 是否网上办
        String wangshangban = obj.getString("wangshangban");
        // 1.3 是否6+1类
        String sixshenpilb = obj.getString("sixshenpilb");
        // 1.3 是否6+1类
        String yishenqing = obj.getString("ysq");
        // 是否跨省通办
        String kstbsx = obj.getString("kstbsx");
        // 是否跨省通办
        String qstb = obj.getString("qstb");
        // 1.3 是否全程通办
        String qctb = obj.getString("qctb");
        // 1.3 是否6+1类
        String isyc = obj.getString("isyc");
        // 是否行政权力
        String xzql = obj.getString("xzql");
        // 1.4 是否公共服务
        String ggfw = obj.getString("ggfw");
        // 1.4 是否公共服务
        String xzxk = obj.getString("xzxk");
        // 1.4 是否智能办
        String isznb = obj.getString("isznb");
        // 1.5 是否老年人事项
        String islnr = obj.getString("islnr");
        // 1.6是否智能搜索
        String znss = obj.getString("znss");

        String applyerType = "";
        // 1.8、获取申请人类型
        if ("getTaskList".equals(function)) {
            applyerType = obj.getString("usertype");
        } else {
            applyerType = obj.getString("applyertype");
        }
        // 1.9、获取主题当前页码
        String currentPageBusiness = obj.getString("currentpagebusiness");
        // 1.10、获取主题每页数目
        String pageSizeBusiness = obj.getString("pagesizebusiness");
        // 1.11、获取用户登录信息
        AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
        // 2、定义返回JSON对象
        JSONObject dataJson = new JSONObject();
        // 3、判断是否使用全文检索（0：查询全文检索 1：查询数据库）
        String isNeedQwjs = ConfigUtil.getConfigValue("isNeedQwjs");
        // 2022/06/17 阳佳 调整关键字查询逻辑 优先查找audit_task_keyword 如果能找到 则不走后面逻辑
        boolean fromKeyWord = false;
        List<String> list = null;
        try {
            if (StringUtil.isNotBlank(keyWord)) {
                //关键词搜索的集合
                list = iAuditTaskKeywordService.findListByKeyWord(keyWord, areaCode);
                if (ValidateUtil.isNotBlankCollection(list)) {
                    // 根据taskid找到最新的事项
                    SqlConditionUtil sqlUtils = new SqlConditionUtil();
                    sqlUtils.eq("is_enable", ZwfwConstant.CONSTANT_STR_ONE);
                    sqlUtils.eq("is_editafterimport", ZwfwConstant.CONSTANT_STR_ONE);
                    sqlUtils.isBlankOrValue("is_history", ZwfwConstant.CONSTANT_STR_ZERO);
                    sqlUtils.in("task_id", StringUtil.joinSql(list));
                    List<AuditTask> taskList = iAuditTask.getAllTask(sqlUtils.getMap()).getResult();
                    // 如果能找到则返回，不能找到则执行之前逻辑
                    if (ValidateUtil.isNotBlankCollection(taskList)) {
                        //同时查询原生的集合
                        List<String> list1 = iAuditTaskKeywordService.findListBytaskname(keyWord, areaCode);
                        list.addAll(list1);
                        fromKeyWord = true;
                    }
                }
            }
            boolean isXz = false;// 标识是否是乡镇延伸
            String znyd_task = ConfigUtil.getFrameConfigValue("znyd_task");
            if ("1".equals(isNeedQwjs)) {
                // 3.1、查询数据库方式
                // 3.1.1、根据查询条件获取该区域下的事项信息
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                if (StringUtil.isNotBlank(splb) && !splb.contains("all") && !"\"\"".equals(splb) && !splb.contains("全部")) {
                    sqlConditionUtil.in("SHENPILB", splb);// 部门标识
                }

                // 3.1.1.1、拼接事项查询条件
                if (StringUtil.isNotBlank(areaCode)) {
                    if (areaCode.length() == 6) {
                        if (!"1".equals(znss)) {
                            sqlConditionUtil.eq("a.areacode", areaCode);// 区域编码
                        } else {
                            sqlConditionUtil.eq("areacode", areaCode);// 区域编码
                        }
                    } else {
                        sqlConditionUtil.eq("d.areacode", areaCode);// 乡镇辖区编码
                        isXz = true;
                    }
                }
                if (StringUtil.isNotBlank(is_fugongfuchan) || StringUtil.isNotBlank(Dao_xc_num)
                        || StringUtil.isNotBlank(ISPYC) || StringUtil.isNotBlank(Operationscope)
                        || StringUtil.isNotBlank(If_express) || StringUtil.isNotBlank(CHARGE_FLAG)
                        || StringUtil.isNotBlank(sixshenpilb)) {
                    sqlConditionUtil.nq("taskcode", "''");
                }
                if (StringUtil.isNotBlank(is_fugongfuchan)) {
                    sqlConditionUtil.eq("is_fugongfuchan", is_fugongfuchan);// 部门标识
                }

                if (StringUtil.isNotBlank(ggfw)) {
                    sqlConditionUtil.eq("shenpilb", "11");// 部门标识
                }

                if (StringUtil.isNotBlank(islnr)) {
                    sqlConditionUtil.eq("islnr", "1");// 部门标识
                }

                if (StringUtil.isNotBlank(xzxk)) {
                    sqlConditionUtil.eq("shenpilb", "01");// 部门标识
                }

                if (StringUtil.isNotBlank(Dao_xc_num) && "1".equals(Dao_xc_num)) {
                    sqlConditionUtil.in("applyermin_count", "'0','1'");// 部门标识
                    sqlConditionUtil.eq("businesstype", "1");// 部门标识
                }
                if (StringUtil.isNotBlank(yishenqing) && "1".equals(yishenqing)) {
                    sqlConditionUtil.eq("businesstype", "1");
                    sqlConditionUtil.like("applyertype", applyerType);// 申请人类型
                }

                if ("1".equals(kstbsx)) {
                    sqlConditionUtil.eq("iskstb", "1");
                }
                if ("1".equals(qstb)) {
                    sqlConditionUtil.eq("isjnqstb", "1");
                }

                if ("1".equals(xzql)) {
                    sqlConditionUtil.nq("shenpilb", "11");
                }

                if (StringUtil.isNotBlank(ISPYC)) {
                    sqlConditionUtil.eq("ISPYC", "1");// 部门标识
                }

                if (StringUtil.isNotBlank(Operationscope)) {
                    sqlConditionUtil.in("CROSS_SCOPE", "'1','2','3'");// 部门标识
                }

                if (StringUtil.isNotBlank(If_express)) {
                    sqlConditionUtil.eq("IS_DELIVERY", "1");// 部门标识
                }

                if (StringUtil.isNotBlank(mpmb)) {
                    sqlConditionUtil.eq("is_mpmb", "1");// 部门标识
                }

                if (StringUtil.isNotBlank(bianminfuwu)) {
                    sqlConditionUtil.eq("is_bianminfuwu", "1");// 部门标识
                }

                if (StringUtil.isBlank(bianminfuwu) && StringUtil.isBlank(mpmb) && StringUtil.isBlank(isznb)) {
                    sqlConditionUtil.nq("shenpilb", "15");
                }

                if (StringUtil.isNotBlank(CHARGE_FLAG)) {
                    sqlConditionUtil.eq("CHARGE_FLAG", "1");// 部门标识
                }

                if (StringUtil.isNotBlank(Dao_xc_num) && "0".equals(Dao_xc_num)) {
                    sqlConditionUtil.eq("lpt", Dao_xc_num);// 主题分类标识
                }

                if (StringUtil.isNotBlank(ouGuid)) {
                    sqlConditionUtil.eq("a.ouguid", ouGuid);// 部门标识
                }
                //当申请类型type不为空的时候，进行筛选
                //&& (!"1".equals(isyc) || "1".equals(qctb))
                if (StringUtil.isNotBlank(applyerType)) {
                    sqlConditionUtil.like("applyertype", applyerType);// 申请人类型
                }

                if ("1".equals(isznb)) {
                    //sqlConditionUtil.like("applyertype", applyerType);// 申请人类型
                    sqlConditionUtil.eq("isznb", isznb);// 主题分类标识
                }

                if (StringUtil.isNotBlank(dictId)) {
                    //个人
                    if ("20".equals(applyerType)) {
                        sqlConditionUtil.eq("TASKCLASS_FORPERSION", dictId);// 主题分类标识
                    } else {
                        //法人
                        sqlConditionUtil.eq("TASKCLASS_FORCOMPANY", dictId);// 主题分类标识
                    }

                }
                if (StringUtil.isNotBlank(mashangban)) {
                    sqlConditionUtil.eq("mashangban", mashangban);// 主题分类标识
                }
                if (StringUtil.isNotBlank(wangshangban)) {
                    sqlConditionUtil.eq("wangshangban", wangshangban);// 主题分类标识
                }
                if (StringUtil.isNotBlank(qctb)) {
                    sqlConditionUtil.eq("qctb", qctb);// 主题分类标识
                }
                if (StringUtil.isNotBlank(sixshenpilb) && "1".equals(sixshenpilb)) {
                    // 主题分类标识
                    sqlConditionUtil.in("shenpilb", "'01','10','07','05','08','06','11'");
                }
                if (StringUtil.isNotBlank(keyWord)) {
                    // 3.1.1.2、过滤%和_，以免输入%或者_查出全部数据
                    // 2022/06/19 阳佳 如果要查audit_key_word表，则通过task_id过滤
                    if (fromKeyWord) {
                        sqlConditionUtil.in("task_id", StringUtil.joinSql(list));
                        //sqlConditionUtil.like("task_id in (" + StringUtil.joinSql(list) + ") or taskname ", keyWord);
                    } else {
                        if ("%".equals(keyWord) || "_".equals(keyWord)) {
                            String taskname = "/" + keyWord;
                            sqlConditionUtil.like("taskname", taskname);
                        } else {
                            sqlConditionUtil.like("taskname", keyWord);// 事项名称
                        }
                    }

                }

                sqlConditionUtil.eq("ISTEMPLATE", "0"); // 非模板事项
                sqlConditionUtil.eq("IS_EDITAFTERIMPORT", "1");// 审核通过后事项
                sqlConditionUtil.eq("IS_ENABLE", "1");// 启用事项
                sqlConditionUtil.isBlankOrValue("IS_HISTORY", "0"); // 判断是否为历史版本
                sqlConditionUtil.in("ifnull(iswtshow,'')", "'','1'");// 判断在网厅展示

                int firstResultTask = Integer.parseInt(currentPageTask) * Integer.parseInt(pageSizeTask);
                // 3.1.2、主项事项结果返回
                PageData<AuditTask> fatherTaskPageData = null;
                // 3.1.2.1、判断是否使用大小项；主项结尾为 000, 子项为XXX结尾
                String newCategory = iHandleConfig.getFrameConfig("AS_ITEM_CATEGORY", "").getResult();
                if (StringUtil.isNotBlank(newCategory) && ZwfwConstant.CONSTANT_STR_ONE.equals(newCategory)
                        && !"1".equals(turnmpmb) && !"1".equals(znss)) {
                    // 3.1.2.2、获取大项事项（区分大小项）
                    // 3.1.2.3、属于乡镇事项
                    if (isXz) {
                        fatherTaskPageData = iJnAppRestService.getXZGBAuditTaskPageData(sqlConditionUtil.getMap(), areaCode,
                                firstResultTask, Integer.parseInt(pageSizeTask), "ordernum", "desc").getResult();
                    }
                    // 3.1.2.4、不属于乡镇事项的主项列表
                    else {
                        fatherTaskPageData = iJnAppRestService.getGBAuditTaskPageData(sqlConditionUtil.getMap(),
                                firstResultTask, Integer.parseInt(pageSizeTask), "ordernum", "desc").getResult();
                    }
                } else {
                    // 3.1.2.5、乡镇事项可用事项
                    if (isXz) {
                        fatherTaskPageData = iAuditTaskDelegate.getXZpageDataList(sqlConditionUtil, firstResultTask,
                                Integer.parseInt(pageSizeTask), "ordernum", "desc").getResult();
                    }
                    // 3.1.2.6、不属于乡镇事项
                    else {
                        if ("1".equals(turnmpmb)) {
                            fatherTaskPageData = iJnAppRestService.getGBAuditTaskPageData(sqlConditionUtil.getMap(),
                                    firstResultTask, Integer.parseInt(pageSizeTask), "ordernum", "desc").getResult();
                        } else if ("1".equals(znss)) {
                            //搜索本级部门
                            StringBuilder ouguids = new StringBuilder();
                            List<FrameOu> oulist = iJnAppRestService.getOuListByAreacode(areaCode);
                            if (oulist != null && !oulist.isEmpty()) {
                                for (FrameOu frameOu : oulist) {
                                    ouguids.append("'" + frameOu.getOuguid() + "',");
                                }
                            }
                            if (StringUtil.isNotBlank(ouguids)) {
                                String ouguid = ouguids.substring(0, ouguids.length() - 1).toString();
                                sqlConditionUtil.in("ouguid", ouguid);
                            }
                            fatherTaskPageData = iAuditTask.getAuditTaskPageData(sqlConditionUtil.getMap(), firstResultTask,
                                    Integer.parseInt(pageSizeTask), "ordernum", "desc").getResult();
                        } else {
                            fatherTaskPageData = iHandleTask
                                    .getAuditTaskListByCondition(true, null, sqlConditionUtil.getMap(), firstResultTask,
                                            Integer.parseInt(pageSizeTask), "ordernum", "desc")
                                    .getResult();
                        }

                    }
                }
                int totalTaskCount = fatherTaskPageData.getRowCount();// 查询事项的数量
                List<AuditTask> fatherAuditTaskList = fatherTaskPageData.getList();// 查询事项的信息
                List<JSONObject> fatherTaskJsonList = new ArrayList<JSONObject>(); // 返回的事项JSON列表
                for (AuditTask fatherAuditTask : fatherAuditTaskList) {
                    // 3.1.3.1、获取事项基本信息拼接成返回的JSON
                    JSONObject fatherTaskJson = commonTaskJson(fatherAuditTask, auditOnlineRegister, "0");
                    if (isXz) {
                        // 3.1.3.2、如果主项被下放至当前区域，需要显示下放的部门名称
                        AuditTaskDelegate auditTaskDelegate = iAuditTaskDelegate
                                .findByTaskIDAndAreacode(fatherAuditTask.getTask_id(), areaCode).getResult();
                        if (auditTaskDelegate != null) {
                            fatherTaskJson.put("ouname", auditTaskDelegate.getOuname());// 部门名称
                        }
                    }
                    if (isXz) {
                        // 3.1.3.3、子项条件中先去除区域筛选，优先获取到主项下的所有子项， 然后找去子项下放到该区域的数据
                        Map<String, String> conditionMap = sqlConditionUtil.getMap();
                        if (conditionMap.containsKey("d.areacode" + ZwfwConstant.ZWFW_SPLIT + ZwfwConstant.SQL_OPERATOR_EQ
                                + ZwfwConstant.ZWFW_SPLIT + "S")) {
                            conditionMap.remove("d.areacode" + ZwfwConstant.ZWFW_SPLIT + ZwfwConstant.SQL_OPERATOR_EQ
                                    + ZwfwConstant.ZWFW_SPLIT + "S");
                        }
                    }
                    // 3.1.3.4、获取子项事项
                    if (StringUtil.isNotBlank(newCategory) && ZwfwConstant.CONSTANT_STR_ONE.equals(newCategory)
                            && !"1".equals(turnmpmb)) {
                        String item = fatherAuditTask.getItem_id();
                        Map<String, String> sqlmap = sqlConditionUtil.getMap();
                        SqlConditionUtil sqlConditionUtil1 = new SqlConditionUtil();
                        sqlConditionUtil1.eq("length(item_id)", "33");
                        sqlConditionUtil1.leftLike("item_id", item);
                        sqlConditionUtil1.nq("item_id", item);
                        sqlmap = sqlConditionUtil.getMap();
                        sqlmap.putAll(sqlConditionUtil1.getMap());
                        sqlmap.remove("taskname#zwfw#like#zwfw#S");
                        sqlmap.remove("webapplytype#zwfw#in#zwfw#S");
                        sqlmap.remove("dict_id#zwfw#eq#zwfw#S");
                        sqlmap.remove("mashangban#zwfw#eq#zwfw#S");
                        sqlmap.remove("wangshangban#zwfw#eq#zwfw#S");
                        sqlmap.remove("qctb#zwfw#eq#zwfw#S");
                        sqlmap.remove("lpt#zwfw#eq#zwfw#S");
                        sqlmap.remove("isznb#zwfw#eq#zwfw#S");
                        PageData<AuditTask> childTaskPageData = iAuditTask
                                .getAuditTaskPageData(sqlmap, 0, 0, "ordernum", "desc").getResult();
                        List<JSONObject> childTaskJsonList = new ArrayList<JSONObject>();
                        List<AuditTask> childAuditTasks = null;
                        if (childTaskPageData != null) {
                            childAuditTasks = childTaskPageData.getList();
                        }
                        if (childAuditTasks != null && childAuditTasks.size() > 0) {
                            for (AuditTask childAuditTask : childTaskPageData.getList()) {
                                JSONObject childTaskJson = commonTaskJson(childAuditTask, auditOnlineRegister, "1");
                                if (StringUtil.isNotBlank(znyd_task) && znyd_task.contains(childAuditTask.getItem_id())) {
                                    childTaskJson.put("znyd", 1);
                                }
                                childTaskJsonList.add(childTaskJson);
                            }
                            fatherTaskJson.put("haschildtask", "1");
                        }
                        fatherTaskJson.put("childtasklist", childTaskJsonList);
                        fatherTaskJson.put("childtaskcount", childTaskJsonList.size());
                    }
                    fatherTaskJsonList.add(fatherTaskJson);
                }
                dataJson.put("tasklist", fatherTaskJsonList);
                dataJson.put("totalcount", totalTaskCount);
                // 3.1.4、根据查询条件获取该区域下的套餐信息(头部搜索框)
                if (StringUtil.isNotBlank(currentPageBusiness) && StringUtil.isNotBlank(pageSizeBusiness)) {
                    SqlConditionUtil sqlConditionUtilBusiness = new SqlConditionUtil();
                    sqlConditionUtilBusiness.eq("businesstype", "2"); // 一般并联审批（套餐）
                    sqlConditionUtilBusiness.eq("del", "0"); // 非禁用的套餐
                    if (StringUtil.isNotBlank(areaCode)) {
                        sqlConditionUtilBusiness.eq("areacode", areaCode);
                    }
                    if (StringUtil.isNotBlank(keyWord)) {
                        // 过滤%和_，以免输入%或者_查出全部数据
                        if ("%".equals(keyWord) || "_".equals(keyWord)) {
                            String businessname = "/" + keyWord;
                            sqlConditionUtilBusiness.like("businessname", businessname);
                        } else {
                            sqlConditionUtilBusiness.like("businessname", keyWord);// 事项名称
                        }
                    }
                    int firstResultBusiness = Integer.parseInt(currentPageBusiness) * Integer.parseInt(pageSizeBusiness);
                    // 3.1.4.1、获取分页的套餐信息
                    PageData<AuditSpBusiness> auditSpBusinessPageData = iAuditSpBusiness
                            .getAuditSpBusinessByPage(sqlConditionUtilBusiness.getMap(), firstResultBusiness,
                                    Integer.parseInt(pageSizeBusiness), "ordernumber", "desc")
                            .getResult();
                    int totalBusinessCount = auditSpBusinessPageData.getRowCount();// 查询套餐的数量
                    List<AuditSpBusiness> auditSpBusinesses = auditSpBusinessPageData.getList();// 查询套餐的信息
                    List<JSONObject> businessJsonList = new ArrayList<JSONObject>();
                    for (AuditSpBusiness auditSpBusiness : auditSpBusinesses) {
                        JSONObject businessJson = new JSONObject();
                        businessJson.put("businessname", auditSpBusiness.getBusinessname()); // 套餐名称
                        businessJson.put("businessguid", auditSpBusiness.getRowguid()); // 套餐标识
                        businessJson.put("promiseday", (auditSpBusiness.getPromise_day() == null ? "0"
                                : auditSpBusiness.getPromise_day().toString()) + "工作日"); // 承诺期限
                        businessJson.put("linktel", auditSpBusiness.getLink_tel()); // 联系电话
                        businessJson.put("supervise_tel", auditSpBusiness.getSupervise_tel()); // 监督电话
                        businessJson.put("transact_time", auditSpBusiness.getTransact_time()); // 办理时间
                        businessJson.put("transact_addr", auditSpBusiness.getTransact_addr()); // 办理地点
                        businessJson.put("note", auditSpBusiness.getNote()); // 备注信息
                        businessJson.put("daoxcnum", StringUtil.isBlank(auditSpBusiness.getDao_xc_num()) ? ""
                                : iCodeItemsService.getItemTextByCodeName("现场次数", auditSpBusiness.getDao_xc_num()));// 到现场次数
                        businessJsonList.add(businessJson);
                    }
                    dataJson.put("businesslist", businessJsonList);
                    dataJson.put("businesstotalcount", totalBusinessCount);
                }
            } else {
                // 3.2、查询全文检索方式
                // 3.2.1、检索事项
                String searchWordsRangsTask = "taskname";// 搜索关键字的范围：事项名称
                List<SearchCondition> searchConditionTask = new ArrayList<SearchCondition>();
                List<SearchUnionCondition> searchUnionConditionTask = new ArrayList<SearchUnionCondition>();

                // 3.2.1.2、拼接事项相关的查询条件
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(onlineHandle)) {
                    // 3.2.1.2.1、事项是否在线办理
                    SearchCondition searchCondition = new SearchCondition();
                    searchCondition.setFieldName("webapplytype");
                    searchCondition.setEqual(onlineHandle);
                    searchCondition.setIsLike(true);
                    searchConditionTask.add(searchCondition);
                }
                if (StringUtil.isNotBlank(applyerType)) {
                    // 3.2.1.2.2、事项申请人类型
                    SearchCondition searchCondition = new SearchCondition();
                    searchCondition.setFieldName("applytype");
                    searchCondition.setEqual(applyerType);
                    searchCondition.setIsLike(true);
                    searchConditionTask.add(searchCondition);
                }
                if (StringUtil.isNotBlank(ouGuid)) {
                    // 3.2.1.2.3、事项部门标识
                    SearchCondition searchCondition = new SearchCondition();
                    searchCondition.setFieldName("ouguid");
                    searchCondition.setEqual(ouGuid);
                    searchConditionTask.add(searchCondition);
                }
                if (StringUtil.isNotBlank(dictId)) {
                    // 3.2.1.2.4、事项的主题分类标识
                    SearchCondition searchCondition = new SearchCondition();
                    searchCondition.setFieldName("taskdic");
                    searchCondition.setEqual(dictId);
                    searchConditionTask.add(searchCondition);
                }
                if (StringUtil.isNotBlank(areaCode)) {
                    // 3.2.1.2.5、事项的区域编码
                    SearchCondition searchCondition = new SearchCondition();
                    searchCondition.setFieldName("areacode");
                    searchCondition.setEqual(areaCode);
                    searchConditionTask.add(searchCondition);
                }
                // 3.2.1.3、获取系统参数中配置的事项分类号
                String categoryNumsTask = iHandleConfig.getFrameConfig("categoryNum_Task", "").getResult();
                // 3.2.1.4、获取全文检索接口地址
                String restIpConfig = ConfigUtil.getConfigValue("inteligentSearchRestIpConfig");
                String auditTasks = iHandleTask.getData(restIpConfig, categoryNumsTask, keyWord, searchConditionTask,
                                searchUnionConditionTask, searchWordsRangsTask,
                                String.valueOf(Integer.parseInt(currentPageTask) * Integer.parseInt(pageSizeTask)), pageSizeTask)
                        .getResult();
                // 3.2.1.5、解析返回的事项数据
                JSONObject taskJsonResult = JSONObject.parseObject(auditTasks).getJSONObject("result");
                String taskTotalCount = String.valueOf(taskJsonResult.get("totalcount"));// 总记录数
                int taskCount = Integer.parseInt(taskTotalCount);
                List<JSONObject> taskJsonList = new ArrayList<JSONObject>();
                if (taskCount > 0) {
                    JSONArray jsonArray = (JSONArray) taskJsonResult.get("records");
                    int size = jsonArray.size();
                    for (int i = 0; i < size; i++) {
                        JSONObject data = (JSONObject) jsonArray.get(i);
                        JSONObject taskJson = new JSONObject();
                        if (StringUtil.isNotBlank(znyd_task) && znyd_task.contains(data.getString("itemid"))) {
                            taskJson.put("znyd", ZwfwConstant.CONSTANT_STR_ONE);
                        }
                        taskJson.put("taskname", data.getString("taskname"));// 事项名称
                        taskJson.put("taskguid", data.getString("taskguid"));// 事项标识
                        taskJson.put("ouname", data.getString("ouname"));// 部门名称
                        taskJson.put("addressandtime", data.getString("addressandtime"));// 办理地点和时间,法律依据
                        taskJson.put("addressandtime", "");
                        taskJson.put("itemid", data.getString("itemid"));
                        taskJson.put("qlkind", data.getString("qlkind"));// 权力类型
                        taskJson.put("exerciseclass", data.getString("exerciseclass"));// 行使层级
                        taskJson.put("managementobj", data.getString("managementobj"));// 办理对象
                        taskJson.put("type", data.getString("type"));// 办理类型
                        taskJson.put("anticipateday",
                                data.getString("anticipateday") == null ? "" : data.getString("anticipateday") + "工作日");// 法定期限
                        taskJson.put("promiseday", data.getString("promiseday") + "工作日");// 承诺期限
                        taskJson.put("linktel", data.getString("linktel"));// 咨询电话
                        taskJson.put("supervisetel", data.getString("supervisetel"));// 监督投诉电话
                        taskJsonList.add(taskJson);
                    }
                }
                dataJson.put("tasklist", taskJsonList);
                dataJson.put("totalcount", taskCount);
                // 3.2.2、检索套餐数据(顶部搜索框)
                if (StringUtil.isNotBlank(currentPageBusiness) && StringUtil.isNotBlank(pageSizeBusiness)) {
                    String searchWordsRangsBusiness = "businessname";// 搜索关键字的范围
                    // 3.2.2.1、拼接套餐相关的查询条件
                    List<SearchCondition> searchConditionBusinesses = new ArrayList<SearchCondition>();// 额外的拼接的条件
                    if (StringUtil.isNotBlank(areaCode)) {
                        SearchCondition searchCondition = new SearchCondition();
                        searchCondition.setFieldName("areacode");
                        searchCondition.setEqual(areaCode);
                        searchConditionBusinesses.add(searchCondition);
                    }
                    // 3.2.2.2、获取系统参数中配置的套餐分类号
                    String categoryNumsBusiness = iHandleConfig.getFrameConfig("categoryNum_Business", "").getResult();
                    // 3.2.2.3、获取全文检索查询的套餐结果信息
                    String auditBusinesses = iHandleTask.getData(restIpConfig, categoryNumsBusiness, keyWord,
                            searchConditionBusinesses, searchUnionConditionTask, searchWordsRangsBusiness,
                            String.valueOf(Integer.parseInt(currentPageBusiness) * Integer.parseInt(pageSizeBusiness)),
                            pageSizeBusiness).getResult();
                    // 3.2.2.4、解析返回的套餐数据
                    JSONObject businessJsonResult = JSONObject.parseObject(auditBusinesses).getJSONObject("result");
                    String businessTotalCount = String.valueOf(businessJsonResult.get("totalcount"));// 总记录数
                    int businessCount = Integer.parseInt(businessTotalCount);
                    List<JSONObject> businessList = new ArrayList<JSONObject>();
                    if (businessCount > 0) {
                        JSONArray jsonArray = (JSONArray) businessJsonResult.get("records");
                        int size = jsonArray.size();
                        for (int i = 0; i < size; i++) {
                            JSONObject data = (JSONObject) jsonArray.get(i);
                            JSONObject businessJson = new JSONObject();
                            businessJson.put("businessname", data.getString("businessname"));// 主题名称
                            businessJson.put("businessguid", data.getString("businessguid"));// 主题标识
                            businessJson.put("promiseday", (StringUtil.isBlank(data.getString("promiseday")) ? "0"
                                    : data.getString("promiseday").toString()) + "工作日");// 承诺时限
                            businessJson.put("linktel", data.getString("linktel"));// 联系电话
                            businessJson.put("supervise_tel", data.getString("supervisetel"));// 监督电话
                            businessJson.put("transact_time", data.getString("transacttime"));// 办理时间
                            businessJson.put("transact_addr", data.getString("transact_addr"));// 办理地点
                            businessJson.put("note", data.getString("note"));// 备注
                            businessJson.put("daoxcnum", StringUtil.isBlank(data.getString("daoxcnum")) ? ""
                                    : iCodeItemsService.getItemTextByCodeName("现场次数", data.getString("daoxcnum")));// 到现场次数
                            businessList.add(businessJson);
                        }
                    }
                    dataJson.put("businesslist", businessList);
                    dataJson.put("businesstotalcount", businessCount);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("获取事项列表报错。。。。");
        }
        return dataJson;
    }


    /**
     * 通用方法获取事项列表
     *
     * @param obj     接口传入参数
     * @param request 请求头数据
     * @return
     */
    public JSONObject handleGetTaskListNew(JSONObject obj, HttpServletRequest request) {
        // 1.1、获取事项当前页码
        String currentPageTask = obj.getString("currentpage");
        // 1.2、获取事项每页数目
        String pageSizeTask = obj.getString("pagesize");
        // 1.3、获取搜索条件
        String keyWord = obj.getString("searchcondition");
        // 1.5、获取区域编码
        String areaCode = obj.getString("areacode");
        String applyerType = "";
        // 1.8、获取申请人类型
        applyerType = obj.getString("applyertype");
        // 2、定义返回JSON对象
        JSONObject dataJson = new JSONObject();
        List<String> list = null;
        Record record = new Record();
        boolean fromKeyWord = false;
        //查询数据库方式
        try {
            //关键字查询逻辑，查找audit_task_keyword
            System.out.println("handleGetTaskListNew======11111====" + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_MILLI_FORMAT));

            if (StringUtil.isNotBlank(keyWord)) {
                //关键词搜索的集合
                list = iAuditTaskKeywordService.findListByKeyWord(keyWord, areaCode);
                System.out.println("新事项搜索关键词集合：" + list);
                if (ValidateUtil.isNotBlankCollection(list)) {
                    fromKeyWord = true;
                    String taskids = StringUtils.join(list, ",");
                    record.set("taskids", taskids);
                }
            }
            System.out.println("handleGetTaskListNew======22222====" + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_MILLI_FORMAT));


            // 申请人类型
            if (StringUtil.isNotBlank(applyerType)) {
                record.set("applyertype", applyerType);
            }
            if (StringUtil.isNotBlank(keyWord)) {
                record.set("taskname", keyWord);

            }
            int firstResultTask = Integer.parseInt(currentPageTask) * Integer.parseInt(pageSizeTask);
            // 事项结果返回
            PageData<AuditTask> TaskPageData = iAuditTaskKeywordService.getTaskListByTaskName(record, firstResultTask, Integer.parseInt(pageSizeTask), fromKeyWord);
            int totalTaskCount = TaskPageData.getRowCount();// 查询事项的数量
            System.out.println("handleGetTaskListNew======33333====" + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_MILLI_FORMAT));
            List<AuditTask> AuditTaskList = TaskPageData.getList();// 查询事项的信息
            List<JSONObject> TaskJsonList = new ArrayList<JSONObject>(); // 返回的事项JSON列表
            for (AuditTask auditTask : AuditTaskList) {
                System.out.println("handleGetTaskListNew======3.1====" + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_MILLI_FORMAT));
                // 3.1.3.1、获取事项基本信息拼接成返回的JSON
                JSONObject taskJson = new JSONObject();
                //标识，首先是否展示市级的事项信息
                if (auditTask.getInt("iscity") > 0) {
                    //重新查询市本级事项信息
                    AuditTask auditTaskcity = iAuditTaskKeywordService.getTaskcityByTaskname(auditTask.getTaskname());
                    System.out.println("handleGetTaskListNew======3.2====" + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_MILLI_FORMAT));
                    if (auditTaskcity != null) {
                        AuditTaskExtension auditTaskExtension = iAuditTaskExtension.getTaskExtensionByTaskGuid(auditTaskcity.getRowguid(), true).getResult();
                        System.out.println("handleGetTaskListNew======3.3====" + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_MILLI_FORMAT));
                        taskJson.put("iscity", 1);
                        taskJson.put("taskguid", auditTaskcity.getRowguid());
                        taskJson.put("taskid", auditTaskcity.getTask_id());
                        taskJson.put("taskname", auditTaskcity.getTaskname());
                        String qlKind = auditTaskcity.getShenpilb();
                        if (StringUtil.isNotBlank(qlKind)) {
                            qlKind = iCodeItemsService.getItemTextByCodeName("审批类别", qlKind);
                        }
                        taskJson.put("qlkind", qlKind);// 事项权力类型
                        taskJson.put("itemid", auditTaskcity.getItem_id());
                        taskJson.put("ouname", auditTaskcity.getOuname());
                        taskJson.put("type", iCodeItemsService.getItemTextByCodeName("办件类型", String.valueOf(auditTaskcity.getType())));// 办件类型
                        String useLevel = "";
                        if (auditTaskExtension != null) {
                            useLevel = auditTaskExtension.getUse_level();// 行使层级
                        }
                        String exerciseClass = "";
                        if (StringUtil.isNotBlank(useLevel)) {
                            String[] userlevelarr = useLevel.split(";");
                            for (int i = 0; i < userlevelarr.length; i++) {
                                exerciseClass = iCodeItemsService.getItemTextByCodeName("行使层级", userlevelarr[i]) + ";";
                            }
                            exerciseClass = exerciseClass.substring(0, exerciseClass.length() - 1);
                        }
                        taskJson.put("exerciseclass", exerciseClass);
                        taskJson.put("linktel", auditTaskcity.getLink_tel());
                        Integer anticipateday = auditTaskcity.getAnticipate_day();//法定期限天数
                        String anticipatetype = "";
                        if (StringUtil.isNotBlank(auditTaskcity.getStr("ANTICIPATE_TYPE"))) {
                            anticipatetype = iCodeItemsService.getItemTextByCodeName("国标_时限单位", auditTaskcity.getStr("ANTICIPATE_TYPE"));
                        }
                        if (StringUtil.isBlank(anticipatetype)) {
                            if (StringUtil.isBlank(anticipateday)) {
                                taskJson.put("anticipateday", "无");// 法定期限
                            } else {
                                taskJson.put("anticipateday", auditTaskcity.getAnticipate_day() + "个工作日");// 法定期限
                            }
                        } else {
                            if ("4".equals(auditTaskcity.getStr("ANTICIPATE_TYPE"))) {
                                taskJson.put("anticipateday", "当场");
                            } else {
                                taskJson.put("anticipateday", auditTaskcity.getAnticipate_day() + "个" + anticipatetype);
                            }
                        }

                        Integer promiseday = auditTaskcity.getPromise_day();//承诺期限天数
                        String promisetype = "";
                        if (StringUtil.isNotBlank(auditTaskcity.getPromise_type())) {
                            promisetype = iCodeItemsService.getItemTextByCodeName("国标_时限单位", auditTaskcity.getPromise_type());
                        }
                        if (StringUtil.isBlank(promisetype)) {
                            if (StringUtil.isBlank(promiseday)) {
                                taskJson.put("promiseday", "无");// 承诺期限
                            } else {
                                taskJson.put("promiseday", auditTaskcity.getPromise_day() + "个工作日");// 承诺期限
                            }
                        } else {
                            if ("4".equals(auditTaskcity.getPromise_type())) {
                                taskJson.put("promiseday", "当场");
                            } else {
                                taskJson.put("promiseday", auditTaskcity.getPromise_day() + "个" + promisetype);
                            }
                        }
                    }
                    System.out.println("handleGetTaskListNew======3.4====" + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_MILLI_FORMAT));

                    List<JSONObject> arealist = new ArrayList<>();
                    List<CodeItems> codeItems = iCodeItemsService.listCodeItemsByCodeName("辖区对应关系");
                    for (CodeItems codeitem : codeItems) {
                        JSONObject area = new JSONObject();
                        area.put("areaname", codeitem.getItemText());
                        area.put("addrGuid", codeitem.getItemValue());
                        boolean is_highlight = iAuditTaskKeywordService.Ishighlight(codeitem.getItemValue(), auditTask.getTaskname());
                        area.put("not_allow", is_highlight);
                        arealist.add(area);
                    }
                    System.out.println("handleGetTaskListNew======3.5====" + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_MILLI_FORMAT));
                    taskJson.put("addList", arealist);
                } else {
                    taskJson.put("taskname", auditTask.getTaskname());
                    List<JSONObject> arealist = new ArrayList<>();
                    List<CodeItems> codeItems = iCodeItemsService.listCodeItemsByCodeName("辖区对应关系");
                    for (CodeItems codeitem : codeItems) {
                        JSONObject area = new JSONObject();
                        area.put("areaname", codeitem.getItemText());
                        area.put("addrGuid", codeitem.getItemValue());
                        boolean is_highlight = iAuditTaskKeywordService.Ishighlight(codeitem.getItemValue(), auditTask.getTaskname());
                        area.put("not_allow", is_highlight);
                        arealist.add(area);
                    }
                    taskJson.put("iscity", 0);
                    taskJson.put("addList", arealist);
                    System.out.println("handleGetTaskListNew======3.6====" + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_MILLI_FORMAT));
                }
                TaskJsonList.add(taskJson);
            }
            System.out.println("handleGetTaskListNew======4444====" + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_MILLI_FORMAT));
            dataJson.put("tasklist", TaskJsonList);
            dataJson.put("totalcount", totalTaskCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataJson;
    }


    /**
     * 获取事项列表
     * 【提供爱山东，勿改动入参返回】
     *
     * @param obj 接口传入参数
     * @return
     */
    public JSONObject handleGetTaskListSearch(JSONObject obj) {
        // 获取事项当前页码
        String currentPageTask = obj.getString("currentpage");
        // 获取事项每页数目
        String pageSizeTask = obj.getString("pagesize");
        // 获取搜索条件
        String keyWord = obj.getString("searchcondition");
        // 获取区域编码
        String areaCode = obj.getString("areacode");
        // 获取申请人类型
        String applyerType = obj.getString("applyertype");
        // 2、定义返回JSON对象
        JSONObject dataJson = new JSONObject();
        List<String> list = null;
        try {
            // 3.2、查询全文检索方式
            // 3.2.1、检索事项
            String searchWordsRangsTask = "taskname;tasknametx";// 搜索关键字的范围：事项名称
            List<SearchCondition> searchConditionTask = new ArrayList<SearchCondition>();
            List<SearchUnionCondition> searchUnionConditionTask = new ArrayList<SearchUnionCondition>();
            if (StringUtil.isNotBlank(applyerType)) {
                // 3.2.1.2.2、事项申请人类型
                SearchCondition searchCondition = new SearchCondition();
                searchCondition.setFieldName("applytype");
                searchCondition.setEqual(applyerType);
                searchCondition.setIsLike(true);
                searchConditionTask.add(searchCondition);
            }

            // 3.2.1.3、获取系统参数中配置的事项分类号
            String categoryNumsTask = iHandleConfig.getFrameConfig("categoryNum_Task", "").getResult();
            // 3.2.1.4、获取全文检索接口地址
            String restIpConfig = ConfigUtil.getConfigValue("inteligentSearchRestIpConfig");
            log.info(keyWord + ":" + EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
            String auditTasks = iHandleTask.getData(restIpConfig, categoryNumsTask, keyWord, searchConditionTask,
                            searchUnionConditionTask, searchWordsRangsTask,
                            String.valueOf(Integer.parseInt(currentPageTask) * Integer.parseInt(pageSizeTask)), pageSizeTask)
                    .getResult();
            log.info(keyWord + ":" + EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
            // 3.2.1.5、解析返回的事项数据
            JSONObject taskJsonResult = JSONObject.parseObject(auditTasks).getJSONObject("result");

            String taskTotalCount = String.valueOf(taskJsonResult.get("totalcount"));// 总记录数
            log.info("taskTotalCount:"+taskTotalCount);
            int taskCount = 0;
            if(taskTotalCount!=null){
                taskCount = Integer.parseInt(taskTotalCount);
            }else{
                log.info("auditTasks:"+auditTasks);
            }
            List<JSONObject> taskJsonList = new ArrayList<JSONObject>();
            Set<String> taskname = new HashSet<>();
            if (taskCount > 0) {
                JSONArray jsonArray = (JSONArray) taskJsonResult.get("records");
                int size = jsonArray.size();
                log.info(keyWord + ":" + EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                log.info("size:" + size);
                List<JSONObject> arealist = new ArrayList<>();
                List<CodeItems> codeItems = iCodeItemsService.listCodeItemsByCodeName("辖区对应关系");
                log.info(keyWord + ":" + EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                String tasknames = jsonArray.stream().map(obj11 -> {
                    JSONObject jsonObj = (JSONObject) obj11;
                    return jsonObj.getString("taskname");
                }).collect(Collectors.joining("','"));
                log.info(keyWord + ":" + tasknames);
                Map<String, String> tasknametoarea = new HashMap<>();
                List<Record> records = iAuditTaskKeywordService.highlightareacode(tasknames);
                if (CollectionUtils.isNotEmpty(records)) {
                    for (Record record : records) {
                        String name = record.get("taskname");
                        String areacode = record.get("areacode");
                        if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(areacode)) {
                            if (tasknametoarea.containsKey(name)) {
                                String oldareacode = tasknametoarea.get(name);
                                if (oldareacode.indexOf(areacode) < 0) {
                                    tasknametoarea.put(name, oldareacode + "," + areacode);
                                }
                            } else {
                                tasknametoarea.put(name, areacode);
                            }
                        }
                    }
                }
                for (int i = 0; i < size; i++) {
                    JSONObject data = (JSONObject) jsonArray.get(i);
                    JSONObject taskJson = new JSONObject();
                    //对同名的过滤
                    if (taskname.contains(data.getString("taskname"))) {
                        taskCount--;
                        continue;
                    } else {
                        taskname.add(data.getString("taskname"));
                    }
                    //对无辖区的过滤
                    String areacodes = tasknametoarea.get(data.getString("taskname"));
                    log.info("====areacodes==="+areacodes);
                    if (StringUtils.isBlank(areacodes)) {
                        taskCount--;
                        continue;
                    }
                    //标识，首先是否展示市级的事项信息
                    if (data.getInteger("iscity") > 0) {
                        //重新查询市本级事项信息
                        log.info(keyWord + ":" + EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                        AuditTask auditTaskcity = iAuditTaskKeywordService.getTaskcityByTaskname(data.getString("taskname"));
                        log.info(keyWord + ":" + EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                        if (auditTaskcity != null) {
                            AuditTaskExtension auditTaskExtension = iAuditTaskExtension.getTaskExtensionByTaskGuid(auditTaskcity.getRowguid(), true).getResult();
                            taskJson.put("iscity", 1);
                            taskJson.put("taskguid", auditTaskcity.getRowguid());
                            taskJson.put("taskid", auditTaskcity.getTask_id());
                            taskJson.put("taskname", auditTaskcity.getTaskname());
                            String qlKind = auditTaskcity.getShenpilb();
                            if (StringUtil.isNotBlank(qlKind)) {
                                qlKind = iCodeItemsService.getItemTextByCodeName("审批类别", qlKind);
                            }
                            taskJson.put("qlkind", qlKind);// 事项权力类型
                            taskJson.put("itemid", auditTaskcity.getItem_id());
                            taskJson.put("ouname", auditTaskcity.getOuname());
                            taskJson.put("type", iCodeItemsService.getItemTextByCodeName("办件类型", String.valueOf(auditTaskcity.getType())));// 办件类型
                            String useLevel = "";
                            if (auditTaskExtension != null) {
                                useLevel = auditTaskExtension.getUse_level();// 行使层级
                            }
                            String exerciseClass = "";
                            if (StringUtil.isNotBlank(useLevel)) {
                                String[] userlevelarr = useLevel.split(";");
                                for (int j = 0; j < userlevelarr.length; j++) {
                                    exerciseClass = iCodeItemsService.getItemTextByCodeName("行使层级", userlevelarr[j]) + ";";
                                }
                                exerciseClass = exerciseClass.substring(0, exerciseClass.length() - 1);
                            }
                            taskJson.put("exerciseclass", exerciseClass);
                            taskJson.put("linktel", auditTaskcity.getLink_tel());
                            Integer anticipateday = auditTaskcity.getAnticipate_day();//法定期限天数
                            String anticipatetype = "";
                            if (StringUtil.isNotBlank(auditTaskcity.getStr("ANTICIPATE_TYPE"))) {
                                anticipatetype = iCodeItemsService.getItemTextByCodeName("国标_时限单位", auditTaskcity.getStr("ANTICIPATE_TYPE"));
                            }
                            if (StringUtil.isBlank(anticipatetype)) {
                                if (StringUtil.isBlank(anticipateday)) {
                                    taskJson.put("anticipateday", "无");// 法定期限
                                } else {
                                    taskJson.put("anticipateday", auditTaskcity.getAnticipate_day() + "个工作日");// 法定期限
                                }
                            } else {
                                if ("4".equals(anticipatetype)) {
                                    taskJson.put("anticipateday", "当场");
                                } else {
                                    taskJson.put("anticipateday", auditTaskcity.getAnticipate_day() + "个" + anticipatetype);
                                }
                            }

                            Integer promiseday = auditTaskcity.getPromise_day();//承诺期限天数
                            String promisetype = "";
                            if (StringUtil.isNotBlank(auditTaskcity.getPromise_type())) {
                                promisetype = iCodeItemsService.getItemTextByCodeName("国标_时限单位", auditTaskcity.getPromise_type());
                            }
                            if (StringUtil.isBlank(promisetype)) {
                                if (StringUtil.isBlank(promiseday)) {
                                    taskJson.put("promiseday", "无");// 承诺期限
                                } else {
                                    taskJson.put("promiseday", auditTaskcity.getPromise_day() + "个工作日");// 承诺期限
                                }
                            } else {
                                if ("4".equals(promisetype)) {
                                    taskJson.put("promiseday", "当场");
                                } else {
                                    taskJson.put("promiseday", auditTaskcity.getPromise_day() + "个" + promisetype);
                                }
                            }
                            log.info(keyWord + ":" + EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                            exerciseClass="市级";
                            boolean isxianji=false;
                            for (CodeItems codeitem : codeItems) {
                                JSONObject area = new JSONObject();
                                area.put("areaname", codeitem.getItemText());
                                area.put("addrGuid", codeitem.getItemValue());
                                if (StringUtils.isNotBlank(areacodes) && areacodes.contains(codeitem.getItemValue())) {
                                    area.put("not_allow", true);
                                    if(!"370800".equals(codeitem.getItemValue())) {
                                        isxianji = true;
                                    }
                                } else {
                                    area.put("not_allow", false);
                                }
                                arealist.add(area);
                            }
                            if(isxianji){
                                exerciseClass+=",县级";
                            }

                            boolean isxiangzhen=false;
                            String[] split = areacodes.split(",");
                            if(split.length>1) {
                                for (String areacode : split) {
                                    boolean is_highlight =iAuditTaskKeywordService.IsTownTask(areacode, auditTaskcity.getTaskname());
                                    if (is_highlight) {
                                        isxiangzhen = true;
                                    }
                                }
                                if (isxiangzhen) {
                                    exerciseClass += ",镇级";
                                }

                            }
                            taskJson.put("exerciseclass", exerciseClass);
                            taskJson.put("addList", arealist);
                            arealist = new ArrayList<>();
                            taskJsonList.add(taskJson);
                        } else {
                            taskCount--;
                        }
                    } else {
                        //AuditTask auditTask = iAuditTaskKeywordService.getTaskcityByTaskname(data.getString("taskname"));
                        log.info("data-taskname==="+data.getString("taskname"));
                        log.info("areacodes==="+areacodes);
                        List<AuditTask> auditTasklist = iAuditTaskKeywordService.getTaskByTaskname(data.getString("taskname"), areacodes);
                        AuditTask auditTask=null;
                        if (CollectionUtils.isNotEmpty(auditTasklist)) {
                            auditTask=auditTasklist.get(0);
                        }
                        taskJson.put("taskname", data.getString("taskname"));
                        if(auditTask!=null) {
                            taskJson.put("taskguid", auditTask.getRowguid());
                            taskJson.put("taskid", auditTask.getTask_id());
                            String qlKind = auditTask.getShenpilb();
                            if (StringUtil.isNotBlank(qlKind)) {
                                qlKind = iCodeItemsService.getItemTextByCodeName("审批类别", qlKind);
                            }
                            taskJson.put("qlkind", qlKind);// 事项权力类型
                            taskJson.put("itemid", auditTask.getItem_id());
                            taskJson.put("ouname", auditTask.getOuname());
                            taskJson.put("type", iCodeItemsService.getItemTextByCodeName("办件类型", String.valueOf(auditTask.getType())));// 办件类型
                            taskJson.put("linktel", auditTask.getLink_tel());
                            Integer anticipateday = auditTask.getAnticipate_day();//法定期限天数
                            String anticipatetype = "";
                            if (StringUtil.isNotBlank(auditTask.getStr("ANTICIPATE_TYPE"))) {
                                anticipatetype = iCodeItemsService.getItemTextByCodeName("国标_时限单位", auditTask.getStr("ANTICIPATE_TYPE"));
                            }
                            if (StringUtil.isBlank(anticipatetype)) {
                                if (StringUtil.isBlank(anticipateday)) {
                                    taskJson.put("anticipateday", "无");// 法定期限
                                } else {
                                    taskJson.put("anticipateday", auditTask.getAnticipate_day() + "个工作日");// 法定期限
                                }
                            } else {
                                if ("4".equals(anticipatetype)) {
                                    taskJson.put("anticipateday", "当场");
                                } else {
                                    taskJson.put("anticipateday", auditTask.getAnticipate_day() + "个" + anticipatetype);
                                }
                            }

                            Integer promiseday = auditTask.getPromise_day();//承诺期限天数
                            String promisetype = "";
                            if (StringUtil.isNotBlank(auditTask.getPromise_type())) {
                                promisetype = iCodeItemsService.getItemTextByCodeName("国标_时限单位", auditTask.getPromise_type());
                            }
                            if (StringUtil.isBlank(promisetype)) {
                                if (StringUtil.isBlank(promiseday)) {
                                    taskJson.put("promiseday", "无");// 承诺期限
                                } else {
                                    taskJson.put("promiseday", auditTask.getPromise_day() + "个工作日");// 承诺期限
                                }
                            } else {
                                if ("4".equals(promisetype)) {
                                    taskJson.put("promiseday", "当场");
                                } else {
                                    taskJson.put("promiseday", auditTask.getPromise_day() + "个" + promisetype);
                                }
                            }
                        }
                        for (CodeItems codeitem : codeItems) {
                            JSONObject area = new JSONObject();
                            area.put("areaname", codeitem.getItemText());
                            area.put("addrGuid", codeitem.getItemValue());
                            if (StringUtils.isNotBlank(areacodes) && areacodes.contains(codeitem.getItemValue())) {
                                area.put("not_allow", true);
                                taskJson.put("exerciseclass", "县级");
                            } else {
                                area.put("not_allow", false);
                            }
                            arealist.add(area);
                        }
                        log.info(keyWord + ":" + EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
                        taskJson.put("iscity", 0);
                        if(auditTask!=null) {
                            taskJson.put("iscity", 1);
                        }
                        taskJson.put("addList", arealist);
                        arealist = new ArrayList<>();
                        taskJsonList.add(taskJson);
                    }

                }
            }
            dataJson.put("tasklist", taskJsonList);
            dataJson.put("totalcount", taskCount);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("获取事项列表报错。。。。");
        }
        return dataJson;
    }



    /**
     * 获取搜索页弹窗数据
     *【提供爱山东，勿动】
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getPopInfo", method = RequestMethod.POST)
    public String getPopInfo(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getPopInfo接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String taskname = obj.getString("taskname");
                String areacode = obj.getString("areacode");
                JSONObject datajson = new JSONObject();
                JSONObject info = new JSONObject();
                if (StringUtil.isNotBlank(areacode)) {
                    //查找区一级事项
                    if (areacode.length() == 6) {
                        List<AuditTask> auditTasks = iAuditTaskKeywordService.getTaskDistrictByTaskname(taskname, areacode);
                        if (CollectionUtils.isNotEmpty(auditTasks)) {
                            AuditTask auditTask = auditTasks.get(0);
                                info.put("itemName", auditTask.getTaskname());
                                info.put("itemDept", auditTask.getOuname());
                                info.put("taskguid", auditTask.getRowguid());
                                info.put("taskid", auditTask.getTask_id());
                                info.put("applytype", auditTask.getApplyertype());
                            datajson.put("info", info);
                        }
                        //同时找到辖区开头为这个的所有镇
                        List<JSONObject> arealist = new ArrayList<>();
                        List<AuditOrgaArea> orgaAreas = iAuditTaskKeywordService.getOrgaAreaListByAreacodeAndType(areacode, "1");
                        for (AuditOrgaArea auditOrgaArea : orgaAreas) {
                            JSONObject area = new JSONObject();
                            area.put("areaname", auditOrgaArea.getXiaquname());
                            area.put("addrGuid", auditOrgaArea.getXiaqucode());
                            boolean is_highlight = iAuditTaskKeywordService.IshighlightTown(auditOrgaArea.getXiaqucode(), taskname);
                            area.put("not_allow", is_highlight);
                            arealist.add(area);
                        }
                        datajson.put("addList", arealist);
                    }
                    //镇
                    else if (areacode.length() == 9) {
                        List<AuditTask> auditTasks = iAuditTaskKeywordService.getTaskTownByTaskname(taskname, areacode);
                        if (CollectionUtils.isNotEmpty(auditTasks)) {
                            AuditTask auditTask = auditTasks.get(0);
                                info.put("itemName", auditTask.getTaskname());
                                info.put("itemDept", auditTask.getOuname());
                                info.put("taskguid", auditTask.getRowguid());
                                info.put("taskid", auditTask.getTask_id());
                                info.put("applytype", auditTask.getApplyertype());
                            datajson.put("info", info);
                        }
                        //找到所属的所有村
                        List<JSONObject> arealist = new ArrayList<>();
                        List<AuditOrgaArea> orgaAreas = iAuditTaskKeywordService.getOrgaAreaListByAreacodeAndType(areacode, "2");
                        for (AuditOrgaArea auditOrgaArea : orgaAreas) {
                            JSONObject area = new JSONObject();
                            area.put("areaname", auditOrgaArea.getXiaquname());
                            area.put("addrGuid", auditOrgaArea.getXiaqucode());
                            boolean is_highlight = iAuditTaskKeywordService.IshighlightTown(auditOrgaArea.getXiaqucode(), taskname);
                            area.put("not_allow", is_highlight);
                            arealist.add(area);
                        }
                        datajson.put("addList", arealist);
                    }
                    //村
                    else if (areacode.length() == 12) {
                        List<AuditTask> auditTasks =iAuditTaskKeywordService.getTaskTownByTaskname(taskname, areacode);
                        if (CollectionUtils.isNotEmpty(auditTasks)) {
                            AuditTask auditTask = auditTasks.get(0);
                                info.put("itemName", auditTask.getTaskname());
                                info.put("itemDept", auditTask.getOuname());
                            datajson.put("info", info);
                        }
                    }
                }
                log.info("=======结束调用getPopInfo接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取弹窗信息成功", datajson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getPopInfo接口参数：params【" + params + "】=======");
            log.info("=======getPopInfo异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取搜索页弹窗数据
     *
     * @param params 接口的入参
     * @return˙
     */
    @RequestMapping(value = "/getPopInfo2", method = RequestMethod.POST)
    public String getPopInfo2(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getPopInfo2接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String taskname = obj.getString("taskname");
                String areacode = obj.getString("areacode");
                JSONObject datajson = new JSONObject();
                JSONArray infos = new JSONArray();
                if (StringUtil.isNotBlank(areacode)) {
                    //查找区一级事项
                    if (areacode.length() == 6) {
                        List<AuditTask> auditTasks = iAuditTaskKeywordService.getTaskDistrictByTaskname(taskname, areacode);
                        if (CollectionUtils.isNotEmpty(auditTasks)) {
                            for (AuditTask auditTask : auditTasks) {
                                JSONObject info = new JSONObject();
                                info.put("itemName", auditTask.getTaskname());
                                info.put("itemDept", auditTask.getOuname());
                                info.put("taskguid", auditTask.getRowguid());
                                info.put("taskid", auditTask.getTask_id());
                                info.put("applytype", auditTask.getApplyertype());
                                infos.add(info);
                            }
                            datajson.put("infos", infos);
                        }
                        //同时找到辖区开头为这个的所有镇
                        List<JSONObject> arealist = new ArrayList<>();
                        List<AuditOrgaArea> orgaAreas = iAuditTaskKeywordService.getOrgaAreaListByAreacodeAndType(areacode, "1");
                        for (AuditOrgaArea auditOrgaArea : orgaAreas) {
                            JSONObject area = new JSONObject();
                            area.put("areaname", auditOrgaArea.getXiaquname());
                            area.put("addrGuid", auditOrgaArea.getXiaqucode());
                            boolean is_highlight = iAuditTaskKeywordService.IshighlightTown(auditOrgaArea.getXiaqucode(), taskname);
                            area.put("not_allow", is_highlight);
                            arealist.add(area);
                        }
                        datajson.put("addList", arealist);
                    }
                    //镇
                    else if (areacode.length() == 9) {
                        List<AuditTask> auditTasks = iAuditTaskKeywordService.getTaskTownByTaskname(taskname, areacode);
                        if (CollectionUtils.isNotEmpty(auditTasks)) {
                            for (AuditTask auditTask : auditTasks) {
                                JSONObject info = new JSONObject();
                                info.put("itemName", auditTask.getTaskname());
                                info.put("itemDept", auditTask.getOuname());
                                info.put("taskguid", auditTask.getRowguid());
                                info.put("taskid", auditTask.getTask_id());
                                info.put("applytype", auditTask.getApplyertype());
                                infos.add(info);
                            }
                            datajson.put("infos", infos);
                        }
                        //找到所属的所有村
                        List<JSONObject> arealist = new ArrayList<>();
                        List<AuditOrgaArea> orgaAreas = iAuditTaskKeywordService.getOrgaAreaListByAreacodeAndType(areacode, "2");
                        for (AuditOrgaArea auditOrgaArea : orgaAreas) {
                            JSONObject area = new JSONObject();
                            area.put("areaname", auditOrgaArea.getXiaquname());
                            area.put("addrGuid", auditOrgaArea.getXiaqucode());
                            boolean is_highlight = iAuditTaskKeywordService.IshighlightTown(auditOrgaArea.getXiaqucode(), taskname);
                            area.put("not_allow", is_highlight);
                            arealist.add(area);
                        }
                        datajson.put("addList", arealist);
                    }
                    //村
                    else if (areacode.length() == 12) {
                        List<AuditTask> auditTasks = iAuditTaskKeywordService.getTaskTownByTaskname(taskname, areacode);
                        if (CollectionUtils.isNotEmpty(auditTasks)) {
                            for (AuditTask auditTask : auditTasks) {
                                JSONObject info = new JSONObject();
                                info.put("itemName", auditTask.getTaskname());
                                info.put("itemDept", auditTask.getOuname());
                                infos.add(info);
                            }
                            datajson.put("infos", infos);
                        }
                    }
                }
                log.info("=======结束调用getPopInfo2接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取弹窗信息成功", datajson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getPopInfo2接口参数：params【" + params + "】=======");
            log.info("=======getPopInfo2异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取失败：" + e.getMessage(), "");
        }
    }


    /**
     * 获取通用的json返回 getTaskListByCondition用
     *
     * @param auditTask
     * @param auditOnlineRegister
     * @return
     */
    public JSONObject commonTaskJson(AuditTask auditTask, AuditOnlineRegister auditOnlineRegister, String type) {
        JSONObject auditTaskJson = new JSONObject();
        try {
            AuditTaskExtension auditTaskExtension = iAuditTaskExtension
                    .getTaskExtensionByTaskGuid(auditTask.getRowguid(), true).getResult();
            AuditTask task1 = iAuditTask.getAuditTaskByGuid(auditTask.getRowguid(), true).getResult();
            if (task1 != null) {
                auditTask = task1;
                auditTaskJson.put("taskname", auditTask.getTaskname());// 事项名称
                auditTaskJson.put("taskguid", auditTask.getRowguid());// 事项标识
                auditTaskJson.put("taskid", auditTask.getTask_id());// 事项唯一标识
                auditTaskJson.put("ouname", auditTask.getOuname());// 部门名称
                auditTaskJson.put("usescope", auditTaskExtension.get("usescope"));// 适用范围

                String ywztcode = auditTask.getStr("ywztcode");
                if (ywztcode.contains(";")) {
                    String[] ywztcodes = ywztcode.split(";");
                    if (ywztcodes.length >= 2) {
                        auditTaskJson.put("ywztcode", ywztcodes[1]);
                    }
                } else {
                    auditTaskJson.put("ywztcode", "");
                }

                String truntype = auditTask.getStr("turn_type");
                if (StringUtil.isNotBlank(truntype)) {
                    auditTaskJson.put("truntype", truntype);
                } else {
                    auditTaskJson.put("truntype", "0");
                }

                String znbtype = "0";

                if ("1".equals(auditTask.getStr("is_mpmb"))) {
                    znbtype = "1";
                    auditTaskJson.put("znbtype", znbtype);
                    auditTaskJson.put("typename", "秒批秒办");
                } else if ("1".equals(auditTask.getStr("isbigshow"))) {
                    znbtype = "2";
                    auditTaskJson.put("znbtype", znbtype);
                    auditTaskJson.put("typename", "全程电子化");
                }

                auditTaskJson.put("znbtype", znbtype);

                String wzdsrowguid = auditTask.getStr("wzds_rowguid");
                if (StringUtil.isNotBlank(wzdsrowguid)) {
                    auditTaskJson.put("haswzdsrowguid", "1");
                    auditTaskJson.put("wzdsrowguid", auditTask.getStr("wzds_rowguid"));
                } else {
                    auditTaskJson.put("haswzdsrowguid", "0");
                    auditTaskJson.put("wzdsrowguid", "0");
                }

                if ("1".equals(truntype)) {
                    auditTaskJson.put("turnzj", auditTaskExtension.get("turnzj"));
                } else if ("3".equals(truntype)) {
                    String turncliengguid = auditTaskExtension.get("turnattachguid");
                    if (StringUtil.isNotBlank(turncliengguid)) {
                        List<FrameAttachInfo> attachs = iAttachService.getAttachInfoListByGuid(turncliengguid);
                        if (attachs.size() > 0) {
                            FrameAttachInfo attach = attachs.get(0);
                            auditTaskJson.put("turnzj", attach.getAttachGuid());
                        }
                    }
                } else if ("4".equals(truntype)) {
                    auditTaskJson.put("turnzj", auditTaskExtension.get("turncontent"));
                } else {
                    auditTaskJson.put("turnzj", "0");
                }

                // 判断如果用户已收藏该事项后，收藏灯需要点亮
                if (auditOnlineRegister != null) {
                    String taskid = auditTask.getTask_id();
                    if (isCollect(taskid, auditOnlineRegister.getAccountguid())) {
                        // type为一，表示小项的收藏结果
                        if (ZwfwConstant.CONSTANT_STR_ONE.equals(type)) {
                            auditTaskJson.put("ischildcollect", "1");// 返回改事项已收藏标识
                        } else {
                            auditTaskJson.put("iscollect", "1");// 返回改事项已收藏标识
                        }
                    }
                    auditTaskJson.put("islogin", "1");// 是否登录
                }
                String timeAndAddress = (StringUtil.isBlank(auditTask.getTransact_addr()) ? "" : auditTask.getTransact_addr())
                        + (StringUtil.isBlank(auditTask.getTransact_time()) ? "" : auditTask.getTransact_time()); // 办理地点和办理时间
                String byLaw = StringUtil.isBlank(auditTask.getBy_law()) ? "" : auditTask.getBy_law();
                auditTaskJson.put("addressandtime", timeAndAddress + byLaw);// 办理地点和办理时间+法律依据
                auditTaskJson.put("webapptype", auditTaskExtension.getWebapplytype()); // 网上申报类型
                auditTaskJson.put("itemid", auditTask.getItem_id());// 事项编码
                auditTaskJson.put("applyertype", auditTask.getApplyertype());// 申请人类型
                String qlKind = auditTask.getShenpilb();
                if (StringUtil.isNotBlank(qlKind)) {
                    qlKind = iCodeItemsService.getItemTextByCodeName("审批类别", qlKind);
                }
                auditTaskJson.put("qlkind", qlKind);// 事项权力类型
                String useLevel = "";
                if (auditTaskExtension != null) {
                    useLevel = auditTaskExtension.getUse_level();// 行使层级
                }
                String exerciseClass = "";
                if (StringUtil.isNotBlank(useLevel)) {
                    String[] userlevelarr = useLevel.split(";");
                    for (int i = 0; i < userlevelarr.length; i++) {
                        exerciseClass = iCodeItemsService.getItemTextByCodeName("行使层级", userlevelarr[i]) + ";";
                    }
                    exerciseClass = exerciseClass.substring(0, exerciseClass.length() - 1);
                }
                auditTaskJson.put("exerciseclass", exerciseClass);
                String SERVICEOBJECT = auditTask.getStr("SERVICE_OBJECT");
                String managementObj = "";
                if (StringUtil.isNotBlank(SERVICEOBJECT)) {
                    String[] applyertypearr = SERVICEOBJECT.split(";");
                    for (int i = 0; i < applyertypearr.length; ++i) {
                        managementObj = this.iCodeItemsService.getItemTextByCodeName("服务对象", applyertypearr[i]) + ";";
                    }
                    managementObj = managementObj.substring(0, managementObj.length() - 1);
                }
                auditTaskJson.put("managementobj", managementObj);
                auditTaskJson.put("type", iCodeItemsService.getItemTextByCodeName("办件类型", String.valueOf(auditTask.getType())));// 办件类型
                auditTaskJson.put("anticipateday",
                        auditTask.getAnticipate_day() == null ? "" : auditTask.getAnticipate_day() + "工作日");// 法定期限
                auditTaskJson.put("promiseday", auditTask.getPromise_day() == null ? "" : auditTask.getPromise_day() + "工作日");// 承诺期限
                auditTaskJson.put("linktel", auditTask.getLink_tel());// 咨询电话
                auditTaskJson.put("supervisetel", auditTask.getSupervise_tel());// 监督投诉电话
                auditTask = iAuditTask.getAuditTaskByGuid(auditTask.getRowguid(), true).getResult();
                if (StringUtil.isNotBlank(auditTask.getStr("projectform"))) {
                    auditTaskJson.put("projectform", auditTask.getStr("projectform"));
                } else {
                    auditTaskJson.put("projectform", "");
                }

                auditTaskJson.put("tasktype", auditTask.getApplyertype());
                auditTaskJson.put("tasktypename", auditTask.getApplyertype().replace("10", "法人").replaceAll("20", "个人"));

                Integer webApplyType = auditTaskExtension == null ? 0
                        : (auditTaskExtension.getWebapplytype() == null ? 0 : auditTaskExtension.getWebapplytype());
                String onlineHandle = "0";
                String WEBAPPLYDEPTH = "信息发布";
                if (webApplyType != null) {
                    onlineHandle = webApplyType == 0 ? "0" : "1";
                    switch (webApplyType) {
                        case 2:
                            WEBAPPLYDEPTH = "在线核验";
                            break;
                        case 1:
                            WEBAPPLYDEPTH = "在线预审";
                            break;
                        default:
                            WEBAPPLYDEPTH = "信息发布";
                            break;
                    }
                }
                String shendu = auditTaskExtension.getStr("wangbanshendu");
                String businesstype = auditTask.getStr("businesstype");
                if (StringUtil.isNotBlank(businesstype)) {
                    if (StringUtil.isNotBlank(shendu)) {
                        if (shendu.contains("5") || shendu.contains("6") || shendu.contains("7")) {
                            WEBAPPLYDEPTH = "全程网办";
                        } else if (shendu.contains("4")) {
                            WEBAPPLYDEPTH = "在线核验";
                        } else if (shendu.contains("3") || shendu.contains("2")) {
                            WEBAPPLYDEPTH = "在线预审";
                        } else {
                            WEBAPPLYDEPTH = "信息发布";
                        }
                    }
                } else {
                    WEBAPPLYDEPTH = "信息发布";
                }
                auditTaskJson.put("depth", WEBAPPLYDEPTH);
                String is_youjicailiao = auditTask.getStr("is_youjicailiao");
                if (StringUtil.isNotBlank(is_youjicailiao)) {
                    is_youjicailiao = "1".equals(is_youjicailiao) ? "是" : "否";
                } else {
                    is_youjicailiao = "否";
                }
                auditTaskJson.put("is_youjicailiao", is_youjicailiao);

                //依申请的事项都可以，businesstype为1和为空的，都可以申报
                if (StringUtil.isBlank(businesstype)) {
                    auditTaskJson.put("onlinehandle", 1); // 事项是否可以网上申报
                } else {
                    if ("1".equals(businesstype)) {
                        auditTaskJson.put("onlinehandle", 1); // 事项是否可以网上申报
                    } else {
                        auditTaskJson.put("onlinehandle", 0); // 事项是否可以网上申报
                    }
                }

                String znyd_task = ConfigUtil.getFrameConfigValue("znyd_task");
                if (StringUtil.isNotBlank(znyd_task) && znyd_task.contains(auditTask.getItem_id())) {
                    auditTaskJson.put("znyd", ZwfwConstant.CONSTANT_STR_ONE);
                }

                // 判断事项预约状态
                if (isAppoint(auditTask.getTask_id())) {
                    auditTaskJson.put("appointment",
                            auditTaskExtension == null ? "0" : auditTaskExtension.getReservationmanagement());// 事项是否可以网上预约
                } else {
                    auditTaskJson.put("appointment", "0");
                }

                String daoxcnum = auditTaskExtension == null ? "0" : auditTaskExtension.getDao_xc_num();
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(daoxcnum)) {
                    auditTaskJson.put("daoxcnum", "1");// 到现场次数
                } else {
                    auditTaskJson.put("daoxcnum", "0");// 到现场次数
                }
                // 获取事项下放信息
                auditTaskJson.put("town", "0");// 是否下放到乡镇
                auditTaskJson.put("community", "0");// 是否下放到社区
                List<AuditTaskDelegate> auditTaskDelegates = iAuditTaskDelegate.selectDelegateByTaskID(auditTask.getTask_id())
                        .getResult();
                if (auditTaskDelegates != null && auditTaskDelegates.size() > 0) {
                    for (AuditTaskDelegate auditTaskDelegate : auditTaskDelegates) {
                        // 使用乡镇维护的数据
                        if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditTaskDelegate.getStatus())) {
                            String xzAreacode = auditTaskDelegate.getAreacode();
                            if (xzAreacode.length() == 9) {
                                auditTaskJson.put("town", "1");// 是否下放到乡镇
                            } else if (xzAreacode.length() == 12) {
                                auditTaskJson.put("community", "1");// 是否下放到社区
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.info("组装事项信息json报错....");
        }
        return auditTaskJson;
    }

    /**
     * 判断该事项是否配有要素且要素选项配有材料
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/checkIsExistElement", method = RequestMethod.POST)
    public String checkIsExistElement(@RequestBody String params) {
        try {
            log.info("=======开始调用checkIsExistElement接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取事项唯一标识
                String taskId = obj.getString("taskid");
                if (StringUtil.isNotBlank(taskId)) {
                    // 2、根据taskId判断事项要素是否满足条件（选项配有材料或者后置要素的要素选项配有材料）
                    boolean isExist = iAuditTaskElementService.checkELementMaterial(taskId).getResult();
                    // 2.1、 根据isExist的值，判定是否需要进行场景选择
                    String isNeedFaq = (isExist) ? ZwfwConstant.CONSTANT_STR_ONE : ZwfwConstant.CONSTANT_STR_ZERO;
                    AuditTask task = iJnAppRestService.getAuditTaskByTaskid(taskId).getResult();
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("isneedfaq", isNeedFaq);
                    dataJson.put("taskname", task.getTaskname());
                    String zjurl = task.getStr("zj_url");
                    if (StringUtil.isNotBlank(zjurl)) {
                        dataJson.put("zjurl", zjurl);
                    } else {
                        dataJson.put("zjurl", "0");
                    }
                    String isturn = task.getStr("is_turn");
                    if (StringUtil.isNotBlank(isturn)) {
                        dataJson.put("isturn", isturn);
                    } else {
                        dataJson.put("isturn", "0");
                    }

                    String sql = "select * from jn_task_relation where remark = ?";
                    List<JnTaskRelation> relations = iJnTaskRelationService.findList(sql, taskId);
                    if (!relations.isEmpty()) {
                        dataJson.put("isxztask", "1");
                    }
                    if ("1137080123456789072370117000002".equals(task.getItem_id())
                            || "1137080123456789072370117000010".equals(task.getItem_id())
                            || "1137080123456789072370117000009".equals(task.getItem_id())
                            || "1137080123456789072370117000008".equals(task.getItem_id())
                            || "1137080123456789072370117000007".equals(task.getItem_id())
                            || "1137080123456789072370117000006".equals(task.getItem_id())
                            || "1137080123456789072370117000005".equals(task.getItem_id())
                            || "1137080123456789072370117000004".equals(task.getItem_id())
                            || "1137080123456789072370117000003".equals(task.getItem_id())
                            || "1137080123456789072370117000001".equals(task.getItem_id())) {
                        dataJson.put("isturn", "jisjningzwfww");
                    }
                    dataJson.put("webapplyurl", task.getStr("webapplyurl"));
                    dataJson.put("areacode", task.getAreacode());
                    dataJson.put("itemcode", task.getItem_id());
                    log.info("=======结束调用checkIsExistElement接口=======");
                    return JsonUtils.zwdtRestReturn("1", "判断事项是否配有要素成功", dataJson.toString());
                } else {
                    // 1.1、获取事项唯一标识
                    String taskguid = obj.getString("taskguid");
                    AuditTask task = iAuditTask.getAuditTaskByGuid(taskguid, false).getResult();
                    // 2、根据taskId判断事项要素是否满足条件（选项配有材料或者后置要素的要素选项配有材料）
                    boolean isExist = iAuditTaskElementService.checkELementMaterial(task.getTask_id()).getResult();
                    // 2.1、 根据isExist的值，判定是否需要进行场景选择
                    String isNeedFaq = (isExist) ? ZwfwConstant.CONSTANT_STR_ONE : ZwfwConstant.CONSTANT_STR_ZERO;
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("isneedfaq", isNeedFaq);
                    dataJson.put("taskname", task.getTaskname());
                    String zjurl = task.getStr("zj_url");
                    if (StringUtil.isNotBlank(zjurl)) {
                        dataJson.put("zjurl", zjurl);
                    } else {
                        dataJson.put("zjurl", "0");
                    }
                    String isturn = task.getStr("is_turn");
                    if (StringUtil.isNotBlank(isturn)) {
                        dataJson.put("isturn", isturn);
                    } else {
                        dataJson.put("isturn", "0");
                    }

                    String sql = "select * from jn_task_relation where remark = ?";
                    List<JnTaskRelation> relations = iJnTaskRelationService.findList(sql, taskId);
                    if (!relations.isEmpty()) {
                        dataJson.put("isxztask", "1");
                    }
                    if ("1137080123456789072370117000002".equals(task.getItem_id())
                            || "1137080123456789072370117000010".equals(task.getItem_id())
                            || "1137080123456789072370117000009".equals(task.getItem_id())
                            || "1137080123456789072370117000008".equals(task.getItem_id())
                            || "1137080123456789072370117000007".equals(task.getItem_id())
                            || "1137080123456789072370117000006".equals(task.getItem_id())
                            || "1137080123456789072370117000005".equals(task.getItem_id())
                            || "1137080123456789072370117000004".equals(task.getItem_id())
                            || "1137080123456789072370117000003".equals(task.getItem_id())
                            || "1137080123456789072370117000001".equals(task.getItem_id())) {
                        dataJson.put("isturn", "jisjningzwfww");
                    }
                    dataJson.put("webapplyurl", task.getStr("webapplyurl"));
                    dataJson.put("areacode", task.getAreacode());
                    dataJson.put("itemcode", task.getItem_id());
                    log.info("=======结束调用checkIsExistElement接口=======");
                    return JsonUtils.zwdtRestReturn("1", "判断事项是否配有要素成功", dataJson.toString());
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======checkIsExistElement接口参数：params【" + params + "】=======");
            log.info("=======checkIsExistElement异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "判断事项是否配有要素失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取事项列表的接口（主题分类/部门分类可展示大小项）
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getTaskList1", method = RequestMethod.POST)
    public String getTaskList1(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getTaskList1接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                JSONObject returnJson = handleGetTaskList(obj, request, "getTaskList");
                log.info("=======结束调用getTaskList1接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取事项列表成功", returnJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getTaskList1接口参数：params【" + params + "】=======");
            log.info("=======getTaskList1异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取事项列表失败：" + e.getMessage(), "");
        }
    }

    /**
     * 根据centerguid获取对应的窗口部门（咨询投诉用）
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getOuListByCenterguid", method = RequestMethod.POST)
    public String getOuListByCenterguid(@RequestBody String params) {
        try {
            log.info("=======开始调用getOuListByCenterguid接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取中心标识
                String centerGuid = obj.getString("centerguid");
                String taskguid = obj.getString("taskguid");
                // 2、获取区域下的窗口部门
                List<FrameOu> frameOus = new ArrayList<FrameOu>();
                List<String> ouguids = iAuditOrgaWindow.getoulistBycenterguid(centerGuid).getResult();
                if (ouguids != null && ouguids.size() > 0) {
                    for (String string : ouguids) {
                        if (StringUtil.isNotBlank(string)) {
                            FrameOu frameOu = ouService.getOuByOuGuid(string);
                            if (frameOu != null) {
                                if (StringUtil.isNotBlank(frameOu.getTel())) {
                                    frameOus.add(ouService.getOuByOuGuid(string));
                                }
                            }
                        }
                    }
                }
                List<JSONObject> ouJsonList = new ArrayList<JSONObject>();
                JSONObject ouJson = new JSONObject();
                AuditTask task = iAuditTask.getAuditTaskByGuid(taskguid, false).getResult();
                if (task != null) {
                    FrameOu frameOu = ouService.getOuByOuGuid(task.getOuguid());
                    ouJson.put("ouname", frameOu.getOuname());
                    ouJson.put("ouguid", frameOu.getOuguid());
                    ouJson.put("isselected", "true");
                } else {
                    ouJson.put("ouname", "请选择");
                    ouJson.put("ouguid", "");
                    ouJson.put("isselected", "true");
                }

                ouJsonList.add(0, ouJson);
                for (int i = 0; i < frameOus.size(); i++) {
                    FrameOu frameOu = frameOus.get(i);
                    JSONObject ouJson2 = new JSONObject();
                    ouJson2.put("ouname", frameOu.getOuname());// 部门名称
                    ouJson2.put("ouguid", frameOu.getOuguid());// 部门标识
                    ouJsonList.add(ouJson2);
                }
                // 3、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("oulist", ouJsonList);
                log.info("=======结束调用getOuListByCenterguid接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取中心下窗口部门成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getOuListByCenterguid接口参数：params【" + params + "】=======");
            log.info("=======getOuListByCenterguid异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取中心下窗口部门失败：" + e.getMessage(), "");
        }
    }

    /**
     * 根据centerguid获取对应的窗口部门（咨询投诉用）
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getTaskListByOuguid", method = RequestMethod.POST)
    public String getTaskListByOuguid(@RequestBody String params) {
        try {
            log.info("=======开始调用getTaskListByOuguid接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String ouguid = obj.getString("ouguid");
                String taskguid = obj.getString("taskguid");
                String taskname = obj.getString("taskname");
                List<AuditTask> tasklist = zwdtService.getTaskListByOuguidAndName(ouguid, taskname);
                List<JSONObject> ouJsonList = new ArrayList<JSONObject>();
                JSONObject ouJson = new JSONObject();
                AuditTask audittask = iAuditTask.getAuditTaskByGuid(taskguid, false).getResult();
                if (audittask != null) {
                    ouJson.put("taskname", audittask.getTaskname());
                    ouJson.put("taskguid", audittask.getRowguid());
                    ouJson.put("isselected", "true");
                } else {
                    ouJson.put("taskname", "请选择");
                    ouJson.put("taskguid", "");
                    ouJson.put("isselected", "true");
                }

                ouJsonList.add(0, ouJson);
                if (tasklist != null && tasklist.size() > 0) {
                    for (AuditTask task : tasklist) {
                        JSONObject ouJson2 = new JSONObject();
                        ouJson2.put("taskname", task.getTaskname());// 事项名称
                        ouJson2.put("taskguid", task.getRowguid());// 事项标识
                        ouJsonList.add(ouJson2);
                    }
                }
                // 3、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("tasklist", ouJsonList);
                log.info("=======结束调用getTaskListByOuguid接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取窗口部门事项清单成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getTaskListByOuguid接口参数：params【" + params + "】=======");
            log.info("=======getTaskListByOuguid异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取窗口部门事项清单失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取事项列表的接口
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getTaskListByContension", method = RequestMethod.POST)
    public String getTaskListByContension(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getTaskListByContension接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取事项当前页码
                String currentPageTask = obj.getString("currentpage");
                // 1.2、获取事项每页数目
                String pageSizeTask = obj.getString("pagesize");

                String ouguid = obj.getString("ouguid");

                if (ouguid.contains("%")) {
                    ouguid = URLDecoder.decode(ouguid, "utf-8");
                }

                String taskname = obj.getString("taskname");
                if (StringUtil.isNotBlank(taskname)) {
                    taskname = URLDecoder.decode(taskname, "utf-8");
                }

                List<AuditTask> list = zwdtService.getTaskListByContension(taskname, ouguid,
                        Integer.parseInt(currentPageTask) * Integer.parseInt(pageSizeTask),
                        Integer.parseInt(pageSizeTask));

                JSONObject returnJson = new JSONObject();
                int i = 0;
                for (AuditTask task : list) {
                    i++;
                    task.set("page", i);
                    if (StringUtil.isNotBlank(task.getTransact_addr())) {
                        task.set("tasklist",
                                zwdtService.getTaskListByAddress(task.getTransact_addr(), taskname, ouguid));
                    }
                }
                returnJson.put("datalist", list);
                log.info("=======结束调用getTaskListByContension接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取事项列表成功", returnJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getTaskListByContension接口参数：params【" + params + "】=======");
            log.info("=======getTaskListByContension异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取事项列表失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取事项列表的接口（主题分类/部门分类可展示大小项）
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getFrameOuByOuguid", method = RequestMethod.POST)
    public String getFrameOuByOuguid(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getFrameOuByOuguid接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = jsonObject.getJSONObject("params");
                String ouguid = obj.getString("ouguid");
                JSONObject returnJson = new JSONObject();
                returnJson.put("datadetail", zwdtService.getFrameOuByOuguid(ouguid));
                log.info("=======结束调用getFrameOuByOuguid接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取部门详情成功", returnJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getFrameOuByOuguid接口参数：params【" + params + "】=======");
            log.info("=======getFrameOuByOuguid异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取部门详情失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取事项列表的接口（主题分类/部门分类可展示大小项）
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getShenpiList", method = RequestMethod.POST)
    public String getShenpiList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getShenpiList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = jsonObject.getJSONObject("params");
                String ouguid = obj.getString("ouguid");
                String applyertype = obj.getString("applyertype");
                JSONObject returnJson = new JSONObject();
                returnJson.put("shenlilist", zwdtService.getShenpiList(ouguid, applyertype));
                log.info("=======结束调用getShenpiList接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取审批类别成功", returnJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getShenpiList接口参数：params【" + params + "】=======");
            log.info("=======getShenpiList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取审批类别失败：" + e.getMessage(), "");
        }
    }

    /**
     * 同步办事流程
     */
    @SuppressWarnings("unchecked")
    public static List<Record> syncbanliliucheng(String banliliucheng, String type, String folder_code) {
        List<Record> list = new ArrayList<Record>();

        // 解析XML
        Document document = null;
        try {
            document = DocumentHelper.parseText(banliliucheng);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        if (banliliucheng != null && StringUtil.isNotBlank(banliliucheng)) {
            if (document != null) {
                Element root = document.getRootElement();
                Element data1 = root.element("FLOWS");
                if (data1 == null) {
                    List<Element> dataflows = root.elements("node");
                    for (Element data : dataflows) {
                        Record record = new Record();
                        List<Element> content2 = data.content();
                        for (Element element : content2) {
                            String label = element.attributeValue("label");
                            String text = element.getText();
                            if ("NAME".equals(label)) {
                                record.set("name", text);
                            } else if ("CONTENT".equals(label)) {
                                record.set("content", text);
                            } else if ("TIME_LIMIT".equals(label)) {
                                record.set("timelimit", text);
                            } else if ("OFFICE".equals(label)) {
                                record.set("timelimit", text);
                            } else if ("REMARK".equals(label)) {
                                //办理结果 如果为空 放入-
                                if (StringUtil.isNotBlank(text)) {
                                    record.set("remark", text);
                                } else {
                                    record.set("remark", "-");
                                }
                            } else if ("REQUIRE_RESOURCE".equals(label)) {
                                record.set("requireresource", text);
                            } else if ("EXTENDFIELD".equals(label)) {
                                record.set("requireresource", text.substring(13, text.length() - 2));
                            }
                        }
                        //新事项库的事项才默认为-，国脉事项有remark字段
                        if ("01".equals(type) && StringUtil.isNotBlank(folder_code) && !folder_code.contains("JN")) {
                            record.set("remark", "-");
                        }
                        list.add(record);
                    }
                } else {
                    List<Element> dataflows = data1.elements("FLOW");
                    for (Element flow : dataflows) {
                        Element FLOWTYPE = flow.element("FLOWTYPE");
                        if ("平台流程".equals(FLOWTYPE.getStringValue())) {
                            Element steps = flow.element("STEPS");
                            List<Element> datas = steps.elements("STEP");
                            for (Element data : datas) {
                                Element NAME = data.element("NAME");
                                Element CONTENT = data.element("CONTENT");
                                Element TIME_LIMIT = data.element("TIME_LIMIT");
                                Element REMARK = data.element("REMARK");
                                Element REQUIRE_RESOURCE = data.element("REQUIRE_RESOURCE");
                                Record record = new Record();
                                record.set("name", NAME.getStringValue());
                                record.set("content", CONTENT.getStringValue());
                                record.set("timelimit", TIME_LIMIT.getStringValue());
                                if (StringUtil.isNotBlank(REMARK)) {
                                    record.set("remark", REMARK.getStringValue());
                                } else {
                                    record.set("remark", "-");
                                }

                                record.set("requireresource", REQUIRE_RESOURCE.getStringValue());
                                //新事项库的事项才默认为-，国脉事项有remark字段
                                if ("01".equals(type) && StringUtil.isNotBlank(folder_code) && !folder_code.contains("JN")) {
                                    record.set("remark", "-");
                                }
                                list.add(record);
                            }
                        }
                    }
                }
            }
        }
        return list;

    }

    /**
     * 同步办事流程
     */
    @SuppressWarnings("unchecked")
    public static String synccomplaininfo(String complaininfo) {
        List<Record> list = new ArrayList<Record>();

        // 解析XML
        Document document = null;
        try {
            document = DocumentHelper.parseText(complaininfo);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        if (complaininfo != null && StringUtil.isNotBlank(complaininfo)) {
            if (document != null) {
                Element root = document.getRootElement();
                List<Element> dataflows = root.elements("node");
                for (Element data : dataflows) {
                    Record record = new Record();
                    List<Element> content2 = data.content();
                    for (Element element : content2) {
                        String label = element.attributeValue("label");
                        String text = element.getText();
                        if ("NAME".equals(label)) {
                            record.set("name", text);
                        } else if ("PHONE".equals(label)) {
                            record.set("phone", text);
                        } else if ("ADDRESS".equals(label)) {
                            record.set("address", text);
                        } else if ("TYPE".equals(label)) {
                            if (("2").equals(text)) {
                                record.set("type", "行政复议");
                            } else if (("3").equals(text)) {
                                record.set("type", "行政诉讼");
                            }
                        }
                    }
                    list.add(record);
                }

            }
        }
        StringBuffer resultBuffer = new StringBuffer();
        int i = 1;
        for (Record record : list) {
            if (i != 1) {
                resultBuffer.append("<br>");
            }
            i++;
            resultBuffer.append(record.getStr("type")).append("<br>部门：").append(record.getStr("name")).append("<br>地址：")
                    .append(record.getStr("address")).append("<br>电话：").append(record.getStr("phone"));
        }
        return resultBuffer.toString();

    }

    //收费标准
    public static String syncchargeiteminfo(String chargeiteminfo) {
        List<Record> list = new ArrayList<Record>();

        // 解析XML
        Document document = null;
        try {
            document = DocumentHelper.parseText(chargeiteminfo);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        if (chargeiteminfo != null && StringUtil.isNotBlank(chargeiteminfo)) {
            if (document != null) {
                Element root = document.getRootElement();
                List<Element> dataflows = root.elements("node");
                for (Element data : dataflows) {
                    Record record = new Record();
                    List<Element> content2 = data.content();
                    for (Element element : content2) {
                        String label = element.attributeValue("label");
                        String text = element.getText();
                        if ("STANDARD".equals(label)) {
                            record.set("standrd", text);
                        }
                    }
                    list.add(record);
                }
            }
        }
        StringBuffer resultBuffer = new StringBuffer();
        resultBuffer.append("收费标准：");
        for (int i = 0; i < list.size(); i++) {
            resultBuffer.append(list.get(i).getStr("standrd") + "<br>");
        }
        return resultBuffer.toString();
    }


    /**
     * 中介服务事项基本信息
     */
    public static String syncZjfwinfo(String complaininfo) {
        List<Record> list = new ArrayList<Record>();

        // 解析XML
        Document document = null;
        try {
            document = DocumentHelper.parseText(complaininfo);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        if (complaininfo != null && StringUtil.isNotBlank(complaininfo)) {
            if (document != null) {
                Element root = document.getRootElement();
                List<Element> dataflows = root.elements("node");
                for (Element data : dataflows) {
                    Record record = new Record();
                    List<Element> content2 = data.content();
                    for (Element element : content2) {
                        String label = element.attributeValue("label");
                        String text = element.getText();
                        if ("NAME".equals(label)) {
                            record.set("name", text);
                        }
                    }
                    list.add(record);
                }
            }
        }
        StringBuffer resultBuffer = new StringBuffer();
        int i = 1;
        for (Record record : list) {
            if (i != 1) {
                resultBuffer.append("<br>");
            }
            i++;
            resultBuffer.append("中介服务项目名称：").append(record.getStr("name")).append("<br>");
        }
        return resultBuffer.toString();
    }

    /**
     * 同步办事流程
     */
    @SuppressWarnings("unchecked")
    public static List<Record> syncbanlididiansj(String banlididiansj) {
        List<Record> list = new ArrayList<Record>();

        // 解析XML
        Document document = null;
        try {
            document = DocumentHelper.parseText(banlididiansj);

            if (banlididiansj != null && StringUtil.isNotBlank(banlididiansj)) {
                if (document != null) {
                    Element root = document.getRootElement();
                    if (root == null) {
                        return list;
                    }
                    Element data1 = root.element("ACCEPT_ADDRESSS");
                    if (data1 == null) {
                        List<Element> dataflows = root.elements("node");
                        for (Element data : dataflows) {
                            Record record = new Record();
                            List<Element> content2 = data.content();
                            for (Element element : content2) {
                                String label = element.attributeValue("label");
                                String text = element.getText();
                                if ("OFFICE_HOUR".equals(label)) {
                                    record.set("officehour", text);
                                } else if ("ADDRESS".equals(label)) {
                                    record.set("address", text);
                                }
                            }
                            list.add(record);
                        }
                        return list;
                    }
                    List<Element> datas = data1.elements("ACCEPT_ADDRESS");
                    if (datas == null) {
                        return list;
                    }
                    for (Element data : datas) {
                        Element NAME = data.element("NAME");
                        Element ADDRESS = data.element("ADDRESS");
                        Element OFFICE_HOUR = data.element("OFFICE_HOUR");
                        Element WINDOW_NUM = data.element("WINDOW_NUM");
                        Record record = new Record();
                        if (StringUtil.isNotBlank(NAME)) {
                            record.set("name", NAME.getStringValue());
                        }
                        if (StringUtil.isNotBlank(ADDRESS)) {
                            record.set("address", ADDRESS.getStringValue());
                        }
                        if (StringUtil.isNotBlank(OFFICE_HOUR)) {
                            record.set("officehour", OFFICE_HOUR.getStringValue());
                        }
                        if (StringUtil.isNotBlank(WINDOW_NUM)) {
                            record.set("windownum", WINDOW_NUM.getStringValue());
                        }

                        list.add(record);
                    }
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return list;

    }

    /**
     * 获取事项列表的接口（主题分类/部门分类可展示大小项）
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getShenpiListByAreacode", method = RequestMethod.POST)
    public String getShenpiListByAreacode(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getShenpiListByAreacode接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = jsonObject.getJSONObject("params");
                String areacode = obj.getString("areacode");
                String ouguid = obj.getString("ouguid");
                String applyertype = obj.getString("applyertype");
                String isyc = obj.getString("isyc");
                // 是否行政权力
                String xzql = obj.getString("xzql");
                // 1.3 到现场次数
                String Dao_xc_num = obj.getString("Dao_xc_num");
                // 1.3 是否一次办好
                String ISPYC = obj.getString("ISPYC");
                // 1.3 是否全市通办
                String Operationscope = obj.getString("Operationscope");
                // 1.3 是否支持物流
                String If_express = obj.getString("If_express");
                // 1.3 是否支持物流
                String mpmb = obj.getString("mpmb");
                // 1.3 是否收费
                String CHARGE_FLAG = obj.getString("CHARGE_FLAG");
                // 1.3 是否马上办
                String mashangban = obj.getString("mashangban");
                // 1.3 是否全网通办
                String wangshangban = obj.getString("wangshangban");
                // 1.3 是否6+1类
                String sixshenpilb = obj.getString("sixshenpilb");
                // 1.3 是否依申请
                String yishenqing = obj.getString("ysq");
                // 是否跨省通办
                String kstbsx = obj.getString("kstbsx");
                // 是否跨省通办
                String qstb = obj.getString("qstb");
                // 1.3 是否全程通办
                String qctb = obj.getString("qctb");

                String dictid = obj.getString("dictid");
                JSONObject returnJson = new JSONObject();
                List<AuditTask> list = new ArrayList<AuditTask>();
                if (StringUtil.isNotBlank(isyc)) {
                    list = zwdtService.getShenpiListByAreacodeYC(areacode, ouguid, applyertype, dictid, Dao_xc_num,
                            ISPYC, Operationscope, If_express, mpmb, CHARGE_FLAG, mashangban, wangshangban, sixshenpilb,
                            yishenqing, isyc, qctb, xzql, qstb, kstbsx);
                } else {
                    list = zwdtService.getShenpiListByAreacode(areacode, ouguid, applyertype, dictid);
                }
                returnJson.put("shenlilist", list);
                log.info("=======结束调用getShenpiListByAreacode接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取审批类别成功", returnJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getShenpiListByAreacode接口参数：params【" + params + "】=======");
            log.info("=======getShenpiListByAreacode异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取审批类别失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取事项列表的接口（主题分类/部门分类可展示大小项）
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getShenpiListByAreacodeDt", method = RequestMethod.POST)
    public String getShenpiListByAreacodeDt(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getShenpiListByAreacodeDt接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = jsonObject.getJSONObject("params");
                String areacode = obj.getString("areacode");
                JSONObject returnJson = new JSONObject();
                List<AuditTask> list = new ArrayList<AuditTask>();
                list = zwdtService.getShenpiListByAreacodeDt(areacode);
                returnJson.put("shenlilist", list);
                log.info("=======结束调用getShenpiListByAreacodeDt接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取审批类别成功", returnJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getShenpiListByAreacodeDt接口参数：params【" + params + "】=======");
            log.info("=======getShenpiListByAreacodeDt异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取审批类别失败：" + e.getMessage(), "");
        }
    }

    /**
     * 部门分类的接口
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getZiXunOulist", method = RequestMethod.POST)
    public String getZiXunOulist(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getZiXunOulist接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取区域编码
                String areaCode = obj.getString("areacode");
                // 2、获取事项所属的部门
                List<FrameOu> oulist = zwdtService.getOuListByAreacode(areaCode);
                List<JSONObject> ouJsonList = new ArrayList<JSONObject>();
                for (FrameOu record : oulist) {
                    JSONObject ouObject = new JSONObject();
                    String ouname = record.get("ouname");
                    ouObject.put("ouname", record.get("OUSHORTNAME"));
                    ouObject.put("ouguid", record.get("ouguid"));
                    ouJsonList.add(ouObject);
                }
                // 4、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("oulist", ouJsonList);
                log.info("=======结束调用getZiXunOulist接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取部门列表成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getTaskKindsByOu接口参数：params【" + params + "】=======");
            log.info("=======getTaskKindsByOu异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取部门列表失败：" + e.getMessage(), "");
        }
    }

    /**
     * 部门分类的接口
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getHcpStatiEvluate", method = RequestMethod.POST)
    public String getHcpStatiEvluate(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getHcpStatiEvluate接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 4、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                // 1.1、获取事项标识
                String taskguid = obj.getString("taskguid");
                Record record = iSpaceAcceptService.getstaicfiedByTaskguid(taskguid);
                if (record != null) {
                    int total = record.getInt("total");
                    // 创建一个数值格式化对象
                    NumberFormat numberFormat = NumberFormat.getInstance();
                    // 设置精确到小数点后2位
                    numberFormat.setMaximumFractionDigits(2);
                    Record ecaluenums = iSpaceAcceptService.getevaluatStatiByTaskguid(taskguid);
                    String feichangmanyis = ecaluenums.getStr("feichangmanyi");
                    if (StringUtil.isNotBlank(feichangmanyis)) {
                        int feichangmanyi = ecaluenums.getInt("feichangmanyi");
                        int manyi = ecaluenums.getInt("manyi");
                        int jibenmanyi = ecaluenums.getInt("jibenmanyi");
                        int bumanyi = ecaluenums.getInt("bumanyi");
                        int feichangbumanyi = ecaluenums.getInt("feichangbumanyi");
                        String feichangmanyi1 = numberFormat.format((float) feichangmanyi / (float) total * 100);
                        String manyi1 = numberFormat.format((float) manyi / (float) total * 100);
                        String jibenmanyi1 = numberFormat.format((float) jibenmanyi / (float) total * 100);
                        String bumanyi1 = numberFormat.format((float) bumanyi / (float) total * 100);
                        String feichangbumanyi1 = numberFormat.format((float) feichangbumanyi / (float) total * 100);
                        String avestatis = record.getStr("avestatis");
                        // 总评价人数
                        dataJson.put("total", total);
                        // 平均评价满意度
                        dataJson.put("avestatis", avestatis);
                        // 非常满意
                        dataJson.put("feichangmanyi", feichangmanyi1);
                        dataJson.put("manyi", manyi1);
                        dataJson.put("jibenmanyi", jibenmanyi1);
                        dataJson.put("bumanyi", bumanyi1);
                        dataJson.put("feichangbumanyi", feichangbumanyi1);
                    } else {
                        AuditTask task = iAuditTask.getAuditTaskByGuid(taskguid, true).getResult();
                        if (task != null) {
                            String shenpilb = task.getShenpilb();
                            if ("01".equals(shenpilb) || "10".equals(shenpilb) || "11".equals(shenpilb)
                                    || "07".equals(shenpilb) || "05".equals(shenpilb) || "06".equals(shenpilb)
                                    || "08".equals(shenpilb)) {
                                // 平均评价满意度
                                dataJson.put("avestatis", "5");
                                // 非常满意
                                dataJson.put("feichangmanyi", "100");
                                dataJson.put("manyi", "0");
                                dataJson.put("jibenmanyi", "0");
                                dataJson.put("bumanyi", "0");
                                dataJson.put("feichangbumanyi", "0");
                            }
                        }
                    }
                    log.info("=======结束调用getHcpStatiEvluate接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取好差评满意度成功", dataJson.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "该事项不存在满意度评价！", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getHcpStatiEvluate接口参数：params【" + params + "】=======");
            log.info("=======getHcpStatiEvluate异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取好差评失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取事项下要素及要素选项
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getTaskElement", method = RequestMethod.POST)
    public String getTaskElement(@RequestBody String params) {
        try {
            log.info("=======开始调用getTaskElement接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            log.info("getTaskElement入参：" + params);
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取事项唯一标识
                String taskId = obj.getString("taskid");
                // 2、获取事项的情景要素列表 (没有前置要素的)
                // 2.1、 拼接查询条件
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.isBlankOrValue("draft", "0"); // 不为草稿
                sqlConditionUtil.eq("taskid", taskId); // 事项id
                sqlConditionUtil.isBlankOrValue("preoptionguid", "start");
                List<AuditTaskElement> auditTaskElements = iAuditTaskElementService
                        .findListByCondition(sqlConditionUtil.getMap()).getResult();
                log.info("auditTaskElements:" + auditTaskElements);
                // 定义存储要素信息的List
                List<JSONObject> elementJsonList = new ArrayList<JSONObject>();
                if (auditTaskElements != null && auditTaskElements.size() > 0) {
                    int index = 1;
                    for (AuditTaskElement auditTaskElement : auditTaskElements) {
                        // 定义单多选标志位，默认单选
                        String type = "radio";
                        String elementName = "[单选]";
                        // 定义存储要素信息的json
                        JSONObject elementJson = new JSONObject();
                        // 2.2 根据事项要素唯一标识查询事项要素选项
                        List<AuditTaskOption> auditTaskOptions = iAuditTaskOptionService
                                .findListByElementIdWithoutNoName(auditTaskElement.getRowguid()).getResult();
                        log.info("auditTaskOptions:" + auditTaskOptions);
                        if (auditTaskOptions != null && auditTaskOptions.size() > 1) {
                            // 定义存储要素选项信息的List
                            List<JSONObject> optionJsonList = new ArrayList<JSONObject>();
                            for (AuditTaskOption auditTaskOption : auditTaskOptions) {
                                // 定义存储要素选项信息的json
                                JSONObject optionJson = new JSONObject();
                                optionJson.put("optionname", auditTaskOption.getOptionname());
                                optionJson.put("optionguid", auditTaskOption.getRowguid());
                                optionJsonList.add(optionJson);
                            }
                            if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditTaskElement.getMultiselect())) {
                                type = "checkbox";
                                elementName = "[多选]";
                            }
                            elementJson.put("index", index);
                            elementJson.put("tag", elementName);
                            elementJson.put("type", type); // 单选还是多选的标志位
                            elementJson.put("optionlist", optionJsonList); // 要素选项列表
                            elementJson.put("elementquestion", elementName + auditTaskElement.getElementname()); // 要素问题
                            elementJson.put("elementname", auditTaskElement.getElementname()); // 要素名称
                            elementJson.put("elementguid", auditTaskElement.getRowguid()); // 要素唯一标识
                            elementJsonList.add(elementJson);
                            index++;
                        }
                    }
                }
                // 定义返回的JSON
                JSONObject dataJson = new JSONObject();
                dataJson.put("elementlist", elementJsonList);// 要素及要素选项列表
                log.info("=======结束调用getTaskElement接口=======");
                return JsonUtils.zwdtRestReturn("1", "事项要素获取成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getTaskElement接口参数：params【" + params + "】=======");
            log.info("=======getTaskElement异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "事项要素获取失败：" + e.getMessage(), "");
        }
    }

    /**
     * 获取事项下要素及要素选项
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getTaskElementByOption", method = RequestMethod.POST)
    public String getTaskElementByOption(@RequestBody String params) {
        try {
            log.info("=======开始调用getTaskElementByOption接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            log.info("getTaskElementByOption入参" + params);
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取要素选项唯一标识
                String optionGuid = obj.getString("optionguid");
                // 1.2、获取事项唯一标识
                String taskId = obj.getString("taskid");
                // 2、获取事项的要素列表
                // 2.1、 拼接查询条件
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.isBlankOrValue("draft", "0"); // 不为草稿
                sqlConditionUtil.eq("taskid", taskId); // 事项id
                if (optionGuid.indexOf(";") != -1) {
                    String guids[] = optionGuid.split(";");
                    String options = null;
                    List<String> list = new ArrayList<>();
                    if (guids != null && guids.length > 0) {
                        for (int i = 0; i < guids.length; i++) {
                            list.add(guids[i]);
                        }
                        options = "'" + StringUtil.join(list, "','") + "'";
                        sqlConditionUtil.in("preoptionguid", options);
                    }
                } else {
                    if (StringUtil.isNotBlank(optionGuid)) {
                        sqlConditionUtil.eq("preoptionguid", optionGuid);
                    } else {
                        sqlConditionUtil.eq("preoptionguid", "preoptionguid");
                    }
                }
                List<AuditTaskElement> auditTaskElements = iAuditTaskElementService
                        .findListByCondition(sqlConditionUtil.getMap()).getResult();
                // 定义存储要素信息的List
                List<JSONObject> subElementJsonList = new ArrayList<JSONObject>();
                if (auditTaskElements != null && auditTaskElements.size() > 0) {
                    int index = 1;
                    for (AuditTaskElement auditTaskElement : auditTaskElements) {
                        // 定义单多选标志位，默认单选
                        String type = "radio";
                        String elementName = "[单选]";
                        // 定义存储要素信息的Json
                        JSONObject subElementJson = new JSONObject();
                        // 2.2、查询要素选项信息
                        List<AuditTaskOption> auditTaskOptions = iAuditTaskOptionService
                                .findListByElementIdWithoutNoName(auditTaskElement.getRowguid()).getResult();
                        if (auditTaskOptions != null && auditTaskOptions.size() > 1) {
                            // 定义存储要素选项信息的List
                            List<JSONObject> optionJsonList = new ArrayList<JSONObject>();
                            for (AuditTaskOption auditTaskOption : auditTaskOptions) {
                                // 定义存储要素选项的Json
                                JSONObject optionJson = new JSONObject();
                                optionJson.put("optionname", auditTaskOption.getOptionname());
                                optionJson.put("optionguid", auditTaskOption.getRowguid());
                                optionJsonList.add(optionJson);
                            }
                            if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditTaskElement.getMultiselect())) {
                                type = "checkbox";
                                elementName = "[多选]";
                            }
                            subElementJson.put("tag", elementName);
                            subElementJson.put("type", type); // 单选还是多选的标志位
                            subElementJson.put("optionlist", optionJsonList); // 要素选项列表
                            subElementJson.put("subelementquestion", elementName + auditTaskElement.getElementname()); // 要素问题
                            subElementJson.put("subelementguid", auditTaskElement.getRowguid()); // 要素唯一标识
                            subElementJson.put("subelementname", auditTaskElement.getElementname()); // 要素名称
                            subElementJson.put("index", index);
                            subElementJsonList.add(subElementJson);
                            index++;
                        }
                    }
                }
                JSONObject dataJson = new JSONObject();
                dataJson.put("subelementlist", subElementJsonList);// 要素及要素选项列表
                log.info("=======结束调用getTaskElementByOption接口=======");
                return JsonUtils.zwdtRestReturn("1", "事项后置要素获取成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getTaskElementByOption接口参数：params【" + params + "】=======");
            log.info("=======getTaskElementByOption异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "事项后置要素获取失败：" + e.getMessage(), "");
        }
    }

    /**
     * 根据用户选择信息，获取情形唯一标识
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getCaseGuidBySelectedOptions", method = RequestMethod.POST)
    public String getCaseGuidBySelectedOptions(@RequestBody String params) {
        try {
            log.info("=======开始调用getCaseGuidBySelectedOptions接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            log.info("getCaseGuidBySelectedOptions入参：" + params);
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取用户选择信息
                String selectedOptions = obj.getString("selectedoptions");
                // 1.2、获取事项唯一标识
                String taskGuid = obj.getString("taskguid");
                // 1.3、获取事项id
                String taskId = obj.getString("taskid");
                String faqCaseGuid = "";
                if (StringUtil.isNotBlank(selectedOptions)) {
                    // 2、调用接口，通过用户选择信息获取faqCaseGuid
                    faqCaseGuid = iAuditTaskCase.checkOrAddNewCase(taskId, taskGuid, selectedOptions).getResult();
                    if (ZwfwConstant.CONSTANT_STR_ZERO.equals(faqCaseGuid)) {
                        return JsonUtils.zwdtRestReturn("0", "获取事项情形标识失败！", "");
                    }
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取用户选择信息失败！", "");
                }
                JSONObject dataJson = new JSONObject();
                dataJson.put("faqcaseguid", faqCaseGuid); // 是否配置要素或要素下是否配置材料并标识
                log.info("=======结束调用getCaseGuidBySelectedOptions接口=======");
                return JsonUtils.zwdtRestReturn("1", "情形唯一标识获取成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getCaseGuidBySelectedOptions接口参数：params【" + params + "】=======");
            log.info("=======getCaseGuidBySelectedOptions异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "情形唯一标识获取失败：" + e.getMessage(), "");
        }
    }

    /**
     * 根据centerguid获取对应的窗口部门（咨询投诉用）
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getTaskGuidByItemid", method = RequestMethod.POST)
    public String getTaskGuidByItemid(@RequestBody String params) {
        try {
            log.info("=======开始调用getTaskGuidByItemid接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            // 3、定义返回JSON对象
            JSONObject dataJson = new JSONObject();
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String itemid = obj.getString("itemid");
                AuditTask task = zwdtService.getAuditTaskByItemid(itemid);
                if (task != null) {
                    dataJson.put("taskguid", task.getRowguid());
                    log.info("=======结束调用getTaskListByOuguid接口=======");
                    return JsonUtils.zwdtRestReturn("1", "获取事项标识成功", dataJson.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "未找到对应的相关事项", dataJson.toString());
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getTaskGuidByItemid接口参数：params【" + params + "】=======");
            log.info("=======getTaskGuidByItemid异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取事项Taskguid失败：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/getattachlist", method = RequestMethod.POST)
    public String getattachlist(@RequestBody String params) {
        try {
            log.info("=======开始调用getattachlist接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject returnJson = new JSONObject();
            // 3、定义返回JSON对象
            JSONObject dataJson = new JSONObject();
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String cliengguid = obj.getString("cliengguid");
                List<FrameAttachInfo> attachs = iAttachService.getAttachInfoListByGuid(cliengguid);
                if (attachs != null) {
                    List<Record> list = new ArrayList<Record>();
                    for (FrameAttachInfo attach : attachs) {
                        Record record = new Record();
                        record.set("attachguid", attach.getAttachGuid());
                        record.set("attachname", attach.getAttachFileName());
                        list.add(record);
                    }
                    returnJson.put("attachlist", list);
                    return JsonUtils.zwdtRestReturn("1", "获取附件清单成功！", returnJson.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "未找到对应的附件", dataJson.toString());
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getattachlist接口参数：params【" + params + "】=======");
            log.info("=======getattachlist异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取附件失败：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/getAreaList", method = RequestMethod.POST)
    public String getAreaList(@RequestBody String params) {
        try {
            log.info("=======开始调用getAreaList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject returnJson = new JSONObject();
            // 3、定义返回JSON对象
            JSONObject dataJson = new JSONObject();
            if (ZwdtConstant.SysValidateData.equals(token)) {
                List<CodeItems> items = iJnAppRestService.getAreaList();
                if (items != null) {
                    List<Record> list = new ArrayList<Record>();
                    for (CodeItems item : items) {
                        Record record = new Record();
                        record.set("areaname", item.getItemText());
                        record.set("areacode", item.getItemValue());
                        list.add(record);
                    }
                    returnJson.put("arealist", list);
                    return JsonUtils.zwdtRestReturn("1", "获取附件清单成功！", returnJson.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "未找到对应的附件", dataJson.toString());
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getAreaList接口参数：params【" + params + "】=======");
            log.info("=======getAreaList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取附件失败：" + e.getMessage(), "");
        }
    }

    @RequestMapping(value = "/getXzTaskAreaList", method = RequestMethod.POST)
    public String getXzTaskAreaList(@RequestBody String params) {
        try {
            log.info("=======开始调用getXzTaskAreaList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            JSONObject returnJson = new JSONObject();
            // 3、定义返回JSON对象
            JSONObject dataJson = new JSONObject();
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String qxtaskid = obj.getString("taskid");
                String sql = "select * from jn_task_relation where remark = ?";
                List<JnTaskRelation> relations = iJnTaskRelationService.findList(sql, qxtaskid);
                if (!relations.isEmpty()) {
                    List<Record> list = new ArrayList<Record>();
                    for (JnTaskRelation relation : relations) {
                        Record record = new Record();
                        AuditTask task = iAuditTask.selectUsableTaskByTaskID(relation.getTaskid()).getResult();
                        if (task != null) {
                            record.set("taskguid", task.getRowguid());
                        } else {
                            continue;
                        }
                        record.set("areaname", relation.getAreaname());
                        record.set("taskid", relation.getTaskid());
                        list.add(record);
                    }
                    returnJson.put("arealist", list);
                    return JsonUtils.zwdtRestReturn("1", "获取乡镇事项清单成功！", returnJson.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "未获取乡镇事项清单", dataJson.toString());
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getXzTaskAreaList接口参数：params【" + params + "】=======");
            log.info("=======getgetXzTaskAreaListAreaList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取乡镇事项清单失败：" + e.getMessage(), "");
        }
    }

    /***
     * 弹框信息获取
     * @param params
     * @return
     */
    @RequestMapping(value = "/getTk", method = RequestMethod.POST)
    public String getTk(@RequestBody String params) {
        try {
            log.info("=======开始调用getCenterListByAreaCode接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.2、获取事项标识
                String taskGuid = obj.getString("taskguid");
                // 1.3、获取套餐标识
                JSONObject dataJson = new JSONObject();
                JSONObject custom = new JSONObject();
                if (StringUtil.isNotBlank(taskGuid)) {
                    // 如果传了taskguid，以事项的areacode为准，如果没传则用前台传来的areacode
                    AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, true).getResult();
                    if (auditTask != null) {
                        SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                        sqlConditionUtil.eq("taskid", auditTask.getTask_id());
                        AuditTaskPopInfo auditTaskPopInfo = iAuditTaskPopInfoService.find(sqlConditionUtil.getMap());
                        if(auditTaskPopInfo!=null){
                            if(StringUtil.isNotBlank(auditTaskPopInfo.getContent())) {
                                custom.put("isshowpop", true);
                                custom.put("content", auditTaskPopInfo.getContent());
                            }
                            else{
                                custom.put("isshowpop", false);
                            }
                        }
                        else{
                            custom.put("isshowpop",false);
                        }
                    }
                    else{
                        custom.put("isshowpop",false);
                    }
                }
                dataJson.put("pop",custom);


                log.info("=======结束调用getCenterListByAreaCode接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取区域下中心成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getCenterListByAreaCode接口参数：params【" + params + "】=======");
            log.info("=======getCenterListByAreaCode异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取区域下中心失败：" + e.getMessage(), "");
        }
    }

}
