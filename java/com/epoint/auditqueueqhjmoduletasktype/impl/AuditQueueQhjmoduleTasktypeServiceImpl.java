package com.epoint.auditqueueqhjmoduletasktype.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.auditqueueqhjmoduletasktype.api.IAuditQueueQhjmoduleTasktypeService;
import com.epoint.auditqueueqhjmoduletasktype.api.entity.AuditQueueQhjmoduleTasktype;

/**
 * 取号机大厅模块关联事项类别配置表对应的后台service实现类
 * 
 * @author Epoint
 * @version [版本号, 2024-11-15 09:20:43]
 */
@Component
@Service
public class AuditQueueQhjmoduleTasktypeServiceImpl implements IAuditQueueQhjmoduleTasktypeService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditQueueQhjmoduleTasktype record) {
        return new AuditQueueQhjmoduleTasktypeService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new AuditQueueQhjmoduleTasktypeService().deleteByGuid(guid);
    }

    /**
     * 删除全部关联数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public void deleteAllByGuid(String guid) {
        new AuditQueueQhjmoduleTasktypeService().deleteAllByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditQueueQhjmoduleTasktype record) {
        return new AuditQueueQhjmoduleTasktypeService().update(record);
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
    public AuditQueueQhjmoduleTasktype find(Object primaryKey) {
        return new AuditQueueQhjmoduleTasktypeService().find(primaryKey);
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
    public AuditQueueQhjmoduleTasktype find(String sql, Object... args) {
        return new AuditQueueQhjmoduleTasktypeService().find(sql, args);
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
    public List<AuditQueueQhjmoduleTasktype> findList(String sql, Object... args) {
        return new AuditQueueQhjmoduleTasktypeService().findList(sql, args);
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
    public List<AuditQueueQhjmoduleTasktype> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new AuditQueueQhjmoduleTasktypeService().findList(sql, pageNumber, pageSize, args);
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
    @Override
    public Integer countAuditQueueQhjmoduleTasktype(String sql, Object... args) {
        return new AuditQueueQhjmoduleTasktypeService().countAuditQueueQhjmoduleTasktype(sql, args);
    }

}
