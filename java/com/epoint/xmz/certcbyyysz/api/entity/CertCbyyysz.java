package com.epoint.xmz.certcbyyysz.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 船舶营业运输证本地库实体
 * 
 * @作者 1
 * @version [版本号, 2022-06-15 14:41:12]
 */
@Entity(table = "cert_cbyyysz", id = "rowguid")
public class CertCbyyysz extends BaseEntity implements Cloneable {
	private static final long serialVersionUID = 1L;

	/**
	 * 本船经营范围
	 */
	public String getBcjyfw() {
		return super.get("bcjyfw");
	}

	public void setBcjyfw(String bcjyfw) {
		super.set("bcjyfw", bcjyfw);
	}

	/**
	 * 船舶长度
	 */
	public String getCbcd() {
		return super.get("cbcd");
	}

	public void setCbcd(String cbcd) {
		super.set("cbcd", cbcd);
	}

	/**
	 * 船舶材料
	 */
	public String getCbcl() {
		return super.get("cbcl");
	}

	public void setCbcl(String cbcl) {
		super.set("cbcl", cbcl);
	}

	/**
	 * 船舶登记号
	 */
	public String getCbdjh() {
		return super.get("cbdjh");
	}

	public void setCbdjh(String cbdjh) {
		super.set("cbdjh", cbdjh);
	}

	/**
	 * 船舶管理人
	 */
	public String getCbglr() {
		return super.get("cbglr");
	}

	public void setCbglr(String cbglr) {
		super.set("cbglr", cbglr);
	}

	/**
	 * 船舶经营人
	 */
	public String getCbjyr() {
		return super.get("cbjyr");
	}

	public void setCbjyr(String cbjyr) {
		super.set("cbjyr", cbjyr);
	}

	/**
	 * 船舶来源
	 */
	public String getCbly() {
		return super.get("cbly");
	}

	public void setCbly(String cbly) {
		super.set("cbly", cbly);
	}

	/**
	 * 船舶所有人
	 */
	public String getCbsyr() {
		return super.get("cbsyr");
	}

	public void setCbsyr(String cbsyr) {
		super.set("cbsyr", cbsyr);
	}

	/**
	 * 船舶型宽
	 */
	public String getCbxk() {
		return super.get("cbxk");
	}

	public void setCbxk(String cbxk) {
		super.set("cbxk", cbxk);
	}

	/**
	 * 船舶型深
	 */
	public String getCbxs() {
		return super.get("cbxs");
	}

	public void setCbxs(String cbxs) {
		super.set("cbxs", cbxs);
	}

	/**
	 * 船舶种类
	 */
	public String getCbzl() {
		return super.get("cbzl");
	}

	public void setCbzl(String cbzl) {
		super.set("cbzl", cbzl);
	}

	/**
	 * 船检登记号
	 */
	public String getCjdjh() {
		return super.get("cjdjh");
	}

	public void setCjdjh(String cjdjh) {
		super.set("cjdjh", cjdjh);
	}

	/**
	 * 船籍港
	 */
	public String getCjg() {
		return super.get("cjg");
	}

	public void setCjg(String cjg) {
		super.set("cjg", cjg);
	}

	/**
	 * 车位
	 */
	public String getCw() {
		return super.get("cw");
	}

	public void setCw(String cw) {
		super.set("cw", cw);
	}

	/**
	 * 曾用名
	 */
	public String getCym() {
		return super.get("cym");
	}

	public void setCym(String cym) {
		super.set("cym", cym);
	}

	/**
	 * 发证机构
	 */
	public String getFzjg() {
		return super.get("fzjg");
	}

	public void setFzjg(String fzjg) {
		super.set("fzjg", fzjg);
	}

	/**
	 * 发证日期
	 */
	public Date getFzrq() {
		return super.getDate("fzrq");
	}

	public void setFzrq(Date fzrq) {
		super.set("fzrq", fzrq);
	}

	/**
	 * 改建日期
	 */
	public Date getGjrq() {
		return super.getDate("gjrq");
	}

	public void setGjrq(Date gjrq) {
		super.set("gjrq", gjrq);
	}

	/**
	 * 管理许可证编号
	 */
	public String getGlxkzbh() {
		return super.get("glxkzbh");
	}

	public void setGlxkzbh(String glxkzbh) {
		super.set("glxkzbh", glxkzbh);
	}

	/**
	 * 是否注销
	 */
	public String getIs_cancel() {
		return super.get("is_cancel");
	}

	public void setIs_cancel(String is_cancel) {
		super.set("is_cancel", is_cancel);
	}

	/**
	 * 是否在用
	 */
	public String getIs_enable() {
		return super.get("is_enable");
	}

	public void setIs_enable(String is_enable) {
		super.set("is_enable", is_enable);
	}

	/**
	 * 建成日期
	 */
	public Date getJcrq() {
		return super.getDate("jcrq");
	}

	public void setJcrq(Date jcrq) {
		super.set("jcrq", jcrq);
	}

	/**
	 * 净吨
	 */
	public String getJd() {
		return super.get("jd");
	}

	public void setJd(String jd) {
		super.set("jd", jd);
	}

	/**
	 * 经营区域
	 */
	public String getJyfw() {
		return super.get("jyfw");
	}

	public void setJyfw(String jyfw) {
		super.set("jyfw", jyfw);
	}

	/**
	 * 经营许可证编号
	 */
	public String getJyxkzbh() {
		return super.get("jyxkzbh");
	}

