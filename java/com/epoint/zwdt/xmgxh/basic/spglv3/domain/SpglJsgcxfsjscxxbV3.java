package com.epoint.zwdt.xmgxh.basic.spglv3.domain;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 建设工程消防设计审查信息表实体
 * 
 * @作者  Epoint
 * @version [版本号, 2023-09-25 14:00:20]
 */
@Entity(table = "SPGL_JSGCXFSJSCXXB_V3", id = "rowguid")
public class SpglJsgcxfsjscxxbV3 extends BaseEntity implements Cloneable
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
  * 流水号
  */
  public  Integer  getLsh(){ return super.getInt("lsh");}
public void setLsh(Integer  lsh){ super.set("lsh",lsh);} /**
  * 地方数据主键
  */
  public  String  getDfsjzj(){ return super.get("dfsjzj");}
public void setDfsjzj(String  dfsjzj){ super.set("dfsjzj",dfsjzj);} /**
  * 行政区划代码
  */
  public  String  getXzqhdm(){ return super.get("xzqhdm");}
public void setXzqhdm(String  xzqhdm){ super.set("xzqhdm",xzqhdm);} /**
  * 项目代码
  */
  public  String  getXmdm(){ return super.get("xmdm");}
public void setXmdm(String  xmdm){ super.set("xmdm",xmdm);} /**
  * 工程代码
  */
  public  String  getGcdm(){ return super.get("gcdm");}
public void setGcdm(String  gcdm){ super.set("gcdm",gcdm);} /**
  * 审批事项实例编码
  */
  public  String  getSpsxslbm(){ return super.get("spsxslbm");}
public void setSpsxslbm(String  spsxslbm){ super.set("spsxslbm",spsxslbm);} /**
  * 建设单位
  */
  public  String  getJsdw(){ return super.get("jsdw");}
public void setJsdw(String  jsdw){ super.set("jsdw",jsdw);} /**
  * 建设单位代码
  */
  public  String  getJsdwdm(){ return super.get("jsdwdm");}
public void setJsdwdm(String  jsdwdm){ super.set("jsdwdm",jsdwdm);} /**
  * 建设单位类型
  */
  public  Integer  getJsdwlx(){ return super.getInt("jsdwlx");}
public void setJsdwlx(Integer  jsdwlx){ super.set("jsdwlx",jsdwlx);} /**
  * 建设单位项目负责人
  */
  public  String  getJsdwxmfzr(){ return super.get("jsdwxmfzr");}
public void setJsdwxmfzr(String  jsdwxmfzr){ super.set("jsdwxmfzr",jsdwxmfzr);} /**
  * 建设单位项目负责人身份证件号码
  */
  public  String  getJsfzrzjhm(){ return super.get("jsfzrzjhm");}
public void setJsfzrzjhm(String  jsfzrzjhm){ super.set("jsfzrzjhm",jsfzrzjhm);} /**
  * 建设单位项目负责人身份证件类型
  */
  public  Integer  getJsfzrzjlx(){ return super.getInt("jsfzrzjlx");}
public void setJsfzrzjlx(Integer  jsfzrzjlx){ super.set("jsfzrzjlx",jsfzrzjlx);} /**
  * 建设单位项目负责人联系电话
  */
  public  String  getJsfzrlxdh(){ return super.get("jsfzrlxdh");}
public void setJsfzrlxdh(String  jsfzrlxdh){ super.set("jsfzrlxdh",jsfzrlxdh);} /**
  * 工程名称
  */
  public  String  getGcmc(){ return super.get("gcmc");}
public void setGcmc(String  gcmc){ super.set("gcmc",gcmc);} /**
  * 申请日期
  */
  public  Date  getSqrq(){ return super.getDate("sqrq");}
public void setSqrq(Date  sqrq){ super.set("sqrq",sqrq);} /**
  * 工程地址
  */
  public  String  getGcdz(){ return super.get("gcdz");}
public void setGcdz(String  gcdz){ super.set("gcdz",gcdz);} /**
  * 建设性质
  */
  public  Integer  getJsxz(){ return super.getInt("jsxz");}
