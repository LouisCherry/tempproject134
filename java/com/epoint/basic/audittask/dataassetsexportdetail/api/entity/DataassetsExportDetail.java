package com.epoint.basic.audittask.dataassetsexportdetail.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 导出详情实体
 * 
 * @作者  95453
 * @version [版本号, 2020-08-24 17:27:29]
 */
@Entity(table = "DATAASSETS_EXPORT_DETAIL", id = "rowguid")
public class DataassetsExportDetail extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

    /**
     * 导出标识
     */
    public String getExportguid() {
        return super.get("exportguid");
    }

    public void setExportguid(String exportguid) {
        super.set("exportguid", exportguid);
    }

    /**
    * 业务标识
    */
    public String getYewguid() {
        return super.get("yewguid");
    }

    public void setYewguid(String yewguid) {
        super.set("yewguid", yewguid);
    }

    /**
    * 业务名称
    */
    public String getYename() {
        return super.get("yename");
    }

    public void setYename(String yename) {
        super.set("yename", yename);
    }

    /**
    * 数据资产类型
    */
    public String getType() {
        return super.get("type");
    }

    public void setType(String type) {
        super.set("type", type);
    }

    /**
    * 导出状态
    */
    public String getStatus() {
        return super.get("status");
    }

    public void setStatus(String status) {
        super.set("status", status);
    }

    /**
    * 流程实例标识
    */
    public String getPviguid() {
        return super.get("pviguid");
    }

    public void setPviguid(String pviguid) {
        super.set("pviguid", pviguid);
    }

    /**
    * 操作人所属单位guid
    */
    public String getOperateuserbaseouguid() {
        return super.get("operateuserbaseouguid");
    }

    public void setOperateuserbaseouguid(String operateuserbaseouguid) {
        super.set("operateuserbaseouguid", operateuserbaseouguid);
    }

    /**
    * 操作人所属部门guid
    */
    public String getOperateuserouguid() {
        return super.get("operateuserouguid");
    }

    public void setOperateuserouguid(String operateuserouguid) {
        super.set("operateuserouguid", operateuserouguid);
    }

    /**
    * 操作人guid
    */
    public String getOperateuserguid() {
        return super.get("operateuserguid");
    }

    public void setOperateuserguid(String operateuserguid) {
        super.set("operateuserguid", operateuserguid);
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
     * 区分产品和项目
     */
     public String getProjecttype() {
         return super.get("projecttype");
     }

     public void setProjecttype(String projecttype) {
         super.set("projecttype", projecttype);
     }
     
     /**
      * 所属部门名称
      */
      public String getOuname() {
          return super.get("ouname");
      }

      public void setOuname(String ouname) {
          super.set("ouname", ouname);
      }
      
      /**
       * 所属部门唯一标识
       */
       public String getOuguid() {
           return super.get("ouguid");
       }

       public void setOuguid(String ouguid) {
           super.set("ouguid", ouguid);
       }
}
