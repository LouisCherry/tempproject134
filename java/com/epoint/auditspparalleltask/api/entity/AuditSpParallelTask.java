package com.epoint.auditspparalleltask.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 并行阶段阶段事项配置表实体
 * 
 * @作者 lzhming
 * @version [版本号, 2023-03-17 09:18:40]
 */
@Entity(table = "AUDIT_SP_PARALLEL_TASK", id = "rowguid")
public class AuditSpParallelTask extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

    /**
     * 办事指南链接
     */
    public String getGuideurl() {
        return super.get("guideurl");
    }

    public void setGuideurl(String guideurl) {
        super.set("guideurl", guideurl);
    }

    /**
     * 是否重要
     */
    public String getIsimportant() {
        return super.get("isimportant");
    }

    public void setIsimportant(String isimportant) {
        super.set("isimportant", isimportant);
    }

    /**
     * 排序
     */
    public Integer getOrdernum() {
        return super.getInt("ordernum");
    }

    public void setOrdernum(Integer ordernum) {
        super.set("ordernum", ordernum);
    }

    /**
     * 部门标识
     */
    public String getOuguid() {
        return super.get("ouguid");
    }

    public void setOuguid(String ouguid) {
        super.set("ouguid", ouguid);
    }

    /**
     * 部门名称
     */
    public String getOuname() {
        return super.get("ouname");
    }

    public void setOuname(String ouname) {
        super.set("ouname", ouname);
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
     * 承诺期限（日）
     */
    public Integer getPromise_day() {
        return super.getInt("promise_day");
    }

    public void setPromise_day(Integer promise_day) {
        super.set("promise_day", promise_day);
    }

    /**
     * 事项版本唯一标识
     */
    public String getTaskid() {
        return super.get("taskid");
    }

    public void setTaskid(String taskid) {
        super.set("taskid", taskid);
    }

    /**
     * 事项名称
     */
    public String getTaskname() {
        return super.get("taskname");
    }

    public void setTaskname(String taskname) {
        super.set("taskname", taskname);
    }

    /**
     * 阶段类型
     */
    public String getType() {
        return super.get("type");
    }

    public void setType(String type) {
        super.set("type", type);
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
