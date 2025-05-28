package com.epoint.auditsp.auditspbasetaskp.service;

import com.epoint.auditsp.auditspbasetaskp.domain.AuditSpBasetaskP;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AuditSpbasetaskPService {
    /**
     * 通用dao
     */
    private ICommonDao commonDao;

    public AuditSpbasetaskPService() {
        commonDao = CommonDao.getInstance("sp");
    }

    public void addBasetask(AuditSpBasetaskP record) {
        commonDao.insert(record);
    }

    public void updateBasetask(AuditSpBasetaskP record) {
        commonDao.update(record);
    }

    public AuditSpBasetaskP getAuditSpBasetaskPByrowguid(String rowguid) {
        return new SQLManageUtil().getBeanByOneField(AuditSpBasetaskP.class, "rowguid", rowguid);
    }

    public void delAuditSpBasetaskPByrowguid(String rowGuid) {
        new SQLManageUtil().deleteByOneField(AuditSpBasetaskP.class, "rowGuid", rowGuid);
    }

    public PageData<AuditSpBasetaskP> getAuditSpBasetaskPByPage(Map<String, String> conditionMap, int first,
                                                                int pageSize, String sortField, String sortOrder) {
        return new SQLManageUtil().getDbListByPage(AuditSpBasetaskP.class, conditionMap, first, pageSize, sortField,
                sortOrder);
    }

    public List<AuditSpBasetaskP> getAuditSpBasetaskPByCondition(Map<String, String> conditionMap) {
        SQLManageUtil sqlm = new SQLManageUtil();
        return sqlm.getListByCondition(AuditSpBasetaskP.class, conditionMap);
    }

    public Integer getCountByCondition(Map<String, String> condition) {
        SQLManageUtil sqlm = new SQLManageUtil();
        String sqle = sqlm.buildSql(condition);
        String sql = "select count(1) from audit_sp_basetask " + sqle;
        return commonDao.queryInt(sql);
    }

    public List<Record> getParentBaseTaskList(int pageNumber, int pageSize, String taskname, String ouname,
                                              String phaseid) {
        ArrayList<String> params = new ArrayList<String>();
        String sql = "select b.rowguid,b.tasktaskname,b.tasktaskcode,b.phaseid,b.operatedate,b.ouname,1 as leafcount from audit_sp_basetask a inner join audit_sp_basetask_p b where a.pid =b.rowguid and a.pid is not null ";
        if (StringUtil.isNotBlank(taskname)) {
            sql += " and b.tasktaskname like concat('%', ? , '%') ";
            params.add(taskname);
        }
        if (StringUtil.isNotBlank(ouname)) {
            sql += " and b.ouname like concat('%', ? , '%') ";
            params.add(ouname);
        }
        if (StringUtil.isNotBlank(phaseid)) {
            sql += " and b.phaseid = ? ";
            params.add(phaseid);
        }
        sql += "union select a.RowGuid,a.TASKNAME,a.taskcode, a.phaseid,a.operatedate,a.ouname,0 as leafcount from audit_sp_basetask a where ( a.pid is null or a.pid = '') ";
        if (StringUtil.isNotBlank(taskname)) {
            sql += " and a.taskname like concat('%', ? , '%') ";
            params.add(taskname);
        }
        if (StringUtil.isNotBlank(ouname)) {
            sql += " and a.ouname like concat('%', ? , '%') ";
            params.add(ouname);
        }
        if (StringUtil.isNotBlank(phaseid)) {
            sql += " and a.phaseid = ? ";
            params.add(phaseid);
        }
        sql += "order by OperateDate DESC ";
        return commonDao.findList(sql, pageNumber, pageSize, Record.class, params.toArray());
    }

    public Integer getParentBaseTaskCount(String taskname, String ouname, String phaseid) {
        ArrayList<String> params = new ArrayList<String>();
        String sql = "select b.rowguid,b.tasktaskname,b.tasktaskcode,b.phaseid,b.operatedate,b.ouname,1 as leafcount from audit_sp_basetask a inner join audit_sp_basetask_p b where a.pid =b.rowguid and a.pid is not null ";
        if (StringUtil.isNotBlank(taskname)) {
            sql += " and b.tasktaskname like concat('%', ? , '%') ";
            params.add(taskname);
        }
        if (StringUtil.isNotBlank(ouname)) {
            sql += " and b.ouname like concat('%', ? , '%') ";
            params.add(ouname);
        }
        if (StringUtil.isNotBlank(phaseid)) {
            sql += " and b.phaseid = ? ";
            params.add(phaseid);
        }
        sql += "union select a.RowGuid,a.TASKNAME,a.taskcode, a.phaseid,a.operatedate,a.ouname,0 as leafcount from audit_sp_basetask a where (a.pid is null or a.pid = '') ";
        if (StringUtil.isNotBlank(taskname)) {
            sql += " and a.taskname like concat('%', ? , '%') ";
            params.add(taskname);
        }
        if (StringUtil.isNotBlank(ouname)) {
            sql += " and a.ouname like concat('%', ? , '%') ";
            params.add(ouname);
        }
        if (StringUtil.isNotBlank(phaseid)) {
            sql += " and a.phaseid = ? ";
            params.add(phaseid);
        }
        sql += "order by OperateDate DESC ";

        String newsql = "select count(*) from (" + sql + ") as aaa ";
        return commonDao.queryInt(newsql, params.toArray());
    }

}
