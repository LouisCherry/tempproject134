package com.epoint.basic.auditqueue.auditznsbremotehelp.service;

import com.epoint.common.service.AuditCommonService;

public class AuditZnsbRemoteHelpService extends AuditCommonService
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    

    public int getAuditZnsbRemoteHelpByAccount(String account) {
        String sql = "select count(1) from Audit_Znsb_RemoteHelp where account=?1";
        return commonDao.queryInt(sql, account);
    }
    
    public int getAuditZnsbRemoteHelpByMachineGuid(String machineguid) {
        String sql = "select count(1) from Audit_Znsb_RemoteHelp where machineguid=?1";
        return commonDao.queryInt(sql, machineguid);
    }

}