	public void setJyxkzbh(String jyxkzbh) {
		super.set("jyxkzbh", jyxkzbh);
	}

	/**
	 * 客位
	 */
	public String getKw() {
		return super.get("kw");
	}

	public void setKw(String kw) {
		super.set("kw", kw);
	}

	/**
	 * 立方米
	 */
	public String getLfm() {
		return super.get("lfm");
	}

	public void setLfm(String lfm) {
		super.set("lfm", lfm);
	}

	/**
	 * 内河沿海
	 */
	public String getNhyh() {
		return super.get("nhyh");
	}

	public void setNhyh(String nhyh) {
		super.set("nhyh", nhyh);
	}

	/**
	 * 是否标准船型
	 */
	public String getSfbzcx() {
		return super.get("sfbzcx");
	}

	public void setSfbzcx(String sfbzcx) {
		super.set("sfbzcx", sfbzcx);
	}

	/**
	 * 是否自有船舶
	 */
	public String getSfzycb() {
		return super.get("sfzycb");
	}

	public void setSfzycb(String sfzycb) {
		super.set("sfzycb", sfzycb);
	}

	/**
	 * TEU(标箱)
	 */
	public String getTeu() {
		return super.get("teu");
	}

	public void setTeu(String teu) {
		super.set("teu", teu);
	}

	/**
	 * 版本
	 */
	public String getVersion() {
		return super.get("version");
	}

	public void setVersion(String version) {
		super.set("version", version);
	}

	/**
	 * 相关证书号
	 */
	public String getXgzsh() {
		return super.get("xgzsh");
	}

	public void setXgzsh(String xgzsh) {
		super.set("xgzsh", xgzsh);
	}

	/**
	 * 许可证经营范围
	 */
	public String getXkzjyfw() {
		return super.get("xkzjyfw");
	}

	public void setXkzjyfw(String xkzjyfw) {
		super.set("xkzjyfw", xkzjyfw);
	}

	/**
	 * 有效标志
	 */
	public String getYxbz() {
		return super.get("yxbz");
	}

	public void setYxbz(String yxbz) {
		super.set("yxbz", yxbz);
	}

	/**
	 * 有效日期
	 */
	public Date getYxrq() {
		return super.getDate("yxrq");
	}

	public void setYxrq(Date yxrq) {
		super.set("yxrq", yxrq);
	}

	/**
	 * 营运证编号
	 */
	public String getYyzbh() {
		return super.get("yyzbh");
	}

	public void setYyzbh(String yyzbh) {
		super.set("yyzbh", yyzbh);
	}

	/**
	 * 主机台数
	 */
	public String getZcts() {
		return super.get("zcts");
	}

	public void setZcts(String zcts) {
		super.set("zcts", zcts);
	}

	/**
	 * 总吨
	 */
	public String getZd() {
		return super.get("zd");
	}

	public void setZd(String zd) {
		super.set("zd", zd);
	}

	/**
	 * 主机功率(千瓦)
	 */
	public String getZjgl() {
		return super.get("zjgl");
	}

	public void setZjgl(String zjgl) {
		super.set("zjgl", zjgl);
	}

	/**
	 * 中文船名
	 */
	public String getZwcm() {
		return super.get("zwcm");
	}

	public void setZwcm(String zwcm) {
		super.set("zwcm", zwcm);
	}

	/**
	 * 注销情况
	 */
	public String getZxqk() {
		return super.get("zxqk");
	}

	public void setZxqk(String zxqk) {
		super.set("zxqk", zxqk);
	}

	/**
	 * 注销原因
	 */
	public String getZxyy() {
		return super.get("zxyy");
	}

	public void setZxyy(String zxyy) {
		super.set("zxyy", zxyy);
	}

	/**
	 * 主运货类
	 */
	public String getZyhl() {
		return super.get("zyhl");
	}

	public void setZyhl(String zyhl) {
		super.set("zyhl", zyhl);
	}

	/**
	 * 载重吨
	 */
	public String getZzd() {
		return super.get("zzd");
	}

	public void setZzd(String zzd) {
		super.set("zzd", zzd);
	}

	/**
	 * 默认主键字段
	 */
	public String getRowguid() {
		return super.get("rowguid");
	}

	public void setRowguid(String rowguid) {
		super.set("rowguid", rowguid);
	}

	/**
	 * 年份标识
	 */
	public String getYearflag() {
		return super.get("yearflag");
	}

	public void setYearflag(String yearflag) {
		super.set("yearflag", yearflag);
	}

	/**
	 * 序号
	 */
	public Integer getRow_id() {
		return super.getInt("row_id");
	}

	public void setRow_id(Integer row_id) {
		super.set("row_id", row_id);
	}

	/**
	 * 操作日期
	 */
	public Date getOperatedate() {
		return super.getDate("operatedate");
	}

	public void setOperatedate(Date operatedate) {
		super.set("operatedate", operatedate);
	}

	/**
	 * 操作者名字
	 */
	public String getOperateusername() {
		return super.get("operateusername");
	}

	public void setOperateusername(String operateusername) {
		super.set("operateusername", operateusername);
	}

	/**
	 * 所属辖区号
	 */
	public String getBelongxiaqucode() {
		return super.get("belongxiaqucode");
	}

	public void setBelongxiaqucode(String belongxiaqucode) {
		super.set("belongxiaqucode", belongxiaqucode);
	}

}