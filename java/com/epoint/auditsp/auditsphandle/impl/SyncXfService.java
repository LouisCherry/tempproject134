package com.epoint.auditsp.auditsphandle.impl;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.database.peisistence.crud.impl.model.PageData;

import java.util.List;

public class SyncXfService {

    /**
     * 前置库数据源配置
     */
    String url = ConfigUtil.getConfigValue("jdbc", "xfdjurl_highgo");
    String username = ConfigUtil.getConfigValue("jdbc", "xfdjusername_highgo");
    String password = ConfigUtil.getConfigValue("jdbc", "xfdjpassword_highgo");
    private DataSourceConfig config = new DataSourceConfig(url, username, password);

    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public SyncXfService() {
        baseDao = CommonDao.getInstance(config);
    }

    public PageData<Record> geXflistByXmdm(int first, int pageSize, String sortField, String sortOrder, String xmdm) {
        PageData<Record> pageData = new PageData<>();
        String sql = "select LSH,SBSJ,SJSCZT,SBYY from SPGL_XMJBXXB where xmdm = ? order by SBSJ desc";
        List<Record> list = baseDao.findList(sql, first, pageSize, Record.class, xmdm);
        String sqlcount = "select count(1) from SPGL_XMJBXXB where xmdm = ?";
        int count = baseDao.queryInt(sqlcount, xmdm);
        pageData.setList(list);
        pageData.setRowCount(count);
        return pageData;
    }
}
