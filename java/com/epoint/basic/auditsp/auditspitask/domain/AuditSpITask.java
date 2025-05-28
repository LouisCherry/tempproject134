package com.epoint.basic.auditsp.auditspitask.domain;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 主题事项实例表---实体类
 * 
 * @version [版本号, 2017年2月28日]
 */
@Entity(table = "AUDIT_SP_I_TASK", id = "rowguid")
public class AuditSpITask extends BaseEntity implements Cloneable
{

    /**
     * 
     */
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
     * 主题唯一标识
     */
    public String getBusinessguid() {
        return super.get("businessguid");
    }

    public void setBusinessguid(String businessguid) {
        super.set("businessguid", businessguid);
    }

    /**
     * 主题实例唯一标识
     */
    public String getBiguid() {
        return super.get("biguid");
    }

    public void setBiguid(String biguid) {
        super.set("biguid", biguid);
    }

    /**
     * 阶段唯一标识
     */
    public String getPhaseguid() {
        return super.get("phaseguid");
    }

    public void setPhaseguid(String phaseguid) {
        super.set("phaseguid", phaseguid);
    }

    /**
     * 事项唯一标识
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
     * 办件唯一标识
     */
    public String getProjectguid() {
        return super.get("projectguid");
    }

    public void setProjectguid(String projectguid) {
        super.set("projectguid", projectguid);
    }

    /**
     * 办件唯一标识
     */
    public String getAreacode() {
        return super.get("areacode");
    }

    public void setAreacode(String areacode) {
        super.set("areacode", areacode);
    }

    /**
     * 子申报唯一标识，每个阶段默认一下子申报
     */
    public String getSubappguid() {
        return super.get("subappguid");
    }

    public void setSubappguid(String subappguid) {
        super.set("subappguid", subappguid);
    }

    /**
     * 排序
     */
    public Integer getOrdernumber() {
        return super.getInt("ordernumber");
    }

    public void setOrdernumber(Integer ordernumber) {
        super.set("ordernumber", ordernumber);
    }

    /**
     * 征求标识
     */
    public String getReviewguid() {
        return super.get("reviewguid");
    }

    public void setReviewguid(String reviewguid) {
        super.set("reviewguid", reviewguid);
    }

    /**
     * 是否里程碑事项
     */
    public String getSflcbsx() {
        return super.get("sflcbsx");
    }

    public void setSflcbsx(String sflcbsx) {
        super.set("sflcbsx", sflcbsx);
    }

    /**
     * 当前事项的状态，0为为神阿勃，1为外网提交，2为内网退回
     */
    public String getStatus() {
        return super.get("status");
    }

    public void setStatus(String status) {
        super.set("status", status);
    }
    
    /**
     * 批次标识
     */
    public String getBatchguid() {
        return super.get("batchguid");
    }

    public void setBatchguid(String batchguid) {
        super.set("batchguid", batchguid);
    }
    
    /**
     * 收件申报时间
     */
    public Date getSjapplydate() {
        return super.getDate("sjapplydate");
    }

    public void setSjapplydate(Date sjapplydate) {
        super.set("sjapplydate", sjapplydate);
    }

    /**
     * 一件事推送办件标识,默认为0，1=已推送 0=未推送
     */
    public String getPushstatus() {
        return super.get("pushstatus");
    }

    public void setPushstatus(String pushstatus) {
        super.set("pushstatus", pushstatus);
    }

    /**
     * 推送时间
     */
    public Date getPushtime() {
        return super.getDate("pushtime");
    }

    public void setPushtime(Date pushtime) {
        super.set("pushtime", pushtime);
    }

    /**
     * 办件的状态
     */
    public String getProjectstatus() {
        return super.get("projectstatus");
    }

    public void setProjectstatus(String projectstatus) {
        super.set("projectstatus", projectstatus);
    }

    /**
     * 办件流水号
     */
    public String getFlowsn() {
        return super.get("flowsn");
    }

    public void setFlowsn(String flowsn) {
        super.set("flowsn", flowsn);
    }

    /**
     * 不通过原因
     */
    public String getNopassreason() {
        return super.get("nopassreason");
    }

    public void setNopassreason(String nopassreason) {
        super.set("nopassreason", nopassreason);
    }

    /**
     * 前置事项
     */
    public String getPre_taskid() {
        return super.get("pre_taskid");
    }

    public void setPre_taskid(String pre_taskid) {
        super.set("pre_taskid", pre_taskid);
    }
    /**
     * 是否乡镇
     */
    public String getTownship() {
        return super.get("township");
    }
    
    public void setTownship(String township) {
        super.set("township", township);
    }
    
    /**
     * 乡镇编码
     */
    public String getTownareacode() {
        return super.get("townareacode");
    }
    
    public void setTownareacode(String townareacode) {
        super.set("townareacode", townareacode);
    }
}
