package com.epoint.sghd.auditjianguan.rlconfig.api;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 政策文件对应的实体
 *
 * @version [版本号, 2018年10月10日]
 * @作者 shibin
 */
@Entity(table = "rlconfigsendmsg", id = {"rowguid"})
public class RlconfigSendmsg extends BaseEntity implements Cloneable {
    private static final long serialVersionUID = 1L;

    public String getRowguid() {
        return (String) super.get("rowguid");
    }

    public void setRowguid(String rowguid) {
        super.set("rowguid", rowguid);
    }

    public String getRlouguid() {
        return (String) super.get("rlouguid");
    }

    public void setRlouguid(String rlouguid) {
        super.set("rlouguid", rlouguid);
    }

    public String getRlouname() {
        return (String) super.get("rlouname");
    }

    public void setRlouname(String rlouname) {
        super.set("rlouname", rlouname);
    }

}