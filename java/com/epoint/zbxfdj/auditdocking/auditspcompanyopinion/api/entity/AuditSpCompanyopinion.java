package com.epoint.zbxfdj.auditdocking.auditspcompanyopinion.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 单位意见信息表实体
 * 
 * @author WZW
 * @version [版本号, 2022-12-07 15:10:17]
 */
@Entity(table = "AUDIT_SP_COMPANYOPINION", id = "rowguid")
public class AuditSpCompanyopinion extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

	/**
	 * 工程信息标识
	 */
	public String getSpprojectguid() {
		return super.get("spprojectguid");
	}

	public void setSpprojectguid (String spprojectguid) {
		super.set("spprojectguid",spprojectguid);
	}

	/**
	 * 单位名称
	 */
	public String getCompanyname() {
		return super.get("companyname");
	}

	public void setCompanyname (String companyname) {
		super.set("companyname",companyname);
	}

	/**
	 * 意见类型
	 */
	public String getOpiniontype() {
		return super.get("opiniontype");
	}

	public void setOpiniontype (String opiniontype) {
		super.set("opiniontype",opiniontype);
	}

	/**
	 * 项目负责人名称/项目总监理工程师名称/项目经理名称
	 */
	public String getPrincipal() {
		return super.get("principal");
	}

	public void setPrincipal (String principal) {
		super.set("principal",principal);
	}

	/**
	 * 技术服务机构情况/基本情况/经审查合格的消防设计文件实施情况/符合消防工程技术标准的设计文件实施情况
	 */
	public String getOpinion() {
		return super.get("opinion");
	}

	public void setOpinion (String opinion) {
		super.set("opinion",opinion);
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