package com.epoint.window.floatwindow.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 飘窗浮窗表实体
 *
 * @version [版本号, 2023-07-17 10:15:09]
 * @作者 86183
 */
@Entity(table = "float_window", id = "rowguid")
public class FloatWindow extends BaseEntity implements Cloneable {
    private static final long serialVersionUID = 1L;

    /**
     * 功能名称
     */
    public String getName() {
        return super.get("name");
    }

    public void setName(String name) {
        super.set("name", name);
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
     * 所属站点
     */
    public String getSite() {
        return super.get("site");
    }

    public void setSite(String site) {
        super.set("site", site);
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
     * 功能类型
     */
    public String getType() {
        return super.get("type");
    }

    public void setType(String type) {
        super.set("type", type);
    }

    /**
     * 背景图
     */
    public String getImgclientguid() {
        return super.get("imgclientguid");
    }

    public void setImgclientguid(String imgclientguid) {
        super.set("imgclientguid", imgclientguid);
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
     * 链接地址
     */
    public String getUrl() {
        return super.get("url");
    }

    public void setUrl(String url) {
        super.set("url", url);
    }

    /**
     * 统一认证登陆参数
     */
    public String getAppmark() {
        return super.get("appmark");
    }

    public void setAppmark(String appmark) {
        super.set("appmark", appmark);
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

    /**
     * 排序
     */
    public Integer getSort() {
        return super.getInt("sort");
    }

    public void setSort(Integer sort) {
        super.set("sort", sort);
    }

    /**
     * 飘窗浮窗启用状态
     */
    public String getStart() {
        return super.get("start");
    }

    public void setStart(String start) {
        super.set("start", start);
    }

}