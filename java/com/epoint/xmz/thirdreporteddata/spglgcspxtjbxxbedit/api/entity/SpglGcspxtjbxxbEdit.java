package com.epoint.xmz.thirdreporteddata.spglgcspxtjbxxbedit.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 上报工改系统基本信息表实体
 *
 * @version [版本号, 2023-08-31 15:33:34]
 * @作者 lzhming
 */
@Entity(table = "SPGL_GCSPXTJBXXB_EDIT", id = "ROWGUID")
public class SpglGcspxtjbxxbEdit extends BaseEntity implements Cloneable {
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
    public String getLsh() {
        return super.get("lsh");
    }

    public void setLsh(String lsh) {
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
     * 系统建设方式
     */
    public String getXtjsfs() {
        return super.get("xtjsfs");
    }

    public void setXtjsfs(String xtjsfs) {
        super.set("xtjsfs", xtjsfs);
    }

    /**
     * 建设单位名称
     */
    public String getJsdwmc() {
        return super.get("jsdwmc");
    }

    public void setJsdwmc(String jsdwmc) {
        super.set("jsdwmc", jsdwmc);
    }

    /**
     * 建设单位联系人
     */
    public String getJsdwlxr() {
        return super.get("jsdwlxr");
    }

    public void setJsdwlxr(String jsdwlxr) {
        super.set("jsdwlxr", jsdwlxr);
    }

    /**
     * 建设单位联系人电话
     */
    public String getJsdwlxrdh() {
        return super.get("jsdwlxrdh");
    }

    public void setJsdwlxrdh(String jsdwlxrdh) {
        super.set("jsdwlxrdh", jsdwlxrdh);
    }

    /**
     * 建设单位联系人手机号
     */
    public String getJsdwlxrsjh() {
        return super.get("jsdwlxrsjh");
    }

    public void setJsdwlxrsjh(String jsdwlxrsjh) {
        super.set("jsdwlxrsjh", jsdwlxrsjh);
    }

    /**
     * 运维单位名称
     */
    public String getYwdwmc() {
        return super.get("ywdwmc");
    }

    public void setYwdwmc(String ywdwmc) {
        super.set("ywdwmc", ywdwmc);
    }

    /**
     * 运维工作联系人
     */
    public String getYwgzlxr() {
        return super.get("ywgzlxr");
    }

    public void setYwgzlxr(String ywgzlxr) {
        super.set("ywgzlxr", ywgzlxr);
    }

    /**
     * 运维工作联系人电话
     */
    public String getYwgzlxrdh() {
        return super.get("ywgzlxrdh");
    }

    public void setYwgzlxrdh(String ywgzlxrdh) {
        super.set("ywgzlxrdh", ywgzlxrdh);
    }

    /**
     * 运维工作联系人手机号
     */
    public String getYwgzlxrsjh() {
        return super.get("ywgzlxrsjh");
    }

    public void setYwgzlxrsjh(String ywgzlxrsjh) {
        super.set("ywgzlxrsjh", ywgzlxrsjh);
    }

    /**
     * 地方工程审批系统地址
     */
    public String getDfgcspxtdz() {
        return super.get("dfgcspxtdz");
    }

    public void setDfgcspxtdz(String dfgcspxtdz) {
        super.set("dfgcspxtdz", dfgcspxtdz);
    }

    /**
     * 数据有效标识
     */
    public String getSjyxbs() {
        return super.get("sjyxbs");
    }

    public void setSjyxbs(String sjyxbs) {
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

    /**
     * 数据上传状态
     */
    public String getSjsczt() {
        return super.get("sjsczt");
    }

    public void setSjsczt(String sjsczt) {
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
     * 同步标识
     */
    public String getSync() {
        return super.get("sync");
    }

    public void setSync(String sync) {
        super.set("sync", sync);
    }
}