package com.epoint.xmz.thirdreporteddata.basic.spglv3.domain;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 勘察设计人员信息表实体
 *
 * @version [版本号, 2023-09-25 11:21:13]
 * @作者 Epoint
 */
@Entity(table = "SPGL_KCSJRYXXB_V3", id = "rowguid")
public class SpglKcsjryxxbV3 extends BaseEntity implements Cloneable {
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
     * 项目代码
     */
    public String getXmdm() {
        return super.get("xmdm");
    }

    public void setXmdm(String xmdm) {
        super.set("xmdm", xmdm);
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
     * 施工图审查业务编号
     */
    public String getStywbh() {
        return super.get("stywbh");
    }

    public void setStywbh(String stywbh) {
        super.set("stywbh", stywbh);
    }

    /**
     * 单体编码
     */
    public String getDtbm() {
        return super.get("dtbm");
    }

    public void setDtbm(String dtbm) {
        super.set("dtbm", dtbm);
    }

    /**
     * 人员姓名
     */
    public String getRyxm() {
        return super.get("ryxm");
    }

    public void setRyxm(String ryxm) {
        super.set("ryxm", ryxm);
    }

    /**
     * 人员类型
     */
    public Integer getRylx() {
        return super.getInt("rylx");
    }

    public void setRylx(Integer rylx) {
        super.set("rylx", rylx);
    }

    /**
     * 人员专业类型
     */
    public String getRyzylx() {
        return super.get("ryzylx");
    }

    public void setRyzylx(String ryzylx) {
        super.set("ryzylx", ryzylx);
    }

    /**
     * 人员身份证件类型
     */
    public Integer getRysfzjlx() {
        return super.getInt("rysfzjlx");
    }

    public void setRysfzjlx(Integer rysfzjlx) {
        super.set("rysfzjlx", rysfzjlx);
    }

    /**
     * 人员证件号码
     */
    public String getRyzjhm() {
        return super.get("ryzjhm");
    }

    public void setRyzjhm(String ryzjhm) {
        super.set("ryzjhm", ryzjhm);
    }

    /**
     * 人员联系电话
     */
    public String getRylxfs() {
        return super.get("rylxfs");
    }

    public void setRylxfs(String rylxfs) {
        super.set("rylxfs", rylxfs);
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
     * 是否上报
     */
    public String getSync() {
        return super.get("sync");
    }

    public void setSync(String sync) {
        super.set("sync", sync);
    }

}