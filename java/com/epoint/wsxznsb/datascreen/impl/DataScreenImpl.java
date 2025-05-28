package com.epoint.wsxznsb.datascreen.impl;

import java.lang.invoke.MethodHandles;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;
import com.epoint.wsxznsb.datascreen.api.IdataScreen;
import com.epoint.wsxznsb.datascreen.service.DataScreenService;

@Component
@Service
public class DataScreenImpl implements IdataScreen
{
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public AuditCommonResult<Integer> getHistoryQueueCount(String centerGuid, String type) {
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        DataScreenService dataScreenService = new DataScreenService();
        try {
            result.setResult(dataScreenService.getHistoryQueueCount(centerGuid, type));
        }
        catch (Exception e) {
            log.info(e);
            result.setBusinessFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Record> getCurrentDayQueue(String centerGuid) {
        AuditCommonResult<Record> result = new AuditCommonResult<Record>();
        DataScreenService dataScreenService = new DataScreenService();
        try {
            result.setResult(dataScreenService.getCurrentDayQueue(centerGuid));
        }
        catch (Exception e) {
            log.info(e);
            result.setBusinessFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Record> getCurrentDayEvate(String centerGuid) {
        AuditCommonResult<Record> result = new AuditCommonResult<Record>();
        DataScreenService dataScreenService = new DataScreenService();
        try {
            result.setResult(dataScreenService.getCurrentDayEvate(centerGuid));
        }
        catch (Exception e) {
            log.info(e);
            result.setBusinessFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getWindowHistoryQueueMonth(String centerGuid) {
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        DataScreenService dataScreenService = new DataScreenService();
        try {
            result.setResult(dataScreenService.getWindowHistoryQueueMonth(centerGuid));
        }
        catch (Exception e) {
            log.info(e);
            result.setBusinessFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getWindowTodayQueueMonth(String centerGuid) {
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        DataScreenService dataScreenService = new DataScreenService();
        try {
            result.setResult(dataScreenService.getWindowTodayQueueMonth(centerGuid));
        }
        catch (Exception e) {
            log.info(e);
            result.setBusinessFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getOuHistoryQueueMonth(String centerGuid) {
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        DataScreenService dataScreenService = new DataScreenService();
        try {
            result.setResult(dataScreenService.getOuHistoryQueueMonth(centerGuid));
        }
        catch (Exception e) {
            log.info(e);
            result.setBusinessFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<List<Record>> getOuTodayQueueMonth(String centerGuid) {
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        DataScreenService dataScreenService = new DataScreenService();
        try {
            result.setResult(dataScreenService.getOuTodayQueueMonth(centerGuid));
        }
        catch (Exception e) {
            log.info(e);
            result.setBusinessFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Record> getSexCountToday(String centerGuid) {
        AuditCommonResult<Record> result = new AuditCommonResult<Record>();
        DataScreenService dataScreenService = new DataScreenService();
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
        DataScreenService dataScreenService = new DataScreenService();
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
        DataScreenService dataScreenService = new DataScreenService();
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
        DataScreenService dataScreenService = new DataScreenService();
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
    public AuditCommonResult<List<Record>> getProjectCount() {
        AuditCommonResult<List<Record>> result = new AuditCommonResult<List<Record>>();
        DataScreenService dataScreenService = new DataScreenService();
        try {
            result.setResult(dataScreenService.getProjectCount());
        }
        catch (Exception e) {
            log.info(e);
            result.setBusinessFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Record> getProjectTotal() {
        AuditCommonResult<Record> result = new AuditCommonResult<Record>();
        DataScreenService dataScreenService = new DataScreenService();
        try {
            result.setResult(dataScreenService.getProjectTotal());
        }
        catch (Exception e) {
            log.info(e);
            result.setBusinessFail(e.getMessage());
        }
        return result;
    }

    @Override
    public AuditCommonResult<Integer> getYuYueWechatCount(String centerGuid, String yuyueType) {
        AuditCommonResult<Integer> result = new AuditCommonResult<Integer>();
        DataScreenService dataScreenService = new DataScreenService();
        try {
            result.setResult(dataScreenService.getYuYueWechatCount(centerGuid, yuyueType));
        }
        catch (Exception e) {
            log.info(e);
            result.setBusinessFail(e.getMessage());
        }
        return result;
    }
}
