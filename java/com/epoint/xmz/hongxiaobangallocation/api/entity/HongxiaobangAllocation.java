package com.epoint.xmz.hongxiaobangallocation.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 红小帮配置表实体
 *
 * @version [版本号, 2024-05-20 17:08:28]
 * @作者 Administrator
 */
@Entity(table = "hongxiaobang_allocation", id = "rowguid")
public class HongxiaobangAllocation extends BaseEntity implements Cloneable {
    private static final long serialVersionUID = 1L;

    /**
     * 配置内容
     */
    public String getContent() {
        return super.get("content");
    }

    public void setContent(String content) {
        super.set("content", content);
    }

    /**
     * 图片标识
     */
    public String getPictureguid() {
        return super.get("pictureguid");
    }

    public void setPictureguid(String pictureguid) {
        super.set("pictureguid", pictureguid);
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