package com.epoint.knowledge.common.domain;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 知识库审核步骤实体
 * 
 * @作者  xuyunhai
 * @version [版本号, 2017-02-15 14:44:55]
 */
@Entity(table = "CNS_KINFO_STEP", id = "rowguid")
public class CnsKinfoStep extends BaseEntity implements Cloneable
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
    * 知识库知识guid
    */
    public String getKguid() {
        return super.get("kguid");
    }

    public void setKguid(String kguid) {
        super.set("kguid", kguid);
    }

    /**
    * 创建时间
    */
    public Date getCreatetime() {
        return super.getDate("createtime");
    }

    public void setCreatetime(Date createtime) {
        super.set("createtime", createtime);
    }

    /**
    * 处理完成时间
    */
    public Date getHandletime() {
        return super.getDate("handletime");
    }

    public void setHandletime(Date handletime) {
        super.set("handletime", handletime);
    }

    /**
    * 处理人
    */
    public String getHandlername() {
        return super.get("handlername");
    }

    public void setHandlername(String handlername) {
        super.set("handlername", handlername);
    }

    /**
    * 处理人所在部门名称
    */
    public String getHandlerouname() {
        return super.get("handlerouname");
    }

    public void setHandlerouname(String handlerouname) {
        super.set("handlerouname", handlerouname);
    }

    /**
    * 操作人处理人guid
    */
    public String getHandlerguid() {
        return super.get("handlerguid");
    }

    public void setHandlerguid(String handlerguid) {
        super.set("handlerguid", handlerguid);
    }

    /**
    * 处理人所在部门名称
    */
    public String getHandlerouguid() {
        return super.get("handlerouguid");
    }

    public void setHandlerouguid(String handlerouguid) {
        super.set("handlerouguid", handlerouguid);
    }

    /**
    * 处理意见
    */
    public String getHandleopinion() {
        return super.get("handleopinion");
    }

    public void setHandleopinion(String handleopinion) {
        super.set("handleopinion", handleopinion);
    }

    /**
    * 发送时间
    */
    public Date getSendtime() {
        return super.getDate("sendtime");
    }

    public void setSendtime(Date sendtime) {
        super.set("sendtime", sendtime);
    }

    /**
    * 发送人姓名
    */
    public String getSendername() {
        return super.get("sendername");
    }

    public void setSendername(String sendername) {
        super.set("sendername", sendername);
    }

    /**
    * 活动节点类型
    */
    public String getActivitytype() {
        return super.get("activitytype");
    }

    public void setActivitytype(String activitytype) {
        super.set("activitytype", activitytype);
    }

    /**
    * 操作状态
    */
    public String getOpeartionstatus() {
        return super.get("opeartionstatus");
    }

    public void setOpeartionstatus(String opeartionstatus) {
        super.set("opeartionstatus", opeartionstatus);
    }

    /**
    * 管理附件
    */
    public String getCliengguid() {
        return super.get("cliengguid");
    }

    public void setCliengguid(String cliengguid) {
        super.set("cliengguid", cliengguid);
    }

}
