package com.epoint.auditqueue.auditznsbqueue.api;

import java.util.List;
import java.util.Map;

import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.grammar.Record;
import com.epoint.sso.frame.domain.FrameUser;

public interface IQueueList
{

    AuditCommonResult<List<FrameUser>> getZnsbQueueHandleUser(Map<String, String> conditionMap);

    AuditCommonResult<List<Record>> getModuleClick(String centerguid, String startime, String endtime);

    AuditCommonResult<List<Record>> getModuleClick(String startime, String endtime);

}
