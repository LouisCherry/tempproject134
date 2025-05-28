package com.epoint.projectstatisticsconfig.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 办件统计配置表实体
 * 
 * @作者  15056
 * @version [版本号, 2022-05-23 17:43:09]
 */
@Entity(table = "project_statistics_config", id = "rowguid")
public class ProjectStatisticsConfig extends BaseEntity implements Cloneable
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
  * 所属辖区
  */
  public  String  getArea_code(){ return super.get("area_code");}
public void setArea_code(String  area_code){ super.set("area_code",area_code);} /**
  * 所属部门
  */
  public  String  getOu_guid(){ return super.get("ou_guid");}
public void setOu_guid(String  ou_guid){ super.set("ou_guid",ou_guid);}

}