package com.epoint.powersupply.impl;

import com.epoint.powersupply.api.IAuditSpProjectinfoGdbzService;
import com.epoint.powersupply.entity.AuditSpProjectinfoGdbz;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class AuditSpProjectinfoGdbzServiceImpl implements IAuditSpProjectinfoGdbzService {
    @Override
    public int insert(AuditSpProjectinfoGdbz auditSpProjectinfoGdbz) {
        return new AuditSpProjectinfoGdbzService().insert(auditSpProjectinfoGdbz);
    }

    @Override
    public int update(AuditSpProjectinfoGdbz auditSpProjectinfoGdbz) {
        return new AuditSpProjectinfoGdbzService().update(auditSpProjectinfoGdbz);
    }

    @Override
    public AuditSpProjectinfoGdbz getBySubAppGuid(String subAppGuid) {
        return new AuditSpProjectinfoGdbzService().getBySubAppGuid(subAppGuid);
    }
}
