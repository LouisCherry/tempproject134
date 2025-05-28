package com.epoint.queue.api;

import java.util.List;

import com.epoint.basic.auditorga.audithall.domain.AuditOrgaHall;
import com.epoint.common.service.AuditCommonResult;

public interface IJnQueue
{

	AuditCommonResult<List<AuditOrgaHall>> getHallList(String centerguid);

	int getWindowCount(String lobbytype);

	int getWaitCount(String hallguid);

	int getTotalWaitCount(String centerguid);

	int getTotalQueueCount(String centerguid);
    
}
