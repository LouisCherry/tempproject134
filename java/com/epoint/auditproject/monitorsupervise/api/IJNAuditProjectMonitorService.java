package com.epoint.auditproject.monitorsupervise.api;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;

import java.util.Map;

public interface IJNAuditProjectMonitorService {

    String findsupervise(String userGuid, String string);

    public AuditCommonResult<PageData<AuditProject>> getAuditProjectPageDataMonitor(String fieldstr,
                                                                                    Map<String, String> conditionMap, String userguid, Integer ismaterial, Integer firstResult, Integer maxResults,
                                                                                    String sortField, String sortOrder);

    public AuditCommonResult<PageData<auditprojectold>> getAuditProjectPageDataMonitorOld(String fieldstr,
                                                                                          Map<String, String> conditionMap, Integer firstResult, Integer maxResults,
                                                                                          String sortField, String sortOrder);

    public auditprojectold getAuditProjectByRowGuid(String projectguid);

    public AuditProject getOldProjectByRowGuid(String projectguid);
}
