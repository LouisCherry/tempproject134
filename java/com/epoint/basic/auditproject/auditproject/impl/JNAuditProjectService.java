package com.epoint.basic.auditproject.auditproject.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditprojectoperation.domain.AuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.basedata.participantsinfo.api.entity.ParticipantsInfo;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.DbKit;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.Parameter;
import com.epoint.database.jdbc.config.SplitTableConfig;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.sharding.util.ShardingUtil;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;

public class JNAuditProjectService {
    /**
     * 通用dao
     */
    private ICommonDao commonDao;

    public JNAuditProjectService() {
        commonDao = CommonDao.getInstance("project");

    }

    public JNAuditProjectService(Class<? extends BaseEntity> baseClass) {
        Entity en = baseClass.getAnnotation(Entity.class);

        SplitTableConfig conf = ShardingUtil.getSplitTableConfig(en.table());
        if (conf != null) {
            commonDao = CommonDao.getInstance(conf);
        } else {
            commonDao = CommonDao.getInstance();
        }
    }

    /**
     * 新增某条记录
     *
     * @param baseClass
     * @param record
     * @param useCache
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void addRecord(Class<? extends BaseEntity> baseClass, Record record) {

        Entity en = baseClass.getAnnotation(Entity.class);
        if (StringUtil.isNotBlank(en.table())) {
            commonDao.insert(record);
        }

    }

    /**
     * 更新某条记录
     *
     * @param baseClass
     * @param record
     * @param key
     * @param useCache
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void updateRecord(Class<? extends BaseEntity> baseClass, Record record) {
        Entity en = baseClass.getAnnotation(Entity.class);
        if (StringUtil.isNotBlank(en.table())) {
            commonDao.update(record);
        }
    }

    public void deleteRecods(Class<? extends BaseEntity> baseClass, String keyValue, String key) {
        Entity en = baseClass.getAnnotation(Entity.class);
        if (StringUtil.isNotBlank(en.table())) {
            // 直接拼接删除语句进行处理
            String sql = "delete from " + en.table() + " where " + key + "=?";
            commonDao.execute(sql, keyValue);
        }
    }

    public void deleteRecodByMap(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap) {
        Entity en = baseClass.getAnnotation(Entity.class);
        if (StringUtil.isNotBlank(en.table())) {
            String sql = "delete from " + en.table();
            SQLManageUtil sqlManageUtil = new SQLManageUtil("project", true);
            sql += sqlManageUtil.buildSql(conditionMap);
            commonDao.execute(sql);
        }
    }

    /**
     * 获取某条记录
     *
     * @param baseClass
     * @param rowGuid
     * @param key
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("unchecked")
    public <T> T getDetail(String fieldstr, Class<? extends BaseEntity> baseClass, String rowGuid, String key) {
        String sql = "";
        Entity en = baseClass.getAnnotation(Entity.class);
        if (StringUtil.isNotBlank(en.table())) {
            // 直接拼接查询语句进行处理
            sql = "select " + fieldstr + " from " + en.table() + " where " + key + "=?";
        }
        if (StringUtil.isNotBlank(sql)) {
            return (T) commonDao.find(sql, baseClass, rowGuid);
        } else {
            return null;
        }
    }

    /**
     * 获取某条记录
     *
     * @param baseClass
     * @param conditonMap
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("unchecked")
    public <T> T getDetail(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(baseClass);
        return (T) sqlManageUtil.getBeanByCondition(baseClass, conditionMap);
    }

    public <T> List<T> selectRecordList(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(baseClass);
        return sqlManageUtil.getListByCondition(baseClass, conditionMap);
    }

    public void updateRecord(Class<? extends BaseEntity> baseClass, Map<String, String> updateFieldMap,
                             Map<String, String> conditionMap) {
        String sql = "";
        Entity en = baseClass.getAnnotation(Entity.class);
        if (StringUtil.isNotBlank(en.table()) && !updateFieldMap.isEmpty()) {
            // 直接拼接查询语句进行处理
            sql = "update " + en.table() + " set ";
            for (Map.Entry<String, String> entry : updateFieldMap.entrySet()) {
                sql += entry.getKey() + "'" + entry.getValue() + "',";
                // 如果是补正修改isbuzheng字段的值
                if (ZwfwConstant.Material_AuditStatus_DBZ.equals(entry.getValue())) {
                    sql += " isbuzheng='1',";
                }
            }
            sql = sql.substring(0, sql.length() - 1);
            SQLManageUtil sqlManageUtil = new SQLManageUtil(baseClass);
            sql += sqlManageUtil.buildSql(conditionMap);
            commonDao.execute(sql);
        }
    }

    // 获取外网大厅的操作记录
    public List<AuditProjectOperation> getAuditProjectOperationFieldList(String fieldstr, String whereStr,
                                                                         String grouporsortstr) {
        String sql = "select " + fieldstr + " from Audit_Project_Operation where " + whereStr + grouporsortstr;
        List<AuditProjectOperation> findList = commonDao.findList(sql, AuditProjectOperation.class);
        return findList;
    }

    // 获取外网大厅的操作记录
    public AuditProjectOperation getAuditProjectOperationField(String fieldstr, String whereStr,
                                                               String grouporsortstr) {
        String sql = "select " + fieldstr + " from Audit_Project_Operation where " + whereStr + grouporsortstr;
        AuditProjectOperation findList = commonDao.find(sql, AuditProjectOperation.class);
        return findList;
    }

    /**
     * 查找 通过windowguid
     *
     * @param fieldstr       查询的字段（,分割）
     * @param grouporsortstr 分组的sql语句（group by开头）
     * @param wherestr       过滤条件
     * @param windowguid     窗口guid
     */
    public List<AuditProject> getAuditProjectFieldList(String fieldstr, String grouporsortstr, String intaskguids) {
        String sql = "select " + fieldstr + " from audit_project where " + intaskguids + grouporsortstr;
        List<AuditProject> findList = commonDao.findList(sql, AuditProject.class);
        return findList;
    }

    /**
     * 查找 通过windowguid
     *
     * @param fieldstr       查询的字段（,分割）
     * @param grouporsortstr 分组的sql语句（group by开头）
     * @param wherestr       过滤条件
     * @param windowguid     窗口guid
     */
    public AuditProject getAuditProjectField(String fieldstr, String grouporsortstr, String whereStr) {
        String sql = "select " + fieldstr + " from audit_project where " + whereStr + grouporsortstr;
        AuditProject findList = commonDao.find(sql, AuditProject.class);
        return findList;
    }

    /**
     * 获取办件列表 带分页
     *
     * @param projectType 办件类型
     * @param windowguid  窗口id
     * @param first       分页条件
     * @param pageSize    分页条件
     * @param pageSize    申请人姓名（选填）
     */

    public PageData<AuditProject> getBanJianListByPage(String fieldstr, String projectType, String taskguids, int first,
                                                       int pageSize, String areaCode, String applyerName) {
        int BANJIAN_STATUS = 0;
        if (StringUtil.isBlank(applyerName)) {
            applyerName = "";
        }
        if ("DJJ".equals(projectType)) {
            BANJIAN_STATUS = ZwfwConstant.BANJIAN_STATUS_DJJ;
        } else if ("DSL".equals(projectType)) {
            BANJIAN_STATUS = ZwfwConstant.BANJIAN_STATUS_YJJ;
        } else if ("DBB".equals(projectType)) {
            BANJIAN_STATUS = ZwfwConstant.BANJIAN_STATUS_DBB;
        } else if ("DYS".equals(projectType)) {
            BANJIAN_STATUS = ZwfwConstant.BANJIAN_STATUS_WWYTJ;
        } else if ("YSTG".equals(projectType)) {
            BANJIAN_STATUS = ZwfwConstant.BANJIAN_STATUS_WWYSTG;
        }
        List<Object> params = new ArrayList<Object>();
        String sql = "";
        String sqlCount = "";
        if ("".equals(taskguids) || taskguids == null) {
            sql = "";
        } else {
            if (StringUtil.isNotBlank(projectType) && "YZT".equals(projectType)) {
                sql = "select " + fieldstr
                        + " from audit_project where status<90 and areacode=? and  is_pause = '1' and taskguid in ";
            } else if (StringUtil.isNotBlank(projectType) && "DBJ".equals(projectType)) {
                sql = "select " + fieldstr + " from audit_project where  areacode=? " + " and status>='"
                        + ZwfwConstant.BANJIAN_STATUS_YSLDBJ + "' and status <='" + ZwfwConstant.BANJIAN_STATUS_SPTG
                        + "' and taskguid in ";
            } else if (StringUtil.isNotBlank(projectType) && "DBB".equals(projectType)) {
                sql = "select " + fieldstr + " from audit_project where  areacode=? and status in ("
                        + ZwfwConstant.BANJIAN_STATUS_DBB + "," + ZwfwConstant.BANJIAN_STATUS_YSLDBB
                        + ") and taskguid in ";
            } else {
                sql = "select " + fieldstr + " from audit_project where  areacode=? and status=" + BANJIAN_STATUS
                        + " and taskguid in ";
            }
            params.add(areaCode);
            Parameter pa = DbKit.splitIn(taskguids);
            sql += pa.getSql();
            for (Object obj : pa.getValue()) {
                params.add(obj);
            }
            sql += pa.getSql();
            if (StringUtil.isNotBlank(applyerName)) {
                sql += "and applyerName like ? ";
                params.add("%" + applyerName.replace("\\", "\\\\").replace("%", "\\%") + "%");
            }
            sql += " order by OperateDate desc";
        }
        List<AuditProject> list = commonDao.findList(sql, first, pageSize, AuditProject.class, params.toArray());
        sqlCount = sql.replace("select " + fieldstr + " from", "select count(1) from");
        int count = 0;
        if ("".equals(taskguids) || taskguids == null) {
            count = 0;
        } else {
            count = commonDao.queryInt(sqlCount, params.toArray());
        }
        PageData<AuditProject> pageData = new PageData<>();
        pageData.setList(list);
        pageData.setRowCount(count);
        return pageData;
    }

