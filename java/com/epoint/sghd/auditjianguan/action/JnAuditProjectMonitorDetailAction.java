package com.epoint.sghd.auditjianguan.action;

import com.epoint.basic.controller.BaseController;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.message.entity.MessagesCenter;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.frame.service.organ.role.api.IRoleService;
import com.epoint.frame.service.organ.role.entity.FrameRole;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.frame.service.organ.userrole.api.IUserRoleRelationService;
import com.epoint.frame.service.organ.userrole.entity.FrameUserRoleRelation;
import com.epoint.jxgl.basic.audit.domain.AuditProject;
import com.epoint.jxgl.basic.audit.inter.IJxglAuditProject;
import com.epoint.jxgl.basic.auditprojectmonitor.domain.AuditProjectMonitor;
import com.epoint.jxgl.basic.auditprojectmonitor.inter.IJxglMonitor;
import com.epoint.jxgl.basic.auditprojectmonitorreply.domain.AuditProjectMonitorReply;
import com.epoint.jxgl.basic.auditprojectmonitorreply.inter.IJxglMonitorReply;
import com.epoint.jxgl.basic.auditprojectmonitorresult.domain.AuditProjectMonitorResult;
import com.epoint.jxgl.basic.auditprojectmonitorresult.inter.IJxglMonitorResult;
import com.epoint.jxgl.basic.jxglmonitoralert.domain.JxglMonitorAlert;
import com.epoint.jxgl.basic.jxglmonitoralert.inter.IJxglMonitorAlert;
import com.epoint.jxgl.commonutils.JxglConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 *  办件督办反馈详情action
 * @作者 shibin
 * @version [版本号, 2018年10月10日]
 */
@RestController("jnauditprojectmonitordetailaction")
@Scope("request")
public class JnAuditProjectMonitorDetailAction extends BaseController {
    private static final long serialVersionUID = 181286499037869484L;
    private String monitorGuid = "";
    private String superviseorgname = "";
    private String projectname = "";
    private String projectguid = "";
    private String businessno = "";
    private String replyOpinion = "";
    private AuditProjectMonitor dataBean = null;
    private String isReply = "1";
    private String monitorOpinion = "";
    private String response = "";
    private String banjieOpinion = "";
    private String pviguid = "";
    private JxglMonitorAlert jxglMonitorAlert;
    private DataGridModel<AuditProjectMonitorReply> model;
    @Autowired
    private IUserService userService;
    @Autowired
    private IOuService ouService;
    @Autowired
    private IJxglMonitor superviseService;
    @Autowired
    private IJxglMonitorReply monitorReplyService;
    @Autowired
    private IUserRoleRelationService roleRelationService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IJxglMonitorResult monitorResultService;
    @Autowired
    private IMessagesCenterService messageCenterService;
    @Autowired
    private IJxglMonitorAlert monitorAlertService;
    @Autowired
    private IJxglAuditProject auditProjectService;

