package com.epoint.auditselfservice.service;

import java.util.List;

import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;

public interface IJNTask
{

    List<AuditTaskMaterial> getOrderdTaskMaterial(String taskguid);

}
