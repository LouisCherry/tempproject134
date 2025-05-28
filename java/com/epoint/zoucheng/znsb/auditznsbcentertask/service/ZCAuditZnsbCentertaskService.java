package com.epoint.zoucheng.znsb.auditznsbcentertask.service;

import java.util.List;

import com.epoint.common.service.AuditCommonService;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;

public class ZCAuditZnsbCentertaskService extends AuditCommonService
{

    private static final long serialVersionUID = 1L;

    public boolean ISExistbyTaskId(String TaskId, String centerguid) {
        boolean retbol = false;
        String sql = "select count(1) from Audit_Znsb_CenterTask where task_id=?1 and centerguid=?2";
        if (commonDao.queryInt(sql, TaskId, centerguid) > 0) {
            retbol = true;
        }
        return retbol;
    }

    public void updateedit(String edit, String centerguid) {
        String sql = "update Audit_Znsb_CenterTask set is_edit=?1 where centerguid=?2";

        commonDao.execute(sql, edit, centerguid);

    }

    public void updateeditbytaskid(String edit, String taskid, String centerguid) {
        String sql = "update Audit_Znsb_CenterTask set is_edit=?1 where task_id=?2 and  centerguid=?3";

        commonDao.execute(sql, edit, taskid, centerguid);

    }

    public void updatehottask(String taskid, String ishottask, Integer hottaskordernum) {
        String sql = "update Audit_Znsb_CenterTask set is_hottask=?2,hottaskordernum=?3 where task_id=?1";
        commonDao.execute(sql, taskid, ishottask, hottaskordernum);

    }

    public void updatenohottask(String areacode) {
        String sql = "update Audit_Znsb_CenterTask set is_hottask='' where areacode=?1";
        commonDao.execute(sql, areacode);

    }

    public void deletebyedit(String edit, String centerguid) {
        String sql = "delete from Audit_Znsb_CenterTask where is_edit=?1 and centerguid=?2 ";

        commonDao.execute(sql, edit, centerguid);

    }

    public void deletebytaskguid(String taskguid) {
        String sql = "delete from Audit_Znsb_CenterTask where taskguid=?1 ";

        commonDao.execute(sql, taskguid);

    }

    public void deletebyTaskId(String TaskId, String CenterGuid) {
        String sql = "delete from Audit_Znsb_CenterTask where task_id=?1 and CenterGuid=?2";

        commonDao.execute(sql, TaskId, CenterGuid);

    }

    public List<String> getTaskOUList(String centerguid, String applyertype, int first, int pageSize) {
        String sql = "";
        if (StringUtil.isNotBlank(applyertype)) {
            sql = "select DISTINCT ouguid from Audit_Znsb_CenterTask where centerguid=?1 and applyertype like?2 and is_enable=1";
        }
        else {
            sql = "select DISTINCT ouguid from Audit_Znsb_CenterTask where centerguid=?1 and  is_enable=1";
        }
        return commonDao.findList(sql, first, pageSize, String.class, centerguid, "%" + applyertype + "%");
    }

    public List<String> getTaskOUList(String centerguid, String applyertype) {
        String sql = "";
        if (StringUtil.isNotBlank(applyertype)) {
            sql = "select DISTINCT ouguid from Audit_Znsb_CenterTask where centerguid=?1 and applyertype like?2 and is_enable=1";
        }
        else {
            sql = "select DISTINCT ouguid from Audit_Znsb_CenterTask where centerguid=?1 and  is_enable=1";
        }
        return commonDao.findList(sql, String.class, centerguid, "%" + applyertype + "%");
    }

    public List<String> getTaskOUListByOuName(String centerguid, String applyertype, String ouname) {
        String sql = "";
        if (StringUtil.isNotBlank(applyertype)) {
            sql = "select DISTINCT ouguid from Audit_Znsb_CenterTask where centerguid=?1 and applyertype like?2 and is_enable=1";
        }
        else {
            sql = "select DISTINCT ouguid from Audit_Znsb_CenterTask where centerguid=?1 and  is_enable=1";
        }
        if(StringUtil.isNotBlank(ouname)){
            sql = sql + " and OUNAME like ?3 ";
        }
        return commonDao.findList(sql, String.class, centerguid, "%" + applyertype + "%", "%" + ouname + "%");
    }
    
    public int getTaskOUListCount(String centerguid, String applyertype) {
        String sql = "";
        if (StringUtil.isNotBlank(applyertype)) {
            sql = "select Count(1) from ( select DISTINCT ouguid from Audit_Znsb_CenterTask where centerguid=?1 and applyertype like?2 and is_enable=1) a";
        }
        else {
            sql = "select Count(1) from ( select DISTINCT ouguid from Audit_Znsb_CenterTask where centerguid=?1  and is_enable=1) a";
        }
        return commonDao.queryInt(sql, centerguid, "%" + applyertype + "%");
    }

    public List<String> getSampleOUList(String centerguid, int first, int pageSize) {

        String sql = "select DISTINCT ouguid from Audit_Znsb_CenterTask where centerguid=?1 and  is_enable=1 and IS_SHOW_SAMPLE=1";

        return commonDao.findList(sql, first, pageSize, String.class, centerguid);
    }

    public int getSampleOUListCount(String centerguid) {
        String sql = "select Count(1) from ( select DISTINCT ouguid from Audit_Znsb_CenterTask where centerguid=?1 and is_enable=1 and IS_SHOW_SAMPLE=1) a";

        return commonDao.queryInt(sql, centerguid);
    }

    public List<Record> getSampleTaskList(String centerguid, String ouguid, String taskname, int first, int pageSize) {

        String sql = "select taskname,taskguid from Audit_Znsb_CenterTask where centerguid=?1 and is_enable=1 and IS_SHOW_SAMPLE=1";
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

        String sql = "select taskname,taskguid from Audit_Znsb_CenterTask where centerguid=?1 and is_enable=1 and IS_SHOW_SAMPLE=1 and IS_HOTTASK=1";
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
