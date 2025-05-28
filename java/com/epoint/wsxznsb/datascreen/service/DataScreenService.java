package com.epoint.wsxznsb.datascreen.service;

import java.util.ArrayList;
import java.util.List;

import com.epoint.common.service.AuditCommonService;
import com.epoint.core.grammar.Record;

/**
 * 
 * 大数据屏service
 * 
 * @author jyong
 * @version 2022年1月19日
 */
public class DataScreenService extends AuditCommonService
{
    private static final long serialVersionUID = -8666888959494839764L;

    public int getHistoryQueueCount(String centerGuid, String type) {
        int historyCount = 0;
        if (commonDao.isMySql()) {
            String sql = "";
            // 年度统计
            if ("1".equals(type)) {
                sql = "select ifnull(count(*),1) historycount from audit_queue_history where CenterGuid =? and YEAR(GETNOTIME) = YEAR(now())";
            }
            else {
                sql = "select ifnull(count(*),1) historycount from audit_queue_history where CenterGuid =? and DATE_FORMAT(GETNOTIME,'%Y%m') = DATE_FORMAT(CURDATE(), '%Y%m')";
            }
            historyCount = commonDao.queryInt(sql, centerGuid);
        }
        return historyCount;
    }

    public Record getCurrentDayQueue(String centerGuid) {
        String sql = "select count(*) qhcount,IFNULL(SUM(case when STATUS = '1' then 1 else 0 end),0) blcount,IFNULL(SUM(case when STATUS = '0' then 1 else 0 end),0) ddcount";
        sql += " from audit_queue where CenterGuid =?";
        return commonDao.find(sql, Record.class, centerGuid);
    }

    public Record getCurrentDayEvate(String centerGuid) {
        Record record = new Record();
        if (commonDao.isMySql()) {
            String sql = "select count(*) counts,IFNULL(SUM(case when satisfied = '5' then 1 else 0 end),0) fcmy,IFNULL(SUM(case when satisfied = '4' then 1 else 0 end),0) my,";
            sql += " IFNULL(SUM(case when satisfied = '3' then 1 else 0 end),0) jbmy,IFNULL(SUM(case when satisfied = '2' then 1 else 0 end),0) bmy,IFNULL(SUM(case when satisfied = '1' then 1 else 0 end),0) fcbmy,";
            sql += " IFNULL(SUM(case when satisfied = '0' then 1 else 0 end),0) mrmy";
            sql += " from audit_online_evaluat aoe left join audit_queue aq on aoe.ClientIdentifier = aq.ROWGUID where aq.CenterGuid =? and TO_DAYS(aoe.Evaluatedate) = TO_DAYS(NOW())";
            record = commonDao.find(sql, Record.class, centerGuid);
        }
        return record;
    }

    public List<Record> getWindowHistoryQueueMonth(String centerGuid) {
        List<Record> list = new ArrayList<Record>();
        if (commonDao.isMySql()) {
            String sql = "select q.*,w.WINDOWNAME from (select count(*) jhcount,HANDLEWINDOWGUID from audit_queue_history";
            sql += " where CenterGuid =? and DATE_FORMAT(GETNOTIME,'%Y%m') = DATE_FORMAT(CURDATE(), '%Y%m') and IFNULL(HANDLEWINDOWGUID,'')<>''";
            sql += " GROUP BY HANDLEWINDOWGUID) q INNER JOIN audit_orga_window w";
            sql += " on q.HANDLEWINDOWGUID = w.rowguid where w.WINDOWNAME is not null ORDER BY q.jhcount desc";
            list = commonDao.findList(sql, Record.class, centerGuid);
        }
        return list;
    }

    public List<Record> getWindowTodayQueueMonth(String centerGuid) {
        List<Record> list = new ArrayList<Record>();
        if (commonDao.isMySql()) {
            String sql = "select q.*,w.WINDOWNAME from (select count(*) jhcount,HANDLEWINDOWGUID from audit_queue";
            sql += " where CenterGuid =? and DATE_FORMAT(GETNOTIME,'%Y%m') = DATE_FORMAT(CURDATE(), '%Y%m') and IFNULL(HANDLEWINDOWGUID,'')<>''";
            sql += " GROUP BY HANDLEWINDOWGUID) q INNER JOIN audit_orga_window w";
            sql += " on q.HANDLEWINDOWGUID = w.rowguid where w.WINDOWNAME is not null ORDER BY q.jhcount desc";
            list = commonDao.findList(sql, Record.class, centerGuid);
        }
        return list;
    }

