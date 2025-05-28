package com.epoint.takan.kanyanproject.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 勘验项目实体
 *
 * @作者  panshunxing
 * @version [版本号, 2024-09-19 21:25:37]
 */
@Entity(table = "KANYANPROJECT", id = "rowguid")
public class Kanyanproject extends BaseEntity implements Cloneable
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
 * projectname
 */
public  String  getProjectname(){ return super.get("projectname");}
  public void setProjectname(String  projectname){ super.set("projectname",projectname);} /**
 * mainpoint
 */
public  String  getMainpoint(){ return super.get("mainpoint");}
  public void setMainpoint(String  mainpoint){ super.set("mainpoint",mainpoint);} /**
 * shenchacontent
 */
public  String  getShenchacontent(){ return super.get("shenchacontent");}
  public void setShenchacontent(String  shenchacontent){ super.set("shenchacontent",shenchacontent);} /**
 * tiaokuannum
 */
public  String  getTiaokuannum(){ return super.get("tiaokuannum");}
  public void setTiaokuannum(String  tiaokuannum){ super.set("tiaokuannum",tiaokuannum);} /**
 * kanyanjietuclientguid
 */
public  String  getKanyanjietuclientguid(){ return super.get("kanyanjietuclientguid");}
  public void setKanyanjietuclientguid(String  kanyanjietuclientguid){ super.set("kanyanjietuclientguid",kanyanjietuclientguid);} /**
 * scrresult
 */
public  String  getScrresult(){ return super.get("scrresult");}
  public void setScrresult(String  scrresult){ super.set("scrresult",scrresult);} /**
 * scrcomment
 */
public  String  getScrcomment(){ return super.get("scrcomment");}
  public void setScrcomment(String  scrcomment){ super.set("scrcomment",scrcomment);} /**
 * zjresult
 */
public  String  getZjresult(){ return super.get("zjresult");}
  public void setZjresult(String  zjresult){ super.set("zjresult",zjresult);} /**
 * zjcomment
 */
public  String  getZjcomment(){ return super.get("zjcomment");}
  public void setZjcomment(String  zjcomment){ super.set("zjcomment",zjcomment);} /**
 * bylaw
 */
public  String  getBylaw(){ return super.get("bylaw");}
  public void setBylaw(String  bylaw){ super.set("bylaw",bylaw);}
  /**
   * kanyaguid
   */
  public  String  getKanyaguid(){ return super.get("kanyaguid");}
  public void setKanyaguid(String  kanyaguid){ super.set("kanyaguid",kanyaguid);}
  /**
   * urls
   */
  public  String  getUrls(){ return super.get("urls");}
  public void setUrls(String  urls){ super.set("urls",urls);}

  /**
   * videourls
   */
  public  String  getVideourls(){ return super.get("videourls");}
  public void setVideourls(String  videourls){ super.set("videourls",videourls);}


}