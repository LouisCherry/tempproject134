package com.epoint.znsb.znsbclientarea.domain;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import java.util.Date;

@Entity(table = "audit_znsb_client_area", id = {"rowguid"})
public class ZnsbClientArea extends BaseEntity implements Cloneable {
    private static final long serialVersionUID = 1L;

    public String getClientname() {
        return (String) super.get("clientname");
    }

    public void setClientname(String clientname) {
        super.set("clientname", clientname);
    }

    public String getClientfilename() {
        return (String) super.get("clientfilename");
    }

    public void setClientfilename(String clientfilename) {
        super.set("clientfilename", clientfilename);
    }

    public String getPlugintype() {
        return (String) super.get("plugintype");
    }

    public void setPlugintype(String plugintype) {
        super.set("plugintype", plugintype);
    }

    public String getCommontype() {
        return (String) super.get("commontype");
    }

    public void setCommontype(String commontype) {
        super.set("commontype", commontype);
    }

    public String getVersion() {
        return (String) super.get("version");
    }

    public void setVersion(String version) {
        super.set("version", version);
    }

    public String getRemark() {
        return (String) super.get("remark");
    }

    public void setRemark(String remark) {
        super.set("remark", remark);
    }

    public String getAreacode() {
        return (String) super.get("areacode");
    }

    public void setAreacode(String areacode) {
        super.set("areacode", areacode);
    }

    public String getClientguid() {
        return (String) super.get("clientguid");
    }

    public void setClientguid(String clientguid) {
        super.set("clientguid", clientguid);
    }

    public String getPviguid() {
        return (String) super.get("pviguid");
    }

    public void setPviguid(String pviguid) {
        super.set("pviguid", pviguid);
    }

    public String getOperateuserbaseouguid() {
        return (String) super.get("operateuserbaseouguid");
    }

    public void setOperateuserbaseouguid(String operateuserbaseouguid) {
        super.set("operateuserbaseouguid", operateuserbaseouguid);
    }

    public String getOperateuserouguid() {
        return (String) super.get("operateuserouguid");
    }

    public void setOperateuserouguid(String operateuserouguid) {
        super.set("operateuserouguid", operateuserouguid);
    }

    public String getOperateuserguid() {
        return (String) super.get("operateuserguid");
    }

    public void setOperateuserguid(String operateuserguid) {
        super.set("operateuserguid", operateuserguid);
    }

    public String getBelongxiaqucode() {
        return (String) super.get("belongxiaqucode");
    }

    public void setBelongxiaqucode(String belongxiaqucode) {
        super.set("belongxiaqucode", belongxiaqucode);
    }

    public String getOperateusername() {
        return (String) super.get("operateusername");
    }

    public void setOperateusername(String operateusername) {
        super.set("operateusername", operateusername);
    }

    public Date getOperatedate() {
        return super.getDate("operatedate");
    }

    public void setOperatedate(Date operatedate) {
        super.set("operatedate", operatedate);
    }

    public Integer getRow_id() {
        return super.getInt("row_id");
    }

    public void setRow_id(Integer row_id) {
        super.set("row_id", row_id);
    }

    public String getYearflag() {
        return (String) super.get("yearflag");
    }

    public void setYearflag(String yearflag) {
        super.set("yearflag", yearflag);
    }

    public String getRowguid() {
        return (String) super.get("rowguid");
    }

    public void setRowguid(String rowguid) {
        super.set("rowguid", rowguid);
    }

    public String getAttachguid() {
        return (String) super.get("attachguid");
    }

    public void setAttachguid(String attachguid) {
        super.set("attachguid", attachguid);
    }
}