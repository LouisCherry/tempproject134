package com.epoint.auditselfservice.service;

import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;

import java.util.List;

public class JNTaskService {
    public List<AuditTaskMaterial> getOrderdTaskMaterial(String taskguid) {
        ICommonDao commdao = CommonDao.getInstance();
        String sql = "select * from audit_task_material where TASKGUID = ? ORDER BY ORDERNUM DESC ,OperateDate asc";
        return commdao.findList(sql, AuditTaskMaterial.class, taskguid);
    }
}
