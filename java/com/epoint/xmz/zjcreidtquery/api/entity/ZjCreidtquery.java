package com.epoint.xmz.zjcreidtquery.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 信用查询调用统计表实体
 * 
 * @作者  1
 * @version [版本号, 2021-10-09 14:58:46]
 */
@Entity(table = "zj_creidtquery", id = "rowguid")
public class ZjCreidtquery extends BaseEntity implements Cloneable
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
  * 所属区县
  */
  public  String  getAreacode(){ return super.get("areacode");}
public void setAreacode(String  areacode){ super.set("areacode",areacode);} /**
  * 所属区县名称
  */
  public  String  getAreaname(){ return super.get("areaname");}
public void setAreaname(String  areaname){ super.set("areaname",areaname);} /**
  * 信用奖惩调用次数
  */
  public  Integer  getJctotal(){ return super.getInt("jctotal");}
public void setJctotal(Integer  jctotal){ super.set("jctotal",jctotal);} /**
  * 信用查询调用次数
  */
  public  String  getXytaotal(){ return super.get("xytaotal");}
public void setXytaotal(String  xytaotal){ super.set("xytaotal",xytaotal);} /**
  * 接口类型
  */
  public  String  getType(){ return super.get("type");}
public void setType(String  type){ super.set("type",type);} /**
  * 调用时间
  */
  public  Date  getCreatetime(){ return super.getDate("createtime");}
public void setCreatetime(Date  createtime){ super.set("createtime",createtime);}

}