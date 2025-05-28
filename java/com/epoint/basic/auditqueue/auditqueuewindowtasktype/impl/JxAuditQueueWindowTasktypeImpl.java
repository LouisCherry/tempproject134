package com.epoint.basic.auditqueue.auditqueuewindowtasktype.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditqueue.auditqueuewindowtasktype.domain.AuditQueueWindowTasktype;
import com.epoint.basic.auditqueue.auditqueuewindowtasktype.inter.JxIAuditQueueWindowTasktype;
import com.epoint.basic.auditqueue.auditqueuewindowtasktype.service.JxAuditQueueWindowTasktypeService;
import com.epoint.basic.auditqueue.service.AuditQueueBasicService;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;

/**
 * 窗口与事项类别的关系实体
 * 
 * @author Administrator
 * @version [版本号, 2016-11-14 19:00:22]
 */
@Component
@Service
public class JxAuditQueueWindowTasktypeImpl implements JxIAuditQueueWindowTasktype
{

    @Override
    public AuditCommonResult<PageData<AuditQueueWindowTasktype>> getWindowTasktypeByPage(
            Map<String, String> conditionMap, int first, int pageSize) {

    	AuditQueueBasicService<AuditQueueWindowTasktype> auditQueueService = new AuditQueueBasicService<AuditQueueWindowTasktype>();
		AuditCommonResult<PageData<AuditQueueWindowTasktype>> result = new AuditCommonResult<PageData<AuditQueueWindowTasktype>>();
		try {
			PageData<AuditQueueWindowTasktype> definitionPageData = auditQueueService
					.getRecordPageData(AuditQueueWindowTasktype.class, conditionMap, first, pageSize, "", "");
			result.setResult(definitionPageData);
		} catch (Exception e) {
			result.setSystemFail(e.toString());
		}
		return result;
    }

    /**
     * 根据指定条件获取事项分类所有窗口
     * 
     * @param conditionMap
     * @return
     */
    @Override
    public AuditCommonResult<List<AuditQueueWindowTasktype>> getAllWindowTasktype(Map<String, String> conditionMap) {
        AuditQueueBasicService<AuditQueueWindowTasktype> auditQueueService = new AuditQueueBasicService<AuditQueueWindowTasktype>();
        AuditCommonResult<List<AuditQueueWindowTasktype>> result = new AuditCommonResult<List<AuditQueueWindowTasktype>>();
        try {
            List<AuditQueueWindowTasktype> returnList = auditQueueService
                    .selectRecordList(AuditQueueWindowTasktype.class, conditionMap);
            result.setResult(returnList);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> insertWindowtasktype(AuditQueueWindowTasktype windowtasktype) {
        AuditQueueBasicService<AuditQueueWindowTasktype> auditQueueService = new AuditQueueBasicService<AuditQueueWindowTasktype>();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            auditQueueService.addRecord(AuditQueueWindowTasktype.class, windowtasktype);
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> deletebyTaskTypeGuid(String tasktypeguid) {

        AuditQueueBasicService<AuditQueueWindowTasktype> auditQueueService = new AuditQueueBasicService<AuditQueueWindowTasktype>();

        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {

            auditQueueService.deleteRecords(AuditQueueWindowTasktype.class, tasktypeguid, "tasktypeguid");
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> deletebyWindowguid(String windowGuid) {

        AuditQueueBasicService<AuditQueueWindowTasktype> auditQueueService = new AuditQueueBasicService<AuditQueueWindowTasktype>();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {

            auditQueueService.deleteRecords(AuditQueueWindowTasktype.class, windowGuid, "windowguid");
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<String>> getWindowListByTaskTypeGuid(String TaskTypeGuid) {

        JxAuditQueueWindowTasktypeService tasktypeservice = new JxAuditQueueWindowTasktypeService();
        AuditCommonResult<List<String>> result = new AuditCommonResult<List<String>>();
        try {
       
            result.setResult(tasktypeservice.getWindowListByTaskTypeGuid(TaskTypeGuid));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Boolean> IsSameQueuevalue(String windowguid) {

        JxAuditQueueWindowTasktypeService tasktypeservice = new JxAuditQueueWindowTasktypeService();
        AuditCommonResult<Boolean> result = new AuditCommonResult<Boolean>();
        try {
            result.setResult(tasktypeservice.IsSameQueuevalue(windowguid));
        }
        catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }
}
