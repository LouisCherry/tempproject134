package com.epoint.sghd.sg.impl;

import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.sghd.auditjianguan.renlingrecord.api.entity.RenlingRecord;

import java.util.List;

/**
 * 联合奖惩记录对应的后台service
 *
 * @author lizhenjie
 * @version [版本号, 2020-12-26 14:58:19]
 */
public class GxhAuditOrgaMemberService {
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public GxhAuditOrgaMemberService() {
        baseDao = CommonDao.getInstance();
    }


    public void deleteWindowTaskByWindowGuid(String windowguid) {
        String sql = "delete from audit_orga_membertask where memberguid=  ?1 ";
        baseDao.execute(sql, windowguid);
    }

    public List<Record> getRenlingListByAreacode(String s) {
        String sql = "\n" +
                "select targetOuguid from (\n" +
                "\n" +
                "SELECT\n" +
                "\ttargetOuguid,\n" +
                "\tSUM( CASE WHEN renlingtime IS NULL THEN 1 ELSE 0 END ) wrl \n" +
                "FROM\n" +
                "\t(\n" +
                "\tSELECT\n" +
                "\t\tp.rowguid,\n" +
                "\t\tp.flowsn,\n" +
                "\t\tp.projectname,\n" +
                "\t\tp.taskguid,\n" +
                "\t\tp.banjieresult,\n" +
                "\t\tp.banjiedate,\n" +
                "\t\tp.applydate,\n" +
                "\t\tfou.ouname,\n" +
                "\t\tp.applyername,\n" +
                "\t\tp.renlingtime,\n" +
                "\t\ta.task_id \n" +
                "\tFROM\n" +
                "\t\taudit_project p\n" +
                "\t\tINNER JOIN ( SELECT task_id, targetOuguid FROM audit_task_taian WHERE is_hz = 1 ) a ON a.task_id = p.task_id\n" +
                "\t\tLEFT JOIN audit_task_extension c ON p.taskguid = c.taskguid\n" +
                "\t\tINNER JOIN frame_ou fou ON fou.OUGUID = a.targetOuguid \n" +
                "\tWHERE\n" +
                "\t\tp.STATUS >= 90 \n" +
                "\t\tAND p.queueGuid IS NULL \n" +
                "\t\tAND p.areaCode =?1 \n" +
                "\t\tAND a.targetOuguid IS NOT NULL \n" +
                "\tORDER BY\n" +
                "\t\tp.applyDate DESC \n" +
                "\t) renling \n" +
                "GROUP BY\n" +
                "\ttargetOuguid) a WHERE a.wrl>0";

        return baseDao.findList(sql, Record.class, s);
    }

    public List<Record> getMemberGuidByTaskId(String taskid) {
        String sql = "select memberguid from audit_orga_membertask where taskid=?1";
        return baseDao.findList(sql, Record.class, taskid);
    }

    public List<RenlingRecord> getWfkOption() {
        String sql = "SELECT ouguid,userguid,projectguid,projectname FROM renling_record WHERE date_sub(CURDATE(), interval 6 day) = date(renlingtime) and opiniontime is  null";
        return baseDao.findList(sql, RenlingRecord.class);
    }
}
