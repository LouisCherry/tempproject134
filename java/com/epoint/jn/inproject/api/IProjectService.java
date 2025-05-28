package com.epoint.jn.inproject.api;

import java.util.List;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.BaseEntity;
import com.epoint.core.grammar.Record;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.jn.inproject.api.entity.eajcstepbasicinfogt;
import com.epoint.jn.inproject.api.entity.eajcstepbasicinfogtnew;
import com.epoint.jn.inproject.api.entity.eajcstepdonegt;
import com.epoint.jn.inproject.api.entity.eajcstepdonegtnew;
import com.epoint.jn.inproject.api.entity.eajcstepprocgt;
import com.epoint.jn.inproject.api.entity.eajcstepprocgtnew;
import com.epoint.jn.inproject.api.entity.lcproject;

public interface IProjectService
{

	List<Record> finList();

	void insertQzkBaseInfo(eajcstepbasicinfogt eajcstepbasicinfogt);
	
	void insertQzkProcess(eajcstepprocgt eajcstepprocgt);
	
	void insertQzkDone(eajcstepdonegt eajcstepdonegt);
	
	void insertQzkBaseInfonew(eajcstepbasicinfogtnew eajcstepbasicinfogt);
	
	void insertQzkProcessnew(eajcstepprocgtnew eajcstepprocgt);
	
	void insertQzkDonenew(eajcstepdonegtnew eajcstepdonegt);
	
	void updatezjprojectByrowguid(String syncdone, String rowguid);
	
	
	
}