    /**
     * 获取办件列表 带分页(根据TaskID获取)
     *
     * @param projectType 办件类型
     * @param windowguid  窗口id
     * @param first       分页条件
     * @param pageSize    分页条件
     * @param pageSize    申请人姓名（选填）
     */
    public PageData<AuditProject> getBanJianListByPageAndTaskids(String fieldstr, String projectType, String taskids,
                                                                 int first, int pageSize, String areaCode, String applyerName, Date datestart, Date dateend,
                                                                 String windowguid) {
        int BANJIAN_STATUS = 0;
        if (StringUtil.isBlank(applyerName)) {
            applyerName = "";
        }
        if ("DJJ".equals(projectType)) {
            BANJIAN_STATUS = ZwfwConstant.BANJIAN_STATUS_DJJ;
        } else if ("DSL".equals(projectType)) {
            BANJIAN_STATUS = ZwfwConstant.BANJIAN_STATUS_YJJ;
        } else if ("DBB".equals(projectType)) {
            BANJIAN_STATUS = ZwfwConstant.BANJIAN_STATUS_DBB;
        } else if ("DYS".equals(projectType)) {
            BANJIAN_STATUS = ZwfwConstant.BANJIAN_STATUS_WWYTJ;
        } else if ("YSTG".equals(projectType)) {
            BANJIAN_STATUS = ZwfwConstant.BANJIAN_STATUS_WWYSTG;
        }
        String sql = "";
        String sqlCount = "";
        String sqlExt = "";
        String formatDateStart = null;
        String formatDateEnd = null;
        List<Object> params = new ArrayList<Object>();
        Parameter pa = DbKit.splitIn(taskids);
        if (commonDao.isMySql()) {
            if (datestart != null) {
                formatDateStart = "'" + EpointDateUtil.convertDate2String(datestart, "yyyy-MM-dd HH:mm:ss") + "'";
            }
            if (dateend != null) {
                formatDateEnd = "'" + EpointDateUtil.convertDate2String(dateend, "yyyy-MM-dd HH:mm:ss") + "'";
            }
            sqlExt = "and (windowguid is null or windowguid='' or windowguid=? )";
        }
        if (commonDao.isOracle()) {
            if (datestart != null) {
                formatDateStart = "to_date('" + EpointDateUtil.convertDate2String(datestart, "yyyy-MM-dd HH:mm:ss")
                        + "','yyyy-MM-dd hh24:mi:ss')";
            }
            if (dateend != null) {
                formatDateEnd = "to_date('" + EpointDateUtil.convertDate2String(dateend, "yyyy-MM-dd HH:mm:ss")
                        + "','yyyy-MM-dd hh24:mi:ss')";
            }
            sqlExt = "and (windowguid is null or windowguid=? )";
        }
        if (commonDao.isDM()) {
            if (datestart != null) {
                formatDateStart = "to_date('" + EpointDateUtil.convertDate2String(datestart, "yyyy-MM-dd HH:mm:ss")
                        + "','yyyy-MM-dd hh24:mi:ss')";
            }
            if (dateend != null) {
                formatDateEnd = "to_date('" + EpointDateUtil.convertDate2String(dateend, "yyyy-MM-dd HH:mm:ss")
                        + "','yyyy-MM-dd hh24:mi:ss')";
            }
            sqlExt = "and (windowguid is null or windowguid=? )";
        }
        if ("".equals(taskids) || taskids == null) {
            sql = "";
        } else {
            if (StringUtil.isNotBlank(projectType) && "YZT".equals(projectType)) {
                sql = "select " + fieldstr + " from audit_project where areacode=? and  is_pause = '1' and task_id in ";
                params.add(areaCode);
                sql += pa.getSql();
                for (Object obj : pa.getValue()) {
                    params.add(obj);
                }
                if (datestart != null) {
                    sql += "and acceptuserdate > ? ";
                    params.add(formatDateStart);
                }
                if (dateend != null) {
                    sql += "and acceptuserdate < ? ";
                    params.add(formatDateEnd);
                }
            } else if (StringUtil.isNotBlank(projectType) && "DBJ".equals(projectType)) {
                sql = "select " + fieldstr + " from audit_project where  areacode=? " + " and status>='"
                        + ZwfwConstant.BANJIAN_STATUS_YSLDBJ + "' and status <='" + ZwfwConstant.BANJIAN_STATUS_SPTG
                        + "' and task_id in ";
                params.add(areaCode);
                sql += pa.getSql();
                for (Object obj : pa.getValue()) {
                    params.add(obj);
                }
                if (datestart != null) {
                    sql += "and promiseenddate > ? ";
                    params.add(formatDateStart);
                }
                if (dateend != null) {
                    sql += "and promiseenddate < ? ";
                    params.add(formatDateEnd);
                }
            } else if (StringUtil.isNotBlank(projectType) && "DBB".equals(projectType)) {
                sql = "select " + fieldstr + " from audit_project where  areacode=? and status in ("
                        + ZwfwConstant.BANJIAN_STATUS_DBB + "," + ZwfwConstant.BANJIAN_STATUS_YSLDBB
                        + ") and task_id in ";
                params.add(areaCode);
                sql += pa.getSql();
                for (Object obj : pa.getValue()) {
                    params.add(obj);
                }
                if (datestart != null) {
                    sql += "and bubandate > ? ";
                    params.add(formatDateStart);
                }
                if (dateend != null) {
                    sql += "and bubandate < ? ";
                    params.add(formatDateEnd);
                }
            } else if (StringUtil.isNotBlank(projectType) && "DSL".equals(projectType)) {
                sql = "select " + fieldstr + " from audit_project where  areacode=? and status=" + BANJIAN_STATUS
                        + " and task_id in ";
                params.add(areaCode);
                sql += pa.getSql();
                for (Object obj : pa.getValue()) {
                    params.add(obj);
                }
                if (datestart != null) {
                    sql += "and RECEIVEDATE > ? ";
                    params.add(formatDateStart);
                }
                if (dateend != null) {
                    sql += "and RECEIVEDATE < ? ";
                    params.add(formatDateEnd);
                }
            } else if (StringUtil.isNotBlank(projectType) && "DJJ".equals(projectType)) {
                sql = "select " + fieldstr + " from audit_project where areacode = ? and status=" + BANJIAN_STATUS
                        + " and task_id in ";
                params.add(areaCode);
                sql += pa.getSql();
                for (Object obj : pa.getValue()) {
                    params.add(obj);
                }
                if (datestart != null) {
                    sql += "and applydate > ? ";
                    params.add(formatDateStart);
                }
                if (dateend != null) {
                    sql += "and applydate < ? ";
                    params.add(formatDateEnd);
                }
            } else if (StringUtil.isNotBlank(projectType) && "DYS".equals(projectType)) {
                sql = "select " + fieldstr + " from audit_project where  areacode=? and status=" + BANJIAN_STATUS
                        + " and task_id in ";
                params.add(areaCode);
                sql += pa.getSql();
                for (Object obj : pa.getValue()) {
                    params.add(obj);
                }
                if (datestart != null) {
                    sql += "and applydate > ? ";
                    params.add(formatDateStart);
                }
                if (dateend != null) {
                    sql += "and applydate < ? ";
                    params.add(formatDateEnd);
                }
            } else if (StringUtil.isNotBlank(projectType) && "YSTG".equals(projectType)) {
                sql = "select " + fieldstr + " from audit_project where  areacode=? and status=" + BANJIAN_STATUS
                        + " and task_id in ";
                params.add(areaCode);
                sql += pa.getSql();
                for (Object obj : pa.getValue()) {
                    params.add(obj);
                }
                if (datestart != null) {
                    sql += "and yushendate > ? ";
                    params.add(formatDateStart);
                }
                if (dateend != null) {
                    sql += "and yushendate < ? ";
                    params.add(formatDateEnd);
                }
            } else {
                sql = "select " + fieldstr + " from audit_project where  areacode=? and status=" + BANJIAN_STATUS
                        + " and task_id in ";
                params.add(areaCode);
                sql += pa.getSql();
                for (Object obj : pa.getValue()) {
                    params.add(obj);
                }
            }
            if (StringUtil.isNotBlank(applyerName)) {
                sql += "and applyerName like ? ";
                params.add("%" + applyerName.replace("\\", "\\\\").replace("%", "\\%") + "%");
            }
            sql += sqlExt + " order by OperateDate desc";
            params.add(windowguid);
        }
        List<AuditProject> list = commonDao.findList(sql, first, pageSize, AuditProject.class, params.toArray());
        sqlCount = sql.replace("select " + fieldstr + " from", "select count(1) from");
        int count = 0;
        if ("".equals(taskids) || taskids == null) {
            count = 0;
        } else {
            count = commonDao.queryInt(sqlCount, params.toArray());
        }
        PageData<AuditProject> pageData = new PageData<>();
        pageData.setList(list);
        pageData.setRowCount(count);
        return pageData;
    }

    /**
     * 获取办件列表 带分页(根据TaskID获取)
     *
     * @param projectType 办件类型
     * @param windowguid  窗口id
     * @param first       分页条件
     * @param pageSize    分页条件
     * @param pageSize    申请人姓名（选填）
     */
    public PageData<AuditProject> getBanJianListByPageAndTaskidsAndCenterGuid(String fieldstr, String projectType,
                                                                              String taskids, int first, int pageSize, String areaCode, String centerGuid, String applyerName,
                                                                              Date datestart, Date dateend, String windowguid) {
        int BANJIAN_STATUS = 0;
        if (StringUtil.isBlank(applyerName)) {
            applyerName = "";
        }
        if ("DJJ".equals(projectType)) {
            BANJIAN_STATUS = ZwfwConstant.BANJIAN_STATUS_DJJ;
        } else if ("DSL".equals(projectType)) {
            BANJIAN_STATUS = ZwfwConstant.BANJIAN_STATUS_YJJ;
        } else if ("DBB".equals(projectType)) {
            BANJIAN_STATUS = ZwfwConstant.BANJIAN_STATUS_DBB;
        } else if ("DYS".equals(projectType)) {
            BANJIAN_STATUS = ZwfwConstant.BANJIAN_STATUS_WWYTJ;
        } else if ("YSTG".equals(projectType)) {
            BANJIAN_STATUS = ZwfwConstant.BANJIAN_STATUS_WWYSTG;
        }

        String sql = "";
        String sqlCount = "";
        String sqlExt = "";
        String formatDateStart = null;
        String formatDateEnd = null;
        List<Object> params = new ArrayList<Object>();
        if (commonDao.isMySql()) {
            if (datestart != null) {
                formatDateStart = "'" + EpointDateUtil.convertDate2String(datestart, "yyyy-MM-dd HH:mm:ss") + "'";
            }
            if (dateend != null) {
                formatDateEnd = "'" + EpointDateUtil.convertDate2String(dateend, "yyyy-MM-dd HH:mm:ss") + "'";
            }
            // sqlExt = "and (windowguid is null or windowguid='' or
            // windowguid='" + windowguid + "')"+ "and (currentareacode
            // ='"+areaCode+"' or currentareacode is null or currentareacode
            // ='')";
            sqlExt = "and( (currentareacode = ? or currentareacode is null or  currentareacode ='')"
                    + " and( (centerGuid=? and( acceptareacode is null or acceptareacode='')) or acceptareacode is not null))";
        }
        if (commonDao.isOracle()) {
            if (datestart != null) {
                formatDateStart = "to_date('" + EpointDateUtil.convertDate2String(datestart, "yyyy-MM-dd HH:mm:ss")
                        + "','yyyy-MM-dd hh24:mi:ss')";
            }
            if (dateend != null) {
                formatDateEnd = "to_date('" + EpointDateUtil.convertDate2String(dateend, "yyyy-MM-dd HH:mm:ss")
                        + "','yyyy-MM-dd hh24:mi:ss')";
            }
            // sqlExt = "and (windowguid is null or windowguid='" + windowguid +
            // "')"+ "and (currentareacode ='"+areaCode+"' or currentareacode is
            // null)";
            sqlExt = "and( (currentareacode = ? or currentareacode is null)"
                    + " and( (centerGuid= ? and acceptareacode is null) or acceptareacode is not null))";
        }
        if (commonDao.isDM()) {
            if (datestart != null) {
                formatDateStart = "to_date('" + EpointDateUtil.convertDate2String(datestart, "yyyy-MM-dd HH:mm:ss")
                        + "','yyyy-MM-dd hh24:mi:ss')";
            }
            if (dateend != null) {
                formatDateEnd = "to_date('" + EpointDateUtil.convertDate2String(dateend, "yyyy-MM-dd HH:mm:ss")
                        + "','yyyy-MM-dd hh24:mi:ss')";
            }
            // sqlExt = "and (windowguid is null or windowguid='" + windowguid +
            // "')"+ "and (currentareacode ='"+areaCode+"' or currentareacode is
            // null)";
            sqlExt = "and( (currentareacode =? or currentareacode is null)"
                    + " and( (centerGuid=? and acceptareacode is null) or acceptareacode is not null))";
        }
        // String XZareacode = areaCode;
        if ("".equals(taskids) || taskids == null) {
            sql = "";
        } else {
            if (StringUtil.isNotBlank(projectType) && "YZT".equals(projectType)) {
                sql = "select " + fieldstr + " from audit_project where areaCode=? and  is_pause = '1' and task_id in ";
                params.add(areaCode);
                Parameter pa = DbKit.splitIn(taskids);
                sql += pa.getSql();
                for (Object obj : pa.getValue()) {
                    params.add(obj);
                }
                if (datestart != null) {
                    sql += "and acceptuserdate > ? ";
                    params.add(formatDateStart);
                }
                if (dateend != null) {
                    sql += "and acceptuserdate < ? ";
                    params.add(formatDateEnd);
                }
            } else if (StringUtil.isNotBlank(projectType) && "DBJ".equals(projectType)) {
                sql = "select " + fieldstr + " from audit_project where  areaCode=? " + " and status>='"
                        + ZwfwConstant.BANJIAN_STATUS_YSLDBJ + "' and status <='" + ZwfwConstant.BANJIAN_STATUS_SPTG
                        + "' and task_id in ";
                params.add(areaCode);
                Parameter pa = DbKit.splitIn(taskids);
                sql += pa.getSql();
                for (Object obj : pa.getValue()) {
                    params.add(obj);
                }
                if (datestart != null) {
                    sql += "and promiseenddate > ? ";
                    params.add(formatDateStart);
                }
                if (dateend != null) {
                    sql += "and promiseenddate < ? ";
                    params.add(formatDateEnd);
                }
            } else if (StringUtil.isNotBlank(projectType) && "DBB".equals(projectType)) {
                sql = "select " + fieldstr + " from audit_project where  areaCode=? and status in ("
                        + ZwfwConstant.BANJIAN_STATUS_DBB + "," + ZwfwConstant.BANJIAN_STATUS_YSLDBB
                        + ") and task_id in ";
                params.add(areaCode);
                Parameter pa = DbKit.splitIn(taskids);
                sql += pa.getSql();
                for (Object obj : pa.getValue()) {
                    params.add(obj);
                }
                if (datestart != null) {
                    sql += "and bubandate > ? ";
                    params.add(formatDateStart);
                }
                if (dateend != null) {
                    sql += "and bubandate < ? ";
                    params.add(formatDateEnd);
                }
            } else if (StringUtil.isNotBlank(projectType) && "DSL".equals(projectType)) {
                sql = "select " + fieldstr + " from audit_project where  areaCode= ? and status=" + BANJIAN_STATUS
                        + " and task_id in ";
                params.add(areaCode);
                Parameter pa = DbKit.splitIn(taskids);
                sql += pa.getSql();
                for (Object obj : pa.getValue()) {
                    params.add(obj);
                }
                if (datestart != null) {
                    sql += "and RECEIVEDATE > ? ";
                    params.add(formatDateStart);
                }
                if (dateend != null) {
                    sql += "and RECEIVEDATE < ? ";
                    params.add(formatDateEnd);
                }
            } else if (StringUtil.isNotBlank(projectType) && "DJJ".equals(projectType)) {
                sql = "select " + fieldstr + " from audit_project where  areaCode= ? and status=" + BANJIAN_STATUS
                        + " and task_id in ";
                params.add(areaCode);
                Parameter pa = DbKit.splitIn(taskids);
                sql += pa.getSql();
                for (Object obj : pa.getValue()) {
                    params.add(obj);
                }
                if (datestart != null) {
                    sql += "and applydate > ? ";
                    params.add(formatDateStart);
                }
                if (dateend != null) {
                    sql += "and applydate < ? ";
                    params.add(formatDateEnd);
                }
            } else if (StringUtil.isNotBlank(projectType) && "DYS".equals(projectType)) {
                sql = "select " + fieldstr + " from audit_project where  areaCode=? and status=" + BANJIAN_STATUS
                        + " and task_id in ";
                params.add(areaCode);
                Parameter pa = DbKit.splitIn(taskids);
                sql += pa.getSql();
                for (Object obj : pa.getValue()) {
                    params.add(obj);
                }
                if (datestart != null) {
                    sql += "and applydate > ? ";
                    params.add(formatDateStart);
                }
                if (dateend != null) {
                    sql += "and applydate < ? ";
                    params.add(formatDateEnd);
                }
            } else if (StringUtil.isNotBlank(projectType) && "YSTG".equals(projectType)) {
                sql = "select " + fieldstr + " from audit_project where  areaCode=? and status=" + BANJIAN_STATUS
                        + " and task_id in ";
                params.add(areaCode);
                Parameter pa = DbKit.splitIn(taskids);
                sql += pa.getSql();
                for (Object obj : pa.getValue()) {
                    params.add(obj);
                }
                if (datestart != null) {
                    sql += "and yushendate > ? ";
                    params.add(formatDateStart);
                }
                if (dateend != null) {
                    sql += "and yushendate < ? ";
                    params.add(formatDateEnd);
                }
            } else {
                sql = "select " + fieldstr + " from audit_project where  areaCode=?  and status=" + BANJIAN_STATUS
                        + " and task_id in ? ";
                params.add(areaCode);
                Parameter pa = DbKit.splitIn(taskids);
                sql += pa.getSql();
                for (Object obj : pa.getValue()) {
                    params.add(obj);
                }
            }
            if (StringUtil.isNotBlank(applyerName)) {
                sql += "and applyerName like ? ";
                params.add("%" + applyerName.replace("\\", "\\\\").replace("%", "\\%") + "%");
            }
            // if (!StringUtil.isNotBlank(projectType) &&
            // projectType.equals("DBJ")){
            // sql += sqlExt +" order by OperateDate desc";
            // }else{
            sql += sqlExt + " order by OperateDate desc";
            params.add(areaCode);
            params.add(centerGuid);
            // }

        }
        List<AuditProject> list = commonDao.findList(sql, first, pageSize, AuditProject.class, params.toArray());
        sqlCount = sql.replace("select " + fieldstr + " from", "select count(1) from");
        int count = 0;
        if ("".equals(taskids) || taskids == null) {
            count = 0;
        } else {
            count = commonDao.queryInt(sqlCount, params.toArray());
        }
        PageData<AuditProject> pageData = new PageData<>();
        pageData.setList(list);
        pageData.setRowCount(count);
        return pageData;
    }

