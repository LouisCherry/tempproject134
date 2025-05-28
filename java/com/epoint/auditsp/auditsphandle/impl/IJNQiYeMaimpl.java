package com.epoint.auditsp.auditsphandle.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.auditsp.auditsphandle.api.IJNQiYeMa;
import com.epoint.auditsp.auditsphandle.api.IJnAuditSpInstance;
import com.epoint.basic.auditsp.auditspinstance.domain.AuditSpInstance;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;

@Component
@Service
public class IJNQiYeMaimpl implements IJNQiYeMa
{

    @Override
    public List<FrameAttachInfo> findListByGuids(String attachguids, int first, int pageSize, String sortField,
            String sortOrder) {
        return new JNQiYeMaService().findListByGuids(attachguids, first, pageSize, sortField, sortOrder);
    }


}
