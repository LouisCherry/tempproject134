package com.epoint.auditqueue.auditqueuerest.queue.api;

import com.epoint.common.service.AuditCommonService;

public class HallWaitCountService extends AuditCommonService
{
      public int getHallWaitCount(String Hallguid,String status){
          String sql ="select Count(DISTINCT(c.ROWGUID)) from  audit_queue c INNER JOIN (select TaskTypeGuid from  audit_queue_window_tasktype a LEFT JOIN  audit_orga_window b on a.windowguid = b.rowguid where LOBBYTYPE=?) d on c.TASKGUID = d.TaskTypeGuid WHERE C.`STATUS` =?";
          return commonDao.queryInt(sql, Hallguid,status);
      }
}
