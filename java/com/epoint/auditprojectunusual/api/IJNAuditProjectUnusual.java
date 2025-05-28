package com.epoint.auditprojectunusual.api;

import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;

import java.util.List;

public interface IJNAuditProjectUnusual {
    /**
     * 查询办件暂停记录
     * @param projectguid
     * @return
     */
    public List<AuditProjectUnusual> getZantingData(String projectguid);


}
