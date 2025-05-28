package com.epoint.zbxfdj.auditdocking.auditspattach.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 附件信息表实体
 * 
 * @author WZW
 * @version [版本号, 2022-12-07 15:14:51]
 */
@Entity(table = "AUDIT_SP_ATTACH", id = "rowguid")
public class AuditSpAttach extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

	/**
	 * 附件标识
	 */
	public String getSpprojectguid() {
		return super.get("spprojectguid");
	}

	public void setSpprojectguid (String spprojectguid) {
		super.set("spprojectguid",spprojectguid);
	}

	/**
	 * 附件名称
	 */
	public String getName() {
		return super.get("name");
	}

	public void setName (String name) {
		super.set("name",name);
	}

	/**
	 * 文件URL
	 */
	public String getCode() {
		return super.get("code");
	}

	public void setCode (String code) {
		super.set("code",code);
	}

	/**
	 * 文件类型
	 */
	public String getDirtype() {
		return super.get("dirtype");
	}

	public void setDirtype (String dirtype) {
		super.set("dirtype",dirtype);
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
	 * 操作人所在独立部门Guid
	 */
	public String getOperateuserbaseouguid() {
		return super.get("operateuserbaseouguid");
	}

	public void setOperateuserbaseouguid (String operateuserbaseouguid) {
		super.set("operateuserbaseouguid",operateuserbaseouguid);
	}

	/**
	 * 所属辖区编号
	 */
	public String getBelongxiaqucode() {
		return super.get("belongxiaqucode");
	}

	public void setBelongxiaqucode (String belongxiaqucode) {
		super.set("belongxiaqucode",belongxiaqucode);
	}

	/**
	 * 主键guid
	 */
	public String getRowguid() {
		return super.get("rowguid");
	}

	public void setRowguid (String rowguid) {
		super.set("rowguid",rowguid);
	}

	/**
	 * 操作人所在部门Guid
	 */
	public String getOperateuserouguid() {
		return super.get("operateuserouguid");
	}

	public void setOperateuserouguid (String operateuserouguid) {
		super.set("operateuserouguid",operateuserouguid);
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
	 * 操作人姓名
	 */
	public String getOperateusername() {
		return super.get("operateusername");
	}

	public void setOperateusername (String operateusername) {
		super.set("operateusername",operateusername);
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
	 * 操作人Guid
	 */
	public String getOperateuserguid() {
		return super.get("operateuserguid");
	}

	public void setOperateuserguid (String operateuserguid) {
		super.set("operateuserguid",operateuserguid);
	}

	/**
	 * 流程标识
	 */
	public String getPviguid() {
		return super.get("pviguid");
	}

	public void setPviguid (String pviguid) {
		super.set("pviguid",pviguid);
	}

}