package com.epoint.jiningzwfw.epointsphmzc.api;
import java.io.Serializable;
import java.util.List;

import com.epoint.core.grammar.Record;
import com.epoint.jiningzwfw.epointsphmzc.api.entity.EpointSpHmzc;

/**
 * 惠企政策库对应的后台service接口
 * 
 * @author 86180
 * @version [版本号, 2019-10-08 23:39:45]
 */
public interface IEpointSpHmzcService extends Serializable
{ 
   
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(EpointSpHmzc record);

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
    public int update(EpointSpHmzc record);

    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public EpointSpHmzc find(Object primaryKey);

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
    public EpointSpHmzc find(String sql,Object... args);

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
    public List<EpointSpHmzc> findList(String sql, Object... args);

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
    public List<EpointSpHmzc> findList(String sql, int pageNumber, int pageSize,Object... args);
    
    public List<EpointSpHmzc> findList(int pageNumber, int pageSize, String qybq, String jnhygm,  String wwsmzq, String zcmc, String sfsxqy, String ssbm, String ouguids);
    
    public int findListCount( String qybq, String jnhygm,  String wwsmzq, String zcmc, String sfsxqy, String ssbm, String ouguids);

    public List<Record> getOuListByHmzc(String areacode);
    
    public List<Record> getOuList(String areacode);
}
