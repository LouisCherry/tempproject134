package com.epoint.jn.inproject.impl;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.grammar.Record;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.jn.inproject.api.IProjectService;
import com.epoint.jn.inproject.api.entity.eajcstepbasicinfogt;
import com.epoint.jn.inproject.api.entity.eajcstepbasicinfogtnew;
import com.epoint.jn.inproject.api.entity.eajcstepdonegt;
import com.epoint.jn.inproject.api.entity.eajcstepdonegtnew;
import com.epoint.jn.inproject.api.entity.eajcstepprocgt;
import com.epoint.jn.inproject.api.entity.eajcstepprocgtnew;

@Component
@Service
public class InprojectServiceImpl implements IProjectService
{
    
	public List<Record> finList() {
        return new InprojectService().finList() ;
    }
	
    public void insertQzkBaseInfo(eajcstepbasicinfogt record) {
		new InprojectService().insertQzkBaseInfo(record);
    }
    
    public void insertQzkProcess(eajcstepprocgt record) {
		new InprojectService().insertQzkProcess(record);
    }
    
    public void insertQzkDone(eajcstepdonegt record) {
		new InprojectService().insertQzkDone(record);
    }
    
    public void insertQzkBaseInfonew(eajcstepbasicinfogtnew record) {
		new InprojectService().insertQzkBaseInfonew(record);
    }
    
    public void insertQzkProcessnew(eajcstepprocgtnew record) {
		new InprojectService().insertQzkProcessnew(record);
    }
    
    public void insertQzkDonenew(eajcstepdonegtnew record) {
		new InprojectService().insertQzkDonenew(record);
    }
    
    public void updatezjprojectByrowguid(String syncdone,String rowguid) {
		 new InprojectService().updatezjprojectByrowguid(syncdone, rowguid);
	}

}
