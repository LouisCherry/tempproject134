package com.epoint.sghd.auditjianguan.domain;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 政策文件对应的实体
 *
 * @version [版本号, 2018年10月10日]
 * @作者 shibin
 */
@Entity(table = "audit_task_sharefile", id = {"rowguid"})
public class AuditTaskShareFile extends BaseEntity implements Cloneable {
    private static final long serialVersionUID = 1L;

    public String getRowguid() {
        return (String) super.get("rowguid");
    }

    public void setRowguid(String rowguid) {
        super.set("rowguid", rowguid);
    }

    public String getTaskguid() {
        return (String) super.get("taskguid");
    }

    public void setTaskguid(String taskguid) {
        super.set("taskguid", taskguid);
    }

    public String getTaskid() {
        return (String) super.get("taskid");
    }

    public void setTaskid(String taskid) {
        super.set("taskid", taskid);
    }

    public String getFileclientguid() {
        return (String) super.get("fileclientguid");
    }

    public void setFileclientguid(String fileclientguid) {
        super.set("fileclientguid", fileclientguid);
    }

    public String getTaskname() {
        return (String) super.get("taskname");
    }

    public void setTaskname(String taskname) {
        super.set("taskname", taskname);
    }

    public Date getCreatetime() {
        return (Date) super.get("createtime");
    }

    public void setCreatetime(Date createtime) {
        super.set("createtime", createtime);
    }

    public Date getUpdatetime() {
        return (Date) super.get("updatetime");
    }

    public void setUpdatetime(Date updatetime) {
        super.set("updatetime", updatetime);
    }

    public String getOuguid() {
        return (String) super.get("ouguid");
    }

    public void setOuguid(String ouguid) {
        super.set("ouguid", ouguid);
    }

    public String getTheme() {
        return (String) super.get("theme");
    }

    public void setTheme(String theme) {
        super.set("theme", theme);
    }

}