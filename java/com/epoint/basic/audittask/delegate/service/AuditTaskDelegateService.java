package com.epoint.basic.audittask.delegate.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.delegate.domain.AuditTaskDelegate;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;

public class AuditTaskDelegateService
{
    /**
     * 通用dao
     */
    private ICommonDao commonDao;

    public AuditTaskDelegateService() {
        commonDao = CommonDao.getInstance("task");
    }


    public void add(AuditTaskDelegate auditTaskDelegate) {
        commonDao.insert(auditTaskDelegate);
    }

    public PageData<Record> getXZpageData(SqlConditionUtil sqlcondition, int first, int pageSize, String sortField,
            String sortOrder) {
        PageData<Record> pageData = new PageData<Record>();
        SQLManageUtil sqlManageUtil = new SQLManageUtil("task",true);
        if (StringUtil.isNotBlank(sortField) && StringUtil.isNotBlank(sortOrder)) {
            sqlcondition.setOrder(sortField, sortOrder);
        }
        Map<String, String> map = sqlcondition.getMap();
        String condition = sqlManageUtil.buildSql(map);
        condition = condition.replaceAll("where 1=1", "");
        String sqlcg = "and ((select count(1) from audit_task where task_id=t.task_id)=1 or IS_EDITAFTERIMPORT=1) ";
        String sql = "select T.taskname,D.delegatetype,item_id,T.ouname,D.xzorder,status,T.IS_EDITAFTERIMPORT,D.TASKID,T.rowguid as ROWGUID,T.task_id,T.IS_ENABLE  from audit_task_delegate d,audit_task t where d.TASKID=t.TASK_ID and IS_EDITAFTERIMPORT<4 AND (D .DELEGATETYPE = '10' OR ( D.DELEGATETYPE >'10' AND T.IS_ENABLE = '1'))and d.STATUS='1' "
                +sqlcg + condition;
        pageData.setList(commonDao.findList(sql, first, pageSize, Record.class));
        pageData.setRowCount(commonDao.findList(sql, Record.class).size());
        return pageData;
    }
    public PageData<Record> getCJpageData(SqlConditionUtil sqlcondition, int first, int pageSize, String sortField,
            String sortOrder) {
        PageData<Record> pageData = new PageData<Record>();
        SQLManageUtil sqlManageUtil = new SQLManageUtil("task",true);
        if (StringUtil.isNotBlank(sortField) && StringUtil.isNotBlank(sortOrder)) {
            sqlcondition.setOrder(sortField, sortOrder);
        }
        Map<String, String> map = sqlcondition.getMap();
        String condition = sqlManageUtil.buildSql(map);
        condition = condition.replaceAll("where 1=1", "");
        String sql = "select T.taskname,D.delegatetype,item_id,T.ouname,D.xzorder,status,T.IS_EDITAFTERIMPORT,D.TASKID,T.rowguid as ROWGUID,T.task_id,T.IS_ENABLE  from audit_task_delegate d,audit_task t where d.TASKID=t.TASK_ID and (IS_HISTORY='0' or (IS_EDITAFTERIMPORT='1' and IS_HISTORY is null)) and IS_EDITAFTERIMPORT<5 AND (D .DELEGATETYPE = '50' OR ( D.DELEGATETYPE >'10' AND T.IS_ENABLE = '1'))and d.STATUS='1' "
                + condition;
        pageData.setList(commonDao.findList(sql, first, pageSize, Record.class));
        pageData.setRowCount(commonDao.findList(sql, Record.class).size());
        return pageData;
    }
    public AuditTaskDelegate findByTaskIDAndAreacode(String taskID, String areacode) {
        return commonDao.find("select * from audit_task_delegate where taskid=?1 and areacode=?2",
                AuditTaskDelegate.class, taskID, areacode);
    }

    public void updata(AuditTaskDelegate bean) {
        commonDao.update(bean);
    }

    public List<AuditTask> getTaskListByArea(String areacode) {
        SqlConditionUtil sqlcondition = new SqlConditionUtil();
        SQLManageUtil sqlManageUtil = new SQLManageUtil("task",true);
        sqlcondition.eq("d.areacode", areacode);
        sqlcondition.eq("d.status", "1");
        sqlcondition.eq("IS_EDITAFTERIMPORT", "1");
        sqlcondition.eq("d.isallowaccept", "1");
        String condition = sqlManageUtil.buildPatchSql(sqlcondition.getMap());
        String sql = "select t.* from audit_task_delegate d,audit_task t where d.TASKID=t.TASK_ID and (IS_HISTORY='0' or (IS_EDITAFTERIMPORT='1' and IS_HISTORY is null)) and T.IS_ENABLE='1'"
                + condition;
        return commonDao.findList(sql, AuditTask.class);
    }

