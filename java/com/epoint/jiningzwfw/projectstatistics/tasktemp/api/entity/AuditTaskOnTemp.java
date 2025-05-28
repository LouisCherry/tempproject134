package com.epoint.jiningzwfw.projectstatistics.tasktemp.api.entity;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 实体
 * 
 * @作者  yangyi
 * @version [版本号, 2021-06-30 16:53:22]
 */
@Entity(table = "audit_task_on_temp", id = "rowguid")
public class AuditTaskOnTemp extends BaseEntity implements Cloneable
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
  * 事项ID
  */
  public  String  getTask_id(){ return super.get("task_id");}
public void setTask_id(String  task_id){ super.set("task_id",task_id);} /**
  * 事项编码
  */
  public  String  getItem_id(){ return super.get("item_id");}
public void setItem_id(String  item_id){ super.set("item_id",item_id);} /**
  * 事项所属部门
  */
  public  String  getOuguid(){ return super.get("ouguid");}
public void setOuguid(String  ouguid){ super.set("ouguid",ouguid);} /**
  * 是否可编辑
  */
  public  String  getIs_editafterimport(){ return super.get("is_editafterimport");}
public void setIs_editafterimport(String  is_editafterimport){ super.set("is_editafterimport",is_editafterimport);} /**
  * 是否启用
  */
  public  String  getIs_enable(){ return super.get("is_enable");}
public void setIs_enable(String  is_enable){ super.set("is_enable",is_enable);} /**
  * 是否为历史版本
  */
  public  String  getIs_history(){ return super.get("is_history");}
public void setIs_history(String  is_history){ super.set("is_history",is_history);} /**
  * 业务类型
  */
  public  String  getBusinesstype(){ return super.get("businesstype");}
public void setBusinesstype(String  businesstype){ super.set("businesstype",businesstype);} /**
  * 事项所属辖区
  */
  public  String  getAreacode(){ return super.get("areacode");}
public void setAreacode(String  areacode){ super.set("areacode",areacode);} /**
 * 事项名称
 */
public  String  getTaskname(){ return super.get("taskname");}
  public void setTaskname(String  taskname){ super.set("taskname",taskname);}
  /**
   * 事项名称
   */
  public  String  getOuname(){ return super.get("ouname");}
  public void setOuname(String  ouname){ super.set("ouname",ouname);}

}