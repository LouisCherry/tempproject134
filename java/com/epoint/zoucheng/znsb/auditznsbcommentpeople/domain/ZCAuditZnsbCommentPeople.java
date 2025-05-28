package com.epoint.zoucheng.znsb.auditznsbcommentpeople.domain;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 工作台评价窗口人员记录表实体
 * 
 * @作者  chencong
 * @version [版本号, 2020-04-01 16:23:17]
 */
@Entity(table = "audit_znsb_comment_people", id = "rowguid")
public class ZCAuditZnsbCommentPeople extends BaseEntity implements Cloneable
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
    * 中心guid
    */
    public String getCenterguid() {
        return super.get("centerguid");
    }

    public void setCenterguid(String centerguid) {
        super.set("centerguid", centerguid);
    }

    /**
    * 部门guid
    */
    public String getOuguid() {
        return super.get("ouguid");
    }

    public void setOuguid(String ouguid) {
        super.set("ouguid", ouguid);
    }

    /**
    * 窗口guid
    */
    public String getWindowguid() {
        return super.get("windowguid");
    }

    public void setWindowguid(String windowguid) {
        super.set("windowguid", windowguid);
    }

    /**
    * 窗口人员姓名
    */
    public String getWindowpeoplename() {
        return super.get("windowpeoplename");
    }

    public void setWindowpeoplename(String windowpeoplename) {
        super.set("windowpeoplename", windowpeoplename);
    }

    /**
    * 窗口人员guid
    */
    public String getWindowuserguid() {
        return super.get("windowuserguid");
    }

    public void setWindowuserguid(String windowuserguid) {
        super.set("windowuserguid", windowuserguid);
    }

    /**
    * 满意度
    */
    public String getSatisfiedtype() {
        return super.get("satisfiedtype");
    }

    public void setSatisfiedtype(String satisfiedtype) {
        super.set("satisfiedtype", satisfiedtype);
    }

    /**
    * 满意度评价选项
    */
    public String getSatisfiedtext() {
        return super.get("satisfiedtext");
    }

    public void setSatisfiedtext(String satisfiedtext) {
        super.set("satisfiedtext", satisfiedtext);
    }

    /**
    * 评价内容
    */
    public String getCommentcontent() {
        return super.get("commentcontent");
    }

    public void setCommentcontent(String commentcontent) {
        super.set("commentcontent", commentcontent);
    }

    /**
    * 评价人身份证
    */
    public String getCommentcard() {
        return super.get("commentcard");
    }

    public void setCommentcard(String commentcard) {
        super.set("commentcard", commentcard);
    }

    /**
    * 评价人名称
    */
    public String getCommentpeoplename() {
        return super.get("commentpeoplename");
    }

    public void setCommentpeoplename(String commentpeoplename) {
        super.set("commentpeoplename", commentpeoplename);
    }
    
    /**
     * 评价时间
     */
     public Date getCommentdate() {
         return super.getDate("commentdate");
     }

     public void setCommentdate(Date commentdate) {
         super.set("commentdate", commentdate);
     }

}
