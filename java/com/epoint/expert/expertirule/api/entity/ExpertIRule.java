package com.epoint.expert.expertirule.api.entity;
import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 专家抽取规则表实体
 * 
 * @作者  Lee
 * @version [版本号, 2019-08-21 15:41:56]
 */
@Entity(table = "Expert_I_Rule", id = "rowguid")
public class ExpertIRule extends BaseEntity implements Cloneable
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
  * Expert_Instance表主键
  */
  public  String  getInstanceguid(){ return super.get("instanceguid");}
public void setInstanceguid(String  instanceguid){ super.set("instanceguid",instanceguid);} /**
  * 抽取规则对象
  */
  public  String  getObjecttype(){ return super.get("objecttype");}
public void setObjecttype(String  objecttype){ super.set("objecttype",objecttype);} /**
  * 对象标识
  */
  public  String  getObjectguid(){ return super.get("objectguid");}
public void setObjectguid(String  objectguid){ super.set("objectguid",objectguid);} /**
  * 对象名称
  */
  public  String  getObjectname(){ return super.get("objectname");}
public void setObjectname(String  objectname){ super.set("objectname",objectname);}

}