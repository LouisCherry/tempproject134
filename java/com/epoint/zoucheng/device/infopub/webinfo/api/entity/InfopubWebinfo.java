package com.epoint.zoucheng.device.infopub.webinfo.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 网页信息表实体
 * 
 * @作者  why
 * @version [版本号, 2019-09-17 11:17:19]
 */
@Entity(table = "InfoPub_WebInfo", id = "rowguid")
public class InfopubWebinfo extends BaseEntity implements Cloneable
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
  * 网页地址
  */
  public  String  getWebaddress(){ return super.get("webaddress");}
public void setWebaddress(String  webaddress){ super.set("webaddress",webaddress);} /**
  * 网页名称
  */
  public  String  getWebname(){ return super.get("webname");}
public void setWebname(String  webname){ super.set("webname",webname);}

}