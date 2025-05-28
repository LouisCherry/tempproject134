package com.epoint.xmz.audittaskjn.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 事项个性化表实体
 *
 * @version [版本号, 2023-11-01 14:01:22]
 * @作者 Administrator
 */
@Entity(table = "audit_task_jn", id = "rowguid")
public class AuditTaskJn extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

    /**
     * 是否划转
     */
    public String getIs_hz() {
        return super.get("is_hz");
    }

    public void setIs_hz(String is_hz) {
        super.set("is_hz", is_hz);
    }

    /**
     * 事项编码
     */
    public String getItem_id() {
        return super.get("item_id");
    }

    public void setItem_id(String item_id) {
        super.set("item_id", item_id);
    }

    /**
     * 监管认领部门标识
     */
    public String getJg_ouguid() {
        return super.get("jg_ouguid");
    }

    public void setJg_ouguid(String jg_ouguid) {
        super.set("jg_ouguid", jg_ouguid);
    }

    /**
     * 监管认领部门
     */
    public String getJg_ouname() {
        return super.get("jg_ouname");
    }

    public void setJg_ouname(String jg_ouname) {
        super.set("jg_ouname", jg_ouname);
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
     * 监管人员姓名
     */
    public String getJg_username() {
        return super.get("jg_username");
    }

    public void setJg_username(String jg_username) {
        super.set("jg_username", jg_username);
    }

    /**
     * 监管人员
     */
    public String getJg_userguid() {
        return super.get("jg_userguid");
    }

    public void setJg_userguid(String jg_userguid) {
        super.set("jg_userguid", jg_userguid);
    }

    /**
     * 事项部门
     */
    public String getOuguid() {
        return super.get("ouguid");
    }

    public void setOuguid(String ouguid) {
        super.set("ouguid", ouguid);
    }
}
