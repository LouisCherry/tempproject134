package com.epoint.xmz.rpaysgzzba.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 院士工作站备案实体
 * 
 * @作者  1
 * @version [版本号, 2022-12-20 10:26:25]
 */
@Entity(table = "rpa_ysgzzba", id = "rowguid")
public class RpaYsgzzba extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

 /**
  * 单位名称
  */
  public  String  getCompanyname(){ return super.get("companyname");}
public void setCompanyname(String  companyname){ super.set("companyname",companyname);} /**
  * 通知公告日期
  */
  public  Date  getNoticedate(){ return super.getDate("noticedate");}
public void setNoticedate(Date  noticedate){ super.set("noticedate",noticedate);} /**
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