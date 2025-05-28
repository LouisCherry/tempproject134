package com.epoint.zwzt.xxfb.xxfbinfocontent.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 信息正文内容表实体
 *
 * @author D0Be
 * @version [版本号, 2022-04-28 14:01:44]
 */
@Entity(table = "xxfb_info_content", id = "rowguid")
public class XxfbInfoContent extends BaseEntity implements Cloneable
{
	private static final long serialVersionUID = 1L;

	/**
	 * 信息内容
	 */
	public String getInfo_content() {
		return super.get("info_content");
	}

	public void setInfo_content(String info_content) {
		super.set("info_content", info_content);
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
	 * 操作人所在独立部门Guid
	 */
	public String getOperateuserbaseouguid() {
		return super.get("operateuserbaseouguid");
	}

	public void setOperateuserbaseouguid(String operateuserbaseouguid) {
		super.set("operateuserbaseouguid", operateuserbaseouguid);
	}

	/**
	 * 所属辖区编号
	 */
	public String getBelongxiaqucode() {
		return super.get("belongxiaqucode");
	}

	public void setBelongxiaqucode(String belongxiaqucode) {
		super.set("belongxiaqucode", belongxiaqucode);
	}

	/**
	 * 主键guid
	 */
	public String getRowguid() {
		return super.get("rowguid");
	}

	public void setRowguid(String rowguid) {
		super.set("rowguid", rowguid);
	}

	/**
	 * 操作人所在部门Guid
	 */
	public String getOperateuserouguid() {
		return super.get("operateuserouguid");
	}

	public void setOperateuserouguid(String operateuserouguid) {
		super.set("operateuserouguid", operateuserouguid);
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
	 * 操作人姓名
	 */
	public String getOperateusername() {
		return super.get("operateusername");
	}

	public void setOperateusername(String operateusername) {
		super.set("operateusername", operateusername);
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
	 * 操作人Guid
	 */
	public String getOperateuserguid() {
		return super.get("operateuserguid");
	}

	public void setOperateuserguid(String operateuserguid) {
		super.set("operateuserguid", operateuserguid);
	}

	/**
	 * 流程标识
	 */
	public String getPviguid() {
		return super.get("pviguid");
	}

	public void setPviguid(String pviguid) {
		super.set("pviguid", pviguid);
	}

}
