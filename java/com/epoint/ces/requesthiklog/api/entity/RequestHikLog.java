package com.epoint.ces.requesthiklog.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 请求海康日志信息表实体
 * 
 * @作者  shun
 * @version [版本号, 2021-11-22 14:30:42]
 */
@Entity(table = "REQUEST_HIK_LOG", id = "rowguid")
public class RequestHikLog extends BaseEntity implements Cloneable
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
  * 请求时间
  */
  public  Date  getReqdate(){ return super.getDate("reqdate");}
public void setReqdate(Date  reqdate){ super.set("reqdate",reqdate);} /**
  * 响应时间(毫秒)
  */
  public  Integer  getRespdate(){ return super.getInt("respdate");}
public void setRespdate(Integer  respdate){ super.set("respdate",respdate);} /**
  * 响应结果
  */
  public  String  getRespcontent(){ return super.get("respcontent");}
public void setRespcontent(String  respcontent){ super.set("respcontent",respcontent);}

}