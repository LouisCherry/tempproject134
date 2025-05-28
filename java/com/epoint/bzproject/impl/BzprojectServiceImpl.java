package com.epoint.bzproject.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditonlineuser.auditonlineproject.domain.AuditOnlineProject;
import com.epoint.bzproject.api.IBzprojectService;
import org.springframework.stereotype.Component;

@Component
@Service
public class BzprojectServiceImpl implements IBzprojectService {

    @Override
    public AuditOnlineProject getOnlineProjectByguid(String SOURCEGUID) {
        return new BzprojectService().getOnlineProjectByguid(SOURCEGUID);
    }

}
