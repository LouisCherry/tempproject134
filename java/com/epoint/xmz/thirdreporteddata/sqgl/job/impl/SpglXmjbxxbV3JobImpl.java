package com.epoint.xmz.thirdreporteddata.sqgl.job.impl;

import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.xmz.thirdreporteddata.sqgl.job.api.ISpglXmjbxxbV3Job;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service
public class SpglXmjbxxbV3JobImpl implements ISpglXmjbxxbV3Job {

    @Override
    public List<AuditRsItemBaseinfo> getAuditRsItemBaseInfoList() {
        return new SpglXmjbxxbV3JobService().getAuditRsItemBaseInfoList();
    }

    @Override
    public boolean IsXmjbxxbV3(String gcdm) {
        return new SpglXmjbxxbV3JobService().IsXmjbxxbV3(gcdm);
    }
}
