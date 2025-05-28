package com.epoint.jn.qh.statistics.api;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 取号量统计实体
 * 
 * @作者  夜雨清尘
 * @version [版本号, 2019-06-06 16:10:26]
 */
@Entity(table = "qh_statistics", id = "rowguid")
public class QhStatistics extends BaseEntity implements Cloneable
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
    * 设备名称
    */
    public String getMachinename() {
        return super.get("machinename");
    }

    public void setMachinename(String machinename) {
        super.set("machinename", machinename);
    }

    /**
    * 设备编号
    */
    public String getMachineno() {
        return super.get("machineno");
    }

    public void setMachineno(String machineno) {
        super.set("machineno", machineno);
    }

    /**
    * 设备mac
    */
    public String getMacaddress() {
        return super.get("macaddress");
    }

    public void setMacaddress(String macaddress) {
        super.set("macaddress", macaddress);
    }

    /**
    * 取号量
    */
    public Integer getCount() {
        return super.getInt("count");
    }

    public void setCount(Integer count) {
        super.set("count", count);
    }
    public Date getCreatedate() {
        return super.getDate("createdate");
    }

    public void setCreatedate(Date createdate) {
        super.set("createdate", createdate);
    }
}
