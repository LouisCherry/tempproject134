package com.epoint.auditrsitembaseinfo.auditrsitembaseinfoonewscompany.domain;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 泰安建设项目第一阶段涉及外商投资项目信息表实体
 * 
 * @作者  wangxiaolong
 * @version [版本号, 2019-08-06 16:24:47]
 */
@Entity(table = "audit_rs_item_baseinfo_one_wscompany", id = "rowguid")
public class AuditRsItemBaseinfoOneWscompany extends BaseEntity implements Cloneable
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
  * 主表guid
  */
  public  String  getParentid(){ return super.get("parentid");}
public void setParentid(String  parentid){ super.set("parentid",parentid);} /**
  * 阶段guid
  */
  public  String  getPhaseguid(){ return super.get("phaseguid");}
public void setPhaseguid(String  phaseguid){ super.set("phaseguid",phaseguid);} /**
  * 注册地点
  */
  public  String  getZcdd(){ return super.get("zcdd");}
public void setZcdd(String  zcdd){ super.set("zcdd",zcdd);} /**
  * 注册资金（含增资后）（万美元）
  */
  public  String  getZczj(){ return super.get("zczj");}
public void setZczj(String  zczj){ super.set("zczj",zczj);} /**
  * 投资方甲（国别）
  */
  public  String  getTzfj(){ return super.get("tzfj");}
public void setTzfj(String  tzfj){ super.set("tzfj",tzfj);} /**
  * 投资方甲占比（%）
  */
  public  String  getTzfjzb(){ return super.get("tzfjzb");}
public void setTzfjzb(String  tzfjzb){ super.set("tzfjzb",tzfjzb);} /**
  * 投资方已（国别）
  */
  public  String  getTzfy(){ return super.get("tzfy");}
public void setTzfy(String  tzfy){ super.set("tzfy",tzfy);} /**
  * 投资方已占比（%）
  */
  public  String  getTzfyzb(){ return super.get("tzfyzb");}
public void setTzfyzb(String  tzfyzb){ super.set("tzfyzb",tzfyzb);} /**
  * 投资方丙（国别）
  */
  public  String  getTzfb(){ return super.get("tzfb");}
public void setTzfb(String  tzfb){ super.set("tzfb",tzfb);} /**
  * 投资方丙占比（%）
  */
  public  String  getTzfbzb(){ return super.get("tzfbzb");}
public void setTzfbzb(String  tzfbzb){ super.set("tzfbzb",tzfbzb);} /**
  * 备用字段
  */
  public  String  getBak(){ return super.get("bak");}
public void setBak(String  bak){ super.set("bak",bak);}

}