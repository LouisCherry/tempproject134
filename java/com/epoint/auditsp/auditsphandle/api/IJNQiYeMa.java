package com.epoint.auditsp.auditsphandle.api;

import java.util.List;

import com.epoint.frame.service.attach.entity.FrameAttachInfo;

public interface IJNQiYeMa
{

    List<FrameAttachInfo> findListByGuids(String attachguids, int first, int pageSize, String sortField,
            String sortOrder);
}
