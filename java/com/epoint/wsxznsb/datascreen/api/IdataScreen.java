package com.epoint.wsxznsb.datascreen.api;

import java.util.List;

import org.springframework.stereotype.Service;

import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;

/**
 * 
 * [大屏展示api]
 * 
 * @author jyong
 * @version 2022年1月19日
 */
@Service
public interface IdataScreen
{
    // 获取历史取号量
    public AuditCommonResult<Integer> getHistoryQueueCount(String centerGuid, String type);

    // 获取当日取号量
    public AuditCommonResult<Record> getCurrentDayQueue(String centerGuid);

    // 获取当天满意度
    public AuditCommonResult<Record> getCurrentDayEvate(String centerGuid);

    // 获取历史窗口取号量列表
    public AuditCommonResult<List<Record>> getWindowHistoryQueueMonth(String centerGuid);

    // 获取今日窗口取号量列表
    public AuditCommonResult<List<Record>> getWindowTodayQueueMonth(String centerGuid);

    // 获取历史部门取号量列表
    public AuditCommonResult<List<Record>> getOuHistoryQueueMonth(String centerGuid);

    // 获取今日部门取号量列表
    public AuditCommonResult<List<Record>> getOuTodayQueueMonth(String centerGuid);

    // 获取办事人员男女数量(今日表)
    public AuditCommonResult<Record> getSexCountToday(String centerGuid);

    // 获取办事人员男女数量(历史表)
    public AuditCommonResult<Record> getSexCountHistory(String centerGuid);

    // 获取办事人员各年龄区间数量(今日表)
    public AuditCommonResult<Record> getAgeCountToday(String centerGuid);

    // 获取办事人员各年龄区间数量(历史表)
    public AuditCommonResult<Record> getAgeCountHistory(String centerGuid);

    // 获取乡镇办件数据
    public AuditCommonResult<List<Record>> getProjectCount();

    public AuditCommonResult<Record> getProjectTotal();

    // 获取微信端月度预约取号量
    public AuditCommonResult<Integer> getYuYueWechatCount(String centerGuid, String yuyueType);

}
