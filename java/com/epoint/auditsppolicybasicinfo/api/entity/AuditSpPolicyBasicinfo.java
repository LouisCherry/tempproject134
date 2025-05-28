package com.epoint.auditsppolicybasicinfo.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 政策信息表实体
 * 
 * @作者 lzhming
 * @version [版本号, 2023-03-17 09:26:17]
 */
@Entity(table = "AUDIT_SP_POLICY_BASICINFO", id = "rowguid")
public class AuditSpPolicyBasicinfo extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

    /**
     * 附件
     */
    public String getClientguid() {
        return super.get("clientguid");
    }

    public void setClientguid(String clientguid) {
        super.set("clientguid", clientguid);
    }

    /**
     * 政策内容
     */
    public String getContent() {
        return super.get("content");
    }

    public void setContent(String content) {
        super.set("content", content);
    }

    /**
     * 发文号
     */
    public String getFwbh() {
        return super.get("fwbh");
    }

    public void setFwbh(String fwbh) {
        super.set("fwbh", fwbh);
    }

    /**
     * 项目类别
     */
    public String getItemtype() {
        return super.get("itemtype");
    }

    public void setItemtype(String itemtype) {
        super.set("itemtype", itemtype);
    }

    /**
     * 发文日期
     */
    public Date getPublishtime() {
        return super.getDate("publishtime");
    }

    public void setPublishtime(Date publishtime) {
        super.set("publishtime", publishtime);
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
