package com.epoint.ces.auditznsbwaitvedio.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 等待屏视频实体
 * 
 * @作者  admin
 * @version [版本号, 2020-03-31 15:07:02]
 */
@Entity(table = "audit_znsb_waitvedio", id = "rowguid")
public class AuditZnsbWaitvedio extends BaseEntity implements Cloneable
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
  * 视频标识
  */
  public  String  getVideoguid(){ return super.get("videoguid");}
public void setVideoguid(String  videoguid){ super.set("videoguid",videoguid);} /**
  * 视频名称
  */
  public  String  getVideoname(){ return super.get("videoname");}
public void setVideoname(String  videoname){ super.set("videoname",videoname);} /**
  * 排序值
  */
  public  String  getOrdernum(){ return super.get("ordernum");}
public void setOrdernum(String  ordernum){ super.set("ordernum",ordernum);}

public  String  getCenterguid(){ return super.get("centerguid");}
public void setCenterguid(String  centerguid){ super.set("centerguid",centerguid);}


}