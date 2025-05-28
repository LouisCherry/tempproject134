package com.epoint.cs.auditdmvtask.impl;
import java.util.List;

import org.springframework.stereotype.Component;
import com.epoint.cs.auditdmvtask.api.entity.AuditDmvTask;
import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.cs.auditdmvtask.api.IAuditDmvTaskService;
/**
 * 车管所事项对应的后台service实现类
 * 
 * @author admin
 * @version [版本号, 2021-03-02 11:37:44]
 */
@Component
@Service
public class AuditDmvTaskServiceImpl implements IAuditDmvTaskService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditDmvTask record) {
        return new AuditDmvTaskService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new AuditDmvTaskService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditDmvTask record) {
        return new AuditDmvTaskService().update(record);
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
    public AuditDmvTask find(Object primaryKey) {
       return new AuditDmvTaskService().find(primaryKey);
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
    public AuditDmvTask find(String sql, Object... args) {
        return new AuditDmvTaskService().find(sql,args);
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
    public List<AuditDmvTask> findList(String sql, Object... args) {
       return new AuditDmvTaskService().findList(sql,args);
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
    public List<AuditDmvTask> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new AuditDmvTaskService().findList(sql,pageNumber,pageSize,args);
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
    public Integer countAuditDmvTask(String sql, Object... args){
        return new AuditDmvTaskService().countAuditDmvTask(sql, args);
    }

}
