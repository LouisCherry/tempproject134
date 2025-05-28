package com.epoint.zoucheng.znsb.auditqueue.qhj.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.zoucheng.znsb.auditqueue.qhj.api.IZCAuditQueue;
import com.epoint.zoucheng.znsb.auditqueue.qhj.service.ZCAuditQueueService;


import org.springframework.stereotype.Component;

@Component
@Service
public class ZCAuditQueueImpl implements IZCAuditQueue {


    @Override
    public void updateIsSendSmsByQNO(String qno, String centerguid) {
        ZCAuditQueueService queueService = new ZCAuditQueueService();
        queueService.updateIsSendSmsByQNO(qno,centerguid);
    }

    public AuditCommonResult<Integer> getQueueCountBySfz(String sfz, String FromDate, String ToDate) {
        ZCAuditQueueService queueService = new ZCAuditQueueService();
        AuditCommonResult<Integer> result = new AuditCommonResult<>();

        try {
            result.setResult(queueService.getQueueCountBySfz(sfz, FromDate, ToDate));
            return result;
        } catch (Exception var8) {
            result.setSystemFail(var8.toString());
            return result;
        }
    }

}