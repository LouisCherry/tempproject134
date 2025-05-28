package com.epoint.basic.auditonlineuser.auditonlineconsult.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditonlineuser.auditonlineconsult.domain.AuditDaibanConsult;
import com.epoint.basic.auditonlineuser.auditonlineconsult.inter.IAuditDaibanConsult;
import com.epoint.basic.auditonlineuser.auditonlineconsult.service.AuditDaibanConsultService;
import com.epoint.basic.auditonlineuser.service.AuditDaibanUserService;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.esotericsoftware.minlog.Log;

@Component
@Service
public class AuditDaibanConsultImpl implements IAuditDaibanConsult
{

    @Override
    public AuditCommonResult<String> addConsult(AuditDaibanConsult consult) {
        AuditDaibanUserService<AuditDaibanConsult> auditConsult = new AuditDaibanUserService<AuditDaibanConsult>();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            auditConsult.addRecord(AuditDaibanConsult.class, consult, false);
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> updateConsult(AuditDaibanConsult consult) {
        AuditDaibanUserService<AuditDaibanConsult> auditConsult = new AuditDaibanUserService<AuditDaibanConsult>();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            auditConsult.updateRecord(AuditDaibanConsult.class, consult, "rowguid", false);
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<AuditDaibanConsult> getConsultByRowguid(String rowguid) {
        AuditDaibanUserService<AuditDaibanConsult> auditConsult = new AuditDaibanUserService<AuditDaibanConsult>();
        AuditCommonResult<AuditDaibanConsult> result = new AuditCommonResult<AuditDaibanConsult>();
        try {
            AuditDaibanConsult consult = auditConsult.getDetail(AuditDaibanConsult.class, rowguid, "rowguid", false);
            result.setResult(consult);
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> deleteConsult(AuditDaibanConsult consult) {
        AuditDaibanUserService<AuditDaibanConsult> auditConsult = new AuditDaibanUserService<AuditDaibanConsult>();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            auditConsult.deleteRecod(AuditDaibanConsult.class, consult, "rowguid", false);
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> deleteConsultByRowguid(String rowguid) {
        AuditDaibanUserService<AuditDaibanConsult> auditConsult = new AuditDaibanUserService<AuditDaibanConsult>();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            AuditDaibanConsult consult = this.getConsultByRowguid(rowguid).getResult();
            auditConsult.deleteRecod(AuditDaibanConsult.class, consult, "rowguid", true);
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditDaibanConsult>> selectConsultByPage(Map<String, String> conditionMap,
            Integer first, Integer pageSize, String sortField, String sortOrder) {
        AuditDaibanUserService<AuditDaibanConsult> auditConsultService = new AuditDaibanUserService<AuditDaibanConsult>();
        AuditCommonResult<List<AuditDaibanConsult>> result = new AuditCommonResult<List<AuditDaibanConsult>>();
        try {
            PageData<AuditDaibanConsult> pageData = auditConsultService.getAllRecordByPage(AuditDaibanConsult.class,
                    conditionMap, first, pageSize, sortField, sortOrder);
            List<AuditDaibanConsult> consult = pageData.getList();
            result.setResult(consult);
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<PageData<AuditDaibanConsult>> selectConsultByPageData(Map<String, String> conditionMap,
            Integer first, Integer pageSize, String sortField, String sortOrder) {
        AuditDaibanUserService<AuditDaibanConsult> auditConsultService = new AuditDaibanUserService<AuditDaibanConsult>();
        AuditCommonResult<PageData<AuditDaibanConsult>> result = new AuditCommonResult<PageData<AuditDaibanConsult>>();
        try {
            PageData<AuditDaibanConsult> pageData = auditConsultService.getAllRecordByPage(AuditDaibanConsult.class,
                    conditionMap, first, pageSize, sortField, sortOrder);
            result.setResult(pageData);
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditDaibanConsult>> selectAuditOnlineConsultList(Map<String, String> conditionMap) {
        if (!SQLManageUtil.validate(conditionMap)) {
            Log.info("本次查询无任何条件，请检查代码！");
            return null;
        }
        AuditDaibanUserService<AuditDaibanConsult> auditConsultService = new AuditDaibanUserService<AuditDaibanConsult>();
        AuditCommonResult<List<AuditDaibanConsult>> result = new AuditCommonResult<List<AuditDaibanConsult>>();
        try {
            List<AuditDaibanConsult> auditOnlineConsults = auditConsultService.getAllRecord(AuditDaibanConsult.class,
                    conditionMap, false);
            AuditDaibanConsult auditOnlineConsultcn = new AuditDaibanConsult();
            List<AuditDaibanConsult> auditOnlineConsultscns = new ArrayList<AuditDaibanConsult>();
            for (AuditDaibanConsult auditOnlineConsult : auditOnlineConsults) {
                auditOnlineConsultcn = (AuditDaibanConsult) auditOnlineConsult.clone();
                auditOnlineConsultscns.add(auditOnlineConsultcn);
            }
            result.setResult(auditOnlineConsultscns);
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getConsultCount(Map<String, String> conditionMap) {
        AuditDaibanUserService<AuditDaibanConsult> auditConsult = new AuditDaibanUserService<AuditDaibanConsult>();
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        try {
            Integer allRecordCount = auditConsult.getAllRecordCount(AuditDaibanConsult.class, conditionMap, false);
            result.setResult(allRecordCount);
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getAnswerCount(String askerGuid, Boolean isAsked, String cosulttype) {
        AuditDaibanConsultService auditOnlineConsult = new AuditDaibanConsultService();
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        try {
            Integer allRecordCount = auditOnlineConsult.getAnswerCount(askerGuid, isAsked, cosulttype);
            result.setResult(allRecordCount);
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setSystemFail(e.toString());
        }
        return result;

    }

    @Override
    public AuditCommonResult<String> updateConsultByField(String rowGuid, String key, String value) {
        AuditDaibanUserService<AuditDaibanConsult> auditConsult = new AuditDaibanUserService<AuditDaibanConsult>();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            auditConsult.updateRecord(AuditDaibanConsult.class, rowGuid, key, value);
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<PageData<AuditDaibanConsult>> getAuditOnlineConsultListByPage(
            Map<String, String> conditionMap, int first, int pageSize, String sortField, String sortOrder) {
        AuditDaibanConsultService auditOnlineConsultService = new AuditDaibanConsultService();
        AuditCommonResult<PageData<AuditDaibanConsult>> result = new AuditCommonResult<PageData<AuditDaibanConsult>>();
        try {
            PageData<AuditDaibanConsult> taskList = new PageData<AuditDaibanConsult>();
            taskList = auditOnlineConsultService.getAuditOnlineConsultListByPage(conditionMap, first, pageSize,
                    sortField, sortOrder);
            result.setResult(taskList);
        }
        catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }
}
