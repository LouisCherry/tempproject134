package com.epoint.basic.auditonlineuser.inter;

import java.util.List;
import java.util.Map;

import com.epoint.core.BaseEntity;
import com.epoint.core.grammar.Record;

public interface IAuditDaibanUser<T>
{

    /**
     * 获取所有记录
     * @param baseClass 基础类型
     * @return
     */
    public List<T> getAllRecord(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap);

    /**
     * 获取所有记录的数量
     * @param baseClass
     * @param conditionMap
     * @return
     */
    public Integer getAllRecordCount(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap);

    /**
     * 新增某条记录
     * @param t数据对象
     */
    public void addRecord(Class<? extends BaseEntity> baseClass, Record record);

    /**
     * 按分页获取记录
     * @param baseClass 基础类型
     * @param conditionMap 条件集合
     * @param first 页码
     * @param pageSize 每页数量
     * @param sortField 排序字段
     * @param sortOrder
     * @return
     */
    public List<T> getAllRecordByPage(Class<? extends BaseEntity> baseClass, Map<String, String> conditionMap,
            Integer first, Integer pageSize, String sortField, String sortOrder);

    /**
     * 更新某条记录
     * @param t数据对象
     */
    public void updateRecord(Class<? extends BaseEntity> baseClass, Record record, String key);
    
    /**
     * 更新某条记录
     * @param baseClass
     * @param rowGuid
     * @param key
     * @param value
     */
    public void updateRecord(Class<? extends BaseEntity> baseClass, String rowGuid, String key, String value);

    /**
     * 删除某条记录
     * @param t数据对象
     */
    public void deleteRecod(Class<? extends BaseEntity> baseClass, Record record, String key);

    /**
     * 删除一批数据
     * @param baseClass
     * @param keyValue
     * @param key
     */
    public void deleteRecords(Class<? extends BaseEntity> baseClass, String keyValue, String key);

    /**
     *根据唯一标识详情
     * @param rowGuid
     * @return
     */
    public T getDetail(Class<? extends BaseEntity> baseClass, String rowGuid, String key);
}
