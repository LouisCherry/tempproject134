package com.epoint.auditqueue.qhj.impl;

import java.util.List;

import com.epoint.basic.auditqueue.auditqueuetasktype.domain.AuditQueueTasktype;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;

public class JNTasktypeListService
{
   public List<Record> getWindowLinkedTskType(String centerguid,String ouguid,int firstpage,int pagesize){
       ICommonDao commondao = CommonDao.getInstance(); 
       String sql ="select TaskTypeName,RowGuid from audit_queue_tasktype  where ouguid=?1 and centerguid=?2 and RowGuid in (select DISTINCT TaskTypeguid from audit_queue_window_tasktype) ORDER BY OrderNum desc";
       return commondao.findList(sql, firstpage, pagesize, Record.class, new Object[]{ouguid, centerguid});
   }
   
   public int getWindowLinkedTskTypeCount(String centerguid,String ouguid){
       ICommonDao commondao = CommonDao.getInstance(); 
       String sql ="select count(1) from audit_queue_tasktype  where ouguid=?1 and centerguid=?2 and RowGuid in (select DISTINCT TaskTypeguid from audit_queue_window_tasktype)";
       return commondao.queryInt(sql, new Object[]{ouguid, centerguid});
   }
}
