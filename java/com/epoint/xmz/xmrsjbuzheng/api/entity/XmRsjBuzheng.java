package com.epoint.xmz.xmrsjbuzheng.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 人社局补正信息表实体
 * 
 * @作者  LYA
 * @version [版本号, 2020-12-23 13:15:19]
 */
@Entity(table = "xm_rsj_buzheng", id = "rowguid")
public class XmRsjBuzheng extends BaseEntity implements Cloneable
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
  * 办件流水号
  */
  public  String  getFlowsn(){ return super.get("flowsn");}
public void setFlowsn(String  flowsn){ super.set("flowsn",flowsn);} /**
  * 补正告知发出人姓名
  */
  public  String  getBzgzfcrxm(){ return super.get("bzgzfcrxm");}
public void setBzgzfcrxm(String  bzgzfcrxm){ super.set("bzgzfcrxm",bzgzfcrxm);} /**
  * 补正告知原因
  */
  public  String  getBzgzyy(){ return super.get("bzgzyy");}
public void setBzgzyy(String  bzgzyy){ super.set("bzgzyy",bzgzyy);} /**
  * 补正材料清单
  */
  public  String  getBzclqd(){ return super.get("bzclqd");}
public void setBzclqd(String  bzclqd){ super.set("bzclqd",bzclqd);} /**
  * 补正告知时限
  */
  public  Integer  getBzgzsx(){ return super.getInt("bzgzsx");}
public void setBzgzsx(Integer  bzgzsx){ super.set("bzgzsx",bzgzsx);} /**
  * 补正告知时限单位
  */
  public  String  getBzgzsxdw(){ return super.get("bzgzsxdw");}
public void setBzgzsxdw(String  bzgzsxdw){ super.set("bzgzsxdw",bzgzsxdw);} /**
  * 补正告知材料编码
  */
  public  String  getBqbzclbm(){ return super.get("bqbzclbm");}
public void setBqbzclbm(String  bqbzclbm){ super.set("bqbzclbm",bqbzclbm);} /**
  * 补正告知时间
  */
  public  Date  getBzgzsj(){ return super.getDate("bzgzsj");}
public void setBzgzsj(Date  bzgzsj){ super.set("bzgzsj",bzgzsj);} /**
  * 补正告知部门所在地行政区划代码
  */
  public  String  getXzqhdm(){ return super.get("xzqhdm");}
public void setXzqhdm(String  xzqhdm){ super.set("xzqhdm",xzqhdm);} /**
  * 备注
  */
  public  String  getBz(){ return super.get("bz");}
public void setBz(String  bz){ super.set("bz",bz);} /**
  * 投资项目统一编码
  */
  public  String  getProject_code(){ return super.get("project_code");}
public void setProject_code(String  project_code){ super.set("project_code",project_code);} /**
  * 数据提供部门组织机构代码/社会信用代码
  */
  public  String  getD_zzjgdm(){ return super.get("d_zzjgdm");}
public void setD_zzjgdm(String  d_zzjgdm){ super.set("d_zzjgdm",d_zzjgdm);}

}