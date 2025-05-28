package com.epoint.cs.yyzzprint.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 营业执照打印记录表实体
 * 
 * @作者  admin
 * @version [版本号, 2020-04-23 11:25:13]
 */
@Entity(table = "yyzzprint", id = "rowguid")
public class Yyzzprint extends BaseEntity implements Cloneable
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
  * 统一信用代码
  */
  public  String  getCreditcode(){ return super.get("creditcode");}
public void setCreditcode(String  creditcode){ super.set("creditcode",creditcode);} /**
  * 打印设备
  */
  public  String  getMachineno(){ return super.get("machineno");}
public void setMachineno(String  machineno){ super.set("machineno",machineno);} /**
  * 单位名称
  */
  public  String  getDwmc(){ return super.get("dwmc");}
public void setDwmc(String  dwmc){ super.set("dwmc",dwmc);} /**
  * 企业类型
  */
  public  String  getCompanytype(){ return super.get("companytype");}
public void setCompanytype(String  companytype){ super.set("companytype",companytype);} /**
  * 法人代表
  */
  public  String  getLegalman(){ return super.get("legalman");}
public void setLegalman(String  legalman){ super.set("legalman",legalman);} /**
  * 经营范围
  */
  public  String  getJyfw(){ return super.get("jyfw");}
public void setJyfw(String  jyfw){ super.set("jyfw",jyfw);}

public  String  getAttacguid(){ return super.get("attachguid");}
public void setAttacguid(String  attachguid){ super.set("attachguid",attachguid);}

public  String  getFbattacguid(){ return super.get("fbattachguid");}
public void setFbattacguid(String  fbattachguid){ super.set("fbattachguid",fbattachguid);}

}