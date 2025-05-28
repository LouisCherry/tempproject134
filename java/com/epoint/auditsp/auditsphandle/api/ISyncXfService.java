package com.epoint.auditsp.auditsphandle.api;

import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;

public interface ISyncXfService {

    PageData<Record> geXflistByXmdm(int first, int pageSize, String sortField, String sortOrder, String xmdm);

}
