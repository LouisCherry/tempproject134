package com.epoint.auditsp.auditspjointitem.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 一件事联办事项实体
 * 
 * @作者 zyq
 * @version [版本号, 2024-12-02 17:15:40]
 */
@Entity(table = "audit_sp_jointitem", id = "rowguid")
public class AuditSpJointitem extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

    /**
     * 实施机构
     */
    public String getAgency() {
        return super.get("agency");
    }

    public void setAgency(String agency) {
        super.set("agency", agency);
    }

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
     * 设定依据
     */
    public String getBylaw() {
        return super.get("bylaw");
    }

    public void setBylaw(String bylaw) {
        super.set("bylaw", bylaw);
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
