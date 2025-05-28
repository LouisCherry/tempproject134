package com.epoint.jnzwfwauditsample.auditsamplerest.sample;

import java.util.List;

import com.epoint.common.service.AuditCommonService;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;

public class JnDqxAuditZnsbCentertaskService extends AuditCommonService
{

    private static final long serialVersionUID = 1L;

    public List<Record> getSampleTaskList(String centerguid, String ouguid, String taskname, int first, int pageSize) {

        String sql = "select taskname,taskguid,task_id from Audit_Znsb_CenterTask where centerguid=?1 and is_enable=1 and IS_SHOW_SAMPLE=1";
        if (StringUtil.isNotBlank(ouguid)) {
            sql += " and ouguid=?2";
        }

        if (StringUtil.isNotBlank(taskname)) {
            sql += " and taskname like ?3";
        }
        sql += " order by ordernum desc ";
        return commonDao.findList(sql, first, pageSize, Record.class, centerguid, ouguid, "%" + taskname + "%");
    }

    public int getSampleTaskCount(String centerguid, String ouguid, String taskname) {

        String sql = "select Count(1) from Audit_Znsb_CenterTask where centerguid=?1 and is_enable=1 and IS_SHOW_SAMPLE=1";
        if (StringUtil.isNotBlank(ouguid)) {
            sql += " and ouguid=?2";
        }

        if (StringUtil.isNotBlank(taskname)) {
            sql += " and taskname like ?3";
        }

        return commonDao.queryInt(sql, centerguid, ouguid, "%" + taskname + "%");
    }

    public List<Record> getCommonSampleTaskList(String centerguid, String taskname, int first, int pageSize) {

        String sql = "select taskname,taskguid,task_id from Audit_Znsb_CenterTask where centerguid=?1 and is_enable=1 and IS_SHOW_SAMPLE=1 and IS_HOTTASK=1";
        if (StringUtil.isNotBlank(taskname)) {
            sql += " and taskname like ?2";
        }
        sql += " order by hottaskordernum desc ";
        return commonDao.findList(sql, first, pageSize, Record.class, centerguid, "%" + taskname + "%");
    }

    public int getCommonSampleTaskCount(String centerguid, String taskname) {

        String sql = "select count(1) from Audit_Znsb_CenterTask where centerguid=?1 and is_enable=1 and IS_SHOW_SAMPLE=1 and IS_HOTTASK=1";
        if (StringUtil.isNotBlank(taskname)) {
            sql += " and taskname like ?2";
        }
        return commonDao.queryInt(sql, centerguid, "%" + taskname + "%");
    }
}
