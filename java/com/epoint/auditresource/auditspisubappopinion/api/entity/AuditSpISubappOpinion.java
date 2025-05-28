package com.epoint.auditresource.auditspisubappopinion.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 并联审批意见实体
 * 
 * @作者  zhaoy
 * @version [版本号, 2019-04-24 15:11:41]
 */
@Entity(table = "audit_sp_i_subapp_opinion", id = "rowguid")
public class AuditSpISubappOpinion extends BaseEntity implements Cloneable
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
    * 并联审批主题guid
    */
    public String getBusinessguid() {
        return super.get("businessguid");
    }

    public void setBusinessguid(String businessguid) {
        super.set("businessguid", businessguid);
    }

    /**
    * 业务实例guid
    */
    public String getBiguid() {
        return super.get("biguid");
    }

    public void setBiguid(String biguid) {
        super.set("biguid", biguid);
    }

    /**
    * 意见时间
    */
    public Date getCreatedate() {
        return super.getDate("createdate");
    }

    public void setCreatedate(Date createdate) {
        super.set("createdate", createdate);
    }

    /**
    * 意见人
    */
    public String getUsername() {
        return super.get("username");
    }

    public void setUsername(String username) {
        super.set("username", username);
    }

    /**
    * 意见人guid
    */
    public String getUserguid() {
        return super.get("userguid");
    }

    public void setUserguid(String userguid) {
        super.set("userguid", userguid);
    }
    
    /**
    * 意见
    */
    public String getContent() {
        return super.get("content");
    }

    public void setContent(String content) {
        super.set("content", content);
    }

    /**
    * 类型，1为领导批复，2为问题
    */
    public String getType() {
        return super.get("type");
    }

    public void setType(String type) {
        super.set("type", type);
    }
    
    /**
    * 问题内容
    */
    public String getFaqcontent() {
        return super.get("faqcontent");
    }

    public void setFaqcontent(String faqcontent) {
        super.set("faqcontent", faqcontent);
    }
    
    /**
    * 问题回复
    */
    public String getFaqanswer() {
        return super.get("faqanswer");
    }

    public void setFaqanswer(String faqanswer) {
        super.set("faqanswer", faqanswer);
    }
    
    /**
    * 问题回复时间
    */
    public Date getFaqanswerdate() {
        return super.get("faqanswerdate");
    }

    public void setFaqanswerdate(Date faqanswerdate) {
        super.set("faqanswerdate", faqanswerdate);
    }
    
    /**
    * 答疑人姓名
    */
    public String getFaqansweruser() {
        return super.get("faqansweruser");
    }

    public void setFaqansweruser(String faqansweruser) {
        super.set("faqansweruser", faqansweruser);
    }

    /**
    * 答疑人guid
    */
    public String getFaqansweruserguid() {
        return super.get("faqansweruserguid");
    }

    public void setFaqansweruserguid(String faqansweruserguid) {
        super.set("faqansweruserguid", faqansweruserguid);
    }
}