    public List<Record> getOuHistoryQueueMonth(String centerGuid) {
        List<Record> list = new ArrayList<Record>();
        if (commonDao.isMySql()) {
            String sql = "select SUM(jhcount) oujhcount,o.OUNAME,o.OUGUID from (select count(*) jhcount,HANDLEWINDOWGUID from audit_queue_history";
            sql += " where CenterGuid =? and DATE_FORMAT(GETNOTIME,'%Y%m') = DATE_FORMAT(CURDATE(), '%Y%m') and IFNULL(HANDLEWINDOWGUID,'')<>''";
            sql += " GROUP BY HANDLEWINDOWGUID) q INNER JOIN audit_orga_window w on q.HANDLEWINDOWGUID = w.rowguid INNER JOIN frame_ou o on w.ouguid = o.ouguid";
            sql += " where w.WINDOWNAME is not null GROUP BY o.ouguid ORDER BY oujhcount desc";
            list = commonDao.findList(sql, Record.class, centerGuid);
        }
        return list;
    }

    public List<Record> getOuTodayQueueMonth(String centerGuid) {
        List<Record> list = new ArrayList<Record>();
        if (commonDao.isMySql()) {
            String sql = "select SUM(jhcount) oujhcount,o.OUNAME,o.OUGUID from (select count(*) jhcount,HANDLEWINDOWGUID from audit_queue";
            sql += " where CenterGuid =? and DATE_FORMAT(GETNOTIME,'%Y%m') = DATE_FORMAT(CURDATE(), '%Y%m') and IFNULL(HANDLEWINDOWGUID,'')<>''";
            sql += " GROUP BY HANDLEWINDOWGUID) q INNER JOIN audit_orga_window w on q.HANDLEWINDOWGUID = w.rowguid INNER JOIN frame_ou o on w.ouguid = o.ouguid";
            sql += " where w.WINDOWNAME is not null GROUP BY o.ouguid ORDER BY oujhcount desc";
            list = commonDao.findList(sql, Record.class, centerGuid);
        }
        return list;
    }

    public Record getSexCountToday(String centerGuid) {
        String sql = "select count(*) counts,IFNULL(SUM(case when substring(IDENTITYCARDNUM,17,1)%2 = 1 then 1 else 0 end),0) man,";
        sql += " IFNULL(SUM(case when substring(IDENTITYCARDNUM,17,1)%2 = 0 then 1 else 0 end),0) women from";
        sql += " (select distinct(IDENTITYCARDNUM) from audit_queue where CenterGuid =? and LENGTH(`IDENTITYCARDNUM`) = 18) a";
        return commonDao.find(sql, Record.class, centerGuid);
    }

    public Record getSexCountHistory(String centerGuid) {
        String sql = "select count(*) counts,IFNULL(SUM(case when substring(IDENTITYCARDNUM,17,1)%2 = 1 then 1 else 0 end),0) man,";
        sql += " IFNULL(SUM(case when substring(IDENTITYCARDNUM,17,1)%2 = 0 then 1 else 0 end),0) women from";
        sql += " (select distinct(IDENTITYCARDNUM) from audit_queue_history where CenterGuid =? and LENGTH(`IDENTITYCARDNUM`) = 18) a";
        return commonDao.find(sql, Record.class, centerGuid);
    }

