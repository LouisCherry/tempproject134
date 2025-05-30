package com.epoint.zwdt.zwdtrest.sghd.api;

import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;

public interface IGxhAuditJianguan {

    PageData<Record> getAuditJianguanPageData(Record record, int pageindex, int pagesize, String sortField, String sortOrder);

    int getSpxxCount();

    int getYrlCount();

    int getWrlCount();

}
