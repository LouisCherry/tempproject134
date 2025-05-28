package com.epoint.guidang.auditprojectguidang.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 办件归档记录表实体
 * 
 * @作者  chengninghua
 * @version [版本号, 2018-01-05 14:04:07]
 */
@Entity(table = "auditprojectguidang", id = "rowguid")
public class Auditprojectguidang extends BaseEntity implements Cloneable
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
    * 办件Guid
    */
    public String getProjectguid() {
        return super.get("projectguid");
    }

    public void setProjectguid(String projectguid) {
        super.set("projectguid", projectguid);
    }

    /**
    * 归档时间
    */
    public Date getGuidangdate() {
        return super.getDate("guidangdate");
    }

    public void setGuidangdate(Date guidangdate) {
        super.set("guidangdate", guidangdate);
    }

    /**
    * 归档来源（1办结按钮，2手动）
    */
    public String getGuidangtype() {
        return super.get("guidangtype");
    }

    public void setGuidangtype(String guidangtype) {
        super.set("guidangtype", guidangtype);
    }

    /**
    * 办件编号
    */
    public String getFlowsn() {
        return super.get("flowsn");
    }

    public void setFlowsn(String flowsn) {
        super.set("flowsn", flowsn);
    }

     public String getFilename() {
         return super.get("filename");
     }

     public void setFilename(String filename) {
         super.set("filename", filename);
     }
     
      public String getFilepath() {
          return super.get("filepath");
      }

      public void setFilepath(String filepath) {
          super.set("filepath", filepath);
      }
      
       public String getAttachguid() {
           return super.get("attachguid");
       }

       public void setAttachguid(String attachguid) {
           super.set("attachguid", attachguid);
       }
}
