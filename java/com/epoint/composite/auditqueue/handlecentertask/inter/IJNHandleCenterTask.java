package com.epoint.composite.auditqueue.handlecentertask.inter;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;

@Service
public interface IJNHandleCenterTask {

    public AuditCommonResult<String> initCenterTask(String CenterGuid);
	
    public AuditCommonResult<String> initCenterTaskbyTaskids(String CenterGuid,String Taskids);
}
