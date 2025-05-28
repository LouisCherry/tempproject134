package com.epoint.queue.impl;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.epoint.basic.auditorga.audithall.domain.AuditOrgaHall;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.queue.api.IJnQueue;

@Component
@Service
public class JnQueueImpl implements IJnQueue {

	@Override
	public AuditCommonResult<List<AuditOrgaHall>> getHallList(String centerguid) {
		JnQueueService service = new JnQueueService();
		AuditCommonResult<List<AuditOrgaHall>> result = new AuditCommonResult<List<AuditOrgaHall>>();
		try {
			result.setResult(service.getHallList(centerguid));
		} catch (Exception e) {
			result.setSystemFail(e.getMessage());
		}
		return result;
	}

	@Override
	public int getWindowCount(String lobbytype) {
		JnQueueService service = new JnQueueService();
		return service.getWindowCount(lobbytype);
	}

	@Override
	public int getWaitCount(String hallguid) {
		JnQueueService service = new JnQueueService();
		return service.getWaitCount(hallguid);
	}
	
	@Override
	public int getTotalWaitCount(String centerguid) {
		JnQueueService service = new JnQueueService();
		return service.getTotalWaitCount(centerguid);
	}
	
	@Override
	public int getTotalQueueCount(String centerguid) {
		JnQueueService service = new JnQueueService();
		return service.getTotalQueueCount(centerguid);
	}

}
