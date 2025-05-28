package com.epoint.znsb.auditznsbytjlabel.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 一体机模块标签实体
 * 
 * @作者  Administrator
 * @version [版本号, 2021-04-20 09:47:21]
 */
@Entity(table = "AUDIT_ZNSB_YTJLABEL", id = "rowguid")
public class AuditZnsbYtjlabel extends BaseEntity implements Cloneable
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
  * 标签名称
  */
  public  String  getLablename(){ return super.get("lablename");}
public void setLablename(String  lablename){ super.set("lablename",lablename);} /**
  * 底色
  */
  public  String  getLabelcolor(){ return super.get("labelcolor");}
public void setLabelcolor(String  labelcolor){ super.set("labelcolor",labelcolor);} /**
  * 标签图片
  */
  public  String  getPngattachguid(){ return super.get("pngattachguid");}
public void setPngattachguid(String  pngattachguid){ super.set("pngattachguid",pngattachguid);} /**
  * 排序
  */
  public  Integer  getOrdernum(){ return super.getInt("ordernum");}
public void setOrdernum(Integer  ordernum){ super.set("ordernum",ordernum);}

}