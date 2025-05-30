package com.epoint.zwdt.xmgxh.basic.spglv3.domain;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 建设用地规划许可信息表实体
 * 
 * @作者  Epoint
 * @version [版本号, 2023-11-01 14:34:54]
 */
@Entity(table = "SPGL_JSYDGHXKXXB_V3", id = "rowguid")
public class SpglJsydghxkxxbV3 extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

 /**
  * 失败原因
  */
  public  String  getSbyy(){ return super.get("sbyy");}
public void setSbyy(String  sbyy){ super.set("sbyy",sbyy);} /**
  * 数据上传状态
  */
  public  Integer  getSjsczt(){ return super.getInt("sjsczt");}
public void setSjsczt(Integer  sjsczt){ super.set("sjsczt",sjsczt);} /**
  * 数据无效原因
  */
  public  String  getSjwxyy(){ return super.get("sjwxyy");}
public void setSjwxyy(String  sjwxyy){ super.set("sjwxyy",sjwxyy);} /**
  * 数据有效标识
  */
  public  Integer  getSjyxbs(){ return super.getInt("sjyxbs");}
public void setSjyxbs(Integer  sjyxbs){ super.set("sjyxbs",sjyxbs);} /**
  * 联系人手机号
  */
  public  String  getLxrsjh(){ return super.get("lxrsjh");}
public void setLxrsjh(String  lxrsjh){ super.set("lxrsjh",lxrsjh);} /**
  * 联系人/代理人
  */
  public  String  getLxr(){ return super.get("lxr");}
public void setLxr(String  lxr){ super.set("lxr",lxr);} /**
  * 附图及附件名称
  */
  public  String  getFjmc(){ return super.get("fjmc");}
public void setFjmc(String  fjmc){ super.set("fjmc",fjmc);} /**
  * 土地获取方式
  */
  public  String  getTdhqfs(){ return super.get("tdhqfs");}
public void setTdhqfs(String  tdhqfs){ super.set("tdhqfs",tdhqfs);} /**
  * 建设规模
  */
  public  String  getJsgm(){ return super.get("jsgm");}
public void setJsgm(String  jsgm){ super.set("jsgm",jsgm);} /**
  * 土地用途
  */
  public  String  getTdyt(){ return super.get("tdyt");}
public void setTdyt(String  tdyt){ super.set("tdyt",tdyt);} /**
  * 用地面积
  */
  public  Double  getYdmj(){ return super.getDouble("ydmj");}
public void setYdmj(Double  ydmj){ super.set("ydmj",ydmj);} /**
  * 用地位置
  */
  public  String  getYdwz(){ return super.get("ydwz");}
public void setYdwz(String  ydwz){ super.set("ydwz",ydwz);} /**
  * 批准用地文号
  */
  public  String  getPzydwh(){ return super.get("pzydwh");}
public void setPzydwh(String  pzydwh){ super.set("pzydwh",pzydwh);} /**
  * 发证日期
  */
  public  Date  getFzrq(){ return super.getDate("fzrq");}
public void setFzrq(Date  fzrq){ super.set("fzrq",fzrq);} /**
  * 批准用地机关
  */
  public  String  getPzydjg(){ return super.get("pzydjg");}
public void setPzydjg(String  pzydjg){ super.set("pzydjg",pzydjg);} /**
  * 发证机关统一社会信用代码
  */
  public  String  getFzjgtyshxydm(){ return super.get("fzjgtyshxydm");}
public void setFzjgtyshxydm(String  fzjgtyshxydm){ super.set("fzjgtyshxydm",fzjgtyshxydm);} /**
  * 发证机关
  */
  public  String  getFzjg(){ return super.get("fzjg");}
public void setFzjg(String  fzjg){ super.set("fzjg",fzjg);} /**
  * 用地单位统一社会信用代码
  */
  public  String  getYdtyshxydm(){ return super.get("ydtyshxydm");}
public void setYdtyshxydm(String  ydtyshxydm){ super.set("ydtyshxydm",ydtyshxydm);} /**
  * 用地单位
  */
  public  String  getYddw(){ return super.get("yddw");}
public void setYddw(String  yddw){ super.set("yddw",yddw);} /**
  * 项目名称
  */
  public  String  getXmmc(){ return super.get("xmmc");}
public void setXmmc(String  xmmc){ super.set("xmmc",xmmc);} /**
  * 所属辖区号
  */
  public  String  getBelongxiaqucode(){ return super.get("belongxiaqucode");}
public void setBelongxiaqucode(String  belongxiaqucode){ super.set("belongxiaqucode",belongxiaqucode);} /**
  * 建设用地规划许可证编号
  */
  public  String  getYdghxkzbh(){ return super.get("ydghxkzbh");}
public void setYdghxkzbh(String  ydghxkzbh){ super.set("ydghxkzbh",ydghxkzbh);} /**
  * 操作者名字
  */
  public  String  getOperateusername(){ return super.get("operateusername");}
public void setOperateusername(String  operateusername){ super.set("operateusername",operateusername);} /**
  * 审批事项实例编码
  */
  public  String  getSpsxslbm(){ return super.get("spsxslbm");}
public void setSpsxslbm(String  spsxslbm){ super.set("spsxslbm",spsxslbm);} /**
  * 操作日期
  */
  public  Date  getOperatedate(){ return super.getDate("operatedate");}
public void setOperatedate(Date  operatedate){ super.set("operatedate",operatedate);} /**
  * 工程代码
  */
  public  String  getGcdm(){ return super.get("gcdm");}
public void setGcdm(String  gcdm){ super.set("gcdm",gcdm);} /**
  * 序号
  */
  public  Integer  getRow_id(){ return super.getInt("row_id");}
public void setRow_id(Integer  row_id){ super.set("row_id",row_id);} /**
  * 行政区划代码
  */
  public  String  getXzqhdm(){ return super.get("xzqhdm");}
public void setXzqhdm(String  xzqhdm){ super.set("xzqhdm",xzqhdm);} /**
  * 年份标识
  */
  public  String  getYearflag(){ return super.get("yearflag");}
public void setYearflag(String  yearflag){ super.set("yearflag",yearflag);} /**
  * 地方数据主键
  */
  public  String  getDfsjzj(){ return super.get("dfsjzj");}
public void setDfsjzj(String  dfsjzj){ super.set("dfsjzj",dfsjzj);} /**
  * 默认主键字段
  */
  public  String  getRowguid(){ return super.get("rowguid");}
public void setRowguid(String  rowguid){ super.set("rowguid",rowguid);} /**
  * 流水号
  */
  public  Integer  getLsh(){ return super.getInt("lsh");}
public void setLsh(Integer  lsh){ super.set("lsh",lsh);}

}