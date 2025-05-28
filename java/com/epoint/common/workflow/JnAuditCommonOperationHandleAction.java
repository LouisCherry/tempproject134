package com.epoint.common.workflow;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.delegate.inter.IAuditTaskDelegateAuth;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.delegate.domain.AuditTaskDelegate;
import com.epoint.basic.audittask.delegate.inter.IAuditTaskDelegate;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.workflow.inter.IAuditTaskRiskpoint;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditorga.handleframeou.IHandleFrameOU;
import com.epoint.composite.auditresource.handledoc.inter.IHandleDoc;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.message.entity.MessagesCenter;
import com.epoint.frame.service.metadata.opinion.api.IOpinionService;
import com.epoint.frame.service.metadata.opinion.entity.FrameOpinion;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.workflow.service.common.custom.ProcessVersionInstance;
import com.epoint.workflow.service.common.entity.config.WorkflowTransition;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import com.epoint.workflow.service.config.api.IWorkflowActivityService;
import com.epoint.workflow.service.core.api.IWFDefineAPI9;
import com.epoint.workflow.service.core.api.IWFInitPageAPI9;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 通过类型通用流程处理页面的后台
 *
 * @author shengjia
 * @version [版本号, 2016年4月22日]
 */
@RestController("jnauditcommonoperationhandleaction")
@Scope("request")
public class JnAuditCommonOperationHandleAction extends BaseController {
    public static final String lclcurl = ConfigUtil.getConfigValue("epointframe", "lclcurl");

    public static final String lcjbxxurl = ConfigUtil.getConfigValue("epointframe", "lcjbxxurl");

    private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Autowired
    private IOpinionService frameOpinionService;
    @Autowired
    private IWFInitPageAPI9 oWAPI;

    @Autowired
    private IWFInstanceAPI9 oWInstance;

    // 组合服务
    @Autowired
    private IHandleProject handleProjectService;

    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private IHandleDoc docService;

    @Autowired
    private IAuditTaskExtension auditTaskExtensionService;

    @Autowired
    private IHandleConfig handleConfig;
    public StringBuffer strbuf = new StringBuffer();
    @Autowired
    private IAuditTaskDelegateAuth auditTaskDelegateAuthService;

    @Autowired
    private IAuditTaskDelegate auditTaskDelegateService;

    @Autowired
    private IAuditTaskRiskpoint auditTaskRiskpointService;

    @Autowired
    private IWFInstanceAPI9 wfInstance;

    @Autowired
    private IAuditProject iAuditProject;

    @Autowired
    private IWorkflowActivityService iWorkflowActivityService;

    @Autowired
    private IWFDefineAPI9 iWFDefineAPI9;

    @Autowired
    private IHandleFrameOU frameOu;

    @Autowired
    private IAuditTask iAuditTask;

    @Autowired
    private IMessagesCenterService messageCenterService;

    private WorkflowWorkItem dataBean;

    private String fileUpload;

    public String getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(String fileUpload) {
        this.fileUpload = fileUpload;
    }

    /**
     * 附件上传model
     */
    private FileUploadModel9 attachUploadModel;

    @Override
    public void pageLoad() {
        String pviguid = getRequestParameter("processVersionInstanceGuid");
        String workItemGuid = getRequestParameter("workItemGuid");
        if (!isPostback()) {
            ProcessVersionInstance pvi = wfInstance.getProcessVersionInstance(pviguid);
            dataBean = oWInstance.getWorkItem(pvi, workItemGuid);
            addViewData("fileUpload", dataBean.getActivityInstanceGuid());
        }
    }

