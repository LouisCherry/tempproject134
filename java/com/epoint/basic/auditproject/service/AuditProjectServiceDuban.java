package com.epoint.basic.auditproject.service;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditprojectoperation.domain.AuditProjectOperation;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwfw.authentication.ZwfwUserSession;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.config.SplitTableConfig;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.sharding.util.ShardingUtil;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

public class AuditProjectServiceDuban {
    private ICommonDao commonDao;

    public AuditProjectServiceDuban() {
        this.commonDao = CommonDao.getInstance();
    }

    public AuditProjectServiceDuban(Class<? extends BaseEntity> baseClass) {
        Entity en = baseClass.getAnnotation(Entity.class);
        SplitTableConfig conf = ShardingUtil.getSplitTableConfig(en.table());
        if (conf != null) {
            this.commonDao = CommonDao.getInstance(conf);
        } else {
            this.commonDao = CommonDao.getInstance();
        }

    }

    public void addRecord(Class<? extends BaseEntity> baseClass, Record record) {
        Entity en = baseClass.getAnnotation(Entity.class);
        if (StringUtil.isNotBlank(en.table())) {
            this.commonDao.insert(record);
        }

    }

    public void updateRecord(Class<? extends BaseEntity> baseClass, Record record) {
        Entity en = baseClass.getAnnotation(Entity.class);
        if (StringUtil.isNotBlank(en.table())) {
            this.commonDao.update(record);
        }

    }

    public void deleteRecods(Class<? extends BaseEntity> baseClass, String keyValue, String key) {
        Entity en = baseClass.getAnnotation(Entity.class);
        if (StringUtil.isNotBlank(en.table())) {
            String sql = "delete from " + en.table() + " where " + key + "=?1";
            this.commonDao.execute(sql, new Object[]{keyValue});
        }

    }

    public void deleteRecodByMap(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap) {
        Entity en = baseClass.getAnnotation(Entity.class);
        if (StringUtil.isNotBlank(en.table())) {
            String sql = "delete from " + en.table();
            SQLManageUtil sqlManageUtil = new SQLManageUtil();
            sql = sql + sqlManageUtil.buildSql(conditionMap);
            this.commonDao.execute(sql, new Object[0]);
        }

    }

    public <T> T getDetail(String fieldstr, Class<? extends BaseEntity> baseClass, String rowGuid, String key) {
        String sql = "";
        Entity en = baseClass.getAnnotation(Entity.class);
        if (StringUtil.isNotBlank(en.table())) {
            sql = "select " + fieldstr + " from " + en.table() + " where " + key + "=?1";
        }

        return StringUtil.isNotBlank(sql) ? (T) this.commonDao.find(sql, baseClass, new Object[]{rowGuid}) : null;
    }

