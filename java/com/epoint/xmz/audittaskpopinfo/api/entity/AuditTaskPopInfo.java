package com.epoint.xmz.audittaskpopinfo.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 弹窗信息维护实体
 * 
 * @作者  dahe
 * @version [版本号, 2024-11-26 10:20:20]
 */
@Entity(table = "audit_task_pop_info", id = "rowguid")
public class AuditTaskPopInfo extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

 /**
  * 弹窗内容
  */
  public  String  getContent(){ return super.get("content");}
public void setContent(String  content){ super.set("content",content);} /**
  * 创建人
  */
  public  String  getCreate_by(){ return super.get("create_by");}
public void setCreate_by(String  create_by){ super.set("create_by",create_by);} /**
  * 创建时间
  */
  public  Date  getCreate_time(){ return super.getDate("create_time");}
public void setCreate_time(Date  create_time){ super.set("create_time",create_time);} /**
  * 操作/更新人标识
  */
  public  String  getOperateuserguid(){ return super.get("operateuserguid");}
public void setOperateuserguid(String  operateuserguid){ super.set("operateuserguid",operateuserguid);} /**
  * 工作流实例标识
  */
  public  String  getPviguid(){ return super.get("pviguid");}
public void setPviguid(String  pviguid){ super.set("pviguid",pviguid);} /**
  * 事项唯一标识
  */
  public  String  getTaskid(){ return super.get("taskid");}
public void setTaskid(String  taskid){ super.set("taskid",taskid);} /**
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