package com.epoint.auditydp.jnydp.api;

import java.util.List;

import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditqueue.auditznsbcentertask.domain.AuditZnsbCentertask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.dict.domain.AuditTaskDict;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

public interface IJnAuditydp
{

    AuditCommonResult<List<FrameOu>> getConditionOU(String condition, int firstpage, int pagesize);

    int getConditionOUCount(String condition);

    int getConditionTaskCount(String condition);

    AuditCommonResult<List<AuditZnsbCentertask>> getConditionTask(String condition, int firstpage, int pagesize);

    int getConditionDictCount(String condition);

    AuditCommonResult<List<AuditTaskDict>> getConditionDict(String condition, int firstpage, int pagesize);

    AuditCommonResult<List<AuditOrgaWindow>> getWindowListByTaskId(String taskid, int firstpage, int pagesize);

    int getWindowListCountByTaskId(String taskid);

}
