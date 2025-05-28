package com.epoint.auditqueue.auditznsbqueue.impl;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.auditqueue.auditznsbqueue.api.IQueueList;
import com.epoint.basic.auditqueue.service.AuditQueueBasicService;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;
import com.epoint.sso.frame.domain.FrameUser;

@Component
@Service
public class QueueListImpl implements IQueueList
{
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public AuditCommonResult<List<FrameUser>> getZnsbQueueHandleUser(Map<String, String> conditionMap) {
        AuditQueueBasicService<List<FrameUser>> baseservice = new AuditQueueBasicService<List<FrameUser>>();
        AuditCommonResult<List<FrameUser>> result = new AuditCommonResult<List<FrameUser>>();
        try {
            List<FrameUser> queuehistorylist = baseservice.selectRecordList(FrameUser.class, conditionMap);
            result.setResult(queuehistorylist);
        }
        catch (Exception e) {
            log.error("异常信息:", e);
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getModuleClick(String startime, String endtime) {
        QueueListService auditqueueService = new QueueListService();
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        try {
            result.setResult(auditqueueService.getModuleClick(startime, endtime));

        }
        catch (Exception e) {
            log.error("异常信息:", e);
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getModuleClick(String centerguid, String startime, String endtime) {
        QueueListService auditqueueService = new QueueListService();
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        try {
            result.setResult(auditqueueService.getModuleClick(centerguid, startime, endtime));

        }
        catch (Exception e) {
            log.error("异常信息:", e);
            result.setSystemFail(e.getMessage());
        }
        return result;
    }
}
