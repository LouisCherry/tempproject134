package com.epoint.auditproject.auditproject.action;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.epoint.auditprojectunusual.api.IJNAuditProjectUnusual;
import com.epoint.auditprojectunusual.utils.AuditProjectUnusualUtils;
import com.epoint.basic.auditorga.auditworkingday.inter.IAuditOrgaWorkingDay;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.basic.auditonlineuser.auditonlineindividual.domain.AuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineindividual.inter.IAuditOnlineIndividual;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;
import com.epoint.basic.auditproject.auditprojectunusual.inter.IAuditProjectUnusual;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyBaseinfo;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.common.rabbitmq.ProducerMQ;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.common.zwfw.workflow.AuditWorkflowBizlogic;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.message.entity.MessagesCenter;
import com.epoint.frame.service.message.remind.api.IMessagesMessageService;
import com.epoint.frame.service.message.remind.entity.MessagesMessage;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.workflow.service.common.custom.ProcessVersionInstance;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;

@RestController("auditprojectterminateaction")
@Scope("request")
public class AuditProjectTerminateAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = -7964796173554880741L;

    /**
     * 办件标识
     */
    private String projectguid;
    /**
     * 工作项标识
     */
    private String workitemguid;
    /**
     * 办件信息
     */
    private AuditProject auditProject;

    /**
     * 事项信息
     */
    private AuditTask auditTask;

    /**
     * 事项扩展信息
     */
    private AuditTaskExtension auditTaskExtension;

    /**
     * 终止原因
     */
    private String reason;
    /**
     * 操作类型
     */
    private String operateType;
    /**
     * 事项标识
     */
    private String taskGuid;

    @Autowired
    IAuditProject auditProjectService;

    @Autowired
    IAuditTask auditTaskService;

    /**
     * 事项扩展信息API
     */
    @Autowired
    private IAuditTaskExtension iAuditTaskExtension;

    /**
     * 办件异常表API
     */
    @Autowired
    private IAuditProjectUnusual iAuditProjectUnusual;

    @Autowired
    IHandleProject handleProjectService;

    @Autowired
    ISendMQMessage sendMQMessageService;

    @Autowired
    private IWFInstanceAPI9 wfinstance;

    @Autowired
    private IMessagesCenterService messageCenterService;

    @Autowired
    private IConfigService iconfigService;

    @Autowired
    private IMessagesCenterService messagesCenterService;

    /**
     * 消息提醒
     */
    @Autowired
    private IMessagesMessageService iMessagesMessageService;

    private AuditWorkflowBizlogic auditWorkflowBizlogic = new AuditWorkflowBizlogic();

    @Autowired
    private IAuditOnlineIndividual auditOnlieIndividualService;
    @Autowired
    IAuditRsCompanyBaseinfo auditRsCompanyBaseinfoService;

    @Autowired
    private IJNAuditProjectUnusual ijnAuditProjectUnusual;

    /**
     * 上传附件
     */
    private FileUploadModel9 fileUploadModel;
    private String cliengGuid = "";

    @Override
    public void pageLoad() {
        workitemguid = getRequestParameter("workItemGuid");
        // 简易流程下workitemguid未定义
        if ("undefined".equals(workitemguid)) {
            workitemguid = null;
        }
        projectguid = getRequestParameter("projectguid");
        taskGuid = getRequestParameter("taskGuid");
        auditTask = auditTaskService.getAuditTaskByGuid(taskGuid, true).getResult();
        String fields = " biguid,acceptuserguid,rowguid,taskguid,projectname,applyeruserguid,applyername,areacode,pviguid,status,applyway,promise_day,handleareacode,currentareacode,applyertype,task_id,subappguid,businessguid ";
        auditProject = auditProjectService.getAuditProjectByRowGuid(fields, projectguid, auditTask.getAreacode())
                .getResult();
        operateType = getRequestParameter("operateType");

        // *************开始*************
        // add by yrchan,2022-04-19,勘验事项
        auditTaskExtension = iAuditTaskExtension.getTaskExtensionByTaskGuid(taskGuid, false).getResult();
        if (auditTaskExtension != null
                && ZwfwConstant.CONSTANT_STR_ONE.equals(auditTaskExtension.getStr("is_inquest"))) {
            addCallbackParam("fileup", ZwfwConstant.CONSTANT_STR_ONE);
        }
        // 是勘验事项
        if (!isPostback()) {
            // 第一次加载页面随机产生，并放入viewdata
            cliengGuid = UUID.randomUUID().toString();
            addViewData("cliengGuid", cliengGuid);
        }
        else {
            // 非第一次从viewdata中获取
            cliengGuid = getViewData("cliengGuid");
        }
        // *************结束*************

    }

    /**
     * 
     * add by yrchan,2022-04-19,[附件上传]
     * 
     * @return
     */
    public FileUploadModel9 getFileUploadModel() {
        if (fileUploadModel == null) {
            if (StringUtil.isNotBlank(getViewData("cliengGuid"))) {
                cliengGuid = getViewData("cliengGuid");
            }
            else {
                cliengGuid = UUID.randomUUID().toString();
                addViewData("cliengGuid", cliengGuid);
            }
            fileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(cliengGuid,
                    userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fileUploadModel;
    }

    /**
     * 
     * 处理办件
     * 
     */
    public void add() {
        // 保存处理过办件的区域编码(不重复保存)
        String handleAreaCode = ZwfwUserSession.getInstance().getAreaCode();
        String areaStr = "";
        if (StringUtil.isBlank(auditProject.getHandleareacode())) {
            areaStr = handleAreaCode + ",";
        }
        else if ((auditProject.getHandleareacode().indexOf(handleAreaCode + ",") < 0)) {
            areaStr = auditProject.getHandleareacode() + handleAreaCode + ",";
        }
        if (StringUtil.isNotBlank(areaStr)) {
            auditProject.setHandleareacode(areaStr);
        }

        String unusualGuid = "";
        if (ZwfwConstant.SHENPIOPERATE_TYPE_CX.equals(operateType)) {
            // 撤销申请
            unusualGuid = handleProjectService.handleRevoke(auditProject, userSession.getDisplayName(),
                    userSession.getUserGuid(), workitemguid, reason).getResult();
            // *************开始*************
            // add by yrchan,2022-04-19,勘验事项
            if (auditTaskExtension != null
                    && ZwfwConstant.CONSTANT_STR_ONE.equals(auditTaskExtension.getStr("is_inquest"))
                    && StringUtil.isNotBlank(cliengGuid)) {
                // 是勘验事项
                AuditProjectUnusual auditProjectUnusual = iAuditProjectUnusual
                        .getAuditProjectUnusualByRowguid(unusualGuid).getResult();
                if (auditProjectUnusual != null) {
                    auditProjectUnusual.set("cliengGuid", cliengGuid);
                    iAuditProjectUnusual.updateProjectUnusual(auditProjectUnusual);
                }
                // 给申请人发送消息提醒，消息内容“XX申请人，你好，您申请的【办件名称】因XXX（撤销申请）原因，已被撤销申请，请知悉！
                String content = auditProject.getApplyername() + "申请人，你好，您申请的【" + auditProject.getProjectname() + "】因"
                        + reason + "（撤销申请）原因，已被撤销申请，请知悉！";
                // 发送消息提醒
                MessagesMessage messagesMessage = new MessagesMessage();
                messagesMessage.setMessageGuid(UUID.randomUUID().toString());
                messagesMessage.setMessageContent(content);
                messagesMessage.setFromUserID(userSession.getUserGuid());
                messagesMessage.setFromUserName(userSession.getDisplayName());
                messagesMessage.setTargetUserID(auditProject.getApplyeruserguid());
                messagesMessage.setTargetUserName(auditProject.getApplyername());
                messagesMessage.setClientIdentifier(auditProject.getRowguid());
                messagesMessage.setSendTime(new Date());
                messagesMessage.setTitle("撤销申请");
                iMessagesMessageService.insertMessage(messagesMessage);
            }
            // *************结束*************

        }
        else if (ZwfwConstant.SHENPIOPERATE_TYPE_ZZ.equals(operateType)) {
            // 异常终止
            unusualGuid = handleProjectService.handleSuspension(auditProject, userSession.getDisplayName(),
                    userSession.getUserGuid(), workitemguid, reason).getResult();
            // *************开始*************
            // add by yrchan,2022-04-19,勘验事项
            if (auditTaskExtension != null
                    && ZwfwConstant.CONSTANT_STR_ONE.equals(auditTaskExtension.getStr("is_inquest"))
                    && StringUtil.isNotBlank(cliengGuid)) {
                // 是勘验事项
                AuditProjectUnusual auditProjectUnusual = iAuditProjectUnusual
                        .getAuditProjectUnusualByRowguid(unusualGuid).getResult();
                if (auditProjectUnusual != null) {
                    auditProjectUnusual.set("cliengGuid", cliengGuid);
                    iAuditProjectUnusual.updateProjectUnusual(auditProjectUnusual);
                }
                // 给申请人发送消息提醒，消息内容“XX申请人，你好，您申请的【办件名称】因XXX（异常终止）原因，已被异常终止，请知悉！
                String content = auditProject.getApplyername() + "申请人，你好，您申请的【" + auditProject.getProjectname() + "】因"
                        + reason + "（异常终止）原因，已被异常终止，请知悉！";
                // 发送消息提醒
                MessagesMessage messagesMessage = new MessagesMessage();
                messagesMessage.setMessageGuid(UUID.randomUUID().toString());
                messagesMessage.setMessageContent(content);
                messagesMessage.setFromUserID(userSession.getUserGuid());
                messagesMessage.setFromUserName(userSession.getDisplayName());
                messagesMessage.setTargetUserID(auditProject.getApplyeruserguid());
                messagesMessage.setTargetUserName(auditProject.getApplyername());
                messagesMessage.setClientIdentifier(auditProject.getRowguid());
                messagesMessage.setSendTime(new Date());
                messagesMessage.setTitle("异常终止");
                iMessagesMessageService.insertMessage(messagesMessage);
            }
            // *************结束*************
        }
        auditProject = auditProjectService.getAuditProjectByRowGuid("*", auditProject.getRowguid(), auditTask.getAreacode())
                .getResult();
        IAuditOrgaWorkingDay auditCenterWorkingDayService = ContainerFactory.getContainInfo()
                .getComponent(IAuditOrgaWorkingDay.class);
        //更新承诺办结时间
        if(auditTask!=null) {
            List<AuditProjectUnusual> auditProjectUnusuals = ijnAuditProjectUnusual.getZantingData(auditProject.getRowguid());
            Date acceptdat = auditProject.getAcceptuserdate();
            Date shouldEndDate;
            if (auditTask.getPromise_day() != null && auditTask.getPromise_day() > 0) {
                shouldEndDate = auditCenterWorkingDayService.getWorkingDayWithOfficeSet(
                        auditProject.getCenterguid(), acceptdat, auditTask.getPromise_day()).getResult();
                log.info("shouldEndDate:"+shouldEndDate);
            } else {
                shouldEndDate = null;
            }
            if(auditProjectUnusuals != null && auditProjectUnusuals.size() > 0) {
                AuditProjectUnusualUtils auditProjectUnusualUtils= new AuditProjectUnusualUtils();
                int totalWorkingDaysPaused  = auditProjectUnusualUtils.calculateTotalWorkingDaysPaused(auditProjectUnusuals,auditProject.getCenterguid());
                if (totalWorkingDaysPaused > 0 && shouldEndDate != null) {
                    // 重新计算包含暂停时间的预计结束日期
                    shouldEndDate = auditCenterWorkingDayService.getWorkingDayWithOfficeSet(
                            auditProject.getCenterguid(), shouldEndDate, (int) totalWorkingDaysPaused).getResult();
                    log.info("考虑暂停时间后的预计结束日期 shouldEndDate: " + shouldEndDate);
                }
                log.info("shouldEndDate:"+shouldEndDate);
            }
            if(shouldEndDate!=null && !"1753-1-1".equals(EpointDateUtil.convertDate2String(shouldEndDate))){
                auditProject.setPromiseenddate(shouldEndDate);
            }
        }
        auditProjectService.updateProject(auditProject);

        if (StringUtil.isNotBlank(unusualGuid)) {
            String msg = "handleSpecial:" + projectguid + ";" + auditProject.getApplyername() + ";" + unusualGuid
                    + "&handleSpecialResult:" + projectguid + ";" + unusualGuid;
            ProducerMQ.sendByExchange("receiveproject", msg, "");
            // 接办分离 特殊操作
            msg = projectguid + "." + auditProject.getApplyername() + "." + unusualGuid;
            sendMQMessageService.sendByExchange("exchange_handle", msg,
                    "project." + ZwfwUserSession.getInstance().getAreaCode() + ".special." + auditProject.getTask_id());
            // 接办分离 特殊操作结果
            msg = projectguid + "." + unusualGuid;
            sendMQMessageService.sendByExchange("exchange_handle", msg, "project."
                    + ZwfwUserSession.getInstance().getAreaCode() + ".specialresult." + auditProject.getTask_id());

            String isJSTY = ConfigUtil.getConfigValue("isJSTY");
            if (ZwfwConstant.CONSTANT_STR_ONE.equals(isJSTY)) {
                msg = "handleSpecial:" + projectguid + ";" + unusualGuid + ";" + auditProject.getAreacode()
                        + "/com.epoint.auditjob.rabbitmqhandle.MQHandleJiangSu";
                ProducerMQ.sendByExchange("receiveproject", msg, "");
            }
        }
        if (auditProject.getSubappguid() != null) {
            handleProjectService.handleblspsub(auditProject, UserSession.getInstance().getUserGuid(),
                    UserSession.getInstance().getDisplayName());
        }
        ProcessVersionInstance pvi = wfinstance.getProcessVersionInstance(auditProject.getPviguid());
        // 流程中止操作
        if (pvi != null) {
            auditWorkflowBizlogic.finish(workitemguid, reason, pvi, "");
        }
        // 删除自己发送的待办
        List<MessagesCenter> messagesCenterList = messageCenterService.queryForList(
                UserSession.getInstance().getUserGuid(), null, null, "", IMessagesCenterService.MESSAGETYPE_WAIT,
                auditProject.getRowguid(), "", -1, "", null, null, 0, -1);
        if (messagesCenterList != null && !messagesCenterList.isEmpty()) {
            for (MessagesCenter messagescenter : messagesCenterList) {
                messageCenterService.deleteMessage(messagescenter.getMessageItemGuid(), messagescenter.getTargetUser());
            }
        }

        String title = "【异常终止】" + auditProject.getProjectname();
        // 发送异常终止消息通知
        String applyerGuid = "";
        int type = auditProject.getApplyertype();
        String openUrl = iconfigService.getFrameConfigValue("zwdtMsgurl")
                + "/epointzwmhwz/pages/myspace/detail?projectguid=" + auditProject.getRowguid() + "&tabtype=9&taskguid="
                + auditProject.getTaskguid() + "&taskcaseguid=" + auditProject.getTaskcaseguid();

        // 个人
        if (type == 20) {
            AuditOnlineIndividual auditonlineindividual = auditOnlieIndividualService
                    .getIndividualByApplyerGuid(auditProject.getApplyeruserguid()).getResult();
            if (auditonlineindividual != null) {
                applyerGuid = auditonlineindividual.getAccountguid();
            }
        }
        // 法人
        else {
            applyerGuid = auditProject.getOnlineapplyerguid();
        }
        if (StringUtil.isNotBlank(applyerGuid)) {
            messagesCenterService.insertWaitHandleMessage(UUID.randomUUID().toString(), title,
                    IMessagesCenterService.MESSAGETYPE_WAIT, applyerGuid, "", UserSession.getInstance().getUserGuid(),
                    UserSession.getInstance().getDisplayName(), "", openUrl, UserSession.getInstance().getOuGuid(),
                    UserSession.getInstance().getBaseOUGuid(), 1, null, "", null, null, new Date(), null,
                    UserSession.getInstance().getUserGuid(), null, "");

        }
    };

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
