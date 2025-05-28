package com.epoint.xmz.thirdreporteddata.spglqypg.spglqypgxxb.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 区域评估信息表实体
 *
 * @version [版本号, 2023-09-15 13:56:34]
 * @作者 Epoint
 */
@Entity(table = "SPGL_QYPGXXB_EDIT", id = "rowguid")
public class SpglQypgxxb extends BaseEntity implements Cloneable {
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
     * 区域评估区域名称
     */
    public String getQypgqymc() {
        return super.get("qypgqymc");
    }

    public void setQypgqymc(String qypgqymc) {
        super.set("qypgqymc", qypgqymc);
    }

    /**
     * 区域评估范围描述
     */
    public String getQypgfwms() {
        return super.get("qypgfwms");
    }

    public void setQypgfwms(String qypgfwms) {
        super.set("qypgfwms", qypgfwms);
    }

    /**
     * 区域评估范围坐标信息
     */
    public String getQypgfwzbxx() {
        return super.get("qypgfwzbxx");
    }

    public void setQypgfwzbxx(String qypgfwzbxx) {
        super.set("qypgfwzbxx", qypgfwzbxx);
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

    public String getItemcode() {
        return super.get("itemcode");
    }

    public void setItemcode(String itemcode) {
        super.set("itemcode", itemcode);
    }

    public String getAreaname() {
        return super.get("areaname");
    }

    public void setAreaname(String areaname) {
        super.set("areaname", areaname);
    }

    /**
     * 父项目标识
     *
     * @return
     */
    public String getPreItemGuid() {
        return super.get("pre_itemguid");
    }

    public void setPreItemGuid(String pre_itemguid) {
        super.set("pre_itemguid", pre_itemguid);
    }

    /**
     * 是否上报
     */
    public Integer getSync() {
        return super.getInt("sync");
    }

    public void setSync(Integer sync) {
        super.set("sync", sync);
    }

    /**
     * 区域评估面积
     */
    public Double getQypgmj() {
        return super.getDouble("qypgmj");
    }

    public void setQypgmj(Double qypgmj) {
        super.set("qypgmj", qypgmj);
    }

}
