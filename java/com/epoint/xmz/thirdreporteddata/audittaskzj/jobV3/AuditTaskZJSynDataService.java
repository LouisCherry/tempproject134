package com.epoint.xmz.thirdreporteddata.audittaskzj.jobV3;

import com.epoint.basic.spgl.domain.*;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import org.quartz.DisallowConcurrentExecution;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@DisallowConcurrentExecution
public class AuditTaskZJSynDataService {
    protected ICommonDao commonDao;
    protected ICommonDao qzkcommonDao;

    //初始化数据源
    public AuditTaskZJSynDataService() {
        String URL = ConfigUtil.getConfigValue("datasyncjdbc", "zjqzkurl_v3");
        String NAME = ConfigUtil.getConfigValue("datasyncjdbc", "zjqzkname_v3");
        String PASSWORD = ConfigUtil.getConfigValue("datasyncjdbc", "zjqzkpassword_v3");

        DataSourceConfig dataSourceConfig = new DataSourceConfig(URL, NAME, PASSWORD);
        /**
         * 数据库操作DAO
         */
        commonDao = CommonDao.getInstance();

        qzkcommonDao = CommonDao.getInstance(dataSourceConfig);
    }

    /**
     * 前置库数据源
     */

    /**
     * 查询需要同步的记录
     *
     * @param baseClass
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> selectNeedsync(Class<? extends BaseEntity> baseClass) {
        Entity en = baseClass.getAnnotation(Entity.class);
        String table = en.table();
        String sql = "select * from " + table + " where (sync = 0 or sync is null) and sjsczt = 0 order by row_id asc";
        return (List<T>) commonDao.findList(sql, 0, 50, baseClass);
    }

    /**
     * 查询需要同步的单位
     *
     * @return
     */
    public List<SpglXmdwxxb> selectNeedXmdwxxb() {
        String sql = "select * from SPGL_XMDWXXB a where (sync = 0 or sync is null) and sjsczt = 0 and  "
                + "(select count(1) from spgl_xmjbxxb b where a.`GCDM` = b.`GCDM` and a.`XMDM`  = b.`XMDM` and b.sjsczt =3) > 0 order by row_id asc";
        return commonDao.findList(sql, 0, 50, SpglXmdwxxb.class);
    }

    /**
     * 查询需要同步的事项实例
     *
     * @return
     */
    public List<SpglXmspsxblxxb> selectNeedXmspsxblxxb() {
        String sql = "select * from spgl_xmspsxblxxb a where (sync = 0 or sync is null) and sjsczt = 0 and  "
                + "(select count(1) from spgl_xmjbxxb b where a.`GCDM` = b.`GCDM` and b.sjsczt =3) > 0 order by row_id asc";
        return commonDao.findList(sql, 0, 50, SpglXmspsxblxxb.class);
    }

    /**
     * 查询需要同步的环节
     *
     * @return
     */
    public List<SpglXmspsxblxxxxb> selectNeedXmspsxblxxxxb() {
        String sql = "select * from Spgl_Xmspsxblxxxxb a where (sync = 0 or sync is null) and sjsczt = 0 and  "
                + "(select count(1) from SPGL_XMSPSXBLXXB b where a.`GCDM` = b.`GCDM` and a.`SPSXSLBM` =b.`SPSXSLBM` and b.sjsczt =3) > 0 order by row_id asc";
        return commonDao.findList(sql, 0, 50, SpglXmspsxblxxxxb.class);
    }

    /**
     * 查询需要同步的前期意见
     *
     * @return
     */
    public List<SpglXmspsxzqyjxxb> selectNeedXmspsxzqyjxxb() {
        String sql = "select * from SPGL_XMSPSXZQYJXXB a where (sync = 0 or sync is null) and sjsczt = 0 and  "
                + "(select count(1) from SPGL_XMSPSXBLXXB b where a.`GCDM` = b.`GCDM` and a.`SPSXSLBM` =b.`SPSXSLBM` and b.sjsczt =3) > 0 order by row_id asc";
        return commonDao.findList(sql, 0, 50, SpglXmspsxzqyjxxb.class);
    }

