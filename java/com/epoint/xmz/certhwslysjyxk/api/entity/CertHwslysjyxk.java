package com.epoint.xmz.certhwslysjyxk.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 省际普通货物水路运输经营许可本地库实体
 * 
 * @作者 1
 * @version [版本号, 2022-04-26 14:57:52]
 */
@Entity(table = "cert_hwslysjyxk", id = "rowguid")
public class CertHwslysjyxk extends BaseEntity implements Cloneable {
	private static final long serialVersionUID = 1L;

	/**
	 * 所属辖区号
	 */
	public String getBelongxiaqucode() {
		return super.get("belongxiaqucode");
	}

	public void setBelongxiaqucode(String belongxiaqucode) {
		super.set("belongxiaqucode", belongxiaqucode);
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
	 * 操作日期
	 */
	public Date getOperatedate() {
		return super.getDate("operatedate");
	}

	public void setOperatedate(Date operatedate) {
		super.set("operatedate", operatedate);
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
	 * 年份标识
	 */
	public String getYearflag() {
		return super.get("yearflag");
	}

	public void setYearflag(String yearflag) {
		super.set("yearflag", yearflag);
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
	 * 编号
	 */
	public String getBh() {
		return super.get("bh");
	}

	public void setBh(String bh) {
		super.set("bh", bh);
	}

	/**
	 * 经营人名称
	 */
	public String getJyzmc() {
		return super.get("jyzmc");
	}

	public void setJyzmc(String jyzmc) {
		super.set("jyzmc", jyzmc);
	}

	/**
	 * 经营人地址
	 */
	public String getJyrdz() {
		return super.get("jyrdz");
	}

	public void setJyrdz(String jyrdz) {
		super.set("jyrdz", jyrdz);
	}

	/**
	 * 法定代表人
	 */
	public String getFddbr() {
		return super.get("fddbr");
	}

	public void setFddbr(String fddbr) {
		super.set("fddbr", fddbr);
	}

	/**
	 * 联系电话
	 */
	public String getLxdh() {
		return super.get("lxdh");
	}

	public void setLxdh(String lxdh) {
		super.set("lxdh", lxdh);
	}

	/**
	 * 企业性质
	 */
	public String getQyxz() {
		return super.get("qyxz");
	}

	public void setQyxz(String qyxz) {
		super.set("qyxz", qyxz);
	}

	/**
	 * 经营区域
	 */
	public String getJyqy() {
		return super.get("jyqy");
	}

	public void setJyqy(String jyqy) {
		super.set("jyqy", jyqy);
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
	 * 经营方式
	 */
	public String getJyfs() {
		return super.get("jyfs");
	}

	public void setJyfs(String jyfs) {
		super.set("jyfs", jyfs);
	}

	/**
	 * 注册资金 （万元）
	 */
	public String getZczj() {
		return super.get("zczj");
	}

	public void setZczj(String zczj) {
		super.set("zczj", zczj);
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
	 * 客运经营范围
	 */
	public String getKyjyfw() {
		return super.get("kyjyfw");
	}

	public void setKyjyfw(String kyjyfw) {
		super.set("kyjyfw", kyjyfw);
	}

	/**
	 * 货运经营范围
	 */
	public String getHyjyfw() {
		return super.get("hyjyfw");
	}

	public void setHyjyfw(String hyjyfw) {
		super.set("hyjyfw", hyjyfw);
	}

	/**
	 * 兼营范围
	 */
	public String getJyfw() {
		return super.get("jyfw");
	}

	public void setJyfw(String jyfw) {
		super.set("jyfw", jyfw);
	}

	/**
	 * 经营期限
	 */
	public String getJyqx() {
		return super.get("jyqx");
	}

	public void setJyqx(String jyqx) {
		super.set("jyqx", jyqx);
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
	 * 开始日期
	 */
	public Date getKsrq() {
		return super.getDate("ksrq");
	}

	public void setKsrq(Date ksrq) {
		super.set("ksrq", ksrq);
	}

	/**
	 * 截止日期
	 */
	public Date getJzrq() {
		return super.getDate("jzrq");
	}

	public void setJzrq(Date jzrq) {
		super.set("jzrq", jzrq);
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
	 * 批准机关及文号
	 */
	public Date getPzjg() {
		return super.getDate("pzjg");
	}

	public void setPzjg(Date pzjg) {
		super.set("pzjg", pzjg);
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
	 * 版本
	 */
	public String getVersion() {
		return super.get("version");
	}

	public void setVersion(String version) {
		super.set("version", version);
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
	 * 是否注销
	 */
	public String getIs_cancel() {
		return super.get("is_cancel");
	}

	public void setIs_cancel(String is_cancel) {
		super.set("is_cancel", is_cancel);
	}

}