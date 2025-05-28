package com.epoint.gxhworkflow.impl;

import java.util.List;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.workflow.service.common.entity.config.WorkflowTransition;

public class GxhWorkflowService {
	/**
	 * 数据增删改查组件
	 */
	protected ICommonDao baseDao;

	public GxhWorkflowService() {
		baseDao = CommonDao.getInstance();
	}

	public List<WorkflowTransition> findwork(String activityguid) {
		String sql = "SELECT * from workflow_transition a JOIN workflow_workitem b ON a.TOACTIVITYGUID = b.ACTIVITYGUID and b.ACTIVITYGUID=? ";
		return baseDao.findList(sql, WorkflowTransition.class, activityguid);
	}
}
