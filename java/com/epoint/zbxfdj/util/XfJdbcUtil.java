package com.epoint.zbxfdj.util;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;

/**
 * 个性化数据源（3.0省消防前置库）
 */
public class XfJdbcUtil {

    /**
     * 连接消防前置库
     *
     * @return
     */
    public static ICommonDao getXfSpgljdbc() {
        String url = ConfigUtil.getConfigValue("datasyncjdbc", "zjqzkurl_v3");
        String username = ConfigUtil.getConfigValue("datasyncjdbc", "zjqzkname_v3");
        String password = ConfigUtil.getConfigValue("datasyncjdbc", "zjqzkpassword_v3");
        DataSourceConfig config = new DataSourceConfig(url, username, password);
        return CommonDao.getInstance(config);
    }
}
