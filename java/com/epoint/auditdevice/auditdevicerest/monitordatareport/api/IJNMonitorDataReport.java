package com.epoint.auditdevice.auditdevicerest.monitordatareport.api;

import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;

public interface IJNMonitorDataReport
{

    boolean IsTerminalRegister(String Macaddress);

    AuditZnsbEquipment getMachinelikebyMacaddress(String Macaddress);

}
