package com.epoint.union.auditunionprojectmaterial.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 异地通办材料信息实体
 * 
 * @作者 zhaoyan
 * @version [版本号, 2020-03-22 11:18:43]
 */
@Entity(table = "audit_union_project_material", id = "rowguid")
public class AuditUnionProjectMaterial extends BaseEntity implements Cloneable {
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
	 * 是否必要
	 */
	public Integer getNecessity() {
		return super.getInt("necessity");
	}

	public void setNecessity(Integer necessity) {
		super.set("necessity", necessity);
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
	 * 异地通办办件标识
	 */
	public String getUnionprojectguid() {
		return super.get("unionprojectguid");
	}

	public void setUnionprojectguid(String unionprojectguid) {
		super.set("unionprojectguid", unionprojectguid);
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
	 * 材料唯一标识
	 */
	public String getTaskmaterialguid() {
		return super.get("taskmaterialguid");
	}

	public void setTaskmaterialguid(String taskmaterialguid) {
		super.set("taskmaterialguid", taskmaterialguid);
	}

	/**
	 * 材料提交方式
	 */
	public String getAuditstatus() {
		return super.get("auditstatus");
	}

	public void setAuditstatus(String auditstatus) {
		super.set("auditstatus", auditstatus);
	}

	/**
	 * 材料提交状态
	 */
	public String getStatus() {
		return super.get("status");
	}

	public void setStatus(String status) {
		super.set("status", status);
	}

	/**
	 * 材料名称
	 */
	public String getTaskmaterial() {
		return super.get("taskmaterial");
	}

	public void setTaskmaterial(String taskmaterial) {
		super.set("taskmaterial", taskmaterial);
	}

	/**
	 * 附件标识
	 */
	public String getCliengguid() {
		return super.get("cliengguid");
	}

	public void setCliengguid(String cliengguid) {
		super.set("cliengguid", cliengguid);
	}

	/**
	 * 是否容缺
	 */
	public String getIs_rongque() {
		return super.get("is_rongque");
	}

	public void setIs_rongque(String is_rongque) {
		super.set("is_rongque", is_rongque);
	}

}