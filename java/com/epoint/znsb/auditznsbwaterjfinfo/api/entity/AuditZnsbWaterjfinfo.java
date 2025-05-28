package com.epoint.znsb.auditznsbwaterjfinfo.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 水务缴费信息实体
 * 
 * @作者  HYF
 * @version [版本号, 2021-11-11 14:49:42]
 */
@Entity(table = "AUDIT_ZNSB_WATERJFINFO", id = "rowguid")
public class AuditZnsbWaterjfinfo extends BaseEntity implements Cloneable
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
  * 交易流水号
  */
  public  String  getWaterflowon(){ return super.get("waterflowon");}
public void setWaterflowon(String  waterflowon){ super.set("waterflowon",waterflowon);} /**
  * 缴费金额
  */
  public  String  getWaterpaymoney(){ return super.get("waterpaymoney");}
public void setWaterpaymoney(String  waterpaymoney){ super.set("waterpaymoney",waterpaymoney);} /**
  * 户号
  */
  public  String  getWaternumber(){ return super.get("waternumber");}
public void setWaternumber(String  waternumber){ super.set("waternumber",waternumber);} /**
  * 交易时间
  */
  public  String  getWatertime(){ return super.get("watertime");}
public void setWatertime(String  watertime){ super.set("watertime",watertime);}
    /**
     * 欠费开始时间
     */
    public  String  getStarttime(){ return super.get("starttime");}
    public void setStarttime(String  starttime){ super.set("starttime",starttime);}

    /**
     * 欠费结束时间
     */
    public  String  getEndtime(){ return super.get("endtime");}
    public void setEndtime(String  endtime){ super.set("endtime",endtime);}

}