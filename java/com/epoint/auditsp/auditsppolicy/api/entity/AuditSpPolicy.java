package com.epoint.auditsp.auditsppolicy.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 一件事政策解读实体
 * 
 * @作者 zyq
 * @version [版本号, 2024-12-02 17:15:34]
 */
@Entity(table = "audit_sp_policy", id = "rowguid")
public class AuditSpPolicy extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

    /**
     * 解答
     */
    public String getAnswer() {
        return super.get("answer");
    }

    public void setAnswer(String answer) {
        super.set("answer", answer);
    }

    /**
     * 一件事主键
     */
    public String getBusinessguid() {
        return super.get("businessguid");
    }

    public void setBusinessguid(String businessguid) {
        super.set("businessguid", businessguid);
    }

    /**
     * 问题
     */
    public String getQuestion() {
        return super.get("question");
    }

    public void setQuestion(String question) {
        super.set("question", question);
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
     * 所属辖区号
     */
    public String getBelongxiaqucode() {
        return super.get("belongxiaqucode");
    }

    public void setBelongxiaqucode(String belongxiaqucode) {
        super.set("belongxiaqucode", belongxiaqucode);
    }

}
