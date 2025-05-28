package com.epoint.basic.audittask.dataassetsexportdetail.api;

import java.io.Serializable;
import java.util.List;

import com.epoint.basic.audittask.dataassetsexportdetail.api.entity.DataassetsExportDetail;
import com.epoint.core.grammar.Record;

/**
 * 导出详情对应的后台service接口
 * 
 * @author 95453
 * @version [版本号, 2020-08-24 17:27:29]
 */
public interface IDataassetsExportDetailService extends Serializable
{

	public Record getEvaDetail(String codeid);
	
	/**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(DataassetsExportDetail record);

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
    public int update(DataassetsExportDetail record);

    /**
     * 根据ID查找单个实体
     * 
     * @param clazz
     *            类<必须继承BaseEntity>
     * @param primaryKey
     *            主键
     * @return T extends BaseEntity
     */
    public DataassetsExportDetail find(Object primaryKey);

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
    public DataassetsExportDetail find(String sql, Object... args);

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
    public List<DataassetsExportDetail> findList(String sql, Object... args);

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
    public List<DataassetsExportDetail> findList(String sql, int pageNumber, int pageSize, Object... args);

    /**
    * 查询数量
    * 
    * @param sql
    *            执行语句
    * @param args
    *            参数
    * @return Integer
    */
    public Integer countDataassetsExportDetail(String sql, Object... args);

    /**
     *  [通过导出标志，查询导出详情列表]
     *  [功能详细描述]
     *  @param exportguid 导出记录唯一标识
     *  @return    
     */
    public List<DataassetsExportDetail> findListByExportguid(String exportguid);
}
