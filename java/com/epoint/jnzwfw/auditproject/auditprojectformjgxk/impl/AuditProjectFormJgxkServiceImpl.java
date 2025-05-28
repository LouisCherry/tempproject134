package com.epoint.jnzwfw.auditproject.auditprojectformjgxk.impl;
import java.util.List;

import org.springframework.stereotype.Component;
import com.epoint.jnzwfw.auditproject.auditprojectformjgxk.api.entity.AuditProjectFormJgxk;
import com.epoint.jnzwfw.auditproject.auditprojectformsgxkz.api.entity.AuditProjectFormSgxkz;
import com.epoint.jnzwfw.auditproject.auditprojectformsgxkz.impl.AuditProjectFormSgxkzService;
import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.jnzwfw.auditproject.auditprojectformjgxk.api.IAuditProjectFormJgxkService;
/**
 * 竣工信息表对应的后台service实现类
 * 
 * @author 86180
 * @version [版本号, 2019-07-08 15:07:59]
 */
@Component
@Service
public class AuditProjectFormJgxkServiceImpl implements IAuditProjectFormJgxkService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditProjectFormJgxk record) {
        return new AuditProjectFormJgxkService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new AuditProjectFormJgxkService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditProjectFormJgxk record) {
        return new AuditProjectFormJgxkService().update(record);
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
    public AuditProjectFormJgxk find(Object primaryKey) {
       return new AuditProjectFormJgxkService().find(primaryKey);
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
    public AuditProjectFormJgxk find(String sql, Object... args) {
        return new AuditProjectFormJgxkService().find(args);
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
    public List<AuditProjectFormJgxk> findList(String sql, Object... args) {
       return new AuditProjectFormJgxkService().findList(sql,args);
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
    public List<AuditProjectFormJgxk> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new AuditProjectFormJgxkService().findList(sql,pageNumber,pageSize,args);
    }
    
    public AuditProjectFormJgxk getRecordBy(String projectguid){
        return new AuditProjectFormJgxkService().getRecordBy(projectguid);
    }

}
