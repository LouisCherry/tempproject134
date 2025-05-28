package com.epoint.xmz.thirdreporteddata.basic.spglv3.domain;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 住建部_项目审批事项办理信息表实体
 *
 * @version [版本号, 2018-11-16 15:08:57]
 * @作者 zhpengsy
 */
@Entity(table = "SPGL_XMSPSXBLXXB_V3", id = "rowguid")
public class SpglXmspsxblxxbV3 extends BaseEntity implements Cloneable {
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
     * 审批流程编码
     */
    public String getSplcbm() {
        return super.get("splcbm");
    }

    public void setSplcbm(String splcbm) {
        super.set("splcbm", splcbm);
    }

    /**
     * 审批流程版本号
     */
    public Double getSplcbbh() {
        return super.getDouble("splcbbh");
    }

    public void setSplcbbh(Double splcbbh) {
        super.set("splcbbh", splcbbh);
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
     * 审批部门编码
     */
    public String getSpbmbm() {
        return super.get("spbmbm");
    }

    public void setSpbmbm(String spbmbm) {
        super.set("spbmbm", spbmbm);
    }

    /**
     * 审批部门名称
     */
    public String getSpbmmc() {
        return super.get("spbmmc");
    }

    public void setSpbmmc(String spbmmc) {
        super.set("spbmmc", spbmmc);
    }

    /**
     * 受理方式
     */
    public Integer getSlfs() {
        return super.getInt("slfs");
    }

    public void setSlfs(Integer slfs) {
        super.set("slfs", slfs);
    }


    /**
     * 公开方式
     */
    public Integer getGkfs() {
        return super.getInt("gkfs");
    }

    public void setGkfs(Integer gkfs) {
        super.set("gkfs", gkfs);
    }

    /**
     * 并联审批实例编码
     */
    public String getBlspslbm() {
        return super.get("blspslbm");
    }

    public void setBlspslbm(String blspslbm) {
        super.set("blspslbm", blspslbm);
    }

    /**
     * 事项办理时限
     */
    public Integer getSxblsxm() {
        return super.get("sxblsx");
    }

    public void setSxblsx(Integer sxblsx) {
        super.set("sxblsx", sxblsx);
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
     * 联系人/代理人
     */
    public String getLxr() {
        return super.get("lxr");
    }

    public void setLxr(String lxr) {
        super.set("lxr", lxr);
    }


    /**
     * 联系人手机号
     */
    public String getLxrsjh() {
        return super.get("lxrsjh");
    }

    public void setLxrsjh(String lxrsjh) {
        super.set("lxrsjh", lxrsjh);
    }

    /**
     * 业务情形
     */
    public Integer getYwqx() {
        return super.get("ywqx");
    }

    public void setYwqx(Integer ywqx) {
        super.set("ywqx", ywqx);
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
     * 应用区域评估情况
     */
    public String getYyqypgqk() {
        return super.get("yyqypgqk");
    }

    public void setYyqypgqk(String yyqypgqk) {
        super.set("yyqypgqk", yyqypgqk);
    }

    /**
     * 实行告知承 诺制审批情况
     */
    public String getSxgzcnzspqk() {
        return super.get("sxgzcnzspqk");
    }

    public void setSxgzcnzspqk(String sxgzcnzspqk) {
        super.set("sxgzcnzspqk", sxgzcnzspqk);
    }
}
