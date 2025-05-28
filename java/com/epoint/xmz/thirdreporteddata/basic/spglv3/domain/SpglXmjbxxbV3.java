package com.epoint.xmz.thirdreporteddata.basic.spglv3.domain;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 住建部_项目基本信息表实体
 *
 * @version [版本号, 2018-11-16 15:08:41]
 * @作者 zhpengsy
 */
@Entity(table = "SPGL_XMJBXXB_V3", id = "rowguid")
public class SpglXmjbxxbV3 extends BaseEntity implements Cloneable {
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
     * 项目代码
     */
    public String getXmdm() {
        return super.get("xmdm");
    }

    public void setXmdm(String xmdm) {
        super.set("xmdm", xmdm);
    }

    /**
     * 项目名称
     */
    public String getXmmc() {
        return super.get("xmmc");
    }

    public void setXmmc(String xmmc) {
        super.set("xmmc", xmmc);
    }

    /**
     * 工程分类
     */
    public Integer getGcfl() {
        return super.getInt("gcfl");
    }

    public void setGcfl(Integer gcfl) {
        super.set("gcfl", gcfl);
    }

    /**
     * 立项类型
     */
    public Integer getLxlx() {
        return super.getInt("lxlx");
    }

    public void setLxlx(Integer lxlx) {
        super.set("lxlx", lxlx);
    }

    /**
     * 建设性质
     */
    public Integer getJsxz() {
        return super.getInt("jsxz");
    }

    public void setJsxz(Integer jsxz) {
        super.set("jsxz", jsxz);
    }

    /**
     * 项目资金属性
     */
    public Integer getXmzjsx() {
        return super.getInt("xmzjsx");
    }

    public void setXmzjsx(Integer xmzjsx) {
        super.set("xmzjsx", xmzjsx);
    }

    /**
     * 国标行业
     */
    public String getGbhy() {
        return super.get("gbhy");
    }

    public void setGbhy(String gbhy) {
        super.set("gbhy", gbhy);
    }

    /**
     * 拟开工时间
     */
    public Date getNkgsj() {
        return super.getDate("nkgsj");
    }

    public void setNkgsj(Date nkgsj) {
        super.set("nkgsj", nkgsj);
    }

    /**
     * 拟建成时间
     */
    public Date getNjcsj() {
        return super.getDate("njcsj");
    }

    public void setNjcsj(Date njcsj) {
        super.set("njcsj", njcsj);
    }

    /**
     * 总投资额（万元）
     */
    public Double getZtze() {
        return super.getDouble("ztze");
    }

    public void setZtze(Double ztze) {
        super.set("ztze", ztze);
    }

    /**
     * 建设地点
     */
    public String getJsdd() {
        return super.get("jsdd");
    }

    public void setJsdd(String jsdd) {
        super.set("jsdd", jsdd);
    }

    /**
     * 建设规模及内容
     */
    public String getJsgmjnr() {
        return super.get("jsgmjnr");
    }

    public void setJsgmjnr(String jsgmjnr) {
        super.set("jsgmjnr", jsgmjnr);
    }

    /**
     * 申报时间
     */
    public Date getSbsj() {
        return super.getDate("sbsj");
    }

