package com.epoint.sghd.auditjianguan.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 审管一体化审批信息列表办件vo对象
 *
 * @author 刘雨雨
 * @time 2018年12月14日上午11:22:20
 */
public class ProjectVO implements Serializable
{

    private static final long serialVersionUID = 1844644541948901311L;

    private String rowguid;

    private String flowsn;

    private String projectname;

    private Integer banjieresult;

    private Date banjiedate;

    private Date applydate;

    private String ouname;

    private String applyername;

    private String renlingtype;

    private String renling_username;
    private Date renlingdate;

    private String iszijianxitong;

    /**
     * 业务来源
     */
    private String businessSource;

    public String getRowguid() {
        return rowguid;
    }

    public void setRowguid(String rowguid) {
        this.rowguid = rowguid;
    }

    public String getFlowsn() {
        return flowsn;
    }

    public void setFlowsn(String flowsn) {
        this.flowsn = flowsn;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public Integer getBanjieresult() {
        return banjieresult;
    }

    public void setBanjieresult(Integer banjieresult) {
        this.banjieresult = banjieresult;
    }

    public Date getBanjiedate() {
        return banjiedate;
    }

    public void setBanjiedate(Date banjiedate) {
        this.banjiedate = banjiedate;
    }

    public Date getApplydate() {
        return applydate;
    }

    public void setApplydate(Date applydate) {
        this.applydate = applydate;
    }

    public String getOuname() {
        return ouname;
    }

    public void setOuname(String ouname) {
        this.ouname = ouname;
    }

    public String getApplyername() {
        return applyername;
    }

    public void setApplyername(String applyername) {
        this.applyername = applyername;
    }

    public String getRenlingtype() {
        return renlingtype;
    }

    public void setRenlingtype(String renlingtype) {
        this.renlingtype = renlingtype;
    }

    public String getIszijianxitong() {
        return iszijianxitong;
    }

    public void setIszijianxitong(String iszijianxitong) {
        this.iszijianxitong = iszijianxitong;
    }

    public String getBusinessSource() {
        return businessSource;
    }

    public void setBusinessSource(String businessSource) {
        this.businessSource = businessSource;
    }

    public String getRenling_username() {
        return renling_username;
    }

    public void setRenling_username(String renling_username) {
        this.renling_username = renling_username;
    }

    public Date getRenlingdate() {
        return renlingdate;
    }

    public void setRenlingdate(Date renlingdate) {
        this.renlingdate = renlingdate;
    }
}
