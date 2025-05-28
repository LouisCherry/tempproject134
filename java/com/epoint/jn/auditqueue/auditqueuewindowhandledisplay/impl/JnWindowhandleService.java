package com.epoint.jn.auditqueue.auditqueuewindowhandledisplay.impl;

import java.util.List;

import com.epoint.basic.auditqueue.auditqueue.domain.AuditQueue;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;

public class JnWindowhandleService
{
public List<AuditQueue> getorderedlinkauditqueue(String equipmentguid){
    ICommonDao commondao = CommonDao.getInstance();
    String sql = "select QNO,HANDLEWINDOWGUID from audit_queue a  inner join (SELECT WINDOWGUID from audit_queue_window where equipmentguid='"+equipmentguid+"') w on a.HANDLEWINDOWGUID= w.WINDOWGUID where  `STATUS`='1' ORDER BY OPERATEDATE DESC ;";
    return commondao.findList(sql, AuditQueue.class);
}
public List<AuditQueue> getauditqueuenohander(String equipmentguid){
    ICommonDao commondao = CommonDao.getInstance();
    String sql = "select QNO from audit_queue where TASKGUID in (select TaskTypeGuid from audit_queue_window_tasktype where WindowGuid in (SELECT WINDOWGUID from audit_queue_window where equipmentguid='"+equipmentguid+"')) AND `STATUS`='0' ORDER BY GETNOTIME ASC LIMIT 8";
    return commondao.findList(sql, AuditQueue.class);
}

}
