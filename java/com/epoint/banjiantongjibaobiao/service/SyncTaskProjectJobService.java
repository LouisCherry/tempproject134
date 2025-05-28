package com.epoint.banjiantongjibaobiao.service;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.date.EpointDateUtil;

import java.util.Date;
import java.util.List;

/**
 * 事项统计表service
 *
 * @author Epoint
 */
public class SyncTaskProjectJobService {
    private ICommonDao baseDao;

    public SyncTaskProjectJobService() {
        if (baseDao == null) {
            this.baseDao = CommonDao.getInstance();
        }
    }


    public Record getLastDate() {
        String sql = "select max(ttp.`date`) as maxDate ,count(1) as counts from tjfx_task_project ttp";
        return baseDao.find(sql, Record.class);
    }

    public Date getMinDate() {
        String sql = "select min(applydate) as mindate  from audit_project";
        return baseDao.find(sql, Date.class);
    }

    public List<String> getOuGuidList(Date startDate) {
        String sql = "select distinct ouguid from audit_project ap where ap.applydate is not null and  datediff(date_format( APPLYDATE , '%Y-%m-%d'),?)=0 ";
        return baseDao.findList(sql, String.class, startDate);
    }

    public List<Record> getInfos(Date startDate, String ouGuid) {
        String sql = "select ouname, projectname, taskguid, tasktype, applydate, areacode, status, count(1) as totalcount   from audit_project ap where datediff(date_format( APPLYDATE , '%Y-%m-%d'),?)= 0 and ouguid =? group by PROJECTNAME, status, tasktype ";
        return baseDao.findList(sql, Record.class, startDate, ouGuid);
    }

    public int deleteExistDate(Date startDate, String ouGuid) {
        String sql = "delete from tjfx_task_project  where datediff(`date`, ?)= 0 and ouguid = ?";
        return baseDao.execute(sql, startDate, ouGuid);
    }

    public void insertNewRecord(Record value) {
        baseDao.insert(value);
    }



    /* 分库分表 */
    public List<Record> getTjInfo(Date startDate, String ouGuid) {
        // 获取查询日期的月数
        int monthOfDate = EpointDateUtil.getMonthOfDate(startDate) + 1;
        // 设置外部办件基本信息表的表名和外部办件扩展表的表名
        String external_info = "external_project_info_" + monthOfDate;
        String external_info_ext = "external_project_info_ext_" + monthOfDate;
        String sql = "select rowguid,ap.OUGUID as ouguid, ouname, projectname, taskguid, tasktype, applydate, areacode, status, count(1) as totalcount, ( select count(1) from " + external_info + " epi, " + external_info_ext + " epie where epi.task_id = ap.TASK_ID and epi.accept_ou_guid =? and epi.rowguid = epie.project_guid and datediff( epie.applydate ,?)= 0 and epie.status = ap.STATUS) as wbdrbjl from audit_project ap where datediff(date_format( APPLYDATE , '%Y-%m-%d'),?)= 0 and ap.ouguid = ? group by projectname, status, tasktype order by ap.applydate desc; ";
        return baseDao.findList(sql, Record.class, ouGuid, startDate, startDate, ouGuid);
    }
}
