package com.epoint.zoucheng.device.infopub.infopubprogram.api.entity;
import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 节目表实体
 * 
 * @作者  why
 * @version [版本号, 2019-09-23 10:52:48]
 */
@Entity(table = "InfoPub_Program", id = "rowguid")
public class InfopubProgram extends BaseEntity implements Cloneable
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
  * 节目名称
  */
  public  String  getProgramname(){ return super.get("programname");}
public void setProgramname(String  programname){ super.set("programname",programname);} /**
  * 节目内容
  */
  public  String  getContent(){ return super.get("content");}
public void setContent(String  content){ super.set("content",content);} /**
  * 分辨率
  */
  public  String  getResolution(){ return super.get("resolution");}
public void setResolution(String  resolution){ super.set("resolution",resolution);} /**
  * 更新时间
  */
  public  Date  getUpdatetime(){ return super.getDate("updatetime");}
public void setUpdatetime(Date  updatetime){ super.set("updatetime",updatetime);} /**
  * 网页滚动速度
  */
  public  String  getScrollspeed(){ return super.get("scrollspeed");}
public void setScrollspeed(String  scrollspeed){ super.set("scrollspeed",scrollspeed);} /**
  * 图片轮播时间
  */
  public  Integer  getDelaytime(){ return super.getInt("delaytime");}
public void setDelaytime(Integer  delaytime){ super.set("delaytime",delaytime);} /**
  * 锁定边界
  */
  public  String  getLockborder(){ return super.get("lockborder");}
public void setLockborder(String  lockborder){ super.set("lockborder",lockborder);} /**
  * MD5
  */
  public  String  getMd5(){ return super.get("md5");}
public void setMd5(String  md5){ super.set("md5",md5);} /**
  * 下载地址
  */
  public  String  getPath(){ return super.get("path");}
public void setPath(String  path){ super.set("path",path);}

}