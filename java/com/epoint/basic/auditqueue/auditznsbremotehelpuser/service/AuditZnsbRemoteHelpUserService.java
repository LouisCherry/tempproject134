package com.epoint.basic.auditqueue.auditznsbremotehelpuser.service;

import com.epoint.common.service.AuditCommonService;

public class AuditZnsbRemoteHelpUserService extends AuditCommonService
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    
    public int getAuditZnsbRemoteHelpUserByAccount(String username) {
        String sql = "select count(1) from Audit_Znsb_RemoteHelp_User where account=?1";
        return commonDao.queryInt(sql, username);
    }
    
    public int getAuditZnsbRemoteHelpUserByUserguid(String assessguid) {
        String sql = "select count(1) from Audit_Znsb_RemoteHelp_User where userguid=?1";
        return commonDao.queryInt(sql, assessguid);
    }


}
