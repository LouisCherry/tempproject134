package com.epoint.xmz.auditelectricdata.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 电力事项信息表实体
 *
 * @version [版本号, 2023-08-10 15:23:54]
 * @作者 lee
 */
@Entity(table = "audit_electric_data", id = "rowguid")
public class AuditElectricData extends BaseEntity implements Cloneable {
    private static final long serialVersionUID = 1L;

    /**
     * 同步错误原因
     */
    public String getError() {
        return super.get("error");
    }

    public void setError(String error) {
        super.set("error", error);
    }

    /**
     * 办件流水号
     */
    public String getFlowsn() {
        return super.get("flowsn");
    }

    public void setFlowsn(String flowsn) {
        super.set("flowsn", flowsn);
    }

    /**
     * 参数
     */
    public String getParams() {
        return super.get("params");
    }

    public void setParams(String params) {
        super.set("params", params);
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
     * 返回值
     */
    public String getResult() {
        return super.get("result");
    }

    public void setResult(String result) {
        super.set("result", result);
    }

    /**
     * 接口类型
     */
    public String getType() {
        return super.get("type");
    }

    public void setType(String type) {
        super.set("type", type);
    }

    /**
     * 更新时间
     */
    public Date getUpdatetime() {
        return super.getDate("updatetime");
    }

    public void setUpdatetime(Date updatetime) {
        super.set("updatetime", updatetime);
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