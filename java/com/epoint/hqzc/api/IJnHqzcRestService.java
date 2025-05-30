package com.epoint.hqzc.api;

import java.util.List;

import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;

public interface IJnHqzcRestService
{
    public AuditCommonResult<List<Record>> selectOuList(String areacode);
    
    public AuditCommonResult<List<Record>> getHyflList();

    public AuditCommonResult<List<Record>> getHygmList();
    
    public AuditCommonResult<List<Record>> getSmzqList();
    
    public AuditCommonResult<List<Record>> getPolicyListByContion(String ssbm, String qybq, String jnhygm, String wwsmzq, String sfsxqy, int pageIndex, int pageSize, String ouguids);

    public AuditCommonResult<Record> getPolicyListByContion(String ssbm, String qybq, String jnhygm, String wwsmzq, String sfsxqy, String ouguids);

    public AuditCommonResult<List<Record>> getPolicyListByContionFirst(String str, int pageIndex, int pageSize);
    
    public AuditCommonResult<Record> getPolicyDetailByRowguid(String rowguid);

}
