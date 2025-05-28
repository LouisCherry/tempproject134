package com.epoint.auditorga.auditwindow.api;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;

import java.util.List;

public interface IJnGxhTaskConfigService {

    List<AuditProject> getAuditProjectListByTaskid(String taskid);

}
