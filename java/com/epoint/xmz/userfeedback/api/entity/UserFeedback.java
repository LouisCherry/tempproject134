package com.epoint.xmz.userfeedback.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 用户反馈表实体
 * 
 * @作者 1
 * @version [版本号, 2022-10-28 15:37:33]
 */
@Entity(table = "user_feedback", id = "rowguid")
public class UserFeedback extends BaseEntity implements Cloneable {
	private static final long serialVersionUID = 1L;

	/**
	 * 反馈附件
	 */
	public String getBackcliengguid() {
		return super.get("backcliengguid");
	}

	public void setBackcliengguid(String backcliengguid) {
		super.set("backcliengguid", backcliengguid);
	}

	/**
	 * 反馈时间
	 */
	public Date getBacktime() {
		return super.getDate("backtime");
	}

	public void setBacktime(Date backtime) {
		super.set("backtime", backtime);
	}

	/**
	 * 反馈内容
	 */
	public String getContent() {
		return super.get("content");
	}

	public void setContent(String content) {
		super.set("content", content);
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
	 * 反馈人
	 */
	public String getUsername() {
		return super.get("username");
	}

	public void setUsername(String username) {
		super.set("username", username);
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
	
	/**
	 * 所属辖区号
	 */
	public String getSubappguid() {
		return super.get("subappguid");
	}

	public void setSubappguid(String subappguid) {
		super.set("subappguid", subappguid);
	}

}