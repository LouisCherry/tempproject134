package com.epoint.expert.expertcompanyavoid.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 专家回避单位表实体
 * 
 * @作者  cqsong
 * @version [版本号, 2019-08-21 16:37:07]
 */
@Entity(table = "Expert_CompanyAvoid", id = "rowguid")
public class ExpertCompanyavoid extends BaseEntity implements Cloneable
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
    * 专家
    */
    public String getExpertguid() {
        return super.get("expertguid");
    }

    public void setExpertguid(String expertguid) {
        super.set("expertguid", expertguid);
    }

    /**
    * 回避单位唯一标识
    */
    public String getCompanyguid() {
        return super.get("companyguid");
    }

    public void setCompanyguid(String companyguid) {
        super.set("companyguid", companyguid);
    }

    /**
    * 回避单位名称
    */
    public String getCompanyname() {
        return super.get("companyname");
    }

    public void setCompanyname(String companyname) {
        super.set("companyname", companyname);
    }

    /**
    * 回避说明
    */
    public String getAvoidreason() {
        return super.get("avoidreason");
    }

    public void setAvoidreason(String avoidreason) {
        super.set("avoidreason", avoidreason);
    }

}
