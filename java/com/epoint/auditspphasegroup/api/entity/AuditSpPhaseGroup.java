package com.epoint.auditspphasegroup.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 前四阶段分组配置表实体
 * 
 * @作者 lzhming
 * @version [版本号, 2023-03-17 09:01:54]
 */
@Entity(table = "AUDIT_SP_PHASE_GROUP", id = "rowguid")
public class AuditSpPhaseGroup extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

    /**
     * 分组（事项组）排序
     */
    public String getBigordernum() {
        return super.get("bigordernum");
    }

    public void setBigordernum(String bigordernum) {
        super.set("bigordernum", bigordernum);
    }

    /**
     * 分组（事项组）展示类型
     */
    public String getBigtype() {
        return super.get("bigtype");
    }

    public void setBigtype(String bigtype) {
        super.set("bigtype", bigtype);
    }

    /**
     * 组名
     */
    public String getGroupname() {
        return super.get("groupname");
    }

    public void setGroupname(String groupname) {
        super.set("groupname", groupname);
    }

    /**
     * 是否重要
     */
    public String getIssamelevel() {
        return super.get("issamelevel");
    }

    public void setIssamelevel(String issamelevel) {
        super.set("issamelevel", issamelevel);
    }

    /**
     * 阶段值
     */
    public String getPhaseguid() {
        return super.get("phaseguid");
    }

    public void setPhaseguid(String phaseguid) {
        super.set("phaseguid", phaseguid);
    }

    /**
     * 阶段名称
     */
    public String getPhasename() {
        return super.get("phasename");
    }

    public void setPhasename(String phasename) {
        super.set("phasename", phasename);
    }

    /**
     * 提示
     */
    public String getTip() {
        return super.get("tip");
    }

    public void setTip(String tip) {
        super.set("tip", tip);
    }

    /**
     * 是否多选一
     */
    public String getToonly() {
        return super.get("toonly");
    }

    public void setToonly(String toonly) {
        super.set("toonly", toonly);
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
     * 年份标识
     */
    public String getYearflag() {
        return super.get("yearflag");
    }

    public void setYearflag(String yearflag) {
        super.set("yearflag", yearflag);
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
     * 操作日期
     */
    public Date getOperatedate() {
        return super.getDate("operatedate");
    }

    public void setOperatedate(Date operatedate) {
        super.set("operatedate", operatedate);
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
     * 所属辖区号
     */
    public String getBelongxiaqucode() {
        return super.get("belongxiaqucode");
    }

    public void setBelongxiaqucode(String belongxiaqucode) {
        super.set("belongxiaqucode", belongxiaqucode);
    }

    public String getIsmerge() {
        return super.get("ismerge");
    }

    public void setIsmerge(String ismerge) {
        super.set("ismerge", ismerge);
    }

}
