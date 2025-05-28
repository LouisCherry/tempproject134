package com.epoint.auditprojectunusual.imp;

import com.epoint.auditprojectunusual.api.IJNAuditProjectUnusual;
import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service
public class JNAuditProjectUnusualImpl implements IJNAuditProjectUnusual
{

    @Override
    public List<AuditProjectUnusual> getZantingData(String projectguid){
        return new JNAuditProjectUnusualService().getZantingData(projectguid);
    }
}
