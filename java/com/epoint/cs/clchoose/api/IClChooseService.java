package com.epoint.cs.clchoose.api;

import java.util.List;

import com.epoint.basic.auditsp.auditspimaterial.domain.AuditSpIMaterial;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;

public interface IClChooseService
{

    List<AuditTaskMaterial> findList(String taskguid);

    AuditTaskMaterial findMaterial(String id);

    void insert(AuditSpIMaterial auditSpIMaterial);

}
