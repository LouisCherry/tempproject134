package com.epoint.audittask.fee.action;

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
import com.epoint.basic.audittask.fee.domain.AuditTaskChargeItem;
import com.epoint.basic.audittask.fee.inter.IAuditTaskChargeItem;
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
 * 事项收费项目list页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2016-10-09 16:08:02]
 */
@RestController("jnaudittaskchargeitemlistaction")
@Scope("request")
public class JNAuditTaskChargeItemListAction extends BaseController
{

    /**
     * 
     */
    private static final long serialVersionUID = -5110950212722431279L;
    
    
    @Autowired
    private IAuditTaskExtension auditTaskExtensionImpl;
    
    @Autowired
    private IAuditTaskChargeItem auditChargeItemImpl;
    
    @Autowired
    private IAuditTaskChargeItem atci;
    /**
     * 事项收费项目实体对象
     */
    private AuditTaskChargeItem dataBean;

    /**
     * 表格控件model
     */
    private DataGridModel<AuditTaskChargeItem> model;

    /**
     * 导出模型
     */
    private ExportModel exportModel;
    /**
     * 事项guid
     */
    private String taskGuid = "";
    /**
     * 拷贝过来的taskGuid
     */
    private String copyTaskGuid = "";
    /**
     * 事项
     */
    private AuditTask auditTask;
    /**
     * 事项扩展信息
     */
    private AuditTaskExtension auditTaskExtension;
    /**
     * 事项id
     */
    private String taskId;
    /**
     * 当前版本taskguid
     */
    private String currentTaskGuid;
    /**
     * 操作
     */
    private String operation;
    
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
        taskGuid = this.getRequestParameter("taskguid");
        copyTaskGuid = this.getRequestParameter("copyTaskGuid");
        taskId = this.getRequestParameter("taskId");
        operation = this.getRequestParameter("operation");
        currentTaskGuid = this.getViewData("currentTaskGuid");
        if (StringUtil.isBlank(currentTaskGuid)) {
            currentTaskGuid = this.getRequestParameter("currentGuid");
        }
        //时间戳为空，则说明是用新增页面跳转过来的
        if (StringUtil.isNotBlank(copyTaskGuid)) {
            if ("windowCopy".equals(operation) || "copy".equals(operation)) {
                //事项新增从数据库获取
                auditTask = auditTaskServie.getAuditTaskByGuid(copyTaskGuid, false).getResult();
                auditTaskExtension = auditTaskExtensionImpl.getTaskExtensionByTaskGuid(copyTaskGuid,false).getResult();
                        //auditTaskExtensionBizlogic.getTaskExtensionByTaskGuid(copyTaskGuid, false);
            }
            else {
                auditTask = auditTaskServie.getAuditTaskByGuid(copyTaskGuid, false).getResult();
                auditTaskExtension = auditTaskExtensionImpl.getTaskExtensionByTaskGuid(copyTaskGuid,false).getResult();
            }
        }
        else {
            //事项新增从数据库获取
            auditTask = auditTaskServie.getAuditTaskByGuid(taskGuid, false).getResult();
            auditTaskExtension = auditTaskExtensionImpl.getTaskExtensionByTaskGuid(taskGuid,false).getResult();

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
            auditChargeItemImpl.deleteAuditTaskChargeItem("rowguid", sel);
        }
        addCallbackParam("msg", "成功删除！");
    }

    public DataGridModel<AuditTaskChargeItem> getDataGridData() {
        // 获得表格对象
        if (model == null) {
            model = new DataGridModel<AuditTaskChargeItem>()
            {

                @Override
                public List<AuditTaskChargeItem> fetchData(int first, int pageSize, String sortField,
                        String sortOrder) {
                    SqlConditionUtil sql = new SqlConditionUtil();
                    //是编辑的
                    if (StringUtil.isNotBlank(copyTaskGuid)) {
                        sql.eq("TaskGuid", copyTaskGuid);
                        if ("edit".equals(operation) || "windowChange".equals(operation)
                                || "windowReportEdit".equals(operation) || "inaudit".equals(operation)) {

                        }
                    }
                    else if (StringUtil.isNotBlank(currentTaskGuid)) {
                        sql.eq("TaskGuid", currentTaskGuid);
                    }
                    else {
                        sql.eq("TaskGuid", taskGuid);
                    }
                    PageData<AuditTaskChargeItem> pageData = auditChargeItemImpl
                            .getAuditTaskChargeItemPageData(sql.getMap(), first, pageSize, sortField, sortOrder).getResult();
                    this.setRowCount(pageData.getRowCount());
                    return pageData.getList();
                }

            };
        }
        return model;
    }

    /**
     * 
     *  把当前的guid和比较的guid存入view
     *  @param currentTaskGuid
     *  @param compareTaskGuid    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void addTaskGuidToView(String currentTaskGuid, String compareTaskGuid) {
        this.addViewData("currentTaskGuid", currentTaskGuid);
        this.addViewData("compareTaskGuid", compareTaskGuid);
    }

    /**
     * 
     * 审核通过操作   
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void passAudit() {
        String msg = "";
        AuditCommonResult<String> result;
		String syncTaskGuid = auditTask.getRowguid();
		int isNeedNewVersion = auditTask.getIsneednewversion() == null ? 0 : 1;
		boolean flag = isNeedNewVersion == 1
				|| auditTaskServie.judgeNewVersionByBean(EHCacheUtil.get(ZwfwConstant.VERSION_CONTROL_FIELD_CACHEFLAG),
						taskGuid, auditTask.getRowguid(), auditTask, auditTaskExtension).getResult();
		if(!flag){
			syncTaskGuid = taskGuid;
		}
        if (StringUtil.isNotBlank(auditTask.getVersion())) {
            //从缓存里获取存储标志的map
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
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void notPassAudit() {
        String msg = "";
        //调用存储过程审核通过
        msg = handleTaskService.notpassAuditTask(auditTask.getRowguid()).getResult();
        this.addCallbackParam("msg", msg);
    }

    public AuditTaskChargeItem getDataBean() {
        if (dataBean == null) {
            dataBean = new AuditTaskChargeItem();
        }
        return dataBean;
    }

    public void setDataBean(AuditTaskChargeItem dataBean) {
        this.dataBean = dataBean;
    }

    public ExportModel getExportModel() {
        if (exportModel == null) {
            exportModel = new ExportModel("", "");
        }
        return exportModel;
    }

    /**
     * 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void saveInfo() {
        List<AuditTaskChargeItem> audittaskchargeList = getDataGridData().getWrappedData();
        for (AuditTaskChargeItem audittaskcharge : audittaskchargeList) {
            atci.updateAuditTaskChargeItem(audittaskcharge);
        }
        addCallbackParam("msg", "保存成功！");
    }

    /**
     * 
     *  点击下一步或者上一步触发，用来判断是否生成了新版本。标志位是否为true 
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void update() {

    }

    /**
     * 
     *  点击提交按钮触发  
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void submit() {
        String msg = "";
        String syncTaskGuid = auditTask.getRowguid();
        AuditCommonResult<String> result;
        //新增时候点击提交
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
				boolean flag =taskversion && (isNeedNewVersion == 1
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
}