    /**
     * 获取办件列表 带分页(根据TaskID获取)
     *
     * @param projectType 办件类型
     * @param windowguid  窗口id
     * @param first       分页条件
     * @param pageSize    分页条件
     * @param pageSize    申请人姓名（选填）
     */
    public PageData<AuditProject> getBanJianListByPageAndTaskidsAndCenterGuidAndAreacode(String fieldstr,
                                                                                         String projectType, String taskids, int first, int pageSize, String areaCode, String centerGuid,
            String applyerName, Date datestart, Date dateend, String windowguid, String realAreacode,
            String projectname, String commitnum, String sortField, String sortOrder) {
        int BANJIAN_STATUS = 0;
        if (StringUtil.isBlank(applyerName)) {
            applyerName = "";
        }
        if ("DJJ".equals(projectType)) {
            BANJIAN_STATUS = ZwfwConstant.BANJIAN_STATUS_DJJ;
        } else if ("DSL".equals(projectType)) {
            BANJIAN_STATUS = ZwfwConstant.BANJIAN_STATUS_YJJ;
        } else if ("DBB".equals(projectType)) {
            BANJIAN_STATUS = ZwfwConstant.BANJIAN_STATUS_DBB;
        } else if ("DYS".equals(projectType)) {
            BANJIAN_STATUS = ZwfwConstant.BANJIAN_STATUS_WWYTJ;
        } else if ("YSTG".equals(projectType)) {
            BANJIAN_STATUS = ZwfwConstant.BANJIAN_STATUS_WWYSTG;
        } else if ("YSTU".equals(projectType)) {
            BANJIAN_STATUS = ZwfwConstant.BANJIAN_STATUS_WWYSTU;
        }
        String sql = "";
        String sqlCount = "";
        String sqlExt = "";
        String formatDateStart = null;
        String formatDateEnd = null;
        List<Object> params = new ArrayList<Object>();
        String datestartsql = "? ";
        String dateendsql = "? ";
        if (datestart != null) {
            formatDateStart = EpointDateUtil.convertDate2String(datestart, "yyyy-MM-dd HH:mm:ss");
        }
        if (dateend != null) {
            formatDateEnd = EpointDateUtil.convertDate2String(dateend, "yyyy-MM-dd HH:mm:ss");
        }
        if (commonDao.isMySql()) {
            sqlExt = "and( (currentareacode =? or currentareacode is null or  currentareacode ='')"
                    + " and( ((centerGuid=? or centerGuid is null or centerGuid='')and( acceptareacode is null or acceptareacode='')) or acceptareacode is not null))";

        }
        if (commonDao.isOracle()) {
            if (datestart != null) {
                datestartsql = "to_date(?,'yyyy-MM-dd hh24:mi:ss') ";
            }
            if (dateend != null) {
                dateendsql = "to_date(?,'yyyy-MM-dd hh24:mi:ss') ";
            }
            sqlExt = "and( (currentareacode =? or currentareacode is null)"
                    + " and( ( (centerGuid=? or centerGuid is null) and acceptareacode is null) or acceptareacode is not null))";
        }
        if (commonDao.isDM()) {
            if (datestart != null) {
                datestartsql = "to_date(?,'yyyy-MM-dd hh24:mi:ss') ";
            }
            if (dateend != null) {
                dateendsql = "to_date(?,'yyyy-MM-dd hh24:mi:ss') ";
            }
            sqlExt = "and( (currentareacode =? or currentareacode is null)"
                    + " and( ( ( centerGuid=? or centerGuid is null) and acceptareacode is null) or acceptareacode is not null))";
        }

        if ("".equals(taskids) || taskids == null) {
            sql = "";
        } else {
            if (StringUtil.isNotBlank(projectType) && "YZT".equals(projectType)) {
                sql = "select " + fieldstr + " from audit_project where status<90 and areaCode=? and  is_pause = '1' and task_id in ";
                params.add(areaCode);
                Parameter pa = DbKit.splitIn(taskids);
                sql += pa.getSql();
                for (Object obj : pa.getValue()) {
                    params.add(obj);
                }
                if (datestart != null) {
                    sql += "and acceptuserdate > " + datestartsql;
                    params.add(formatDateStart);
                }
                if (dateend != null) {
                    sql += "and acceptuserdate < " + dateendsql;
                    params.add(formatDateEnd);
                }
            } else if (StringUtil.isNotBlank(projectType) && "DBJ".equals(projectType)) {
                sql = "select " + fieldstr + " from audit_project where  areaCode= ? " + " and status>='"
                        + ZwfwConstant.BANJIAN_STATUS_YSLDBJ + "' and status <='" + ZwfwConstant.BANJIAN_STATUS_SPTG
                        + "' and task_id in ";
                params.add(areaCode);
                Parameter pa = DbKit.splitIn(taskids);
                sql += pa.getSql();
                for (Object obj : pa.getValue()) {
                    params.add(obj);
                }
                if (datestart != null) {
                    sql += "and promiseenddate > " + datestartsql;
                    params.add(formatDateStart);
                }
                if (dateend != null) {
                    sql += "and promiseenddate < " + dateendsql;
                    params.add(formatDateEnd);
                }
            } else if (StringUtil.isNotBlank(projectType) && "DBB".equals(projectType)) {
                sql = "select " + fieldstr + " from audit_project where  areaCode=? and status in ("
                        + ZwfwConstant.BANJIAN_STATUS_DBB + "," + ZwfwConstant.BANJIAN_STATUS_YSLDBB
                        + ") and task_id in ";
                params.add(areaCode);
                Parameter pa = DbKit.splitIn(taskids);
                sql += pa.getSql();
                for (Object obj : pa.getValue()) {
                    params.add(obj);
                }
                if (datestart != null) {
                    sql += "and bubandate >  " + datestartsql;
                    params.add(formatDateStart);
                }
                if (dateend != null) {
                    sql += "and bubandate <  " + dateendsql;
                    params.add(formatDateEnd);
                }
            } else if (StringUtil.isNotBlank(projectType) && "DSL".equals(projectType)) {
                sql = "select " + fieldstr + " from audit_project where  areaCode=? and status=" + BANJIAN_STATUS
                        + " and task_id in ";
                params.add(areaCode);
                Parameter pa = DbKit.splitIn(taskids);
                sql += pa.getSql();
                for (Object obj : pa.getValue()) {
                    params.add(obj);
                }
                if (datestart != null) {
                    sql += "and RECEIVEDATE > " + datestartsql;
                    params.add(formatDateStart);
                }
                if (dateend != null) {
                    sql += "and RECEIVEDATE <  " + dateendsql;
                    params.add(formatDateEnd);
                }
            } else if (StringUtil.isNotBlank(projectType) && "DJJ".equals(projectType)) {
                sql = "select " + fieldstr + " from audit_project where  areaCode=? and status=" + BANJIAN_STATUS
                        + " and task_id in ";
                params.add(areaCode);
                Parameter pa = DbKit.splitIn(taskids);
                sql += pa.getSql();
                for (Object obj : pa.getValue()) {
                    params.add(obj);
                }
                if (datestart != null) {
                    sql += "and applydate > " + datestartsql;
                    params.add(formatDateStart);
                }
                if (dateend != null) {
                    sql += "and applydate < ? " + dateendsql;
                    params.add(formatDateEnd);
                }
            } else if (StringUtil.isNotBlank(projectType) && "DYS".equals(projectType)) {
                sql = "select " + fieldstr + " from audit_project where  areaCode=? and status=" + BANJIAN_STATUS
                        + " and task_id in ";
                params.add(areaCode);
                Parameter pa = DbKit.splitIn(taskids);
                sql += pa.getSql();
                for (Object obj : pa.getValue()) {
                    params.add(obj);
                }
                if (datestart != null) {
                    sql += "and applydate > " + datestartsql;
                    params.add(formatDateStart);
                }
                if (dateend != null) {
                    sql += "and applydate < " + dateendsql;
                    params.add(formatDateEnd);
                }
            } else if (StringUtil.isNotBlank(projectType) && "YSTG".equals(projectType)) {
                sql = "select " + fieldstr + " from audit_project where  areaCode=? and status=" + BANJIAN_STATUS
                        + " and task_id in ";
                params.add(areaCode);
                Parameter pa = DbKit.splitIn(taskids);
                sql += pa.getSql();
                for (Object obj : pa.getValue()) {
                    params.add(obj);
                }
                if (datestart != null) {
                    sql += "and yushendate > " + datestartsql;
                    params.add(formatDateStart);
                }
                if (dateend != null) {
                    sql += "and yushendate < " + dateendsql;
                    params.add(formatDateEnd);
                }
            } else {
                sql = "select " + fieldstr + " from audit_project where areaCode = ? and status =" + BANJIAN_STATUS
                        + " and task_id in ";
                params.add(areaCode);
                Parameter pa = DbKit.splitIn(taskids);
                sql += pa.getSql();
                for (Object obj : pa.getValue()) {
                    params.add(obj);
                }
            }
            if (StringUtil.isNotBlank(applyerName)) {
                sql += "and applyerName like ? ";
                params.add("%" + applyerName.replace("\\", "\\\\").replace("%", "\\%") + "%");
            }

            if (StringUtil.isNotBlank(projectname)) {
                sql += "and projectname like ? ";
                params.add("%" + projectname.replace("\\", "\\\\").replace("%", "\\%") + "%");
            }

            if (StringUtil.isNotBlank(commitnum)) {
                sql += "and ifnull(commitnum,1) = ? ";
                params.add(commitnum);
            }
            sql += sqlExt;
            if (StringUtil.isNotBlank(sortField)) {
                if (StringUtil.isBlank(sortOrder)) {
                    sortOrder = "desc";
                }
                sql += " order by " + sortField + " " + sortOrder;
            }
            else {
                sql += " order by OperateDate desc";
            }
            params.add(realAreacode);
            params.add(centerGuid);
        }

        //Log.info("待受理查询sql：" + sql);
        List<AuditProject> list = commonDao.findList(sql, first, pageSize, AuditProject.class, params.toArray());
        sqlCount = sql.replace("select " + fieldstr + " from", "select count(1) from");
        int count = 0;
        if ("".equals(taskids) || taskids == null) {
            count = 0;
        } else {
            count = commonDao.queryInt(sqlCount, params.toArray());
        }
        PageData<AuditProject> pageData = new PageData<>();
        pageData.setList(list);
        pageData.setRowCount(count);
        return pageData;
    }

    /**
     * 获取办件列表
     *
     * @param projectType 办件类型
     * @param windowguid  窗口id
     */

    public List<AuditProject> getBanJianList(String fieldstr, List<String> taskGuidList, String projectType,
                                             String windowguid) {

        // 2017/4/10 CH 更改办理工作台数字
        String taskGuids = "";
        if (taskGuidList != null) {
            for (String taskGuidExp : taskGuidList) {
                taskGuids += "'" + taskGuidExp + "',";
            }
            if (taskGuids != "") {
                taskGuids = taskGuids.substring(0, taskGuids.length() - 1);
            }
        }
        List<Object> params = new ArrayList<Object>();
        /*
         * int BANJIAN_STATUS = 0;
         * if (projectType.equals("DJJ")) {
         * BANJIAN_STATUS = ZwfwConstant.BANJIAN_STATUS_DJJ;
         * }
         * else if (projectType.equals("DSL")) {
         * BANJIAN_STATUS = ZwfwConstant.BANJIAN_STATUS_YJJ;
         * }
         * else if (projectType.equals("DBB")) {
         * BANJIAN_STATUS = ZwfwConstant.BANJIAN_STATUS_DBB;
         * }
         * else if (projectType.equals("DYS")) {
         * BANJIAN_STATUS = ZwfwConstant.BANJIAN_STATUS_WWYTJ;
         * }
         * else if (projectType.equals("YSTG")) {
         * BANJIAN_STATUS = ZwfwConstant.BANJIAN_STATUS_WWYSTG;
         * }
         */

        String sql = "";
        if (StringUtil.isNotBlank(projectType) && "YZT".equals(projectType)) {
            sql = "select " + fieldstr + " from audit_project where status<90 and is_pause = '1' and taskguid in ";
            Parameter pa = DbKit.splitIn(taskGuids);
            sql += pa.getSql();
            for (Object obj : pa.getValue()) {
                params.add(obj);
            }
        } else if (StringUtil.isNotBlank(projectType) && "DBJ".equals(projectType)) {
            sql = "select " + fieldstr + " from audit_project where " + " ((TASKTYPE='" + ZwfwConstant.ITEMTYPE_JBJ
                    + "' and status='" + ZwfwConstant.BANJIAN_STATUS_YSL + "') or (TASKTYPE!='"
                    + ZwfwConstant.ITEMTYPE_JBJ + "' and (status='" + ZwfwConstant.BANJIAN_STATUS_SPBTG
                    + "' or status='" + ZwfwConstant.BANJIAN_STATUS_SPTG + "'))) and taskguid in ";
            Parameter pa = DbKit.splitIn(taskGuids);
            sql += pa.getSql();
            for (Object obj : pa.getValue()) {
                params.add(obj);
            }
        } else if (StringUtil.isNotBlank(projectType) && "DBB".equals(projectType)) {
            sql = "select " + fieldstr + " from audit_project where status in (" + ZwfwConstant.BANJIAN_STATUS_DBB + ","
                    + ZwfwConstant.BANJIAN_STATUS_YSLDBB + ") and taskguid in ";
            Parameter pa = DbKit.splitIn(taskGuids);
            sql += pa.getSql();
            for (Object obj : pa.getValue()) {
                params.add(obj);
            }
        }

        List<AuditProject> list = commonDao.findList(sql, AuditProject.class, params.toArray());

        return list;
    }

