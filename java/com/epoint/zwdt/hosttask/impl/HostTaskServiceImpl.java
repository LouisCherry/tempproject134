package com.epoint.zwdt.hosttask.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.hottask.domain.AuditTaskHottask;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.core.grammar.Record;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.zwdt.hosttask.api.IHostTask;

@Component
@Service
public class HostTaskServiceImpl implements IHostTask
{

    @Override
    public List<Record> getListAuditTaskHottask(String taskname, String areacode) {
        return new HostTaskService().getListAuditTaskHottask(taskname, areacode);
    }

    @Override
    public FrameOu getFrameou(String ouguid) {
        return new HostTaskService().getFrameou(ouguid);
    }

    @Override
    public AuditTask getTaskBasic(String rowguid) {
        return new HostTaskService().getTaskBasic(rowguid);
    }

    @Override
    public AuditTask getBasicInfo(String rowguid) {
        return new HostTaskService().getBasicInfo(rowguid);
    }

    @Override
    public AuditTaskExtension getCbdwname(String rowguid) {
        return new HostTaskService().getCbdwname(rowguid);
    }

    @Override
    public AuditTask getImplementationBasis(String rowguid) {
        return new HostTaskService().getImplementationBasis(rowguid);
    }

    @Override
    public List<AuditTaskMaterial> geTaskMaterial(String taskguid) {
        return new HostTaskService().geTaskMaterial(taskguid);
    }

    @Override
    public Record getQcode(String taskguid) {
        return new HostTaskService().getQcode(taskguid);
    }

    @Override
    public FrameAttachInfo getFrameAttachInfoByClientguid(String clientguid) {
        return new HostTaskService().getFrameAttachInfoByClientguid(clientguid);
    }

    @Override
    public AuditTask getAuditTaskByTaskId(String taskid) {
        return new HostTaskService().getAuditTaskByTaskId(taskid);
    }

}
