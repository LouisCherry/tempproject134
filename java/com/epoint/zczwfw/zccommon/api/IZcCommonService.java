package com.epoint.zczwfw.zccommon.api;

import java.util.List;
import java.util.Map;

import com.epoint.core.BaseEntity;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;

/**
 * 
 * 通用service
 * 
 * @author yrchan
 * @version 2022年4月19日
 */
public interface IZcCommonService
{

    /**
     * 
     * 查询实体类
     * 
     * @param baseClass
     *            反射对象
     * @param fieldName
     *            字段
     * @param fieldValue
     *            值
     * @return 返回实体
     */
    public <T extends BaseEntity> T find(Class<T> baseClass, String fieldName, String fieldValue);

    /**
     * 
     * 查询实体类
     * 
     * @param baseClass
     *            反射对象
     * @param primary
     *            主键
     * @return 返回实体
     */
    public <T extends BaseEntity> T find(Class<T> baseClass, String primary);

    /**
     * 查找单个实体
     * 
     * @param baseClass
     *            11
     * @param conditionMap
     *            条件
     * @return T 返回单个实体
     */
    public <T extends BaseEntity> T find(Class<T> baseClass, Map<String, Object> conditionMap);

    /**
     * 插入
     * 
     * @param record
     *            record
     */
    public void insert(Record record);

    /**
     * 删除
     * 
     * @param record
     *            record
     * @return 删除是否成功
     */
    public Boolean delete(Record record);

    /**
     * 更新
     * 
     * @param record
     *            record
     * @return 更新是否成功
     */
    public Boolean update(Record record);

    /**
     * 查找实体列表
     * 
     * @param map
     *            条件
     * @param baseClass
     *            baseClass
     * @return 实体列表
     */
    public <T extends BaseEntity> List<T> findList(Map<String, Object> map, Class<T> baseClass);

    /**
     * 查询数量
     * 
     * @param map
     *            条件
     * @param baseClass
     *            baseClass
     * @return 返回查询数量
     */
    public <T extends BaseEntity> int queryInt(Map<String, Object> map, Class<T> baseClass);

    /**
     * 
     * 分页列表
     * 
     * @param baseClass
     *            baseClass
     * @param map
     *            条件
     * @param first
     *            页码
     * @param pageSize
     *            没有数量
     * @return PageData<T> 返回分类列表
     */
    public <T extends BaseEntity> PageData<T> paginatorList(Class<T> baseClass, Map<String, Object> map, int first,
            int pageSize);

}
