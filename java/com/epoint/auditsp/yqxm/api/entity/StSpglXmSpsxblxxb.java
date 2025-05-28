package com.epoint.auditsp.yqxm.api.entity;

import java.util.Date;
import com.epoint.core.annotation.Entity;
import com.epoint.core.BaseEntity;
/**
* 实体类 
* 
* @作者 yj
* @version [版本号, 2022-06-08 14:40:43] 
*/

@Entity(table = "st_spgl_xmspsxblxxb", id = "rowguid")
public class StSpglXmSpsxblxxb extends BaseEntity {
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
	public String getSpsxbm() { 
 		return super.getStr("spsxbm");
 	}
	/**
	* 设置
	*/
	public void setSpsxbm(String SPSXBM) { 
 		super.set("spsxbm", SPSXBM);
 	}
	/**
	* 获取
	*/
	public String getSpsxmc() { 
 		return super.getStr("spsxmc");
 	}
	/**
	* 设置
	*/
	public void setSpsxmc(String SPSXMC) { 
 		super.set("spsxmc", SPSXMC);
 	}
	/**
	* 获取
	*/
	public String getDybzspsxbm() { 
 		return super.getStr("dybzspsxbm");
 	}
	/**
	* 设置
	*/
	public void setDybzspsxbm(String DYBZSPSXBM) { 
 		super.set("dybzspsxbm", DYBZSPSXBM);
 	}
	/**
	* 获取
	*/
	public Double getSpsxbbh() { 
 		return super.getDouble("spsxbbh");
 	}
	/**
	* 设置
	*/
	public void setSpsxbbh(Double SPSXBBH) { 
 		super.set("spsxbbh", SPSXBBH);
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
	public String getJsrowguid() { 
 		return super.getStr("jsrowguid");
 	}
	/**
	* 设置
	*/
	public void setJsrowguid(String JSROWGUID) { 
 		super.set("jsrowguid", JSROWGUID);
 	}
	/**
	* 获取
	*/
	public String getSpsxslbm() { 
 		return super.getStr("spsxslbm");
 	}
	/**
	* 设置
	*/
	public void setSpsxslbm(String SPSXSLBM) { 
 		super.set("spsxslbm", SPSXSLBM);
 	}
	/**
	* 获取
	*/
	public String getSpbmbm() { 
 		return super.getStr("spbmbm");
 	}
	/**
	* 设置
	*/
	public void setSpbmbm(String SPBMBM) { 
 		super.set("spbmbm", SPBMBM);
 	}
	/**
	* 获取
	*/
	public String getSpbmmc() { 
 		return super.getStr("spbmmc");
 	}
	/**
	* 设置
	*/
	public void setSpbmmc(String SPBMMC) { 
 		super.set("spbmmc", SPBMMC);
 	}
	/**
	* 获取
	*/
	public Integer getSlfs() { 
 		return super.getInt("slfs");
 	}
	/**
	* 设置
	*/
	public void setSlfs(Integer SLFS) { 
 		super.set("slfs", SLFS);
 	}
	/**
	* 获取
	*/
	public Integer getGkfs() { 
 		return super.getInt("gkfs");
 	}
	/**
	* 设置
	*/
	public void setGkfs(Integer GKFS) { 
 		super.set("gkfs", GKFS);
 	}
	/**
	* 获取
	*/
	public String getBlspslbm() { 
 		return super.getStr("blspslbm");
 	}
	/**
	* 设置
	*/
	public void setBlspslbm(String BLSPSLBM) { 
 		super.set("blspslbm", BLSPSLBM);
 	}
	/**
	* 获取
	*/
	public Integer getSxblsx() { 
 		return super.getInt("sxblsx");
 	}
	/**
	* 设置
	*/
	public void setSxblsx(Integer SXBLSX) { 
 		super.set("sxblsx", SXBLSX);
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
	public Integer getBlzt() { 
 		return super.getInt("blzt");
 	}
	/**
	* 设置
	*/
	public void setBlzt(Integer BLZT) { 
 		super.set("blzt", BLZT);
 	}
	/**
	* 获取
	*/
	public String getBlyj() { 
 		return super.getStr("blyj");
 	}
	/**
	* 设置
	*/
	public void setBlyj(String BLYJ) { 
 		super.set("blyj", BLYJ);
 	}
	/**
	* 获取
	*/
	public Date getBlsj() { 
 		return super.getDate("blsj");
 	}
	/**
	* 设置
	*/
	public void setBlsj(Date BLSJ) { 
 		super.set("blsj", BLSJ);
 	}
	/**
	* 获取
	*/
	public Integer getBjsfwqbj() { 
 		return super.getInt("bjsfwqbj");
 	}
	/**
	* 设置
	*/
	public void setBjsfwqbj(Integer BJSFWQBJ) { 
 		super.set("bjsfwqbj", BJSFWQBJ);
 	}
	/**
	* 获取
	*/
	public Date getSjsj() { 
 		return super.getDate("sjsj");
 	}
	/**
	* 设置
	*/
	public void setSjsj(Date SJSJ) { 
 		super.set("sjsj", SJSJ);
 	}
	/**
	* 获取
	*/
	public Date getSlsj() { 
 		return super.getDate("slsj");
 	}
	/**
	* 设置
	*/
	public void setSlsj(Date SLSJ) { 
 		super.set("slsj", SLSJ);
 	}
	/**
	* 获取
	*/
	public Date getBjsj() { 
 		return super.getDate("bjsj");
 	}
	/**
	* 设置
	*/
	public void setBjsj(Date BJSJ) { 
 		super.set("bjsj", BJSJ);
 	}
	/**
	* 获取
	*/
	public Integer getSpsxblsx() { 
 		return super.getInt("spsxblsx");
 	}
	/**
	* 设置
	*/
	public void setSpsxblsx(Integer SPSXBLSX) { 
 		super.set("spsxblsx", SPSXBLSX);
 	}
	/**
	* 获取
	*/
	public Date getCnbjsj() { 
 		return super.getDate("cnbjsj");
 	}
	/**
	* 设置
	*/
	public void setCnbjsj(Date CNBJSJ) { 
 		super.set("cnbjsj", CNBJSJ);
 	}
	/**
	* 获取
	*/
	public Integer getBjspys() { 
 		return super.getInt("bjspys");
 	}
	/**
	* 设置
	*/
	public void setBjspys(Integer BJSPYS) { 
 		super.set("bjspys", BJSPYS);
 	}
	/**
	* 获取
	*/
	public Integer getBjkdys() { 
 		return super.getInt("bjkdys");
 	}
	/**
	* 设置
	*/
	public void setBjkdys(Integer BJKDYS) { 
 		super.set("bjkdys", BJKDYS);
 	}
	/**
	* 获取
	*/
	public Integer getBjbzys() { 
 		return super.getInt("bjbzys");
 	}
	/**
	* 设置
	*/
	public void setBjbzys(Integer BJBZYS) { 
 		super.set("bjbzys", BJBZYS);
 	}
	/**
	* 获取
	*/
	public Integer getBjtbcxys() { 
 		return super.getInt("bjtbcxys");
 	}
	/**
	* 设置
	*/
	public void setBjtbcxys(Integer BJTBCXYS) { 
 		super.set("bjtbcxys", BJTBCXYS);
 	}
	/**
	* 获取
	*/
	public Integer getSjsplclx() { 
 		return super.getInt("sjsplclx");
 	}
	/**
	* 设置
	*/
	public void setSjsplclx(Integer SJSPLCLX) { 
 		super.set("sjsplclx", SJSPLCLX);
 	}
	/**
	* 获取
	*/
	public Integer getSplclx() { 
 		return super.getInt("splclx");
 	}
	/**
	* 设置
	*/
	public void setSplclx(Integer SPLCLX) { 
 		super.set("splclx", SPLCLX);
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
	public Integer getBjsfcq() { 
 		return super.getInt("bjsfcq");
 	}
	/**
	* 设置
	*/
	public void setBjsfcq(Integer BJSFCQ) { 
 		super.set("bjsfcq", BJSFCQ);
 	}
}