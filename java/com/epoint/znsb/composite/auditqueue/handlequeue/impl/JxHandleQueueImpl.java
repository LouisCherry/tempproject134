package com.epoint.znsb.composite.auditqueue.handlequeue.impl;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.znsb.composite.auditqueue.handlequeue.inter.JxIHandleQueue;
import com.epoint.znsb.composite.auditqueue.handlequeue.service.JxHandleQueueService;


@Component
@Service
public class JxHandleQueueImpl implements JxIHandleQueue
{

    @Override
    public AuditCommonResult<Integer> getTaskWaitNum(String TaskGuid, Boolean redis) {

        JxHandleQueueService JxHandleQueueService = new JxHandleQueueService();
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        try {
            result.setResult(JxHandleQueueService.getTaskWaitNum(TaskGuid, redis));

        }
        catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Map<String, String>> getAPQNO(String appointguid, String centerguid) {
        JxHandleQueueService JxHandleQueueService = new JxHandleQueueService();
        AuditCommonResult<Map<String, String>> result = new AuditCommonResult<Map<String, String>>();
        try {
            result.setResult(JxHandleQueueService.getAPQNO(appointguid, centerguid));
        }
        catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> getNextQNO(String WindowGuid, String WindowNo, String CenterGuid, String UserGuid,
            Boolean UseCall) {
        JxHandleQueueService JxHandleQueueService = new JxHandleQueueService();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            result.setResult(JxHandleQueueService.getNextQNO(WindowGuid, WindowNo, CenterGuid, UserGuid, UseCall));
        }
        catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Map<String, String>> getQNO(String sfz, String phone, String taskguid, String centerguid,
            String hallguid) {
        JxHandleQueueService JxHandleQueueService = new JxHandleQueueService();
        AuditCommonResult<Map<String, String>> result = new AuditCommonResult<Map<String, String>>();
        try {
            result.setResult(JxHandleQueueService.getQNO(sfz, phone, taskguid, centerguid, hallguid));
        }
        catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

  

    @Override
    public AuditCommonResult<Integer> getQueueCount(String centerguid, Date startTime, Date endTime) {
        JxHandleQueueService JxHandleQueueService = new JxHandleQueueService();
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        try {
            result.setResult(JxHandleQueueService.getQueueCount(centerguid, startTime, endTime));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Map<String, String>> getTurnWindowQNO(String auditqueueguid,String windowguid,String handleuserguid,String centerguid) {
        JxHandleQueueService JxHandleQueueService = new JxHandleQueueService();
        AuditCommonResult<Map<String, String>> result = new AuditCommonResult<Map<String, String>>();
        try {
            result.setResult(JxHandleQueueService.getTurnWindowQNO(auditqueueguid,windowguid,handleuserguid,centerguid));
        }
        catch (Exception e) {
            //system.out.println("异常");
            e.printStackTrace();
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Map<String, String>> getTurnTaskQNO(String auditqueueguid,String taskguid,String handluserguid,String centerguid) {
        
        JxHandleQueueService JxHandleQueueService = new JxHandleQueueService();
        AuditCommonResult<Map<String, String>> result = new AuditCommonResult<Map<String, String>>();
        try {
            result.setResult(JxHandleQueueService.getTurnTaskQNO(auditqueueguid,taskguid,handluserguid,centerguid));
        }
        catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }
}
