package com.epoint.yutai.queuewindow.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditqueue.auditqueue.domain.AuditQueue;
import com.epoint.basic.auditqueue.auditqueuehostory.domain.AuditQueueHistory;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;
import com.epoint.yutai.queuewindow.api.IYTAuditQueue;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@Service
public class YTHandleQueueImpl implements IYTAuditQueue
{


    @Override
    public List<String> getWaitQno(String queuevalue, String windowguid) {
        return new YTHandleQueueService().getWaitQno(queuevalue,windowguid);
    }

}
