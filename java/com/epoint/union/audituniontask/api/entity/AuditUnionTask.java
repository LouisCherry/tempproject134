package com.epoint.union.audituniontask.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 异地通办事项配置表实体
 * 
 * @作者 zhaoyan
 * @version [版本号, 2020-03-22 16:59:46]
 */
@Entity(table = "audit_union_task", id = "rowguid")
public class AuditUnionTask extends BaseEntity implements Cloneable {
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
	 * 事项task_id
	 */
	public String getTask_id() {
		return super.get("task_id");
	}

	public void setTask_id(String task_id) {
		super.set("task_id", task_id);
	}

	/**
	 * 事项名称
	 */
	public String getTaskname() {
		return super.get("taskname");
	}

	public void setTaskname(String taskname) {
		super.set("taskname", taskname);
	}

	/**
	 * 事项编码
	 */
	public String getItem_id() {
		return super.get("item_id");
	}

	public void setItem_id(String item_id) {
		super.set("item_id", item_id);
	}

	/**
	 * 部门名称
	 */
	public String getOuname() {
		return super.get("ouname");
	}

	public void setOuname(String ouname) {
		super.set("ouname", ouname);
	}
	
	/**
	 * 辖区编码
	 */
	public String getaAreacode() {
		return super.get("areacode");
	}

	public void setAreacode(String areacode) {
		super.set("areacode", areacode);
	}

	/**
	 * 部门标识
	 */
	public String getOuguid() {
		return super.get("ouguid");
	}

	public void setOuguid(String ouguid) {
		super.set("ouguid", ouguid);
	}

	/**
	 * 是否初始化
	 */
	public String getIs_init() {
		return super.get("is_init");
	}

	public void setIs_init(String is_init) {
		super.set("is_init", is_init);
	}
	
	/**
	 * 联系电话
	 */
	public String getLink_tel() {
		return super.get("link_tel");
	}

	public void setLink_tel(String link_tel) {
		super.set("link_tel", link_tel);
	}

}