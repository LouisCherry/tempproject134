package com.epoint.auditqueue.auditznsbqueue.impl;

import java.util.List;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;

public class QueueListService
{
    public List<Record> getModuleClick(String centerguid, String startime, String endtime) {
        ICommonDao commonDao = CommonDao.getInstance();
        String sql = "";
        if (commonDao.isMySql()) {
            sql = "select modulename,count(1) clicknum from audit_znsb_module where CenterGuid=? and DATE(onclicktime)>=? and  DATE(onclicktime)<=? group by modulename order by count(1)  desc";
        }
        else if (commonDao.isSqlserver()) {
            sql = "select  modulename,count(1) clicknum from audit_znsb_module where CenterGuid=? and DATE(onclicktime)>=? and  DATE(onclicktime)<=? group by modulename order by count(1)  desc";
        }
        else {
            sql = "select modulename,count(1) clicknum from audit_znsb_module where CenterGuid=? and onclicktime >= TO_DATE(?,'yyyy-MM-dd') and  onclicktime <= TO_DATE(?,'yyyy-MM-dd')  group by modulename order by count(1) desc";
        }
        return commonDao.findList(sql, Record.class, centerguid, startime, endtime);
    }

    public List<Record> getModuleClick(String startime, String endtime) {
        String sql = "";
        ICommonDao commonDao = CommonDao.getInstance();
        if (commonDao.isMySql()) {
            sql = "select modulename,count(1) clicknum from audit_znsb_module where  DATE(onclicktime)>=? and  DATE(onclicktime)<=? group by modulename order by count(1) desc";
        }
        else if (commonDao.isSqlserver()) {
            sql = "select  modulename,count(1) clicknum from audit_znsb_module where  DATE(onclicktime)>=? and  DATE(onclicktime)<=? group by modulename order by count(1) desc";
        }
        else {
            sql = "select modulename,count(1) clicknum from audit_znsb_module  where onclicktime >= TO_DATE(?,'yyyy-MM-dd') and  onclicktime <= TO_DATE(?,'yyyy-MM-dd')  group by modulename order by count(1) desc";
        }
        return commonDao.findList(sql, Record.class, startime, endtime);
    }
}
