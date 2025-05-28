package com.epoint.statistics.service;

import java.util.List;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;

import javax.xml.crypto.Data;

/**
 * @author yuchenglin
 * @version 1.0.0
 * @ClassName OverdueAuditService.java
 * @Description 超期办件的统计的具体的dao类
 * @createTime 2022年01月05日 16:48:00
 */
public class OverdueAuditService {

    /**
     * 公司的数据增删改查的组件
     */
    protected ICommonDao baseDao;

    public OverdueAuditService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * @param code: areacode
     * @param sql:  条件中的sql
     * @Author: yuchenglin
     * @Description:根据areacode来统计
     * @Date: 2022/1/6 19:09
     * @return: java.util.List<com.epoint.core.grammar.Record>
     **/
    public List<Record> getListByAreaCode(String code, String sql) {
        String sqlQuery = "";
        if (StringUtil.isBlank(sql)) {
            sqlQuery = "select  ifnull(sum(cq),0) as cq,  ifnull(sum(wbj),0) as wbj , ifnull(sum(ybj),0) as ybj from"
                    + " ( select  case when sparetime<0  then 1 end as cq , "
                    + "case when sparetime<0 and status<90 then 1 end  as wbj,"
                    + " case when sparetime<0 and status>=90 then 1 end as ybj  from " + " audit_project"
                    + "  where   areacode like '" + code + "%'  ) a";
        }
        else {
            sqlQuery = "select  ifnull(sum(cq),0) as cq, ifnull(sum(wbj),0) as wbj , ifnull(sum(ybj),0) as ybj from"
                    + " ( select  case when sparetime<0  then 1 end as cq , "
                    + "case when sparetime<0 and status<90 then 1 end  as wbj,"
                    + " case when sparetime<0  and status>=90 then 1 end as ybj  from   audit_project"
                    + "  where   areacode like '" + code + "%' " + sql + " ) a   ";
        }
        return baseDao.findList(sqlQuery, Record.class);
    }

    public List<Record> getListByOuguids(List<String> list, String sql) {
        String ouguids = "('" + StringUtil.join(list, "','") + "')";
        String sqlQuery = "";
        if (StringUtil.isBlank(sql)) {
            sqlQuery = "select  ifnull(sum(cq),0) as cq,  ifnull(sum(wbj),0) as wbj , ifnull(sum(ybj),0) as ybj from"
                    + " ( select  case when sparetime<0  then 1 end as cq , "
                    + "case when sparetime<0 and status<90 then 1 end  as wbj,"
                    + " case when sparetime<0 and status>=90 then 1 end as ybj  from " + " audit_project"
                    + "  where   ouguid in " + ouguids + "  ) a";
        }
        else {
            sqlQuery = "select  ifnull(sum(cq),0) as cq, ifnull(sum(wbj),0) as wbj , ifnull(sum(ybj),0) as ybj from"
                    + " ( select  case when sparetime<0  then 1 end as cq , "
                    + "case when sparetime<0 and status<90 then 1 end  as wbj,"
                    + " case when sparetime<0  and status>=90 then 1 end as ybj  from   audit_project"
                    + "  where   ouguid in" + ouguids + sql + " ) a   ";
        }
        return baseDao.findList(sqlQuery, Record.class);
    }


    public List<Record> getListByAOuguid(String code, String sql) {
        String sqlQuery = "";
        if (StringUtil.isBlank(sql)) {
            sqlQuery = "select  ifnull(sum(cq),0) as cq,  ifnull(sum(wbj),0) as wbj , ifnull(sum(ybj),0) as ybj from"
                    + " ( select  case when sparetime<0  then 1 end as cq , "
                    + "case when sparetime<0 and status<90 then 1 end  as wbj,"
                    + " case when sparetime<0 and status>=90 then 1 end as ybj  from  audit_project"
                    + "  where   ouguid ='" + code + "'  ) a";
        }
        else {
            sqlQuery = "select  ifnull(sum(cq),0) as cq, ifnull(sum(wbj),0) as wbj , ifnull(sum(ybj),0) as ybj from"
                    + " ( select  case when sparetime<0  then 1 end as cq , "
                    + "case when sparetime>0 and status<90 then 1 end  as wbj,"
                    + " case when sparetime<0 and status>=90  then 1 end as ybj  from audit_project"
                    + "  where   ouguid = '" + code + "' " + sql + " ) a   ";
        }
        return baseDao.findList(sqlQuery, Record.class);
    }

