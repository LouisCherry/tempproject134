package com.epoint.xmgj.projectauthorization.api.entity;
import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 项目授权表实体
 * 
 * @作者  pansh
 * @version [版本号, 2025-02-13 14:58:47]
 */
@Entity(table = "PROJECT_AUTHORIZATION", id = "rowguid")
public class ProjectAuthorization extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

 /**
  * rowguid
  */
  public  String  getRowguid(){ return super.get("rowguid");}
public void setRowguid(String  rowguid){ super.set("rowguid",rowguid);} /**
  * ProjectID
  */
  public  String  getProjectid(){ return super.get("projectid");}
public void setProjectid(String  projectid){ super.set("projectid",projectid);} /**
  * UserID
  */
  public  String  getUserid(){ return super.get("userid");}
public void setUserid(String  userid){ super.set("userid",userid);} /**
  * AuthorizationDate
  */
  public  Date  getAuthorizationdate(){ return super.getDate("authorizationdate");}
public void setAuthorizationdate(Date  authorizationdate){ super.set("authorizationdate",authorizationdate);} /**
  * AuthorizedBy
  */
  public  String  getAuthorizedby(){ return super.get("authorizedby");}
public void setAuthorizedby(String  authorizedby){ super.set("authorizedby",authorizedby);}

}