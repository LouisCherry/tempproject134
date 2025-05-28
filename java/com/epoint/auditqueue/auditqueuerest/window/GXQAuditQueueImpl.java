package com.epoint.auditqueue.auditqueuerest.window;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditqueue.auditqueue.domain.AuditQueue;
import com.epoint.core.grammar.Record;

@Component
@Service
public class GXQAuditQueueImpl implements IGXQAuditQueue
{
   
     @Override
     public List<Record> getHandlingQueue(String hallguid){
         GXQAuditQueueService service = new GXQAuditQueueService();
         return service.getHandlingQueue(hallguid);
     }
}
