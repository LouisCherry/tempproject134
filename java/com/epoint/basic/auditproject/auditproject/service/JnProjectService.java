package com.epoint.basic.auditproject.auditproject.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.config.SplitTableConfig;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.sharding.util.ShardingUtil;

public class JnProjectService
{
    private ICommonDao commonDao = CommonDao.getInstance();

    public JnProjectService() {
        SplitTableConfig conf = ShardingUtil.getSplitTableConfig("AUDIT_PROJECT");
        if (conf != null) {
            this.commonDao = CommonDao.getInstance(conf);
        }

    }

    
    public PageData<AuditProject> getAuditProjectPageDataMonitor(String fieldstr, Map<String, String> conditionMap,
            Integer ismaterial, Integer firstResult, Integer maxResults, String sortField, String sortOrder) {
        PageData pageData = new PageData();
        SQLManageUtil sqlManageUtil = new SQLManageUtil();
        String sql = "select " + fieldstr + " from audit_project";
        String sqlCount = "select count(1) from audit_project";
        String sqlCondition = sqlManageUtil.buildSql(conditionMap);
        String sqlExtra = "";
        if (ismaterial.intValue() == 1) {
            sqlExtra = " and rowguid in (select projectguid from audit_project_material where status=\'20\' or status=\'25\') ";
        }
        else if (ismaterial.intValue() == 2) {
            sqlExtra = " and rowguid not in (select projectguid from audit_project_material where status=\'20\' or status=\'25\') ";
        }

        String sqlOrder = "";
        if (StringUtil.isNotBlank(sortField)) {
            sqlOrder = " order by " + sortField + " " + sortOrder;
        }

        List dataList = this.commonDao.findList(sql + sqlCondition + sqlExtra + sqlOrder, firstResult.intValue(),
                maxResults.intValue(), AuditProject.class, new Object[0]);
        int dataCount = this.commonDao.queryInt(sqlCount + sqlCondition + sqlExtra, new Object[0]).intValue();
        pageData.setList(dataList);

        pageData.setRowCount(dataCount);
        return pageData;
    }

    public PageData<AuditProject> getAuditProjectPageDataMonitor(String userGuid,String fieldstr, Map<String, String> conditionMap,
            Integer ismaterial, Integer firstResult, Integer maxResults, String sortField, String sortOrder) {
        PageData pageData = new PageData();
        SQLManageUtil sqlManageUtil = new SQLManageUtil();
        String sql = "select " + fieldstr + " from audit_project";
        String sqlCount = "select count(1) from audit_project";
        String sqlCondition = sqlManageUtil.buildSql(conditionMap);
        String sqlExtra = "";
        if (ismaterial.intValue() == 1) {
            sqlExtra = " and rowguid in (select projectguid from audit_project_material where status=\'20\' or status=\'25\') ";
        }
        else if (ismaterial.intValue() == 2) {
            sqlExtra = " and rowguid not in (select projectguid from audit_project_material where status=\'20\' or status=\'25\') ";
        }
        
        if (StringUtil.isNotBlank(userGuid)) {
        	sqlExtra+=" and ((TASKTYPE = 1 AND ACCEPTUSERGUID = '" + userGuid
                    + "') OR (TASKTYPE = 2 AND RECEIVEUSERGUID = '" + userGuid + "' ))";
        }

        String sqlOrder = "";
        if (StringUtil.isNotBlank(sortField)) {
            sqlOrder = " order by " + sortField + " " + sortOrder;
        }

        List dataList = this.commonDao.findList(sql + sqlCondition + sqlExtra + sqlOrder, firstResult.intValue(),
                maxResults.intValue(), AuditProject.class, new Object[0]);
        int dataCount = this.commonDao.queryInt(sqlCount + sqlCondition + sqlExtra, new Object[0]).intValue();
        pageData.setList(dataList);

        pageData.setRowCount(dataCount);
        return pageData;
    }

    /**
     * 
     * @Description: 重载getAuditProjectPageDataMonitor方法，权力类型判断
     * @author male    
     * @date 2018年12月27日 上午10:17:03
     * @return PageData<AuditProject>    返回类型    
     * @throws
     */
    public PageData<AuditProject> getAuditProjectPageDataMonitor(String fieldstr, Map<String, String> conditionMap,
            Integer ismaterial, Integer firstResult, Integer maxResults, String sortField, String sortOrder,
            String shenpilb) {
        PageData pageData = new PageData();
        SQLManageUtil sqlManageUtil = new SQLManageUtil();
        String sql = "select " + fieldstr + " from audit_project";
        String sqlCount = "select count(1) from audit_project";
        String sqlCondition = sqlManageUtil.buildSql(conditionMap);
        String sqlExtra = "";
        /*        if (ismaterial.intValue() == 1) {
            sqlExtra = " and rowguid in (select projectguid from audit_project_material where status=\'20\' or status=\'25\') ";
        }
        else if (ismaterial.intValue() == 2) {
            sqlExtra = " and rowguid not in (select projectguid from audit_project_material where status=\'20\' or status=\'25\') ";
        }*/
        if (StringUtil.isNotBlank(shenpilb)) {
            sqlExtra = " and TASKGUID in (select rowguid from audit_task where shenpilb = \'" + shenpilb
                    + "\') order by applydate desc";
        }

        String sqlOrder = "";
        if (StringUtil.isNotBlank(sortField)) {
            sqlOrder = " order by " + sortField + " " + sortOrder;
        }

        //处理sqlCondition
        if (sqlCondition.indexOf("order") > 0) {
            sqlCondition = sqlCondition.substring(0, sqlCondition.indexOf("order"));
        }

        List dataList = this.commonDao.findList(sql + sqlCondition + sqlExtra + sqlOrder, firstResult.intValue(),
                maxResults.intValue(), AuditProject.class, new Object[0]);
        int dataCount = this.commonDao.queryInt(sqlCount + sqlCondition + sqlExtra, new Object[0]).intValue();
        pageData.setList(dataList);
        pageData.setRowCount(dataCount);
        return pageData;
    }

    public Integer getCSCount(String windowGuid, String areaCode) {
        String sql = "select count(rowguid) from audit_project where rowguid in (select projectguid from audit_project_sparetime where SPAREMINUTES<1440) and windowguid=?1 and areacode=?2 and tasktype=\'2\' ";
        SqlConditionUtil sqlUtil = new SqlConditionUtil();
        sqlUtil.lt("PROMISEENDDATE", new Date());
        sqlUtil.ge("status", String.valueOf(26));
        sql = sql + (new SQLManageUtil()).buildPatchSql(sqlUtil.getMap());
        return this.commonDao.queryInt(sql, new Object[] {windowGuid, areaCode });
    }
}
