package com.epoint.evainstanceck.api;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.epoint.core.grammar.Record;
import com.epoint.evainstanceck.api.entity.EvainstanceCk;

/**
 * 好差评信息表对应的后台service接口
 * 
 * @author 31220
 * @version [版本号, 2023-11-06 11:18:19]
 */
public interface IEvainstanceCkService extends Serializable
{

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(EvainstanceCk record);

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
    public int update(EvainstanceCk record);

    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public EvainstanceCk find(Object primaryKey);

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
    public EvainstanceCk find(String sql, Object... args);

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
    public List<EvainstanceCk> findList(String sql, Object... args);

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
    public List<EvainstanceCk> findList(String sql, int pageNumber, int pageSize, Object... args);

    /**
     * 查询数量
     * 
     * @param sql
     *            执行语句
     * @param args
     *            参数
     * @return Integer
     */
    public Integer countEvainstanceCk(String sql, Object... args);

    /**
     * 
     * 根据ouguid获取对应的好差评数据
     * 
     * @param leftTreeNodeGuid
     * @param first
     * @param pageSize
     * @return
     */
    public List<Record> findListByouguidTemp(String leftTreeNodeGuid, Date createdate, String evalevel, String iszg,
            String RECEIVEUSERNAM, int first, int pageSize);

    /**
     *
     * 获取办件对应的好差评数据
     *
     * @param flowsn
     * @return
     */
    public Record findByflowsn(String flowsn);

    /**
     * 
     * 根据ouguid获取对应的好差评数量
     * 
     * @param leftTreeNodeGuid
     * @param first
     * @param pageSize
     * @return
     */
    public int countListByouguidTemp(String leftTreeNodeGuid, Date createdate, String evalevel, String iszg,
            String RECEIVEUSERNAM);
}
