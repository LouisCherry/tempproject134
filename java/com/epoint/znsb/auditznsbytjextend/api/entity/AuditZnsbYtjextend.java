package com.epoint.znsb.auditznsbytjextend.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 一体机模块额外配置实体
 * 
 * @作者  Administrator
 * @version [版本号, 2021-04-20 10:11:49]
 */
@Entity(table = "AUDIT_ZNSB_YTJEXTEND", id = "rowguid")
public class AuditZnsbYtjextend extends BaseEntity implements Cloneable
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
  * 是否热门模块
  */
  public  String  getIs_hot(){ return super.get("is_hot");}
public void setIs_hot(String  is_hot){ super.set("is_hot",is_hot);} /**
  * 是否新模块
  */
  public  String  getIs_new(){ return super.get("is_new");}
public void setIs_new(String  is_new){ super.set("is_new",is_new);} /**
  * 标签归属
  */
  public  String  getLableguid(){ return super.get("lableguid");}
public void setLableguid(String  lableguid){ super.set("lableguid",lableguid);} /**
  * 模块图片
  */
  public  String  getPngattachguid(){ return super.get("pngattachguid");}
public void setPngattachguid(String  pngattachguid){ super.set("pngattachguid",pngattachguid);} /**
  * 排序
  */
  public  Integer  getOrdernum(){ return super.getInt("ordernum");}
public void setOrdernum(Integer  ordernum){ super.set("ordernum",ordernum);}/**
 * 模块唯一标识
 */
public  String  getModuleguid(){ return super.get("moduleguid");}
    public void setModuleguid(String  moduleguid){ super.set("moduleguid",moduleguid);}
    /**
     * 是否新模块
     */
    public  String  getIs_show(){ return super.get("is_show");}
    public void setIs_show(String  is_show){ super.set("is_show",is_show);}

}