package com.epoint.xmz.homepagenotice.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 首页弹窗表实体
 *
 * @version [版本号, 2023-11-08 15:36:46]
 * @作者 Administrator
 */
@Entity(table = "homepage_notice", id = "rowguid")
public class HomepageNotice extends BaseEntity implements Cloneable {
    private static final long serialVersionUID = 1L;

    /**
     * 内容
     */
    public String getContent() {
        return super.get("content");
    }

    public void setContent(String content) {
        super.set("content", content);
    }

    /**
     * 是否展示
     */
    public String getIs_show() {
        return super.get("is_show");
    }

    public void setIs_show(String is_show) {
        super.set("is_show", is_show);
    }

    /**
     * 发布时间
     */
    public Date getReleasetime() {
        return super.getDate("releasetime");
    }

    public void setReleasetime(Date releasetime) {
        super.set("releasetime", releasetime);
    }

    /**
     * 标题
     */
    public String getTitle() {
        return super.get("title");
    }

    public void setTitle(String title) {
        super.set("title", title);
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