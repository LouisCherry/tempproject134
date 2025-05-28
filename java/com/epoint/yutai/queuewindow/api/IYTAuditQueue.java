package com.epoint.yutai.queuewindow.api;

import com.epoint.basic.auditqueue.auditqueue.domain.AuditQueue;
import com.epoint.basic.auditqueue.auditqueuehostory.domain.AuditQueueHistory;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IYTAuditQueue
{
    public List<String> getWaitQno(String queuevalue,String windowguid);
}
