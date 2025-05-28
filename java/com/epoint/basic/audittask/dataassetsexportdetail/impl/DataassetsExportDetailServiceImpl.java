package com.epoint.basic.audittask.dataassetsexportdetail.impl;
import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.audittask.dataassetsexportdetail.api.IDataassetsExportDetailService;
import com.epoint.basic.audittask.dataassetsexportdetail.api.entity.DataassetsExportDetail;
import com.epoint.core.grammar.Record;
/**
 * 导出详情对应的后台service实现类
 * 
 * @author 95453
 * @version [版本号, 2020-08-24 17:27:29]
 */
@Component
@Service
public class DataassetsExportDetailServiceImpl implements IDataassetsExportDetailService
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(DataassetsExportDetail record) {
        return new DataassetsExportDetailService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new DataassetsExportDetailService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(DataassetsExportDetail record) {
        return new DataassetsExportDetailService().update(record);
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
    public DataassetsExportDetail find(Object primaryKey) {
       return new DataassetsExportDetailService().find(primaryKey);
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
    public DataassetsExportDetail find(String sql, Object... args) {
        return new DataassetsExportDetailService().find(sql,args);
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
    public List<DataassetsExportDetail> findList(String sql, Object... args) {
       return new DataassetsExportDetailService().findList(sql,args);
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
    public List<DataassetsExportDetail> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new DataassetsExportDetailService().findList(sql,pageNumber,pageSize,args);
    }
    
     /**
     * 查询数量
     * 
     * @param sql
     *            执行语句
     * @param args
     *            参数
     * @return Integer
     */
     @Override
    public Integer countDataassetsExportDetail(String sql, Object... args){
        return new DataassetsExportDetailService().countDataassetsExportDetail(sql, args);
    }

    @Override
    public List<DataassetsExportDetail> findListByExportguid(String exportguid) {
        return new DataassetsExportDetailService().findListByExportguid(exportguid);
    }

	@Override
	public Record getEvaDetail(String codeid) {
		
		 return new DataassetsExportDetailService().getEvaDetail(codeid);
	}

}
