package com.epoint.auditsp.auditsphandle.api;

import java.util.List;

import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;

public interface IAuditSpITaskfwxm
{
    public abstract AuditCommonResult<String> addTaskInstance(String paramString1, String paramString2,
            String paramString3, String paramString4, String paramString5, String paramString6, Integer paramInteger,
            String fwxmguid);

    public abstract AuditCommonResult<List<AuditSpITask>> getTaskInstanceBySubappGuid(String paramString);

    public abstract AuditCommonResult<List<AuditTaskMaterial>> getTaskInstanceBytaskguid(String paramString);

    public abstract String findPhase(String phaseguid);

    public abstract Record findTaskname(String materialguid, String string);
    
    public abstract Record findTasknameByTaskguid(String taskGuid);

    public abstract String findPhaseBysubappguid(String subAppGuid);

}
