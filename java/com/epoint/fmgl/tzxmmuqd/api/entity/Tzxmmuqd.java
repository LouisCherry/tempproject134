package com.epoint.fmgl.tzxmmuqd.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 投资项目目录清单实体
 * 
 * @作者  Administrator
 * @version [版本号, 2020-09-26 12:07:54]
 */
@Entity(table = "tzxmmuqd", id = "rowguid")
public class Tzxmmuqd extends BaseEntity implements Cloneable
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
    * 目录编码(行业核准目录代码)
    */
    public String getMlbm() {
        return super.get("mlbm");
    }

    public void setMlbm(String mlbm) {
        super.set("mlbm", mlbm);
    }

    /**
    * 区划编码
    */
    public String getXzqhbm() {
        return super.get("xzqhbm");
    }

    public void setXzqhbm(String xzqhbm) {
        super.set("xzqhbm", xzqhbm);
    }

    /**
    * 区划名称
    */
    public String getXzqhmc() {
        return super.get("xzqhmc");
    }

    public void setXzqhmc(String xzqhmc) {
        super.set("xzqhmc", xzqhmc);
    }

    /**
    * 目录行业分类（投资项目行业分类）
    */
    public String getMlhyfl() {
        return super.get("mlhyfl");
    }

    public void setMlhyfl(String mlhyfl) {
        super.set("mlhyfl", mlhyfl);
    }

    /**
    * 目录名称
    */
    public String getMlmc() {
        return super.get("mlmc");
    }

    public void setMlmc(String mlmc) {
        super.set("mlmc", mlmc);
    }

    /**
    * 部门编码
    */
    public String getBmbm() {
        return super.get("bmbm");
    }

    public void setBmbm(String bmbm) {
        super.set("bmbm", bmbm);
    }

    /**
    * 部门名称
    */
    public String getBmmc() {
        return super.get("bmmc");
    }

    public void setBmmc(String bmmc) {
        super.set("bmmc", bmmc);
    }

    /**
    * 目录类型（项目类型）
    */
    public String getMllx() {
        return super.get("mllx");
    }

    public void setMllx(String mllx) {
        super.set("mllx", mllx);
    }

}
