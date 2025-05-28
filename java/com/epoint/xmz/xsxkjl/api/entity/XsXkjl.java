package com.epoint.xmz.xsxkjl.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 国土许可记录实体
 * 
 * @作者  1
 * @version [版本号, 2022-10-06 17:26:17]
 */
@Entity(table = "XS_XKJL", id = "rowguid")
public class XsXkjl extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

 /**
  * 电子监管号
  */
  public  String  getDzjgh(){ return super.get("dzjgh");}
public void setDzjgh(String  dzjgh){ super.set("dzjgh",dzjgh);} /**
  * 核减面积
  */
  public  Double  getHjmj(){ return super.getDouble("hjmj");}
public void setHjmj(Double  hjmj){ super.set("hjmj",hjmj);} /**
  * 建设单位
  */
  public  String  getJsdw(){ return super.get("jsdw");}
public void setJsdw(String  jsdw){ super.set("jsdw",jsdw);} /**
  * 建设规模
  */
  public  Double  getJsgm(){ return super.getDouble("jsgm");}
public void setJsgm(Double  jsgm){ super.set("jsgm",jsgm);} /**
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
  * 统一社会信用代码
  */
  public  String  getTyshxydm(){ return super.get("tyshxydm");}
public void setTyshxydm(String  tyshxydm){ super.set("tyshxydm",tyshxydm);} /**
  * 许可类型
  */
  public  String  getXklx(){ return super.get("xklx");}
public void setXklx(String  xklx){ super.set("xklx",xklx);} /**
  * 项目编号
  */
  public  String  getXmbh(){ return super.get("xmbh");}
public void setXmbh(String  xmbh){ super.set("xmbh",xmbh);} /**
  * 项目代码
  */
  public  String  getXmdm(){ return super.get("xmdm");}
public void setXmdm(String  xmdm){ super.set("xmdm",xmdm);} /**
  * 项目位置
  */
  public  String  getXmwz(){ return super.get("xmwz");}
public void setXmwz(String  xmwz){ super.set("xmwz",xmwz);} /**
  * 行政区代码
  */
  public  String  getXzqdm(){ return super.get("xzqdm");}
public void setXzqdm(String  xzqdm){ super.set("xzqdm",xzqdm);} /**
  * 行政区名称
  */
  public  String  getXzqmc(){ return super.get("xzqmc");}
public void setXzqmc(String  xzqmc){ super.set("xzqmc",xzqmc);} /**
  * 用地面积
  */
  public  Double  getYdmj(){ return super.getDouble("ydmj");}
public void setYdmj(Double  ydmj){ super.set("ydmj",ydmj);} /**
  * 要素代码
  */
  public  String  getYsdm(){ return super.get("ysdm");}
public void setYsdm(String  ysdm){ super.set("ysdm",ysdm);} /**
  * 预审单位
  */
  public  String  getYsdw(){ return super.get("ysdw");}
public void setYsdw(String  ysdw){ super.set("ysdw",ysdw);} /**
  * 预审级别
  */
  public  String  getYsjb(){ return super.get("ysjb");}
public void setYsjb(String  ysjb){ super.set("ysjb",ysjb);} /**
  * 预审批复名称
  */
  public  String  getYspfmc(){ return super.get("yspfmc");}
public void setYspfmc(String  yspfmc){ super.set("yspfmc",yspfmc);} /**
  * 预审批复文号
  */
  public  String  getYspfwh(){ return super.get("yspfwh");}
public void setYspfwh(String  yspfwh){ super.set("yspfwh",yspfwh);} /**
  * 预审日期
  */
  public  Date  getYsrq(){ return super.getDate("ysrq");}
public void setYsrq(Date  ysrq){ super.set("ysrq",ysrq);} /**
  * 预审行政区代码
  */
  public  String  getYsxzqdm(){ return super.get("ysxzqdm");}
public void setYsxzqdm(String  ysxzqdm){ super.set("ysxzqdm",ysxzqdm);} /**
  * 预审行政区名称
  */
  public  String  getYsxzqmc(){ return super.get("ysxzqmc");}
public void setYsxzqmc(String  ysxzqmc){ super.set("ysxzqmc",ysxzqmc);} /**
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