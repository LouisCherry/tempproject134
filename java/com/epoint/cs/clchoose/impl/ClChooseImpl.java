package com.epoint.cs.clchoose.impl;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.epoint.basic.auditsp.auditspimaterial.domain.AuditSpIMaterial;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.cs.clchoose.api.IClChooseService;

@Component
@Service
public class ClChooseImpl implements IClChooseService
{

    @Override
    public List<AuditTaskMaterial> findList(String taskguid) {
        String sql = "SELECT a.rowguid,a.materialname from audit_task_material a JOIN audit_task b on a.taskguid=b.rowguid where (b.IS_HISTORY=0 or b.IS_HISTORY='' OR b.IS_HISTORY IS NULL) and b.IS_EDITAFTERIMPORT=1 and b.IS_ENABLE=1 and b.task_id=?";
        return CommonDao.getInstance().findList(sql, AuditTaskMaterial.class, taskguid);
    }

    @Override
    public AuditTaskMaterial findMaterial(String id) {
        String sql = "select * from audit_task_material where rowguid=?";
        return CommonDao.getInstance().find(sql, AuditTaskMaterial.class, id);
    }

    @Override
    public void insert(AuditSpIMaterial auditSpIMaterial) {
        CommonDao.getInstance().insert(auditSpIMaterial);
    }

}
