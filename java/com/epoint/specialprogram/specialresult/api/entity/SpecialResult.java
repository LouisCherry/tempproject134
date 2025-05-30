package com.epoint.specialprogram.specialresult.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 特别程序结果实体
 * 
 * @作者 lizhenjie
 * @version [版本号, 2020-12-23 19:34:13]
 */
@Entity(table = "special_result", id = "rowguid")
public class SpecialResult extends BaseEntity implements Cloneable {
	private static final long serialVersionUID = 1L;

	
	
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
	 * 申办流水号
	 */
	public String getSblsh_short() {
		return super.get("sblsh_short");
	}

	public void setSblsh_short(String sblsh_short) {
		super.set("sblsh_short", sblsh_short);
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
	 * 特别程序结果
	 */
	public String getTbcxjg() {
		return super.get("tbcxjg");
	}

	public void setTbcxjg(String tbcxjg) {
		super.set("tbcxjg", tbcxjg);
	}

	/**
	 * 结果产生日期
	 */
	public String getJgcsrq() {
		return super.get("jgcsrq");
	}

	public void setJgcsrq(String jgcsrq) {
		super.set("jgcsrq", jgcsrq);
	}

	/**
	 * 特别程序结束日期
	 */
	public String getTbcxjsrq() {
		return super.get("tbcxjsrq");
	}

	public void setTbcxjsrq(String tbcxjsrq) {
		super.set("tbcxjsrq", tbcxjsrq);
	}

	/**
	 * 特别程序收费金 额
	 */
	public String getTbcxsfje() {
		return super.get("tbcxsfje");
	}

	public void setTbcxsfje(String tbcxsfje) {
		super.set("tbcxsfje", tbcxsfje);
	}

	/**
	 * 金额单位代码
	 */
	public String getJedwdm() {
		return super.get("jedwdm");
	}

	public void setJedwdm(String jedwdm) {
		super.set("jedwdm", jedwdm);
	}

	/**
	 * 特别程序结果部门所在地行政区划代码
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
	 * 备用字段
	 */
	public String getByzd3() {
		return super.get("byzd3");
	}

	public void setByzd3(String byzd3) {
		super.set("byzd3", byzd3);
	}

	/**
	 * 备用字段
	 */
	public String getByzd4() {
		return super.get("byzd4");
	}

	public void setByzd4(String byzd4) {
		super.set("byzd4", byzd4);
	}

}