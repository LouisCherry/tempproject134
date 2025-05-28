package com.epoint.auditproject.auditrongquematerial.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 容缺材料信息表实体
 * @authory shibin
 * @version 2019年10月23日 下午7:13:04
 */
@Entity(table = "Audit_Project_Material_Rongque", id = "rowguid")
public class AuditProjectMaterialRongque extends BaseEntity implements Cloneable
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
    * 办件标识
    */
    public String getProjectguid() {
        return super.get("Projectguid");
    }

    public void setProjectguid(String Projectguid) {
        super.set("Projectguid", Projectguid);
    }

    /**
    * 材料名称
    */
    public String getMaterialname() {
        return super.get("Materialname");
    }

    public void setMaterialname(String Materialname) {
        super.set("Materialname", Materialname);
    }

    /**
    * 办件材料标识
    */
    public String getProjectMaterialguid() {
        return super.get("ProjectMaterialguid");
    }

    public void setProjectMaterialguid(String ProjectMaterialguid) {
        super.set("ProjectMaterialguid", ProjectMaterialguid);
    }

    /**
     * 事项材料标识
     */
    public String getTaskMaterialguid() {
        return super.get("TaskMaterialguid");
    }

    public void setTaskMaterialguid(String TaskMaterialguid) {
        super.set("TaskMaterialguid", TaskMaterialguid);
    }

}
