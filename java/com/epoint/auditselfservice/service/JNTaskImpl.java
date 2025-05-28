package com.epoint.auditselfservice.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
@Component
@Service
public class JNTaskImpl implements IJNTask
{
    @Override
    public List<AuditTaskMaterial> getOrderdTaskMaterial(String taskguid) {
        JNTaskService service = new JNTaskService();
        return service.getOrderdTaskMaterial(taskguid);
    }
}
