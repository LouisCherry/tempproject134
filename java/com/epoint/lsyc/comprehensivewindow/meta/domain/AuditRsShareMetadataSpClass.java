package com.epoint.lsyc.comprehensivewindow.meta.domain;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

@Entity(table = "AUDIT_RS_SHARE_METADATA_SP_CLASS", id = {"rowguid" })
public class AuditRsShareMetadataSpClass extends BaseEntity implements Cloneable {
    private static final long serialVersionUID = 1L;

    public String getOperateusername() {
        return (String) super.get("operateusername");
    }

    public void setOperateusername(String operateusername) {
        super.set("operateusername", operateusername);
    }

    public String getClassname() {
        return (String) super.get("classname");
    }

    public void setClassname(String classname) {
        super.set("classname", classname);
    }

    public Date getOperatedate() {
        return super.getDate("operatedate");
    }

    public void setOperatedate(Date operatedate) {
        super.set("operatedate", operatedate);
    }

    public String getRowguid() {
        return (String) super.get("rowguid");
    }

    public void setRowguid(String rowguid) {
        super.set("rowguid", rowguid);
    }

    public String getMaterialguid() {
        return (String) super.get("materialguid");
    }

    public void setMaterialguid(String materialguid) {
        super.set("materialguid", materialguid);
    }

    public String getBusinessGuid() {
        return (String) super.get("businessGuid");
    }

    public void setBusinessGuid(String businessGuid) {
        super.set("businessGuid", businessGuid);
    }

    public String getPhaseguid() {
        return (String) super.get("phaseguid");
    }

    public void setPhaseguid(String phaseguid) {
        super.set("phaseguid", phaseguid);
    }
    
    public Integer getOrdernum() {
        return (Integer) super.get("ordernum");
    }

    public void setOrdernum(Integer ordernum) {
        super.set("ordernum", ordernum);
    }
}
