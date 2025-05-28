package com.epoint.zoucheng.znsb.auditznsbmodule.impl;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditqueue.auditznsbmodule.domain.FunctionModule;
import com.epoint.basic.auditqueue.service.AuditQueueBasicService;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.zoucheng.znsb.auditznsbmodule.inter.IZCAuditZnsbModule;
import com.epoint.zoucheng.znsb.auditznsbmodule.service.ZCAuditZnsbModuleService;

/**
 * 
 * 
 * @author Administrator
 * @version [版本号, 2016年11月23日]
 
 
 */
@Component
@Service
public class ZCAuditZnsbModuleImpl implements IZCAuditZnsbModule
{
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public AuditCommonResult<PageData<FunctionModule>> getAuditQueueModule(String fieldstr,
            Map<String, String> conditionMap, int first, int pagesize, String sortField, String sortOrder) {
        AuditQueueBasicService<FunctionModule> service = new AuditQueueBasicService<FunctionModule>();
        AuditCommonResult<PageData<FunctionModule>> result = new AuditCommonResult<PageData<FunctionModule>>();
        try {
            PageData<FunctionModule> moduleList = service.getRecordPageData(fieldstr, FunctionModule.class,
                    conditionMap, first, pagesize, sortField, sortOrder);
            result.setResult(moduleList);
        }
        catch (Exception e) {
            log.info(e);
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<String> insert(FunctionModule bean) {
        AuditQueueBasicService<FunctionModule> service = new AuditQueueBasicService<FunctionModule>();
        AuditCommonResult<String> result = new AuditCommonResult<String>();
        try {
            service.addRecord(FunctionModule.class, bean);
            result.setResult("success");
        }
        catch (Exception e) {
            log.info(e);
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getRecordCount() {
        ZCAuditZnsbModuleService auditqueueService = new ZCAuditZnsbModuleService();
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        try {
            result.setResult(auditqueueService.getRecordCount());
        }
        catch (Exception e) {
            log.info(e);
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getRecordCountByMacaddress(String macaddress) {
        ZCAuditZnsbModuleService auditqueueService = new ZCAuditZnsbModuleService();
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        try {
            result.setResult(auditqueueService.getRecordCountByMacaddress(macaddress));
        }
        catch (Exception e) {
            log.info(e);
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getRecordCountByCenterguidAndMacaddress(String centerguid, String macaddress) {
        ZCAuditZnsbModuleService auditqueueService = new ZCAuditZnsbModuleService();
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        try {
            if (StringUtil.isNotBlank(centerguid)) {
                result.setResult(auditqueueService.getRecordCountByCenterguidAndMacaddress(centerguid, macaddress));
            }
            else {
                result.setResult(auditqueueService.getRecordCountByMacaddress(macaddress));
            }

        }
        catch (Exception e) {
            log.info(e);
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getRecordCountByCenterguid(String centerguid) {
        ZCAuditZnsbModuleService auditqueueService = new ZCAuditZnsbModuleService();
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        try {
            result.setResult(auditqueueService.getRecordCountByCenterguid(centerguid));

        }
        catch (Exception e) {
            log.info(e);
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getModuleClickByCenterguid(String centerguid) {
        ZCAuditZnsbModuleService auditqueueService = new ZCAuditZnsbModuleService();
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        try {
            result.setResult(auditqueueService.getModuleClickByCenterguid(centerguid));

        }
        catch (Exception e) {
            log.info(e);
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getRecordCount(String macaddress, String from, String to) {

        ZCAuditZnsbModuleService auditqueueService = new ZCAuditZnsbModuleService();
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        try {
            result.setResult(auditqueueService.getRecordCount(macaddress, from, to));
        }
        catch (Exception e) {
            log.info(e);
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getRecordCountByCenterguidAndTime(String centerguid, String from, String to) {

        ZCAuditZnsbModuleService auditqueueService = new ZCAuditZnsbModuleService();
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        try {
            result.setResult(auditqueueService.getRecordCountByCenterguidAndTime(centerguid, from, to));
        }
        catch (Exception e) {
            log.info(e);
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getRecordByCenterguidAndTime(String centerguid, String from, String to) {

        ZCAuditZnsbModuleService auditqueueService = new ZCAuditZnsbModuleService();
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        try {
            result.setResult(auditqueueService.getRecordByCenterguidAndTime(centerguid, from, to));
        }
        catch (Exception e) {
            log.info(e);
            result.setSystemFail(e.toString());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getModuleClick(String centerguid) {
        ZCAuditZnsbModuleService auditqueueService = new ZCAuditZnsbModuleService();
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        try {
            result.setResult(auditqueueService.getModuleClick(centerguid));

        }
        catch (Exception e) {
            log.info(e);
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getModuleClick() {
        ZCAuditZnsbModuleService auditqueueService = new ZCAuditZnsbModuleService();
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        try {
            result.setResult(auditqueueService.getModuleClick());

        }
        catch (Exception e) {
            log.info(e);
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getCenterClick() {
        ZCAuditZnsbModuleService auditqueueService = new ZCAuditZnsbModuleService();
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        try {
            result.setResult(auditqueueService.getCenterClick());

        }
        catch (Exception e) {
            log.info(e);
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getModuleClickByMac(String macaddress) {
        ZCAuditZnsbModuleService auditqueueService = new ZCAuditZnsbModuleService();
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        try {
            result.setResult(auditqueueService.getModuleClickByMac(macaddress));

        }
        catch (Exception e) {
            log.info(e);
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getModuleClickWithoutMac(String macaddress, String centerguid) {
        ZCAuditZnsbModuleService auditqueueService = new ZCAuditZnsbModuleService();
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        try {
            result.setResult(auditqueueService.getModuleClickWithoutMac(macaddress, centerguid));

        }
        catch (Exception e) {
            log.info(e);
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getModuleClick(String startime, String endtime) {
        ZCAuditZnsbModuleService auditqueueService = new ZCAuditZnsbModuleService();
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        try {
            result.setResult(auditqueueService.getModuleClick(startime, endtime));

        }
        catch (Exception e) {
            log.info(e);
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getModuleClick(String centerguid, String startime, String endtime) {
        ZCAuditZnsbModuleService auditqueueService = new ZCAuditZnsbModuleService();
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        try {
            result.setResult(auditqueueService.getModuleClick(centerguid, startime, endtime));

        }
        catch (Exception e) {
            log.info(e);
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getModuleDayClick(String centerguid, String modulename, String startime,
            String endtime) {
        ZCAuditZnsbModuleService auditqueueService = new ZCAuditZnsbModuleService();
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        try {
            result.setResult(auditqueueService.getModuleDayClick(centerguid, modulename, startime, endtime));

        }
        catch (Exception e) {
            log.info(e);
            result.setSystemFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> updateByMacaddress(String oldmacaddress, String newmacaddress) {

        ZCAuditZnsbModuleService auditqueueService = new ZCAuditZnsbModuleService();
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        try {
            result.setResult(auditqueueService.updateByMacaddress(oldmacaddress, newmacaddress));
        }
        catch (Exception e) {
            log.info(e);
            result.setSystemFail(e.toString());
        }
        return result;
    }
}
