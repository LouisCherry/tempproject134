package com.epoint.xmz.gxhimportcert.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 个性化证照导入表实体
 * 
 * @作者 dyxin
 * @version [版本号, 2023-06-12 16:30:17]
 */
@Entity(table = "gxh_import_cert_ls", id = "rowguid")
public class GxhImportCertLs extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

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
     * 许可证号
     */
    public String getXkzh() {
        return super.get("xkzh");
    }

    public void setXkzh(String xkzh) {
        super.set("xkzh", xkzh);
    }

    /**
     * 证照类型编号
     */
    public String getCerttypecode() {
        return super.get("certtypecode");
    }

    public void setCerttypecode(String certtypecode) {
        super.set("certtypecode", certtypecode);
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
     * 年份标识
     */
    public String getYearflag() {
        return super.get("yearflag");
    }

    public void setYearflag(String yearflag) {
        super.set("yearflag", yearflag);
    }

    /**
     * 证照名称
     */
    public String getCertname() {
        return super.get("certname");
    }

    public void setCertname(String certname) {
        super.set("certname", certname);
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
     * 操作日期
     */
    public Date getOperatedate() {
        return super.getDate("operatedate");
    }

    public void setOperatedate(Date operatedate) {
        super.set("operatedate", operatedate);
    }

    /**
     * 有效期开始日期
     */
    public Date getYxqstartdate() {
        return super.getDate("yxqstartdate");
    }

    public void setYxqstartdate(Date yxqstartdate) {
        super.set("yxqstartdate", yxqstartdate);
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
     * 有效期截止日期
     */
    public Date getYxqenddate() {
        return super.getDate("yxqenddate");
    }

    public void setYxqenddate(Date yxqenddate) {
        super.set("yxqenddate", yxqenddate);
    }

    /**
     * 申请单位
     */
    public String getApplyname() {
        return super.get("applyname");
    }

    public void setApplyname(String applyname) {
        super.set("applyname", applyname);
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
     * 统一社会信用代码
     */
    public String getTyshxydm() {
        return super.get("tyshxydm");
    }

    public void setTyshxydm(String tyshxydm) {
        super.set("tyshxydm", tyshxydm);
    }

    /**
     * 单位负责人姓名
     */
    public String getDwfzr() {
        return super.get("dwfzr");
    }

    public void setDwfzr(String dwfzr) {
        super.set("dwfzr", dwfzr);
    }

    /**
     * 手机号码
     */
    public String getMobile() {
        return super.get("mobile");
    }

    public void setMobile(String mobile) {
        super.set("mobile", mobile);
    }

    /**
     * 办理科室
     */
    public String getHandleks() {
        return super.get("handleks");
    }

    public void setHandleks(String handleks) {
        super.set("handleks", handleks);
    }

    /**
     * 办理科室联系电话
     */
    public String getKsphone() {
        return super.get("ksphone");
    }

    public void setKsphone(String ksphone) {
        super.set("ksphone", ksphone);
    }

    /**
     * 是否自办
     */
    public String getIs_zb() {
        return super.get("is_zb");
    }

    public void setIs_zb(String is_zb) {
        super.set("is_zb", is_zb);
    }

    /**
     * 业务状态
     */
    public String getYwstatus() {
        return super.get("ywstatus");
    }

    public void setYwstatus(String ywstatus) {
        super.set("ywstatus", ywstatus);
    }

    /**
     * 短信状态
     */
    public String getSmstatus() {
        return super.get("smstatus");
    }

    public void setSmstatus(String smstatus) {
        super.set("smstatus", smstatus);
    }

    /**
     * 短信发送时间
     */
    public Date getSmsdate() {
        return super.getDate("smsdate");
    }

    public void setSmsdate(Date smsdate) {
        super.set("smsdate", smsdate);
    }

}
