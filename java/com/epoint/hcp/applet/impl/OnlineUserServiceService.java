package com.epoint.hcp.applet.impl;

import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.hcp.api.entity.Evainstance;
import org.apache.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * @author lzj
 * @version [版本号, 2021-07-05 11:15:21]
 */
public class OnlineUserServiceService {

    Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public OnlineUserServiceService() {
        baseDao = CommonDao.getInstance();
    }


    public List<AuditOnlineRegister> geOnlineRegisterByOpenid(String openid) {
        String sql = "select IDNUMBER from audit_online_register where openid=?";
        List<AuditOnlineRegister> ad = baseDao.findList(sql, AuditOnlineRegister.class, openid);
        baseDao.close();
        return ad;
    }

    public List<AuditProject> findAuditProjectByCertNum(String idnumber) {
        String sql = "select projectname,flowsn,ouname,applydate,rowguid,applydate  from audit_project where CERTNUM=?";
        List<AuditProject> ap = baseDao.findList(sql, AuditProject.class, idnumber);
        baseDao.close();
        return ap;
    }

    public AuditProject getAuditProjectByRowGuid(String rowguid) {
        String sql = "select areacode,flowsn,windowguid,projectname,rowguid,ouname,applydate,hcpstatus,ouname from audit_project where rowguid=?";
        AuditProject auditProject = baseDao.find(sql, AuditProject.class, rowguid);
        baseDao.close();
        return auditProject;

    }

    public List<Evainstance> getZbByFlowsn(String flowsn) {
        String sql = "select evalDetail,writingEvaluation,satisfaction,projectno from evainstance where projectno = ? ";
        List<Evainstance> ev = baseDao.findList(sql, Evainstance.class, flowsn);
        baseDao.close();
        return ev;
    }

    public Record getZhibiao(String zb) {
        String sql = "SELECT itemtext itemtext,ITEMVALUE ITEMVALUE FROM code_items WHERE codeid = '1016097' AND ITEMVALUE = ?";
        Record record = baseDao.find(sql, Record.class, zb);
        baseDao.close();
        return record;
    }
}
