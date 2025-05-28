package com.epoint.jn.externalprojectinfo.api;
import java.io.Serializable;
import java.util.List;

import com.epoint.core.grammar.Record;
import com.epoint.jn.externalprojectinfo.api.entity.ExternalProjectInfo;

/**
 * 外部办件基本信息表对应的后台service接口
 * 
 * @author wannengDB
 * @version [版本号, 2022-01-06 14:37:04]
 */
public interface IExternalProjectInfoService extends Serializable
{ 
   
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(ExternalProjectInfo record);

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
    public int update(ExternalProjectInfo record);

    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public ExternalProjectInfo find(Object primaryKey);

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
    public ExternalProjectInfo find(String sql,Object... args);

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
//    public List<ExternalProjectInfo> findList(String sql, Object... args);

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
    public List<ExternalProjectInfo> findList(String sql, int pageNumber, int pageSize,Object... args);

	 /**
     * 查询数量
     * 
     * @param sql
     *            执行语句
     * @param args
     *            参数
     * @return Integer
     */
	 public Integer countExternalProjectInfo(String sql, Object... args);
	 
	 /**
	     * 插入数据
	     * 
	     * @param record
	     *            BaseEntity或Record对象 <必须继承Record>
	     * @return int
	     */
	 public int insert(ExternalProjectInfo record,int month);

    /**
     * 根据projectguids，areacode 查询分页数据
     * @param first
     * @param pageSize
     * @param projectGuids
     * @param leftTreeNodeGuid
     * @return
     */
    List<ExternalProjectInfo> findList(int first, int pageSize, String leftTreeNodeGuid, List<String> projectGuids);

    Integer findCount(String leftTreeNodeGuid, List<String> projectGuids);
}
