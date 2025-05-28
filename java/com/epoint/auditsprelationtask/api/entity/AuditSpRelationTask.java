package com.epoint.auditsprelationtask.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 前四阶段事项关系表实体
 * 
 * @作者 lzhming
 * @version [版本号, 2023-03-17 09:13:27]
 */
@Entity(table = "AUDIT_SP_RELATION_TASK", id = "rowguid")
public class AuditSpRelationTask extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

    /**
     * 后置大模块（事项组）排序
     */
    public Integer getBigordernum() {
        return super.getInt("bigordernum");
    }

    public void setBigordernum(Integer bigordernum) {
        super.set("bigordernum", bigordernum);
    }

    /**
     * 后置选中的事项标识
     */
    public String getGroupguid() {
        return super.get("groupguid");
    }

    public void setGroupguid(String groupguid) {
        super.set("groupguid", groupguid);
    }

    /**
     * 阶段值
     */
    public String getPhaseguid() {
        return super.get("phaseguid");
    }

    public void setPhaseguid(String phaseguid) {
        super.set("phaseguid", phaseguid);
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
     * 前四阶段事项配置标识
     */
    public String getPhasetaskguid() {
        return super.get("phasetaskguid");
    }

    public void setPhasetaskguid(String phasetaskguid) {
        super.set("phasetaskguid", phasetaskguid);
    }

    /**
     * 后置事项版本唯一标识
     */
    public String getTaskid() {
        return super.get("taskid");
    }

    public void setTaskid(String taskid) {
        super.set("taskid", taskid);
    }

    /**
     * 后置事项名称
     */
    public String getTaskname() {
        return super.get("taskname");
    }

    public void setTaskname(String taskname) {
        super.set("taskname", taskname);
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

    public String getPhasegroupguid() {
        return super.get("phasegroupguid");
    }

    public void setPhasegroupguid(String phasegroupguid) {
        super.set("phasegroupguid", phasegroupguid);
    }
}
