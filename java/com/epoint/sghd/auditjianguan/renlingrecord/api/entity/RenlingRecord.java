package com.epoint.sghd.auditjianguan.renlingrecord.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 认领记录表实体
 *
 * @version [版本号, 2022-11-04 19:24:23]
 * @作者 lizhenjie
 */
@Entity(table = "renling_record", id = "rowguid")
public class RenlingRecord extends BaseEntity implements Cloneable {
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
     * 办件名称
     */
    public String getProjectname() {
        return super.get("projectname");
    }

    public void setProjectname(String projectname) {
        super.set("projectname", projectname);
    }

    /**
     * 办件guid
     */
    public String getProjectguid() {
        return super.get("projectguid");
    }

    public void setProjectguid(String projectguid) {
        super.set("projectguid", projectguid);
    }

    /**
     * 认领人
     */
    public String getUserguid() {
        return super.get("userguid");
    }

    public void setUserguid(String userguid) {
        super.set("userguid", userguid);
    }

    /**
     * 认领人
     */
    public String getUsername() {
        return super.get("username");
    }

    public void setUsername(String username) {
        super.set("username", username);
    }

    /**
     * 部门guid
     */
    public String getOuguid() {
        return super.get("ouguid");
    }

    public void setOuguid(String ouguid) {
        super.set("ouguid", ouguid);
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
     * 认领时间
     */
    public Date getRenlingtime() {
        return super.getDate("renlingtime");
    }

    public void setRenlingtime(Date renlingtime) {
        super.set("renlingtime", renlingtime);
    }

    /**
     * 认领意见
     */
    public String getOpinion() {
        return super.get("opinion");
    }

    public void setOpinion(String opinion) {
        super.set("opinion", opinion);
    }

    /**
     * 反馈时间
     */
    public Date getOpiniontime() {
        return super.getDate("opiniontime");
    }

    public void setOpiniontime(Date opiniontime) {
        super.set("opiniontime", opiniontime);
    }

}