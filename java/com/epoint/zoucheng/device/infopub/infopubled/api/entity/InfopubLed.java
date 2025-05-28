package com.epoint.zoucheng.device.infopub.infopubled.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * LED大屏实体
 * 
 * @作者  why
 * @version [版本号, 2019-09-23 16:19:50]
 */
@Entity(table = "InfoPub_Led", id = "rowguid")
public class InfopubLed extends BaseEntity implements Cloneable
{
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
    * LED名称
    */
    public String getLedname() {
        return super.get("ledname");
    }

    public void setLedname(String ledname) {
        super.set("ledname", ledname);
    }

    /**
    * 分辨率
    */
    public String getResolution() {
        return super.get("resolution");
    }

    public void setResolution(String resolution) {
        super.set("resolution", resolution);
    }

    /**
    * LED编号
    */
    public Integer getLednumber() {
        return super.getInt("lednumber");
    }

    public void setLednumber(Integer lednumber) {
        super.set("lednumber", lednumber);
    }

    /**
    * LED分组
    */
    public String getLedstyle() {
        return super.get("ledstyle");
    }

    public void setLedstyle(String ledstyle) {
        super.set("ledstyle", ledstyle);
    }

}
