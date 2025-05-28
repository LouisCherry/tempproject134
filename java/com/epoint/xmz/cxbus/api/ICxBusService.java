package com.epoint.xmz.cxbus.api;
import java.io.Serializable;
import java.util.List;

import com.epoint.core.grammar.Record;
import com.epoint.xmz.cxbus.api.entity.CxBus;

/**
 * 车辆信息表对应的后台service接口
 * 
 * @author 1
 * @version [版本号, 2021-02-03 15:42:45]
 */
public interface ICxBusService extends Serializable
{ 
   
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(CxBus record);

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
    public int update(CxBus record);

    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public CxBus find(Object primaryKey);

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
    public CxBus find(String sql,Object... args);

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
    public List<CxBus> findList(String sql, Object... args);

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
    public List<CxBus> findList(String sql, int pageNumber, int pageSize,Object... args);

	 /**
     * 查询数量
     * 
     * @param sql
     *            执行语句
     * @param args
     *            参数
     * @return Integer
     */
	 public Integer countCxBus(String sql, Object... args);
	 
	 
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
    public CxBus getCxbusByGuid(String gcguid);
    
    
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
    public  Record getYlzmDetailByRowguid(String rowguid);
    
    
    public  Record getCsdxggDetailByRowguid(String rowguid);
    
    public  Record getYsDetailByRowguid(String rowguid);

    public Record getSlysqykysq(String projectguid);
    
    public List<Record> getSlysqykysqSon(String projectguid);
    
    public  Record getJzjnjscprdwbxtbdByRowguid(String rowguid);

    public Record getCodeItemTextByValue(String itemvalue);

	public Record getGgwsxk(String tableName, String rowguid);
    
	public Record getDzbdDetail(String tableName, String rowguid);
	
	public Record getGgwsDzbdDetail(String rowguid);
	
	public Record getFrameAttachStorage(String cliengguid);
    
	public List<Record> getApplyerListByPorjectGuid(String projectguid);

	int updateEformByRowguid(String tablename, String rowguid, String sbryxx, String jsryxx, String zzzylbdb,
			String isqualiy);

	Record getJzqyzzDetailByRowguid(String tablename, String rowguid);

	public List<Record> getJnJzqyzzzyByZzlb(String zzlb);

	String getSqlTableNameByFormId(String formId);

	
}
