package com.epoint.xmz.xstkdk.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 国土踏勘地块实体
 * 
 * @作者  1
 * @version [版本号, 2022-10-06 17:19:01]
 */
@Entity(table = "XS_TKDK", id = "rowguid")
public class XsTkdk extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

 /**
  * 地块编号
  */
  public  String  getDkbh(){ return super.get("dkbh");}
public void setDkbh(String  dkbh){ super.set("dkbh",dkbh);} /**
  * 地块名称
  */
  public  String  getDkmc(){ return super.get("dkmc");}
public void setDkmc(String  dkmc){ super.set("dkmc",dkmc);} /**
  * 地块面积
  */
  public  String  getDkmj(){ return super.get("dkmj");}
public void setDkmj(String  dkmj){ super.set("dkmj",dkmj);} /**
  * 空间位置
  */
  public  String  getKjwz(){ return super.get("kjwz");}
public void setKjwz(String  kjwz){ super.set("kjwz",kjwz);} /**
  * 备注
  */
  public  String  getRemark(){ return super.get("remark");}
public void setRemark(String  remark){ super.set("remark",remark);} /**
  * 状态
  */
  public  String  getStatus(){ return super.get("status");}
public void setStatus(String  status){ super.set("status",status);} /**
  * 土地用途代码
  */
  public  String  getTdytdm(){ return super.get("tdytdm");}
public void setTdytdm(String  tdytdm){ super.set("tdytdm",tdytdm);} /**
  * 土地用途名称
  */
  public  String  getTdytmc(){ return super.get("tdytmc");}
public void setTdytmc(String  tdytmc){ super.set("tdytmc",tdytmc);} /**
  * 踏勘ID
  */
  public  String  getTkid(){ return super.get("tkid");}
public void setTkid(String  tkid){ super.set("tkid",tkid);} /**
  * 县级行政区代码
  */
  public  String  getXjxzqdm(){ return super.get("xjxzqdm");}
public void setXjxzqdm(String  xjxzqdm){ super.set("xjxzqdm",xjxzqdm);} /**
  * 县级行政区名称
  */
  public  String  getXjxzqmc(){ return super.get("xjxzqmc");}
public void setXjxzqmc(String  xjxzqmc){ super.set("xjxzqmc",xjxzqmc);} /**
  * 项目代码
  */
  public  String  getXmdm(){ return super.get("xmdm");}
public void setXmdm(String  xmdm){ super.set("xmdm",xmdm);} /**
  * 要素代码
  */
  public  String  getYsdm(){ return super.get("ysdm");}
public void setYsdm(String  ysdm){ super.set("ysdm",ysdm);} /**
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