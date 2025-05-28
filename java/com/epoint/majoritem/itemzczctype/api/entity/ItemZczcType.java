package com.epoint.majoritem.itemzczctype.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 重点项目政策支持类型表实体
 *
 * @version [版本号, 2024-07-09 15:06:03]
 * @作者 19273
 */
@Entity(table = "item_zczc_type", id = "rowguid")
public class ItemZczcType extends BaseEntity implements Cloneable {
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
     * 默认主键字段
     */
    public String getRowguid() {
        return super.get("rowguid");
    }

    public void setRowguid(String rowguid) {
        super.set("rowguid", rowguid);
    }

    /**
     * 政策支持类型
     */
    public String getZczclx() {
        return super.get("zczclx");
    }

    public void setZczclx(String zczclx) {
        super.set("zczclx", zczclx);
    }

    /**
     * 统计年份
     */
    public String getTjyear() {
        return super.get("tjyear");
    }

    public void setTjyear(String tjyear) {
        super.set("tjyear", tjyear);
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