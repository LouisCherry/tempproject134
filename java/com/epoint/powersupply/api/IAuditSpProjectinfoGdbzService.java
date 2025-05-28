package com.epoint.powersupply.api;

import com.epoint.powersupply.entity.AuditSpProjectinfoGdbz;

public interface IAuditSpProjectinfoGdbzService {

    int insert(AuditSpProjectinfoGdbz auditSpProjectinfoGdbz);

    int update(AuditSpProjectinfoGdbz auditSpProjectinfoGdbz);

    AuditSpProjectinfoGdbz getBySubAppGuid(String subAppGuid);
}
