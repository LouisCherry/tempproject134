package com.epoint.xmz.gtxstkjzd.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 国土踏勘界址点实体
 * 
 * @作者  1
 * @version [版本号, 2022-10-06 18:06:12]
 */
@Entity(table = "GTXS_TKJZD", id = "rowguid")
public class GtxsTkjzd extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

 /**
  * 备注
  */
  public  String  getBz(){ return super.get("bz");}
public void setBz(String  bz){ super.set("bz",bz);} /**
  * 地块编号
  */
  public  String  getDkbh(){ return super.get("dkbh");}
public void setDkbh(String  dkbh){ super.set("dkbh",dkbh);} /**
  * 点序号
  */
  public  String  getDxh(){ return super.get("dxh");}
public void setDxh(String  dxh){ super.set("dxh",dxh);} /**
  * 界址点号
  */
  public  String  getJzdh(){ return super.get("jzdh");}
public void setJzdh(String  jzdh){ super.set("jzdh",jzdh);} /**
  * 圈号
  */
  public  String  getQh(){ return super.get("qh");}
public void setQh(String  qh){ super.set("qh",qh);} /**
  * 返回内容
  */
  public  String  getRemark(){ return super.get("remark");}
public void setRemark(String  remark){ super.set("remark",remark);} /**
  * 状态
  */
  public  String  getStatus(){ return super.get("status");}
public void setStatus(String  status){ super.set("status",status);} /**
  * 踏勘ID
  */
  public  String  getTkid(){ return super.get("tkid");}
public void setTkid(String  tkid){ super.set("tkid",tkid);} /**
  * 项目代码
  */
  public  String  getXmdm(){ return super.get("xmdm");}
public void setXmdm(String  xmdm){ super.set("xmdm",xmdm);} /**
  * X坐标
  */
  public  Double  getXzb(){ return super.getDouble("xzb");}
public void setXzb(Double  xzb){ super.set("xzb",xzb);} /**
  * 要素代码
  */
  public  String  getYsdm(){ return super.get("ysdm");}
public void setYsdm(String  ysdm){ super.set("ysdm",ysdm);} /**
  * Y坐标
  */
  public  Double  getYzb(){ return super.getDouble("yzb");}
public void setYzb(Double  yzb){ super.set("yzb",yzb);} /**
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