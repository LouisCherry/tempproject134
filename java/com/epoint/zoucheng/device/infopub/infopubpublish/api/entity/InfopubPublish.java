package com.epoint.zoucheng.device.infopub.infopubpublish.api.entity;
import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 节目发布表实体
 * 
 * @作者  why
 * @version [版本号, 2019-09-23 11:19:47]
 */
@Entity(table = "InfoPub_Publish", id = "rowguid")
public class InfopubPublish extends BaseEntity implements Cloneable
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
  * LED标识
  */
  public  String  getLedguid(){ return super.get("ledguid");}
public void setLedguid(String  ledguid){ super.set("ledguid",ledguid);} /**
  * 节目标识
  */
  public  String  getProgramguid(){ return super.get("programguid");}
public void setProgramguid(String  programguid){ super.set("programguid",programguid);} /**
  * 播放开始时间（小时
  */
  public  Integer  getStarttimehour(){ return super.getInt("starttimehour");}
public void setStarttimehour(Integer  starttimehour){ super.set("starttimehour",starttimehour);} /**
  * 播放开始时间（分钟）
  */
  public  Integer  getStarttimeminute(){ return super.getInt("starttimeminute");}
public void setStarttimeminute(Integer  starttimeminute){ super.set("starttimeminute",starttimeminute);} /**
  * 播放结束时间（小时）
  */
  public  Integer  getEndtimehour(){ return super.getInt("endtimehour");}
public void setEndtimehour(Integer  endtimehour){ super.set("endtimehour",endtimehour);} /**
  * 播放结束时间（分钟）
  */
  public  Integer  getEndtimeminute(){ return super.getInt("endtimeminute");}
public void setEndtimeminute(Integer  endtimeminute){ super.set("endtimeminute",endtimeminute);} /**
  * 循环播放时长
  */
  public  Integer  getDuration(){ return super.getInt("duration");}
public void setDuration(Integer  duration){ super.set("duration",duration);}

}