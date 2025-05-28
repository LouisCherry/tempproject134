package com.epoint.auditorga.auditwindow.entiy;

import java.util.Date;
import com.epoint.core.annotation.Entity;
import com.epoint.core.BaseEntity;
/**
* 实体类 
* 
* @作者 张彬彬 
* @version [版本号, 2023-01-09 15:59:38] 
*/

@Entity(table = "audit_orga_windowyjs", id = "rowguid")
public class AuditOrgaWindowYjs extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	* 获取
	*/
	public String getBelongxiaqucode() { 
 		return super.getStr("belongxiaqucode");
 	}
	/**
	* 设置
	*/
	public void setBelongxiaqucode(String BelongXiaQuCode) { 
 		super.set("belongxiaqucode", BelongXiaQuCode);
 	}
	/**
	* 获取
	*/
	public String getOperateusername() { 
 		return super.getStr("operateusername");
 	}
	/**
	* 设置
	*/
	public void setOperateusername(String OperateUserName) { 
 		super.set("operateusername", OperateUserName);
 	}
	/**
	* 获取
	*/
	public Date getOperatedate() { 
 		return super.getDate("operatedate");
 	}
	/**
	* 设置
	*/
	public void setOperatedate(Date OperateDate) { 
 		super.set("operatedate", OperateDate);
 	}
	/**
	* 获取
	*/
	public Integer getRow_id() { 
 		return super.getInt("row_id");
 	}
	/**
	* 设置
	*/
	public void setRow_id(Integer ROW_ID) { 
 		super.set("row_id", ROW_ID);
 	}
	/**
	* 获取
	*/
	public String getYearflag() { 
 		return super.getStr("yearflag");
 	}
	/**
	* 设置
	*/
	public void setYearflag(String YearFlag) { 
 		super.set("yearflag", YearFlag);
 	}
	/**
	* 获取
	*/
	public String getRowguid() { 
 		return super.getStr("rowguid");
 	}
	/**
	* 设置
	*/
	public void setRowguid(String RowGuid) { 
 		super.set("rowguid", RowGuid);
 	}
	/**
	* 获取
	*/
	public String getHandletype() { 
 		return super.getStr("handletype");
 	}
	/**
	* 设置
	*/
	public void setHandletype(String HANDLETYPE) { 
 		super.set("handletype", HANDLETYPE);
 	}
	/**
	* 获取
	*/
	public Integer getOrdernum() { 
 		return super.getInt("ordernum");
 	}
	/**
	* 设置
	*/
	public void setOrdernum(Integer ORDERNUM) { 
 		super.set("ordernum", ORDERNUM);
 	}
	/**
	* 获取
	*/
	public Integer getIs_showdp() { 
 		return super.getInt("is_showdp");
 	}
	/**
	* 设置
	*/
	public void setIs_showdp(Integer IS_SHOWDP) { 
 		super.set("is_showdp", IS_SHOWDP);
 	}
	/**
	* 获取
	*/
	public String getTaskguid() { 
 		return super.getStr("taskguid");
 	}
	/**
	* 设置
	*/
	public void setTaskguid(String TASKGUID) { 
 		super.set("taskguid", TASKGUID);
 	}
	/**
	* 获取
	*/
	public String getWindowguid() { 
 		return super.getStr("windowguid");
 	}
	/**
	* 设置
	*/
	public void setWindowguid(String WINDOWGUID) { 
 		super.set("windowguid", WINDOWGUID);
 	}
	/**
	* 获取
	*/
	public String getEnabled() { 
 		return super.getStr("enabled");
 	}
	/**
	* 设置
	*/
	public void setEnabled(String ENABLED) { 
 		super.set("enabled", ENABLED);
 	}
	/**
	* 获取
	*/
	public String getTaskid() { 
 		return super.getStr("taskid");
 	}
	/**
	* 设置
	*/
	public void setTaskid(String TASKID) { 
 		super.set("taskid", TASKID);
 	}
}