package com.epoint.bzproject.impl;

import com.epoint.basic.auditonlineuser.auditonlineproject.domain.AuditOnlineProject;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;

public class BzprojectService {

    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public BzprojectService() {
        baseDao = CommonDao.getInstance();
    }

    public AuditOnlineProject getOnlineProjectByguid(String sourceguid) {
        String sql = "select * from audit_online_project where SOURCEGUID = ? ";
        return baseDao.find(sql, AuditOnlineProject.class, sourceguid);
    }

}
