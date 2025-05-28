package com.epoint.zczwfw.zccommon.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.epoint.basic.bizlogic.sysconf.datasource.service.DataSourceService9;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.sql.SqlConditionUtil;
import com.epoint.core.utils.sql.SqlHelper;
import com.epoint.database.jdbc.config.SplitTableConfig;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.sharding.util.ShardingUtil;
import com.epoint.zczwfw.util.EnhancedSqlConditionUtil;

/**
 * 
 * 通用service
 * 
 * @author yrchan
 * @version 2022年4月19日
 */
public class ZcCommonService
{

    /**
     * 通用dao
     */
    private ICommonDao commonDao;

    public <T extends BaseEntity> ZcCommonService(Class<T> baseClass) {
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
     * 构造方法
     */
    public ZcCommonService() {
        commonDao = CommonDao.getInstance();
    }

    public ZcCommonService(CommonDao dao) {
        commonDao = dao;
    }

    public ZcCommonService(DataSourceService9 dataSourceService9) {
        commonDao = CommonDao.getInstance(dataSourceService9);
    }

    /**
     * 
     * 查询实体类
     * 
     * @param baseClass
     *            反射对象
     * @param primary
     *            主键
     * @return <T extends BaseEntity>
     */
    public <T extends BaseEntity> T find(Class<T> baseClass, String primary) {
        return commonDao.find(baseClass, primary);
    }

    /**
     * 
     * 查询实体类
     * 
     * @param baseClass
     * @param fieldName
     * @param fieldValue
     * @return T
     */
    public <T extends BaseEntity> T find(Class<T> baseClass, String fieldName, String fieldValue) {
        SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
        sqlConditionUtil.eq(fieldName, fieldValue);
        return this.find(baseClass, sqlConditionUtil.getMap());
    }

    /**
     * 
     * 查询实体类
     * 
     * @param baseClass
     * @param conditionMap
     * @return T
     */
    public <T extends BaseEntity> T find(Class<T> baseClass, Map<String, Object> conditionMap) {
        String sql = new SqlHelper().getSqlComplete(baseClass, conditionMap);
        return commonDao.find(sql, baseClass);
    }

    /**
     * 
     * 数据插入
     * 
     * @param record
     */
    public void insert(Record record) {
        commonDao.insert(record);
    }

    /**
     * 
     * 数据删除
     * 
     * @param record
     * @return
     */
    public Boolean delete(Record record) {
        return commonDao.delete(record) > 0;
    }

    /**
     * 
     * 数据更新
     * 
     * @param record
     * @return
     */
    public Boolean update(Record record) {
        ICommonDao dao = CommonDao.getInstance();
        Integer n = dao.update(record);
        return n > 0;
    }

    /**
     * 
     * 列表查询
     * 
     * @param conditionMap
     * @param baseClass
     * @return
     */
    public <T extends BaseEntity> List<T> findlist(Map<String, Object> conditionMap, Class<T> baseClass) {
        return commonDao.findList(new SqlHelper().getSqlComplete(baseClass, conditionMap), baseClass);
    }

    /**
     * 
     * 数量查询
     * 
     * @param conditionMap
     * @param baseClass
     * @return
     */
    public <T extends BaseEntity> int queryInt(Map<String, Object> conditionMap, Class<T> baseClass) {
        // 清除排序条件
        conditionMap.entrySet().removeIf(item -> item.getKey().contains("sort"));
        SqlConditionUtil conditionUtil = new EnhancedSqlConditionUtil(conditionMap);
        conditionUtil.setSelectFields("count(*)");
        List<Object> params = new ArrayList<>();
        return commonDao.queryInt(new SqlHelper().getSqlComplete(baseClass, conditionUtil.getMap(), params),
                params.toArray());
    }

    /**
     * 分页列表
     *
     * @param baseClass
     *            baseClass
     * @param map
     *            条件
     * @param first
     *            first
     * @param pageSize
     *            pageSize
     * @return 分页列表
     */
    public <T extends BaseEntity> PageData<T> paginatorList(Class<T> baseClass, Map<String, Object> map, int first,
            int pageSize) {
        List<Object> params = new ArrayList<>();
        List<T> list = commonDao.findList(new SqlHelper().getSqlComplete(baseClass, map, params), first, pageSize,
                baseClass, params.toArray());
        int count = count(baseClass, map);
        return new PageData<T>(list, count);
    }

    /**
     * 
     * 数量查询
     * 
     * @param baseClass
     * @param conditionMap
     * @return
     */
    public <T extends BaseEntity> Integer count(Class<T> baseClass, Map<String, Object> conditionMap) {
        // 清除排序条件
        conditionMap.entrySet().removeIf(item -> item.getKey().contains("sort"));
        SqlConditionUtil conditionUtil = new EnhancedSqlConditionUtil(conditionMap);
        conditionUtil.setSelectFields("count(*)");
        List<Object> params = new ArrayList<>();
        return commonDao.queryInt(new SqlHelper().getSqlComplete(baseClass, conditionUtil.getMap(), params),
                params.toArray());
    }

}
