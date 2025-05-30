package com.epoint.takan.kanyanmain.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 勘验主表实体
 * 
 * @作者  panshunxing
 * @version [版本号, 2024-09-19 21:25:28]
 */
@Entity(table = "KANYANMAIN", id = "rowguid")
public class Kanyanmain extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

 /**
  * BelongXiaQuCode
  */
  public  String  getBelongxiaqucode(){ return super.get("belongxiaqucode");}
public void setBelongxiaqucode(String  belongxiaqucode){ super.set("belongxiaqucode",belongxiaqucode);} /**
  * OperateUserName
  */
  public  String  getOperateusername(){ return super.get("operateusername");}
public void setOperateusername(String  operateusername){ super.set("operateusername",operateusername);} /**
  * OperateDate
  */
  public  Date  getOperatedate(){ return super.getDate("operatedate");}
public void setOperatedate(Date  operatedate){ super.set("operatedate",operatedate);} /**
  * ROW_ID
  */
  public  Integer  getRow_id(){ return super.getInt("row_id");}
public void setRow_id(Integer  row_id){ super.set("row_id",row_id);} /**
  * YearFlag
  */
  public  String  getYearflag(){ return super.get("yearflag");}
public void setYearflag(String  yearflag){ super.set("yearflag",yearflag);} /**
  * RowGuid
  */
  public  String  getRowguid(){ return super.get("rowguid");}
public void setRowguid(String  rowguid){ super.set("rowguid",rowguid);} /**
  * name
  */
  public  String  getName(){ return super.get("name");}
public void setName(String  name){ super.set("name",name);} /**
  * taskname
  */
  public  String  getTaskname(){ return super.get("taskname");}
public void setTaskname(String  taskname){ super.set("taskname",taskname);} /**
  * flowsn
  */
  public  String  getFlowsn(){ return super.get("flowsn");}
public void setFlowsn(String  flowsn){ super.set("flowsn",flowsn);} /**
  * projctguid
  */
  public  String  getProjctguid(){ return super.get("projctguid");}
public void setProjctguid(String  projctguid){ super.set("projctguid",projctguid);}

      /**
   * videourls
   */
  public  String  getVideourls(){ return super.get("videourls");}
  public void setVideourls(String  videourls){ super.set("videourls",videourls);}

}