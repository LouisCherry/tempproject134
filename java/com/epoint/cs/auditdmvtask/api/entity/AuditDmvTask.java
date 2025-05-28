package com.epoint.cs.auditdmvtask.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 车管所事项实体
 * 
 * @作者  admin
 * @version [版本号, 2021-03-02 11:37:44]
 */
@Entity(table = "audit_dmv_task", id = "rowguid")
public class AuditDmvTask extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

 /**
  * 所属辖区号
  */
  public  String  getBelongxiaqucode(){ return super.get("belongxiaqucode");}
public void setBelongxiaqucode(String  belongxiaqucode){ super.set("belongxiaqucode",belongxiaqucode);} /**
  * 操作者名字
  */
  public  String  getOperateusername(){ return super.get("operateusername");}
public void setOperateusername(String  operateusername){ super.set("operateusername",operateusername);} /**
  * 操作日期
  */
  public  Date  getOperatedate(){ return super.getDate("operatedate");}
public void setOperatedate(Date  operatedate){ super.set("operatedate",operatedate);} /**
  * 序号
  */
  public  Integer  getRow_id(){ return super.getInt("row_id");}
public void setRow_id(Integer  row_id){ super.set("row_id",row_id);} /**
  * 年份标识
  */
  public  String  getYearflag(){ return super.get("yearflag");}
public void setYearflag(String  yearflag){ super.set("yearflag",yearflag);} /**
  * 默认主键字段
  */
  public  String  getRowguid(){ return super.get("rowguid");}
public void setRowguid(String  rowguid){ super.set("rowguid",rowguid);} /**
  * 事项名称
  */
  public  String  getTaskname(){ return super.get("taskname");}
public void setTaskname(String  taskname){ super.set("taskname",taskname);} /**
  * 关联事项名称
  */
  public  String  getLinkedtaskname(){ return super.get("linkedtaskname");}
public void setLinkedtaskname(String  linkedtaskname){ super.set("linkedtaskname",linkedtaskname);} /**
  * 关联事项
  */
  public  String  getLinkedtaskguid(){ return super.get("linkedtaskguid");}
public void setLinkedtaskguid(String  linkedtaskguid){ super.set("linkedtaskguid",linkedtaskguid);}

}