    // 获取不进驻中心办件
    public PageData<AuditProject> getJZListByPage(String fieldstr, String OUGuids, int first, int pageSize,
                                                  String areaCode, String applyerName) {
        List<Object> params = new ArrayList<Object>();
        if (StringUtil.isBlank(applyerName)) {
            applyerName = "";
        }
        String sql = "";
        String sqlCount = "";
        if ("".equals(OUGuids) || OUGuids == null) {
            sql = "select " + fieldstr + " from audit_project where IF_JZ_HALL=1 ";
        } else {
            sql = "select " + fieldstr
                    + " from audit_project where IF_JZ_HALL=1 and areacode=?  and applyerName like ? "
                    + " and ((status>='" + ZwfwConstant.BANJIAN_STATUS_DJJ + "' and status <='"
                    + ZwfwConstant.BANJIAN_STATUS_SPTG + "') or status='" + ZwfwConstant.BANJIAN_STATUS_WWYTJ
                    + "' or status='" + ZwfwConstant.BANJIAN_STATUS_WWYSTG + "') and OUGuid in ";
            params.add(areaCode);
            params.add("%" + applyerName.replace("\\", "\\\\").replace("%", "\\%") + "%");
            Parameter pa = DbKit.splitIn(OUGuids);
            sql += pa.getSql();
            for (Object obj : pa.getValue()) {
                params.add(obj);
            }
        }
        sql += " order by status asc";

        List<AuditProject> list = commonDao.findList(sql, first, pageSize, AuditProject.class, params.toArray());
        sqlCount = sql.replace("select " + fieldstr + " from", "select count(1) from");
        int count = commonDao.queryInt(sqlCount, params.toArray());
        PageData<AuditProject> pageData = new PageData<>();
        pageData.setList(list);
        pageData.setRowCount(count);
        return pageData;
    }

    // 获取不进驻中心办件
    public List<AuditProject> getJZList(String fieldstr, String OUGuids, String areaCode) {
        String sql = "";
        List<Object> params = new ArrayList<Object>();
        if ("".equals(OUGuids) || OUGuids == null) {
            sql = "select " + fieldstr + " from audit_project where IF_JZ_HALL=1 and areacode=?";
            params.add(areaCode);
        } else {
            sql = "select " + fieldstr + " from audit_project where IF_JZ_HALL=1 and areacode=? and ((status>='"
                    + ZwfwConstant.BANJIAN_STATUS_DJJ + "' and status <=? ) or status=? or status=? ) and OUGuid in ";
            Parameter pa = DbKit.splitIn(OUGuids);
            sql += pa.getSql();
            params.add(areaCode);
            params.add(ZwfwConstant.BANJIAN_STATUS_SPTG);
            params.add(ZwfwConstant.BANJIAN_STATUS_WWYTJ);
            params.add(ZwfwConstant.BANJIAN_STATUS_WWYSTG);
            for (Object obj : pa.getValue()) {
                params.add(obj);
            }
        }
        List<AuditProject> list = commonDao.findList(sql, AuditProject.class, params.toArray());
        return list;
    }

    // 获取不进驻中心办件数量
    public int getNotJZCount(String OUGuids, String areaCode) {
        String sql = "";
        List<Object> params = new ArrayList<Object>();
        if ("".equals(OUGuids) || OUGuids == null) {
            sql = "select count(1) as total from audit_project where IF_JZ_HALL=1 and areacode=?";
            params.add(areaCode);
        } else {
            sql = "select count(1) as total from audit_project where IF_JZ_HALL=1 and areacode=? and ((status>='"
                    + ZwfwConstant.BANJIAN_STATUS_DJJ + "' and status <=? ) or status=? or status=? ) and OUGuid in ";
            Parameter pa = DbKit.splitIn(OUGuids);
            sql += pa.getSql();
            params.add(areaCode);
            params.add(ZwfwConstant.BANJIAN_STATUS_SPTG);
            params.add(ZwfwConstant.BANJIAN_STATUS_WWYTJ);
            params.add(ZwfwConstant.BANJIAN_STATUS_WWYSTG);
            for (Object obj : pa.getValue()) {
                params.add(obj);
            }
        }
        return commonDao.queryInt(sql, params.toArray());
    }

    /**
     * 通过窗口guid得到不同办件状态的数量，获取待接件即map.get("DJJ"); 获取待接件即map.get("DJJ");
     * 获取待受理即map.get("DSL"); 获取已暂停即map.get("YZT"); 获取待办结即map.get("DBJ");
     * 获取待补办即map.get("DBB"); 获取待预审即map.get("DYS"); 获取预审通过即map.get("YSTG");
     *
     * @param windowguid 窗口guid
     * @return 不同状态的办件数量Map集合
     */
    public Map<String, Integer> getCountStatusByWindowguid(List<String> taskidList, String windowguid,
                                                           String areaCode) {
        String taskids = "";
        List<Object> params = new ArrayList<Object>();
        if (taskidList != null) {
            for (String taskidExp : taskidList) {
                taskids += "'" + taskidExp + "',";
            }
            if (StringUtil.isNotBlank(taskids)) {
                taskids = taskids.substring(0, taskids.length() - 1);
            }
        }

        Map<String, Integer> map = new HashMap<String, Integer>(16);
        int daijiejiancount = 0;
        int daishoulicount = 0;
        int daibubancount = 0;
        int daibanjiecount = 0;
        int daiyushencount = 0;
        int yushentonguocount = 0;
        int yizantingcount = 0;
        int shenpicount = 0;
        int ysldbjcount = 0;
        int ysldbbcount = 0;
        int spbtgcount = 0;
        int sptgcount = 0;

        if (StringUtil.isNotBlank(taskids)) {
            List<AuditProject> tempList = null;
            AuditProject arrobj = null;

            String fieldstr = "status, count(1) count";// 查询条件
            String groupstr = "  group by STATUS";// 分组条件
            // 受理之后的就只能当前窗口看到
            String sqlExt = "";
            if (commonDao.isMySql()) {
                sqlExt = "and (currentareacode =? or currentareacode is null or  currentareacode ='')";
                params.add(areaCode);
            }
            if (commonDao.isOracle()) {
                sqlExt = "and (currentareacode =? or currentareacode is null)";
            }
            if (commonDao.isDM()) {
                sqlExt = "and (currentareacode =? or currentareacode is null)";
            }
            String wherestr = " task_id in (" + taskids + ") and areacode='" + areaCode + "' ";
            wherestr += sqlExt;
            // 待确认
            tempList = getAuditProjectFieldList(fieldstr, groupstr, wherestr);
            for (int i = 0; i < tempList.size(); i++) {
                arrobj = tempList.get(i);
                // 待接件
                if (ZwfwConstant.BANJIAN_STATUS_DJJ == Integer.parseInt(arrobj.getStr("status"))) {
                    daijiejiancount = arrobj.getStr("count") == null ? ZwfwConstant.CONSTANT_INT_ZERO
                            : Integer.parseInt(arrobj.getStr("count"));
                }
                // 待受理
                else if (ZwfwConstant.BANJIAN_STATUS_YJJ == Integer.parseInt(arrobj.getStr("status"))) {
                    daishoulicount = arrobj.getStr("count") == null ? ZwfwConstant.CONSTANT_INT_ZERO
                            : Integer.parseInt(arrobj.getStr("count"));
                }
                // 待补办
                else if (ZwfwConstant.BANJIAN_STATUS_DBB == Integer.parseInt(arrobj.getStr("status"))) {
                    daibubancount = arrobj.getStr("count") == null ? ZwfwConstant.CONSTANT_INT_ZERO
                            : Integer.parseInt(arrobj.getStr("count"));
                }
                // 已受理待补办
                else if (ZwfwConstant.BANJIAN_STATUS_YSLDBB == Integer.parseInt(arrobj.getStr("status"))) {
                    daibubancount += arrobj.getStr("count") == null ? ZwfwConstant.CONSTANT_INT_ZERO
                            : Integer.parseInt(arrobj.getStr("count"));
                }
                // 外网预审通过
                else if (ZwfwConstant.BANJIAN_STATUS_WWYSTG == Integer.parseInt(arrobj.getStr("status"))) {
                    yushentonguocount += arrobj.getStr("count") == null ? ZwfwConstant.CONSTANT_INT_ZERO
                            : Integer.parseInt(arrobj.getStr("count"));
                }
                // 外网待预审
                else if (ZwfwConstant.BANJIAN_STATUS_WWYTJ == Integer.parseInt(arrobj.getStr("status"))) {
                    daiyushencount += arrobj.getStr("count") == null ? ZwfwConstant.CONSTANT_INT_ZERO
                            : Integer.parseInt(arrobj.getStr("count"));
                }
                // BANJIAN_STATUS_YSLDBJ
                else if (ZwfwConstant.BANJIAN_STATUS_YSLDBJ == Integer.parseInt(arrobj.getStr("status"))) {
                    ysldbjcount += arrobj.getStr("count") == null ? ZwfwConstant.CONSTANT_INT_ZERO
                            : Integer.parseInt(arrobj.getStr("count"));
                }
                // BANJIAN_STATUS_YSLDBB
                else if (ZwfwConstant.BANJIAN_STATUS_YSLDBB == Integer.parseInt(arrobj.getStr("status"))) {
                    ysldbbcount += arrobj.getStr("count") == null ? ZwfwConstant.CONSTANT_INT_ZERO
                            : Integer.parseInt(arrobj.getStr("count"));
                }
                // BANJIAN_STATUS_YSL
                else if (ZwfwConstant.BANJIAN_STATUS_YSL == Integer.parseInt(arrobj.getStr("status"))) {
                    shenpicount += arrobj.getStr("count") == null ? ZwfwConstant.CONSTANT_INT_ZERO
                            : Integer.parseInt(arrobj.getStr("count"));
                }
                // 审批通过
                else if (ZwfwConstant.BANJIAN_STATUS_SPTG == Integer.parseInt(arrobj.getStr("status"))) {
                    sptgcount += arrobj.getStr("count") == null ? ZwfwConstant.CONSTANT_INT_ZERO
                            : Integer.parseInt(arrobj.getStr("count"));
                }
                // 审批不通过
                else if (ZwfwConstant.BANJIAN_STATUS_SPTG == Integer.parseInt(arrobj.getStr("status"))) {
                    spbtgcount += arrobj.getStr("count") == null ? ZwfwConstant.CONSTANT_INT_ZERO
                            : Integer.parseInt(arrobj.getStr("count"));
                }
            }
            // 已暂停
            fieldstr = "count(*) count";// 查询条件
            groupstr = "";// 分组条件

            wherestr = "  task_id in (" + taskids + ") AND is_pause = '1' and areacode='" + areaCode + "' " + sqlExt;// 条件
            arrobj = getAuditProjectField(fieldstr, groupstr, wherestr);
            yizantingcount = Integer.parseInt(
                    (arrobj.getStr("count") == null ? ZwfwConstant.CONSTANT_STR_ZERO : arrobj.getStr("count")));

            // 待办结
            daibanjiecount = ysldbjcount + ysldbbcount + sptgcount + spbtgcount;
            // 审批中
            shenpicount = shenpicount + ysldbjcount + ysldbbcount;
        }

        map.put("DJJ", daijiejiancount);
        map.put("DSL", daishoulicount);
        map.put("YZT", yizantingcount);
        map.put("DBJ", daibanjiecount);
        map.put("DBB", daibubancount);
        map.put("DYS", daiyushencount);
        map.put("YSTG", yushentonguocount);
        map.put("SPZ", shenpicount);
        return map;

    }

