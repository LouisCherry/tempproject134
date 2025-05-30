package com.epoint.zwdt.xmgxh.basic.zwfwbase.api;

import java.io.Serializable;
import java.util.List;

import com.epoint.core.BaseEntity;
import com.epoint.core.grammar.Record;

/**
 * 
 * @Description :政务服务操作数据库通用方法
 *  
 * @author male
 * @version [版本号, 2019年1月22日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface IZwfwBasoDao extends Serializable
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int insert(T record);

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid, Class<? extends BaseEntity> clazz);

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int update(T record);

    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public <T> T find(Class<? extends BaseEntity> clazz, Object primaryKey);

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
    public <T> T find(String sql, Class<T> clazz, Object... args);

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
    public <T> List<T> findList(String sql, Class<T> clazz, Object... args);

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
    public <T> List<T> findList(String sql, int pageNumber, int pageSize, Class<T> clazz, Object... args);

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
    public <T> T findByField(String tablename, Class<T> clazz, List<String> conditionList, Object... args);

    public int queryInt(String sql, Object... args);

    public String queryString(String sql, Object... args);

    public int execute(String var1, Object... var2);

    public Object executeProcudureWithResult(int var1, int var2, String var3, Object... var4);

    public List<Record> executeProcudure(String var1, Object... var2);

    public <T extends Record> int delete(T var1);

    void beginTransaction();

    void rollBackTransaction();

    void close();

    void commitTransaction();

}
