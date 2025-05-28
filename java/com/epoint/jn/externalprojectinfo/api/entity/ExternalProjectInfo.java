package com.epoint.jn.externalprojectinfo.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 外部办件基本信息表实体
 * 
 * @作者  wannengDB
 * @version [版本号, 2022-01-06 14:37:04]
 */
@Entity(table = "external_project_info", id = "rowguid")
public class ExternalProjectInfo extends BaseEntity implements Cloneable
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
  * 受理时间
  */
  public  Date  getAccept_date(){ return super.getDate("accept_date");}
public void setAccept_date(Date  accept_date){ super.set("accept_date",accept_date);} /**
  * 受理人标识
  */
  public  String  getAccept_user_guid(){ return super.get("accept_user_guid");}
public void setAccept_user_guid(String  accept_user_guid){ super.set("accept_user_guid",accept_user_guid);} /**
  * 受理单位标识
  */
  public  String  getAccept_ou_guid(){ return super.get("accept_ou_guid");}
public void setAccept_ou_guid(String  accept_ou_guid){ super.set("accept_ou_guid",accept_ou_guid);} /**
  * 所属区县
  */
  public  String  getAreacode(){ return super.get("areacode");}
public void setAreacode(String  areacode){ super.set("areacode",areacode);} /**
  * 事项唯一标识
  */
  public  String  getTask_guid(){ return super.get("task_guid");}
public void setTask_guid(String  task_guid){ super.set("task_guid",task_guid);} /**
  * 事项版本标识
  */
  public  String  getTask_id(){ return super.get("task_id");}
public void setTask_id(String  task_id){ super.set("task_id",task_id);} /**
  * 办结时间
  */
  public  Date  getComplete_date(){ return super.getDate("complete_date");}
public void setComplete_date(Date  complete_date){ super.set("complete_date",complete_date);}

}