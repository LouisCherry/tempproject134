package com.epoint.xmz.thirdreporteddata.basic.basedata.participatsinfo.impl;

import com.epoint.basic.basedata.participantsinfo.api.entity.ParticipantsInfo;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.metadata.datasource.api.IDataSourceService;
import com.epoint.frame.service.metadata.datasource.entity.DataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * 参建单位信息表对应的后台service
 *
 * @author eiai9
 * @version [版本号, 2018-05-18 13:51:00]
 */
public class GxhParticipantsInfoService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    /**
     * 获取外部数据源
     */
    public CommonDao getOtherDataSourse(String dsName) {
        ICommonDao dao = CommonDao.getInstance();
        DataSource dataSource = dao.find("select * from DataSource where DsName=? ", DataSource.class, dsName);
        DataSourceConfig config = IDataSourceService.changeDsToDatasourceConfig(dataSource);
        dao.close();
        return new CommonDao(config);
    }

    public GxhParticipantsInfoService() {
        baseDao = CommonDao.getInstance();
        //        CommonDao daory = getOtherDataSourse("企业库");
    }

    /**
     * 查询事项单位列表
     *
     * @param first
     * @param pagesize
     * @param taskguid
     * @param subappguid
     * @param corpname
     * @param corpcode
     * @param legal
     * @param keyword
     * @return
     */
    public PageData<ParticipantsInfo> getListByTaskguidAndSubappguid(int first, int pagesize, String taskguid,
            String subappguid, String corpname, String corpcode, String legal, String keyword) {
        String sql = "select a.*, case when b.rowguid is null then 0 else 1 end as status from PARTICIPANTS_INFO a left join ( select rowguid, corpguid from AUDIT_SP_I_TASK_CORP where taskguid = ?) b on a.ROWGUID = b.corpguid where a.SUBAPPGUID = ? and a.CORPTYPE != 999";
        String sqlCount = "select count(1) from PARTICIPANTS_INFO a left join ( select rowguid, corpguid from AUDIT_SP_I_TASK_CORP where taskguid = ?) b on a.ROWGUID = b.corpguid where a.SUBAPPGUID = ? and a.CORPTYPE != 999";
        List<String> params = new ArrayList<>();
        params.add(taskguid);
        params.add(subappguid);
        if (StringUtil.isNotBlank(corpname)) {
            sql += " and corpname like ?";
            sqlCount += " and corpname like ?";
            params.add("%" + corpname + "%");
        }
        if (StringUtil.isNotBlank(corpcode)) {
            sql += " and corpcode like ?";
            sqlCount += " and corpcode like ?";
            params.add("%" + corpcode + "%");
        }
        if (StringUtil.isNotBlank(legal)) {
            sql += " and a.legal like ?";
            sqlCount += " and a.legal like ?";
            params.add("%" + legal + "%");
        }
        if (StringUtil.isNotBlank(keyword)) {
            sql += " and (corpname like '%" + keyword + "%' or corpcode like '%" + keyword + "%' or legal like '%"
                    + keyword + "%')";
            sqlCount += " and (corpname like '%" + keyword + "%' or corpcode like '%" + keyword + "%' or legal like '%"
                    + keyword + "%')";
        }
        sql += " order by status desc,operatedate desc";
        PageData<ParticipantsInfo> pageData = new PageData<>();
        pageData.setList(baseDao.findList(sql, first, pagesize, ParticipantsInfo.class, params.toArray()));
        pageData.setRowCount(baseDao.queryInt(sqlCount, params.toArray()));
        return pageData;
    }
}
