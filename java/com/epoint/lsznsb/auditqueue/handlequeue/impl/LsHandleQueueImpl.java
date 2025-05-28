package com.epoint.lsznsb.auditqueue.handlequeue.impl;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.lsznsb.auditqueue.handlequeue.inter.LsIHandleQueue;
import com.epoint.lsznsb.auditqueue.handlequeue.service.LsHandleQueueService;

@Component
@Service
public class LsHandleQueueImpl implements LsIHandleQueue
{
    @Override
    public AuditCommonResult<Map<String, String>> getQNO(String sfz, String phone, String taskguid, String centerguid,
            String hallguid, String islove) {
        LsHandleQueueService handlequeueService = new LsHandleQueueService();
        AuditCommonResult<Map<String, String>> result = new AuditCommonResult<Map<String, String>>();
        try {
            result.setResult(handlequeueService.getQNO(sfz, phone, taskguid, centerguid, hallguid, islove));
        }
        catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> getNextQNO(String WindowGuid, String WindowNo, String CenterGuid, String UserGuid,
            Boolean UseCall) {
        LsHandleQueueService handlequeueService = new LsHandleQueueService();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            result.setResult(handlequeueService.getNextQNO(WindowGuid, WindowNo, CenterGuid, UserGuid, UseCall));
        }
        catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }
}
