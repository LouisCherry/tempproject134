
package com.epoint.sghd.auditjianguan.action;

import com.epoint.basic.auditproject.auditprojectmonitorresult.domain.AuditProjectMonitorResult;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.AttachHandler9;
import com.epoint.basic.faces.fileupload.AttachStorage;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.organ.user.api.IUserService;
import com.epoint.frame.service.organ.user.entity.FrameUser;
import com.epoint.jxgl.commonutils.JxglConstant;
import com.epoint.sghd.auditjianguan.domain.AuditProjectPermissionChange;
import com.epoint.sghd.auditjianguan.inter.IJnAuditJianGuan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 发起许可变更意见action
 *
 * @version [版本号, 2018年10月10日]
 * @作者 shibin
 */
@RestController("jnauditprojectbiangengaction")
@Scope("request")
public class JnAuditProjectBianGengAction extends BaseController {
    private static final long serialVersionUID = 181286499037869484L;

    private String monitorGuid = "";
    private String userguid = "";
    private String senduserguid = "";
    private String isreply = "";

    private AuditProjectPermissionChange dataBean = null;

    private AuditProjectMonitorResult result = null;

    private String response = "未认定";

    private String monitorOpinion = "";
    private String banjieOpinion = "";

    @Autowired
    private IMessagesCenterService messageCenterService;

    @Autowired
    private IUserService userService;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditProjectPermissionChange> model;

    /**
     * 附件上传model
     */
    private FileUploadModel9 fileUploadModel = null;

    private String fileclientguidnew;

    @Autowired
    private IJnAuditJianGuan jnAuditJianGuanService;

    @Override
    public void pageLoad() {
        monitorGuid = getRequestParameter("monitorGuid");
        userguid = getRequestParameter("selectuserguid");
        senduserguid = getRequestParameter("senduserguid");
        isreply = getRequestParameter("isreply");
        dataBean = jnAuditJianGuanService.find(AuditProjectPermissionChange.class, monitorGuid);

        List<Record> list = new ArrayList<>();
        String themeGuid = jnAuditJianGuanService.getThemeguidFromAuditPP(dataBean.getRowguid());
        list = jnAuditJianGuanService.getFileclientguidByThemeguid(themeGuid);

        List<FrameAttachInfo> frameAttachInfoAllList = new ArrayList<>();
        List<FrameAttachInfo> frameAttachInfoList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            frameAttachInfoList = jnAuditJianGuanService
                    .getAllFrameAttachInfoByCliengGuid(list.get(i).getStr("fileclientguid"));

            frameAttachInfoAllList.addAll(frameAttachInfoList);
        }

        addCallbackParam("list", frameAttachInfoAllList);

        if (StringUtil.isNotBlank(this.dataBean.getFileclientguid())) {
            this.fileclientguidnew = this.dataBean.getFileclientguid();
            this.addViewData("fileclientguid", this.fileclientguidnew);
        }

