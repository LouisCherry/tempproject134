package com.epoint.hcp.api;

import java.io.Serializable;
import java.util.List;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.core.grammar.Record;
import com.epoint.hcp.api.entity.Evainstance;
import com.epoint.hcp.api.entity.EvainstanceRecord;
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
import com.epoint.hcp.externalprojectinfo.api.entity.ExternalProjectInfo;
import com.epoint.xmz.action.AuditProjectZjxtController;

/**
 * 好差评相关接口
 * @作者 atjiao
 * @version [版本号, 2020年6月8日]
 */
public interface IHcpService extends Serializable
{

    int addEvaluateService(Record r);

    int addEvaluate(Record r);

    Record getServiceByProjectno(String projectno, int assessNumber,String month);

    Record getInatanceByRowguid(String rowguid);

    void updateEva(Record r);
    
    void updateEvaRecord(Record r);
    
    void updateEvaCk(Record r);

    Boolean findEva(String projectno, int assessNumber);

    List<Record> getMyEvaluate(String projectname, String applyername, String applyerpagecode, Integer status,
            Integer currentpage, Integer pagesize);

    int getMyEvaluateCount(String projectname, String applyername, String applyerpagecode, Integer status);

    void updateProService(Record r,String month);

    Record getProService(String projectno, int servicenumber);

    List<Record> getWaitEvaluateList(int start,int size);

	Record findEvaluateservice(String projectno, String servicenumber);
	
	Record findEvaluateserviceck(String projectno, String servicenumber);

	List<AuditProject> getWaitEvaluateSbList();

	List<Evainstance> getHcpInstacneSuppList();
	
	List<EvainstanceRecord> getHcpInstacneTwoSuppList();

	List<lcproject> getLcEvaluateSbList(int start,int size);

	List<ExternalProjectInfo> getExterEvaluateList(int start,int size,String month);
	
	List<lcprojectten> getProjectMoveList(int start,int size);

	List<Record> getOneTwoEvaluateList();

	 void updateLcProject(String status, String rowguid);

	 void updateExterProject(String status, String rowguid,String month);

    List<Record> findEvaluateserviceByFlowsn(String s);

    void updateProject(String projectno, String s);

    Record getServiceByProjectnooneold(String projectno, int assessNumber);
    
	Record getServiceByProjectnotwo(String projectno, int assessNumber);

	Record getServiceByProjectnothree(String projectno, int assessNumber);

	Record getServiceByProjectnofour(String projectno, int assessNumber);

	Record getServiceByProjectnofive(String projectno, int assessNumber);

	Record getServiceByProjectnosix(String projectno, int assessNumber);

	Record getServiceByProjectnoseven(String projectno, int assessNumber);

	Record getServiceByProjectnoeight(String projectno, int assessNumber);

	Record getServiceByProjectnonine(String projectno, int assessNumber);

	Record getServiceByProjectnoten(String projectno, int assessNumber);

	void updateProServiceoneold(Record r);
	
	void updateProServicetwo(Record r);

	void updateProServicethree(Record r);

	void updateProServicefour(Record r);

	void updateProServicefive(Record r);

	void updateProServicesix(Record r);

	void updateProServiceseven(Record r);

	void updateProServiceeight(Record r);

	void updateProServicenine(Record r);

	void updateProServiceten(Record r);

	List<Record> getWaitEvaluateTwoList(int start, int size);

	List<Record> getWaitEvaluateThreeList(int start, int size);

	List<Record> getWaitEvaluateFourList(int start, int size);

	List<Record> getWaitEvaluateFiveList(int start, int size);

	List<Record> getWaitEvaluateSixList(int start, int size);

	List<Record> getWaitEvaluateSevenList(int start, int size);

	List<Record> getWaitEvaluateEightList(int start, int size);

	List<Record> getWaitEvaluateNineList(int start, int size);

	List<Record> getWaitEvaluateTenList(int start, int size);

	Record findEvaluateservicetwo(String projectno, String servicenumber);

	Record findEvaluateservicethree(String projectno, String servicenumber);

	Record findEvaluateservicefour(String projectno, String servicenumber);

	Record findEvaluateservicefive(String projectno, String servicenumber);

	Record findEvaluateservicesix(String projectno, String servicenumber);

	Record findEvaluateserviceseven(String projectno, String servicenumber);

	Record findEvaluateserviceeight(String projectno, String servicenumber);

	Record findEvaluateservicenine(String projectno, String servicenumber);

	Record findEvaluateserviceten(String projectno, String servicenumber);

	List<lcprojecttwo> getLcEvaluateSbTwoList(int start, int size);

	lcprojecttwo getlcprojecttwoByRowguid(String rowguid);

	List<lcprojectthree> getLcEvaluateSbThreeList(int start, int size);

	List<lcprojectfour> getLcEvaluateSbFourList(int start, int size);

	List<lcprojectfive> getLcEvaluateSbFiveList(int start, int size);

	List<lcprojectsix> getLcEvaluateSbSixList(int start, int size);

	List<lcprojectseven> getLcEvaluateSbSevenList(int start, int size);

	List<lcprojecteight> getLcEvaluateSbEightList(int start, int size);

	List<lcprojectnine> getLcEvaluateSbNineList(int start, int size);

	List<lcprojectten> getLcEvaluateSbTenList(int start, int size);

	void updateLcProjecttwo(String status, String rowguid);

	void updateLcProjectthree(String status, String rowguid);

	void updateLcProjectfour(String status, String rowguid);

	void updateLcProjectfive(String status, String rowguid);

	void updateLcProjectsix(String status, String rowguid);

	void updateLcProjectseven(String status, String rowguid);

	void updateLcProjecteight(String status, String rowguid);

	void updateLcProjectnine(String status, String rowguid);

	void updateLcProjectten(String status, String rowguid);

	List<Record> getAuditProjectZjxtList(int start, int size, String month);

	void updateAuditProjectZjxt(String status, String rowguid);
}
