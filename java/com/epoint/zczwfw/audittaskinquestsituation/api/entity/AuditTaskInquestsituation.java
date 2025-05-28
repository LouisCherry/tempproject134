package com.epoint.zczwfw.audittaskinquestsituation.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 勘验事项情形表实体
 * 
 * @author yrchan
 * @version [版本号, 2022-04-18 09:53:53]
 */
@Entity(table = "audit_task_inquestsituation", id = "rowguid")
public class AuditTaskInquestsituation extends BaseEntity implements Cloneable
{

    private static final long serialVersionUID = 2439069498902609964L;

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
     * 事项标识
     */
    public String getTaskguid() {
        return super.get("taskguid");
    }

    public void setTaskguid(String taskguid) {
        super.set("taskguid", taskguid);
    }

    /**
     * 勘验情形名称
     */
    public String getSituationname() {
        return super.get("situationname");
    }

    public void setSituationname(String situationname) {
        super.set("situationname", situationname);
    }

    /**
     * 勘验情形地址
     */
    public String getSituationurl() {
        return super.get("situationurl");
    }

    public void setSituationurl(String situationurl) {
        super.set("situationurl", situationurl);
    }

    /**
     * 创建时间
     */
    public Date getCreatedate() {
        return super.getDate("createdate");
    }

    public void setCreatedate(Date createdate) {
        super.set("createdate", createdate);
    }

}
