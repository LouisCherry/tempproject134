package com.epoint.jnzwfw.projectdocandcert.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 办件通知书及电子证照调用实体
 * 
 * @作者  shibin
 * @version [版本号, 2019-07-23 14:21:12]
 */
@Entity(table = "projectdocandcert", id = "rowguid")
public class Projectdocandcert extends BaseEntity implements Cloneable
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
    * 办件标识
    */
    public String getProjectguid() {
        return super.get("projectguid");
    }

    public void setProjectguid(String projectguid) {
        super.set("projectguid", projectguid);
    }

    /**
     * 事项标识
     */
    public String getTaskguid() {
        return super.get("taskguid");
    }

    public void setTaskguid(String taskguid) {
        super.set("taskguid", taskguid);
    }

    /**
    * 部门名称
    */
    public String getOuname() {
        return super.get("ouname");
    }

    public void setOuname(String ouname) {
        super.set("ouname", ouname);
    }

    /**
    * 部门标识
    */
    public String getOuguid() {
        return super.get("ouguid");
    }

    public void setOuguid(String ouguid) {
        super.set("ouguid", ouguid);
    }

    /**
    * 窗口名称
    */
    public String getWindowname() {
        return super.get("windowname");
    }

    public void setWindowname(String windowname) {
        super.set("windowname", windowname);
    }

    /**
    * 窗口标识
    */
    public String getWindowguid() {
        return super.get("windowguid");
    }

    public void setWindowguid(String windowguid) {
        super.set("windowguid", windowguid);
    }

    /**
    * 材料补正通知书
    */
    public Integer getClbzdoc() {
        return super.getInt("clbzdoc");
    }

    public void setClbzdoc(Integer clbzdoc) {
        super.set("clbzdoc", clbzdoc);
    }

    /**
    * 受理通知书
    */
    public Integer getSldoc() {
        return super.getInt("sldoc");
    }

    public void setSldoc(Integer sldoc) {
        super.set("sldoc", sldoc);
    }

    /**
    * 不予受理决定书
    */
    public Integer getBysldoc() {
        return super.getInt("bysldoc");
    }

    public void setBysldoc(Integer bysldoc) {
        super.set("bysldoc", bysldoc);
    }

    /**
    * 不予许可决定书
    */
    public Integer getByxkdoc() {
        return super.getInt("byxkdoc");
    }

    public void setByxkdoc(Integer byxkdoc) {
        super.set("byxkdoc", byxkdoc);
    }

    /**
    * 准予许可决定书
    */
    public Integer getZyxkdoc() {
        return super.getInt("zyxkdoc");
    }

    public void setZyxkdoc(Integer zyxkdoc) {
        super.set("zyxkdoc", zyxkdoc);
    }

    /**
    * 电子身份证照
    */
    public Integer getCertcard() {
        return super.getInt("certcard");
    }

    public void setCertcard(Integer certcard) {
        super.set("certcard", certcard);
    }

    /**
    * 营业执照调用
    */
    public Integer getBusinesslicense() {
        return super.getInt("businesslicense");
    }

    public void setBusinesslicense(Integer businesslicense) {
        super.set("businesslicense", businesslicense);
    }

    /**
    * 受理时间
    */
    public Date getAcceptdate() {
        return super.getDate("acceptdate");
    }

    public void setAcceptdate(Date acceptdate) {
        super.set("acceptdate", acceptdate);
    }

    /**
    * 办结时间
    */
    public Date getBanjiedate() {
        return super.getDate("banjiedate");
    }

    public void setBanjiedate(Date banjiedate) {
        super.set("banjiedate", banjiedate);
    }

}
