package com.epoint.audittask.material.action;

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
import com.epoint.basic.audittask.material.domain.AuditTaskCase;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskCase;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterialCase;
import com.epoint.basic.controller.BaseController;
import com.epoint.basic.faces.export.ExportModel;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.composite.auditresource.handletask.inter.IHandleTask;
import com.epoint.core.dto.model.DataGridModel;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.memory.EHCacheUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;

/**
 * 事项材料表list页面对应的后台
 * 
 * @author Administrator
 * @version [版本号, 2016-10-08 15:54:42]
 */
@RestController("jnaudittaskmateriallistaction")
@Scope("request")
public class JNAuditTaskMaterialListAction extends BaseController {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7946493408743739342L;
	@Autowired
	private IAuditTaskExtension auditTaskExtensionImpl;
	/**
	 * 事项材料表实体对象
	 */
	private AuditTaskMaterial dataBean;
	/**
	 * 事项
	 */
	private AuditTask auditTask;
	/**
	 * 事项扩展信息
	 */
	private AuditTaskExtension auditTaskExtension;
	/**
	 * 表格控件model
	 */
	private DataGridModel<AuditTaskMaterial> model;

	/**
	 * 导出模型
	 */
	private ExportModel exportModel;
	/**
	 * 事项标识
	 */
	private String taskGuid = "";
	/**
	 * 拷贝事项taskguid
	 */
	private String copyTaskGuid;
	/**
	 * 事项同步service
	 */
	// private AuditTaskSyncService auditTaskSyncService = new
	// AuditTaskSyncService();

	/**
	 * 事项id
	 */
	private String taskId;
	/**
	 * 当前版本guid
	 */
	private String currentTaskGuid = "";
	/**
	 * 操作
	 */
	private String operation = "";
	/**
	 * 事项数据库操作实现
	 */
	
	@Autowired
	private IAuditTask auditTaskBasicImpl;
	
	@Autowired
	private IAuditTaskCase auditTaskCaseImpl;

	@Autowired
	private IAttachService attachService;

	@Autowired
	private IAuditTaskMaterialCase auditTaskMaterialCaseService;

	@Autowired
	private IHandleTask handleTaskService;

	@Autowired
	private IAuditTask auditTaskServie;
	@Autowired
	private IAuditTaskMaterial auditTaskMaterialImpl;
	
	@Autowired
    private IHandleConfig handleConfigService;

    @Autowired
    private ISendMQMessage sendMQMessageService;
	
