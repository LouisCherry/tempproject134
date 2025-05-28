package com.epoint.basic.auditqueue.auditqueuewindowtasktype.inter;

import com.epoint.basic.auditqueue.auditqueuewindowtasktype.domain.AuditQueueWindowTasktype;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import java.util.List;
import java.util.Map;

public interface JxIAuditQueueWindowTasktype {
    AuditCommonResult<String> deletebyTaskTypeGuid(String arg0);

    AuditCommonResult<Boolean> IsSameQueuevalue(String arg0);

    AuditCommonResult<String> insertWindowtasktype(AuditQueueWindowTasktype arg0);

    AuditCommonResult<PageData<AuditQueueWindowTasktype>> getWindowTasktypeByPage(Map<String, String> arg0, int arg1,
            int arg2);

    AuditCommonResult<List<AuditQueueWindowTasktype>> getAllWindowTasktype(Map<String, String> arg0);

    AuditCommonResult<String> deletebyWindowguid(String arg0);

    AuditCommonResult<List<String>> getWindowListByTaskTypeGuid(String arg0);
}