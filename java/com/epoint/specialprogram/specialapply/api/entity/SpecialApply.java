package com.epoint.specialprogram.specialapply.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 特别程序申请实体
 * 
 * @作者 lizhenjie
 * @version [版本号, 2020-12-23 19:34:29]
 */
@Entity(table = "special_apply", id = "rowguid")
public class SpecialApply extends BaseEntity implements Cloneable {
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
	 * SBLSH_SHORT
	 */
	public String getbSlsh_short() {
		return super.get("SBLSH_SHORT");
	}

	public void setSblsh_short(String SBLSH_SHORT) {
		super.set("SBLSH_SHORT", SBLSH_SHORT);
	}

	/**
	 * 备用流水号
	 */
	public String getSblsh() {
		return super.get("sblsh");
	}

	public void setSblsh(String sblsh) {
		super.set("sblsh", sblsh);
	}

	/**
	 * 事项编码
	 */
	public String getSuspendtype() {
		return super.get("suspendtype");
	}

	public void setSuspendtype(String suspendtype) {
		super.set("suspendtype", suspendtype);
	}
	/**
	 * 事项编码
	 */
	public String getSxbm() {
		return super.get("sxbm");
	}

	public void setSxbm(String sxbm) {
		super.set("sxbm", sxbm);
	}

	/**
	 * 事项简码
	 */
	public String getSxbm_short() {
		return super.get("sxbm_short");
	}

	public void setSxbm_short(String sxbm_short) {
		super.set("sxbm_short", sxbm_short);
	}

	/**
	 * 事项情形编码
	 */
	public String getSxqxbm() {
		return super.get("sxqxbm");
	}

	public void setSxqxbm(String sxqxbm) {
		super.set("sxqxbm", sxqxbm);
	}

	/**
	 * 序号
	 */
	public String getXh() {
		return super.get("xh");
	}

	public void setXh(String xh) {
		super.set("xh", xh);
	}

	/**
	 * 特别程序种类
	 */
	public String getTbcxzl() {
		return super.get("tbcxzl");
	}

	public void setTbcxzl(String tbcxzl) {
		super.set("tbcxzl", tbcxzl);
	}

	/**
	 * 特别程序种类名
	 */
	public String getTbcxzlmc() {
		return super.get("tbcxzlmc");
	}

	public void setTbcxzlmc(String tbcxzlmc) {
		super.set("tbcxzlmc", tbcxzlmc);
	}

	/**
	 * 特别程序开始日期
	 */
	public String getTbcxksrq() {
		return super.get("tbcxksrq");
	}

	public void setTbcxksrq(String tbcxksrq) {
		super.set("tbcxksrq", tbcxksrq);
	}

	/**
	 * 特别程序批准人
	 */
	public String getTbcxpzr() {
		return super.get("tbcxpzr");
	}

	public void setTbcxpzr(String tbcxpzr) {
		super.set("tbcxpzr", tbcxpzr);
	}

	/**
	 * 特别程序启动理由或依据
	 */
	public String getTbcxqdly() {
		return super.get("tbcxqdly");
	}

	public void setTbcxqdly(String tbcxqdly) {
		super.set("tbcxqdly", tbcxqdly);
	}

	/**
	 * 申请内容
	 */
	public String getSqnr() {
		return super.get("sqnr");
	}

	public void setSqnr(String sqnr) {
		super.set("sqnr", sqnr);
	}

	/**
	 * 特别程序时限
	 */
	public String getTbcxsx() {
		return super.get("tbcxsx");
	}

	public void setTbcxsx(String tbcxsx) {
		super.set("tbcxsx", tbcxsx);
	}

	/**
	 * 特别程序时限单位
	 */
	public String getTbcxsxdw() {
		return super.get("tbcxsxdw");
	}

	public void setTbcxsxdw(String tbcxsxdw) {
		super.set("tbcxsxdw", tbcxsxdw);
	}

	/**
	 * 特别程序申请部门所在地行政区划代码
	 */
	public String getXzqhdm() {
		return super.get("xzqhdm");
	}

	public void setXzqhdm(String xzqhdm) {
		super.set("xzqhdm", xzqhdm);
	}

	/**
	 * 备注
	 */
	public String getBz() {
		return super.get("bz");
	}

	public void setBz(String bz) {
		super.set("bz", bz);
	}

	/**
	 * 处理状态
	 */
	public String getClzt() {
		return super.get("clzt");
	}

	public void setClzt(String clzt) {
		super.set("clzt", clzt);
	}

	/**
	 * 处理时间
	 */
	public String getClsj() {
		return super.get("clsj");
	}

	public void setClsj(String clsj) {
		super.set("clsj", clsj);
	}

	/**
	 * 备用字段
	 */
	public String getByzd() {
		return super.get("byzd");
	}

	public void setByzd(String byzd) {
		super.set("byzd", byzd);
	}

	/**
	 * 备用字段
	 */
	public String getByzd1() {
		return super.get("byzd1");
	}

	public void setByzd1(String byzd1) {
		super.set("byzd1", byzd1);
	}

	/**
	 * 备用字段
	 */
	public String getByzd2() {
		return super.get("byzd2");
	}

	public void setByzd2(String byzd2) {
		super.set("byzd2", byzd2);
	}

	/**
	 * 备用字段 3
	 */
	public String getByzd3() {
		return super.get("byzd3");
	}

	public void setByzd3(String byzd3) {
		super.set("byzd3", byzd3);
	}

	/**
	 * 备用字段4
	 */
	public String getByzd4() {
		return super.get("byzd4");
	}

	public void setByzd4(String byzd4) {
		super.set("byzd4", byzd4);
	}

}