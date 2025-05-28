package com.epoint.xmz.cxbus.impl;
import java.util.List;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.xmz.cxbus.api.entity.CxBus;

/**
 * 车辆信息表对应的后台service
 * 
 * @author 1
 * @version [版本号, 2021-02-03 15:42:45]
 */
public class CxBusService
{
	 private static String URL = ConfigUtil.getConfigValue("datasyncjdbc", "dzbdurl");
	 private static String NAME = ConfigUtil.getConfigValue("datasyncjdbc", "dzbdusername");
	 private static String PASSWORD = ConfigUtil.getConfigValue("datasyncjdbc", "dzbdpassword");
	 
	 /**
	     * 前置库数据源
	     */
	private DataSourceConfig dataSourceConfig = new DataSourceConfig(URL, NAME, PASSWORD);
	    
	/**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;
    /**
     * 数据增删改查组件
     */
    protected ICommonDao formbaseDao;

    public CxBusService() {
        baseDao = CommonDao.getInstance();
        formbaseDao = CommonDao.getInstance(dataSourceConfig);
    }
    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int insert(CxBus record) {
        return baseDao.insert(record);
    }

    /**
     * 删除数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public <T extends Record> int deleteByGuid(String guid) {
        T t = baseDao.find(CxBus.class, guid);
        return baseDao.delete(t);
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int update(CxBus record) {
        return baseDao.update(record);
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
        return baseDao.find(CxBus.class, primaryKey);
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
    public CxBus find(String sql,  Object... args) {
        return baseDao.find(sql, CxBus.class, args);
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
        return baseDao.findList(sql, CxBus.class, args);
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
        return baseDao.findList(sql, pageNumber, pageSize, CxBus.class, args);
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
    public Integer countCxBus(String sql, Object... args){
        return baseDao.queryInt(sql, args);
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
    	String sql = "select * from cx_bus where vehicleid = ?";
        return baseDao.find(sql, CxBus.class, gcguid);
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
    public Record getYlzmDetailByRowguid(String rowguid) {
    	String sql = "select * from YLGGSQB where rowguid = ?";
        return formbaseDao.find(sql, Record.class, rowguid);
    }
    
    public Record getCsdxggDetailByRowguid(String rowguid) {
    	String sql = "select * from HWGGSQB where rowguid = ?";
    	return formbaseDao.find(sql, Record.class, rowguid);
    }
    
    public Record getYsDetailByRowguid(String rowguid) {
    	String sql = "select * from TSQKYSWTS where rowguid = ?";
    	return formbaseDao.find(sql, Record.class, rowguid);
    }
    
    public Record getSlysqykysq(String projectguid) {
        String sql = "select * from slysqykysq a  where a.rowguid = ?";
        return formbaseDao.find(sql, Record.class, projectguid);
    }
    
    public List<Record> getSlysqykysqSon(String projectguid) {
    	String sql = "select * from ryjbxxzb b where b.connect = ?";
    	return formbaseDao.findList(sql, Record.class, projectguid);
    }
    
    public Record getJzjnjscprdwbxtbdByRowguid(String rowguid) {
    	String sql = "select * from jzjnjscprdwbxtbd where rowguid = ?";
    	return formbaseDao.find(sql, Record.class, rowguid);
    }
    
    
    public Record getCodeItemTextByValue(String itemvalue) {
    	String sql = "select * from code_items where codeid = '244' and itemvalue = ?";
    	return formbaseDao.find(sql, Record.class, itemvalue);
    }
	public Record getGgwsxk(String formid, String rowguid) {
		Record result = null;
		String tableName = "";
		String fields = "";
		if("254".equals(formid)) {
			tableName = "formtable20211213224502";
			fields = " sqdw,fddbr,dwdz,duoxklb1 xksx ";
		} else if ("274".equals(formid)) {
			tableName = "formtable20220111193910";
			fields = " sqdw,fddbr,dwdz,dxklb13 xksx ";
		}
		try {
			String sql = " select  " + fields + " from " + tableName + " where rowguid = ? ";
			result = formbaseDao.find(sql, Record.class, rowguid);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(formbaseDao != null) {
				formbaseDao.close();
			}
		}
		return result;
	}
	
	public Record getDzbdDetail(String tablename, String rowguid) {
		String sql = " select * from " + tablename + " where rowguid = ? ";
		return formbaseDao.find(sql, Record.class, rowguid);
	}
	
	public Record getGgwsDzbdDetail( String rowguid) {
		String sql = " select * from formtable20220228091113 where rowguid = ? ";
		return formbaseDao.find(sql, Record.class, rowguid);
	}
    
	public Record getFrameAttachStorage(String cliengguid) {
		String sql = "select * from frame_attachstorage where attachguid in (select attachguid from frame_attachinfo where cliengguid  = ?)";
		return formbaseDao.find(sql, Record.class, cliengguid);
	}
	
	public List<Record> getApplyerListByPorjectGuid(String projectguid) {
    	String sql = "select rowguid,xm,sbryxx,jsryxx,zzzylbdb,isqualiy from formtable20220627120002 where projectguid = ?";
    			sql += " union all select rowguid,xm,sbryxx,jsryxx,zzzylbdb,isqualiy from formtable20220629170401 where projectguid = ?";
    			sql += " union all select rowguid,xm,sbryxx,jsryxx,zzzylbdb,isqualiy from formtable20220629170950 where projectguid = ?";
    	return formbaseDao.findList(sql, Record.class, projectguid,projectguid,projectguid);
    }
	
	public int updateEformByRowguid(String tablename,String rowguid,String sbryxx,String jsryxx,String zzzylbdb,String isqualiy) {
		String sql = " update ? set sbryxx = ?,jsryxx=?,zzzylbdb=?,isqualiy=? where rowguid= ?";
        return formbaseDao.execute(sql, tablename,sbryxx,jsryxx,zzzylbdb,isqualiy,rowguid);
    }
	
	public Record getJzqyzzDetailByRowguid(String tablename,String rowguid){
		String sql = "select * from ? where rowguid = ?";
		return formbaseDao.find(sql, Record.class, tablename,rowguid);
	}
	
	public List<Record> getJnJzqyzzzyByZzlb(String zzlb){
		String sql = " select * from jn_jzqyzzzy where zzlb = ?";
		return baseDao.findList(sql, Record.class, zzlb);
	}

    public String getSqlTableNameByFormId(String formId) {
		String sql = "select SQlTableName from Epointsform where formId = ? ";
		return formbaseDao.queryString(sql, formId);
    }

}
