package com.epoint.cs.tsproject.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 推送数据实体
 * 
 * @作者  18300
 * @version [版本号, 2018-12-13 20:11:11]
 */
@Entity(table = "ts_project", id = "rowguid")
public class TsProject extends BaseEntity implements Cloneable
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
  * 事项名称
  */
  public  String  getTaskname(){ return super.get("taskname");}
public void setTaskname(String  taskname){ super.set("taskname",taskname);} /**
  * 办件编号
  */
  public  String  getFlow(){ return super.get("flow");}
public void setFlow(String  flow){ super.set("flow",flow);} /**
  * 申请人
  */
  public  String  getApplyname(){ return super.get("applyname");}
public void setApplyname(String  applyname){ super.set("applyname",applyname);} /**
  * 申请时间
  */
  public  String  getApplytime(){ return super.get("applytime");}
public void setApplytime(String  applytime){ super.set("applytime",applytime);} /**
  * 受理人员
  */
  public  String  getAccpetname(){ return super.get("accpetname");}
public void setAccpetname(String  accpetname){ super.set("accpetname",accpetname);} /**
  * 受理时间
  */
  public  String  getAccepttime(){ return super.get("accepttime");}
public void setAccepttime(String  accepttime){ super.set("accepttime",accepttime);} /**
  * 办结人员
  */
  public  String  getBanjiename(){ return super.get("banjiename");}
public void setBanjiename(String  banjiename){ super.set("banjiename",banjiename);} /**
  * 办结时间
  */
  public  String  getBanjietime(){ return super.get("banjietime");}
public void setBanjietime(String  banjietime){ super.set("banjietime",banjietime);} /**
  * 办件状态
  */
  public  String  getStatus(){ return super.get("status");}
public void setStatus(String  status){ super.set("status",status);}

}