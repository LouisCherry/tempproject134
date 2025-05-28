package com.epoint.jnycsl.domain;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 部门自建系统
 * @author 刘雨雨
 * @time 2018年9月12日上午10:24:32
 */
@Entity(table = "taian_dept_self_build_system", id = "rowguid")
public class DeptSelfBuildSystem extends BaseEntity {

	private static final long serialVersionUID = -3586594573539196356L;

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

	public Date getLastMofifyDate() {
		return get("lastMofifyDate");
	}

	public void setLastMofifyDate(Date lastMofifyDate) {
		set("lastMofifyDate", lastMofifyDate);
	}

	public String getOperateUserGuid() {
		return get("operateUserGuid");
	}

	public void setOperateUserGuid(String operateUserGuid) {
		set("operateUserGuid", operateUserGuid);
	}

	public String getOperateUserName() {
		return get("operateUserName");
	}

	public void setOperateUserName(String operateUserName) {
		set("operateUserName", operateUserName);
	}

	/**
	 * 部门自建系统名称
	 */
	public String getAppName() {
		return get("appName");
	}

	/**
	 * 部门自建系统名称
	 */
	public void setAppName(String appName) {
		set("appName", appName);
	}

	/**
	 * 分配给自建系统的appKey
	 */
	public String getAppKey() {
		return get("appKey");
	}

	/**
	 * 分配给自建系统的appKey
	 */
	public void setAppKey(String appKey) {
		set("appKey", appKey);
	}

	/**
	 * 我方分配给自建系统的appSecret
	 */
	public String getAppSecret() {
		return get("appSecret");
	}

	/**
	 * 我方分配给自建系统的appSecret
	 */
	public void setAppSecret(String appSecret) {
		set("appSecret", appSecret);
	}

	/**
	 * 自建系统的收件/受理 信息接口地址
	 */
	public String getReceiveProjectApiUrl() {
		return get("receiveProjectApiUrl");
	}

	/**
	 * 自建系统的收件/受理 信息接口地址
	 */
	public void setReceiveProjectApiUrl(String receiveProjectApiUrl) {
		set("receiveProjectApiUrl", receiveProjectApiUrl);
	}

	/**
	 * 自建系统的收件/受理 信息接口类型，http,webservice
	 */
	public String getReceiveProjectApiType() {
		return get("receiveProjectApiType");
	}

	/**
	 * 自建系统的收件/受理 信息接口类型，http,webservice
	 */
	public void setReceiveProjectApiType(String receiveProjectApiType) {
		set("receiveProjectApiType", receiveProjectApiType);
	}

	/**
	 * 自建系统发布的获取token的接口地址
	 */
	public String getRequestTokenApiUrl() {
		return get("requestTokenApiUrl");
	}

	/**
	 * 自建系统发布的获取token的接口地址
	 */
	public void setRequestTokenApiUrl(String requestTokenApiUrl) {
		set("requestTokenApiUrl", requestTokenApiUrl);
	}
	
	/**
	 * 自建系统发布的获取token的接口类型
	 */
	public String getRequestTokenApiType() {
		return get("requestTokenApiType");
	}

	/**
	 * 自建系统发布的获取token的接口类型
	 */
	public void setRequestTokenApiType(String requestTokenApiType) {
		set("requestTokenApiType", requestTokenApiType);
	}

	/**
	 * 自建系统分配给我方的appKey
	 * @return
	 */
	public String getAppKeyAllotedByDept() {
		return get("appKeyAllotedByDept");
	}

	/**
	 * 自建系统分配给我方的appKey
	 */
	public void setAppKeyAllotedByDept(String appKeyAllotedByDept) {
		set("appKeyAllotedByDept", appKeyAllotedByDept);
	}

	/**
	 * 自建系统分配给我方的appSecret
	 */
	public String getAppSecretAllotedByDept() {
		return get("appSecretAllotedByDept");
	}

	/**
	 * 自建系统分配给我方的appSecret
	 */
	public void setAppSecretAllotedByDept(String appSecretAllotedByDept) {
		set("appSecretAllotedByDept", appSecretAllotedByDept);
	}
	
	/**
     * 自建系统url地址
     */
    public String getSelfsystemurl() {
        return get("selfsystemurl");
    }
    public void setSelfsystemurl(String selfsystemurl) {
        set("selfsystemurl", selfsystemurl);
    }
    
    /**
     * 自建系统图标clienguid
     */
    public String getSltcliengguid() {
        return get("sltcliengguid");
    }
    public void setSltcliengguid(String sltcliengguid) {
        set("sltcliengguid", sltcliengguid);
    }
    
    /**
     * 自建系统授权部门
     */
    public String getOuguid() {
        return get("ouguid");
    }
    public void setOuguid(String ouguid) {
        set("ouguid", ouguid);
    }
}
