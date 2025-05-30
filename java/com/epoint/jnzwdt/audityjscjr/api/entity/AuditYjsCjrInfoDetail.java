package com.epoint.jnzwdt.audityjscjr.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 残疾人一件事结果详情实体
 *
 * @version [版本号, 2021-04-13 10:39:33]
 * @作者 ez
 */
@Entity(table = "audit_yjs_cjrinfo_detail", id = "rowguid")
public class AuditYjsCjrInfoDetail extends BaseEntity implements Cloneable {
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
     * 残疾种类
     */
    public String getCjzl() {
        return super.get("cjzl");
    }

    public void setCjzl(String cjzl) {
        super.set("cjzl", cjzl);
    }

    /**
     * 评定医师
     */
    public String getPdys() {
        return super.get("pdys");
    }

    public void setPdys(String pdys) {
        super.set("pdys", pdys);
    }

    /**
     * 评定时间
     */
    public Date getPdsj() {
        return super.getDate("pdsj");
    }

    public void setPdsj(Date pdsj) {
        super.set("pdsj", pdsj);
    }

    /**
     * 评定医院
     */
    public String getPdyy() {
        return super.get("pdyy");
    }

    public void setPdyy(String pdyy) {
        super.set("pdyy", pdyy);
    }

    /**
     * 致残原因
     */
    public String getZcyy() {
        return super.get("zcyy");
    }

    public void setZcyy(String zcyy) {
        super.set("zcyy", zcyy);
    }

    /**
     * 残疾表征
     */
    public String getCjbz() {
        return super.get("cjbz");
    }

    public void setCjbz(String cjbz) {
        super.set("cjbz", cjbz);
    }

    /**
     * 残疾人信息标识
     */
    public String getInfoguid() {
        return super.get("infoguid");
    }

    public void setInfoguid(String infoguid) {
        super.set("infoguid", infoguid);
    }

}
