package com.epoint.auditqueueqhjmoduletasktype.impl;

import java.util.List;

import com.epoint.auditqueueqhjmoduletasktype.api.entity.AuditQueueQhjmoduleTasktype;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;

/**
 * 取号机大厅模块关联事项类别配置表对应的后台service
 * 
 * @author Epoint
 * @version [版本号, 2024-11-15 09:20:43]
 */
public class AuditQueueQhjmoduleTasktypeService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public AuditQueueQhjmoduleTasktypeService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditQueueQhjmoduleTasktype record) {
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
        T t = baseDao.find(AuditQueueQhjmoduleTasktype.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> void deleteAllByGuid(String guid) {
        List<AuditQueueQhjmoduleTasktype> list = baseDao.findList(
                "select * from audit_queue_qhjmodule_tasktype where qhjmoduleguid = ?",
                AuditQueueQhjmoduleTasktype.class, guid);
        for (AuditQueueQhjmoduleTasktype auditQueueQhjmoduleTasktype : list) {
            baseDao.delete(auditQueueQhjmoduleTasktype);
        }
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditQueueQhjmoduleTasktype record) {
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
    public AuditQueueQhjmoduleTasktype find(Object primaryKey) {
        return baseDao.find(AuditQueueQhjmoduleTasktype.class, primaryKey);
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
        return baseDao.find(sql, AuditQueueQhjmoduleTasktype.class, args);
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
        return baseDao.findList(sql, AuditQueueQhjmoduleTasktype.class, args);
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
        return baseDao.findList(sql, pageNumber, pageSize, AuditQueueQhjmoduleTasktype.class, args);
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
    public Integer countAuditQueueQhjmoduleTasktype(String sql, Object... args) {
        return baseDao.queryInt(sql, args);
    }
}
