package com.epoint.cert.auditcertrelation.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 证照字段关系表实体
 *
 * @version [版本号, 2024-10-16 09:08:40]
 * @作者 miemieyang12128
 */
@Entity(table = "audit_cert_relation", id = "rowguid")
public class AuditCertRelation extends BaseEntity implements Cloneable {
    private static final long serialVersionUID = 1L;

    /**
     * 证照版本标识
     */
    public String getCertid() {
        return super.get("certid");
    }

    public void setCertid(String certid) {
        super.set("certid", certid);
    }

    /**
     * 证照名称
     */
    public String getCertname() {
        return super.get("certname");
    }

    public void setCertname(String certname) {
        super.set("certname", certname);
    }

    /**
     * 证照编号
     */
    public String getCertno() {
        return super.get("certno");
    }

    public void setCertno(String certno) {
        super.set("certno", certno);
    }

    /**
     * 创建人
     */
    public String getCreate_by() {
        return super.get("create_by");
    }

    public void setCreate_by(String create_by) {
        super.set("create_by", create_by);
    }

    /**
     * 创建时间
     */
    public Date getCreate_time() {
        return super.getDate("create_time");
    }

    public void setCreate_time(Date create_time) {
        super.set("create_time", create_time);
    }

    /**
     * 电子表单ID
     */
    public String getFormid() {
        return super.get("formid");
    }

    public void setFormid(String formid) {
        super.set("formid", formid);
    }

    /**
     * 阶段标识
     */
    public String getPhaseguid() {
        return super.get("phaseguid");
    }

    public void setPhaseguid(String phaseguid) {
        super.set("phaseguid", phaseguid);
    }

    /**
     * 证照字段和关联字段关系
     */
    public String getRelationjson() {
        return super.get("relationjson");
    }

    public void setRelationjson(String relationjson) {
        super.set("relationjson", relationjson);
    }

    /**
     * 关联类型
     */
    public String getRelationtype() {
        return super.get("relationtype");
    }

    public void setRelationtype(String relationtype) {
        super.set("relationtype", relationtype);
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
     * 是否多表合一
     */
    public String getIsdbhy() {
        return super.get("isdbhy");
    }

    public void setIsdbhy(String isdbhy) {
        super.set("isdbhy", isdbhy);
    }

    /**
     * 父表id
     */
    public String getPformid() {
        return super.get("pformid");
    }

    public void setPformid(String pformid) {
        super.set("pformid", pformid);
    }

}