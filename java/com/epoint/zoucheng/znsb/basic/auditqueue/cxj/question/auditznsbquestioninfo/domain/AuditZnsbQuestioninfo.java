package com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestioninfo.domain;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 问卷调查-问题实体
 * 
 * @作者 LQ
 * @version [版本号, 2021-08-04 10:35:17]
 */
@Entity(table = "audit_znsb_questioninfo", id = "rowguid")
public class AuditZnsbQuestioninfo extends BaseEntity implements Cloneable
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
     * 问题
     */
    public String getQuestion() {
        return super.get("question");
    }

    public void setQuestion(String question) {
        super.set("question", question);
    }

    /**
     * 问题描述
     */
    public String getQuestiondescription() {
        return super.get("questiondescription");
    }

    public void setQuestiondescription(String questiondescription) {
        super.set("questiondescription", questiondescription);
    }

    /**
     * 问题类型
     */
    public String getQuestiontype() {
        return super.get("questiontype");
    }

    public void setQuestiontype(String questiontype) {
        super.set("questiontype", questiontype);
    }

    /**
     * 问卷guid
     */
    public String getQuestionnaireguid() {
        return super.get("questionnaireguid");
    }

    public void setQuestionnaireguid(String questionnaireguid) {
        super.set("questionnaireguid", questionnaireguid);
    }

    /**
     * 排序
     */
    public Integer getSort() {
        return super.get("sort");
    }

    public void setSort(Integer sort) {
        super.set("sort", sort);
    }

    /**
     * 中心编码
     */
    public String getCenterguid() {
        return super.get("centerguid");
    }

    public void setCenterguid(String centerguid) {
        super.set("centerguid", centerguid);
    }
}
