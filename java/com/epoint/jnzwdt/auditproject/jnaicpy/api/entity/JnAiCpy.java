package com.epoint.jnzwdt.auditproject.jnaicpy.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 成品油零售经营企业库实体
 * 
 * @作者 18039505500
 * @version [版本号, 2020-06-23 19:57:28]
 */
@Entity(table = "jn_ai_cpy", id = "rowguid")
public class JnAiCpy extends BaseEntity implements Cloneable {
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
	 * 企业名称
	 */
	public String getName() {
		return super.get("name");
	}

	public void setName(String name) {
		super.set("name", name);
	}

	/**
	 * 地址
	 */
	public String getAddress() {
		return super.get("address");
	}

	public void setAddress(String address) {
		super.set("address", address);
	}

	/**
	 * 法定代表人(企业负责人)
	 */
	public String getLegal() {
		return super.get("legal");
	}

	public void setLegal(String legal) {
		super.set("legal", legal);
	}

	/**
	 * 统一社会信用代码
	 */
	public String getCode() {
		return super.get("code");
	}

	public void setCode(String code) {
		super.set("code", code);
	}

	/**
	 * 证书编号
	 */
	public String getCertno() {
		return super.get("certno");
	}

	public void setCertno(String certno) {
		super.set("certno", certno);
	}

	/**
	 * 发证机关
	 */
	public String getCertificate() {
		return super.get("certificate");
	}

	public void setCertificate(String certificate) {
		super.set("certificate", certificate);
	}

	/**
	 * 发证日期
	 */
	public Date getCertdate() {
		return super.getDate("certdate");
	}

	public void setCertdate(Date certdate) {
		super.set("certdate", certdate);
	}

	/**
	 * 有效期
	 */
	public String getValiditytime() {
		return super.get("validitytime");
	}

	public void setValiditytime(String validitytime) {
		super.set("validitytime", validitytime);
	}

	/**
	 * 备注
	 */
	public String getRemark() {
		return super.get("remark");
	}

	public void setRemark(String remark) {
		super.set("remark", remark);
	}

	/**
	 * 版本
	 */
	public Integer getVersion() {
		return super.getInt("version");
	}

	public void setVersion(Integer version) {
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

}