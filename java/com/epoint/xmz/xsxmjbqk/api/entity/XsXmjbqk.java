package com.epoint.xmz.xsxmjbqk.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 国土项目基本情况实体
 * 
 * @作者  1
 * @version [版本号, 2022-10-06 15:46:23]
 */
@Entity(table = "XS_XMJBQK", id = "rowguid")
public class XsXmjbqk extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

 /**
  * 跨行政区标识
  */
  public  String  getKxzqbs(){ return super.get("kxzqbs");}
public void setKxzqbs(String  kxzqbs){ super.set("kxzqbs",kxzqbs);} /**
  * 其他说明
  */
  public  String  getQtsm(){ return super.get("qtsm");}
public void setQtsm(String  qtsm){ super.set("qtsm",qtsm);} /**
  * 备注
  */
  public  String  getRemark(){ return super.get("remark");}
public void setRemark(String  remark){ super.set("remark",remark);} /**
  * 状态
  */
  public  String  getStatus(){ return super.get("status");}
public void setStatus(String  status){ super.set("status",status);} /**
  * 所在省份名称
  */
  public  String  getSzsfmc(){ return super.get("szsfmc");}
public void setSzsfmc(String  szsfmc){ super.set("szsfmc",szsfmc);} /**
  * 项目编号
  */
  public  String  getXmbh(){ return super.get("xmbh");}
public void setXmbh(String  xmbh){ super.set("xmbh",xmbh);} /**
  * 项目代码
  */
  public  String  getXmdm(){ return super.get("xmdm");}
public void setXmdm(String  xmdm){ super.set("xmdm",xmdm);} /**
  * 项目名称
  */
  public  String  getXmmc(){ return super.get("xmmc");}
public void setXmmc(String  xmmc){ super.set("xmmc",xmmc);} /**
  * 项目批准机关
  */
  public  String  getXmpzjg(){ return super.get("xmpzjg");}
public void setXmpzjg(String  xmpzjg){ super.set("xmpzjg",xmpzjg);} /**
  * 项目批准类型
  */
  public  String  getXmpzlx(){ return super.get("xmpzlx");}
public void setXmpzlx(String  xmpzlx){ super.set("xmpzlx",xmpzlx);} /**
  * 所在行政区代码
  */
  public  String  getXmszxzqdm(){ return super.get("xmszxzqdm");}
public void setXmszxzqdm(String  xmszxzqdm){ super.set("xmszxzqdm",xmszxzqdm);} /**
  * 项目占用土地规模
  */
  public  Double  getXmzytdgm(){ return super.getDouble("xmzytdgm");}
public void setXmzytdgm(Double  xmzytdgm){ super.set("xmzytdgm",xmzytdgm);} /**
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