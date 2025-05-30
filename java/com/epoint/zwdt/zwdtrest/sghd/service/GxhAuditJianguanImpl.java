package com.epoint.zwdt.zwdtrest.sghd.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zwdt.zwdtrest.sghd.api.IGxhAuditJianguan;
import org.springframework.stereotype.Component;

@Component
@Service
public class GxhAuditJianguanImpl implements IGxhAuditJianguan {

    @Override
    public PageData<Record> getAuditJianguanPageData(Record record, int pageindex, int pagesize, String sortField, String sortOrder) {
        return new GxhAuditJianguanService().getAuditJianguanPageData(record, pageindex, pagesize, sortField, sortOrder);
    }

    @Override
    public int getSpxxCount() {
        return new GxhAuditJianguanService().getSpxxCount();
    }

    @Override
    public int getYrlCount() {
        return new GxhAuditJianguanService().getYrlCount();
    }

    @Override
    public int getWrlCount() {
        return new GxhAuditJianguanService().getWrlCount();
    }

}
