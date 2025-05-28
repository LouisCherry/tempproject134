package com.epoint.xmz.yjsczcapplyer.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 一件事住租车申请人信息表实体
 * 
 * @作者  1
 * @version [版本号, 2022-04-18 15:38:32]
 */
@Entity(table = "yjs_czc_applyer", id = "rowguid")
public class YjsCzcApplyer extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

 /**
  * 申请类型
  */
  public  String  getApply(){ return super.get("apply");}
public void setApply(String  apply){ super.set("apply",apply);} /**
  * 身份证号
  */
  public  String  getCertnum(){ return super.get("certnum");}
public void setCertnum(String  certnum){ super.set("certnum",certnum);} /**
  * 生成日期
  */
  public  Date  getCreatedate(){ return super.getDate("createdate");}
public void setCreatedate(Date  createdate){ super.set("createdate",createdate);} /**
  * 姓名
  */
  public  String  getName(){ return super.get("name");}
public void setName(String  name){ super.set("name",name);} /**
  * 性别
  */
  public  String  getSex(){ return super.get("sex");}
public void setSex(String  sex){ super.set("sex",sex);} /**
  * 默认主键字段
  */
  public  String  getRowguid(){ return super.get("rowguid");}
public void setRowguid(String  rowguid){ super.set("rowguid",rowguid);} /**
  * 年份标识
  */
  public  String  getYearflag(){ return super.get("yearflag");}
public void setYearflag(String  yearflag){ super.set("yearflag",yearflag);} /**
  * 序号
  */
  public  Integer  getRow_id(){ return super.getInt("row_id");}
public void setRow_id(Integer  row_id){ super.set("row_id",row_id);} /**
  * 操作日期
  */
  public  Date  getOperatedate(){ return super.getDate("operatedate");}
public void setOperatedate(Date  operatedate){ super.set("operatedate",operatedate);} /**
  * 操作者名字
  */
  public  String  getOperateusername(){ return super.get("operateusername");}
public void setOperateusername(String  operateusername){ super.set("operateusername",operateusername);} /**
  * 所属辖区号
  */
  public  String  getBelongxiaqucode(){ return super.get("belongxiaqucode");}
public void setBelongxiaqucode(String  belongxiaqucode){ super.set("belongxiaqucode",belongxiaqucode);}

}