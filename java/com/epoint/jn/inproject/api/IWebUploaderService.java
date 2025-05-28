package com.epoint.jn.inproject.api;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.grammar.Record;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.hcp.api.entity.*;
import com.epoint.jn.inproject.api.entity.lcproject;
import com.epoint.jn.inproject.api.entity.*;

import java.util.List;

public interface IWebUploaderService
{

	/**
	 *
	 * @param first
	 * @param pagesize
	 * @param ouguid
	 * @param areacode
	 * @return
	 */
    List<Record> finList(int first, int pagesize, String ouguid, String areacode);
    
    List<Record> finListTwo(int first, int pagesize, String ouguid, String areacode,String startDate,String endDate,String certnum);
    
    Integer finTotal(String ouguid,String areacode);
    
    Integer finTotalTwo(String ouguid,String areacode,String startDate,String endDate,String certnum);
    
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

	void updatezjproject(String rowguid,String username,String userguid);
	
	void insertQzkBaseInfo(eajcstepbasicinfogt eajcstepbasicinfogt);
	
	void deleteQzkBaseInfo(String guid);
	
	void insertQzkProcess(eajcstepprocgt eajcstepprocgt);
	
	void deleteQzkProcess(String guid);
	
	void insertQzkDone(eajcstepdonegt eajcstepdonegt);

	void insertQzkSpecialnode(eajcstepspecialnode specialnode);
	
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

	void insertSbDate(Record record);

	void insertYbDate(Record record);
	
	Record getTotalPorjectByNow();
	
	Record getSizeInPorject();

	int getInCountByAreacode(String areacode);
	
	int getNewInCountByAreacode(String areacode);

	void insertLcprojectRecord(String areaCode, int length);
	
	void insertExterRecord(String areaCode, int length);

	eajcstepbasicinfogt getQzkBaseInfo(String flowsn);

	public eajcstepbasicinfogt getQzkBaseInfoByOrgbusno(String orgbusno);

	public Record getQzkStepInfo(String orgbusno, String sn);

	public Record getQzkBanjieInfo(String orgbusno);

	int insertRestQzkBaseInfo(eajcstepbasicinfogt eajcstepbasicinfogt);

	int insertRestQzkProcess(eajcstepprocgt eajcstepprocgt);

	int insertRestQzkDone(eajcstepdonegt eajcstepdonegt);

	public int insertProject(AuditProject auditProject);

	public AuditProject getProjectByFlowsn(String flowsn);

}
