package com.epoint.hcp.applet.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.core.grammar.Record;
import com.epoint.hcp.api.entity.Evainstance;
import com.epoint.hcp.applet.api.IOnlineUserService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lzj
 * @version [版本号, 2021-07-05 11:15:21]
 */
@Component
@Service
public class OnlineUserServiceServiceImpl implements IOnlineUserService {


    @Override
    public List<AuditOnlineRegister> geOnlineRegisterByOpenid(String openid) {
        return new OnlineUserServiceService().geOnlineRegisterByOpenid(openid);
    }

    @Override
    public List<AuditProject> findAuditProjectByCertNum(String idnumber) {
        return new OnlineUserServiceService().findAuditProjectByCertNum(idnumber);
    }

    @Override
    public AuditProject getAuditProjectByRowGuid(String rowguid) {
        return new OnlineUserServiceService().getAuditProjectByRowGuid(rowguid);
    }

    @Override
    public List<Evainstance> getZbByFlowsn(String flowsn) {
        return new OnlineUserServiceService().getZbByFlowsn(flowsn);
    }

    @Override
    public Record getZhibiao(String zb) {
        return new OnlineUserServiceService().getZhibiao(zb);
    }

}
