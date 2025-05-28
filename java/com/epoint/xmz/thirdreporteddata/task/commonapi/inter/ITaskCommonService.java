package com.epoint.xmz.thirdreporteddata.task.commonapi.inter;

import com.epoint.basic.auditsp.auditspbasetask.domain.AuditSpBasetask;
import com.epoint.basic.basedata.participantsinfo.api.entity.ParticipantsInfo;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.core.BaseEntity;

import java.util.List;

public interface ITaskCommonService {


    public AuditCommonResult<String> getCropInfo(String taskguid, String subappguid, String pahseguid);

    public AuditCommonResult<String> getCropName(String baseTaskguid);

    public AuditCommonResult<List<AuditSpBasetask>> getAuditSpTask(String businessguid, String task_id);

    public AuditCommonResult<Integer> getYwsjCommonByCondition(Class<? extends BaseEntity> class1, String itemcode,
                                                               String flowsn);

    public AuditCommonResult<List<ParticipantsInfo>> getJsdwInfor(String gcdm);

}
