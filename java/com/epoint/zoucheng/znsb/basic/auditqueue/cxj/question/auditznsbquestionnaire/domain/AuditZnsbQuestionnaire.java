package com.epoint.zoucheng.znsb.basic.auditqueue.cxj.question.auditznsbquestionnaire.domain;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 问卷调查-问卷实体
 * 
 * @作者 LQ
 * @version [版本号, 2021-08-04 10:35:24]
 */
@Entity(table = "audit_znsb_questionnaire", id = "rowguid")
public class AuditZnsbQuestionnaire extends BaseEntity implements Cloneable
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
     * 问卷名称
     */
    public String getQuestionnairename() {
        return super.get("questionnairename");
    }

    public void setQuestionnairename(String questionnairename) {
        super.set("questionnairename", questionnairename);
    }

    /**
     * 问卷描述
     */
    public String getQuestionnairedescription() {
        return super.get("questionnairedescription");
    }

    public void setQuestionnairedescription(String questionnairedescription) {
        super.set("questionnairedescription", questionnairedescription);
    }

    /**
     * 是否启用
     */
    public String getIs_use() {
        return super.get("is_use");
    }

    public void setIs_use(String is_use) {
        super.set("is_use", is_use);
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

    /**
     * 创建日期
     */
    public Date getCreatetime() {
        return super.getDate("createtime");
    }

    public void setCreatetime(Date createtime) {
        super.set("createtime", createtime);
    }


    public String getCover() {
        return super.get("cover");
    }

    public void setCover(String cover) {
        super.set("cover", cover);
    }

}
