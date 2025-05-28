package com.epoint.auditspparalleltask.impl;

import com.epoint.auditspparalleltask.api.entity.AuditSpParallelTask;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;

import java.util.List;
import java.util.Map;

/**
 * 并行阶段阶段事项配置表对应的后台service
 * 
 * @author lzhming
 * @version [版本号, 2023-03-17 09:18:40]
 */
public class AuditSpParallelTaskService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public AuditSpParallelTaskService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditSpParallelTask record) {
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
        T t = baseDao.find(AuditSpParallelTask.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditSpParallelTask record) {
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
    public AuditSpParallelTask find(Object primaryKey) {
        return baseDao.find(AuditSpParallelTask.class, primaryKey);
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
    public AuditSpParallelTask find(String sql, Object... args) {
        return baseDao.find(sql, AuditSpParallelTask.class, args);
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
    public List<AuditSpParallelTask> findList(String sql, Object... args) {
        return baseDao.findList(sql, AuditSpParallelTask.class, args);
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
    public List<AuditSpParallelTask> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, AuditSpParallelTask.class, args);
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
    public Integer countAuditSpParallelTask(String sql, Object... args) {
        return baseDao.queryInt(sql, args);
    }
    
    /**
     * 
     * 获取并行阶段事项集合
     * 
     * @param conditionMap
     *            查询条件
     * @return list结果
     */
    public List<AuditSpParallelTask> getAuditSpParallelTaskListByCondition(Map<String, String> conditionMap) {
        SQLManageUtil sqlManageUtil = new SQLManageUtil();
        String allSql = sqlManageUtil.buildSqlComoplete(AuditSpParallelTask.class, conditionMap);       
        return baseDao.findList(allSql, AuditSpParallelTask.class);
    }
}
