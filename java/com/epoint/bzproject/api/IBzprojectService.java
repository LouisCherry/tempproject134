package com.epoint.bzproject.api;

import com.epoint.basic.auditonlineuser.auditonlineproject.domain.AuditOnlineProject;

public interface IBzprojectService {

    AuditOnlineProject getOnlineProjectByguid(String SOURCEGUID);

}
