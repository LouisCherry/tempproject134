package com.epoint.auditsp.yqxm.api.entity;

import java.util.Date;
import com.epoint.core.annotation.Entity;
import com.epoint.core.BaseEntity;
/**
* 实体类 
* 
* @作者 阳佳
* @version [版本号, 2022-06-07 10:40:25] 
*/

@Entity(table = "st_spgl_gcjdxxb", id = "rowguid")
public class StSpglGcJdXxb extends BaseEntity {
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
	public String getXmmc() { 
 		return super.getStr("xmmc");
 	}
	/**
	* 设置
	*/
	public void setXmmc(String XMMC) { 
 		super.set("xmmc", XMMC);
 	}
	/**
	* 获取
	*/
	public String getGcdm() { 
 		return super.getStr("gcdm");
 	}
	/**
	* 设置
	*/
	public void setGcdm(String GCDM) { 
 		super.set("gcdm", GCDM);
 	}
	/**
	* 获取
	*/
	public String getGcfw() { 
 		return super.getStr("gcfw");
 	}
	/**
	* 设置
	*/
	public void setGcfw(String GCFW) { 
 		super.set("gcfw", GCFW);
 	}
	/**
	* 获取
	*/
	public String getSpjdbm() { 
 		return super.getStr("spjdbm");
 	}
	/**
	* 设置
	*/
	public void setSpjdbm(String SPJDBM) { 
 		super.set("spjdbm", SPJDBM);
 	}
	/**
	* 获取
	*/
	public String getSpjdmc() { 
 		return super.getStr("spjdmc");
 	}
	/**
	* 设置
	*/
	public void setSpjdmc(String SPJDMC) { 
 		super.set("spjdmc", SPJDMC);
 	}
	/**
	* 获取
	*/
	public Integer getSpjdxh() { 
 		return super.getInt("spjdxh");
 	}
	/**
	* 设置
	*/
	public void setSpjdxh(Integer SPJDXH) { 
 		super.set("spjdxh", SPJDXH);
 	}
	/**
	* 获取
	*/
	public String getDybzspjdxh() { 
 		return super.getStr("dybzspjdxh");
 	}
	/**
	* 设置
	*/
	public void setDybzspjdxh(String DYBZSPJDXH) { 
 		super.set("dybzspjdxh", DYBZSPJDXH);
 	}
	/**
	* 获取
	*/
	public Integer getSpjdsx() { 
 		return super.getInt("spjdsx");
 	}
	/**
	* 设置
	*/
	public void setSpjdsx(Integer SPJDSX) { 
 		super.set("spjdsx", SPJDSX);
 	}
	/**
	* 获取
	*/
	public String getSplcbm() { 
 		return super.getStr("splcbm");
 	}
	/**
	* 设置
	*/
	public void setSplcbm(String SPLCBM) { 
 		super.set("splcbm", SPLCBM);
 	}
	/**
	* 获取
	*/
	public Double getSplcbbh() { 
 		return super.getDouble("splcbbh");
 	}
	/**
	* 设置
	*/
	public void setSplcbbh(Double SPLCBBH) { 
 		super.set("splcbbh", SPLCBBH);
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
	public Date getTimestamp() { 
 		return super.getDate("timestamp");
 	}
	/**
	* 设置
	*/
	public void setTimestamp(Date TIMESTAMP) { 
 		super.set("timestamp", TIMESTAMP);
 	}
	/**
	* 获取
	*/
	public String getDatasource() { 
 		return super.getStr("datasource");
 	}
	/**
	* 设置
	*/
	public void setDatasource(String DATASOURCE) { 
 		super.set("datasource", DATASOURCE);
 	}
	/**
	* 获取
	*/
	public Date getCreatetimestamp() { 
 		return super.getDate("createtimestamp");
 	}
	/**
	* 设置
	*/
	public void setCreatetimestamp(Date CREATETIMESTAMP) { 
 		super.set("createtimestamp", CREATETIMESTAMP);
 	}
	/**
	* 获取
	*/
	public Integer getGcjdsfbj() { 
 		return super.getInt("gcjdsfbj");
 	}
	/**
	* 设置
	*/
	public void setGcjdsfbj(Integer GCJDSFBJ) { 
 		super.set("gcjdsfbj", GCJDSFBJ);
 	}
	/**
	* 获取
	*/
	public Date getGcjdkssj() { 
 		return super.getDate("gcjdkssj");
 	}
	/**
	* 设置
	*/
	public void setGcjdkssj(Date GCJDKSSJ) { 
 		super.set("gcjdkssj", GCJDKSSJ);
 	}
	/**
	* 获取
	*/
	public Date getGcjdjssj() { 
 		return super.getDate("gcjdjssj");
 	}
	/**
	* 设置
	*/
	public void setGcjdjssj(Date GCJDJSSJ) { 
 		super.set("gcjdjssj", GCJDJSSJ);
 	}
	/**
	* 获取
	*/
	public Integer getGcjdspys() { 
 		return super.getInt("gcjdspys");
 	}
	/**
	* 设置
	*/
	public void setGcjdspys(Integer GCJDSPYS) { 
 		super.set("gcjdspys", GCJDSPYS);
 	}
	/**
	* 获取
	*/
	public Integer getGcjdkdys() { 
 		return super.getInt("gcjdkdys");
 	}
	/**
	* 设置
	*/
	public void setGcjdkdys(Integer GCJDKDYS) { 
 		super.set("gcjdkdys", GCJDKDYS);
 	}
	/**
	* 获取
	*/
	public Integer getGcjdtbcxys() { 
 		return super.getInt("gcjdtbcxys");
 	}
	/**
	* 设置
	*/
	public void setGcjdtbcxys(Integer GCJDTBCXYS) { 
 		super.set("gcjdtbcxys", GCJDTBCXYS);
 	}
	/**
	* 获取
	*/
	public Integer getGcjdbzys() { 
 		return super.getInt("gcjdbzys");
 	}
	/**
	* 设置
	*/
	public void setGcjdbzys(Integer GCJDBZYS) { 
 		super.set("gcjdbzys", GCJDBZYS);
 	}
	/**
	* 获取
	*/
	public Integer getGcjdjdys() { 
 		return super.getInt("gcjdjdys");
 	}
	/**
	* 设置
	*/
	public void setGcjdjdys(Integer GCJDJDYS) { 
 		super.set("gcjdjdys", GCJDJDYS);
 	}
	/**
	* 获取
	*/
	public Integer getJsdwhlhys() { 
 		return super.getInt("jsdwhlhys");
 	}
	/**
	* 设置
	*/
	public void setJsdwhlhys(Integer JSDWHLHYS) { 
 		super.set("jsdwhlhys", JSDWHLHYS);
 	}
	/**
	* 获取
	*/
	public Integer getSfytj() { 
 		return super.getInt("sfytj");
 	}
	/**
	* 设置
	*/
	public void setSfytj(Integer SFYTJ) { 
 		super.set("sfytj", SFYTJ);
 	}
	/**
	* 获取
	*/
	public Integer getSfyqtlxbj() { 
 		return super.getInt("sfyqtlxbj");
 	}
	/**
	* 设置
	*/
	public void setSfyqtlxbj(Integer SFYQTLXBJ) { 
 		super.set("sfyqtlxbj", SFYQTLXBJ);
 	}
	/**
	* 获取
	*/
	public Integer getSjsjzqzt() { 
 		return super.getInt("sjsjzqzt");
 	}
	/**
	* 设置
	*/
	public void setSjsjzqzt(Integer SJSJZQZT) { 
 		super.set("sjsjzqzt", SJSJZQZT);
 	}
	/**
	* 获取
	*/
	public String getQjdgcdm() { 
 		return super.getStr("qjdgcdm");
 	}
	/**
	* 设置
	*/
	public void setQjdgcdm(String QJDGCDM) { 
 		super.set("qjdgcdm", QJDGCDM);
 	}
	/**
	* 获取
	*/
	public Integer getSpjdsfcq() { 
 		return super.getInt("spjdsfcq");
 	}
	/**
	* 设置
	*/
	public void setSpjdsfcq(Integer SPJDSFCQ) { 
 		super.set("spjdsfcq", SPJDSFCQ);
 	}
}