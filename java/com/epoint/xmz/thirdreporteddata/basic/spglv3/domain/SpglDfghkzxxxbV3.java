package com.epoint.xmz.thirdreporteddata.basic.spglv3.domain;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 地方规划控制线信息表实体
 *
 * @version [版本号, 2023-09-25 15:35:51]
 * @作者 Epoint
 */
@Entity(table = "SPGL_DFGHKZXXXB_V3", id = "rowguid")
public class SpglDfghkzxxxbV3 extends BaseEntity implements Cloneable {
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
     * 控制线文件序号
     */
    public Integer getKzxwjxh() {
        return super.getInt("kzxwjxh");
    }

    public void setKzxwjxh(Integer kzxwjxh) {
        super.set("kzxwjxh", kzxwjxh);
    }

    /**
     * 控制线文件名称
     */
    public String getKzxwjmc() {
        return super.get("kzxwjmc");
    }

    public void setKzxwjmc(String kzxwjmc) {
        super.set("kzxwjmc", kzxwjmc);
    }

    /**
     * 控制线适用开始时间
     */
    public Date getKzxsykssj() {
        return super.getDate("kzxsykssj");
    }

    public void setKzxsykssj(Date kzxsykssj) {
        super.set("kzxsykssj", kzxsykssj);
    }

    /**
     * 控制线文件说明
     */
    public String getKzxwjsm() {
        return super.get("kzxwjsm");
    }

    public void setKzxwjsm(String kzxwjsm) {
        super.set("kzxwjsm", kzxwjsm);
    }

    /**
     * 控制线文件附件
     */
    public byte[] getKzxwjfj() {
        return super.getBytes("kzxwjfj");
    }

    public void setKzxwjfj(byte[] kzxwjfj) {
        super.set("kzxwjfj", kzxwjfj);
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
