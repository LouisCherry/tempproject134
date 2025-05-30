package com.epoint.auditprojectunusual.imp;

import com.epoint.auditprojectunusual.api.IJNAuditProjectUnusual;
import com.epoint.auditqueue.auditqueuerest.appointment.api.ICzQueueAppointment;
import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;
import com.epoint.zwdt.zwdtrest.task.keyword.api.entity.AuditTaskKeyword;
import com.epoint.zwdt.zwdtrest.task.keyword.impl.AuditTaskKeywordService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service
public class JNAuditProjectUnusualImpl implements IJNAuditProjectUnusual
{

    @Override
    public List<AuditProjectUnusual> getZantingData(String projectguid){
        return new JNAuditProjectUnusualService().getZantingData(projectguid);
    }
}
