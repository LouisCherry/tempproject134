package com.epoint.xmz.sgxkcert.job;

import java.util.List;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;

public class SgxkSggSendDataService
{
    protected ICommonDao commonDao; 
    protected ICommonDao ggxtcommonDao;
    
    private static String URL = ConfigUtil.getConfigValue("datasyncjdbc", "ggxturl");
    private static String NAME = ConfigUtil.getConfigValue("datasyncjdbc", "ggxtname");
    private static String PASSWORD = ConfigUtil.getConfigValue("datasyncjdbc", "ggxtpassword");
    
    
    DataSourceConfig dataSourceConfig = new DataSourceConfig(URL, NAME, PASSWORD);
    
    //初始化数据源
    public SgxkSggSendDataService() {
        commonDao = CommonDao.getInstance();
    }
    
    public ICommonDao getGgxtcommonDao() {
        return ggxtcommonDao;
    }

    public ICommonDao getCommonDao() {
        return commonDao;
    }
    

    /**
     * 
     * 新增前置库数据
     * 
     * @param baseClass
     * @param record
     */
    public void insertSggRecord(Record record) {
        ggxtcommonDao = CommonDao.getInstance(dataSourceConfig);
        ggxtcommonDao.insert(record);
        ggxtcommonDao.close();
    }
    
    /**
     *  获取本地库数据库数据
     *  sggsync  省工改还未同步的标识
     *  is_history  在用版本
     */
    public List<Record> getCertInfoList() {
        String CERTCATALOGID=ConfigUtil.getConfigValue("datasyncjdbc","GGCERTCATALOGID");
        String sql = "select * from cert_info where CERTCATALOGID='"+CERTCATALOGID+"' and  ifnull(sggsync,0) = 0 and ishistory = 0 and DATE_FORMAT(OperateDate,'%Y')>='2020' order by operatedate desc limit 50";
        List<Record> certList = commonDao.findList(sql, Record.class);
        commonDao.close();
        return certList;
    }

    /**
     *  修改本地库数据状态
     */
    public void updateRecord(Record record) {
        record.setPrimaryKeys("rowguid");
        record.setSql_TableName("cert_info");
        commonDao.update(record);
        commonDao.close();
    }


}
