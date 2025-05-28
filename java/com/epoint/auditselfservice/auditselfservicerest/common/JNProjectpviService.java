package com.epoint.auditselfservice.auditselfservicerest.common;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;

import java.util.List;

public class JNProjectpviService {
    public List<WorkflowWorkItem> getworkflowbypvi(String pviguid) {
        String sql = "select ACTIVITYNAME,OPERATIONDATE,ProcessVersionInstanceGuid from workflow_workitem where ProcessVersionInstanceGuid = ?";
        ICommonDao commondao = CommonDao.getInstance();
        return commondao.findList(sql, WorkflowWorkItem.class, pviguid);
    }
}
