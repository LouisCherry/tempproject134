package com.epoint.yyyz.businesslicense.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 一业一证并联办件结果实体
 * @description
 * @author shibin
 * @date  2020年5月19日 上午10:56:28
 */
@Entity(table = "businesslicense_result", id = "rowguid")
public class BusinessLicenseResult extends BaseEntity implements Cloneable
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
    * 审批实例guid
    */
    public String getBiGuid() {
        return super.get("biguid");
    }

    public void setBiGuid(String biguid) {
        super.set("biguid", biguid);
    }

    /**
     * 综合证guid
     */
    public String getCertrowguid() {
        return super.get("certrowguid");
    }

    public void setCertrowguid(String certrowguid) {
        super.set("certrowguid", certrowguid);
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
     * 附件标识
     */
    public String getClientguid() {
        return super.get("clientguid");
    }

    public void setClientguid(String clientguid) {
        super.set("clientguid", clientguid);
    }

    /**
     * 事项唯一标识
     */
    public String getTaskid() {
        return super.get("task_id");
    }

    public void setTaskid(String task_id) {
        super.set("task_id", task_id);
    }

    /**
     * 事项唯一标识
     */
    public String getBusinessguid() {
        return super.get("businessguid");
    }

    public void setBusinessguid(String businessguid) {
        super.set("businessguid", businessguid);
    }
}
