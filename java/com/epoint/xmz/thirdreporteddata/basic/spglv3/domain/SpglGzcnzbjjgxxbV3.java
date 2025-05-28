package com.epoint.xmz.thirdreporteddata.basic.spglv3.domain;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 告知承诺制办件监管信息表实体
 *
 * @version [版本号, 2023-09-25 15:31:35]
 * @作者 Epoint
 */
@Entity(table = "SPGL_GZCNZBJJGXXB_V3", id = "rowguid")
public class SpglGzcnzbjjgxxbV3 extends BaseEntity implements Cloneable {
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
    public String getSpsxbm() {
        return super.get("spsxbm");
    }

    public void setSpsxbm(String spsxbm) {
        super.set("spsxbm", spsxbm);
    }

    /**
     * 检查编号
     */
    public String getJcbh() {
        return super.get("jcbh");
    }

    public void setJcbh(String jcbh) {
        super.set("jcbh", jcbh);
    }

    /**
     * 检查时间
     */
    public Date getJcsj() {
        return super.getDate("jcsj");
    }

    public void setJcsj(Date jcsj) {
        super.set("jcsj", jcsj);
    }

    /**
     * 检查部门名称
     */
    public String getJcbmmc() {
        return super.get("jcbmmc");
    }

    public void setJcbmmc(String jcbmmc) {
        super.set("jcbmmc", jcbmmc);
    }

    /**
     * 检查部门统一信用社会代码
     */
    public String getJcbmtyshxydm() {
        return super.get("jcbmtyshxydm");
    }

    public void setJcbmtyshxydm(String jcbmtyshxydm) {
        super.set("jcbmtyshxydm", jcbmtyshxydm);
    }

    /**
     * 检查依据或参考说明
     */
    public String getJcyjhsm() {
        return super.get("jcyjhsm");
    }

    public void setJcyjhsm(String jcyjhsm) {
        super.set("jcyjhsm", jcyjhsm);
    }

    /**
     * 是否履行告知承诺
     */
    public Integer getSflxgzcn() {
        return super.getInt("sflxgzcn");
    }

    public void setSflxgzcn(Integer sflxgzcn) {
        super.set("sflxgzcn", sflxgzcn);
    }

    /**
     * 告知承诺履行情况
     */
    public String getGzcnlxqk() {
        return super.get("gzcnlxqk");
    }

    public void setGzcnlxqk(String gzcnlxqk) {
        super.set("gzcnlxqk", gzcnlxqk);
    }

    /**
     * 其他
     */
    public String getQt() {
        return super.get("qt");
    }

    public void setQt(String qt) {
        super.set("qt", qt);
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
