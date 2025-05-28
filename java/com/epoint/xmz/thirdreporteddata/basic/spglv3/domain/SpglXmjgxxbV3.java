package com.epoint.xmz.thirdreporteddata.basic.spglv3.domain;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 项目监管信息表实体
 *
 * @version [版本号, 2023-09-25 15:12:50]
 * @作者 Epoint
 */
@Entity(table = "SPGL_XMJGXXB_V3", id = "rowguid")
public class SpglXmjgxxbV3 extends BaseEntity implements Cloneable {
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
     * 异常情形
     */
    public Integer getYcqx() {
        return super.getInt("ycqx");
    }

    public void setYcqx(Integer ycqx) {
        super.set("ycqx", ycqx);
    }

    /**
     * 异常级别
     */
    public Integer getYcjb() {
        return super.getInt("ycjb");
    }

    public void setYcjb(Integer ycjb) {
        super.set("ycjb", ycjb);
    }

    /**
     * 异常行为内容
     */
    public String getYcxwnr() {
        return super.get("ycxwnr");
    }

    public void setYcxwnr(String ycxwnr) {
        super.set("ycxwnr", ycxwnr);
    }

    /**
     * 记录异常时间
     */
    public Date getJlycsj() {
        return super.getDate("jlycsj");
    }

    public void setJlycsj(Date jlycsj) {
        super.set("jlycsj", jlycsj);
    }

    /**
     * 监管部门名称
     */
    public String getJgbmmc() {
        return super.get("jgbmmc");
    }

    public void setJgbmmc(String jgbmmc) {
        super.set("jgbmmc", jgbmmc);
    }

    /**
     * 监管部门代码
     */
    public String getJgbmdm() {
        return super.get("jgbmdm");
    }

    public void setJgbmdm(String jgbmdm) {
        super.set("jgbmdm", jgbmdm);
    }

    /**
     * 监管部门处理结果
     */
    public String getJgbmcljg() {
        return super.get("jgbmcljg");
    }

    public void setJgbmcljg(String jgbmcljg) {
        super.set("jgbmcljg", jgbmcljg);
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