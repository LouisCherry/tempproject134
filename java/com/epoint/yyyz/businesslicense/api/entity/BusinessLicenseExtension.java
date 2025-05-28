package com.epoint.yyyz.businesslicense.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 一业一证基本信息表实体
 * @description
 * @author shibin
 * @date  2020年5月19日 上午10:56:28
 */
@Entity(table = "businesslicense_extension", id = "rowguid")
public class BusinessLicenseExtension extends BaseEntity implements Cloneable
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
    * 基本信息表标识
    */
    public String getBaseinfoGuid() {
        return super.get("baseinfoGuid");
    }

    public void setBaseinfoGuid(String baseinfoGuid) {
        super.set("baseinfoGuid", baseinfoGuid);
    }

    /**
     * 事项信息
     */
    public String getSelectItem() {
        return super.get("selectItem");
    }

    public void setSelectItem(String selectItem) {
        super.set("selectItem", selectItem);
    }

    /**
     * 材料信息
     */
    public String getMaterialsInfo() {
        return super.get("materialsInfo");
    }

    public void setMaterialsInfo(String materialsInfo) {
        super.set("materialsInfo", materialsInfo);
    }

    /**
     * 通用材料信息
     */
    public String getUnivMaterial() {
        return super.get("univMaterial");
    }

    public void setUnivMaterial(String univMaterial) {
        super.set("univMaterial", univMaterial);
    }

    /**
     * 表单信息
     */
    public String getFormsInfo() {
        return super.get("formsInfo");
    }

    public void setFormsInfo(String formsInfo) {
        super.set("formsInfo", formsInfo);
    }

}
