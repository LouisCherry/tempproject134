package com.epoint.majoritem.itmeservelog.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 重点项目服务记录表实体
 *
 * @version [版本号, 2024-07-09 15:05:57]
 * @作者 19273
 */
@Entity(table = "itme_servelog", id = "rowguid")
public class ItmeServelog extends BaseEntity implements Cloneable {
    private static final long serialVersionUID = 1L;

    /**
     * 项目主键
     */
    public String getItemguid() {
        return super.get("itemguid");
    }

    public void setItemguid(String itemguid) {
        super.set("itemguid", itemguid);
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
     * 服务日期
     */
    public Date getServertime() {
        return super.getDate("servertime");
    }

    public void setServertime(Date servertime) {
        super.set("servertime", servertime);
    }

    /**
     * 服务人员
     */
    public String getServername() {
        return super.get("servername");
    }

    public void setServername(String servername) {
        super.set("servername", servername);
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
     * 服务内容
     */
    public String getServercontent() {
        return super.get("servercontent");
    }

    public void setServercontent(String servercontent) {
        super.set("servercontent", servercontent);
    }

    /**
     * 已完成投资额
     */
    public Double getFinishmoney() {
        return super.getDouble("finishmoney");
    }

    public void setFinishmoney(Double finishmoney) {
        super.set("finishmoney", finishmoney);
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
     * 施工进度
     */
    public String getSgjd() {
        return super.get("sgjd");
    }

    public void setSgjd(String sgjd) {
        super.set("sgjd", sgjd);
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

    /**
     * 施工现场附件guid
     */
    public String getSgxcclientguid() {
        return super.get("sgxcclientguid");
    }

    public void setSgxcclientguid(String sgxcclientguid) {
        super.set("sgxcclientguid", sgxcclientguid);
    }

    /**
     * 审批流程图附件guid
     */
    public String getSplctclientguid() {
        return super.get("splctclientguid");
    }

    public void setSplctclientguid(String splctclientguid) {
        super.set("splctclientguid", splctclientguid);
    }

}