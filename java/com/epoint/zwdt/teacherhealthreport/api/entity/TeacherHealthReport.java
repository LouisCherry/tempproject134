package com.epoint.zwdt.teacherhealthreport.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 教师资格体检报告实体
 * 
 * @author liuhui
 * @version 2022年5月13日
 */
@Entity(table = "teacher_health_report", id = "rowguid")
public class TeacherHealthReport extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = -7182230603295132642L;

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
     * 姓名
     */
    public String getName() {
        return super.get("name");
    }

    public void setName(String name) {
        super.set("name", name);
    }

    /**
     * 身份证
     */
    public String getSfz() {
        return super.get("sfz");
    }

    public void setSfz(String sfz) {
        super.set("sfz", sfz);
    }

    /**
     * 体检单位
     */
    public String getTjdw() {
        return super.get("tjdw");
    }

    public void setTjdw(String tjdw) {
        super.set("tjdw", tjdw);
    }

    /**
     * 体检报告编号
     */
    public String getReportnumber() {
        return super.get("reportnumber");
    }

    public void setReportnumber(String reportnumber) {
        super.set("reportnumber", reportnumber);
    }

    /**
     * 体检时间
     */
    public Date getTjdate() {
        return super.getDate("tjdate");
    }

    public void setTjdate(Date tjdate) {
        super.set("tjdate", tjdate);
    }

    /**
     * 是否合格
     */
    public String getIspass() {
        return super.get("ispass");
    }

    public void setIspass(String ispass) {
        super.set("ispass", ispass);
    }

    /**
     * 医院名称
     */
    public String getHospitalname() {
        return super.get("hospitalname");
    }

    public void setHospitalname(String hospitalname) {
        super.set("hospitalname", hospitalname);
    }

    /**
     * 县区
     */
    public String getCounty() {
        return super.get("county");
    }

    public void setCounty(String county) {
        super.set("county", county);
    }

}
