package com.epoint.znsb.jnzwfw.module.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
/**
 *一体机模块点击量统计表
 * 
 */
@Entity(table = "AUDIT_ZNSB_MODULE", id = "rowguid")
public class AuditZnsbModule extends BaseEntity implements Cloneable 
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
     * 身份证号
     */
    public String getCardid() {
        return super.get("CardID");
    }

    public void setCardid(String CardID) {
        super.set("CardID", CardID);
    }
    /**
     *中心guid
     */
    public String getCenterguid() {
        return super.get("Centerguid");
    }

    public void setCenterguid(String Centerguid) {
        super.set("Centerguid", Centerguid);
    }
    /**
     * 点击时间
     */
    public Date getOnclicktime() {
        return super.get("Onclicktime");
    }

    public void setOnclicktime(Date Onclicktime) {
        super.set("Onclicktime", Onclicktime);
    }
    
    /**
     * 模块名称
     */
    public String getModulename() {
        return super.get("Modulename");
    }

    public void setModulename(String Modulename) {
        super.set("Modulename", Modulename);
    }
    /**
     * 机器mac地址
     */
    public String getMacaddress() {
        return super.get("Macaddress");
    }

    public void setMacaddress(String Macaddress) {
        super.set("Macaddress", Macaddress);
    }
    
    /**
     * 初始areacode
     */
    public String getFromareacode() {
        return super.get("fromareacode");
    }

    public void setFromareacode(String fromareacode) {
        super.set("fromareacode", fromareacode);
    }
    
    /**
     * 请求端areacode
     */
    public String getToareacode() {
        return super.get("toareacode");
    }

    public void setToareacode(String toareacode) {
        super.set("toareacode", toareacode);
    }
    
    /**
     * 是否同步
     */
    public String getIssync() {
        return super.get("issync");
    }

    public void setIssync(String issync) {
        super.set("issync", issync);
    }
}
