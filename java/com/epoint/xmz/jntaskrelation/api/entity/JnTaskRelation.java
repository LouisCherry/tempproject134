package com.epoint.xmz.jntaskrelation.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 事项关联乡镇表实体
 * 
 * @作者  1
 * @version [版本号, 2022-10-09 16:26:57]
 */
@Entity(table = "jn_task_relation", id = "rowguid")
public class JnTaskRelation extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

 /**
  * 辖区编码
  */
  public  String  getAreacode(){ return super.get("areacode");}
public void setAreacode(String  areacode){ super.set("areacode",areacode);} /**
  * 辖区名称
  */
  public  String  getAreaname(){ return super.get("areaname");}
public void setAreaname(String  areaname){ super.set("areaname",areaname);} /**
  * 事项编码
  */
  public  String  getItemid(){ return super.get("itemid");}
public void setItemid(String  itemid){ super.set("itemid",itemid);} /**
  * 区县辖区编码
  */
  public  String  getQxareacde(){ return super.get("qxareacde");}
public void setQxareacde(String  qxareacde){ super.set("qxareacde",qxareacde);} /**
  * 区县事项编码
  */
  public  String  getQxitemid(){ return super.get("qxitemid");}
public void setQxitemid(String  qxitemid){ super.set("qxitemid",qxitemid);} /**
  * 备注
  */
  public  String  getRemark(){ return super.get("remark");}
public void setRemark(String  remark){ super.set("remark",remark);} /**
  * 事项标识
  */
  public  String  getTaskid(){ return super.get("taskid");}
public void setTaskid(String  taskid){ super.set("taskid",taskid);} /**
  * 事项名称
  */
  public  String  getTaskname(){ return super.get("taskname");}
public void setTaskname(String  taskname){ super.set("taskname",taskname);} /**
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