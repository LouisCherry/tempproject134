package com.epoint.basic.auditsp.auditspbasetaskr.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.epoint.basic.auditsp.auditspbasetaskr.domain.AuditSpBasetaskR;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.DbKit;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.Parameter;

public class AuditSpbasetaskrService
{
    /**
     * 通用dao
     */
    private ICommonDao commonDao;

    public AuditSpbasetaskrService() {
        commonDao = CommonDao.getInstance("sp");
    }

    public void addBasetaskR(AuditSpBasetaskR record) {
        commonDao.insert(record);
    }

    public void updateBasetaskR(AuditSpBasetaskR record) {
        commonDao.update(record);
    }

    public AuditSpBasetaskR getAuditSpBasetaskRByrowguid(String rowguid) {
        return new SQLManageUtil().getBeanByOneField(AuditSpBasetaskR.class, "rowguid", rowguid);
    }

    public void delAuditSpBasetaskR(String rowGuid) {
        new SQLManageUtil().deleteByOneField(AuditSpBasetaskR.class, "rowGuid", rowGuid);
    }

    public void delByBasetaskguid(String basetaskguid) {
        new SQLManageUtil().deleteByOneField(AuditSpBasetaskR.class, "basetaskguid", basetaskguid);
    }

    public List<AuditSpBasetaskR> getAuditSpBasetaskrByCondition(Map<String, String> conditionMap) {
        return new SQLManageUtil().getListByCondition(AuditSpBasetaskR.class, conditionMap);
    }

    public List<AuditSpBasetaskR> getAuditSpBasetaskAndRByCondition(Map<String, String> conditionMap) {
        String sql = "select a.* from audit_sp_basetask_r a,audit_sp_basetask b where a.basetaskguid=b.rowguid";
        sql += new SQLManageUtil().buildPatchSql(conditionMap);
        return commonDao.findList(sql, AuditSpBasetaskR.class);
    }

    public Integer getAuditSpBasetaskrCountByCondition(Map<String, String> conditionMap) {
        return new SQLManageUtil().getListCount(AuditSpBasetaskR.class, conditionMap);
    }

    public List<Record> getTaskidlistbyBasetaskOuname(String ouname, List<String> basetaskguids) {
        List<Object> params = new ArrayList<Object>();
        String sql = "select distinct b.taskid,b.xiaquname,b.areacode from audit_sp_basetask a,audit_sp_basetask_r b where a.ouname = ? and a.rowguid=b.basetaskguid and a.rowguid in ";
        if (StringUtil.isBlank(ouname)) {
            sql = sql.replace("a.ouname = ? and", "");
        }
        else {
            params.add(ouname);
        }
        Parameter pa = DbKit.splitIn("'" + StringUtil.join(basetaskguids, "','") + "'");
        sql += pa.getSql();
        for (Object obj : pa.getValue()) {
            params.add(obj);
        }
        return commonDao.findList(sql, Record.class, params.toArray());
    }

    public List<String> getTaskidlistbyPhaseid(String phaseid) {
        String sql = "select taskid from audit_sp_basetask a,audit_sp_basetask_r b where a.rowguid=b.basetaskguid and phaseid=?";
        return commonDao.findList(sql, String.class, phaseid);
    }

    public List<String> getTaskidlistbySpecialPhaseid(String phaseid) {
        if ("2".equals(phaseid) || "4".equals(phaseid)) {
            String sql = "select taskid from audit_sp_basetask a,audit_sp_basetask_r b where a.rowguid=b.basetaskguid and phaseid=?";
            return commonDao.findList(sql, String.class, phaseid);
        }
        else {
            String sql = "select taskid from audit_sp_basetask a,audit_sp_basetask_r b where a.rowguid=b.basetaskguid and phaseid in ('2','4')";
            return commonDao.findList(sql, String.class);
        }
    }

    public List<String> getBasetaskguidsBytaskids(List<String> taskids) {
        if (taskids == null || taskids.size() == 0) {
            return new ArrayList<>();
        }
        String sql = "select distinct basetaskguid from audit_sp_basetask_r where taskid in ("
                + StringUtil.joinSql(taskids) + ")";
        return commonDao.findList(sql, String.class);
    }

    public List<String> gettaskidsByBusinedssguid(String businedssguid, String phaseid) {
        String sql = "select b.taskid from audit_sp_task a , audit_sp_basetask_r b,audit_sp_phase c where a.basetaskguid = b.basetaskguid and a.PHASEGUID = c.RowGuid and PHASEID = ? and businedssguid = ?";
        return commonDao.findList(sql, String.class, phaseid, businedssguid);
    }

