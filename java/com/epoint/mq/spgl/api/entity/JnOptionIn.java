package com.epoint.mq.spgl.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
/**
* 实体类 
* 
* @作者 张彬彬 
* @version [版本号, 2020-08-10 14:27:11] 
*/

@Entity(table = "SPGL_XMQQYJXXB", id = "rowguid")
public class JnOptionIn extends BaseEntity {
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
	public void setRow_id(Integer Row_ID) { 
 		super.set("row_id", Row_ID);
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
	public Integer getLsh() { 
 		return super.getInt("lsh");
 	}
	/**
	* 设置
	*/
	public void setLsh(Integer LSH) { 
 		super.set("lsh", LSH);
 	}
	/**
	* 获取
	*/
	public String getDfsjzj() { 
 		return super.getStr("dfsjzj");
 	}
	/**
	* 设置
	*/
	public void setDfsjzj(String DFSJZJ) { 
 		super.set("dfsjzj", DFSJZJ);
 	}
	/**
	* 获取
	*/
	public String getXzqhdm() { 
 		return super.getStr("xzqhdm");
 	}
	/**
	* 设置
	*/
	public void setXzqhdm(String XZQHDM) { 
 		super.set("xzqhdm", XZQHDM);
 	}
	/**
	* 获取
	*/
	public String getXmdm() { 
 		return super.getStr("xmdm");
 	}
	/**
	* 设置
	*/
	public void setXmdm(String XMDM) { 
 		super.set("xmdm", XMDM);
 	}
	/**
	* 获取
	*/
	public String getQqyjslbm() { 
 		return super.getStr("qqyjslbm");
 	}
	/**
	* 设置
	*/
	public void setQqyjslbm(String QQYJSLBM) { 
 		super.set("qqyjslbm", QQYJSLBM);
 	}
	/**
	* 获取
	*/
	public String getBldwdm() { 
 		return super.getStr("bldwdm");
 	}
	/**
	* 设置
	*/
	public void setBldwdm(String BLDWDM) { 
 		super.set("bldwdm", BLDWDM);
 	}
	/**
	* 获取
	*/
	public String getBldwmc() { 
 		return super.getStr("bldwmc");
 	}
	/**
	* 设置
	*/
	public void setBldwmc(String BLDWMC) { 
 		super.set("bldwmc", BLDWMC);
 	}
	/**
	* 获取
	*/
	public Date getFksj() { 
 		return super.getDate("fksj");
 	}
	/**
	* 设置
	*/
	public void setFksj(Date FKSJ) { 
 		super.set("fksj", FKSJ);
 	}
	/**
	* 获取
	*/
	public String getBlr() { 
 		return super.getStr("blr");
 	}
	/**
	* 设置
	*/
	public void setBlr(String BLR) { 
 		super.set("blr", BLR);
 	}
	/**
	* 获取
	*/
	public String getQqyj() { 
 		return super.get("qqyj");
 	}
	/**
	* 设置
	*/
	public void setQqyj(String QQYJ) { 
 		super.set("qqyj", QQYJ);
 	}
	/**
	* 获取
	*/
	public String getFjmc() { 
 		return super.getStr("fjmc");
 	}
	/**
	* 设置
	*/
	public void setFjmc(String FJMC) { 
 		super.set("fjmc", FJMC);
 	}
	/**
	* 获取
	*/
	public String getFjid() { 
 		return super.getStr("fjid");
 	}
	/**
	* 设置
	*/
	public void setFjid(String FJID) { 
 		super.set("fjid", FJID);
 	}
	/**
	* 获取
	*/
	public Integer getSjyxbs() { 
 		return super.getInt("sjyxbs");
 	}
	/**
	* 设置
	*/
	public void setSjyxbs(Integer SJYXBS) { 
 		super.set("sjyxbs", SJYXBS);
 	}
	/**
	* 获取
	*/
	public String getSjwxyy() { 
 		return super.getStr("sjwxyy");
 	}
	/**
	* 设置
	*/
	public void setSjwxyy(String SJWXYY) { 
 		super.set("sjwxyy", SJWXYY);
 	}
	/**
	* 获取
	*/
	public Integer getSjsczt() { 
 		return super.getInt("sjsczt");
 	}
	/**
	* 设置
	*/
	public void setSjsczt(Integer SJSCZT) { 
 		super.set("sjsczt", SJSCZT);
 	}
	/**
	* 获取
	*/
	public String getSbyy() { 
 		return super.getStr("sbyy");
 	}
	/**
	* 设置
	*/
	public void setSbyy(String SBYY) { 
 		super.set("sbyy", SBYY);
 	}
	/**
	* 获取
	*/
	public Integer getSync() { 
 		return super.getInt("sync");
 	}
	/**
	* 设置
	*/
	public void setSync(Integer SYNC) { 
 		super.set("sync", SYNC);
 	}
}