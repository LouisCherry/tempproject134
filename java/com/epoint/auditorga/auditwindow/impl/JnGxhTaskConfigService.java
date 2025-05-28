package com.epoint.auditorga.auditwindow.impl;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;

import java.util.List;

public class JnGxhTaskConfigService {

    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public JnGxhTaskConfigService() {
        baseDao = CommonDao.getInstance();
    }

    public List<AuditProject> getAuditProjectListByTaskid(String taskid) {
        String sql = "select * from audit_project where task_id = ? and status != 14 and status != 20 and status != 40 and status >= 12 and status < 90";
        return baseDao.findList(sql, AuditProject.class, taskid);
    }

}
