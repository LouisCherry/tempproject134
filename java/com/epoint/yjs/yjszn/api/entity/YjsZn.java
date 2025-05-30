package com.epoint.yjs.yjszn.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 一件事指南配置实体
 *
 * @version [版本号, 2024-10-08 15:22:37]
 * @作者 panshunxing
 */
@Entity(table = "YJS_ZN", id = "rowguid")
public class YjsZn extends BaseEntity implements Cloneable {
    private static final long serialVersionUID = 1L;

    /**
     * BelongXiaQuCode
     */
    public String getBelongxiaqucode() {
        return super.get("belongxiaqucode");
    }

    public void setBelongxiaqucode(String belongxiaqucode) {
        super.set("belongxiaqucode", belongxiaqucode);
    }

    /**
     * OperateUserName
     */
    public String getOperateusername() {
        return super.get("operateusername");
    }

    public void setOperateusername(String operateusername) {
        super.set("operateusername", operateusername);
    }

    /**
     * OperateDate
     */
    public Date getOperatedate() {
        return super.getDate("operatedate");
    }

    public void setOperatedate(Date operatedate) {
        super.set("operatedate", operatedate);
    }

    /**
     * ROW_ID
     */
    public Integer getRow_id() {
        return super.getInt("row_id");
    }

    public void setRow_id(Integer row_id) {
        super.set("row_id", row_id);
    }

    /**
     * YearFlag
     */
    public String getYearflag() {
        return super.get("yearflag");
    }

    public void setYearflag(String yearflag) {
        super.set("yearflag", yearflag);
    }

    /**
     * RowGuid
     */
    public String getRowguid() {
        return super.get("rowguid");
    }

    public void setRowguid(String rowguid) {
        super.set("rowguid", rowguid);
    }

    /**
     * type
     */
    public String getType() {
        return super.get("type");
    }

    public void setType(String type) {
        super.set("type", type);
    }

    /**
     * title
     */
    public String getTitle() {
        return super.get("title");
    }

    public void setTitle(String title) {
        super.set("title", title);
    }

    /**
     * areacode
     */
    public String getAreacode() {
        return super.get("areacode");
    }

    public void setAreacode(String areacode) {
        super.set("areacode", areacode);
    }

    /**
     * url
     */
    public String getUrl() {
        return super.get("url");
    }

    public void setUrl(String url) {
        super.set("url", url);
    }

    /**
     * attachguid
     */
    public String getAttachguid() {
        return super.get("attachguid");
    }

    public void setAttachguid(String attachguid) {
        super.set("attachguid", attachguid);
    }

    /**
     * 发布时间
     */
    public Date getPublishtime() {
        return super.getDate("publishtime");
    }

    public void setPublishtime(Date publishtime) {
        super.set("publishtime", publishtime);
    }

    /**
     * 来源
     */
    public String getSourcefrom() {
        return super.get("sourcefrom");
    }

    public void setSourcefrom(String sourcefrom) {
        super.set("sourcefrom", sourcefrom);
    }

}