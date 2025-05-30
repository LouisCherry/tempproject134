package com.epoint.jnzwdt.zczwdt.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 卫生许可告知书附件表实体
 * 
 * @作者  Epoint
 * @version [版本号, 2022-01-23 17:06:24]
 */
@Entity(table = "audit_promise_book", id = "rowguid")
public class AuditPromiseBook extends BaseEntity implements Cloneable
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
  * 告知承诺类型
  */
  public  String  getPromisetype(){ return super.get("promisetype");}
public void setPromisetype(String  promisetype){ super.set("promisetype",promisetype);} /**
  * 附件cliengguid
  */
  public  String  getPromiseattachguid(){ return super.get("promiseattachguid");}
public void setPromiseattachguid(String  promiseattachguid){ super.set("promiseattachguid",promiseattachguid);}

}