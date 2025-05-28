package com.epoint.basic.auditqueue.auditznsbremotehelp.domain;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 一体机绑定好视通账户实体
 * 
 * @作者  JackLove
 * @version [版本号, 2018-04-12 15:24:50]
 */
@Entity(table = "Audit_Znsb_RemoteHelp", id = "rowguid")
public class AuditZnsbRemoteHelp extends BaseEntity implements Cloneable
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
  * 账户
  */
  public  String  getAccount(){ return super.get("account");}
public void setAccount(String  account){ super.set("account",account);} /**
  * 密码
  */
  public  String  getPassword(){ return super.get("password");}
public void setPassword(String  password){ super.set("password",password);} /**
  * 一体机rowguid
  */
  public  String  getMachineguid(){ return super.get("machineguid");}
public void setMachineguid(String  machineguid){ super.set("machineguid",machineguid);}/**
* 中心guid
*/
public  String  getCenterguid(){ return super.get("centerguid");}
public void setCenterguid(String  centerguid){ super.set("centerguid",centerguid);}

}