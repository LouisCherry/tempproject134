package com.epoint.jnzwfw.auditorga.auditorgausercondition.impl;
import java.util.List;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.dao.CommonDao;
import com.epoint.jnzwfw.auditorga.auditorgausercondition.api.entity.AuditOrgaUsercondition;

/**
 * 人员在岗信息表对应的后台service
 * 
 * @author zhaoy
 * @version [版本号, 2019-05-04 17:10:14]
 */
public class AuditOrgaUserconditionService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public AuditOrgaUserconditionService() {
        baseDao = CommonDao.getInstance();
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(AuditOrgaUsercondition record) {
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
        T t = baseDao.find(AuditOrgaUsercondition.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(AuditOrgaUsercondition record) {
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
    public AuditOrgaUsercondition find(Object primaryKey) {
        return baseDao.find(AuditOrgaUsercondition.class, primaryKey);
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
    public AuditOrgaUsercondition find(String sql,  Object... args) {
        return baseDao.find(sql, AuditOrgaUsercondition.class, args);
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
    public List<AuditOrgaUsercondition> findList(String sql, Object... args) {
        return baseDao.findList(sql, AuditOrgaUsercondition.class, args);
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
    public List<AuditOrgaUsercondition> findList(String sql, int pageNumber, int pageSize, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, AuditOrgaUsercondition.class, args);
    }

}
