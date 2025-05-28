package com.epoint.auditorga.auditwindow.action;

import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.core.grammar.Record;

public interface IJNAuditWindow
{

    void Updateaudiywindow(String rowguid, String indicating, String childindicating);

    Record getauditwindow(String rowguid);

    AuditOrgaWindow getauditwindowdetail(String rowguid);

}
