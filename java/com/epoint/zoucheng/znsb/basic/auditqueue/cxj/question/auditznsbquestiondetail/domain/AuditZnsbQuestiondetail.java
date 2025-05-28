package com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestiondetail.domain;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 问卷调查-问题详情实体
 * 
 * @作者 LQ
 * @version [版本号, 2021-08-04 10:35:09]
 */
@Entity(table = "audit_znsb_questiondetail", id = "rowguid")
public class AuditZnsbQuestiondetail extends BaseEntity implements Cloneable
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
     * 选项
     */
    public String getAnsweroption() {
        return super.get("answeroption");
    }

    public void setAnsweroption(String answeroption) {
        super.set("answeroption", answeroption);
    }

    /**
     * 问题guid
     */
    public String getQuestionguid() {
        return super.get("questionguid");
    }

    public void setQuestionguid(String questionguid) {
        super.set("questionguid", questionguid);
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
     * 是否正确答案
     */
    public String getAnswer() {
        return super.get("answer");
    }

    public void setAnswer(String answer) {
        super.set("answer", answer);
    }
}
