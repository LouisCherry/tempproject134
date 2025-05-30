package com.epoint.hcp.externalprojectinfoext.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 外部办件基本扩展信息表实体
 * 
 * @作者 wannengDB
 * @version [版本号, 2022-01-06 17:07:55]
 */
@Entity(table = "external_project_info_ext", id = "rowguid")
public class ExternalProjectInfoExt extends BaseEntity implements Cloneable
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
     * 关联建，关联external_project_info表主键
     */
    public String getProject_guid() {
        return super.get("project_guid");
    }

    public void setProject_guid(String project_guid) {
        super.set("project_guid", project_guid);
    }

    /**
     * 受理时间
     */
    public Date getAccept_date() {
        return super.getDate("accept_date");
    }

    public void setAccept_date(Date accept_date) {
        super.set("accept_date", accept_date);
    }

    /**
     * 受理人
     */
    public String getAccept_user() {
        return super.get("accept_user");
    }

    public void setAccept_user(String accept_user) {
        super.set("accept_user", accept_user);
    }

    /**
     * 受理单位
     */
    public String getAccept_ou() {
        return super.get("accept_ou");
    }

    public void setAccept_ou(String accept_ou) {
        super.set("accept_ou", accept_ou);
    }

    /**
     * 所属区县名称
     */
    public String getAreacode_name() {
        return super.get("areacode_name");
    }

    public void setAreacode_name(String areacode_name) {
        super.set("areacode_name", areacode_name);
    }

    /**
     * 事项名称
     */
    public String getTask_name() {
        return super.get("task_name");
    }

    public void setTask_name(String task_name) {
        super.set("task_name", task_name);
    }

    /**
     * 联系人
     */
    public String getLink_user() {
        return super.get("link_user");
    }

    public void setLink_user(String link_user) {
        super.set("link_user", link_user);
    }

    /**
     * 联系电话
     */
    public String getLink_phone() {
        return super.get("link_phone");
    }

    public void setLink_phone(String link_phone) {
        super.set("link_phone", link_phone);
    }

    /**
     * 承诺时间
     */
    public String getPromise_date() {
        return super.get("promise_date");
    }

    public void setPromise_date(String promise_date) {
        super.set("promise_date", promise_date);
    }

    /**
     * 办结时间
     */
    public Date getComplete_date() {
        return super.getDate("complete_date");
    }

    public void setComplete_date(Date complete_date) {
        super.set("complete_date", complete_date);
    }

    /**
     * 申请人或申请单位名称
     */
    public String getApply_obj() {
        return super.get("apply_obj");
    }

    public void setApply_obj(String apply_obj) {
        super.set("apply_obj", apply_obj);
    }

    /**
     * 身份证号
     */
    public String getApply_id() {
        return super.get("apply_id");
    }

    public void setApply_id(String apply_id) {
        super.set("apply_id", apply_id);
    }

    /**
     * 统一社会信用代码
     */
    public String getApply_uscc() {
        return super.get("apply_uscc");
    }

    public void setApply_uscc(String apply_uscc) {
        super.set("apply_uscc", apply_uscc);
    }

    /**
     * 组织机构代码
     */
    public String getApply_oc() {
        return super.get("apply_oc");
    }

    public void setApply_oc(String apply_oc) {
        super.set("apply_oc", apply_oc);
    }

    /**
     * 法定代表人姓名
     */
    public String getLegal_repr() {
        return super.get("legal_repr");
    }

    public void setLegal_repr(String legal_repr) {
        super.set("legal_repr", legal_repr);
    }

    /**
     * 联系地址
     */
    public String getLink_addr() {
        return super.get("link_addr");
    }

    public void setLink_addr(String link_addr) {
        super.set("link_addr", link_addr);
    }

    /**
     * 办件编码
     */
    public String getProject_no() {
        return super.get("project_no");
    }

    public void setProject_no(String project_no) {
        super.set("project_no", project_no);
    }

    /**
     * 流水号
     */
    public String getFlowsn() {
        return super.get("flowsn");
    }

    public void setFlowsn(String flowsn) {
        super.set("flowsn", flowsn);
    }

    /**
     * 申请人类型
     */
    public Integer getApplyer_type() {
        return super.get("applyer_type");
    }

    public void setApplyer_type(Integer applyer_type) {
        super.set("applyer_type", applyer_type);
    }

    /**
     * 证件类型
     */
    public String getCert_type() {
        return super.get("cert_type");
    }

    public void setCert_type(String cert_type) {
        super.set("cert_type", cert_type);
    }

    /**
     * 受理单位guid
     */
    public String getAccept_ou_guid() {
        return super.get("accept_ou_guid");
    }

    public void setAccept_ou_guid(String accept_ou_guid) {
        super.set("accept_ou_guid", accept_ou_guid);
    }

    /**
     * 办件来源
     */
    public String getIs_lczj() {
        return super.get("is_lczj");
    }

    public void setIs_lczj(String is_lczj) {
        super.set("is_lczj", is_lczj);
    }

    /**
     * 申请时间
     */
    public Date getApplydate() {
        return super.get("applydate");
    }

    public void setApplydate(Date applydate) {
        super.set("applydate", applydate);
    }

    /**
     * 事项类型
     */
    public Integer getTasktype() {
        return super.get("tasktype");
    }

    public void setTasktype(Integer tasktype) {
        super.set("tasktype", tasktype);
    }

    /**
     * 办件状态
     */
    public Integer getStatus() {
        return super.get("status");
    }

    public void setStatus(Integer status) {
        super.set("status", status);
    }

    /**
     * 办结类型
     */
    public Integer getBanjieresult() {
        return super.get("banjieresult");
    }

    public void setBanjieresult(Integer banjieresult) {
        super.set("banjieresult", banjieresult);
    }

    /**
     * 辖区编码
     */
    public String getAreacode() {
        return super.get("areacode");
    }

    public void setAreacode(String areacode) {
        super.set("areacode", areacode);
    }

}
