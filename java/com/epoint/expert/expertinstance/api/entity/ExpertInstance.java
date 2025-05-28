package com.epoint.expert.expertinstance.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 专家抽取实例表实体
 * 
 * @作者  Lee
 * @version [版本号, 2019-08-21 15:41:50]
 */
@Entity(table = "Expert_Instance", id = "rowguid")
public class ExpertInstance extends BaseEntity implements Cloneable
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
  * 抽取时间
  */
  public  Date  getExtracttime(){ return super.getDate("extracttime");}
public void setExtracttime(Date  extracttime){ super.set("extracttime",extracttime);} /**
  * 事项编码
  */
  public  String  getItem_id(){ return super.get("item_id");}
public void setItem_id(String  item_id){ super.set("item_id",item_id);} /**
  * 事项名称
  */
  public  String  getTaskname(){ return super.get("taskname");}
public void setTaskname(String  taskname){ super.set("taskname",taskname);} /**
  * 评标时间
  */
  public  Date  getBidtime(){ return super.getDate("bidtime");}
public void setBidtime(Date  bidtime){ super.set("bidtime",bidtime);} /**
  * 评委签到时间
  */
  public  Date  getCheckintime(){ return super.getDate("checkintime");}
public void setCheckintime(Date  checkintime){ super.set("checkintime",checkintime);} /**
  * 评标耗时
  */
  public  String  getBidcost(){ return super.get("bidcost");}
public void setBidcost(String  bidcost){ super.set("bidcost",bidcost);} /**
  * 评标地点
  */
  public  String  getBidaddress(){ return super.get("bidaddress");}
public void setBidaddress(String  bidaddress){ super.set("bidaddress",bidaddress);} /**
  * 详细地址
  */
  public  String  getAddress(){ return super.get("address");}
public void setAddress(String  address){ super.set("address",address);} /**
  * 回避天数
  */
  public  Integer  getAvoiddays(){ return super.getInt("avoiddays");}
public void setAvoiddays(Integer  avoiddays){ super.set("avoiddays",avoiddays);} /**
  * 回避时间段
  */
  public  Integer  getAvoidtimes(){ return super.getInt("avoidtimes");}
public void setAvoidtimes(Integer  avoidtimes){ super.set("avoidtimes",avoidtimes);} /**
  * 抽取规则备注
  */
  public  String  getAvoidremark(){ return super.get("avoidremark");}
public void setAvoidremark(String  avoidremark){ super.set("avoidremark",avoidremark);} /**
  * 本次抽取专家人数
  */
  public  Integer  getExtractnum(){ return super.getInt("extractnum");}
public void setExtractnum(Integer  extractnum){ super.set("extractnum",extractnum);} /**
  * 抽取专家数备注
  */
  public  String  getExtractremark(){ return super.get("extractremark");}
public void setExtractremark(String  extractremark){ super.set("extractremark",extractremark);} /**
  * 是否已抽取
  */
  public  String  getIs_extracted(){ return super.get("is_extracted");}
public void setIs_extracted(String  is_extracted){ super.set("is_extracted",is_extracted);}

}