package com.epoint.zwdt.zwdtrest.sghd.api;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 认领记录表实体
 * 
 * @作者 zyq
 * @version [版本号, 2024-01-29 13:38:21]
 */
@Entity(table = "audit_task_jn_renling", id = "rowguid")
public class AuditTaskJnRenling extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

    /**
     * 办件标识
     */
    public String getProjectguid() {
        return super.get("projectguid");
    }

    public void setProjectguid(String projectguid) {
        super.set("projectguid", projectguid);
    }

    public String getTask_id() {
        return super.get("task_id");
    }

    public void setTask_id(String task_id) {
        super.set("task_id", task_id);
    }

    /**
     * 认领时间
     */
    public Date getRenlingdate() {
        return super.getDate("renlingdate");
    }

    public void setRenlingdate(Date renlingdate) {
        super.set("renlingdate", renlingdate);
    }

    /**
     * 认领人员部门guid
     */
    public String getRenling_ouguid() {
        return super.get("renling_ouguid");
    }

    public void setRenling_ouguid(String renling_ouguid) {
        super.set("renling_ouguid", renling_ouguid);
    }

    /**
     * 认领人员部门名称
     */
    public String getRenling_ouname() {
        return super.get("renling_ouname");
    }

    public void setRenling_ouname(String renling_ouname) {
        super.set("renling_ouname", renling_ouname);
    }

    /**
     * 认领人员guid
     */
    public String getRenling_userguid() {
        return super.get("renling_userguid");
    }

    public void setRenling_userguid(String renling_userguid) {
        super.set("renling_userguid", renling_userguid);
    }

    /**
     * 认领人员姓名
     */
    public String getRenling_username() {
        return super.get("renling_username");
    }

    public void setRenling_username(String renling_username) {
        super.set("renling_username", renling_username);
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
