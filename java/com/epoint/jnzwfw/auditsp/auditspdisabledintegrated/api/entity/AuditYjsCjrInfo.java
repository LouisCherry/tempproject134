package com.epoint.jnzwfw.auditsp.auditspdisabledintegrated.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 残疾人一件事结果实体
 *
 * @version [版本号, 2021-04-13 10:39:28]
 * @作者 ez
 */
@Entity(table = "audit_yjs_cjrinfo", id = "rowguid")
public class AuditYjsCjrInfo extends BaseEntity implements Cloneable {
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
     * 残疾人信息表示
     */
    public String getCjrguid() {
        return super.get("cjrguid");
    }

    public void setCjrguid(String cjrguid) {
        super.set("cjrguid", cjrguid);
    }

    /**
     * 残疾证号
     */
    public String getCjrcard() {
        return super.get("cjrcard");
    }

    public void setCjrcard(String cjrcard) {
        super.set("cjrcard", cjrcard);
    }

    /**
     * 残疾类别
     */
    public String getCjlb() {
        return super.get("cjlb");
    }

    public void setCjlb(String cjlb) {
        super.set("cjlb", cjlb);
    }

    /**
     * 残疾等级
     */
    public String getCjdj() {
        return super.get("cjdj");
    }

    public void setCjdj(String cjdj) {
        super.set("cjdj", cjdj);
    }

    /**
     * 残疾详情
     */
    public String getCjxq() {
        return super.get("cjxq");
    }

    public void setCjxq(String cjxq) {
        super.set("cjxq", cjxq);
    }

    /**
     * 残疾人证号
     */
    public String getCjrzh() {
        return super.get("cjrzh");
    }

    public void setCjrzh(String cjrzh) {
        super.set("cjrzh", cjrzh);
    }

    /**
     * 最后审核日期
     */
    public Date getZhshrq() {
        return super.getDate("zhshrq");
    }

    public void setZhshrq(Date zhshrq) {
        super.set("zhshrq", zhshrq);
    }

    /**
     * 首次办证日期
     */
    public Date getScbzrq() {
        return super.getDate("scbzrq");
    }

    public void setScbzrq(Date scbzrq) {
        super.set("scbzrq", scbzrq);
    }

    /**
     * 有效期开始时间
     */
    public Date getYxqkssj() {
        return super.getDate("yxqkssj");
    }

    public void setYxqkssj(Date yxqkssj) {
        super.set("yxqkssj", yxqkssj);
    }

    /**
     * 有效期结束时间
     */
    public Date getYxqjssj() {
        return super.getDate("yxqjssj");
    }

    public void setYxqjssj(Date yxqjssj) {
        super.set("yxqjssj", yxqjssj);
    }

}
