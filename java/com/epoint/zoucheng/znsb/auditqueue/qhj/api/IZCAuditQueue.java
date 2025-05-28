package com.epoint.zoucheng.znsb.auditqueue.qhj.api;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;


@Service
public interface IZCAuditQueue {

    void updateIsSendSmsByQNO(String qno,String centerguid);
    AuditCommonResult<Integer> getQueueCountBySfz(String sfz,String FromDate,String ToDate);

}