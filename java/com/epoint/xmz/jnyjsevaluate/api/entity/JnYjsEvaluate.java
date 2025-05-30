package com.epoint.xmz.jnyjsevaluate.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 一件事评价表实体
 * 
 * @作者 1
 * @version [版本号, 2022-11-11 14:59:29]
 */
@Entity(table = "jn_yjs_evaluate", id = "rowguid")
public class JnYjsEvaluate extends BaseEntity implements Cloneable {
	private static final long serialVersionUID = 1L;

	/**
	 * 辖区编码
	 */
	public String getAreacode() {
		return super.get("areacode");
	}

	public void setAreacode(String areacode) {
		super.set("areacode", areacode);
	}

	/**
	 * 主题标识
	 */
	public String getBusinessguid() {
		return super.get("businessguid");
	}

	public void setBusinessguid(String businessguid) {
		super.set("businessguid", businessguid);
	}

	/**
	 * 主题名称
	 */
	public String getBusinessname() {
		return super.get("businessname");
	}

	public void setBusinessname(String businessname) {
		super.set("businessname", businessname);
	}

	/**
	 * 标签
	 */
	public String getLabel() {
		return super.get("label");
	}

	public void setLabel(String label) {
		super.set("label", label);
	}

	/**
	 * 理由
	 */
	public String getReason() {
		return super.get("reason");
	}

	public void setReason(String reason) {
		super.set("reason", reason);
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
	 * 满意度
	 */
	public String getSatisfaction() {
		return super.get("satisfaction");
	}

	public void setSatisfaction(String satisfaction) {
		super.set("satisfaction", satisfaction);
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