package com.epoint.xmz.jgtaskmaterialrelation.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 监管事项材料上传关联关系表实体
 *
 * @version [版本号, 2023-08-14 18:17:34]
 * @作者 Administrator
 */
@Entity(table = "jg_taskmaterialrelation", id = "rowguid")
public class JgTaskmaterialrelation extends BaseEntity implements Cloneable {
    private static final long serialVersionUID = 1L;

    /**
     * 工程规划批后监管
     */
    public String getGccliengguid() {
        return super.get("gccliengguid");
    }

    public void setGccliengguid(String gccliengguid) {
        super.set("gccliengguid", gccliengguid);
    }

    /**
     * 项目代码
     */
    public String getItemcode() {
        return super.get("itemcode");
    }

    public void setItemcode(String itemcode) {
        super.set("itemcode", itemcode);
    }

    /**
     * 项目标识
     */
    public String getItemguid() {
        return super.get("itemguid");
    }

    public void setItemguid(String itemguid) {
        super.set("itemguid", itemguid);
    }

    /**
     * 建筑工程质量安全监督
     */
    public String getJzcliengguid() {
        return super.get("jzcliengguid");
    }

    public void setJzcliengguid(String jzcliengguid) {
        super.set("jzcliengguid", jzcliengguid);
    }

    /**
     * 人防工程质量安全监督
     */
    public String getRfcliengguid() {
        return super.get("rfcliengguid");
    }

    public void setRfcliengguid(String rfcliengguid) {
        super.set("rfcliengguid", rfcliengguid);
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

}