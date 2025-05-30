package com.epoint.jnzwdt.auditsptasktype.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;
import java.util.Objects;

/**
 * 事项分类实体
 *
 * @version [版本号, 2024-09-22 10:56:21]
 * @作者 qichudong
 */
@Entity(table = "audit_sp_tasktype", id = "rowguid")
public class AuditSpTasktype extends BaseEntity implements Cloneable {
    private static final long serialVersionUID = 1L;

    /**
     * 所属阶段
     */
    public String getPhaseid() {
        return super.get("phaseid");
    }

    public void setPhaseid(String phaseid) {
        super.set("phaseid", phaseid);
    }

    /**
     * 事项类别名称
     */
    public String getTasktypename() {
        return super.get("tasktypename");
    }

    public void setTasktypename(String tasktypename) {
        super.set("tasktypename", tasktypename);
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

    @Override
    public boolean equals(Object o) {
        return Objects.equals(((AuditSpTasktype)o).getStr("rowguid"), super.getStr("rowguid"));
    }
}