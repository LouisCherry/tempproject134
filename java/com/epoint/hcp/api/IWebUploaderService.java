package com.epoint.hcp.api;

import java.util.List;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.BaseEntity;
import com.epoint.core.grammar.Record;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.hcp.api.entity.eajcstepbasicinfogt;
import com.epoint.hcp.api.entity.eajcstepbasicinfogtnew;
import com.epoint.hcp.api.entity.eajcstepdonegt;
import com.epoint.hcp.api.entity.eajcstepdonegtnew;
import com.epoint.hcp.api.entity.eajcstepprocgt;
import com.epoint.hcp.api.entity.eajcstepprocgtnew;
import com.epoint.hcp.api.entity.lcproject;
import com.epoint.hcp.api.entity.lcprojecteight;
import com.epoint.hcp.api.entity.lcprojectfive;
import com.epoint.hcp.api.entity.lcprojectfour;
import com.epoint.hcp.api.entity.lcprojectnine;
import com.epoint.hcp.api.entity.lcprojectseven;
import com.epoint.hcp.api.entity.lcprojectsix;
import com.epoint.hcp.api.entity.lcprojectten;
import com.epoint.hcp.api.entity.lcprojectthree;
import com.epoint.hcp.api.entity.lcprojecttwo;

public interface IWebUploaderService
{

    List<Record> finList(int first, int pagesize, String ouguid, String areacode);
    
    Integer finTotal(String ouguid,String areacode);
    
    public int deleteByGuid(String guid);
    
    void insert(lcproject record);
    
    void insert(lcprojecttwo record);
    
    void insert(lcprojectthree record);
    
    void insert(lcprojectfour record);
    
    void insert(lcprojectfive record);
    
    void insert(lcprojectsix record);
    
    void insert(lcprojectseven record);
    
    void insert(lcprojecteight record);
    
    void insert(lcprojectnine record);
    
    void insert(lcprojectten record);
    
    void update(lcprojectten record);

	void updatezjproject(String rowguid,String username,String userguid);
	
	void insertQzkBaseInfo(eajcstepbasicinfogt eajcstepbasicinfogt);
	
	void deleteQzkBaseInfo(String guid);
	
	void insertQzkProcess(eajcstepprocgt eajcstepprocgt);
	
	void deleteQzkProcess(String guid);
	
	void insertQzkDone(eajcstepdonegt eajcstepdonegt);
	
	void deleteQzkDone(String guid);
	
	void insertQzkBaseInfonew(eajcstepbasicinfogtnew eajcstepbasicinfogt);
	
	void insertQzkProcessnew(eajcstepprocgtnew eajcstepprocgt);
	
	void insertQzkDonenew(eajcstepdonegtnew eajcstepdonegt);
	
	AuditTask getAuditTaskByTaskname(String taskname, String areacode,String ouguid);
	
	FrameOu getFrameOuByOuname(String ouname);
	
	FrameOu getFrameOuByOunameNew(String ouname,String areacode);
	
	Record getJnzjProjectByRowguid(String rowguid);

	void updatezjprojectByrowguid(String syncdone, String rowguid);
	
	void deletejprojectByguid(String rowguid);

	Record getTotalPorjectByNow();
	
	Record getSizeInPorject();

	int getInCountByAreacode(String areacode);
	
	int getNewInCountByAreacode(String areacode);

	void insertLcprojectRecord(String areaCode, int length);
	
	void insertExterRecord(String areaCode, int length);
	
}
