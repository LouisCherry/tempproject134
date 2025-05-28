package com.epoint.basic.auditqueue.auditznsbmachinehelp.impl;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditqueue.auditznsbmachinehelp.domain.AuditZnsbMachineHelp;
import com.epoint.basic.auditqueue.auditznsbmachinehelp.inter.IAuditZnsbMachineHelp;
import com.epoint.basic.auditqueue.service.AuditQueueBasicService;
import com.epoint.common.service.AuditCommonResult;

/**
 * 设备维护表对应的后台service
 * 
 * @author WeiY
 * @version [版本号, 2016-11-07 14:37:54]
 */
@Component
@Service
public class AuditZnsbMachineHelpImpl implements IAuditZnsbMachineHelp {

    public AuditCommonResult<AuditZnsbMachineHelp> selectByRowguid(String rowGuid) {
        AuditQueueBasicService<AuditZnsbMachineHelp> service = new AuditQueueBasicService<AuditZnsbMachineHelp>();
        AuditCommonResult<AuditZnsbMachineHelp> result = new AuditCommonResult<AuditZnsbMachineHelp>();
        try {
            result.setResult(service.getDetail(AuditZnsbMachineHelp.class,  rowGuid, "rowGuid"));
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> update(AuditZnsbMachineHelp datebean) {
        AuditQueueBasicService<AuditZnsbMachineHelp> service = new AuditQueueBasicService<AuditZnsbMachineHelp>();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            service.updateRecord(AuditZnsbMachineHelp.class,datebean);
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> addRecord(AuditZnsbMachineHelp datebean) {
        AuditQueueBasicService<AuditZnsbMachineHelp> service = new AuditQueueBasicService<AuditZnsbMachineHelp>();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            service.addRecord(AuditZnsbMachineHelp.class,datebean);
            result.setResult("success");
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }
}
