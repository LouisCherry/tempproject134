package com.epoint.xmz.xmztaskguideconfig.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 事项指南配置表实体
 * 
 * @作者  xczheng0314
 * @version [版本号, 2023-03-21 11:38:55]
 */
@Entity(table = "xmz_taskguide_config", id = "rowguid")
public class XmzTaskguideConfig extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

 /**
  * 事项名称
  */
  public  String  getTaskname(){ return super.get("taskname");}
public void setTaskname(String  taskname){ super.set("taskname",taskname);} /**
  * 默认主键字段
  */
  public  String  getRowguid(){ return super.get("rowguid");}
public void setRowguid(String  rowguid){ super.set("rowguid",rowguid);} /**
  * 事项id
  */
  public  String  getTaskid(){ return super.get("taskid");}
public void setTaskid(String  taskid){ super.set("taskid",taskid);} /**
  * 指南附件标识
  */
  public  String  getGuidecliengguid(){ return super.get("guidecliengguid");}
public void setGuidecliengguid(String  guidecliengguid){ super.set("guidecliengguid",guidecliengguid);} /**
  * 年份标识
  */
  public  String  getYearflag(){ return super.get("yearflag");}
public void setYearflag(String  yearflag){ super.set("yearflag",yearflag);} /**
  * 辖区
  */
  public  String  getAreacode(){ return super.get("areacode");}
public void setAreacode(String  areacode){ super.set("areacode",areacode);} /**
  * 序号
  */
  public  Integer  getRow_id(){ return super.getInt("row_id");}
public void setRow_id(Integer  row_id){ super.set("row_id",row_id);} /**
  * 创建日期
  */
  public  Date  getCreatedate(){ return super.getDate("createdate");}
public void setCreatedate(Date  createdate){ super.set("createdate",createdate);} /**
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