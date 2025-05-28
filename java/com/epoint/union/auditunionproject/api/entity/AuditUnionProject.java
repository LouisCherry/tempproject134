package com.epoint.union.auditunionproject.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 异地通办办件信息表实体
 * 
 * @作者 zhaoyan
 * @version [版本号, 2020-03-22 11:18:36]
 */
@Entity(table = "audit_union_project", id = "rowguid")
public class AuditUnionProject extends BaseEntity implements Cloneable {
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
	 * 办件名称
	 */
	public String getProjectname() {
		return super.get("projectname");
	}

	public void setProjectname(String projectname) {
		super.set("projectname", projectname);
	}

	/**
	 * 事项标识
	 */
	public String getTask_id() {
		return super.get("task_id");
	}

	public void setTask_id(String task_id) {
		super.set("task_id", task_id);
	}

	/**
	 * 事项唯一标识
	 */
	public String getTaskguid() {
		return super.get("taskguid");
	}

	public void setTaskguid(String taskguid) {
		super.set("taskguid", taskguid);
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

	/**
	 * 申请人姓名
	 */
	public String getApplyername() {
		return super.get("applyername");
	}

	public void setApplyername(String applyername) {
		super.set("applyername", applyername);
	}

	/**
	 * 申请人类型
	 */
	public String getApplyertype() {
		return super.get("applyertype");
	}

	public void setApplyertype(String applyertype) {
		super.set("applyertype", applyertype);
	}

	/**
	 * 申请时间
	 */
	public Date getApplydate() {
		return super.getDate("applydate");
	}

	public void setApplydate(Date applydate) {
		super.set("applydate", applydate);
	}
	
	/**
	 * 办结时间
	 */
	public Date getBanjiedate() {
		return super.getDate("banjiedate");
	}

	public void setBanjiedate(Date banjiedate) {
		super.set("banjiedate", banjiedate);
	}

	/**
	 * 申请人证件编号
	 */
	public String getCertnum() {
		return super.get("certnum");
	}

	public void setCertnum(String certnum) {
		super.set("certnum", certnum);
	}

	/**
	 * 证件类型
	 */
	public String getCerttype() {
		return super.get("certtype");
	}

	public void setCerttype(String certtype) {
		super.set("certtype", certtype);
	}

	/**
	 * 地址
	 */
	public String getAddress() {
		return super.get("address");
	}

	public void setAddress(String address) {
		super.set("address", address);
	}

	/**
	 * 是否异地提交
	 */
	public String getIs_submit() {
		return super.get("is_submit");
	}

	public void setIs_submit(String is_submit) {
		super.set("is_submit", is_submit);
	}

	/**
	 * 联系电话
	 */
	public String getContactphone() {
		return super.get("contactphone");
	}

	public void setContactphone(String contactphone) {
		super.set("contactphone", contactphone);
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
	 * 操作理由
	 */
	public String getReason() {
		return super.get("reason");
	}

	public void setReason(String reason) {
		super.set("reason", reason);
	}
	
	/**
	 * 办件状态
	 */
	public Integer getStatus() {
		return super.get("status");
	}

	public void setStatus(Integer status) {
		super.set("status", status);
	}

}