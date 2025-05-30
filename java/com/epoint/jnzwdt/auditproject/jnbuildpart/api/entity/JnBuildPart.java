package com.epoint.jnzwdt.auditproject.jnbuildpart.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 建筑业企业资质数据库实体
 * 
 * @作者 86180
 * @version [版本号, 2020-04-15 11:11:06]
 */
@Entity(table = "jn_build_part", id = "rowguid")
public class JnBuildPart extends BaseEntity implements Cloneable {
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
	public String getItemname() {
		return super.get("itemname");
	}

	public void setItemname(String itemname) {
		super.set("itemname", itemname);
	}

	/**
	 * 详细地址
	 */
	public String getAddress() {
		return super.get("address");
	}

	public void setAddress(String address) {
		super.set("address", address);
	}

	/**
	 * 法定代表人
	 */
	public String getLegal() {
		return super.get("legal");
	}

	public void setLegal(String legal) {
		super.set("legal", legal);
	}

	/**
	 * 经济性质
	 */
	public String getEcon() {
		return super.get("econ");
	}

	public void setEcon(String econ) {
		super.set("econ", econ);
	}

	/**
	 * 注册资本
	 */
	public String getRegister() {
		return super.get("register");
	}

	public void setRegister(String register) {
		super.set("register", register);
	}

	/**
	 * 统一社会信用代码(或营业执照注册号)
	 */
	public String getCode() {
		return super.get("code");
	}

	public void setCode(String code) {
		super.set("code", code);
	}

	/**
	 * 资质类别及等级
	 */
	public String getGrade() {
		return super.get("grade");
	}

	public void setGrade(String grade) {
		super.set("grade", grade);
	}

	/**
	 * 证书编号
	 */
	public String getItemcode() {
		return super.get("itemcode");
	}

	public void setItemcode(String itemcode) {
		super.set("itemcode", itemcode);
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
	public Date getValiditytime() {
		return super.getDate("validitytime");
	}

	public void setValiditytime(Date validitytime) {
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

}