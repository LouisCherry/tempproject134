package com.epoint.xmz.certczqcgxdj.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 出租汽车更新登记计划库实体
 * 
 * @作者 dyxin
 * @version [版本号, 2023-05-22 13:17:25]
 */
@Entity(table = "cert_czqcgxdj", id = "rowguid")
public class CertCzqcgxdj extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

    /**
     * 公司
     */
    public String getCompany() {
        return super.get("company");
    }

    public void setCompany(String company) {
        super.set("company", company);
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
     * 燃料类型
     */
    public String getFueltype() {
        return super.get("fueltype");
    }

    public void setFueltype(String fueltype) {
        super.set("fueltype", fueltype);
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
     * 车牌号
     */
    public String getCarid() {
        return super.get("carid");
    }

    public void setCarid(String carid) {
        super.set("carid", carid);
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
     * 序号
     */
    public Integer getIndexdesc() {
        return super.getInt("indexdesc");
    }

    public void setIndexdesc(Integer indexdesc) {
        super.set("indexdesc", indexdesc);
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
     * 操作者名字
     */
    public String getOperateusername() {
        return super.get("operateusername");
    }

    public void setOperateusername(String operateusername) {
        super.set("operateusername", operateusername);
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

}
