package com.epoint.lsznsb.bigscreen.impl;

import java.util.Date;
import java.util.List;

import com.epoint.common.service.AuditCommonService;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;

public class LsxZnsbBigScreenService extends AuditCommonService
{
    private static final long serialVersionUID = 1L;

    public Record getSexCountToday(String centerGuid) {
        String sql = "select count(*) counts,IFNULL(SUM(case when substring(IDENTITYCARDNUM,17,1)%2 = 1 then 1 else 0 end),0) man,";
        sql += " IFNULL(SUM(case when substring(IDENTITYCARDNUM,17,1)%2 = 0 then 1 else 0 end),0) women from";
        sql += " (select IDENTITYCARDNUM from audit_queue where CenterGuid =? and LENGTH(`IDENTITYCARDNUM`) = 18) a";
        return commonDao.find(sql, Record.class, centerGuid);
    }

    public Record getSexCountHistory(String centerGuid) {
        String sql = "select count(*) counts,IFNULL(SUM(case when substring(IDENTITYCARDNUM,17,1)%2 = 1 then 1 else 0 end),0) man,";
        sql += " IFNULL(SUM(case when substring(IDENTITYCARDNUM,17,1)%2 = 0 then 1 else 0 end),0) women from";
        sql += " (select IDENTITYCARDNUM from audit_queue_history where CenterGuid =? and LENGTH(`IDENTITYCARDNUM`) = 18) a";
        return commonDao.find(sql, Record.class, centerGuid);
    }

    public Record getAgeCountToday(String centerGuid) {
        String sql = "";
        Record record = new Record();
        if (commonDao.isMySql()) {
            sql = "select count(*) counts,IFNULL(SUM(case when (YEAR(now())- substring(IDENTITYCARDNUM,7,4) BETWEEN 0 and 18) then 1 else 0 end),0) level1,";
            sql += " IFNULL(SUM(case when (YEAR(now())- substring(IDENTITYCARDNUM,7,4) BETWEEN 19 and 30) then 1 else 0 end),0) level2,";
            sql += " IFNULL(SUM(case when (YEAR(now())- substring(IDENTITYCARDNUM,7,4) BETWEEN 31 and 40) then 1 else 0 end),0) level3,";
            sql += " IFNULL(SUM(case when (YEAR(now())- substring(IDENTITYCARDNUM,7,4) BETWEEN 41 and 50) then 1 else 0 end),0) level4,";
            sql += " IFNULL(SUM(case when (YEAR(now())- substring(IDENTITYCARDNUM,7,4) BETWEEN 51 and 60) then 1 else 0 end),0) level5,";
            sql += " IFNULL(SUM(case when (YEAR(now())- substring(IDENTITYCARDNUM,7,4) >= 61) then 1 else 0 end),0) level6 from";
            sql += " (select IDENTITYCARDNUM from audit_queue where CenterGuid =? and LENGTH(`IDENTITYCARDNUM`) = 18) a";
            record = commonDao.find(sql, Record.class, centerGuid);
        }
        return record;
    }

    public Record getAgeCountHistory(String centerGuid) {
        String sql = "";
        Record record = new Record();
        if (commonDao.isMySql()) {
            sql = "select count(*) counts,IFNULL(SUM(case when (YEAR(now())- substring(IDENTITYCARDNUM,7,4) BETWEEN 0 and 18) then 1 else 0 end),0) level1,";
            sql += " IFNULL(SUM(case when (YEAR(now())- substring(IDENTITYCARDNUM,7,4) BETWEEN 19 and 30) then 1 else 0 end),0) level2,";
            sql += " IFNULL(SUM(case when (YEAR(now())- substring(IDENTITYCARDNUM,7,4) BETWEEN 31 and 40) then 1 else 0 end),0) level3,";
            sql += " IFNULL(SUM(case when (YEAR(now())- substring(IDENTITYCARDNUM,7,4) BETWEEN 41 and 50) then 1 else 0 end),0) level4,";
            sql += " IFNULL(SUM(case when (YEAR(now())- substring(IDENTITYCARDNUM,7,4) BETWEEN 51 and 60) then 1 else 0 end),0) level5,";
            sql += " IFNULL(SUM(case when (YEAR(now())- substring(IDENTITYCARDNUM,7,4) >= 61) then 1 else 0 end),0) level6 from";
            sql += " (select IDENTITYCARDNUM from audit_queue_history where CenterGuid =? and LENGTH(`IDENTITYCARDNUM`) = 18) a";
            record = commonDao.find(sql, Record.class, centerGuid);
        }
        return record;
    }