    @Override
    public void pageLoad() {
        this.monitorGuid = this.getRequestParameter("monitorGuid");
        this.dataBean = this.superviseService.getAuditProjectMonitorByGuid(this.monitorGuid);
        this.isReply = this.getRequestParameter("isreply");
        if (StringUtil.isBlank(this.getRequestParameter("isreply"))) {
            this.isReply = this.dataBean.getIsreply().toString();
        }

        String messageItemGuid = this.getRequestParameter("messageItemGuid");
        if (StringUtil.isNotBlank(messageItemGuid)) {
            MessagesCenter mc = this.messageCenterService.getDetail(messageItemGuid, this.userSession.getUserGuid());
            if (mc == null || mc.isEmpty()) {
                this.addCallbackParam("msg", "rehandle");
            }
        } else if (!this.isReply.equals(this.dataBean.getIsreply().toString())) {
            this.addCallbackParam("msg", "rehandle");
        } else if (this.dataBean.getEnd_date() != null) {
            this.addCallbackParam("msg", "isdone");
        }

        if (this.dataBean != null) {
            FrameOu ou = this.ouService.getOuByOuGuid(this.dataBean.getMonitor_org_id());
            if (ou != null) {
                this.superviseorgname = ou.getOuname();
            }

            if (JxglConstant.MONITOR_TYPE_SUPERVISE == this.dataBean.getSpvtype()) {
                this.jxglMonitorAlert = this.monitorAlertService
                        .getJxglMonitorAlertByGuid(this.dataBean.getAlertguid());
                if (this.jxglMonitorAlert != null) {
                    this.projectname = this.jxglMonitorAlert.getBusinessname();
                    this.businessno = this.jxglMonitorAlert.getBusinessno();
                    this.projectguid = this.jxglMonitorAlert.getInfoguid();
                    this.pviguid = this.jxglMonitorAlert.getPviguid();
                }
            } else {
                AuditProject auditProject = null;
                if (StringUtil.isNotBlank(this.dataBean.getAlertguid())) {
                    auditProject = this.auditProjectService.getAuditProjectByRowGuid(this.dataBean.getAlertguid(),
                            this.dataBean.getAreacode());
                } else {
                    auditProject = this.auditProjectService.getAuditProjectByFlowsn(this.dataBean.getBj_no(),
                            this.dataBean.getAreacode());
                }

                if (auditProject != null) {
                    this.projectname = auditProject.getProjectname();
                    this.businessno = auditProject.getFlowsn();
                    this.projectguid = auditProject.getRowguid();
                    this.pviguid = auditProject.getPviguid();
                }
            }
        }

    }

