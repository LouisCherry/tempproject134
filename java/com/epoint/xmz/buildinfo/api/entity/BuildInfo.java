package com.epoint.xmz.buildinfo.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 工改二阶段建筑表实体
 * 
 * @作者  1
 * @version [版本号, 2022-09-08 14:14:05]
 */
@Entity(table = "build_info", id = "rowguid")
public class BuildInfo extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

 /**
  * 防护单元
  */
  public  String  getFhdy(){ return super.get("fhdy");}
public void setFhdy(String  fhdy){ super.set("fhdy",fhdy);} /**
  * 防化级别
  */
  public  String  getFhjb(){ return super.get("fhjb");}
public void setFhjb(String  fhjb){ super.set("fhjb",fhjb);} /**
  * 项目标识
  */
  public  String  getItemguid(){ return super.get("itemguid");}
public void setItemguid(String  itemguid){ super.set("itemguid",itemguid);} /**
  * 建筑面积
  */
  public  String  getJzmj(){ return super.get("jzmj");}
public void setJzmj(String  jzmj){ super.set("jzmj",jzmj);} /**
  * 抗力级别
  */
  public  String  getKljb(){ return super.get("kljb");}
public void setKljb(String  kljb){ super.set("kljb",kljb);} /**
  * 平时用途
  */
  public  String  getPsyt(){ return super.get("psyt");}
public void setPsyt(String  psyt){ super.set("psyt",psyt);} /**
  * 申报标识
  */
  public  String  getSubappguid(){ return super.get("subappguid");}
public void setSubappguid(String  subappguid){ super.set("subappguid",subappguid);} /**
  * 类型
  */
  public  String  getType(){ return super.get("type");}
public void setType(String  type){ super.set("type",type);} /**
  * 战时功能
  */
  public  String  getZsgn(){ return super.get("zsgn");}
public void setZsgn(String  zsgn){ super.set("zsgn",zsgn);} /**
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