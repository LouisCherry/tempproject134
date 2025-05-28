package com.epoint.basic.auditqueue.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditqueue.auditqueuetasktype.domain.AuditQueueTasktype;
import com.epoint.basic.auditqueue.auditqueuetasktype.service.AuditQueueTasktypeService;
import com.epoint.basic.auditqueue.inter.JxIAuditQueueTasktype;
import com.epoint.basic.auditqueue.service.JxAuditQueueBasicService;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.service.AuditCommonService;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;

/**
 * 事项分类实体
 * 
 * @author Administrator
 * @version [版本号, 2016-11-09 09:27:31]
 */
@Component
@Service
public class JxAuditQueueTasktypeImpl implements JxIAuditQueueTasktype
{

    /**
     * 根据指定条件获取事项分类所有窗口
     * 
     * @param conditionMap
     * @return
     */

    @Override
    public AuditCommonResult<List<AuditQueueTasktype>> getAllTasktype(Map<String, String> conditionMap) {
        JxAuditQueueBasicService<AuditQueueTasktype> auditQueueService = new JxAuditQueueBasicService<AuditQueueTasktype>();
        AuditCommonResult<List<AuditQueueTasktype>> result = new AuditCommonResult<List<AuditQueueTasktype>>();
        try {
            List<AuditQueueTasktype> returnList = auditQueueService.selectRecordList(AuditQueueTasktype.class,
                    conditionMap);
            result.setResult(returnList);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<AuditQueueTasktype> getAuditQueueTasktypeByRowguid(String rowguid) {
        JxAuditQueueBasicService<AuditQueueTasktype> auditQueueService = new JxAuditQueueBasicService<AuditQueueTasktype>();
        AuditCommonResult<AuditQueueTasktype> result = new AuditCommonResult<AuditQueueTasktype>();
        try {
            AuditQueueTasktype auditQueueTasktype = auditQueueService.getDetail(AuditQueueTasktype.class, rowguid,
                    "rowguid");
            result.setResult((AuditQueueTasktype) auditQueueTasktype.clone());
        }
        catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getAuditQueueTasktypeByPage(String ouguid, String centerguid, int first,
            int pageSize) {
        AuditQueueTasktypeService tasktypeService = new AuditQueueTasktypeService();
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        try {
            List<Record> tasktypeList = tasktypeService.getTasktypeByOuguid(ouguid, centerguid, first, pageSize);
            result.setResult(tasktypeList);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditQueueTasktype>> getAuditQueueTasktypeByPage(Map<String, String> conditionMap,
            int first, int pageSize, String fields) {
        AuditCommonService auditQueueService = new AuditCommonService();
        AuditCommonResult<List<AuditQueueTasktype>> result = new AuditCommonResult<List<AuditQueueTasktype>>();
        try {
            List<AuditQueueTasktype> tasktypeList = auditQueueService.getSomeFieldsByPage(AuditQueueTasktype.class,
                    conditionMap, first, pageSize, "ordernum", "desc", fields);
            result.setResult(tasktypeList);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<PageData<AuditQueueTasktype>> getAuditQueueTasktypeByPage(String fieldstr,
            Map<String, String> conditionMap, int first, int pagesize, String sortField, String sortOrder) {
        JxAuditQueueBasicService<AuditQueueTasktype> auditQueueService = new JxAuditQueueBasicService<AuditQueueTasktype>();
        AuditCommonResult<PageData<AuditQueueTasktype>> result = new AuditCommonResult<PageData<AuditQueueTasktype>>();
        try {
            PageData<AuditQueueTasktype> tasktypeList = auditQueueService.getRecordPageData(fieldstr,
                    AuditQueueTasktype.class, conditionMap, first, pagesize, sortField, sortOrder);
            result.setResult(tasktypeList);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getCountbyOUGuid(String ouguid, String centerguid) {
        AuditQueueTasktypeService tasktypeService = new AuditQueueTasktypeService();
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        try {
            result.setResult(tasktypeService.getCountbyOUGuid(ouguid, centerguid));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public Integer getAuditQueueTasktypeCount(Map<String, String> conditionMap, String fields) {
        /*
         * AuditCommonService auditQueueService = new AuditCommonService();
         * return auditQueueService.getListByCondition(AuditQueueTasktype.class,
         * conditionMap, fields).size();
         */
        JxAuditQueueBasicService<AuditQueueTasktype> auditQueueService = new JxAuditQueueBasicService<AuditQueueTasktype>();
        return auditQueueService.selectRecordList(AuditQueueTasktype.class, conditionMap, fields).size();
    }

    @Override
    public AuditCommonResult<Integer> getAuditQueueTasktypeCount(String TaskTypeName, String OUGuid,
            String centerguid) {

        AuditQueueTasktypeService tasktypeservice = new AuditQueueTasktypeService();
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        try {

            result.setResult(tasktypeservice.getAuditQueueTasktypeCount(TaskTypeName, OUGuid, centerguid));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> delete(String RowGuid) {
        JxAuditQueueBasicService<AuditQueueTasktype> auditQueueService = new JxAuditQueueBasicService<AuditQueueTasktype>();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            auditQueueService.deleteRecords(AuditQueueTasktype.class, RowGuid, "rowguid");

        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> insertTasktype(AuditQueueTasktype auditQueueTasktype) {
        JxAuditQueueBasicService<AuditQueueTasktype> auditQueueService = new JxAuditQueueBasicService<AuditQueueTasktype>();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            auditQueueService.addRecord(AuditQueueTasktype.class, auditQueueTasktype);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> updateAuditQueueTasktype(AuditQueueTasktype auditQueueTasktype) {
        JxAuditQueueBasicService<AuditQueueTasktype> auditQueueService = new JxAuditQueueBasicService<AuditQueueTasktype>();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            auditQueueService.updateRecord(AuditQueueTasktype.class, auditQueueTasktype);

        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<AuditQueueTasktype> getTasktypeByguid(String rowguid) {
        JxAuditQueueBasicService<AuditQueueTasktype> auditQueueService = new JxAuditQueueBasicService<AuditQueueTasktype>();
        AuditCommonResult<AuditQueueTasktype> result = new AuditCommonResult<AuditQueueTasktype>();
        try {
            result.setResult(auditQueueService.getDetail(AuditQueueTasktype.class, rowguid, "rowguid"));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditQueueTasktype>> selectTaskTypeByGuid(String RowGuid) {
        AuditQueueTasktypeService tasktypeservice = new AuditQueueTasktypeService();
        AuditCommonResult<List<AuditQueueTasktype>> result = new AuditCommonResult<List<AuditQueueTasktype>>();
        try {

            result.setResult(tasktypeservice.selectTaskTypeByGuid(RowGuid));
        }
        catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> deletebyqueuevalue(String queuevalue) {
        AuditQueueTasktypeService tasktypeservice = new AuditQueueTasktypeService();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            tasktypeservice.deletebyqueuevalue(queuevalue);

        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> updateTaskTypeQueueValue(String queuevalue, String rowguid) {
        AuditQueueTasktypeService tasktypeservice = new AuditQueueTasktypeService();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            tasktypeservice.updateTaskTypeQueueValue(queuevalue, rowguid);

        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditQueueTasktype>> selectAuditQueueTasktypeByCondition(String condition) {
        AuditQueueTasktypeService tasktypeservice = new AuditQueueTasktypeService();
        AuditCommonResult<List<AuditQueueTasktype>> result = new AuditCommonResult<List<AuditQueueTasktype>>();
        try {
            List<AuditQueueTasktype> auditTasktypes = tasktypeservice.selectAuditTasktypeByCondition(condition);
            result.setResult(auditTasktypes);
        }
        catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditQueueTasktype>> selectAuditQueueTasktypeOuByOuGuid(String ouGuid) {
        AuditQueueTasktypeService tasktypeservice = new AuditQueueTasktypeService();
        AuditCommonResult<List<AuditQueueTasktype>> result = new AuditCommonResult<List<AuditQueueTasktype>>();
        try {
            List<AuditQueueTasktype> auditTasktypes = tasktypeservice.selectAuditTasktypeOuByOuGuid(ouGuid);
            result.setResult(auditTasktypes);
        }
        catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<AuditQueueTasktype>> getTasktypebyLargeTasktypeGuid(String largetasktypeguid) {
        AuditQueueTasktypeService tasktypeservice = new AuditQueueTasktypeService();
        AuditCommonResult<List<AuditQueueTasktype>> result = new AuditCommonResult<List<AuditQueueTasktype>>();
        try {
            List<AuditQueueTasktype> auditTasktypes = tasktypeservice.getTasktypebyLargeTasktypeGuid(largetasktypeguid);
            result.setResult(auditTasktypes);
        }
        catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> deleteLargeTasktypeGuidbyLargeTasktypeGuid(String largetasktypeguid) {
        AuditQueueTasktypeService tasktypeservice = new AuditQueueTasktypeService();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            tasktypeservice.deleteLargeTasktypeGuidbyLargeTasktypeGuid(largetasktypeguid);
        }
        catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> updateTasktypeByRowguid(String rowguid, String largetasktypeguid) {
        AuditQueueTasktypeService tasktypeservice = new AuditQueueTasktypeService();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            tasktypeservice.updateTasktypeByRowguid(rowguid, largetasktypeguid);
        }
        catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> getLargeTasktypeGuidbyRowguidandCenterGuid(String rowguid, String centerguid) {
        AuditQueueTasktypeService tasktypeservice = new AuditQueueTasktypeService();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            String largetasktypeguid = tasktypeservice.getLargeTasktypeGuidbyRowguidandCenterGuid(rowguid, centerguid);
            result.setResult(largetasktypeguid);
        }
        catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> emptyLargeTasktypeGuidbyGuid(String rowguid) {
        AuditQueueTasktypeService tasktypeservice = new AuditQueueTasktypeService();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            tasktypeservice.emptyLargeTasktypeGuidbyGuid(rowguid);
        }
        catch (Exception e) {
            result.setSystemFail(ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    @Override
    public int gettasktypesfzCount(String tasktypeguid, String sfz) {
        JxAuditQueueBasicService jxService = new JxAuditQueueBasicService<>();
        return jxService.gettasktypesfzCount(tasktypeguid, sfz);
    }

}
