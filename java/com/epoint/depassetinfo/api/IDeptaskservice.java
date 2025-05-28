package com.epoint.depassetinfo.api;

import java.util.List;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

public interface IDeptaskservice {

	List<FrameOu> findAll();

	int findCountByPid(String ouguid);

	List<AuditTask> findAllByPid(String guid);

	AuditTask gettaskbyguid(String taskguid);

}
