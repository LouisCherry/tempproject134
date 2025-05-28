package com.epoint.ces.requesthikuserlog.api.entity;

import java.util.Date;

import com.epoint.basic.auditorga.auditrecord.domain.AuditOrgaRecord;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 请求海康日志信息人员考勤记录表实体
 *
 * @version [版本号, 2021-11-22 14:32:24]
 * @作者 shun
 */
@Entity(table = "REQUEST_HIK_USER_LOG", id = "rowguid")
public class RequestHikUserLog extends BaseEntity implements Cloneable {
    private static final long serialVersionUID = 1L;

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
     * 序号
     */
    public Integer getRow_id() {
        return super.getInt("row_id");
    }

    public void setRow_id(Integer row_id) {
        super.set("row_id", row_id);
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
     * 默认主键字段
     */
    public String getRowguid() {
        return super.get("rowguid");
    }

    public void setRowguid(String rowguid) {
        super.set("rowguid", rowguid);
    }

    /**
     * 回调时间
     */
    public Date getBackdate() {
        return super.getDate("backdate");
    }

    public void setBackdate(Date backdate) {
        super.set("backdate", backdate);
    }

    /**
     * 工号
     */
    public String getXh() {
        return super.get("xh");
    }

    public void setXh(String xh) {
        super.set("xh", xh);
    }

    /**
     * 考勤时间
     */
    public Date getRhdate() {
        return super.getDate("rhdate");
    }

    public void setRhdate(Date rhdate) {
        super.set("rhdate", rhdate);
    }


}