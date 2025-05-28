package com.epoint.yyyz.auditspiyyyzmaterial.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 一业一证主题材料实例表实体
 * 
 * @作者  LYA
 * @version [版本号, 2020-06-20 19:20:32]
 */
@Entity(table = "AUDIT_SP_I_YYYZ_MATERIAL", id = "rowguid")
public class AuditSpIYyyzMaterial extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

    /**
     * 对应材料标识
     */
    public String getMaterialguid() {
        return super.get("materialguid");
    }

    public void setMaterialguid(String materialguid) {
        super.set("materialguid", materialguid);
    }

    /**
    * EFFICTIVERANGE
    */
    public String getEffictiverange() {
        return super.get("effictiverange");
    }

    public void setEffictiverange(String effictiverange) {
        super.set("effictiverange", effictiverange);
    }

    /**
    * CLIENGGUID
    */
    public String getCliengguid() {
        return super.get("cliengguid");
    }

    public void setCliengguid(String cliengguid) {
        super.set("cliengguid", cliengguid);
    }

    /**
    * STATUS
    */
    public String getStatus() {
        return super.get("status");
    }

    public void setStatus(String status) {
        super.set("status", status);
    }

    /**
    * RESULT
    */
    public String getResult() {
        return super.get("result");
    }

    public void setResult(String result) {
        super.set("result", result);
    }

    /**
    * SUBMITTYPE
    */
    public String getSubmittype() {
        return super.get("submittype");
    }

    public void setSubmittype(String submittype) {
        super.set("submittype", submittype);
    }

    /**
    * 是否共享材料
    */
    public String getShared() {
        return super.get("shared");
    }

    public void setShared(String shared) {
        super.set("shared", shared);
    }

    /**
    * 是否允许容缺
    */
    public String getAllowrongque() {
        return super.get("allowrongque");
    }

    public void setAllowrongque(String allowrongque) {
        super.set("allowrongque", allowrongque);
    }

    /**
    * 是否必须
    */
    public String getNecessity() {
        return super.get("necessity");
    }

    public void setNecessity(String necessity) {
        super.set("necessity", necessity);
    }

    /**
    * 子申报唯一标识
    */
    public String getSubappguid() {
        return super.get("subappguid");
    }

    public void setSubappguid(String subappguid) {
        super.set("subappguid", subappguid);
    }

    /**
    * 阶段唯一标识
    */
    public String getPhaseguid() {
        return super.get("phaseguid");
    }

    public void setPhaseguid(String phaseguid) {
        super.set("phaseguid", phaseguid);
    }

    /**
    * 主题实例唯一标识
    */
    public String getBiguid() {
        return super.get("biguid");
    }

    public void setBiguid(String biguid) {
        super.set("biguid", biguid);
    }

    /**
    * 主题标识
    */
    public String getBusinessguid() {
        return super.get("businessguid");
    }

    public void setBusinessguid(String businessguid) {
        super.set("businessguid", businessguid);
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
    * 材料名称
    */
    public String getMaterialname() {
        return super.get("materialname");
    }

    public void setMaterialname(String materialname) {
        super.set("materialname", materialname);
    }

    /**
    * 排序号
    */
    public int getOrdernum() {
        return super.getInt("ordernum");
    }

    public void setOrdernum(int ordernum) {
        super.set("ordernum", ordernum);
    }

    /**
    * certinfoinstanceguid
    */
    public String getCertinfoinstanceguid() {
        return super.get("certinfoinstanceguid");
    }

    public void setCertinfoinstanceguid(String certinfoinstanceguid) {
        super.set("certinfoinstanceguid", certinfoinstanceguid);
    }

    /**
    * MATERIALTYPE
    */
    public String getMaterialtype() {
        return super.get("materialtype");
    }

    public void setMaterialtype(String materialtype) {
        super.set("materialtype", materialtype);
    }

    /**
    * 未通过原因
    */
    public String getNopassreason() {
        return super.get("nopassreason");
    }

    public void setNopassreason(String nopassreason) {
        super.set("nopassreason", nopassreason);
    }

    /**
    * 是否补正
    */
    public String getIsbuzheng() {
        return super.get("isbuzheng");
    }

    public void setIsbuzheng(String isbuzheng) {
        super.set("isbuzheng", isbuzheng);
    }
    
    /**
     * 事项业务唯一id
     */
     public String getTaks_id() {
         return super.get("task_id");
     }

     public void setTaks_id(String task_id) {
         super.set("task_id", task_id);
     }

}
