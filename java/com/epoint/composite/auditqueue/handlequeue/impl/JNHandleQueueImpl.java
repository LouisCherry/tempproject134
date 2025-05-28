package com.epoint.composite.auditqueue.handlequeue.impl;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.composite.auditqueue.handlequeue.inter.IJNHandleQueue;
import com.epoint.composite.auditqueue.handlequeue.service.HandleQueueService;
import com.epoint.composite.auditqueue.handlequeue.service.JNHandleQueueService;

@Component
@Service
public class JNHandleQueueImpl implements IJNHandleQueue
{

    @Override
    public AuditCommonResult<Integer> getTaskWaitNum(String TaskGuid, Boolean redis) {

        HandleQueueService handlequeueService = new HandleQueueService();
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        try {
            result.setResult(handlequeueService.getTaskWaitNum(TaskGuid, redis));

        }
        catch (Exception e) {
            e.printStackTrace();
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Map<String, String>> getAPQNO(String appointguid, String centerguid) {
        HandleQueueService handlequeueService = new HandleQueueService();
        AuditCommonResult<Map<String, String>> result = new AuditCommonResult<Map<String, String>>();
        try {
            result.setResult(handlequeueService.getAPQNO(appointguid, centerguid));
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> getNextQNO(String WindowGuid, String WindowNo, String CenterGuid, String UserGuid,
            Boolean UseCall) {
        HandleQueueService handlequeueService = new HandleQueueService();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            result.setResult(handlequeueService.getNextQNO(WindowGuid, WindowNo, CenterGuid, UserGuid, UseCall));
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Map<String, String>> getQNO(String sfz, String phone, String taskguid, String centerguid,
            String hallguid) {
        JNHandleQueueService handlequeueService = new JNHandleQueueService();
        AuditCommonResult<Map<String, String>> result = new AuditCommonResult<Map<String, String>>();
        try {
            result.setResult(handlequeueService.getQNO(sfz, phone, taskguid, centerguid, hallguid));
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Map<String, String>> getQNO(String sfz, String phone, String taskguid, String centerguid,
            String hallguid, String islove) {
        JNHandleQueueService handlequeueService = new JNHandleQueueService();
        AuditCommonResult<Map<String, String>> result = new AuditCommonResult<Map<String, String>>();
        try {
            result.setResult(handlequeueService.getQNO(sfz, phone, taskguid, centerguid, hallguid, islove));
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getQueueCount(String centerguid, Date startTime, Date endTime) {
        HandleQueueService handlequeueService = new HandleQueueService();
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        try {
            result.setResult(handlequeueService.getQueueCount(centerguid, startTime, endTime));
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Map<String, String>> getTurnWindowQNO(String auditqueueguid, String windowguid,
            String handleuserguid, String centerguid) {
        HandleQueueService handlequeueService = new HandleQueueService();
        AuditCommonResult<Map<String, String>> result = new AuditCommonResult<Map<String, String>>();
        try {
            result.setResult(
                    handlequeueService.getTurnWindowQNO(auditqueueguid, windowguid, handleuserguid, centerguid));
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Map<String, String>> getTurnTaskQNO(String auditqueueguid, String taskguid,
            String handluserguid, String centerguid) {

        HandleQueueService handlequeueService = new HandleQueueService();
        AuditCommonResult<Map<String, String>> result = new AuditCommonResult<Map<String, String>>();
        try {
            result.setResult(handlequeueService.getTurnTaskQNO(auditqueueguid, taskguid, handluserguid, centerguid));
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setSystemFail(e.toString());
        }
        return result;
    }
}
