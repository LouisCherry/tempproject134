package com.epoint.qfzwfw.auditznsbyyzzinfo.api.entity;
import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 智能设备营业执照表实体
 * 
 * @作者  LIUCTT
 * @version [版本号, 2018-06-07 09:51:27]
 */
@Entity(table = "Audit_ZNSB_YYZZ_Info", id = "rowguid")
public class AuditZnsbYyzzInfo extends BaseEntity implements Cloneable
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
  * 统一社会信用代码
  */
  public  String  getTyshxydm(){ return super.get("tyshxydm");}
public void setTyshxydm(String  tyshxydm){ super.set("tyshxydm",tyshxydm);} /**
  * 营业范围
  */
  public  String  getRangen(){ return super.get("rangen");}
public void setRangen(String  rangen){ super.set("rangen",rangen);} /**
  * 注册资本
  */
  public  String  getZczb(){ return super.get("zczb");}
public void setZczb(String  zczb){ super.set("zczb",zczb);} /**
  * 企业中文名
  */
  public  String  getQyname(){ return super.get("qyname");}
public void setQyname(String  qyname){ super.set("qyname",qyname);} /**
  * 法人
  */
  public  String  getLegal(){ return super.get("legal");}
public void setLegal(String  legal){ super.set("legal",legal);} /**
  * 住所
  */
  public  String  getAddress(){ return super.get("address");}
public void setAddress(String  address){ super.set("address",address);} /**
  * 企业类型
  */
  public  String  getQytype(){ return super.get("qytype");}
public void setQytype(String  qytype){ super.set("qytype",qytype);} /**
  * 负责人
  */
  public  String  getRpreson(){ return super.get("rpreson");}
public void setRpreson(String  rpreson){ super.set("rpreson",rpreson);} /**
  * 成立日期
  */
  public  Date  getCldate(){ return super.getDate("cldate");}
public void setCldate(Date  cldate){ super.set("cldate",cldate);} /**
  * 核准日期
  */
  public  Date  getHzdate(){ return super.getDate("hzdate");}
public void setHzdate(Date  hzdate){ super.set("hzdate",hzdate);} /**
  * 营业期限自
  */
  public  Date  getFromdate(){ return super.getDate("fromdate");}
public void setFromdate(Date  fromdate){ super.set("fromdate",fromdate);} /**
  * 营业期限至
  */
  public  Date  getTodate(){ return super.getDate("todate");}
public void setTodate(Date  todate){ super.set("todate",todate);} /**
  * 负责人证件号码
  */
  public  String  getIdcard(){ return super.get("idcard");}
public void setIdcard(String  idcard){ super.set("idcard",idcard);} /**
  * 负责人手机号
  */
  public  String  getFzrmobile(){ return super.get("fzrmobile");}
public void setFzrmobile(String  fzrmobile){ super.set("fzrmobile",fzrmobile);} /**
  * 代理人手机号
  */
  public  String  getDlrmobile(){ return super.get("dlrmobile");}
public void setDlrmobile(String  dlrmobile){ super.set("dlrmobile",dlrmobile);} /**
  * 是否已打印
  */
  public  String  getIsprint(){ return super.get("isprint");}
public void setIsprint(String  isprint){ super.set("isprint",isprint);} /**
  * 签名
  */
  public  String  getSign(){ return super.get("sign");}
public void setSign(String  sign){ super.set("sign",sign);} /**
  * 证照类型
  */
  public  String  getZztype(){ return super.get("zztype");}
public void setZztype(String  zztype){ super.set("zztype",zztype);} /**
  * 实名认证照片
  */
  public  String  getCertificationphoto(){ return super.get("certificationphoto");}
public void setCertificationphoto(String  certificationphoto){ super.set("certificationphoto",certificationphoto);} /**
  * 手机号验证码
  */
  public  String  getRzmobileyzm(){ return super.get("rzmobileyzm");}
public void setRzmobileyzm(String  rzmobileyzm){ super.set("rzmobileyzm",rzmobileyzm);}

/**
 * 注册号
 */
 public  String  getZch(){ return super.get("zch");}
public void setZch(String  zch){ super.set("zch",zch);} 

}