    /**
     * @param first:first
     * @param pageSize:pageSize
     * @param ouguids:ouguids
     * @param type:type
     * @param rouguid:rouguid
     * @Author: yuchenglin
     * @Description: 分页查询
     * @Date: 2022/1/7 12:45
     * @return: java.util.List<com.epoint.basic.auditproject.auditproject.domain.AuditProject>
     **/
    public List<AuditProject> getAuditProjectByTJ(int first, int pageSize, List<String> ouguids, String type,
                                                  String flowsn) {
        // 超期办件
        String sql = this.getSQL(ouguids, type, flowsn);
        sql += " order by applydate desc";
        return baseDao.findList(sql, first, pageSize, AuditProject.class);
    }

    public List<AuditProject> getAuditProjectByTJ(int first, int pageSize, List<String> ouguids, String type,
                                                  AuditProject auditProject) {
        // 超期办件
        String sql = this.getSQL(ouguids, type, auditProject.getFlowsn());
        if (StringUtil.isNotBlank(auditProject.getStr("startData"))) {
            sql += " and promiseenddate >= '" + auditProject.getStr("startData") + "'";
        }
        if (StringUtil.isNotBlank(auditProject.get("endData"))) {
            sql += " and promiseenddate <= '" + auditProject.get("endData") + "'";
        }
        if (auditProject.getApplyway() != null) {
            sql += " and applyway = '" + auditProject.get("applyway") + "'";
        }
        if (auditProject.getStatus() != null) {
            sql += " and  status = '" + auditProject.get("status") + "'";
        }
        sql += " order by applydate desc";
        return baseDao.findList(sql, first, pageSize, AuditProject.class);
    }


    /**
     * @param ouguids:ouguids
     * @param type:type
     * @param rouguid:rouguid
     * @Author: yuchenglin
     * @Description:根据条件查询条数
     * @Date: 2022/1/7 12:45
     * @return: int
     **/
    public int getCount(List<String> ouguids, String type, String flowsn) {
        String sql = this.getSQL(ouguids, type, flowsn);
        sql = "select count(0) from (" + sql + ") a";
        return baseDao.queryInt(sql);
    }

    public int getCount(List<String> ouguids, String type, AuditProject auditProject) {
        String sql = this.getSQL(ouguids, type, auditProject.getFlowsn());
        if (StringUtil.isNotBlank(auditProject.getStr("startData"))) {
            sql += " and promiseenddate >= '" + auditProject.getStr("startData") + "'";
        }
        if (StringUtil.isNotBlank(auditProject.get("endData"))) {
            sql += " and promiseenddate <= '" + auditProject.get("endData") + "'";
        }
        if (auditProject.getApplyway() != null) {
            sql += " and applyway = '" + auditProject.get("applyway") + "'";
        }
        if (auditProject.getStatus() != null) {
            sql += " and  status = '" + auditProject.get("status") + "'";
        }
        sql = "select count(0) from (" + sql + ") a";
        return baseDao.queryInt(sql);
    }

    /**
     * @param ouguids:ouguids
     * @param type:type
     * @param rouguid:rouguid
     * @Author: yuchenglin
     * @Description:获取拼接的sql
     * @Date: 2022/1/7 11:26
     * @return: java.lang.String
     **/
    private String getSQL(List<String> ouguids, String type, String flowsn) {
        String sql = "";
        String insql = "'" + StringUtil.join(ouguids, "','") + "'";
        if ("cq".equals(type)) {
            sql = "select rowguid, pviguid,ouguid,windowname,flowsn,applydate,banjiedate,status,applyway,projectname,ouname"
                    + " from  audit_project where sparetime<0 and ouguid in (" + insql + ")";
        }
        else if ("wbj".equals(type)) {
            sql = "select rowguid, pviguid, ouguid,windowname,flowsn,applydate,banjiedate,status,applyway,projectname ,ouname"
                    + " from  audit_project where sparetime < 0 and status< 90 and ouguid in (" + insql + ")";
        }
        else if ("ybj".equals(type)) {
            sql = "select  rowguid,pviguid,  ouguid,windowname,flowsn,applydate,banjiedate,status,applyway,projectname ,ouname"
                    + " from  audit_project where  sparetime < 0 and status >= 90 and ouguid in (" + insql + ")";
        }
        if (StringUtil.isNotBlank(flowsn)) {
            sql += "and flowsn ='" + flowsn + "'";
        }
        return sql;
    }

}
