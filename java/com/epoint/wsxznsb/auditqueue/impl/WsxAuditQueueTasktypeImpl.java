package com.epoint.wsxznsb.auditqueue.impl;

import java.lang.invoke.MethodHandles;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditqueue.auditqueuetasktype.domain.AuditQueueTasktype;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.wsxznsb.auditqueue.inter.WsxIAuditQueueTasktype;
import com.epoint.wsxznsb.auditqueue.service.WsxAuditQueueBasicService;

/**
 * 事项分类实体
 * 
 * @author Administrator
 * @version [版本号, 2016-11-09 09:27:31]
 */
@Component
@Service
public class WsxAuditQueueTasktypeImpl implements WsxIAuditQueueTasktype
{
    /**
     * 日志
     */
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public AuditCommonResult<PageData<AuditQueueTasktype>> getAuditQueueTasktypeByPage(String fieldstr,
            Map<String, String> conditionMap, int first, int pagesize, String sortField, String sortOrder) {
        WsxAuditQueueBasicService<AuditQueueTasktype> auditQueueService = new WsxAuditQueueBasicService<AuditQueueTasktype>();
        AuditCommonResult<PageData<AuditQueueTasktype>> result = new AuditCommonResult<PageData<AuditQueueTasktype>>();
        try {
            PageData<AuditQueueTasktype> tasktypeList = auditQueueService.getRecordPageData(fieldstr,
                    AuditQueueTasktype.class, conditionMap, first, pagesize, sortField, sortOrder);
            result.setResult(tasktypeList);
        }
        catch (Exception e) {
            log.info(e);
            result.setSystemFail(e.getMessage());
        }
        return result;
    }
}
