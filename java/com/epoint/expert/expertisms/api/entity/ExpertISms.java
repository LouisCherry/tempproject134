package com.epoint.expert.expertisms.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 专家抽取短信发送表实体
 * 
 * @作者  Lee
 * @version [版本号, 2019-08-21 15:42:10]
 */
@Entity(table = "Expert_I_SMS", id = "rowguid")
public class ExpertISms extends BaseEntity implements Cloneable
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
  * 短信标题
  */
  public  String  getTitle(){ return super.get("title");}
public void setTitle(String  title){ super.set("title",title);} /**
  * 短信内容
  */
  public  String  getContent(){ return super.get("content");}
public void setContent(String  content){ super.set("content",content);} /**
  * 收件人姓名
  */
  public  String  getUsernames(){ return super.get("usernames");}
public void setUsernames(String  usernames){ super.set("usernames",usernames);} /**
  * 收件人标识
  */
  public  String  getUserguids(){ return super.get("userguids");}
public void setUserguids(String  userguids){ super.set("userguids",userguids);}

}