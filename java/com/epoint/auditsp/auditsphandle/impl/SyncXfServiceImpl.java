package com.epoint.auditsp.auditsphandle.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.auditsp.auditsphandle.api.ISyncXfService;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import org.springframework.stereotype.Component;

@Component
@Service
public class SyncXfServiceImpl implements ISyncXfService {
    @Override
    public PageData<Record> geXflistByXmdm(int first, int pageSize, String sortField, String sortOrder, String xmdm) {
        return new SyncXfService().geXflistByXmdm(first,pageSize,sortField,sortOrder,xmdm);
    }
}
