package com.epoint.basic.auditqueue.auditqueue.impl;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.epoint.basic.auditqueue.auditqueue.inter.ISSAuditQueue;
import com.epoint.basic.auditqueue.auditqueue.service.SSAuditQueueService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditqueue.auditqueue.domain.AuditQueue;
import com.epoint.basic.auditqueue.auditqueue.inter.IAuditQueue;
import com.epoint.basic.auditqueue.auditqueue.service.AuditQueueService;
import com.epoint.basic.auditqueue.auditqueuehostory.domain.AuditQueueHistory;
import com.epoint.basic.auditqueue.auditqueueorgawindow.domain.AuditQueueOrgaWindow;
import com.epoint.basic.auditqueue.auditqueueorgawindow.inter.IAuditQueueOrgaWindow;
import com.epoint.basic.auditqueue.service.AuditQueueBasicService;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.database.peisistence.procedure.ProcedureService;

/**
 *
 * 队列窗口api
 *
 * @author Administrator
 * @version [版本号, 2016年11月23日]
 *
 *
 */
@Component
@Service
public class SSAuditQueueImpl implements ISSAuditQueue
{
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());


    @Override
    public AuditCommonResult<String> getNextQueueNOshardingNoLock(String WindowGuid, String CenterGuid,
                                                                  String UserGuid,boolean isautoassign) {

        SSAuditQueueService queueService = new SSAuditQueueService();
        AuditCommonResult<String> result = new AuditCommonResult<>();
        try {
            result.setResult(queueService.getNextQueueNOshardingNoLock(WindowGuid, CenterGuid, UserGuid, isautoassign));
        }
        catch (Exception e) {
            log.error("异常信息:", e);
            result.setSystemFail(e.toString());
        }
        return result;
    }


}
