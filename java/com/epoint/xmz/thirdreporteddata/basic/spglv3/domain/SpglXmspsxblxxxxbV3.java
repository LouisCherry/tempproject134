package com.epoint.xmz.thirdreporteddata.basic.spglv3.domain;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 住建部_项目审批事项办理详细信息表实体
 *
 * @version [版本号, 2018-11-16 15:09:04]
 * @作者 zhpengsy
 */
@Entity(table = "SPGL_XMSPSXBLXXXXB_V3", id = "rowguid")
public class SpglXmspsxblxxxxbV3 extends BaseEntity implements Cloneable {
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
    public Long getLsh() {
        return super.getLong("lsh");
    }

    public void setLsh(Long lsh) {
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
     * 工程代码
     */
    public String getGcdm() {
        return super.get("gcdm");
    }

    public void setGcdm(String gcdm) {
        super.set("gcdm", gcdm);
    }

    /**
     * 审批事项实例编码
     */
    public String getSpsxslbm() {
        return super.get("spsxslbm");
    }

    public void setSpsxslbm(String spsxslbm) {
        super.set("spsxslbm", spsxslbm);
    }

    /**
     * 办理处（科）室
     */
    public String getBlcs() {
        return super.get("blcs");
    }

    public void setBlcs(String blcs) {
        super.set("blcs", blcs);
    }

    /**
     * 办理人
     */
    public String getBlr() {
        return super.get("blr");
    }

    public void setBlr(String blr) {
        super.set("blr", blr);
    }

    /**
     * 办理状态
     */
    public Integer getBlzt() {
        return super.getInt("blzt");
    }

    public void setBlzt(Integer blzt) {
        super.set("blzt", blzt);
    }

    /**
     * 办理意见
     */
    public String getBlyj() {
        return super.get("blyj");
    }

    public void setBlyj(String blyj) {
        super.set("blyj", blyj);
    }

    /**
     * 办理时间
     */
    public Date getBlsj() {
        return super.getDate("blsj");
    }

    public void setBlsj(Date blsj) {
        super.set("blsj", blsj);
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
     * 单位名称
     */
    public String getDwmc() {
        return super.get("dwmc");
    }

    public void setDwmc(String dwmc) {
        super.set("dwmc", dwmc);
    }

    /**
     * 单位统一社会信用代码
     */
    public String getDwtyshxydm() {
        return super.get("dwtyshxydm");
    }

    public void setDwtyshxydm(String dwtyshxydm) {
        super.set("dwtyshxydm", dwtyshxydm);
    }

    /**
     * 数据来源
     */
    public String getSjly() {
        return super.get("sjly");
    }

    public void setSjly(String sjly) {
        super.set("sjly", sjly);
    }

}
