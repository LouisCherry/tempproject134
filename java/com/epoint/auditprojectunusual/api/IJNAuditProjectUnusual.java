package com.epoint.auditprojectunusual.api;

import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;

import java.util.List;

public interface IJNAuditProjectUnusual {
    /**
     * 
     * @param projectguid
     * @return
     */
    public List<AuditProjectUnusual> getZantingData(String projectguid);
}
