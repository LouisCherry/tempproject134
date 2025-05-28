package com.epoint.basic.audittask.basic.api;

import java.util.List;
import java.util.Map;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;

public interface IJnAuditTaskService
{
    public  PageData<AuditTask> getUseTaskList(String fileds, Map<String, String> map, int first,
            int pageSize, String sortField, String sortOrder);

    public List<Record> getXinYongTaskList();
    
    
}
