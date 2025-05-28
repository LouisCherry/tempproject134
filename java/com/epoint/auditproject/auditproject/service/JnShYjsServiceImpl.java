package com.epoint.auditproject.auditproject.service;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.epoint.auditproject.auditproject.api.IJnShYjsService;
import com.epoint.core.grammar.Record;
/**
 * 
 * 
 * @author 18039505500
 * @version [版本号, 2020-06-23 19:57:28]
 */
@Component
@Service
public class JnShYjsServiceImpl implements IJnShYjsService
{
   
	@Override
	public void inserRecord(Record record) {
		 new JnShYjsService().inserRecord(record);
	}

    
	 
}
