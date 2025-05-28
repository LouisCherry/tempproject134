package com.epoint.xmz.servicecenterextension.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 中心配置信息表实体
 *
 * @version [版本号, 2024-04-09 14:14:08]
 * @作者 Administrator
 */
@Entity(table = "servicecenter_extension", id = "rowguid")
public class ServicecenterExtension extends BaseEntity implements Cloneable {
    private static final long serialVersionUID = 1L;

    /**
     * 中心地址
     */
    public String getAddress() {
        return super.get("address");
    }

    public void setAddress(String address) {
        super.set("address", address);
    }

    /**
     * 中心标识
     */
    public String getCenterguid() {
        return super.get("centerguid");
    }

    public void setCenterguid(String centerguid) {
        super.set("centerguid", centerguid);
    }

    /**
     * 咨询电话
     */
    public String getMobile() {
        return super.get("mobile");
    }

    public void setMobile(String mobile) {
        super.set("mobile", mobile);
    }

    /**
     * 工作时间
     */
    public String getWorktime() {
        return super.get("worktime");
    }

    public void setWorktime(String worktime) {
        super.set("worktime", worktime);
    }

    /**
     * 短信预约模板
     */
    public String getYuyuemessage() {
        return super.get("yuyuemessage");
    }

    public void setYuyuemessage(String yuyuemessage) {
        super.set("yuyuemessage", yuyuemessage);
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