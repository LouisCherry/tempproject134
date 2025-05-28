package com.epoint.auditproject.util.factory;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;

/**
 * @author rachel
 */
public interface FinishAuditProjectInterface {

    /**
     * 办件办结后处理业务
     *
     * @param auditProject
     * @param auditTask
     */
    void handleBusiness(AuditProject auditProject, AuditTask auditTask);

}