    public List<Record> getTaskListToday(String centerguid, Date from, Date to) {
        String sql = "select TASKGUID,aqt.TaskTypeName,count(1) taskcount from audit_queue aqh left join audit_queue_tasktype aqt";
        sql += " on aqh.TASKGUID = aqt.RowGuid where aqh.CenterGuid = ?";
        String fromDate = EpointDateUtil.convertDate2String(from, "yyyy-MM-dd HH:mm:ss");
        String toDate = EpointDateUtil.convertDate2String(to, "yyyy-MM-dd HH:mm:ss");
        if (commonDao.isSqlserver()) {
            sql += " and  GETNOTIME>'" + fromDate + "' and GETNOTIME<'" + toDate + "'";
        }
        else if (commonDao.isMySql()) {
            sql += " and  GETNOTIME>'" + fromDate + "' and GETNOTIME<'" + toDate + "'";
        }
        else {
            sql += " and  GETNOTIME>to_date('" + fromDate + "','yyyy-mm-dd hh24:mi:ss') and GETNOTIME<to_date('"
                    + toDate + "','yyyy-mm-dd hh24:mi:ss')";
        }
        sql += " group by aqh.TASKGUID order by taskcount desc";
        return commonDao.findList(sql, Record.class, centerguid);
    }

    public List<Record> getTaskListHistory(String centerguid, Date from, Date to) {
        String sql = "select TASKGUID,aqt.TaskTypeName,count(1) taskcount from audit_queue_history aqh left join audit_queue_tasktype aqt";
        sql += " on aqh.TASKGUID = aqt.RowGuid where aqh.CenterGuid = ?";
        String fromDate = EpointDateUtil.convertDate2String(from, "yyyy-MM-dd HH:mm:ss");
        String toDate = EpointDateUtil.convertDate2String(to, "yyyy-MM-dd HH:mm:ss");
        if (commonDao.isSqlserver()) {
            sql += " and  GETNOTIME>'" + fromDate + "' and GETNOTIME<'" + toDate + "'";
        }
        else if (commonDao.isMySql()) {
            sql += " and  GETNOTIME>'" + fromDate + "' and GETNOTIME<'" + toDate + "'";
        }
        else {
            sql += " and  GETNOTIME>to_date('" + fromDate + "','yyyy-mm-dd hh24:mi:ss') and GETNOTIME<to_date('"
                    + toDate + "','yyyy-mm-dd hh24:mi:ss')";
        }
        sql += " group by aqh.TASKGUID order by taskcount desc";
        return commonDao.findList(sql, Record.class, centerguid);
    }

    public List<Record> getEquipmentList(String centerguid) {
        String sql = "select machinetype,count(1) machinecount from audit_znsb_equipment where centerguid = ? group by machinetype";
        return commonDao.findList(sql, Record.class, centerguid);
    }

    public List<Record> getTimesQueuenums(String centerguid) {
        String sql = "select CASE WHEN hour(GETNOTIME) < 9 THEN '09:00'";
        sql += " WHEN hour(GETNOTIME) = 9 THEN '10:00'";
        sql += " WHEN hour(GETNOTIME) = 10 THEN '11:00'";
        sql += " WHEN hour(GETNOTIME) = 11 THEN '12:00'";
        sql += " WHEN hour(GETNOTIME) = 12 THEN '13:00'";
        sql += " WHEN hour(GETNOTIME) = 13 THEN '14:00'";
        sql += " WHEN hour(GETNOTIME) = 14 THEN '15:00'";
        sql += " WHEN hour(GETNOTIME) = 15 THEN '16:00'";
        sql += " WHEN hour(GETNOTIME) = 16 THEN '17:00'";
        sql += " WHEN hour(GETNOTIME) >= 17 THEN '18:00'";
        sql += " END AS name,COUNT(*) AS value FROM audit_queue where centerguid =? and hour(GETNOTIME) IS NOT NULL";
        if (commonDao.isSqlserver()) {
            sql += " and  GETNOTIME>CONVERT(VARCHAR(10),GETDATE(),111) and GETNOTIME<CONVERT(VARCHAR(10),dateadd(day,1,GETDATE()),111)  ";
        }
        else if (commonDao.isOracle()) {
            sql += " and  to_char(GETNOTIME, 'YYYY-MM-DD')=to_char(sysdate, 'YYYY-MM-DD') ";
        }
        else if (commonDao.isDM()) {
            sql += " and  to_char(GETNOTIME, 'YYYY-MM-DD')=to_char(sysdate, 'YYYY-MM-DD') ";
        }
        else if ("Atlas".equalsIgnoreCase(commonDao.getDataSource().getDatabase())) {
            sql += " and  to_char(GETNOTIME, 'YYYY-MM-DD')=to_char(sysdate, 'YYYY-MM-DD') ";
        }
        else {
            sql += " and  date(GETNOTIME) = curdate()";
        }
        sql += " GROUP BY name";
        return commonDao.findList(sql, Record.class, centerguid);
    }
}
