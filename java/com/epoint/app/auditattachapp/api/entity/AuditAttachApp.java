package com.epoint.app.auditattachapp.api.entity;
import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 网盘申请信息表实体
 * 
 * @作者  admin
 * @version [版本号, 2019-07-01 10:46:55]
 */
@Entity(table = "Audit_Attach_App", id = "rowguid")
public class AuditAttachApp extends BaseEntity implements Cloneable
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
  * 应用名称
  */
  public  String  getAppname(){ return super.get("appname");}
public void setAppname(String  appname){ super.set("appname",appname);} /**
  * 对应接口的uid
  */
  public  String  getAppkey(){ return super.get("appkey");}
public void setAppkey(String  appkey){ super.set("appkey",appkey);} /**
  * 申请时间
  */
  public  Date  getApplydate(){ return super.getDate("applydate");}
public void setApplydate(Date  applydate){ super.set("applydate",applydate);} /**
  * 是否启用
  */
  public  Integer  getIs_enable(){ return super.getInt("is_enable");}
public void setIs_enable(Integer  is_enable){ super.set("is_enable",is_enable);} /**
  * 排序
  */
  public  Integer  getOrdernum(){ return super.getInt("ordernum");}
public void setOrdernum(Integer  ordernum){ super.set("ordernum",ordernum);} /**
  * 接口描述
  */
  public  String  getRemark(){ return super.get("remark");}
public void setRemark(String  remark){ super.set("remark",remark);}

}