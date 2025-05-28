package com.epoint.auditproject.auditproject.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.workflow.bizlogic.api.WFAPI9;
import com.epoint.workflow.service.common.entity.config.WorkflowActivity;
import com.epoint.workflow.service.config.api.IWorkflowActivityService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
/*import com.epoint.auditproject.api.IAuditProjectApi;*/
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectsparetime.domain.AuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectsparetime.service.AuditProjectSparetimeService;
import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;
import com.epoint.basic.auditproject.auditprojectunusual.inter.IAuditProjectUnusual;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.material.inter.IAuditMaterialcaseCondition;
import com.epoint.basic.authentication.UserSession;
import com.epoint.basic.controller.BaseController;
import com.epoint.composite.auditsp.handleproject.inter.IHandleProject;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.gxhworkflow.api.IGxhWorkflowService;
import com.epoint.workflow.service.common.custom.ProcessVersionInstance;
import com.epoint.workflow.service.common.entity.config.WorkflowActivityOperation;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import com.epoint.workflow.service.config.api.IWorkflowActOperationService;
import com.epoint.workflow.service.config.api.IWorkflowTransitionService;
import com.epoint.workflow.service.core.api.IWFEngineAPI9;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;
import com.itextpdf.text.log.SysoCounter;

@RestController("auditprojectthaction")
@Scope("request")
public class AuditProjectThAction extends BaseController {

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

	private AuditTask auditTask;

	/**
	 * 工作项标识
	 */
	private String workitemguid;

	/**
	 * 办件信息
	 */
	private AuditProject auditProject;

	/**
	 * 退回原因
	 */
	private String reason;

	private String fromactivityguid;

	private static int a=3;

	@Autowired
	IAuditProjectSparetime auditProjectSparetimeService;

	@Autowired
	IAuditProject auditProjectService;

	@Autowired
	IAuditTask auditTaskService;

	@Autowired
	IHandleProject handleProjectService;

	@Autowired
	IGxhWorkflowService iworkservice;

	@Autowired
	IMessagesCenterService messagesCenterService;

	@Autowired
	IWorkflowActOperationService workflowActOperationService;
	@Autowired
	IWFInstanceAPI9 wfinstanceService;
	@Autowired
	IWorkflowTransitionService transitionService;

	private WFAPI9 wfapi = new WFAPI9();

	@Autowired
	private IWorkflowActivityService iWorkflowActivityService;


	/*
	 * @Autowired IAuditProjectApi iAuditProjectApi;
	 */

	@Override
	public void pageLoad() {
		workitemguid = getRequestParameter("workItemGuid");
		projectguid = getRequestParameter("projectguid");
		taskGuid = getRequestParameter("taskGuid");

		auditTask = auditTaskService.getAuditTaskByGuid(taskGuid, true).getResult();
		auditProject = auditProjectService.getAuditProjectByRowGuid(projectguid, auditTask.getAreacode()).getResult();
	}