    /**
     *  实现反馈的功能
     */
    public void monitorBack() {
        AuditProjectMonitorReply projectMonitorReply = new AuditProjectMonitorReply();
        projectMonitorReply.setSupervise_org_id(this.dataBean.getSupervise_org_id());
        projectMonitorReply.setReply_date(new Date());
        projectMonitorReply.setMonitor_id(this.userSession.getUserGuid());
        projectMonitorReply.setMonitor_person(this.userSession.getDisplayName());
        projectMonitorReply.setMonitor_no(this.dataBean.getNo());
        projectMonitorReply.setNo(UUID.randomUUID().toString());
        projectMonitorReply.setRowguid(UUID.randomUUID().toString());
        projectMonitorReply.setOperatedate(new Date());
        projectMonitorReply.setOperateusername(this.userSession.getDisplayName());
//        if ("1".equals(this.isReply)) {
//            projectMonitorReply.setReply_opinion(this.reply_opinion);
//            this.dataBean.setIsreply(JxglConstant.CONSTANT_INT_ZERO);
//        } else {
            projectMonitorReply.setReply_opinion(this.monitorOpinion);
            this.dataBean.setIsreply(JxglConstant.CONSTANT_INT_ONE);
//        }

        this.monitorReplyService.addMonitorReply(projectMonitorReply);
        if (this.dataBean.getFirstreplydate() == null) {
            this.dataBean.setFirstreplydate(new Date());
        }

        this.dataBean.setSigndate(new Date());
        this.dataBean.setSignusername(this.userSession.getDisplayName());
        this.dataBean.setOperatedate(new Date());
        this.dataBean.setOperateusername(this.userSession.getDisplayName());
        this.superviseService.updateAuditProjectMonitor(this.dataBean);
        if (StringUtil.isBlank(this.getRequestParameter("messageItemGuid"))) {
            this.messageCenterService.deleteMessageByIdentifier(this.dataBean.getRowguid(),
                    this.userSession.getUserGuid());
        }

        String title = null;
        String handleUrl = null;
        FrameUser user;
        if ("1".equals(this.isReply)) {
            user = this.userService.getUserByUserField("userguid", this.dataBean.getSupervise_id());
            title = "【督办反馈】关于【" + this.projectname + "】办件的监察督办反馈(" + this.userSession.getDisplayName() + ")";
            handleUrl = "epointjxgl/runmonitor/supervision/monitorsupervisehandle?monitorGuid="
                    + this.dataBean.getRowguid() + "&isreply=0";
            this.sendWaitHandleMessage(title, this.dataBean.getSupervise_id(), this.dataBean.getSupervise_person(),
                    handleUrl, this.dataBean.getRowguid(), this.pviguid, "【督办反馈】", "督办反馈", user, user.getOuGuid());
            if (JxglConstant.MONITOR_TYPE_SUPERVISE == this.dataBean.getSpvtype()) {
                this.jxglMonitorAlert.setStatus("2");
                this.monitorAlertService.updateMonitorAlert(this.jxglMonitorAlert);
            }
        } else if (this.dataBean.getDubantype() != null && this.dataBean.getDubantype() == 10) {
            title = "【督办】关于【" + this.projectname + "】办件的监察督办(" + this.dataBean.getSupervise_person() + ")";
            handleUrl = "epointjxgl/runmonitor/supervision/monitorsupervisehandle?monitorGuid="
                    + this.dataBean.getRowguid() + "&isreply=1";
            user = this.userService.getUserByUserField("userguid", this.dataBean.getMonitor_id());
            this.sendWaitHandleMessage(title, this.dataBean.getMonitor_id(), this.dataBean.getMonitor_person(),
                    handleUrl, this.dataBean.getRowguid(), this.pviguid, "【督办】", "待回复督办", user, user.getOuGuid());
        } else if (this.dataBean.getDubantype() != null && this.dataBean.getDubantype() == 20) {
            FrameRole frameRole = this.roleService.getRoleByRoleField("rolename", "行政监察（部门）");
            if (frameRole != null) {
                String roleguid = frameRole.getRoleGuid();
                List<FrameUserRoleRelation> frameUserRoleRelations = this.roleRelationService
                        .getRelationListByField("roleGuid", roleguid, (String) null, (String) null);
                if (frameUserRoleRelations != null && frameUserRoleRelations.size() > 0) {
                    Iterator var7 = frameUserRoleRelations.iterator();

                    while (var7.hasNext()) {
                        FrameUserRoleRelation frameUserRoleRelation = (FrameUserRoleRelation) var7.next();
                        FrameUser user1 = this.userService.getUserByUserField("userguid",
                                frameUserRoleRelation.getUserGuid());
                        this.sendWaitHandleMessage(title, user1.getUserGuid(), user1.getDisplayName(), handleUrl,
                                this.dataBean.getRowguid(), this.pviguid, "【督办】", "待回复督办", user1, user1.getOuGuid());
                    }
                }
            }
        }

        if (StringUtil.isNotBlank(this.getRequestParameter("messageItemGuid"))) {
            this.messageCenterService.deleteMessage(this.getRequestParameter("messageItemGuid"),
                    this.userSession.getUserGuid());
        }

        this.addCallbackParam("msg", "反馈成功！");
    }

    /**
     *  实现办结的功能
     */
    public void endMonitor() {
        this.dataBean.setEnd_date(new Date());
        this.dataBean.setSignusername(this.userSession.getDisplayName());
        this.dataBean.setOperatedate(new Date());
        this.dataBean.setOperateusername(this.userSession.getDisplayName());
        this.superviseService.updateAuditProjectMonitor(this.dataBean);
        AuditProjectMonitorResult monitorResult = new AuditProjectMonitorResult();
        monitorResult.setOperatedate(new Date());
        monitorResult.setOperateuserguid(this.userSession.getUserGuid());
        monitorResult.setOperateusername(this.userSession.getDisplayName());
        monitorResult.setSupervise_org_id(this.dataBean.getSupervise_org_id());
        monitorResult.setEnd_opinion(this.banjieOpinion);
        monitorResult.setConfirm(this.response);
        if ("2".equals(this.response)) {
            monitorResult.setScoreoperation("0");
        } else {
            monitorResult.setScoreoperation("1");
        }

        monitorResult.setEnd_date(new Date());
        monitorResult.setMonitor_no(this.dataBean.getNo());
        monitorResult.setRowguid(UUID.randomUUID().toString());
        this.monitorResultService.addMonitorResult(monitorResult);
        if (StringUtil.isNotBlank(this.getRequestParameter("messageItemGuid"))) {
            this.messageCenterService.deleteMessage(this.getRequestParameter("messageItemGuid"),
                    this.userSession.getUserGuid());
        } else {
            this.messageCenterService.deleteMessageByIdentifier(this.dataBean.getRowguid(),
                    this.userSession.getUserGuid());
        }

        if (JxglConstant.MONITOR_TYPE_SUPERVISE == this.dataBean.getSpvtype()) {
            this.jxglMonitorAlert.setStatus("3");
            this.jxglMonitorAlert.setEnd_opinion(this.banjieOpinion);
            this.monitorAlertService.updateMonitorAlert(this.jxglMonitorAlert);
        }

        this.addCallbackParam("msg", "办结成功！");
    }

