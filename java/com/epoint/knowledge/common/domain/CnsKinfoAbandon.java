package com.epoint.knowledge.common.domain;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 下架知识表实体
 * 
 * @作者  wxlin
 * @version [版本号, 2017-06-07 18:50:19]
 */
@Entity(table = "CNS_KINFO_ABANDON", id = "rowguid")
public class CnsKinfoAbandon extends BaseEntity implements Cloneable
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
    * 下架时间
    */
    public Date getAbandontime() {
        return super.getDate("abandontime");
    }

    public void setAbandontime(Date abandontime) {
        super.set("abandontime", abandontime);
    }

    /**
    * 下架知识名称
    */
    public String getAbandonkinfoname() {
        return super.get("abandonkinfoname");
    }

    public void setAbandonkinfoname(String abandonkinfoname) {
        super.set("abandonkinfoname", abandonkinfoname);
    }

    /**
    * 下架知识内容
    */
    public String getAbandonkinfocontent() {
        return super.get("abandonkinfocontent");
    }

    public void setAbandonkinfocontent(String abandonkinfocontent) {
        super.set("abandonkinfocontent", abandonkinfocontent);
    }

    /**
    * 知识库guid
    */
    public String getKguid() {
        return super.get("kguid");
    }

    public void setKguid(String kguid) {
        super.set("kguid", kguid);
    }

    /**
    * 下架原因类别编号
    */
    public String getAbandonreasontype() {
        return super.get("abandonreasontype");
    }

    public void setAbandonreasontype(String abandonreasontype) {
        super.set("abandonreasontype", abandonreasontype);
    }

    /**
    * 下架原因内容
    */
    public String getAbandonreasoncontent() {
        return super.get("abandonreasoncontent");
    }

    public void setAbandonreasoncontent(String abandonreasoncontent) {
        super.set("abandonreasoncontent", abandonreasoncontent);
    }
    
    /**
     * 下架知识类别编号
     */
     public String getAbandonkinfocategoryguid() {
         return super.get("abandonkinfocategoryguid");
     }

     public void setAbandonkinfocategoryguid(String abandonkinfocategoryguid) {
         super.set("abandonkinfocategoryguid", abandonkinfocategoryguid);
     }

     /**
     * 下架知识类别内容
     */
     public String getAbandonkinfocategoryname() {
         return super.get("abandonkinfocategoryname");
     }

     public void setAbandonkinfocategoryname(String abandonkinfocategoryname) {
         super.set("abandonkinfocategoryname", abandonkinfocategoryname);
     }

}