    /**
     * 查询需要同步的特別程序
     *
     * @return
     */
    public List<SpglXmspsxbltbcxxxb> selectNeedXmspsxbltbcxxxb() {
        String sql = "select * from SPGL_XMSPSXBLTBCXXXB a where (sync = 0 or sync is null) and sjsczt = 0 and  "
                + "(select count(1) from SPGL_XMSPSXBLXXB b where a.`GCDM` = b.`GCDM` and a.`SPSXSLBM` =b.`SPSXSLBM` and b.sjsczt =3) > 0 order by row_id asc";
        return commonDao.findList(sql, 0, 50, SpglXmspsxbltbcxxxb.class);
    }

    /**
     * 查询需要同步的特別程序
     *
     * @return
     */
    public List<SpglXmspsxpfwjxxb> selectNeedXmspsxpfwjxxb() {
        String sql = "select * from SPGL_XMSPSXPFWJXXB a where (sync = 0 or sync is null) and sjsczt = 0 and  "
                + "(select count(1) from SPGL_XMSPSXBLXXB b where a.`GCDM` = b.`GCDM` and a.`SPSXSLBM` =b.`SPSXSLBM` and b.sjsczt =3) > 0 order by row_id asc";
        return commonDao.findList(sql, 0, 50, SpglXmspsxpfwjxxb.class);
    }

    /**
     * 查询需要同步的特別程序
     *
     * @return
     */
    public List<SpglXmqtfjxxb> selectNeedXmqtfjxxb() {
        String sql = "select * from SPGL_XMQTFJXXB a where (sync = 0 or sync is null) and sjsczt = 0 and  "
                + "(select count(1) from SPGL_XMSPSXBLXXB b where a.`GCDM` = b.`GCDM` and a.`SPSXSLBM` =b.`SPSXSLBM` and b.sjsczt =3) > 0 order by row_id asc";
        return commonDao.findList(sql, 0, 50, SpglXmqtfjxxb.class);
    }


    /**
     * 查询已同步、有效且数据上传状态为sjsczt的记录
     *
     * @param baseClass
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> selectSynced(Class<? extends BaseEntity> baseClass, String sjsczt) {
        Entity en = baseClass.getAnnotation(Entity.class);
        String table = en.table();
        String sql = "select * from " + table + " where sjyxbs = 1 and sync = 1 and lsh is not null and sjsczt = ?";
        return (List<T>) commonDao.findList(sql, 0, 50, baseClass, sjsczt);
    }

    /**
     * 查询需要同步的记录
     *
     * @param baseClass
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> selectNeedAqbjsjsync(Class<? extends BaseEntity> baseClass) {
        Entity en = baseClass.getAnnotation(Entity.class);
        String table = en.table();
        String sql = "select * from " + table + "  where (sync = 0 or sync is null) and sjsczt = 0 order by row_id asc";
        return (List<T>) commonDao.findList(sql, 0, 50, baseClass);
    }

    /**
     * 新增某条记录
     *
     * @param baseClass
     * @param record
     */
    public void addRecord(Class<? extends BaseEntity> baseClass, Record record) {
        Entity en = baseClass.getAnnotation(Entity.class);
        if (StringUtil.isNotBlank(en.table())) {
            qzkcommonDao.insert(record);
            qzkcommonDao.close();
        }
    }

    /**
     * 新增某条记录，返回流水号
     *
     * @param baseClass
     * @param record
     */
    public String insertRecord(Class<? extends BaseEntity> baseClass, Record record) {
        Entity en = baseClass.getAnnotation(Entity.class);
        String lsh = null;
        if (StringUtil.isNotBlank(en.table())) {
            // 插入表
            record.setSql_TableName(isV3Domain(baseClass));
            record.remove("lsh");
            record.remove("sync");
            //qzkcommonDao.insert(record);
            //手动拼接sql的方式插入数据
            dataTSTableToZJK(isV3Domain(baseClass), record);
            lsh = null;
        }
        return lsh;
    }

