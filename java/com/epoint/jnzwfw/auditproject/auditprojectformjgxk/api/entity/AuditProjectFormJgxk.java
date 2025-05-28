package com.epoint.jnzwfw.auditproject.auditprojectformjgxk.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 竣工信息表实体
 * 
 * @作者  86180
 * @version [版本号, 2019-07-08 15:07:59]
 */
@Entity(table = "audit_project_form_jgxk", id = "rowguid")
public class AuditProjectFormJgxk extends BaseEntity implements Cloneable
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
    * 项目编号
    */
    public String getProjectguid() {
        return super.get("projectguid");
    }

    public void setProjectguid(String projectguid) {
        super.set("projectguid", projectguid);
    }

    /**
    * 表单编号
    */
    public String getForm_id() {
        return super.get("form_id");
    }

    public void setForm_id(String form_id) {
        super.set("form_id", form_id);
    }

    /**
    * 流程编号
    */
    public String getFlowsn() {
        return super.get("flowsn");
    }

    public void setFlowsn(String flowsn) {
        super.set("flowsn", flowsn);
    }

    /**
    * 开始日期
    */
    public Date getBdate() {
        return super.getDate("bdate");
    }

    public void setBdate(Date bdate) {
        super.set("bdate", bdate);
    }

    /**
    * 结束日期
    */
    public Date getEdate() {
        return super.getDate("edate");
    }

    public void setEdate(Date edate) {
        super.set("edate", edate);
    }

    /**
    * 建设工程编号
    */
    public String getBuilderlicencenum() {
        return super.get("builderlicencenum");
    }

    public void setBuilderlicencenum(String builderlicencenum) {
        super.set("builderlicencenum", builderlicencenum);
    }

    /**
    * 工程区域
    */
    public String getFactarea() {
        return super.get("factarea");
    }

    public void setFactarea(String factarea) {
        super.set("factarea", factarea);
    }

    /**
    * 项目花费
    */
    public String getFactcost() {
        return super.get("factcost");
    }

    public void setFactcost(String factcost) {
        super.set("factcost", factcost);
    }

    /**
    * 项目规模
    */
    public String getFactsize() {
        return super.get("factsize");
    }

    public void setFactsize(String factsize) {
        super.set("factsize", factsize);
    }

    /**
    * 长度
    */
    public String getFactlength() {
        return super.get("factlength");
    }

    public void setFactlength(String factlength) {
        super.set("factlength", factlength);
    }

    /**
    * 跨度
    */
    public String getSpan() {
        return super.get("span");
    }

    public void setSpan(String span) {
        super.set("span", span);
    }

}
