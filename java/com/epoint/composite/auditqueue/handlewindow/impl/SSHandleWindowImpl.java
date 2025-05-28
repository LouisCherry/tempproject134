package com.epoint.composite.auditqueue.handlewindow.impl;

import com.epoint.composite.auditqueue.handlewindow.inter.ISSHandleWindow;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.composite.auditqueue.handlewindow.inter.IHandleWindow;
import com.epoint.composite.auditqueue.handlewindow.service.SSHandleWindowService;

@Component
@Service
public class SSHandleWindowImpl implements ISSHandleWindow
{

	  @Override
	    public AuditCommonResult<String> initAuditQueueOrgaWindow() {
	       
		    SSHandleWindowService windowservice=new SSHandleWindowService();
	        AuditCommonResult<String> result = new AuditCommonResult<String>();
	        try {
	        	windowservice.initAuditQueueOrgaWindow();

	        }
	        catch (Exception e) {
	            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
	        }
	        return result;
	    }

	  @Override
	    public AuditCommonResult<String> initAuditQueueOrgaWindowbyWindow(String windowguid) {
	       
		     SSHandleWindowService windowservice=new SSHandleWindowService();
	        AuditCommonResult<String> result = new AuditCommonResult<String>();
	        try {
	        	windowservice.initAuditQueueOrgaWindowbyWindow(windowguid);

	        }
	        catch (Exception e) {
	            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
	        }
	        return result;
	    }
}
