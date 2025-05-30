package com.epoint.zwdt.zwdtrest.task.keyword.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 事项关键字关系表实体
 * 
 * @作者  yangyi
 * @version [版本号, 2022-06-17 10:47:41]
 */
@Entity(table = "audit_task_keyword", id = "rowguid")
public class AuditTaskKeyword extends BaseEntity implements Cloneable
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
  * 事项版本唯一标识
  */
  public  String  getTaskid(){ return super.get("taskid");}
public void setTaskid(String  taskid){ super.set("taskid",taskid);} /**
  * 关键字名称
  */
  public  String  getKeywordname(){ return super.get("keywordname");}
public void setKeywordname(String  keywordname){ super.set("keywordname",keywordname);} /**
  * 关键字代码项值
  */
  public  String  getKeywordvalue(){ return super.get("keywordvalue");}
public void setKeywordvalue(String  keywordvalue){ super.set("keywordvalue",keywordvalue);}

}