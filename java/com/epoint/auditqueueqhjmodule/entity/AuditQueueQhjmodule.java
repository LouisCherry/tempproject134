package com.epoint.auditqueueqhjmodule.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 取号机大厅模块配置表实体
 * 
 * @作者 Epoint
 * @version [版本号, 2024-11-14 09:51:07]
 */
@Entity(table = "audit_queue_qhjmodule", id = "rowguid")
public class AuditQueueQhjmodule extends BaseEntity implements Cloneable
{

    private static final long serialVersionUID = 1L;

    /**
     * 背景图片选择
     */
    public String getIsshowou() {
        return super.get("isshowou");
    }

    public void setIsshowou(String isshowou) {
        super.set("isshowou", isshowou);
    }

    /**
     * 背景图片选择
     */
    public String getBgcls() {
        return super.get("bgcls");
    }

    public void setBgcls(String bgcls) {
        super.set("bgcls", bgcls);
    }

    /**
     * 关联中心标识
     */
    public String getCenterguid() {
        return super.get("centerguid");
    }

    public void setCenterguid(String centerguid) {
        super.set("centerguid", centerguid);
    }

    /**
     * 模块名称
     */
    public String getmodulename() {
        return super.get("modulename");
    }

    public void setmodulename(String modulename) {
        super.set("modulename", modulename);
    }

    /**
     * 排序号
     */
    public Integer getOrdernum() {
        return super.getInt("ordernum");
    }

    public void setOrdernum(Integer ordernum) {
        super.set("ordernum", ordernum);
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
