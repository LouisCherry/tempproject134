package com.epoint.gxhworkflow.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.core.dao.CommonDao;
import com.epoint.gxhworkflow.api.IGxhWorkflowService;
import com.epoint.workflow.service.common.entity.config.WorkflowTransition;

@Component
@Service
public class GxhWorkflowServiceImpl  implements IGxhWorkflowService{
	
	public List<WorkflowTransition> findwork(String activityguid) {
	       return new GxhWorkflowService().findwork(activityguid);
	    }
	
	public String findStepguid(String activityguid, String pviguid){
		return CommonDao.getInstance().queryString("select ACTIVITYINSTANCEGUID from workflow_activity_instance where PROCESSVERSIONINSTANCEGUID=?"
				+ " and ACTIVITYGUID=? order by startdate desc", pviguid, activityguid);
	}
}
