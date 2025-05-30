package com.epoint.smsinfo.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 上行短信内容实体
 * 
 * @author yrchan
 * @version [版本号, 2022-04-11 16:23:40]
 */
@Entity(table = "sms_info", id = "rowguid")
public class SmsInfo extends BaseEntity implements Cloneable
{

    /**
     * 
     */
    private static final long serialVersionUID = 5894405182377907551L;

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
     * 请求时间
     */
    public Date getReq_date() {
        return super.getDate("req_date");
    }

    public void setReq_date(Date req_date) {
        super.set("req_date", req_date);
    }

    /**
     * 上行手机号码
     */
    public String getMobile() {
        return super.get("mobile");
    }

    public void setMobile(String mobile) {
        super.set("mobile", mobile);
    }

    /**
     * 上行短信内容
     */
    public String getSmscontent() {
        return super.get("smscontent");
    }

    public void setSmscontent(String smscontent) {
        super.set("smscontent", smscontent);
    }

    /**
     * 上行短信发送时间
     */
    public Date getSendtime() {
        return super.getDate("sendtime");
    }

    public void setSendtime(Date sendtime) {
        super.set("sendtime", sendtime);
    }

    /**
     * 上行服务代码
     */
    public String getAddSerial() {
        return super.get("addSerial");
    }

    public void setAddSerial(String addSerial) {
        super.set("addSerial", addSerial);
    }

}
