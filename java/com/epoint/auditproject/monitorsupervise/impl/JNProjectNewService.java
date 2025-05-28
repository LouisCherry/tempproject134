package com.epoint.auditproject.monitorsupervise.impl;

import com.epoint.auditproject.monitorsupervise.api.AuditprojectoldNew;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.config.SplitTableConfig;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.sharding.util.ShardingUtil;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class JNProjectNewService {

    /**
     * 通用dao
     */
    private ICommonDao commonDao;

    public JNProjectNewService() {
        commonDao = CommonDao.getInstance("project");
        SplitTableConfig conf = ShardingUtil.getSplitTableConfig("AUDIT_PROJECT");
        if (conf != null) {
            commonDao = CommonDao.getInstance(conf);
        }
    }

    public int getMaxOrgNumberinfo(String orgword, String rowguid, String areaCode, String task_id) {
        // 1.定义flowSn
        int maxnum = 0;
        String flowSn = "";
        // 2.调用存储过程的service
        // 2.1设置过程参数
        Object[] args = new Object[7];
        // 2.1.1 @NumberName NVARCHAR(50),--flowsn名称，例如：办件编号
        args[0] = "文书编号";
        // 2.1.2 @NumberFlag NVARCHAR(50),--flowsn标识，例如：LS（系统参数：AS_FLOWSN_PRE）
        args[1] = "WS" + areaCode;
        // 2.1.3 @theYear INT,--年
        args[2] = EpointDateUtil.getYearOfDate(new Date());
        // 2.1.4 @theYearLength INT,--显示年的长度
        args[3] = 4;
        // 2.1.5 @theMonth INT,--月
        args[4] = "13";
        // 2.1.6 @theDay INT,--日
        args[5] = "13";
        // 2.1.7 @SN_length INT,--流水号长度
        args[6] = 4;
        try {
            flowSn = String.valueOf(CommonDao.getInstance("common").executeProcudureWithResult(args.length + 1,
                    java.sql.Types.VARCHAR, "Common_Getflowsn", args));
            maxnum = Integer.parseInt(flowSn.substring(flowSn.length() - 4));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maxnum;
    }

    public PageData<AuditProject> getAuditProjectPageDataMonitor(String fieldstr, Map<String, String> conditionMap, String userguid,
                                                                 Integer ismaterial, Integer firstResult, Integer maxResults, String sortField, String sortOrder) {
        PageData<AuditProject> pageData = new PageData<>();
        SQLManageUtil sqlManageUtil = new SQLManageUtil("project", true);
        String sql = "select " + fieldstr + " from audit_project";
        String sqlCount = "select count(1) from audit_project";
        String sqlCondition = sqlManageUtil.buildSql(conditionMap);
        String sqlExtra = "";
        if (ismaterial == 1) {
            sqlExtra = " and rowguid in (select projectguid from audit_project_material where status='20' or status='25') ";
        } else if (ismaterial == 2) {
            sqlExtra = " and rowguid not in (select projectguid from audit_project_material where status='20' or status='25') ";
        }
        if (StringUtil.isNotBlank(userguid)) {
            sql = sql + " where is_samecity = 1 and RECEIVEUSERGUID = '" + userguid + "' UNION ALL " + sql;
            sqlCount = sqlCount + " where is_samecity = 1 and RECEIVEUSERGUID = '" + userguid + "'  UNION ALL " + sqlCount;
        }

        String sqlOrder = "";
        if (StringUtil.isNotBlank(sortField)) {
            sqlOrder = " order by " + sortField + " " + sortOrder;
        }

        List<AuditProject> dataList = commonDao.findList(sql + sqlCondition + sqlExtra + sqlOrder, firstResult,
                maxResults, AuditProject.class);
        List<Integer> dataCount = commonDao.findList(sqlCount + sqlCondition.replace("order by applydate desc", "") + sqlExtra, Integer.class);
        pageData.setList(dataList);
        pageData.setRowCount(dataCount.get(0) + dataCount.get(1));
        return pageData;
    }

    public PageData<AuditprojectoldNew> getAuditProjectPageDataMonitorOld(String fieldstr, Map<String, String> conditionMap, Integer firstResult, Integer maxResults, String sortField, String sortOrder) {
        PageData<AuditprojectoldNew> pageData = new PageData<>();
        SQLManageUtil sqlManageUtil = new SQLManageUtil("project", true);
        String sql = "select " + fieldstr + " from audit_project_old_new";
        String sqlCount = "select count(1) from audit_project_old_new";
        String sqlCondition = sqlManageUtil.buildSql(conditionMap);
        String sqlOrder = "";
        if (StringUtil.isNotBlank(sortField)) {
            sqlOrder = " order by " + sortField + " " + sortOrder;
        }

        List<AuditprojectoldNew> dataList = commonDao.findList(sql + sqlCondition + sqlOrder, firstResult,
                maxResults, AuditprojectoldNew.class);
        List<Integer> dataCount = commonDao.findList(sqlCount + sqlCondition.replace("order by applydate desc", ""), Integer.class);
        pageData.setList(dataList);
        pageData.setRowCount(dataCount.get(0));
        return pageData;
    }

    public Integer getCSCount(String windowGuid, String areaCode) {
        String sql = "select count(rowguid) from audit_project where rowguid in (select projectguid from audit_project_sparetime where SPAREMINUTES<1440)"
                + " and windowguid=?1 and areacode=?2 and tasktype='2' ";
        SqlConditionUtil sqlUtil = new SqlConditionUtil();
        sqlUtil.lt("PROMISEENDDATE", new Date());
        sqlUtil.ge("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_YJJ));
        sql = sql + new SQLManageUtil("project", true).buildPatchSql(sqlUtil.getMap());
        return commonDao.queryInt(sql, windowGuid, areaCode);
    }

    public AuditprojectoldNew getAuditProjectByRowGuid(String projectguid) {
        String sql = "select taskcaseguid,task_id,is_samecity,xiangmubm,xiangmuname,AREAALL,AREABUILD,INVESTMENT,PROJECTCONTENT,PROJECTALLOWEDNO,businessguid,biguid,subappguid,rowguid,taskguid,projectname,applyeruserguid,applyername,areacode,pviguid,status,applyway,tasktype,spendtime,banjieresult,flowsn,sparetime,is_test,applydate,applyertype,certnum,certtype,address,contactperson,contactphone,contactmobile,contactpostcode,contactemail,contactfax,contactcertnum,remark,banjiedate,If_express,legal,centerguid,is_charge,hebingshoulishuliang,Certrowguid,Certdocguid,acceptareacode,legalid,ISSYNACWAVE,dataObj_baseinfo ";
        sql += " from audit_project_old_new where rowguid = ?";
        return commonDao.find(sql, AuditprojectoldNew.class, projectguid);
    }

    public AuditProject getOldProjectByRowGuid(String projectguid) {
        String sql = "select taskcaseguid,task_id,is_samecity,xiangmubm,xiangmuname,AREAALL,AREABUILD,INVESTMENT,PROJECTCONTENT,PROJECTALLOWEDNO,businessguid,biguid,subappguid,rowguid,taskguid,projectname,applyeruserguid,applyername,areacode,pviguid,status,applyway,tasktype,spendtime,banjieresult,flowsn,sparetime,is_test,applydate,applyertype,certnum,certtype,address,contactperson,contactphone,contactmobile,contactpostcode,contactemail,contactfax,contactcertnum,remark,banjiedate,If_express,legal,centerguid,is_charge,hebingshoulishuliang,Certrowguid,Certdocguid,acceptareacode,legalid,ISSYNACWAVE,dataObj_baseinfo ";
        sql += " from audit_project_old_new where rowguid = ?";
        return commonDao.find(sql, AuditProject.class, projectguid);
    }

}
