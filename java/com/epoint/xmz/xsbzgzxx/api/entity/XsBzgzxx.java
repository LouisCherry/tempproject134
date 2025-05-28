package com.epoint.xmz.xsbzgzxx.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 国土补正告知实体
 * 
 * @作者  1
 * @version [版本号, 2022-10-06 17:26:12]
 */
@Entity(table = "XS_BZGZXX", id = "rowguid")
public class XsBzgzxx extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

 /**
  * 补正告知日期
  */
  public  Date  getBzgzrq(){ return super.getDate("bzgzrq");}
public void setBzgzrq(Date  bzgzrq){ super.set("bzgzrq",bzgzrq);} /**
  * 补正内容
  */
  public  String  getBznr(){ return super.get("bznr");}
public void setBznr(String  bznr){ super.set("bznr",bznr);} /**
  * 补正序号
  */
  public  String  getBzxh(){ return super.get("bzxh");}
public void setBzxh(String  bzxh){ super.set("bzxh",bzxh);} /**
  * 补正原因
  */
  public  String  getBzyy(){ return super.get("bzyy");}
public void setBzyy(String  bzyy){ super.set("bzyy",bzyy);} /**
  * 附件路径
  */
  public  String  getFjlj(){ return super.get("fjlj");}
public void setFjlj(String  fjlj){ super.set("fjlj",fjlj);} /**
  * 核减面积
  */
  public  Double  getHjmj(){ return super.getDouble("hjmj");}
public void setHjmj(Double  hjmj){ super.set("hjmj",hjmj);} /**
  * 备注
  */
  public  String  getRemark(){ return super.get("remark");}
public void setRemark(String  remark){ super.set("remark",remark);} /**
  * 状态
  */
  public  String  getStatus(){ return super.get("status");}
public void setStatus(String  status){ super.set("status",status);} /**
  * 项目编号
  */
  public  String  getXmbh(){ return super.get("xmbh");}
public void setXmbh(String  xmbh){ super.set("xmbh",xmbh);} /**
  * 项目代码
  */
  public  String  getXmdm(){ return super.get("xmdm");}
public void setXmdm(String  xmdm){ super.set("xmdm",xmdm);} /**
  * 响应截止日期
  */
  public  Date  getXyjzrq(){ return super.getDate("xyjzrq");}
public void setXyjzrq(Date  xyjzrq){ super.set("xyjzrq",xyjzrq);} /**
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