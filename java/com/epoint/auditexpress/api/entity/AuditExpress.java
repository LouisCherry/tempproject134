package com.epoint.auditexpress.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 快递信息表实体
 * 
 * @作者  Administrator
 * @version [版本号, 2018-08-03 09:12:22]
 */
@Entity(table = "Audit_Express", id = "rowguid")
public class AuditExpress extends BaseEntity implements Cloneable
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
    public Integer getRowID() {
        return super.getInt("row_id");
    }

    public void setRowID(Integer rowID) {
        super.set("row_id", rowID);
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
    * 内件品名
    */
    public String getInternalsname() {
        return super.get("internalsname");
    }

    public void setInternalsname(String internalsname) {
        super.set("internalsname", internalsname);
    }

    /**
    * 寄件人类型
    */
    public String getSendertype() {
        return super.get("sendertype");
    }

    public void setSendertype(String sendertype) {
        super.set("sendertype", sendertype);
    }

    /**
    * 寄件人姓名
    */
    public String getSendername() {
        return super.get("sendername");
    }

    public void setSendername(String sendername) {
        super.set("sendername", sendername);
    }

    /**
    * 寄件人联系方式
    */
    public String getSenderphone() {
        return super.get("senderphone");
    }

    public void setSenderphone(String senderphone) {
        super.set("senderphone", senderphone);
    }

    /**
    * 寄件人地址
    */
    public String getSenderaddress() {
        return super.get("senderaddress");
    }

    public void setSenderaddress(String senderaddress) {
        super.set("senderaddress", senderaddress);
    }

    /**
    * 寄件人邮编
    */
    public String getSenderpostcode() {
        return super.get("senderpostcode");
    }

    public void setSenderpostcode(String senderpostcode) {
        super.set("senderpostcode", senderpostcode);
    }

    /**
    * 寄件人单位
    */
    public String getSenderunit() {
        return super.get("senderunit");
    }

    public void setSenderunit(String senderunit) {
        super.set("senderunit", senderunit);
    }

    /**
    * 收件人姓名
    */
    public String getConsigneename() {
        return super.get("consigneename");
    }

    public void setConsigneename(String consigneename) {
        super.set("consigneename", consigneename);
    }

    /**
    * 收件人联系电话
    */
    public String getConsigneephone() {
        return super.get("consigneephone");
    }

    public void setConsigneephone(String consigneephone) {
        super.set("consigneephone", consigneephone);
    }

    /**
    * 收件人邮编
    */
    public String getConsigneepostcode() {
        return super.get("consigneepostcode");
    }

    public void setConsigneepostcode(String consigneepostcode) {
        super.set("consigneepostcode", consigneepostcode);
    }

    /**
    * 收件人地址
    */
    public String getConsigneeaddress() {
        return super.get("consigneeaddress");
    }

    public void setConsigneeaddress(String consigneeaddress) {
        super.set("consigneeaddress", consigneeaddress);
    }

    /**
    * 收件人公司
    */
    public String getConsigneeunit() {
        return super.get("consigneeunit");
    }

    public void setConsigneeunit(String consigneeunit) {
        super.set("consigneeunit", consigneeunit);
    }

    /**
    * 邮寄部门名称
    */
    public String getOuname() {
        return super.get("ouname");
    }

    public void setOuname(String ouname) {
        super.set("ouname", ouname);
    }

    /**
    * 邮寄部门编号
    */
    public String getOucode() {
        return super.get("oucode");
    }

    public void setOucode(String oucode) {
        super.set("oucode", oucode);
    }

    /**
    * 收费金额
    */
    public Double getPrice() {
        return super.getDouble("price");
    }

    public void setPrice(Double price) {
        super.set("price", price);
    }

    /**
    * 付款方式
    */
    public String getPayway() {
        return super.get("payway");
    }

    public void setPayway(String payway) {
        super.set("payway", payway);
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
    * 事项名称
    */
    public String getTaskname() {
        return super.get("taskname");
    }

    public void setTaskname(String taskname) {
        super.set("taskname", taskname);
    }

    /**
    * 寄送范围
    */
    public String getSendarea() {
        return super.get("sendarea");
    }

    public void setSendarea(String sendarea) {
        super.set("sendarea", sendarea);
    }

    /**
    * 办件唯一标识
    */
    public String getProjectguid() {
        return super.get("projectguid");
    }

    public void setProjectguid(String projectguid) {
        super.set("projectguid", projectguid);
    }

    /**
    * 快递单号
    */
    public String getTrackingnumber() {
        return super.get("trackingnumber");
    }

    public void setTrackingnumber(String trackingnumber) {
        super.set("trackingnumber", trackingnumber);
    }

    /**
    * 窗口人员
    */
    public String getWindowperson() {
        return super.get("windowperson");
    }

    public void setWindowperson(String windowperson) {
        super.set("windowperson", windowperson);
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
    * 上门取件返回状态
    */
    public String getQjstatus() {
        return super.get("qjstatus");
    }

    public void setQjstatus(String qjstatus) {
        super.set("qjstatus", qjstatus);
    }

    /**
    * 上门取件状态反馈错误描述
    */
    public String getQjremark() {
        return super.get("qjremark");
    }

    public void setQjremark(String qjremark) {
        super.set("qjremark", qjremark);
    }

    /**
    * 快递状态
    */
    public String getStatus() {
        return super.get("status");
    }

    public void setStatus(String status) {
        super.set("status", status);
    }

    /**
    * 推送标记
    */
    public String getRemark() {
        return super.get("remark");
    }

    public void setRemark(String remark) {
        super.set("remark", remark);
    }

    /**
    * 手机填写标识
    */
    public String getPhonetag() {
        return super.get("phonetag");
    }

    public void setPhonetag(String phonetag) {
        super.set("phonetag", phonetag);
    }

    /**
    * 是否补录
    */
    public String getIsAddition() {
        return super.get("is_addition");
    }

    public void setIsAddition(String isAddition) {
        super.set("is_addition", isAddition);
    }

    /**
    * 预约时间
    */
    public Date getOrdertime() {
        return super.getDate("ordertime");
    }

    public void setOrdertime(Date ordertime) {
        super.set("ordertime", ordertime);
    }

    /**
    * 预约起始时间
    */
    public Date getStarttime() {
        return super.getDate("starttime");
    }

    public void setStarttime(Date starttime) {
        super.set("starttime", starttime);
    }

    /**
    * 预约结束时间
    */
    public Date getEndtime() {
        return super.getDate("endtime");
    }

    public void setEndtime(Date endtime) {
        super.set("endtime", endtime);
    }

    /**
    * 待处理时间
    */
    public Date getWaithandledate() {
        return super.getDate("waithandledate");
    }

    public void setWaithandledate(Date waithandledate) {
        super.set("waithandledate", waithandledate);
    }

    /**
    * 已处理时间
    */
    public Date getAlreadyhandledate() {
        return super.getDate("alreadyhandledate");
    }

    public void setAlreadyhandledate(Date alreadyhandledate) {
        super.set("alreadyhandledate", alreadyhandledate);
    }

    /**
     * 打印状态
     */
    public String getDystatus() {
        return super.get("dystatus");
    }

    public void setDystatus(String dystatus) {
        super.set("dystatus", dystatus);
    }

    /**
     * 网厅快递状态
     */
    public String getZwdtExpressStatus() {
        return super.get("zwdtExpressStatus");
    }

    public void setZwdtExpressStatus(String zwdtExpressStatus) {
        super.set("zwdtExpressStatus", zwdtExpressStatus);
    }

    /**
     * 收件人省
     */
    public String getProvince() {
        return super.get("province");
    }

    public void setProvince(String province) {
        super.set("province", province);
    }

    /**
    * 收件人市
    */
    public String getCity() {
        return super.get("city");
    }

    public void setCity(String city) {
        super.set("city", city);
    }

    /**
    * 收件人区
    */
    public String getRegion() {
        return super.get("region");
    }

    public void setRegion(String region) {
        super.set("region", region);
    }

    /**
     * EMS业务来源
     */
    public String getEMSSources() {
        return super.get("EMSSources");
    }

    public void setEMSSources(String EMSSources) {
        super.set("EMSSources", EMSSources);
    }
}