	@Override
	public void pageLoad() {
		// 获取taskguid
		taskGuid = this.getRequestParameter("taskGuid");
		// 获取复制taskGuid
		copyTaskGuid = this.getRequestParameter("copyTaskGuid");
		// 获取taskId
		taskId = this.getRequestParameter("taskId");
		// 获取操作
		operation = this.getRequestParameter("operation");
		// 获取当前事项guid
		currentTaskGuid = this.getViewData("currentTaskGuid");
		if (StringUtil.isBlank(currentTaskGuid)) {
			currentTaskGuid = this.getRequestParameter("currentGuid");
		}
		if (StringUtil.isNotBlank(taskGuid)) {
			// 时间戳为空，则说明是用新增页面跳转过来的
			if (StringUtil.isNotBlank(copyTaskGuid)) {
				auditTask = auditTaskBasicImpl.getAuditTaskByGuid(copyTaskGuid, false).getResult();
				auditTaskExtension = auditTaskExtensionImpl.getTaskExtensionByTaskGuid(copyTaskGuid, false).getResult();
			} else {
				// 事项新增从数据库获取
				auditTask = auditTaskBasicImpl.getAuditTaskByGuid(taskGuid, false).getResult();
				auditTaskExtension = auditTaskExtensionImpl.getTaskExtensionByTaskGuid(taskGuid, false).getResult();
			}
		}
        AuditTask Task = auditTaskBasicImpl.getAuditTaskByGuid(taskGuid, false).getResult();
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
			auditTaskMaterialImpl
					.deleteAuditTaskMaterial(auditTaskMaterialImpl.getAuditTaskMaterialByRowguid(sel).getResult());
			auditTaskMaterialCaseService.deleteTaskMaterialCaseByMaterialGuid(sel);
		}
		addCallbackParam("msg", "成功删除！");

	}

	/**
	 * 保存排序
	 * 
	 * @exception/throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public void saveInfo() {
		List<AuditTaskMaterial> auditTaskMaterialList = this.getDataGridData().getWrappedData();
		String msg = "排序成功！";
		for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterialList) {
			if (Integer.parseInt(auditTaskMaterial.getOrdernum().toString()) < 0) {
				msg = "排序不能为负数！";
				break;
			}
		}
		if ("排序成功！".equals(msg)) {
			for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterialList) {
				auditTaskMaterial.keep("rowguid", "ordernum");
				auditTaskMaterialImpl.updateAuditTaskMaterial(auditTaskMaterial);
			}
		}
		addCallbackParam("msg", msg);
	}

	/**
	 * 意见添加所有情形
	 */
	public void addAllCase() {
		AuditTask auditTask = auditTaskBasicImpl.getAuditTaskByGuid(taskGuid, false).getResult();
		List<String> indexs = getDataGridData().getSelectKeys();
		if (indexs != null && !indexs.isEmpty()) {
			for (String index : indexs) {
				auditTaskCaseImpl.addMaterialToAllCase(auditTask, index);
			}
		}
		addCallbackParam("msg", "添加成功！");
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

	public DataGridModel<AuditTaskMaterial> getDataGridData() {
		// 获得表格对象
		if (model == null) {
			model = new DataGridModel<AuditTaskMaterial>() {

				@Override
				public List<AuditTaskMaterial> fetchData(int first, int pageSize, String sortField, String sortOrder) {
					SqlConditionUtil sql = new SqlConditionUtil();
					if (StringUtil.isNotBlank(copyTaskGuid)) {
						sql.eq("TASKGUID", copyTaskGuid);
						taskGuid = copyTaskGuid;
						if ("edit".equals(operation) || "windowChange".equals(operation)
								|| "windowReportEdit".equals(operation) || "inaudit".equals(operation)) {
						}
					} else if (StringUtil.isNotBlank(currentTaskGuid)) {
						taskGuid = currentTaskGuid;
						sql.eq("TASKGUID", currentTaskGuid);
					} else {
						sql.eq("TASKGUID", taskGuid);
					}
					PageData<AuditTaskMaterial> pageData = auditTaskMaterialImpl
							.getAuditTaskMaterialPageData(sql.getMap(), first, pageSize, sortField, sortOrder)
							.getResult();
					// 判断是否存在情形
					String is_Exit_Case =ZwfwConstant.CONSTANT_STR_ZERO;
					List<AuditTaskCase> auditTaskCases = auditTaskCaseImpl.selectTaskCaseByTaskGuid(taskGuid)
							.getResult();
					if (auditTaskCases != null && auditTaskCases.size() > 0) {
						is_Exit_Case = ZwfwConstant.CONSTANT_STR_ONE;
					}
					addCallbackParam("is_Exit_Case", is_Exit_Case);
					for (AuditTaskMaterial auditTaskMaterial : pageData.getList()) {
						if (StringUtil.isNotBlank(auditTaskMaterial.getExampleattachguid())) {
							List<FrameAttachInfo> frameAttachInfos = attachService
									.getAttachInfoListByGuid(auditTaskMaterial.getExampleattachguid());
							if (frameAttachInfos != null && frameAttachInfos.size() > 0) {
								auditTaskMaterial.setExampleattachguid(frameAttachInfos.get(0).getAttachGuid());
							} else {
								auditTaskMaterial.setExampleattachguid("");
							}
						}

						if (StringUtil.isNotBlank(auditTaskMaterial.getTemplateattachguid())) {
							List<FrameAttachInfo> frameAttachInfos = attachService
									.getAttachInfoListByGuid(auditTaskMaterial.getTemplateattachguid());
							if (frameAttachInfos != null && frameAttachInfos.size() > 0) {
								auditTaskMaterial.setTemplateattachguid(frameAttachInfos.get(0).getAttachGuid());
							} else {
								auditTaskMaterial.setTemplateattachguid("");
							}
						}
						// 根据事项材料查询情形名称
						if (ZwfwConstant.CONSTANT_STR_ONE.equals(is_Exit_Case)) {
							List<Record> auditTaskCaseMaterials = auditTaskMaterialImpl
									.getMaterialCaseNameList(taskGuid, auditTaskMaterial.getRowguid()).getResult();
							String caseName = "";

							for (Record r : auditTaskCaseMaterials) {
								caseName += r.get("casename") + "<br/>";
							}
							auditTaskMaterial.put("caseName", caseName);
						}
					}
					this.setRowCount(pageData.getRowCount());
					return pageData.getList();
				}

			};
		}
		return model;
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
		if (StringUtil.isNotBlank(copyTaskGuid)) {
			boolean taskversion =true;
			String as_notaskversion = handleConfigService.getFrameConfig("AS_NOTASKVERSION", "").getResult();
			if(ZwfwConstant.CONSTANT_STR_ONE.equals(as_notaskversion)){
				taskversion=false;
			}
			int isNeedNewVersion = auditTask.getIsneednewversion() == null ? 0 : auditTask.getIsneednewversion();
			boolean flag = taskversion && (isNeedNewVersion == 1 || auditTaskServie
					.judgeNewVersionByBean(EHCacheUtil.get(ZwfwConstant.VERSION_CONTROL_FIELD_CACHEFLAG), taskGuid,
							auditTask.getRowguid(), auditTask, auditTaskExtension)
					.getResult());
			
			result = handleTaskService.submitTask(flag, taskGuid, auditTask, auditTaskExtension,
					userSession.getDisplayName(), userSession.getUserGuid());
			if (!flag) {
				syncTaskGuid = taskGuid;
			}
		} else {
			if ("confirmEdit".equals(operation)) {
				result = handleTaskService.submitTask(true, taskGuid, auditTask, auditTaskExtension,
						userSession.getDisplayName(), userSession.getUserGuid());
			} else {
				// 新增页面提交操作
				result = handleTaskService.submitNewTask(auditTask, auditTaskExtension, operation,
						ZwfwUserSession.getInstance().getCenterGuid());
			}
		}
		if (!result.isSystemCode()) {
			msg = "处理失败！";
		} else {
			msg = result.getResult();
			syncWindowTask("modify", syncTaskGuid);
		}
		addCallbackParam("msg", msg);
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
		if (!flag) {
			syncTaskGuid = taskGuid;
		}
		if (StringUtil.isNotBlank(auditTask.getVersion())) {
			// 从缓存里获取存储标志的map
			result = handleTaskService.passTask(flag, copyTaskGuid, taskId, auditTask, auditTaskExtension,
					userSession.getDisplayName(), userSession.getUserGuid());
		} else {
			result = handleTaskService.passTask(flag, copyTaskGuid, taskId, auditTask, auditTaskExtension,
					userSession.getDisplayName(), userSession.getUserGuid());
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
			Map<Object, Object> map = handleTaskService
					.judgeItemId(auditTask.getItem_id(), StringUtil.isBlank(copyTaskGuid) ? taskGuid : copyTaskGuid,
							ZwfwUserSession.getInstance().getCenterGuid())
					.getResult();
			msg = (String) map.get("msg");
			if (StringUtil.isNotBlank(msg)) {
				this.addCallbackParam("msg", msg);
				return;
			}
		}
		// 上报事项
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
/*			String RabbitMQMsg = "handleSerachIndex:" + SendType + ";" + taskGuid + "#" + "handleCenterTask:" + SendType
					+ ";" + taskGuid + "/com.epoint.auditjob.rabbitmqhandle.MQHandleCenterTask";
			ProducerMQ.TopicPublish("auditTask", SendType, RabbitMQMsg, true);*/
		    String RabbitMQMsg = SendType + ";" + taskGuid ;
            sendMQMessageService.sendByExchange("znsb_exchange_handle", RabbitMQMsg, "task."+ZwfwUserSession.getInstance().getAreaCode()+"."+SendType);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AuditTaskMaterial getDataBean() {
		if (dataBean == null) {
			dataBean = new AuditTaskMaterial();
		}
		return dataBean;
	}

	public void setDataBean(AuditTaskMaterial dataBean) {
		this.dataBean = dataBean;
	}

	public ExportModel getExportModel() {
		if (exportModel == null) {
			exportModel = new ExportModel("", "");
		}
		return exportModel;
	}

}
