package com.epoint.auditsp.auditspbasetaskp.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.auditsp.auditspbasetaskp.domain.AuditSpBasetaskP;
import com.epoint.auditsp.auditspbasetaskp.inter.IAuditSpBasetaskP;
import com.epoint.auditsp.auditspbasetaskp.service.AuditSpbasetaskPService;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Service
public class AuditSpBasetaskPImpl implements IAuditSpBasetaskP {

    @Override
    public AuditCommonResult<Void> addBasetask(AuditSpBasetaskP basetask) {
        AuditSpbasetaskPService auditspbasetaskservice = new AuditSpbasetaskPService();
        AuditCommonResult<Void> result = new AuditCommonResult<Void>();
        auditspbasetaskservice.addBasetask(basetask);
        return result;
    }

    @Override
    public AuditCommonResult<Void> updateBasetask(AuditSpBasetaskP business) {
        AuditSpbasetaskPService auditspbasetaskservice = new AuditSpbasetaskPService();
        AuditCommonResult<Void> result = new AuditCommonResult<Void>();
        auditspbasetaskservice.updateBasetask(business);
        return result;
    }

    @Override
    public AuditCommonResult<AuditSpBasetaskP> getAuditSpBasetaskPByrowguid(String rowGuid) {
        AuditSpbasetaskPService auditspbasetaskservice = new AuditSpbasetaskPService();
        AuditCommonResult<AuditSpBasetaskP> result = new AuditCommonResult<AuditSpBasetaskP>();
        result.setResult(auditspbasetaskservice.getAuditSpBasetaskPByrowguid(rowGuid));
        return result;
    }

    @Override
    public AuditCommonResult<Void> delAuditSpBasetaskPByrowguid(String rowGuid) {
        AuditSpbasetaskPService auditspbasetaskservice = new AuditSpbasetaskPService();
        AuditCommonResult<Void> result = new AuditCommonResult<Void>();
        auditspbasetaskservice.delAuditSpBasetaskPByrowguid(rowGuid);
        return result;
    }

    @Override
    public AuditCommonResult<PageData<AuditSpBasetaskP>> getAuditSpBasetaskPByPage(Map<String, String> conditionMap,
                                                                                   int first, int pageSize, String sortField, String sortOrder) {
        AuditSpbasetaskPService auditspbasetaskservice = new AuditSpbasetaskPService();
        AuditCommonResult<PageData<AuditSpBasetaskP>> result = new AuditCommonResult<PageData<AuditSpBasetaskP>>();
        result.setResult(
                auditspbasetaskservice.getAuditSpBasetaskPByPage(conditionMap, first, pageSize, sortField, sortOrder));
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditSpBasetaskP>> getAuditSpBasetaskPByCondition(Map<String, String> conditionMap) {
        AuditCommonResult<List<AuditSpBasetaskP>> result = new AuditCommonResult<List<AuditSpBasetaskP>>();
        AuditSpbasetaskPService auditspbasetaskservice = new AuditSpbasetaskPService();
        result.setResult(auditspbasetaskservice.getAuditSpBasetaskPByCondition(conditionMap));
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getCountByCondition(Map<String, String> condition) {
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        AuditSpbasetaskPService auditspbasetaskservice = new AuditSpbasetaskPService();
        result.setResult(auditspbasetaskservice.getCountByCondition(condition));
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getParentBaseTaskList(int pageNumber, int pageSize, String taskname,
                                                                 String ouname, String phaseid) {
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        AuditSpbasetaskPService auditspbasetaskservice = new AuditSpbasetaskPService();
        result.setResult(auditspbasetaskservice.getParentBaseTaskList(pageNumber, pageSize, taskname, ouname, phaseid));
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getParentBaseTaskCount(String taskname, String ouname, String phaseid) {
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        AuditSpbasetaskPService auditspbasetaskservice = new AuditSpbasetaskPService();
        result.setResult(auditspbasetaskservice.getParentBaseTaskCount(taskname, ouname, phaseid));
        return result;
    }

}
