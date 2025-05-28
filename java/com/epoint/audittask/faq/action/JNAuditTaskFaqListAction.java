package com.epoint.audittask.faq.action;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import com.epoint.auditmq.sendMessage.api.ISendMQMessage;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.faq.domain.AuditTaskFaq;
import com.epoint.basic.audittask.faq.inter.IAuditTaskFaq;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
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

/**
 * 事项的常见问题list页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2016-10-09 10:04:47]
 */
@RestController("jnaudittaskfaqlistaction")
@Scope("request")
public class JNAuditTaskFaqListAction extends BaseController
{
    /**
     * 
     */
    private static final long serialVersionUID = 6091857162729835401L;
    
    @Autowired
    private IAuditTaskExtension auditTaskExtensionImpl ;
    
    @Autowired
    private IAuditTaskFaq auditTaskFaqImpl ;

    @Autowired
    private IAuditTaskFaq audittaskfaqImpl;
    /**
     * 事项id
     */
    private String taskId = "";
    /**
     * 事项guid
     */
    private String taskGuid = "";
    /**
     * 事项的常见问题实体对象
     */
    private AuditTaskFaq dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditTaskFaq> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;

    /**
     * 事项
     */
    private AuditTask auditTask;
    /**
     * 事项扩展信息
     */
    private AuditTaskExtension auditTaskExtension;
    /**
     * 操作
     */
    private String operation;
    /**
     * 复制事项guid
     */
    private String copyTaskGuid;
    /**
     * 问题，搜索用
     */
    private String question;
    
    @Autowired
    private IHandleTask handleTaskService;
    
    @Autowired
    private IAuditTask auditTaskServie;
    
    @Autowired
    private IHandleConfig handleConfigService;
    @Autowired
    private ISendMQMessage sendMQMessageService;
    @Override
    public void pageLoad() {
        // 获取taskguid
        taskId = this.getRequestParameter("taskId");
        taskGuid = this.getRequestParameter("taskGuid");
        operation = this.getRequestParameter("operation");
        copyTaskGuid = this.getRequestParameter("copyTaskGuid");
        if(StringUtil.isNotBlank(taskGuid)){
            auditTask = auditTaskServie.getAuditTaskByGuid(taskGuid, false).getResult();
            taskId = auditTask.getTask_id();
        }
        if (StringUtil.isNotBlank(taskId)) {
            // 获取前台传来的时间戳
            // 时间戳为空，则说明是用新增页面跳转过来的
            if (StringUtil.isBlank(copyTaskGuid)) {
                auditTask = auditTaskServie.getAuditTaskByGuid(taskGuid, false).getResult();
                auditTaskExtension = auditTaskExtensionImpl.getTaskExtensionByTaskGuid(taskGuid,false).getResult();
                // 时间戳不为空，则说明是变更事项操作
            }
            else {
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
        }
        AuditTask Task = auditTaskServie.getAuditTaskByGuid(taskGuid, false).getResult();
    	if(StringUtil.isNotBlank(Task)){
    		String unid=Task.get("unid");
    		//system.out.println(unid);
    		auditTask.set("unid", Task.get("unid"));
    	}
    }

    /**
     * 删除选定
     * 
     */
    public void deleteSelect() {
        List<String> select = getDataGridData().getSelectKeys();
        for (String sel : select) {
            auditTaskFaqImpl.deleteAuditTaskFaq("rowguid", sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public DataGridModel<AuditTaskFaq> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditTaskFaq>()
            {

                @Override
                public List<AuditTaskFaq> fetchData(int first, int pageSize, String sortField, String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    if (StringUtil.isNotBlank(question)) {
                        sql.like("question", question);
                    }
                    sql.eq("TaskId", taskId);
                    PageData<AuditTaskFaq> pageData = auditTaskFaqImpl
                            .getAuditTaskFaqPageData(sql.getMap(), first, pageSize, "ordernum", "desc").getResult();
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }

            };
        }
        return model;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * 
     * 把当前的guid和比较的guid存入view
     * 
     * @param currentTaskGuid
     * @param compareTaskGuid
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void addTaskGuidToView(String currentTaskGuid, String compareTaskGuid) {
        this.addViewData("currentTaskGuid", currentTaskGuid);
        this.addViewData("compareTaskGuid", compareTaskGuid);
    }

    /**
     * 
     * 点击提交按钮触发
     * 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void submit() {
        String msg = "";
        String syncTaskGuid = auditTask.getRowguid();
        AuditCommonResult<String> result;
        if (StringUtil.isNotBlank(copyTaskGuid) && !"copy".equals(operation)||"confirmEdit".equals(operation)) {
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
				boolean flag = taskversion&&( isNeedNewVersion == 1
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
        else {
            result = handleTaskService.submitNewTask(auditTask, auditTaskExtension, operation, ZwfwUserSession.getInstance().getCenterGuid());
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

    public AuditTaskFaq getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditTaskFaq();
        }
        return dataBean;
    }

    public void setDataBean(AuditTaskFaq dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

    public void saveInfo() {
        List<AuditTaskFaq> audittaskfaqList = getDataGridData().getWrappedData();
        for (AuditTaskFaq audittaskfaq : audittaskfaqList) {
            audittaskfaqImpl.updateAuditTaskFaq(audittaskfaq);
        }
        addCallbackParam("msg", "保存成功！");
    }

}
