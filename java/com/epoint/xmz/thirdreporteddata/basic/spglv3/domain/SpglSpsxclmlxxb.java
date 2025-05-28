package com.epoint.xmz.thirdreporteddata.basic.spglv3.domain;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 审批事项材料目录信息表实体
 *
 * @version [版本号, 2023-09-19 17:17:26]
 * @作者 Epoint
 */
@Entity(table = "spgl_spsxclmlxxb_V3", id = "rowguid")
public class SpglSpsxclmlxxb extends BaseEntity implements Cloneable {
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
     * 审批事项编码
     */
    public String getSpsxbm() {
        return super.get("spsxbm");
    }

    public void setSpsxbm(String spsxbm) {
        super.set("spsxbm", spsxbm);
    }

    /**
     * 审批事项版本号
     */
    public Double getSpsxbbh() {
        return super.getDouble("spsxbbh");
    }

    public void setSpsxbbh(Double spsxbbh) {
        super.set("spsxbbh", spsxbbh);
    }

    /**
     * 排序
     */
    public Integer getPx() {
        return super.getInt("px");
    }

    public void setPx(Integer px) {
        super.set("px", px);
    }

    /**
     * 材料目录编号
     */
    public String getClmlbh() {
        return super.get("clmlbh");
    }

    public void setClmlbh(String clmlbh) {
        super.set("clmlbh", clmlbh);
    }

    /**
     * 材料名称
     */
    public String getClmc() {
        return super.get("clmc");
    }

    public void setClmc(String clmc) {
        super.set("clmc", clmc);
    }

    /**
     * 材料类型
     */
    public Integer getCllx() {
        return super.getInt("cllx");
    }

    public void setCllx(Integer cllx) {
        super.set("cllx", cllx);
    }

    /**
     * 材料形式
     */
    public Integer getClxs() {
        return super.getInt("clxs");
    }

    public void setClxs(Integer clxs) {
        super.set("clxs", clxs);
    }

    /**
     * 材料必要性
     */
    public Integer getClbyx() {
        return super.getInt("clbyx");
    }

    public void setClbyx(Integer clbyx) {
        super.set("clbyx", clbyx);
    }

    /**
     * 空白表格
     */
    public String getKbbg() {
        return super.get("kbbg");
    }

    public void setKbbg(String kbbg) {
        super.set("kbbg", kbbg);
    }

    /**
     * 示例样本
     */
    public String getSlyb() {
        return super.get("slyb");
    }

    public void setSlyb(String slyb) {
        super.set("slyb", slyb);
    }

    /**
     * 来源渠道
     */
    public Integer getLyqd() {
        return super.getInt("lyqd");
    }

    public void setLyqd(Integer lyqd) {
        super.set("lyqd", lyqd);
    }

    /**
     * 来源渠道说明
     */
    public String getLyqdsm() {
        return super.get("lyqdsm");
    }

    public void setLyqdsm(String lyqdsm) {
        super.set("lyqdsm", lyqdsm);
    }

    /**
     * 纸质材料份数
     */
    public Integer getZzclfs() {
        return super.getInt("zzclfs");
    }

    public void setZzclfs(Integer zzclfs) {
        super.set("zzclfs", zzclfs);
    }

    /**
     * 纸质材料规格
     */
    public String getZzclgg() {
        return super.get("zzclgg");
    }

    public void setZzclgg(String zzclgg) {
        super.set("zzclgg", zzclgg);
    }

    /**
     * 填报须知
     */
    public String getTbxz() {
        return super.get("tbxz");
    }

    public void setTbxz(String tbxz) {
        super.set("tbxz", tbxz);
    }

    /**
     * 受理标准
     */
    public String getSlbz() {
        return super.get("slbz");
    }

    public void setSlbz(String slbz) {
        super.set("slbz", slbz);
    }

    /**
     * 要求提供材料的依据
     */
    public String getYqtgcldyj() {
        return super.get("yqtgcldyj");
    }

    public void setYqtgcldyj(String yqtgcldyj) {
        super.set("yqtgcldyj", yqtgcldyj);
    }

    /**
     * 备注
     */
    public String getBz() {
        return super.get("bz");
    }

    public void setBz(String bz) {
        super.set("bz", bz);
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
     * 同步标识
     */
    public Integer getSync() {
        return super.getInt("sync");
    }

    public void setSync(Integer sync) {
        super.set("sync", sync);
    }

}
