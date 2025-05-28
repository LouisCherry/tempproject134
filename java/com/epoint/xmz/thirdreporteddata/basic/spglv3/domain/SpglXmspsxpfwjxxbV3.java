package com.epoint.xmz.thirdreporteddata.basic.spglv3.domain;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 住建部_审批事项批复文件信息表实体
 *
 * @version [版本号, 2018-11-16 15:09:30]
 * @作者 zhpengsy
 */
@Entity(table = "SPGL_XMSPSXPFWJXXB_V3", id = "rowguid")
public class SpglXmspsxpfwjxxbV3 extends BaseEntity implements Cloneable {
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
     * 批复日期
     */
    public Date getPfrq() {
        return super.getDate("pfrq");
    }

    public void setPfrq(Date pfrq) {
        super.set("pfrq", pfrq);
    }

    /**
     * 批复文号
     */
    public String getPfwh() {
        return super.get("pfwh");
    }

    public void setPfwh(String pfwh) {
        super.set("pfwh", pfwh);
    }

    /**
     * 批复文件标题
     */
    public String getPfwjbt() {
        return super.get("pfwjbt");
    }

    public void setPfwjbt(String pfwjbt) {
        super.set("pfwjbt", pfwjbt);
    }

    /**
     * 批复文件有效期限
     */
    public Date getPfwjyxqx() {
        return super.getDate("pfwjyxqx");
    }

    public void setPfwjyxqx(Date pfwjyxqx) {
        super.set("pfwjyxqx", pfwjyxqx);
    }

    /**
     * 审批结果类型
     */
    public String getSpjglx() {
        return super.get("spjglx");
    }

    public void setSpjglx(String spjglx) {
        super.set("spjglx", spjglx);
    }

    /**
     * 证照编号
     */
    public String getZzbh() {
        return super.get("zzbh");
    }

    public void setZzbh(String zzbh) {
        super.set("zzbh", zzbh);
    }

    /**
     * 证照标识
     */
    public String getZzbs() {
        return super.get("zzbs");
    }

    public void setZzbs(String zzbs) {
        super.set("zzbs", zzbs);
    }

    /**
     * 电子证照文件路径
     */
    public String getDzzzwjlj() {
        return super.get("dzzzwjlj");
    }

    public void setDzzzwjlj(String dzzzwjlj) {
        super.set("dzzzwjlj", dzzzwjlj);
    }

    /**
     * 附件名称
     */
    public String getFjmc() {
        return super.get("fjmc");
    }

    public void setFjmc(String fjmc) {
        super.set("fjmc", fjmc);
    }

    /**
     * 附件ID
     */
    public String getFjid() {
        return super.get("fjid");
    }

    public void setFjid(String fjid) {
        super.set("fjid", fjid);
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
    public String getSjxwyy() {
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
     * 附件类型
     */
    public String getFjlx() {
        return super.get("fjlx");
    }

    public void setFjlx(String fjlx) {
        super.set("fjlx", fjlx);
    }

}
