package com.epoint.auditproject.audithandlecontrol.action.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.auditproject.audithandlecontrol.action.api.IHandleControlService;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.workflow.domain.AuditTaskRiskpoint;
import com.epoint.workflow.service.common.entity.config.WorkflowTransition;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import org.springframework.stereotype.Component;

@Component
@Service
public class IHandleControlServiceImpl implements IHandleControlService {


    @Override
    public WorkflowWorkItem getWorkflowWorkItemByguid(String WORKITEMGUID) {
        return new HandleControlService().getWorkflowWorkItemByguid(WORKITEMGUID);
    }

    @Override
    public WorkflowTransition getWorkflowTransitionByPvguid(String PROCESSVERSIONGUID, String TRANSITIONNAME) {
        return new HandleControlService().getWorkflowTransitionByPvguid(PROCESSVERSIONGUID, TRANSITIONNAME);
    }

    @Override
    public AuditTaskRiskpoint getRiskpoint(String taskguid, String activityname) {
        return new HandleControlService().getRiskpoint(taskguid, activityname);
    }

    @Override
    public AuditTask getAuditTaskByItemid(String item_id) {
        return new HandleControlService().getAuditTaskByItemid(item_id);
    }


}
