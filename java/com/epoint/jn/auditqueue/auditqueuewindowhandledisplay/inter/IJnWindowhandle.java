package com.epoint.jn.auditqueue.auditqueuewindowhandledisplay.inter;

import java.util.List;

import com.epoint.basic.auditqueue.auditqueue.domain.AuditQueue;

public interface IJnWindowhandle
{

    List<AuditQueue> getorderedlinkauditqueue(String equipmentguid);

    List<AuditQueue> getauditqueuenohander(String equipmentguid);

}
