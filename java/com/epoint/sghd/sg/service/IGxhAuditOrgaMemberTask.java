package com.epoint.sghd.sg.service;

import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;
import com.epoint.sghd.auditjianguan.renlingrecord.api.entity.RenlingRecord;
import com.epoint.sghd.sg.entity.AuditOrgaMemberTask;

import java.util.List;

public interface IGxhAuditOrgaMemberTask {
    AuditCommonResult<List<String>> getTaskidsByWindow(String var1);

    void deleteWindowTaskByWindowGuid(String var1);

    AuditCommonResult<String> insertWindowTask(AuditOrgaMemberTask var1);

    AuditCommonResult<List<AuditOrgaMemberTask>> getTaskByWindow(String var1);


    List<Record> getRenlingListByAreacode(String s);

    List<Record> getMemberGuidByTaskId(String taskid);

    List<RenlingRecord> getWfkOption();
}
