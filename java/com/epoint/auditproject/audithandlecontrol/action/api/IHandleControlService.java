package com.epoint.auditproject.audithandlecontrol.action.api;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.workflow.domain.AuditTaskRiskpoint;
import com.epoint.workflow.service.common.entity.config.WorkflowTransition;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;

public interface IHandleControlService {

    public WorkflowWorkItem getWorkflowWorkItemByguid(String WORKITEMGUID);

    public WorkflowTransition getWorkflowTransitionByPvguid(String PROCESSVERSIONGUID, String TRANSITIONNAME);

    public AuditTaskRiskpoint getRiskpoint(String taskguid, String activityname);

    AuditTask getAuditTaskByItemid(String item_id);

}
