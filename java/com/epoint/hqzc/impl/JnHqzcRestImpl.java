package com.epoint.hqzc.impl;

import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.hqzc.api.IJnHqzcRestService;

@Component
@Service
public class JnHqzcRestImpl implements IJnHqzcRestService
{

    public AuditCommonResult<List<Record>> selectOuList(String areacode) {
        JnHqzcRestService auditTaskBasicService = new JnHqzcRestService();
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();

        try {
            result.setResult(auditTaskBasicService.selectOuList(areacode));
        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }

        return result;
    }

    public AuditCommonResult<List<Record>> getHyflList() {
        JnHqzcRestService auditTaskBasicService = new JnHqzcRestService();
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();

        try {
            result.setResult(auditTaskBasicService.getHyflList());
        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }

        return result;
    }
    
    public AuditCommonResult<List<Record>> getHygmList() {
        JnHqzcRestService auditTaskBasicService = new JnHqzcRestService();
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();

        try {
            result.setResult(auditTaskBasicService.getHygmList());
        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }

        return result;
    }
    
    
    public AuditCommonResult<List<Record>> getSmzqList() {
        JnHqzcRestService auditTaskBasicService = new JnHqzcRestService();
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();

        try {
            result.setResult(auditTaskBasicService.getSmzqList());
        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }

        return result;
    }
    
    public AuditCommonResult<List<Record>> getPolicyListByContion(String ssbm, String qybq, String jnhygm, String wwsmzq, String sfsxqy, int pageIndex, int pageSize,String ouguids) {
        JnHqzcRestService auditTaskBasicService = new JnHqzcRestService();
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();

        try {
            result.setResult(auditTaskBasicService.getPolicyListByContion(ssbm,qybq,jnhygm,wwsmzq,sfsxqy,pageIndex,pageSize,ouguids));
        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }

        return result;
    }
    
    public AuditCommonResult<Record> getPolicyListByContion(String ssbm, String qybq, String jnhygm, String wwsmzq, String sfsxqy, String ouguids) {
        JnHqzcRestService auditTaskBasicService = new JnHqzcRestService();
        AuditCommonResult<Record> result = new AuditCommonResult<Record>();
        try {
            result.setResult(auditTaskBasicService.getPolicyListByContion(ssbm,qybq,jnhygm,wwsmzq,sfsxqy,ouguids));
        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }

        return result;
    }
    

    public AuditCommonResult<List<Record>> getPolicyListByContionFirst(String str, int pageIndex, int pageSize) {
        JnHqzcRestService auditTaskBasicService = new JnHqzcRestService();
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();

        try {
            result.setResult(auditTaskBasicService.getPolicyListByContionFirst(str,pageIndex,pageSize));
        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }

        return result;
    }

    public AuditCommonResult<Record> getPolicyDetailByRowguid(String rowguid) {
        JnHqzcRestService auditTaskBasicService = new JnHqzcRestService();
        AuditCommonResult<Record> result = new AuditCommonResult<Record>();

        try {
            result.setResult(auditTaskBasicService.getPolicyDetailByRowguid(rowguid));
        } catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }

        return result;
    }

    
}
