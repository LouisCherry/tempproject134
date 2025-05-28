package com.epoint.xmz.jnvisitrecord.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 网厅访问量统计表实体
 * 
 * @作者  1
 * @version [版本号, 2022-09-14 14:54:42]
 */
@Entity(table = "jn_visit_record", id = "rowguid")
public class JnVisitRecord extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

 /**
  * 辖区编码
  */
  public  String  getAreacode(){ return super.get("areacode");}
public void setAreacode(String  areacode){ super.set("areacode",areacode);} /**
  * 调用次数
  */
  public  String  getRecordtotal(){ return super.get("recordtotal");}
public void setRecordtotal(String  recordtotal){ super.set("recordtotal",recordtotal);} /**
  * 备注
  */
  public  String  getRemark(){ return super.get("remark");}
public void setRemark(String  remark){ super.set("remark",remark);} /**
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