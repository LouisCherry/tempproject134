package com.epoint.basic.auditproject.auditproject.impl;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditproject.auditproject.inter.IJNAuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.grammar.Record;

@Component
@Service
public class JNAuditProjectImpl implements IJNAuditProject
{
	

	@Override
	public Record getMaxZjNum(String name) {
		return new JNAuditProjectService().getMaxZjNum(name);
	}

	@Override
	public void UpdateMaxZjNum(String maxnum, String name) {
		new JNAuditProjectService().UpdateMaxZjNum(maxnum,name);
	}
	
	@Override
	public AuditTask getAuditTaskByUnid(String unid) {
		return new JNAuditProjectService().getAuditTaskByUnid(unid);
	}
	
	 
}
