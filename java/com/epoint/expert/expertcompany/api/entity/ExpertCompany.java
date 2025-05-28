package com.epoint.expert.expertcompany.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 从业单位表实体
 * 
 * @作者  cqsong
 * @version [版本号, 2019-08-21 16:09:09]
 */
@Entity(table = "Expert_Company", id = "rowguid")
public class ExpertCompany extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

    /**
     * 是否删除
     */
    public Integer getIs_del() {
        return super.getInt("is_del");
    }

    public void setIs_del(Integer is_del) {
        super.set("is_del", is_del);
    }

    /**
    * 单位状态
    */
    public String getStatus() {
        return super.get("status");
    }

    public void setStatus(String status) {
        super.set("status", status);
    }

    /**
    * 备注
    */
    public String getRemark() {
        return super.get("remark");
    }

    public void setRemark(String remark) {
        super.set("remark", remark);
    }

    /**
    * 附件标识
    */
    public String getCliengguid() {
        return super.get("cliengguid");
    }

    public void setCliengguid(String cliengguid) {
        super.set("cliengguid", cliengguid);
    }

    /**
    * 注册资本
    */
    public Double getZczb() {
        return super.getDouble("zczb");
    }

    public void setZczb(Double zczb) {
        super.set("zczb", zczb);
    }

    /**
    * 实收资本
    */
    public Double getSszb() {
        return super.getDouble("sszb");
    }

    public void setSszb(Double sszb) {
        super.set("sszb", sszb);
    }

    /**
    * 办公电话
    */
    public String getContactphone() {
        return super.get("contactphone");
    }

    public void setContactphone(String contactphone) {
        super.set("contactphone", contactphone);
    }

    /**
    * 法定代表人
    */
    public String getLegal() {
        return super.get("legal");
    }

    public void setLegal(String legal) {
        super.set("legal", legal);
    }

    /**
    * 经营范围
    */
    public String getBusinessscope() {
        return super.get("businessscope");
    }

    public void setBusinessscope(String businessscope) {
        super.set("businessscope", businessscope);
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
    * 详细地址
    */
    public String getAddress() {
        return super.get("address");
    }

    public void setAddress(String address) {
        super.set("address", address);
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
    * 区
    */
    public String getCountry() {
        return super.get("country");
    }

    public void setCountry(String country) {
        super.set("country", country);
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
    * 市
    */
    public String getCity() {
        return super.get("city");
    }

    public void setCity(String city) {
        super.set("city", city);
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
    * 省
    */
    public String getProvince() {
        return super.get("province");
    }

    public void setProvince(String province) {
        super.set("province", province);
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
    * 单位类型
    */
    public String getType() {
        return super.get("type");
    }

    public void setType(String type) {
        super.set("type", type);
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
    * 单位名称
    */
    public String getCompanyname() {
        return super.get("companyname");
    }

    public void setCompanyname(String companyname) {
        super.set("companyname", companyname);
    }

}
