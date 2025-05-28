package com.epoint.auditsp.auditsptask.api;

import java.util.List;

import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.core.grammar.Record;

public interface IJNAuditWindowTaskService
{

	List<AuditOrgaArea> getAllArea();

	String getCenterguidBytaskguid(String guiud);


}
