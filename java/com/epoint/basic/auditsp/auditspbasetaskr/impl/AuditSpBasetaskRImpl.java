package com.epoint.basic.auditsp.auditspbasetaskr.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditsp.auditspbasetaskr.domain.AuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspbasetaskr.inter.IAuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspbasetaskr.service.AuditSpbasetaskrService;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;

@Component
@Service
public class AuditSpBasetaskRImpl implements IAuditSpBasetaskR
{

    @Override
    public AuditCommonResult<Void> addBasetaskR(AuditSpBasetaskR basetaskr) {
        AuditSpbasetaskrService auditspbasetaskrservice = new AuditSpbasetaskrService();
        AuditCommonResult<Void> result = new AuditCommonResult<Void>();
        auditspbasetaskrservice.addBasetaskR(basetaskr);
        return result;
    }

    @Override
    public AuditCommonResult<Void> updateBasetaskR(AuditSpBasetaskR basetaskr) {
        AuditSpbasetaskrService auditspbasetaskrservice = new AuditSpbasetaskrService();
        AuditCommonResult<Void> result = new AuditCommonResult<Void>();
        auditspbasetaskrservice.updateBasetaskR(basetaskr);
        return result;
    }

    @Override
    public AuditCommonResult<AuditSpBasetaskR> getAuditSpBasetaskRByrowguid(String rowGuid) {
        AuditSpbasetaskrService auditspbasetaskrservice = new AuditSpbasetaskrService();
        AuditCommonResult<AuditSpBasetaskR> result = new AuditCommonResult<AuditSpBasetaskR>();
        result.setResult(auditspbasetaskrservice.getAuditSpBasetaskRByrowguid(rowGuid));
        return result;
    }

    @Override
    public AuditCommonResult<Void> delAuditSpBasetaskR(String rowGuid) {
        AuditSpbasetaskrService auditspbasetaskrservice = new AuditSpbasetaskrService();
        AuditCommonResult<Void> result = new AuditCommonResult<Void>();
        auditspbasetaskrservice.delAuditSpBasetaskR(rowGuid);
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditSpBasetaskR>> getAuditSpBasetaskrByCondition(Map<String, String> conditionMap) {
        AuditSpbasetaskrService auditspbasetaskrservice = new AuditSpbasetaskrService();
        AuditCommonResult<List<AuditSpBasetaskR>> result = new AuditCommonResult<List<AuditSpBasetaskR>>();
        result.setResult(auditspbasetaskrservice.getAuditSpBasetaskrByCondition(conditionMap));
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditSpBasetaskR>> getAuditSpBasetaskAndRByCondition(
            Map<String, String> conditionMap) {
        AuditSpbasetaskrService auditspbasetaskrservice = new AuditSpbasetaskrService();
        AuditCommonResult<List<AuditSpBasetaskR>> result = new AuditCommonResult<List<AuditSpBasetaskR>>();
        result.setResult(auditspbasetaskrservice.getAuditSpBasetaskAndRByCondition(conditionMap));
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getAuditSpBasetaskrCountByCondition(Map<String, String> conditionMap) {
        AuditSpbasetaskrService auditspbasetaskrservice = new AuditSpbasetaskrService();
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        result.setResult(auditspbasetaskrservice.getAuditSpBasetaskrCountByCondition(conditionMap));
        return result;
    }

    @Override
    public AuditCommonResult<Void> delByBasetaskguid(String basetaskrguid) {
        AuditSpbasetaskrService auditspbasetaskrservice = new AuditSpbasetaskrService();
        AuditCommonResult<Void> result = new AuditCommonResult<Void>();
        auditspbasetaskrservice.delByBasetaskguid(basetaskrguid);
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getTaskidlistbyBasetaskOuname(String ouname, List<String> basetaskguids) {
        AuditSpbasetaskrService auditspbasetaskrservice = new AuditSpbasetaskrService();
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        result.setResult(auditspbasetaskrservice.getTaskidlistbyBasetaskOuname(ouname, basetaskguids));
        return result;
    }

    @Override
    public AuditCommonResult<List<String>> getTaskidlistbyPhaseid(String phaseid) {
        AuditSpbasetaskrService auditspbasetaskrservice = new AuditSpbasetaskrService();
        AuditCommonResult<List<String>> result = new AuditCommonResult<List<String>>();
        result.setResult(auditspbasetaskrservice.getTaskidlistbyPhaseid(phaseid));
        return result;
    }

    @Override
    public AuditCommonResult<List<String>> getTaskidlistbySpecialPhaseid(String phaseid) {
        AuditSpbasetaskrService auditspbasetaskrservice = new AuditSpbasetaskrService();
        AuditCommonResult<List<String>> result = new AuditCommonResult<List<String>>();
        result.setResult(auditspbasetaskrservice.getTaskidlistbySpecialPhaseid(phaseid));
        return result;
    }

    @Override
    public AuditCommonResult<List<String>> getBasetaskguidsBytaskids(List<String> taskids) {
        AuditSpbasetaskrService auditspbasetaskrservice = new AuditSpbasetaskrService();
        AuditCommonResult<List<String>> result = new AuditCommonResult<List<String>>();
        result.setResult(auditspbasetaskrservice.getBasetaskguidsBytaskids(taskids));
        return result;
    }

    @Override
    public AuditCommonResult<List<String>> gettaskidsByBusinedssguid(String businedssguid, String phaseid) {
        AuditSpbasetaskrService auditspbasetaskrservice = new AuditSpbasetaskrService();
        AuditCommonResult<List<String>> result = new AuditCommonResult<List<String>>();
        result.setResult(auditspbasetaskrservice.gettaskidsByBusinedssguid(businedssguid, phaseid));
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getBasetaskByAreacodelistandTaskid(String taskid,
            List<String> areacodeList) {
        AuditSpbasetaskrService auditspbasetaskrservice = new AuditSpbasetaskrService();
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        result.setResult(auditspbasetaskrservice.getBasetaskByAreacodelistandTaskid(taskid, areacodeList));
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getBasetaskByAreacodelistandTaskid(String taskid, List<String> areacodeList,
            List<String> basetaskList) {
        AuditSpbasetaskrService auditspbasetaskrservice = new AuditSpbasetaskrService();
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        result.setResult(
                auditspbasetaskrservice.getBasetaskByAreacodelistandTaskid(taskid, areacodeList, basetaskList));
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getBasetaskByTaskid(String taskid) {
        AuditSpbasetaskrService auditspbasetaskrservice = new AuditSpbasetaskrService();
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        result.setResult(auditspbasetaskrservice.getBasetaskByTaskid(taskid));
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getBasetaskrByTaskIdAndBusinessType(String taskid, String BusinessType) {
        AuditSpbasetaskrService auditspbasetaskrservice = new AuditSpbasetaskrService();
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        result.setResult(auditspbasetaskrservice.getBasetaskrByTaskIdAndBusinessType(taskid, BusinessType));
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getBasetaskByAreacodelistandTaskidNew(String taskid,
            List<String> areacodeList, String type) {
        AuditSpbasetaskrService auditspbasetaskrservice = new AuditSpbasetaskrService();
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        result.setResult(auditspbasetaskrservice.getBasetaskByAreacodelistandTaskidNew(taskid, areacodeList, type));
        return result;
    }

    @Override
    public List<String> getAlreadyUsedTaskid(String businesstype) {
        AuditSpbasetaskrService auditspbasetaskrservice = new AuditSpbasetaskrService();
        return auditspbasetaskrservice.getAlreadyUsedTaskid(businesstype);
    }
}
