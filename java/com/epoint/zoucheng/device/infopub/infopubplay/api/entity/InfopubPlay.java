package com.epoint.zoucheng.device.infopub.infopubplay.api.entity;
import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 节目发布单实体
 * 
 * @作者  why
 * @version [版本号, 2019-09-23 11:20:32]
 */
@Entity(table = "InfoPub_Play", id = "rowguid")
public class InfopubPlay extends BaseEntity implements Cloneable
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
  * 发布单名称
  */
  public  String  getPlayname(){ return super.get("playname");}
public void setPlayname(String  playname){ super.set("playname",playname);} /**
  * 发布人
  */
  public  String  getPlayuser(){ return super.get("playuser");}
public void setPlayuser(String  playuser){ super.set("playuser",playuser);} /**
  * 发布时间
  */
  public  Date  getPlaytime(){ return super.getDate("playtime");}
public void setPlaytime(Date  playtime){ super.set("playtime",playtime);}

}