package com.epoint.zoucheng.device.infopub.infopubplayprogram.api.entity;
import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 发布单节目列表实体
 * 
 * @作者  why
 * @version [版本号, 2019-09-23 11:20:18]
 */
@Entity(table = "InfoPub_PlayProgram", id = "rowguid")
public class InfopubPlayprogram extends BaseEntity implements Cloneable
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
  * 发布单Guid
  */
  public  String  getPlayguid(){ return super.get("playguid");}
public void setPlayguid(String  playguid){ super.set("playguid",playguid);} /**
  * 节目Guid
  */
  public  String  getProgramguid(){ return super.get("programguid");}
public void setProgramguid(String  programguid){ super.set("programguid",programguid);} /**
  * 播放开始时间（小时）
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
  * 播放时间
  */
  public  Integer  getShowtime(){ return super.getInt("showtime");}
public void setShowtime(Integer  showtime){ super.set("showtime",showtime);} /**
  * 设备音量
  */
  public  Integer  getVol(){ return super.getInt("vol");}
public void setVol(Integer  vol){ super.set("vol",vol);}

}