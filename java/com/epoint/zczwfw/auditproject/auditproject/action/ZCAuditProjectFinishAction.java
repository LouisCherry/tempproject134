package com.epoint.zczwfw.auditproject.auditproject.action;

import java.util.Date;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectoperation.domain.AuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectoperation.inter.IAuditProjectOperation;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.fileupload.FileUploadModel9;
import com.epoint.basic.faces.tree.DefaultFileUploadHandlerImpl9;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.message.remind.api.IMessagesMessageService;
import com.epoint.frame.service.message.remind.entity.MessagesMessage;

/**
 * 
 * 勘验事项，办结
 * 
 * @author yrchan
 * @version 2022年4月19日
 */
@RestController("zcauditprojectfinishaction")
@Scope("request")
public class ZCAuditProjectFinishAction extends BaseController
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
    transient Logger log = Logger.getLogger(ZCAuditProjectFinishAction.class);

    @Autowired
    private IAuditProject auditProjectService;

    @Autowired
    private IAuditProjectOperation auditProjectOperationService;

    /**
     * 消息提醒
     */
    @Autowired
    private IMessagesMessageService iMessagesMessageService;

    private AuditProjectOperation auditProjectOperation;

    /**
     * 上传附件
     */
    private FileUploadModel9 fileUploadModel;
    private String operationRowGuid = "";

    @Override
    public void pageLoad() {
        projectguid = getRequestParameter("projectguid");
        String fields = " biguid,acceptuserguid,rowguid,projectname,applyeruserguid,applyername,pviguid,hebingshoulishuliang,taskguid,remark,ouguid,windowguid,applyway,areacode,tasktype,centerguid,taskcaseguid,contactmobile,applydate,status,ouname,applyertype,is_lczj,flowsn  ";
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
        auditProject = auditProjectService.getAuditProjectByRowGuid(fields, projectguid, area).getResult();
        if (auditProject != null) {
            // 办件操作类型:60:办结
            String strWhere = "projectguid = '" + auditProject.getRowguid() + "' and  operatetype = '"
                    + ZwfwConstant.OPERATE_BJ + "'";
            String filds = " rowguid,remarks ";
            // 添加办结备注
            auditProjectOperation = auditProjectOperationService.getOperationFileldByProjectGuid(strWhere, filds, "")
                    .getResult();
            if (auditProjectOperation == null) {
                // 是勘验事项
                if (!isPostback()) {
                    // 第一次加载页面随机产生，并放入viewdata
                    operationRowGuid = UUID.randomUUID().toString();
                    addViewData("operationRowGuid", operationRowGuid);
                }
                else {
                    // 非第一次从viewdata中获取
                    operationRowGuid = getViewData("operationRowGuid");
                }
            }

        }

    }

    /**
     * 确认
     * 
     */
    public void add() {
        if (auditProject.getStatus() >= ZwfwConstant.BANJIAN_STATUS_ZCBJ) {
            addCallbackParam("error", "该步骤已经处理完成，请勿重复操作！");
        }
        if (auditProjectOperation != null) {
            auditProjectOperation.setRemarks(reason);
            auditProjectOperationService.updateAuditProjectOperation(auditProjectOperation);
        }
        else {
            auditProjectOperation = new AuditProjectOperation();
            if (StringUtil.isNotBlank(operationRowGuid)) {
                auditProjectOperation.setRowguid(operationRowGuid);
                auditProjectOperation.setOperatedate(new Date());
                auditProjectOperation.setOperateUserGuid(userSession.getUserGuid());
                auditProjectOperation.setOperateusername(userSession.getDisplayName());
                auditProjectOperation.setProjectGuid(auditProject.getRowguid());
                auditProjectOperation.setRemarks(reason);
                auditProjectOperation.setPVIGuid(auditProject.getPviguid());
                auditProjectOperation.setApplyerName(auditProject.getApplyername());
                auditProjectOperation.setTaskGuid(auditProject.getTaskguid());
                auditProjectOperation.setOperateType(ZwfwConstant.OPERATE_BJ);
                auditProjectOperation.setAreaCode(auditProject.getAreacode());
                auditProjectOperation.setApplyerGuid(auditProject.getApplyeruserguid());
                auditProjectOperationService.addProjectOperation(auditProjectOperation);
            }

        }
        // 给申请人发送消息提醒，消息内容“XX申请人，你好，您申请的【办件名称】因xxx（不予受理）原因，不予受理，请知悉！
        String content = auditProject.getApplyername() + "申请人，你好，您申请的【" + auditProject.getProjectname() + "】已正常办结，请知悉！";
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
        messagesMessage.setTitle("正常办结");
        iMessagesMessageService.insertMessage(messagesMessage);
    }

    /**
     * 
     * add by yrchan,2022-04-19,[附件上传]
     * 
     * @return
     */
    public FileUploadModel9 getFileUploadModel() {
        if (fileUploadModel == null) {
            if (StringUtil.isNotBlank(getViewData("operationRowGuid"))) {
                operationRowGuid = getViewData("operationRowGuid");
            }
            else {
                operationRowGuid = UUID.randomUUID().toString();
                addViewData("operationRowGuid", operationRowGuid);
            }
            fileUploadModel = new FileUploadModel9(new DefaultFileUploadHandlerImpl9(operationRowGuid,
                    userSession.getUserGuid(), userSession.getDisplayName()));
        }
        return fileUploadModel;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
