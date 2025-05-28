package com.epoint.auditproject.audithandlecontrol.action.service;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.workflow.domain.AuditTaskRiskpoint;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.workflow.service.common.entity.config.WorkflowTransition;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;

public class HandleControlService {

    private ICommonDao commonDao;

    public HandleControlService() {
        commonDao = CommonDao.getInstance();
    }

    public WorkflowWorkItem getWorkflowWorkItemByguid(String workitemguid) {
        String sql = "select * from workflow_workitem where WORKITEMGUID = ? ";
        return commonDao.find(sql, WorkflowWorkItem.class, workitemguid);
    }


    public WorkflowTransition getWorkflowTransitionByPvguid(String processversionguid, String TRANSITIONNAME) {
        String sql = "select * from Workflow_Transition where PROCESSVERSIONGUID = ? and TRANSITIONNAME = ?";
        return commonDao.find(sql, WorkflowTransition.class, processversionguid, TRANSITIONNAME);
    }

    public AuditTaskRiskpoint getRiskpoint(String taskguid, String activityname) {
        String sql = "select * from audit_task_riskpoint where taskguid = ? and activityname = ?";
        return commonDao.find(sql, AuditTaskRiskpoint.class, taskguid, activityname);
    }

    public AuditTask getAuditTaskByItemid(String item_id) {
        String sql = "select * from audit_task where item_id = ? and is_enable = '1' and is_editafterimport = '1' and ifnull(is_history,'0') = '0' ";
        return commonDao.find(sql,AuditTask.class,item_id);
    }
}
