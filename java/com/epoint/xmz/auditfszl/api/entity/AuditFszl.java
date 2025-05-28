package com.epoint.xmz.auditfszl.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 放射诊疗数据实体
 *
 * @version [版本号, 2024-06-20 10:28:16]
 * @作者 ljh
 */
@Entity(table = "audit_fszl", id = "rowguid")
public class AuditFszl extends BaseEntity implements Cloneable {
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
     * 医疗机构名称
     */
    public String getYljgmc() {
        return super.get("yljgmc");
    }

    public void setYljgmc(String yljgmc) {
        super.set("yljgmc", yljgmc);
    }

    /**
     * 医疗机构登记号
     */
    public String getYljgdjh() {
        return super.get("yljgdjh");
    }

    public void setYljgdjh(String yljgdjh) {
        super.set("yljgdjh", yljgdjh);
    }

    /**
     * 经营地址
     */
    public String getJydz() {
        return super.get("jydz");
    }

    public void setJydz(String jydz) {
        super.set("jydz", jydz);
    }

    /**
     * 经济类型
     */
    public String getSyzxs() {
        return super.get("syzxs");
    }

    public void setSyzxs(String syzxs) {
        super.set("syzxs", syzxs);
    }

    /**
     * 法定代表人
     */
    public String getFddbr() {
        return super.get("fddbr");
    }

    public void setFddbr(String fddbr) {
        super.set("fddbr", fddbr);
    }

    /**
     * 主要负责人
     */
    public String getZyfzr() {
        return super.get("zyfzr");
    }

    public void setZyfzr(String zyfzr) {
        super.set("zyfzr", zyfzr);
    }

    /**
     * 联系电话
     */
    public String getLxdh() {
        return super.get("lxdh");
    }

    public void setLxdh(String lxdh) {
        super.set("lxdh", lxdh);
    }

    /**
     * 许可项目
     */
    public String getXkxm() {
        return super.get("xkxm");
    }

    public void setXkxm(String xkxm) {
        super.set("xkxm", xkxm);
    }

    /**
     * 放射诊疗许可证批准日期
     */
    public String getCertificatedate() {
        return super.get("certificatedate");
    }

    public void setCertificatedate(String certificatedate) {
        super.set("certificatedate", certificatedate);
    }

    /**
     * 放射诊疗许可证号
     */
    public String getCertno() {
        return super.get("certno");
    }

    public void setCertno(String certno) {
        super.set("certno", certno);
    }

    /**
     * 放射设备校验日期
     */
    public Date getJydate() {
        return super.getDate("jydate");
    }

    public void setJydate(Date jydate) {
        super.set("jydate", jydate);
    }

    /**
     * 校验有效期
     */
    public String getValiditydate() {
        return super.get("validitydate");
    }

    public void setValiditydate(String validitydate) {
        super.set("validitydate", validitydate);
    }

    /**
     * 下一次校验日期
     */
    public Date getNextjydate() {
        return super.getDate("nextjydate");
    }

    public void setNextjydate(Date nextjydate) {
        super.set("nextjydate", nextjydate);
    }

    /**
     * 上一年度设备状态检测报告出具时间
     */
    public Date getSyndsbztjcbgcjsj() {
        return super.getDate("syndsbztjcbgcjsj");
    }

    public void setSyndsbztjcbgcjsj(Date syndsbztjcbgcjsj) {
        super.set("syndsbztjcbgcjsj", syndsbztjcbgcjsj);
    }

    /**
     * 报告编制单位
     */
    public String getBaogbzdw() {
        return super.get("baogbzdw");
    }

    public void setBaogbzdw(String baogbzdw) {
        super.set("baogbzdw", baogbzdw);
    }

    /**
     * 许可证状态
     */
    public String getCertstatus() {
        return super.get("certstatus");
    }

    public void setCertstatus(String certstatus) {
        super.set("certstatus", certstatus);
    }

    /**
     * 更新日期
     */
    public Date getUpdatetime() {
        return super.getDate("updatetime");
    }

    public void setUpdatetime(Date updatetime) {
        super.set("updatetime", updatetime);
    }

    /**
     * 辖区编码
     */
    public String getAreacode() {
        return super.get("areacode");
    }

    public void setAreacode(String areacode) {
        super.set("areacode", areacode);
    }

    /**
     * 版本
     */
    public Integer getVersion() {
        return super.getInt("version");
    }

    public void setVersion(Integer version) {
        super.set("version", version);
    }

    /**
     * 是否历史版本
     */
    public String getIs_history() {
        return super.get("is_history");
    }

    public void setIs_history(String is_history) {
        super.set("is_history", is_history);
    }

    /**
     * 是否发送预警提醒
     */
    public String getSendtip() {
        return super.get("sendtip");
    }

    public void setSendtip(String sendtip) {
        super.set("sendtip", sendtip);
    }

    /**
     * 放射诊疗版本唯一标识
     */
    public String getFszlid() {
        return super.get("fszlid");
    }

    public void setFszlid(String fszlid) {
        super.set("fszlid", fszlid);
    }

    /**
     * 版本日期
     */
    public Date getVersiondate() {
        return super.getDate("versiondate");
    }

    public void setVersiondate(Date versiondate) {
        super.set("versiondate", versiondate);
    }

}