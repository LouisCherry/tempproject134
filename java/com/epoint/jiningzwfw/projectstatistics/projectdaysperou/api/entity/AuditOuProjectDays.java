package com.epoint.jiningzwfw.projectstatistics.projectdaysperou.api.entity;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 每日部门办件统计分析表实体
 * 
 * @作者  yangyi
 * @version [版本号, 2021-06-30 17:37:23]
 */
@Entity(table = "AUDIT_OU_PROJECT_DAYS", id = "rowguid")
public class AuditOuProjectDays extends BaseEntity implements Cloneable
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
  * 部门标识
  */
  public  String  getOuguid(){ return super.get("ouguid");}
public void setOuguid(String  ouguid){ super.set("ouguid",ouguid);} /**
  * 部门名称
  */
  public  String  getOuname(){ return super.get("ouname");}
public void setOuname(String  ouname){ super.set("ouname",ouname);} /**
  * 部门所属辖区号
  */
  public  String  getAreacode(){ return super.get("areacode");}
public void setAreacode(String  areacode){ super.set("areacode",areacode);} /**
  * 办结量
  */
  public  Integer  getTocnum(){ return super.getInt("tocnum");}
public void setTocnum(Integer  tocnum){ super.set("tocnum",tocnum);} /**
  * 办结外网量（外网申报数）
  */
  public  Integer  getExttocnum(){ return super.getInt("exttocnum");}
public void setExttocnum(Integer  exttocnum){ super.set("exttocnum",exttocnum);} /**
  * 评价量
  */
  public  Integer  getEvanum(){ return super.getInt("evanum");}
public void setEvanum(Integer  evanum){ super.set("evanum",evanum);} /**
  * 外网申报未受理量
  */
  public  Integer  getExtnotacceptednum(){ return super.getInt("extnotacceptednum");}
public void setExtnotacceptednum(Integer  extnotacceptednum){ super.set("extnotacceptednum",extnotacceptednum);} /**
  * 按期办理量
  */
  public  Integer  getOnnum(){ return super.getInt("onnum");}
public void setOnnum(Integer  onnum){ super.set("onnum",onnum);} /**
  * 网上咨询量
  */
  public  Integer  getExtconnum(){ return super.getInt("extconnum");}
public void setExtconnum(Integer  extconnum){ super.set("extconnum",extconnum);} /**
  * 网上按期答复量
  */
  public  Integer  getExtconreplynum(){ return super.getInt("extconreplynum");}
public void setExtconreplynum(Integer  extconreplynum){ super.set("extconreplynum",extconreplynum);}

}