    public Map<String, Integer> getCountStatusByCenterguid(List<String> taskidList, String windowguid,
                                                           String centerGuid, String areaCode) {
        String taskids = "";
        if (taskidList != null) {
            for (String taskidExp : taskidList) {
                taskids += "'" + taskidExp + "',";
            }
            if (StringUtil.isNotBlank(taskids)) {
                taskids = taskids.substring(0, taskids.length() - 1);
            }
        }

        Map<String, Integer> map = new HashMap<String, Integer>();
        int daijiejiancount = 0;
        int daishoulicount = 0;
        int daibubancount = 0;
        int daibanjiecount = 0;
        int daiyushencount = 0;
        int yushentonguocount = 0;
        int yizantingcount = 0;

        if (StringUtil.isNotBlank(taskids)) {
            List<AuditProject> tempList = null;
            AuditProject arrobj = null;

            String fieldstr = "status, count(1) count";// 查询条件
            String groupstr = "  group by STATUS";// 分组条件
            // 受理之后的就只能当前窗口看到
            String sqlExt = "";
            if (commonDao.isMySql()) {
                sqlExt = "and (windowguid is null or windowguid='' or windowguid='" + windowguid + "')";
            }
            if (commonDao.isOracle()) {
                sqlExt = "and (windowguid is null or windowguid='" + windowguid + "')";
            }
            if (commonDao.isDM()) {
                sqlExt = "and (windowguid is null or windowguid='" + windowguid + "')";
            }
            String wherestr = " task_id in (" + taskids + ") and centerGuid='" + centerGuid + "' and areacode='"
                    + areaCode + "' ";
            wherestr += sqlExt;
            // 待确认
            tempList = getAuditProjectFieldList(fieldstr, groupstr, wherestr);
            for (int i = 0; i < tempList.size(); i++) {
                arrobj = tempList.get(i);
                // 待接件
                if (ZwfwConstant.BANJIAN_STATUS_DJJ == Integer.parseInt(arrobj.getStr("status"))) {
                    daijiejiancount = arrobj.getStr("count") == null ? 0 : Integer.parseInt(arrobj.getStr("count"));
                }
                // 待受理
                else if (ZwfwConstant.BANJIAN_STATUS_YJJ == Integer.parseInt(arrobj.getStr("status"))) {
                    daishoulicount = arrobj.getStr("count") == null ? 0 : Integer.parseInt(arrobj.getStr("count"));
                }
                // 待补办
                else if (ZwfwConstant.BANJIAN_STATUS_DBB == Integer.parseInt(arrobj.getStr("status"))) {
                    daibubancount = arrobj.getStr("count") == null ? 0 : Integer.parseInt(arrobj.getStr("count"));
                }
                // 已受理待补办
                else if (ZwfwConstant.BANJIAN_STATUS_YSLDBB == Integer.parseInt(arrobj.getStr("status"))) {
                    daibubancount += arrobj.getStr("count") == null ? 0 : Integer.parseInt(arrobj.getStr("count"));
                }
            }
            // 已暂停
            fieldstr = "count(*) count";// 查询条件
            groupstr = "";// 分组条件

            wherestr = "  task_id in (" + taskids + ") AND is_pause = '1' and centerGuid='" + centerGuid
                    + "' and areacode='" + areaCode + "' " + sqlExt;// 条件
            arrobj = getAuditProjectField(fieldstr, groupstr, wherestr);
            yizantingcount = Integer.parseInt((arrobj.getStr("count") == null ? "0" : arrobj.getStr("count")));

            // 待办结
            fieldstr = "count(*) count";// 查询条件
            groupstr = ""; // 分组条件
            wherestr = "  task_id in (" + taskids + ") and status>='" + ZwfwConstant.BANJIAN_STATUS_YSLDBJ
                    + "' and status<='" + ZwfwConstant.BANJIAN_STATUS_SPTG + "' and centerGuid='" + centerGuid
                    + "' and areacode='" + areaCode + "' " + sqlExt;// 条件
            arrobj = getAuditProjectField(fieldstr, groupstr, wherestr);
            daibanjiecount = Integer.parseInt((arrobj.getStr("count") == null ? "0" : arrobj.getStr("count")));

            // 待预审
            fieldstr = "count(*) count";// 查询条件
            groupstr = "";// 分组条件
            wherestr = " status ='" + ZwfwConstant.BANJIAN_STATUS_WWYTJ + "' and task_id in (" + taskids
                    + ") and centerGuid='" + centerGuid + "' and areacode='" + areaCode + "' " + sqlExt;// 条件
            arrobj = getAuditProjectField(fieldstr, groupstr, wherestr);
            daiyushencount = Integer.parseInt((arrobj.getStr("count") == null ? "0" : arrobj.getStr("count")));

            // 预审通过
            fieldstr = "count(*) count";// 查询条件
            groupstr = "";// 分组条件
            wherestr = "  task_id in (" + taskids + ") and status ='" + ZwfwConstant.BANJIAN_STATUS_WWYSTG
                    + "' and centerGuid='" + centerGuid + "' and areacode='" + areaCode + "' " + sqlExt; // 条件
            arrobj = getAuditProjectField(fieldstr, groupstr, wherestr);
            yushentonguocount = Integer.parseInt((arrobj.getStr("count") == null ? "0" : arrobj.getStr("count")));
        }

        map.put("DJJ", daijiejiancount);
        map.put("DSL", daishoulicount);
        map.put("YZT", yizantingcount);
        map.put("DBJ", daibanjiecount);
        map.put("DBB", daibubancount);
        map.put("DYS", daiyushencount);
        map.put("YSTG", yushentonguocount);
        return map;

    }

    /**
     * 通过窗口guid得到不同办件状态的数量，获取待接件即map.get("DJJ"); 获取待接件即map.get("DJJ");
     * 获取待受理即map.get("DSL"); 获取已暂停即map.get("YZT"); 获取待办结即map.get("DBJ");
     * 获取待补办即map.get("DBB"); 获取待预审即map.get("DYS"); 获取预审通过即map.get("YSTG");
     *
     * @param windowguid 窗口guid
     * @return 不同状态的办件数量Map集合
     */
    public Map<String, Integer> getCountStatusByWindowguidAndCenterguid(List<String> taskidList, String windowguid,
                                                                        String areaCode, String centerGuid) {
        String taskids = "";
        if (taskidList != null) {
            for (String taskidExp : taskidList) {
                taskids += "'" + taskidExp + "',";
            }
            if (StringUtil.isNotBlank(taskids)) {
                taskids = taskids.substring(0, taskids.length() - 1);
            }
        }

        Map<String, Integer> map = new HashMap<String, Integer>(16);
        int daijiejiancount = 0;
        int daishoulicount = 0;
        int daibubancount = 0;
        int daibanjiecount = 0;
        int daiyushencount = 0;
        int yushentonguocount = 0;
        int yizantingcount = 0;
        int shenpicount = 0;
        int ysldbjcount = 0;
        int ysldbbcount = 0;
        int spbtgcount = 0;
        int sptgcount = 0;

        if (StringUtil.isNotBlank(taskids)) {
            List<AuditProject> tempList = null;
            AuditProject arrobj = null;

            String fieldstr = "status, count(1) count";// 查询条件
            String groupstr = "  group by STATUS";// 分组条件
            // 受理之后的就只能当前窗口看到
            String sqlExt = "";
            if (commonDao.isMySql()) {
                sqlExt = "and (currentareacode ='" + areaCode + "' or currentareacode is null or  currentareacode ='')"
                        + " and( (centerGuid='" + centerGuid
                        + "' and( acceptareacode is null or acceptareacode='')) or acceptareacode is not null))";

            }
            if (commonDao.isOracle()) {
                sqlExt = "and (currentareacode ='" + areaCode + "' or currentareacode is null)" + " and( (centerGuid='"
                        + centerGuid + "' and acceptareacode is null) or acceptareacode is not null))";
            }
            if (commonDao.isDM()) {
                sqlExt = "and (currentareacode ='" + areaCode + "' or currentareacode is null)" + " and( (centerGuid='"
                        + centerGuid + "' and acceptareacode is null) or acceptareacode is not null))";
            }
            String wherestr = " task_id in (" + taskids + ") and areacode='" + areaCode + "' ";
            wherestr += sqlExt;
            // 待确认
            tempList = getAuditProjectFieldList(fieldstr, groupstr, wherestr);
            for (int i = 0; i < tempList.size(); i++) {
                arrobj = tempList.get(i);
                // 待接件
                if (ZwfwConstant.BANJIAN_STATUS_DJJ == Integer.parseInt(arrobj.getStr("status"))) {
                    daijiejiancount = arrobj.getStr("count") == null ? ZwfwConstant.CONSTANT_INT_ZERO
                            : Integer.parseInt(arrobj.getStr("count"));
                }
                // 待受理
                else if (ZwfwConstant.BANJIAN_STATUS_YJJ == Integer.parseInt(arrobj.getStr("status"))) {
                    daishoulicount = arrobj.getStr("count") == null ? ZwfwConstant.CONSTANT_INT_ZERO
                            : Integer.parseInt(arrobj.getStr("count"));
                }
                // 待补办
                else if (ZwfwConstant.BANJIAN_STATUS_DBB == Integer.parseInt(arrobj.getStr("status"))) {
                    daibubancount = arrobj.getStr("count") == null ? ZwfwConstant.CONSTANT_INT_ZERO
                            : Integer.parseInt(arrobj.getStr("count"));
                }
                // 已受理待补办
                else if (ZwfwConstant.BANJIAN_STATUS_YSLDBB == Integer.parseInt(arrobj.getStr("status"))) {
                    daibubancount += arrobj.getStr("count") == null ? ZwfwConstant.CONSTANT_INT_ZERO
                            : Integer.parseInt(arrobj.getStr("count"));
                }
                // 外网预审通过
                else if (ZwfwConstant.BANJIAN_STATUS_WWYSTG == Integer.parseInt(arrobj.getStr("status"))) {
                    yushentonguocount += arrobj.getStr("count") == null ? ZwfwConstant.CONSTANT_INT_ZERO
                            : Integer.parseInt(arrobj.getStr("count"));
                }
                // 外网待预审
                else if (ZwfwConstant.BANJIAN_STATUS_WWYTJ == Integer.parseInt(arrobj.getStr("status"))) {
                    daiyushencount += arrobj.getStr("count") == null ? ZwfwConstant.CONSTANT_INT_ZERO
                            : Integer.parseInt(arrobj.getStr("count"));
                }
                // BANJIAN_STATUS_YSLDBJ
                else if (ZwfwConstant.BANJIAN_STATUS_YSLDBJ == Integer.parseInt(arrobj.getStr("status"))) {
                    ysldbjcount += arrobj.getStr("count") == null ? ZwfwConstant.CONSTANT_INT_ZERO
                            : Integer.parseInt(arrobj.getStr("count"));
                }
                // BANJIAN_STATUS_YSLDBB
                else if (ZwfwConstant.BANJIAN_STATUS_YSLDBB == Integer.parseInt(arrobj.getStr("status"))) {
                    ysldbbcount += arrobj.getStr("count") == null ? ZwfwConstant.CONSTANT_INT_ZERO
                            : Integer.parseInt(arrobj.getStr("count"));
                }
                // BANJIAN_STATUS_YSL
                else if (ZwfwConstant.BANJIAN_STATUS_YSL == Integer.parseInt(arrobj.getStr("status"))) {
                    shenpicount += arrobj.getStr("count") == null ? ZwfwConstant.CONSTANT_INT_ZERO
                            : Integer.parseInt(arrobj.getStr("count"));
                }
                // 审批通过
                else if (ZwfwConstant.BANJIAN_STATUS_SPTG == Integer.parseInt(arrobj.getStr("status"))) {
                    sptgcount += arrobj.getStr("count") == null ? ZwfwConstant.CONSTANT_INT_ZERO
                            : Integer.parseInt(arrobj.getStr("count"));
                }
                // 审批不通过
                else if (ZwfwConstant.BANJIAN_STATUS_SPTG == Integer.parseInt(arrobj.getStr("status"))) {
                    spbtgcount += arrobj.getStr("count") == null ? ZwfwConstant.CONSTANT_INT_ZERO
                            : Integer.parseInt(arrobj.getStr("count"));
                }
            }
            // 已暂停
            fieldstr = "count(*) count";// 查询条件
            groupstr = "";// 分组条件

            wherestr = "  task_id in (" + taskids + ") AND is_pause = '1' and areacode='" + areaCode + "' " + sqlExt;// 条件
            arrobj = getAuditProjectField(fieldstr, groupstr, wherestr);
            yizantingcount = Integer.parseInt(
                    (arrobj.getStr("count") == null ? ZwfwConstant.CONSTANT_STR_ZERO : arrobj.getStr("count")));

            // 待办结
            daibanjiecount = ysldbjcount + ysldbbcount + sptgcount + spbtgcount;
            // 审批中
            shenpicount = shenpicount + ysldbjcount + ysldbbcount;
        }

        map.put("DJJ", daijiejiancount);
        map.put("DSL", daishoulicount);
        map.put("YZT", yizantingcount);
        map.put("DBJ", daibanjiecount);
        map.put("DBB", daibubancount);
        map.put("DYS", daiyushencount);
        map.put("YSTG", yushentonguocount);
        map.put("SPZ", shenpicount);
        return map;

    }

