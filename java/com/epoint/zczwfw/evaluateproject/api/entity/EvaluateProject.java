package com.epoint.zczwfw.evaluateproject.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 评价办件信息表实体
 * 
 * @author yrchan
 * @version 2022-04-11 15:52:28
 */
@Entity(table = "evaluate_project", id = "rowguid")
public class EvaluateProject extends BaseEntity implements Cloneable
{

    private static final long serialVersionUID = -3799564600077154512L;

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
     * 创建时间/导入时间
     */
    public Date getCreat_date() {
        return super.getDate("creat_date");
    }

    public void setCreat_date(Date creat_date) {
        super.set("creat_date", creat_date);
    }

    /**
     * 办件标识
     */
    public String getProject_guid() {
        return super.get("project_guid");
    }

    public void setProject_guid(String project_guid) {
        super.set("project_guid", project_guid);
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
     * 受理人
     */
    public String getAccept_user() {
        return super.get("accept_user");
    }

    public void setAccept_user(String accept_user) {
        super.set("accept_user", accept_user);
    }

    /**
     * 受理所属部门
     */
    public String getAccept_department() {
        return super.get("accept_department");
    }

    public void setAccept_department(String accept_department) {
        super.set("accept_department", accept_department);
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
     * 申请人或申请单位
     */
    public String getApply_object() {
        return super.get("apply_object");
    }

    public void setApply_object(String apply_object) {
        super.set("apply_object", apply_object);
    }

    /**
     * 身份证/统一社会信用代码
     */
    public String getApply_id() {
        return super.get("apply_id");
    }

    public void setApply_id(String apply_id) {
        super.set("apply_id", apply_id);
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
     * 办结时间
     */
    public Date getHandle_date() {
        return super.getDate("handle_date");
    }

    public void setHandle_date(Date handle_date) {
        super.set("handle_date", handle_date);
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
     * 是否发送短信
     */
    public Integer getIs_send() {
        return super.getInt("is_send");
    }

    public void setIs_send(Integer is_send) {
        super.set("is_send", is_send);
    }

    /**
     * 是否评价
     */
    public Integer getIs_evaluate() {
        return super.getInt("is_evaluate");
    }

    public void setIs_evaluate(Integer is_evaluate) {
        super.set("is_evaluate", is_evaluate);
    }

    /**
     * 评价时间
     */
    public Date getEvaluate_date() {
        return super.getDate("evaluate_date");
    }

    public void setEvaluate_date(Date evaluate_date) {
        super.set("evaluate_date", evaluate_date);
    }

    /**
     * 评价结果
     */
    public String getEvaluate_result() {
        return super.get("evaluate_result");
    }

    public void setEvaluate_result(String evaluate_result) {
        super.set("evaluate_result", evaluate_result);
    }

    /**
     * 评价办件来源
     */
    public Integer getProject_source() {
        return super.getInt("project_source");
    }

    public void setProject_source(Integer project_source) {
        super.set("project_source", project_source);
    }

}
