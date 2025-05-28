package com.epoint.sghd.auditjianguan.rlconfig.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.sghd.auditjianguan.rlconfig.api.IRlconfigSendmsg;
import com.epoint.sghd.auditjianguan.rlconfig.api.RlconfigSendmsg;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Component
public class RlconfigSendmsgImpl implements IRlconfigSendmsg {
    protected ICommonDao baseDao;

    public RlconfigSendmsgImpl() {
        baseDao = CommonDao.getInstance();
    }

    @Override
    public int insert(RlconfigSendmsg rlconfigsendmsg) {
        return baseDao.insert(rlconfigsendmsg);
    }

    @Override
    public int deleteByGuid(String guid) {
        RlconfigSendmsg rlconfigsendmsg = baseDao.find(RlconfigSendmsg.class, guid);
        return baseDao.delete(rlconfigsendmsg);
    }

    @Override
    public int update(RlconfigSendmsg rlconfigsendmsg) {
        return baseDao.update(rlconfigsendmsg);
    }

    @Override
    public RlconfigSendmsg find(Object primaryKey) {
        return baseDao.find(RlconfigSendmsg.class, primaryKey);
    }

    @Override
    public PageData<RlconfigSendmsg> getListByPage(Map<String, String> conditionMap, int first,
                                                   int pageSize, String sortField, String sortOrder) {
        PageData<RlconfigSendmsg> pageData = new PageData<RlconfigSendmsg>();
        SQLManageUtil sm = new SQLManageUtil();
        String order = "";
        if (StringUtil.isNotBlank(sortField) && StringUtil.isNotBlank(sortOrder)) {
            order = " order by " + sortField + " " + sortOrder;
        }
        String sqle = sm.buildSql(conditionMap);

        String sql = "select * from rlconfigsendmsg " + sqle + order;
        String sqlcount = sql.replace("*", "count(1)");
        List<RlconfigSendmsg> list = baseDao.findList(sql, first, pageSize, RlconfigSendmsg.class);
        pageData.setList(list);
        pageData.setRowCount(baseDao.queryInt(sqlcount));
        return pageData;
    }

    @Override
    public List<RlconfigSendmsg> findtosendmsg() {
        String nowstr = EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT);
        String sql = "select * from rlconfigsendmsg where senddate is null or senddate not like '" + nowstr + "%'";
        List<RlconfigSendmsg> list = baseDao.findList(sql, RlconfigSendmsg.class);
        baseDao.close();
        return list;
    }

    /**
     * 部门获取已认领数量
     *
     * @return
     */
    @Override
    public int getTaJianGuanTabYrlCount(String areaCode, String ouguid) {
        String nowstr = EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT);
        String sql = "select count(1) from audit_project p INNER JOIN (select task_id,jg_ouguid from audit_task_jn WHERE is_hz = 1) a ON a.TASK_ID = p.TASK_ID where p.areaCode = ? and a.jg_ouguid like '%"
                + ouguid + "%' and p.status = 90 and p.Banjieresult = '40' AND p.renlingtime is not NULL and p.banjiedate like '" + nowstr + "%'";
        int YrlCount = baseDao.queryInt(sql, areaCode);
        baseDao.close();
        return YrlCount;
    }

    /**
     * 部门获取未认领数量
     *
     * @return
     */
    @Override
    public int getTaJianGuanTabWrlCount(String areaCode, String ouguid) {
        String nowstr = EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT);
        String sql = "select count(1) from audit_project p INNER JOIN (select task_id,jg_ouguid from audit_task_jn WHERE is_hz = 1) a ON a.TASK_ID = p.TASK_ID where  p.areaCode = ? and a.jg_ouguid like '%"
                + ouguid + "%' and p.status = 90 and p.Banjieresult = '40' AND p.renlingtime is NULL and p.banjiedate like '" + nowstr + "%'";
        int WrlCount = baseDao.queryInt(sql, areaCode);
        baseDao.close();
        return WrlCount;
    }

    @Override
    public int updateSendinfo(String rowguid, String sendmsg) {
        String sql = "update rlconfigsendmsg set senddate=now(),sendmsg='" + sendmsg + "' where rowguid='" + rowguid + "'";
        int updateresult = baseDao.execute(sql);
        baseDao.close();
        return updateresult;
    }
}
