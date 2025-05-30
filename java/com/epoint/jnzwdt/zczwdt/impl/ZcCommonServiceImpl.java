package com.epoint.jnzwdt.zczwdt.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.epoint.core.BaseEntity;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.jnzwdt.zczwdt.api.IZcCommonService;

/**
 * 
 * 通用impl
 * 
 * @author yrchan
 * @version 2022年4月19日
 */
@Component
@Service
public class ZcCommonServiceImpl implements IZcCommonService
{
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
    @Override
    public <T extends BaseEntity> T find(Class<T> baseClass, String primary) {
        return new ZcCommonService(baseClass).find(baseClass, primary);
    }

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
    @Override
    public <T extends BaseEntity> T find(Class<T> baseClass, String fieldName, String fieldValue) {
        return new ZcCommonService(baseClass).find(baseClass, fieldName, fieldValue);
    }

    /**
     * 查找单个实体
     * 
     * @param baseClass
     *            11
     * @param conditionMap
     *            条件
     * @return T 返回单个实体
     */
    @Override
    public <T extends BaseEntity> T find(Class<T> baseClass, Map<String, Object> conditionMap) {
        return new ZcCommonService(baseClass).find(baseClass, conditionMap);
    }

    /**
     * 
     * 数据插入
     * 
     * @param record
     */
    @Override
    public void insert(Record record) {
        new ZcCommonService().insert(record);
    }

    /**
     * 删除
     *
     * @param record
     *            record
     * @return 删除是否成功
     */
    @Override
    public Boolean delete(Record record) {
        return new ZcCommonService().delete(record);
    }

    /**
     * 更新
     *
     * @param record
     *            record
     * @return 更新是否成功
     */
    @Override
    public Boolean update(Record record) {
        return new ZcCommonService().update(record);
    }

    /**
     * 查找实体列表
     * 
     * @param map
     *            条件
     * @param baseClass
     *            baseClass
     * @return 实体列表
     */
    @Override
    public <T extends BaseEntity> List<T> findList(Map<String, Object> map, Class<T> baseClass) {
        return new ZcCommonService().findList(map, baseClass);
    }

    /**
     * 查询数量
     * 
     * @param map
     *            条件
     * @param baseClass
     *            baseClass
     * @return 返回查询数量
     */
    @Override
    public <T extends BaseEntity> int queryInt(Map<String, Object> map, Class<T> baseClass) {
        return new ZcCommonService().queryInt(map, baseClass);
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
    @Override
    public <T extends BaseEntity> PageData<T> paginatorList(Class<T> baseClass, Map<String, Object> map, int first,
            int pageSize) {
        return new ZcCommonService().paginatorList(baseClass, map, first, pageSize);
    }

}
