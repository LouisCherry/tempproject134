package com.epoint.basic.auditqueue.auditznsbremotehelpuser.impl;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditqueue.auditznsbremotehelpuser.domain.AuditZnsbRemoteHelpUser;
import com.epoint.basic.auditqueue.auditznsbremotehelpuser.inter.IAuditZnsbRemoteHelpUserService;
import com.epoint.basic.auditqueue.auditznsbremotehelpuser.service.AuditZnsbRemoteHelpUserService;
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
public class AuditZnsbRemoteHelpUserServiceImpl implements IAuditZnsbRemoteHelpUserService
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public AuditCommonResult<PageData<AuditZnsbRemoteHelpUser>> getFileByPage(Map<String, String> conditionMap, int first,
            int pageSize, String sortField, String sortOrder) {
        AuditQueueBasicService<AuditZnsbRemoteHelpUser> auditqueueService = new AuditQueueBasicService<AuditZnsbRemoteHelpUser>();
        AuditCommonResult<PageData<AuditZnsbRemoteHelpUser>> result = new AuditCommonResult<PageData<AuditZnsbRemoteHelpUser>>();
        try {

            PageData<AuditZnsbRemoteHelpUser> equipmentList = auditqueueService
                    .getRecordPageData(AuditZnsbRemoteHelpUser.class, conditionMap, first, pageSize, sortField, sortOrder);

            result.setResult(equipmentList);
        } catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }
    
    @Override
    public AuditCommonResult<String> insert(AuditZnsbRemoteHelpUser AuditZnsbAssessconfig) {
        AuditQueueBasicService<AuditZnsbRemoteHelpUser> auditqueueService = new AuditQueueBasicService<AuditZnsbRemoteHelpUser>();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {                   
            auditqueueService.addRecord(AuditZnsbRemoteHelpUser.class, AuditZnsbAssessconfig);
        } catch (Exception e) {
            result.setSystemFail(e.getMessage());
        }
        return result;
    }
    
    @Override
    public AuditCommonResult<String> deletebyRowGuid(String RowGuid) {  
         AuditQueueBasicService<AuditZnsbRemoteHelpUser> informationservice = new AuditQueueBasicService<AuditZnsbRemoteHelpUser>();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            informationservice.deleteRecords(AuditZnsbRemoteHelpUser.class, RowGuid, "rowguid");
        
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }
    
    @Override
    public AuditCommonResult<AuditZnsbRemoteHelpUser> getDetail(String RowGuid) {  
         AuditQueueBasicService<AuditZnsbRemoteHelpUser> informationservice = new AuditQueueBasicService<AuditZnsbRemoteHelpUser>();
        AuditCommonResult<AuditZnsbRemoteHelpUser> result = new AuditCommonResult<AuditZnsbRemoteHelpUser>();
        try {
            result.setResult(informationservice.getDetail(AuditZnsbRemoteHelpUser.class, RowGuid, "rowguid"));
        
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }
    
    @Override
    public AuditCommonResult<String> update(AuditZnsbRemoteHelpUser dataBean) {    
         AuditQueueBasicService<AuditZnsbRemoteHelpUser> informationservice = new AuditQueueBasicService<AuditZnsbRemoteHelpUser>();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            informationservice.updateRecord(AuditZnsbRemoteHelpUser.class, dataBean);
        
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }
    
    @Override
    public Integer getAuditZnsbRemoteHelpUserByAccount(String account) {
        AuditZnsbRemoteHelpUserService auditZnsbRemoteHelpUserService = new AuditZnsbRemoteHelpUserService();
        Integer result = 0;
        try {
            result = auditZnsbRemoteHelpUserService.getAuditZnsbRemoteHelpUserByAccount(account);
        }
        catch (Exception e) {
            e.getMessage();
        }
        return result;
    }
    
    @Override
    public Integer getAuditZnsbRemoteHelpUserByUserguid(String userguid) {
        AuditZnsbRemoteHelpUserService auditZnsbRemoteHelpUserService = new AuditZnsbRemoteHelpUserService();
        Integer result = 0;
        try {
            result = auditZnsbRemoteHelpUserService.getAuditZnsbRemoteHelpUserByUserguid(userguid);
        }
        catch (Exception e) {
            e.getMessage();
        }
        return result;
    }
    
    @Override
    public AuditCommonResult<AuditZnsbRemoteHelpUser> getDetailByUserguid(String userguid) {  
         AuditQueueBasicService<AuditZnsbRemoteHelpUser> informationservice = new AuditQueueBasicService<AuditZnsbRemoteHelpUser>();
        AuditCommonResult<AuditZnsbRemoteHelpUser> result = new AuditCommonResult<AuditZnsbRemoteHelpUser>();
        try {
            result.setResult(informationservice.getDetail(AuditZnsbRemoteHelpUser.class, userguid, "userguid"));
        
        } catch (Exception e) {
            result.setSystemFail(e.toString());
        }
        return result;
    }
    

}
