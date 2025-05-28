package com.epoint.xmz.certbgxzdj.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 变更性质登记库实体
 * 
 * @作者  dyxin
 * @version [版本号, 2023-05-22 13:17:42]
 */
@Entity(table = "cert_bgxzdj", id = "rowguid")
public class CertBgxzdj extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

 /**
  * 公司
  */
  public  String  getCompany(){ return super.get("company");}
public void setCompany(String  company){ super.set("company",company);} /**
  * 默认主键字段
  */
  public  String  getRowguid(){ return super.get("rowguid");}
public void setRowguid(String  rowguid){ super.set("rowguid",rowguid);} /**
  * 车型
  */
  public  String  getCartype(){ return super.get("cartype");}
public void setCartype(String  cartype){ super.set("cartype",cartype);} /**
  * 年份标识
  */
  public  String  getYearflag(){ return super.get("yearflag");}
public void setYearflag(String  yearflag){ super.set("yearflag",yearflag);} /**
  * 车辆型号
  */
  public  String  getCarid(){ return super.get("carid");}
public void setCarid(String  carid){ super.set("carid",carid);} /**
  * 序号
  */
  public  Integer  getRow_id(){ return super.getInt("row_id");}
public void setRow_id(Integer  row_id){ super.set("row_id",row_id);} /**
  * 发动机号
  */
  public  String  getEngineid(){ return super.get("engineid");}
public void setEngineid(String  engineid){ super.set("engineid",engineid);} /**
  * 操作日期
  */
  public  Date  getOperatedate(){ return super.getDate("operatedate");}
public void setOperatedate(Date  operatedate){ super.set("operatedate",operatedate);} /**
  * 序号
  */
  public  Integer  getIndexdesc(){ return super.getInt("indexdesc");}
public void setIndexdesc(Integer  indexdesc){ super.set("indexdesc",indexdesc);} /**
  * 操作者名字
  */
  public  String  getOperateusername(){ return super.get("operateusername");}
public void setOperateusername(String  operateusername){ super.set("operateusername",operateusername);} /**
  * 所属辖区号
  */
  public  String  getBelongxiaqucode(){ return super.get("belongxiaqucode");}
public void setBelongxiaqucode(String  belongxiaqucode){ super.set("belongxiaqucode",belongxiaqucode);} /**
  * 变更时间
  */
  public  Date  getUpdatetime(){ return super.getDate("updatetime");}
public void setUpdatetime(Date  updatetime){ super.set("updatetime",updatetime);}

}