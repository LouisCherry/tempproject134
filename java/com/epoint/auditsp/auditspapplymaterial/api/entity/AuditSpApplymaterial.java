package com.epoint.auditsp.auditspapplymaterial.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 一件事申报材料实体
 * 
 * @作者 zyq
 * @version [版本号, 2024-12-02 17:15:47]
 */
@Entity(table = "audit_sp_applymaterial", id = "rowguid")
public class AuditSpApplymaterial extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

    /**
     * 一件事主键
     */
    public String getBusinessguid() {
        return super.get("businessguid");
    }

    public void setBusinessguid(String businessguid) {
        super.set("businessguid", businessguid);
    }

    /**
     * 示例样本
     */
    public String getExample_guid() {
        return super.get("example_guid");
    }

    public void setExample_guid(String example_guid) {
        super.set("example_guid", example_guid);
    }

    /**
     * 材料名称
     */
    public String getMaterialname() {
        return super.get("materialname");
    }

    public void setMaterialname(String materialname) {
        super.set("materialname", materialname);
    }

    /**
     * 材料介质
     */
    public String getMaterial_form() {
        return super.get("material_form");
    }

    public void setMaterial_form(String material_form) {
        super.set("material_form", material_form);
    }

    /**
     * 材料类型
     */
    public String getMaterial_type() {
        return super.get("material_type");
    }

    public void setMaterial_type(String material_type) {
        super.set("material_type", material_type);
    }

    /**
     * 材料份数
     */
    public Integer getPage_num() {
        return super.getInt("page_num");
    }

    public void setPage_num(Integer page_num) {
        super.set("page_num", page_num);
    }

    /**
     * 来源渠道
     */
    public String getSource_type() {
        return super.get("source_type");
    }

    public void setSource_type(String source_type) {
        super.set("source_type", source_type);
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
     * 排序号
     */
    public Integer getOrdernum() {
        return super.getInt("ordernum");
    }

    public void setOrdernum(Integer ordernum) {
        super.set("ordernum", ordernum);
    }

}
