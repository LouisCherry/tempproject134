package com.epoint.znsb.auditznsbwater.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 水务对账信息实体
 * 
 * @作者  HYF
 * @version [版本号, 2021-11-11 16:08:59]
 */
@Entity(table = "AUDIT_ZNSB_WATER", id = "rowguid")
public class Auditznsbwater extends BaseEntity implements Cloneable
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
  * 对账文件
  */
  public  String  getWaterinfo(){ return super.get("waterinfo");}
public void setWaterinfo(String  waterinfo){ super.set("waterinfo",waterinfo);} /**
  * 是否成功
  */
  public  String  getIsupload(){ return super.get("isupload");}
public void setIsupload(String  isupload){ super.set("isupload",isupload);}

}