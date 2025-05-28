package com.epoint.auditqueue.auditqueuerest.window;

import java.util.List;

import com.epoint.basic.auditqueue.auditqueue.domain.AuditQueue;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;


public class GXQAuditQueueService
{

    /**
     * 数据增删改查组件
     */
    protected ICommonDao baseDao;

    public GXQAuditQueueService() {
        baseDao = CommonDao.getInstance();
    }

 
  public List<Record> getHandlingQueue(String hallguid){
     String  condition = "";
     
      if(!"all".equals(hallguid)){
          hallguid =  "'" + hallguid.replace(",", "','") + "'";
          condition = "hallguid in ("+hallguid+") and";
      }
   
      String sql="select a.*,b.WINDOWNO windowno from (select  QNO currentno,HANDLEWINDOWGUID,CALLTIME from  audit_queue where  "+condition+"   status ='1' ) a LEFT JOIN audit_orga_window b on a.HANDLEWINDOWGUID = b.RowGuid  order by a.CALLTIME desc ";
      ICommonDao commndao = CommonDao.getInstance();
      return commndao.findList(sql, Record.class);
  }
}