public void setJsxz(Integer  jsxz){ super.set("jsxz",jsxz);} /**
  * 工程投资额
  */
  public  Double  getGctze(){ return super.getDouble("gctze");}
public void setGctze(Double  gctze){ super.set("gctze",gctze);} /**
  * 总建筑面积
  */
  public  Double  getZjzmj(){ return super.getDouble("zjzmj");}
public void setZjzmj(Double  zjzmj){ super.set("zjzmj",zjzmj);} /**
  * 立项批复（核准、备案）文件
  */
  public  String  getLxpfwj(){ return super.get("lxpfwj");}
public void setLxpfwj(String  lxpfwj){ super.set("lxpfwj",lxpfwj);} /**
  * 立项批复（核准、备案）日期
  */
  public  Date  getLxpfrq(){ return super.getDate("lxpfrq");}
public void setLxpfrq(Date  lxpfrq){ super.set("lxpfrq",lxpfrq);} /**
  * 用地规划许可文件（依法需要办理的）
  */
  public  String  getYdghxkwj(){ return super.get("ydghxkwj");}
public void setYdghxkwj(String  ydghxkwj){ super.set("ydghxkwj",ydghxkwj);} /**
  * 用地规划许可日期
  */
  public  Date  getYdghxkrq(){ return super.getDate("ydghxkrq");}
public void setYdghxkrq(Date  ydghxkrq){ super.set("ydghxkrq",ydghxkrq);} /**
  * 建设工程规划许可文件（依法需要办理的）
  */
  public  String  getGcghxkzbh(){ return super.get("gcghxkzbh");}
public void setGcghxkzbh(String  gcghxkzbh){ super.set("gcghxkzbh",gcghxkzbh);} /**
  * 建设工程规划许可日期
  */
  public  Date  getGcghxkrq(){ return super.getDate("gcghxkrq");}
public void setGcghxkrq(Date  gcghxkrq){ super.set("gcghxkrq",gcghxkrq);} /**
  * 临时性建筑批准文件（依法需要办理的）
  */
  public  String  getLsxjzpzwj(){ return super.get("lsxjzpzwj");}
public void setLsxjzpzwj(String  lsxjzpzwj){ super.set("lsxjzpzwj",lsxjzpzwj);} /**
  * 临时性建筑批准日期（依法需要办理的）
  */
  public  Date  getLsxjzpzrq(){ return super.getDate("lsxjzpzrq");}
public void setLsxjzpzrq(Date  lsxjzpzrq){ super.set("lsxjzpzrq",lsxjzpzrq);} /**
  * 特殊消防设计
  */
  public  Integer  getTsxfsj(){ return super.getInt("tsxfsj");}
public void setTsxfsj(Integer  tsxfsj){ super.set("tsxfsj",tsxfsj);} /**
  * 是否建筑高度大于 250m 的建筑采取加强性消防设计措施
  */
  public  Integer  getSfjqxxf(){ return super.getInt("sfjqxxf");}
public void setSfjqxxf(Integer  sfjqxxf){ super.set("sfjqxxf",sfjqxxf);} /**
  * 特殊建设工程情形
  */
  public  String  getTsjsgcqx(){ return super.get("tsjsgcqx");}
public void setTsjsgcqx(String  tsjsgcqx){ super.set("tsjsgcqx",tsjsgcqx);} /**
  * 是否包含装饰装修工程
  */
  public  Integer  getSfbhzxzsgc(){ return super.getInt("sfbhzxzsgc");}
public void setSfbhzxzsgc(Integer  sfbhzxzsgc){ super.set("sfbhzxzsgc",sfbhzxzsgc);} /**
  * 装饰装修部位
  */
  public  String  getZxbw(){ return super.get("zxbw");}
public void setZxbw(String  zxbw){ super.set("zxbw",zxbw);} /**
  * 装修面积（平方米）
  */
  public  Double  getZxmj(){ return super.getDouble("zxmj");}
