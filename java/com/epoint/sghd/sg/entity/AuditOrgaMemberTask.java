package com.epoint.sghd.sg.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 窗口配置表实体
 *
 * @author WST-PC
 * @version [版本号, 2016-09-26 10:45:32]
 */
@Entity(table = "AUDIT_ORGA_MEMBERTASK", id = "rowguid")
public class AuditOrgaMemberTask extends BaseEntity implements Cloneable, Serializable {
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
     * 窗口guid
     */
    public String getMemberguid() {
        return super.get("memberguid");
    }

    public void setMemberguid(String memberguid) {
        super.set("memberguid", memberguid);
    }

    /**
     * 事项guid
     */
    public String getTaskguid() {
        return super.get("taskguid");
    }

    public void setTaskguid(String taskguid) {
        super.set("taskguid", taskguid);
    }

    /**
     * 是否显示在大屏
     */
    public Integer getIsshowdp() {
        return super.get("is_showdp");
    }

    public void setIsshowdp(int is_showdp) {
        super.set("is_showdp", is_showdp);
    }

    /**
     * 是否显示在大屏
     */
    public String getHandletype() {
        return super.get("handletype");
    }

    public void setHandletype(String handletype) {
        super.set("handletype", handletype);
    }


    /**
     * 排序
     */
    public Integer getOrdernum() {
        return super.get("ordernum");
    }

    public void setOrdernum(Integer ordernum) {
        super.set("ordernum", ordernum);
    }

    /**
     * 是否启用
     *
     * @return
     */
    public String getEnabled() {
        return super.get("enabled");
    }

    public void setEnabled(String enabled) {
        super.set("enabled", enabled);
    }

    /**
     * 事项版本标识
     *
     * @return
     */
    public String getTaskid() {
        return super.get("taskid");
    }

    public void setTaskid(String taskid) {
        super.set("taskid", taskid);
    }

}
