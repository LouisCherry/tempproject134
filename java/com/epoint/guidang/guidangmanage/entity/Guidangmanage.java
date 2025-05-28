package com.epoint.guidang.guidangmanage.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 归档管理实体
 * 
 * @作者  chengninghua
 * @version [版本号, 2017-12-15 15:11:49]
 */
@Entity(table = "guidangmanage", id = "rowguid")
public class Guidangmanage extends BaseEntity implements Cloneable
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
    * 服务器IP
    */
    public String getServiceip() {
        return super.get("serviceip");
    }

    public void setServiceip(String serviceip) {
        super.set("serviceip", serviceip);
    }

    /**
    * 保存年限
    */
    public String getSaveyear() {
        return super.get("saveyear");
    }

    public void setSaveyear(String saveyear) {
        super.set("saveyear", saveyear);
    }

    /**
    * 辖区
    */
    public String getAreacode() {
        return super.get("areacode");
    }

    public void setAreacode(String areacode) {
        super.set("areacode", areacode);
    }

    /**
    * 部门
    */
    public String getOucode() {
        return super.get("oucode");
    }

    public void setOucode(String oucode) {
        super.set("oucode", oucode);
    }

    /**
    * 事项标识
    */
    public String getTaskid() {
        return super.get("taskid");
    }

    public void setTaskid(String taskid) {
        super.set("taskid", taskid);
    }

}
