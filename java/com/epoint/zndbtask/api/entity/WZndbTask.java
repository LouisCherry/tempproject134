package com.epoint.zndbtask.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 智能导办办理事项表实体
 *
 * @version [版本号, 2023-09-18 14:43:48]
 * @作者 19273
 */
@Entity(table = "zndb_task", id = "rowguid")
public class WZndbTask extends BaseEntity implements Cloneable {
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
     * 事项类别
     */
    public String getTasktype() {
        return super.get("tasktype");
    }

    public void setTasktype(String tasktype) {
        super.set("tasktype", tasktype);
    }

    /**
     * 工程阶段
     */
    public String getPhase() {
        return super.get("phase");
    }

    public void setPhase(String phase) {
        super.set("phase", phase);
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
     * 审批事项
     */
    public String getName() {
        return super.get("name");
    }

    public void setName(String name) {
        super.set("name", name);
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
     * 实施主体
     */
    public String getDeptname() {
        return super.get("deptname");
    }

    public void setDeptname(String deptname) {
        super.set("deptname", deptname);
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
     * 办件类型
     */
    public String getType() {
        return super.get("type");
    }

    public void setType(String type) {
        super.set("type", type);
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
     * 承诺时限
     */
    public Integer getFinshtime() {
        return super.getInt("finshtime");
    }

    public void setFinshtime(Integer finshtime) {
        super.set("finshtime", finshtime);
    }

    /**
     * 办事指南
     */
    public String getGuide() {
        return super.get("guide");
    }

    public void setGuide(String guide) {
        super.set("guide", guide);
    }

    /**
     * 办理结果
     */
    public String getResult() {
        return super.get("result");
    }

    public void setResult(String result) {
        super.set("result", result);
    }

    /**
     * 父事项
     */
    public String getParenttaskguid() {
        return super.get("parenttaskguid");
    }

    public void setParenttaskguid(String parenttaskguid) {
        super.set("parenttaskguid", parenttaskguid);
    }

}