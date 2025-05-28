package com.epoint.apimanage.utils.service;

import com.epoint.apimanage.log.entity.ApiManageLog;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * 镇街经纬度对应的后台service
 *
 * @author Administrator
 * @version [版本号, 2019-04-24 15:18:22]
 */
public class CommonDaoService {

    //api-manage前置库连接方式
    private static String URL = ConfigUtil.getConfigValue("datasyncjdbc", "apimanager_url");
    private static String NAME = ConfigUtil.getConfigValue("datasyncjdbc", "apimanager_username");
    private static String PASSWORD = ConfigUtil.getConfigValue("datasyncjdbc", "apimanager_password");

    DataSourceConfig dataSourceConfig = new DataSourceConfig(URL, NAME, PASSWORD);


    /**
     * 数据库操作DAO
     */
    protected ICommonDao baseDao;


    public CommonDaoService() {
        baseDao = CommonDao.getInstance(dataSourceConfig);

    }


    public List<ApiManageLog> findLogList() {
        String sql = "select aml.rowguid, ami.apiname,aml.status ,ami.apipath from api_manage_log aml left join api_manage_info ami on aml.requesturl =ami.apipath where (aml.status !=? or aml.STATUS is null) and ifnull(aml.scanned,'') <> ?";
        return baseDao.findList(sql, ApiManageLog.class, new Object[]{HttpStatus.OK.value(), "1"});
    }

    public int update(ApiManageLog apiManageLog) {
        return baseDao.update(apiManageLog);
    }
}
