package com.epoint.auditqueue.auditqueuetasktype.action;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.audittask.basic.domain.AuditTask;

@Component
@Service
public class TasktypeImpl implements ITasktype {
    @Override
    public List<AuditTask> gettasklist(String type, String params) {
        TasktypeService service = new TasktypeService();
        return service.gettasklist(type, params);
    }
}
