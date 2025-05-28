package com.epoint.powersupply.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 供电报装表单信息实体
 */
@Entity(table = "audit_sp_projectinfo_gdbz", id = "rowguid")
public class AuditSpProjectinfoGdbz extends BaseEntity implements Cloneable {

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
     * 操作日期
     */
    public Date getOperatedate() {
        return super.getDate("operatedate");
    }

    public void setOperatedate(Date operatedate) {
        super.set("operatedate", operatedate);
    }

    /**
     * 对应办件guid
     */
    public String getProjectGuid() {
        return super.getStr("projectguid");
    }

    public void setProjectGuid(String projectGuid) {
        super.set("projectguid", projectGuid);
    }

    /**
     * 对应subappGuid
     */
    public String getSubappGuid() {
        return super.getStr("subappGuid");
    }

    public void setSubAppGuid(String subAppGuid) {
        super.set("subappGuid", subAppGuid);
    }
}
