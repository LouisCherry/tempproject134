package com.epoint.hcp.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.core.grammar.Record;
import com.epoint.hcp.api.IHcpService;
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

/**
 * 好差评相关接口的实现
 * @作者 atjiao
 * @version [版本号, 2020年6月8日]
 */
@Component
@Service
public class HcpServiceImpl implements IHcpService
{

    /**
     *
     */
    private static final long serialVersionUID = 3258268474743087728L;

    @Override
    public int addEvaluateService(Record r) {
        return new HcpService().addEvaluateService(r);
    }

    @Override
    public int addEvaluate(Record r) {
        return new HcpService().addEvaluate(r);
    }

    @Override
    public Record getServiceByProjectno(String projectno, int assessNumber,String month) {
        return new HcpService().getServiceByProjectno(projectno, assessNumber,month);
    }

    @Override
    public Record getServiceByProjectnooneold(String projectno, int assessNumber) {
    	return new HcpService().getServiceByProjectnooneold(projectno, assessNumber);
    }
    @Override
    public Record getServiceByProjectnotwo(String projectno, int assessNumber) {
    	return new HcpService().getServiceByProjectnotwo(projectno, assessNumber);
    }

    @Override
    public Record getServiceByProjectnothree(String projectno, int assessNumber) {
    	return new HcpService().getServiceByProjectnothree(projectno, assessNumber);
    }

    @Override
    public Record getServiceByProjectnofour(String projectno, int assessNumber) {
    	return new HcpService().getServiceByProjectnofour(projectno, assessNumber);
    }

    @Override
    public Record getServiceByProjectnofive(String projectno, int assessNumber) {
    	return new HcpService().getServiceByProjectnofive(projectno, assessNumber);
    }

    @Override
    public Record getServiceByProjectnosix(String projectno, int assessNumber) {
    	return new HcpService().getServiceByProjectnosix(projectno, assessNumber);
    }

    @Override
    public Record getServiceByProjectnoseven(String projectno, int assessNumber) {
    	return new HcpService().getServiceByProjectnoseven(projectno, assessNumber);
    }

    @Override
    public Record getServiceByProjectnoeight(String projectno, int assessNumber) {
    	return new HcpService().getServiceByProjectnoeight(projectno, assessNumber);
    }

    @Override
    public Record getServiceByProjectnonine(String projectno, int assessNumber) {
    	return new HcpService().getServiceByProjectnonine(projectno, assessNumber);
    }

    @Override
    public Record getServiceByProjectnoten(String projectno, int assessNumber) {
    	return new HcpService().getServiceByProjectnoten(projectno, assessNumber);
    }

    @Override
    public Record getInatanceByRowguid(String rowguid) {
    	return new HcpService().getInatanceByRowguid(rowguid);
    }

    @Override
    public void updateEva(Record r) {
        new HcpService().updateEva(r);
    }
    
    @Override
    public void updateEvaRecord(Record r) {
    	new HcpService().updateEvaRecord(r);
    }
    
    @Override
    public void updateEvaCk(Record r) {
    	new HcpService().updateEvaCk(r);
    }

    @Override
    public Boolean findEva(String projectno, int assessNumber) {
        return new HcpService().findEva(projectno, assessNumber);
    }

    @Override
    public List<Record> getMyEvaluate(String projectname, String applyername, String applyerpagecode, Integer status,
            Integer currentpage, Integer pagesize) {
        return new HcpService().getMyEvaluate(projectname, applyername, applyerpagecode, status, currentpage, pagesize);
    }

    @Override
    public int getMyEvaluateCount(String projectname, String applyername, String applyerpagecode, Integer status) {
        return new HcpService().getMyEvaluateCount(projectname, applyername, applyerpagecode, status);
    }

    @Override
    public void updateProService(Record r,String month) {
        new HcpService().updateProService(r,month);
    }

    @Override
    public void updateProServiceoneold(Record r) {
    	new HcpService().updateProServiceoneold(r);
    }
    
    @Override
    public void updateProServicetwo(Record r) {
    	new HcpService().updateProServicetwo(r);
    }

    @Override
    public void updateProServicethree(Record r) {
    	new HcpService().updateProServicethree(r);
    }

    @Override
    public void updateProServicefour(Record r) {
    	new HcpService().updateProServicefour(r);
    }

    @Override
    public void updateProServicefive(Record r) {
    	new HcpService().updateProServicefive(r);
    }

    @Override
    public void updateProServicesix(Record r) {
    	new HcpService().updateProServicesix(r);
    }

