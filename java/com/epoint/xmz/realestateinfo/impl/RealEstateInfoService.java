package com.epoint.xmz.realestateinfo.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.sql.SqlConditionUtil;
import com.epoint.core.utils.sql.SqlHelper;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.xmz.realestateinfo.api.entity.RealEstateInfo;

/**
 * 楼盘信息表对应的后台service
 * 
 * @author 1
 * @version [版本号, 2022-10-17 15:27:39]
 */
public class RealEstateInfoService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public RealEstateInfoService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(RealEstateInfo record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(RealEstateInfo.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(RealEstateInfo record) {
        return baseDao.update(record);
    }

    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public RealEstateInfo find(Object primaryKey) {
        return baseDao.find(RealEstateInfo.class, primaryKey);
    }

    /**
     * 查找单条记录
     * 
     * @param sql
     *            查询语句
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组
     *            ;String.class;Integer.class;Long.class]
     * @param args
     *            参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public RealEstateInfo find(String sql,  Object... args) {
        return baseDao.find(sql, RealEstateInfo.class, args);
    }

    /**
     * 查找一个list
     * 
     * @param sql
     *            查询语句
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args
     *            参数值数组
     * @return T extends BaseEntity
     */
    public List<RealEstateInfo> findList(String sql, Object... args) {
        return baseDao.findList(sql, RealEstateInfo.class, args);
    }

    /**
     * 分页查找一个list
     * 
     * @param sql
     *            查询语句
     * @param pageNumber
     *            记录行的偏移量
     * @param pageSize
     *            记录行的最大数目
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args
     *            参数值数组
     * @return T extends BaseEntity
     */
    public List<RealEstateInfo> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, RealEstateInfo.class, args);
    }

	/**
     * 查询数量
     * 
     * @param sql
     *            执行语句
     * @param args
     *            参数
     * @return Integer
     */
    public Integer countRealEstateInfo(String sql, Object... args){
        return baseDao.queryInt(sql, args);
    }
    
    /**
     * 分页查找一个list
     *
     * @param conditionMap 查询条件集合
     * @param pageNumber   记录行的偏移量
     * @param pageSize     记录行的最大数目
     * @return T extends BaseEntity
     */
    public PageData<RealEstateInfo> paginatorList(Map<String, Object> conditionMap, int pageNumber, int pageSize) {
        List<Object> params = new ArrayList<>();
        String sql = new SqlHelper().getSqlComplete(RealEstateInfo.class, conditionMap, params);
        List<RealEstateInfo> list = baseDao.findList(sql, pageNumber, pageSize, RealEstateInfo.class, params.toArray());
        int count = countRealEstateInfo(conditionMap);
        return new PageData<RealEstateInfo>(list, count);
    }
    
    
    public Integer countRealEstateInfo(Map<String, Object> conditionMap) {
        List<Object> params = new ArrayList<>();
        SqlConditionUtil conditionUtil = new SqlConditionUtil(getSqlCondition(conditionMap));
        conditionUtil.setSelectFields("count(*)");
        String sql = new SqlHelper().getSqlComplete(RealEstateInfo.class, conditionUtil.getMap(), params);
        return baseDao.queryInt(sql, params.toArray());
    }
    
    public Map<String, String> getSqlCondition(Map<String, Object> conditionMap) {
        HashMap<String, String> map = new HashMap<>();
        for (Map.Entry<String, Object> stringObjectEntry : conditionMap.entrySet()) {
            map.put(stringObjectEntry.getKey(), stringObjectEntry.getValue().toString());
        }
        return map;
    }

    
    
    
}
