package com.epoint.auditsp.yqxm.api.entity;

import java.util.Date;
import com.epoint.core.annotation.Entity;
import com.epoint.core.BaseEntity;
/**
* 实体类 
* 省厅_审批管理_工程阶段信息表
* @作者 阳佳
* @version [版本号, 2022-06-07 10:42:35] 
*/

@Entity(table = "st_spgl_xmjbxxb", id = "rowguid")
public class StSpglXmjbxxb extends BaseEntity {
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
	public Integer getXmtzly() { 
 		return super.getInt("xmtzly");
 	}
	/**
	* 设置
	*/
	public void setXmtzly(Integer XMTZLY) { 
 		super.set("xmtzly", XMTZLY);
 	}
	/**
	* 获取
	*/
	public Integer getTdhqfs() { 
 		return super.getInt("tdhqfs");
 	}
	/**
	* 设置
	*/
	public void setTdhqfs(Integer TDHQFS) { 
 		super.set("tdhqfs", TDHQFS);
 	}
	/**
	* 获取
	*/
	public Integer getTdsfdsjfa() { 
 		return super.getInt("tdsfdsjfa");
 	}
	/**
	* 设置
	*/
	public void setTdsfdsjfa(Integer TDSFDSJFA) { 
 		super.set("tdsfdsjfa", TDSFDSJFA);
 	}
	/**
	* 获取
	*/
	public Integer getSfwcqypg() { 
 		return super.getInt("sfwcqypg");
 	}
	/**
	* 设置
	*/
	public void setSfwcqypg(Integer SFWCQYPG) { 
 		super.set("sfwcqypg", SFWCQYPG);
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
	public Integer getLxlx() { 
 		return super.getInt("lxlx");
 	}
	/**
	* 设置
	*/
	public void setLxlx(Integer LXLX) { 
 		super.set("lxlx", LXLX);
 	}
	/**
	* 获取
	*/
	public Integer getGcfl() { 
 		return super.getInt("gcfl");
 	}
	/**
	* 设置
	*/
	public void setGcfl(Integer GCFL) { 
 		super.set("gcfl", GCFL);
 	}
	/**
	* 获取
	*/
	public Integer getJsxz() { 
 		return super.getInt("jsxz");
 	}
	/**
	* 设置
	*/
	public void setJsxz(Integer JSXZ) { 
 		super.set("jsxz", JSXZ);
 	}
	/**
	* 获取
	*/
	public Integer getXmzjsx() { 
 		return super.getInt("xmzjsx");
 	}
	/**
	* 设置
	*/
	public void setXmzjsx(Integer XMZJSX) { 
 		super.set("xmzjsx", XMZJSX);
 	}
	/**
	* 获取
	*/
	public String getGbhydmfbnd() { 
 		return super.getStr("gbhydmfbnd");
 	}
	/**
	* 设置
	*/
	public void setGbhydmfbnd(String GBHYDMFBND) { 
 		super.set("gbhydmfbnd", GBHYDMFBND);
 	}
	/**
	* 获取
	*/
	public String getGbhy() { 
 		return super.getStr("gbhy");
 	}
	/**
	* 设置
	*/
	public void setGbhy(String GBHY) { 
 		super.set("gbhy", GBHY);
 	}
	/**
	* 获取
	*/
	public Date getNkgsj() { 
 		return super.getDate("nkgsj");
 	}
	/**
	* 设置
	*/
	public void setNkgsj(Date NKGSJ) { 
 		super.set("nkgsj", NKGSJ);
 	}
	/**
	* 获取
	*/
	public Date getNjcsj() { 
 		return super.getDate("njcsj");
 	}
	/**
	* 设置
	*/
	public void setNjcsj(Date NJCSJ) { 
 		super.set("njcsj", NJCSJ);
 	}
	/**
	* 获取
	*/
	public Integer getXmsfwqbj() { 
 		return super.getInt("xmsfwqbj");
 	}
	/**
	* 设置
	*/
	public void setXmsfwqbj(Integer XMSFWQBJ) { 
 		super.set("xmsfwqbj", XMSFWQBJ);
 	}
	/**
	* 获取
	*/
	public Date getXmwqbjsj() { 
 		return super.getDate("xmwqbjsj");
 	}
	/**
	* 设置
	*/
	public void setXmwqbjsj(Date XMWQBJSJ) { 
 		super.set("xmwqbjsj", XMWQBJSJ);
 	}
	/**
	* 获取
	*/
	public Double getZtze() {
 		return super.get("ztze");
 	}
	/**
	* 设置
	*/
	public void setZtze(Double ZTZE) {
 		super.set("ztze", ZTZE);
 	}
	/**
	* 获取
	*/
	public String getJsddxzqh() { 
 		return super.getStr("jsddxzqh");
 	}
	/**
	* 设置
	*/
	public void setJsddxzqh(String JSDDXZQH) { 
 		super.set("jsddxzqh", JSDDXZQH);
 	}
	/**
	* 获取
	*/
	public String getJsdd() { 
 		return super.getStr("jsdd");
 	}
	/**
	* 设置
	*/
	public void setJsdd(String JSDD) { 
 		super.set("jsdd", JSDD);
 	}
	/**
	* 获取
	*/
	public Double getXmjsddx() {
 		return super.get("xmjsddx");
 	}
	/**
	* 设置
	*/
	public void setXmjsddx(Double XMJSDDX) {
 		super.set("xmjsddx", XMJSDDX);
 	}
	/**
	* 获取
	*/
	public Double getXmjsddy() {
 		return super.get("xmjsddy");
 	}
	/**
	* 设置
	*/
	public void setXmjsddy(Double XMJSDDY) {
 		super.set("xmjsddy", XMJSDDY);
 	}
	/**
	* 获取
	*/
	public String getJsgmjnr() { 
 		return super.getStr("jsgmjnr");
 	}
	/**
	* 设置
	*/
	public void setJsgmjnr(String JSGMJNR) { 
 		super.set("jsgmjnr", JSGMJNR);
 	}
	/**
	* 获取
	*/
	public Double getYdmj() {
 		return super.get("ydmj");
 	}
	/**
	* 设置
	*/
	public void setYdmj(Double YDMJ) {
 		super.set("ydmj", YDMJ);
 	}
	/**
	* 获取
	*/
	public Double getJzmj() {
 		return super.get("jzmj");
 	}
	/**
	* 设置
	*/
	public void setJzmj(Double JZMJ) {
 		super.set("jzmj", JZMJ);
 	}
	/**
	* 获取
	*/
	public Date getSbsj() { 
 		return super.getDate("sbsj");
 	}
	/**
	* 设置
	*/
	public void setSbsj(Date SBSJ) { 
 		super.set("sbsj", SBSJ);
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
	public Integer getXmspys() { 
 		return super.getInt("xmspys");
 	}
	/**
	* 设置
	*/
	public void setXmspys(Integer XMSPYS) { 
 		super.set("xmspys", XMSPYS);
 	}
	/**
	* 获取
	*/
	public Integer getXmkdys() { 
 		return super.getInt("xmkdys");
 	}
	/**
	* 设置
	*/
	public void setXmkdys(Integer XMKDYS) { 
 		super.set("xmkdys", XMKDYS);
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
	public Integer getXmsfyq() { 
 		return super.getInt("xmsfyq");
 	}
	/**
	* 设置
	*/
	public void setXmsfyq(Integer XMSFYQ) { 
 		super.set("xmsfyq", XMSFYQ);
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
	public Integer getZspsxgs() { 
 		return super.getInt("zspsxgs");
 	}
	/**
	* 设置
	*/
	public void setZspsxgs(Integer ZSPSXGS) { 
 		super.set("zspsxgs", ZSPSXGS);
 	}
	/**
	* 获取
	*/
	public Integer getBxspzsx() { 
 		return super.getInt("bxspzsx");
 	}
	/**
	* 设置
	*/
	public void setBxspzsx(Integer BXSPZSX) { 
 		super.set("bxspzsx", BXSPZSX);
 	}
	/**
	* 获取
	*/
	public Integer getBxspsxgs() { 
 		return super.getInt("bxspsxgs");
 	}
	/**
	* 设置
	*/
	public void setBxspsxgs(Integer BXSPSXGS) { 
 		super.set("bxspsxgs", BXSPSXGS);
 	}
	/**
	* 获取
	*/
	public Integer getBlspzsx() { 
 		return super.getInt("blspzsx");
 	}
	/**
	* 设置
	*/
	public void setBlspzsx(Integer BLSPZSX) { 
 		super.set("blspzsx", BLSPZSX);
 	}
	/**
	* 获取
	*/
	public Integer getBlspsxgs() { 
 		return super.getInt("blspsxgs");
 	}
	/**
	* 设置
	*/
	public void setBlspsxgs(Integer BLSPSXGS) { 
 		super.set("blspsxgs", BLSPSXGS);
 	}
	/**
	* 获取
	*/
	public Date getSqsj() { 
 		return super.getDate("sqsj");
 	}
	/**
	* 设置
	*/
	public void setSqsj(Date SQSJ) { 
 		super.set("sqsj", SQSJ);
 	}
	/**
	 * 获取
	 */
	public String getAreacode() {
		return super.getStr("areacode");
	}
	/**
	 * 设置
	 */
	public void setAreacode(String areacode) {
		super.set("areacode", areacode);
	}
}