package com.epoint.jiningzwfw.projectstatistics.projecttaskr.api.entity;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 每日办件事项记录表实体
 * 
 * @作者  yangyi
 * @version [版本号, 2021-07-01 09:24:03]
 */
@Entity(table = "PROJECT_TASK_R", id = "rowguid")
public class ProjectTaskR extends BaseEntity implements Cloneable
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
  * 日期
  */
  public  String  getSdate(){ return super.get("sdate");}
public void setSdate(String  sdate){ super.set("sdate",sdate);} /**
  * 事项版本标识
  */
  public  String  getTask_id(){ return super.get("task_id");}
public void setTask_id(String  task_id){ super.set("task_id",task_id);} /**
  * 事项编码
  */
  public  String  getItem_id(){ return super.get("item_id");}
public void setItem_id(String  item_id){ super.set("item_id",item_id);} /**
  * 办件所属辖区
  */
  public  String  getAreacode(){ return super.get("areacode");}
public void setAreacode(String  areacode){ super.set("areacode",areacode);} /**
  * 部门标识
  */
  public  String  getOuguid(){ return super.get("ouguid");}
public void setOuguid(String  ouguid){ super.set("ouguid",ouguid);} /**
  * 部门名称
  */
  public  String  getOuname(){ return super.get("ouname");}
public void setOuname(String  ouname){ super.set("ouname",ouname);}

}