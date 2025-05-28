package com.epoint.auditqueueqhjmoduletasktype.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 取号机大厅模块关联事项类别配置表实体
 * 
 * @作者 Epoint
 * @version [版本号, 2024-11-15 09:20:43]
 */
@Entity(table = "audit_queue_qhjmodule_tasktype", id = "rowguid")
public class AuditQueueQhjmoduleTasktype extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

    /**
     * 取号机模块标识
     */
    public String getQhjmoduleguid() {
        return super.get("qhjmoduleguid");
    }

    public void setQhjmoduleguid(String qhjmoduleguid) {
        super.set("qhjmoduleguid", qhjmoduleguid);
    }

    /**
     * 事项类别标识
     */
    public String getTasktypeguid() {
        return super.get("tasktypeguid");
    }

    public void setTasktypeguid(String tasktypeguid) {
        super.set("tasktypeguid", tasktypeguid);
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
