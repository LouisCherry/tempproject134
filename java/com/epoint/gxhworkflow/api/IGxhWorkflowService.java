package com.epoint.gxhworkflow.api;

import java.io.Serializable;
import java.util.List;

import com.epoint.workflow.service.common.entity.config.WorkflowTransition;



public interface IGxhWorkflowService extends Serializable{
	public List<WorkflowTransition> findwork(String activityguid);
	
	public String findStepguid(String activityguid, String pviguid);
}
