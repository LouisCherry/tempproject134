package com.epoint.xmz.cxbus.impl;
import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.core.grammar.Record;
import com.epoint.xmz.cxbus.api.ICxBusService;
import com.epoint.xmz.cxbus.api.entity.CxBus;
/**
 * 车辆信息表对应的后台service实现类
 * 
 * @author 1
 * @version [版本号, 2021-02-03 15:42:45]
 */
@Component
@Service
public class CxBusServiceImpl implements ICxBusService
{
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(CxBus record) {
        return new CxBusService().insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new CxBusService().deleteByGuid(guid);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(CxBus record) {
        return new CxBusService().update(record);
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
    public CxBus find(Object primaryKey) {
       return new CxBusService().find(primaryKey);
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
    public CxBus find(String sql, Object... args) {
        return new CxBusService().find(sql,args);
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
    public List<CxBus> findList(String sql, Object... args) {
       return new CxBusService().findList(sql,args);
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
    public List<CxBus> findList(String sql, int pageNumber, int pageSize, Object... args) {
       return new CxBusService().findList(sql,pageNumber,pageSize,args);
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
    public Integer countCxBus(String sql, Object... args){
        return new CxBusService().countCxBus(sql, args);
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
     public CxBus getCxbusByGuid(String gcguid) {
         return new CxBusService().getCxbusByGuid(gcguid);
     }

     
     public Record getYlzmDetailByRowguid(String rowguid) {
         return new CxBusService().getYlzmDetailByRowguid(rowguid);
     }
     
     public Record getJzjnjscprdwbxtbdByRowguid(String rowguid) {
    	 return new CxBusService().getJzjnjscprdwbxtbdByRowguid(rowguid);
     }
     
     public Record getCsdxggDetailByRowguid(String rowguid) {
    	 return new CxBusService().getCsdxggDetailByRowguid(rowguid);
     }
     
     public Record getYsDetailByRowguid(String rowguid) {
    	 return new CxBusService().getYsDetailByRowguid(rowguid);
     }

    @Override
    public Record getSlysqykysq(String projectguid) {
        return new CxBusService().getSlysqykysq(projectguid);
    }
    
    @Override
    public List<Record> getSlysqykysqSon(String projectguid) {
    	return new CxBusService().getSlysqykysqSon(projectguid);
    }
     
    @Override
    public Record getCodeItemTextByValue(String itemvalue) {
    	return new CxBusService().getCodeItemTextByValue(itemvalue);
    }

	@Override
	public Record getGgwsxk(String formid, String rowguid) {
		// TODO Auto-generated method stub
		return new CxBusService().getGgwsxk(formid,rowguid);
	}
	@Override
	public Record getDzbdDetail(String tablename, String rowguid) {
		// TODO Auto-generated method stub
		return new CxBusService().getDzbdDetail(tablename,rowguid);
	}
	@Override
	public Record getGgwsDzbdDetail(String rowguid) {
		return new CxBusService().getGgwsDzbdDetail(rowguid);
	}
	@Override
	public Record getFrameAttachStorage(String cliengguid) {
		return new CxBusService().getFrameAttachStorage(cliengguid);
	}
	
	 @Override
    public List<Record> getApplyerListByPorjectGuid(String projectguid) {
    	return new CxBusService().getApplyerListByPorjectGuid(projectguid);
    }
	 @Override
	 public int updateEformByRowguid(String tablename,String rowguid,String sbryxx,String jsryxx,String zzzylbdb,String isqualiy) {
		 return new CxBusService().updateEformByRowguid(tablename,rowguid,sbryxx,jsryxx,zzzylbdb,isqualiy);
	 }
	 
	 @Override
	public Record getJzqyzzDetailByRowguid(String tablename,String rowguid) {
		return new CxBusService().getJzqyzzDetailByRowguid(tablename,rowguid);
	}
	 
	 @Override
	 public List<Record> getJnJzqyzzzyByZzlb(String zzlb) {
		 return new CxBusService().getJnJzqyzzzyByZzlb(zzlb);
	 }

    @Override
    public String getSqlTableNameByFormId(String formId) {
        return new CxBusService().getSqlTableNameByFormId(formId);
    }


}
