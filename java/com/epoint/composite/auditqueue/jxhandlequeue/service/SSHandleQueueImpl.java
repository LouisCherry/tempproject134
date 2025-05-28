package com.epoint.composite.auditqueue.jxhandlequeue.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditqueue.auditqueue.domain.AuditQueue;
import com.epoint.basic.auditqueue.auditqueueorgawindow.domain.AuditQueueOrgaWindow;
import com.epoint.basic.auditqueue.auditqueueorgawindow.inter.IAuditQueueOrgaWindow;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.ZwfwRedisCacheUtil;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.composite.auditqueue.jxhandlequeue.inter.IJXHandleQueue;
import com.epoint.composite.auditqueue.jxhandlequeue.inter.ISSHandleQueue;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;


@Component
@Service
public class SSHandleQueueImpl implements ISSHandleQueue
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
        SSHandleQueueService handlequeueService = new SSHandleQueueService();
        IAuditQueueOrgaWindow windowservice = ContainerFactory.getContainInfo().getComponent(IAuditQueueOrgaWindow.class);
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        ZwfwRedisCacheUtil redis = null;
        try {
            String nextQNO = handlequeueService.getNextQNO(WindowGuid, WindowNo, CenterGuid, UserGuid, UseCall);
            AuditQueueOrgaWindow auditqueueorgawindow = windowservice.getDetailbyWindowguid(WindowGuid).getResult();
            String waittime = auditqueueorgawindow.get("waittime");
            // 不为空时候清空等待时间
            if(StringUtil.isNotBlank(nextQNO)&&!QueueConstant.Current_None.equals(nextQNO)){
                waittime = "";
            }
            else{
                // 为空时如果没有等待时间，则用当前时间替换，有则不更新
                if(StringUtil.isBlank(auditqueueorgawindow.get("waittime"))){
                    waittime = EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss");
                }
            }
            redis = new ZwfwRedisCacheUtil(false);
            if(StringUtil.isBlank(auditqueueorgawindow.get("waittime"))||!waittime.equals(auditqueueorgawindow.get("waittime"))){
                redis.updateByHash(AuditQueueOrgaWindow.class, WindowGuid, "waittime", waittime);
            }
            if(StringUtil.isBlank(auditqueueorgawindow.get("loopcount"))){
                redis.updateByHash(AuditQueueOrgaWindow.class, WindowGuid, "loopcount", QueueConstant.CONSTANT_STR_ZERO);
            }
            result.setResult(nextQNO);
        }
        catch (Exception e) {
            //log.error("异常信息:", e);
            result.setSystemFail(e.toString());
        }
        finally {
            if (redis != null) {
                redis.close();
            }
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> getNextQNO(String WindowGuid, String WindowNo, String CenterGuid, String UserGuid, Boolean UseCall, Boolean isAutoAssign) {
        SSHandleQueueService handlequeueService = new SSHandleQueueService();
        IAuditQueueOrgaWindow windowservice = ContainerFactory.getContainInfo().getComponent(IAuditQueueOrgaWindow.class);
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        ZwfwRedisCacheUtil redis = null;
        try {
            String nextQNO = handlequeueService.getNextQNO(WindowGuid, WindowNo, CenterGuid, UserGuid, UseCall, isAutoAssign);
            AuditQueueOrgaWindow auditqueueorgawindow = windowservice.getDetailbyWindowguid(WindowGuid).getResult();
            String waittime = auditqueueorgawindow.get("waittime");
            // 不为空时候清空等待时间
            if(StringUtil.isNotBlank(nextQNO)&&!QueueConstant.Current_None.equals(nextQNO)){
                waittime = "";
            }
            else{
                // 为空时如果没有等待时间，则用当前时间替换，有则不更新
                if(StringUtil.isBlank(auditqueueorgawindow.get("waittime"))){
                    waittime = EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss");
                }
            }
            redis = new ZwfwRedisCacheUtil(false);
            if(StringUtil.isBlank(auditqueueorgawindow.get("waittime"))||!waittime.equals(auditqueueorgawindow.get("waittime"))){
                redis.updateByHash(AuditQueueOrgaWindow.class, WindowGuid, "waittime", waittime);
            }
            if(StringUtil.isBlank(auditqueueorgawindow.get("loopcount"))){
                redis.updateByHash(AuditQueueOrgaWindow.class, WindowGuid, "loopcount", QueueConstant.CONSTANT_STR_ZERO);
            }
            result.setResult(nextQNO);
        }
        catch (Exception e) {
            //log.error("异常信息:", e);
            result.setSystemFail(e.toString());
        }
        finally {
            if (redis != null) {
                redis.close();
            }
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
