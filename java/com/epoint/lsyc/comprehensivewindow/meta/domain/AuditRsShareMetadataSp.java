package com.epoint.lsyc.comprehensivewindow.meta.domain;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

@Entity(table = "AUDIT_RS_SHARE_METADATA_SP", id = {"rowguid" })
public class AuditRsShareMetadataSp extends BaseEntity implements Cloneable {
    private static final long serialVersionUID = 1L;

    public String getDatasource_codename() {
        return (String) super.get("datasource_codename");
    }

    public void setDatasource_codename(String datasource_codename) {
        super.set("datasource_codename", datasource_codename);
    }

    public Integer getControlwidth() {
        return super.getInt("controlwidth");
    }

    public void setControlwidth(Integer controlwidth) {
        super.set("controlwidth", controlwidth);
    }

    public String getFielddisplaytype() {
        return (String) super.get("fielddisplaytype");
    }

    public void setFielddisplaytype(String fielddisplaytype) {
        super.set("fielddisplaytype", fielddisplaytype);
    }

    public Integer getOrdernum() {
        return super.getInt("ordernum");
    }

    public void setOrdernum(Integer ordernum) {
        super.set("ordernum", ordernum);
    }

    public String getBelongxiaqucode() {
        return (String) super.get("belongxiaqucode");
    }

    public void setBelongxiaqucode(String belongxiaqucode) {
        super.set("belongxiaqucode", belongxiaqucode);
    }

    public String getNotnull() {
        return (String) super.get("notnull");
    }

    public void setNotnull(String notnull) {
        super.set("notnull", notnull);
    }

    public String getOperateusername() {
        return (String) super.get("operateusername");
    }

    public void setOperateusername(String operateusername) {
        super.set("operateusername", operateusername);
    }

    public String getFieldchinesename() {
        return (String) super.get("fieldchinesename");
    }

    public void setFieldchinesename(String fieldchinesename) {
        super.set("fieldchinesename", fieldchinesename);
    }

    public Date getOperatedate() {
        return super.getDate("operatedate");
    }

    public void setOperatedate(Date operatedate) {
        super.set("operatedate", operatedate);
    }

    public String getFieldtype() {
        return (String) super.get("fieldtype");
    }

    public void setFieldtype(String fieldtype) {
        super.set("fieldtype", fieldtype);
    }

    public Integer getRow_id() {
        return super.getInt("row_id");
    }

    public void setRow_id(Integer row_id) {
        super.set("row_id", row_id);
    }

    public String getFieldname() {
        return (String) super.get("fieldname");
    }

    public void setFieldname(String fieldname) {
        super.set("fieldname", fieldname);
    }

    public String getYearflag() {
        return (String) super.get("yearflag");
    }

    public void setYearflag(String yearflag) {
        super.set("yearflag", yearflag);
    }

    public String getParentguid() {
        return (String) super.get("parentguid");
    }

    public void setParentguid(String parentguid) {
        super.set("parentguid", parentguid);
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

    public String getDispInadd() {
        return (String) super.get("dispinadd");
    }

    public void setDisplayInadd(String dispinadd) {
        super.set("dispinadd", dispinadd);
    }

    public String getIsRelateSubTable() {
        return (String) super.get("isrelatesubtable");
    }

    public void setIsRelateSubTable(String isrelatesubtable) {
        super.set("isrelatesubtable", isrelatesubtable);
    }

    public Integer getMaxLength() {
        return (Integer) super.get("maxlength");
    }

    public void setMaxLength(Integer maxlength) {
        super.set("maxlength", maxlength);
    }

    public String getValidation() {
        return (String) super.get("validation");
    }

    public void setValidation(String validation) {
        super.set("validation", validation);
    }

    public String getDateformat() {
        return (String) super.get("dateformat");
    }

    public void setDateformat(String dateformat) {
        super.set("dateformat", dateformat);
    }

    public String getDispinforms() {
        return (String) super.get("dispinforms");
    }

    public void setDispinforms(String dispinforms) {
        super.set("dispinforms", dispinforms);
    }

    public String getIsrelatetable() {
        return (String) super.get("isrelatetable");
    }

    public void setIsrelatetable(String isrelatetable) {
        super.set("isrelatetable", isrelatetable);
    }

    public String getRelatetablefield() {
        return (String) super.get("relatetablefield");
    }

    public void setRelatetablefield(String relatetablefield) {
        super.set("relatetablefield", relatetablefield);
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

    public int getIsshare() {
        return super.getInt("isshare");
    }

    public void setIsshare(int isshare) {
        super.set("isshare", isshare);
    }

    public String getEffictiverange() {
        return (String) super.get("effictiverange");
    }

    public void setEffictiverange(String effictiverange) {
        super.set("effictiverange", effictiverange);
    }

    public String getLinkfile() {
        return (String) super.get("linkfile");
    }

    public void setLinkfile(String linkfile) {
        super.set("linkfile", linkfile);
    }
    
    public String getClassguid() {
        return (String) super.get("classguid");
    }

    public void setClassguid(String classguid) {
        super.set("classguid", classguid);
    }
}
