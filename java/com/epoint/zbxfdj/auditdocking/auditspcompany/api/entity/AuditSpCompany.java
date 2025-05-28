package com.epoint.zbxfdj.auditdocking.auditspcompany.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 单位信息表实体
 * 
 * @author WZW
 * @version [版本号, 2022-12-07 15:04:12]
 */
@Entity(table = "AUDIT_SP_COMPANY", id = "rowguid")
public class AuditSpCompany extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

    /**
     * 工程信息标识
     */
    public String getSpprojectguid() {
        return super.get("spprojectguid");
    }

    public void setSpprojectguid(String spprojectguid) {
        super.set("spprojectguid", spprojectguid);
    }

    /**
     * 单位名称
     */
    public String getCompanyname() {
        return super.get("companyname");
    }

    public void setCompanyname(String companyname) {
        super.set("companyname", companyname);
    }

    /**
     * 单位类型
     */
    public String getCompanytype() {
        return super.get("companytype");
    }

    public void setCompanytype(String companytype) {
        super.set("companytype", companytype);
    }

    /**
     * 单位统一社会信用代码
     */
    public String getUscc() {
        return super.get("uscc");
    }

    public void setUscc(String uscc) {
        super.set("uscc", uscc);
    }

    /**
     * 设计专业
     */
    public String getDesignmajor() {
        return super.get("designmajor");
    }

    public void setDesignmajor(String designmajor) {
        super.set("designmajor", designmajor);
    }

    /**
     * 是否总包
     */
    public String getIsgeneral() {
        return super.get("isgeneral");
    }

    public void setIsgeneral(String isgeneral) {
        super.set("isgeneral", isgeneral);
    }

    /**
     * 资质等级
     */
    public String getQualificationlevel() {
        return super.get("qualificationlevel");
    }

    public void setQualificationlevel(String qualificationlevel) {
        super.set("qualificationlevel", qualificationlevel);
    }

    /**
     * 法定代表人(身份证号)
     */
    public String getLegalrepresentative() {
        return super.get("legalrepresentative");
    }

    public void setLegalrepresentative(String legalrepresentative) {
        super.set("legalrepresentative", legalrepresentative);
    }

    /**
     * 项目负责人(身份证号)
     */
    public String getPrincipal() {
        return super.get("principal");
    }

    public void setPrincipal(String principal) {
        super.set("principal", principal);
    }

    /**
     * 联系电话(移动电话和座机)
     */
    public String getContactno() {
        return super.get("contactno");
    }

    public void setContactno(String contactno) {
        super.set("contactno", contactno);
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

    /**
     * 涉及单位标识
     */
    public String getParticipantsguid() {
        return super.get("participantsguid");
    }

    public void setParticipantsguid(String participantsguid) {
        super.set("participantsguid", participantsguid);
    }
}
