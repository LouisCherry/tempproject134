package com.epoint.auditrsitembaseinfo.auditrsitembaseinfofour.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 泰安建设项目第四阶段基本信息拓展表实体
 * 
 * @作者 Administrator
 * @version [版本号, 2019-08-27 16:28:22]
 */
@Entity(table = "audit_rs_item_baseinfo_four", id = "rowguid")
public class AuditRsItemBaseinfoFour extends BaseEntity implements Cloneable {
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
	 * 立项级别
	 */
	public String getLxjb() {
		return super.get("lxjb");
	}

	public void setLxjb(String lxjb) {
		super.set("lxjb", lxjb);
	}

	/**
	 * 环保前期批复文件号
	 */
	public String getHbqqpfwjh() {
		return super.get("hbqqpfwjh");
	}

	public void setHbqqpfwjh(String hbqqpfwjh) {
		super.set("hbqqpfwjh", hbqqpfwjh);
	}

	/**
	 * 规划许可证号
	 */
	public String getGhxkzh() {
		return super.get("ghxkzh");
	}

	public void setGhxkzh(String ghxkzh) {
		super.set("ghxkzh", ghxkzh);
	}

	/**
	 * 施工许可证号
	 */
	public String getSgxkzh() {
		return super.get("sgxkzh");
	}

	public void setSgxkzh(String sgxkzh) {
		super.set("sgxkzh", sgxkzh);
	}

	/**
	 * 施工图审查机构
	 */
	public String getSgtscxjg() {
		return super.get("sgtscxjg");
	}

	public void setSgtscxjg(String sgtscxjg) {
		super.set("sgtscxjg", sgtscxjg);
	}

	/**
	 * 施工图设计文件审查合格书编号
	 */
	public String getSgtsjwjschgsbh() {
		return super.get("sgtsjwjschgsbh");
	}

	public void setSgtsjwjschgsbh(String sgtsjwjschgsbh) {
		super.set("sgtsjwjschgsbh", sgtsjwjschgsbh);
	}

	/**
	 * 消防审核意见书文号
	 */
	public String getXfshyjswh() {
		return super.get("xfshyjswh");
	}

	public void setXfshyjswh(String xfshyjswh) {
		super.set("xfshyjswh", xfshyjswh);
	}

	/**
	 * 水土保持方案审批文号
	 */
	public String getStbcfaspwh() {
		return super.get("stbcfaspwh");
	}

	public void setStbcfaspwh(String stbcfaspwh) {
		super.set("stbcfaspwh", stbcfaspwh);
	}

	/**
	 * 档案责任书编号
	 */
	public String getDazrsbh() {
		return super.get("dazrsbh");
	}

	public void setDazrsbh(String dazrsbh) {
		super.set("dazrsbh", dazrsbh);
	}

	/**
	 * 阶段guid
	 */
	public String getPhaseguid() {
		return super.get("phaseguid");
	}

	public void setPhaseguid(String phaseguid) {
		super.set("phaseguid", phaseguid);
	}

	/**
	 * 主表rowguid
	 */
	public String getParentid() {
		return super.get("parentid");
	}

	public void setParentid(String parentid) {
		super.set("parentid", parentid);
	}

	/**
	 * 阶段实例guid
	 */
	public String getSubappguid() {
		return super.get("subappguid");
	}

	public void setSubappguid(String subappguid) {
		super.set("subappguid", subappguid);
	}

	/**
	 * 备注
	 */
	public String getBak() {
		return super.get("bak");
	}

	public void setBak(String bak) {
		super.set("bak", bak);
	}

}