package com.epoint.xmz.thirdreporteddata.sqgl.job.impl;

import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.xmz.thirdreporteddata.basic.spglv3.domain.SpglXmjbxxbV3;

import java.util.List;

public class SpglXmjbxxbV3JobService {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    protected ICommonDao qzkDao;

    //api-manage前置库连接方式
    private static String URL = ConfigUtil.getConfigValue("datasyncjdbc", "apimanager_url");
    private static String NAME = ConfigUtil.getConfigValue("datasyncjdbc", "apimanager_username");
    private static String PASSWORD = ConfigUtil.getConfigValue("datasyncjdbc", "apimanager_password");

    DataSourceConfig dataSourceConfig = new DataSourceConfig(URL, NAME, PASSWORD);

    public SpglXmjbxxbV3JobService() {
        baseDao = CommonDao.getInstance();
        qzkDao = CommonDao.getInstance(dataSourceConfig);
    }

    /**
     * @return
     */
    public List<AuditRsItemBaseinfo> getAuditRsItemBaseInfoList() {
        String sql = "select * from audit_rs_item_baseinfo where issendzj = '1' and issendzj_v3 is null limit 500";
        return baseDao.findList(sql, AuditRsItemBaseinfo.class);
    }

    public boolean IsXmjbxxbV3(String gcdm) {
        String sql = "select * from spgl_xmjbxxb where gcdm = ?";
        SpglXmjbxxbV3 spglXmjbxxbV3 = qzkDao.find(sql, SpglXmjbxxbV3.class, gcdm);
        if (spglXmjbxxbV3 == null) {
            return false;
        } else {
            return true;
        }
    }

}
