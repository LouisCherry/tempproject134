package com.epoint.composite.auditqueue.handlecentertask.impl;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.composite.auditqueue.handlecentertask.inter.IJNHandleCenterTask;
import com.epoint.composite.auditqueue.handlecentertask.service.JNHandleCenterTaskService;

@Component
@Service
public class JNHandleCenterTaskImpl implements IJNHandleCenterTask {

	@Override
	public AuditCommonResult<String> initCenterTask(String CenterGuid) {
		AuditCommonResult<String> result = new AuditCommonResult<String>();
	JNHandleCenterTaskService handletaskservice=new JNHandleCenterTaskService();
		try {
		
			handletaskservice.initCenterTask(CenterGuid);
		} catch (Exception e) {
			result.setSystemFail(e.toString());
		}
		return result;
	}
	
	@Override
	public AuditCommonResult<String> initCenterTaskbyTaskids(String CenterGuid,String Taskids) {
		AuditCommonResult<String> result = new AuditCommonResult<String>();
	JNHandleCenterTaskService handletaskservice=new JNHandleCenterTaskService();
		try {
		
			handletaskservice.initCenterTaskbyTaskids(CenterGuid,Taskids);
		} catch (Exception e) {
			result.setSystemFail(e.toString());
		}
		return result;
	}

}