    public List<Record> getBasetaskByAreacodelistandTaskid(String taskid, List<String> areacodeList) {
        List<Object> params = new ArrayList<Object>();
        String sql = "select a.ouname,b.xiaquname,b.TASKNAME,a.QUESTIONS,b.TASKID,b.BASETASKGUID,b.AREACODE from audit_sp_basetask a,audit_sp_basetask_r b where a.ROWGUID = b.BASETASKGUID and b.TASKID  = ? and BUSINESSTYPE = '2' and b.AREACODE in ";
        params.add(taskid);
        Parameter pa = DbKit.splitIn("'" + StringUtil.join(areacodeList, "','") + "'");
        sql += pa.getSql();
        for (Object obj : pa.getValue()) {
            params.add(obj);
        }
        return commonDao.findList(sql, Record.class, params.toArray());
    }

    public List<Record> getBasetaskByAreacodelistandTaskid(String taskid, List<String> areacodeList,
            List<String> basetaskList) {
        List<Object> params = new ArrayList<Object>();
        String sql = "select a.ouname,b.xiaquname,b.TASKNAME,a.QUESTIONS,b.TASKID,b.BASETASKGUID,b.AREACODE from audit_sp_basetask a,audit_sp_basetask_r b where a.ROWGUID = b.BASETASKGUID and b.TASKID  = ? and BUSINESSTYPE = '2' and b.AREACODE in ";
        params.add(taskid);
        Parameter pa = DbKit.splitIn("'" + StringUtil.join(areacodeList, "','") + "'");
        sql += pa.getSql();
        for (Object obj : pa.getValue()) {
            params.add(obj);
        }
        if (!basetaskList.isEmpty()) {
            Parameter ba = DbKit.splitIn("'" + StringUtil.join(basetaskList, "','") + "'");
            sql += "and b.basetaskguid in";
            sql += ba.getSql();
            for (Object ob : ba.getValue()) {
                params.add(ob);
            }
        }

        return commonDao.findList(sql, Record.class, params.toArray());
    }

    public List<Record> getBasetaskByTaskid(String taskid) {
        String sql = "select b.TASKNAME,a.QUESTIONS,b.TASKID,b.BASETASKGUID,b.AREACODE from audit_sp_basetask a,audit_sp_basetask_r b where a.ROWGUID = b.BASETASKGUID and b.TASKID  = ? and BUSINESSTYPE = '2' ";
        return commonDao.findList(sql, Record.class, taskid);
    }

    public List<Record> getBasetaskrByTaskIdAndBusinessType(String taskid, String businessType) {
        String sql = "select b.TASKNAME,a.QUESTIONS,b.TASKID,b.BASETASKGUID,b.AREACODE,a.TASKNAME AS basetaskname  from audit_sp_basetask a,audit_sp_basetask_r b where a.ROWGUID = b.BASETASKGUID and b.TASKID  = ?  and a.businesstype = ?";
        return commonDao.findList(sql, Record.class, taskid, businessType);
    }

    public List<Record> getBasetaskByAreacodelistandTaskidNew(String taskid, List<String> areacodeList, String type) {
        List<Object> params = new ArrayList<Object>();
        String sql = "select a.ouname,b.xiaquname,b.TASKNAME,a.QUESTIONS,b.TASKID,b.BASETASKGUID,b.AREACODE from audit_sp_basetask a,audit_sp_basetask_r b where a.ROWGUID = b.BASETASKGUID and b.TASKID  = ?  and b.AREACODE in ";
        params.add(taskid);
        Parameter pa = DbKit.splitIn("'" + StringUtil.join(areacodeList, "','") + "'");
        sql += pa.getSql();
        if (StringUtil.isNotBlank(type)) {
            sql += " and BUSINESSTYPE = '" + type + "'";
        }
        for (Object obj : pa.getValue()) {
            params.add(obj);
        }
        return commonDao.findList(sql, Record.class, params.toArray());
    }

    public List<String> getAlreadyUsedTaskid(String businesstype) {
        String sql = "select b.taskid from audit_sp_basetask a,audit_sp_basetask_r b where a.rowguid = b.basetaskguid and  a.businesstype = ?";
        return commonDao.findList(sql, String.class, businesstype);
    }
}
