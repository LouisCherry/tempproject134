package com.epoint.knowledge.common.domain;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 知识库评价实体
 * 
 * @作者  xuyunhai
 * @version [版本号, 2017-02-13 10:28:58]
 */
@Entity(table = "CNS_Kinfo_Evl", id = "rowguid")
public class CnsKinfoEvl extends BaseEntity implements Cloneable
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
    * 知识标示
    */
    public String getKguid() {
        return super.get("kguid");
    }

    public void setKguid(String kguid) {
        super.set("kguid", kguid);
    }

    /**
    * 评价人
    */
    public String getEvlperson() {
        return super.get("evlperson");
    }

    public void setEvlperson(String evlperson) {
        super.set("evlperson", evlperson);
    }

    /**
    * 评价人Guid
    */
    public String getEvlpersonguid() {
        return super.get("evlpersonguid");
    }

    public void setEvlpersonguid(String evlpersonguid) {
        super.set("evlpersonguid", evlpersonguid);
    }

    /**
    * 评价结果
    */
    public String getEvlresult() {
        return super.get("evlresult");
    }

    public void setEvlresult(String evlresult) {
        super.set("evlresult", evlresult);
    }

    /**
    * 评价补充
    */
    public String getEvlcontent() {
        return super.get("evlcontent");
    }

    public void setEvlcontent(String evlcontent) {
        super.set("evlcontent", evlcontent);
    }

    /**
    * 评价时间
    */
    public Date getEvltime() {
        return super.getDate("evltime");
    }

    public void setEvltime(Date evltime) {
        super.set("evltime", evltime);
    }

    /**
     * 部门guid
     */
     public String getOuguid() {
         return super.get("ouguid");
     }

     public void setOuguid(String ouguid) {
         super.set("ouguid", ouguid);
     }
     
     /**
      * 部门名称
      */
      public String getOuname() {
          return super.get("ouname");
      }

      public void setOuname(String ouname) {
          super.set("ouname", ouname);
      }
}
