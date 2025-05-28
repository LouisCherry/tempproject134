package com.epoint.jn.auditqueue.auditqueuewindowhandledisplay.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditqueue.auditqueue.domain.AuditQueue;
import com.epoint.jn.auditqueue.auditqueuewindowhandledisplay.inter.IJnWindowhandle;

@Component
@Service
public class JnWindowhandleImpl implements IJnWindowhandle
{
    @Override
    public List<AuditQueue> getorderedlinkauditqueue(String equipmentguid){
        JnWindowhandleService service = new JnWindowhandleService();
        return service.getorderedlinkauditqueue(equipmentguid);
    }
    @Override
    public List<AuditQueue> getauditqueuenohander(String equipmentguid){
        JnWindowhandleService service = new JnWindowhandleService();
        return service.getauditqueuenohander(equipmentguid);
    }
}