    @Override
    public void updateProServiceseven(Record r) {
    	new HcpService().updateProServiceseven(r);
    }

    @Override
    public void updateProServiceeight(Record r) {
    	new HcpService().updateProServiceeight(r);
    }

    @Override
    public void updateProServicenine(Record r) {
    	new HcpService().updateProServicenine(r);
    }

    @Override
    public void updateProServiceten(Record r) {
    	new HcpService().updateProServiceten(r);
    }

    @Override
    public Record getProService(String projectno, int servicenumber) {
        return new HcpService().getProService(projectno, servicenumber);
    }

    @Override
    public List<Record> getWaitEvaluateList(int start,int size) {
        return new HcpService().getWaitEvaluateList(start,size);
    }

    @Override
    public List<Record> getWaitEvaluateTwoList(int start,int size) {
    	return new HcpService().getWaitEvaluateTwoList(start,size);
    }

    @Override
    public List<Record> getWaitEvaluateThreeList(int start,int size) {
    	return new HcpService().getWaitEvaluateThreeList(start,size);
    }

    @Override
    public List<Record> getWaitEvaluateFourList(int start,int size) {
    	return new HcpService().getWaitEvaluateFourList(start,size);
    }

    @Override
    public List<Record> getWaitEvaluateFiveList(int start,int size) {
    	return new HcpService().getWaitEvaluateFiveList(start,size);
    }

    @Override
    public List<Record> getWaitEvaluateSixList(int start,int size) {
    	return new HcpService().getWaitEvaluateSixList(start,size);
    }

    @Override
    public List<Record> getWaitEvaluateSevenList(int start,int size) {
    	return new HcpService().getWaitEvaluateSevenList(start,size);
    }

    @Override
    public List<Record> getWaitEvaluateEightList(int start,int size) {
    	return new HcpService().getWaitEvaluateEightList(start,size);
    }

    @Override
    public List<Record> getWaitEvaluateNineList(int start,int size) {
    	return new HcpService().getWaitEvaluateNineList(start,size);
    }

    @Override
    public List<Record> getWaitEvaluateTenList(int start,int size) {
    	return new HcpService().getWaitEvaluateTenList(start,size);
    }

    @Override
    public List<Record> getOneTwoEvaluateList() {
    	return new HcpService().getOneTwoEvaluateList();
    }

    @Override
    public Record findEvaluateservice(String projectno, String servicenumber) {
        return new HcpService().findEvaluateservice(projectno, servicenumber);
    }
    
    @Override
    public Record findEvaluateserviceck(String projectno, String servicenumber) {
    	return new HcpService().findEvaluateserviceck(projectno, servicenumber);
    }

    @Override
    public Record findEvaluateservicetwo(String projectno, String servicenumber) {
    	return new HcpService().findEvaluateservicetwo(projectno, servicenumber);
    }

    @Override
    public Record findEvaluateservicethree(String projectno, String servicenumber) {
    	return new HcpService().findEvaluateservicethree(projectno, servicenumber);
    }

    @Override
    public Record findEvaluateservicefour(String projectno, String servicenumber) {
    	return new HcpService().findEvaluateservicefour(projectno, servicenumber);
    }

    @Override
    public Record findEvaluateservicefive(String projectno, String servicenumber) {
    	return new HcpService().findEvaluateservicefive(projectno, servicenumber);
    }

    @Override
    public Record findEvaluateservicesix(String projectno, String servicenumber) {
    	return new HcpService().findEvaluateservicesix(projectno, servicenumber);
    }

    @Override
    public Record findEvaluateserviceseven(String projectno, String servicenumber) {
    	return new HcpService().findEvaluateserviceseven(projectno, servicenumber);
    }

    @Override
    public Record findEvaluateserviceeight(String projectno, String servicenumber) {
    	return new HcpService().findEvaluateserviceeight(projectno, servicenumber);
    }

    @Override
    public Record findEvaluateservicenine(String projectno, String servicenumber) {
    	return new HcpService().findEvaluateservicenine(projectno, servicenumber);
    }

    @Override
    public Record findEvaluateserviceten(String projectno, String servicenumber) {
    	return new HcpService().findEvaluateserviceten(projectno, servicenumber);
    }

    @Override
    public List<AuditProject> getWaitEvaluateSbList() {
        return new HcpService().getWaitEvaluateSbList();
    }

    @Override
    public List<Evainstance> getHcpInstacneSuppList() {
    	return new HcpService().getHcpInstacneSuppList();
    }
    
