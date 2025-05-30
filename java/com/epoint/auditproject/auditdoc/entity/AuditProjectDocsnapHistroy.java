package com.epoint.auditproject.auditdoc.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * (AuditProjectDocsnapHistroy)实体类
 *
 * @author panshunxing
 * @since 2024-12-25 19:41:13
 */
@Entity(table = "audit_project_docsnap_histroy", id = "rowguid")
public class AuditProjectDocsnapHistroy extends BaseEntity implements Cloneable {
    private static final long serialVersionUID = -11641529018428711L;


    public String getBelongxiaqucode() {
        return super.get("belongxiaqucode");
    }

    public void setBelongxiaqucode(String belongxiaqucode) {
        super.set("belongxiaqucode", belongxiaqucode);
    }


    public String getOperateusername() {
        return super.get("operateusername");
    }

    public void setOperateusername(String operateusername) {
        super.set("operateusername", operateusername);
    }


    public Date getOperatedate() {
        return super.get("operatedate");
    }

    public void setOperatedate(Date operatedate) {
        super.set("operatedate", operatedate);
    }


    public Integer getRowId() {
        return super.get("rowId");
    }

    public void setRowId(Integer rowId) {
        super.set("rowId", rowId);
    }


    public String getYearflag() {
        return super.get("yearflag");
    }

    public void setYearflag(String yearflag) {
        super.set("yearflag", yearflag);
    }


    public String getRowguid() {
        return super.get("rowguid");
    }

    public void setRowguid(String rowguid) {
        super.set("rowguid", rowguid);
    }


    public String getProjectguid() {
        return super.get("projectguid");
    }

    public void setProjectguid(String projectguid) {
        super.set("projectguid", projectguid);
    }


    public String getTaskname() {
        return super.get("taskname");
    }

    public void setTaskname(String taskname) {
        super.set("taskname", taskname);
    }


    public Integer getDoctype() {
        return super.get("doctype");
    }

    public void setDoctype(Integer doctype) {
        super.set("doctype", doctype);
    }


    public String getDochtml() {
        return super.get("dochtml");
    }

    public void setDochtml(String dochtml) {
        super.set("dochtml", dochtml);
    }


    public String getDocattachguid() {
        return super.get("docattachguid");
    }

    public void setDocattachguid(String docattachguid) {
        super.set("docattachguid", docattachguid);
    }


    public Date getPrintdate() {
        return super.get("printdate");
    }

    public void setPrintdate(Date printdate) {
        super.set("printdate", printdate);
    }


    public String getPrintuserguid() {
        return super.get("printuserguid");
    }

    public void setPrintuserguid(String printuserguid) {
        super.set("printuserguid", printuserguid);
    }


    public String getSdhzflag() {
        return super.get("sdhzflag");
    }

    public void setSdhzflag(String sdhzflag) {
        super.set("sdhzflag", sdhzflag);
    }


    public String getHuizhiattachguid() {
        return super.get("huizhiattachguid");
    }

    public void setHuizhiattachguid(String huizhiattachguid) {
        super.set("huizhiattachguid", huizhiattachguid);
    }


    public String getPdfattachguid() {
        return super.get("pdfattachguid");
    }

    public void setPdfattachguid(String pdfattachguid) {
        super.set("pdfattachguid", pdfattachguid);
    }
}

