package com.epoint.auditproject.auditdoc.service;

import com.epoint.auditproject.auditdoc.entity.AuditProjectDocsnapHistroy;

import java.util.List;
import java.util.Map;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.cs.auditepidemiclog.api.entity.AuditEpidemicLog;
import com.epoint.database.peisistence.crud.impl.model.PageData;

/**
 * (AuditProjectDocsnapHistroy)表服务接口
 *
 * @author panshunxing
 * @since 2024-12-25 19:40:33
 */
@Service
public interface IAuditProjectDocsnapHistroyService {

    /**
     * 通过ID查询单条数据
     *
     * @param rowguid 主键
     * @return 实例对象
     */
    AuditProjectDocsnapHistroy queryById(String rowguid);

    /**
     * 新增数据
     *
     * @param auditProjectDocsnapHistroy 实例对象
     * @return 实例对象
     */
    AuditProjectDocsnapHistroy insert(AuditProjectDocsnapHistroy auditProjectDocsnapHistroy);

    /**
     * 修改数据
     *
     * @param auditProjectDocsnapHistroy 实例对象
     * @return 实例对象
     */
    int update(AuditProjectDocsnapHistroy auditProjectDocsnapHistroy);

    /**
     * 通过主键删除数据
     *
     * @param rowguid 主键
     * @return 是否成功
     */
    boolean deleteById(String rowguid);

    /**
     * 查找单条记录
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组
     *              ;String.class;Integer.class;Long.class]
     * @param args  参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    AuditProjectDocsnapHistroy find(String sql, Object... args);

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    List<AuditProjectDocsnapHistroy> findList(String sql, Object... args);


    /**
     * 通过条件执行语句
     *
     * @param sql  执行的sql语句
     * @param args 参数值数组
     * @return 影响行数
     */
    int execute(String sql, Object... args);

    
    PageData<AuditProjectDocsnapHistroy> getListPage(Map<String, String> conditionMap, int first, int pageSize,
                                                           String sortField, String sortOrder);
}
