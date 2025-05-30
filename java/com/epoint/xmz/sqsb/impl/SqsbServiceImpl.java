package com.epoint.xmz.sqsb.impl;
import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditsp.auditspbasetask.domain.AuditSpBasetask;
import com.epoint.basic.auditsp.auditspbasetaskr.domain.AuditSpBasetaskR;
import com.epoint.basic.auditsp.auditspbusiness.domain.AuditSpBusiness;
import com.epoint.basic.auditsp.auditspphase.domain.AuditSpPhase;
import com.epoint.basic.auditsp.auditsptask.domain.AuditSpTask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.grammar.Record;
import com.epoint.xmz.sqsb.api.ISqsbService;

/**
 * 一链办理对应的后台service实现类
 * 
 * @author LYA
 * @version [版本号, 2020-06-20 19:20:32]
 */
@Component
@Service
public class SqsbServiceImpl implements ISqsbService
{
    
    @Override
    public AuditSpBusiness getAuditSpBusinessByOriginid(String originid) {
        return new SqsbService().getAuditSpBusinessByOriginid(originid);
    }

    @Override
    public List<AuditSpBasetaskR> getAuditSpBasetaskrByBaseTaskGuid(String basetaskguid) {
        return new SqsbService().getAuditSpBasetaskrByBaseTaskGuid(basetaskguid);
    }

    @Override
    public AuditTask getAuditTaskByTaskId(String taskid) {
        return new SqsbService().getAuditTaskByTaskId(taskid);
    }

    @Override
    public List<AuditTask> getAuditTaskListByTaskName(String taskname) {
        // TODO Auto-generated method stub
        return new SqsbService().getAuditTaskListByTaskName(taskname);
    }
    
    @Override
    public List<AuditSpBasetaskR> getTaskNameBySqsb(String issqlb,String areacode) {
        return new SqsbService().getTaskNameBySqsb(issqlb,areacode);
    }
    
}