    /**
     * 发送代办消息
     *  @param title
     *  @param targetUser
     *  @param targetDispName
     *  @param handleUrl
     *  @param ClientIdentifier
     *  @param pviGuid
     *  @param clientTag
     *  @param beiZhu
     *  @param frameUser
     *  @param baseOUGuid
     *  @return    
     */
    public boolean sendWaitHandleMessage(String title, String targetUser, String targetDispName, String handleUrl,
            String clientIdentifier, String pviGuid, String clientTag, String beiZhu, FrameUser frameUser,
            String baseOUGuid) {
        String messageItemGuid = UUID.randomUUID().toString();
        if (StringUtil.isNotBlank(handleUrl) && handleUrl.indexOf("?") > 0) {
            handleUrl = handleUrl + "&messageItemGuid=" + messageItemGuid;
        } else {
            handleUrl = handleUrl + "?messageItemGuid=" + messageItemGuid;
        }

        handleUrl = handleUrl + "&clientIdentifier=" + clientIdentifier;
        return this.messageCenterService.insertWaitHandleMessage(messageItemGuid, title, "办理", targetUser,
                targetDispName, this.userSession.getUserGuid(), this.userSession.getDisplayName(), beiZhu, handleUrl,
                frameUser.getOuGuid(), baseOUGuid, JxglConstant.CONSTANT_INT_ONE, "", "", clientIdentifier, clientTag,
                new Date(), pviGuid, frameUser.getUserGuid(), "", "");
    }

    public String getSuperviseorgname() {
        return this.superviseorgname;
    }

    public void setSuperviseorgname(String superviseorgname) {
        this.superviseorgname = superviseorgname;
    }

    public String getProjectname() {
        return this.projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getBusinessno() {
        return this.businessno;
    }

    public void setBusinessno(String businessno) {
        this.businessno = businessno;
    }

    public AuditProjectMonitor getDataBean() {
        return this.dataBean;
    }

    public void setDataBean(AuditProjectMonitor dataBean) {
        this.dataBean = dataBean;
    }

    public String getProjectguid() {
        return this.projectguid;
    }

    public void setProjectguid(String projectguid) {
        this.projectguid = projectguid;
    }

    public String getReplyOpinion() {
        return this.replyOpinion;
    }

    public void setReplyOpinion(String replyOpinion) {
        this.replyOpinion = replyOpinion;
    }

    public String getMonitorOpinion() {
        return this.monitorOpinion;
    }

    public void setMonitorOpinion(String monitorOpinion) {
        this.monitorOpinion = monitorOpinion;
    }

    public String getResponse() {
        return this.response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getBanjieOpinion() {
        return this.banjieOpinion;
    }

    public void setBanjieOpinion(String banjieOpinion) {
        this.banjieOpinion = banjieOpinion;
    }

    public String getPviguid() {
        return this.pviguid;
    }

    public void setPviguid(String pviguid) {
        this.pviguid = pviguid;
    }
}