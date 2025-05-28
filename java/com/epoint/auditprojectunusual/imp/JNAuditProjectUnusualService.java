package com.epoint.auditprojectunusual.imp;

import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;

import java.util.List;

/**
 * 设备维护表对应的后台service
 * 
 * @author zhaoy
 * 
 */
public class JNAuditProjectUnusualService
{

    /**
     * 通用dao
     */
    private ICommonDao baseDao;

    public JNAuditProjectUnusualService() {
        baseDao = CommonDao.getInstance();
    }
    
    public List<AuditProjectUnusual> getZantingData(String projectguid){
        String sql = "select * FROM audit_project_unusual where PROJECTGUID=? and OPERATETYPE in(10,11) order by OperateDate asc";
        return baseDao.findList(sql, AuditProjectUnusual.class,projectguid);
    }

    
}
