package com.epoint.znsb.jnzwfw.module;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditqueue.auditznsbselfmachinemodule.domain.AuditZnsbSelfmachinemodule;
import com.epoint.basic.auditqueue.service.AuditQueueBasicService;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.znsb.jnzwfw.module.entity.AuditZnsbModule;

/**
 * 
 * 
 * @author Administrator
 * @version [版本号, 2016年11月23日]
 * 
 * 
 */
@Component
@Service
public class AuditZnsbModuleServiceImpl implements IAuditZnsbModuleService
{
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public AuditCommonResult<List<AuditZnsbModule>> getAuditZnsbModuleList(Map<String, String> conditionMap) {
        AuditQueueBasicService<AuditZnsbModule> service = new AuditQueueBasicService<AuditZnsbModule>();
        AuditCommonResult<List<AuditZnsbModule>> result = new AuditCommonResult<List<AuditZnsbModule>>();
        try {
            List<AuditZnsbModule> moduleList = service.selectRecordList(AuditZnsbModule.class, conditionMap);
            result.setResult(moduleList);
        }
        catch (Exception e) {
            log.error("异常信息:", e);
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<PageData<AuditZnsbModule>> getAuditZnsbModuleListPageData(Map<String, String> conditionMap,
            int first, int pageSize, String sortField, String sortOrder) {
        AuditQueueBasicService<AuditZnsbModule> worklistservice = new AuditQueueBasicService<AuditZnsbModule>();
        AuditCommonResult<PageData<AuditZnsbModule>> result = new AuditCommonResult<PageData<AuditZnsbModule>>();
        try {
            PageData<AuditZnsbModule> worklist = worklistservice.getRecordPageData(AuditZnsbModule.class, conditionMap,
                    first, pageSize, sortField, sortOrder);
            result.setResult(worklist);
        }
        catch (Exception e) {
            log.error("异常信息:", e);
            result.setSystemFail(e.toString());
        }
        return result;
    }

    /**
     * 更新数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public void update(AuditZnsbModule record) {
        AuditQueueBasicService<AuditZnsbModule> service = new AuditQueueBasicService<AuditZnsbModule>();
        service.updateRecord(AuditZnsbModule.class, record);
        ;
    }

    /**
     * 插入数据
     * 
     * @param record
     *            BaseEntity或Record对象 <必须继承Record>
     * @return int
     */
    public void insert(AuditZnsbModule record) {
        AuditQueueBasicService<AuditZnsbModule> service = new AuditQueueBasicService<AuditZnsbModule>();
        service.addRecord(AuditZnsbModule.class, record);
    }

    @Override
    public int findMacListCountByConfigType(String macaddress, String centerguid, String moduleconfigtype) {
        JNAuditZnsbModuleService service = new JNAuditZnsbModuleService();
        return service.findMacListCountByConfigType(macaddress, centerguid, moduleconfigtype);
    }

    @Override
    public int findMacListCount(String macaddress, String centerguid) {
        JNAuditZnsbModuleService service = new JNAuditZnsbModuleService();
        return service.findMacListCount(macaddress, centerguid);
    }

    @Override
    public int findCenterListCount(String centerguid) {
        JNAuditZnsbModuleService service = new JNAuditZnsbModuleService();
        return service.findCenterListCount(centerguid);
    }

    @Override
    public List<AuditZnsbSelfmachinemodule> findCenterListCountAndLabel(String centerguid, String lableguid) {
        JNAuditZnsbModuleService service = new JNAuditZnsbModuleService();
        return service.findCenterListCountAndLabel(centerguid, lableguid);
    }

    @Override
    public List<AuditZnsbSelfmachinemodule> findMacListCountAndLabel(String macaddress, String centerguid,
            String lableguid) {
        JNAuditZnsbModuleService service = new JNAuditZnsbModuleService();
        return service.findMacListCountAndLabel(macaddress, centerguid, lableguid);
    }

    @Override
    public List<AuditZnsbSelfmachinemodule> findMacListByConfigTypeAndLabel(String macaddress, String centerguid,
            String moduleconfigtype, String lableguid) {
        JNAuditZnsbModuleService service = new JNAuditZnsbModuleService();
        return service.findMacListByConfigTypeAndLabel(macaddress, centerguid, moduleconfigtype, lableguid);
    }
}
