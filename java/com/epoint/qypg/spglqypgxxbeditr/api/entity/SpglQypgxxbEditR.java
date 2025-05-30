package com.epoint.qypg.spglqypgxxbeditr.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 区域评估信息关联表实体
 *
 * @version [版本号, 2023-11-10 14:17:59]
 * @作者 shaoyuhui
 */
@Entity(table = "SPGL_QYPGXXB_EDIT_R", id = "rowguid")
public class SpglQypgxxbEditR extends BaseEntity implements Cloneable {
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
     * 项目标识
     */
    public String getSubappguid() {
        return super.get("subappguid");
    }

    public void setSubappguid(String subappguid) {
        super.set("subappguid", subappguid);
    }

    /**
     * 父项目标识
     */
    public String getPre_itemguid() {
        return super.get("pre_itemguid");
    }

    public void setPre_itemguid(String pre_itemguid) {
        super.set("pre_itemguid", pre_itemguid);
    }

    /**
     * 区域评估标识
     */
    public String getQypgguid() {
        return super.get("qypgguid");
    }

    public void setQypgguid(String qypgguid) {
        super.set("qypgguid", qypgguid);
    }

    /**
     * 创建时间
     */
    public Date getCreatedate() {
        return super.getDate("createdate");
    }

    public void setCreatedate(Date createdate) {
        super.set("createdate", createdate);
    }

}
