package com.epoint.cert.domain;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 证照推送数据留痕类
 *
 */
@Entity(table = "xmz_cert_push_log", id = "rowguid")
public class XmzCertPushLog extends BaseEntity implements Cloneable {
    /**
     * 主键
     */
    public String getRowguid() {
        return super.get("rowguid");
    }

    public void setRowguid(String rowguid) {
        super.set("rowguid", rowguid);
    }

    /**
     * 办件标识
     */
    public String getProjectguid() {
        return super.get("projectguid");
    }

    public void setProjectguid(String projectguid) {
        super.set("projectguid", projectguid);
    }

    /**
     * 证照标识
     */
    public String getCertrowguid() {
        return super.get("certrowguid");
    }

    public void setCertrowguid(String certrowguid) {
        super.set("certrowguid", certrowguid);
    }

    /**
     * 推送返回状态
     */
    public String getReturncode() {
        return super.get("returncode");
    }

    public void setReturncode(String returncode) {
        super.set("returncode", returncode);
    }

    /**
     * 失败信息
     */
    public String getErrormsg() {
        return super.get("errormsg");
    }

    public void setErrormsg(String errormsg) {
        super.set("errormsg", errormsg);
    }

    /**
     * 预警信息
     */
    public String getWarnmsg() {
        return super.get("warnmsg");
    }

    public void setWarnmsg(String warnmsg) {
        super.set("warnmsg", warnmsg);
    }

    /**
     * 推送时间
     */
    public Date getPushdate() {
        return super.get("pushdate");
    }

    public void setPushdate(Date pushdate) {
        super.set("pushdate", pushdate);
    }
}
