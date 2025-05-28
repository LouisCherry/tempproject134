package com.epoint.basic.auditqueue.auditznsbmachinehelp.inter;

import org.springframework.stereotype.Service;

import com.epoint.basic.auditqueue.auditznsbmachinehelp.domain.AuditZnsbMachineHelp;
import com.epoint.common.service.AuditCommonResult;

/**
 * 
 * 
 * @author Administrator
 * @version [版本号, 2017-05-01 13:50:10]
 */
@Service
public interface IAuditZnsbMachineHelp 
{
   
    public AuditCommonResult<AuditZnsbMachineHelp> selectByRowguid(String rowGuid);
    public AuditCommonResult<String> update(AuditZnsbMachineHelp datebean);
    public AuditCommonResult<String> addRecord(AuditZnsbMachineHelp datebean);
}
