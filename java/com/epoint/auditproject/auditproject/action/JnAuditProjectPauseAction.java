package com.epoint.auditproject.auditproject.action;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;
import com.epoint.basic.auditproject.auditprojectunusual.inter.IAuditProjectUnusual;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.util.DataUtil;
import com.epoint.common.rabbitmq.ProducerMQ;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.api.ICodeMainService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.frame.service.metadata.mis.util.CodeModalFactory;

@RestController("jnauditprojectpauseaction")
@Scope("request")
public class JnAuditProjectPauseAction extends BaseController
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
     * 事项标识
     */
    private String taskGuid;
    /**
     * 工作项标识
     */
    private String workitemguid;
    /**
     * 办件信息
     */
    private AuditProject auditProject;
    /**
     * 暂停原因
     */
    private String pauseReason;
    /**
     * 备注
     */
    private String note;

    /**
     * 原因下拉列表modal
     */
    private List<SelectItem> reasonModel = null;

    @Autowired
    IAuditProject auditProjectService;

    @Autowired
    IAuditTask auditTaskService;

    @Autowired
    IHandleProject handleProjectService;

    @Autowired
    IAuditProjectUnusual auditProjectUnusual;
    
    @Autowired
    ISendMQMessage sendMQMessageService;
    @Autowired
    ICodeMainService codeMainService;
    @Autowired
    ICodeItemsService codeItemsService;
    @Override
    public void pageLoad() {
        workitemguid = getRequestParameter("workItemGuid");
        projectguid = getRequestParameter("projectguid");
        taskGuid = getRequestParameter("taskGuid");
        AuditTask auditTask = auditTaskService.getAuditTaskByGuid(taskGuid, true).getResult();
        String fields = " rowguid,taskguid,projectname,applyeruserguid,applyername,areacode,pviguid,handleareacode,currentareacode,taskid,biguid,subappguid ";
        auditProject = auditProjectService.getAuditProjectByRowGuid(fields, projectguid, auditTask.getAreacode())
                .getResult();
    }

    /**
     * 暂停确认操作
     * 
     */
    public void add() {
    	 log.info("开始暂停记录");
        //保存处理过办件的区域编码(不重复保存)
        String handleAreaCode = ZwfwUserSession.getInstance().getAreaCode();
        String areaStr = "";
        if(StringUtil.isBlank(auditProject.getHandleareacode())){
            areaStr = handleAreaCode+",";
        }else if((auditProject.getHandleareacode().indexOf(handleAreaCode+",")<0)){
            areaStr = auditProject.getHandleareacode() + handleAreaCode + ",";
        }
        if(StringUtil.isNotBlank(areaStr)){
            auditProject.setHandleareacode(areaStr);
        }    
        //TODO 需要赋值的
        String messageItemGuid = getRequestParameter("messageItemGuid");
        String unusualGuid = handleProjectService.handlePause(auditProject, userSession.getDisplayName(),
                userSession.getUserGuid(), workitemguid, messageItemGuid,note).getResult();        
        log.info("暂停unusualGuid："+unusualGuid);
        if (StringUtil.isNotBlank(unusualGuid)) {
          //原先未存暂停原因和备注，本次方法是直接做修改
            AuditProjectUnusual unusual = auditProjectUnusual.getAuditProjectUnusualByRowguid(unusualGuid).getResult();
            if (StringUtil.isNotBlank(pauseReason)) {     
                unusual.setPauseReason(pauseReason);
            }
            if (StringUtil.isNotBlank(note)) {
                unusual.setNote(note);
            }
            auditProjectUnusual.updateProjectUnusual(unusual);
            
            String msg = "handleSpecial:" + projectguid + ";" + auditProject.getApplyername() + ";" + unusualGuid
                    + "&handleSpecialResult:" + projectguid + ";" + unusualGuid;

            ProducerMQ.sendByExchange("receiveproject", msg, "");
            
            //接办分离  特殊操作
            msg = projectguid + "." + auditProject.getApplyername() + "." + unusualGuid;
            sendMQMessageService.sendByExchange("exchange_handle", msg, "project."
                    + ZwfwUserSession.getInstance().getAreaCode() + ".special." + auditProject.getTask_id());
          //接办分离 特殊操作结果
            msg =  projectguid + "." + unusualGuid;
            
            log.info("工改暂停办件信息推送："+msg);
            sendMQMessageService.sendByExchange("exchange_handle", msg, "project."
                    + ZwfwUserSession.getInstance().getAreaCode() + ".specialresult." + auditProject.getTaskid());

        }
        addCallbackParam("message", "保存成功！");
    }

    public String valueChanged(){
        String text = getRequestParameter("text");
        CodeItems c = codeItemsService.getCodeItemByCodeName("项目暂停原因",text);
        return  c.getDmAbr1();
    }
    
    @SuppressWarnings("unchecked")
    public List<SelectItem> getReasonModel() {
        if (reasonModel == null) {
            reasonModel = DataUtil.convertMap2ComboBox(
                    (List<Map<String, String>>) CodeModalFactory.factory("下拉列表", "项目暂停原因", null, false));
        }
        return this.reasonModel;
    }

    public String getPauseReason() {
        return pauseReason;
    }

    public void setPauseReason(String pauseReason) {
        this.pauseReason = pauseReason;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
