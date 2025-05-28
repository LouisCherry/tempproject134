package com.epoint.taskcase.impl;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.epoint.basic.audittask.material.domain.AuditTaskCase;
import com.epoint.core.dao.CommonDao;
import com.epoint.taskcase.api.ITaskCaseService;

@Service
@Component
public class TaskCaseImpl implements ITaskCaseService
{

    @Override
    public AuditTaskCase Check(String taskguid, String casename, String casecode) {
        String sql = "select rowguid from audit_task_case where taskguid = '" + taskguid + "' " + " and casename = '"
                + casename + "' and rowguid = '" + casecode + "'";
        return CommonDao.getInstance().find(sql, AuditTaskCase.class);
    }

    @Override
    public List<String> findTaskGuid() {
        String sql = "SELECT a.rowguid from audit_task a join  audit_task_extension b on a.RowGuid=b.TASKGUID"
                + " where a.IS_ENABLE='1' and (a.IS_HISTORY='0' or a.IS_HISTORY is null) and a.IS_EDITAFTERIMPORT='1' and (b.CASE_SETTING_INFO is null or b.CASE_SETTING_INFO='')";
        return CommonDao.getInstance().findList(sql, String.class);
    }

}
