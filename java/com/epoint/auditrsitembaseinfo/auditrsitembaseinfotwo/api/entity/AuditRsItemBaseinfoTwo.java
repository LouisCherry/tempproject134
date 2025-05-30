package com.epoint.auditrsitembaseinfo.auditrsitembaseinfotwo.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 泰安建设项目第二阶段基本信息拓展表实体
 * 
 * @作者  wangxiaolong
 * @version [版本号, 2019-08-26 10:47:06]
 */
@Entity(table = "audit_rs_item_baseinfo_two", id = "rowguid")
public class AuditRsItemBaseinfoTwo extends BaseEntity implements Cloneable
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
  * 绿地面积
  */
  public  String  getLdmj(){ return super.get("ldmj");}
public void setLdmj(String  ldmj){ super.set("ldmj",ldmj);} /**
  * 绿地率
  */
  public  String  getLdl(){ return super.get("ldl");}
public void setLdl(String  ldl){ super.set("ldl",ldl);} /**
  * 取水方式
  */
  public  String  getQsfs(){ return super.get("qsfs");}
public void setQsfs(String  qsfs){ super.set("qsfs",qsfs);} /**
  * 退水方式
  */
  public  String  getTsfs(){ return super.get("tsfs");}
public void setTsfs(String  tsfs){ super.set("tsfs",tsfs);} /**
  * 取水量
  */
  public  String  getQsl(){ return super.get("qsl");}
public void setQsl(String  qsl){ super.set("qsl",qsl);} /**
  * 取水类型
  */
  public  String  getQslx(){ return super.get("qslx");}
public void setQslx(String  qslx){ super.set("qslx",qslx);} /**
  * 电源情况
  */
  public  String  getDyqk(){ return super.get("dyqk");}
public void setDyqk(String  dyqk){ super.set("dyqk",dyqk);} /**
  * 土壤情况
  */
  public  String  getTrqk(){ return super.get("trqk");}
public void setTrqk(String  trqk){ super.set("trqk",trqk);} /**
  * 防雷图号
  */
  public  String  getFlth(){ return super.get("flth");}
public void setFlth(String  flth){ super.set("flth",flth);} /**
  * 结构类型
  */
  public  String  getJglx(){ return super.get("jglx");}
public void setJglx(String  jglx){ super.set("jglx",jglx);} /**
  * 招标代理机构
  */
  public  String  getZbdljg(){ return super.get("zbdljg");}
public void setZbdljg(String  zbdljg){ super.set("zbdljg",zbdljg);} /**
  * 工程造价咨询企业
  */
  public  String  getGczjzxqy(){ return super.get("gczjzxqy");}
public void setGczjzxqy(String  gczjzxqy){ super.set("gczjzxqy",gczjzxqy);} /**
  * 主表guid
  */
  public  String  getParentid(){ return super.get("parentid");}
public void setParentid(String  parentid){ super.set("parentid",parentid);} /**
  * 阶段guid
  */
  public  String  getPhaseguid(){ return super.get("phaseguid");}
public void setPhaseguid(String  phaseguid){ super.set("phaseguid",phaseguid);} /**
  * 阶段实例guid
  */
  public  String  getSubappguid(){ return super.get("subappguid");}
public void setSubappguid(String  subappguid){ super.set("subappguid",subappguid);}

}