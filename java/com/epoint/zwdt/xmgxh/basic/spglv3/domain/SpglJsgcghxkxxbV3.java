package com.epoint.zwdt.xmgxh.basic.spglv3.domain;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 建设工程规划许可信息表实体
 * 
 * @作者  Epoint
 * @version [版本号, 2023-11-01 14:51:00]
 */
@Entity(table = "SPGL_JSGCGHXKXXB_V3", id = "rowguid")
public class SpglJsgcghxkxxbV3 extends BaseEntity implements Cloneable
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
  public  String  getFtjfjmc(){ return super.get("ftjfjmc");}
public void setFtjfjmc(String  ftjfjmc){ super.set("ftjfjmc",ftjfjmc);} /**
  * 项目总平面图
  */
  public  String  getXmzpmt(){ return super.get("xmzpmt");}
public void setXmzpmt(String  xmzpmt){ super.set("xmzpmt",xmzpmt);} /**
  * 建设规模
  */
  public  String  getJsgm(){ return super.get("jsgm");}
public void setJsgm(String  jsgm){ super.set("jsgm",jsgm);} /**
  * 建设位置
  */
  public  String  getJswz(){ return super.get("jswz");}
public void setJswz(String  jswz){ super.set("jswz",jswz);} /**
  * 发证日期
  */
  public  Date  getFzrq(){ return super.getDate("fzrq");}
public void setFzrq(Date  fzrq){ super.set("fzrq",fzrq);} /**
  * 发证机关统一社会信用代码
  */
  public  String  getFzjgtyshxydm(){ return super.get("fzjgtyshxydm");}
public void setFzjgtyshxydm(String  fzjgtyshxydm){ super.set("fzjgtyshxydm",fzjgtyshxydm);} /**
  * 发证机关
  */
  public  String  getFzjg(){ return super.get("fzjg");}
public void setFzjg(String  fzjg){ super.set("fzjg",fzjg);} /**
  * 项目名称
  */
  public  String  getXmmc(){ return super.get("xmmc");}
public void setXmmc(String  xmmc){ super.set("xmmc",xmmc);} /**
  * 建设工程规划许可证编号
  */
  public  String  getGcghxkzbh(){ return super.get("gcghxkzbh");}
public void setGcghxkzbh(String  gcghxkzbh){ super.set("gcghxkzbh",gcghxkzbh);} /**
  * 建设用地规划许可证编号
  */
  public  String  getYdghxkzbh(){ return super.get("ydghxkzbh");}
public void setYdghxkzbh(String  ydghxkzbh){ super.set("ydghxkzbh",ydghxkzbh);} /**
  * 建设单位项目负责人联系电话
  */
  public  String  getJsfzrlxdh(){ return super.get("jsfzrlxdh");}
public void setJsfzrlxdh(String  jsfzrlxdh){ super.set("jsfzrlxdh",jsfzrlxdh);} /**
  * 建设单位项目负责人身份证件类型
  */
  public  Integer  getJsfzrzjlx(){ return super.getInt("jsfzrzjlx");}
public void setJsfzrzjlx(Integer  jsfzrzjlx){ super.set("jsfzrzjlx",jsfzrzjlx);} /**
  * 建设单位项目负责人身份证件号码
  */
  public  String  getJsfzrzjhm(){ return super.get("jsfzrzjhm");}
public void setJsfzrzjhm(String  jsfzrzjhm){ super.set("jsfzrzjhm",jsfzrzjhm);} /**
  * 建设单位项目负责人
  */
  public  String  getJsdwxmfzr(){ return super.get("jsdwxmfzr");}
public void setJsdwxmfzr(String  jsdwxmfzr){ super.set("jsdwxmfzr",jsdwxmfzr);} /**
  * 建设单位类型
  */
  public  Integer  getJsdwlx(){ return super.getInt("jsdwlx");}
public void setJsdwlx(Integer  jsdwlx){ super.set("jsdwlx",jsdwlx);} /**
  * 建设单位代码
  */
  public  String  getJsdwdm(){ return super.get("jsdwdm");}
public void setJsdwdm(String  jsdwdm){ super.set("jsdwdm",jsdwdm);} /**
  * 所属辖区号
  */
  public  String  getBelongxiaqucode(){ return super.get("belongxiaqucode");}
public void setBelongxiaqucode(String  belongxiaqucode){ super.set("belongxiaqucode",belongxiaqucode);} /**
  * 建设单位
  */
  public  String  getJsdw(){ return super.get("jsdw");}
public void setJsdw(String  jsdw){ super.set("jsdw",jsdw);} /**
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