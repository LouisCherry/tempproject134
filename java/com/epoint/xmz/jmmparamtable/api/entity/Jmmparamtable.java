package com.epoint.xmz.jmmparamtable.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 居民码市县参数配置表实体
 *
 * @version [版本号, 2023-06-26 14:34:17]
 * @作者 Administrator
 */
@Entity(table = "jmmparamtable", id = "rowguid")
public class Jmmparamtable extends BaseEntity implements Cloneable {
    private static final long serialVersionUID = 1L;

    /**
     * 用码机构id
     */
    public String getAcceptorgid() {
        return super.get("acceptorgid");
    }

    public void setAcceptorgid(String acceptorgid) {
        super.set("acceptorgid", acceptorgid);
    }

    /**
     * 辖区代码
     */
    public String getAreacode() {
        return super.get("areacode");
    }

    public void setAreacode(String areacode) {
        super.set("areacode", areacode);
    }

    /**
     * 市县名称
     */
    public String getAreaname() {
        return super.get("areaname");
    }

    public void setAreaname(String areaname) {
        super.set("areaname", areaname);
    }

    /**
     * 消息通知接口地址
     */
    public String getMessageurl() {
        return super.get("messageurl");
    }

    public void setMessageurl(String messageurl) {
        super.set("messageurl", messageurl);
    }

    /**
     * 网关接入id
     */
    public String getPinid() {
        return super.get("pinid");
    }

    public void setPinid(String pinid) {
        super.set("pinid", pinid);
    }

    /**
     * 网关接入token
     */
    public String getPintoken() {
        return super.get("pintoken");
    }

    public void setPintoken(String pintoken) {
        super.set("pintoken", pintoken);
    }

    /**
     * 验码接口地址
     */
    public String getYmurl() {
        return super.get("ymurl");
    }

    public void setYmurl(String ymurl) {
        super.set("ymurl", ymurl);
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