package com.epoint.xmz.task.commonapi.inter;

import com.epoint.basic.auditsp.auditspbasetask.domain.AuditSpBasetask;
import com.epoint.basic.basedata.participantsinfo.api.entity.ParticipantsInfo;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.BaseEntity;

import java.util.List;

public interface ITaskCommonService {


    AuditCommonResult<String> getCropInfo(String taskguid, String subappguid, String pahseguid);

    AuditCommonResult<String> getCropName(String baseTaskguid);

    AuditCommonResult<List<AuditSpBasetask>> getAuditSpTask(String businessguid, String task_id);

    AuditCommonResult<Integer> getYwsjCommonByCondition(Class<? extends BaseEntity> class1, String itemcode,
                                                        String flowsn);

    AuditCommonResult<List<ParticipantsInfo>> getJsdwInfor(String gcdm);

}
