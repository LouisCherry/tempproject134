package com.epoint.auditdevice.auditdevicerest.monitordatareport.impl;

import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;

public class JNMonitorDataReportService
{
    public boolean IsTerminalRegister(String Macaddress) {
        ICommonDao commonDao = CommonDao.getInstance();
        boolean retbol = false;
        if (Macaddress.length() <= 12) {
            return retbol;
        }

        String sql = "select count(1) from Audit_Znsb_Equipment where Macaddress like ?";
        if (commonDao.queryInt(sql, "%" + Macaddress + "%") > 0) {
            retbol = true;
        }
        return retbol;
    }

    public AuditZnsbEquipment getMachinelikebyMacaddress(String Macaddress) {
        ICommonDao commonDao = CommonDao.getInstance();
        if (Macaddress.length() <= 12) {
            return null;
        }
        String sql = "select * from Audit_Znsb_Equipment where Macaddress like ?";
        return commonDao.find(sql, AuditZnsbEquipment.class, "%" + Macaddress + "%");
    }
}
