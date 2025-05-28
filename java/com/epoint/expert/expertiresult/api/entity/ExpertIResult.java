package com.epoint.expert.expertiresult.api.entity;
import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 专家抽取结果表实体
 * 
 * @作者  Lee
 * @version [版本号, 2019-08-21 15:42:03]
 */
@Entity(table = "Expert_I_Result", id = "rowguid")
public class ExpertIResult extends BaseEntity implements Cloneable
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
  * Expert_Instance表
  */
  public  String  getInstanceguid(){ return super.get("instanceguid");}
public void setInstanceguid(String  instanceguid){ super.set("instanceguid",instanceguid);} /**
  * 专家标识
  */
  public  String  getExpertguid(){ return super.get("expertguid");}
public void setExpertguid(String  expertguid){ super.set("expertguid",expertguid);} /**
  * 专家编号
  */
  public  String  getExpertno(){ return super.get("expertno");}
public void setExpertno(String  expertno){ super.set("expertno",expertno);} /**
  * 专家名称
  */
  public  String  getExpertname(){ return super.get("expertname");}
public void setExpertname(String  expertname){ super.set("expertname",expertname);} /**
  * 评标专业
  */
  public  String  getPingbzy(){ return super.get("pingbzy");}
public void setPingbzy(String  pingbzy){ super.set("pingbzy",pingbzy);} /**
  * 注册地区_省
  */
  public  String  getProvince(){ return super.get("province");}
public void setProvince(String  province){ super.set("province",province);} /**
  * 注册地区_市
  */
  public  String  getCity(){ return super.get("city");}
public void setCity(String  city){ super.set("city",city);} /**
  * 注册地区_区
  */
  public  String  getCountry(){ return super.get("country");}
public void setCountry(String  country){ super.set("country",country);} /**
  * 联系电话
  */
  public  String  getContactphone(){ return super.get("contactphone");}
public void setContactphone(String  contactphone){ super.set("contactphone",contactphone);} /**
  * 邮箱
  */
  public  String  getEmail(){ return super.get("email");}
public void setEmail(String  email){ super.set("email",email);} /**
  * 是否系统自动抽取
  */
  public  String  getIs_auto(){ return super.get("is_auto");}
public void setIs_auto(String  is_auto){ super.set("is_auto",is_auto);} /**
  * 是否参加
  */
  public  String  getIs_attend(){ return super.get("is_attend");}
public void setIs_attend(String  is_attend){ super.set("is_attend",is_attend);} /**
  * 不能参加原因
  */
  public  String  getAbsentreason(){ return super.get("absentreason");}
public void setAbsentreason(String  absentreason){ super.set("absentreason",absentreason);}

}