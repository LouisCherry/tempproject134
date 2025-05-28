package com.epoint.audittask.docs.action;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.docs.domain.AuditTaskDoc;
import com.epoint.basic.audittask.docs.inter.IAuditTaskDoc;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.controller.BaseController;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditresource.handletask.inter.IHandleTask;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.utils.memory.EHCacheUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;

/**
 * 事项文书配置list页面对应的后台
 * 
 * @author WeiY
 * @version [版本号, 2016-10-09 11:44:42]
 */
@RestController("jnaudittaskdoclistaction")
@Scope("request")
public class JNAuditTaskDocListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    @Autowired
    private IAuditTaskExtension auditTaskExtensionImpl ;
    
    @Autowired
    private IAuditTaskDoc auditTaskDocImpl ;
    
    @Autowired
    private IAttachService  attachServiceImpl ;
    
    @Autowired
    private ISendMQMessage sendMQMessageService;

    /**
     * 事项文书配置实体对象
     */
    private AuditTaskDoc dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditTaskDoc> model;
    /**
     * 事件guid
     */
    private String taskGuid = "";
    /**
     * 复制事项guid
     */
    private String copyTaskGuid;
    /**
     * 事项对象
     */
    private AuditTask auditTask;
    /**
     * 事项扩展
     */
    private AuditTaskExtension auditTaskExtension;
    /**
     * 事项id
     */
    private String taskId;
    /**
     * 操作
     */
    private String operation;
    @Autowired
    private IHandleConfig handleConfigService;
    @Autowired
    private IAttachService iAttachService;
    
    @Autowired
    private IHandleTask handleTaskService;
    
    @Autowired
    private IAuditTask auditTaskServie;

    @Override
    public void pageLoad() {
        taskGuid = this.getRequestParameter("taskguid");
        copyTaskGuid = this.getRequestParameter("copyTaskGuid");
        taskId = this.getRequestParameter("taskId");
        // 获取操作
        operation = this.getRequestParameter("operation");
        // 时间戳为空，则说明是用新增页面跳转过来的
        if (StringUtil.isNotBlank(copyTaskGuid)) {
            // windowCopy直接从库里面获取，没有存入零时缓存
            if ("windowCopy".equals(operation) || "copy".equals(operation)) {
                // 事项新增从数据库获取
                auditTask = auditTaskServie.getAuditTaskByGuid(copyTaskGuid, false).getResult();
                auditTaskExtension = auditTaskExtensionImpl.getTaskExtensionByTaskGuid(copyTaskGuid,false).getResult();
            }
            else {
                auditTask = auditTaskServie.getAuditTaskByGuid(copyTaskGuid, false).getResult();
                auditTaskExtension = auditTaskExtensionImpl.getTaskExtensionByTaskGuid(copyTaskGuid,false).getResult();
            }
        }
        else {
            // 事项新增从数据库获取
            auditTask = auditTaskServie.getAuditTaskByGuid(taskGuid, false).getResult();
            auditTaskExtension = auditTaskExtensionImpl.getTaskExtensionByTaskGuid(taskGuid,false).getResult();

        }
        AuditTask Task = auditTaskServie.getAuditTaskByGuid(taskGuid, false).getResult();
    	if(StringUtil.isNotBlank(Task)){
    		String unid=Task.get("unid");
    		//system.out.println(unid);
    		auditTask.set("unid", Task.get("unid"));
    	}
        String isword = handleConfigService.getFrameConfig("AS_DOC_WORD", ZwfwUserSession.getInstance().getCenterGuid())
                .getResult();
        addCallbackParam("isword", ZwfwConstant.CONSTANT_STR_ZERO.equals(isword) ? "0" : "1");
    }

    /**
     * 删除选定 删除时，
     * 会并联删除与AUDIT_TASK_DOC的rowguid查询到temgcliengguid并关联到Frame_AttachStorage和Frame_AttachStorage的cliengguid的记录
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            // 不存在会报错
            // attachService.deleteAttachByGuid(auditTaskDoc.getTempcliengguid());
            auditTaskDocImpl.deleteAuditTaskDoc("rowguid", sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    /**
     * 
     * 审核通过操作
     * 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void passAudit() {
        String msg = "";
        AuditCommonResult<String> result;
		String syncTaskGuid = auditTask.getRowguid();
		int isNeedNewVersion = auditTask.getIsneednewversion() == null ? 0 : auditTask.getIsneednewversion();
		boolean flag = isNeedNewVersion == 1
				|| auditTaskServie.judgeNewVersionByBean(EHCacheUtil.get(ZwfwConstant.VERSION_CONTROL_FIELD_CACHEFLAG),
						taskGuid, auditTask.getRowguid(), auditTask, auditTaskExtension).getResult();
		if(!flag){
			syncTaskGuid = taskGuid;
		}
        if (StringUtil.isNotBlank(auditTask.getVersion())) {
            // 从缓存里获取存储标志的map
        	result = handleTaskService.passTask(flag, taskGuid, taskId, auditTask, auditTaskExtension, userSession.getDisplayName(), userSession.getUserGuid());
        }
        else {
        	result = handleTaskService.passTask(flag, taskGuid, taskId, auditTask, auditTaskExtension, userSession.getDisplayName(), userSession.getUserGuid());
        }
        if (!result.isSystemCode()) {
			msg = "处理失败！";
		} else {
			msg = result.getResult();
			syncWindowTask("modify", syncTaskGuid);
		}
        this.addCallbackParam("msg", msg);
    }

    /**
     * 
     * 审核不通过操作
     * 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void notPassAudit() {
        String msg = "";
        // 调用存储过程审核通过
        msg = handleTaskService.notpassAuditTask(auditTask.getRowguid()).getResult();
        this.addCallbackParam("msg", msg);
    }

    public DataGridModel<AuditTaskDoc> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditTaskDoc>()
            {

                @Override
                public List<AuditTaskDoc> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();                   
                    if (StringUtil.isNotBlank(copyTaskGuid)) {
                        sql.eq("taskguid", copyTaskGuid);
                        if ("edit".equals(operation) || "windowChange".equals(operation)
                                || "windowReportEdit".equals(operation) || "inaudit".equals(operation)) {
                            // auditTaskDocService.putFlagToVersionMap(taskGuid,
                            // copyTaskGuid, timestamp, operation);
                        }
                    }
                    else {
                        sql.eq("taskguid", taskGuid);
                    }
                    PageData<AuditTaskDoc> pageData = getPageData(sql.getMap(), first, pageSize,
                            sortField, sortOrder);
                    this.setRowCount(pageData.getRowCount());
                    List<AuditTaskDoc> auditTaskDocs = pageData.getList();
                    for (AuditTaskDoc auditTaskDoc : auditTaskDocs) {
                        if (StringUtil.isNotBlank(auditTaskDoc.getTempcliengguid())) {
                            List<FrameAttachInfo> frameAttachInfos = iAttachService
                                    .getAttachInfoListByGuid(auditTaskDoc.getTempcliengguid());
                            if (frameAttachInfos != null && frameAttachInfos.size() > 0) {
                                FrameAttachInfo frameAttachInfo = frameAttachInfos.get(0);
                                auditTaskDoc.set("attachGuid", frameAttachInfo.getAttachGuid());
                                auditTaskDoc.set("attachFileName", frameAttachInfo.getAttachFileName());
                            }
                        }
                    }
                    return auditTaskDocs;
                }

            };
        }
        return model;
    }

    /**
     * 点击提交按钮触发
     * 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void submit() {
        String msg = "";
        String syncTaskGuid = auditTask.getRowguid();
        AuditCommonResult<String> result;
        // 新增时候点击提交
        if (StringUtil.isBlank(copyTaskGuid) || "copy".equals(operation)) {
        	result = handleTaskService.submitNewTask(auditTask, auditTaskExtension, operation, ZwfwUserSession.getInstance().getCenterGuid());
        }
        else {
            if ("confirmEdit".equals(operation)) {
            	result = handleTaskService.submitTask(true, taskGuid, auditTask, auditTaskExtension, userSession.getDisplayName(), userSession.getUserGuid());
            }
            else {
            	boolean taskversion =true;
				String as_notaskversion = handleConfigService.getFrameConfig("AS_NOTASKVERSION", "").getResult();
				if(ZwfwConstant.CONSTANT_STR_ONE.equals(as_notaskversion)){
					taskversion=false;
				}
            	int isNeedNewVersion = auditTask.getIsneednewversion() == null ? 0 : auditTask.getIsneednewversion();
				boolean flag =taskversion &&( isNeedNewVersion == 1
						|| auditTaskServie
								.judgeNewVersionByBean(EHCacheUtil.get(ZwfwConstant.VERSION_CONTROL_FIELD_CACHEFLAG),
										taskGuid, auditTask.getRowguid(), auditTask, auditTaskExtension)
								.getResult());

            	result = handleTaskService.submitTask(flag, taskGuid, auditTask, auditTaskExtension, userSession.getDisplayName(), userSession.getUserGuid());
            	if(!flag){
					syncTaskGuid = taskGuid;
				}
            }
        }
        if (!result.isSystemCode()) {
			msg = "处理失败！";
		} else {
			msg = result.getResult();
			
			if(msg.equals("")){
			    msg="保存成功！";
			}
			syncWindowTask("modify", syncTaskGuid);
		}
        this.addCallbackParam("msg", msg);
    }

    /**
     * 点击上报按钮触发
     * 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void report() {
    	String msg = "";
  	  	// 判断事项编码是否正确
        if (StringUtil.isBlank(copyTaskGuid) || taskGuid.equals(copyTaskGuid)) {
            Map<Object, Object> map = handleTaskService.judgeItemId(auditTask.getItem_id(),
                    StringUtil.isBlank(copyTaskGuid) ? taskGuid : copyTaskGuid, ZwfwUserSession.getInstance().getCenterGuid()).getResult();
            msg = (String) map.get("msg");
            if (StringUtil.isNotBlank(msg)) {
                this.addCallbackParam("msg", msg);
                return;
            }
        }
        //上报事项
        msg = handleTaskService.report(auditTask, auditTaskExtension).getResult();
        this.addCallbackParam("msg", msg);
    }
    
    /**
	 * 
	 * 同步事项到窗口
	 * 
	 * @param SendType
	 *            事项消息发送类型 消息类型有enable、insert、modify、delete
	 * @param RabbitMQMsg
	 *            事项id
	 */
	public void syncWindowTask(String SendType, String taskGuid) {
		// TODO 事项变更之后需要使用通知的方式来处理，不能直接进行更新
		// 2017_4_7 CH 事项变更以后发送消息至RabbitMQ队列
		try {
/*			String RabbitMQMsg = "handleSerachIndex:" + SendType + ";" + taskGuid
					+ "#" + "handleCenterTask:" + SendType + ";" + taskGuid + 
					"/com.epoint.auditjob.rabbitmqhandle.MQHandleCenterTask";
			ProducerMQ.TopicPublish("auditTask", SendType, RabbitMQMsg, true);*/
            String RabbitMQMsg = SendType + ";" + taskGuid ;
            sendMQMessageService.sendByExchange("znsb_exchange_handle", RabbitMQMsg, "task."+ZwfwUserSession.getInstance().getAreaCode()+"."+SendType);

        }
        catch (Exception e) {
			e.printStackTrace();
		}
	}

    public AuditTaskDoc getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditTaskDoc();
        }
        return dataBean;
    }

    public void setDataBean(AuditTaskDoc dataBean) {
        this.dataBean = dataBean;
    }
    
    public PageData<AuditTaskDoc> getPageData(Map<String, String> conditionMap, Integer first, Integer pageSize,
            String sortField, String sortOrder) {
        PageData<AuditTaskDoc> pageData =  auditTaskDocImpl
                .getAuditTaskDocPageData(conditionMap, first, pageSize, sortField, sortOrder).getResult();
        for (AuditTaskDoc auditTaskDoc : pageData.getList()) {
            auditTaskDoc.put("wsmbName", this.getTempUrl(auditTaskDoc.getTempcliengguid()));
        }
        return pageData;
    }
    
    public String getTempUrl(String cliengguid) {
        String wsmbName = "";
        if (StringUtil.isNotBlank(cliengguid) && cliengguid != null) {
            List<FrameAttachInfo> attachInfos = attachServiceImpl.getAttachInfoListByGuid(cliengguid);
            // 有附件
            if (attachInfos != null && attachInfos.size()>0) {
                String strURL = "onclick=\"goToAttach('" + attachInfos.get(0).getAttachGuid() + "')\"";
                wsmbName += "<a style=\"color:blue;text-decoration:underline\" href=\"javascript:void(0)\" " + strURL
                        + ">" + attachInfos.get(0).getAttachFileName() + "</a>&nbsp;&nbsp;";
            }
            else {
                wsmbName = "无模板！";
            }
        }
        else {
            wsmbName = "无模板！";
        }
        return wsmbName;
    }

}
