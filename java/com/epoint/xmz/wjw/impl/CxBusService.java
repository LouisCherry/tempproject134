package com.epoint.xmz.wjw.impl;
import java.util.List;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.xmz.certhwslysjyxk.api.entity.CertHwslysjyxk;

/**
 * 车辆信息表对应的后台service
 * 
 * @author 1
 * @version [版本号, 2021-02-03 15:42:45]
 */
public class CxBusService
{
	 private static String URL = ConfigUtil.getConfigValue("datasyncjdbc", "dzbdzurl");
	 private static String NAME = ConfigUtil.getConfigValue("datasyncjdbc", "dzbdusername");
	 private static String PASSWORD = ConfigUtil.getConfigValue("datasyncjdbc", "dzbdpassword");
	 
	 /**
	     * 前置库数据源
	     */
	private DataSourceConfig dataSourceConfig = new DataSourceConfig(URL, NAME, PASSWORD);
	    
    /**
     * 数据增删改查组件
     */
    protected ICommonDao formbaseDao;

    public CxBusService() {
        formbaseDao = CommonDao.getInstance(dataSourceConfig);
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
    public Record getHdzzl10tyszxhcByRowguid(String rowguid) {
    	String sql = "select * from hdzzl10tyszxhc where rowguid = ?";
        return formbaseDao.find(sql, Record.class, rowguid);
    }
    
    public Record getHdzzl5tys10tyxzxhcByRowguid(String rowguid) {
    	String sql = "select * from hdzzl5tys10tyxzxhc where rowguid = ?";
    	return formbaseDao.find(sql, Record.class, rowguid);
    }
    
    public Record getZxhcByRowguid(String rowguid) {
    	String sql = "select * from zxhc where rowguid = ?";
    	return formbaseDao.find(sql, Record.class, rowguid);
    }
    public Record getWxpyscByRowguid(String projectguid) {
        String sql = "select * from wxpysc where rowguid = ?";
        return formbaseDao.find(sql, Record.class, projectguid);
    }
    
    public Record getCsgccByRowguid(String rowguid) {
    	String sql = "select * from csgcc where rowguid = ?";
    	return formbaseDao.find(sql, Record.class, rowguid);
    }
    
    public Record getQxhcByRowguid(String rowguid) {
    	String sql = "select * from qxhc where rowguid = ?";
    	return formbaseDao.find(sql, Record.class, rowguid);
    }
    
    public Record getWlyyczqcdlyszhfByRowguid(String rowguid) {
    	String sql = "select * from wlyyczqcdlyszhf where rowguid = ?";
    	return formbaseDao.find(sql, Record.class, rowguid);
    }
    
    public List<Record> getWlyyczqcdlyszhfzbByRowguid(String rowguid) {
    	String sql = "select * from wlyyczqcdlyszhfzb where connect = ?";
    	return formbaseDao.findList(sql, Record.class, rowguid);
    }
    
    public Record getXyczqcdlyszbfByRowguid(String rowguid) {
    	String sql = "select * from xyczqcdlyszbf where rowguid = ?";
    	return formbaseDao.find(sql, Record.class, rowguid);
    }
    
    public Record getHwcyzgsqByRowguid(String rowguid) {
    	String sql = "select * from formtable20210902172739 where rowguid = ?";
    	return formbaseDao.find(sql, Record.class, rowguid);
    }
    
    public Record getLkcyzgsqByRowguid(String rowguid) {
    	String sql = "select * from formtable20210902173326 where rowguid = ?";
    	return formbaseDao.find(sql, Record.class, rowguid);
    }
    
    public Record getDlkyjyxkByRowguid(String rowguid) {
    	String sql = "select * from dlbckyhbxjyxkhf where rowguid = ?";
    	return formbaseDao.find(sql, Record.class, rowguid);
    }
    
    public Record getWhjsycyzgxkByRowguid(String rowguid) {
    	String sql = "select * from dlwxhwysjsycyzgxk where rowguid = ?";
    	return formbaseDao.find(sql, Record.class, rowguid);
    }
    
    public Record getDlwxhwysyyrycyzgxkByRowguid(String rowguid) {
    	String sql = "select * from dlwxhwysyyrycyzgxk where rowguid = ?";
    	return formbaseDao.find(sql, Record.class, rowguid);
    }
    
    public Record getWlyyczcjsycyzgxkByRowguid(String rowguid) {
    	String sql = "select * from formtable20210902171645 where rowguid = ?";
    	return formbaseDao.find(sql, Record.class, rowguid);
    }
    
    
    public Record getSlysqyhzbzsqByRowguid(String xkzbh, String qymc) {
    	String sql = "select * from slysqyhzbzsq where xkzbh = ? or qyzwmc = ?";
    	return formbaseDao.find(sql, Record.class, xkzbh,qymc);
    }
    
    public Record getShttzsbgdjByRowguid(String rowguid) {
    	String sql = "select * from shttzsbgdj where rowguid = ?";
    	return formbaseDao.find(sql, Record.class, rowguid);
    }
    
    public Record getPorjectByRowguid(String tablename, String projectguid) {
    	String sql = "select * from "+tablename+" where rowguid = ?";
    	return formbaseDao.find(sql, Record.class, projectguid);
    }
    
    public Record getDzbdDetail(String tablename, String rowguid) {
		String sql = " select * from " + tablename + " where rowguid = ? ";
		return formbaseDao.find(sql, Record.class, rowguid);
	}
    
    public int update(Record record) {
        return formbaseDao.update(record);
    }
    
    public int insert(Record record) {
        return formbaseDao.insert(record);
    }
    
}
