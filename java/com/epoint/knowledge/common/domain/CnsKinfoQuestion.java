package com.epoint.knowledge.common.domain;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 知识库提问表实体
 * 
 * @作者  wxlin
 * @version [版本号, 2017-06-01 13:43:10]
 */
@Entity(table = "CNS_KINFO_QUESTION", id = "rowguid")
public class CnsKinfoQuestion extends BaseEntity implements Cloneable
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
    * 问题内容
    */
    public String getQuestioncontent() {
        return super.get("questioncontent");
    }

    public void setQuestioncontent(String questioncontent) {
        super.set("questioncontent", questioncontent);
    }

    /**
    * 知识类别编号
    */
    public String getKinfocategoryguid() {
        return super.get("kinfocategoryguid");
    }

    public void setKinfocategoryguid(String kinfocategoryguid) {
        super.set("kinfocategoryguid", kinfocategoryguid);
    }

    /**
    * 知识类别名称
    */
    public String getKinfocategoryname() {
        return super.get("kinfocategoryname");
    }

    public void setKinfocategoryname(String kinfocategoryname) {
        super.set("kinfocategoryname", kinfocategoryname);
    }

    /**
    * 部门名称
    */
    public String getDeptname() {
        return super.get("deptname");
    }

    public void setDeptname(String deptname) {
        super.set("deptname", deptname);
    }

    /**
     * 部门编号
     */
    public String getDeptguid() {
        return super.get("deptguid");
    }

    public void setDeptguid(String deptguid) {
        super.set("deptguid", deptguid);
    }

    /**
    * 附件关联编号
    */
    public String getCliengguid() {
        return super.get("cliengguid");
    }

    public void setCliengguid(String cliengguid) {
        super.set("cliengguid", cliengguid);
    }

    /**
    * 提问时间
    */
    public Date getAsktime() {
        return super.getDate("asktime");
    }

    public void setAsktime(Date asktime) {
        super.set("asktime", asktime);
    }

    /**
    * 提问人编号
    */
    public String getAskpersonguid() {
        return super.get("askpersonguid");
    }

    public void setAskpersonguid(String askpersonguid) {
        super.set("askpersonguid", askpersonguid);
    }

    /**
    * 提问人姓名
    */
    public String getAskpersonname() {
        return super.get("askpersonname");
    }

    public void setAskpersonname(String askpersonname) {
        super.set("askpersonname", askpersonname);
    }

    /**
    * 答复人编号
    */
    public String getAnswerpersonguid() {
        return super.get("answerpersonguid");
    }

    public void setAnswerpersonguid(String answerpersonguid) {
        super.set("answerpersonguid", answerpersonguid);
    }

    /**
    * 答复人姓名
    */
    public String getAnswerpersonname() {
        return super.get("answerpersonname");
    }

    public void setAnswerpersonname(String answerpersonname) {
        super.set("answerpersonname", answerpersonname);
    }

    /**
    * 答复内容
    */
    public String getAnswercontent() {
        return super.get("answercontent");
    }

    public void setAnswercontent(String answercontent) {
        super.set("answercontent", answercontent);
    }

    /**
    * 答复时间
    */
    public Date getAnswertime() {
        return super.getDate("answertime");
    }

    public void setAnswertime(Date answertime) {
        super.set("answertime", answertime);
    }

    /**
     * 答复状态
     */
    public String getAnswerstatus() {
        return super.get("answerstatus");
    }

    public void setAnswerstatus(String answerstatus) {
        super.set("answerstatus", answerstatus);
    }

    /**
     * 生效时间
     */
    public Date getBegindate() {
        return super.getDate("begindate");
    }

    public void setBegindate(Date begindate) {
        super.set("begindate", begindate);
    }

    /**
    * 时效时间
    */
    public Date getEffectdate() {
        return super.getDate("effectdate");
    }

    public void setEffectdate(Date effectdate) {
        super.set("effectdate", effectdate);
    }

    /**
     * 知识表关联guid
     */
    public String getKguid() {
        return super.get("kguid");
    }

    public void setKguid(String kguid) {
        super.set("kguid", kguid);
    }

    /**
     * 审核状态
     */
    public String getKstatus() {
        return super.get("kstatus");
    }

    public void setKstatus(String kstatus) {
        super.set("kstatus", kstatus);
    }

    /**
     * 拒绝提问原因类型
     */
    public String getRefusereasontype() {
        return super.get("refusereasontype");
    }

    public void setRefusereasontype(String refusereasontype) {
        super.set("refusereasontype", refusereasontype);
    }

    /**
     * 拒绝提问原因内容
     */
    public String getRefusereasoncontexte() {
        return super.get("refusereasoncontext");
    }

    public void setRefusereasoncontext(String refusereasoncontext) {
        super.set("refusereasoncontext", refusereasoncontext);
    }

    /**
     * 
     * pviguid
     */
    public String getPviguid() {
        return super.get("pviguid");
    }

    public void setPviguid(String pviguid) {
        super.set("pviguid", pviguid);
    }
    public String getIskinfo() {
        return super.get("Iskinfo");
    }

    public void setIskinfo(String Iskinfo) {
        super.set("Iskinfo", Iskinfo);
    }
    public String getKinfoguid() {
        return super.get("Kinfoguid");
    }

    public void setKinfoguid(String Kinfoguid) {
        super.set("Kinfoguid", Kinfoguid);
    }
}