    public Record getAgeCountToday(String centerGuid) {
        String sql = "";
        Record record = new Record();
        if (commonDao.isMySql()) {
            sql = "select count(*) counts,IFNULL(SUM(case when (YEAR(now())- substring(IDENTITYCARDNUM,7,4) BETWEEN 15 and 30) then 1 else 0 end),0) level1,";
            sql += " IFNULL(SUM(case when (YEAR(now())- substring(IDENTITYCARDNUM,7,4) BETWEEN 31 and 40) then 1 else 0 end),0) level2,";
            sql += " IFNULL(SUM(case when (YEAR(now())- substring(IDENTITYCARDNUM,7,4) BETWEEN 41 and 50) then 1 else 0 end),0) level3,";
            sql += " IFNULL(SUM(case when (YEAR(now())- substring(IDENTITYCARDNUM,7,4) BETWEEN 51 and 60) then 1 else 0 end),0) level4,";
            sql += " IFNULL(SUM(case when (YEAR(now())- substring(IDENTITYCARDNUM,7,4) BETWEEN 61 and 70) then 1 else 0 end),0) level5,";
            sql += " IFNULL(SUM(case when (YEAR(now())- substring(IDENTITYCARDNUM,7,4) >= 71) then 1 else 0 end),0) level6,";
            sql += " IFNULL(SUM(case when (YEAR(now())- substring(IDENTITYCARDNUM,7,4) < 15) then 1 else 0 end),0) level7 from";
            sql += " (select distinct(IDENTITYCARDNUM) from audit_queue where CenterGuid =? and LENGTH(`IDENTITYCARDNUM`) = 18) a";
            record = commonDao.find(sql, Record.class, centerGuid);
        }
        return record;
    }

    public Record getAgeCountHistory(String centerGuid) {
        String sql = "";
        Record record = new Record();
        if (commonDao.isMySql()) {
            sql = "select count(*) counts,IFNULL(SUM(case when (YEAR(now())- substring(IDENTITYCARDNUM,7,4) BETWEEN 15 and 30) then 1 else 0 end),0) level1,";
            sql += " IFNULL(SUM(case when (YEAR(now())- substring(IDENTITYCARDNUM,7,4) BETWEEN 31 and 40) then 1 else 0 end),0) level2,";
            sql += " IFNULL(SUM(case when (YEAR(now())- substring(IDENTITYCARDNUM,7,4) BETWEEN 41 and 50) then 1 else 0 end),0) level3,";
            sql += " IFNULL(SUM(case when (YEAR(now())- substring(IDENTITYCARDNUM,7,4) BETWEEN 51 and 60) then 1 else 0 end),0) level4,";
            sql += " IFNULL(SUM(case when (YEAR(now())- substring(IDENTITYCARDNUM,7,4) BETWEEN 61 and 70) then 1 else 0 end),0) level5,";
            sql += " IFNULL(SUM(case when (YEAR(now())- substring(IDENTITYCARDNUM,7,4) >= 71) then 1 else 0 end),0) level6,";
            sql += " IFNULL(SUM(case when (YEAR(now())- substring(IDENTITYCARDNUM,7,4) < 15) then 1 else 0 end),0) level7 from";
            sql += " (select distinct(IDENTITYCARDNUM) from audit_queue_history where CenterGuid =? and LENGTH(`IDENTITYCARDNUM`) = 18) a";
            record = commonDao.find(sql, Record.class, centerGuid);
        }
        return record;
    }

    public List<Record> getProjectCount() {
        List<Record> list = new ArrayList<Record>();
        if (commonDao.isMySql()) {
            String sql = "select xiaquname as name,SUM(case when year(a.applydate) = year(NOW()) THEN 1 else 0 END) as year,SUM(case when  year(a.applydate) = year(NOW()) and MONTH(a.applydate) = MONTH(NOW()) THEN 1 else 0 END) as month  from audit_project a join frame_ou_extendinfo b on a.ouguid = b.ouguid join audit_orga_area c on substr(b.areacode,1,9) = c.xiaqucode ";
            sql += " where  a.areacode = '370830' and c.xiaqucode != '370830013' group by substr(b.areacode,1,9)";
            list = commonDao.findList(sql, Record.class);
        }
        return list;
    }

    public Record getProjectTotal() {
        String sql = "";
        Record record = new Record();
        if (commonDao.isMySql()) {
            sql = "select count(1) as total from audit_project where areacode = '370830' and year(applydate) = year(NOW()) ";
            record = commonDao.find(sql, Record.class);
        }
        return record;
    }

    public int getYuYueWechatCount(String centerGuid, String yuyueType) {
        int historyCount = 0;
        if (commonDao.isMySql()) {
            String sql = "select count(1) from audit_queue_appointment where CenterGuid =? and APPOINTMENTTYPE = ? and DATE_FORMAT(APPOINTMENTFROMTIME ,'%Y%m') = DATE_FORMAT(CURDATE(), '%Y%m')";
            historyCount = commonDao.queryInt(sql, centerGuid, yuyueType);
        }
        return historyCount;
    }
}
