package com.epoint.xmz.auditprojectdelivery.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 快递表实体
 *
 * @version [版本号, 2024-01-15 20:11:12]
 * @作者 Administrator
 */
@Entity(table = "audit_project_delivery", id = "rowguid")
public class AuditProjectDelivery extends BaseEntity implements Cloneable {
    private static final long serialVersionUID = 1L;

    /**
     * 收件人地址（回寄）
     */
    public String getBackaddress() {
        return super.get("backaddress");
    }

    public void setBackaddress(String backaddress) {
        super.set("backaddress", backaddress);
    }

    /**
     * 详细地址（回寄）
     */
    public String getBackdetailaddress() {
        return super.get("backdetailaddress");
    }

    public void setBackdetailaddress(String backdetailaddress) {
        super.set("backdetailaddress", backdetailaddress);
    }

    /**
     * 收件人电话（回寄）
     */
    public String getBackmobile() {
        return super.get("backmobile");
    }

    public void setBackmobile(String backmobile) {
        super.set("backmobile", backmobile);
    }

    /**
     * 收件人姓名（回寄）
     */
    public String getBackname() {
        return super.get("backname");
    }

    public void setBackname(String backname) {
        super.set("backname", backname);
    }

    /**
     * 流水号
     */
    public String getFlowsn() {
        return super.get("flowsn");
    }

    public void setFlowsn(String flowsn) {
        super.set("flowsn", flowsn);
    }

    /**
     * 是否邮寄材料
     */
    public String getIssend() {
        return super.get("issend");
    }

    public void setIssend(String issend) {
        super.set("issend", issend);
    }

    /**
     * 是否回寄材料
     */
    public String getIssendback() {
        return super.get("issendback");
    }

    public void setIssendback(String issendback) {
        super.set("issendback", issendback);
    }

    /**
     * 选择邮寄的材料清单
     */
    public String getMaterialbill() {
        return super.get("materialbill");
    }

    public void setMaterialbill(String materialbill) {
        super.set("materialbill", materialbill);
    }

    /**
     * 选择邮寄的材料清单guid
     */
    public String getMaterialguidbill() {
        return super.get("materialguidbill");
    }

    public void setMaterialguidbill(String materialguidbill) {
        super.set("materialguidbill", materialguidbill);
    }

    /**
     * 办件关联字段
     */
    public String getProjectguid() {
        return super.get("projectguid");
    }

    public void setProjectguid(String projectguid) {
        super.set("projectguid", projectguid);
    }

    /**
     * 收件地址
     */
    public String getRecipientaddress() {
        return super.get("recipientaddress");
    }

    public void setRecipientaddress(String recipientaddress) {
        super.set("recipientaddress", recipientaddress);
    }

    /**
     * 收件人电话
     */
    public String getRecipientmobile() {
        return super.get("recipientmobile");
    }

    public void setRecipientmobile(String recipientmobile) {
        super.set("recipientmobile", recipientmobile);
    }

    /**
     * 收件人姓名
     */
    public String getRecipientname() {
        return super.get("recipientname");
    }

    public void setRecipientname(String recipientname) {
        super.set("recipientname", recipientname);
    }

    /**
     * 寄件人地址
     */
    public String getSendaddress() {
        return super.get("sendaddress");
    }

    public void setSendaddress(String sendaddress) {
        super.set("sendaddress", sendaddress);
    }

    /**
     * 寄件人详细地址
     */
    public String getSenddetailaddress() {
        return super.get("senddetailaddress");
    }

    public void setSenddetailaddress(String senddetailaddress) {
        super.set("senddetailaddress", senddetailaddress);
    }

    /**
     * 寄件人电话
     */
    public String getSendmobile() {
        return super.get("sendmobile");
    }

    public void setSendmobile(String sendmobile) {
        super.set("sendmobile", sendmobile);
    }

    /**
     * 寄件人姓名
     */
    public String getSendname() {
        return super.get("sendname");
    }

    public void setSendname(String sendname) {
        super.set("sendname", sendname);
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
     * 年份标识
     */
    public String getYearflag() {
        return super.get("yearflag");
    }

    public void setYearflag(String yearflag) {
        super.set("yearflag", yearflag);
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