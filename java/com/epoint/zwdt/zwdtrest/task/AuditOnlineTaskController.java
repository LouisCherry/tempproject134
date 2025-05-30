package com.epoint.zwdt.zwdtrest.task;

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
import com.epoint.basic.auditsp.auditspbasetaskr.domain.AuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspbasetaskr.inter.IAuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditsptask.domain.AuditSpTask;
import com.epoint.basic.auditsp.auditsptask.inter.IAuditSpTask;
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
import com.epoint.common.util.*;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditqueue.handlequeue.inter.IHandleQueue;
import com.epoint.composite.auditresource.handletask.inter.IHandleTask;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.classpath.ClassPathUtil;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.convert.ChineseToPinyin;
import com.epoint.core.utils.file.FileManagerUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.core.utils.web.WebUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.search.inteligentsearch.sdk.domain.SearchCondition;
import com.epoint.search.inteligentsearch.sdk.domain.SearchUnionCondition;
import com.epoint.workflow.service.common.entity.config.WorkflowTransition;
import com.epoint.workflow.service.config.api.IWorkflowProcessVersionService;
import com.epoint.workflow.service.config.api.IWorkflowTransitionService;
import com.epoint.zwdt.zwdtrest.task.api.IJnAppRestService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.lang.invoke.MethodHandles;
import java.util.*;

/**
 * 事项相关接口
 *
 * @version [F9.3, 2017年11月9日]
 * @作者 WST
 */
@RestController
@RequestMapping("/zwdtTask")
public class AuditOnlineTaskController {

    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    /**
     * 中心系统参数API
     */
    @Autowired
    private IHandleConfig iHandleConfig;
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
     * 区域API
     */
    @Autowired
    private IAuditOrgaArea iAuditOrgaArea;
    /**
     * 事项基本信息API
     */
    @Autowired
    private IAuditTask iAuditTask;
    /**
     * 热门事项API
     */
    @Autowired
    private IAuditTaskHottask iAuditTaskHottask;
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
     * 部门API
     */
    @Autowired
    private IOuService ouService;
    /**
     * 阶段相关事项
     */
    @Autowired
    private IAuditSpTask iAuditSpTask;

    /**
     * 济宁个性化接口
     */
    @Autowired
    private IJnAppRestService iJnAppRestService;
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
     * 事项要素选项相关
     */
    @Autowired
    private IAuditTaskOptionService iAuditTaskOptionService;

    /**
     * 事项队列分类相关api
     */
    @Autowired
    IAuditQueueTasktypeTask iAuditQueueTasktypeTask;
    /**
     * 事项队列相关api
     */
    @Autowired
    IAuditQueueTasktype iAuditQueueTasktype;

    /**
     * 队列相关api
     */
    @Autowired
    IAuditQueue iAuditQueue;

    /**
     * 办理队列相关api
     */
    @Autowired
    IHandleQueue iHandleQueue;

    /**
     * 预约时间API
     */
    @Autowired
    private IAuditQueueYuyueTime iAuditQueueYuyueTime;
    /**
     * 关联事项API
     */
    @Autowired
    private IAuditSpBasetaskR iAuditSpBasetaskR;

    @Autowired
    private IWorkflowProcessVersionService workflowProcessVersionService;

