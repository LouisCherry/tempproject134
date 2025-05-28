package com.epoint.taskcase.api;

import java.util.List;

import com.epoint.basic.audittask.material.domain.AuditTaskCase;

public interface ITaskCaseService
{

    AuditTaskCase Check(String taskguid, String casename, String casecode);

    List<String> findTaskGuid();

}
