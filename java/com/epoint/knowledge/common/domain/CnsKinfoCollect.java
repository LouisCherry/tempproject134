package com.epoint.knowledge.common.domain;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 知识库收藏实体
 * 
 * @作者  xuyunhai
 * @version [版本号, 2017-02-13 10:29:14]
 */
@Entity(table = "CNS_KINFO_COLLECT", id = "rowguid")
public class CnsKinfoCollect extends BaseEntity implements Cloneable
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
    * 知识库主键
    */
    public String getKguid() {
        return super.get("kguid");
    }

    public void setKguid(String kguid) {
        super.set("kguid", kguid);
    }

    /**
    * 收藏人标识
    */
    public String getUserguid() {
        return super.get("userguid");
    }

    public void setUserguid(String userguid) {
        super.set("userguid", userguid);
    }

    /**
    * 知识库知识标题
    */
    public String getKname() {
        return super.get("kname");
    }

    public void setKname(String kname) {
        super.set("kname", kname);
    }

    /**
    * 知识类别guid
    */
    public String getCategoryguid() {
        return super.get("categoryguid");
    }

    public void setCategoryguid(String categoryguid) {
        super.set("categoryguid", categoryguid);
    }

    /**
    * 知识类别名称
    */
    public String getCategoryname() {
        return super.get("categoryname");
    }

    public void setCategoryname(String categoryname) {
        super.set("categoryname", categoryname);
    }

}
