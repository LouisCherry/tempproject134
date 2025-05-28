package com.epoint.sghd.auditjianguan.domain;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 监管平台人员交流信息实体
 *
 * @version [版本号, 2018年10月10日]
 * @作者 shibin
 */
@Entity(table = "audit_project_permissionchange", id = {"rowguid"})
public class AuditProjectPermissionChange extends BaseEntity implements Cloneable {
    private static final long serialVersionUID = 1L;

    public String getRowguid() {
        return (String) super.get("rowguid");
    }

    public void setRowguid(String rowguid) {
        super.set("rowguid", rowguid);
    }

    public String getOuguid() {
        return (String) super.get("ouguid");
    }

    public void setOuguid(String ouguid) {
        super.set("ouguid", ouguid);
    }

    public String getOuname() {
        return (String) super.get("ouname");
    }

    public void setOuname(String ouname) {
        super.set("ouname", ouname);
    }

    public String getSendUserGuid() {
        return (String) super.get("senduserguid");
    }

    public void setSendUserGuid(String senduserguid) {
        super.set("senduserguid", senduserguid);
    }

    public String getSendperson() {
        return (String) super.get("sendperson");
    }

    public void setSendperson(String sendperson) {
        super.set("sendperson", sendperson);
    }

    public String getCommunicationOuGuid() {
        return (String) super.get("communicationouguid");
    }

    public void setCommunicationOuGuid(String communicationouguid) {
        super.set("communicationouguid", communicationouguid);
    }

    public String getCommunicationOuName() {
        return (String) super.get("communicationouname");
    }

    public void setCommunicationOuName(String communicationouname) {
        super.set("communicationouname", communicationouname);
    }

    public String getCommunicationUserGuid() {
        return (String) super.get("communicationuserguid");
    }

    public void setCommunicationUserGuid(String communicationuserguid) {
        super.set("communicationuserguid", communicationuserguid);
    }

    public String getCommunicationperson() {
        return (String) super.get("communicationperson");
    }

    public void setCommunicationperson(String communicationperson) {
        super.set("communicationperson", communicationperson);
    }

    public String getCommunicationtheme() {
        return (String) super.get("communicationtheme");
    }

    public void setCommunicationtheme(String communicationtheme) {
        super.set("communicationtheme", communicationtheme);
    }

    public String getThemeguid() {
        return (String) super.get("themeguid");
    }

    public void setThemeguid(String themeguid) {
        super.set("themeguid", themeguid);
    }

    public String getFileclientguid() {
        return (String) super.get("fileclientguid");
    }

    public void setFileclientguid(String fileclientguid) {
        super.set("fileclientguid", fileclientguid);
    }

    public String getChangeopinion() {
        return (String) super.get("changeopinion");
    }

    public void setChangeopinion(String changeopinion) {
        super.set("changeopinion", changeopinion);
    }

    public String getDeadtime() {
        return (String) super.get("deadtime");
    }

    public void setDeadtime(String deadtime) {
        super.set("deadtime", deadtime);
    }

    public Date getReplydate() {
        return (Date) super.get("replydate");
    }

    public void setReplydate(Date replydate) {
        super.set("replydate", replydate);
    }

    public String getBanjiesign() {
        return (String) super.get("banjiesign");
    }

    public void setBanjiesign(String banjiesign) {
        super.set("banjiesign", banjiesign);
    }

    public String getHandleUrl() {
        return (String) super.get("handleUrl");
    }

    public void setHandleUrl(String handleUrl) {
        super.set("handleUrl", handleUrl);
    }

}
