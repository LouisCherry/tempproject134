package com.epoint.xmz.thirdreporteddata.basic.spglv3.domain;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 施工图设计文件审查信息表实体
 *
 * @version [版本号, 2023-09-25 11:32:08]
 * @作者 Epoint
 */
@Entity(table = "SPGL_SGTSJWJSCXXB_V3", id = "rowguid")
public class SpglSgtsjwjscxxbV3 extends BaseEntity implements Cloneable {
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
     * 业务情形
     */
    public Integer getYwqx() {
        return super.getInt("ywqx");
    }

    public void setYwqx(Integer ywqx) {
        super.set("ywqx", ywqx);
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
     * 建设单位统一社会信用代码
     */
    public String getJstyshxydm() {
        return super.get("jstyshxydm");
    }

    public void setJstyshxydm(String jstyshxydm) {
        super.set("jstyshxydm", jstyshxydm);
    }

    /**
     * 勘察审查机构
     */
    public String getKcjg() {
        return super.get("kcjg");
    }

    public void setKcjg(String kcjg) {
        super.set("kcjg", kcjg);
    }

    /**
     * 勘察审查机构统一社会信用代码
     */
    public String getKcjgtyshxydm() {
        return super.get("kcjgtyshxydm");
    }

    public void setKcjgtyshxydm(String kcjgtyshxydm) {
        super.set("kcjgtyshxydm", kcjgtyshxydm);
    }

    /**
     * 设计单位
     */
    public String getSjdw() {
        return super.get("sjdw");
    }

    public void setSjdw(String sjdw) {
        super.set("sjdw", sjdw);
    }

    /**
     * 设计单位统一社会信用代码
     */
    public String getSjtyshxydm() {
        return super.get("sjtyshxydm");
    }

    public void setSjtyshxydm(String sjtyshxydm) {
        super.set("sjtyshxydm", sjtyshxydm);
    }

    /**
     * 设计单位项目负责人
     */
    public String getSjdwxmfzr() {
        return super.get("sjdwxmfzr");
    }

    public void setSjdwxmfzr(String sjdwxmfzr) {
        super.set("sjdwxmfzr", sjdwxmfzr);
    }

    /**
     * 设计单位项目负责人身份证号码
     */
    public String getSjdwxmfzrhm() {
        return super.get("sjdwxmfzrhm");
    }

    public void setSjdwxmfzrhm(String sjdwxmfzrhm) {
        super.set("sjdwxmfzrhm", sjdwxmfzrhm);
    }

    /**
     * 设计审查机构
     */
    public String getScjg() {
        return super.get("scjg");
    }

    public void setScjg(String scjg) {
        super.set("scjg", scjg);
    }

    /**
     * 设计审查机构统一社会信用代码
     */
    public String getScjgtyshxydm() {
        return super.get("scjgtyshxydm");
    }

    public void setScjgtyshxydm(String scjgtyshxydm) {
        super.set("scjgtyshxydm", scjgtyshxydm);
    }

    /**
     * 是否与人防联合审查
     */
    public Integer getSfyrflhsc() {
        return super.getInt("sfyrflhsc");
    }

    public void setSfyrflhsc(Integer sfyrflhsc) {
        super.set("sfyrflhsc", sfyrflhsc);
    }

    /**
     * 是否与消防联合审查
     */
    public Integer getSfyxflhsc() {
        return super.getInt("sfyxflhsc");
    }

    public void setSfyxflhsc(Integer sfyxflhsc) {
        super.set("sfyxflhsc", sfyxflhsc);
    }

    /**
     * 是否数字化审查
     */
    public Integer getSfszhsc() {
        return super.getInt("sfszhsc");
    }

    public void setSfszhsc(Integer sfszhsc) {
        super.set("sfszhsc", sfszhsc);
    }

    /**
     * 审查开始日期
     */
    public Date getScksrq() {
        return super.getDate("scksrq");
    }

    public void setScksrq(Date scksrq) {
        super.set("scksrq", scksrq);
    }

    /**
     * 施工图审查合格书编号
     */
    public String getSchgsbh() {
        return super.get("schgsbh");
    }

    public void setSchgsbh(String schgsbh) {
        super.set("schgsbh", schgsbh);
    }

    /**
     * 初审完成时间
     */
    public Date getCswcsj() {
        return super.getDate("cswcsj");
    }

    public void setCswcsj(Date cswcsj) {
        super.set("cswcsj", cswcsj);
    }

    /**
     * 初审是否通过
     */
    public Integer getCssftg() {
        return super.getInt("cssftg");
    }

    public void setCssftg(Integer cssftg) {
        super.set("cssftg", cssftg);
    }

    /**
     * 初审意见
     */
    public String getCsyj() {
        return super.get("csyj");
    }

    public void setCsyj(String csyj) {
        super.set("csyj", csyj);
    }

    /**
     * 审查中发现违反强条总数
     */
    public Integer getSczfxwfqtzs() {
        return super.getInt("sczfxwfqtzs");
    }

    public void setSczfxwfqtzs(Integer sczfxwfqtzs) {
        super.set("sczfxwfqtzs", sczfxwfqtzs);
    }

    /**
     * 审查通过时间
     */
    public Date getSctgsj() {
        return super.getDate("sctgsj");
    }

    public void setSctgsj(Date sctgsj) {
        super.set("sctgsj", sctgsj);
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