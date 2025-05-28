package com.epoint.xmz.zjknowledge.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 自建系统知识库表实体
 * 
 * @作者  1
 * @version [版本号, 2021-10-25 15:12:09]
 */
@Entity(table = "zj_knowledge", id = "rowguid")
public class ZjKnowledge extends BaseEntity implements Cloneable
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
  * 问题类型
  */
  public  String  getType(){ return super.get("type");}
public void setType(String  type){ super.set("type",type);} /**
  * 问题标题
  */
  public  String  getTitle(){ return super.get("title");}
public void setTitle(String  title){ super.set("title",title);} /**
  * 问题解答
  */
  public  String  getAnswer(){ return super.get("answer");}
public void setAnswer(String  answer){ super.set("answer",answer);} /**
  * 所属部门
  */
  public  String  getOuname(){ return super.get("ouname");}
public void setOuname(String  ouname){ super.set("ouname",ouname);} /**
  * 部门标识
  */
  public  String  getOuguid(){ return super.get("ouguid");}
public void setOuguid(String  ouguid){ super.set("ouguid",ouguid);} /**
  * 关联事项标识
  */
  public  String  getTaskguid(){ return super.get("taskguid");}
public void setTaskguid(String  taskguid){ super.set("taskguid",taskguid);} /**
  * 关联事项名称
  */
  public  String  getTaskname(){ return super.get("taskname");}
public void setTaskname(String  taskname){ super.set("taskname",taskname);} /**
  * 关键字
  */
  public  String  getKeyword(){ return super.get("keyword");}
public void setKeyword(String  keyword){ super.set("keyword",keyword);}

}