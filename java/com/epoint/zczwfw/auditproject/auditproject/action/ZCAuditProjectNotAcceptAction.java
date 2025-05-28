package com.epoint.zczwfw.auditproject.auditproject.action;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindowUser;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindowYjs;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectnotify.inter.IAuditProjectNotify;
import com.epoint.basic.auditproject.auditprojectoperation.domain.AuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectoperation.inter.IAuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;
import com.epoint.basic.auditproject.auditprojectunusual.inter.IAuditProjectUnusual;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.common.zwfw.workflow.AuditWorkflowBizlogic;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.message.api.IOnlineMessageInfoService;
import com.epoint.frame.service.message.entity.MessagesCenter;
import com.epoint.frame.service.message.remind.api.IMessagesMessageService;
import com.epoint.frame.service.message.remind.entity.MessagesMessage;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.tazwfw.electricity.rest.action.ElectricityController;
import com.epoint.workflow.service.common.custom.ProcessVersionInstance;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;

@RestController("zcauditprojectnotacceptaction")
@Scope("request")
public class ZCAuditProjectNotAcceptAction extends BaseController
{
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
     * 不予受理原因
     */
    private String reason;
    /**
     * 日志
     */
    transient Logger log = Logger.getLogger(ZCAuditProjectNotAcceptAction.class);

    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private IAuditTaskExtension auditTaskExtensionService;

    @Autowired
    private IAuditProjectUnusual projectUnusualService;

    @Autowired
    private IAuditProjectNotify projectNotifyService;

    @Autowired
    private IHandleProject handleProject;

    @Autowired
    private IUserService userService;

    @Autowired
    private IWFInstanceAPI9 wfinstance;

    @Autowired
    private IAuditSpInstance auditSpInstanceService;

    @Autowired
    private IOnlineMessageInfoService iOnlineMessageInfoService;

    private AuditWorkflowBizlogic auditWorkflowBizlogic = new AuditWorkflowBizlogic();

    @Autowired
    IAuditProjectOperation auditProjectOperationService;

    /**
     * 事项拓展信息
     */
    private AuditTaskExtension auditTaskExtension;

    private IAuditTask audittask;

    @Autowired
    private IMessagesCenterService messagesCenterService;

    /**
     * 消息提醒
     */
    @Autowired
    private IMessagesMessageService iMessagesMessageService;

    @Autowired
    private IAuditOrgaWindowYjs iAuditOrgaWindow;

    /**
     * 上传附件
     */
    private FileUploadModel9 fileUploadModel;
    private String cliengGuid = "";

    @Override
    public void pageLoad() {
        workitemguid = getRequestParameter("workItemGuid");
        projectguid = getRequestParameter("projectguid");
        // add by yrchan,2022-04-24,增加字段task_id
//        String fields = " biguid,acceptuserguid,rowguid,projectname,applyeruserguid,applyername,pviguid,hebingshoulishuliang,taskguid,remark,ouguid,windowguid,applyway,areacode,tasktype,centerguid,taskcaseguid,contactmobile,applydate,status,ouname,applyertype,is_lczj,flowsn,task_id  ";
        String area = "";
        // 如果是镇村接件
        if (ZwfwUserSession.getInstance().getCitylevel() != null
                && (Integer.parseInt(ZwfwUserSession.getInstance().getCitylevel()) > Integer
                        .parseInt(ZwfwConstant.AREA_TYPE_XQJ))) {
            area = ZwfwUserSession.getInstance().getBaseAreaCode();
        }
        else {
            area = ZwfwUserSession.getInstance().getAreaCode();
        }
        auditProject = auditProjectService.getAuditProjectByRowGuid("*", projectguid, area).getResult();
        auditTaskExtension = auditTaskExtensionService.getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true)
                .getResult();