public void setZxmj(Double  zxmj){ super.set("zxmj",zxmj);} /**
  * 装修所在层数
  */
  public  String  getZxszcs(){ return super.get("zxszcs");}
public void setZxszcs(String  zxszcs){ super.set("zxszcs",zxszcs);} /**
  * 是否改变用途
  */
  public  Integer  getSfgbyt(){ return super.getInt("sfgbyt");}
public void setSfgbyt(Integer  sfgbyt){ super.set("sfgbyt",sfgbyt);} /**
  * 原有用途
  */
  public  String  getYyyt(){ return super.get("yyyt");}
public void setYyyt(String  yyyt){ super.set("yyyt",yyyt);} /**
  * 是否使用保温材料
  */
  public  Integer  getSfsybwcl(){ return super.getInt("sfsybwcl");}
public void setSfsybwcl(Integer  sfsybwcl){ super.set("sfsybwcl",sfsybwcl);} /**
  * 建筑保温材料类别
  */
  public  String  getBwcllb(){ return super.get("bwcllb");}
public void setBwcllb(String  bwcllb){ super.set("bwcllb",bwcllb);} /**
  * 保温所在层数
  */
  public  String  getBwszcs(){ return super.get("bwszcs");}
public void setBwszcs(String  bwszcs){ super.set("bwszcs",bwszcs);} /**
  * 保温部位
  */
  public  String  getBwbw(){ return super.get("bwbw");}
public void setBwbw(String  bwbw){ super.set("bwbw",bwbw);} /**
  * 保温材料
  */
  public  String  getBwcl(){ return super.get("bwcl");}
public void setBwcl(String  bwcl){ super.set("bwcl",bwcl);} /**
  * 消防设施及其他
  */
  public  String  getXfcsjqt(){ return super.get("xfcsjqt");}
public void setXfcsjqt(String  xfcsjqt){ super.set("xfcsjqt",xfcsjqt);} /**
  * 工程简要说明
  */
  public  String  getGcjysm(){ return super.get("gcjysm");}
public void setGcjysm(String  gcjysm){ super.set("gcjysm",gcjysm);} /**
  * 审查单位
  */
  public  String  getScdw(){ return super.get("scdw");}
public void setScdw(String  scdw){ super.set("scdw",scdw);} /**
  * 审查日期
  */
  public  Date  getScrq(){ return super.getDate("scrq");}
public void setScrq(Date  scrq){ super.set("scrq",scrq);} /**
  * 审查意见
  */
  public  String  getScyj(){ return super.get("scyj");}
public void setScyj(String  scyj){ super.set("scyj",scyj);} /**
  * 存在问题
  */
  public  String  getCzwt(){ return super.get("czwt");}
public void setCzwt(String  czwt){ super.set("czwt",czwt);} /**
  * 联系人/代理人
  */
  public  String  getLxr(){ return super.get("lxr");}
public void setLxr(String  lxr){ super.set("lxr",lxr);} /**
  * 联系人手机号
  */
  public  String  getLxrsjh(){ return super.get("lxrsjh");}
public void setLxrsjh(String  lxrsjh){ super.set("lxrsjh",lxrsjh);} /**
  * 数据有效标识
  */
  public  Integer  getSjyxbs(){ return super.getInt("sjyxbs");}
public void setSjyxbs(Integer  sjyxbs){ super.set("sjyxbs",sjyxbs);} /**
  * 数据无效原因
  */
  public  String  getSjwxyy(){ return super.get("sjwxyy");}
public void setSjwxyy(String  sjwxyy){ super.set("sjwxyy",sjwxyy);} /**
  * 数据上传状态
  */
  public  Integer  getSjsczt(){ return super.getInt("sjsczt");}
public void setSjsczt(Integer  sjsczt){ super.set("sjsczt",sjsczt);} /**
  * 失败原因
  */
  public  String  getSbyy(){ return super.get("sbyy");}
public void setSbyy(String  sbyy){ super.set("sbyy",sbyy);} /**
  * 是否上报
  */
  public  String  getSync(){ return super.get("sync");}
public void setSync(String  sync){ super.set("sync",sync);}

}