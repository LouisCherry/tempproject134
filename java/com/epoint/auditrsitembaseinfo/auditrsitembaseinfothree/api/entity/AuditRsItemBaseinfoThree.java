package com.epoint.auditrsitembaseinfo.auditrsitembaseinfothree.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 泰安建设项目第三阶段基本信息拓展表实体
 * 
 * @作者  wangxiaolong
 * @version [版本号, 2019-09-02 14:49:35]
 */
@Entity(table = "audit_rs_item_baseinfo_three", id = "rowguid")
public class AuditRsItemBaseinfoThree extends BaseEntity implements Cloneable
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
  * 立项文号
  */
  public  String  getLxwh(){ return super.get("lxwh");}
public void setLxwh(String  lxwh){ super.set("lxwh",lxwh);} /**
  * 立项日期
  */
  public  Date  getLxrq(){ return super.getDate("lxrq");}
public void setLxrq(Date  lxrq){ super.set("lxrq",lxrq);} /**
  * 所有制性质
  */
  public  String  getSyzxz(){ return super.get("syzxz");}
public void setSyzxz(String  syzxz){ super.set("syzxz",syzxz);} /**
  * 图审机构
  */
  public  String  getTsjg(){ return super.get("tsjg");}
public void setTsjg(String  tsjg){ super.set("tsjg",tsjg);} /**
  * 项目拆除面积
  */
  public  String  getXmccmj(){ return super.get("xmccmj");}
public void setXmccmj(String  xmccmj){ super.set("xmccmj",xmccmj);} /**
  * 产生建筑垃圾（立方米）
  */
  public  String  getCsjzlj(){ return super.get("csjzlj");}
public void setCsjzlj(String  csjzlj){ super.set("csjzlj",csjzlj);} /**
  * 主表guid
  */
  public  String  getParentguid(){ return super.get("parentguid");}
public void setParentguid(String  parentguid){ super.set("parentguid",parentguid);} /**
  * 阶段guid
  */
  public  String  getPhaseguid(){ return super.get("phaseguid");}
public void setPhaseguid(String  phaseguid){ super.set("phaseguid",phaseguid);} /**
  * 阶段实例guid
  */
  public  String  getSubappguid(){ return super.get("subappguid");}
public void setSubappguid(String  subappguid){ super.set("subappguid",subappguid);} /**
  * 建筑垃圾倾倒场地
  */
  public  String  getJzljqdcd(){ return super.get("jzljqdcd");}
public void setJzljqdcd(String  jzljqdcd){ super.set("jzljqdcd",jzljqdcd);}

}