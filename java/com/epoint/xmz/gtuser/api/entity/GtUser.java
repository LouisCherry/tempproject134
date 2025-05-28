package com.epoint.xmz.gtuser.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 国土用户管理表实体
 * 
 * @作者  1
 * @version [版本号, 2022-10-06 12:05:00]
 */
@Entity(table = "gt_user", id = "rowguid")
public class GtUser extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

 /**
  * 接口返回码
  */
  public  String  getCode(){ return super.get("code");}
public void setCode(String  code){ super.set("code",code);} /**
  * 群组名称
  */
  public  String  getDatapackagetype(){ return super.get("datapackagetype");}
public void setDatapackagetype(String  datapackagetype){ super.set("datapackagetype",datapackagetype);} /**
  * 登录名
  */
  public  String  getLoginname(){ return super.get("loginname");}
public void setLoginname(String  loginname){ super.set("loginname",loginname);} /**
  * 操作类型
  */
  public  String  getOptype(){ return super.get("optype");}
public void setOptype(String  optype){ super.set("optype",optype);} /**
  * 手机号
  */
  public  String  getPhone(){ return super.get("phone");}
public void setPhone(String  phone){ super.set("phone",phone);} /**
  * 公钥
  */
  public  String  getPublickey(){ return super.get("publickey");}
public void setPublickey(String  publickey){ super.set("publickey",publickey);} /**
  * 备注
  */
  public  String  getReamrk(){ return super.get("reamrk");}
public void setReamrk(String  reamrk){ super.set("reamrk",reamrk);} /**
  * 所在行政区编码
  */
  public  String  getRegioncode(){ return super.get("regioncode");}
public void setRegioncode(String  regioncode){ super.set("regioncode",regioncode);} /**
  * 请求ID
  */
  public  String  getRequestid(){ return super.get("requestid");}
public void setRequestid(String  requestid){ super.set("requestid",requestid);} /**
  * 签名
  */
  public  String  getSign(){ return super.get("sign");}
public void setSign(String  sign){ super.set("sign",sign);} /**
  * 用途管制阶段的简称
  */
  public  String  getStage(){ return super.get("stage");}
public void setStage(String  stage){ super.set("stage",stage);} /**
  * 同步状态
  */
  public  String  getStatus(){ return super.get("status");}
public void setStatus(String  status){ super.set("status",status);} /**
  * 用户名
  */
  public  String  getUsername(){ return super.get("username");}
public void setUsername(String  username){ super.set("username",username);} /**
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