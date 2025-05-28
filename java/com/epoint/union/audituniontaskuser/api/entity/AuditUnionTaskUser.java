package com.epoint.union.audituniontaskuser.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 异地通办事项人员关联表实体
 * 
 * @作者 zhaoyan
 * @version [版本号, 2020-03-22 22:39:40]
 */
@Entity(table = "audit_union_task_user", id = "rowguid")
public class AuditUnionTaskUser extends BaseEntity implements Cloneable {
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
	 * task_id
	 */
	public String getTask_id() {
		return super.get("task_id");
	}

	public void setTask_id(String task_id) {
		super.set("task_id", task_id);
	}

}