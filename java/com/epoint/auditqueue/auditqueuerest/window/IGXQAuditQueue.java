package com.epoint.auditqueue.auditqueuerest.window;

import java.util.List;

import com.epoint.basic.auditqueue.auditqueue.domain.AuditQueue;
import com.epoint.core.grammar.Record;

public interface IGXQAuditQueue
{

   

    List<Record> getHandlingQueue(String hallguid);


    

}
