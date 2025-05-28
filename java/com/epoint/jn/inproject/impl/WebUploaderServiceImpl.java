package com.epoint.jn.inproject.impl;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.grammar.Record;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.hcp.api.entity.*;
import com.epoint.jn.inproject.api.IWebUploaderService;
import com.epoint.jn.inproject.api.entity.lcproject;
import com.epoint.jn.inproject.api.entity.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service
public class WebUploaderServiceImpl implements IWebUploaderService {

    /**
     *
     * @param first
     * @param pagesize
     * @param ouguid
     * @param areacode
     * @return
     */
    public List<Record> finList(int first, int pagesize, String ouguid, String areacode) {
        return new WebUploaderService().finList(first, pagesize, ouguid, areacode);
    }

    public List<Record> finListTwo(int first, int pagesize, String ouguid, String areacode, String startDate, String endDate, String certnum) {
        return new WebUploaderService().finListTwo(first, pagesize, ouguid, areacode, startDate, endDate, certnum);
    }

    /**
     * 删除数据
     * @return int
     */
    public int deleteByGuid(String guid) {
        return new WebUploaderService().deleteByGuid(guid);
    }

    public Integer finTotal(String ouguid, String areacode) {
        return new WebUploaderService().finTotal(ouguid, areacode);
    }

    public Integer finTotalTwo(String ouguid, String areacode, String startDate, String endDate, String certnum) {
        return new WebUploaderService().finTotalTwo(ouguid, areacode, startDate, endDate, certnum);
    }

    public void updatezjproject(String rowguid, String username, String userguid) {
        new WebUploaderService().updatezjproject(rowguid, username, userguid);
    }

    public void insert(lcproject record) {
        new WebUploaderService().insert(record);
    }

    public void insert(lcprojecttwo record) {
        new WebUploaderService().insert(record);
    }

    public void insert(lcprojectthree record) {
        new WebUploaderService().insert(record);
    }

    public void insert(lcprojectfour record) {
        new WebUploaderService().insert(record);
    }

    public void insert(lcprojectfive record) {
        new WebUploaderService().insert(record);
    }

    public void insert(lcprojectsix record) {
        new WebUploaderService().insert(record);
    }

    public void insert(lcprojectseven record) {
        new WebUploaderService().insert(record);
    }

    public void insert(lcprojecteight record) {
        new WebUploaderService().insert(record);
    }

    public void insert(lcprojectnine record) {
        new WebUploaderService().insert(record);
    }

    public void insert(lcprojectten record) {
        new WebUploaderService().insert(record);
    }

    public void insertQzkBaseInfo(eajcstepbasicinfogt record) {
        new WebUploaderService().insertQzkBaseInfo(record);
    }

    public void insertQzkProcess(eajcstepprocgt record) {
        new WebUploaderService().insertQzkProcess(record);
    }

    public void insertQzkDone(eajcstepdonegt record) {
        new WebUploaderService().insertQzkDone(record);
    }

    @Override
    public void insertQzkSpecialnode(eajcstepspecialnode specialnode) {
        new WebUploaderService().insertQzkSpecialnode(specialnode);
    }

    @Deprecated
    public void insertQzkBaseInfonew(eajcstepbasicinfogtnew record) {
        new WebUploaderService().insertQzkBaseInfonew(record);
    }

    @Deprecated
    public void insertQzkProcessnew(eajcstepprocgtnew record) {
        new WebUploaderService().insertQzkProcessnew(record);
    }

    @Deprecated
    public void insertQzkDonenew(eajcstepdonegtnew record) {
        new WebUploaderService().insertQzkDonenew(record);
    }

    public AuditTask getAuditTaskByTaskname(String taskname, String areacode, String ouguid) {
        return new WebUploaderService().getAuditTaskByTaskname(taskname, areacode, ouguid);
    }

    public FrameOu getFrameOuByOuname(String ouname) {
        return new WebUploaderService().getFrameOuByOuname(ouname);
    }

    public FrameOu getFrameOuByOunameNew(String ouname, String areacode) {
        return new WebUploaderService().getFrameOuByOunameNew(ouname, areacode);
    }

    public Record getJnzjProjectByRowguid(String rowguid) {
        return new WebUploaderService().getJnzjProjectByRowguid(rowguid);
    }

    public void updatezjprojectByrowguid(String syncdone, String rowguid) {
        new WebUploaderService().updatezjprojectByrowguid(syncdone, rowguid);
    }

    public void deletejprojectByguid(String rowguid) {
        new WebUploaderService().deletejprojectByguid(rowguid);
    }

    @Override
    public void insertSbDate(Record record) {
        new WebUploaderService().insertSbDate(record);
    }

    @Override
    public void insertYbDate(Record record) {
        new WebUploaderService().insertYbDate(record);
    }

    @Override
    public Record getTotalPorjectByNow() {
        return new WebUploaderService().getTotalPorjectByNow();
    }

    public Record getSizeInPorject() {
        return new WebUploaderService().getSizeInPorject();
    }

    @Override
    public int getInCountByAreacode(String areacode) {
        return new WebUploaderService().getInCountByAreacode(areacode);
    }

    @Override
    public int getNewInCountByAreacode(String areacode) {
        return new WebUploaderService().getNewInCountByAreacode(areacode);
    }

    @Override
    public void insertLcprojectRecord(String areacode, int count) {
        new WebUploaderService().insertLcprojectRecord(areacode, count);
    }

    @Override
    public void insertExterRecord(String areacode, int count) {
        new WebUploaderService().insertExterRecord(areacode, count);
    }

    @Override
    public void deleteQzkBaseInfo(String guid) {
        new WebUploaderService().deleteQzkBaseInfo(guid);
    }

    @Override
    public void deleteQzkProcess(String guid) {
        new WebUploaderService().deleteQzkProcess(guid);
    }

    @Override
    public void deleteQzkDone(String guid) {
        new WebUploaderService().deleteQzkDone(guid);
    }

    @Override
    public eajcstepbasicinfogt getQzkBaseInfo(String flowsn) {
        return new WebUploaderService().getQzkBaseInfo(flowsn);
    }

    @Override
    public eajcstepbasicinfogt getQzkBaseInfoByOrgbusno(String orgbusno) {
        return new WebUploaderService().getQzkBaseInfoByOrgbusno(orgbusno);
    }

    @Override
    public Record getQzkStepInfo(String orgbusno, String sn) {
        return new WebUploaderService().getQzkStepInfo(orgbusno, sn);
    }

    @Override
    public Record getQzkBanjieInfo(String orgbusno) {
        return new WebUploaderService().getQzkBanjieInfo(orgbusno);
    }

    @Override
    public int insertRestQzkBaseInfo(eajcstepbasicinfogt eajcstepbasicinfogt) {
        return new WebUploaderService().insertRestQzkBaseInfo(eajcstepbasicinfogt);
    }

    @Override
    public int insertRestQzkProcess(eajcstepprocgt eajcstepprocgt) {
        return new WebUploaderService().insertRestQzkProcess(eajcstepprocgt);
    }

    @Override
    public int insertRestQzkDone(eajcstepdonegt eajcstepdonegt) {
        return new WebUploaderService().insertRestQzkDone(eajcstepdonegt);
    }

    @Override
    public int insertProject(AuditProject auditProject) {
        return new WebUploaderService().insertProject(auditProject);
    }

    @Override
    public AuditProject getProjectByFlowsn(String flowsn) {
        return new WebUploaderService().getProjectByFlowsn(flowsn);
    }

}