    /**
     * 初始化所有操作处理页面
     *
     * @return
     */
    public String initPage() {
        addViewData("workitemguid", getRequestParameter("workItemGuid"));
        addViewData("pviGuid", getRequestParameter("processVersionInstanceGuid"));
        JSONObject jsonobject = new JSONObject();
        try {
            jsonobject.put("workitemguid", getRequestParameter("workItemGuid"));
            jsonobject.put("operationguid", getRequestParameter("operationGuid"));
            jsonobject.put("transitionguid", getRequestParameter("transitionGuid"));
            jsonobject.put("pviguid", getRequestParameter("processVersionInstanceGuid"));
            jsonobject.put("userguid", userSession.getUserGuid());
            jsonobject.put("baseouguid", userSession.getBaseOUGuid());
            jsonobject.put("isshowopinion", true);
            jsonobject.put("isshowopiniontemplete", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String str = oWAPI.init_getOperationHandlePageInfo(jsonobject.toString());
        JSONObject rtnjson = JSONObject.parseObject(str);
        addViewData("fileUpload", rtnjson.get("activityinstanceguid").toString());
        return rtnjson.toString();
    }

    public FileUploadModel9 getFileUploadModel() {
        if (attachUploadModel == null) {

            AttachHandler9 handler = new AttachHandler9() {

                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public void afterSaveAttachToDB(Object attach) {
                    attachUploadModel.getExtraDatas().put("msg", "上传成功");

                }

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage attachStorage) {

                    return true;
                }

            };
            attachUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(getViewData("fileUpload"), null,
                    null, handler, userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return attachUploadModel;
    }

    /**
     * 把json传返回给handlemain页面
     *
     * @param jsonString
     * @throws JSONException
     * @throws InterruptedException
     */
    public void btnsubmit(String jsonString, String projectGuid) throws JSONException, InterruptedException {
        JSONObject jsonobject = new JSONObject();
        try {
            jsonobject = JSON.parseObject(jsonString);
            jsonobject.put("userguid", userSession.getUserGuid());
            jsonobject.put("pviguid", getRequestParameter("processVersionInstanceGuid"));
            // 获取文书地址并返回
            AuditProject auditProject = auditProjectService
                    .getAuditProjectByRowGuid(projectGuid, null)
                    .getResult();
            // 如果存在待办事宜，则删除待办
            List<MessagesCenter> messagesCenterList = new ArrayList<MessagesCenter>();
            List<MessagesCenter> messagesList = new ArrayList<MessagesCenter>();
            if (auditProject != null && auditProject.getStatus() == ZwfwConstant.BANJIAN_STATUS_YJJ) {
                messagesList = messageCenterService.getWaitHandleListByPviguid(auditProject.getRowguid());
                if (messagesList != null && !messagesList.isEmpty()) {
                    messagesCenterList.addAll(messagesList);
                }
                if (!messagesCenterList.isEmpty()) {
                    for (MessagesCenter messagescenter : messagesCenterList) {
                        messageCenterService.deleteMessage(messagescenter.getMessageItemGuid(),
                                messagescenter.getTargetUser());
                    }
                }
            }
            if (auditProject != null) {
                if (auditProject.getStatus() == ZwfwConstant.BANJIAN_STATUS_BYSL) {
                    addCallbackParam("close", "1");
                    addCallbackParam("message", jsonobject.toString());
                    return;
                }

                AuditTaskExtension auditTaskExtension = auditTaskExtensionService
                        .getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true).getResult();
                if (auditTaskExtension != null && auditProject.getStatus() == 26) {
                    JSONObject submit = new JSONObject();
                    JSONObject params = new JSONObject();

                    params.put("projectguid", auditProject.getRowguid());
                    params.put("areacode", auditProject.getAreacode());
                    params.put("acceptusername", StringUtil.isNotBlank(auditProject.getAcceptusername()) ? auditProject.getAcceptusername() : "一窗系统");
                    submit.put("params", params);

                    String resultsign = "";

                    JSONObject submit2 = new JSONObject();

                    submit2.put("projectGuid", auditProject.getRowguid());
                    submit2.put("areaCode", auditProject.getAreacode());
                    params.put("acceptusername", StringUtil.isNotBlank(auditProject.getAcceptusername()) ? auditProject.getAcceptusername() : "一窗系统");
                    submit2.put("status", "01");

                    String resultsign2 = "";
                    /*if ("1".equals(auditTaskExtension.getStr("is_jianguanlc"))) {
                        resultsign = TARequestUtil.sendPostInner(lcjbxxurl, submit.toJSONString(), "", "");
                        Thread.sleep(2000);
                        resultsign2 = TARequestUtil.sendPostInner(lclcurl, submit2.toJSONString(), "", "");
                        log.info("推送浪潮办件结果：" + resultsign + ";办件编号为：" + auditProject.getFlowsn());
                        log.info("推送浪潮受理流程结果：" + resultsign2 + ";办件编号为：" + auditProject.getFlowsn());
                    }*/

                    String asdocword = handleConfig.getFrameConfig("AS_DOC_WORD", auditProject.getCenterguid()).getResult();
                    boolean isword = ZwfwConstant.CONSTANT_STR_ZERO.equals(asdocword) ? false : true;

                    String address = docService.getDocEditPage(auditProject.getTaskguid(), auditProject.getCenterguid(),
                            auditTaskExtension.getIs_notopendoc(), String.valueOf(ZwfwConstant.DOC_TYPE_SLTZS), false,
                            isword).getResult();
                    if (StringUtil.isNotBlank(address)) {
                        address += "&ProjectGuid=" + auditProject.getRowguid() + "&ProcessVersionInstanceGuid="
                                + auditProject.getPviguid() + "&taskguid=" + auditProject.getTaskguid();
                    }
                    addCallbackParam("address", address.toString());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        addCallbackParam("message", jsonobject.toString());

    }

    /**
     * 退回、终止流程等页面添加个人模板
     *
     * @param txtOpinion 个人意见 受理操作
     */
    public void handleShouli(String projectGuid) {
        String fields = " rowguid,taskguid,projectname,applyeruserguid,applyername,areacode,pviguid,status,applyway,tasktype,ouname,ouguid,centerguid,hebingshoulishuliang,remark,windowguid ";
        AuditProject project = auditProjectService
                .getAuditProjectByRowGuid(fields, projectGuid, ZwfwUserSession.getInstance().getAreaCode().toString())
                .getResult();
        handleProjectService.handleAccept(project, getRequestParameter("workItemGuid"),
                UserSession.getInstance().getDisplayName(), UserSession.getInstance().getUserGuid(),
                ZwfwUserSession.getInstance().getWindowName(), ZwfwUserSession.getInstance().getWindowGuid());
    }

    /**
     * 退回、终止流程等页面添加个人模板
     *
     * @param txtOpinion 个人意见
     */
    public void addintoopinion(String txtOpinion) {
        if (StringUtil.isNotBlank(txtOpinion)) {
            String strScript = "";
            if (txtOpinion.indexOf("'") >= 0 || txtOpinion.indexOf("\"") >= 0) {
                strScript = "意见名称有非法字符，请重新填写";
            }
            // 判断名称长度是否合法
            else if (txtOpinion.length() > 50) {
                strScript = "意见名称太长，请小于五十字";
            }
            /*
             * // 判断是否重复 else if (frameOpinionService.) { strScript = "该名称已经存在";
             * }
             */
            else {
                String opinionGuid = UUID.randomUUID().toString();
                FrameOpinion opinion = new FrameOpinion();
                opinion.setOpinionGuid(opinionGuid);
                opinion.setOpinionText(txtOpinion);
                opinion.setOrderNumber(0);
                opinion.setUserGuid(userSession.getUserGuid());
                opinion.setBaseOuGuid("");
                frameOpinionService.insertOpinion(opinion);
                strScript = "添加成功";
            }
            addCallbackParam("message", l(strScript));
        }
    }

    /**
     * 初始化getalluser页面
     *
     * @param workitemguid 工作项guid
     * @param stepguid     变迁guid或者活动guid
     */
    public String getStepInfo(String workitemguid, String stepguid, String pviGuid, String taskid, String taskguid) {
        JSONObject jsonobject = new JSONObject();
        JSONObject json = new JSONObject();
        try {
            jsonobject.put("workitemguid", getRequestParameter("workitemguid"));
            jsonobject.put("pviguid", getRequestParameter("pviGuid"));
            jsonobject.put("stepguid", getRequestParameter("stepguid"));
            jsonobject.put("userguid", userSession.getUserGuid());
            json = JSONObject.parseObject(oWAPI.init_getStepInfo(jsonobject.toString()).toString());
            // 根据相关参数获取办件guid以及事项guid
            ProcessVersionInstance pvi = wfInstance.getProcessVersionInstance(pviGuid);
            String context = wfInstance.getContextItemValue(pvi, "ProjectGuid");
            int index = context.indexOf("&TaskGuid=");
            String projectguid = "";
            if (index > 0) {
                projectguid = context.substring(0, index);
                taskguid = context.substring(index + 10, context.length());
            } else {
                projectguid = context;
            }
            AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskguid, true).getResult();
            String area = "";
            if (auditTask != null) {
                area = auditTask.getAreacode();
            }
            AuditProject auditProject = iAuditProject
                    .getAuditProjectByRowGuid("acceptareacode,task_id", projectguid, area).getResult();
            if ("undefined".equals(taskid) && auditProject != null) {
                taskid = auditProject.getTask_id();
            }

            ProcessVersionInstance pvinstance = wfInstance.getProcessVersionInstance(pviGuid);
            WorkflowTransition transition = iWFDefineAPI9.getTransition(pvinstance, stepguid);
            // 判断当前活动是否是结束之前的一个活动
            boolean bool = iWorkflowActivityService.isTheEndActivity(transition.getProcessVersionGuid(),
                    transition.getToActivityGuid());

            JSONArray handle = new JSONArray();
            JSONObject tab = new JSONObject();
            // 如果是最后一个活动
            if (bool) {
                // ACCEPTAREACODE不为空，并且为某个可用的乡镇编码，则认为是乡镇受理的,对应的乡镇都能够进行办结
                if (auditProject != null && StringUtil.isNotBlank(auditProject.getAcceptareacode())) {
                    List<FrameOu> list = frameOu.getOUListByAreacode(auditProject.getAcceptareacode()).getResult();
                    if (list != null && list.size() > 0) {
                        if (auditProject.getAcceptareacode().length() == 9) {
                            tab.put("name", "乡镇");
                            tab.put("type", "townou");
                        } else {
                            tab.put("name", "村（社区）");
                            tab.put("type", "cunou");
                        }
                        tab.put("area", auditProject.getAcceptareacode());
                        handle.add(tab);
                        tab = new JSONObject();
                        tab.put("name", "市级部门");
                        tab.put("type", "cityou");
                        tab.put("taskid", taskid);
                        tab = new JSONObject();
                        tab.put("name", "所有用户");
                        tab.put("type", "all");
                        handle.add(tab);
                    }
                } else {
                    tab = new JSONObject();
                    tab.put("name", "本部门");
                    tab.put("type", "ownou");
                    handle.add(tab);
                    tab = new JSONObject();
                    tab.put("name", "市级部门");
                    tab.put("type", "cityou");
                    tab = new JSONObject();
                    tab.put("name", "所有用户");
                    tab.put("type", "all");
                    handle.add(tab);
                }
                json.put("handlertree", handle);
            } else if (ZwfwUserSession.getInstance().getCitylevel() != null
                    && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                    .parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
                // 如果是镇村接件
                String areacode = ZwfwUserSession.getInstance().getAreaCode();
                AuditTaskDelegate auditTaskDelegate = auditTaskDelegateService.findByTaskIDAndAreacode(taskid, areacode)
                        .getResult();

                if (auditTaskDelegate != null) {
                    // 乡镇法定事项
                    if (ZwfwConstant.TASKDELEGATE_TYPE_XZFD.equals(auditTaskDelegate.getDelegatetype())) {
                        handle = new JSONArray();
                        tab = new JSONObject();
                        tab.put("name", "乡镇");
                        tab.put("type", "townou");
                        handle.add(tab);
                        tab = new JSONObject();
                        tab.put("name", "市级部门");
                        tab.put("type", "cityou");
                        tab = new JSONObject();
                        tab.put("name", "所有用户");
                        tab.put("type", "all");
                        handle.add(tab);
                        json.put("handlertree", handle);
                    } else if (ZwfwConstant.TASKDELEGATE_TYPE_CJFD.equals(auditTaskDelegate.getDelegatetype())) {
                        handle = new JSONArray();
                        tab = new JSONObject();
                        tab.put("name", "村（社区）");
                        tab.put("type", "cunou");
                        handle.add(tab);
                        tab = new JSONObject();
                        tab.put("name", "市级部门");
                        tab.put("type", "cityou");
                        tab = new JSONObject();
                        tab.put("name", "所有用户");
                        tab.put("type", "all");
                        handle.add(tab);
                        json.put("handlertree", handle);
                    } else if (areacode.length() == 9) {
                        String curAcctivityGuid = transition.getToActivityGuid();
                        List<String> riskPoints = auditTaskDelegateAuthService
                                .getRpIdByTaskIdAndOuguid(taskid, auditTaskDelegate.getOuguid()).getResult();
                        if (riskPoints != null && riskPoints.size() > 0) {
                            for (String rpid : riskPoints) {
                                Record record = auditTaskRiskpointService.getNameByTaskGuidAndRpId(taskguid, rpid)
                                        .getResult();
                                String activityguid = "";
                                if (record != null) {
                                    activityguid = record.get("activityguid");
                                }
                                if (curAcctivityGuid.equals(activityguid)) {
                                    handle = new JSONArray();
                                    tab = new JSONObject();
                                    tab.put("name", "乡镇");
                                    tab.put("type", "townou");
                                    handle.add(tab);
                                    tab = new JSONObject();
                                    tab.put("name", "市级部门");
                                    tab.put("type", "cityou");
                                    tab = new JSONObject();
                                    tab.put("name", "所有用户");
                                    tab.put("type", "all");
                                    handle.add(tab);
                                    break;
                                } else {
                                    handle = new JSONArray();
                                    tab = new JSONObject();
                                    tab.put("name", "市级部门");
                                    tab.put("type", "cityou");
                                    tab = new JSONObject();
                                    tab.put("name", "所有用户");
                                    tab.put("type", "all");
                                    handle.add(tab);
                                }
                            }
                            json.put("handlertree", handle);
                        } else {
                            handle = new JSONArray();
                            tab = new JSONObject();
                            tab.put("name", "市级部门");
                            tab.put("type", "cityou");
                            tab = new JSONObject();
                            tab.put("name", "所有用户");
                            tab.put("type", "all");
                            handle.add(tab);
                            json.put("handlertree", handle);
                        }
                    } else {
                        String curAcctivityGuid = transition.getToActivityGuid();
                        List<String> riskPoints = auditTaskDelegateAuthService
                                .getRpIdByTaskIdAndOuguid(taskid, auditTaskDelegate.getOuguid()).getResult();
                        if (riskPoints != null && riskPoints.size() > 0) {
                            for (String rpid : riskPoints) {
                                Record record = auditTaskRiskpointService.getNameByTaskGuidAndRpId(taskguid, rpid)
                                        .getResult();
                                String activityguid = "";
                                if (record != null) {
                                    activityguid = record.get("activityguid");
                                }
                                if (curAcctivityGuid.equals(activityguid)) {
                                    handle = new JSONArray();
                                    tab = new JSONObject();
                                    tab.put("name", "村（社区）");
                                    tab.put("type", "cunou");
                                    handle.add(tab);
                                    AuditTaskDelegate auditDelegate = auditTaskDelegateService
                                            .findByTaskIDAndAreacode(taskid, areacode.substring(0, 9)).getResult();
                                    if (auditDelegate != null) {
                                        List<String> risks = auditTaskDelegateAuthService
                                                .getRpIdByTaskIdAndOuguid(taskid, auditDelegate.getOuguid())
                                                .getResult();
                                        if (risks != null && risks.size() > 0) {
                                            for (String pid : risks) {
                                                Record xzrecord = auditTaskRiskpointService
                                                        .getNameByTaskGuidAndRpId(taskguid, pid).getResult();
                                                String xzactivityguid = "";
                                                if (record != null) {
                                                    xzactivityguid = xzrecord.get("activityguid");
                                                }
                                                if (curAcctivityGuid.equals(xzactivityguid)) {
                                                    tab = new JSONObject();
                                                    tab.put("name", "乡镇");
                                                    tab.put("type", "townou");
                                                    handle.add(tab);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    tab = new JSONObject();
                                    tab.put("name", "市级部门");
                                    tab.put("type", "cityou");
                                    tab.put("taskid", taskid);
                                    tab = new JSONObject();
                                    tab.put("name", "所有用户");
                                    tab.put("type", "all");
                                    handle.add(tab);
                                    break;
                                } else {
                                    //TODO
                                    handle = new JSONArray();
                                    AuditTaskDelegate auditDelegate = auditTaskDelegateService
                                            .findByTaskIDAndAreacode(taskid, areacode.substring(0, 9)).getResult();
                                    if (auditDelegate != null) {
                                        List<String> risks = auditTaskDelegateAuthService
                                                .getRpIdByTaskIdAndOuguid(taskid, auditDelegate.getOuguid())
                                                .getResult();
                                        if (risks != null && risks.size() > 0) {
                                            for (String pid : risks) {
                                                Record xzrecord = auditTaskRiskpointService
                                                        .getNameByTaskGuidAndRpId(taskguid, pid).getResult();
                                                String xzactivityguid = "";
                                                if (record != null) {
                                                    xzactivityguid = xzrecord.get("activityguid");
                                                }
                                                if (curAcctivityGuid.equals(xzactivityguid)) {
                                                    tab = new JSONObject();
                                                    tab.put("name", "乡镇");
                                                    tab.put("type", "townou");
                                                    handle.add(tab);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    tab = new JSONObject();
                                    tab.put("name", "市级部门");
                                    tab.put("type", "cityou");
                                    tab.put("taskid", taskid);
                                    tab = new JSONObject();
                                    tab.put("name", "所有用户");
                                    tab.put("type", "all");
                                    handle.add(tab);
                                }
                            }
                            json.put("handlertree", handle);
                        } else {
                            handle = new JSONArray();
                            tab = new JSONObject();
                            tab.put("name", "市级部门");
                            tab.put("type", "cityou");
                            tab.put("taskid", taskid);
                            tab = new JSONObject();
                            tab.put("name", "所有用户");
                            tab.put("type", "all");
                            handle.add(tab);
                            json.put("handlertree", handle);
                        }

                    }
                } else {
                    handle = new JSONArray();
                    tab = new JSONObject();
                    tab.put("name", "市级部门");
                    tab.put("type", "cityou");
                    tab.put("taskid", taskid);
                    tab = new JSONObject();
                    tab.put("name", "所有用户");
                    tab.put("type", "all");
                    handle.add(tab);
                    json.put("handlertree", handle);
                }
            } else {

                handle = new JSONArray();
                tab = new JSONObject();
                tab.put("name", "本部门");
                tab.put("type", "ownou");
                handle.add(tab);
                tab = new JSONObject();
                tab.put("name", "市级部门");
                tab.put("type", "cityou");
                tab.put("taskid", taskid);
                tab = new JSONObject();
                tab.put("name", "所有用户");
                tab.put("type", "all");
                handle.add(tab);
                json.put("handlertree", handle);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

}
