package com.epoint.auditsp.auditsphandle.api;

import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;

import java.util.List;

public interface IJnAuditSpInstance
{
    public void updateSpInstance(AuditSpInstance ins);
    
    public List<AuditSpInstance> getAuditSpInstanceByPage(int first, int pageSize, String applyername,String itemname);
    
    public Integer getAuditSpInstanceCount( String applyername,String itemname);

    List<AuditSpInstance> getAuditSpInstanceListBySearch(int first, int pageSize, String itemcode, String itemname, String areacode);

    int getAuditSpInstanceCountNew(String itemcode, String itemname, String areacode);

}
