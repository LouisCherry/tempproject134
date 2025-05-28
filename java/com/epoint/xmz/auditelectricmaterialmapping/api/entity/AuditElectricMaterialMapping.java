package com.epoint.xmz.auditelectricmaterialmapping.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 电力材料映射表实体
 *
 * @version [版本号, 2023-08-10 15:24:08]
 * @作者 lee
 */
@Entity(table = "audit_electric_material_mapping", id = "rowguid")
public class AuditElectricMaterialMapping extends BaseEntity implements Cloneable {
    private static final long serialVersionUID = 1L;

    /**
     * 材料来源代码
     */
    public String getUUID() {
        return super.get("UUID");
    }

    public void setUUID(String UUID) {
        super.set("UUID", UUID);
    }

    /**
     * 事项编码
     */
    public String getItemid() {
        return super.get("itemid");
    }

    public void setItemid(String itemid) {
        super.set("itemid", itemid);
    }

    /**
     * 材料id
     */
    public String getMaterialid() {
        return super.get("materialid");
    }

    public void setMaterialid(String materialid) {
        super.set("materialid", materialid);
    }

    /**
     * 部门
     */
    public String getOuname() {
        return super.get("ouname");
    }

    public void setOuname(String ouname) {
        super.set("ouname", ouname);
    }

    /**
     * 事项名称
     */
    public String getTaskname() {
        return super.get("taskname");
    }

    public void setTaskname(String taskname) {
        super.set("taskname", taskname);
    }

    /**
     * 更新时间
     */
    public Date getUpdatetime() {
        return super.getDate("updatetime");
    }

    public void setUpdatetime(Date updatetime) {
        super.set("updatetime", updatetime);
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