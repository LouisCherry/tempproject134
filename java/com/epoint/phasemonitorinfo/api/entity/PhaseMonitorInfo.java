package com.epoint.phasemonitorinfo.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 阶段监控表实体
 * 
 * @作者  liucheng
 * @version [版本号, 2019-08-26 14:29:32]
 */
@Entity(table = "phase_monitor_info", id = "rowguid")
public class PhaseMonitorInfo extends BaseEntity implements Cloneable
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
  * 项目名称
  */
  public  String  getItemname(){ return super.get("itemname");}
public void setItemname(String  itemname){ super.set("itemname",itemname);} /**
  * 阶段名称
  */
  public  String  getPhasename(){ return super.get("phasename");}
public void setPhasename(String  phasename){ super.set("phasename",phasename);} /**
  * 监控类型
  */
  public  String  getMonitortype(){ return super.get("monitortype");}
public void setMonitortype(String  monitortype){ super.set("monitortype",monitortype);} /**
  * 发起时间
  */
  public  Date  getStarttime(){ return super.getDate("starttime");}
public void setStarttime(Date  starttime){ super.set("starttime",starttime);} /**
  * 应处理时间
  */
  public  Date  getProcesstime(){ return super.getDate("processtime");}
public void setProcesstime(Date  processtime){ super.set("processtime",processtime);} /**
  * 剩余处理时间
  */
  public  Integer  getRemaintime(){ return super.getInt("remaintime");}
public void setRemaintime(Integer  remaintime){ super.set("remaintime",remaintime);} /**
  * 办理状态
  */
  public  String  getStatus(){ return super.get("status");}
public void setStatus(String  status){ super.set("status",status);}

}