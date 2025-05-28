package com.epoint.xmz.xstdflmj.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 国土土地分类面积实体
 * 
 * @作者  1
 * @version [版本号, 2022-10-06 17:18:56]
 */
@Entity(table = "XS_TDFLMJ", id = "rowguid")
public class XsTdflmj extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

 /**
  * 地类编码
  */
  public  String  getDlbm(){ return super.get("dlbm");}
public void setDlbm(String  dlbm){ super.set("dlbm",dlbm);} /**
  * 地类名称
  */
  public  String  getDlmc(){ return super.get("dlmc");}
public void setDlmc(String  dlmc){ super.set("dlmc",dlmc);} /**
  * 地类面积
  */
  public  String  getDlmj(){ return super.get("dlmj");}
public void setDlmj(String  dlmj){ super.set("dlmj",dlmj);} /**
  * 权属性质
  */
  public  String  getQsxz(){ return super.get("qsxz");}
public void setQsxz(String  qsxz){ super.set("qsxz",qsxz);} /**
  * 备注
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
  * 县级行政区代码
  */
  public  String  getXjxzqdm(){ return super.get("xjxzqdm");}
public void setXjxzqdm(String  xjxzqdm){ super.set("xjxzqdm",xjxzqdm);} /**
  * 县级行政区名称
  */
  public  String  getXjxzqmc(){ return super.get("xjxzqmc");}
public void setXjxzqmc(String  xjxzqmc){ super.set("xjxzqmc",xjxzqmc);} /**
  * 项目编号
  */
  public  String  getXmbh(){ return super.get("xmbh");}
public void setXmbh(String  xmbh){ super.set("xmbh",xmbh);} /**
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