        if (StringUtil.isBlank(this.getViewData("fileclientguidnew"))) {
            this.fileclientguidnew = UUID.randomUUID().toString();
            this.addViewData("fileclientguidnew", this.fileclientguidnew);
        }

    }

    public DataGridModel<AuditProjectPermissionChange> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditProjectPermissionChange>() {

                @Override
                public List<AuditProjectPermissionChange> fetchData(int first, int pageSize, String sortField,
                                                                    String sortOrder) {
                    String themeGuid = jnAuditJianGuanService.getThemeguidFromAuditPP(monitorGuid);

                    List<AuditProjectPermissionChange> monitorReplies = jnAuditJianGuanService
                            .getAuditPPByThemeguid(themeGuid);

                    this.setRowCount(monitorReplies != null ? monitorReplies.size() : 0);
                    return monitorReplies;
                }
            };
        }
        return model;
    }

    public FileUploadModel9 getFileUploadModel() {
        if (fileUploadModel == null) {
            AttachHandler9 attachHandler9 = new AttachHandler9() {
                private static final long serialVersionUID = 1L;

                @Override
                public boolean beforeSaveAttachToDB(AttachStorage attach) {
                    return true;
                }

                @Override
                public void afterSaveAttachToDB(Object attach) {
                    fileUploadModel.getExtraDatas().put("msg", "上传成功！");
                }
            };
            fileUploadModel = new FileUploadModel9(
                    new DefaultFileUploadHandlerImpl9(this.getViewData("fileclientguidnew"), null, null, attachHandler9,
                            userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fileUploadModel;
    }

    /**
     * 实现反馈的功能
     */
    public void monitorBack() {
        String replyName = userSession.getDisplayName();
        AuditProjectPermissionChange reply = new AuditProjectPermissionChange();
        reply.setRowguid(UUID.randomUUID().toString());
        reply.setOuguid(userSession.getOuGuid());
        reply.setOuname(userSession.getOuName());
        reply.setSendUserGuid(userSession.getUserGuid());
        reply.setSendperson(replyName);
        if (replyName.equals(dataBean.getSendperson())) {
            reply.setCommunicationUserGuid(dataBean.getCommunicationUserGuid());
            reply.setCommunicationperson(dataBean.getCommunicationperson());
            reply.setCommunicationOuGuid(dataBean.getCommunicationOuGuid());
            reply.setCommunicationOuName(dataBean.getCommunicationOuName());
        } else {
            reply.setCommunicationUserGuid(dataBean.getSendUserGuid());
            reply.setCommunicationperson(dataBean.getSendperson());
            reply.setCommunicationOuGuid(dataBean.getOuguid());
            reply.setCommunicationOuName(dataBean.getOuname());
        }
        reply.setCommunicationtheme(dataBean.getCommunicationtheme());
        reply.setThemeguid(dataBean.getThemeguid());
        reply.setChangeopinion(this.monitorOpinion);
        reply.setDeadtime(dataBean.getDeadtime());

        // 更新audit_project_permissionchange中的fileclientguid，以及frame_attachinfo中附件的CLIENGGUID
        reply.setFileclientguid(this.getViewData("fileclientguidnew"));

        reply.setReplydate(new Date());

        //        jnAuditJianGuanService.insert(reply);

        if (StringUtil.isBlank(this.getRequestParameter("messageItemGuid"))) {
            this.messageCenterService.deleteMessageByIdentifier(this.dataBean.getRowguid(),
                    this.userSession.getUserGuid());
        }
        String title = null;
        String handleUrl = null;
        FrameUser user;
        if ("1".equals(isreply)) {
            user = this.userService.getUserByUserField("userguid", senduserguid);
            title = "【许可变更意见反馈】关于【" + dataBean.getCommunicationtheme() + "】主题的反馈(" + this.userSession.getDisplayName()
                    + ")";
            handleUrl = "jiningzwfw/sghd/projectjianguan/auditprojectbiangeng?monitorGuid=" + this.dataBean.getRowguid()
                    + "&isreply=0&selectuserguid=" + userSession.getUserGuid();
            this.sendWaitHandleMessage(title, user.getUserGuid(), user.getDisplayName(), handleUrl,
                    this.dataBean.getRowguid(), "", "【许可变更意见反馈】", "许可变更意见反馈", user, user.getOuGuid());
        } else {
            user = this.userService.getUserByUserField("userguid", userguid);
            title = "【许可变更意见反馈】关于【" + dataBean.getCommunicationtheme() + "】主题的反馈(" + this.userSession.getDisplayName()
                    + ")";
            handleUrl = "jiningzwfw/sghd/projectjianguan/auditprojectbiangeng?monitorGuid=" + this.dataBean.getRowguid()
                    + "&isreply=0&selectuserguid=" + userSession.getUserGuid();
            this.sendWaitHandleMessage(title, user.getUserGuid(), user.getDisplayName(), handleUrl,
                    this.dataBean.getRowguid(), "", "【许可变更意见反馈】", "许可变更意见反馈", user, user.getOuGuid());
        }

        reply.setHandleUrl(handleUrl);
        jnAuditJianGuanService.insert(reply);

        if (StringUtil.isNotBlank(this.getRequestParameter("messageItemGuid"))) {
            this.messageCenterService.deleteMessage(this.getRequestParameter("messageItemGuid"),
                    this.userSession.getUserGuid());
        }
        this.addCallbackParam("msg", "反馈成功！");
    }

    /**
     * 发送代办消息
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
                new Date(), "", this.userSession.getUserGuid(), "", "");
    }

    /**
     * 实现办结的功能
     */
    public void endMonitor() {
        String replyName = userSession.getDisplayName();
        AuditProjectPermissionChange permission = new AuditProjectPermissionChange();
        permission.setRowguid(UUID.randomUUID().toString());
        permission.setOuguid(userSession.getOuGuid());
        permission.setOuname(userSession.getOuName());
        permission.setSendUserGuid(userSession.getUserGuid());
        permission.setSendperson(replyName);
        if (replyName.equals(dataBean.getSendperson())) {
            permission.setCommunicationUserGuid(dataBean.getCommunicationUserGuid());
            permission.setCommunicationperson(dataBean.getCommunicationperson());
            permission.setCommunicationOuGuid(dataBean.getCommunicationOuGuid());
            permission.setCommunicationOuName(dataBean.getCommunicationOuName());
        } else {
            permission.setCommunicationUserGuid(dataBean.getSendUserGuid());
            permission.setCommunicationperson(dataBean.getSendperson());
            permission.setCommunicationOuGuid(dataBean.getOuguid());
            permission.setCommunicationOuName(dataBean.getOuname());
        }
        permission.setCommunicationtheme(dataBean.getCommunicationtheme());
        permission.setThemeguid(dataBean.getThemeguid());
        permission.setChangeopinion(this.banjieOpinion);
        permission.setDeadtime(dataBean.getDeadtime());
        permission.setFileclientguid(this.getViewData("fileclientguidnew"));
        permission.setReplydate(new Date());
        permission.setBanjiesign("1");

        jnAuditJianGuanService.insert(permission);

        if (StringUtil.isNotBlank(this.getRequestParameter("messageItemGuid"))) {
            this.messageCenterService.deleteMessage(this.getRequestParameter("messageItemGuid"),
                    this.userSession.getUserGuid());
        } else {
            this.messageCenterService.deleteMessageByIdentifier(this.dataBean.getRowguid(),
                    this.userSession.getUserGuid());
        }

        this.addCallbackParam("msg", "办结成功！");
    }

    public AuditProjectPermissionChange getDataBean() {
        return dataBean;
    }

    public void setDataBean(AuditProjectPermissionChange dataBean) {
        this.dataBean = dataBean;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public AuditProjectMonitorResult getResult() {
        return result;
    }

    public void setResult(AuditProjectMonitorResult result) {
        this.result = result;
    }

    public String getMonitorOpinion() {
        return monitorOpinion;
    }

    public void setMonitorOpinion(String monitorOpinion) {
        this.monitorOpinion = monitorOpinion;
    }

    public String getBanjieOpinion() {
        return banjieOpinion;
    }

    public void setBanjieOpinion(String banjieOpinion) {
        this.banjieOpinion = banjieOpinion;
    }

    public String getUserguid() {
        return userguid;
    }

    public void setUserguid(String userguid) {
        this.userguid = userguid;
    }

    public String getSenduserguid() {
        return senduserguid;
    }

    public void setSenduserguid(String senduserguid) {
        this.senduserguid = senduserguid;
    }

    public String getIsreply() {
        return isreply;
    }

    public void setIsreply(String isreply) {
        this.isreply = isreply;
    }
}
