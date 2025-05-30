package com.epoint.zwdt.zwdtrest.bdc.impl;

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
public class BdcService
{
	  /**
     * 数据增删改查组件
     */
    protected ICommonDao dao;
    //浪潮前置库链接
    protected ICommonDao bdckdao;
    private static String QZKJURL = ConfigUtil.getConfigValue("datasyncjdbc", "bdcurl");
	private static String QZKNAME = ConfigUtil.getConfigValue("datasyncjdbc", "bdcname");
	private static String QZKPASSWORD = ConfigUtil.getConfigValue("datasyncjdbc", "bdcpassword");
	
	private DataSourceConfig dataSourceConfig = new DataSourceConfig(QZKJURL, QZKNAME, QZKPASSWORD);
    
    /**
     * 前置库数据源
     */
    
    public BdcService() {
        dao = CommonDao.getInstance();
        bdckdao = CommonDao.getInstance(dataSourceConfig);

    }
   


	public Record getBdcDetailByBdcdyh(String bdcdyh) {
		String sql = "SELECT * FROM BDCDJ.BDC_CER_SPJ WHERE bdcqzh = '"+bdcdyh+"'";
		return bdckdao.find(sql, Record.class);
	}


}
