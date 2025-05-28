package com.epoint.auditydp.jnydp.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.auditydp.jnydp.api.IJnAuditydp;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditqueue.auditznsbcentertask.domain.AuditZnsbCentertask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.dict.domain.AuditTaskDict;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
@Service
@Component
public class JnAuditydpImpl implements IJnAuditydp
{
    @Override
    public AuditCommonResult<List<FrameOu>> getConditionOU(String condition,int firstpage,int pagesize){
        AuditCommonResult<List<FrameOu>> result = new AuditCommonResult<List<FrameOu>>();
        JnAuditydpService service = new JnAuditydpService();
        result.setResult(service.getConditionOU(condition, firstpage, pagesize));
        return result;
    }
    @Override
    public int getConditionOUCount(String condition){
        JnAuditydpService service = new JnAuditydpService();
        return service.getConditionOUCount(condition);
     
    }
    @Override
    public int getConditionTaskCount(String condition){
        JnAuditydpService service = new JnAuditydpService();
        return service.getConditionTaskCount(condition);
    }
    @Override
    public AuditCommonResult<List<AuditZnsbCentertask>> getConditionTask(String condition,int firstpage,int pagesize){
        AuditCommonResult<List<AuditZnsbCentertask>> result = new AuditCommonResult<List<AuditZnsbCentertask>>();
        JnAuditydpService service = new JnAuditydpService();
        result.setResult(service.getConditionTask(condition, firstpage, pagesize));
        return result;
    }
    @Override
    public int getConditionDictCount(String condition){
        JnAuditydpService service = new JnAuditydpService();
        return service.getConditionDictCount(condition);
    }
    @Override
    public AuditCommonResult<List<AuditTaskDict>> getConditionDict(String condition,int firstpage,int pagesize){
        AuditCommonResult<List<AuditTaskDict>> result = new AuditCommonResult<List<AuditTaskDict>>();
        JnAuditydpService service = new JnAuditydpService();
        result.setResult(service.getConditionDict(condition, firstpage, pagesize));
        return result;
    }
    @Override
    public AuditCommonResult<List<AuditOrgaWindow>> getWindowListByTaskId(String taskid,int firstpage,int pagesize) {
        AuditCommonResult<List<AuditOrgaWindow>> result = new AuditCommonResult<List<AuditOrgaWindow>>();
        JnAuditydpService service = new JnAuditydpService();
        result.setResult(service.getWindowListByTaskId(taskid, firstpage, pagesize));
        return result;
    }
    @Override
    public int getWindowListCountByTaskId(String taskid){
        JnAuditydpService service = new JnAuditydpService();
        return service.getWindowListCountByTaskId(taskid);
    }
}