    @Override
    public List<EvainstanceRecord> getHcpInstacneTwoSuppList() {
    	return new HcpService().getHcpInstacneTwoSuppList();
    }

    @Override
    public List<lcproject> getLcEvaluateSbList(int start,int size) {
        return new HcpService().getLcEvaluateSbList(start,size);
    }

    @Override
    public List<ExternalProjectInfo> getExterEvaluateList(int start,int size,String month) {
    	return new HcpService().getExterEvaluateList(start,size,month);
    }
    
    @Override
    public List<lcprojectten> getProjectMoveList(int start,int size) {
	    return new HcpService().getProjectMoveList(start,size);
	} 

    @Override
    public List<lcprojecttwo> getLcEvaluateSbTwoList(int start,int size) {
    	return new HcpService().getLcEvaluateSbTwoList(start,size);
    }

    @Override
    public lcprojecttwo getlcprojecttwoByRowguid(String rowguid) {
        return new HcpService().getlcprojecttwoByRowguid(rowguid);
    }

    @Override
    public List<lcprojectthree> getLcEvaluateSbThreeList(int start,int size) {
    	return new HcpService().getLcEvaluateSbThreeList(start,size);
    }

    @Override
    public List<lcprojectfour> getLcEvaluateSbFourList(int start,int size) {
    	return new HcpService().getLcEvaluateSbFourList(start,size);
    }

    @Override
    public List<lcprojectfive> getLcEvaluateSbFiveList(int start,int size) {
    	return new HcpService().getLcEvaluateSbFiveList(start,size);
    }

    @Override
    public List<lcprojectsix> getLcEvaluateSbSixList(int start,int size) {
    	return new HcpService().getLcEvaluateSbSixList(start,size);
    }

    @Override
    public List<lcprojectseven> getLcEvaluateSbSevenList(int start,int size) {
    	return new HcpService().getLcEvaluateSbSevenList(start,size);
    }

    @Override
    public List<lcprojecteight> getLcEvaluateSbEightList(int start,int size) {
    	return new HcpService().getLcEvaluateSbEightList(start,size);
    }

    @Override
    public List<lcprojectnine> getLcEvaluateSbNineList(int start,int size) {
    	return new HcpService().getLcEvaluateSbNineList(start,size);
    }

    @Override
    public List<lcprojectten> getLcEvaluateSbTenList(int start,int size) {
    	return new HcpService().getLcEvaluateSbTenList(start,size);
    }

    @Override
    public void updateLcProject(String status, String rowguid) {
        new HcpService().updateLcProject(status,rowguid);
    }

    @Override
    public void updateExterProject(String status, String rowguid,String month) {
    	new HcpService().updateExterProject(status,rowguid,month);
    }

    @Override
    public void updateLcProjecttwo(String status, String rowguid) {
    	new HcpService().updateLcProjecttwo(status,rowguid);
    }

    @Override
    public void updateLcProjectthree(String status, String rowguid) {
    	new HcpService().updateLcProjectthree(status,rowguid);
    }

    @Override
    public void updateLcProjectfour(String status, String rowguid) {
    	new HcpService().updateLcProjectfour(status,rowguid);
    }

    @Override
    public void updateLcProjectfive(String status, String rowguid) {
    	new HcpService().updateLcProjectfive(status,rowguid);
    }

    @Override
    public void updateLcProjectsix(String status, String rowguid) {
    	new HcpService().updateLcProjectsix(status,rowguid);
    }

    @Override
    public void updateLcProjectseven(String status, String rowguid) {
    	new HcpService().updateLcProjectseven(status,rowguid);
    }

    @Override
    public void updateLcProjecteight(String status, String rowguid) {
    	new HcpService().updateLcProjecteight(status,rowguid);
    }

    @Override
    public void updateLcProjectnine(String status, String rowguid) {
    	new HcpService().updateLcProjectnine(status,rowguid);
    }

    @Override
    public void updateLcProjectten(String status, String rowguid) {
    	new HcpService().updateLcProjectten(status,rowguid);
    }

    @Override
    public List<Record> findEvaluateserviceByFlowsn(String s) {
        return new HcpService().findEvaluateserviceByFlowsn(s);
    }

    @Override
    public void updateProject(String projectno, String s) {
        new HcpService().updateProject(projectno,s);
    }

    @Override
    public List<Record> getAuditProjectZjxtList(int start, int size, String month){
        return new HcpService().getAuditProjectZjxtList(start,size,month);
    }

    @Override
    public void updateAuditProjectZjxt(String status, String rowguid){
        new HcpService().updateAuditProjectZjxt(status,rowguid);
    }
}
