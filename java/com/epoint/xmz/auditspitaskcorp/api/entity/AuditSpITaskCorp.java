package com.epoint.xmz.auditspitaskcorp.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 并联审批事项实例单位信息表实体
 *
 * @version [版本号, 2023-10-10 15:01:07]
 * @作者 Epoint
 */
@Entity(table = "AUDIT_SP_I_TASK_CORP", id = "rowguid")
public class AuditSpITaskCorp extends BaseEntity implements Cloneable {
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
     * 单体标识
     */
    public String getCorpguid() {
        return super.get("corpguid");
    }

    public void setCorpguid(String corpguid) {
        super.set("corpguid", corpguid);
    }

    /**
     * 单位类型
     */
    public String getCorptyppe() {
        return super.get("corptyppe");
    }

    public void setCorptyppe(String corptyppe) {
        super.set("corptyppe", corptyppe);
    }

    /**
     * 事项实例标识
     */
    public String getItaskguid() {
        return super.get("itaskguid");
    }

    public void setItaskguid(String itaskguid) {
        super.set("itaskguid", itaskguid);
    }

    /**
     * 事项标识
     */
    public String getTaskguid() {
        return super.get("taskguid");
    }

    public void setTaskguid(String taskguid) {
        super.set("taskguid", taskguid);
    }

    /**
     * 子申报标识
     */
    public String getSubappguid() {
        return super.get("subappguid");
    }

    public void setSubappguid(String subappguid) {
        super.set("subappguid", subappguid);
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