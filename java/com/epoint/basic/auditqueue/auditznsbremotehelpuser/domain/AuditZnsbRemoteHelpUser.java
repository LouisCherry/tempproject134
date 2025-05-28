package com.epoint.basic.auditqueue.auditznsbremotehelpuser.domain;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 好视通账户绑定审批人员实体
 * 
 * @作者  JackLove
 * @version [版本号, 2018-04-13 09:47:53]
 */
@Entity(table = "Audit_Znsb_RemoteHelp_User", id = "rowguid")
public class AuditZnsbRemoteHelpUser extends BaseEntity implements Cloneable
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
  * 房间
  */
  public  String  getRoom(){ return super.get("room");}
public void setRoom(String  room){ super.set("room",room);} /**
  * 审批人员guid
  */
  public  String  getUserguid(){ return super.get("userguid");}
public void setUserguid(String  userguid){ super.set("userguid",userguid);} /**
  * 中心guid
  */
  public  String  getCenterguid(){ return super.get("centerguid");}
public void setCenterguid(String  centerguid){ super.set("centerguid",centerguid);}

}