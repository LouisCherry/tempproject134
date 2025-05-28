package com.epoint.auditsp.yqxm.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 项目审批事项办理详细信息表实体
 * 
 * @作者  fenglin
 * @version [版本号, 2019-07-18 10:39:18]
 */
@Entity(table = "ST_SPGL_XMSPSXBLXXXXB", id = "rowguid")
public class StSpglXmspsxblxxxxb extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

 /**
  * CREATETIMESTAMP
  */
  public  Date  getCreatetimestamp(){ return super.getDate("createtimestamp");}
public void setCreatetimestamp(Date  createtimestamp){ super.set("createtimestamp",createtimestamp);} /**
  * DATASOURCE
  */
  public  String  getDatasource(){ return super.get("datasource");}
public void setDatasource(String  datasource){ super.set("datasource",datasource);} /**
  * TIMESTAMP
  */
  public  Date  getTimestamp(){ return super.getDate("timestamp");}
public void setTimestamp(Date  timestamp){ super.set("timestamp",timestamp);} /**
  * SBYY
  */
  public  String  getSbyy(){ return super.get("sbyy");}
public void setSbyy(String  sbyy){ super.set("sbyy",sbyy);} /**
  * SJSCZT
  */
  public  Integer  getSjsczt(){ return super.getInt("sjsczt");}
public void setSjsczt(Integer  sjsczt){ super.set("sjsczt",sjsczt);} /**
  * SJWXYY
  */
  public  String  getSjwxyy(){ return super.get("sjwxyy");}
public void setSjwxyy(String  sjwxyy){ super.set("sjwxyy",sjwxyy);} /**
  * SJYXBS
  */
  public  Integer  getSjyxbs(){ return super.getInt("sjyxbs");}
public void setSjyxbs(Integer  sjyxbs){ super.set("sjyxbs",sjyxbs);} /**
  * BLSJ
  */
  public  Date  getBlsj(){ return super.getDate("blsj");}
public void setBlsj(Date  blsj){ super.set("blsj",blsj);} /**
  * BLYJ
  */
  public  String  getBlyj(){ return super.get("blyj");}
public void setBlyj(String  blyj){ super.set("blyj",blyj);} /**
  * BLZT
  */
  public  Integer  getBlzt(){ return super.getInt("blzt");}
public void setBlzt(Integer  blzt){ super.set("blzt",blzt);} /**
  * BLR
  */
  public  String  getBlr(){ return super.get("blr");}
public void setBlr(String  blr){ super.set("blr",blr);} /**
  * BLCS
  */
  public  String  getBlcs(){ return super.get("blcs");}
public void setBlcs(String  blcs){ super.set("blcs",blcs);} /**
  * SPSXSLBM
  */
  public  String  getSpsxslbm(){ return super.get("spsxslbm");}
public void setSpsxslbm(String  spsxslbm){ super.set("spsxslbm",spsxslbm);} /**
  * GCDM
  */
  public  String  getGcdm(){ return super.get("gcdm");}
public void setGcdm(String  gcdm){ super.set("gcdm",gcdm);} /**
  * XZQHDM
  */
  public  String  getXzqhdm(){ return super.get("xzqhdm");}
public void setXzqhdm(String  xzqhdm){ super.set("xzqhdm",xzqhdm);} /**
  * DFSJZJ
  */
  public  String  getDfsjzj(){ return super.get("dfsjzj");}
public void setDfsjzj(String  dfsjzj){ super.set("dfsjzj",dfsjzj);} /**
  * LSH
  */
  public  Integer  getLsh(){ return super.getInt("lsh");}
public void setLsh(Integer  lsh){ super.set("lsh",lsh);} /**
  * RowGuid
  */
  public  String  getRowguid(){ return super.get("rowguid");}
public void setRowguid(String  rowguid){ super.set("rowguid",rowguid);} /**
  * YearFlag
  */
  public  String  getYearflag(){ return super.get("yearflag");}
public void setYearflag(String  yearflag){ super.set("yearflag",yearflag);} /**
  * Row_ID
  */
  public  Integer  getRow_id(){ return super.getInt("row_id");}
public void setRow_id(Integer  row_id){ super.set("row_id",row_id);} /**
  * OperateDate
  */
  public  Date  getOperatedate(){ return super.getDate("operatedate");}
public void setOperatedate(Date  operatedate){ super.set("operatedate",operatedate);} /**
  * OperateUserName
  */
  public  String  getOperateusername(){ return super.get("operateusername");}
public void setOperateusername(String  operateusername){ super.set("operateusername",operateusername);} /**
  * BelongXiaQuCode
  */
  public  String  getBelongxiaqucode(){ return super.get("belongxiaqucode");}
public void setBelongxiaqucode(String  belongxiaqucode){ super.set("belongxiaqucode",belongxiaqucode);} /**
  * 审批流程类型
  */
  public  Integer  getSplclx(){ return super.getInt("splclx");}
public void setSplclx(Integer  splclx){ super.set("splclx",splclx);} /**
  * 省级审批流程类型
  */
  public  Integer  getSjsplclx(){ return super.getInt("sjsplclx");}
public void setSjsplclx(Integer  sjsplclx){ super.set("sjsplclx",sjsplclx);} /**
  * 省级数据抓取状态
  */
  public  Integer  getSjsjzqzt(){ return super.getInt("sjsjzqzt");}
public void setSjsjzqzt(Integer  sjsjzqzt){ super.set("sjsjzqzt",sjsjzqzt);} /**
  * 办件是否超期
  */
  public  Integer  getBjsfcq(){ return super.getInt("bjsfcq");}
public void setBjsfcq(Integer  bjsfcq){ super.set("bjsfcq",bjsfcq);}

}