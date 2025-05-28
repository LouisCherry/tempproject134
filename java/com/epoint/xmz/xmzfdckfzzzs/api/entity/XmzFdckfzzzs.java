package com.epoint.xmz.xmzfdckfzzzs.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 房地产开发资质证书实体
 * 
 * @作者  86177
 * @version [版本号, 2021-05-12 09:40:37]
 */
@Entity(table = "xmz_fdckfzzzs", id = "rowguid")
public class XmzFdckfzzzs extends BaseEntity implements Cloneable
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
  * 区县
  */
  public  String  getQx(){ return super.get("qx");}
public void setQx(String  qx){ super.set("qx",qx);} /**
  * 区划代码
  */
  public  String  getQhdm(){ return super.get("qhdm");}
public void setQhdm(String  qhdm){ super.set("qhdm",qhdm);} /**
  * 企业名称
  */
  public  String  getQymc(){ return super.get("qymc");}
public void setQymc(String  qymc){ super.set("qymc",qymc);} /**
  * 地址
  */
  public  String  getDz(){ return super.get("dz");}
public void setDz(String  dz){ super.set("dz",dz);} /**
  * 法定代表人
  */
  public  String  getFddbr(){ return super.get("fddbr");}
public void setFddbr(String  fddbr){ super.set("fddbr",fddbr);} /**
  * 公司类型
  */
  public  String  getGglx(){ return super.get("gglx");}
public void setGglx(String  gglx){ super.set("gglx",gglx);} /**
  * 注册资本(万元)
  */
  public  String  getZczb(){ return super.get("zczb");}
public void setZczb(String  zczb){ super.set("zczb",zczb);} /**
  * 营业执照
  */
  public  String  getYyzz(){ return super.get("yyzz");}
public void setYyzz(String  yyzz){ super.set("yyzz",yyzz);} /**
  * 批准时间
  */
  public  Date  getPzsj(){ return super.getDate("pzsj");}
public void setPzsj(Date  pzsj){ super.set("pzsj",pzsj);} /**
  * 资质等级
  */
  public  String  getZzdj(){ return super.get("zzdj");}
public void setZzdj(String  zzdj){ super.set("zzdj",zzdj);} /**
  * 证书编号
  */
  public  String  getZsbh(){ return super.get("zsbh");}
public void setZsbh(String  zsbh){ super.set("zsbh",zsbh);} /**
  * 发证机关
  */
  public  String  getFzjg(){ return super.get("fzjg");}
public void setFzjg(String  fzjg){ super.set("fzjg",fzjg);} /**
  * 发证日期
  */
  public  Date  getFzrq(){ return super.getDate("fzrq");}
public void setFzrq(Date  fzrq){ super.set("fzrq",fzrq);} /**
  * 有效期至
  */
  public  Date  getYxqz(){ return super.getDate("yxqz");}
public void setYxqz(Date  yxqz){ super.set("yxqz",yxqz);}

}