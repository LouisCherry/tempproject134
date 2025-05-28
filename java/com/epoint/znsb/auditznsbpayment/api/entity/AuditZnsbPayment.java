package com.epoint.znsb.auditznsbpayment.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 水电气缴费实体
 * 
 * @作者  Administrator
 * @version [版本号, 2021-05-18 16:40:35]
 */
@Entity(table = "AUDIT_ZNSB_PAYMENT", id = "rowguid")
public class AuditZnsbPayment extends BaseEntity implements Cloneable
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
  * 中心唯一标识
  */
  public  String  getCenterguid(){ return super.get("centerguid");}
public void setCenterguid(String  centerguid){ super.set("centerguid",centerguid);} /**
  * 缴费机构
  */
  public  String  getPaymentname(){ return super.get("paymentname");}
public void setPaymentname(String  paymentname){ super.set("paymentname",paymentname);} /**
  * 缴费机构代码
  */
  public  String  getPaymentcode(){ return super.get("paymentcode");}
public void setPaymentcode(String  paymentcode){ super.set("paymentcode",paymentcode);} /**
  * 缴费类型
  */
  public  String  getPaytype(){ return super.get("paytype");}
public void setPaytype(String  paytype){ super.set("paytype",paytype);} /**
  * 区域名称
  */
  public  String  getAreaname(){ return super.get("areaname");}
public void setAreaname(String  areaname){ super.set("areaname",areaname);}/**
 * 区域名称
 */
public  String  getPngattachguid(){ return super.get("pngattachguid");}
    public void setPngattachguid(String  pngattachguid){ super.set("pngattachguid",pngattachguid);}


}