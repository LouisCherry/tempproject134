package com.epoint.zoucheng.znsb.auditznsbquestionnairequestion.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 试卷和题目的关系表实体
 * 
 * @作者 TJX
 * @version [版本号, 2022-07-08 16:34:55]
 */
@Entity(table = "audit_znsb_questionnaire_question", id = "rowguid")
public class AuditZnsbQuestionnaireQuestion extends BaseEntity implements Cloneable
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
     * 目录标识
     */
    public String getQuestionnaireguid() {
        return super.get("questionnaireguid");
    }

    public void setQuestionnaireguid(String questionnaireguid) {
        super.set("questionnaireguid", questionnaireguid);
    }

    /**
     * 问题标识
     */
    public String getQuestion() {
        return super.get("question");
    }

    public void setQuestion(String question) {
        super.set("question", question);
    }

}
