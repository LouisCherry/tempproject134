package com.epoint.xmz.auditfszlperson.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 反射工作人员表实体
 *
 * @version [版本号, 2024-06-20 10:28:38]
 * @作者 ljh
 */
@Entity(table = "audit_fszl_person", id = "rowguid")
public class AuditFszlPerson extends BaseEntity implements Cloneable {
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
     * 姓名
     */
    public String getXm() {
        return super.get("xm");
    }

    public void setXm(String xm) {
        super.set("xm", xm);
    }

    /**
     * 放射工作人员证编号
     */
    public String getFsgzryzbh() {
        return super.get("fsgzryzbh");
    }

    public void setFsgzryzbh(String fsgzryzbh) {
        super.set("fsgzryzbh", fsgzryzbh);
    }

    /**
     * 执业类别
     */
    public String getZylb() {
        return super.get("zylb");
    }

    public void setZylb(String zylb) {
        super.set("zylb", zylb);
    }

    /**
     * 执业范围
     */
    public String getZyfw() {
        return super.get("zyfw");
    }

    public void setZyfw(String zyfw) {
        super.set("zyfw", zyfw);
    }

    /**
     * 职业分类
     */
    public String getZyfl() {
        return super.get("zyfl");
    }

    public void setZyfl(String zyfl) {
        super.set("zyfl", zyfl);
    }

    /**
     * 上一年度年度职业健康体检日期
     */
    public Date getSyndndzyjktjrq() {
        return super.getDate("syndndzyjktjrq");
    }

    public void setSyndndzyjktjrq(Date syndndzyjktjrq) {
        super.set("syndndzyjktjrq", syndndzyjktjrq);
    }

    /**
     * 上一季度个人剂量检测日期
     */
    public Date getSyjdgrjljcrq() {
        return super.getDate("syjdgrjljcrq");
    }

    public void setSyjdgrjljcrq(Date syjdgrjljcrq) {
        super.set("syjdgrjljcrq", syjdgrjljcrq);
    }

}