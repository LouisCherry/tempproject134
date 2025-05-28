package com.epoint.auditproject.auditproject.service;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;

/**
 * 
 * 
 * @author 18039505500
 * @version [版本号, 2020-06-23 19:57:28]
 */
public class JnShYjsService
{
 /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;
    
    protected ICommonDao commonDaoTo;
    
    private static String URL = ConfigUtil.getConfigValue("jdbc", "shqzurl");
    private static String NAME = ConfigUtil.getConfigValue("jdbc", "shqzusername");
    private static String PASSWORD = ConfigUtil.getConfigValue("jdbc", "shqzpassword");

    /**
     * 前置库数据源
     */
    private DataSourceConfig dataSourceConfig = new DataSourceConfig(URL, NAME, PASSWORD);
    

    public JnShYjsService() {
        baseDao = CommonDao.getInstance();
        commonDaoTo = CommonDao.getInstance(dataSourceConfig);
    }
    
    
    public void inserRecord(Record record) {
    	commonDaoTo.insert(record);
	}
    
}
