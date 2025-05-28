package com.epoint.yyyz.businesslicense.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 一业一证基本信息表实体
 * @description
 * @author shibin
 * @date  2020年5月19日 上午10:56:28
 */
@Entity(table = "businesslicense_baseinfo", id = "rowguid")
public class BusinessLicenseBaseinfo extends BaseEntity implements Cloneable
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
    * 行业名称
    */
    public String getIndustryName() {
        return super.get("industryName");
    }

    public void setIndustryName(String industryName) {
        super.set("industryName", industryName);
    }

    /**
     * 行业编码
     */
    public String getIndustryCode() {
        return super.get("industryCode");
    }

    public void setIndustryCode(String industryCode) {
        super.set("industryCode", industryCode);
    }

    /**
     * 区划编码
     */
    public String getRegionCode() {
        return super.get("regionCode");
    }

    public void setRegionCode(String regionCode) {
        super.set("regionCode", regionCode);
    }

    /**
     * 网上申请号
     */
    public String getApplyNo() {
        return super.get("applyNo");
    }

    public void setApplyNo(String applyNo) {
        super.set("applyNo", applyNo);
    }

    /**
     * 业务流水号
     */
    public String getSerialNo() {
        return super.get("serialNo");
    }

    public void setSerialNo(String serialNo) {
        super.set("serialNo", serialNo);
    }

    /**
     * 服务对象
     */
    public String getServiceObj() {
        return super.get("serviceObj");
    }

    public void setServiceObj(String serviceObj) {
        super.set("serviceObj", serviceObj);
    }

    /**
     * 申请人
     */
    public String getApplyername() {
        return super.get("Applyername");
    }

    public void setApplyername(String Applyername) {
        super.set("Applyername", Applyername);
    }

    /**
     * 申请时间
     */
    public Date getApplydate() {
        return super.getDate("Applydate");
    }

    public void setApplydate(Date Applydate) {
        super.set("Applydate", Applydate);
    }

    /**
     * 主题唯一值
     */
    public String getBusinessGuid() {
        return super.get("businessGuid");
    }

    public void setBusinessGuid(String businessGuid) {
        super.set("businessGuid", businessGuid);
    }

    /**
     * 主题实例唯一值
     */
    public String getBiGuid() {
        return super.get("biGuid");
    }

    public void setBiGuid(String biGuid) {
        super.set("biGuid", biGuid);
    }

}
