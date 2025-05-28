package com.epoint.tongbufw;

import java.util.List;

import org.apache.log4j.Logger;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;

public class EpointRsprojectService
{

    transient Logger log = LogUtil.getLog(EpointSyncDone.class);

    /**
     * 数据库操作DAO
     */
    protected ICommonDao commonDaoFrom;

    protected ICommonDao commonDaoTo;
    private static String URL = ConfigUtil.getConfigValue("datasyncjdbc", "qzkurl");
    private static String NAME = ConfigUtil.getConfigValue("datasyncjdbc", "qzkusername");
    private static String PASSWORD = ConfigUtil.getConfigValue("datasyncjdbc", "qzkpassword");

    private static String RsURL = ConfigUtil.getConfigValue("datasyncjdbc", "Rsqzurl");
    private static String RsNAME = ConfigUtil.getConfigValue("datasyncjdbc", "Rsqzusername");
    private static String RsPASSWORD = ConfigUtil.getConfigValue("datasyncjdbc", "Rsqzpassword");

    /**
     * 前置库数据源
     */
    private DataSourceConfig dataSourceConfig = new DataSourceConfig(RsURL, RsNAME, RsPASSWORD);
    /**
     * mysql前置库数据源
     */
    private DataSourceConfig dataSourceConfig1 = new DataSourceConfig(URL, NAME, PASSWORD);

    public EpointRsprojectService() {
        commonDaoFrom = CommonDao.getInstance(dataSourceConfig);
        commonDaoTo = CommonDao.getInstance(dataSourceConfig1);
    }

    public ICommonDao getCommonDaoFrom() {
        return commonDaoFrom;
    }

    public ICommonDao getCommonDaoTo() {
        return commonDaoTo;
    }

    public List<Record> getRsproject() {
        String sql = "select * from RSJ.GOVAFFAIRS_INFO where synid is null and rownum<=20";
        List<Record> list = commonDaoFrom.findList(sql, Record.class);
        return list;
    }

    public List<Record> getRsgovaffproject() {
        String sql = "select * from RSJ.GOV_AFFAIRS_INFO where synid is null and rownum<=20";
        List<Record> list = commonDaoFrom.findList(sql, Record.class);
        return list;
    }

    public int insertProject(Record record2) {
        record2.setSql_TableName("pre_project");
        return commonDaoTo.insert(record2);
    }

    public int insertPhase(Record record2) {
        record2.setSql_TableName("pre_phase");
        int res = commonDaoTo.insert(record2);
        return res;

    }

    public int updateSynid(String unid) {
        String sql = "update RSJ.GOVAFFAIRS_INFO set synid='N' where unid=? ";
        int count = commonDaoFrom.execute(sql, unid);
        commonDaoFrom.commitTransaction();
        return count;
    }

    public int updategovaSynid(String unid) {
        String sql = "update RSJ.GOV_AFFAIRS_INFO set synid='N' where unid=? ";
        int count = commonDaoFrom.execute(sql, unid);
        commonDaoFrom.commitTransaction();
        return count;
    }
}
