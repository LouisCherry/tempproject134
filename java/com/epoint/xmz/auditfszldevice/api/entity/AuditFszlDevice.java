package com.epoint.xmz.auditfszldevice.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 放射装置表实体
 *
 * @version [版本号, 2024-06-20 10:28:22]
 * @作者 ljh
 */
@Entity(table = "audit_fszl_device", id = "rowguid")
public class AuditFszlDevice extends BaseEntity implements Cloneable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    public String getRowguid() {
        return super.get("rowguid");
    }

    public void setRowguid(String rowguid) {
        super.set("rowguid", rowguid);
    }

    /**
     * 工作流实例标识
     */
    public String getPviguid() {
        return super.get("pviguid");
    }

    public void setPviguid(String pviguid) {
        super.set("pviguid", pviguid);
    }

    /**
     * 记录所属辖区编码
     */
    public String getBelongxiaqucode() {
        return super.get("belongxiaqucode");
    }

    public void setBelongxiaqucode(String belongxiaqucode) {
        super.set("belongxiaqucode", belongxiaqucode);
    }

    /**
     * 创建人
     */
    public String getCreate_by() {
        return super.get("create_by");
    }

    public void setCreate_by(String create_by) {
        super.set("create_by", create_by);
    }

    /**
     * 创建时间
     */
    public Date getCreate_time() {
        return super.getDate("create_time");
    }

    public void setCreate_time(Date create_time) {
        super.set("create_time", create_time);
    }

    /**
     * 操作/更新人标识
     */
    public String getOperateuserguid() {
        return super.get("operateuserguid");
    }

    public void setOperateuserguid(String operateuserguid) {
        super.set("operateuserguid", operateuserguid);
    }

    /**
     * 操作/更新人
     */
    public String getOperateusername() {
        return super.get("operateusername");
    }

    public void setOperateusername(String operateusername) {
        super.set("operateusername", operateusername);
    }

    /**
     * 操作/更新时间
     */
    public Date getOperatedate() {
        return super.getDate("operatedate");
    }

    public void setOperatedate(Date operatedate) {
        super.set("operatedate", operatedate);
    }

    /**
     * 放射诊疗数据标识
     */
    public String getFszlguid() {
        return super.get("fszlguid");
    }

    public void setFszlguid(String fszlguid) {
        super.set("fszlguid", fszlguid);
    }

    /**
     * 所在场所
     */
    public String getSzcs() {
        return super.get("szcs");
    }

    public void setSzcs(String szcs) {
        super.set("szcs", szcs);
    }

    /**
     * 主要参数
     */
    public String getZycs() {
        return super.get("zycs");
    }

    public void setZycs(String zycs) {
        super.set("zycs", zycs);
    }

    /**
     * 生产厂家
     */
    public String getSccj() {
        return super.get("sccj");
    }

    public void setSccj(String sccj) {
        super.set("sccj", sccj);
    }

    /**
     * 设备编号
     */
    public String getSbbh() {
        return super.get("sbbh");
    }

    public void setSbbh(String sbbh) {
        super.set("sbbh", sbbh);
    }

    /**
     * 装置名称
     */
    public String getZzmc() {
        return super.get("zzmc");
    }

    public void setZzmc(String zzmc) {
        super.set("zzmc", zzmc);
    }

    /**
     * 型号
     */
    public String getXh() {
        return super.get("xh");
    }

    public void setXh(String xh) {
        super.set("xh", xh);
    }

}