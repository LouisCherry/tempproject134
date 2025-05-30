package com.epoint.zwdt.xmgxh.basic.spglv3.domain;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 住建部_地方项目审批流程阶段事项信息表实体
 * 
 * @作者  zhpengsy
 * @version [版本号, 2018-11-16 15:10:10]
 */
@Entity(table = "SPGL_SPLCJBLSXXXB_V3", id = "rowguid")
public class Spglsplcjblsxxxb extends BaseEntity implements Cloneable
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
    * 审批阶段序号
    */
    public Integer getSpjdxh() {
        return super.getInt("spjdxh");
    }

    public void setSpjdxh(Integer spjdxh) {
        super.set("spjdxh", spjdxh);
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
