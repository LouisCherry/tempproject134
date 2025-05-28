package com.epoint.gxqzwfw.auditqueue.windowled.domain;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

@Entity(table = "audit_znsb_windowled", id = "rowguid")
public class WindowLed extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

    public Date getOperatedate() {
        return super.getDate("operatedate");
    }

    public void setOperatedate(Date operatedate) {
        super.set("operatedate", operatedate);
    }

    /**
    * 窗口号
    */
    public String getWindowno() {
        return super.get("windowno");
    }

    public void setWindowno(String windowno) {
        super.set("windowno", windowno);
    }

    /**
    * 当前内容
    */
    public String getContent() {
        return super.get("content");
    }

    public void setContent(String content) {
        super.set("content", content);
    }
    public String getRowguid() {
        return super.get("rowguid");
    }

    public void setRowguid(String rowguid) {
        super.set("rowguid", rowguid);
    }

}
