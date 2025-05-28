package com.epoint.auditorga.auditwindow.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.auditorga.auditwindow.api.IJnGxhTaskConfigService;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Service
public class JnGxhTaskConfigServiceImpl implements IJnGxhTaskConfigService {
    @Override
    public List<AuditProject> getAuditProjectListByTaskid(String taskid) {
        return new JnGxhTaskConfigService().getAuditProjectListByTaskid(taskid);
    }
}
