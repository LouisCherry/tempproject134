package com.epoint.lsznsb.bigscreen.impl;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;
import com.epoint.lsznsb.bigscreen.api.LsxIznsbBigScreen;

@Component
@Service
public class LsxZnsbBigScreenImpl implements LsxIznsbBigScreen
{
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public AuditCommonResult<Record> getSexCountToday(String centerGuid) {
        AuditCommonResult<Record> result = new AuditCommonResult<Record>();
        LsxZnsbBigScreenService dataScreenService = new LsxZnsbBigScreenService();
        try {
            result.setResult(dataScreenService.getSexCountToday(centerGuid));
        }
        catch (Exception e) {
            log.info(e);
            result.setBusinessFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Record> getSexCountHistory(String centerGuid) {
        AuditCommonResult<Record> result = new AuditCommonResult<Record>();
        LsxZnsbBigScreenService dataScreenService = new LsxZnsbBigScreenService();
        try {
            result.setResult(dataScreenService.getSexCountHistory(centerGuid));
        }
        catch (Exception e) {
            log.info(e);
            result.setBusinessFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Record> getAgeCountToday(String centerGuid) {
        AuditCommonResult<Record> result = new AuditCommonResult<Record>();
        LsxZnsbBigScreenService dataScreenService = new LsxZnsbBigScreenService();
        try {
            result.setResult(dataScreenService.getAgeCountToday(centerGuid));
        }
        catch (Exception e) {
            log.info(e);
            result.setBusinessFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Record> getAgeCountHistory(String centerGuid) {
        AuditCommonResult<Record> result = new AuditCommonResult<Record>();
        LsxZnsbBigScreenService dataScreenService = new LsxZnsbBigScreenService();
        try {
            result.setResult(dataScreenService.getAgeCountHistory(centerGuid));
        }
        catch (Exception e) {
            log.info(e);
            result.setBusinessFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getTaskListHistory(String centerguid, Date from, Date to) {
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        LsxZnsbBigScreenService dataScreenService = new LsxZnsbBigScreenService();
        try {
            result.setResult(dataScreenService.getTaskListHistory(centerguid, from, to));
        }
        catch (Exception e) {
            log.info(e);
            result.setBusinessFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getTaskListToday(String centerguid, Date from, Date to) {
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        LsxZnsbBigScreenService dataScreenService = new LsxZnsbBigScreenService();
        try {
            result.setResult(dataScreenService.getTaskListToday(centerguid, from, to));
        }
        catch (Exception e) {
            log.info(e);
            result.setBusinessFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getEquipmentList(String centerguid) {
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        LsxZnsbBigScreenService dataScreenService = new LsxZnsbBigScreenService();
        try {
            result.setResult(dataScreenService.getEquipmentList(centerguid));
        }
        catch (Exception e) {
            log.info(e);
            result.setBusinessFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getTimesQueuenums(String centerguid) {
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        LsxZnsbBigScreenService dataScreenService = new LsxZnsbBigScreenService();
        try {
            result.setResult(dataScreenService.getTimesQueuenums(centerguid));
        }
        catch (Exception e) {
            log.info(e);
            result.setBusinessFail(e.getMessage());
        }
        return result;
    }
}
