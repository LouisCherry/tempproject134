package com.epoint.zbxfdj.xfdj.spglxmspgcxxb.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 项目审批过程信息表实体
 *
 * @author Anber
 * @version [版本号, 2022-12-22 21:30:33]
 */
@Entity(table = "370800_SPGL_XMSPGCXXB", id = "id")
public class SpglXmspgcxxb extends BaseEntity implements Cloneable {
    private static final long serialVersionUID = 1L;

    /**
     * 数据主键
     */
    public String getId() {
        return super.get("id");
    }

    public void setId(String id) {
        super.set("id", id);
    }

    /**
     * 流水号
     */
    public String getLsh() {
        return super.get("lsh");
    }

    public void setLsh(String lsh) {
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
     * 审批人姓名
     */
    public String getSprxm() {
        return super.get("sprxm");
    }

    public void setSprxm(String sprxm) {
        super.set("sprxm", sprxm);
    }

    /**
     * 审批时间
     */
    public Date getSpsj() {
        return super.getDate("spsj");
    }

    public void setSpsj(Date spsj) {
        super.set("spsj", spsj);
    }

    /**
     * 审批动作类型
     */
    public Integer getSpdzlx() {
        return super.getInt("spdzlx");
    }

    public void setSpdzlx(Integer spdzlx) {
        super.set("spdzlx", spdzlx);
    }

    /**
     * 审批结果
     */
    public Integer getSpjg() {
        return super.getInt("spjg");
    }

    public void setSpjg(Integer spjg) {
        super.set("spjg", spjg);
    }

    /**
     * 审批意见
     */
    public String getSpyj() {
        return super.get("spyj");
    }

    public void setSpyj(String spyj) {
        super.set("spyj", spyj);
    }

    /**
     * 审批节点名称
     */
    public String getSpjdmc() {
        return super.get("spjdmc");
    }

    public void setSpjdmc(String spjdmc) {
        super.set("spjdmc", spjdmc);
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
     * 数据状态标识
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
     * 流程实例标识
     */
    public String getPviguid() {
        return super.get("pviguid");
    }

    public void setPviguid(String pviguid) {
        super.set("pviguid", pviguid);
    }

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
     * 年份标识
     */
    public String getYearflag() {
        return super.get("yearflag");
    }

    public void setYearflag(String yearflag) {
        super.set("yearflag", yearflag);
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
     * 操作人所属单位guid
     */
    public String getOperateuserbaseouguid() {
        return super.get("operateuserbaseouguid");
    }

    public void setOperateuserbaseouguid(String operateuserbaseouguid) {
        super.set("operateuserbaseouguid", operateuserbaseouguid);
    }

    /**
     * 操作人所属部门guid
     */
    public String getOperateuserouguid() {
        return super.get("operateuserouguid");
    }

    public void setOperateuserouguid(String operateuserouguid) {
        super.set("operateuserouguid", operateuserouguid);
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
     * 操作人guid
     */
    public String getOperateuserguid() {
        return super.get("operateuserguid");
    }

    public void setOperateuserguid(String operateuserguid) {
        super.set("operateuserguid", operateuserguid);
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
     * 序号
     */
    public Integer getRow_id() {
        return super.getInt("row_id");
    }

    public void setRow_id(Integer row_id) {
        super.set("row_id", row_id);
    }

}
