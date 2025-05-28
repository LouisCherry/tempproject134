package com.epoint.xmz.kypoint.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 勘验要点表实体
 * 
 * @作者 RaoShaoliang
 * @version [版本号, 2023-07-10 16:28:23]
 */
@Entity(table = "ky_point", id = "rowguid")
public class KyPoint extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

    /**
     * 勘验rowguid
     */
    public String getKyguid() {
        return super.get("kyguid");
    }

    public void setKyguid(String kyguid) {
        super.set("kyguid", kyguid);
    }

    /**
     * 勘验要点
     */
    public String getKypoint() {
        return super.get("kypoint");
    }

    public void setKypoint(String kypoint) {
        super.set("kypoint", kypoint);
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
