package com.epoint.zbxfdj.xfdj.spglxmspgcpfwjxxb.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 项目审批过程批复文件信息表
 *
 * @author Anber
 * @version [版本号, 2022-12-22 21:30:33]
 */
@Entity(table = "370800_SPGL_XMSPGCPFWJXXB", id = "id")
public class SpglXmspgcpfwjxxb extends BaseEntity implements Cloneable {
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
     * 审批过程ID
     */
    public String getSpgcid() {
        return super.get("spgcid");
    }

    public void setSpgcid(String spgcid) {
        super.set("spgcid", spgcid);
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
     * 批复日期
     */
    public Date getPfrq() {
        return super.getDate("pfrq");
    }

    public void setPfrq(Date pfrq) {
        super.set("pfrq", pfrq);
    }

    /**
     * 批复文号
     */
    public String getPfwh() {
        return super.get("pfwh");
    }

    public void setPfwh(String pfwh) {
        super.set("pfwh", pfwh);
    }

    /**
     * 批复文件标题
     */
    public String getPfwjbt() {
        return super.get("pfwjbt");
    }

    public void setPfwjbt(String pfwjbt) {
        super.set("pfwjbt", pfwjbt);
    }

    /**
     * 批复文件有效期限
     */
    public Date getPfwjyxqx() {
        return super.getDate("pfwjyxqx");
    }

    public void setPfwjyxqx(Date pfwjyxqx) {
        super.set("pfwjyxqx", pfwjyxqx);
    }

    /**
     * 附件名称
     */
    public String getFjmc() {
        return super.get("fjmc");
    }

    public void setFjmc(String fjmc) {
        super.set("fjmc", fjmc);
    }

    /**
     * 附件类型
     */
    public String getFjlx() {
        return super.get("fjlx");
    }

    public void setFjlx(String fjlx) {
        super.set("fjlx", fjlx);
    }

    /**
     * 附件URL
     */
    public String getFjurl() {
        return super.get("fjurl");
    }

    public void setFjurl(String fjurl) {
        super.set("fjurl", fjurl);
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
