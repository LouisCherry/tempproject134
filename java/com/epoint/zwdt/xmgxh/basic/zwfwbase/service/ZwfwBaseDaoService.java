package com.epoint.zwdt.xmgxh.basic.zwfwbase.service;

import java.util.List;

import com.epoint.core.BaseEntity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;

public class ZwfwBaseDaoService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public ZwfwBaseDaoService() {
        baseDao = CommonDao.getInstance();
    }

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int insert(T record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     * 
     * @param clazz
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid, Class<? extends BaseEntity> clazz) {
        T t = baseDao.find(clazz, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int update(T record) {
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
    public <T> T find(Class<? extends BaseEntity> clazz, Object primaryKey) {
        return baseDao.find(clazz, primaryKey);
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
    public <T> T find(String sql, Class<T> clazz, Object... args) {
        return baseDao.find(sql, clazz, args);
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
    public <T> List<T> findList(String sql, Class<T> clazz, Object... args) {
        return baseDao.findList(sql, clazz, args);
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
    public <T> List<T> findList(String sql, int pageNumber, int pageSize, Class<T> clazz, Object... args) {
        return baseDao.findList(sql, pageNumber, pageSize, clazz, args);
    }

    public <T> T findByField(String tablename, Class<T> clazz, List<String> conditionList, Object... args) {
        StringBuilder condition = new StringBuilder();
        for (String string : conditionList) {
            condition.append(string).append("=?");
        }

        return baseDao.find("select * from " + tablename + " where " + condition, clazz, args);
    }

    public int queryInt(String sql, Object... args) {
        return baseDao.queryInt(sql, args);
    }

    public String queryString(String sql, Object... args) {
        return baseDao.queryString(sql, args);
    }

    public int execute(String sql, Object... args) {
        return baseDao.execute(sql, args);
    }

    public Object executeProcudureWithResult(int var1, int var2, String var3, Object... var4) {
        return baseDao.executeProcudureWithResult(var1, var2, var3, var4);
    }

    public List<Record> executeProcudure(String var1, Object... var2) {
        return baseDao.executeProcudure(var1, var2);

    }

    public <T extends Record> int delete(T var1) {
        return baseDao.delete(var1);
    }

    public void beginTransaction() {
        baseDao.beginTransaction();
    }

    public void rollBackTransaction() {
        baseDao.rollBackTransaction();
    }

    public void close() {
        baseDao.close();
    }

    public void commitTransaction() {
        baseDao.commitTransaction();
    }

}
