package com.epoint.auditorga.auditwindow.action;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.core.grammar.Record;

@Component
@Service
public class JNAuditWindowImpl implements IJNAuditWindow
{
    @Override
    public void Updateaudiywindow(String rowguid,String indicating,String childindicating) {
        JNAuditWindowService service = new JNAuditWindowService();
        service.Updateaudiywindow(rowguid, indicating, childindicating);
    }
    @Override
    public Record getauditwindow(String rowguid) {
        JNAuditWindowService service = new JNAuditWindowService();
        return  service.getauditwindow(rowguid);
    }
    @Override
    public AuditOrgaWindow getauditwindowdetail(String rowguid) {
        JNAuditWindowService service = new JNAuditWindowService();
        return  service.getauditwindowdetail(rowguid);
    }
}
