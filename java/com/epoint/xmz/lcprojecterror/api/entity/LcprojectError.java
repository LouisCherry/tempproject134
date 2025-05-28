package com.epoint.xmz.lcprojecterror.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 浪潮推送失败记录表实体
 * 
 * @作者 1
 * @version [版本号, 2021-06-29 10:03:18]
 */
@Entity(table = "lcproject_error", id = "rowguid")
public class LcprojectError extends BaseEntity implements Cloneable {
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
	 * 申请人名称
	 */
	public String getApplyername() {
		return super.get("applyername");
	}

	public void setApplyername(String applyername) {
		super.set("applyername", applyername);
	}

	/**
	 * 接件时间
	 */
	public Date getAccepttime() {
		return super.getDate("accepttime");
	}

	public void setAccepttime(Date accepttime) {
		super.set("accepttime", accepttime);
	}

	/**
	 * 是否推送成功
	 */
	public String getStatus() {
		return super.get("status");
	}

	public void setStatus(String status) {
		super.set("status", status);
	}

	/**
	 * 办件标识
	 */
	public String getProjectguid() {
		return super.get("projectguid");
	}

	public void setProjectguid(String projectguid) {
		super.set("projectguid", projectguid);
	}

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
	 * 办件编号
	 */
	public String getFlowsn() {
		return super.get("flowsn");
	}

	public void setFlowsn(String flowsn) {
		super.set("flowsn", flowsn);
	}

}