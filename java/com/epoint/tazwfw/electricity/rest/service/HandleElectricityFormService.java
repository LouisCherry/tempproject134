package com.epoint.tazwfw.electricity.rest.service;

import java.util.List;

import org.apache.log4j.Logger;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;

public class HandleElectricityFormService
{

    transient Logger log = Logger.getLogger(HandleElectricityFormService.class);
    /**
     * 数据库操作DAO
     */
    protected ICommonDao commonDao;

    public HandleElectricityFormService() {
        commonDao = CommonDao.getInstance();
    }

    /**
     * DI
     * 
     * @return
     */
    public ICommonDao getCommonDao() {
        return commonDao;
    }

    public int insertData(String tableName, String columns, String[] value) {
        StringBuffer valueBuff = new StringBuffer();
        for (int i = 0; i < value.length; i++) {
            String tableColunm = value[i];
            if (tableColunm != null) {
                tableColunm = "'" + tableColunm + "'";
            }
            if (i == (value.length - 1)) {
                valueBuff.append(tableColunm);
            }
            else {
                valueBuff.append(tableColunm + ",");
            }
        }
        // 操作时间字段加在最前
        String sql = "INSERT INTO " + tableName + " (" + columns + ") VALUES ( ";
        if (columns.contains("OPERATEDATE")) {
            sql += " SYSDATE(), ";
        }
        sql += " " + valueBuff.toString() + ")";
        log.info("========执行sql插入========SQL：" + sql);
        int i = commonDao.execute(sql);
        return i;
    }

    /**
     * 根据taskID查询对应的表单表名（sql_tablename）
     * 
     * @param tsakID
     * @return
     */
    public String getTableName(String tsakID) {
        String sql = " SELECT SQL_TABLENAME FROM EPOINTSFORM_TABLE_BASICINFO WHERE TABLEID =("
                + " SELECT FORMTABLEID FROM AUDIT_TASK_TAIAN WHERE TASK_ID =? )";
        String tableName = commonDao.find(sql, String.class, tsakID);
        return tableName;
    }

    /**
     * 根据sql_tablename查询对应的表单id
     * 
     * @param tsakID
     * @return
     */
    public String getTableID(String sql_tablename) {
        String sql = " SELECT tableid FROM EPOINTSFORM_TABLE_BASICINFO WHERE SQL_TABLENAME = ? ";
        String tableName = commonDao.find(sql, String.class, sql_tablename);
        return tableName;
    }

    /**
     * 根据biGuid查询办件信息
     * @param biGuid
     * @return
     */
    public List<AuditProject> getPorjectByBiGuid(String biGuid) {
        List<AuditProject> projectList = null;
        String sql = "SELECT * FROM AUDIT_PROJECT WHERE BIGUID =?";
        projectList = commonDao.findList(sql, AuditProject.class, biGuid);
        return projectList;
    }

}
