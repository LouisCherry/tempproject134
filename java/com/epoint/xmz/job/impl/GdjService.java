package com.epoint.xmz.job.impl;

import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;

/**
 * 好差评相关接口的详细实现
 * @作者 atjiao
 * @version [版本号, 2020年6月8日]
 */
public class GdjService
{
	  /**
     * 数据增删改查组件
     */
    protected ICommonDao swcommonDao;
    
    private static String URL = ConfigUtil.getConfigValue("datasyncjdbc", "gdjurl");
	private static String NAME = ConfigUtil.getConfigValue("datasyncjdbc", "gdjusername");
	private static String PASSWORD = ConfigUtil.getConfigValue("datasyncjdbc", "gdjpassword");
	
	
    
    private DataSourceConfig dataSourceConfig = new DataSourceConfig(URL, NAME, PASSWORD);
    
    /**
     * 前置库数据源
     */
    
    public GdjService() {
    	swcommonDao = CommonDao.getInstance(dataSourceConfig);
    }
   
    
    /**
	 * 
	 * 新增某条记录
	 * 
	 * @param baseClass
	 * @param record
	 */
	public void insertsw(AuditRsItemBaseinfo record) {
		swcommonDao.insert(record);
	}

	/**
	 * 修改某条记录
	 * 
	 * @param baseClass
	 * @param record
	 */
	public void updatesw(AuditRsItemBaseinfo record) {
		swcommonDao.update(record);
	}
	
	
	/**
	 * 修改某条记录ue
	 * 
	 * @param baseClass
	 * @param record
	 */
	public AuditRsItemBaseinfo getSwInteminfoByItemcode(String Itemcode) {
		String sql = "select  * from AUDIT_RS_ITEM_BASEINFO where 1=1  and Itemcode=? ";
		return swcommonDao.find(sql, AuditRsItemBaseinfo.class, Itemcode);
	}

	/**
	 * 查询记录
	 */
	public AuditRsItemBaseinfo getSwInteminfoByRowguid(String rowguid) {
		return swcommonDao.find(AuditRsItemBaseinfo.class, rowguid);
	}
	    

}
