package com.epoint.jiningzwfw.epointsphmzc.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 惠企政策库实体
 * 
 * @作者  86180
 * @version [版本号, 2019-10-08 23:39:45]
 */
@Entity(table = "epoint_sp_hmzc", id = "rowguid")
public class EpointSpHmzc extends BaseEntity implements Cloneable
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
    * 政策名称
    */
    public String getZcmc() {
        return super.get("zcmc");
    }

    public void setZcmc(String zcmc) {
        super.set("zcmc", zcmc);
    }

    /**
    * 所属部门
    */
    public String getSsbm() {
        return super.get("ssbm");
    }

    public void setSsbm(String ssbm) {
        super.set("ssbm", ssbm);
    }

    /**
    * 政策内容
    */
    public String getZcnr() {
        return super.get("zcnr");
    }

    public void setZcnr(String zcnr) {
        super.set("zcnr", zcnr);
    }

    /**
    * 企业标签
    */
    public String getQybq() {
        return super.get("qybq");
    }

    public void setQybq(String qybq) {
        super.set("qybq", qybq);
    }

    /**
    * 政策指南
    */
    public String getZczn() {
        return super.get("zczn");
    }

    public void setZczn(String zczn) {
        super.set("zczn", zczn);
    }

    /**
    * 是否四新企业
    */
    public String getSfsxqy() {
        return super.get("sfsxqy");
    }

    public void setSfsxqy(String sfsxqy) {
        super.set("sfsxqy", sfsxqy);
    }

    /**
    * 行业规模
    */
    public String getJnhygm() {
        return super.get("jnhygm");
    }

    public void setJnhygm(String jnhygm) {
        super.set("jnhygm", jnhygm);
    }

    /**
    * 生命周期
    */
    public String getWwsmzq() {
        return super.get("wwsmzq");
    }

    public void setWwsmzq(String wwsmzq) {
        super.set("wwsmzq", wwsmzq);
    }

    /**
     * 实施细则
     */
     public String getSsxz() {
         return super.get("ssxz");
     }

     public void setSsxz(String ssxz) {
         super.set("ssxz", ssxz);
     }
     
     /**
      * 层级
      */
      public String getCengji() {
          return super.get("cengji");
      }

      public void setCengji(String cengji) {
          super.set("cengji", cengji);
      }
}
