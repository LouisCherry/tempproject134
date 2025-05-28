package com.epoint.auditdevice.auditdevicerest.monitordatareport.impl;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.auditdevice.auditdevicerest.monitordatareport.api.IJNMonitorDataReport;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;

@Component
@Service
public class JNMonitorDataReportImpl implements IJNMonitorDataReport
{
    @Override
    public boolean IsTerminalRegister(String Macaddress) {
        JNMonitorDataReportService service = new JNMonitorDataReportService();
        return service.IsTerminalRegister(Macaddress);
    }

    @Override
    public AuditZnsbEquipment getMachinelikebyMacaddress(String Macaddress) {
        JNMonitorDataReportService service = new JNMonitorDataReportService();
        return service.getMachinelikebyMacaddress(Macaddress);
    }
}
