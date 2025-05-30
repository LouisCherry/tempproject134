package com.epoint.auditrsitembaseinfo.auditrsitembaseinfoone.domain;
import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 泰安建设项目第一阶段基本信息拓展表实体
 * 
 * @作者  wangxiaolong
 * @version [版本号, 2019-08-05 14:21:41]
 */
@Entity(table = "audit_rs_item_baseinfo_one", id = "rowguid")
public class AuditRsItemBaseinfoOne extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

 /**
  * 所属辖区号
  */
  public  String  getBelongxiaqucode(){ return super.get("belongxiaqucode");}
public void setBelongxiaqucode(String  belongxiaqucode){ super.set("belongxiaqucode",belongxiaqucode);} /**
  * 操作者名字
  */
  public  String  getOperateusername(){ return super.get("operateusername");}
public void setOperateusername(String  operateusername){ super.set("operateusername",operateusername);} /**
  * 操作日期
  */
  public  Date  getOperatedate(){ return super.getDate("operatedate");}
public void setOperatedate(Date  operatedate){ super.set("operatedate",operatedate);} /**
  * 序号
  */
  public  Integer  getRow_id(){ return super.getInt("row_id");}
public void setRow_id(Integer  row_id){ super.set("row_id",row_id);} /**
  * 年份标识
  */
  public  String  getYearflag(){ return super.get("yearflag");}
public void setYearflag(String  yearflag){ super.set("yearflag",yearflag);} /**
  * 默认主键字段
  */
  public  String  getRowguid(){ return super.get("rowguid");}
public void setRowguid(String  rowguid){ super.set("rowguid",rowguid);} /**
  * 主表guid
  */
  public  String  getParentid(){ return super.get("parentid");}
public void setParentid(String  parentid){ super.set("parentid",parentid);} /**
  * 重点项目
  */
  public  String  getZdxm(){ return super.get("zdxm");}
public void setZdxm(String  zdxm){ super.set("zdxm",zdxm);} /**
  * 项目地址
  */
  public  String  getXmdz(){ return super.get("xmdz");}
public void setXmdz(String  xmdz){ super.set("xmdz",xmdz);} /**
  * 冬至
  */
  public  String  getDz(){ return super.get("dz");}
public void setDz(String  dz){ super.set("dz",dz);} /**
  * 西至
  */
  public  String  getXz(){ return super.get("xz");}
public void setXz(String  xz){ super.set("xz",xz);} /**
  * 南至
  */
  public  String  getNz(){ return super.get("nz");}
public void setNz(String  nz){ super.set("nz",nz);} /**
  * 北至
  */
  public  String  getBz(){ return super.get("bz");}
public void setBz(String  bz){ super.set("bz",bz);} /**
  * 立项类型
  */
  public  String  getLxlx(){ return super.get("lxlx");}
public void setLxlx(String  lxlx){ super.set("lxlx",lxlx);} /**
  * 立项部门
  */
  public  String  getLxbm(){ return super.get("lxbm");}
public void setLxbm(String  lxbm){ super.set("lxbm",lxbm);} /**
  * 行业类别
  */
  public  String  getHylb(){ return super.get("hylb");}
public void setHylb(String  hylb){ super.set("hylb",hylb);} /**
  * 规划用地性质及现状
  */
  public  String  getGhydxzjxz(){ return super.get("ghydxzjxz");}
public void setGhydxzjxz(String  ghydxzjxz){ super.set("ghydxzjxz",ghydxzjxz);} /**
  * 耕地（基本农田）
  */
  public  String  getGd(){ return super.get("gd");}
public void setGd(String  gd){ super.set("gd",gd);} /**
  * 建设类型
  */
  public  String  getJslx(){ return super.get("jslx");}
public void setJslx(String  jslx){ super.set("jslx",jslx);} /**
  * 地上建筑面积
  */
  public  String  getDsjzmj(){ return super.get("dsjzmj");}
public void setDsjzmj(String  dsjzmj){ super.set("dsjzmj",dsjzmj);} /**
  * 地下建筑面积
  */
  public  String  getDxjzmj(){ return super.get("dxjzmj");}
public void setDxjzmj(String  dxjzmj){ super.set("dxjzmj",dxjzmj);} /**
  * 新增年综合能源消费量吨标煤（当量值）
  */
  public  String  getXznzhnyxh(){ return super.get("xznzhnyxh");}
public void setXznzhnyxh(String  xznzhnyxh){ super.set("xznzhnyxh",xznzhnyxh);} /**
  * 新增年用电量（万千瓦时）
  */
  public  String  getXznydl(){ return super.get("xznydl");}
public void setXznydl(String  xznydl){ super.set("xznydl",xznydl);} /**
  * 支持性文件
  */
  public  String  getZcxwj(){ return super.get("zcxwj");}
public void setZcxwj(String  zcxwj){ super.set("zcxwj",zcxwj);} /**
  * 建设用地
  */
  public  String  getJsyd(){ return super.get("jsyd");}
public void setJsyd(String  jsyd){ super.set("jsyd",jsyd);} /**
  * 未利用地
  */
  public  String  getWlyd(){ return super.get("wlyd");}
public void setWlyd(String  wlyd){ super.set("wlyd",wlyd);}
/**
 * 未利用地
 */
 public  String  getSubappname(){ return super.get("subappname");}
public void setSubappname(String  subappname){ super.set("subappname",subappname);}

}