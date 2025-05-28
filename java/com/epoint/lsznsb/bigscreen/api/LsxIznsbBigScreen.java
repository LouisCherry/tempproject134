package com.epoint.lsznsb.bigscreen.api;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;

@Service
public interface LsxIznsbBigScreen
{
    // 获取办事人员男女数量(今日表)
    public AuditCommonResult<Record> getSexCountToday(String centerGuid);

    // 获取办事人员男女数量(历史表)
    public AuditCommonResult<Record> getSexCountHistory(String centerGuid);

    // 获取办事人员各年龄区间数量(今日表)
    public AuditCommonResult<Record> getAgeCountToday(String centerGuid);

    // 获取办事人员各年龄区间数量(历史表)
    public AuditCommonResult<Record> getAgeCountHistory(String centerGuid);

    // 获取当天取号事项类别数量列表(倒序)
    public AuditCommonResult<List<Record>> getTaskListToday(String centerguid, Date from, Date to);

    // 获取历史取号事项类别数量列表(倒序)
    public AuditCommonResult<List<Record>> getTaskListHistory(String centerguid, Date from, Date to);

    // 获取各设备类型数量
    public AuditCommonResult<List<Record>> getEquipmentList(String centerguid);

    // 获取当天各时间段取号数量
    public AuditCommonResult<List<Record>> getTimesQueuenums(String centerguid);
}
