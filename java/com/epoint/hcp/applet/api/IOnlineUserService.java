package com.epoint.hcp.applet.api;

import java.io.Serializable;
import java.util.List;

import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.core.grammar.Record;
import com.epoint.hcp.api.entity.Evainstance;

/**
 * @author lzj
 * @version [版本号, 2021-07-05 11:15:21]
 */
public interface IOnlineUserService extends Serializable {

    List<AuditOnlineRegister> geOnlineRegisterByOpenid(String openid);

    List<AuditProject> findAuditProjectByCertNum(String idnumber);

    AuditProject getAuditProjectByRowGuid(String rowguid);

    List<Evainstance> getZbByFlowsn(String flowsn);

    Record getZhibiao(String zb);

}
