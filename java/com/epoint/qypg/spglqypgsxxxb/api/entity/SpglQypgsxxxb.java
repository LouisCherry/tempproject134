package com.epoint.qypg.spglqypgsxxxb.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 区域评估事项信息表实体
 *
 * @version [版本号, 2023-09-15 14:21:45]
 * @作者 Epoint
 */
@Entity(table = "SPGL_QYPGSXXXB_EDIT", id = "rowguid")
public class SpglQypgsxxxb extends BaseEntity implements Cloneable
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
     * 流水号
     */
    public Integer getLsh() {
        return super.getInt("lsh");
    }

    public void setLsh(Integer lsh) {
        super.set("lsh", lsh);
    }

    /**
     * 地方数据主键
     */
    public String getDfsjzj() {
        return super.get("dfsjzj");
    }

    public void setDfsjzj(String dfsjzj) {
        super.set("dfsjzj", dfsjzj);
    }

    /**
     * 行政区划代码
     */
    public String getXzqhdm() {
        return super.get("xzqhdm");
    }

    public void setXzqhdm(String xzqhdm) {
        super.set("xzqhdm", xzqhdm);
    }

    /**
     * 区域评估单元编码
     */
    public String getQypgdybm() {
        return super.get("qypgdybm");
    }

    public void setQypgdybm(String qypgdybm) {
        super.set("qypgdybm", qypgdybm);
    }

    /**
     * 开展区域评估事项编码
     */
    public String getQypgsxbm() {
        return super.get("qypgsxbm");
    }

    public void setQypgsxbm(String qypgsxbm) {
        super.set("qypgsxbm", qypgsxbm);
    }

    /**
     * 开展区域评估事项名称
     */
    public String getQypgsxmc() {
        return super.get("qypgsxmc");
    }

    public void setQypgsxmc(String qypgsxmc) {
        super.set("qypgsxmc", qypgsxmc);
    }

    /**
     * 对应标准审批事项编码
     */
    public String getDybzspsxbm() {
        return super.get("dybzspsxbm");
    }

    public void setDybzspsxbm(String dybzspsxbm) {
        super.set("dybzspsxbm", dybzspsxbm);
    }

    /**
     * 区域评估成果生效日期
     */
    public Date getQypgcgsxrq() {
        return super.getDate("qypgcgsxrq");
    }

    public void setQypgcgsxrq(Date qypgcgsxrq) {
        super.set("qypgcgsxrq", qypgcgsxrq);
    }

    /**
     * 区域评估成果截止日期
     */
    public Date getQypgcgjzrq() {
        return super.getDate("qypgcgjzrq");
    }

    public void setQypgcgjzrq(Date qypgcgjzrq) {
        super.set("qypgcgjzrq", qypgcgjzrq);
    }

    /**
     * 简化审批的方式
     */
    public String getJhspdfs() {
        return super.get("jhspdfs");
    }

    public void setJhspdfs(String jhspdfs) {
        super.set("jhspdfs", jhspdfs);
    }

    /**
     * 区域评估成果材料类型
     */
    public String getQypgcgcllx() {
        return super.get("qypgcgcllx");
    }

    public void setQypgcgcllx(String qypgcgcllx) {
        super.set("qypgcgcllx", qypgcgcllx);
    }

    /**
     * 区域评估成果材料名称
     */
    public String getQypgcgclmc() {
        return super.get("qypgcgclmc");
    }

    public void setQypgcgclmc(String qypgcgclmc) {
        super.set("qypgcgclmc", qypgcgclmc);
    }

    /**
     * 区域评估成果附件ID
     */
    public String getQypgcgfjid() {
        return super.get("qypgcgfjid");
    }

    public void setQypgcgfjid(String qypgcgfjid) {
        super.set("qypgcgfjid", qypgcgfjid);
    }

    /**
     * 数据有效标识
     */
    public Integer getSjyxbs() {
        return super.getInt("sjyxbs");
    }

    public void setSjyxbs(Integer sjyxbs) {
        super.set("sjyxbs", sjyxbs);
    }

    /**
     * 数据无效原因
     */
    public String getSjwxyy() {
        return super.get("sjwxyy");
    }

    public void setSjwxyy(String sjwxyy) {
        super.set("sjwxyy", sjwxyy);
    }

    public String getTaskguid() {
        return super.get("taskguid");
    }

    public void setTaskguid(String taskguid) {
        super.set("taskguid", taskguid);
    }

    public String getCliengguid() {
        return super.get("cliengguid");
    }

    public void setCliengguid(String cliengguid) {
        super.set("cliengguid", cliengguid);
    }

    /**
     * 数据上传状态
     */
    public Integer getSjsczt() {
        return super.getInt("sjsczt");
    }

    public void setSjsczt(Integer sjsczt) {
        super.set("sjsczt", sjsczt);
    }

    /**
     * 失败原因
     */
    public String getSbyy() {
        return super.get("sbyy");
    }

    public void setSbyy(String sbyy) {
        super.set("sbyy", sbyy);
    }

    /**
     * 是否上报
     */
    public String getSync() {
        return super.get("sync");
    }

    public void setSync(String sync) {
        super.set("sync", sync);
    }

    public String getQypgguid() {
        return super.get("qypgguid");
    }

    public void setQypgguid(String qypgguid) {
        super.set("qypgguid", qypgguid);
    }

}