    public <T> T getDetail(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(baseClass);
        return sqlManageUtil.getBeanByCondition(baseClass, conditionMap);
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
            sql = "update " + en.table() + " set ";

            Entry entry;
            for (Iterator sqlManageUtil = updateFieldMap.entrySet().iterator(); sqlManageUtil
                    .hasNext(); sql = sql + (String) entry.getKey() + "\'" + (String) entry.getValue() + "\',") {
                entry = (Entry) sqlManageUtil.next();
            }

            sql = sql.substring(0, sql.length() - 1);
            SQLManageUtil sqlManageUtil1 = new SQLManageUtil(baseClass);
            sql = sql + sqlManageUtil1.buildSql(conditionMap);
            this.commonDao.execute(sql, new Object[0]);
        }

    }

    public List<AuditProjectOperation> getAuditProjectOperationFieldList(String fieldstr, String whereStr,
                                                                         String grouporsortstr) {
        String sql = "select " + fieldstr + " from Audit_Project_Operation where " + whereStr + grouporsortstr;
        List findList = this.commonDao.findList(sql, AuditProjectOperation.class, new Object[0]);
        return findList;
    }

    public AuditProjectOperation getAuditProjectOperationField(String fieldstr, String whereStr,
                                                               String grouporsortstr) {
        String sql = "select " + fieldstr + " from Audit_Project_Operation where " + whereStr + grouporsortstr;
        AuditProjectOperation findList = this.commonDao.find(sql, AuditProjectOperation.class, new Object[0]);
        return findList;
    }

    public List<AuditProject> getAuditProjectFieldList(String fieldstr, String grouporsortstr, String intaskguids) {
        String sql = "select " + fieldstr + " from audit_project p where " + intaskguids + grouporsortstr;
        List findList = this.commonDao.findList(sql, AuditProject.class, new Object[0]);
        return findList;
    }

    public AuditProject getAuditProjectField(String fieldstr, String grouporsortstr, String whereStr) {
        String sql = "select " + fieldstr + " from audit_project p where " + whereStr + grouporsortstr;
        AuditProject findList = this.commonDao.find(sql, AuditProject.class, new Object[0]);
        return findList;
    }

    public AuditProject getAuditProjectAndAudittaskField(String fieldstr, String grouporsortstr, String whereStr) {
        String sql = "select " + fieldstr
                + " from audit_project p INNER JOIN audit_task_extension t ON p.TASKGUID = t.taskguid where " + whereStr
                + grouporsortstr;
        AuditProject findList = this.commonDao.find(sql, AuditProject.class, new Object[0]);
        return findList;
    }

    public PageData<AuditProject> getBanJianListByPage(String fieldstr, String projectType, String taskguids, int first,
                                                       int pageSize, String areaCode, String applyerName) {
        byte banjianStatus = 0;
        if (StringUtil.isBlank(applyerName)) {
            applyerName = "";
        }

        if ("DJJ".equals(projectType)) {
            banjianStatus = 24;
        } else if ("DSL".equals(projectType)) {
            banjianStatus = 26;
        } else if ("DBB".equals(projectType)) {
            banjianStatus = 28;
        } else if ("DYS".equals(projectType)) {
            banjianStatus = 12;
        } else if ("YSTG".equals(projectType)) {
            banjianStatus = 16;
        }

        String sql = "";
        String sqlCount = "";
        if (!"".equals(taskguids) && taskguids != null) {
            if (StringUtil.isNotBlank(projectType) && "YZT".equals(projectType)) {
                sql = "select " + fieldstr + " from audit_project where areacode=\'" + areaCode
                        + "\' and  is_pause = \'1\' and taskguid in (" + taskguids + ")";
            } else if (StringUtil.isNotBlank(projectType) && "DBJ".equals(projectType)) {
                sql = "select " + fieldstr + " from audit_project where  areacode=\'" + areaCode
                        + "\' and taskguid in (" + taskguids + ")" + " and status>=\'" + 35 + "\' and status <=\'" + 50
                        + "\'";
            } else if (StringUtil.isNotBlank(projectType) && "DBB".equals(projectType)) {
                sql = "select " + fieldstr + " from audit_project where  areacode=\'" + areaCode + "\' and status in ("
                        + 28 + "," + 37 + ") and taskguid in" + " (" + taskguids + ")";
            } else {
                sql = "select " + fieldstr + " from audit_project where  areacode=\'" + areaCode + "\' and status="
                        + banjianStatus + " and taskguid in (" + taskguids + ")";
            }

            if (StringUtil.isNotBlank(applyerName)) {
                sql = sql + "and applyerName like \'%" + applyerName + "%\'";
            }

            sql = sql + " order by OperateDate desc";
        } else {
            sql = "";
        }

        List list = this.commonDao.findList(sql, first, pageSize, AuditProject.class, new Object[0]);
        sqlCount = sql.replace("select " + fieldstr + " from", "select count(1) from");
        boolean count = false;
        int count1;
        if (!"".equals(taskguids) && taskguids != null) {
            count1 = this.commonDao.queryInt(sqlCount, new Object[0]).intValue();
        } else {
            count1 = 0;
        }

        PageData pageData = new PageData();
        pageData.setList(list);
        pageData.setRowCount(count1);
        return pageData;
    }

    public PageData<AuditProject> getBanJianListByPageAndTaskids(String fieldstr, String projectType, String taskids,
                                                                 int first, int pageSize, String areaCode, String applyerName, Date datestart, Date dateend,
                                                                 String windowguid) {
        byte banjianStatus = 0;
        if (StringUtil.isBlank(applyerName)) {
            applyerName = "";
        }

        if ("DJJ".equals(projectType)) {
            banjianStatus = 24;
        } else if ("DSL".equals(projectType)) {
            banjianStatus = 26;
        } else if ("DBB".equals(projectType)) {
            banjianStatus = 28;
        } else if ("DYS".equals(projectType)) {
            banjianStatus = 12;
        } else if ("YSTG".equals(projectType)) {
            banjianStatus = 16;
        }

        String sql = "";
        String sqlCount = "";
        String sqlExt = "";
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatDateStart = null;
        String formatDateEnd = null;
        if (this.commonDao.isMySql()) {
            if (datestart != null) {
                formatDateStart = "\'" + dFormat.format(datestart) + "\'";
            }

            if (dateend != null) {
                formatDateEnd = "\'" + dFormat.format(dateend) + "\'";
            }

            sqlExt = "and (windowguid is null or windowguid=\'\' or windowguid=\'" + windowguid + "\')";
        }

        if (this.commonDao.isOracle()) {
            if (datestart != null) {
                formatDateStart = "to_date(\'" + dFormat.format(datestart) + "\',\'yyyy-MM-dd hh24:mi:ss\')";
            }

            if (dateend != null) {
                formatDateEnd = "to_date(\'" + dFormat.format(dateend) + "\',\'yyyy-MM-dd hh24:mi:ss\')";
            }

            sqlExt = "and (windowguid is null or windowguid=\'" + windowguid + "\')";
        }

        if (!"".equals(taskids) && taskids != null) {
            if (StringUtil.isNotBlank(projectType) && "YZT".equals(projectType)) {
                sql = "select " + fieldstr + " from audit_project where areacode=\'" + areaCode
                        + "\' and  is_pause = \'1\' and task_id in (" + taskids + ")";
                if (datestart != null) {
                    sql = sql + "and acceptuserdate >" + formatDateStart;
                }

                if (dateend != null) {
                    sql = sql + "and acceptuserdate <" + formatDateEnd;
                }
            } else if (StringUtil.isNotBlank(projectType) && "DBJ".equals(projectType)) {
                sql = "select " + fieldstr + " from audit_project where  areacode=\'" + areaCode + "\' and task_id in ("
                        + taskids + ")" + " and status>=\'" + 35 + "\' and status <=\'" + 50 + "\'";
                if (datestart != null) {
                    sql = sql + "and promiseenddate >" + formatDateStart;
                }

                if (dateend != null) {
                    sql = sql + "and promiseenddate <" + formatDateEnd;
                }
            } else if (StringUtil.isNotBlank(projectType) && "DBB".equals(projectType)) {
                sql = "select " + fieldstr + " from audit_project where  areacode=\'" + areaCode + "\' and status in ("
                        + 28 + "," + 37 + ") and task_id in" + " (" + taskids + ")";
                if (datestart != null) {
                    sql = sql + "and bubandate >" + formatDateStart;
                }

                if (dateend != null) {
                    sql = sql + "and bubandate <" + formatDateEnd;
                }
            } else if (StringUtil.isNotBlank(projectType) && "DSL".equals(projectType)) {
                sql = "select " + fieldstr + " from audit_project where  areacode=\'" + areaCode + "\' and status="
                        + banjianStatus + " and task_id in (" + taskids + ")";
                if (datestart != null) {
                    sql = sql + "and RECEIVEDATE >" + formatDateStart;
                }

                if (dateend != null) {
                    sql = sql + "and RECEIVEDATE <" + formatDateEnd;
                }
            } else if (StringUtil.isNotBlank(projectType) && "DJJ".equals(projectType)) {
                sql = "select " + fieldstr + " from audit_project where  areacode=\'" + areaCode + "\' and status="
                        + banjianStatus + " and task_id in (" + taskids + ")";
                if (datestart != null) {
                    sql = sql + "and applydate >" + formatDateStart;
                }

                if (dateend != null) {
                    sql = sql + "and applydate <" + formatDateEnd;
                }
            } else if (StringUtil.isNotBlank(projectType) && "DYS".equals(projectType)) {
                sql = "select " + fieldstr + " from audit_project where  areacode=\'" + areaCode + "\' and status="
                        + banjianStatus + " and task_id in (" + taskids + ")";
                if (datestart != null) {
                    sql = sql + "and applydate >" + formatDateStart;
                }

                if (dateend != null) {
                    sql = sql + "and applydate <" + formatDateEnd;
                }
            } else if (StringUtil.isNotBlank(projectType) && "YSTG".equals(projectType)) {
                sql = "select " + fieldstr + " from audit_project where  areacode=\'" + areaCode + "\' and status="
                        + banjianStatus + " and task_id in (" + taskids + ")";
                if (datestart != null) {
                    sql = sql + "and yushendate >" + formatDateStart;
                }

                if (dateend != null) {
                    sql = sql + "and yushendate <" + formatDateEnd;
                }
            } else {
                sql = "select " + fieldstr + " from audit_project where  areacode=\'" + areaCode + "\' and status="
                        + banjianStatus + " and task_id in (" + taskids + ")";
            }

            if (StringUtil.isNotBlank(applyerName)) {
                sql = sql + "and applyerName like \'%" + applyerName + "%\'";
            }

            sql = sql + sqlExt + " order by OperateDate desc";
        } else {
            sql = "";
        }

        List list = this.commonDao.findList(sql, first, pageSize, AuditProject.class, new Object[0]);
        sqlCount = sql.replace("select " + fieldstr + " from", "select count(1) from");
        boolean count = false;
        int count1;
        if (!"".equals(taskids) && taskids != null) {
            count1 = this.commonDao.queryInt(sqlCount, new Object[0]).intValue();
        } else {
            count1 = 0;
        }

        PageData pageData = new PageData();
        pageData.setList(list);
        pageData.setRowCount(count1);
        return pageData;
    }

    public PageData<AuditProject> getBanJianListByPageAndTaskidsAndCenterGuid(String fieldstr, String projectType,
                                                                              String taskids, int first, int pageSize, String areaCode, String centerGuid, String applyerName,
                                                                              Date datestart, Date dateend, String windowguid) {
        byte banjianStatus = 0;
        if (StringUtil.isBlank(applyerName)) {
            applyerName = "";
        }

        if ("DJJ".equals(projectType)) {
            banjianStatus = 24;
        } else if ("DSL".equals(projectType)) {
            banjianStatus = 26;
        } else if ("DBB".equals(projectType)) {
            banjianStatus = 28;
        } else if ("DYS".equals(projectType)) {
            banjianStatus = 12;
        } else if ("YSTG".equals(projectType)) {
            banjianStatus = 16;
        }

        String sql = "";
        String sqlCount = "";
        String sqlExt = "";
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatDateStart = null;
        String formatDateEnd = null;
        if (this.commonDao.isMySql()) {
            if (datestart != null) {
                formatDateStart = "\'" + dFormat.format(datestart) + "\'";
            }

            if (dateend != null) {
                formatDateEnd = "\'" + dFormat.format(dateend) + "\'";
            }

            sqlExt = "and( (currentareacode =\'" + areaCode + "\' or currentareacode is null or  currentareacode =\'\')"
                    + " and( (centerGuid=\'" + centerGuid
                    + "\' and( acceptareacode is null or acceptareacode=\'\')) or acceptareacode is not null))";
        }

        if (this.commonDao.isOracle()) {
            if (datestart != null) {
                formatDateStart = "to_date(\'" + dFormat.format(datestart) + "\',\'yyyy-MM-dd hh24:mi:ss\')";
            }

            if (dateend != null) {
                formatDateEnd = "to_date(\'" + dFormat.format(dateend) + "\',\'yyyy-MM-dd hh24:mi:ss\')";
            }

            sqlExt = "and( (currentareacode =\'" + areaCode + "\' or currentareacode is null)" + " and( (centerGuid=\'"
                    + centerGuid + "\' and acceptareacode is null) or acceptareacode is not null))";
        }

        if (!"".equals(taskids) && taskids != null) {
            if (StringUtil.isNotBlank(projectType) && "YZT".equals(projectType)) {
                sql = "select " + fieldstr + " from audit_project where areaCode=\'" + areaCode
                        + "\' and  is_pause = \'1\' and task_id in (" + taskids + ")";
                if (datestart != null) {
                    sql = sql + "and acceptuserdate >" + formatDateStart;
                }

                if (dateend != null) {
                    sql = sql + "and acceptuserdate <" + formatDateEnd;
                }
            } else if (StringUtil.isNotBlank(projectType) && "DBJ".equals(projectType)) {
                sql = "select " + fieldstr + " from audit_project where  areaCode=\'" + areaCode + "\' and task_id in ("
                        + taskids + ")" + " and status>=\'" + 35 + "\' and status <=\'" + 50 + "\'";
                if (datestart != null) {
                    sql = sql + "and promiseenddate >" + formatDateStart;
                }

                if (dateend != null) {
                    sql = sql + "and promiseenddate <" + formatDateEnd;
                }
            } else if (StringUtil.isNotBlank(projectType) && "DBB".equals(projectType)) {
                sql = "select " + fieldstr + " from audit_project where  areaCode=\'" + areaCode + "\' and status in ("
                        + 28 + "," + 37 + ") and task_id in" + " (" + taskids + ")";
                if (datestart != null) {
                    sql = sql + "and bubandate >" + formatDateStart;
                }

                if (dateend != null) {
                    sql = sql + "and bubandate <" + formatDateEnd;
                }
            } else if (StringUtil.isNotBlank(projectType) && "DSL".equals(projectType)) {
                sql = "select " + fieldstr + " from audit_project where  areaCode=\'" + areaCode + "\' and status="
                        + banjianStatus + " and task_id in (" + taskids + ")";
                if (datestart != null) {
                    sql = sql + "and RECEIVEDATE >" + formatDateStart;
                }

                if (dateend != null) {
                    sql = sql + "and RECEIVEDATE <" + formatDateEnd;
                }
            } else if (StringUtil.isNotBlank(projectType) && "DJJ".equals(projectType)) {
                sql = "select " + fieldstr + " from audit_project where  areaCode=\'" + areaCode + "\' and status="
                        + banjianStatus + " and task_id in (" + taskids + ")";
                if (datestart != null) {
                    sql = sql + "and applydate >" + formatDateStart;
                }

                if (dateend != null) {
                    sql = sql + "and applydate <" + formatDateEnd;
                }
            } else if (StringUtil.isNotBlank(projectType) && "DYS".equals(projectType)) {
                sql = "select " + fieldstr + " from audit_project where  areaCode=\'" + areaCode + "\' and status="
                        + banjianStatus + " and task_id in (" + taskids + ")";
                if (datestart != null) {
                    sql = sql + "and applydate >" + formatDateStart;
                }

                if (dateend != null) {
                    sql = sql + "and applydate <" + formatDateEnd;
                }
            } else if (StringUtil.isNotBlank(projectType) && "YSTG".equals(projectType)) {
                sql = "select " + fieldstr + " from audit_project where  areaCode=\'" + areaCode + "\' and status="
                        + banjianStatus + " and task_id in (" + taskids + ")";
                if (datestart != null) {
                    sql = sql + "and yushendate >" + formatDateStart;
                }

                if (dateend != null) {
                    sql = sql + "and yushendate <" + formatDateEnd;
                }
            } else {
                sql = "select " + fieldstr + " from audit_project where  areaCode=\'" + areaCode + "\' and status="
                        + banjianStatus + " and task_id in (" + taskids + ")";
            }

            if (StringUtil.isNotBlank(applyerName)) {
                sql = sql + "and applyerName like \'%" + applyerName + "%\'";
            }

            sql = sql + sqlExt + " order by OperateDate desc";
        } else {
            sql = "";
        }

        List list = this.commonDao.findList(sql, first, pageSize, AuditProject.class, new Object[0]);
        sqlCount = sql.replace("select " + fieldstr + " from", "select count(1) from");
        boolean count = false;
        int count1;
        if (!"".equals(taskids) && taskids != null) {
            count1 = this.commonDao.queryInt(sqlCount, new Object[0]).intValue();
        } else {
            count1 = 0;
        }

        PageData pageData = new PageData();
        pageData.setList(list);
        pageData.setRowCount(count1);
        return pageData;
    }

    // 窗口首页内部列表数据显示
    // 2018年6月13日
    public PageData<AuditProject> getBanJianListByPageAndTaskidsAndCenterGuidAndAreacode(String fieldstr,
                                                                                         String projectType, String taskids, int first, int pageSize, String areaCode, String centerGuid,
                                                                                         String applyerName, Date datestart, Date dateend, String windowguid, String realAreacode) {
        byte banjianStatus = 0;
        if (StringUtil.isBlank(applyerName)) {
            applyerName = "";
        }

        if ("DJJ".equals(projectType)) {// 待接件
            banjianStatus = 24;
        } else if ("DSL".equals(projectType)) {// 待受理
            banjianStatus = 26;
        } else if ("DBB".equals(projectType)) {// 待补办
            banjianStatus = 28;
        } else if ("DYS".equals(projectType)) {
            banjianStatus = 12;
        } else if ("YSTG".equals(projectType)) {
            banjianStatus = 16;
        } else if ("DBJ".equals(projectType)) {// 待办结
            banjianStatus = 50;
        } else if ("QCWB".equals(projectType)) {// 外网申报之全程网办
            banjianStatus = 24;
        } else if ("WWBJ".equals(projectType)) {// 外网申报之外网办结
            banjianStatus = 90;
        }

        String sql = "";
        String sqlCount = "";
        String sqlExt = "";
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatDateStart = null;
        String formatDateEnd = null;
        if (this.commonDao.isMySql()) {
            if (datestart != null) {
                formatDateStart = "\'" + dFormat.format(datestart) + "\'";
            }

            if (dateend != null) {
                formatDateEnd = "\'" + dFormat.format(dateend) + "\'";
            }

            sqlExt = "and( (currentareacode =\'" + realAreacode
                    + "\' or currentareacode is null or  currentareacode =\'\')" + " and( (centerGuid=\'" + centerGuid
                    + "\' and( acceptareacode is null or acceptareacode=\'\')) or acceptareacode is not null))";
        }

        if (this.commonDao.isOracle()) {
            if (datestart != null) {
                formatDateStart = "to_date(\'" + dFormat.format(datestart) + "\',\'yyyy-MM-dd hh24:mi:ss\')";
            }

            if (dateend != null) {
                formatDateEnd = "to_date(\'" + dFormat.format(dateend) + "\',\'yyyy-MM-dd hh24:mi:ss\')";
            }

            sqlExt = "and( (currentareacode =\'" + realAreacode + "\' or currentareacode is null)"
                    + " and( (centerGuid=\'" + centerGuid
                    + "\' and acceptareacode is null) or acceptareacode is not null))";
        }

        if (!"".equals(taskids) && taskids != null) {
            if (StringUtil.isNotBlank(projectType) && "YZT".equals(projectType)) {
                // 已暂停
                sql = "select " + fieldstr + " from audit_project p where areaCode=\'" + areaCode
                        + "\' and  is_pause = \'1\' and task_id in (" + taskids + ")";
                if (datestart != null) {
                    sql = sql + "and acceptuserdate >" + formatDateStart;
                }

                if (dateend != null) {
                    sql = sql + "and acceptuserdate <" + formatDateEnd;
                }
                // 窗口条件
                if (StringUtil.isNotBlank(windowguid)) {
                    sql = sql + "and windowguid = '" + windowguid + "' ";
                }

            } else if (StringUtil.isNotBlank(projectType) && "DBJ".equals(projectType)) {
                // 待办结
                sql = "select " + fieldstr + " from audit_project p where  areaCode=\'" + areaCode + "\'"
                        + " and APPLYWAY = 20 " + " and task_id in (" + taskids + ")" + " and status>=\'" + 35
                        + "\' and status <=\'" + 50 + "\'";
                if (datestart != null) {
                    sql = sql + "and promiseenddate >" + formatDateStart;
                }

                if (dateend != null) {
                    sql = sql + "and promiseenddate <" + formatDateEnd;
                }
                // 窗口条件
                if (StringUtil.isNotBlank(windowguid)) {
                    sql = sql + "and windowguid = '" + windowguid + "' ";
                }
            } else if (StringUtil.isNotBlank(projectType) && "DBB".equals(projectType)) {
                // 待补办
                sql = "select " + fieldstr + " from audit_project p where  areaCode=\'" + areaCode
                        + "\' and status in (" + 28 + "," + 37 + ") and task_id in" + " (" + taskids + ")";
                if (datestart != null) {
                    sql = sql + "and bubandate >" + formatDateStart;
                }

                if (dateend != null) {
                    sql = sql + "and bubandate <" + formatDateEnd;
                }
                // 窗口条件
                if (StringUtil.isNotBlank(windowguid)) {
                    sql = sql + "and windowguid = '" + windowguid + "' ";
                }
            } else if (StringUtil.isNotBlank(projectType) && "DSL".equals(projectType)) {
                // 待受理
                sql = "select " + fieldstr + " from audit_project p where  areaCode=\'" + areaCode + "\' and status="
                        + banjianStatus + " and APPLYWAY = 20 " + " and task_id in (" + taskids + ")";
                if (datestart != null) {
                    sql = sql + "and RECEIVEDATE >" + formatDateStart;
                }

                if (dateend != null) {
                    sql = sql + "and RECEIVEDATE <" + formatDateEnd;
                }
                //限制，不显示一窗受理的事项
                sql += " and isycsl is not true ";
                // 窗口条件
                // if (StringUtil.isNotBlank(windowguid)) {
                // sql = sql + "and windowguid = '" + windowguid + "' ";
                // }
            } else if (StringUtil.isNotBlank(projectType) && "DJJ".equals(projectType)) {
                // 待接件
                sql = "select " + fieldstr + " from audit_project p where  areaCode=\'" + areaCode + "\' and status="
                        + banjianStatus + " and APPLYWAY = 20 " + " and task_id in (" + taskids + ")";
                if (datestart != null) {
                    sql = sql + "and applydate >" + formatDateStart;
                }

                if (dateend != null) {
                    sql = sql + "and applydate <" + formatDateEnd;
                }
                // 窗口条件
                if (StringUtil.isNotBlank(windowguid)) {
                    sql = sql + "and windowguid = '" + windowguid + "' ";
                }

            } else if (StringUtil.isNotBlank(projectType) && "SPZ".equals(projectType)) {
                // 审批中
                sql = "select " + fieldstr + " from audit_project p where  areaCode=\'" + areaCode
                        + "\' and status >= 30 and status < 40" + " and APPLYWAY = 20 " + " and task_id in (" + taskids
                        + ")";
                if (datestart != null) {
                    sql = sql + "and applydate >" + formatDateStart;
                }

                if (dateend != null) {
                    sql = sql + "and applydate <" + formatDateEnd;
                }
                // 窗口条件
                if (StringUtil.isNotBlank(windowguid)) {
                    sql = sql + "and windowguid = '" + windowguid + "' ";
                }

            } else if (StringUtil.isNotBlank(projectType) && "DYS".equals(projectType)) {
                // 待预审
                sql = "select " + fieldstr + " from audit_project p where  areaCode=\'" + areaCode + "\' and status="
                        + banjianStatus + " and task_id in (" + taskids + ")";
                if (datestart != null) {
                    sql = sql + "and applydate >" + formatDateStart;
                }

                if (dateend != null) {
                    sql = sql + "and applydate <" + formatDateEnd;
                }
            } else if (StringUtil.isNotBlank(projectType) && "YSTG".equals(projectType)) {
                // 预审通过
                sql = "select " + fieldstr + " from audit_project p where  areaCode=\'" + areaCode + "\' and status="
                        + banjianStatus + " and task_id in (" + taskids + ")";
                if (datestart != null) {
                    sql = sql + "and yushendate >" + formatDateStart;
                }

                if (dateend != null) {
                    sql = sql + "and yushendate <" + formatDateEnd;
                }
            } else if (StringUtil.isNotBlank(projectType) && "QCWB".equals(projectType)) {
                // 外网申报之全程网办
                sql = "select " + fieldstr
                        + " from audit_project p INNER JOIN audit_task_extension t ON p.TASKGUID = t.TASKGUID  where  p.areaCode=\'"
                        + areaCode + "\' and status= '24' and (APPLYWAY = 11 or APPLYWAY = 10) and WEBAPPLYTYPE = '2' "
                        + " and p.task_id in (" + taskids + ")";
                if (datestart != null) {
                    sql = sql + "and applydate >" + formatDateStart;
                }

                if (dateend != null) {
                    sql = sql + "and applydate <" + formatDateEnd;
                }
            } else if (StringUtil.isNotBlank(projectType) && "ZXSB".equals(projectType)) {
                // 外网申报之在线申办
                sql = "select " + fieldstr
                        + " from audit_project p INNER JOIN audit_task_extension t ON p.TASKGUID = t.TASKGUID  where  p.areaCode=\'"
                        + areaCode + "\' and status='24' and (APPLYWAY = 11 or APPLYWAY = 10) and WEBAPPLYTYPE = '4' "
                        + " and p.task_id in (" + taskids + ")";
                if (datestart != null) {
                    sql = sql + "and applydate >" + formatDateStart;
                }

                if (dateend != null) {
                    sql = sql + "and applydate <" + formatDateEnd;
                }
            } else if (StringUtil.isNotBlank(projectType) && "WWSB".equals(projectType)) {
                // 外网申报之外网申报
                sql = "select " + fieldstr
                        + " from audit_project p INNER JOIN audit_task_extension t ON p.TASKGUID = t.TASKGUID  where  p.areaCode=\'"
                        + areaCode
                        + "\' and (status ='24' or status = '12') and (APPLYWAY = 11 or APPLYWAY = 10) and WEBAPPLYTYPE in ('1','2','4') and p.task_id in ("
                        + taskids + ")";
                if (datestart != null) {
                    sql = sql + "and applydate >" + formatDateStart;
                }

                if (dateend != null) {
                    sql = sql + "and applydate <" + formatDateEnd;
                }
            } else if (StringUtil.isNotBlank(projectType) && "WWBL".equals(projectType)) {
                // 外网申报之外网办理
                sql = "select " + fieldstr + " from audit_project p where  areaCode=\'" + areaCode
                        + "\' and status > 24 and status < 90" + " and (APPLYWAY = 11 or APPLYWAY = 10)"
                        + " and task_id in (" + taskids + ")";
                if (datestart != null) {
                    sql = sql + "and applydate >" + formatDateStart;
                }

                if (dateend != null) {
                    sql = sql + "and applydate <" + formatDateEnd;
                }
                // 窗口条件
                if (StringUtil.isNotBlank(windowguid)) {
                    sql = sql + "and windowguid = '" + windowguid + "' ";
                }
            } else if (StringUtil.isNotBlank(projectType) && "WWBJ".equals(projectType)) {
                // 外网申报之外网办结
                sql = "select " + fieldstr + " from audit_project p where  areaCode=\'" + areaCode + "\' and status >= "
                        + banjianStatus + " and (APPLYWAY = 11 or APPLYWAY = 10)" + " and task_id in (" + taskids + ")";
                if (datestart != null) {
                    sql = sql + "and banjiedate >" + formatDateStart;
                }

                if (dateend != null) {
                    sql = sql + "and banjiedate <" + formatDateEnd;
                }
                // 窗口条件
                if (StringUtil.isNotBlank(windowguid)) {
                    sql = sql + "and windowguid = '" + windowguid + "' ";
                }
            } else {
                sql = "select " + fieldstr + " from audit_project p where  areaCode=\'" + areaCode + "\' and status="
                        + banjianStatus + " and task_id in (" + taskids + ")";
            }

            if (!"DJJ".equals(projectType) && !"DSL".equals(projectType) && !"DBB".equals(projectType)
                    && !"DBJ".equals(projectType)) {

                // 增加外网条件 外网申报展示窗口配置的事项
                if (!"DYS".equals(projectType) && !"YSTG".equals(projectType) && !"QCWB".equals(projectType)
                        && !"WWBL".equals(projectType) && !"WWBJ".equals(projectType) && !"WWSB".equals(projectType)
                        && !"ZXSB".equals(projectType)) {

                    // 首页大厅办理显示本窗口的事项 增加窗口的条件
                    if (StringUtil.isBlank(windowguid)) {

                    } else {
                        sql = sql + " and windowguid = '" + windowguid + "' ";
                    }
                }

            }

            if (StringUtil.isNotBlank(applyerName)) {
                sql = sql + "and applyerName like \'%" + applyerName + "%\'";
            }

            if (StringUtil.isNotBlank(projectType) && "DSL".equals(projectType)) {

                sql = sql + " order by applydate desc";
            } else if (StringUtil.isNotBlank(projectType) && "WWBJ".equals(projectType)) {
                sql = sql + " order by banjiedate desc";
            } else {

                sql = sql + sqlExt + " order by applydate desc";
            }
        } else {
            sql = "";
        }

        List list = this.commonDao.findList(sql, first, pageSize, AuditProject.class, new Object[0]);
        sqlCount = sql.replace("select " + fieldstr + " from", "select count(1) from");
        boolean count = false;
        int count1;
        if (!"".equals(taskids) && taskids != null) {
            count1 = this.commonDao.queryInt(sqlCount, new Object[0]).intValue();
        } else {
            count1 = 0;
        }

        PageData pageData = new PageData();
        pageData.setList(list);
        pageData.setRowCount(count1);
        return pageData;
    }

    public List<AuditProject> getBanJianList(String fieldstr, List<String> taskGuidList, String projectType,
                                             String windowguid) {
        String taskGuids = "";
        if (taskGuidList != null) {
            String list;
            for (Iterator sql = taskGuidList.iterator(); sql.hasNext(); taskGuids = taskGuids + "\'" + list + "\',") {
                list = (String) sql.next();
            }

            if (!"".equals(taskGuids)) {
                taskGuids = taskGuids.substring(0, taskGuids.length() - 1);
            }
        }

        String sql1 = "";
        if (StringUtil.isNotBlank(projectType) && "YZT".equals(projectType)) {
            sql1 = "select " + fieldstr + " from audit_project where   is_pause = \'1\' and taskguid in (" + taskGuids
                    + ")";
        } else if (StringUtil.isNotBlank(projectType) && "DBJ".equals(projectType)) {
            sql1 = "select " + fieldstr + " from audit_project where taskguid in (" + taskGuids + ")"
                    + " and ((TASKTYPE=\'" + "1" + "\' and status=\'" + 30 + "\') or (TASKTYPE!=\'" + "1"
                    + "\' and (status=\'" + 40 + "\' or status=\'" + 50 + "\')))";
        } else if (StringUtil.isNotBlank(projectType) && "DBB".equals(projectType)) {
            sql1 = "select " + fieldstr + " from audit_project where status in (" + 28 + "," + 37
                    + ") and taskguid in (" + taskGuids + ")";
        }

        List list1 = this.commonDao.findList(sql1, AuditProject.class, new Object[0]);
        return list1;
    }

    public PageData<AuditProject> getJZListByPage(String fieldstr, String ouGuids, int first, int pageSize,
                                                  String areaCode, String applyerName) {
        if (StringUtil.isBlank(applyerName)) {
            applyerName = "";
        }

        String sql = "";
        String sqlCount = "";
        if (!"".equals(ouGuids) && ouGuids != null) {
            sql = "select " + fieldstr + " from audit_project where IF_JZ_HALL=1 and areacode=\'" + areaCode
                    + "\' and OUGuid in (" + ouGuids + ")  and applyerName like \'%" + applyerName + "%\'"
                    + " and ((status>=\'" + 24 + "\' and status <=\'" + 50 + "\') or status=\'" + 12 + "\' or status=\'"
                    + 16 + "\')";
        } else {
            sql = "select " + fieldstr + " from audit_project where IF_JZ_HALL=1 ";
        }

        sql = sql + " order by status asc";
        List list = this.commonDao.findList(sql, first, pageSize, AuditProject.class, new Object[0]);
        sqlCount = sql.replace("select " + fieldstr + " from", "select count(1) from");
        int count = this.commonDao.queryInt(sqlCount, new Object[0]).intValue();
        PageData pageData = new PageData();
        pageData.setList(list);
        pageData.setRowCount(count);
        return pageData;
    }

    public List<AuditProject> getJZList(String fieldstr, String ouGuids, String areaCode) {
        String sql = "";
        if (!"".equals(ouGuids) && ouGuids != null) {
            sql = "select " + fieldstr + " from audit_project where IF_JZ_HALL=1 and areacode=\'" + areaCode
                    + "\' and OUGuid in (" + ouGuids + ") and ((status>=\'" + 24 + "\' and status <=\'" + 50
                    + "\') or status=\'" + 12 + "\' or status=\'" + 16 + "\')";
        } else {
            sql = "select " + fieldstr + " from audit_project where IF_JZ_HALL=1 ";
        }

        List list = this.commonDao.findList(sql, AuditProject.class, new Object[0]);
        return list;
    }

    public Map<String, Integer> getCountStatusByWindowguid(List<String> taskidList, String windowguid,
                                                           String areaCode) {
        String taskids = "";
        if (taskidList != null) {
            String daijiejiancount;
            for (Iterator map = taskidList.iterator(); map
                    .hasNext(); taskids = taskids + "\'" + daijiejiancount + "\',") {
                daijiejiancount = (String) map.next();
            }

            if (!"".equals(taskids)) {
                taskids = taskids.substring(0, taskids.length() - 1);
            }
        }

        HashMap arg20 = new HashMap(16);
        int arg21 = 0;
        int daishoulicount = 0;
        int daibubancount = 0;
        int daibanjiecount = 0;
        int daiyushencount = 0;
        int yushentonguocount = 0;
        int yizantingcount = 0;
        int shenpicount = 0;
        if (StringUtil.isNotBlank(taskids)) {
            List tempList = null;
            AuditProject arrobj = null;
            String fieldstr = "status, count(1) count";
            String groupstr = "  group by STATUS";
            String sqlExt = "";
            if (this.commonDao.isMySql()) {
                sqlExt = "and (currentareacode =\'" + areaCode
                        + "\' or currentareacode is null or  currentareacode =\'\')";
            }

            if (this.commonDao.isOracle()) {
                sqlExt = "and (currentareacode =\'" + areaCode + "\' or currentareacode is null)";
            }

            String wherestr = " task_id in (" + taskids + ") and areacode=\'" + areaCode + "\' ";
            wherestr = wherestr + sqlExt;
            tempList = this.getAuditProjectFieldList(fieldstr, groupstr, wherestr);

            for (int i = 0; i < tempList.size(); ++i) {
                arrobj = (AuditProject) tempList.get(i);
                if (24 == Integer.parseInt(arrobj.getStr("status"))) {
                    arg21 = arrobj.getStr("count") == null ? 0 : Integer.parseInt(arrobj.getStr("count"));
                } else if (26 == Integer.parseInt(arrobj.getStr("status"))) {
                    daishoulicount = arrobj.getStr("count") == null ? 0 : Integer.parseInt(arrobj.getStr("count"));
                } else if (28 == Integer.parseInt(arrobj.getStr("status"))) {
                    daibubancount = arrobj.getStr("count") == null ? 0 : Integer.parseInt(arrobj.getStr("count"));
                } else if (37 == Integer.parseInt(arrobj.getStr("status"))) {
                    daibubancount += arrobj.getStr("count") == null ? 0 : Integer.parseInt(arrobj.getStr("count"));
                }
            }

            fieldstr = "count(*) count";
            groupstr = "";
            wherestr = "  task_id in (" + taskids + ") AND is_pause = \'1\' and areacode=\'" + areaCode + "\' "
                    + sqlExt;
            arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
            yizantingcount = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("count"));
            fieldstr = "count(*) count";
            groupstr = "";
            wherestr = "  task_id in (" + taskids + ") and status>=\'" + 35 + "\' and status<=\'" + 50
                    + "\' and areacode=\'" + areaCode + "\' " + sqlExt;
            arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
            daibanjiecount = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("count"));
            fieldstr = "count(*) count";
            groupstr = "";
            wherestr = "  task_id in (" + taskids + ") and status>=\'" + 30 + "\' and status<\'" + 40
                    + "\' and areacode=\'" + areaCode + "\' " + sqlExt;
            arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
            shenpicount = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("count"));
            fieldstr = "count(*) count";
            groupstr = "";
            wherestr = " status =\'12\' and task_id in (" + taskids + ") and areacode=\'" + areaCode + "\' " + sqlExt;
            arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
            daiyushencount = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("count"));
            fieldstr = "count(*) count";
            groupstr = "";
            wherestr = "  task_id in (" + taskids + ") and status =\'" + 16 + "\' and areacode=\'" + areaCode + "\' "
                    + sqlExt;
            arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
            yushentonguocount = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("count"));
        }

        arg20.put("DJJ", Integer.valueOf(arg21));
        arg20.put("DSL", Integer.valueOf(daishoulicount));
        arg20.put("YZT", Integer.valueOf(yizantingcount));
        arg20.put("DBJ", Integer.valueOf(daibanjiecount));
        arg20.put("DBB", Integer.valueOf(daibubancount));
        arg20.put("DYS", Integer.valueOf(daiyushencount));
        arg20.put("YSTG", Integer.valueOf(yushentonguocount));
        arg20.put("SPZ", Integer.valueOf(shenpicount));
        return arg20;
    }

    public Map<String, Integer> getCountStatusByCenterguid(List<String> taskidList, String windowguid,
                                                           String centerGuid, String areaCode) {
        String taskids = "";
        if (taskidList != null) {
            String daijiejiancount;
            for (Iterator map = taskidList.iterator(); map
                    .hasNext(); taskids = taskids + "\'" + daijiejiancount + "\',") {
                daijiejiancount = (String) map.next();
            }

            if (!"".equals(taskids)) {
                taskids = taskids.substring(0, taskids.length() - 1);
            }
        }

        HashMap arg20 = new HashMap();
        int arg21 = 0;
        int daishoulicount = 0;
        int daibubancount = 0;
        int daibanjiecount = 0;
        int daiyushencount = 0;
        int yushentonguocount = 0;
        int yizantingcount = 0;
        if (StringUtil.isNotBlank(taskids)) {
            List tempList = null;
            AuditProject arrobj = null;
            String fieldstr = "status, count(1) count";
            String groupstr = "  group by STATUS";
            String sqlExt = "";
            if (this.commonDao.isMySql()) {
                sqlExt = "and (windowguid is null or windowguid=\'\' or windowguid=\'" + windowguid + "\')";
            }

            if (this.commonDao.isOracle()) {
                sqlExt = "and (windowguid is null or windowguid=\'" + windowguid + "\')";
            }

            String wherestr = " task_id in (" + taskids + ") and centerGuid=\'" + centerGuid + "\' and areacode=\'"
                    + areaCode + "\' ";
            wherestr = wherestr + sqlExt;
            tempList = this.getAuditProjectFieldList(fieldstr, groupstr, wherestr);

            for (int i = 0; i < tempList.size(); ++i) {
                arrobj = (AuditProject) tempList.get(i);
                if (24 == Integer.parseInt(arrobj.getStr("status"))) {
                    arg21 = arrobj.getStr("count") == null ? 0 : Integer.parseInt(arrobj.getStr("count"));
                } else if (26 == Integer.parseInt(arrobj.getStr("status"))) {
                    daishoulicount = arrobj.getStr("count") == null ? 0 : Integer.parseInt(arrobj.getStr("count"));
                } else if (28 == Integer.parseInt(arrobj.getStr("status"))) {
                    daibubancount = arrobj.getStr("count") == null ? 0 : Integer.parseInt(arrobj.getStr("count"));
                } else if (37 == Integer.parseInt(arrobj.getStr("status"))) {
                    daibubancount += arrobj.getStr("count") == null ? 0 : Integer.parseInt(arrobj.getStr("count"));
                }
            }

            fieldstr = "count(*) count";
            groupstr = "";
            wherestr = "  task_id in (" + taskids + ") AND is_pause = \'1\' and centerGuid=\'" + centerGuid
                    + "\' and areacode=\'" + areaCode + "\' " + sqlExt;
            arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
            yizantingcount = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("count"));
            fieldstr = "count(*) count";
            groupstr = "";
            wherestr = "  task_id in (" + taskids + ") and status>=\'" + 35 + "\' and status<=\'" + 50
                    + "\' and centerGuid=\'" + centerGuid + "\' and areacode=\'" + areaCode + "\' " + sqlExt;
            arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
            daibanjiecount = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("count"));
            fieldstr = "count(*) count";
            groupstr = "";
            wherestr = " status =\'12\' and task_id in (" + taskids + ") and centerGuid=\'" + centerGuid
                    + "\' and areacode=\'" + areaCode + "\' " + sqlExt;
            arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
            daiyushencount = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("count"));
            fieldstr = "count(*) count";
            groupstr = "";
            wherestr = "  task_id in (" + taskids + ") and status =\'" + 16 + "\' and centerGuid=\'" + centerGuid
                    + "\' and areacode=\'" + areaCode + "\' " + sqlExt;
            arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
            yushentonguocount = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("count"));
        }

        arg20.put("DJJ", Integer.valueOf(arg21));
        arg20.put("DSL", Integer.valueOf(daishoulicount));
        arg20.put("YZT", Integer.valueOf(yizantingcount));
        arg20.put("DBJ", Integer.valueOf(daibanjiecount));
        arg20.put("DBB", Integer.valueOf(daibubancount));
        arg20.put("DYS", Integer.valueOf(daiyushencount));
        arg20.put("YSTG", Integer.valueOf(yushentonguocount));
        return arg20;
    }

    public Map<String, Integer> getCountStatusByWindowguidAndCenterguid(List<String> taskidList, String windowguid,
                                                                        String areaCode, String centerGuid) {
        String taskids = "";
        if (taskidList != null) {
            String daijiejiancount;
            for (Iterator map = taskidList.iterator(); map
                    .hasNext(); taskids = taskids + "\'" + daijiejiancount + "\',") {
                daijiejiancount = (String) map.next();
            }

            if (!"".equals(taskids)) {
                taskids = taskids.substring(0, taskids.length() - 1);
            }
        }

        HashMap arg21 = new HashMap(16);
        int arg22 = 0;
        int daishoulicount = 0;
        int daibubancount = 0;
        int daibanjiecount = 0;
        int daiyushencount = 0;
        int yushentonguocount = 0;
        int yizantingcount = 0;
        int shenpicount = 0;
        if (StringUtil.isNotBlank(taskids)) {
            List tempList = null;
            AuditProject arrobj = null;
            String fieldstr = "status, count(1) count";
            String groupstr = "  group by STATUS";
            String sqlExt = "";
            if (this.commonDao.isMySql()) {
                sqlExt = "and (currentareacode =\'" + areaCode
                        + "\' or currentareacode is null or  currentareacode =\'\')" + " and( (centerGuid=\'"
                        + centerGuid
                        + "\' and( acceptareacode is null or acceptareacode=\'\')) or acceptareacode is not null))";
            }

            if (this.commonDao.isOracle()) {
                sqlExt = "and (currentareacode =\'" + areaCode + "\' or currentareacode is null)"
                        + " and( (centerGuid=\'" + centerGuid
                        + "\' and acceptareacode is null) or acceptareacode is not null))";
            }

            String wherestr = " task_id in (" + taskids + ") and areacode=\'" + areaCode + "\' ";
            wherestr = wherestr + sqlExt;
            tempList = this.getAuditProjectFieldList(fieldstr, groupstr, wherestr);

            for (int i = 0; i < tempList.size(); ++i) {
                arrobj = (AuditProject) tempList.get(i);
                if (24 == Integer.parseInt(arrobj.getStr("status"))) {
                    arg22 = arrobj.getStr("count") == null ? 0 : Integer.parseInt(arrobj.getStr("count"));
                } else if (26 == Integer.parseInt(arrobj.getStr("status"))) {
                    daishoulicount = arrobj.getStr("count") == null ? 0 : Integer.parseInt(arrobj.getStr("count"));
                } else if (28 == Integer.parseInt(arrobj.getStr("status"))) {
                    daibubancount = arrobj.getStr("count") == null ? 0 : Integer.parseInt(arrobj.getStr("count"));
                } else if (37 == Integer.parseInt(arrobj.getStr("status"))) {
                    daibubancount += arrobj.getStr("count") == null ? 0 : Integer.parseInt(arrobj.getStr("count"));
                }
            }

            fieldstr = "count(*) count";
            groupstr = "";
            wherestr = "  task_id in (" + taskids + ") AND is_pause = \'1\' and areacode=\'" + areaCode + "\' "
                    + sqlExt;
            arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
            yizantingcount = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("count"));
            fieldstr = "count(*) count";
            groupstr = "";
            wherestr = "  task_id in (" + taskids + ") and status>=\'" + 35 + "\' and status<=\'" + 50
                    + "\' and areacode=\'" + areaCode + "\' " + sqlExt;
            arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
            daibanjiecount = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("count"));
            fieldstr = "count(*) count";
            groupstr = "";
            wherestr = "  task_id in (" + taskids + ") and status>=\'" + 30 + "\' and status<\'" + 40
                    + "\' and areacode=\'" + areaCode + "\' " + sqlExt;
            arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
            shenpicount = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("count"));
            fieldstr = "count(*) count";
            groupstr = "";
            wherestr = " status =\'12\' and task_id in (" + taskids + ") and areacode=\'" + areaCode + "\' " + sqlExt;
            arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
            daiyushencount = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("count"));
            fieldstr = "count(*) count";
            groupstr = "";
            wherestr = "  task_id in (" + taskids + ") and status =\'" + 16 + "\' and areacode=\'" + areaCode + "\' "
                    + sqlExt;
            arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
            yushentonguocount = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("count"));
        }

        arg21.put("DJJ", Integer.valueOf(arg22));
        arg21.put("DSL", Integer.valueOf(daishoulicount));
        arg21.put("YZT", Integer.valueOf(yizantingcount));
        arg21.put("DBJ", Integer.valueOf(daibanjiecount));
        arg21.put("DBB", Integer.valueOf(daibubancount));
        arg21.put("DYS", Integer.valueOf(daiyushencount));
        arg21.put("YSTG", Integer.valueOf(yushentonguocount));
        arg21.put("SPZ", Integer.valueOf(shenpicount));
        return arg21;
    }

    // 窗口人员 首页 &&大厅办理 办件数量
    public Map<String, Integer> getCountStatusByWindowguidAndCenterguidAndAreacode(List<String> taskidList,
                                                                                   String windowguid, String areaCode, String centerGuid, String baseareacode) {
        String taskids = "";
        if (taskidList != null) {
            String daijiejiancount;
            for (Iterator map = taskidList.iterator(); map
                    .hasNext(); taskids = taskids + "\'" + daijiejiancount + "\',") {
                daijiejiancount = (String) map.next();
            }

            if (!"".equals(taskids)) {
                taskids = taskids.substring(0, taskids.length() - 1);
            }
        }

        HashMap arg22 = new HashMap(16);
        int arg23 = 0;
        int daishoulicount = 0;
        int daibubancount = 0;
        int daibanjiecount = 0;
        int daiyushencount = 0;
        int yushentonguocount = 0;
        int yizantingcount = 0;
        int shenpicount = 0;
        int qcwb = 0;
        int wwbl = 0;
        int wwbj = 0;
        if (StringUtil.isNotBlank(taskids)) {
            List tempList = null;
            List tempDSLList = null;
            AuditProject arrobj = null;
            String fieldstr = "status, count(1) count";
            String groupstr = "  group by STATUS";
            String sqlExt = "";
            if (this.commonDao.isMySql()) {
                sqlExt = "and (currentareacode =\'" + areaCode
                        + "\' or currentareacode is null or  currentareacode =\'\')" + " and( (centerGuid=\'"
                        + centerGuid
                        + "\' and( acceptareacode is null or acceptareacode=\'\')) or acceptareacode is not null)";
            }

            if (this.commonDao.isOracle()) {
                sqlExt = "and (currentareacode =\'" + areaCode + "\' or currentareacode is null)"
                        + " and( (centerGuid=\'" + centerGuid
                        + "\' and acceptareacode is null) or acceptareacode is not null)";
            }

            String wherestr = " task_id in (" + taskids + ") and areacode=\'" + baseareacode + "\' and APPLYWAY = 20 ";
            //一窗受理事项不统计
            String wherestr2 = wherestr + " and status='26' and isycsl is not true";
            tempDSLList = this.getAuditProjectFieldList(fieldstr, groupstr, wherestr2);
            if (null != tempDSLList) {
                for (int i = 0; i < tempDSLList.size(); ++i) {
                    arrobj = (AuditProject) tempDSLList.get(i);
                    if (26 == Integer.parseInt(arrobj.getStr("status"))) {
                        daishoulicount = arrobj.getStr("count") == null ? 0 : Integer.parseInt(arrobj.getStr("count"));
                    }
                }
            }

            // 增加窗口条件
            if (StringUtil.isBlank(windowguid)) {

            } else {
                sqlExt = sqlExt + " and windowguid = '" + windowguid + "' ";
            }
            wherestr = wherestr + sqlExt;
            tempList = this.getAuditProjectFieldList(fieldstr, groupstr, wherestr);

            for (int i = 0; i < tempList.size(); ++i) {
                arrobj = (AuditProject) tempList.get(i);
                if (24 == Integer.parseInt(arrobj.getStr("status"))) {
                    arg23 = arrobj.getStr("count") == null ? 0 : Integer.parseInt(arrobj.getStr("count"));
                    // } else if (26 ==
                    // Integer.parseInt(arrobj.getStr("status"))) {
                    // daishoulicount = arrobj.getStr("count") == null ? 0 :
                    // Integer.parseInt(arrobj.getStr("count"));
                } else if (28 == Integer.parseInt(arrobj.getStr("status"))) {
                    daibubancount = arrobj.getStr("count") == null ? 0 : Integer.parseInt(arrobj.getStr("count"));
                } else if (37 == Integer.parseInt(arrobj.getStr("status"))) {
                    daibubancount += arrobj.getStr("count") == null ? 0 : Integer.parseInt(arrobj.getStr("count"));
                }
            }

            fieldstr = "count(*) count";
            groupstr = "";
            wherestr = "  task_id in (" + taskids + ") AND is_pause = \'1\' and areacode=\'" + baseareacode + "\' "
                    + sqlExt;
            arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
            yizantingcount = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("count"));

            // 待办结
            fieldstr = "count(*) count";
            groupstr = "";
            wherestr = "  task_id in (" + taskids + ") and status>=\'" + 35 + "\' and status<=\'" + 50
                    + "\' and areacode=\'" + baseareacode + "\' and APPLYWAY = 20 " + sqlExt;
            arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
            daibanjiecount = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("count"));

            // 审批中
            fieldstr = "count(*) count";
            groupstr = "";
            wherestr = "  task_id in (" + taskids + ") and status>=\'" + 30 + "\' and status<\'" + 40
                    + "\' and areacode=\'" + baseareacode + "\' and APPLYWAY = 20 " + sqlExt;
            arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
            shenpicount = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("count"));

            // 待预审
            fieldstr = "count(*) count";
            groupstr = "";
            wherestr = " status =\'12\' and task_id in (" + taskids + ") and areacode=\'" + baseareacode + "\' "
                    + sqlExt;
            arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
            daiyushencount = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("count"));

            // 预审通过
            fieldstr = "count(*) count";
            groupstr = "";
            wherestr = "  task_id in (" + taskids + ") and status =\'" + 16 + "\' and areacode=\'" + baseareacode
                    + "\' " + sqlExt;
            arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
            yushentonguocount = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("count"));

            // 全程网办
            fieldstr = "count(*) count";
            groupstr = "";
            wherestr = "  task_id in (" + taskids + ") and status=\'" + 24 + "\' and areacode=\'" + baseareacode
                    + "\' and APPLYWAY = 11 " + sqlExt;
            arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
            qcwb = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("count"));

            // 外网办理
            fieldstr = "count(*) count";
            groupstr = "";
            wherestr = "  task_id in (" + taskids + ") and status>=\'" + 24 + "\' and status<\'" + 90
                    + "\' and areacode=\'" + baseareacode + "\' and (APPLYWAY = 11 or APPLYWAY = 10)" + sqlExt;
            arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
            wwbl = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("count"));

            // 外网办结
            fieldstr = "count(*) count";
            groupstr = "";
            wherestr = "  task_id in (" + taskids + ") and status>=\'" + 90 + "\' and areacode=\'" + baseareacode
                    + "\' and (APPLYWAY = 11 or APPLYWAY = 10)" + sqlExt;
            arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
            wwbj = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("count"));
        }

        arg22.put("DJJ", Integer.valueOf(arg23));
        arg22.put("DSL", Integer.valueOf(daishoulicount));
        arg22.put("YZT", Integer.valueOf(yizantingcount));
        arg22.put("DBJ", Integer.valueOf(daibanjiecount));
        arg22.put("DBB", Integer.valueOf(daibubancount));
        arg22.put("DYS", Integer.valueOf(daiyushencount));
        arg22.put("YSTG", Integer.valueOf(yushentonguocount));
        arg22.put("SPZ", Integer.valueOf(shenpicount));
        arg22.put("QCWB", Integer.valueOf(qcwb));
        arg22.put("WWBL", Integer.valueOf(wwbl));
        arg22.put("WWBJ", Integer.valueOf(wwbj));
        return arg22;
    }

    // 窗口人员首页 外网&&外网申报 办件数量
    public Map<String, Integer> getCountStatusByWindowguidAndCenterguidAndAreacodeByWwsb(List<String> taskidList,
                                                                                         String windowguid, String areaCode, String centerGuid, String baseareacode) {
        String taskids = "";
        if (taskidList != null) {
            String daijiejiancount;
            for (Iterator map = taskidList.iterator(); map
                    .hasNext(); taskids = taskids + "\'" + daijiejiancount + "\',") {
                daijiejiancount = (String) map.next();
            }

            if (!"".equals(taskids)) {
                taskids = taskids.substring(0, taskids.length() - 1);
            }
        }

        HashMap arg22 = new HashMap(16);
        int arg23 = 0;
        int daishoulicount = 0;
        int daibubancount = 0;
        int daibanjiecount = 0;
        int daiyushencount = 0;
        int yushentonguocount = 0;
        int yizantingcount = 0;
        int shenpicount = 0;
        int qcwb = 0; //全程网办
        int wwbl = 0; //外网办理
        int wwbj = 0; //外网办结
        int wwsb = 0; //外网申报
        int zxsb = 0; //在线申办
        if (StringUtil.isNotBlank(taskids)) {
            List tempList = null;
            AuditProject arrobj = null;
            String fieldstr = "status, count(1) count";
            String groupstr = "  group by STATUS";
            String sqlExt = "";

            if (this.commonDao.isMySql()) {
                sqlExt = "and (currentareacode =\'" + areaCode
                        + "\' or currentareacode is null or  currentareacode =\'\')" + " and( (centerGuid=\'"
                        + centerGuid
                        + "\' and( acceptareacode is null or acceptareacode=\'\')) or acceptareacode is not null)";
            }

            if (this.commonDao.isOracle()) {
                sqlExt = "and (currentareacode =\'" + areaCode + "\' or currentareacode is null)"
                        + " and( (centerGuid=\'" + centerGuid
                        + "\' and acceptareacode is null) or acceptareacode is not null)";
            }

            String wherestr = " task_id in (" + taskids + ") and areacode=\'" + baseareacode + "\' and APPLYWAY = 20 ";
            wherestr = wherestr + sqlExt;
            tempList = this.getAuditProjectFieldList(fieldstr, groupstr, wherestr);

            for (int i = 0; i < tempList.size(); ++i) {
                arrobj = (AuditProject) tempList.get(i);
                if (24 == Integer.parseInt(arrobj.getStr("status"))) {
                    arg23 = arrobj.getStr("count") == null ? 0 : Integer.parseInt(arrobj.getStr("count"));
                } else if (26 == Integer.parseInt(arrobj.getStr("status"))) {
                    daishoulicount = arrobj.getStr("count") == null ? 0 : Integer.parseInt(arrobj.getStr("count"));
                } else if (28 == Integer.parseInt(arrobj.getStr("status"))) {
                    daibubancount = arrobj.getStr("count") == null ? 0 : Integer.parseInt(arrobj.getStr("count"));
                } else if (37 == Integer.parseInt(arrobj.getStr("status"))) {
                    daibubancount += arrobj.getStr("count") == null ? 0 : Integer.parseInt(arrobj.getStr("count"));
                }
            }

            fieldstr = "count(*) count";
            groupstr = "";
            wherestr = "  task_id in (" + taskids + ") AND is_pause = \'1\' and areacode=\'" + baseareacode + "\' "
                    + sqlExt;
            arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
            yizantingcount = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("count"));

            // 待办结
            fieldstr = "count(*) count";
            groupstr = "";
            wherestr = "  task_id in (" + taskids + ") and status>=\'" + 35 + "\' and status<=\'" + 50
                    + "\' and areacode=\'" + baseareacode + "\' and APPLYWAY = 20 " + sqlExt;
            arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
            daibanjiecount = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("count"));

            // 审批中
            fieldstr = "count(*) count";
            groupstr = "";
            wherestr = "  task_id in (" + taskids + ") and status>=\'" + 30 + "\' and status<\'" + 40
                    + "\' and areacode=\'" + baseareacode + "\' " + sqlExt;
            arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
            shenpicount = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("count"));

            // 待预审
            fieldstr = "count(*) count";
            groupstr = "";
            wherestr = " status =\'12\' and task_id in (" + taskids + ") and areacode=\'" + baseareacode + "\' "
                    + sqlExt;
            arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
            daiyushencount = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("count"));

            // 预审通过
            fieldstr = "count(*) count";
            groupstr = "";
            wherestr = "  task_id in (" + taskids + ") and status =\'" + 16 + "\' and areacode=\'" + baseareacode
                    + "\' " + sqlExt;
            arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
            yushentonguocount = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("count"));

            // 全程网办  webapplytype=2
            fieldstr = "count(*) count";
            groupstr = "";
            wherestr = "  p.task_id in (" + taskids + ") and status=\'" + 24 + "\' and p.areacode=\'" + baseareacode
                    + "\' and (APPLYWAY = 11 or APPLYWAY = 10) " + sqlExt + " and webapplytype='2' ";
            arrobj = this.getAuditProjectAndAudittaskField(fieldstr, groupstr, wherestr);
            qcwb = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("count"));

            // 外网办理
            fieldstr = "count(*) count";
            groupstr = "";
            wherestr = "  task_id in (" + taskids + ") and status>=\'" + 24 + "\' and status<\'" + 90
                    + "\' and areacode=\'" + baseareacode + "\' and (APPLYWAY = 11 or APPLYWAY = 10)" + sqlExt
                    + " and windowguid = '" + windowguid + "' ";
            arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
            wwbl = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("count"));

            // 外网办结
            fieldstr = "count(*) count";
            groupstr = "";
            wherestr = "  task_id in (" + taskids + ") and status>=\'" + 90 + "\' and areacode=\'" + baseareacode
                    + "\' and (APPLYWAY = 11 or APPLYWAY = 10)" + sqlExt + " and windowguid = '" + windowguid + "' ";
            arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
            wwbj = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("count"));

            // 外网申报  all
            fieldstr = "count(*) count";
            groupstr = "";
            wherestr = "  p.task_id in (" + taskids + ") and (status = '12' or status='24') and p.areacode=\'"
                    + baseareacode + "\' and (APPLYWAY = 11 or APPLYWAY = 10) and WEBAPPLYTYPE in ('1','2','4')"
                    + sqlExt;
            arrobj = this.getAuditProjectAndAudittaskField(fieldstr, groupstr, wherestr);
            wwsb = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("count"));

            // 在线申办  webapplytype=4
            fieldstr = "count(*) count";
            groupstr = "";
            wherestr = "  p.task_id in (" + taskids + ") and status =\'" + 24 + "\' and p.areacode=\'" + baseareacode
                    + "\' and (APPLYWAY = 11 or APPLYWAY = 10) " + sqlExt + " and webapplytype='4' and windowguid = '" + windowguid + "' ";
            arrobj = this.getAuditProjectAndAudittaskField(fieldstr, groupstr, wherestr);
            zxsb = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("count"));

        }

        arg22.put("DJJ", Integer.valueOf(arg23));
        arg22.put("DSL", Integer.valueOf(daishoulicount));
        arg22.put("YZT", Integer.valueOf(yizantingcount));
        arg22.put("DBJ", Integer.valueOf(daibanjiecount));
        arg22.put("DBB", Integer.valueOf(daibubancount));
        arg22.put("DYS", Integer.valueOf(daiyushencount));
        arg22.put("YSTG", Integer.valueOf(yushentonguocount));
        arg22.put("SPZ", Integer.valueOf(shenpicount));
        arg22.put("QCWB", Integer.valueOf(qcwb));
        arg22.put("WWBL", Integer.valueOf(wwbl));
        arg22.put("WWBJ", Integer.valueOf(wwbj));
        arg22.put("WWSB", Integer.valueOf(wwsb));
        arg22.put("ZXSB", Integer.valueOf(zxsb));
        return arg22;
    }

    public List<Record> selectHotTaskId() {
        String sql = "select task_id ,count(*) as num from AUDIT_Project group by TASK_ID order by num desc LIMIT 20";
        List list = this.commonDao.findList(sql, Record.class, new Object[0]);
        return list;
    }

    public <T> PageData<T> getRecordPageData(String fieldstr, Class<? extends BaseEntity> baseClass,
                                             Map<String, String> conditionMap, Integer first, Integer pageSize, String sortField, String sortOrder,
                                             String keyword) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(baseClass);
        PageData pageData = new PageData();
        Entity en = baseClass.getAnnotation(Entity.class);
        StringBuffer sb = new StringBuffer();
        sb.append(sqlManageUtil.buildSql(conditionMap));
        if (StringUtil.isNotBlank(keyword)) {
            sb.append(" and (flowsn like \'%" + keyword + "%\' or applyername like \'%" + keyword + "%\')");
        }

        // 增加窗口条件
        if (StringUtil.isNotBlank(ZwfwUserSession.getInstance().getWindowGuid())) {
            sb.append(" and windowguid =  '" + ZwfwUserSession.getInstance().getWindowGuid() + "' ");
        }

        if (StringUtil.isNotBlank(sortField)) {
            sb.append(" order by " + sortField + " " + sortOrder);
        }

        String sqlRecord = "select " + fieldstr + " from " + en.table() + sb.toString();

        String sqlCount = "select count(*) from " + en.table() + sb.toString();
        List dataList = this.commonDao.findList(sqlRecord, first.intValue(), pageSize.intValue(), baseClass,
                new Object[0]);
        int dataCount = this.commonDao.queryInt(sqlCount, new Object[0]).intValue();
        pageData.setList(dataList);
        pageData.setRowCount(dataCount);
        return pageData;
    }

    public <T> PageData<T> getRecordPageDataByHandleareacode(String fieldstr, Class<? extends BaseEntity> baseClass,
                                                             Map<String, String> conditionMap, Integer first, Integer pageSize, String sortField, String sortOrder,
                                                             String keyword, String handleareacode) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(baseClass);
        PageData pageData = new PageData();
        Entity en = baseClass.getAnnotation(Entity.class);
        StringBuffer sb = new StringBuffer();
        sb.append(sqlManageUtil.buildSql(conditionMap));
        if (StringUtil.isNotBlank(keyword)) {
            sb.append(" and (flowsn like \'%" + keyword + "%\' or applyername like \'%" + keyword + "%\')");
        }

        if (StringUtil.isNotBlank(handleareacode)) {
            sb.append(" and (handleareacode like\'%" + handleareacode + "\'or handleareacode like \'%" + handleareacode
                    + ",%\')");
        }

        if (StringUtil.isNotBlank(sortField)) {
            sb.append(" order by " + sortField + " " + sortOrder);
        }

        String sqlRecord = "select " + fieldstr + " from " + en.table() + sb.toString();
        String sqlCount = "select count(*) from " + en.table() + sb.toString();
        List dataList = this.commonDao.findList(sqlRecord, first.intValue(), pageSize.intValue(), baseClass,
                new Object[0]);
        int dataCount = this.commonDao.queryInt(sqlCount, new Object[0]).intValue();
        pageData.setList(dataList);
        pageData.setRowCount(dataCount);
        return pageData;
    }

    public Map<String, String> getReportCount(String ouguid, String areaCode) {
        HashMap map = new HashMap(16);
        boolean jishounum = false;
        boolean jibannum = false;
        boolean cnshounum = false;
        boolean cnbannum = false;
        boolean bushounum = false;
        boolean cqnum = false;
        boolean cnnum = false;
        AuditProject arrobj = null;
        String fieldstr = "status, count(1) count";
        String groupstr = "  group by STATUS";
        String wherestr = " areacode=\'" + areaCode + "\' ";
        fieldstr = " ouname ,ouguid, count(*) as JISHOUNUM ";
        groupstr = "";
        wherestr = " status BETWEEN 30 and 90 and tasktype=\'1\' and OUGUID =\'" + ouguid + "\' and areacode =\'"
                + areaCode + "\'";
        arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
        int jishounum1 = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("JISHOUNUM"));
        fieldstr = " ouname ,ouguid, count(*) as JIBANNUM ";
        groupstr = "";
        wherestr = " status=90  and tasktype=\'1\' and OUGUID =\'" + ouguid + "\' and areacode =\'" + areaCode + "\'";
        arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
        int jibannum1 = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("JIBANNUM"));
        fieldstr = " ouname ,ouguid, count(*) as CNSHOUNUM ";
        groupstr = "";
        wherestr = " status BETWEEN 30 and 90 and tasktype=\'2\' and OUGUID =\'" + ouguid + "\' and areacode =\'"
                + areaCode + "\'";
        arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
        int cnshounum1 = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("CNSHOUNUM"));
        fieldstr = " ouname ,ouguid, count(*) as CNBANNUM ";
        groupstr = "";
        wherestr = " status=90  and tasktype=\'2\' and OUGUID =\'" + ouguid + "\' and areacode =\'" + areaCode + "\'";
        arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
        int cnbannum1 = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("CNBANNUM"));
        fieldstr = "ouname ,ouguid, count(*) as BUSHOUNUM ";
        groupstr = "";
        wherestr = "  status =97 and OUGUID =\'" + ouguid + "\' and areacode =\'" + areaCode + "\'";
        arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
        int bushounum1 = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("BUSHOUNUM"));
        fieldstr = "ouname ,ouguid, count(*) as CQNUM ";
        groupstr = "";
        wherestr = " rowguid in (select projectguid from audit_project_sparetime where SPAREMINUTES<0) and tasktype=\'2\' and areacode =\'"
                + areaCode + "\' and OUGUID =\'" + ouguid + "\' ";
        arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
        int cqnum1 = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("CQNUM"));
        fieldstr = " ouname ,ouguid, count(*) as CNNUM ";
        groupstr = "";
        wherestr = " status=90  and tasktype=\'2\'  and OUGUID =\'" + ouguid + "\' and areacode =\'" + areaCode
                + "\' and SPARETIME>0 ";
        arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
        int cnnum1 = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("CNNUM"));
        double jibanlv;
        if (jishounum1 + cnshounum1 != 0) {
            jibanlv = (double) jishounum1 / (double) (jishounum1 + cnshounum1);
        } else {
            jibanlv = 0.0D;
        }

        double cntqlv;
        if (cnshounum1 != 0) {
            cntqlv = (double) cnnum1 / (double) cnshounum1;
        } else {
            cntqlv = 0.0D;
        }

        map.put("JISHOUNUM", jishounum1 + "");
        map.put("JIBANNUM", jibannum1 + "");
        map.put("CNSHOUNUM", cnshounum1 + "");
        map.put("CNBANNUM", cnbannum1 + "");
        map.put("LEISHOUNUM", jishounum1 + cnshounum1 + "");
        map.put("LEIBANNUM", jibannum1 + cnbannum1 + "");
        map.put("BUSHOUNUM", bushounum1 + "");
        map.put("BUBANNUM", bushounum1 + "");
        map.put("CQNUM", cqnum1 + "");
        map.put("JIBANLV", jibanlv + "");
        map.put("CNTIQIANLV", cntqlv + "");
        return map;
    }

    public Map<String, String> getReportCount(String ouguid, String areaCode, String startDate, String endDate) {
        HashMap map = new HashMap(16);
        boolean jishounum = false;
        boolean jibannum = false;
        boolean cnshounum = false;
        boolean cnbannum = false;
        boolean bushounum = false;
        boolean cqnum = false;
        boolean cnnum = false;
        AuditProject arrobj = null;
        String fieldstr = "status, count(1) count";
        String groupstr = "  group by STATUS";
        String wherestr = " areacode=\'" + areaCode + "\' ";
        String searchstr = " and APPLYDATE between str_to_date(\'" + startDate
                + " 00:00:00\',\'%Y-%m-%d %H:%i:%s\') and str_to_date(\'" + endDate
                + " 23:59:59\',\'%Y-%m-%d %H:%i:%s\')";
        fieldstr = " ouname ,ouguid, count(*) as JISHOUNUM ";
        groupstr = "";
        wherestr = " status BETWEEN 30 and 90 and tasktype=\'1\' and OUGUID =\'" + ouguid + "\' and areacode =\'"
                + areaCode + "\'" + searchstr;
        arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
        int jishounum1 = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("JISHOUNUM"));
        fieldstr = " ouname ,ouguid, count(*) as JIBANNUM ";
        groupstr = "";
        wherestr = " status=90  and tasktype=\'1\' and OUGUID =\'" + ouguid + "\' and areacode =\'" + areaCode + "\'"
                + searchstr;
        arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
        int jibannum1 = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("JIBANNUM"));
        fieldstr = " ouname ,ouguid, count(*) as CNSHOUNUM ";
        groupstr = "";
        wherestr = " status BETWEEN 30 and 90 and tasktype=\'2\' and OUGUID =\'" + ouguid + "\' and areacode =\'"
                + areaCode + "\'" + searchstr;
        arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
        int cnshounum1 = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("CNSHOUNUM"));
        fieldstr = " ouname ,ouguid, count(*) as CNBANNUM ";
        groupstr = "";
        wherestr = " status=90  and tasktype=\'2\' and OUGUID =\'" + ouguid + "\' and areacode =\'" + areaCode + "\'"
                + searchstr;
        arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
        int cnbannum1 = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("CNBANNUM"));
        fieldstr = "ouname ,ouguid, count(*) as BUSHOUNUM ";
        groupstr = "";
        wherestr = "  status =97 and OUGUID =\'" + ouguid + "\' and areacode =\'" + areaCode + "\'" + searchstr;
        arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
        int bushounum1 = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("BUSHOUNUM"));
        fieldstr = "ouname ,ouguid, count(*) as CQNUM ";
        groupstr = "";
        wherestr = " rowguid in (select projectguid from audit_project_sparetime where SPAREMINUTES<0) and tasktype=\'2\' and areacode =\'"
                + areaCode + "\' and OUGUID =\'" + ouguid + "\' " + searchstr;
        arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
        int cqnum1 = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("CQNUM"));
        fieldstr = " ouname ,ouguid, count(*) as CNNUM ";
        groupstr = "";
        wherestr = " status=90  and tasktype=\'2\'  and OUGUID =\'" + ouguid + "\' and areacode =\'" + areaCode
                + "\' and SPARETIME>0 " + searchstr;
        arrobj = this.getAuditProjectField(fieldstr, groupstr, wherestr);
        int cnnum1 = Integer.parseInt(arrobj.getStr("count") == null ? "0" : arrobj.getStr("CNNUM"));
        double jibanlv;
        if (jishounum1 + cnshounum1 != 0) {
            jibanlv = (double) jishounum1 / (double) (jishounum1 + cnshounum1);
        } else {
            jibanlv = 0.0D;
        }

        double cntqlv;
        if (cnshounum1 != 0) {
            cntqlv = (double) cnnum1 / (double) cnshounum1;
        } else {
            cntqlv = 0.0D;
        }

        map.put("JISHOUNUM", jishounum1 + "");
        map.put("JIBANNUM", jibannum1 + "");
        map.put("CNSHOUNUM", cnshounum1 + "");
        map.put("CNBANNUM", cnbannum1 + "");
        map.put("LEISHOUNUM", jishounum1 + cnshounum1 + "");
        map.put("LEIBANNUM", jibannum1 + cnbannum1 + "");
        map.put("BUSHOUNUM", bushounum1 + "");
        map.put("BUBANNUM", bushounum1 + "");
        map.put("CQNUM", cqnum1 + "");
        map.put("JIBANLV", jibanlv + "");
        map.put("CNTIQIANLV", cntqlv + "");
        return map;
    }

    public List<Record> getReportOu(String areaCode) {
        String sql = "select ouname,ouguid from Audit_Project where ouname!=\'\' and ouname !=\'&nbsp;&nbsp;政务服务中心\' and areacode =\'"
                + areaCode + "\'  group by OUNAME,OUGUID";
        List findList = this.commonDao.findList(sql, Record.class, new Object[0]);
        this.commonDao.close();
        return findList;
    }

    public List<Record> getHotBanJianList(List<String> taskGuidList, String centerguid) {
        String sql = "";
        sql = "select count(*) as num,TASK_ID from audit_project where task_id in (\'"
                + StringUtil.join(taskGuidList, "\',\'") + "\')  and centerguid = \'" + centerguid
                + "\' GROUP BY TASK_ID ORDER BY num DESC ";
        List findList = this.commonDao.findList(sql, 1, 3, Record.class, new Object[0]);
        this.commonDao.close();
        return findList;
    }

    public List<Record> getHotBanJianList(List<String> taskGuidList, String centerguid, String areacode) {
        String sql = "";
        SqlConditionUtil sqlUtil = new SqlConditionUtil();
        sqlUtil.eq("centerguid", centerguid);
        sqlUtil.eq("areacode", areacode);
        SQLManageUtil sqlManageUtil = new SQLManageUtil(AuditProject.class);
        StringBuffer sb = new StringBuffer();
        sb.append(sqlManageUtil.buildPatchSql(sqlUtil.getMap()));
        sql = "select count(*) as num,TASK_ID from audit_project where task_id in (\'"
                + StringUtil.join(taskGuidList, "\',\'") + "\') " + sb.toString()
                + "  GROUP BY TASK_ID ORDER BY num DESC ";
        List findList = this.commonDao.findList(sql, 0, 3, Record.class, new Object[0]);
        this.commonDao.close();
        return findList;
    }

    public List<Record> getWeekLineList(List<String> ouguidlist, String status, Date date) {
        String sql = "";
        SqlConditionUtil sqlUtil = new SqlConditionUtil();
        sqlUtil.ge("BANJIEDATE", date);
        sqlUtil.eq("status", status);
        SQLManageUtil sqlManageUtil = new SQLManageUtil(AuditProject.class);
        StringBuffer sb = new StringBuffer();
        sb.append(sqlManageUtil.buildPatchSql(sqlUtil.getMap()));
        sql = "select count(*) as num,ouguid from audit_project where ouguid in (\'"
                + StringUtil.join(ouguidlist, "\',\'") + "\') " + sb.toString() + "GROUP BY ouguid ORDER BY num DESC ";
        List findList = this.commonDao.findList(sql, Record.class, new Object[0]);
        this.commonDao.close();
        return findList;
    }

    public List<Record> getWeekLineList(String centerguid, String areacode, String status, Date date) {
        String sql = "";
        SqlConditionUtil sqlUtil = new SqlConditionUtil();
        sqlUtil.ge("APPLYDATE", date);
        sqlUtil.ge("status", status);
        sqlUtil.eq("centerguid", centerguid);
        SQLManageUtil sqlManageUtil = new SQLManageUtil(AuditProject.class);
        StringBuffer sb = new StringBuffer();
        sb.append(sqlManageUtil.buildPatchSql(sqlUtil.getMap()));
        sql = "select count(*) as num,ouguid from audit_project where areacode =?1   " + sb.toString()
                + "GROUP BY ouguid ORDER BY num DESC ";
        List findList = this.commonDao.findList(sql, Record.class, new Object[]{areacode});
        this.commonDao.close();
        return findList;
    }

    public List<Record> getWeekLineListByOu(List<String> ouGuidList, String status, Date date) {
        String sql = "";
        SqlConditionUtil sqlUtil = new SqlConditionUtil();
        sqlUtil.ge("ACCEPTUSERDATE", date);
        sqlUtil.ge("status", status);
        SQLManageUtil sqlManageUtil = new SQLManageUtil(AuditProject.class);
        StringBuffer sb = new StringBuffer();
        sb.append(sqlManageUtil.buildPatchSql(sqlUtil.getMap()));
        sql = "select count(*) as num,ouguid from audit_project where ouguid in (\'"
                + StringUtil.join(ouGuidList, "\',\'") + "\') " + sb.toString() + "GROUP BY ouguid ORDER BY num DESC ";
        List findList = this.commonDao.findList(sql, Record.class, new Object[0]);
        this.commonDao.close();
        return findList;
    }

    public Integer getAuditProjectCountByApplyway(Map<String, String> conditionMap, String applyway) {
        String sql = "";
        SQLManageUtil sqlManageUtil = new SQLManageUtil(AuditProject.class);
        StringBuffer sb = new StringBuffer();
        sb.append(sqlManageUtil.buildSql(conditionMap));
        sql = "select count(*) as num from audit_project  " + sb.toString()
                + "and taskguid in (select rowguid from audit_task where shenpilb = \'" + applyway + "\' )  ";
        Integer findList = this.commonDao.queryInt(sql, new Object[0]);
        this.commonDao.close();
        return findList;
    }

    public List<Record> getAuditProjectCountByCondition(Map<String, String> conditionMap) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(AuditProject.class);
        StringBuffer sb = new StringBuffer();
        sb.append(sqlManageUtil.buildSql(conditionMap));
        String sql = "select count(*) as num,tasktype from audit_project " + sb.toString() + " GROUP BY tasktype";
        List findList = this.commonDao.findList(sql, Record.class, new Object[0]);
        this.commonDao.close();
        return findList;
    }

    public Integer getTodayHandleProjectCount(String conditionMap) {
        boolean count = false;
        String sql = "select count(*) from audit_project " + conditionMap;
        int count1 = this.commonDao.queryInt(sql, new Object[0]).intValue();
        this.commonDao.close();
        return Integer.valueOf(count1);
    }

    public Integer getTodayHandleProjectCount(String conditionMap, String field) {
        boolean count = false;
        String sql = "select count(*) from audit_project " + conditionMap;
        if (this.commonDao.isMySql()) {
            sql = sql + "and date(" + field + ") = curdate()";
        } else if (this.commonDao.isOracle()) {
            sql = sql + "and to_char(" + field + ",\'yyyy-MM-dd\')=to_char(sysdate,\'yyyy-MM-dd\')";
        }

        int count1 = this.commonDao.queryInt(sql, new Object[0]).intValue();
        this.commonDao.close();
        return Integer.valueOf(count1);
    }

    public Integer getOverUserCount(Map<String, String> conditionMap, int todayHandle) {
        boolean count = false;
        SQLManageUtil sqlManageUtil = new SQLManageUtil(AuditProject.class);
        StringBuffer sb = new StringBuffer();
        sb.append(sqlManageUtil.buildSql(conditionMap));
        String sql = "";
        if (this.commonDao.isMySql()) {
            sql = "select count(*) from ( select count(*),ACCEPTUSERGUID FROM audit_project " + sb.toString()
                    + "and date(ACCEPTUSERDATE) = curdate() GROUP BY ACCEPTUSERGUID HAVING COUNT(*)<\'" + todayHandle
                    + "\')t";
        } else if (this.commonDao.isOracle()) {
            sql = "select count(*) from ( select count(*),ACCEPTUSERGUID FROM audit_project " + sb.toString()
                    + "and to_char(ACCEPTUSERDATE,\'yyyy-MM-dd\')=to_char(sysdate,\'yyyy-MM-dd\') GROUP BY ACCEPTUSERGUID HAVING COUNT(*)<\'"
                    + todayHandle + "\')t";
        }

        int count1 = this.commonDao.queryInt(sql, new Object[0]).intValue();
        this.commonDao.close();
        return Integer.valueOf(count1);
    }

    public Integer getOverFinishCount(Map<String, String> conditionMap, int todayFinish) {
        boolean count = false;
        SQLManageUtil sqlManageUtil = new SQLManageUtil(AuditProject.class);
        StringBuffer sb = new StringBuffer();
        sb.append(sqlManageUtil.buildSql(conditionMap));
        String sql = "";
        if (this.commonDao.isMySql()) {
            sql = "select count(*) from ( select count(*),BANJIEUSERGUID FROM audit_project " + sb.toString()
                    + "and date(BANJIEDATE) = curdate() GROUP BY BANJIEUSERGUID HAVING COUNT(*)<\'" + todayFinish
                    + "\')t";
        } else if (this.commonDao.isOracle()) {
            sql = "select count(*) from ( select count(*),BANJIEUSERGUID FROM audit_project " + sb.toString()
                    + "and to_char(BANJIEDATE,\'yyyy-MM-dd\')=to_char(sysdate,\'yyyy-MM-dd\') GROUP BY BANJIEUSERGUID HAVING COUNT(*)<\'"
                    + todayFinish + "\')t";
        }

        int count1 = this.commonDao.queryInt(sql, new Object[0]).intValue();
        this.commonDao.close();
        return Integer.valueOf(count1);
    }

    public Integer getAvgSpendtime(Map<String, String> map) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(AuditProject.class);
        String sql = "select floor(avg(spendtime)) spendtime from audit_project where areacode=\'320001\' "
                + sqlManageUtil.buildPatchSql(map);
        return this.commonDao.queryInt(sql, new Object[0]);
    }

    public PageData<AuditProject> getPagaBySpareTime(String fieldStr, Map<String, String> conditionMap,
                                                     Integer firstResult, Integer maxResults, String sortField, String sortOrder, String string) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(AuditProject.class);
        PageData pageData = new PageData();
        String sqlext = sqlManageUtil.buildPatchSql(conditionMap);
        String sqlRecord = "select " + fieldStr
                + " from  audit_project a,audit_project_sparetime b where a.rowguid=b.projectguid " + sqlext;
        String sqlCount = "select count(1) from  audit_project a,audit_project_sparetime b where a.rowguid=b.projectguid "
                + sqlext;
        /// //system.out.println(sqlCount);
        List dataList = this.commonDao.findList(sqlRecord, firstResult.intValue(), maxResults.intValue(),
                AuditProject.class, new Object[0]);
        int dataCount = this.commonDao.queryInt(sqlCount, new Object[0]).intValue();
        pageData.setList(dataList);
        pageData.setRowCount(dataCount);
        return pageData;
    }

    public PageData<AuditProject> getPagaBySpareTime(String fieldStr, Map<String, String> conditionMap,
                                                     Integer firstResult, Integer maxResults, String sortField, String sortOrder, String string,
                                                     String userGuid) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(AuditProject.class);
        PageData pageData = new PageData();
        String sqlext = "";
        if (StringUtil.isNotBlank(userGuid)) {
            sqlext += " and ((TASKTYPE = 1 AND ACCEPTUSERGUID = '" + userGuid
                    + "') OR (TASKTYPE = 2 AND RECEIVEUSERGUID = '" + userGuid + "' ))";
        }
        sqlext += sqlManageUtil.buildPatchSql(conditionMap);
        String sqlRecord = "select " + fieldStr
                + " from  audit_project a,audit_project_sparetime b where a.rowguid=b.projectguid " + sqlext;
        String sqlCount = "select count(1) from  audit_project a,audit_project_sparetime b where a.rowguid=b.projectguid "
                + sqlext;
        /// //system.out.println(sqlCount);
        List dataList = this.commonDao.findList(sqlRecord, firstResult.intValue(), maxResults.intValue(),
                AuditProject.class, new Object[0]);
        int dataCount = this.commonDao.queryInt(sqlCount, new Object[0]).intValue();
        pageData.setList(dataList);
        pageData.setRowCount(dataCount);
        return pageData;
    }

    public PageData<AuditProject> getAuditProjectListByBZ(String fieldstr, Map<String, String> map, int first,
                                                          int pageSize, String sortField, String sortOrder, String string) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(AuditProject.class);
        PageData pageData = new PageData();
        String sqlext = sqlManageUtil.buildSql(map);
        String sqlRecord = "select " + fieldstr + " from  audit_project a " + sqlext
                + " and  EXISTS(select a.RowGuid from audit_project_operation b where a.rowguid=b.projectguid and operatetype=22)";
        String sqlCount = "select count(1) from  audit_project a  " + sqlext
                + " and  EXISTS(select a.RowGuid from audit_project_operation b where a.rowguid=b.projectguid and operatetype=22)";
        List dataList = this.commonDao.findList(sqlRecord, first, pageSize, AuditProject.class, new Object[0]);
        int dataCount = this.commonDao.queryInt(sqlCount, new Object[0]).intValue();
        pageData.setList(dataList);
        pageData.setRowCount(dataCount);
        return pageData;
    }
}
