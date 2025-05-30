package com.epoint.zwdt.zwdtrest.project.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zwdt.zwdtrest.project.api.IYjsProjectService;
import org.springframework.stereotype.Component;

@Service
@Component
public class YjsProjectServiceImpl implements IYjsProjectService {

    @Override
    public PageData<AuditSpBusiness> getAuditSpBusinessByPage(Record record, int pageIndex, int pageSize) {
        return new YjsProjectService().getAuditSpBusinessByPage(record, pageIndex, pageSize);
    }

    @Override
    public boolean Ishighlight(String itemValue, String businessname) {
        return new YjsProjectService().Ishighlight(itemValue, businessname);
    }

    @Override
    public AuditSpBusiness getBusinessByNameAndAreacode(String taskname, String areacode) {
        return new YjsProjectService().getBusinessByNameAndAreacode(taskname, areacode);
    }

    @Override
    public boolean IshighlightTown(String xiaqucode, String businessname) {
        return new YjsProjectService().IshighlightTown(xiaqucode, businessname);
    }

}
