package com.epoint.auditspphasebaseinfo.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 前四阶段信息配置表实体
 * 
 * @作者 lzhming
 * @version [版本号, 2023-03-17 08:57:44]
 */
@Entity(table = "AUDIT_SP_PHASE_BASEINFO", id = "rowguid")
public class AuditSpPhaseBaseinfo extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

    /**
     * 承诺期限（日）
     */
    public Integer getDay() {
        return super.getInt("day");
    }

    public void setDay(Integer day) {
        super.set("day", day);
    }

    /**
     * 排序
     */
    public String getOrdernum() {
        return super.get("ordernum");
    }

    public void setOrdernum(String ordernum) {
        super.set("ordernum", ordernum);
    }

    /**
     * 阶段名称
     */
    public String getPhasename() {
        return super.get("phasename");
    }

    public void setPhasename(String phasename) {
        super.set("phasename", phasename);
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
     * 列数
     */
    public Integer getCols() {
        return super.get("cols");
    }

    public void setCols(Integer cols) {
        super.set("cols", cols);
    }

}
