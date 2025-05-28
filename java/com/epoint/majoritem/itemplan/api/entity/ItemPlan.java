package com.epoint.majoritem.itemplan.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 重点项目项目进度表实体
 *
 * @version [版本号, 2024-07-09 15:05:52]
 * @作者 19273
 */
@Entity(table = "item_plan", id = "rowguid")
public class ItemPlan extends BaseEntity implements Cloneable {
    private static final long serialVersionUID = 1L;

    /**
     * 项目主键
     */
    public String getItemguid() {
        return super.get("itemguid");
    }

    public void setItemguid(String itemguid) {
        super.set("itemguid", itemguid);
    }

    /**
     * 记录时间
     */
    public Date getJltime() {
        return super.getDate("jltime");
    }

    public void setJltime(Date jltime) {
        super.set("jltime", jltime);
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
     * 项目进度
     */
    public String getItemplan() {
        return super.get("itemplan");
    }

    public void setItemplan(String itemplan) {
        super.set("itemplan", itemplan);
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
     * 备注
     */
    public String getRemark() {
        return super.get("remark");
    }

    public void setRemark(String remark) {
        super.set("remark", remark);
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