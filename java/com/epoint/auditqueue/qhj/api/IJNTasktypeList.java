package com.epoint.auditqueue.qhj.api;

import java.util.List;

import com.epoint.basic.auditqueue.auditqueuetasktype.domain.AuditQueueTasktype;
import com.epoint.core.grammar.Record;

public interface IJNTasktypeList
{

    List<Record> getWindowLinkedTskType(String centerguid, String ouguid, int firstpage, int pagesize);

    int getWindowLinkedTskTypeCount(String centerguid, String ouguid);

}
