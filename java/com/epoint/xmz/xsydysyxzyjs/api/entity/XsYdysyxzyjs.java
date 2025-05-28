package com.epoint.xmz.xsydysyxzyjs.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 国土用地预审与选址意见书实体
 * 
 * @作者  1
 * @version [版本号, 2022-10-06 17:34:36]
 */
@Entity(table = "XS_YDYSYXZYJS", id = "rowguid")
public class XsYdysyxzyjs extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

 /**
  * 电子监管号
  */
  public  String  getDzjgh(){ return super.get("dzjgh");}
public void setDzjgh(String  dzjgh){ super.set("dzjgh",dzjgh);} /**
  * 发证日期
  */
  public  Date  getFzrq(){ return super.getDate("fzrq");}
public void setFzrq(Date  fzrq){ super.set("fzrq",fzrq);} /**
  * 核发机关
  */
  public  String  getHfjg(){ return super.get("hfjg");}
public void setHfjg(String  hfjg){ super.set("hfjg",hfjg);} /**
  * 备注
  */
  public  String  getRemark(){ return super.get("remark");}
public void setRemark(String  remark){ super.set("remark",remark);} /**
  * 是否已注销
  */
  public  String  getSfyzx(){ return super.get("sfyzx");}
public void setSfyzx(String  sfyzx){ super.set("sfyzx",sfyzx);} /**
  * 状态
  */
  public  String  getStatus(){ return super.get("status");}
public void setStatus(String  status){ super.set("status",status);} /**
  * 失效日期
  */
  public  Date  getSxrq(){ return super.getDate("sxrq");}
public void setSxrq(Date  sxrq){ super.set("sxrq",sxrq);} /**
  * 项目编号
  */
  public  String  getXmbh(){ return super.get("xmbh");}
public void setXmbh(String  xmbh){ super.set("xmbh",xmbh);} /**
  * 项目代码
  */
  public  String  getXmdm(){ return super.get("xmdm");}
public void setXmdm(String  xmdm){ super.set("xmdm",xmdm);} /**
  * 行政区代码
  */
  public  String  getXzqdm(){ return super.get("xzqdm");}
public void setXzqdm(String  xzqdm){ super.set("xzqdm",xzqdm);} /**
  * 行政区名称
  */
  public  String  getXzqmc(){ return super.get("xzqmc");}
public void setXzqmc(String  xzqmc){ super.set("xzqmc",xzqmc);} /**
  * 要素代码
  */
  public  String  getYsdm(){ return super.get("ysdm");}
public void setYsdm(String  ysdm){ super.set("ysdm",ysdm);} /**
  * 证书编号
  */
  public  String  getZsbh(){ return super.get("zsbh");}
public void setZsbh(String  zsbh){ super.set("zsbh",zsbh);} /**
  * 证书附图
  */
  public  String  getZsft(){ return super.get("zsft");}
public void setZsft(String  zsft){ super.set("zsft",zsft);} /**
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