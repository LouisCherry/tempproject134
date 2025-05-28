package com.epoint.composite.auditqueue.jxhandlequeue.service;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditqueue.auditqueue.domain.AuditQueue;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.composite.auditqueue.jxhandlequeue.inter.IJXHandleQueue;


@Component
@Service
public class JXHandleQueueImpl implements IJXHandleQueue
{

    @Override
    public AuditCommonResult<Integer> getTaskWaitNum(String TaskGuid, Boolean redis) {

        JXHandleQueueService handlequeueService = new JXHandleQueueService();
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        try {
            result.setResult(handlequeueService.getTaskWaitNum(TaskGuid, redis));

        }
        catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Map<String, String>> getAPQNO(String appointguid, String centerguid) {
        JXHandleQueueService handlequeueService = new JXHandleQueueService();
        AuditCommonResult<Map<String, String>> result = new AuditCommonResult<Map<String, String>>();
        try {
            result.setResult(handlequeueService.getAPQNO(appointguid, centerguid));
        }
        catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> getNextQNO(String WindowGuid, String WindowNo, String CenterGuid, String UserGuid,
            Boolean UseCall) {
        JXHandleQueueService handlequeueService = new JXHandleQueueService();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            result.setResult(handlequeueService.getNextQNO(WindowGuid, WindowNo, CenterGuid, UserGuid, UseCall));
        }
        catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Map<String, String>> getQNO(String sfz, String phone, String taskguid, String centerguid,
            String hallguid) {
        JXHandleQueueService handlequeueService = new JXHandleQueueService();
        AuditCommonResult<Map<String, String>> result = new AuditCommonResult<Map<String, String>>();
        try {
            result.setResult(handlequeueService.getQNO(sfz, phone, taskguid, centerguid, hallguid));
        }
        catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

  

    @Override
    public AuditCommonResult<Integer> getQueueCount(String centerguid, Date startTime, Date endTime) {
        JXHandleQueueService handlequeueService = new JXHandleQueueService();
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        try {
            result.setResult(handlequeueService.getQueueCount(centerguid, startTime, endTime));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }
    
    @Override
    public AuditCommonResult<AuditQueue> getQueueDetail(String fieldstr, String qno, String centerguid) {
        JXHandleQueueService handlequeueService = new JXHandleQueueService();
        AuditCommonResult<AuditQueue> result = new AuditCommonResult<AuditQueue>();
        try {
            result.setResult(handlequeueService.getQueueDetail(fieldstr, qno, centerguid));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

}
