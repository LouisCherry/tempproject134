package com.epoint.auditproject.monitorsupervise.impl;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.epoint.auditproject.monitorsupervise.api.IJNAuditProjectMonitorService;
import com.epoint.auditproject.monitorsupervise.api.auditprojectold;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.dao.CommonDao;
import com.epoint.database.peisistence.crud.impl.model.PageData;

@Service
@Component
public class JNAuditProjectMonitorImpl implements IJNAuditProjectMonitorService {

    @Override
    public String findsupervise(String userGuid, String string) {
        String sql = "SELECT rowguid from audit_project_monitor where MONITOR_ID=? and END_DATE is null and alertguid=?";
        return CommonDao.getInstance().find(sql, String.class, userGuid, string);
    }

    @Override
    public auditprojectold getAuditProjectByRowGuid(String projectguid) {
        return new JNProjectService().getAuditProjectByRowGuid(projectguid);
    }

    @Override
    public AuditProject getOldProjectByRowGuid(String projectguid) {
        return new JNProjectService().getOldProjectByRowGuid(projectguid);
    }

    @Override
    public AuditCommonResult<PageData<AuditProject>> getAuditProjectPageDataMonitor(String fieldstr,
                                                                                    Map<String, String> conditionMap, String userguid, Integer ismaterial, Integer firstResult, Integer maxResults,
                                                                                    String sortField, String sortOrder) {
        JNProjectService projectService = new JNProjectService();
        AuditCommonResult<PageData<AuditProject>> result = new AuditCommonResult<PageData<AuditProject>>();
        try {
            PageData<AuditProject> taskList = projectService.getAuditProjectPageDataMonitor(fieldstr, conditionMap,
                    userguid, ismaterial, firstResult, maxResults, sortField, sortOrder);
            result.setResult(taskList);
        } catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<PageData<auditprojectold>> getAuditProjectPageDataMonitorOld(String fieldstr,
                                                                                          Map<String, String> conditionMap, Integer firstResult, Integer maxResults,
                                                                                          String sortField, String sortOrder) {
        JNProjectService projectService = new JNProjectService();
        AuditCommonResult<PageData<auditprojectold>> result = new AuditCommonResult<PageData<auditprojectold>>();
        try {
            PageData<auditprojectold> taskList = projectService.getAuditProjectPageDataMonitorOld(fieldstr, conditionMap, firstResult, maxResults, sortField, sortOrder);
            result.setResult(taskList);
        } catch (Exception e) {
            result.setSystemFail(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }


}
