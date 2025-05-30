package com.epoint.departdocking.provinceselfbuild.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * token数据表
 * @author 刘雨雨
 * @time 2018年9月17日上午9:40:40
 */
@Entity(id = {"rowguid" }, table = "taian_access_token")
public class AccessToken extends BaseEntity
{

    private static final long serialVersionUID = 3641889823824364916L;

    public String getRowguid() {
        return get("rowguid");
    }

    public void setRowguid(String rowguid) {
        set("rowguid", rowguid);
    }

    public Date getCreateDate() {
        return get("createDate");
    }

    public void setCreateDate(Date createDate) {
        set("createDate", createDate);
    }

    public Date getModifyDate() {
        return get("modifyDate");
    }

    public void setModifyDate(Date modifyDate) {
        set("modifyDate", modifyDate);
    }

    public String getOperateUserName() {
        return get("operateUserName");
    }

    public void setOperateUserName(String operateUserName) {
        set("operateUserName", operateUserName);
    }

    public String getAppKey() {
        return get("appKey");
    }

    public void setAppKey(String appKey) {
        set("appKey", appKey);
    }

    public String getAppSecret() {
        return get("appSecret");
    }

    public void setAppSecret(String appSecret) {
        set("appSecret", appSecret);
    }

    public Long getTokenGenerateTime() {
        return get("tokenGenerateTime");
    }

    public void setTokenGenerateTime(Long tokenGenerateTime) {
        set("tokenGenerateTime", tokenGenerateTime);
    }

    public String getToken() {
        return get("token");
    }

    public void setToken(String token) {
        set("token", token);
    }

    public long getTokenEndTime() {
        return get("tokenEndTime");
    }

    public void setTokenEndTime(long tokenEndTime) {
        set("tokenEndTime", tokenEndTime);
    }

    public String getOwnerType() {
        return get("ownerType");
    }

    public void setOwnerType(String ownerType) {
        set("ownerType", ownerType);
    }

}
