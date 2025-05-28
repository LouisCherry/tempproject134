package com.epoint.auditproject.auditproject.service;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.auditproject.auditproject.api.IZtProjectService;
import com.epoint.core.grammar.Record;

@Service
@Component
public class ZtProjectServiceImpl implements IZtProjectService
{

	/**
     * 
     */
    private static final long serialVersionUID = -4737301911580678408L;

  

	@Override
	public void inserRecord(Record record) {
		 new ZtProjectService().inserRecord(record);
	}

	@Override
	public int UpdateRecord(Record record) {
		return new ZtProjectService().updateRecord(record);
	}
	
	public Record getDzbdDetailByZzbh(String tablename, String zzbh) {
		return new ZtProjectService().getDzbdDetailByZzbh(tablename,zzbh);
	}
	
	

}
