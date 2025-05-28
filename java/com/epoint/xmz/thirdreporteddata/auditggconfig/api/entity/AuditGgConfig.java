package com.epoint.xmz.thirdreporteddata.auditggconfig.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 工改配置信息表实体
 *
 * @version [版本号, 2023-11-06 17:08:23]
 * @作者 shaoyuhui
 */
@Entity(table = "audit_gg_config", id = "rowguid")
public class AuditGgConfig extends BaseEntity implements Cloneable {
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
     * 配置字段英文名
     */
    public String getConfigname() {
        return super.get("configname");
    }

    public void setConfigname(String configname) {
        super.set("configname", configname);
    }

    /**
     * 配置说明
     */
    public String getConfigdesc() {
        return super.get("configdesc");
    }

    public void setConfigdesc(String configdesc) {
        super.set("configdesc", configdesc);
    }

    /**
     * 配置字段值
     */
    public String getConfigvalue() {
        return super.get("configvalue");
    }

    public void setConfigvalue(String configvalue) {
        super.set("configvalue", configvalue);
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

}
