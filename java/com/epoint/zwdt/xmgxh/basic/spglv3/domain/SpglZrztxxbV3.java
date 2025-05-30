package com.epoint.zwdt.xmgxh.basic.spglv3.domain;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 责任主体信息表实体
 * 
 * @作者 Epoint
 * @version [版本号, 2023-09-25 17:01:40]
 */
@Entity(table = "SPGL_ZRZTXXB_V3", id = "rowguid")
public class SpglZrztxxbV3 extends BaseEntity implements Cloneable
{
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
     * 审批事项实例编码
     */
    public String getSpsxslbm() {
        return super.get("spsxslbm");
    }

    public void setSpsxslbm(String spsxslbm) {
        super.set("spsxslbm", spsxslbm);
    }

    /**
     * 许可（备案、技术审查）编号
     */
    public String getXkbabh() {
        return super.get("xkbabh");
    }

    public void setXkbabh(String xkbabh) {
        super.set("xkbabh", xkbabh);
    }

    /**
     * 单位类型
     */
    public Integer getDwlx() {
        return super.getInt("dwlx");
    }

    public void setDwlx(Integer dwlx) {
        super.set("dwlx", dwlx);
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
     * 资质等级
     */
    public String getZzdj() {
        return super.get("zzdj");
    }

    public void setZzdj(String zzdj) {
        super.set("zzdj", zzdj);
    }

    /**
     * 单位法定代表人姓名
     */
    public String getFddbr() {
        return super.get("fddbr");
    }

    public void setFddbr(String fddbr) {
        super.set("fddbr", fddbr);
    }

    /**
     * 单位法定代表人证件类型
     */
    public Integer getFrzjlx() {
        return super.getInt("frzjlx");
    }

    public void setFrzjlx(Integer frzjlx) {
        super.set("frzjlx", frzjlx);
    }

    /**
     * 单位法定代表人证件号码
     */
    public String getFrzjhm() {
        return super.get("frzjhm");
    }

    public void setFrzjhm(String frzjhm) {
        super.set("frzjhm", frzjhm);
    }

    /**
     * 项目负责人姓名
     */
    public String getFzrxm() {
        return super.get("fzrxm");
    }

    public void setFzrxm(String fzrxm) {
        super.set("fzrxm", fzrxm);
    }

    /**
     * 项目负责人身份证件类型
     */
    public Integer getFzrzjlx() {
        return super.getInt("fzrzjlx");
    }

    public void setFzrzjlx(Integer fzrzjlx) {
        super.set("fzrzjlx", fzrzjlx);
    }

    /**
     * 项目负责人证件号码
     */
    public String getFzrzjhm() {
        return super.get("fzrzjhm");
    }

    public void setFzrzjhm(String fzrzjhm) {
        super.set("fzrzjhm", fzrzjhm);
    }

    /**
     * 项目负责人联系电话
     */
    public String getFzrlxdh() {
        return super.get("fzrlxdh");
    }

    public void setFzrlxdh(String fzrlxdh) {
        super.set("fzrlxdh", fzrlxdh);
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
