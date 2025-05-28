package com.epoint.auditorga.leaderindex.service;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;

public class JNLeaderIndexService
{

    public String getQueuecount(String string) {
        String sql = "SELECT count(1) from audit_queue_history where GETNOTIME>=?";
        return CommonDao.getInstance().find(sql, String.class,string);
    }

    public AuditProject getSatisfiedCount(String areacode) {
        String sql = "select count(case when e.satisfied > '3' THEN 1 else 0 END) as satiscount from audit_online_evaluat e left join audit_project p on e.ClientIdentifier = p.rowguid where clienttype = '10' "
                + " and DATE_SUB(CURDATE(), INTERVAL 1 MONTH) <= date(evaluatedate)  and p.areacode = ?1";
        return CommonDao.getInstance().find(sql, AuditProject.class, areacode);
    }

    public Record getTotalSatisfiedCount(String areacode) {
        String sql = "select count(1) as totalAudit from audit_project where status > 10 and status <> 20 and status <> 22 "
                + " and areacode=?1 and DATE_SUB(CURDATE(), INTERVAL 1 MONTH) <= date(OperateDate) ";
        return CommonDao.getInstance().find(sql, AuditProject.class, areacode);
    }

    public Record getSpProject(String areacode) {
        String sql = " SELECT count(case when DATE_FORMAT(CURDATE(),'%Y-%m')=DATE_FORMAT(OperateDate,'%Y-%m') then 1 end) as '0' ,"
                + " count(case when DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 1 MONTH),'%Y-%m')=DATE_FORMAT(OperateDate,'%Y-%m') then 1 end) as '1'  ,"
                + " count(case when DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 2 MONTH),'%Y-%m')=DATE_FORMAT(OperateDate,'%Y-%m') then 1 end) as '2'  ,"
                + " count(case when DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 3 MONTH),'%Y-%m')=DATE_FORMAT(OperateDate,'%Y-%m') then 1 end) as '3'  ,"
                + " count(case when DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 4 MONTH),'%Y-%m')=DATE_FORMAT(OperateDate,'%Y-%m') then 1 end) as '4'  ,"
                + " count(case when DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 5 MONTH),'%Y-%m')=DATE_FORMAT(OperateDate,'%Y-%m') then 1 end) as '5'  "
                + " from audit_project where BUSINESSGUID is not null and AREACODE=?1 ";
        return CommonDao.getInstance().find(sql, AuditProject.class, areacode);
    }

}
