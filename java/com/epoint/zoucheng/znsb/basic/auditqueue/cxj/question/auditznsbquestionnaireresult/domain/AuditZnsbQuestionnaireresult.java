package com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestionnaireresult.domain;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 问卷结果实体
 * 
 * @作者 LQ
 * @version [版本号, 2021-08-05 15:25:58]
 */
@Entity(table = "audit_znsb_questionnaireresult", id = "rowguid")
public class AuditZnsbQuestionnaireresult extends BaseEntity implements Cloneable
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
     * 问卷实体
     */
    public String getQuestionnairereinfo() {
        return super.get("questionnairereinfo");
    }

    public void setQuestionnairereinfo(String questionnairereinfo) {
        super.set("questionnairereinfo", questionnairereinfo);
    }

    /**
     * 问卷答案
     */
    public String getQuestionnairereanswer() {
        return super.get("questionnairereanswer");
    }

    public void setQuestionnairereanswer(String questionnairereanswer) {
        super.set("questionnairereanswer", questionnairereanswer);
    }

    /**
     * 设备地址
     */
    public String getMacaddress() {
        return super.get("macaddress");
    }

    public void setMacaddress(String macaddress) {
        super.set("macaddress", macaddress);
    }

    /**
     * 问卷名称
     */
    public String getQuestionnairename() {
        return super.get("questionnairename");
    }

    public void setQuestionnairename(String questionnairename) {
        super.set("questionnairename", questionnairename);
    }

    /**
     * 问卷标识
     */
    public String getQuestionnaireguid() {
        return super.get("questionnaireguid");
    }

    public void setQuestionnaireguid(String questionnaireguid) {
        super.set("questionnaireguid", questionnaireguid);
    }

    /**
     * 中心号
     */
    public String getCenterguid() {
        return super.get("centerguid");
    }

    public void setCenterguid(String centerguid) {
        super.set("centerguid", centerguid);
    }
}
