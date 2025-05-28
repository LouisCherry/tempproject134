package com.epoint.apimanage.log.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * api日志表实体
 *
 * @author sjw
 * @version 2017-07-21 17:48:10
 */
@Entity(table = "api_manage_log", id = "rowguid")
public class ApiManageLog extends BaseEntity implements Cloneable {
    private static final long serialVersionUID = 1L;

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
     * 网关响应失败类型 ApiErrorType
     */

    public String getErrorFlag() {
        return super.get("errorflag");
    }

    public void setErrorFlag(String yearflag) {
        super.set("errorflag", yearflag);
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
     * 开始时间
     */
    public Date getStartat() {
        return super.getDate("startat");
    }

    public void setStartat(Date startat) {
        super.set("startat", startat);
    }

    /**
     * 请求方法
     */
    public String getMethod() {
        return super.get("method");
    }

    public void setMethod(String method) {
        super.set("method", method);
    }

    /**
     * 请求包大小
     */
    public Double getRequestsize() {
        return super.getDouble("requestsize");
    }

    public void setRequestsize(Double requestsize) {
        super.set("requestsize", requestsize);
    }

    /**
     * 客户端ip
     */
    public String getClientip() {
        return super.get("clientip");
    }

    public void setClientip(String clientip) {
        super.set("clientip", clientip);
    }

    /**
     * 返回包大小
     */
    public Double getResponsesize() {
        return super.getDouble("responsesize");
    }

    public void setResponsesize(Double responsesize) {
        super.set("responsesize", responsesize);
    }

    /**
     * 返回状态码
     */
    public Integer getStatus() {
        return super.getInt("status");
    }

    public void setStatus(Integer status) {
        super.set("status", status);
    }

    /**
     * 转发耗时
     */
    public Integer getForwardtime() {
        return super.getInt("forwardtime");
    }

    public void setForwardtime(Integer forwardtime) {
        super.set("forwardtime", forwardtime);
    }

    /**
     * 请求耗时
     */
    public Long getRequesttime() {
        return super.getLong("requesttime");
    }

    public void setRequesttime(Long timeConsuming) {
        super.set("requesttime", timeConsuming);
    }

    /**
     * 请求地址
     */
    public String getRequesturl() {
        return super.get("requesturl");
    }

    public void setRequesturl(String requesturl) {
        super.set("requesturl", requesturl);
    }

    /**
     * 请求内容
     */
    public String getContext() {
        return super.get("context");
    }

    public void setContext(String context) {
        super.set("context", context);
    }

    /**
     * 流水线标识
     */
    public String getIdentification() {
        return super.get("identification");
    }

    public void setIdentification(String identification) {
        super.set("identification", identification);
    }

    /**
     * 所属分类guid
     */
    public String getCategoryguid() {
        return super.get("categoryguid");
    }

    public void setCategoryguid(String categoryguid) {
        super.set("categoryguid", categoryguid);
    }

    /**
     * 终端
     */
    public String getUserAgent() {
        return super.get("useragent");
    }

    public void setUserAgent(String useragent) {
        super.set("useragent", useragent);
    }

    /**
     * 响应时间
     */
    public Integer getResponseTime() {
        return super.getInt("responsetime");
    }

    public void setResponseTime(Integer responsetime) {
        super.set("responsetime", responsetime);
    }
}
