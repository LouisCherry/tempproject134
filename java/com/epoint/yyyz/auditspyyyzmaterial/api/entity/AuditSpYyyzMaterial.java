package com.epoint.yyyz.auditspyyyzmaterial.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 一业一证专项材料表实体
 * 
 * @作者  LYA
 * @version [版本号, 2020-06-18 21:34:52]
 */
@Entity(table = "audit_sp_yyyz_material", id = "rowguid")
public class AuditSpYyyzMaterial extends BaseEntity implements Cloneable
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
    * 专项材料标识
    */
    public String getYyyzmaterialguid() {
        return super.get("yyyzmaterialguid");
    }

    public void setYyyzmaterialguid(String yyyzmaterialguid) {
        super.set("yyyzmaterialguid", yyyzmaterialguid);
    }

    /**
    * 专项材料名称
    */
    public String getMaterialname() {
        return super.get("materialname");
    }

    public void setMaterialname(String materialname) {
        super.set("materialname", materialname);
    }

    /**
    * 当前主题标识
    */
    public String getBusinessguid() {
        return super.get("businessguid");
    }

    public void setBusinessguid(String businessguid) {
        super.set("businessguid", businessguid);
    }

    /**
    * 备注
    */
    public String getNote() {
        return super.get("note");
    }

    public void setNote(String note) {
        super.set("note", note);
    }

    /**
    * 排序
    */
    public Integer getOrdernumber() {
        return super.getInt("ordernumber");
    }

    public void setOrdernumber(Integer ordernumber) {
        super.set("ordernumber", ordernumber);
    }

    /**
    * 是否启用
    */
    public String getStatus() {
        return super.get("status");
    }

    public void setStatus(String status) {
        super.set("status", status);
    }

    /**
    * 是否必需
    */
    public String getNecessity() {
        return super.get("necessity");
    }

    public void setNecessity(String necessity) {
        super.set("necessity", necessity);
    }

    /**
    * 提交方式
    */
    public String getSubmittype() {
        return super.get("submittype");
    }

    public void setSubmittype(String submittype) {
        super.set("submittype", submittype);
    }

    /**
    * 是否容缺
    */
    public String getRongque() {
        return super.get("rongque");
    }

    public void setRongque(String rongque) {
        super.set("rongque", rongque);
    }

    /**
    * 来源渠道
    */
    public String getFile_source() {
        return super.get("file_source");
    }

    public void setFile_source(String file_source) {
        super.set("file_source", file_source);
    }

    /**
    * 事项标识
    */
    public String getTask_id() {
        return super.get("task_id");
    }

    public void setTask_id(String task_id) {
        super.set("task_id", task_id);
    }

}
