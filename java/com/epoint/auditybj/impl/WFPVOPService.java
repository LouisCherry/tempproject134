package com.epoint.auditybj.impl;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;

import java.util.List;

public class WFPVOPService {
    protected ICommonDao baseDao;

    public WFPVOPService() {
        baseDao = CommonDao.getInstance();
    }

    public Integer getCountByPvguidAndUserguid(String pvguid, String userguid) {
        baseDao = CommonDao.getInstance();
        String sql = "select count(*) from workflow_pvi_opinion where pviguid=? and adduserguid=?";
        int execute = baseDao.queryInt(sql, pvguid, userguid);
        return execute;

    }

    public List<String> getPviguidByUserguid(String userguid) {
        baseDao = CommonDao.getInstance();
        String sql = "select DISTINCT pviguid from workflow_pvi_opinion where adduserguid=?";
        List<String> result = baseDao.findList(sql, String.class, userguid);
        return result;
    }

    public List<String> getProjectguidByUserguid(String userguid) {
        String sql = "select DISTINCT projectguid from audit_project_operation where OperateUserGuid = ? ";
        return baseDao.findList(sql, String.class, userguid);
    }

}
