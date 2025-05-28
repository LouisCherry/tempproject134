package com.epoint.auditproject.auditproject.service;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;

/**
 * 建筑业企业资质数据库对应的后台service
 *
 * @author 86180
 * @version [版本号, 2020-04-15 11:11:06]
 */
public class ZtProjectService {

    private static String qzkURL = ConfigUtil.getConfigValue("datasyncjdbc", "ztqzkurl");
    private static String qzkNAME = ConfigUtil.getConfigValue("datasyncjdbc", "ztqzkname");
    private static String qzkPASSWORD = ConfigUtil.getConfigValue("datasyncjdbc", "ztqzkpassword");


    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;


    protected ICommonDao commonDaoQZK;

    private DataSourceConfig dataSourceConfigQZK = new DataSourceConfig(qzkURL, qzkNAME, qzkPASSWORD);

    public ZtProjectService() {
        baseDao = CommonDao.getInstance();
        commonDaoQZK = CommonDao.getInstance(dataSourceConfigQZK);
    }

    /**
     * 更新数据
     *
     * @param record BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public int updateRecord(Record record) {
        return commonDaoQZK.update(record);
    }

    public void inserRecord(Record record) {
        commonDaoQZK.insert(record);
    }

    public Record getDzbdDetailByZzbh(String tablename, String zzbh) {
        String sql = " select * from " + tablename + " where zzbh = ? ";
        return commonDaoQZK.find(sql, Record.class, zzbh);
    }


}
