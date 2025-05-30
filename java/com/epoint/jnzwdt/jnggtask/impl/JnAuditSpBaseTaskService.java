package com.epoint.jnzwdt.jnggtask.impl;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;

import java.util.List;

public class JnAuditSpBaseTaskService
{

    protected ICommonDao baseDao;

    public JnAuditSpBaseTaskService() {
        baseDao = CommonDao.getInstance();
    }

    public List<String> listTaskIdsByPhaseGuid(String phaseguid) {
        String sql = "select a.taskid from AUDIT_SP_BASETASK_R a\n"
                + "inner join AUDIT_SP_BASETASK b on a.BASETASKGUID = b.rowguid\n"
                + "left join audit_sp_phase c on b.PHASEID = c.PHASEID\n" + "where c.rowguid= ? and b.isgptask = 1";
        return baseDao.findList(sql, String.class, phaseguid);
    }

    public List<String> listTaskIdsByBusinessGuid(String businessGuid) {
        String sql = "select b.TASKID from audit_sp_task a inner join AUDIT_SP_BASETASK_r b on a.BASETASKGUID = b.BASETASKGUID"
                + " where a.BUSINESSGUID = ?";
        return baseDao.findList(sql, String.class, businessGuid);
    }
}
