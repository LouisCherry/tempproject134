package com.epoint.xmz.xstkjl.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 国土踏勘记录实体
 * 
 * @作者  1
 * @version [版本号, 2022-10-06 17:18:51]
 */
@Entity(table = "XS_TKJL", id = "rowguid")
public class XsTkjl extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

 /**
  * 备注
  */
  public  String  getBz(){ return super.get("bz");}
public void setBz(String  bz){ super.set("bz",bz);} /**
  * 记录日期
  */
  public  Date  getJlrq(){ return super.getDate("jlrq");}
public void setJlrq(Date  jlrq){ super.set("jlrq",jlrq);} /**
  * 状态
  */
  public  String  getStatus(){ return super.get("status");}
public void setStatus(String  status){ super.set("status",status);} /**
  * 踏勘ID
  */
  public  Integer  getTkid(){ return super.getInt("tkid");}
public void setTkid(Integer  tkid){ super.set("tkid",tkid);} /**
  * 踏勘名称
  */
  public  String  getTkmc(){ return super.get("tkmc");}
public void setTkmc(String  tkmc){ super.set("tkmc",tkmc);} /**
  * 项目编号
  */
  public  String  getXmbh(){ return super.get("xmbh");}
public void setXmbh(String  xmbh){ super.set("xmbh",xmbh);} /**
  * 项目代码
  */
  public  String  getXmdm(){ return super.get("xmdm");}
public void setXmdm(String  xmdm){ super.set("xmdm",xmdm);} /**
  * 行政区代码
  */
  public  String  getXzqdm(){ return super.get("xzqdm");}
public void setXzqdm(String  xzqdm){ super.set("xzqdm",xzqdm);} /**
  * 行政区名称
  */
  public  String  getXzqmc(){ return super.get("xzqmc");}
public void setXzqmc(String  xzqmc){ super.set("xzqmc",xzqmc);} /**
  * 要素代码
  */
  public  String  getYsdm(){ return super.get("ysdm");}
public void setYsdm(String  ysdm){ super.set("ysdm",ysdm);} /**
  * 坐标单位
  */
  public  String  getZbdw(){ return super.get("zbdw");}
public void setZbdw(String  zbdw){ super.set("zbdw",zbdw);} /**
  * 坐标系
  */
  public  String  getZbx(){ return super.get("zbx");}
public void setZbx(String  zbx){ super.set("zbx",zbx);} /**
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