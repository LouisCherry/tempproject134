package com.epoint.knowledge.common.domain;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 知识库审核实体
 * 
 * @作者  xuyunhai
 * @version [版本号, 2017-02-13 10:29:05]
 */
@Entity(table = "CNS_KINFO_AUDIT", id = "rowguid")
public class CnsKinfoAudit extends BaseEntity implements Cloneable
{
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
    * 知识库主键
    */
    public String getKguid() {
        return super.get("kguid");
    }

    public void setKguid(String kguid) {
        super.set("kguid", kguid);
    }

    /**
    * 审核人
    */
    public String getAuditperson() {
        return super.get("auditperson");
    }

    public void setAuditperson(String auditperson) {
        super.set("auditperson", auditperson);
    }

    /**
    * 审核时间
    */
    public Date getAudittime() {
        return super.getDate("audittime");
    }

    public void setAudittime(Date audittime) {
        super.set("audittime", audittime);
    }

    /**
    * 审核人guid
    */
    public String getAuditpersonguid() {
        return super.get("auditpersonguid");
    }

    public void setAuditpersonguid(String auditpersonguid) {
        super.set("auditpersonguid", auditpersonguid);
    }

    /**
    * 审核意见
    */
    public String getAuditopinion() {
        return super.get("auditopinion");
    }

    public void setAuditopinion(String auditopinion) {
        super.set("auditopinion", auditopinion);
    }

    /**
    * 审核是否同意
    */
    public String getAuditresult() {
        return super.get("auditresult");
    }

    public void setAuditresult(String auditresult) {
        super.set("auditresult", auditresult);
    }

    /**
    * 申请时间
    */
    public Date getApplytime() {
        return super.getDate("applytime");
    }

    public void setApplytime(Date applytime) {
        super.set("applytime", applytime);
    }

    /**
    * 申请人
    */
    public String getApplyperson() {
        return super.get("applyperson");
    }

    public void setApplyperson(String applyperson) {
        super.set("applyperson", applyperson);
    }

    /**
    * 申请人Guid
    */
    public String getApplypersonguid() {
        return super.get("applypersonguid");
    }

    public void setApplypersonguid(String applypersonguid) {
        super.set("applypersonguid", applypersonguid);
    }

    /**
    * 申请人所在部门
    */
    public String getApplyou() {
        return super.get("applyou");
    }

    public void setApplyou(String applyou) {
        super.set("applyou", applyou);
    }

    /**
    * 申请原因
    */
    public String getApplyreason() {
        return super.get("applyreason");
    }

    public void setApplyreason(String applyreason) {
        super.set("applyreason", applyreason);
    }

}
