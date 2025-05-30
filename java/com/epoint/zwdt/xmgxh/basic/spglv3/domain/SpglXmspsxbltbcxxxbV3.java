package com.epoint.zwdt.xmgxh.basic.spglv3.domain;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 项目审批事项办理特别程序信息表实体
 * 
 * @作者  Administrator
 * @version [版本号, 2019-07-02 16:09:33]
 */
@Entity(table = "SPGL_XMSPSXBLTBCXXXB_V3", id = "rowguid")
public class SpglXmspsxbltbcxxxbV3 extends BaseEntity implements Cloneable
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
    * 审批事项实例编码
    */
    public String getSpsxslbm() {
        return super.get("spsxslbm");
    }

    public void setSpsxslbm(String spsxslbm) {
        super.set("spsxslbm", spsxslbm);
    }

    /**
    * 特别程序
    */
    public Integer getTbcx() {
        return super.getInt("tbcx");
    }

    public void setTbcx(Integer tbcx) {
        super.set("tbcx", tbcx);
    }

    /**
    * 特别程序名称
    */
    public String getTbcxmc() {
        return super.get("tbcxmc");
    }

    public void setTbcxmc(String tbcxmc) {
        super.set("tbcxmc", tbcxmc);
    }

    /**
    * 特别程序开始时间
    */
    public Date getTbcxkssj() {
        return super.getDate("tbcxkssj");
    }

    public void setTbcxkssj(Date tbcxkssj) {
        super.set("tbcxkssj", tbcxkssj);
    }

    /**
    * 特别程序时限类型
    */
    public Integer getTbcxsxlx() {
        return super.getInt("tbcxsxlx");
    }

    public void setTbcxsxlx(Integer tbcxsxlx) {
        super.set("tbcxsxlx", tbcxsxlx);
    }

    /**
    * 特别程序时限
    */
    public Integer getTbcxsx() {
        return super.getInt("tbcxsx");
    }

    public void setTbcxsx(Integer tbcxsx) {
        super.set("tbcxsx", tbcxsx);
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

}
