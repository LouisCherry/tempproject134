package com.epoint.wsxznsb.auditqueue.inter;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.epoint.basic.auditqueue.auditqueuetasktype.domain.AuditQueueTasktype;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;

/**
 * 
 * 事项分类api
 * 
 * @author Administrator
 * @version [版本号, 2016年11月23日]
 * 
 * 
 */
@Service
public interface WsxIAuditQueueTasktype
{
    public AuditCommonResult<PageData<AuditQueueTasktype>> getAuditQueueTasktypeByPage(String fieldstr,
            Map<String, String> conditionMap, int first, int pagesize, String sortField, String sortOrder);
}
