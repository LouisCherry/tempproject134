package com.epoint.auditqueue.auditqueuerest.pad.impl;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.auditqueue.auditqueuerest.pad.api.IJNQueuePad;
import com.epoint.basic.auditqueue.auditqueue.domain.AuditQueue;

@Component
@Service
public class JNQueuePadImpl implements IJNQueuePad
{
    @Override
    public String selectqno(String qno, String WindowGuid, String WindowNo, String CenterGuid, String UserGuid) {
        JNQueuePadService service = new JNQueuePadService();
        return service.selectqno(qno, WindowGuid, WindowNo, CenterGuid, UserGuid);
    }

    @Override
    public AuditQueue getqnodatailbyflownno(String flowno) {
        JNQueuePadService service = new JNQueuePadService();
        return service.getqnodatailbyflownno(flowno);
    }

}
