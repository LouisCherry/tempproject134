package com.epoint.auditprojectunusual.imp;

import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;
import com.epoint.basic.auditqueue.auditqueueuserinfo.domain.AuditQueueUserinfo;
import com.epoint.basic.auditqueue.auditqueueyuyuetime.domain.AuditQueueYuyuetime;
import com.epoint.basic.auditqueue.auditznsbcentertask.domain.AuditZnsbCentertask;
import com.epoint.basic.cache.CacheService9;
import com.epoint.common.service.AuditCommonService;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;

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
