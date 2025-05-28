package com.epoint.xmz.xsyjsfj.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 国土上传预审批复函实体
 * 
 * @作者  1
 * @version [版本号, 2022-10-06 17:34:42]
 */
@Entity(table = "XS_YJSFJ", id = "rowguid")
public class XsYjsfj extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

 /**
  * 电子监管号
  */
  public  String  getDzjgh(){ return super.get("dzjgh");}
public void setDzjgh(String  dzjgh){ super.set("dzjgh",dzjgh);} /**
  * 附件路径
  */
  public  String  getFjlj(){ return super.get("fjlj");}
public void setFjlj(String  fjlj){ super.set("fjlj",fjlj);} /**
  * 附件名称
  */
  public  String  getFjmc(){ return super.get("fjmc");}
public void setFjmc(String  fjmc){ super.set("fjmc",fjmc);} /**
  * 批复文号
  */
  public  String  getPfwh(){ return super.get("pfwh");}
public void setPfwh(String  pfwh){ super.set("pfwh",pfwh);} /**
  * 备注
  */
  public  String  getRemark(){ return super.get("remark");}
public void setRemark(String  remark){ super.set("remark",remark);} /**
  * 状态
  */
  public  String  getStatus(){ return super.get("status");}
public void setStatus(String  status){ super.set("status",status);} /**
  * 项目编号
  */
  public  String  getXmbh(){ return super.get("xmbh");}
public void setXmbh(String  xmbh){ super.set("xmbh",xmbh);} /**
  * 要素代码
  */
  public  String  getYsdm(){ return super.get("ysdm");}
public void setYsdm(String  ysdm){ super.set("ysdm",ysdm);} /**
  * 证书编号
  */
  public  String  getZsbh(){ return super.get("zsbh");}
public void setZsbh(String  zsbh){ super.set("zsbh",zsbh);} /**
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