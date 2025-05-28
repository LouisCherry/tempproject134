package com.epoint.jnzwfw.auditproject.auditprojectformsgxkz.impl;

import java.util.List;

import org.springframework.stereotype.Component;
import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.jnzwfw.auditproject.auditprojectformsgxkz.api.IAuditProjectFormSgxkzService;
import com.epoint.jnzwfw.auditproject.auditprojectformsgxkz.api.entity.AuditProjectFormSgxkz;
import com.epoint.jnzwfw.auditproject.auditprojectformsgxkzdanti.api.entity.AuditProjectFormSgxkzDanti;

/**
 * 施工许可证表单对应的后台service实现类
 * 
 * @author zhaoy
 * @version [版本号, 2019-06-02 15:13:10]
 */
@Component
@Service
public class AuditProjectFormSgxkzServiceImpl implements IAuditProjectFormSgxkzService
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditProjectFormSgxkz record) {
        return new AuditProjectFormSgxkzService().insert(record);
    }
    
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditProjectFormSgxkzDanti record) {
        return new AuditProjectFormSgxkzService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new AuditProjectFormSgxkzService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditProjectFormSgxkz record) {
        return new AuditProjectFormSgxkzService().update(record);
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
    public AuditProjectFormSgxkz find(Object primaryKey) {
        return new AuditProjectFormSgxkzService().find(primaryKey);
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
    public AuditProjectFormSgxkz find(String sql, Object... args) {
        return new AuditProjectFormSgxkzService().find(args);
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
    public List<AuditProjectFormSgxkz> findList(String sql, Object... args) {
        return new AuditProjectFormSgxkzService().findList(sql, args);
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
    public List<AuditProjectFormSgxkz> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return new AuditProjectFormSgxkzService().findList(sql, pageNumber, pageSize, args);
    }
    
    public AuditProjectFormSgxkz getRecordBy(String projectguid){
        return new AuditProjectFormSgxkzService().getRecordBy(projectguid);
    }
    
    public List<AuditProjectFormSgxkzDanti> findDantiList(int pageNumber, int pageSize, String projectguid) {
        return new AuditProjectFormSgxkzService().findDantiList(pageNumber, pageSize,projectguid);
    }
    
    public Integer queryInt(String projectguid) {
        return new AuditProjectFormSgxkzService().queryInt(projectguid);
    }
}
