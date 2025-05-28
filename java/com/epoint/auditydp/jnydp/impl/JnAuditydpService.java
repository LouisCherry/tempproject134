package com.epoint.auditydp.jnydp.impl;

import java.util.List;

import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindow;
import com.epoint.basic.auditqueue.auditznsbcentertask.domain.AuditZnsbCentertask;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.dict.domain.AuditTaskDict;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

public class JnAuditydpService
{
  public List<FrameOu> getConditionOU(String condition,int firstpage,int pagesize){
      ICommonDao commondao = CommonDao.getInstance();
      String sql = "select  OUNAME,OUSHORTNAME,OUGUID from frame_ou where OUNAME like '%"+condition+"%' and OUGUID in (select DISTINCT OUGUID from audit_znsb_centertask where CenterGuid in ('46db0d30-b3ea-4d9c-8a66-771731e4b33a','4391ec2f-6903-4a1a-af2d-6ba281bc5884'))";
      return commondao.findList(sql, firstpage, pagesize, FrameOu.class);
}
  
  public int getConditionOUCount(String condition){
      ICommonDao commondao = CommonDao.getInstance();
      String sql = "select  count(1) from frame_ou where OUNAME like '%"+condition+"%' and OUGUID in (select DISTINCT OUGUID from audit_znsb_centertask where CenterGuid in ('46db0d30-b3ea-4d9c-8a66-771731e4b33a','4391ec2f-6903-4a1a-af2d-6ba281bc5884'))";
      return commondao.queryInt(sql);
}
  public List<AuditZnsbCentertask> getConditionTask(String condition,int firstpage,int pagesize){
      ICommonDao commondao = CommonDao.getInstance();
      String sql = "select * from audit_znsb_centertask where CenterGuid in ('46db0d30-b3ea-4d9c-8a66-771731e4b33a','4391ec2f-6903-4a1a-af2d-6ba281bc5884') and TASKNAME LIKE '%"+condition+"%' ";
      return commondao.findList(sql, firstpage, pagesize, AuditZnsbCentertask.class);
  }
  public int getConditionTaskCount(String condition){
      ICommonDao commondao = CommonDao.getInstance();
      String sql = "select count(1) from audit_znsb_centertask where CenterGuid in ('46db0d30-b3ea-4d9c-8a66-771731e4b33a','4391ec2f-6903-4a1a-af2d-6ba281bc5884') and TASKNAME LIKE '%"+condition+"%' ";
      return commondao.queryInt(sql);
  }
  
  public List<AuditTaskDict> getConditionDict(String condition,int firstpage,int pagesize){
      ICommonDao commondao = CommonDao.getInstance();
      String sql = "select * from audit_task_dict where CLASS_NAME like '%"+condition+"%' and RowGuid in (select DISTINCT DICT_ID from audit_task_map)";
      return commondao.findList(sql, firstpage, pagesize, AuditTaskDict.class);
  }
  public int getConditionDictCount(String condition){
      ICommonDao commondao = CommonDao.getInstance();
      String sql = "select count(1) from audit_task_dict where CLASS_NAME like '%"+condition+"%' and RowGuid in (select DISTINCT DICT_ID from audit_task_map)";
      return commondao.queryInt(sql);
  }
  public List<AuditOrgaWindow> getWindowListByTaskId(String taskid,int firstpage,int pagesize) {
      ICommonDao commondao = CommonDao.getInstance();
      String sql = "SELECT a.* FROM audit_orga_window a ,audit_orga_windowtask b WHERE a.RowGuid=b.WINDOWGUID AND b.TASKID =?1 and a.windowtype='10'";
      return commondao.findList(sql,firstpage, pagesize, AuditOrgaWindow.class, new Object[]{taskid});
  }
  public int getWindowListCountByTaskId(String taskid){
      ICommonDao commondao = CommonDao.getInstance();
      String sql = "SELECT count(1) FROM audit_orga_window a ,audit_orga_windowtask b WHERE a.RowGuid=b.WINDOWGUID AND b.TASKID =?1  and a.windowtype='10'";
      return commondao.queryInt(sql,new Object[]{taskid});
  }
}
