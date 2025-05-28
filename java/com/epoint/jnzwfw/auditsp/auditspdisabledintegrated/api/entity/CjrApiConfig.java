package com.epoint.jnzwfw.auditsp.auditspdisabledintegrated.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 残疾人接口配置表实体
 *
 * @作者 ez
 * @version [版本号, 2021-04-13 18:23:40]
 */
@Entity(table = "cjr_api_config", id = "rowguid")
public class CjrApiConfig extends BaseEntity implements Cloneable {
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
	 * userguid
	 */
	public String getUserguid() {
		return super.get("userguid");
	}

	public void setUserguid(String userguid) {
		super.set("userguid", userguid);
	}

	/**
	 * username
	 */
	public String getUsername() {
		return super.get("username");
	}

	public void setUsername(String username) {
		super.set("username", username);
	}

	/**
	 * password
	 */
	public String getPassword() {
		return super.get("password");
	}

	public void setPassword(String password) {
		super.set("password", password);
	}

	public String getAccountName() {
		return super.get("accountname");
	}
}
