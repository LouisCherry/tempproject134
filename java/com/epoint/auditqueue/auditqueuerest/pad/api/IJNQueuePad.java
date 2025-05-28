package com.epoint.auditqueue.auditqueuerest.pad.api;

import com.epoint.basic.auditqueue.auditqueue.domain.AuditQueue;

public interface IJNQueuePad
{

    String selectqno(String qno, String WindowGuid, String WindowNo, String CenterGuid, String UserGuid);

    AuditQueue getqnodatailbyflownno(String flowno);

}
