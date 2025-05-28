package com.epoint.auditqueue.auditqueuetasktype.action;

import com.epoint.basic.audittask.basic.domain.AuditTask;

import java.util.List;

public interface ITasktype {

    List<AuditTask> gettasklist(String type, String params);

}