    /**
     * 通过窗口guid得到不同办件状态的数量，获取待接件即map.get("DJJ"); 获取待接件即map.get("DJJ");
     * 获取待受理即map.get("DSL"); 获取已暂停即map.get("YZT"); 获取待办结即map.get("DBJ");
     * 获取待补办即map.get("DBB"); 获取待预审即map.get("DYS"); 获取预审通过即map.get("YSTG");
     *
     * @param windowguid 窗口guid
     * @return 不同状态的办件数量Map集合
     */
    public Map<String, Integer> getCountStatusByWindowguidAndCenterguidAndAreacode(List<String> taskidList,
                                                                                   String windowguid, String areaCode, String centerGuid, String baseareacode) {
        String taskids = "";
        if (taskidList != null) {
            taskids = "'" + StringUtil.join(taskidList, "','") + "'";
        }

        Map<String, Integer> map = new HashMap<String, Integer>(16);
        int daijiejiancount = 0;
        int daishoulicount = 0;
        int daibubancount = 0;
        int daibanjiecount = 0;
        int daiyushencount = 0;
        int yushentonguocount = 0;
        int yizantingcount = 0;
        int shenpicount = 0;
        int ysldbjcount = 0;
        int ysldbbcount = 0;
        int spbtgcount = 0;
        int sptgcount = 0;
        if (StringUtil.isNotBlank(taskids)) {
            List<AuditProject> tempList = null;
            AuditProject arrobj = null;

            String fieldstr = "status, count(1) count";// 查询条件
            String groupstr = "  group by STATUS";// 分组条件
            // 受理之后的就只能当前窗口看到
            String sqlExt = "";
            if (commonDao.isMySql()) {
                sqlExt = "and (currentareacode ='" + areaCode + "' or currentareacode is null or  currentareacode ='')"
                        + " and( ( ( centerGuid='" + centerGuid
                        + "' or centerGuid ='' or centerGuid is null) and( acceptareacode is null or acceptareacode='')) or acceptareacode is not null)";

            }
            if (commonDao.isOracle()) {
                sqlExt = "and (currentareacode ='" + areaCode + "' or currentareacode is null)" + " and( ((centerGuid='"
                        + centerGuid
                        + "' or centerGuid is null)and acceptareacode is null) or acceptareacode is not null)";
            }
            if (commonDao.isDM()) {
                sqlExt = "and (currentareacode ='" + areaCode + "' or currentareacode is null or  currentareacode ='')"
                        + " and( ((centerGuid='" + centerGuid
                        + "' or centerGuid is null ) and( acceptareacode is null or acceptareacode='')) or acceptareacode is not null)";
            }
            String wherestr = " task_id in (" + taskids + ") and areacode='" + baseareacode + "' ";
            wherestr += sqlExt;
            // 待确认
            tempList = getAuditProjectFieldList(fieldstr, groupstr, wherestr);
            for (int i = 0; i < tempList.size(); i++) {
                arrobj = tempList.get(i);
                // 待接件
                if (ZwfwConstant.BANJIAN_STATUS_DJJ == Integer.parseInt(arrobj.getStr("status"))) {
                    daijiejiancount = arrobj.getStr("count") == null ? ZwfwConstant.CONSTANT_INT_ZERO
                            : Integer.parseInt(arrobj.getStr("count"));
                }
                // 待受理
                else if (ZwfwConstant.BANJIAN_STATUS_YJJ == Integer.parseInt(arrobj.getStr("status"))) {
                    daishoulicount = arrobj.getStr("count") == null ? ZwfwConstant.CONSTANT_INT_ZERO
                            : Integer.parseInt(arrobj.getStr("count"));
                }
                // 待补办
                else if (ZwfwConstant.BANJIAN_STATUS_DBB == Integer.parseInt(arrobj.getStr("status"))) {
                    daibubancount = arrobj.getStr("count") == null ? ZwfwConstant.CONSTANT_INT_ZERO
                            : Integer.parseInt(arrobj.getStr("count"));
                }
                // 已受理待补办
                else if (ZwfwConstant.BANJIAN_STATUS_YSLDBB == Integer.parseInt(arrobj.getStr("status"))) {
                    daibubancount += arrobj.getStr("count") == null ? ZwfwConstant.CONSTANT_INT_ZERO
                            : Integer.parseInt(arrobj.getStr("count"));
                }
                // 外网预审通过
                else if (ZwfwConstant.BANJIAN_STATUS_WWYSTG == Integer.parseInt(arrobj.getStr("status"))) {
                    yushentonguocount += arrobj.getStr("count") == null ? ZwfwConstant.CONSTANT_INT_ZERO
                            : Integer.parseInt(arrobj.getStr("count"));
                }
                // 外网待预审
                else if (ZwfwConstant.BANJIAN_STATUS_WWYTJ == Integer.parseInt(arrobj.getStr("status"))) {
                    daiyushencount += arrobj.getStr("count") == null ? ZwfwConstant.CONSTANT_INT_ZERO
                            : Integer.parseInt(arrobj.getStr("count"));
                }
                // BANJIAN_STATUS_YSLDBJ
                else if (ZwfwConstant.BANJIAN_STATUS_YSLDBJ == Integer.parseInt(arrobj.getStr("status"))) {
                    ysldbjcount += arrobj.getStr("count") == null ? ZwfwConstant.CONSTANT_INT_ZERO
                            : Integer.parseInt(arrobj.getStr("count"));
                }
                // BANJIAN_STATUS_YSLDBB
                else if (ZwfwConstant.BANJIAN_STATUS_YSLDBB == Integer.parseInt(arrobj.getStr("status"))) {
                    ysldbbcount += arrobj.getStr("count") == null ? ZwfwConstant.CONSTANT_INT_ZERO
                            : Integer.parseInt(arrobj.getStr("count"));
                }
                // BANJIAN_STATUS_YSL
                else if (ZwfwConstant.BANJIAN_STATUS_YSL == Integer.parseInt(arrobj.getStr("status"))) {
                    shenpicount += arrobj.getStr("count") == null ? ZwfwConstant.CONSTANT_INT_ZERO
                            : Integer.parseInt(arrobj.getStr("count"));
                }
                // 审批通过
                else if (ZwfwConstant.BANJIAN_STATUS_SPTG == Integer.parseInt(arrobj.getStr("status"))) {
                    sptgcount += arrobj.getStr("count") == null ? ZwfwConstant.CONSTANT_INT_ZERO
                            : Integer.parseInt(arrobj.getStr("count"));
                }
                // 审批不通过
                else if (ZwfwConstant.BANJIAN_STATUS_SPBTG == Integer.parseInt(arrobj.getStr("status"))) {
                    spbtgcount += arrobj.getStr("count") == null ? ZwfwConstant.CONSTANT_INT_ZERO
                            : Integer.parseInt(arrobj.getStr("count"));
                }
            }
            // 已暂停
            fieldstr = "count(*) count";// 查询条件
            groupstr = "";// 分组条件

            wherestr = "  task_id in (" + taskids + ") AND is_pause = '1' and areacode='" + baseareacode + "' "
                    + sqlExt;// 条件
            arrobj = getAuditProjectField(fieldstr, groupstr, wherestr);
            yizantingcount = Integer.parseInt(
                    (arrobj.getStr("count") == null ? ZwfwConstant.CONSTANT_STR_ZERO : arrobj.getStr("count")));
            // 待办结
            daibanjiecount = ysldbjcount + ysldbbcount + sptgcount + spbtgcount;
            // 审批中
            shenpicount = shenpicount + ysldbjcount + ysldbbcount;
        }

        map.put("DJJ", daijiejiancount);
        map.put("DSL", daishoulicount);
        map.put("YZT", yizantingcount);
        map.put("DBJ", daibanjiecount);
        map.put("DBB", daibubancount);
        map.put("DYS", daiyushencount);
        map.put("YSTG", yushentonguocount);
        map.put("SPZ", shenpicount);
        return map;

    }

    /**
     * 获取办件量排名
     *
     * @return
     */
    public List<Record> selectHotTaskId() {
        String sql = "select task_id ,count(*) as num from AUDIT_Project group by TASK_ID order by num desc LIMIT 20";
        List<Record> list = commonDao.findList(sql, Record.class);
        return list;
    }

