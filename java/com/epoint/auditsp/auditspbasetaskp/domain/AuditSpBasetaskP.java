package com.epoint.auditsp.auditspbasetaskp.domain;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 并联审批父事项表实体
 *
 * @version [版本号, 2019-04-25 10:07:31]
 * @作者 Administrator
 */
@Entity(table = "audit_sp_basetask_p", id = "rowguid")
public class AuditSpBasetaskP extends BaseEntity implements Cloneable {
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
     * 事项名称
     */
    public String getTasktaskname() {
        return super.get("tasktaskname");
    }

    public void setTasktaskname(String tasktaskname) {
        super.set("tasktaskname", tasktaskname);
    }

    /**
     * 事项编码
     */
    public String getTasktaskcode() {
        return super.get("tasktaskcode");
    }

    public void setTasktaskcode(String tasktaskcode) {
        super.set("tasktaskcode", tasktaskcode);
    }

    /**
     * 中央指导部门
     */
    public String getOuname() {
        return super.get("ouname");
    }

    public void setOuname(String ouname) {
        super.set("ouname", ouname);
    }

    /**
     * 审批阶段
     */
    public String getPhaseid() {
        return super.get("phaseid");
    }

    public void setPhaseid(String phaseid) {
        super.set("phaseid", phaseid);
    }

    /**
     * 创建时间
     */
    public Date getCreatedate() {
        return super.getDate("createdate");
    }

    public void setCreatedate(Date createdate) {
        super.set("createdate", createdate);
    }

}
