package com.epoint.zbxfdj.util;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;

/**
 * 个性化数据源
 */
public class JDBCUtil {

    /**
     * 连接消防对接前置库（mysql）
     *
     * @return
     */
    /*public static ICommonDao getSPGLJDBC() {
        String url = ConfigUtil.getConfigValue("jdbc", "xfdjurl");
        String username = ConfigUtil.getConfigValue("jdbc", "xfdjusername");
        String password = ConfigUtil.getConfigValue("jdbc", "xfdjpassword");
        DataSourceConfig config = new DataSourceConfig(url, username, password);
        return CommonDao.getInstance(config);
    }*/

    /**
     * 连接消防对接前置库（瀚高库）
     *
     * @return
     */
    public static ICommonDao getHighGoJDBC() {
        String url = ConfigUtil.getConfigValue("jdbc", "xfdjurl_highgo");
        String username = ConfigUtil.getConfigValue("jdbc", "xfdjusername_highgo");
        String password = ConfigUtil.getConfigValue("jdbc", "xfdjpassword_highgo");
        DataSourceConfig config = new DataSourceConfig(url, username, password);
        return CommonDao.getInstance(config);
    }

}
