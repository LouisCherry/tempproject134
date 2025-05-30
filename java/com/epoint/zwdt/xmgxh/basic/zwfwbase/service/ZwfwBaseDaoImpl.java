package com.epoint.zwdt.xmgxh.basic.zwfwbase.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.core.BaseEntity;
import com.epoint.core.grammar.Record;
import com.epoint.zwdt.xmgxh.basic.zwfwbase.api.IZwfwBasoDao;

@Service
@Component
public class ZwfwBaseDaoImpl implements IZwfwBasoDao
{

    /**
     * 
     */
    private static final long serialVersionUID = -8306655554967622693L;

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    @Override
    public <T extends Record> int insert(T record) {
        return new ZwfwBaseDaoService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param clazz
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    @Override
    public <T extends Record> int deleteByGuid(String guid, Class<? extends BaseEntity> clazz) {
        return new ZwfwBaseDaoService().deleteByGuid(guid, clazz);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    @Override
    public <T extends Record> int update(T record) {
        return new ZwfwBaseDaoService().update(record);
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
    @Override
    public <T> T find(Class<? extends BaseEntity> clazz, Object primaryKey) {
        return new ZwfwBaseDaoService().find(clazz, primaryKey);
    }

    /**
     * 查找单条记录
     * 
     * @param sql
     *            查询语句
     * @param clazz
     *            可以是[Record.class(弱类型);FrameOu.class(强类型);Object[].class，返回一个数组 ;String.class;Integer.class;Long.class]
     * @param args
     *            参数值数组
     * @return T {String、Integer、Long、Record、FrameOu、Object[]等}
     */
    @Override
    public <T> T find(String sql, Class<T> clazz, Object... args) {
        return new ZwfwBaseDaoService().find(sql, clazz, args);
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
    @Override
    public <T> List<T> findList(String sql, Class<T> clazz, Object... args) {
        return new ZwfwBaseDaoService().findList(sql, clazz, args);
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
    @Override
    public <T> List<T> findList(String sql, int pageNumber, int pageSize, Class<T> clazz, Object... args) {
        return new ZwfwBaseDaoService().findList(sql, pageNumber, pageSize, clazz, args);
    }

    @Override
    public <T> T findByField(String tablename, Class<T> clazz, List<String> conditionList, Object... args) {
        return new ZwfwBaseDaoService().find(tablename, clazz, conditionList, args);
    }

    @Override
    public int queryInt(String sql, Object... args) {
        return new ZwfwBaseDaoService().queryInt(sql, args);
    }

    @Override
    public String queryString(String sql, Object... args) {
        return new ZwfwBaseDaoService().queryString(sql, args);
    }

    @Override
    public int execute(String sql, Object... args) {
        return new ZwfwBaseDaoService().execute(sql, args);
    }

    @Override
    public Object executeProcudureWithResult(int var1, int var2, String var3, Object... var4) {
        return new ZwfwBaseDaoService().executeProcudureWithResult(var1, var2, var3, var4);
    }

    @Override
    public List<Record> executeProcudure(String var1, Object... var2) {
        return new ZwfwBaseDaoService().executeProcudure(var1, var2);
    }

    @Override
    public <T extends Record> int delete(T var1) {
        return new ZwfwBaseDaoService().delete(var1);
    }

    @Override
    public void beginTransaction() {
        new ZwfwBaseDaoService().beginTransaction();
    }

    @Override
    public void rollBackTransaction() {
        new ZwfwBaseDaoService().rollBackTransaction();
    }

    @Override
    public void close() {
        new ZwfwBaseDaoService().close();
    }

    @Override
    public void commitTransaction() {
        new ZwfwBaseDaoService().commitTransaction();
    }

}
