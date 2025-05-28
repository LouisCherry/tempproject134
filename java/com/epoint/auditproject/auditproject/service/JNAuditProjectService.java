package com.epoint.auditproject.auditproject.service;

import com.epoint.auditproject.zjxt.entity.AuditProjectProcessZjxt;
import com.epoint.auditproject.zjxt.entity.AuditProjectZjxt;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.evainstance.entity.EvainstanceCk;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.hcp.api.entity.lcprojectten;
import com.epoint.hcp.api.entity.lcprojecttwo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JNAuditProjectService {

    private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public JNAuditProjectService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * @return int    返回类型
     * @throws
     * @Description: 获取AuditExpress数量
     * @author male
     * @date 2019年1月23日 下午4:16:15
     */
    public int getAuditExpressCount(String projectguid) {
        String sql = "select count(1) from audit_express WHERE projectguid = ? AND zwdtExpressStatus = '10' ";
        return baseDao.queryInt(sql, projectguid);
    }

    /**
     * @return String    返回类型
     * @throws
     * @Description: 根据configName获取attachconnect
     * @author male
     * @date 2019年1月24日 上午9:18:02
     */
    public String getAttachConnect(String configName) {
        String sql = "select ATTACH_CONNECTIONSTRING from frame_attachconfig where ATTACH_CONNECTIONSTRINGNAME=?1";
        return baseDao.queryString(sql, configName);
    }

    /**
     * @return String    返回类型
     * @throws
     * @Description: 判断该材料是否存在
     * @author male
     * @date 2019年1月24日 上午9:20:45
     */
    public String getMaterialStatus(String cliengguid) {
        String sql1 = "select STATUS from audit_project_material where CLIENGGUID=?";
        return baseDao.queryString(sql1, cliengguid);
    }

    /**
     * @return int    返回类型
     * @throws
     * @Description: 更新材料状态
     * @author male
     * @date 2019年1月24日 上午9:22:38
     */
    public int updateMaterialStatus(String cliengguid) {
        baseDao.beginTransaction();
        String sql = "UPDATE audit_project_material SET status = 20 where cliengguid = ?";
        int num = baseDao.execute(sql, cliengguid);
        baseDao.commitTransaction();
        return num;
    }

    /**
     * @return String    返回类型
     * @throws
     * @Description: 获取ReaderType
     * @author male
     * @date 2019年1月24日 上午9:34:12
     */
    public String getReaderType(String windowguid) {
        String sql = "select ReaderType from audit_orga_window where rowguid = ? ";
        return baseDao.queryString(sql, windowguid);
    }

    /**
     * 根据task_id获取电子表单的id
     *
     * @param taskId
     * @return
     */
    public String getFormtableidByTaskid(String taskId) {
        String sql = "select formtableid from audit_task_taian where task_id = ? ";
        return baseDao.queryString(sql, taskId);
    }

    /**
     * 根据task_id获取事项信息
     *
     * @param taskId
     * @return
     */
    public AuditTask getTaskinfoByTaskid(String taskId) {
        String sql = "select * FROM audit_task WHERE RowGuid = ? ";
        return baseDao.find(sql, AuditTask.class, taskId);
    }

    public <T> PageData<T> getRecordPageData(String fieldstr, Class<? extends BaseEntity> baseClass,
                                             Map<String, String> conditionMap, Integer first, Integer pageSize, String sortField, String sortOrder,
                                             String keyword, String userGuid) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(baseClass);
        PageData<T> pageData = new PageData();
        Entity en = baseClass.getAnnotation(Entity.class);
        StringBuffer sb = new StringBuffer();
        sb.append(sqlManageUtil.buildSql(conditionMap));
        if (StringUtil.isNotBlank(keyword)) {
            sb.append(" and (flowsn like '%" + keyword + "%' or applyername like '%" + keyword + "%	')");
        }

        if (StringUtil.isNotBlank(userGuid)) {
            sb.append(" and ((TASKTYPE = 1 AND ACCEPTUSERGUID = '" + userGuid
                    + "') OR (TASKTYPE = 2 AND RECEIVEUSERGUID = '" + userGuid + "' ) OR" + "(BANJIEUSERGUID = '"
                    + userGuid + "'))");
        }

        if (StringUtil.isNotBlank(sortField)) {
            sb.append(" order by " + sortField + " " + sortOrder);
        }

        String sqlRecord = "select " + fieldstr + " from " + en.table() + sb.toString();
        String sqlCount = "select count(*) from " + en.table() + sb.toString();
        List<T> dataList = (List<T>) baseDao.findList(sqlRecord, first, pageSize, baseClass, new Object[0]);
        int dataCount = baseDao.queryInt(sqlCount, new Object[0]);
        pageData.setList(dataList);
        pageData.setRowCount(dataCount);
        return pageData;
    }

    public List<Record> getEpointsformTableStruct(String tableid, String fielddisplaytype) {
        String sql = "select * from epointsform_table_struct where tableid = ?1 and (fielddisplaytype = ?2 or fielddisplaytype = 'textarea') and dispinadd = 1";
        return baseDao.findList(sql, Record.class, tableid, fielddisplaytype);
    }

    public List<Record> getEpointsformTableStructbyradioandcheck(String tableid) {
        String sql = "select * from epointsform_table_struct where tableid = ? and (fielddisplaytype = 'radiobuttonlist' or fielddisplaytype = 'checkboxlist') and dispinadd = 1";
        return baseDao.findList(sql, Record.class, tableid);
    }

    public List<Record> getEpointsformTableStructbyDatepicker(String tableid) {
        String sql = "select * from epointsform_table_struct where tableid = ? and fielddisplaytype = 'datepicker' and dispinadd = 1";
        return baseDao.findList(sql, Record.class, tableid);
    }

    public Record getTableId(String sql_tablename) {
        String sql = "select tableid from epointsform_table_basicinfo where sql_tablename = ?";
        return baseDao.find(sql, Record.class, sql_tablename);
    }

    /***
     * 更新是否容缺字段
     * @authory shibin
     * @version 2019年10月23日 下午5:51:31
     * @param rowguid
     * @param str
     * @return
     */
    public int updateProjectIsrongque(String rowguid, String str) {
        String sql = "UPDATE audit_project SET isrongque  = ? WHERE RowGuid = ?";
        return baseDao.execute(sql, str, rowguid);
    }

    /**
     * 获取办件信息
     *
     * @param projectGuid
     * @return
     * @authory shibin
     * @version 2019年10月23日 下午5:51:18
     */
    public AuditProject getProjectByprojectguid(String projectGuid) {
        String sql = "select RowGuid,promisefileguid from audit_project WHERE RowGuid = ? ";
        return baseDao.find(sql, AuditProject.class, projectGuid);
    }

    /**
     * 更新承诺文件标识
     *
     * @param projectGuid
     * @param viewData
     * @return
     * @authory shibin
     * @version 2019年10月23日 下午5:56:05
     */
    public int updatePromisefileguid(String projectGuid, String viewData) {
        String sql = "UPDATE audit_project SET promisefileguid  = ? WHERE RowGuid = ?";
        return baseDao.execute(sql, viewData, projectGuid);
    }

    /**
     * 是否已发送
     *
     * @param projectguid
     * @param string
     * @return
     * @authory shibin
     * @version 2019年10月24日 下午8:10:53
     */
    public int updateProjectIsreminded(String projectguid, String string) {
        String sql = "UPDATE audit_project SET Isreminded  = ? WHERE RowGuid = ?";
        return baseDao.execute(sql, string, projectguid);
    }

    /**
     * 获取日期差
     *
     * @param projectguid2
     * @return
     */
    public int getBanjieAndProminseDays(String projectguid2) {
        String sql = "select TIMESTAMPDIFF(MINUTE,materialBqTime,BANJIEDATE) FROM audit_project WHERE RowGuid = ? ";
        String num = baseDao.queryString(sql, projectguid2);
        int days = 0;
        if (StringUtil.isNotBlank(num)) {
            days = Integer.parseInt(num);
        }
        return days;
    }

    /**
     * 获取申请人信息
     *
     * @param projectguid2
     * @return
     */
    public Record getApplyerinfo(String projectguid2) {
        String sql = "select CERTNUM,APPLYERTYPE FROM audit_project WHERE RowGuid = ? ";
        Record record = baseDao.find(sql, Record.class, projectguid2);
        Record record2 = null;
        if ("20".equals(record.getStr("APPLYERTYPE"))) {
            // 个人
            String sqli = "select * FROM audit_rs_individual_baseinfo WHERE IDNUMBER=? AND IS_HISTORY = '0' ";
            record2 = baseDao.find(sqli, Record.class, record.getStr("CERTNUM"));
            record2.put("APPLYERTYPE", "20");
        } else {
            String sqli = "select * FROM audit_rs_company_baseinfo WHERE (CREDITCODE=? or ORGANCODE =?) AND IS_HISTORY = '0' ";
            record2 = baseDao.find(sqli, Record.class, record.getStr("CERTNUM"), record.getStr("CERTNUM"));
            record2.put("APPLYERTYPE", "10");
        }
        return record2;
    }

    /**
     * @description
     * @author shibin
     * @date 2020年6月8日 下午3:34:15
     */
    public String getScancodeByguid(String projectGuid) {
        String sql = "select QRcodeinfo FROM audit_project WHERE RowGuid = ? ";
        return baseDao.queryString(sql, projectGuid);
    }

    /**
     * @description
     * @author shibin
     * @date 2020年6月8日 下午3:34:15
     */
    public String getJstScancodeByguid(String projectGuid) {
        String sql = "select jstcodeinfo FROM audit_project WHERE RowGuid = ? ";
        return baseDao.queryString(sql, projectGuid);
    }

    /**
     * @description
     * @author shibin
     * @date 2020年6月9日 下午5:09:30
     */
    public int updateFrameCliengguid(String qRcodeAttachguid, String cliengguid) {
        String sql = "UPDATE frame_attachinfo SET CLIENGGUID = ? WHERE ATTACHGUID = ? ";
        return baseDao.execute(sql, cliengguid, qRcodeAttachguid);
    }

    /**
     * @param projectGuid
     * @return int    返回类型
     * @throws
     * @Description: 更新材料状态
     * @author male
     * @date 2019年1月24日 上午9:22:38
     */
    public int updateMaterialStatus(String cliengguid, String projectGuid) {
        baseDao.beginTransaction();
        String sql = "UPDATE audit_project_material SET status = 20 where cliengguid = ?1 and projectGuid = ?2 ";
        int num = baseDao.execute(sql, cliengguid, projectGuid);
        baseDao.commitTransaction();
        return num;
    }

    public String getScanLegalcodeByguid(String projectGuid) {
        String sql = "select dzyyqcode FROM audit_project WHERE RowGuid = ? ";
        return baseDao.queryString(sql, projectGuid);
    }

    public List<AuditProjectProcessZjxt> findProcessList(String flowsn) {
        String sql = "select *  FROM audit_rs_apply_process_zjxt WHERE PROJECTID = ? ";
        return baseDao.findList(sql, AuditProjectProcessZjxt.class, flowsn);
    }

    public AuditProjectZjxt getAuditProjectZjxtByRowGuid(String rowguid) {
        String sql = "select * FROM audit_project_zjxt WHERE RowGuid = ? ";
        return baseDao.find(sql, AuditProjectZjxt.class, rowguid);
    }

    public List<Record> findDataList(AuditProject dataBean, String applydateStart, String applydateEnd
            , String finishdateStart, String finishdateEnd, String areacode, List<String> searchOulist, int first, int pageSize) {
        StringBuilder oustr0sb = new StringBuilder();
        oustr0sb.append("select o.ouguid, o.ouname, t.allnum from( ");
        // 统计sql段
        StringBuilder sumsqlsb = new StringBuilder();
        if (StringUtil.isNotBlank(dataBean.getStatus())) {
            sumsqlsb.append("SUM(CASE WHEN (STATUS = ").append(dataBean.getStatus()).append(") THEN 1 ELSE 0 END ) as allnum ");
        } else {
            sumsqlsb.append("SUM(CASE WHEN (STATUS >= 26) THEN 1 ELSE 0 END ) as allnum ");
        }
        String strsqlsb = "select ouguid, " + sumsqlsb + "from audit_project where 1=1 ";
        StringBuilder conditionsqlsb = new StringBuilder();
        conditionsqlsb.append(generateSql(dataBean, applydateStart, applydateEnd, finishdateStart, finishdateEnd));
        StringBuilder oustr1sb = new StringBuilder();
        oustr1sb.append(")t RIGHT JOIN frame_ou o on t.ouguid = o.ouguid INNER JOIN frame_ou_extendinfo e on o.ouguid = e.ouguid where 1=1 ");

        if (StringUtil.isNotBlank(areacode)) {
            conditionsqlsb.append(" and areacode='").append(areacode, 0, 6).append("'");
        }
        if (CollectionUtils.isNotEmpty(searchOulist)) {
            oustr1sb.append(" and o.ouguid in ('").append(StringUtil.join(searchOulist, "','")).append("')");
        }
        conditionsqlsb.append(" GROUP BY OUGUID");
        oustr1sb.append(" ORDER BY t.allnum desc");
        String sql = oustr0sb.append(strsqlsb).append(conditionsqlsb).append(oustr1sb).toString();
        return baseDao.findList(sql, first, pageSize, Record.class);
    }


    public List<Record> findEvaluateDataList(EvainstanceCk dataBean, String applydateStart, String applydateEnd
            , String finishdateStart, String finishdateEnd, String areacode, List<String> searchOulist, int first, int pageSize) {
        StringBuilder oustr0sb = new StringBuilder();
        oustr0sb.append("select proDepart as ouname,count(1) as allnum from evainstance_ck where 1=1 ");
        StringBuilder conditionsqlsb = new StringBuilder();
        conditionsqlsb.append(generateCkSql(dataBean, applydateStart, applydateEnd, finishdateStart, finishdateEnd));
        StringBuilder oustr1sb = new StringBuilder();

        if (StringUtil.isNotBlank(areacode)) {
            conditionsqlsb.append(" and areacode='").append(areacode, 0, 6).append("'");
        }
        if (CollectionUtils.isNotEmpty(searchOulist)) {
            oustr1sb.append(" and deptcode in ('").append(StringUtil.join(searchOulist, "','")).append("')");
        }

        conditionsqlsb.append(" AND pf = '4'");

        conditionsqlsb.append(" GROUP BY deptcode");
        String sql = oustr0sb.append(oustr1sb).append(conditionsqlsb).toString();
        return baseDao.findList(sql, first, pageSize, Record.class);
    }

    public Integer getEvaluateDataCount(String areacode, List<String> searchOulist) {
        String sql = "select count(1) from evainstance_ck  " +
                "where 1 = 1 and pf = '4'";
        if (CollectionUtils.isNotEmpty(searchOulist)) {
            sql += " and areacode like ? and deptcode in ('" + StringUtil.join(searchOulist, "','") + "')";
            return baseDao.queryInt(sql, areacode + "%");
        } else {
            sql += " and areacode = ?";
            return baseDao.queryInt(sql, areacode);
        }
    }


    public Integer getAuditDataCount(String areacode, List<String> searchOulist) {
        String sql = "select count(1) from frame_ou o INNER JOIN frame_ou_extendinfo e on o.ouguid = e.ouguid " +
                "where 1 = 1 ";
        if (CollectionUtils.isNotEmpty(searchOulist)) {
            sql += " and areacode like ? and o.ouguid in ('" + StringUtil.join(searchOulist, "','") + "')";
            return baseDao.queryInt(sql, areacode + "%");
        } else {
            sql += " and areacode = ?";
            return baseDao.queryInt(sql, areacode);
        }
    }

    public int findEvaluateTotalDataCount(EvainstanceCk dataBean, String applydateStart, String applydateEnd
            , String finishdateStart, String finishdateEnd, String areacode, List<String> searchOulist) {
        StringBuilder strsqlsb = new StringBuilder();
        strsqlsb.append("select count(1) as allnum from evainstance_ck where 1=1 and pf = '4' ");
        StringBuilder conditionsb = new StringBuilder();
        conditionsb.append(generateCkSql(dataBean, applydateStart, applydateEnd
                , finishdateStart, finishdateEnd));
        if (StringUtil.isNotBlank(areacode)) {
            conditionsb.append(" and areacode='").append(areacode, 0, 6).append("'");
        }
        if (CollectionUtils.isNotEmpty(searchOulist)) {
            conditionsb.append(" and deptcode in ('").append(StringUtil.join(searchOulist, "','")).append("')");
        }
        conditionsb.append(" group by deptcode ");
        String sql = strsqlsb.append(conditionsb).toString();
        return baseDao.queryInt(sql);
    }


    public int findTotalDataCount(AuditProject dataBean, String applydateStart, String applydateEnd
            , String finishdateStart, String finishdateEnd, String areacode, List<String> searchOulist) {
        StringBuilder sumsqlsb = new StringBuilder();
        if (StringUtil.isNotBlank(dataBean.getStatus())) {
            sumsqlsb.append("ifnull(SUM(CASE WHEN (STATUS = ").append(dataBean.getStatus()).append(") THEN 1 ELSE 0 END ),0) as allnum ");
        } else {
            sumsqlsb.append("ifnull(SUM(CASE WHEN (STATUS >= 26) THEN 1 ELSE 0 END ),0) as allnum ");
        }
        StringBuilder strsqlsb = new StringBuilder();
        strsqlsb.append("select ").append(sumsqlsb).append("from audit_project where 1=1 ");
        StringBuilder conditionsb = new StringBuilder();
        conditionsb.append(generateSql(dataBean, applydateStart, applydateEnd
                , finishdateStart, finishdateEnd));
        if (StringUtil.isNotBlank(areacode)) {
            conditionsb.append(" and areacode='").append(areacode, 0, 6).append("'");
        }
        if (CollectionUtils.isNotEmpty(searchOulist)) {
            conditionsb.append(" and ouguid in ('").append(StringUtil.join(searchOulist, "','")).append("')");
        }
        String sql = strsqlsb.append(conditionsb).toString();
        return baseDao.queryInt(sql);
    }

    public String generateCkSql(EvainstanceCk dataBean, String applydateStart, String applydateEnd
            , String finishdateStart, String finishdateEnd) {
        StringBuilder conditionSb = new StringBuilder();
        // 申请时间条件判断
        if (StringUtil.isNotBlank(applydateStart) && StringUtil.isNotBlank(applydateEnd)) {
            conditionSb.append(" and CreateDATE >= '")
                    .append(EpointDateUtil.convertDate2String(EpointDateUtil.getBeginOfDateStr(applydateStart), "yyyy-MM-dd HH:mm:ss"))
                    .append("' and CreateDATE <= '")
                    .append(EpointDateUtil.convertDate2String(EpointDateUtil.getEndOfDateStr(applydateEnd), "yyyy-MM-dd HH:mm:ss"))
                    .append("' ");
        } else if (StringUtil.isNotBlank(applydateStart) && StringUtil.isBlank(applydateEnd)) {
            conditionSb.append(" and CreateDATE >= '")
                    .append(EpointDateUtil.convertDate2String(EpointDateUtil.getBeginOfDateStr(applydateStart), "yyyy-MM-dd HH:mm:ss"))
                    .append("' ");
        } else if (StringUtil.isBlank(applydateStart) && StringUtil.isNotBlank(applydateEnd)) {
            conditionSb.append(" and CreateDATE <= '")
                    .append(EpointDateUtil.convertDate2String(EpointDateUtil.getEndOfDateStr(applydateEnd), "yyyy-MM-dd HH:mm:ss"))
                    .append("' ");
        }
        // 办结时间条件判断
        if (StringUtil.isNotBlank(finishdateStart) && StringUtil.isNotBlank(finishdateEnd)) {
            conditionSb.append(" and CreateDATE >= '")
                    .append(EpointDateUtil.convertDate2String(EpointDateUtil.getBeginOfDateStr(finishdateStart), "yyyy-MM-dd HH:mm:ss"))
                    .append("' and CreateDATE <= '")
                    .append(EpointDateUtil.convertDate2String(EpointDateUtil.getEndOfDateStr(finishdateEnd), "yyyy-MM-dd HH:mm:ss"))
                    .append("' ");
        } else if (StringUtil.isNotBlank(finishdateStart) && StringUtil.isBlank(finishdateEnd)) {
            conditionSb.append(" and CreateDATE >= '")
                    .append(EpointDateUtil.convertDate2String(EpointDateUtil.getBeginOfDateStr(finishdateStart), "yyyy-MM-dd HH:mm:ss"))
                    .append("' ");
        } else if (StringUtil.isBlank(finishdateStart) && StringUtil.isNotBlank(finishdateEnd)) {
            conditionSb.append(" and CreateDATE <= '")
                    .append(EpointDateUtil.convertDate2String(EpointDateUtil.getEndOfDateStr(finishdateEnd), "yyyy-MM-dd HH:mm:ss"))
                    .append("' ");
        }
        return conditionSb.toString();
    }


    public String generateSql(AuditProject dataBean, String applydateStart, String applydateEnd
            , String finishdateStart, String finishdateEnd) {
        StringBuilder conditionSb = new StringBuilder();
        // 申请时间条件判断
        if (StringUtil.isNotBlank(applydateStart) && StringUtil.isNotBlank(applydateEnd)) {
            conditionSb.append(" and APPLYDATE >= '")
                    .append(EpointDateUtil.convertDate2String(EpointDateUtil.getBeginOfDateStr(applydateStart), "yyyy-MM-dd HH:mm:ss"))
                    .append("' and APPLYDATE <= '")
                    .append(EpointDateUtil.convertDate2String(EpointDateUtil.getEndOfDateStr(applydateEnd), "yyyy-MM-dd HH:mm:ss"))
                    .append("' ");
        } else if (StringUtil.isNotBlank(applydateStart) && StringUtil.isBlank(applydateEnd)) {
            conditionSb.append(" and APPLYDATE >= '")
                    .append(EpointDateUtil.convertDate2String(EpointDateUtil.getBeginOfDateStr(applydateStart), "yyyy-MM-dd HH:mm:ss"))
                    .append("' ");
        } else if (StringUtil.isBlank(applydateStart) && StringUtil.isNotBlank(applydateEnd)) {
            conditionSb.append(" and APPLYDATE <= '")
                    .append(EpointDateUtil.convertDate2String(EpointDateUtil.getEndOfDateStr(applydateEnd), "yyyy-MM-dd HH:mm:ss"))
                    .append("' ");
        }
        // 办结时间条件判断
        if (StringUtil.isNotBlank(finishdateStart) && StringUtil.isNotBlank(finishdateEnd)) {
            conditionSb.append(" and BANJIEDATE >= '")
                    .append(EpointDateUtil.convertDate2String(EpointDateUtil.getBeginOfDateStr(finishdateStart), "yyyy-MM-dd HH:mm:ss"))
                    .append("' and BANJIEDATE <= '")
                    .append(EpointDateUtil.convertDate2String(EpointDateUtil.getEndOfDateStr(finishdateEnd), "yyyy-MM-dd HH:mm:ss"))
                    .append("' ");
        } else if (StringUtil.isNotBlank(finishdateStart) && StringUtil.isBlank(finishdateEnd)) {
            conditionSb.append(" and BANJIEDATE >= '")
                    .append(EpointDateUtil.convertDate2String(EpointDateUtil.getBeginOfDateStr(finishdateStart), "yyyy-MM-dd HH:mm:ss"))
                    .append("' ");
        } else if (StringUtil.isBlank(finishdateStart) && StringUtil.isNotBlank(finishdateEnd)) {
            conditionSb.append(" and BANJIEDATE <= '")
                    .append(EpointDateUtil.convertDate2String(EpointDateUtil.getEndOfDateStr(finishdateEnd), "yyyy-MM-dd HH:mm:ss"))
                    .append("' ");
        }
        if (StringUtil.isNotBlank(dataBean.getApplyway())) {
            conditionSb.append(" and APPLYWAY = '").append(dataBean.getApplyway()).append("'");
        }
        if (StringUtil.isNotBlank(dataBean.getStatus())) {
            conditionSb.append(" and STATUS = ").append(dataBean.getStatus());
        } else {
            conditionSb.append(" and STATUS >= ").append(ZwfwConstant.BANJIAN_STATUS_YJJ);
        }
        return conditionSb.toString();
    }

    public List<AuditProject> getAuditProjectDataPageData(AuditProject dataBean, String applyDateStart
            , String applyDateEnd, String finishDateStart, String finishDateEnd, String areacode, List<String> ouguids
            , int first, int pageSize, String sortField, String sortOrder) {

        String condition = generateAPDSql(dataBean, applyDateStart, applyDateEnd, finishDateStart, finishDateEnd
                , areacode, ouguids, sortField, sortOrder);
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("select * from audit_project where 1 = 1 ").append(condition);
        String sql = sqlSb.toString();
        return baseDao.findList(sql, first, pageSize, AuditProject.class);
    }

    public int getAuditProjectDataCount(AuditProject dataBean, String applyDateStart, String applyDateEnd
            , String finishDateStart, String finishDateEnd, String areacode, List<String> ouguids) {
        String condition = generateAPDSql(dataBean, applyDateStart, applyDateEnd, finishDateStart, finishDateEnd
                , areacode, ouguids, null, null);
        StringBuilder sqlSb = new StringBuilder();
        sqlSb.append("select count(*) from audit_project where 1 = 1 ").append(condition);
        String sql = sqlSb.toString();
        return baseDao.queryInt(sql);
    }

    public String generateAPDSql(AuditProject dataBean, String applyDateStart
            , String applyDateEnd, String finishDateStart, String finishDateEnd, String areacode, List<String> ouguids
            , String sortField, String sortOrder) {
        StringBuilder conditionSql = new StringBuilder();
        conditionSql.append(generateSql(dataBean, applyDateStart, applyDateEnd
                , finishDateStart, finishDateEnd));
        if (StringUtil.isNotBlank(dataBean.getFlowsn())) {
            conditionSql.append(" and flowsn like '%").append(dataBean.getFlowsn()).append("%'");
        }
        if (StringUtil.isNotBlank(dataBean.getProjectname())) {
            conditionSql.append(" and projectname like '%").append(dataBean.getProjectname()).append("%'");
        }
        if (StringUtil.isNotBlank(dataBean.getReceiveusername())) {
            conditionSql.append(" and receiveusername like '%").append(dataBean.getReceiveusername()).append("%'");
        }
        if (StringUtil.isNotBlank(areacode)) {
            conditionSql.append(" and AREACODE = ").append(areacode, 0, 6);
        }
        if (CollectionUtils.isNotEmpty(ouguids)) {
            String ouguidStr = String.join("','", ouguids);
            ouguidStr = "'" + ouguidStr + "'";
            conditionSql.append(" and OUGUID in (").append(ouguidStr).append(")");
        }
        if (StringUtil.isNotBlank(sortField)) {
            if (StringUtil.isNotBlank(sortOrder)) {
                conditionSql.append(" order by ").append(sortField).append(" ").append(sortOrder);
            } else {
                conditionSql.append(" order by APPLYDATE desc");
            }
        }
        return conditionSql.toString();
    }


    public List<String> findOUGuidList(String areacode, String leftTreeNodeGuid) {
        String sql = "";
        if (areacode.length() == 6) {
            sql = "select ouguid from frame_ou fo where OUGUID in(" +
                    "select distinct ouguid from frame_ou_extendinfo foe where" +
                    " AREACODE like concat(?,'%') and length(areacode) <= 9 )";
        } else {
            String sql2 = "select * from frame_ou where PARENTOUGUID = ?";
            List<FrameOu> list = baseDao.findList(sql2, FrameOu.class, leftTreeNodeGuid);
            if (CollectionUtils.isEmpty(list)) {
                List<String> list2 = new ArrayList<>();
                list2.add(leftTreeNodeGuid);
                return list2;
            } else {
                sql = "select ouguid from frame_ou fo where OUGUID in(" +
                        "select distinct ouguid from frame_ou_extendinfo foe where" +
                        " AREACODE like concat(?,'%') )";
            }

        }
        return baseDao.findList(sql, String.class, areacode);
    }

    public void updateCrtInfo(String certrowguid) {
        String sql = "update cert_info set ISHISTORY = '1' where rowguid = ?";
        baseDao.execute(sql, certrowguid);
    }

    public List<Record> getSpglDantiInfoBySubappguid(String projectguid) {
        String sql = "SELECT " +
                "a.*" +
                " FROM danti_sub_relation b " +
                " JOIN danti_info a ON b.DANTIGUID = a.rowguid " +
                " inner join audit_project d on b.subappguid = d.subappguid " +
                " where d.rowguid = ?";
        return baseDao.findList(sql, Record.class, projectguid);
    }

    public AuditProject getAuditProjectByCertrowguid(String certrowguid) {
        String sql = "select subappguid,Projectname from audit_project where certrowguid = ?";
        return baseDao.find(sql, AuditProject.class, certrowguid);
    }


    public Integer getLcprojectTwoListCount(Map<String,String> map) {
        return new SQLManageUtil().getListCount(lcprojecttwo.class,map);
    }
    public Integer getLcprojectTenListCount(Map<String,String> map) {
        return new SQLManageUtil().getListCount(lcprojectten.class,map);
    }

}
