package com.epoint.xmz.cjrmzarea.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 残疾人民政辖区对应表实体
 * 
 * @作者  1
 * @version [版本号, 2021-05-24 17:45:08]
 */
@Entity(table = "cjr_mz_area", id = "rowguid")
public class CjrMzArea extends BaseEntity implements Cloneable
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
  * 残疾人辖区名称
  */
  public  String  getCjrname(){ return super.get("cjrname");}
public void setCjrname(String  cjrname){ super.set("cjrname",cjrname);} /**
  * 残疾人所属辖区
  */
  public  String  getCjrareacode(){ return super.get("cjrareacode");}
public void setCjrareacode(String  cjrareacode){ super.set("cjrareacode",cjrareacode);} /**
  * 民政名称
  */
  public  String  getMzname(){ return super.get("mzname");}
public void setMzname(String  mzname){ super.set("mzname",mzname);} /**
  * 民政所属辖区
  */
  public  String  getMzareacode(){ return super.get("mzareacode");}
public void setMzareacode(String  mzareacode){ super.set("mzareacode",mzareacode);}

}