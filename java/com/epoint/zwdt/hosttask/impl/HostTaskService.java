package com.epoint.zwdt.hosttask.impl;

import java.util.List;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.hottask.domain.AuditTaskHottask;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

public class HostTaskService
{
    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public HostTaskService() {
        baseDao = CommonDao.getInstance();
    }

    public List<Record> getListAuditTaskHottask(String taskname, String areacode) {
        //String sql = "select * from audit_task_hottask where area='" + areacode + "'";
        String sql = "select t.TaskName,t.RowGuid,t.ITEM_ID,t.AREACODE,h.taskid,ID from audit_task_hottask h "
                + "INNER JOIN audit_task t ON t.task_id = h.taskid  "
                + "where IFNULL(IS_HISTORY,0) = 0 AND IS_ENABLE = 1 AND IS_EDITAFTERIMPORT = 1 AND area='" + areacode
                + "'";
        if (StringUtil.isNotBlank(taskname)) {
            sql += " and t.taskname like '%" + taskname + "%'";
        }
        return baseDao.findList(sql, Record.class, sql);
        //        //String sql = "select * from audit_task_hottask where area='" + areacode + "'";
        //        String sql = "select t.TaskName,t.RowGuid,t.ITEM_ID,t.AREACODE from audit_task_hottask h "
        //                + "INNER JOIN audit_task t ON ITEM_ID = ITEMID  "
        //                + "where IFNULL(IS_HISTORY,0) = 0 AND IS_ENABLE = 1 AND IS_EDITAFTERIMPORT = 1 AND area='" + areacode
        //                + "'";
        //        if (StringUtil.isNotBlank(taskname)) {
        //            sql += " and t.taskname like '%" + taskname + "%'";
        //        }
        //        return baseDao.findList(sql, Record.class, sql);
    }

    public FrameOu getFrameou(String ouguid) {
        String sql = "select * from frame_ou where ouguid = ?";
        return baseDao.find(sql, FrameOu.class, ouguid);
    }

    public AuditTask getTaskBasic(String rowguid) {
        String sql = "select taskname,ouname,item_id,link_tel,supervise_tel,promise_day from audit_task where rowguid = ?";
        return baseDao.find(sql, AuditTask.class, rowguid);
    }

    public AuditTask getBasicInfo(String rowguid) {
        String sql = "select item_id, areacode,taskname,type,ouname,applyertype,cross_scope,anticipate_day,promise_day,transact_time,transact_addr,id from audit_task where rowguid = ?";
        return baseDao.find(sql, AuditTask.class, rowguid);
    }

    public AuditTaskExtension getCbdwname(String rowguid) {
        String sql = "select cbdwname from audit_task_extension where taskguid = ?";
        return baseDao.find(sql, AuditTaskExtension.class, rowguid);
    }

    public AuditTask getImplementationBasis(String rowguid) {
        String sql = "select charge_flag,charge_basis,charge_standard,by_law,acceptcondition from audit_task where rowguid = ?";
        return baseDao.find(sql, AuditTask.class, rowguid);
    }

    public List<AuditTaskMaterial> geTaskMaterial(String taskguid) {
        String sql = "select * from audit_task_material where taskguid = ? ORDER BY ORDERNUM DESC ";
        return baseDao.findList(sql, AuditTaskMaterial.class, taskguid);
    }

    public Record getQcode(String taskid) {
        String sql = "select serviceMethodclientguid,serviceCodeclientguid,outervideoUrl from audit_task_taian where task_id = ?";
        return baseDao.find(sql, Record.class, taskid);
    }

    public FrameAttachInfo getFrameAttachInfoByClientguid(String clientguid) {
        String sql = "select * from Frame_attachinfo where CLIENGGUID = ?";
        return baseDao.find(sql, FrameAttachInfo.class, clientguid);
    }

    public AuditTask getAuditTaskByTaskId(String taskid) {
        String sql = "SELECT * from audit_task WHERE IFNULL(IS_HISTORY,0) ='0' AND IS_EDITAFTERIMPORT ='1' AND IS_ENABLE = '1' AND TASK_ID = ? ";
        return baseDao.find(sql, AuditTask.class, taskid);
    }
}
