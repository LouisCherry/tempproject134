package com.epoint.auditproject.auditdoc.dao;

import com.epoint.auditproject.auditdoc.entity.AuditProjectDocsnapHistroy;

import java.util.List;
import java.util.Map;

import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.cs.auditepidemiclog.api.entity.AuditEpidemicLog;
import com.epoint.database.peisistence.crud.impl.model.PageData;


/**
 * (AuditProjectDocsnapHistroy)表数据库访问层
 *
 * @author panshunxing
 * @since 2024-12-25 19:40:28
 */
public class AuditProjectDocsnapHistroyDao {
    private final ICommonDao baseDao;

    public AuditProjectDocsnapHistroyDao() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 新增数据
     *
     * @param auditProjectDocsnapHistroy 实例对象
     * @return 影响行数
     */
    public int insert(AuditProjectDocsnapHistroy auditProjectDocsnapHistroy) {
        return baseDao.insert(auditProjectDocsnapHistroy);
    }

    /**
     * 通过主键删除数据
     *
     * @param rowguid 主键
     * @return 影响行数
     */
    public <T extends Record> int deleteById(String rowguid) {
        T t = baseDao.find(AuditProjectDocsnapHistroy.class, rowguid);
        return baseDao.delete(t);
    }

    /**
     * 修改数据
     *
     * @param auditProjectDocsnapHistroy 实例对象
     * @return 影响行数
     */
    public int update(AuditProjectDocsnapHistroy auditProjectDocsnapHistroy) {
        return baseDao.update(auditProjectDocsnapHistroy);
    }

    /**
     * 通过ID查询单条数据
     *
     * @param rowguid 主键
     * @return 实例对象
     */
    public AuditProjectDocsnapHistroy queryById(String rowguid) {
        return baseDao.find(AuditProjectDocsnapHistroy.class, rowguid);
    }

    /**
     * 查找单条记录
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组
     *              ;String.class;Integer.class;Long.class]
     * @param args  参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    public AuditProjectDocsnapHistroy find(String sql, Object... args) {
        return baseDao.find(sql, AuditProjectDocsnapHistroy.class, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public List<AuditProjectDocsnapHistroy> findList(String sql, Object... args) {
        return baseDao.findList(sql, AuditProjectDocsnapHistroy.class, args);
    }

    /**
     * 通过条件执行语句
     *
     * @param sql  执行的sql语句
     * @param args 参数值数组
     * @return 影响行数
     */
    public <T extends Record> int execute(String sql, Object... args) {
        return baseDao.execute(sql, args);
    }

    /**
     * 查找一个list
     *
     * @param sql   查询语句
     * @param clazz 可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class]
     * @param args  参数值数组
     * @return T extends BaseEntity
     */
    public PageData<AuditProjectDocsnapHistroy> getListPage(Map<String, String> conditionMap, int first, int pageSize, String sortField, String sortOrder)  {
        return new SQLManageUtil().getDbListByPage(AuditProjectDocsnapHistroy.class, conditionMap, first, pageSize, sortField, sortOrder);
    }
}

