package com.epoint.zwdt.zwdtrest.user;

import java.util.Date;
import com.epoint.core.annotation.Entity;
import com.epoint.core.BaseEntity;
/**
* 实体类 
* 
* @作者 张彬彬 
* @version [版本号, 2022-05-17 10:27:30] 
*/

@Entity(table = "audit_online_choose_enterprise", id = "rowguid")
public class auditonlinechooseenterprise extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	* 获取
	*/
	public String getRowguid() { 
 		return super.getStr("rowguid");
 	}
	/**
	* 设置
	*/
	public void setRowguid(String rowguid) { 
 		super.set("rowguid", rowguid);
 	}
	/**
	* 获取
	*/
	public String getCompanyid() { 
 		return super.getStr("companyid");
 	}
	/**
	* 设置
	*/
	public void setCompanyid(String companyid) { 
 		super.set("companyid", companyid);
 	}
	/**
	* 获取
	*/
	public String getCreditcode() { 
 		return super.getStr("creditcode");
 	}
	/**
	* 设置
	*/
	public void setCreditcode(String creditcode) { 
 		super.set("creditcode", creditcode);
 	}
	/**
	* 获取
	*/
	public String getAccountguid() { 
 		return super.getStr("accountguid");
 	}
	/**
	* 设置
	*/
	public void setAccountguid(String accountguid) { 
 		super.set("accountguid", accountguid);
 	}
	/**
	* 获取
	*/
	public Date getCreat_date() { 
 		return super.getDate("creat_date");
 	}
	/**
	* 设置
	*/
	public void setCreat_date(Date creat_date) { 
 		super.set("creat_date", creat_date);
 	}
	/**
	* 获取
	*/
	public Date getUpdate_date() { 
 		return super.getDate("update_date");
 	}
	/**
	* 设置
	*/
	public void setUpdate_date(Date update_date) { 
 		super.set("update_date", update_date);
 	}
}