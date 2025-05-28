package com.epoint.jnzwfw.auditproject.auditprojectformsgxkzdanti.impl;
import java.util.List;

import org.springframework.stereotype.Component;
import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.jnzwfw.auditproject.auditprojectformsgxkzdanti.api.IAuditProjectFormSgxkzDantiService;
import com.epoint.jnzwfw.auditproject.auditprojectformsgxkzdanti.api.entity.AuditProjectFormSgxkzDanti;
/**
 * 施工许可单体记录表对应的后台service实现类
 * 
 * @author zhaoy
 * @version [版本号, 2019-06-02 22:59:17]
 */
@Component
@Service
public class AuditProjectFormSgxkzDantiServiceImpl implements IAuditProjectFormSgxkzDantiService
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
    public int insert(AuditProjectFormSgxkzDanti record) {
        return new AuditProjectFormSgxkzDantiService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new AuditProjectFormSgxkzDantiService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditProjectFormSgxkzDanti record) {
        return new AuditProjectFormSgxkzDantiService().update(record);
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
    public AuditProjectFormSgxkzDanti find(Object primaryKey) {
       return new AuditProjectFormSgxkzDantiService().find(primaryKey);
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
    public AuditProjectFormSgxkzDanti find(String sql, Object... args) {
        return new AuditProjectFormSgxkzDantiService().find(args);
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
    public List<AuditProjectFormSgxkzDanti> findList(String sql, Object... args) {
       return new AuditProjectFormSgxkzDantiService().findList(sql,args);
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
    public List<AuditProjectFormSgxkzDanti> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new AuditProjectFormSgxkzDantiService().findList(sql,pageNumber,pageSize,args);
    }

}
