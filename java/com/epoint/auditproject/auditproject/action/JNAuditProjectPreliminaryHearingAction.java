package com.epoint.auditproject.auditproject.action;

import java.util.Date;
import java.util.UUID;

import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.auditonlineproject.auditapplyjslog.inter.IAuditApplyJsLog;
import com.epoint.basic.auditonlineuser.auditonlineindividual.domain.AuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineindividual.inter.IAuditOnlineIndividual;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectnotify.inter.IAuditProjectNotify;
import com.epoint.basic.auditproject.auditprojectnumber.inter.IAuditProjectNumber;
import com.epoint.basic.auditproject.auditprojectoperation.inter.IAuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectunusual.inter.IAuditProjectUnusual;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyBaseinfo;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.DHSendMsgUtil;
import com.epoint.common.util.StarProjectInteractUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.message.api.IMessagesCenterService;

@RestController("jnauditprojectpreliminaryhearingaction")
@Scope("request")
public class JNAuditProjectPreliminaryHearingAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 9019405271414532899L;

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
     * 预审通过原因
     */
    private String reason;

    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private IAuditProjectUnusual projectUnusualService;

    @Autowired
    private IAuditProjectNumber projectNumberService;

    @Autowired
    private IAuditProjectNotify projectNotifyService;

    @Autowired
    private IHandleProject handleProject;

    @Autowired
    private IMessagesCenterService messageCenterService;

    @Autowired
    private IAuditTask iAuditTask;

    @Autowired
    private IAuditTaskExtension auditTaskExtensionService;

    @Autowired
    private IAuditApplyJsLog iAuditApplyJsLog;

    @Autowired
    private IAuditOnlineIndividual auditOnlieIndividualService;
    @Autowired
    IAuditRsCompanyBaseinfo auditRsCompanyBaseinfoService;

    @Autowired
    private IAuditProjectOperation auditProjectOperationService;
    /**
     * 事项拓展信息
     */
    private AuditTaskExtension auditTaskExtension;

    @Override
    public void pageLoad() {
        workitemguid = getRequestParameter("workItemGuid");
        projectguid = getRequestParameter("projectguid");
        String fields = " rowguid,flowsn,ouname,taskguid,centerguid,projectname,applyeruserguid,applyername,areacode,pviguid,hebingshoulishuliang,remark,ouguid,windowguid,applyway,status,applyertype,certnum,certtype,contactperson,contactphone,contactmobile,contactpostcode,contactemail,contactfax,address,contactcertnum,legal,applydate,ouname";
        String areacode = "";
        if (ZwfwConstant.AREA_TYPE_XZJ.equals(ZwfwUserSession.getInstance().getCitylevel())
                || ZwfwConstant.AREA_TYPE_CJ.equals(ZwfwUserSession.getInstance().getCitylevel())) {
            areacode = ZwfwUserSession.getInstance().getBaseAreaCode();
        }
        else {
            areacode = ZwfwUserSession.getInstance().getAreaCode();
        }
        auditProject = auditProjectService.getAuditProjectByRowGuid(fields, projectguid, areacode).getResult();
        auditTaskExtension = auditTaskExtensionService.getTaskExtensionByTaskGuid(auditProject.getTaskguid(), true)
                .getResult();
    }

    /**
     * 确认
     * 
     */
    public void add() {
        log.info("预审退回zwj,开始");
        if (auditProject.getStatus() >= ZwfwConstant.BANJIAN_STATUS_WWYSTU) {
            addCallbackParam("message", "该步骤已经处理完成，请勿重复操作！");
            return;
        }

        String isPass = getRequestParameter("isPass");
        String message = "";
        String title = "";
        String applyerGuid = "";
        String openUrl = "";
        if (StringUtil.isNotBlank(isPass) && Integer.parseInt(isPass) == 1) {
            message = publicMethod(ZwfwConstant.BANJIAN_STATUS_WWYSTG, "预审通过", "", reason);
            // 大厅发代办标题
            title = "【预审通过】" + auditProject.getProjectname();
            openUrl = configService.getFrameConfigValue("zwdtMsgurl")
                    + "/epointzwmhwz/pages/myspace/detail?projectguid=" + auditProject.getRowguid()
                    + "&tabtype=5&taskguid=" + auditProject.getTaskguid() + "&taskcaseguid="
                    + auditProject.getTaskcaseguid();

        }
        else {
            message = publicMethod(ZwfwConstant.BANJIAN_STATUS_WWYSTU, "预审打回", "", reason);
            // 大厅发代办标题
            title = "【预审退回】" + auditProject.getProjectname();
            openUrl = configService.getFrameConfigValue("zwdtMsgurl")
                    + "/epointzwmhwz/pages/onlinedeclaration/declarationinfo?projectguid=" + auditProject.getRowguid()
                    + "&taskguid=" + auditProject.getTaskguid() + "&taskcaseguid=" + auditProject.getTaskcaseguid()
                    + "&centerguid=" + auditProject.getCenterguid();

        }

        // 大厅发代办
        int type = auditProject.getApplyertype();
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
            messageCenterService.insertWaitHandleMessage(UUID.randomUUID().toString(), title,
                    IMessagesCenterService.MESSAGETYPE_WAIT, applyerGuid, "", UserSession.getInstance().getUserGuid(),
                    UserSession.getInstance().getDisplayName(), "", openUrl, UserSession.getInstance().getOuGuid(),
                    UserSession.getInstance().getBaseOUGuid(), 1, null, "", null, null, new Date(), null,
                    UserSession.getInstance().getUserGuid(), null, "");
        }

        // 删除外网发送的代办
        handleProject.delProjectMessage(auditProject.getTask_id(), auditProject.getRowguid());

        // 推送mq消息,同步操作给
        ISendMQMessage sendMQMessageService = ContainerFactory.getContainInfo().getComponent(ISendMQMessage.class);
        log.info("预审退回zwj,flow:" + auditProject.getFlowsn());
        sendMQMessageService.sendByExchange("zwdt_exchange_handle",
                auditProject.getRowguid() + "@" + auditProject.getAreacode(), "space.saving.docking.1");
        addCallbackParam("message", message);
    }

    /**
     * 公共方法
     * 
     * @param banjianStatus
     * @param banjianTitle
     * @param handurl
     * @param reason
     * @return
     */
    public String publicMethod(int banjianStatus, String banjianTitle, String handurl, String reason) {
        // 1、插入异常操作信息
        projectUnusualService.insertProjectUnusual(userSession.getUserGuid(), userSession.getDisplayName(),
                auditProject, banjianStatus, workitemguid, reason);
        // 2、插入每日一填的记录
        projectNumberService.addProjectNumber(auditProject, false, userSession.getUserGuid(),
                userSession.getDisplayName());
        // 3.插入在线通知
        String notirytitle = "【" + banjianTitle + "】" + "<" + auditProject.getProjectname() + ">";
        projectNotifyService.addProjectNotify(auditProject.getApplyeruserguid(), auditProject.getApplyername(),
                notirytitle, reason, handurl, ZwfwConstant.CLIENTTYPE_BJ, auditProject.getRowguid(),
                String.valueOf(banjianStatus), userSession.getUserGuid(), userSession.getDisplayName());
        String message = "";
        String isYS = "";
        if (ZwfwConstant.BANJIAN_STATUS_WWYSTG == banjianStatus) {
            // 4.发送待办事宜
            AuditCommonResult<AuditTask> taskResult = iAuditTask.getAuditTaskByGuid(auditProject.getTaskguid(), true);
            AuditTask task = taskResult.getResult();
            String messageItemGuid = UUID.randomUUID().toString();
            String title = "【待接件】" + auditProject.getProjectname() + "(" + auditProject.getApplyername() + ")";
            String clientTag = "";
            if (auditProject.getApplyeruserguid() != null) {
                clientTag = auditProject.getRowguid().substring(0, 1);
            }
            String formUrl = StringUtil.isNotBlank(auditTaskExtension.getCustomformurl())
                    ? auditTaskExtension.getCustomformurl()
                    : ZwfwConstant.CONSTANT_FORM_URL;
            String handleUrl = formUrl + "?processguid=" + task.getProcessguid() + "&taskguid="
                    + auditProject.getTaskguid() + "&projectguid=" + auditProject.getRowguid();
            messageCenterService.insertWaitHandleMessage(messageItemGuid, title,
                    IMessagesCenterService.MESSAGETYPE_WAIT, userSession.getUserGuid(), userSession.getDisplayName(),
                    userSession.getUserGuid(), userSession.getDisplayName(), "待接件", handleUrl, userSession.getOuGuid(),
                    "", ZwfwConstant.CONSTANT_INT_ONE, "", "", auditProject.getRowguid(), clientTag, new Date(),
                    auditProject.getPviguid(), userSession.getUserGuid(), "", "");
            auditProject.setCenterguid(ZwfwUserSession.getInstance().getCenterGuid());
            message = handleProject
                    .handleYstgAddreason(auditProject, userSession.getDisplayName(), userSession.getUserGuid(), reason)
                    .getResult();
            isYS = "10";
        }
        else {
            AuditCommonResult<AuditTask> taskResult = iAuditTask.getAuditTaskByGuid(auditProject.getTaskguid(), true);
            AuditTask task = taskResult.getResult();
            message = handleProject
                    .handleYsdhAddreason(auditProject, userSession.getDisplayName(), userSession.getUserGuid(), reason)
                    .getResult();

            isYS = "11";
            // 先判断是否江苏通用
            String isJSTY = ConfigUtil.getConfigValue("isJSTY");
            if (ZwfwConstant.CONSTANT_STR_ONE.equals(isJSTY)) {
                String url = ConfigUtil.getConfigValue("dhlogin", "zwdturl");
                // 判断是否配置
                if (StringUtil.isNotBlank(url)) {
                    // 调用大汉统一消息接口
                    try {
                        Record recorddh = DHSendMsgUtil.PostDHMessage("您申请的办件有了新的进度!",
                                auditProject.getContactperson() + "你好，您于"
                                        + EpointDateUtil.convertDate2String(auditProject.getApplydate(),
                                                "yyyy年MM月dd日 HH:mm")
                                        + "在网上申请的“" + auditProject.getProjectname() + "”，预审未通过，请您尽快在江苏政务服务网进行确认。",
                                auditProject.getContactcertnum());
                        iAuditApplyJsLog.insertDHAuditApplyJsLog(recorddh);
                    }
                    catch (DocumentException e) {
                        e.printStackTrace();
                    }
                    url += "/epointzwdt/pages/onlinedeclaration_js/declarationinfo?declareid=" + task.getTask_id()
                            + "&centerguid=" + auditProject.getCenterguid() + "&taskguid=" + task.getRowguid()
                            + "&flowsn=" + auditProject.getFlowsn();
                    // 星网推送状态
                    Record record = StarProjectInteractUtil
                            .updateOrInsertStar(auditProject.getFlowsn(), ZwdtConstant.STAR_PROJECT_YSBTG,
                                    StringUtil.isBlank(task.getDept_yw_reg_no()) ? task.getItem_id()
                                            : task.getDept_yw_reg_no(),
                                    task.getTaskname(), auditProject.getContactperson(),
                                    auditProject.getContactcertnum(), "继续申报", url);
                    iAuditApplyJsLog.insertStarAuditApplyJsLog(record);
                }
            }
        }
        return message;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