    @Autowired
    private IWorkflowTransitionService workflowTransitionService;

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
                    dataJson.put("formid", auditTaskExtension.get("formid"));// 电子表单编码
                    JSONObject taskElementJson = new JSONObject();
                    // 3.1、事项是否可以网上申报
                    Integer webApplyType = auditTaskExtension == null ? 0
                            : (auditTaskExtension.getWebapplytype() == null ? 0 : auditTaskExtension.getWebapplytype());
                    String onlineHandle = "0";
                    if (webApplyType != null) {
                        onlineHandle = webApplyType == 0 ? "0" : "1";
                    }
                    // 3.1.1、判断事项是否配置在窗口
                    boolean flag = false;
                    List<Record> centerGuids = iAuditOrgaWindow.selectCenterGuidsByTaskId(auditTask.getTask_id()).getResult();
                    if (centerGuids != null && centerGuids.size() > 0) {
                        //判断事项的工作流是否异常
                        String processversionguid = workflowProcessVersionService.selectEnableProcessVersion(auditTask.getProcessguid());
                        if (processversionguid != null) {
                            List<WorkflowTransition> list = workflowTransitionService.select(processversionguid);
                            if (list != null && !list.isEmpty()) {
                                flag = true;//事项配置在窗口且事项的工作流正常
                            } else {
                                //尚未新增岗位
                                flag = false;
                            }
                        } else {
                            //流程配置错误
                            flag = false;
                        }
                    }
                    if (flag) {
                        taskElementJson.put("onlinehandle", onlineHandle); // 事项是否可以网上申报  （事项配置在窗口，是否可以申报由事项决定）
                    } else {
                        taskElementJson.put("onlinehandle", ZwfwConstant.CONSTANT_STR_ZERO); // 事项是否可以网上申报 （事项未配置在窗口，不可以网上申报）
                    }
                    // 3.2、事项是否可以网上预约
                    if (isAppoint(auditTask.getTask_id())) {
                        taskElementJson.put("appointment",
                                auditTaskExtension == null ? "0" : auditTaskExtension.getReservationmanagement());// 事项是否可以网上预约
                    } else {
                        taskElementJson.put("appointment", "0");
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
                        String applyerType = auditTask.getApplyertype();
                        String managementObj = "";
                        if (StringUtil.isNotBlank(applyerType)) {
                            String[] applyertypearr = applyerType.split(";");
                            for (int i = 0; i < applyertypearr.length; i++) {
                                managementObj = iCodeItemsService.getItemTextByCodeName("申请人类型", applyertypearr[i])
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
                        taskBasicJson.put("handletime", auditTask.getTransact_time());// 办公时间  为了兼容之前的前台页面暂时不删除
                        taskBasicJson.put("limit_num", auditTask.getLimit_num());// 数量限制
                        //辖区
                        taskBasicJson.put("areacode",auditTask.getAreacode() );
                        if (auditTaskExtension != null) {
                            taskBasicJson.put("dao_xc_num", auditTaskExtension.getDao_xc_num());// 到办事窗口的最少次数
                            taskBasicJson.put("finishfilename", auditTaskExtension.getFinishfilename());// 审批结果名称
                            String finishproductsamples = auditTaskExtension.getFinishproductsamples();
                            if (StringUtil.isNotBlank(finishproductsamples)) {
                                List<FrameAttachInfo> frameAttachInfos = iAttachService
                                        .getAttachInfoListByGuid(finishproductsamples);
                                if (frameAttachInfos != null && frameAttachInfos.size() > 0) {
                                    taskBasicJson.put("finishproductsamples",
                                            frameAttachInfos.get(0).getAttachFileName());// 结果样本名称
                                    taskBasicJson.put("finishproductguid", finishproductsamples);// 审批结果标识
                                }
                            }

                            taskBasicJson.put("powerdeline", auditTaskExtension.getPowerdeline());//权限划分
                            taskBasicJson.put("if_online_sb", auditTaskExtension.getStr("IF_ONLINE_SB"));//是否网办
                            taskBasicJson.put("service_dept", auditTask.getStr("SERVICE_DEPT"));//是否存在中介服务
                            taskBasicJson.put("finishproductsamples", auditTaskExtension.getStr("FINISHPRODUCTSAMPLES"));//结果样本
                            taskBasicJson.put("if_express", auditTaskExtension.getStr("IF_EXPRESS"));//是否支持物流快递
                            taskBasicJson.put("reservationmangement", auditTaskExtension.getStr("RESERVATIONMANAGEMENT"));//是否支持预约办理
                            // 是否进驻大厅
                            taskBasicJson.put("ifjzhall", auditTaskExtension.getIf_jz_hall());
                            // 实施主体性质
                            if (StringUtil.isNotBlank(auditTaskExtension.getSubjectnature())) {
                                taskBasicJson.put("subjectnature", auditTaskExtension.getSubjectnature());
                            } else {
                                taskBasicJson.put("subjectnature", "无");
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

                        }
                        // 3.4.1.3 获取办理情况
                        List<JSONObject> taskHandleJsonList = new ArrayList<JSONObject>();
                        List<AuditOrgaWindow> auditOrgaWindows = iAuditOrgaWindow
                                .getWindowListByTaskId(auditTask.getTask_id()).getResult();
                        //  标志当前循环中的窗口是否是第一个进入循环的市区县的窗口。
                        Boolean firstWindow = true;
                        if (auditOrgaWindows != null && auditOrgaWindows.size() > 0) {
                            for (AuditOrgaWindow auditOrgaWindow : auditOrgaWindows) {
                                AuditOrgaServiceCenter auditOrgaServiceCenter = iAuditOrgaServiceCenter
                                        .findAuditServiceCenterByGuid(auditOrgaWindow.getCenterguid()).getResult();
                                String areaCode = auditOrgaServiceCenter.getBelongxiaqu();
                                //3.4.1.3.1 拼接窗口详细信息
                                String windowAddress = auditOrgaServiceCenter.getOuname() + " "
                                        + auditOrgaWindow.getAddress() + " " + auditOrgaWindow.getWindowname() + " "
                                        + auditOrgaWindow.getWindowno();
                                //3.4.1.3.2 获取排队等待人数
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
                                //3.4.1.3.3 将市区县中心窗口信息作为list的第一个返回
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
                                        // 如果是乡镇法定事项或者村法定，读取audittask内的信息
                                        if (ZwfwConstant.TASKDELEGATE_TYPE_XZFD
                                                .equals(auditTaskDelegate.getDelegatetype())
                                                || ZwfwConstant.TASKDELEGATE_TYPE_CJFD
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
                            materialJson.put("pagenum", StringUtil.isBlank(auditTaskMaterial.getPage_num()) ? "0"
                                    : auditTaskMaterial.getPage_num()); //份数
                            materialJson.put("standard", StringUtil.isBlank(auditTaskMaterial.getStandard()) ? "无"
                                    : auditTaskMaterial.getStandard());//受理标准
                            materialJson.put("fileexplain", StringUtil.isBlank(auditTaskMaterial.getFile_explian())
                                    ? "无" : auditTaskMaterial.getFile_explian());//填报须知
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
                        }
                        dataJson.put("chargeitems", taskChargeItemList);
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
                    // 3.1、查询数据库方式
                    // 3.1.1、查询所有的事项信息
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(areaCode)) {
                        sqlConditionUtil.eq("areacode", areaCode.substring(0, 6));// 区域编码
                    }
                    if (StringUtil.isNotBlank(keyWord)) {
                        //过滤%和_，以免输入%或者_查出全部数据
                        if ("%".equals(keyWord) || "_".equals(keyWord)) {
                            String taskname = "/" + keyWord;
                            sqlConditionUtil.like("taskname", taskname);
                        } else {
                            sqlConditionUtil.like("taskname", keyWord);// 事项名称
                        }
                    }
                    sqlConditionUtil.eq("IS_EDITAFTERIMPORT", "1");
                    sqlConditionUtil.eq("IS_ENABLE", "1");
                    int count = 0;
                    List<AuditTask> fatherAuditTaskList = null;
                    // 3.1.1.1、获取大小项配置的系统参数
                    String newCategory = iHandleConfig.getFrameConfig("AS_ITEM_CATEGORY", "").getResult();
                    if (StringUtil.isNotBlank(newCategory) && ZwfwConstant.CONSTANT_STR_ONE.equals(newCategory)) {
                        // 3.1.1.2、获取主项事项(所有满足条件的子项所对应的大项)
                        PageData<AuditTask> fatherTaskPageData = iAuditTask
                                .getAuditEnableTaskPageData("*", sqlConditionUtil.getMap(), 0, 0, "ordernum", "desc")
                                .getResult();
                        fatherAuditTaskList = fatherTaskPageData.getList();//获取事项列表
                    } else {
                        // 3.1.1.3、未启用大小项时获取的所有事项
                        fatherAuditTaskList = iAuditTask.getAllUsableTaskByMap(keyWord, areaCode.substring(0, 6))
                                .getResult();
                    }
                    List<JSONObject> taskJsonList = new ArrayList<JSONObject>();
                    // 3.1.2、遍历所有事项
                    if (fatherAuditTaskList != null && fatherAuditTaskList.size() > 0) {
                        for (AuditTask fatherAuditTask : fatherAuditTaskList) {
                            // 3.1.2.1、判断当前区域该事项是否存在
                            if (areaCode.length() > 6) {
                                AuditTask xzAuditTask = iAuditTaskDelegate
                                        .selectUsableTaskByTaskID(fatherAuditTask.getTask_id(), areaCode).getResult();
                                if (xzAuditTask != null) {
                                    JSONObject taskJson = new JSONObject();
                                    taskJson.put("taskname", fatherAuditTask.getTaskname());// 事项名称
                                    taskJson.put("taskguid", fatherAuditTask.getRowguid());// 事项标识
                                    taskJson.put("taskid", fatherAuditTask.getTask_id());// 事项唯一标识
                                    taskJson.put("ouname", fatherAuditTask.getOuname());// 部门名称
                                    taskJson.put("applyertype", fatherAuditTask.getApplyertype());//事项类别，10：法人，20：个人
                                    taskJsonList.add(taskJson);
                                }
                            } else {
                                JSONObject taskJson = new JSONObject();
                                taskJson.put("taskname", fatherAuditTask.getTaskname());// 事项名称
                                taskJson.put("taskguid", fatherAuditTask.getRowguid());// 事项标识
                                taskJson.put("taskid", fatherAuditTask.getTask_id());// 事项唯一标识
                                taskJson.put("ouname", fatherAuditTask.getOuname());// 部门名称
                                taskJson.put("applyertype", fatherAuditTask.getApplyertype());//事项类别，10：法人，20：个人
                                taskJsonList.add(taskJson);
                            }
                        }
                    }
                    // 3.1.3、根据查询条件获取该区域下的套餐信息
                    SqlConditionUtil businessSqlConditionUtil = new SqlConditionUtil();
                    businessSqlConditionUtil.eq("businesstype", "2"); // 一般并联审批(套餐)
                    businessSqlConditionUtil.eq("del", "0"); // 非禁用
                    businessSqlConditionUtil.eq("areacode", areaCode);
                    businessSqlConditionUtil.like("businessname", keyWord);
                    List<AuditSpBusiness> auditSpBusinesses = iAuditSpBusiness
                            .getAllAuditSpBusiness(businessSqlConditionUtil.getMap()).getResult();
                    List<JSONObject> businessJsonList = new ArrayList<JSONObject>();
                    count = count + auditSpBusinesses.size();
                    for (AuditSpBusiness auditSpBusiness : auditSpBusinesses) {
                        JSONObject businessJson = new JSONObject();
                        businessJson.put("businessname", auditSpBusiness.getBusinessname());
                        businessJson.put("businessguid", auditSpBusiness.getRowguid());
                        businessJsonList.add(businessJson);
                    }
                    dataJson.put("tasklist", taskJsonList);
                    dataJson.put("businesslist", businessJsonList);
                    dataJson.put("totalcount", count);
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
                                taskJson.put("taskid", data.getString("taskid"));// 事项标识
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
     * 获取事项列表的接口（主题分类/部门分类可展示大小项）
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getTaskList", method = RequestMethod.POST)
    public String getTaskList(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getTaskList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                JSONObject returnJson = handleGetTaskList(obj, request, "getTaskList");
                log.info("=======结束调用getTaskList接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取事项列表成功", returnJson.toString());
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
     * 根据搜索条件获取事项信息的接口（个人办事\法人办事搜索可展示大小项）
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getTaskListByCondition", method = RequestMethod.POST)
    public String getTaskListByCondition(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getTaskListByCondition接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                JSONObject returnJson = handleGetTaskList(obj, request, "getTaskListByCondition");
                log.info("=======结束调用getTaskListByCondition接口=======");
                return JsonUtils.zwdtRestReturn("1", "通过条件获取事项成功", returnJson.toString());
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
     * 根据事项名称获取事项信息的接口（头部搜索框）
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getTaskListByTaskName", method = RequestMethod.POST)
    public String getTaskListByTaskName(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getTaskListByTaskName接口=======");
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
                // 1.4、获取区域编码
                String areaCode = obj.getString("areacode");
                // 1.5、获取主题当前页码
                String currentPageBusiness = obj.getString("currentpagebusiness");
                // 1.6、获取主题每页数目
                String pageSizeBusiness = obj.getString("pagesizebusiness");
                // 1.7、获取用户登录信息
                AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
                // 2、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                // 3、判断是否使用全文检索（0：查询全文检索 1：查询数据库）
                String isNeedQwjs = ConfigUtil.getConfigValue("isNeedQwjs");
                boolean isXz = false;//标识是否是乡镇延伸
                if ("1".equals(isNeedQwjs)) {
                    // 3.1、查询数据库方式
                    // 3.1.1、根据查询条件获取该区域下的事项信息
                    SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                    // 3.1.1.1、拼接事项查询条件
                    if (StringUtil.isNotBlank(areaCode)) {
                        if (areaCode.length() > 6) {
                            isXz = true;
                            sqlConditionUtil.eq("d.areacode", areaCode);// 乡镇辖区编码
                        } else {
                            sqlConditionUtil.eq("areacode", areaCode);// 区域编码
                        }
                    }
                    if (StringUtil.isNotBlank(keyWord)) {
                        // 3.1.1.2、过滤%和_，以免输入%或者_查出全部数据
                        if ("%".equals(keyWord) || "_".equals(keyWord)) {
                            String taskname = "/" + keyWord;
                            sqlConditionUtil.like("taskname", taskname);
                        } else {
                            sqlConditionUtil.like("taskname", keyWord);// 事项名称
                        }
                    }
                    sqlConditionUtil.eq("ISTEMPLATE", "0"); // 非模板事项
                    sqlConditionUtil.eq("IS_EDITAFTERIMPORT", "1");// 审核通过后事项
                    sqlConditionUtil.eq("IS_ENABLE", "1");// 启用事项
                    int firstResultTask = Integer.parseInt(currentPageTask) * Integer.parseInt(pageSizeTask);
                    // 3.1.2、事项结果返回
                    PageData<AuditTask> fatherTaskPageData = null;
                    // 3.1.2.1、判断是否使用国标编码规则
                    // 国标新事项分类规则  / 默认事项编码默认都为31位（但不会强制限制），主项结尾为 000, 子项为XXX结尾
                    String newCategory = iHandleConfig.getFrameConfig("AS_ITEM_CATEGORY", "").getResult();
                    if (StringUtil.isNotBlank(newCategory) && ZwfwConstant.CONSTANT_STR_ONE.equals(newCategory)) {
                        // 3.1.2.2、获取事项(采用国标编码规则)
                        if (isXz) {
                            fatherTaskPageData = iAuditTask.getXZGBpageDataByCondition(sqlConditionUtil.getMap(),
                                    firstResultTask, Integer.parseInt(pageSizeTask), "ordernum", "desc").getResult();
                        } else {
                            fatherTaskPageData = iAuditTask.getAuditEnableTaskPageData("*", sqlConditionUtil.getMap(),
                                    firstResultTask, Integer.parseInt(pageSizeTask), "ordernum", "desc").getResult();

                        }
                    } else {
                        //3.1.2.3、获取分页的事项信息 （无大小项）
                        if (isXz) {
                            fatherTaskPageData = iAuditTask.getXZpageDataByCondition(sqlConditionUtil.getMap(),
                                    firstResultTask, Integer.parseInt(pageSizeTask), "ordernum", "desc").getResult();
                        } else {
                            fatherTaskPageData = iAuditTask.getAuditTaskPageData(sqlConditionUtil.getMap(),
                                    firstResultTask, Integer.parseInt(pageSizeTask), "item_id", "asc").getResult();
                        }
                    }
                    int totalTaskCount = fatherTaskPageData.getRowCount();// 查询事项的数量
                    List<AuditTask> auditTasks = fatherTaskPageData.getList();// 查询事项的信息
                    List<JSONObject> fatherTaskJsonList = new ArrayList<JSONObject>(); // 返回的事项JSON列表
                    for (AuditTask auditTask : auditTasks) {
                        // 3.1.2.4、获取事项基本信息拼接成返回的JSON
                        JSONObject fatherTaskJson = commonTaskJson(auditTask, auditOnlineRegister, "0");
                        fatherTaskJsonList.add(fatherTaskJson);
                    }
                    dataJson.put("tasklist", fatherTaskJsonList);
                    dataJson.put("totalcount", totalTaskCount);
                    // 3.1.3、根据查询条件获取该区域下的套餐信息(头部搜索框)
                    if (StringUtil.isNotBlank(currentPageBusiness) && StringUtil.isNotBlank(pageSizeBusiness)) {
                        SqlConditionUtil sqlConditionUtilBusiness = new SqlConditionUtil();
                        sqlConditionUtilBusiness.eq("businesstype", "2"); // 一般并联审批（套餐）
                        sqlConditionUtilBusiness.eq("del", "0"); // 非禁用的套餐
                        if (StringUtil.isNotBlank(areaCode)) {
                            sqlConditionUtilBusiness.eq("areacode", areaCode);
                        }
                        if (StringUtil.isNotBlank(keyWord)) {
                            //过滤%和_，以免输入%或者_查出全部数据
                            if ("%".equals(keyWord) || "_".equals(keyWord)) {
                                String businessname = "/" + keyWord;
                                sqlConditionUtilBusiness.like("businessname", businessname);
                            } else {
                                sqlConditionUtilBusiness.like("businessname", keyWord);// 事项名称
                            }
                        }
                        int firstResultBusiness = Integer.parseInt(currentPageBusiness)
                                * Integer.parseInt(pageSizeBusiness);
                        // 3.1.3.1、获取分页的套餐信息
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
                            taskJson.put("taskid", data.getString("taskid"));// 事项标识
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
                                businessJson.put("daoxcnum", StringUtil.isBlank(data.getString("daoxcnum")) ? ""
                                        : iCodeItemsService.getItemTextByCodeName("现场次数", data.getString("daoxcnum")));// 到现场次数
                                businessList.add(businessJson);
                            }
                        }
                        dataJson.put("businesslist", businessList);
                        dataJson.put("businesstotalcount", businessCount);
                    }
                }
                log.info("=======结束调用getTaskListByTaskName接口=======");
                return JsonUtils.zwdtRestReturn("1", "通过事项名称获取事项成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getTaskListByTaskName接口参数：params【" + params + "】=======");
            log.info("=======getTaskListByTaskName异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "通过事项名称获取事项获取失败：" + e.getMessage(), "");
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
                        if (webApplyType != null) {
                            onlineHandle = webApplyType == 0 ? "0" : "1";
                        }
                        // 判断事项是否配置在窗口
                        boolean flag = false;
                        List<Record> centerGuids = iAuditOrgaWindow.selectCenterGuidsByTaskId(auditTask.getTask_id())
                                .getResult();
                        if (centerGuids != null && centerGuids.size() > 0) {
                            //判断事项的工作流是否异常
                            String processversionguid = workflowProcessVersionService.selectEnableProcessVersion(auditTask.getProcessguid());
                            if (processversionguid != null) {
                                List<WorkflowTransition> list = workflowTransitionService.select(processversionguid);
                                if (list != null && !list.isEmpty()) {
                                    flag = true;//事项配置在窗口且事项的工作流正常
                                } else {
                                    //尚未新增岗位
                                    flag = false;
                                }
                            } else {
                                //流程配置错误
                                flag = false;
                            }
                        }
                        if (flag) {
                            taskJson.put("onlinehandle", onlineHandle); // 事项是否可以网上申报  （事项配置在窗口，是否可以申报由事项决定）
                        } else {
                            taskJson.put("onlinehandle", ZwfwConstant.CONSTANT_STR_ZERO); // 事项是否可以网上申报 （事项未配置在窗口，不可以网上申报）
                        }
                        if (isAppoint(auditTask.getTask_id())) {
                            taskJson.put("appointment", auditTaskExtension.getReservationmanagement());// 事项是否可以网上预约
                        } else {
                            taskJson.put("appointment", "0");
                        }
                    }
                    String daoxcnum = auditTaskExtension == null ? "0" : auditTaskExtension.getDao_xc_num();
                    if (ZwfwConstant.CONSTANT_STR_ONE.equals(daoxcnum)) {
                        taskJson.put("daoxcnum", "1");//到现场次数
                    } else {
                        taskJson.put("daoxcnum", "0");//到现场次数
                    }
                    // 获取事项下放信息
                    taskJson.put("town", "0");//是否下放到乡镇
                    taskJson.put("community", "0");//是否下放到社区
                    List<AuditTaskDelegate> auditTaskDelegates = iAuditTaskDelegate
                            .selectDelegateByTaskID(auditTask.getTask_id()).getResult();
                    if (auditTaskDelegates != null && auditTaskDelegates.size() > 0) {
                        for (AuditTaskDelegate auditTaskDelegate : auditTaskDelegates) {
                            // 使用乡镇维护的数据
                            if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditTaskDelegate.getStatus())) {
                                String xzAreacode = auditTaskDelegate.getAreacode();
                                if (xzAreacode.length() == 9) {
                                    taskJson.put("town", "1");//是否下放到乡镇
                                } else if (xzAreacode.length() == 12) {
                                    taskJson.put("community", "1");//是否下放到社区
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
                        return JsonUtils.zwdtRestReturn("2", msg, "");
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
                // 1.3、区域编码
                String areacode = obj.getString("areacode");
                // 1.4、获取兼容APP图标系统参数
                String appiconmode = iHandleConfig.getFrameConfig("appiconmode", "").getResult();
                // 2、获取主题分类
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.eq("LENGTH(no)", "4");
                String basePath = "";
                String imagetype = "";//用来区分使用新版图标还是老版图标，0表示新版，1表示老版
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
                    // 2.3.1、采用新的图标
                    if (StringUtil.isNotBlank(appiconmode) && ZwfwConstant.CONSTANT_STR_TWO.equals(appiconmode)) {
                        imagetype = "0";//采用新版图标
                        if (ZwfwConstant.APPLAYERTYPE_GR.equals(applyerType)) {
                            basePath = urlRoot + "/epointzwmhwz/pages/eventdetail/newImages/";
                            sqlConditionUtil.eq("SUBSTRING(no, 1,2)", "B1");
                        } else if (ZwfwConstant.APPLAYERTYPE_QY.equals(applyerType)) {
                            basePath = urlRoot + "/epointzwmhwz/pages/legal/newImages/";
                            sqlConditionUtil.eq("SUBSTRING(no, 1,2)", "B2");
                        }
                    }
                    // 2.3.2、采用原来的图标
                    else {
                        imagetype = "1";//采用老版图标
                        if (ZwfwConstant.APPLAYERTYPE_GR.equals(applyerType)) {
                            basePath = urlRoot + "/epointzwmhwz/pages/eventdetail/images/";
                            sqlConditionUtil.eq("SUBSTRING(no, 1,2)", "B1");
                        } else if (ZwfwConstant.APPLAYERTYPE_QY.equals(applyerType)) {
                            basePath = urlRoot + "/epointzwmhwz/pages/legal/images/";
                            sqlConditionUtil.eq("SUBSTRING(no, 1,2)", "B2");
                        }
                    }
                }
                sqlConditionUtil.setOrderAsc("ORD");
                // 3、获取主题分类数据
                List<JSONObject> dictJsonList = new ArrayList<JSONObject>();
                List<AuditTaskDict> auditTaskDicts = null;
                // 3.1、区分大小项的情况返回对应事项的主题类型
                String newCategory = iHandleConfig.getFrameConfig("AS_ITEM_CATEGORY", "").getResult();
                if (StringUtil.isNotBlank(areacode)) {
                    if (StringUtil.isNotBlank(newCategory) && ZwfwConstant.CONSTANT_STR_ONE.equals(newCategory)) {
                        auditTaskDicts = iAuditTaskDict
                                .getGBAuditTaskDictListWithAreaCode(sqlConditionUtil.getMap(), areacode).getResult();
                    } else {
                        auditTaskDicts = iAuditTaskDict
                                .getAuditTaskDictListWithAreaCode(sqlConditionUtil.getMap(), areacode).getResult();
                    }
                }
                // 3.2、如果未传areacode则返回所有主题
                else {
                    auditTaskDicts = iAuditTaskDict.getAuditTaskDictList(sqlConditionUtil.getMap()).getResult();
                }
                for (AuditTaskDict auditTaskDict : auditTaskDicts) {
                    JSONObject dictObject = new JSONObject();
                    dictObject.put("dictname", auditTaskDict.getClassname()); // 主题类别名称
                    dictObject.put("dictid", auditTaskDict.getRowguid()); // 主题类别标识
                    dictObject.put("dictno", auditTaskDict.getNo()); // 主题类别编号
                    dictObject.put("dictrul", basePath + auditTaskDict.getNo() + ".png");// 主题对应的图片
                    dictJsonList.add(dictObject);
                }
                // 4、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("dictlist", dictJsonList);
                dataJson.put("imagetype", imagetype);
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
                // 2、获取事项所属的部门
                List<Record> records = iJnAppRestService.selectOuByApplyertypeNew(applyerType, areaCode);
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
                            ounamePin = ChineseToPinyin.getPingYin(ouname); //将获取的部门汉语名称转换成拼音
                        }
                        ouObject.put("ouname", record.getStr("OUSHORTNAME"));
                        ouObject.put("ouguid", record.get("ouguid"));
                        ouObject.put("ouurl", urlRoot + ounamePin + ".png");
                        ouJsonList.add(ouObject);
                    }
                }
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
                // 2、获取区域下的中心信息
                String centerGuid = "";
                List<JSONObject> centerJsonList = new ArrayList<JSONObject>();
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.setOrderDesc("ordernum");
                if (StringUtil.isBlank(taskGuid)) {
                    sqlConditionUtil.eq("belongxiaqu", areaCode);
                    List<AuditOrgaServiceCenter> auditOrgaServiceCenters = iAuditOrgaServiceCenter
                            .getAuditOrgaServiceCenterByCondition(sqlConditionUtil.getMap()).getResult();
                    if (auditOrgaServiceCenters != null && auditOrgaServiceCenters.size() > 0) {
                        for (AuditOrgaServiceCenter auditOrgaServiceCenter : auditOrgaServiceCenters) {
                            JSONObject centerJson = new JSONObject();
                            centerJson.put("centerguid", auditOrgaServiceCenter.getRowguid()); // 中心标识
                            centerJson.put("centername", auditOrgaServiceCenter.getCentername()); // 中心名称
                            centerJsonList.add(centerJson);
                        }
                    }
                    // 2.1、若区域下没有中心，则默认显示请选择
                    else {
                        JSONObject centerJson = new JSONObject();
                        centerJson.put("centerguid", ""); // 中心标识
                        centerJson.put("centername", "请选择"); // 中心名称
                        centerJsonList.add(centerJson);
                    }
                    centerGuid = centerJsonList.get(0).get("centerguid").toString();
                }

                // 3、获取区域下的窗口部门
                AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, true).getResult();
                List<JSONObject> ouJsonList = new ArrayList<JSONObject>();
                // 4、判断咨询投诉是针对事项或者是针对中心的
                if (StringUtil.isBlank(taskGuid)) {
                    // 4.1、如果事项标识为空，则咨询投诉不是针对事项的
                    JSONObject ouJson = new JSONObject();
                    ouJson.put("ouname", "请选择");
                    ouJson.put("ouguid", "");
                    ouJson.put("isselected", "true");
                    ouJsonList.add(0, ouJson);
                } else {
                    // 4.2、如果事项标识不为空，则需要获取事项所属部门（主要解决非独立部门不显示部门问题）
                    List<AuditOrgaWindow> list = iAuditOrgaWindow.getWindowListByTaskId(auditTask.getTask_id())
                            .getResult();
                    // 4.2.1、事项配置在窗口
                    if (list != null && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            JSONObject centerJson2 = new JSONObject();
                            centerJson2.put("centerguid", list.get(i).getCenterguid()); // 中心标识
                            centerJson2.put("centername", list.get(i).getCentername()); // 中心名称
                            centerJsonList.add(centerJson2);
                        }
                        centerGuid = centerJsonList.get(0).get("centerguid").toString();
                    }
                    // 4.2.2、事项未配置在窗口
                    else {
                        JSONObject centerJson = new JSONObject();
                        centerJson.put("centerguid", ""); // 中心标识
                        centerJson.put("centername", "请选择"); // 中心名称
                        centerJsonList.add(centerJson);

                        JSONObject ouJson = new JSONObject();
                        ouJson.put("ouname", "请选择");
                        ouJson.put("ouguid", "");
                        ouJson.put("isselected", "true");
                        ouJsonList.add(0, ouJson);
                        centerGuid = "";
                    }
                }
                // 4.3、获取部门
                List<String> ouguids = iAuditOrgaWindow.getoulistBycenterguid(centerGuid).getResult();
                if (ouguids != null && ouguids.size() > 0) {
                    boolean flag = false;
                    for (String string : ouguids) {
                        if (StringUtil.isNotBlank(string)) {
                            FrameOu frameOu = ouService.getOuByOuGuid(string);
                            if (frameOu != null) {
                                JSONObject ouJson = new JSONObject();
                                ouJson.put("ouname", frameOu.getOuname());// 部门名称
                                ouJson.put("ouguid", frameOu.getOuguid());// 部门标识
                                if (StringUtil.isNotBlank(taskGuid)) {
                                    // 4.3.1、如果事项标识不为空，则此部门默认被选择
                                    if (auditTask.getOuguid().equals(frameOu.getOuguid())) {
                                        ouJson.put("isselected", "true");
                                    } else {
                                        flag = true;
                                    }
                                }
                                ouJsonList.add(ouJson);
                            }
                        }
                    }
                    if (flag) {
                        JSONObject ouJson = new JSONObject();
                        ouJson.put("ouname", "请选择");
                        ouJson.put("ouguid", "");
                        ouJson.put("isselected", "true");
                        ouJsonList.add(0, ouJson);
                    }
                }
                // 5、定义返回JSON对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("oulist", ouJsonList);
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
                // 2、获取区域下的窗口部门
                List<FrameOu> frameOus = new ArrayList<FrameOu>();
                List<String> ouguids = iAuditOrgaWindow.getoulistBycenterguid(centerGuid).getResult();
                if (ouguids != null && ouguids.size() > 0) {
                    for (String string : ouguids) {
                        if (StringUtil.isNotBlank(string)) {
                            FrameOu frameOu = ouService.getOuByOuGuid(string);
                            if (frameOu != null) {
                                frameOus.add(ouService.getOuByOuGuid(string));
                            }
                        }
                    }
                }
                List<JSONObject> ouJsonList = new ArrayList<JSONObject>();
                JSONObject ouJson = new JSONObject();
                ouJson.put("ouname", "请选择");
                ouJson.put("ouguid", "");
                ouJson.put("isselected", "true");
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

    /**
     * 根据事项ID获取猜你想办事项（规则为筛选出同套餐内所有的事项）
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getRelateTaskList", method = RequestMethod.POST)
    public String getRelateTaskList(@RequestBody String params) {
        try {
            log.info("=======开始调用getRelateTaskList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                // 2、获取传递参数
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String taskid = obj.getString("taskid"); //事项id
                String taskguid = obj.getString("taskguid"); //事项id
                if (StringUtil.isNotBlank(taskguid)) {
                    AuditTask audittask = iAuditTask.getAuditTaskByGuid(taskguid, false).getResult();
                    taskid = audittask.getTask_id();
                }
                String areaCode = obj.getString("areacode").substring(0, 6); //区域
                // 3、事项ID集合，用于区域重复的taskid
                Set<String> taskidSet = new HashSet<String>();
                // 4、遍历当前区域下的所有套餐
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.eq("businesstype", "2"); // 一般并联审批(套餐)
                sqlConditionUtil.eq("del", "0"); // 非禁用
                sqlConditionUtil.eq("areacode", areaCode);
                List<AuditSpBusiness> auditSpBusinessList = iAuditSpBusiness
                        .getAllAuditSpBusiness(sqlConditionUtil.getMap()).getResult();
                for (AuditSpBusiness auditSpBusiness : auditSpBusinessList) {
                    // 4.1、定义全局taskid，用于保存该套餐下的所有事项
                    String taskids = "";
                    Boolean isRelateTask = false;
                    // 4.2、获取事项List
                    List<AuditSpTask> auditSpTaskList = iAuditSpTask
                            .getAllAuditSpTaskByBusinessGuid(auditSpBusiness.getRowguid()).getResult();
                    // 4.2、去除已经删除或禁用的事项

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
                                // 4.3、此处为判定当前是否是否在这个套餐内
                                if (auditTask.getTask_id().equals(taskid)) {
                                    isRelateTask = true;
                                }
                                // 4.4、拼接当前套餐下的taskid
                                taskids += "'" + auditTask.getTask_id() + "',";
                            }
                        }

                    }
                    // 5、如果当前事项不在套餐内，则不需要添加该事项
                    if (!isRelateTask) {
                        continue;
                    }
                    // 6、获取拼接完毕的taskid
                    taskids = taskids.substring(0, taskids.length() - 1);
                    // 7、查询当前在用版本的事项
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("IS_EDITAFTERIMPORT", "1");
                    sql.eq("IS_ENABLE", "1");
                    sql.isBlankOrValue("IS_HISTORY", "0");
                    sql.in("task_id", taskids);
                    List<AuditTask> auditTasks = iAuditTask.getAllTask(sql.getMap()).getResult();
                    if (auditTasks != null && auditTasks.size() > 0) {
                        for (AuditTask auditTask : auditTasks) {
                            // 7.1、添加到返回结果
                            taskidSet.add(auditTask.getTask_id());
                        }
                    }
                }
                // 8、返回结果Json
                JSONObject dataJson = new JSONObject();
                List<JSONObject> taskList = new ArrayList<JSONObject>();
                // 9、遍历taskid
                for (String valueTaskId : taskidSet) {
                    // 9.2、如果不为当前事项数据则返回
                    if (!valueTaskId.equals(taskid)) {
                        JSONObject taskJson = new JSONObject();
                        AuditTask auditTask = iAuditTask.getUseTaskAndExtByTaskid(valueTaskId).getResult();
                        taskJson.put("taskname", auditTask.getTaskname());
                        taskJson.put("applyertype", auditTask.getApplyertype());// 申请人类型
                        taskJson.put("taskguid", auditTask.getRowguid());// 事项guid
                        taskJson.put("ouname", auditTask.getOuname());// 部门名称
                        taskJson.put("taskid", auditTask.getTask_id());// 事项唯一标识
                        taskList.add(taskJson);
                    }
                }
                dataJson.put("tasklist", taskList);
                log.info("=======结束调用getRelateTaskList接口=======");
                return JsonUtils.zwdtRestReturn("1", "获取猜你想办列表成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getRelateTaskList接口参数：params【" + params + "】=======");
            log.info("=======getRelateTaskList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取猜你想办列表失败：" + e.getMessage(), "");
        }
    }

    /**
     * 事项基本信息的接口(碎片化)
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getStaticTask", method = RequestMethod.POST)
    public String getStaticTask(@RequestBody String params, @Context HttpServletRequest request) {
        log.info("===============碎片化服务开始===============");
        try {
            //1、查询所有可用事项
            SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
            sqlConditionUtil.eq("ISTEMPLATE", "0"); // 非模板事项
            sqlConditionUtil.eq("IS_EDITAFTERIMPORT", "1");// 审核通过后事项
            sqlConditionUtil.eq("IS_ENABLE", "1");// 启用事项
            sqlConditionUtil.isBlankOrValue("IS_HISTORY", "0"); // 判断是否为历史版本
            // 取出事项总数
            int taskCount = iAuditTask.getTasknumByCondition(sqlConditionUtil.getMap()).getResult();
            // 得出需要循环的次数
            int count = (int) Math.round(taskCount / 20.0);
            for (int key = 0; key < count; key++) {
                PageData<AuditTask> auditTaskPageData = iAuditTask
                        .getAuditTaskPageData(sqlConditionUtil.getMap(), 20 * key, 20, "OperateDate", "desc")
                        .getResult();
                List<AuditTask> list = auditTaskPageData.getList();
                int total = auditTaskPageData.getRowCount();
                System.out.println(total);
                if (list != null && list.size() > 0) {
                    log.info("第" + (key + 1) + "次==========查询出" + list.size() + "条可用事项数据===============");
                    for (AuditTask auditTask : list) {
                        // 2、获取事项扩展信息
                        AuditTaskExtension auditTaskExtension = iAuditTaskExtension
                                .getTaskExtensionByTaskGuid(auditTask.getRowguid(), true).getResult();
                        // 3、定义返回JSON对象
                        JSONObject dataJson = new JSONObject();
                        // 4、返回事项基本信息
                        dataJson.put("taskguid", auditTask.getRowguid());// 事项标识
                        dataJson.put("taskid", auditTask.getTask_id());// 事项业务唯一标识
                        dataJson.put("taskname", auditTask.getTaskname());// 事项名称
                        dataJson.put("itemid", auditTask.getItem_id());// 权利编码
                        JSONObject taskElementJson = new JSONObject();
                        Integer webApplyType = auditTaskExtension == null ? 0
                                : (auditTaskExtension.getWebapplytype() == null ? 0
                                : auditTaskExtension.getWebapplytype());
                        String onlineHandle = "0";
                        if (webApplyType != null) {
                            onlineHandle = webApplyType == 0 ? "0" : "1";
                        }
                        // 判断事项是否配置在窗口
                        boolean flag = false;
                        List<Record> centerGuids = iAuditOrgaWindow.selectCenterGuidsByTaskId(auditTask.getTask_id())
                                .getResult();
                        if (centerGuids != null && centerGuids.size() > 0) {
                            //判断事项的工作流是否异常
                            String processversionguid = workflowProcessVersionService.selectEnableProcessVersion(auditTask.getProcessguid());
                            if (processversionguid != null) {
                                List<WorkflowTransition> workflowlist = workflowTransitionService.select(processversionguid);
                                if (workflowlist != null && !workflowlist.isEmpty()) {
                                    flag = true;//事项配置在窗口且事项的工作流正常
                                } else {
                                    //尚未新增岗位
                                    flag = false;
                                }
                            } else {
                                //流程配置错误
                                flag = false;
                            }
                        }
                        if (flag) {
                            taskElementJson.put("onlinehandle", onlineHandle); // 事项是否可以网上申报  （事项配置在窗口，是否可以申报由事项决定）
                        } else {
                            taskElementJson.put("onlinehandle", ZwfwConstant.CONSTANT_STR_ZERO); // 事项是否可以网上申报 （事项未配置在窗口，不可以网上申报）
                        }
                        if (isAppoint(auditTask.getTask_id())) {
                            taskElementJson.put("appointment",
                                    auditTaskExtension == null ? "0" : auditTaskExtension.getReservationmanagement());//事项是否可以网上预约
                        } else {
                            taskElementJson.put("appointment", "0");
                        }
                        dataJson.put("taskelement", taskElementJson);
                        dataJson.put("qrcontent", WebUtil.getRequestCompleteUrl(request)
                                + "/epointzwmhwz/pages/mobile/html/apptaskdetail?taskguid=" + auditTask.getRowguid());//二维码内容
                        dataJson.put("handlecondition", StringUtil.isBlank(auditTask.getAcceptcondition()) ? ""
                                : auditTask.getAcceptcondition());// 受理条件
                        String taskOutImgGuid = auditTask.getTaskoutimgguid();
                        if (StringUtil.isNotBlank(taskOutImgGuid)) {
                            List<FrameAttachInfo> frameAttachInfos = iAttachService
                                    .getAttachInfoListByGuid(taskOutImgGuid);
                            if (frameAttachInfos != null && frameAttachInfos.size() > 0) {
                                taskOutImgGuid = frameAttachInfos.get(0).getAttachGuid();
                                dataJson.put("taskoutimg", ConfigUtil.getConfigValue("zwdturl")
                                        + "/rest/auditattach/readAttach?attachguid=" + taskOutImgGuid);// 办理流程
                            }
                        }
                        dataJson.put("setting", StringUtil.isBlank(auditTask.getBy_law()) ? "" : auditTask.getBy_law());//事项设定依据
                        dataJson.put("result", auditTask.getQuery_method());//事项结果查询
                        JSONObject taskBasicJson = new JSONObject();
                        taskBasicJson.put("itemid", auditTask.getItem_id()); // 事项编码
                        taskBasicJson.put("qlkind", iCodeItemsService.getItemTextByCodeName("审批类别",
                                String.valueOf(auditTask.getShenpilb()))); // 事项审批类别
                        taskBasicJson.put("implementsubject", auditTask.getOuname()); // 实施主体
                        String useLevel = auditTaskExtension == null ? "" : auditTaskExtension.getUse_level();
                        String exerciseClass = "";
                        if (StringUtil.isNotBlank(useLevel)) {
                            String[] userLevelArr = useLevel.split(";");
                            for (int i = 0; i < userLevelArr.length; i++) {
                                exerciseClass = iCodeItemsService.getItemTextByCodeName("行使层级", userLevelArr[i]) + ";";
                            }
                            exerciseClass = exerciseClass.substring(0, exerciseClass.length() - 1);
                        }
                        taskBasicJson.put("exerciseclass", exerciseClass);//行使层级
                        String applyerType = auditTask.getApplyertype();//申请人类型
                        String managementObj = "";
                        if (StringUtil.isNotBlank(applyerType)) {
                            String[] applyertypearr = applyerType.split(";");
                            for (int i = 0; i < applyertypearr.length; i++) {
                                managementObj = iCodeItemsService.getItemTextByCodeName("申请人类型", applyertypearr[i])
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
                        // 5 获取办理情况
                        List<JSONObject> taskHandleJsonList = new ArrayList<JSONObject>();
                        List<AuditOrgaWindow> auditOrgaWindows = iAuditOrgaWindow
                                .getWindowListByTaskId(auditTask.getTask_id()).getResult();
                        //  标志当前循环中的窗口是否是第一个进入循环的市区县的窗口。
                        Boolean firstWindow = true;
                        if (auditOrgaWindows != null && auditOrgaWindows.size() > 0) {
                            for (AuditOrgaWindow auditOrgaWindow : auditOrgaWindows) {
                                AuditOrgaServiceCenter auditOrgaServiceCenter = iAuditOrgaServiceCenter
                                        .findAuditServiceCenterByGuid(auditOrgaWindow.getCenterguid()).getResult();
                                String areaCode = auditOrgaServiceCenter.getBelongxiaqu();
                                //5.1 拼接窗口详细信息
                                String windowAddress = auditOrgaServiceCenter.getOuname() + " "
                                        + auditOrgaWindow.getAddress() + " " + auditOrgaWindow.getWindowname() + " "
                                        + auditOrgaWindow.getWindowno();
                                //5.2 获取排队等待人数
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
                                //3.4.1.3.3 将市区县中心窗口信息作为list的第一个返回
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
                                }
                            }
                        }
                        taskBasicJson.put("addressandtimelist", taskHandleJsonList);
                        if (auditTaskExtension != null) {
                            taskBasicJson.put("dao_xc_num", StringUtil.isBlank(auditTaskExtension.getDao_xc_num()) ? ""
                                    : auditTaskExtension.getDao_xc_num());// 到办事窗口的最少次数
                            taskBasicJson.put("finishfilename", auditTaskExtension.getFinishfilename());// 审批结果名称
                            String finishproductsamples = auditTaskExtension.getFinishproductsamples();
                            if (StringUtil.isNotBlank(finishproductsamples)) {
                                List<FrameAttachInfo> frameAttachInfos = iAttachService
                                        .getAttachInfoListByGuid(finishproductsamples);
                                if (frameAttachInfos != null && frameAttachInfos.size() > 0) {
                                    taskBasicJson.put("finishproductsamples",
                                            frameAttachInfos.get(0).getAttachFileName());// 结果样本名称
                                    taskBasicJson.put("finishproductguid", finishproductsamples);// 审批结果标识
                                }
                            }
                        }
                        taskBasicJson.put("limit_num", auditTask.getLimit_num());// 数量限制
                        dataJson.put("taskbasic", taskBasicJson);
                        // 5、获取材料多情形
                        List<AuditTaskCase> auditTaskCases = iAuditTaskCase
                                .selectTaskCaseByTaskGuid(auditTask.getRowguid()).getResult();
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
                        String firstCaseGuid = "";
                        if (caseConditionList != null && caseConditionList.size() > 0) {
                            firstCaseGuid = caseConditionList.get(0).getString("taskcaseguid");
                        }
                        List<JSONObject> taskMaterialList = new ArrayList<JSONObject>();
                        // 6、获取事项材料信息
                        // 6.1、材料有情形的情况
                        if (StringUtil.isNotBlank(firstCaseGuid)) {
                            List<AuditTaskMaterialCase> auditTaskMaterialCases = iAuditTaskMaterialCase
                                    .selectTaskMaterialCaseByCaseGuid(firstCaseGuid).getResult();
                            for (AuditTaskMaterialCase auditTaskMaterialCase : auditTaskMaterialCases) {
                                AuditTaskMaterial auditTaskMaterial = iAuditTaskMaterial
                                        .getAuditTaskMaterialByRowguid(auditTaskMaterialCase.getMaterialguid())
                                        .getResult();
                                // 6.1.1、拼接材料返回JSON
                                JSONObject materialJson = staticTaskJson(auditTaskMaterial, auditTaskMaterialCase);
                                taskMaterialList.add(materialJson);
                            }
                        }
                        // 6.2、没有情形的情况
                        else {
                            List<AuditTaskMaterial> auditTaskMaterials = iAuditTaskMaterial
                                    .selectTaskMaterialListByTaskGuid(auditTask.getRowguid(), true).getResult();
                            if (auditTaskMaterials != null && auditTaskMaterials.size() > 0) {
                                // 6.2.1、对事项材料进行排序
                                Collections.sort(auditTaskMaterials, new Comparator<AuditTaskMaterial>() {
                                    @Override
                                    public int compare(AuditTaskMaterial b1, AuditTaskMaterial b2) {
                                        // 6.2.1.1、优先对比材料必要性（必要在前）
                                        int comNecessity = b1.getNecessity().compareTo(b2.getNecessity());
                                        int ret = comNecessity;
                                        // 6.2.1.2、材料必要性一致的情况下对比排序号（排序号降序排）
                                        if (comNecessity == 0) {
                                            Integer ordernum1 = b1.getOrdernum() == null ? 0 : b1.getOrdernum();
                                            Integer ordernum2 = b2.getOrdernum() == null ? 0 : b2.getOrdernum();
                                            ret = ordernum2.compareTo(ordernum1);
                                        }
                                        return ret;
                                    }
                                });
                                // 6.2.2、拼接材料返回JSON
                                for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
                                    JSONObject materialJson = staticTaskJson(auditTaskMaterial, null);
                                    taskMaterialList.add(materialJson);
                                }
                            }
                        }
                        dataJson.put("taskmaterials", taskMaterialList);
                        // 7、获取事项收费标准
                        List<JSONObject> taskChargeItemList = new ArrayList<JSONObject>();
                        List<AuditTaskChargeItem> auditTaskChargeItems = iAuditTaskChargeItem
                                .selectAuditTaskChargeItemByTaskGuid(auditTask.getRowguid(), true).getResult();
                        if (auditTaskChargeItems != null && auditTaskChargeItems.size() > 0) {
                            for (AuditTaskChargeItem auditTaskChargeItem : auditTaskChargeItems) {
                                JSONObject chargeItemJson = new JSONObject();
                                chargeItemJson.put("chargeitemname", auditTaskChargeItem.getItemname());// 收费项目名称
                                chargeItemJson.put("chargeitemguid", auditTaskChargeItem.getRowguid());// 收费项目标识
                                chargeItemJson.put("unit", auditTaskChargeItem.getUnit());// 收费单位
                                chargeItemJson.put("chargeitemstand", auditTaskChargeItem.getChargeitem_stand());// 收费标准
                                taskChargeItemList.add(chargeItemJson);
                            }
                        }
                        dataJson.put("chargeitems", taskChargeItemList);
                        // 8、获取事项常见问题
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
                        log.info("=======开始生成json文件=======");
                        //9、生成事项json文件
                        String filePath = ClassPathUtil.getDeployWarPath() + "epointzwmhwz/json/"
                                + auditTask.getTask_id() + ".json";

                        createFile(filePath, dataJson.toString());
                    }
                }
            }
            log.info("===============碎片化服务结束===============");
            return JsonUtils.zwdtRestReturn("1", "生成静态化事项数据成功", "");

        } catch (Exception e) {
            e.printStackTrace();
            return JsonUtils.zwdtRestReturn("1", "生成静态化事项数据失败", "");
        }
    }

    /**
     * 乡镇、社区主题分类的接口
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/getXZTaskThemes", method = RequestMethod.POST)
    public String getXZTaskThemes(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用getXZTaskThemes接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、区域编码
                String areaCode = obj.getString("areacode");
                List<AuditTaskDict> auditTaskDictList;
                // 2、区分大小项的情况返回对应事项的主题类型
                String newCategory = iHandleConfig.getFrameConfig("AS_ITEM_CATEGORY", "").getResult();
                if (StringUtil.isNotBlank(newCategory) && ZwfwConstant.CONSTANT_STR_ONE.equals(newCategory)) {
                    auditTaskDictList = iAuditTaskDict.getXZGBTaskDictListByAreacode(areaCode).getResult();
                } else {
                    auditTaskDictList = iAuditTaskDict.getXZTaskDictListByAreacode(areaCode).getResult();
                }
                // 3、获取主题分类数据
                List<JSONObject> dictJsonList = new ArrayList<JSONObject>();
                for (AuditTaskDict auditTaskDict : auditTaskDictList) {
                    JSONObject dictObject = new JSONObject();
                    dictObject.put("dictname", auditTaskDict.getClassname()); // 主题类别名称
                    dictObject.put("dictid", auditTaskDict.getRowguid()); // 主题类别标识
                    dictObject.put("dictno", auditTaskDict.getNo()); // 主题类别编号
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
            log.info("=======getXZTaskThemes接口参数：params【" + params + "】=======");
            log.info("=======getXZTaskThemes异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "获取乡镇事项主题分类列表失败：" + e.getMessage(), "");
        }
    }

    /**
     * 判断收藏的事项是否有子项接口
     *
     * @param params  接口的入参
     * @param request HTTP请求
     * @return
     */
    @RequestMapping(value = "/private/isParentTask", method = RequestMethod.POST)
    public String isParentTask(@RequestBody String params, @Context HttpServletRequest request) {
        try {
            log.info("=======开始调用isParentTask接口=======");
            // 1、接口入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取事项标识
                String taskguid = obj.getString("clientidentifier");
                // 2、定义是否为大项标志位
                String isParentTask = "0";
                // 3、获取事项
                AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskguid, false).getResult();
                // 4、获取大小项系统参数
                String newCategory = iHandleConfig.getFrameConfig("AS_ITEM_CATEGORY", "").getResult();
                // 4.1、如果启用了大小项
                if (StringUtil.isNotBlank(newCategory) && ZwfwConstant.CONSTANT_STR_ONE.equals(newCategory)) {
                    if (auditTask != null) {
                        String itemid = auditTask.getItem_id().substring(0, auditTask.getItem_id().length() - 3);
                        String lastItemId = auditTask.getItem_id().substring(auditTask.getItem_id().length() - 3);
                        // 4.1.1、判断该事项是否为大项
                        if ("000".equals(lastItemId)) {
                            // 4.1.1.1、查询该大项下是否有可用子项
                            List<AuditTask> childAuditTaskList = iAuditTask
                                    .selectUsableTaskItemListByItemId(itemid, auditTask.getItem_id()).getResult();
                            if (childAuditTaskList != null && childAuditTaskList.size() > 0) {
                                isParentTask = "1";
                            }
                        }
                    }
                }
                // 定义Json返回对象
                JSONObject dataJson = new JSONObject();
                dataJson.put("isParentTask", isParentTask);
                log.info("=======结束调用isParentTask接口=======");
                return JsonUtils.zwdtRestReturn("1", "判断收藏的事项是否有子项成功", dataJson.toString());
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======isParentTask接口参数：params【" + params + "】=======");
            log.info("=======isParentTask异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "判断收藏的事项是否有子项失败：" + e.getMessage(), "");
        }
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
                // 2、根据taskId判断事项要素是否满足条件（选项配有材料或者后置要素的要素选项配有材料）
                boolean isExist = iAuditTaskElementService.checkELementMaterial(taskId).getResult();
                // 2.1、 根据isExist的值，判定是否需要进行场景选择
                String isNeedFaq = (isExist) ? ZwfwConstant.CONSTANT_STR_ONE : ZwfwConstant.CONSTANT_STR_ZERO;
                JSONObject dataJson = new JSONObject();
                dataJson.put("isneedfaq", isNeedFaq);
                log.info("=======结束调用checkIsExistElement接口=======");
                return JsonUtils.zwdtRestReturn("1", "判断事项是否配有要素成功", dataJson.toString());
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
                // 定义存储要素信息的List
                List<JSONObject> elementJsonList = new ArrayList<JSONObject>();
                if (auditTaskElements != null && auditTaskElements.size() > 0) {
                    for (AuditTaskElement auditTaskElement : auditTaskElements) {
                        // 定义单多选标志位，默认单选
                        String type = "radio";
                        String elementName = "[单选]";
                        // 定义存储要素信息的json
                        JSONObject elementJson = new JSONObject();
                        // 2.2 根据事项要素唯一标识查询事项要素选项
                        List<AuditTaskOption> auditTaskOptions = iAuditTaskOptionService
                                .findListByElementIdWithoutNoName(auditTaskElement.getRowguid()).getResult();
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
                            elementJson.put("type", type); // 单选还是多选的标志位
                            elementJson.put("optionlist", optionJsonList); // 要素选项列表
                            elementJson.put("elementquestion", elementName + auditTaskElement.getElementname()); // 要素问题
                            elementJson.put("elementname", auditTaskElement.getElementname()); // 要素名称
                            elementJson.put("elementguid", auditTaskElement.getRowguid()); // 要素唯一标识
                            elementJsonList.add(elementJson);
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
                            subElementJson.put("type", type); // 单选还是多选的标志位
                            subElementJson.put("optionlist", optionJsonList); // 要素选项列表
                            subElementJson.put("subelementquestion",
                                    elementName + auditTaskElement.getElementname()); // 要素问题
                            subElementJson.put("subelementguid", auditTaskElement.getRowguid()); // 要素唯一标识
                            subElementJson.put("subelementname", auditTaskElement.getElementname()); // 要素名称
                            subElementJsonList.add(subElementJson);
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
     * 获取事项办理窗口信息
     *
     * @param params 接口的入参
     * @return
     */
    @RequestMapping(value = "/getWindowList", method = RequestMethod.POST)
    public String getWindowList(@RequestBody String params) {
        try {
            log.info("=======开始调用getWindowList接口=======");
            // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                // 1.1、获取事项唯一标识
                String taskGuid = obj.getString("taskguid");
                // 1.2、获取事项信息
                AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, true).getResult();
                if (auditTask != null) {
                    // 2.1 获取办理情况
                    List<JSONObject> taskHandleJsonList = new ArrayList<JSONObject>();
                    List<AuditOrgaWindow> auditOrgaWindows = iAuditOrgaWindow
                            .getWindowListByTaskId(auditTask.getTask_id()).getResult();
                    //  标志当前循环中的窗口是否是第一个进入循环的市区县的窗口。
                    Boolean firstWindow = true;
                    if (auditOrgaWindows != null && auditOrgaWindows.size() > 0) {
                        for (AuditOrgaWindow auditOrgaWindow : auditOrgaWindows) {
                            AuditOrgaServiceCenter auditOrgaServiceCenter = iAuditOrgaServiceCenter
                                    .findAuditServiceCenterByGuid(auditOrgaWindow.getCenterguid()).getResult();
                            String areaCode = auditOrgaServiceCenter.getBelongxiaqu();
                            // 2.1 拼接窗口详细信息
                            String windowAddress = auditOrgaServiceCenter.getOuname() + " "
                                    + auditOrgaWindow.getAddress() + " " + auditOrgaWindow.getWindowname() + " "
                                    + auditOrgaWindow.getWindowno();
                            // 2.2 获取排队等待人数
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
                            // 2.3 将市区县中心窗口信息作为list的第一个返回
                            if (areaCode.length() == 6 && firstWindow) {
                                firstWindow = false;
                                JSONObject taskBaseHandleJson = new JSONObject();
                                taskBaseHandleJson.put("windowaddress", windowAddress); // 中心窗口地址
                                taskBaseHandleJson.put("handleaddress", auditTask.getTransact_addr());// 办公地点
                                taskBaseHandleJson.put("handletime", auditTask.getTransact_time());// 办公时间
                                taskBaseHandleJson.put("linktel", auditTask.getLink_tel());// 投诉电话
                                taskBaseHandleJson.put("waitnum",
                                        iHandleQueue.getTaskWaitNum(taskTypeGuid, true).getResult()); // 等待人数
                                taskBaseHandleJson.put("leftqueuenum", leftNum); // 剩余取号数
                                taskHandleJsonList.add(0, taskBaseHandleJson);
                                continue;
                            }
                            // 2.4 List的第一个已经是市区县中心窗口信息则加在其之后
                            if (areaCode.length() == 6 && !firstWindow) {
                                JSONObject taskHandleJson = new JSONObject();
                                taskHandleJson.put("windowaddress", windowAddress);// 中心窗口地址
                                taskHandleJson.put("handleaddress", auditTask.getTransact_addr());// 办公地点
                                taskHandleJson.put("handletime", auditTask.getTransact_time());// 办公时间
                                taskHandleJson.put("linktel", auditTask.getLink_tel());// 投诉电话
                                taskHandleJson.put("waitnum",
                                        iHandleQueue.getTaskWaitNum(taskTypeGuid, true).getResult()); // 等待人数
                                taskHandleJson.put("leftqueuenum", leftNum); // 剩余取号数
                                taskHandleJsonList.add(taskHandleJson);
                            }
                            // 2.5 将乡镇，村窗口信息返回
                            else {
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
                                    } else {
                                        // 如果是乡镇下放事项，读取乡镇个性化信息
                                        taskHandleJson.put("handleaddress", auditTaskDelegate.getApplyaddress());// 办公地点
                                        taskHandleJson.put("handletime", auditTaskDelegate.getApplytime());// 办公时间
                                        taskHandleJson.put("linktel", auditTaskDelegate.getLink_tel());// 投诉电话
                                        taskHandleJson.put("waitnum",
                                                iHandleQueue.getTaskWaitNum(taskTypeGuid, true).getResult()); // 等待人数
                                        taskHandleJson.put("leftqueuenum", leftNum); // 剩余取号数
                                        taskHandleJsonList.add(taskHandleJson);
                                    }
                                }
                            }
                        }
                    }
                    // 3. 返回窗口信息
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("addressandtimelist", taskHandleJsonList);
                    log.info("=======结束调用getWindowList接口=======");
                    return JsonUtils.zwdtRestReturn("1", "窗口信息获取成功", dataJson.toString());
                } else {
                    return JsonUtils.zwdtRestReturn("0", "获取事项失败！", "");
                }
            } else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("=======getWindowList接口参数：params【" + params + "】=======");
            log.info("=======getWindowList异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "窗口信息获取失败：" + e.getMessage(), "");
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

    /******************************************** 暂时无用接口 **********************************************/
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
                // 1.3、获取事项业务唯一标识
                String taskId = obj.getString("taskid");
                // 2、获取事项的常见问题
                SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                sqlConditionUtil.eq("taskid", taskId);
                List<AuditTaskFaq> auditTaskFaqs = iAuditTaskFaq.selectAuditTaskFaqByPage(sqlConditionUtil.getMap(),
                        Integer.parseInt(currentPage) * Integer.parseInt(pageSize), Integer.parseInt(pageSize),
                        "ordernum", "desc").getResult();
                List<JSONObject> taskfaqList = new ArrayList<JSONObject>();
                if (auditTaskFaqs != null && auditTaskFaqs.size() > 0) {
                    for (AuditTaskFaq auditTaskFaq : auditTaskFaqs) {
                        JSONObject faqJson = new JSONObject();
                        faqJson.put("faqguid", auditTaskFaq.getRowguid());// 常见问题标识
                        faqJson.put("question", auditTaskFaq.getQuestion());// 问题
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
                    // 获取申请人类型
                    String applyerType = auditTask.getApplyertype();
                    String managementObj = "";
                    if (StringUtil.isNotBlank(applyerType)) {
                        String[] applyertypearr = applyerType.split(";");
                        for (int i = 0; i < applyertypearr.length; i++) {
                            managementObj = iCodeItemsService.getItemTextByCodeName("申请人类型", applyertypearr[i]) + ";";
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

    /**
     * 是否符合json规范
     *
     * @param json
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static boolean isGoodJson(String json) {
        try {
            JSONObject.parseObject(json);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }

    /**
     * 判断是否收藏
     * [功能详细描述]
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
     * 获取通用的json返回
     * getTaskListByCondition用
     *
     * @param auditTask
     * @param auditOnlineRegister
     * @return
     */
    public JSONObject commonTaskJson(AuditTask auditTask, AuditOnlineRegister auditOnlineRegister, String type) {
        JSONObject auditTaskJson = new JSONObject();
        auditTask = iAuditTask.getAuditTaskByGuid(auditTask.getRowguid(), true).getResult();
        AuditTaskExtension auditTaskExtension = iAuditTaskExtension
                .getTaskExtensionByTaskGuid(auditTask.getRowguid(), true).getResult();
        auditTaskJson.put("taskname", auditTask.getTaskname());// 事项名称
        auditTaskJson.put("taskguid", auditTask.getRowguid());// 事项标识
        auditTaskJson.put("taskid", auditTask.getTask_id());// 事项唯一标识
        auditTaskJson.put("ouname", auditTask.getOuname());// 部门名称
        // 判断事项预约状态
        if (isAppoint(auditTask.getTask_id())) {
            auditTaskJson.put("appointment",
                    auditTaskExtension == null ? "0" : auditTaskExtension.getReservationmanagement());// 事项是否可以网上预约
        } else {
            auditTaskJson.put("appointment", "0");
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
        String managementObj = ""; // 办理对象
        if (StringUtil.isNotBlank(auditTask.getApplyertype())) {
            String[] applyerTypeArr = auditTask.getApplyertype().split(";");
            for (int i = 0; i < applyerTypeArr.length; i++) {
                managementObj = iCodeItemsService.getItemTextByCodeName("申请人类型", applyerTypeArr[i]) + ";";
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
        Integer webApplyType = auditTaskExtension == null ? 0
                : (auditTaskExtension.getWebapplytype() == null ? 0 : auditTaskExtension.getWebapplytype());
        String onlineHandle = "0";
        if (webApplyType != null) {
            onlineHandle = webApplyType == 0 ? "0" : "1";
        }
        // 判断事项是否配置在窗口
        boolean flag = false;
        List<Record> centerGuids = iAuditOrgaWindow.selectCenterGuidsByTaskId(auditTask.getTask_id()).getResult();
        if (centerGuids != null && centerGuids.size() > 0) {
            //判断事项的工作流是否异常
            String processversionguid = workflowProcessVersionService.selectEnableProcessVersion(auditTask.getProcessguid());
            if (processversionguid != null) {
                List<WorkflowTransition> list = workflowTransitionService.select(processversionguid);
                if (list != null && !list.isEmpty()) {
                    flag = true;//事项配置在窗口且事项的工作流正常
                } else {
                    //尚未新增岗位
                    flag = false;
                }
            } else {
                //流程配置错误
                flag = false;
            }
        }
        if (flag) {
            auditTaskJson.put("onlinehandle", onlineHandle); // 事项是否可以网上申报  （事项配置在窗口，是否可以申报由事项决定）
        } else {
            auditTaskJson.put("onlinehandle", ZwfwConstant.CONSTANT_STR_ZERO); // 事项是否可以网上申报 （事项未配置在窗口，不可以网上申报）
        }
        if (isAppoint(auditTask.getTask_id())) {
            auditTaskJson.put("appointment",
                    auditTaskExtension == null ? "0" : auditTaskExtension.getReservationmanagement());// 事项是否可以网上预约
        } else {
            auditTaskJson.put("appointment", "0");
        }
        String daoxcnum = auditTaskExtension == null ? "0" : auditTaskExtension.getDao_xc_num();
        if (ZwfwConstant.CONSTANT_STR_ONE.equals(daoxcnum)) {
            auditTaskJson.put("daoxcnum", "1");//到现场次数
        } else {
            auditTaskJson.put("daoxcnum", "0");//到现场次数
        }
        // 获取事项下放信息
        auditTaskJson.put("town", "0");//是否下放到乡镇
        auditTaskJson.put("community", "0");//是否下放到社区
        List<AuditTaskDelegate> auditTaskDelegates = iAuditTaskDelegate.selectDelegateByTaskID(auditTask.getTask_id())
                .getResult();
        if (auditTaskDelegates != null && auditTaskDelegates.size() > 0) {
            for (AuditTaskDelegate auditTaskDelegate : auditTaskDelegates) {
                // 使用乡镇维护的数据
                if (ZwfwConstant.CONSTANT_STR_ONE.equals(auditTaskDelegate.getStatus())) {
                    String xzAreacode = auditTaskDelegate.getAreacode();
                    if (xzAreacode.length() == 9) {
                        auditTaskJson.put("town", "1");//是否下放到乡镇
                    } else if (xzAreacode.length() == 12) {
                        auditTaskJson.put("community", "1");//是否下放到社区
                    }
                }
            }
        }
        if (auditTaskExtension != null) {
            auditTaskJson.put("powerdeline", auditTaskExtension.getPowerdeline());//权限划分
            auditTaskJson.put("if_online_sb", auditTaskExtension.getStr("IF_ONLINE_SB"));//是否网办
            auditTaskJson.put("service_dept", auditTask.getStr("SERVICE_DEPT"));//是否存在中介服务
            auditTaskJson.put("finishproductsamples", auditTaskExtension.getStr("FINISHPRODUCTSAMPLES"));//结果样本
            auditTaskJson.put("if_express", auditTaskExtension.getStr("IF_EXPRESS"));//是否支持物流快递
            auditTaskJson.put("reservationmangement", auditTaskExtension.getStr("RESERVATIONMANAGEMENT"));//是否支持预约办理
        }
        return auditTaskJson;
    }

    /**
     * 通用JSON返回
     *
     */
    public JSONObject staticTaskJson(AuditTaskMaterial auditTaskMaterial, AuditTaskMaterialCase auditTaskMaterialCase) {
        JSONObject materialJson = new JSONObject();
        if (auditTaskMaterial != null) {
            materialJson.put("materialguid", auditTaskMaterial.getRowguid());// 材料标识
            materialJson.put("materialname", auditTaskMaterial.getMaterialname());// 材料名称
            if (StringUtil.isNotBlank(auditTaskMaterial.getExampleattachguid())) {
                int exampleAttachCount = iAttachService
                        .getAttachCountByClientGuid(auditTaskMaterial.getExampleattachguid());
                if (exampleAttachCount > 0) {
                    materialJson.put("exampleattachguid", auditTaskMaterial.getExampleattachguid());//填报示例对应附件标识
                }
            }
            if (StringUtil.isNotBlank(auditTaskMaterial.getTemplateattachguid())) {
                int templateAttachCount = iAttachService
                        .getAttachCountByClientGuid(auditTaskMaterial.getTemplateattachguid());
                if (templateAttachCount > 0) {
                    materialJson.put("templateattachguid", auditTaskMaterial.getTemplateattachguid());//空白模板对应附件标识
                }
            }
            String materialsource;
            if (StringUtil.isBlank(auditTaskMaterial.getFile_source())) {
                materialsource = "申请人自备";
            } else {
                materialsource = iCodeItemsService.getItemTextByCodeName("来源渠道", auditTaskMaterial.getFile_source());//材料来源渠道
            }
            materialJson.put("materialsource", materialsource);
            String necessary = "0";
            if (auditTaskMaterialCase != null) {
                necessary = ZwfwConstant.NECESSITY_SET_YES.equals(String.valueOf(auditTaskMaterialCase.getNecessity()))
                        ? "1" : "0";// 是否必需
            } else {
                necessary = ZwfwConstant.NECESSITY_SET_YES.equals(String.valueOf(auditTaskMaterial.getNecessity()))
                        ? "1" : "0";// 是否必需
            }
            materialJson.put("necessary", necessary);//事项材料必要性
            materialJson.put("pagenum",
                    StringUtil.isBlank(auditTaskMaterial.getPage_num()) ? "0" : auditTaskMaterial.getPage_num()); //份数
            materialJson.put("standard",
                    StringUtil.isBlank(auditTaskMaterial.getStandard()) ? "无" : auditTaskMaterial.getStandard());//受理标准
            materialJson.put("fileexplain", StringUtil.isBlank(auditTaskMaterial.getFile_explian()) ? "无"
                    : auditTaskMaterial.getFile_explian());//填报须知
        }
        return materialJson;
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
        // 获取事项当前页码
        String currentPageTask = obj.getString("currentpage");
        // 获取事项每页数目
        String pageSizeTask = obj.getString("pagesize");
        // 获取搜索条件
        String keyWord = obj.getString("searchcondition");
        // 获取区域编码
        String areaCode = obj.getString("areacode");
        // 获取主题分类标识
        String dictId = obj.getString("dictid");
        // 获取部门标识
        String ouGuid = obj.getString("ouguid");
        String applyerType = "";
        // 获取申请人类型
        if ("getTaskList".equals(function)) {
            applyerType = obj.getString("usertype");
        } else {
            applyerType = obj.getString("applyertype");
        }
        // 1.11、获取用户登录信息
        AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
        // 2、定义返回JSON对象
        JSONObject dataJson = new JSONObject();
        boolean isXz = false;//标识是否是乡镇延伸
        // 3.1、查询数据库方式
        // 3.1.1、根据查询条件获取该区域下的事项信息
        SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
        // 3.1.1.1、拼接事项查询条件
        if (StringUtil.isNotBlank(areaCode)) {
            if (areaCode.length() == 6) {
                sqlConditionUtil.eq("a.areacode", areaCode);// 区域编码
            } else {
                sqlConditionUtil.eq("d.areacode", areaCode);// 乡镇辖区编码
                isXz = true;
            }
        }
        if (StringUtil.isNotBlank(ouGuid)) {
            sqlConditionUtil.eq("a.ouguid", ouGuid);// 部门标识
        }
        if (StringUtil.isNotBlank(applyerType)) {
            sqlConditionUtil.like("applyertype", applyerType);// 申请人类型
        }
        if (StringUtil.isNotBlank(dictId)) {
            sqlConditionUtil.eq("dict_id", dictId);// 主题分类标识
        }
        if (StringUtil.isNotBlank(keyWord)) {
            // 3.1.1.2、过滤%和_，以免输入%或者_查出全部数据
            if ("%".equals(keyWord) || "_".equals(keyWord)) {
                String taskname = "/" + keyWord;
                sqlConditionUtil.like("taskname", taskname);
            } else {
                sqlConditionUtil.like("taskname", keyWord);// 事项名称
            }
        }

        sqlConditionUtil.eq("ISTEMPLATE", "0"); // 非模板事项
        sqlConditionUtil.eq("IS_EDITAFTERIMPORT", "1");// 审核通过后事项
        sqlConditionUtil.eq("IS_ENABLE", "1");// 启用事项
        sqlConditionUtil.isBlankOrValue("IS_HISTORY", "0"); // 判断是否为历史版本
        sqlConditionUtil.in("ifnull(iswtshow,'')", "'','1'");// 判断在网厅展示
        int firstResultTask = Integer.parseInt(currentPageTask) * Integer.parseInt(pageSizeTask);
        //  3.1.2、主项事项结果返回
        PageData<AuditTask> fatherTaskPageData = null;
        // 3.1.2.2、获取大项事项（区分大小项）
        // 3.1.2.3、属于乡镇事项
        if (isXz) {
            fatherTaskPageData = iJnAppRestService.getXZGBAuditTaskPageData(sqlConditionUtil.getMap(), areaCode,
                    firstResultTask, Integer.parseInt(pageSizeTask), "ordernum", "desc").getResult();
        }
        // 3.1.2.4、不属于乡镇事项的主项列表
        else {
            fatherTaskPageData = iJnAppRestService.getNewAuditTaskPageData(sqlConditionUtil.getMap(), firstResultTask,
                    Integer.parseInt(pageSizeTask), "ordernum", "desc").getResult();
        }

        int totalTaskCount = 0;
        List<AuditTask> fatherAuditTaskList = new ArrayList<>();
        if (fatherTaskPageData != null) {
            totalTaskCount = fatherTaskPageData.getRowCount();// 查询事项的数量
            fatherAuditTaskList = fatherTaskPageData.getList();// 查询事项的信息
        }
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
                //3.1.3.3、子项条件中先去除区域筛选，优先获取到主项下的所有子项， 然后找去子项下放到该区域的数据
                Map<String, String> conditionMap = sqlConditionUtil.getMap();
                if (conditionMap.containsKey("d.areacode" + ZwfwConstant.ZWFW_SPLIT + ZwfwConstant.SQL_OPERATOR_EQ
                        + ZwfwConstant.ZWFW_SPLIT + "S")) {
                    conditionMap.remove("d.areacode" + ZwfwConstant.ZWFW_SPLIT + ZwfwConstant.SQL_OPERATOR_EQ
                            + ZwfwConstant.ZWFW_SPLIT + "S");
                }
            }
            // 3.1.3.4、获取子项事项
            Map<String, String> sqlmap = sqlConditionUtil.getMap();
            SqlConditionUtil sqlConditionUtil1 = new SqlConditionUtil();
            sqlConditionUtil1.eq("length(item_id)", "33");
            sqlConditionUtil1.leftLike("item_id", fatherAuditTask.getItem_id());
            sqlConditionUtil1.nq("item_id", fatherAuditTask.getItem_id());
            sqlmap = sqlConditionUtil.getMap();
            sqlmap.putAll(sqlConditionUtil1.getMap());
            sqlmap.remove("isznb#zwfw#eq#zwfw#S");
            PageData<AuditTask> childTaskPageData = iAuditTask.getAuditTaskPageData(sqlmap, 0, 0, "ordernum", "desc").getResult();
            List<JSONObject> childTaskJsonList = new ArrayList<JSONObject>();
            List<AuditTask> childAuditTasks = null;
            if (childTaskPageData != null) {
                childAuditTasks = childTaskPageData.getList();
            }
            if (childAuditTasks != null && childAuditTasks.size() > 0) {
                for (AuditTask childAuditTask : childTaskPageData.getList()) {
                    // 如果是乡镇的，判断该子项是否下放到当前
                    if (isXz) {
                        AuditTaskDelegate auditTaskDelegate = iAuditTaskDelegate
                                .findByTaskIDAndAreacode(childAuditTask.getTask_id(), areaCode).getResult();
                        // 如果没有下放，不显示
                        if (auditTaskDelegate == null) {
                            continue;
                        }
                    }
                    JSONObject childTaskJson = commonTaskJson(childAuditTask, auditOnlineRegister, "1");
                    childTaskJsonList.add(childTaskJson);
                }
                fatherTaskJson.put("haschildtask", "1");
            }
            fatherTaskJson.put("childtasklist", childTaskJsonList);
            fatherTaskJson.put("childtaskcount", childTaskJsonList.size());

            fatherTaskJsonList.add(fatherTaskJson);
        }
        dataJson.put("tasklist", fatherTaskJsonList);
        dataJson.put("totalcount", totalTaskCount);
        return dataJson;
    }

    /**
     * 生成文件
     *
     * @return
     */
    public void createFile(String filePath, String content) {
        FileManagerUtil.deleteFile(filePath);
        // 生成新文件
        if (!FileManagerUtil.isExist(filePath, true)) {
            log.info("===============文件删除成功，开始创建文件，文件名为：" + filePath + "===============");
            // 将内容写入文件
            FileManagerUtil.writeContentToFileByWriter(content.toString(), filePath);
            log.info("===============文件创建成功，文件名为：" + filePath + "===============");
        }
    }

    // 判断事项是否允许预约
    public boolean isAppoint(String taskId) {
        boolean isAppointment = false;
        // 判断事项是否分配到相关排队分类
        List<String> taskTypeGuidList = iAuditQueueTasktypeTask.getTaskTypeguidbyTaskID(taskId).getResult();
        if (taskTypeGuidList != null && taskTypeGuidList.size() > 0) {
            // 获取该事项下的所有中心
            for (String taskTypeGuid : taskTypeGuidList) {
                AuditQueueTasktype tasktype = iAuditQueueTasktype.getTasktypeByguid(taskTypeGuid).getResult();
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
        return isAppointment;
    }

}
