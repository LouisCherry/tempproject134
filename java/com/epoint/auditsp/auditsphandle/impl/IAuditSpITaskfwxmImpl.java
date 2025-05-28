package com.epoint.auditsp.auditsphandle.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.auditsp.auditsphandle.api.IAuditSpITaskfwxm;
import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.auditsp.service.AuditSpService;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;

@Component
@Service
public class IAuditSpITaskfwxmImpl implements IAuditSpITaskfwxm
{

    public AuditCommonResult<String> addTaskInstance(String businessGuid, String biGuid, String phaseGuid,
            String taskGuid, String taskName, String subappGuid, Integer orderNum, String fwxmguid) {
        AuditSpService<AuditSpITask> auditSpService = new AuditSpService();
        AuditSpITask auditSpITask = new AuditSpITask();
        AuditCommonResult<String> result = new AuditCommonResult();
        try {
            auditSpITask.setOperatedate(new Date());
            auditSpITask.setBiguid(biGuid);
            auditSpITask.setBusinessguid(businessGuid);
            auditSpITask.setPhaseguid(phaseGuid);
            auditSpITask.setRowguid(UUID.randomUUID().toString());
            auditSpITask.setSubappguid(subappGuid);
            auditSpITask.setTaskguid(taskGuid);
            auditSpITask.setTaskname(taskName);
            auditSpITask.setOrdernumber(orderNum);
            auditSpITask.set("fwxmguid", fwxmguid);
            auditSpService.addRecord(AuditSpITask.class, auditSpITask, false);
        }
        catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    public AuditCommonResult<List<AuditSpITask>> getTaskInstanceBySubappGuid(String subappGuid) {
        AuditSpService<AuditSpITask> auditSpService = new AuditSpService();
        AuditCommonResult<List<AuditSpITask>> result = new AuditCommonResult();
        try {
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("subappguid", subappGuid);
            sql.nq("fwxmguid", null);
            result.setResult(auditSpService.getAllRecord(AuditSpITask.class, sql.getMap()));
        }
        catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    public AuditCommonResult<List<AuditTaskMaterial>> getTaskInstanceBytaskguid(String taskguid) {
        AuditSpService<AuditTaskMaterial> auditTaskMaterialService = new AuditSpService();
        AuditCommonResult<List<AuditTaskMaterial>> result = new AuditCommonResult();
        try {
            SqlConditionUtil sql = new SqlConditionUtil();
            sql.eq("taskguid", taskguid);
            result.setResult(auditTaskMaterialService.getAllRecord(AuditTaskMaterial.class, sql.getMap()));
        }
        catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public String findPhase(String materialguid) {
        String sql = "select PHASENAME from AUDIT_SP_PHASE where rowguid=?";
        return CommonDao.getInstance().find(sql, String.class, materialguid);
    }

    @Override
    public Record findTaskname(String phaseguid,String string) {
        String sql = "SELECT a.taskname,b.file_source from audit_sp_i_task a join audit_task_material b on a.taskguid=b.taskguid" 
                +" and b.MATERIALID=? and a.subappguid =?";
        return CommonDao.getInstance().find(sql, Record.class, phaseguid,string);
    }
    
    @Override
    public Record findTasknameByTaskguid(String taskGuid) {
        String sql = "SELECT b.MATERIALNAME,a.taskname,b.file_source,b.necessity from audit_task a join audit_task_material b on a.rowguid=b.taskguid where a.rowguid = ?";
        return CommonDao.getInstance().find(sql, Record.class, taskGuid);
    }

    @Override
    public String findPhaseBysubappguid(String subAppGuid) {
        String sql = "SELECT PHASEGUID from audit_sp_i_subapp where RowGuid=?";
        return CommonDao.getInstance().find(sql, String.class, subAppGuid);
    }

}
