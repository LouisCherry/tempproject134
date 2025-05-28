package com.epoint.cs.auditepidemiclog.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 访客登记实体
 * 
 * @作者  Mercury
 * @version [版本号, 2020-02-02 19:35:15]
 */
@Entity(table = "AUDIT_EPIDEMIC_LOG", id = "rowguid")
public class AuditEpidemicLog extends BaseEntity implements Cloneable
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
    public String getId() {
        return super.get("id");
    }

    public void setId(String id) {
        super.set("id", id);
    }

    /**
    * 姓名
    */
    public String getName() {
        return super.get("name");
    }

    public void setName(String name) {
        super.set("name", name);
    }

    /**
    * 住址
    */
    public String getAddress() {
        return super.get("address");
    }

    public void setAddress(String address) {
        super.set("address", address);
    }

    /**
    * 联系电话
    */
    public String getTel() {
        return super.get("tel");
    }

    public void setTel(String tel) {
        super.set("tel", tel);
    }

    /**
    * 进厅时间
    */
    public Date getEntrytime() {
        return super.getDate("entrytime");
    }

    public void setEntrytime(Date entrytime) {
        super.set("entrytime", entrytime);
    }

    /**
    * 出厅时间
    */
    public Date getExittime() {
        return super.getDate("exittime");
    }

    public void setExittime(Date exittime) {
        super.set("exittime", exittime);
    }

    /**
    * 登记状态
    */
    public String getStatus() {
        return super.get("status");
    }

    public void setStatus(String status) {
        super.set("status", status);
    }

    /**
    * 中心标识
    */
    public String getCenterguid() {
        return super.get("centerguid");
    }

    public void setCenterguid(String centerguid) {
        super.set("centerguid", centerguid);
    }

    /**
    * 中心名称
    */
    public String getCentername() {
        return super.get("centername");
    }

    public void setCentername(String centername) {
        super.set("centername", centername);
    }
    
    /**
     * 体温
     */
     public String getTemperature() {
         return super.get("temperature");
     }

     public void setTemperature(String temperature) {
         super.set("temperature", temperature);
     }
    
    

}