    public void setSbsj(Date sbsj) {
        super.set("sbsj", sbsj);
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
     * 数据上传 状态
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
     * 工程代码
     */
    public String getGcdm() {
        return super.get("gcdm");
    }

    public void setGcdm(String gcdm) {
        super.set("gcdm", gcdm);
    }

    /**
     * 工程范围
     */
    public String getGcfw() {
        return super.get("gcfw");
    }

    public void setGcfw(String gcfw) {
        super.set("gcfw", gcfw);
    }

    /**
     * 前阶段关联工程代码
     */
    public String getQjdgcdm() {
        return super.get("qjdgcdm");
    }

    public void setQjdgcdm(String qjdgcdm) {
        super.set("qjdgcdm", qjdgcdm);
    }

    /**
     * 项目投资来源
     */
    public Integer getXmtzly() {
        return super.getInt("xmtzly");
    }

    public void setXmtzly(Integer xmtzly) {
        super.set("xmtzly", xmtzly);
    }

    /**
     * 土地获取方式
     */
    public Integer getTdhqfs() {
        return super.getInt("tdhqfs");
    }

    public void setTdhqfs(Integer tdhqfs) {
        super.set("tdhqfs", tdhqfs);
    }

    /**
     * 土地是否带设计方案
     */
    public Integer getTdsfdsjfa() {
        return super.getInt("tdsfdsjfa");
    }

    public void setTdsfdsjfa(Integer tdsfdsjfa) {
        super.set("tdsfdsjfa", tdsfdsjfa);
    }

    /**
     * 是否完成区域评估
     */
    public Integer getSfwcqypg() {
        return super.getInt("sfwcqypg");
    }

    public void setSfwcqypg(Integer sfwcqypg) {
        super.set("sfwcqypg", sfwcqypg);
    }

    /**
     * 审批流程类型
     */
    public Integer getSplclx() {
        return super.getInt("splclx");
    }

    public void setSplclx(Integer splclx) {
        super.set("splclx", splclx);
    }

    /**
     * 国标行业代码发布年代
     */
    public String getGbhydmfbnd() {
        return super.get("gbhydmfbnd");
    }

    public void setGbhydmfbnd(String gbhydmfbnd) {
        super.set("gbhydmfbnd", gbhydmfbnd);
    }

    /**
     * 项目是否完全办结
     */
    public Integer getXmsfwqbjd() {
        return super.getInt("xmsfwqbj");
    }

    public void setXmsfwqbj(Integer xmsfwqbj) {
        super.set("xmsfwqbj", xmsfwqbj);
    }

    /**
     * 项目完全办结时间
     */
    public Date getXmwqbjsj() {
        return super.getDate("xmwqbjsj");
    }

    public void setXmwqbjsj(Date xmwqbjsj) {
        super.set("xmwqbjsj", xmwqbjsj);
    }

    /**
     * 建设地点行政区划
     */
    public String getJsddxzqh() {
        return super.get("jsddxzqh");
    }

    public void setJsddxzqh(String jsddxzqh) {
        super.set("jsddxzqh", jsddxzqh);
    }

    /**
     * 建设单位
     */
    public String getJsdw() {
        return super.get("jsdw");
    }

    public void setJsdw(String jsdw) {
        super.set("jsdw", jsdw);
    }

    /**
     * 建设单位代码
     */
    public String getJsdwdm() {
        return super.get("jsdwdm");
    }

    public void setJsdwdm(String jsdwdm) {
        super.set("jsdwdm", jsdwdm);
    }

    /**
     * 建设单位类型
     */
    public Integer getJsdwlx() {
        return super.getInt("jsdwlx");
    }

    public void setJsdwlx(Integer jsdwlx) {
        super.set("jsdwlx", jsdwlx);
    }

    /**
     * 项目经纬度坐标
     */
    public String getXmjwdzb() {
        return super.get("xmjwdzb");
    }

    public void setXmjwdzb(String xmjwdzb) {
        super.set("xmjwdzb", xmjwdzb);
    }


    /**
     * 是否线性工程
     */
    public Integer getSfxxgc() {
        return super.getInt("sfxxgc");
    }

    public void setSfxxgc(Integer sfxxgc) {
        super.set("sfxxgc", sfxxgc);
    }

    /**
     * 工程行业分类
     */
    public String getGchyfl() {
        return super.get("gchyfl");
    }

    public void setGchyfl(String gchyfl) {
        super.set("gchyfl", gchyfl);
    }

    /**
     * 长度
     */
    public Double getCd() {
        return super.getDouble("cd");
    }

    public void setCd(Double cd) {
        super.set("cd", cd);
    }


    /**
     * 用地面积
     */
    public Double getYdmj() {
        return super.getDouble("ydmj");
    }

    public void setYdmj(Double ydmj) {
        super.set("ydmj", ydmj);
    }

    /**
     * 建筑面积
     */
    public Double getJzmj() {
        return super.getDouble("jzmj");
    }

    public void setJzmj(Double jzmj) {
        super.set("jzmj", jzmj);
    }

}
