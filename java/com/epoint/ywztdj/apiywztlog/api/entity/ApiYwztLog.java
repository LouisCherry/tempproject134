package com.epoint.ywztdj.apiywztlog.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 业务中台接口日志表实体
 * 
 * @作者  15056
 * @version [版本号, 2024-01-08 14:41:09]
 */
@Entity(table = "api_ywzt_log", id = "rowguid")
public class ApiYwztLog extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

 /**
  * 业务标识
  */
  public  String  getBusinessid(){ return super.get("businessid");}
public void setBusinessid(String  businessid){ super.set("businessid",businessid);} /**
  * 业务名称
  */
  public  String  getBusinessname(){ return super.get("businessname");}
public void setBusinessname(String  businessname){ super.set("businessname",businessname);} /**
  * 接口调用结果说明
  */
  public  String  getCallresultexpl(){ return super.get("callresultexpl");}
public void setCallresultexpl(String  callresultexpl){ super.set("callresultexpl",callresultexpl);} /**
  * 接口调用结果标识
  */
  public  Integer  getCallresultid(){ return super.getInt("callresultid");}
public void setCallresultid(Integer  callresultid){ super.set("callresultid",callresultid);} /**
  * 创建时间
  */
  public  Date  getCreated_time(){ return super.getDate("created_time");}
public void setCreated_time(Date  created_time){ super.set("created_time",created_time);} /**
  * 接口名称
  */
  public  String  getInterfacename(){ return super.get("interfacename");}
public void setInterfacename(String  interfacename){ super.set("interfacename",interfacename);} /**
  * 接口地址
  */
  public  String  getInterfaceuri(){ return super.get("interfaceuri");}
public void setInterfaceuri(String  interfaceuri){ super.set("interfaceuri",interfaceuri);} /**
  * 接口参数
  */
  public  String  getParams(){ return super.get("params");}
public void setParams(String  params){ super.set("params",params);} /**
  * 默认主键字段
  */
  public  String  getRowguid(){ return super.get("rowguid");}
public void setRowguid(String  rowguid){ super.set("rowguid",rowguid);} /**
  * 年份标识
  */
  public  String  getYearflag(){ return super.get("yearflag");}
public void setYearflag(String  yearflag){ super.set("yearflag",yearflag);} /**
  * 序号
  */
  public  Integer  getRow_id(){ return super.getInt("row_id");}
public void setRow_id(Integer  row_id){ super.set("row_id",row_id);} /**
  * 操作日期
  */
  public  Date  getOperatedate(){ return super.getDate("operatedate");}
public void setOperatedate(Date  operatedate){ super.set("operatedate",operatedate);} /**
  * 操作者名字
  */
  public  String  getOperateusername(){ return super.get("operateusername");}
public void setOperateusername(String  operateusername){ super.set("operateusername",operateusername);} /**
  * 所属辖区号
  */
  public  String  getBelongxiaqucode(){ return super.get("belongxiaqucode");}
public void setBelongxiaqucode(String  belongxiaqucode){ super.set("belongxiaqucode",belongxiaqucode);}

}