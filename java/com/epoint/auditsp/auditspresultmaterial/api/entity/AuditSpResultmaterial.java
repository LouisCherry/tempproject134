package com.epoint.auditsp.auditspresultmaterial.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 一件事结果材料实体
 * 
 * @作者 zyq
 * @version [版本号, 2024-12-02 17:15:53]
 */
@Entity(table = "audit_sp_resultmaterial", id = "rowguid")
public class AuditSpResultmaterial extends BaseEntity implements Cloneable
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
     * 结果样本
     */
    public String getResultguid() {
        return super.get("resultguid");
    }

    public void setResultguid(String resultguid) {
        super.set("resultguid", resultguid);
    }

    /**
     * 结果名称
     */
    public String getResultname() {
        return super.get("resultname");
    }

    public void setResultname(String resultname) {
        super.set("resultname", resultname);
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
