package com.epoint.basic.auditqueue.service;

import java.util.List;
import java.util.Map;

import com.epoint.basic.cache.Cache;
import com.epoint.basic.cache.CacheService9;
import com.epoint.common.service.AuditCommonService;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.config.SplitTableConfig;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.sharding.util.ShardingUtil;

@Cache
public class JxAuditQueueBasicService<T> extends CacheService9
{

    /**
     * 
     */
    private static final long serialVersionUID = 1223708331052361525L;

    /**
     * 通用dao
     */
    private ICommonDao commonDao;

    public JxAuditQueueBasicService() {
        commonDao = CommonDao.getInstance();
    }

    public JxAuditQueueBasicService(Class<? extends BaseEntity> baseClass) {
        Entity en = baseClass.getAnnotation(Entity.class);
        SplitTableConfig conf = ShardingUtil.getSplitTableConfig(en.table());
        if (conf != null) {
            commonDao = CommonDao.getInstance(conf);
        }
        else {
            commonDao = CommonDao.getInstance();
        }
    }

    /**
     * 
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
     * 
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

    public void deleteRecords(Class<? extends BaseEntity> baseClass, String keyValue, String key) {
        Entity en = baseClass.getAnnotation(Entity.class);
        if (StringUtil.isNotBlank(en.table())) {
            // 直接拼接删除语句进行处理
            String sql = "delete from " + en.table() + " where " + key + "=?1";
            commonDao.execute(sql, keyValue);
        }
    }

    public void deleteRecordByMap(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap) {
        Entity en = baseClass.getAnnotation(Entity.class);
        if (StringUtil.isNotBlank(en.table())) {
            String sql = "delete from " + en.table();
            SQLManageUtil sqlManageUtil = new SQLManageUtil();
            sql += sqlManageUtil.buildSql(conditionMap);
            commonDao.execute(sql);
        }
    }

    /**
     * 
     * 对办件信息根据条件查询
     * 
     * @param conditionMap
     *            条件map， key为字段名称，value为值
     * @param first
     *            起始记录数
     * @param pageSize
     *            最大记录数
     * @param sortField
     *            排序值
     * @param sortOrder
     *            排序字段
     * @return PageData<AuditTask>
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("hiding")
    public <T> PageData<T> getRecordPageData(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap,
            int first, int pageSize, String sortField, String sortOrder) {
        PageData<T> pageData = new PageData<T>();
        SQLManageUtil sqlManageUtil = new SQLManageUtil(baseClass);
        pageData = sqlManageUtil.getDbListByPage(baseClass, conditionMap, first, pageSize, sortField, sortOrder);
        return pageData;
    }

    /**
     * 
     * 对办件信息根据条件查询
     * 
     * @param strwhere
     *            筛选条件的sql
     * @param first
     *            起始记录数
     * @param pageSize
     *            最大记录数
     * @param sortField
     *            排序值
     * @param sortOrder
     *            排序字段
     * @return PageData<AuditTask>
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("hiding")
    public <T> PageData<T> getRecordPageData(Class<? extends BaseEntity> baseClass, String strWhere, int first,
            int pageSize, String strOrder) {
        PageData<T> pageData = new PageData<T>();
        AuditCommonService commonService = new AuditCommonService(baseClass);
        pageData = commonService.getDbListByPage(baseClass, strWhere, first, pageSize, strOrder);
        return pageData;
    }

    /**
     * 
     * 根据条件查询
     * 
     * @param fieldstr
     *            查询字段
     * @param conditionMap
     *            条件map， key为字段名称，value为值
     * @param first
     *            起始记录数
     * @param pageSize
     *            最大记录数
     * @param sortField
     *            排序值
     * @param sortOrder
     *            排序字段
     * @return PageData<AuditTask>
     */
    @SuppressWarnings({"unchecked", "hiding" })
    public <T> PageData<T> getRecordPageData(String fieldstr, Class<? extends BaseEntity> baseClass,
            Map<String, String> conditionMap, Integer first, Integer pageSize, String sortField, String sortOrder) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(baseClass);
        PageData<T> pageData = new PageData<T>();
        Entity en = baseClass.getAnnotation(Entity.class);
        // 条件sql
        StringBuffer sb = new StringBuffer();
        sb.append(sqlManageUtil.buildSql(conditionMap));
        // 增加Orderby语句
        sb.append(" and rowguid in (select distinct tasktypeguid from audit_queue_window_tasktype) ");
        if (StringUtil.isNotBlank(sortField)) {
            sb.append(" order by " + sortField + " " + sortOrder);
        }
        String sqlRecord = "select " + fieldstr + " from " + en.table() + sb.toString();
        String sqlCount = "select count(*) from " + en.table() + sb.toString();
        List<T> dataList = (List<T>) commonDao.findList(sqlRecord, first, pageSize, baseClass);

        int dataCount = commonDao.queryInt(sqlCount);
        pageData.setList(dataList);
        pageData.setRowCount(dataCount);
        return pageData;
    }

    /**
     * 
     * 获取某条记录
     * 
     * @param baseClass
     * @param rowGuid
     * @param key
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings({"unchecked", "hiding" })
    public <T> T getDetail(Class<? extends BaseEntity> baseClass, String rowGuid, String key) {
        String sql = "";
        Entity en = baseClass.getAnnotation(Entity.class);
        if (StringUtil.isNotBlank(en.table())) {
            // 直接拼接查询语句进行处理
            sql = "select * from " + en.table() + " where " + key + "=?1";
        }
        if (StringUtil.isNotBlank(sql)) {
            return (T) commonDao.find(sql, baseClass, rowGuid);
        }
        else {
            return null;
        }
    }

    /**
     * 
     * 获取某条记录
     * 
     * @param baseClass
     * @param conditonMap
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings({"unchecked", "hiding" })
    public <T> T getDetail(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap) {
        String sql = "";
        Entity en = baseClass.getAnnotation(Entity.class);
        if (StringUtil.isNotBlank(en.table())) {
            // 直接拼接查询语句进行处理
            sql = "select * from " + en.table();
            SQLManageUtil sqlManageUtil = new SQLManageUtil(baseClass);
            sql += sqlManageUtil.buildSql(conditionMap);
            return (T) commonDao.find(sql, baseClass);
        }
        else {
            return null;
        }
    }

    /**
     * 
     * 获取记录列表
     * 
     * @param <T>
     * @param baseClass
     * @param conditonMap
     * @return
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */

    @SuppressWarnings({"hiding" })
    public <T> List<T> selectRecordList(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(baseClass);
        return sqlManageUtil.getListByCondition(baseClass, conditionMap);
    }

    @SuppressWarnings({"hiding" })
    public <T> List<T> selectRecordList(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap,
            String fields) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil(baseClass);
        return sqlManageUtil.getListByCondition(baseClass, conditionMap, fields);
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
            }
            sql = sql.substring(0, sql.length() - 1);
            SQLManageUtil sqlManageUtil = new SQLManageUtil(baseClass);
            sql += sqlManageUtil.buildSql(conditionMap);
            commonDao.execute(sql);
        }
    }

    public int gettasktypesfzCount(String tasktypeguid, String sfz) {
        String sql = "select  count(1) from  audit_queue where TASKGUID =? and IDENTITYCARDNUM = ? and DATE(GETNOTIME)= DATE(now())";
        return commonDao.queryInt(sql, tasktypeguid, sfz);
    }

    /*
     * public void closeConnection(){
     * commonDao.close();
     * }
     */
    @Override
    public void init(String arg0, String arg1, String arg3) {

    }

}
