package com.epoint.lsyc.comprehensivewindow.meta.domain;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

@Entity(table = "metashare", id = {"rowguid"})
public class MetaShare extends BaseEntity implements Cloneable {

    private static final long serialVersionUID = 1L;

    public String getFieldchinesename() {
        return (String) super.get("fieldchinesename");
    }

    public void setFieldchinesename(String fieldchinesename) {
        super.set("fieldchinesename", fieldchinesename);
    }

    public String getRowguid() {
        return (String) super.get("rowguid");
    }

    public void setRowguid(String rowguid) {
        super.set("rowguid", rowguid);
    }

    public String getFieldname() {
        return (String) super.get("fieldname");
    }

    public void setFieldname(String fieldname) {
        super.set("fieldname", fieldname);
    }

    public Date getOperatedate() {
        return super.getDate("operatedate");
    }

    public void setOperatedate(Date operatedate) {
        super.set("operatedate", operatedate);
    }
    public String getParentguid() {
        return (String) super.get("parentguid");
    }

    public void setParentguid(String parentguid) {
        super.set("parentguid", parentguid);
    }
    public String getParentname() {
        return (String) super.get("parentname");
    }

    public void setParentname(String parentname) {
        super.set("parentname", parentname);
    }
}
