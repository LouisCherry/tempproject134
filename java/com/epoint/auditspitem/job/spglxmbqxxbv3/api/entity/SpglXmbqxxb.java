package com.epoint.auditspitem.job.spglxmbqxxbv3.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 项目标签信息表实体
 * 
 * @作者  17919
 * @version [版本号, 2024-07-10 11:03:37]
 */
@Entity(table = "spgl_xmbqxxb", id = "rowguid")
public class SpglXmbqxxb extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

 /**
  * 标签名称
  */
  public  String  getBqmc(){ return super.get("bqmc");}
public void setBqmc(String  bqmc){ super.set("bqmc",bqmc);} /**
  * 标签项
  */
  public  String  getBqx(){ return super.get("bqx");}
public void setBqx(String  bqx){ super.set("bqx",bqx);} /**
  * 备注
  */
  public  String  getBz(){ return super.get("bz");}
public void setBz(String  bz){ super.set("bz",bz);} /**
  * 地方数据主键
  */
  public  String  getDfsjzj(){ return super.get("dfsjzj");}
public void setDfsjzj(String  dfsjzj){ super.set("dfsjzj",dfsjzj);} /**
  * 流水号
  */
  public  String  getLsh(){ return super.get("lsh");}
public void setLsh(String  lsh){ super.set("lsh",lsh);} /**
  * sbyy
  */
  public  String  getSbyy(){ return super.get("sbyy");}
public void setSbyy(String  sbyy){ super.set("sbyy",sbyy);} /**
  * 数据上传状态
  */
  public  String  getSjsczt(){ return super.get("sjsczt");}
public void setSjsczt(String  sjsczt){ super.set("sjsczt",sjsczt);} /**
  * 数据无效原因
  */
  public  String  getSjwxyy(){ return super.get("sjwxyy");}
public void setSjwxyy(String  sjwxyy){ super.set("sjwxyy",sjwxyy);} /**
  * 数据有效标识
  */
  public  String  getSjyxbs(){ return super.get("sjyxbs");}
public void setSjyxbs(String  sjyxbs){ super.set("sjyxbs",sjyxbs);} /**
  * sync
  */
  public  String  getSync(){ return super.get("sync");}
public void setSync(String  sync){ super.set("sync",sync);} /**
  * 统计年份
  */
  public  String  getTjnf(){ return super.get("tjnf");}
public void setTjnf(String  tjnf){ super.set("tjnf",tjnf);} /**
  * 项目代码
  */
  public  String  getXmdm(){ return super.get("xmdm");}
public void setXmdm(String  xmdm){ super.set("xmdm",xmdm);} /**
  * 行政区划代码
  */
  public  String  getXzqhdm(){ return super.get("xzqhdm");}
public void setXzqhdm(String  xzqhdm){ super.set("xzqhdm",xzqhdm);} /**
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