	/**
	 * 退回操作
	 *
	 */
	public void add() {
		EpointFrameDsManager.begin(null);
		try {
			// 退回操作
			ProcessVersionInstance pvi = wfinstanceService.getProcessVersionInstance(auditProject.getPviguid());
			WorkflowWorkItem item = wfinstanceService.getWorkItem(pvi, workitemguid);// 当前工作项
			log.info("pviguid:"+auditProject.getPviguid());
			log.info("workitemguid:"+workitemguid);
			WorkflowWorkItem firstitem = null;
			// String controlename =
			// iAuditProjectApi.getBtnControlNameByControlID("btnAccept");
			List<Integer> list = new ArrayList<>();
			list.add(80);
			log.info("date:"+ new Date());
//			List<WorkflowWorkItem> itemlist = wfinstanceService.getWorkItemListByPVIGuidAndStatus(pvi, list);
//			log.info("date:"+ new Date());
			auditProjectService.updateProject(auditProject);

//			if (itemlist != null && itemlist.size() > 0) {
//				fromactivityguid = iworkservice.findwork(item.getActivityGuid()).get(0).getFromActivityGuid();
//			} else {
//				addCallbackParam("message", "无法退回！");
//				return;
//			}
			List<WorkflowActivity> workflowActivities = iWorkflowActivityService.selectActivityByToActivityGuid(item.getActivityGuid());
			if(CollectionUtils.isNotEmpty(workflowActivities)){
				fromactivityguid = workflowActivities.get(workflowActivities.size()-1).getActivityGuid();
			}else {
				addCallbackParam("message", "无法退回！");
				return;
			}
			log.info("date:"+ EpointDateUtil.convertDate2String(new Date()));
//		log.info("firstitem:" + firstitem);
			int status = auditProject.getStatus();
//		List<WorkflowActivityOperation> list2 = workflowActOperationService
//				.selectByActivityGuid(fromactivityguid);
//		System.out.println("list2:" + list2);
//		boolean b = false;
//		for (WorkflowActivityOperation wao : list2) {
//			if ("受理".equals(wao.getOperationName())) {
//				b = true;
//				break;
//			}
//		}
//		if (status == 30) {
//			if (!b) {
//				auditProject.setStatus(30);
//			} else {
//				auditProject.setStatus(26);
//			}
//		} else if (status == 50) {
//			auditProject.setStatus(30);
//		}

			switch (status){
				case 30:
					auditProject.setStatus(26);
					break;
				case 50:
					auditProject.setStatus(30);
					break;
			}
			auditProjectService.updateProject(auditProject);
			String operationguid = "";
			log.info("date:"+ new Date());
			List<WorkflowActivityOperation> operationlist = workflowActOperationService
					.selectByActivityGuidAndType(item.getActivityGuid(), 30);
			if (operationlist != null && operationlist.size() > 0) {
				operationguid = operationlist.get(0).getOperationGuid();
			} else {
				WorkflowActivityOperation operation = new WorkflowActivityOperation();
				operationguid = UUID.randomUUID().toString();
				operation.setOperationGuid(operationguid);
				operation.setActivityGuid(item.getActivityGuid());
				operation.setOperationName("退回");
				operation.setOperationType(30);
				operation.setOrderNumber(0);
				operation.setTargetActivity(fromactivityguid);
				operation.setIsCheckMaterialSubmit(20);
				operation.setProcessVersionGuid(item.getProcessVersionGuid());
				operation.setIs_ShowNextActivity(true);
				operation.setIs_ShowOperationPage(true);
				operation.setIs_ShowOpinionTemplete(false);
				operation.setMultiTransctorMode("OR");
				workflowActOperationService.addActivityOperation(operation);
			}
			log.info("date:"+ new Date());
			EpointFrameDsManager.commit();
			addCallbackParam("message", "退回成功！");
			JSONObject json = new JSONObject();
			JSONArray array = new JSONArray();
			JSONObject stepJson = new JSONObject();
			stepJson.put("stepguid", iworkservice.findStepguid(fromactivityguid, pvi.getPvi().getProcessVersionInstanceGuid()));
			log.info("date:"+ new Date());
			array.add(stepJson);
			json.put("operationguid", operationguid);
			json.put("workitemguid", workitemguid);
			json.put("opinion", reason);
			json.put("pviguid", auditProject.getPviguid());
			json.put("userguid", userSession.getUserGuid());
			json.put("nextsteplist", array);
			IWFEngineAPI9 wfengine = ContainerFactory.getContainInfo().getComponent(IWFEngineAPI9.class);
			wfapi.getDao().beginTransaction();
			wfengine.operate(json.toString());
			this.wfapi.getDao().commitTransaction();
			log.info("date:"+ new Date());
			// 视为特殊操作
//		IAuditProjectUnusual projectUnusual = ContainerFactory.getContainInfo()
//				.getComponent(IAuditProjectUnusual.class);
//		AuditProjectUnusual auditProjectUnusual = new AuditProjectUnusual();
//		auditProjectUnusual.setRowguid(UUID.randomUUID().toString());
//		auditProjectUnusual.setOperateuserguid(UserSession.getInstance().getUserGuid());
//		auditProjectUnusual.setOperateusername(UserSession.getInstance().getDisplayName());
//		auditProjectUnusual.setOperatedate(new Date());
//		if (auditProject.getStatus() == 26) {
//			auditProjectUnusual.setOperatetype(31);
//		} else {
//			auditProjectUnusual.setOperatetype(auditProject.getStatus());// 退回标志
//		}
//		auditProjectUnusual.setNote(reason);
//		auditProjectUnusual.setProjectguid(auditProject.getRowguid());
//		auditProjectUnusual.setPviguid(auditProject.getPviguid());
//		auditProjectUnusual.setWorkitemguid(workitemguid);
//		projectUnusual.addProjectUnusual(auditProjectUnusual);
//
//		IAuditProjectSparetime projectSparetime = (IAuditProjectSparetime) ContainerFactory.getContainInfo()
//				.getComponent(IAuditProjectSparetime.class);
//		AuditProjectSparetime auditProjectSparetime = (AuditProjectSparetime) projectSparetime
//				.getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
//		if(auditProjectSparetime!=null){
//			auditProjectSparetime.setPause("1");
//			projectSparetime.updateSpareTime(auditProjectSparetime);
//		}


		}catch (Exception e){
			e.printStackTrace();
			addCallbackParam("message", "退回失败！");
			EpointFrameDsManager.rollback();
		}finally {
			EpointFrameDsManager.close();
		}

	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public AuditTask getAuditTask() {
		return auditTask;
	}

	public void setAuditTask(AuditTask auditTask) {
		this.auditTask = auditTask;
	}

}
