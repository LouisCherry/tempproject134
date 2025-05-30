package com.epoint.zwdt.hosttask.api;

import java.util.List;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.hottask.domain.AuditTaskHottask;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.core.grammar.Record;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

/**
 * 高频事项接口
 * @作者 wangxiaolong
 * @version [版本号, 2019年6月25日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface IHostTask
{
    public List<Record> getListAuditTaskHottask(String taskname, String areacode);

    public FrameOu getFrameou(String ouguid);

    public AuditTask getTaskBasic(String rowguid);

    public AuditTask getBasicInfo(String rowguid);

    public AuditTaskExtension getCbdwname(String rowguid);

    public AuditTask getImplementationBasis(String rowguid);

    public List<AuditTaskMaterial> geTaskMaterial(String taskguid);

    public Record getQcode(String taskguid);

    public FrameAttachInfo getFrameAttachInfoByClientguid(String clientguid);

    public AuditTask getAuditTaskByTaskId(String str);

}