    /**
     * 对办件信息根据条件查询
     *
     * @param fieldstr     查询字段
     * @param conditionMap 条件map， key为字段名称，value为值
     * @param first        起始记录数
     * @param pageSize     最大记录数
     * @param sortField    排序值
     * @param sortOrder    排序字段
     * @param keyword      搜索条件，主要是用来区别or,不用or不用传递
     * @return PageData<AuditTask>
     */
    @SuppressWarnings("unchecked")
    public <T> PageData<T> getRecordPageData(String fieldstr, Class<? extends BaseEntity> baseClass,
                                             Map<String, String> conditionMap, Integer first, Integer pageSize, String sortField, String sortOrder,
                                             String keyword) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(baseClass);
        PageData<T> pageData = new PageData<T>();
        Entity en = baseClass.getAnnotation(Entity.class);
        List<Object> params = new ArrayList<Object>();
        String sqlorder = "";
        // 条件sql
        StringBuffer sb = new StringBuffer();
        sb.append(sqlManageUtil.buildSql(conditionMap));
        // 拼接条件keyword
        if (StringUtil.isNotBlank(keyword)) {
            sb.append(" and (flowsn like ? or applyername like ? )");
            params.add("%" + keyword.replace("\\", "\\\\").replace("%", "\\%") + "%");
            params.add("%" + keyword.replace("\\", "\\\\").replace("%", "\\%") + "%");
        }
        // 增加Orderby语句
        if (StringUtil.isNotBlank(sortField)) {
            sb.append(" order by " + sortField + " " + sortOrder);
        }
        String sqlRecord = "select " + fieldstr + " from " + en.table() + sb.toString() + sqlorder;
        String sqlCount = "select count(*) from " + en.table() + sb.toString() + sqlorder;
        List<T> dataList = (List<T>) commonDao.findList(sqlRecord, first, pageSize, baseClass, params.toArray());
        int dataCount = commonDao.queryInt(sqlCount, params.toArray());
        pageData.setList(dataList);
        pageData.setRowCount(dataCount);
        return pageData;
    }

    @SuppressWarnings("unchecked")
    public <T> PageData<T> getRecordPageDataByHandleareacode(String fieldstr, Class<? extends BaseEntity> baseClass,
                                                             Map<String, String> conditionMap, Integer first, Integer pageSize, String sortField, String sortOrder,
                                                             String keyword, String handleareacode) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(baseClass);
        PageData<T> pageData = new PageData<T>();
        Entity en = baseClass.getAnnotation(Entity.class);
        List<Object> params = new ArrayList<Object>();
        String sqlorder = "";
        // 条件sql
        StringBuffer sb = new StringBuffer();
        sb.append(sqlManageUtil.buildSql(conditionMap));
        // 拼接条件keyword
        if (StringUtil.isNotBlank(keyword)) {
            sb.append(" and (flowsn like ? or applyername like ? )");
            params.add("%" + keyword.replace("\\", "\\\\").replace("%", "\\%") + "%");
            params.add("%" + keyword.replace("\\", "\\\\").replace("%", "\\%") + "%");
        }
        if (StringUtil.isNotBlank(handleareacode)) {
            sb.append(" and (handleareacode like ? or handleareacode like ? )");
            params.add("%" + handleareacode.replace("\\", "\\\\").replace("%", "\\%") + "%");
            params.add("%" + handleareacode.replace("\\", "\\\\").replace("%", "\\%") + ",%");
        }
        // 增加Orderby语句
        if (StringUtil.isNotBlank(sortField)) {
            sb.append(" order by " + sortField + " " + sortOrder);
        }
        String sqlRecord = "select " + fieldstr + " from " + en.table() + sb.toString() + sqlorder;
        String sqlCount = "select count(*) from " + en.table() + sb.toString() + sqlorder;
        List<T> dataList = (List<T>) commonDao.findList(sqlRecord, first, pageSize, baseClass, params.toArray());
        int dataCount = commonDao.queryInt(sqlCount, params.toArray());
        pageData.setList(dataList);
        pageData.setRowCount(dataCount);
        return pageData;
    }

    /**
     * 对办件信息根据条件查询
     *
     * @param conditionMap 条件map， key为字段名称，value为值
     * @param first        起始记录数
     * @param pageSize     最大记录数
     * @param sortField    排序值
     * @param sortOrder    排序字段
     * @return PageData<AuditTask>
     */
    public Map<String, String> getReportCount(String ouguid, String areaCode) {

        Map<String, String> map = new HashMap<String, String>(16);
        int JISHOUNUM = 0; // 即办件收件
        int JIBANNUM = 0; // 即办件办结
        int CNSHOUNUM = 0; // 承诺收件
        int CNBANNUM = 0; // 承诺办结
        int BUSHOUNUM = 0; // 不予受理
        int CQNUM = 0; // 超期办件
        int CNNUM = 0; // 承诺办件
        double JIBANLV; // 即办件率
        double CNTQLV; // 承诺提前办结率

        AuditProject arrobj = null;

        String fieldstr = "status, count(1) count";// 查询条件
        String groupstr = "  group by STATUS";// 分组条件
        String wherestr = " areacode='" + areaCode + "' ";

        // 即办件收件
        fieldstr = " ouname ,ouguid, count(*) as JISHOUNUM ";// 查询条件
        groupstr = "";// 分组条件
        wherestr = " status BETWEEN 30 and 90 and tasktype='1' and OUGUID ='" + ouguid + "' and areacode ='" + areaCode
                + "'";// 条件
        arrobj = getAuditProjectField(fieldstr, groupstr, wherestr);
        JISHOUNUM = Integer.parseInt((arrobj.getStr("count") == null ? "0" : arrobj.getStr("JISHOUNUM")));

        // 即办件办结
        fieldstr = " ouname ,ouguid, count(*) as JIBANNUM ";// 查询条件
        groupstr = "";// 分组条件
        wherestr = " status=90  and tasktype='1' and OUGUID ='" + ouguid + "' and areacode ='" + areaCode + "'";// 条件
        arrobj = getAuditProjectField(fieldstr, groupstr, wherestr);
        JIBANNUM = Integer.parseInt((arrobj.getStr("count") == null ? "0" : arrobj.getStr("JIBANNUM")));

        // 承诺件收件
        fieldstr = " ouname ,ouguid, count(*) as CNSHOUNUM ";// 查询条件
        groupstr = "";// 分组条件
        wherestr = " status BETWEEN 30 and 90 and tasktype='2' and OUGUID ='" + ouguid + "' and areacode ='" + areaCode
                + "'";// 条件
        arrobj = getAuditProjectField(fieldstr, groupstr, wherestr);
        CNSHOUNUM = Integer.parseInt((arrobj.getStr("count") == null ? "0" : arrobj.getStr("CNSHOUNUM")));

        // 承诺件办结
        fieldstr = " ouname ,ouguid, count(*) as CNBANNUM ";// 查询条件
        groupstr = "";// 分组条件
        wherestr = " status=90  and tasktype='2' and OUGUID ='" + ouguid + "' and areacode ='" + areaCode + "'";// 条件
        arrobj = getAuditProjectField(fieldstr, groupstr, wherestr);
        CNBANNUM = Integer.parseInt((arrobj.getStr("count") == null ? "0" : arrobj.getStr("CNBANNUM")));

        // 不予受理
        fieldstr = "ouname ,ouguid, count(*) as BUSHOUNUM ";// 查询条件
        groupstr = "";// 分组条件
        wherestr = "  status =97 and OUGUID ='" + ouguid + "' and areacode ='" + areaCode + "'";// 条件
        arrobj = getAuditProjectField(fieldstr, groupstr, wherestr);
        BUSHOUNUM = Integer.parseInt((arrobj.getStr("count") == null ? "0" : arrobj.getStr("BUSHOUNUM")));

        // 超期办件数
        fieldstr = "ouname ,ouguid, count(*) as CQNUM ";// 查询条件
        groupstr = "";// 分组条件
        wherestr = " rowguid in (select projectguid from audit_project_sparetime where SPAREMINUTES<0) and tasktype='2' and areacode ='"
                + areaCode + "' and OUGUID ='" + ouguid + "' ";// 条件
        arrobj = getAuditProjectField(fieldstr, groupstr, wherestr);
        CQNUM = Integer.parseInt((arrobj.getStr("count") == null ? "0" : arrobj.getStr("CQNUM")));

        // 承诺件办结（不超过承诺截止时间）
        fieldstr = " ouname ,ouguid, count(*) as CNNUM ";// 查询条件
        groupstr = "";// 分组条件
        wherestr = " status=90  and tasktype='2'  and OUGUID ='" + ouguid + "' and areacode ='" + areaCode
                + "' and SPARETIME>0 ";// 条件
        arrobj = getAuditProjectField(fieldstr, groupstr, wherestr);
        CNNUM = Integer.parseInt((arrobj.getStr("count") == null ? "0" : arrobj.getStr("CNNUM")));

        if ((JISHOUNUM + CNSHOUNUM) != 0) {
            JIBANLV = (double) JISHOUNUM / (JISHOUNUM + CNSHOUNUM);
        } else {
            JIBANLV = 0.00000;
        }
        if (CNSHOUNUM != 0) {
            CNTQLV = (double) CNNUM / CNSHOUNUM;
        } else {
            CNTQLV = 0.00000;
        }
        map.put("JISHOUNUM", JISHOUNUM + "");
        map.put("JIBANNUM", JIBANNUM + "");
        map.put("CNSHOUNUM", CNSHOUNUM + "");
        map.put("CNBANNUM", CNBANNUM + "");
        map.put("LEISHOUNUM", JISHOUNUM + CNSHOUNUM + "");
        map.put("LEIBANNUM", JIBANNUM + CNBANNUM + "");
        map.put("BUSHOUNUM", BUSHOUNUM + "");
        map.put("BUBANNUM", BUSHOUNUM + "");
        map.put("CQNUM", CQNUM + "");
        map.put("JIBANLV", JIBANLV + "");
        map.put("CNTIQIANLV", CNTQLV + "");
        return map;

    }

    /**
     * 办件报表相关数据查询
     *
     * @param windowguid 窗口guid
     * @return 不同状态的办件数量Map集合
     */
    public Map<String, String> getReportCount(String ouguid, String areaCode, String startDate, String endDate) {

        Map<String, String> map = new HashMap<String, String>(16);
        int JISHOUNUM = 0; // 即办件收件
        int JIBANNUM = 0; // 即办件办结
        int CNSHOUNUM = 0; // 承诺收件
        int CNBANNUM = 0; // 承诺办结
        int BUSHOUNUM = 0; // 不予受理
        int CQNUM = 0; // 超期办件
        int CNNUM = 0; // 承诺办件
        double JIBANLV; // 即办件率
        double CNTQLV; // 承诺提前办结率

        AuditProject arrobj = null;

        String fieldstr = "status, count(1) count";// 查询条件
        String groupstr = "  group by STATUS";// 分组条件
        String wherestr = " areacode='" + areaCode + "' ";
        String searchstr = " and APPLYDATE between str_to_date('" + startDate
                + " 00:00:00','%Y-%m-%d %H:%i:%s') and str_to_date('" + endDate + " 23:59:59','%Y-%m-%d %H:%i:%s')";// 搜索条件

        // 即办件收件
        fieldstr = " ouname ,ouguid, count(*) as JISHOUNUM ";// 查询条件
        groupstr = "";// 分组条件
        wherestr = " status BETWEEN 30 and 90 and tasktype='1' and OUGUID ='" + ouguid + "' and areacode ='" + areaCode
                + "'" + searchstr;// 条件
        arrobj = getAuditProjectField(fieldstr, groupstr, wherestr);
        JISHOUNUM = Integer.parseInt((arrobj.getStr("count") == null ? "0" : arrobj.getStr("JISHOUNUM")));

        // 即办件办结
        fieldstr = " ouname ,ouguid, count(*) as JIBANNUM ";// 查询条件
        groupstr = "";// 分组条件
        wherestr = " status=90  and tasktype='1' and OUGUID ='" + ouguid + "' and areacode ='" + areaCode + "'"
                + searchstr;// 条件
        arrobj = getAuditProjectField(fieldstr, groupstr, wherestr);
        JIBANNUM = Integer.parseInt((arrobj.getStr("count") == null ? "0" : arrobj.getStr("JIBANNUM")));

        // 承诺件收件
        fieldstr = " ouname ,ouguid, count(*) as CNSHOUNUM ";// 查询条件
        groupstr = "";// 分组条件
        wherestr = " status BETWEEN 30 and 90 and tasktype='2' and OUGUID ='" + ouguid + "' and areacode ='" + areaCode
                + "'" + searchstr;// 条件
        arrobj = getAuditProjectField(fieldstr, groupstr, wherestr);
        CNSHOUNUM = Integer.parseInt((arrobj.getStr("count") == null ? "0" : arrobj.getStr("CNSHOUNUM")));

        // 承诺件办结
        fieldstr = " ouname ,ouguid, count(*) as CNBANNUM ";// 查询条件
        groupstr = "";// 分组条件
        wherestr = " status=90  and tasktype='2' and OUGUID ='" + ouguid + "' and areacode ='" + areaCode + "'"
                + searchstr;// 条件
        arrobj = getAuditProjectField(fieldstr, groupstr, wherestr);
        CNBANNUM = Integer.parseInt((arrobj.getStr("count") == null ? "0" : arrobj.getStr("CNBANNUM")));

        // 不予受理
        fieldstr = "ouname ,ouguid, count(*) as BUSHOUNUM ";// 查询条件
        groupstr = "";// 分组条件
        wherestr = "  status =97 and OUGUID ='" + ouguid + "' and areacode ='" + areaCode + "'" + searchstr;// 条件
        arrobj = getAuditProjectField(fieldstr, groupstr, wherestr);
        BUSHOUNUM = Integer.parseInt((arrobj.getStr("count") == null ? "0" : arrobj.getStr("BUSHOUNUM")));

        // 超期办件数
        fieldstr = "ouname ,ouguid, count(*) as CQNUM ";// 查询条件
        groupstr = "";// 分组条件
        wherestr = " rowguid in (select projectguid from audit_project_sparetime where SPAREMINUTES<0) and tasktype='2' and areacode ='"
                + areaCode + "' and OUGUID ='" + ouguid + "' " + searchstr;// 条件
        arrobj = getAuditProjectField(fieldstr, groupstr, wherestr);
        CQNUM = Integer.parseInt((arrobj.getStr("count") == null ? "0" : arrobj.getStr("CQNUM")));

        // 承诺件办结（不超过承诺截止时间）
        fieldstr = " ouname ,ouguid, count(*) as CNNUM ";// 查询条件
        groupstr = "";// 分组条件
        wherestr = " status=90  and tasktype='2'  and OUGUID ='" + ouguid + "' and areacode ='" + areaCode
                + "' and SPARETIME>0 " + searchstr;// 条件
        arrobj = getAuditProjectField(fieldstr, groupstr, wherestr);
        CNNUM = Integer.parseInt((arrobj.getStr("count") == null ? "0" : arrobj.getStr("CNNUM")));

        if ((JISHOUNUM + CNSHOUNUM) != 0) {
            JIBANLV = (double) JISHOUNUM / (JISHOUNUM + CNSHOUNUM);
        } else {
            JIBANLV = 0.00000;
        }
        if (CNSHOUNUM != 0) {
            CNTQLV = (double) CNNUM / CNSHOUNUM;
        } else {
            CNTQLV = 0.00000;
        }
        map.put("JISHOUNUM", JISHOUNUM + "");
        map.put("JIBANNUM", JIBANNUM + "");
        map.put("CNSHOUNUM", CNSHOUNUM + "");
        map.put("CNBANNUM", CNBANNUM + "");
        map.put("LEISHOUNUM", JISHOUNUM + CNSHOUNUM + "");
        map.put("LEIBANNUM", JIBANNUM + CNBANNUM + "");
        map.put("BUSHOUNUM", BUSHOUNUM + "");
        map.put("BUBANNUM", BUSHOUNUM + "");
        map.put("CQNUM", CQNUM + "");
        map.put("JIBANLV", JIBANLV + "");
        map.put("CNTIQIANLV", CNTQLV + "");
        return map;

    }

    public List<Record> getReportOu(String areaCode) {
        String sql = "select ouname,ouguid from Audit_Project where ouname!='' and ouname !='&nbsp;&nbsp;政务服务中心' and areacode =?  group by OUNAME,OUGUID";
        List<Record> findList = commonDao.findList(sql, Record.class, areaCode);
        commonDao.close();
        return findList;
    }

    public List<Record> getHotBanJianList(List<String> taskGuidList, String centerguid) {
        List<Object> params = new ArrayList<Object>();
        String sql = "";
        sql = "select count(*) as num,TASK_ID from audit_project where centerguid = ? and task_id in ('"
                + StringUtil.join(taskGuidList, "','") + "')  GROUP BY TASK_ID ORDER BY num DESC ";
        params.add(centerguid);
        List<Record> findList = commonDao.findList(sql, 1, 3, Record.class, params.toArray());
        commonDao.close();
        return findList;
    }

    public List<Record> getHotBanJianList(List<String> taskGuidList, String centerguid, String areacode) {
        String sql = "";
        SqlConditionUtil sqlUtil = new SqlConditionUtil();
        sqlUtil.eq("centerguid", centerguid);
        sqlUtil.eq("areacode", areacode);
        SQLManageUtil sqlManageUtil = new SQLManageUtil(AuditProject.class);
        // 条件sql
        StringBuffer sb = new StringBuffer();
        sb.append(sqlManageUtil.buildPatchSql(sqlUtil.getMap()));
        sql = "select count(*) as num,TASK_ID from audit_project where task_id in ('"
                + StringUtil.join(taskGuidList, "','") + "') " + sb.toString()
                + "  GROUP BY TASK_ID ORDER BY num DESC ";
        List<Record> findList = commonDao.findList(sql, 0, 3, Record.class);
        commonDao.close();
        return findList;
    }

    public List<Record> getWeekLineList(List<String> ouguidlist, String status, Date date) {
        String sql = "";
        SqlConditionUtil sqlUtil = new SqlConditionUtil();
        sqlUtil.ge("BANJIEDATE", date);
        sqlUtil.eq("status", status);
        SQLManageUtil sqlManageUtil = new SQLManageUtil(AuditProject.class);
        // 条件sql
        StringBuffer sb = new StringBuffer();
        sb.append(sqlManageUtil.buildPatchSql(sqlUtil.getMap()));

        sql = "select count(*) as num,ouguid from audit_project where ouguid in ('" + StringUtil.join(ouguidlist, "','")
                + "') " + sb.toString() + "GROUP BY ouguid ORDER BY num DESC ";
        List<Record> findList = commonDao.findList(sql, Record.class);
        commonDao.close();
        return findList;
    }

    public List<Record> getWeekLineList(String centerguid, String areacode, String status, Date date) {
        String sql = "";
        SqlConditionUtil sqlUtil = new SqlConditionUtil();
        sqlUtil.ge("APPLYDATE", date);
        sqlUtil.ge("status", status);
        sqlUtil.eq("centerguid", centerguid);
        SQLManageUtil sqlManageUtil = new SQLManageUtil(AuditProject.class);
        // 条件sql
        StringBuffer sb = new StringBuffer();
        sb.append(sqlManageUtil.buildPatchSql(sqlUtil.getMap()));
        // '"+areacode+"'
        sql = "select count(*) as num,ouguid from audit_project where areacode =?   " + sb.toString()
                + "GROUP BY ouguid ORDER BY num DESC ";
        List<Record> findList = commonDao.findList(sql, Record.class, areacode);
        commonDao.close();
        return findList;
    }

    public List<Record> getWeekLineListByOu(List<String> ouGuidList, String status, Date date) {
        String sql = "";
        SqlConditionUtil sqlUtil = new SqlConditionUtil();
        sqlUtil.ge("ACCEPTUSERDATE", date);
        sqlUtil.ge("status", status);
        SQLManageUtil sqlManageUtil = new SQLManageUtil(AuditProject.class);
        // 条件sql
        StringBuffer sb = new StringBuffer();
        sb.append(sqlManageUtil.buildPatchSql(sqlUtil.getMap()));
        sql = "select count(*) as num,ouguid from audit_project where ouguid in ('" + StringUtil.join(ouGuidList, "','")
                + "') " + sb.toString() + "GROUP BY ouguid ORDER BY num DESC ";
        List<Record> findList = commonDao.findList(sql, Record.class);
        commonDao.close();
        return findList;
    }

    public Integer getAuditProjectCountByApplyway(Map<String, String> conditionMap, String applyway) {
        String sql = "";
        SQLManageUtil sqlManageUtil = new SQLManageUtil(AuditProject.class);
        // 条件sql
        StringBuffer sb = new StringBuffer();
        sb.append(sqlManageUtil.buildSql(conditionMap));
        sql = "select count(*) as num from audit_project  " + sb.toString()
                + "and taskguid in (select rowguid from audit_task where shenpilb = ? )  ";
        Integer findList = commonDao.queryInt(sql, applyway);
        commonDao.close();
        return findList;
    }

    public List<Record> getAuditProjectCountByCondition(Map<String, String> conditionMap) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(AuditProject.class);
        // 条件sql
        StringBuffer sb = new StringBuffer();
        sb.append(sqlManageUtil.buildSql(conditionMap));
        String sql = "select count(*) as num,tasktype from audit_project " + sb.toString() + " GROUP BY tasktype";
        List<Record> findList = commonDao.findList(sql, Record.class);
        commonDao.close();
        return findList;
    }

    public Integer getTodayHandleProjectCount(String conditionMap) {
        int count = 0;
        String sql = "select count(*) from audit_project " + conditionMap;
        count = commonDao.queryInt(sql);
        commonDao.close();
        return count;
    }

    public Integer getTodayHandleProjectCount(String conditionMap, String field) {
        int count = 0;
        String sql = "select count(*) from audit_project " + conditionMap;
        if (commonDao.isMySql()) {
            sql += "and date(" + field + ") = curdate()";
        } else if (commonDao.isOracle()) {
            sql += "and to_char(" + field + ",'yyyy-MM-dd')=to_char(sysdate,'yyyy-MM-dd')";
        } else if (commonDao.isDM()) {
            sql += "and to_char(" + field + ",'yyyy-MM-dd')=to_char(sysdate,'yyyy-MM-dd')";
        }
        count = commonDao.queryInt(sql);
        commonDao.close();
        return count;
    }

    public Integer getOverUserCount(Map<String, String> conditionMap, int todayHandle) {
        int count = 0;
        SQLManageUtil sqlManageUtil = new SQLManageUtil(AuditProject.class);
        // 条件sql
        StringBuffer sb = new StringBuffer();
        sb.append(sqlManageUtil.buildSql(conditionMap));
        String sql = "";
        if (commonDao.isMySql()) {
            sql = "select count(*) from ( select count(*),ACCEPTUSERGUID FROM audit_project " + sb.toString()
                    + "and date(ACCEPTUSERDATE) = curdate() GROUP BY ACCEPTUSERGUID HAVING COUNT(*)< ? )t";
        } else if (commonDao.isOracle()) {
            sql = "select count(*) from ( select count(*),ACCEPTUSERGUID FROM audit_project " + sb.toString()
                    + "and to_char(ACCEPTUSERDATE,'yyyy-MM-dd')=to_char(sysdate,'yyyy-MM-dd') GROUP BY ACCEPTUSERGUID HAVING COUNT(*)< ? )t";
        } else if (commonDao.isDM()) {
            sql = "select count(*) from ( select count(*),ACCEPTUSERGUID FROM audit_project " + sb.toString()
                    + "and to_char(ACCEPTUSERDATE,'yyyy-MM-dd')=to_char(sysdate,'yyyy-MM-dd') GROUP BY ACCEPTUSERGUID HAVING COUNT(*)< ? )t";
        }

        count = commonDao.queryInt(sql, todayHandle);
        commonDao.close();
        return count;
    }

    public Integer getOverFinishCount(Map<String, String> conditionMap, int todayFinish) {
        int count = 0;
        SQLManageUtil sqlManageUtil = new SQLManageUtil(AuditProject.class);
        // 条件sql
        StringBuffer sb = new StringBuffer();
        sb.append(sqlManageUtil.buildSql(conditionMap));
        String sql = "";
        if (commonDao.isMySql()) {
            sql = "select count(*) from ( select count(*),BANJIEUSERGUID FROM audit_project " + sb.toString()
                    + "and date(BANJIEDATE) = curdate() GROUP BY BANJIEUSERGUID HAVING COUNT(*)< ? )t";
        } else if (commonDao.isOracle()) {
            sql = "select count(*) from ( select count(*),BANJIEUSERGUID FROM audit_project " + sb.toString()
                    + "and to_char(BANJIEDATE,'yyyy-MM-dd')=to_char(sysdate,'yyyy-MM-dd') GROUP BY BANJIEUSERGUID HAVING COUNT(*)< ? )t";

        } else if (commonDao.isDM()) {
            sql = "select count(*) from ( select count(*),BANJIEUSERGUID FROM audit_project " + sb.toString()
                    + "and to_char(BANJIEDATE,'yyyy-MM-dd')=to_char(sysdate,'yyyy-MM-dd') GROUP BY BANJIEUSERGUID HAVING COUNT(*)< ? )t";

        }
        count = commonDao.queryInt(sql, todayFinish);
        commonDao.close();
        return count;
    }

    public Integer getAvgSpendtime(Map<String, String> map) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(AuditProject.class);
        String sql = "select floor(avg(spendtime)) spendtime from audit_project where areacode='320001' "
                + sqlManageUtil.buildPatchSql(map);
        return commonDao.queryInt(sql);
    }

    public PageData<AuditProject> getPagaBySpareTime(String fieldStr, Map<String, String> conditionMap,
                                                     Integer firstResult, Integer maxResults, String sortField, String sortOrder, String string) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(AuditProject.class);
        PageData<AuditProject> pageData = new PageData<AuditProject>();
        String sqlext = sqlManageUtil.buildPatchSql(conditionMap);
        String sqlRecord = "select " + fieldStr
                + " from  audit_project a,audit_project_sparetime b where a.rowguid=b.projectguid " + sqlext;
        String sqlCount = "select count(1) from  audit_project a,audit_project_sparetime b where a.rowguid=b.projectguid "
                + sqlext;
        List<AuditProject> dataList = commonDao.findList(sqlRecord, firstResult, maxResults, AuditProject.class);
        int dataCount = commonDao.queryInt(sqlCount);
        pageData.setList(dataList);
        pageData.setRowCount(dataCount);
        return pageData;
    }

    public Integer getPagaBySpareTimeCount(Map<String, String> conditionMap) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(AuditProject.class);
        String sqlext = sqlManageUtil.buildPatchSql(conditionMap);
        String sqlCount = "select count(1) from  audit_project a,audit_project_sparetime b where a.rowguid=b.projectguid "
                + sqlext;
        int dataCount = commonDao.queryInt(sqlCount);
        return dataCount;
    }

    public PageData<AuditProject> getAuditProjectListByBZ(String fieldstr, Map<String, String> map, int first,
                                                          int pageSize, String sortField, String sortOrder, String string) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(AuditProject.class);
        PageData<AuditProject> pageData = new PageData<AuditProject>();
        String sqlext = sqlManageUtil.buildSql(map);
        String sqlRecord = "select " + fieldstr + " from  audit_project a " + sqlext
                + " and  EXISTS(select a.RowGuid from audit_project_operation b where a.rowguid=b.projectguid and operatetype='22')";
        String sqlCount = "select count(1) from  audit_project a  " + sqlext
                + " and  EXISTS(select a.RowGuid from audit_project_operation b where a.rowguid=b.projectguid and operatetype='22')";
        List<AuditProject> dataList = commonDao.findList(sqlRecord, first, pageSize, AuditProject.class);
        int dataCount = commonDao.queryInt(sqlCount);
        pageData.setList(dataList);
        pageData.setRowCount(dataCount);
        return pageData;
    }

    /**
     * 获取办件列表 带分页
     */
    public PageData<AuditProject> selectRecordListBTS(String fieldstr, String projectname, String flowsn, int first,
                                                      int pageSize, String sortField, String sortOrder) {
        String sql = "";
        String sqlCount = "";
        List<Object> params = new ArrayList<Object>();
        sql = "select * from audit_project where FLOWSN  not in "
                + "(select INTERNAL_NO from AUDIT_RS_APPLY_JS)   and status =? ";

        sqlCount = "select count(1) from audit_project where FLOWSN  not in "
                + "(select INTERNAL_NO from AUDIT_RS_APPLY_JS)   and status =? ";

        params.add(ZwfwConstant.BANJIAN_STATUS_ZCBJ);
        int count = 0;
        if (StringUtil.isNotBlank(projectname)) {
            sql += " and projectname like '%" + projectname + "%'";
            sqlCount += " and projectname like '%" + projectname + "%'";
            params.add("%" + projectname.replace("\\", "\\\\").replace("%", "\\%") + "%");
        }
        if (StringUtil.isNotBlank(flowsn)) {
            sql += " and flowsn =? ";
            sqlCount += " and flowsn =? ";
            params.add(flowsn);
        }
        sql += " order by OperateDate desc";
        List<AuditProject> list = commonDao.findList(sql, first, pageSize, AuditProject.class, params.toArray());
        count = commonDao.queryInt(sqlCount, params.toArray());
        PageData<AuditProject> pageData = new PageData<>();
        pageData.setList(list);
        pageData.setRowCount(count);
        return pageData;
    }

    public List<AuditProject> selectRecordListBTSAN(String fieldstr, String projectname, String flowsn,
                                                    String handleProjectGuid) {
        String sql = "";
        List<Object> params = new ArrayList<Object>();
        sql = "select * from audit_project  where FLOWSN  not in "
                + "(select INTERNAL_NO from AUDIT_RS_APPLY_JS)   and status = " + ZwfwConstant.BANJIAN_STATUS_ZCBJ + "";
        if (StringUtil.isNotBlank(projectname)) {
            sql += " and projectname like ? ";
            params.add("%" + projectname.replace("\\", "\\\\").replace("%", "\\%") + "%");
        }
        if (StringUtil.isNotBlank(flowsn)) {
            sql += " and flowsn = ?";
            params.add(flowsn);
        }
        if (StringUtil.isNotBlank(handleProjectGuid)) {
            sql += " and rowguid = ? ";
            params.add(handleProjectGuid);
        }
        sql += " order by OperateDate desc";
        List<AuditProject> list = commonDao.findList(sql, AuditProject.class, params.toArray());
        return list;

    }

    public List<Record> getProjectnumGroupByTaskid(String groupfield, Integer recordnum,
                                                   Map<String, String> conditionmap) {
        SQLManageUtil sqlm = new SQLManageUtil();
        String sqle = sqlm.buildSql(conditionmap);
        String sql = "select count(1) as num," + groupfield + " from audit_project " + sqle + "group by " + groupfield
                + " order by num desc";
        return commonDao.findList(sql, 0, recordnum, Record.class);
    }

    public PageData<AuditProjectUnusual> getAuditProjectUnusualListByPage(Map<String, String> conditionMap, int first,
                                                                          int pageSize, String sortField, String sortOrder) {
        PageData<AuditProjectUnusual> pageData = new PageData<AuditProjectUnusual>();
        SQLManageUtil sm = new SQLManageUtil();
        String order = "";
        if (StringUtil.isNotBlank(sortField) && StringUtil.isNotBlank(sortOrder)) {
            order = " order by " + sortField + " " + sortOrder;
        }
        String sqle = sm.buildSql(conditionMap);
        String sql = "select * from Audit_Project_Unusual " + sqle + order;
        String sqlcount = sql.replace("*", "count(1)");
        List<AuditProjectUnusual> auditList = commonDao.findList(sql, first, pageSize, AuditProjectUnusual.class);
        pageData.setList(auditList);
        pageData.setRowCount(commonDao.queryInt(sqlcount));
        return pageData;
    }

    /**
     * 分组获取满意度
     */
    public List<Record> getAuditProjectSatisfiedList(Map<String, String> conditionMap) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(AuditProject.class);
        // 条件sql
        StringBuffer sb = new StringBuffer();
        sb.append(sqlManageUtil.buildSql(conditionMap));

        String sql = "select b.satisfied ,count(1) count from audit_project a,audit_online_evaluat b " + sb.toString()
                + " and a.rowguid = b.clientidentifier GROUP BY b.satisfied";
        List<Record> findList = commonDao.findList(sql, Record.class);
        return findList;
    }

    public Integer getProjectMaterialCountByCondition(Map<String, String> condition) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil();
        String sqle = sqlManageUtil.buildSql(condition);
        String sql = "select count(1) from audit_project_material " + sqle;
        return commonDao.queryInt(sql);
    }

    public List<WorkflowWorkItem> getWorkItemListByPVIGuidAndStatus(String pviguid) {
        String sql = "select *  from Workflow_WorkItem where processVersionInstanceGuid = ? and (status =10 or status =20)  ";
        return commonDao.findList(sql, WorkflowWorkItem.class, pviguid);
    }

    public Record getMaxZjNum(String name, String year) {
        String sql = "select * from zj_num where name = ? and year = ? ";
        return commonDao.find(sql, Record.class, name, year);
    }

    public void UpdateMaxZjNum(String maxnum, String name, String year) {
        String sql = "update zj_num set maxnum = ? where name = ? and year = ? ";
        commonDao.execute(sql, maxnum, name, year);
    }

    public Record getMaxZjNumNew(String name) {
        String sql = "select * from zj_num where name = ? ";
        return commonDao.find(sql, Record.class, name);
    }

    public void UpdateMaxZjNumNew(String maxnum, String name) {
        String sql = "update zj_num set maxnum = ? where name = ? ";
        commonDao.execute(sql, maxnum, name);
    }

    public void insetZjNum(String maxnum, String name, String year) {
        String sql = "insert into zj_num(maxnum,name,year) values (?, ?,?)";
        commonDao.execute(sql, maxnum, name, year);
    }

    public ParticipantsInfo getItemlegaldept(String itemGuid) {
        String sql = "select * from participants_info where ITEMGUID = ? and CORPTYPE = '31'";
        return commonDao.find(sql, ParticipantsInfo.class, itemGuid);
    }

    public int updateProjectCertRowguid(String certrowguid, String projectGuid) {
        String sql = "UPDATE audit_project SET certrowguid  = ? WHERE RowGuid = ?";
        return commonDao.execute(sql, certrowguid, projectGuid);
    }

    public AuditTask getAuditTaskByUnid(String unid) {
        String sql = "select * from audit_task where unid = ? ";
        return commonDao.find(sql, AuditTask.class, unid);
    }

    public int getJzListCount(String OUGuids, String areaCode) {
        String sql = "";
        List<Object> params = new ArrayList<Object>();
        if ("".equals(OUGuids) || OUGuids == null) {
            sql = "select count(1) as total from audit_project where IF_JZ_HALL=1 where 1 = 2";
        } else {
            sql = "select count(1) as total from audit_project where IF_JZ_HALL=1 and areacode=? and ((status>='" + ZwfwConstant.BANJIAN_STATUS_DJJ
                    + "' and status <=? ) or status=? or status=? ) and OUGuid in ";
            Parameter pa = DbKit.splitIn(OUGuids);
            sql += pa.getSql();
            params.add(areaCode);
            params.add(ZwfwConstant.BANJIAN_STATUS_SPTG);
            params.add(ZwfwConstant.BANJIAN_STATUS_WWYTJ);
            params.add(ZwfwConstant.BANJIAN_STATUS_WWYSTG);
            for (Object obj : pa.getValue()) {
                params.add(obj);
            }
        }
        return commonDao.queryInt(sql, params.toArray());
    }


}
