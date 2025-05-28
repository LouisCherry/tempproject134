package com.epoint.jnzwfw.sdwaithandle.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 同城通办信息表实体
 * 
 * @作者  17614
 * @version [版本号, 2019-05-20 17:30:46]
 */
@Entity(table = "Audit_Project_SameCity", id = "rowguid")
public class AuditProjectSamecity extends BaseEntity implements Cloneable
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
    * 代办区域
    */
    public String getDaibanarea() {
        return super.get("daibanarea");
    }

    public void setDaibanarea(String daibanarea) {
        super.set("daibanarea", daibanarea);
    }

    /**
    * 代办中心标识
    */
    public String getDaibancenterguid() {
        return super.get("daibancenterguid");
    }

    public void setDaibancenterguid(String daibancenterguid) {
        super.set("daibancenterguid", daibancenterguid);
    }

    /**
    * 代办中心名称
    */
    public String getDaibancentername() {
        return super.get("daibancentername");
    }

    public void setDaibancentername(String daibancentername) {
        super.set("daibancentername", daibancentername);
    }

    /**
    * 代办人所属部门GUID
    */
    public String getDaibandeptguid() {
        return super.get("daibandeptguid");
    }

    public void setDaibandeptguid(String daibandeptguid) {
        super.set("daibandeptguid", daibandeptguid);
    }

    /**
    * 代办人所属部门
    */
    public String getDaibandeptname() {
        return super.get("daibandeptname");
    }

    public void setDaibandeptname(String daibandeptname) {
        super.set("daibandeptname", daibandeptname);
    }

    /**
    * 代办人标识
    */
    public String getDaibanrenguid() {
        return super.get("daibanrenguid");
    }

    public void setDaibanrenguid(String daibanrenguid) {
        super.set("daibanrenguid", daibanrenguid);
    }

    /**
    * 代办人姓名
    */
    public String getDaibanrenname() {
        return super.get("daibanrenname");
    }

    public void setDaibanrenname(String daibanrenname) {
        super.set("daibanrenname", daibanrenname);
    }

    /**
    * 代办人联系电话
    */
    public String getDanbanrentel() {
        return super.get("danbanrentel");
    }

    public void setDanbanrentel(String danbanrentel) {
        super.set("danbanrentel", danbanrentel);
    }
    
    /**
    * 代办人联系电话
    */
    public String getDanbanrenaddress() {
        return super.get("danbanrenaddress");
    }

    public void setDanbanrenaddress(String danbanrenaddress) {
        super.set("danbanrenaddress", danbanrenaddress);
    }

    /**
    * 属地区域
    */
    public String getShudiarea() {
        return super.get("shudiarea");
    }

    public void setShudiarea(String shudiarea) {
        super.set("shudiarea", shudiarea);
    }

    /**
    * 属地中心标识
    */
    public String getShudicenterguid() {
        return super.get("shudicenterguid");
    }

    public void setShudicenterguid(String shudicenterguid) {
        super.set("shudicenterguid", shudicenterguid);
    }

    /**
    * 属地中心名称
    */
    public String getShudicentername() {
        return super.get("shudicentername");
    }

    public void setShudicentername(String shudicentername) {
        super.set("shudicentername", shudicentername);
    }

    /**
    * 属地所属部门GUID
    */
    public String getShudideptguid() {
        return super.get("shudideptguid");
    }

    public void setShudideptguid(String shudideptguid) {
        super.set("shudideptguid", shudideptguid);
    }

    /**
    * 属地所属部门
    */
    public String getShudideptname() {
        return super.get("shudideptname");
    }

    public void setShudideptname(String shudideptname) {
        super.set("shudideptname", shudideptname);
    }

    /**
    * 属地办理人标识
    */
    public String getShudirenguid() {
        return super.get("shudirenguid");
    }

    public void setShudirenguid(String shudirenguid) {
        super.set("shudirenguid", shudirenguid);
    }

    /**
    * 属地办理人
    */
    public String getShudirenname() {
        return super.get("shudirenname");
    }

    public void setShudirenname(String shudirenname) {
        super.set("shudirenname", shudirenname);
    }

    /**
    * 属地办理人电话
    */
    public String getShudirentel() {
        return super.get("shudirentel");
    }

    public void setShudirentel(String shudirentel) {
        super.set("shudirentel", shudirentel);
    }
    
    /**
    * 属地办理人电话
    */
    public String getShudirenaddress() {
        return super.get("shudirenaddress");
    }

    public void setShudirenaddress(String shudirenaddress) {
        super.set("shudirenaddress", shudirenaddress);
    }

    /**
    * 办件Guid
    */
    public String getProjectguid() {
        return super.get("projectguid");
    }

    public void setProjectguid(String projectguid) {
        super.set("projectguid", projectguid);
    }

    /**
    * 是否寄送材料
    */
    public String getIs_sendmaterial() {
        return super.get("is_sendmaterial");
    }

    public void setIs_sendmaterial(String is_sendmaterial) {
        super.set("is_sendmaterial", is_sendmaterial);
    }

    /**
    * 是否送达
    */
    public String getIs_send() {
        return super.get("is_send");
    }

    public void setIs_send(String is_send) {
        super.set("is_send", is_send);
    }

    /**
    * 办件编号
    */
    public String getFlowsn() {
        return super.get("flowsn");
    }

    public void setFlowsn(String flowsn) {
        super.set("flowsn", flowsn);
    }

    /**
    * 申请人
    */
    public String getApplyname() {
        return super.get("applyname");
    }

    public void setApplyname(String applyname) {
        super.set("applyname", applyname);
    }

    /**
    * 申请时间
    */
    public Date getApplydate() {
        return super.getDate("applydate");
    }

    public void setApplydate(Date applydate) {
        super.set("applydate", applydate);
    }

    /**
    * 代办状态
    */
    public String getStatus() {
        return super.get("status");
    }

    public void setStatus(String status) {
        super.set("status", status);
    }

    /**
    * 办件名称
    */
    public String getProjectname() {
        return super.get("projectname");
    }

    public void setProjectname(String projectname) {
        super.set("projectname", projectname);
    }

    /**
    * 通办类型
    */
    public String getSametype() {
        return super.get("sametype");
    }

    public void setSametype(String sametype) {
        super.set("sametype", sametype);
    }

    /**
    * 代办数据源名称
    */
    public String getDaibandatasource() {
        return super.get("daibandatasource");
    }

    public void setDaibandatasource(String daibandatasource) {
        super.set("daibandatasource", daibandatasource);
    }

    /**
    * 事项主键
    */
    public String getTaskguid() {
        return super.get("taskguid");
    }

    public void setTaskguid(String taskguid) {
        super.set("taskguid", taskguid);
    }

    /**
    * 事项版本标识
    */
    public String getTaskid() {
        return super.get("taskid");
    }

    public void setTaskid(String taskid) {
        super.set("taskid", taskid);
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

}
