package com.epoint.jn.externalprojectinfoext.api;

import java.io.Serializable;
import java.util.List;

import com.epoint.core.grammar.Record;
import com.epoint.jn.externalprojectinfoext.api.entity.ExternalProjectInfoExt;

/**
 * 外部办件基本扩展信息表对应的后台service接口
 * 
 * @author wannengDB
 * @version [版本号, 2022-01-06 14:54:57]
 */
public interface IExternalProjectInfoExtService extends Serializable
{

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(ExternalProjectInfoExt record);

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
    public int update(ExternalProjectInfoExt record);

    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public ExternalProjectInfoExt find(Object primaryKey);

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
    public ExternalProjectInfoExt find(String sql, Object... args);

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
    public List<ExternalProjectInfoExt> findList(String sql, Object... args);

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
    public List<ExternalProjectInfoExt> findList(String sql, int pageNumber, int pageSize, Object... args);

    /**
     * 查询数量
     * 
     * @param sql
     *            执行语句
     * @param args
     *            参数
     * @return Integer
     */
    public Integer countExternalProjectInfoExt(String sql, Object... args);

    /**
     * 
     * [分页查询列表数据]
     * 
     * @param first
     * @param pagesize
     * @param ouguid
     * @param areacode
     * @return
     */
    public List<Record> finList(int first, int pagesize, String ouguid, String areacode,int month);

    /**
     * 
     * [分页查询列表总数]
     * 
     * @param ouguid
     * @param areacode
     * @return
     */
    public Integer finTotal(String ouguid, String areacode);

    /**
     *
     * [分页查询列表数据]
     *
     * @param first
     * @param pagesize
     * @param ouguid
     * @param areacode
     * @return
     */
    public List<Record> finList(int first, int pagesize, String ouguid, String areacode,String projectno);

    /**
     *
     * [分页查询列表总数]
     *
     * @param ouguid
     * @param areacode
     * @return
     */
    public Integer finTotal(String ouguid, String areacode,String projectno);
}
