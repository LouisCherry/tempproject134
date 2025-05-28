package com.epoint.auditspitem.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 项目信息表实体
 * 
 * @作者 lzhming
 * @version [版本号, 2023-03-17 09:30:48]
 */
@Entity(table = "AUDIT_SP_ITEM", id = "rowguid")
public class AuditSpItem extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

    /**
     * 建设内容及规模
     */
    public String getBuildcontent() {
        return super.get("buildcontent");
    }

    public void setBuildcontent(String buildcontent) {
        super.set("buildcontent", buildcontent);
    }

    /**
     * 建设结束时间
     */
    public Date getBuildendtime() {
        return super.getDate("buildendtime");
    }

    public void setBuildendtime(Date buildendtime) {
        super.set("buildendtime", buildendtime);
    }

    /**
     * 建设开始时间
     */
    public Date getBuildstarttime() {
        return super.getDate("buildstarttime");
    }

    public void setBuildstarttime(Date buildstarttime) {
        super.set("buildstarttime", buildstarttime);
    }

    /**
     * 建设单位
     */
    public String getBuildunit() {
        return super.get("buildunit");
    }

    public void setBuildunit(String buildunit) {
        super.set("buildunit", buildunit);
    }

    /**
     * 项目代码
     */
    public String getItemcode() {
        return super.get("itemcode");
    }

    public void setItemcode(String itemcode) {
        super.set("itemcode", itemcode);
    }

    /**
     * 项目流程信息
     */
    public String getItemflowinfo() {
        return super.get("itemflowinfo");
    }

    public void setItemflowinfo(String itemflowinfo) {
        super.set("itemflowinfo", itemflowinfo);
    }

    /**
     * 项目名称
     */
    public String getItemname() {
        return super.get("itemname");
    }

    public void setItemname(String itemname) {
        super.set("itemname", itemname);
    }

    /**
     * 政策配套信息
     */
    public String getPolicyinfo() {
        return super.get("policyinfo");
    }

    public void setPolicyinfo(String policyinfo) {
        super.set("policyinfo", policyinfo);
    }

    /**
     * 选择的阶段事项标识
     */
    public String getSelectedphasetaskid() {
        return super.get("selectedphasetaskid");
    }

    public void setSelectedphasetaskid(String selectedphasetaskid) {
        super.set("selectedphasetaskid", selectedphasetaskid);
    }

    /**
     * 选择的政策标识
     */
    public String getSelectedpolicyguid() {
        return super.get("selectedpolicyguid");
    }

    public void setSelectedpolicyguid(String selectedpolicyguid) {
        super.set("selectedpolicyguid", selectedpolicyguid);
    }

    /**
     * 建设专员
     */
    public String getServeuser() {
        return super.get("serveuser");
    }

    public void setServeuser(String serveuser) {
        super.set("serveuser", serveuser);
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

}
