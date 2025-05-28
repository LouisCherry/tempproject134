package com.epoint.xmz.xssbb.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 国土_申报表实体
 * 
 * @作者  1
 * @version [版本号, 2022-10-06 15:34:58]
 */
@Entity(table = "XS_SBB", id = "rowguid")
public class XsSbb extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

 /**
  * 备注
  */
  public  String  getBz(){ return super.get("bz");}
public void setBz(String  bz){ super.set("bz",bz);} /**
  * 行业分类
  */
  public  String  getHyfl(){ return super.get("hyfl");}
public void setHyfl(String  hyfl){ super.set("hyfl",hyfl);} /**
  * 跨行政区标识
  */
  public  String  getKxzqbs(){ return super.get("kxzqbs");}
public void setKxzqbs(String  kxzqbs){ super.set("kxzqbs",kxzqbs);} /**
  * 拟建设规模
  */
  public  Double  getNjsgm(){ return super.getDouble("njsgm");}
public void setNjsgm(Double  njsgm){ super.set("njsgm",njsgm);} /**
  * 拟选位置
  */
  public  String  getNxwz(){ return super.get("nxwz");}
public void setNxwz(String  nxwz){ super.set("nxwz",nxwz);} /**
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
  * 土地用途代码
  */
  public  String  getTdytdm(){ return super.get("tdytdm");}
public void setTdytdm(String  tdytdm){ super.set("tdytdm",tdytdm);} /**
  * 土地用途名称
  */
  public  String  getTdytmc(){ return super.get("tdytmc");}
public void setTdytmc(String  tdytmc){ super.set("tdytmc",tdytmc);} /**
  * 投资规模
  */
  public  Double  getTzgm(){ return super.getDouble("tzgm");}
public void setTzgm(Double  tzgm){ super.set("tzgm",tzgm);} /**
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
  * 占用建设用地面积
  */
  public  Double  getZyjsydmj(){ return super.getDouble("zyjsydmj");}
public void setZyjsydmj(Double  zyjsydmj){ super.set("zyjsydmj",zyjsydmj);} /**
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