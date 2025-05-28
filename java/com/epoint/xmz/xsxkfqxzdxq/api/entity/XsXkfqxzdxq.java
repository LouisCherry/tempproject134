package com.epoint.xmz.xsxkfqxzdxq.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 国土许可分区县占地详情实体
 * 
 * @作者  1
 * @version [版本号, 2022-10-06 17:34:32]
 */
@Entity(table = "XS_XKFQXZDXQ", id = "rowguid")
public class XsXkfqxzdxq extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

 /**
  * 电子监管号
  */
  public  String  getDzjgh(){ return super.get("dzjgh");}
public void setDzjgh(String  dzjgh){ super.set("dzjgh",dzjgh);} /**
  * 备注
  */
  public  String  getRemark(){ return super.get("remark");}
public void setRemark(String  remark){ super.set("remark",remark);} /**
  * 状态
  */
  public  String  getStatus(){ return super.get("status");}
public void setStatus(String  status){ super.set("status",status);} /**
  * 围填海面积
  */
  public  Double  getWthmj(){ return super.getDouble("wthmj");}
public void setWthmj(Double  wthmj){ super.set("wthmj",wthmj);} /**
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
  * 用地面积
  */
  public  Double  getYdmj(){ return super.getDouble("ydmj");}
public void setYdmj(Double  ydmj){ super.set("ydmj",ydmj);} /**
  * 要素代码
  */
  public  String  getYsdm(){ return super.get("ysdm");}
public void setYsdm(String  ysdm){ super.set("ysdm",ysdm);} /**
  * 占用耕地面积
  */
  public  Double  getZygdmj(){ return super.getDouble("zygdmj");}
public void setZygdmj(Double  zygdmj){ super.set("zygdmj",zygdmj);} /**
  * 占用农用地面积
  */
  public  Double  getZynydmj(){ return super.getDouble("zynydmj");}
public void setZynydmj(Double  zynydmj){ super.set("zynydmj",zynydmj);} /**
  * 占用未利用地面积
  */
  public  Double  getZywlydmj(){ return super.getDouble("zywlydmj");}
public void setZywlydmj(Double  zywlydmj){ super.set("zywlydmj",zywlydmj);} /**
  * 占用永久基本农田面积
  */
  public  Double  getZyyjjbntmj(){ return super.getDouble("zyyjjbntmj");}
public void setZyyjjbntmj(Double  zyyjjbntmj){ super.set("zyyjjbntmj",zyyjjbntmj);} /**
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