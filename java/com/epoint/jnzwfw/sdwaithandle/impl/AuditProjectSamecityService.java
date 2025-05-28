package com.epoint.jnzwfw.sdwaithandle.impl;

import java.util.List;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.jnzwfw.sdwaithandle.api.entity.AuditProjectSamecity;

/**
 * 同城通办信息表对应的后台service
 * 
 * @author 17614
 * @version [版本号, 2019-05-20 17:30:46]
 */
public class AuditProjectSamecityService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public AuditProjectSamecityService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditProjectSamecity record) {
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
        T t = baseDao.find(AuditProjectSamecity.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditProjectSamecity record) {
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
    public AuditProjectSamecity find(Object primaryKey) {
        return baseDao.find(AuditProjectSamecity.class, primaryKey);
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
    public AuditProjectSamecity find(String sql, Object... args) {
        return baseDao.find(sql, AuditProjectSamecity.class, args);
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
    public List<AuditProjectSamecity> findList(String sql, Object... args) {
        return baseDao.findList(sql, AuditProjectSamecity.class, args);
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
    public List<AuditProjectSamecity> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, AuditProjectSamecity.class, args);
    }

    /**
     * 根据办件标识查询同城通办信息
     * 
     * @param projectguid
     *            办件标识
     * @return 
     */
    public AuditProjectSamecity getProjectSamecityByProjectguid(String projectguid) {
        String sql = "select * from Audit_Project_SameCity where ProjectGuid=? ";
        return baseDao.find(sql, AuditProjectSamecity.class, projectguid);
    }

}