        // *************开始*************
        // add by yrchan,2022-04-19,勘验事项
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
     * 确认
     * 
     */
    public void add() {
        if (auditProject.getStatus() >= ZwfwConstant.BANJIAN_STATUS_YSL) {
            addCallbackParam("error", "该步骤已经处理完成，请勿重复操作！");
        }
        /*
         * if (auditProject.getApplyway() != 20) { //不予受理调用接口推送给浪潮 Date date =
         * new Date(); //设置要获取到什么样的时间 SimpleDateFormat sdf = new
         * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); String createdate =
         * sdf.format(date); String url1 =
         * "http://59.206.96.202:8020/icity/c/api.itemState/setReturn"; try {
         * String result = java.net.URLEncoder.encode(reason, "UTF-8"); String
         * requestBodyParams = "{" + "\"tag\":" + "\"02\"," +
         * "\"pretreatmenttime\":" + "\"" + createdate + "\"," + "\"material\":"
         * + "\"\"," + "\"opinion\":" + "\"" + result + "\"," +
         * "\"receiveNum\":"+ "\"" + auditProject.getFlowsn() + "\"," +
         * "\"correctionTimes\":" + "\"\"," + "\"correctionMaterial\":" +
         * "\"\"}"; //system.out.println("推送浪潮不予受理：" + requestBodyParams); String
         * str1 = HttpUtils.postHttp(url1, requestBodyParams);
         * //system.out.println(str1); JSONObject responseJsonObj = new
         * JSONObject(); responseJsonObj = (JSONObject) JSONObject.parse(str1);
         * String state = responseJsonObj.getString("state"); if
         * (state.equals("1")) { //system.out.println("推送给浪潮不予受理成功"); } else {
         * String message1 = responseJsonObj.getString("message");
         * //system.out.println("推送给浪潮失败===》》》" + message1);
         * addCallbackParam("error", "办件状态修改失败"); return ; } } catch
         * (IOException e1) { e1.printStackTrace(); } }
         */
        // 1、插入异常操作信息
        String unusualGuid = projectUnusualService
                .insertProjectUnusual(userSession.getUserGuid(), userSession.getDisplayName(), auditProject,
                        Integer.parseInt(ZwfwConstant.BANJIANOPERATE_TYPE_BYSL), workitemguid, reason)
                .getResult();
        // *************开始*************
        // add by yrchan,2022-04-19,勘验事项
        if (auditTaskExtension != null
                && ZwfwConstant.CONSTANT_STR_ONE.equals(auditTaskExtension.getStr("is_inquest"))) {
            // 是勘验事项
            AuditProjectUnusual auditProjectUnusual = projectUnusualService.getAuditProjectUnusualByRowguid(unusualGuid)
                    .getResult();

            if (auditProjectUnusual != null && StringUtil.isNotBlank(getViewData("cliengGuid"))) {
                auditProjectUnusual.set("cliengGuid", getViewData("cliengGuid"));
                projectUnusualService.updateProjectUnusual(auditProjectUnusual);
            }
            // 给申请人发送消息提醒，消息内容“XX申请人，你好，您申请的【办件名称】因XXX（不予受理）原因，不予受理，请知悉！
            String content = auditProject.getApplyername() + "申请人，你好，您申请的【" + auditProject.getProjectname() + "】因"
                    + reason + "（不予受理）原因，不予受理，请知悉！";
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
            messagesMessage.setTitle("不予受理");
            iMessagesMessageService.insertMessage(messagesMessage);
        }
        // *************结束*************

        // 2、插入每日一填的记录;（修改：不予受理不插入记录）
        // projectNumberService.addProjectNumber(auditProject, false,
        // userSession.getUserGuid(),userSession.getDisplayName());
        // 3、插入在线通知
        String notirytitle = "【不予受理】" + "<" + auditProject.getProjectname() + ">";
        String handurl = ""; // TODO 处理页面地址还没有

        projectNotifyService.addProjectNotify(auditProject.getApplyeruserguid(), auditProject.getApplyername(),
                notirytitle, reason, handurl, ZwfwConstant.CLIENTTYPE_BJ, auditProject.getRowguid(),
                String.valueOf(ZwfwConstant.BANJIAN_STATUS_BYSL), userSession.getUserGuid(),
                userSession.getDisplayName());
        String message = handleProject
                .handleReject(auditProject, userSession.getDisplayName(), userSession.getUserGuid(),
                        ZwfwUserSession.getInstance().getWindowName(), ZwfwUserSession.getInstance().getWindowGuid())
                .getResult();

        String strWhere = "projectguid = '" + auditProject.getRowguid() + "' and  operatetype = '"
                + ZwfwConstant.OPERATE_BYSL + "'";
        String Filds = " rowguid,remarks ";
        // 添加不予受理备注
        AuditProjectOperation auditProjectOperation = auditProjectOperationService
                .getOperationFileldByProjectGuid(strWhere, Filds, "").getResult();
        if (auditProjectOperation != null) {
            auditProjectOperation.setRemarks(reason);
            auditProjectOperationService.updateAuditProjectOperation(auditProjectOperation);
        }
        // 不予受理短信
        if (StringUtil.isNotBlank(auditProject.getContactmobile())) {
            String targetUser = "";
            String targetDispName = "";
            // 由于增加了直接受理的功能，因此，接件人信息不存在，所以增加该字段为空的判断
            if (StringUtil.isNotBlank(auditProject.getAcceptusername())) {
                targetUser = auditProject.getAcceptuserguid();
                targetDispName = auditProject.getAcceptusername();
            }
            else {
                targetUser = userSession.getUserGuid();
                targetDispName = userService.getUserNameByUserGuid(userSession.getUserGuid());
            }
            String content = "";
            if (StringUtil.isNotBlank(auditTaskExtension.getNotify_nsl())) {
                content = auditTaskExtension.getNotify_nsl();
                content = content.replace("[#=ApplyerName#]", auditProject.getApplyername());
                content = content.replace("[#=ApplyDate#]",
                        EpointDateUtil.convertDate2String(auditProject.getApplydate(), "yyyy-MM-dd HH:mm:ss"));
                content = content.replace("[#=ProjectName#]", auditProject.getProjectname());
                content = content.replace("[#=Reason#]", reason);
            }
            // 调整MessageType字段"短信"为办件的areacode
            messagesCenterService.insertSmsMessage(UUID.randomUUID().toString(), content, new Date(), 0, new Date(),
                    auditProject.getContactmobile(), targetUser, targetDispName, userSession.getUserGuid(),
                    userService.getUserNameByUserGuid(userSession.getUserGuid()), "", "", "", false,
                    auditProject.getAreacode());
        }
        String[] str = message.split(";");
        addCallbackParam("message", message);
        // 流程中止操作
        if (StringUtil.isNotBlank(auditProject.getPviguid())) {
            ProcessVersionInstance pvi = wfinstance.getProcessVersionInstance(auditProject.getPviguid());
            List<WorkflowWorkItem> listWorkItem = wfinstance.getWorkItemListByUserGuid(pvi, userSession.getUserGuid());
            if (pvi != null && listWorkItem.size() > 0) {
                auditWorkflowBizlogic.finish(listWorkItem.get(0).getWorkItemGuid(), reason, pvi, "");
            }
        }
        if (StringUtil.isNotBlank(str)) {
            addCallbackParam("bysljdsaddress", "0");
        }
        else {
            addCallbackParam("bysljdsaddress", "1");
        }
        // biguid不为空则为并联审批
        if (auditProject.getBiguid() != null) {
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("biguid", auditProject.getBiguid());
            List<AuditProject> auditProjects = auditProjectService.getAuditProjectListByCondition(sql.getMap())
                    .getResult();
            boolean check = true;
            for (AuditProject auditproject : auditProjects) {
                if (auditproject.getStatus() >= ZwfwConstant.BANJIAN_STATUS_ZCBJ) {
                    check = true;
                }
                else {
                    check = false;
                    break;
                }
            }
            if (check == true) {
                AuditSpInstance auditSpInstance = auditSpInstanceService.getDetailByBIGuid(auditProject.getBiguid())
                        .getResult();
                String itemName = auditSpInstance.getItemname();
                iOnlineMessageInfoService.insertMessage(UUID.randomUUID().toString(),
                        UserSession.getInstance().getUserGuid(), UserSession.getInstance().getDisplayName(),
                        auditProject.getAcceptuserguid(), "", auditProject.getAcceptuserguid(),
                        "您发起的项目 " + itemName + "事项已全部办理完毕！", new Date());
            }
        }
        // 如果存在待办事宜，则删除待办
        List<MessagesCenter> messagesCenterList = messagesCenterService.queryForList(userSession.getUserGuid(), null,
                null, "", IMessagesCenterService.MESSAGETYPE_WAIT, auditProject.getRowguid(), "", -1, "", null, null, 0,
                -1);
        if (messagesCenterList != null && messagesCenterList.size() > 0) {
            for (MessagesCenter messagescenter : messagesCenterList) {
                messagesCenterService.deleteMessage(messagescenter.getMessageItemGuid(),
                        messagescenter.getTargetUser());
            }
        }

        // *************开始***********
        // add by yrchan,2022-04-21,勘验事项会从小程序调用接口，发送代办给配置的所有窗口人员；
        if (auditTaskExtension != null
                && ZwfwConstant.CONSTANT_STR_ONE.equals(auditTaskExtension.getStr("is_inquest"))) {

            // 根据中心标识 centerguid，确定所选事项所在窗口，获取该窗口的关联业务人员，发送【待受理】待办
            // 根据指定条件获取窗口人员
            SqlConditionUtil windowUserSql = new SqlConditionUtil();
            windowUserSql.setSelectFields("distinct userguid,username");
            windowUserSql.in("windowguid",
                    "(select a.rowguid from audit_orga_window a,audit_orga_windowtask b where a.RowGuid=b.WINDOWGUID AND b.TASKID = '"
                            + auditProject.getTask_id() + "' and a.centerguid = '" + auditProject.getCenterguid()
                            + "')");
            List<AuditOrgaWindowUser> windowUserList = iAuditOrgaWindow.getWindowUser(windowUserSql.getMap())
                    .getResult();
            if (!windowUserList.isEmpty()) {
                for (AuditOrgaWindowUser windowUser : windowUserList) {
                    List<MessagesCenter> messagesList = messagesCenterService.queryForList(windowUser.getUserguid(),
                            null, null, "", IMessagesCenterService.MESSAGETYPE_WAIT, auditProject.getRowguid(), "", -1,
                            "", null, null, 0, -1);
                    if (!messagesList.isEmpty()) {
                        for (MessagesCenter messagescenter : messagesList) {
                            messagesCenterService.deleteMessage(messagescenter.getMessageItemGuid(),
                                    messagescenter.getTargetUser());
                        }
                    }
                }
            }
        }
        // *************结束***********

        if (StringUtil.isNotBlank(auditProject.getBusinessguid()) && "4".equals(auditProject.getStr("is_lczj"))) {
            auditProject.setStatus(97);
            handleProject.saveProject(auditProject);
            log.info("调用电力接口推送办件状态及信息=========>>>>>>>>不予受理：" + auditProject.getStatus());
            AuditTask auditTask = audittask.getAuditTaskByGuid(auditProject.getTaskguid(), false).getResult();
            new ElectricityController().pushStatusToEc(auditProject, auditTask, "不予受理");
        }

    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
