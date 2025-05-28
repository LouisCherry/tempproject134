package com.epoint.znsb.auditznsbmedical.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 静态医疗信息查询实体
 * 
 * @作者  Administrator
 * @version [版本号, 2021-04-27 09:56:35]
 */
@Entity(table = "AUDIT_ZNSB_MEDICAL", id = "rowguid")
public class AuditZnsbMedical extends BaseEntity implements Cloneable
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
  * 医院名称
  */
  public  String  getMedicalname(){ return super.get("medicalname");}
public void setMedicalname(String  medicalname){ super.set("medicalname",medicalname);} /**
  * 医院级别
  */
  public  String  getMedicallevel(){ return super.get("medicallevel");}
public void setMedicallevel(String  medicallevel){ super.set("medicallevel",medicallevel);} /**
  * 医疗机构性质
  */
  public  String  getMedicaltype(){ return super.get("medicaltype");}
public void setMedicaltype(String  medicaltype){ super.set("medicaltype",medicaltype);} /**
  * 地址
  */
  public  String  getAddress(){ return super.get("address");}
public void setAddress(String  address){ super.set("address",address);} /**
  * 联系人
  */
  public  String  getContacts(){ return super.get("contacts");}
public void setContacts(String  contacts){ super.set("contacts",contacts);} /**
  * 电话
  */
  public  String  getContactnumber(){ return super.get("contactnumber");}
public void setContactnumber(String  contactnumber){ super.set("contactnumber",contactnumber);}

}