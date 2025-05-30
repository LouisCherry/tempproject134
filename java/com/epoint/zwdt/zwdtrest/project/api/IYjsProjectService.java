package com.epoint.zwdt.zwdtrest.project.api;

import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;

public interface IYjsProjectService {

    PageData<AuditSpBusiness> getAuditSpBusinessByPage(Record record, int pageIndex, int pageSize);

    boolean Ishighlight(String itemValue, String businessname);

    AuditSpBusiness getBusinessByNameAndAreacode(String taskname, String areacode);

    boolean IshighlightTown(String xiaqucode, String businessname);

}
