package com.epoint.auditproject.monitorsupervise.impl;

import com.epoint.auditproject.monitorsupervise.api.IJNAuditProjectMonitorNewService;
import com.epoint.auditproject.monitorsupervise.api.AuditprojectoldNew;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.dao.CommonDao;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Component
public class JNAuditProjectMonitorNewImpl implements IJNAuditProjectMonitorNewService {

    @Override
    public String findsupervise(String userGuid, String string) {
        String sql = "SELECT rowguid from audit_project_monitor where MONITOR_ID=? and END_DATE is null and alertguid=?";
        return CommonDao.getInstance().find(sql, String.class, userGuid, string);
    }

    @Override
    public AuditprojectoldNew getAuditProjectByRowGuid(String projectguid) {
        return new JNProjectNewService().getAuditProjectByRowGuid(projectguid);
    }

    @Override
    public AuditProject getOldProjectByRowGuid(String projectguid) {
        return new JNProjectNewService().getOldProjectByRowGuid(projectguid);
    }

    @Override
    public AuditCommonResult<PageData<AuditProject>> getAuditProjectPageDataMonitor(String fieldstr,
                                                                                    Map<String, String> conditionMap, String userguid, Integer ismaterial, Integer firstResult, Integer maxResults,
                                                                                    String sortField, String sortOrder) {
        JNProjectNewService projectService = new JNProjectNewService();
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
    public AuditCommonResult<PageData<AuditprojectoldNew>> getAuditProjectPageDataMonitorOld(String fieldstr,
                                                                                          Map<String, String> conditionMap, Integer firstResult, Integer maxResults,
                                                                                          String sortField, String sortOrder) {
        JNProjectNewService projectService = new JNProjectNewService();
        AuditCommonResult<PageData<AuditprojectoldNew>> result = new AuditCommonResult<PageData<AuditprojectoldNew>>();
        try {
            PageData<AuditprojectoldNew> taskList = projectService.getAuditProjectPageDataMonitorOld(fieldstr, conditionMap, firstResult, maxResults, sortField, sortOrder);
            result.setResult(taskList);
        } catch (Exception e) {
            result.setSystemFail(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }


}
