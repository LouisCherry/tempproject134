package com.epoint.xmz.handhcp.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 手动推送好差评表实体
 * 
 * @作者 1
 * @version [版本号, 2022-03-29 16:57:08]
 */
@Entity(table = "hand_hcp", id = "rowguid")
public class HandHcp extends BaseEntity implements Cloneable {
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
	 * 办件编号
	 */
	public String getProjectno() {
		return super.get("projectno");
	}

	public void setProjectno(String projectno) {
		super.set("projectno", projectno);
	}

	/**
	 * 申请人姓名
	 */
	public String getName() {
		return super.get("name");
	}

	public void setName(String name) {
		super.set("name", name);
	}

	/**
	 * 评价时间
	 */
	public Date getAssesstime() {
		return super.getDate("assesstime");
	}

	public void setAssesstime(Date assesstime) {
		super.set("assesstime", assesstime);
	}

	/**
	 * 服务次数
	 */
	public String getServicenumber() {
		return super.get("servicenumber");
	}

	public void setServicenumber(String servicenumber) {
		super.set("servicenumber", servicenumber);
	}

	/**
	 * 所属辖区
	 */
	public String getAreacode() {
		return super.get("areacode");
	}

	public void setAreacode(String areacode) {
		super.set("areacode", areacode);
	}

	/**
	 * 推送日志
	 */
	public String getRecord() {
		return super.get("record");
	}

	public void setRecord(String record) {
		super.set("record", record);
	}

	/**
	 * 推送标识
	 */
	public String getSbsign() {
		return super.get("sbsign");
	}

	public void setSbsign(String sbsign) {
		super.set("sbsign", sbsign);
	}

}