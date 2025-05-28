package com.epoint.newtranslation.service;

import java.util.List;

import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.newtranslation.dao.SyncToRightOracleNewDao;

public class SyncToRightNewService
{
	
	private static String qzURL = ConfigUtil.getConfigValue("datasyncjdbc", "qzurl");
    private static String qzNAME = ConfigUtil.getConfigValue("datasyncjdbc", "qzusername");
    private static String qzPASSWORD = ConfigUtil.getConfigValue("datasyncjdbc", "qzpassword");


    private DataSourceConfig dataSourceConfig2 = new DataSourceConfig(qzURL, qzNAME, qzPASSWORD);


    private SyncToRightOracleNewDao syncOracleDao;

    public SyncToRightNewService() {
        syncOracleDao = new SyncToRightOracleNewDao(dataSourceConfig2);
    }

    /**
     * 
     *  获取前置机所有存在待同步事项的部门ORG_CODE
     *  [功能详细描述]
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<Record> getQLSXOU() {
        return syncOracleDao.getQLSXOU();
    }


    /**
     * 
     *  根据ORG_CODE 获取待下放的事项
     *  [功能详细描述]
     *  @param orgCode
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<Record> selectRight(String orgCode) {
        return syncOracleDao.selectRight(orgCode);
    }

    /**
     * @Description:获取待同步权力
     * @author:许烨斌
     * @time 2019年3月20日下午3:43:31
     * @return
     */
    public List<Record> selectRightnew() {
        return syncOracleDao.selectRightnew();
    }

    /**
     * @Description:获取特定同步权力事项
     * @author:许烨斌
     * @time 2019年3月20日下午3:43:31
     * @return
     */
    public List<Record> selectAgencyOrganRightnew() {
        return syncOracleDao.selectAgencyOrganRightnew();
    }
    /**
     * 
     *  [一句话功能简述]
     *  [功能详细描述]
     *  @param RowGuid
     *  @param updateType
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<Record> selectRightByRowGuid(String rowGuid, String updateType) {
        return syncOracleDao.selectRightByRowGuid(rowGuid, updateType);
    }

    public List<Record> getfileInfo(String filePath) {
        return syncOracleDao.getfileInfo(filePath);
    }
    
}