    public List<AuditTask> selectAuditTaskByCondition(String condition, String areacode) {
        SqlConditionUtil sqlcondition = new SqlConditionUtil();
        SQLManageUtil sqlManageUtil = new SQLManageUtil("task",true);
        sqlcondition.eq("d.areacode", areacode);
        sqlcondition.eq("d.status", "1");
        sqlcondition.eq("IS_EDITAFTERIMPORT", "1");
        sqlcondition.eq("d.isallowaccept", "1");
        List<Object> params = new ArrayList<Object>();
        String conditionmap = sqlManageUtil.buildPatchSql(sqlcondition.getMap());
        String sql = "select t.* from audit_task_delegate d,audit_task t where d.TASKID=t.TASK_ID and (IS_HISTORY='0' or (IS_EDITAFTERIMPORT='1' and IS_HISTORY is null)) and t.is_enable='1' and t.taskname like ?" ;
        params.add("%"+condition.replace("\\", "\\\\").replace("%", "\\%") + "%");
        sql += conditionmap;
        return commonDao.findList(sql, AuditTask.class,params.toArray());
    }

    public List<AuditTaskDelegate> findByTaskID(String taskid) {
        return commonDao.findList("select * from audit_task_delegate where taskid=? ", AuditTaskDelegate.class,
                taskid);
    }

    public List<AuditTaskDelegate> selectDelegateListByTaskIDAndAreacode(String taskid, String areacode) {
        String sql = "select * from audit_task_delegate where taskid = ? and areacode = ? ";
        return commonDao.findList(sql, AuditTaskDelegate.class, taskid, areacode);
    }

    public AuditTask selectUsableTaskByTaskID(String taskid, String areacode) {
        AuditTask audittask = null;
        String sql = "select t.* from audit_task_delegate d,audit_task t where d.taskid=t.task_id and task_id=? and (is_history=0 or is_history is null) and is_editafterimport=1 and d.status = '1' and d.isallowaccept = '1' and d.areacode =? ";
        List<AuditTask> list = commonDao.findList(sql, AuditTask.class,taskid,areacode);
        if (list != null && list.size() > 0) {
            audittask = list.get(0);
        }
        return audittask;
    }

    public List<AuditTaskDelegate> getDelegateByAreacode(String areacode) {
        String sql = "select * from audit_task_delegate where areacode=?";
        return commonDao.findList(sql, AuditTaskDelegate.class, areacode);
    }

    public PageData<AuditTask> getXZpageDataList(SqlConditionUtil sqlcondition, int first, int pageSize,
            String sortField, String sortOrder) {
        PageData<AuditTask> pageData = new PageData<AuditTask>();
        SQLManageUtil sqlManageUtil = new SQLManageUtil("task", true);
        Map<String, String> map = sqlcondition.getMap();
        String condition = sqlManageUtil.buildSql(map);
        condition = condition.replaceAll("where 1=1", "");
        String leftJoin = "";
        // 如果查询条件中包含了分类信息，则将表audit_task_map拼入查询sql
        if (map.containsKey("dict_id#zwfw#eq#zwfw#S")) {
            leftJoin = "LEFT JOIN audit_task_map c ON t.TASK_ID = c.TASK_ID";
        }
        String sql = "select T.taskname,D.delegatetype,item_id,D.ouname,D.xzorder,status,T.IS_EDITAFTERIMPORT,D.TASKID,T.rowguid as ROWGUID,T.task_id,T.IS_ENABLE  from  audit_task t LEFT JOIN AUDIT_TASK_EXTENSION e ON t.ROWGUID = e.TASKGUID "+ leftJoin +" INNER JOIN audit_task_delegate d ON t.Task_id = d.TASKID where d.TASKID=t.TASK_ID and IS_EDITAFTERIMPORT<5 AND (D .DELEGATETYPE = '10' OR ( D.DELEGATETYPE >'10' AND T.IS_ENABLE = '1'))and d.STATUS='1' "
                + condition + " order by "+ sortField +" "+ sortOrder;
        pageData.setList(commonDao.findList(sql, first, pageSize, AuditTask.class));
        pageData.setRowCount(commonDao.findList(sql, AuditTask.class).size());
        return pageData;
    }
}
