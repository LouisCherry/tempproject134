package com.epoint.jnzwfw.projectdocandcert.api;

import java.io.Serializable;
import java.util.List;


import com.epoint.core.grammar.Record;
import com.epoint.jnzwfw.projectdocandcert.api.entity.Projectdocandcert;

/**
 * 办件通知书及电子证照调用对应的后台service接口
 * 
 * @author shibin
 * @version [版本号, 2019-07-23 14:21:12]
 */
public interface IProjectdocandcertService extends Serializable
{

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(Projectdocandcert record);

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid);

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(Projectdocandcert record);

    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public Projectdocandcert find(Object primaryKey);

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
    public Projectdocandcert find(String sql, Object... args);

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
    public List<Projectdocandcert> findList(String sql, Object... args);

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
    public List<Projectdocandcert> findList(String sql, int pageNumber, int pageSize, Object... args);

    /**
     *  根据办件guid获取信息
     *  @param projectGuid
     *  @return
     */
    public Projectdocandcert getInfoByProjectGuid(String projectGuid);

    /**
     *  根据办件guid获取窗口、事项信息
     *  @param projectGuid
     *  @return
     */
    public Record getProjectAndTaskinfo(String projectGuid);

}
