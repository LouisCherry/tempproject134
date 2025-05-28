package com.epoint.basic.auditqueue.auditznsbremotehelp.impl;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditqueue.auditznsbremotehelp.domain.AuditZnsbRemoteHelp;
import com.epoint.basic.auditqueue.auditznsbremotehelp.inter.IAuditZnsbRemoteHelpService;
import com.epoint.basic.auditqueue.auditznsbremotehelp.service.AuditZnsbRemoteHelpService;
import com.epoint.basic.auditqueue.service.AuditQueueBasicService;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;
/**
 * 
 * 
 * @author JackLove
 * @version [版本号, 2018-03-30 11:11:00]
 */
@Component
@Service
public class AuditZnsbRemoteHelpServiceImpl implements IAuditZnsbRemoteHelpService
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public AuditCommonResult<PageData<AuditZnsbRemoteHelp>> getFileByPage(Map<String, String> conditionMap, int first,
            int pageSize, String sortField, String sortOrder) {
        AuditQueueBasicService<AuditZnsbRemoteHelp> auditqueueService = new AuditQueueBasicService<AuditZnsbRemoteHelp>();
        AuditCommonResult<PageData<AuditZnsbRemoteHelp>> result = new AuditCommonResult<PageData<AuditZnsbRemoteHelp>>();
        try {

            PageData<AuditZnsbRemoteHelp> equipmentList = auditqueueService
                    .getRecordPageData(AuditZnsbRemoteHelp.class, conditionMap, first, pageSize, sortField, sortOrder);

            result.setResult(equipmentList);
        } catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }
    
    @Override
    public AuditCommonResult<String> insert(AuditZnsbRemoteHelp AuditZnsbRemoteHelp) {
        AuditQueueBasicService<AuditZnsbRemoteHelp> auditqueueService = new AuditQueueBasicService<AuditZnsbRemoteHelp>();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {                   
            auditqueueService.addRecord(AuditZnsbRemoteHelp.class, AuditZnsbRemoteHelp);
        } catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }
    
    @Override
    public AuditCommonResult<String> deletebyRowGuid(String RowGuid) {  
         AuditQueueBasicService<AuditZnsbRemoteHelp> informationservice = new AuditQueueBasicService<AuditZnsbRemoteHelp>();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            informationservice.deleteRecords(AuditZnsbRemoteHelp.class, RowGuid, "rowguid");
        
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }
    
    @Override
    public AuditCommonResult<AuditZnsbRemoteHelp> getDetail(String RowGuid) {  
         AuditQueueBasicService<AuditZnsbRemoteHelp> informationservice = new AuditQueueBasicService<AuditZnsbRemoteHelp>();
        AuditCommonResult<AuditZnsbRemoteHelp> result = new AuditCommonResult<AuditZnsbRemoteHelp>();
        try {
            result.setResult(informationservice.getDetail(AuditZnsbRemoteHelp.class, RowGuid, "rowguid"));
        
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }
    
    @Override
    public AuditCommonResult<AuditZnsbRemoteHelp> getDetailByAioRowguid(String RowGuid) {  
         AuditQueueBasicService<AuditZnsbRemoteHelp> informationservice = new AuditQueueBasicService<AuditZnsbRemoteHelp>();
        AuditCommonResult<AuditZnsbRemoteHelp> result = new AuditCommonResult<AuditZnsbRemoteHelp>();
        try {
            result.setResult(informationservice.getDetail(AuditZnsbRemoteHelp.class, RowGuid, "machineguid"));
        
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }
    
    
    @Override
    public AuditCommonResult<String> update(AuditZnsbRemoteHelp dataBean) {    
         AuditQueueBasicService<AuditZnsbRemoteHelp> informationservice = new AuditQueueBasicService<AuditZnsbRemoteHelp>();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            informationservice.updateRecord(AuditZnsbRemoteHelp.class, dataBean);
        
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public Integer getAuditZnsbRemoteHelpName(String account) {
        AuditZnsbRemoteHelpService auditZnsbRemoteHelpService = new AuditZnsbRemoteHelpService();
        Integer result = 0;
        try {
            result = auditZnsbRemoteHelpService.getAuditZnsbRemoteHelpByAccount(account);
        }
        catch (Exception e) {
            e.getMessage();
        }
        return result;
    }
    
    @Override
    public Integer getAuditZnsbRemoteHelpMachineguid(String machineguid) {
        AuditZnsbRemoteHelpService auditZnsbRemoteHelpService = new AuditZnsbRemoteHelpService();
        Integer result = 0;
        try {
            result = auditZnsbRemoteHelpService.getAuditZnsbRemoteHelpByMachineGuid(machineguid);
        }
        catch (Exception e) {
            e.getMessage();
        }
        return result;
    }
    
}