    public void dataTSTableToZJK(String tablename, Record record) {
        String nofields = "belongxiaqucode,operateusername,operatedate,row_id,yearflag,rowguid,lsh";
        String fieldssql = "";
        String valssql = "";
        Boolean isnull = true;
        Iterator itertor = record.keySet().iterator();
        while (itertor.hasNext()) {
            String key = (String) itertor.next();
            if (nofields.contains(key)) {
                continue;
            }
            fieldssql += StringUtil.isBlank(fieldssql) ? key : "," + key;
            Object value = record.get(key);
            if (value == null) {
                valssql += isnull ? value : "," + value;
                isnull = false;
            } else {
                String classname = value.getClass().getName();
                if (classname.contains("String") || classname.contains("Time")) {
                    valssql += isnull ? "'" + record.getStr(key) + "'" : ",'" + record.getStr(key) + "'";
                    isnull = false;
                } else {
                    valssql += isnull ? value : "," + value;
                    isnull = false;
                }
            }

        }
        String sql = "INSERT INTO " + tablename + "(" + fieldssql + ") values (" + valssql + ")";
        qzkcommonDao.execute(sql);
        qzkcommonDao.close();
    }

    /**
     * 修改本地库数据
     *
     * @param baseClass
     * @param record
     */
    public void updateRecord(Class<? extends BaseEntity> baseClass, Record record) {
        Entity en = baseClass.getAnnotation(Entity.class);
        if (StringUtil.isNotBlank(en.table())) {
            commonDao.update(record);
        }
    }

    /**
     * 查询数据同步到前置库后数据上传状态的值
     *
     * @param baseClass
     * @param lsh
     */
    public Record selectAfterSyncInfoByLsh(Class<? extends BaseEntity> baseClass, String lsh) {
        Entity en = baseClass.getAnnotation(Entity.class);
        String sql = "select sjsczt,sbyy  from " + isV3Domain(baseClass) + " where lsh = ?";
        Record record= qzkcommonDao.find(sql, Record.class, lsh);
        qzkcommonDao.close();
        return record;
    }


    /**
     * 查询已同步过的（同步成功或失败）且流水号为空的列表
     *
     * @param baseClass
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> selectAfterSyncAndLshIsNullInfo(Class<? extends BaseEntity> baseClass) {
        Entity en = baseClass.getAnnotation(Entity.class);
        String table = en.table();
        String sql = "select * from " + table + "  where sjyxbs = 1 and sync = 1 and lsh is null order by row_id asc";
        return (List<T>) commonDao.findList(sql, 0, 50, baseClass);
    }

    /**
     * 查询前置库列表
     *
     * @param baseClass
     * @param conditionMap
     */
    public <T> List<T> selectListByCondition(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(qzkcommonDao);

        return sqlManageUtil.getListByCondition(baseClass, conditionMap);
    }

    /**
     * 查询前置库列表
     *
     * @param baseClass
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> selectListByConditions(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap, int pageNumber, int pageSize) {
        Entity en = baseClass.getAnnotation(Entity.class);
        SQLManageUtil sqlManageUtil = new SQLManageUtil(qzkcommonDao);

        String sql = "select *  from " + isV3Domain(baseClass) + " where 1=1  " + sqlManageUtil.buildPatchSql(conditionMap);
        List<T> records= (List<T>) qzkcommonDao.findList(sql,pageNumber,pageSize,baseClass);
//        qzkcommonDao.close();
        return records;
    }

    public void closeDao() {
        commonDao.close();
        qzkcommonDao.close();
    }

    public String isV3Domain(Class<? extends BaseEntity> baseClass) {
        Entity en = baseClass.getAnnotation(Entity.class);
        if (en.table().contains("_V3")) {
            return en.table().replace("_V3", "");
        } else {
            return en.table();
        }
    }

}
