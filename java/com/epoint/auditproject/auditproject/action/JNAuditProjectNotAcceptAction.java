package com.epoint.auditproject.auditproject.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.basic.auditonlineuser.auditonlineproject.domain.AuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineproject.inter.IAuditOnlineProject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectnotify.inter.IAuditProjectNotify;
import com.epoint.basic.auditproject.auditprojectoperation.domain.AuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectoperation.inter.IAuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectunusual.inter.IAuditProjectUnusual;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspbusiness.inter.IAuditSpBusiness;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.inter.IAuditSpInstance;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.common.zwfw.workflow.AuditWorkflowBizlogic;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.message.api.IOnlineMessageInfoService;
import com.epoint.frame.service.message.entity.MessagesCenter;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.tazwfw.electricity.rest.action.ElectricityController;
import com.epoint.workflow.service.common.custom.ProcessVersionInstance;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;

@RestController("jnauditprojectnotacceptaction")
@Scope("request")
public class JNAuditProjectNotAcceptAction extends BaseController
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
    transient Logger log = Logger.getLogger(JNAuditProjectNotAcceptAction.class);

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
     * 子申报API
     */
    @Autowired
    private IAuditSpISubapp iAuditSpISubapp;

    /**
     * 办件API
     */
    @Autowired
    private IAuditOnlineProject iAuditOnlineProject;

    @Autowired
    private IAuditSpInstance iAuditSpInstance;

    /**
     * 事项拓展信息
     */
    private AuditTaskExtension auditTaskExtension;

    private IAuditTask audittask;

    @Autowired
    private IMessagesCenterService messagesCenterService;

    @Autowired
    private IAuditSpBusiness auditspbusiness;

    @Override
    public void pageLoad() {
        workitemguid = getRequestParameter("workItemGuid");
        projectguid = getRequestParameter("projectguid");
//        String fields = " biguid,acceptuserguid,rowguid,projectname,applyeruserguid,applyername,pviguid,hebingshoulishuliang,taskguid,remark,ouguid,windowguid,applyway,areacode,tasktype,centerguid,taskcaseguid,contactmobile,applydate,status,ouname,applyertype,is_lczj,flowsn,subappguid";
        auditProject = auditProjectService.getAuditProjectByRowGuid("*", projectguid, "").getResult();
        if (auditProject != null) {
            auditTaskExtension = auditTaskExtensionService.getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true)
                    .getResult();
        }
    }

    /**
     * 确认
     */
    public void add() {
        if (auditProject.getStatus() >= ZwfwConstant.BANJIAN_STATUS_YSL) {
            addCallbackParam("error", "该步骤已经处理完成，请勿重复操作！");
        }
        // 1、插入异常操作信息
        projectUnusualService.insertProjectUnusual(userSession.getUserGuid(), userSession.getDisplayName(),
                auditProject, Integer.parseInt(ZwfwConstant.BANJIANOPERATE_TYPE_BYSL), workitemguid, reason);
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

        // 一件事不予受理按钮添加退回功能
        // 判断是否为一件的单办件
        if (StringUtil.isNotBlank(auditProject.getSubappguid())) {
            log.info("promisedate:" + auditProject.getPromiseenddate());
            auditProject = auditProjectService.getAuditProjectByRowGuid("*", auditProject.getRowguid(), "").getResult();
            log.info("promisedate:" + auditProject.getPromiseenddate());
            auditProject.set("backreason", reason);// 退回原因

            boolean isGgXm = false;
            if (StringUtil.isNotBlank(auditProject.getBusinessguid())) {
                AuditSpBusiness auditSpBusiness = auditspbusiness
                        .getAuditSpBusinessByRowguid(auditProject.getBusinessguid()).getResult();
                if (auditSpBusiness != null) {
                    if ("1".equals(auditSpBusiness.getBusinesstype())) {
                        // 建设项目
                        isGgXm = true;
                    }
                }
            }

            // 更新子申报状态
            AuditSpISubapp auditSpISubapp = iAuditSpISubapp.getSubappByGuid(auditProject.getSubappguid()).getResult();
            if (auditSpISubapp != null) {
                String status = "26";
                // 如果是工改项目，需要判断下其他办件是否有待补正状态，如果有则需要改成待补正
                if (isGgXm) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    sql.eq("biguid", auditProject.getBiguid());
                    sql.eq("subappguid", auditProject.getSubappguid());
                    sql.eq("businessguid", auditProject.getBusinessguid());
                    sql.nq("rowguid", auditProject.getRowguid());
                    List<Integer> statusList = new ArrayList<Integer>();
                    // 28是受理补正，37是审核补正，对应的是34和35
                    statusList.add(ZwfwConstant.BANJIAN_STATUS_DBB);
                    statusList.add(ZwfwConstant.BANJIAN_STATUS_YSLDBB);
                    sql.in("status", StringUtil.joinSql(statusList));
                    sql.setSelectFields("rowguid");
                    // 只找一个吧
                    sql.setSelectCounts(1);
                    List<AuditProject> auditProjects = auditProjectService.getAuditProjectListByCondition(sql.getMap())
                            .getResult();
                    if (auditProjects != null && !auditProjects.isEmpty()) {
                        // 存在待补办
                        // 用35
                        status = ZwfwConstant.LHSP_Status_DBJ;
                    }
                }
                auditSpISubapp.setStatus(status);
                iAuditSpISubapp.updateAuditSpISubapp(auditSpISubapp);
            }
            // 更新一件事办件iAuditOnlineProject
            if (StringUtil.isNotBlank(auditProject.getBiguid())) {
                AuditSpInstance auditSpInstance = iAuditSpInstance.getDetailByBIGuid(auditProject.getBiguid())
                        .getResult();
                if (auditSpInstance != null) {
                    AuditOnlineProject auditOnlineProject = iAuditOnlineProject
                            .getOnlineProjectByApplyerGuid(auditProject.getBiguid(), auditSpInstance.getApplyerguid())
                            .getResult();
                    if (auditOnlineProject != null) {
                        auditOnlineProject.setStatus("101");
                        iAuditOnlineProject.updateProject(auditOnlineProject);
                    }
                }
            }
            auditProjectService.updateProject(auditProject);

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
        if (StringUtil.isNotBlank(auditProject.getBusinessguid()) && "4".equals(auditProject.getStr("is_lczj"))) {
            auditProject.setStatus(97);
            handleProject.saveProject(auditProject);
            log.info("调用电力接口推送办件状态及信息=========>>>>>>>>不予受理：" + auditProject.getStatus());
            AuditTask auditTask = audittask.getAuditTaskByGuid(auditProject.getTaskguid(), false).getResult();
            new ElectricityController().pushStatusToEc(auditProject, auditTask, "不予受理");
        }
        // 推送mq消息,同步操作给
        ISendMQMessage sendMQMessageService = ContainerFactory.getContainInfo().getComponent(ISendMQMessage.class);
        sendMQMessageService.sendByExchange("zwdt_exchange_handle",
                auditProject.getRowguid() + "@" + auditProject.getAreacode(), "space.saving.docking.1");

    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
