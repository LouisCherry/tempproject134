package com.epoint.auditproject.guiji.dto;

import com.epoint.basic.auditproject.auditprojectcharge.domain.AuditProjectCharge;
import com.epoint.basic.auditproject.auditprojectchargedetail.domain.AuditProjectChargeDetail;

import java.io.Serializable;
import java.util.List;

/**
 * 缴费dto 
 * @author 刘雨雨
 * @time 2018年8月23日下午9:27:03
 */
public class JiaoFeiDto implements Serializable {

	private static final long serialVersionUID = -3258175179318700518L;

	/**
	 * 缴费人姓名
	 */
	private String jiaofeiRenName;

	/**
	 * 缴费人身份证号
	 */
	private String jiaofeiRenCardId;

	/**
	 * 缴费人电话
	 */
	private String jiaofeiRenTel;

	/**
	 * 缴费人手机号
	 */
	private String jiaofeiRenMobile;

	/**
	 * 实收缴费金额(总)
	 */
	private Double actualTotalJine;

	/**
	 * 应收金额（总）
	 */
	private Double shouldlyTotalJine;

	/**
	 * 是否收费
	 */
	private String isCharge;

	/**
	 * 缴费记录
	 */
	private List<AuditProjectCharge> charges;

	private List<AuditProjectChargeDetail> chargeDetails;

	public String getIsCharge() {
		return isCharge;
	}

	public void setIsCharge(String isCharge) {
		this.isCharge = isCharge;
	}

	public String getJiaofeiRenName() {
		return jiaofeiRenName;
	}

	public void setJiaofeiRenName(String jiaofeiRenName) {
		this.jiaofeiRenName = jiaofeiRenName;
	}

	public String getJiaofeiRenCardId() {
		return jiaofeiRenCardId;
	}

	public void setJiaofeiRenCardId(String jiaofeiRenCardId) {
		this.jiaofeiRenCardId = jiaofeiRenCardId;
	}

	public String getJiaofeiRenTel() {
		return jiaofeiRenTel;
	}

	public void setJiaofeiRenTel(String jiaofeiRenTel) {
		this.jiaofeiRenTel = jiaofeiRenTel;
	}

	public String getJiaofeiRenMobile() {
		return jiaofeiRenMobile;
	}

	public void setJiaofeiRenMobile(String jiaofeiRenMobile) {
		this.jiaofeiRenMobile = jiaofeiRenMobile;
	}

	public Double getActualTotalJine() {
		return actualTotalJine;
	}

	public void setActualTotalJine(Double actualTotalJine) {
		this.actualTotalJine = actualTotalJine;
	}

	public Double getShouldlyTotalJine() {
		return shouldlyTotalJine;
	}

	public void setShouldlyTotalJine(Double shouldlyTotalJine) {
		this.shouldlyTotalJine = shouldlyTotalJine;
	}

	public List<AuditProjectCharge> getCharges() {
		return charges;
	}

	public void setCharges(List<AuditProjectCharge> charges) {
		this.charges = charges;
	}

	public List<AuditProjectChargeDetail> getChargeDetails() {
		return chargeDetails;
	}

	public void setChargeDetails(List<AuditProjectChargeDetail> chargeDetails) {
		this.chargeDetails = chargeDetails;
	}
}
