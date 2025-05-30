package com.epoint.hcp.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 好差评用户情况表实体
 * 
 * @author jizhi7
 * @version [版本号, 2019-12-18 12:25:44]
 */
@Entity(table = "Audit_Hcp_UserInfo", id = "rowguid")
public class AuditHcpUserinfo extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

	/**
	 * 辖区编码
	 */
	
	public String getAreacode() {
		return super.get("areacode");
	}

	public void setAreacode (String areacode) {
		super.set("areacode",areacode);
	}

	/**
	 * 流程实例标识
	 */
	
	public String getPviguid() {
		return super.get("pviguid");
	}

	public void setPviguid (String pviguid) {
		super.set("pviguid",pviguid);
	}

	/**
	 * 操作人所属单位guid
	 */
	
	public String getOperateuserbaseouguid() {
		return super.get("operateuserbaseouguid");
	}

	public void setOperateuserbaseouguid (String operateuserbaseouguid) {
		super.set("operateuserbaseouguid",operateuserbaseouguid);
	}

	/**
	 * 操作人所属部门guid
	 */
	
	public String getOperateuserouguid() {
		return super.get("operateuserouguid");
	}

	public void setOperateuserouguid (String operateuserouguid) {
		super.set("operateuserouguid",operateuserouguid);
	}

	/**
	 * 操作人guid
	 */
	
	public String getOperateuserguid() {
		return super.get("operateuserguid");
	}

	public void setOperateuserguid (String operateuserguid) {
		super.set("operateuserguid",operateuserguid);
	}

	/**
	 * 所属辖区号
	 */
	
	public String getBelongxiaqucode() {
		return super.get("belongxiaqucode");
	}

	public void setBelongxiaqucode (String belongxiaqucode) {
		super.set("belongxiaqucode",belongxiaqucode);
	}

	/**
	 * 操作者名字
	 */
	
	public String getOperateusername() {
		return super.get("operateusername");
	}

	public void setOperateusername (String operateusername) {
		super.set("operateusername",operateusername);
	}

	/**
	 * 操作日期
	 */
	public Date getOperatedate() {
		return super.getDate("operatedate");
	}

	public void setOperatedate (Date operatedate) {
		super.set("operatedate",operatedate);
	}

	/**
	 * 序号
	 */
	public Integer getRow_id() {
		return super.getInt("row_id");
	}

	public void setRow_id (Integer row_id) {
		super.set("row_id",row_id);
	}

	/**
	 * 年份标识
	 */
	
	public String getYearflag() {
		return super.get("yearflag");
	}

	public void setYearflag (String yearflag) {
		super.set("yearflag",yearflag);
	}

	/**
	 * 默认主键字段
	 */
	
	public String getRowguid() {
		return super.get("rowguid");
	}

	public void setRowguid (String rowguid) {
		super.set("rowguid",rowguid);
	}

	/**
	 * 申请人名称
	 */
	
	public String getApplyername() {
		return super.get("applyername");
	}

	public void setApplyername (String applyername) {
		super.set("applyername",applyername);
	}

	/**
	 * 事项名称
	 */
	public String getTaskname() {
		return super.get("taskname");
	}

	public void setTaskname (String taskname) {
		super.set("taskname",taskname);
	}

	/**
	 * 部门名称
	 */
	
	public String getProdepart() {
		return super.get("prodepart");
	}

	public void setProdepart (String prodepart) {
		super.set("prodepart",prodepart);
	}

	/**
	 * 满意度（*数量）
	 */
	
	public String getEvalevel() {
		return super.get("evalevel");
	}

	public void setEvalevel (String evalevel) {
		super.set("evalevel",evalevel);
	}

	/**
	 * 评价时间
	 */
	public Date getCreatedate() {
		return super.getDate("createdate");
	}

	public void setCreatedate (Date createdate) {
		super.set("createdate",createdate);
	}

}