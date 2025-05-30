package com.epoint.zwdt.zwdtrest.sghd.service;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GxhAuditJianguanService {

    protected ICommonDao baseDao;

    public GxhAuditJianguanService() {
        baseDao = CommonDao.getInstance();
    }

    public PageData<Record> getAuditJianguanPageData(Record record, int pageindex, int pagesize, String sortField, String sortOrder) {
        List<Record> list = new ArrayList<>();
        List<String> params = new ArrayList<>();
        PageData<Record> pageData = new PageData<>();
        int count = 0;
        String sql = "select p.projectguid,p.flowsn,p.projectname,p.pviguid,p.banjiedate,p.ouname,p.applyername,p.renlingtype,p.renlingdate,p.renlingusername from audit_jianguan p where 1=1 ";
        String sqlnum = "select count(1) from audit_jianguan p where 1=1 ";
        if (StringUtil.isNotBlank(record.getStr("projectname"))) {
            String projectname = record.getStr("projectname");
            if ("%".equals(projectname)) {
                projectname = "\\%";
            }
            sql = sql + " and p.projectname like ? ";
            sqlnum += " and p.projectname like ? ";
            params.add("%" + projectname + "%");
        }
        if (StringUtil.isNotBlank(record.getStr("flowsn"))) {
            String flowsn = record.getStr("flowsn");
            if ("%".equals(flowsn)) {
                flowsn = "\\%";
            }
            sql = sql + " and p.flowsn like ? ";
            sqlnum += " and p.flowsn like ? ";
            params.add("%" + flowsn + "%");
        }
        if (StringUtil.isNotBlank(record.getStr("renlingtype"))) {
            sql = sql + " and p.renlingtype = ? ";
            sqlnum += " and p.renlingtype = ? ";
            params.add(record.getStr("renlingtype"));
        }
        if (StringUtil.isNotBlank(record.getStr("renlingusername"))) {
            sql = sql + " and p.renlingusername like ? ";
            sqlnum += " and p.renlingusername like ? ";
            params.add("%" + record.getStr("renlingusername") + "%");
        }
        if (StringUtil.isNotBlank(record.getStr("applyername"))) {
            String applyername = record.getStr("applyername");
            if ("%".equals(applyername)) {
                applyername = "\\%";
            }
            sql = sql + " and p.applyername like ? ";
            sqlnum += " and p.applyername like ? ";
            params.add("%" + applyername + "%");
        }

        if (StringUtil.isNotBlank(record.get("renlingdateStart"))) {
            Date renlingdateStart = EpointDateUtil.getBeginOfDate(record.getDate("renlingdateStart"));
            sql += " and p.renlingdate >= '" + EpointDateUtil.convertDate2String(renlingdateStart) + "'";
            sqlnum += " and p.renlingdate >= '" + EpointDateUtil.convertDate2String(renlingdateStart) + "'";
        }

        if (StringUtil.isNotBlank(record.get("renlingdateEnd"))) {
            Date renlingdateEnd = EpointDateUtil.getBeginOfDate(record.getDate("renlingdateEnd"));
            sql += " and p.renlingdate <= '" + EpointDateUtil.convertDate2String(renlingdateEnd) + "'";
            sqlnum += " and p.renlingdate <= '" + EpointDateUtil.convertDate2String(renlingdateEnd) + "'";
        }

        if (StringUtil.isNotBlank(record.get("banjiedateStart"))) {
            Date banjiedateStart = EpointDateUtil.getBeginOfDate(record.getDate("banjiedateStart"));
            sql += " and p.banjiedate >= '" + EpointDateUtil.convertDate2String(banjiedateStart) + "'";
            sqlnum += " and p.banjiedate >= '" + EpointDateUtil.convertDate2String(banjiedateStart) + "'";
        }

        if (StringUtil.isNotBlank(record.get("banjiedateEnd"))) {
            Date banjiedateEnd = EpointDateUtil.getBeginOfDate(record.getDate("banjiedateEnd"));
            sql += " and p.banjiedate <= '" + EpointDateUtil.convertDate2String(banjiedateEnd) + "'";
            sqlnum += " and p.banjiedate <= '" + EpointDateUtil.convertDate2String(banjiedateEnd) + "'";
        }

        list = baseDao.findList(sql, pageindex, pagesize, Record.class, params.toArray());
        count = baseDao.queryInt(sqlnum, params.toArray());
        pageData.setList(list);
        pageData.setRowCount(count);
        return pageData;
    }

    /**
     * 部门获取已认领数量
     *
     * @return
     */
    public int getYrlCount() {
        String sql = "select count(1) from audit_jianguan where renlingtype = '1' ";
        return baseDao.queryInt(sql);
    }

    /**
     * 部门获取未认领数量
     *
     * @return
     */
    public int getWrlCount() {
        String sql = "select count(1) from audit_jianguan where renlingtype = '0' ";
        return baseDao.queryInt(sql);
    }

    /**
     * 部门获取审批信息数量
     *
     * @return
     */
    public int getSpxxCount() {
        String sql = "select count(1) from audit_jianguan ";
        return baseDao.queryInt(sql);
    }

    public int insertauditjianguan(Record record) {
        return baseDao.insert(record);
    }
}
