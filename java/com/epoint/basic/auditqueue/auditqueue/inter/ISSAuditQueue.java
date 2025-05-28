package com.epoint.basic.auditqueue.auditqueue.inter;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditqueue.auditqueue.domain.AuditQueue;
import com.epoint.basic.auditqueue.auditqueuehostory.domain.AuditQueueHistory;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;
import com.epoint.database.peisistence.crud.impl.model.PageData;

/**
 * 
 * 队列api
 * 
 * @author Administrator
 * @version [版本号, 2016年11月23日]
 * 
 * 
 */
public interface ISSAuditQueue {
	
    /**
     * 
     * 取下一位(分表)(包含转号判断)(公平叫号区分手动叫号以及自动叫号)
     *  @param WindowGuid
     *  @param CenterGuid
     *  @param UserGuid
     *  @param isautoassign
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public AuditCommonResult<String> getNextQueueNOshardingNoLock(String WindowGuid, String CenterGuid,
            String UserGuid,boolean isautoassign);

  
}
