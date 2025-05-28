package com.epoint.knowledge.common.domain;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 知识库类别实体
 * 
 * @作者  xuyunhai
 * @version [版本号, 2017-02-13 10:19:11]
 */
@Entity(table = "CNS_KINFO_CATEGORY", id = "rowguid")
public class CnsKinfoCategory extends BaseEntity implements Cloneable
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
    * 父类别标识
    */
    public String getParentguid() {
        return super.get("parentguid");
    }

    public void setParentguid(String parentguid) {
        super.set("parentguid", parentguid);
    }

 

    /**
    * 类别类型
    */
    public Integer getCategorytype() {
        return super.getInt("categorytype");
    }

    public void setCategorytype(Integer categorytype) {
        super.set("categorytype", categorytype);
    }

    /**
    * 类别编码
    */
    public String getCategorycode() {
        return super.get("categorycode");
    }

    public void setCategorycode(String categorycode) {
        super.set("categorycode", categorycode);
    }

    /**
    * 类别名称
    */
    public String getCategoryname() {
        return super.get("categoryname");
    }

    public void setCategoryname(String categoryname) {
        super.set("categoryname", categoryname);
    }

    /**
     * 类别名称缩写
     */
    public String getCategoryshortname() {
        return super.get("categoryshortname");
    }

    public void setCategoryshortname(String categoryshortname) {
        super.set("categoryshortname", categoryshortname);
    }

    /**
    * 排序号
    */
    public Integer getOrdernumber() {
        return super.getInt("ordernumber");
    }

    public void setOrdernumber(Integer ordernumber) {
        super.set("ordernumber", ordernumber);
    }

    /**
    * 所属辖区
    */
    public String getAreacode() {
        return super.get("areacode");
    }

    public void setAreacode(String areacode) {
        super.set("areacode", areacode);
    }

    /**
    * 所属部门
    */
    public String getOuguid() {
        return super.get("ouguid");
    }

    public void setOuguid(String ouguid) {
        super.set("ouguid", ouguid);
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
     * 是否是叶子节点
     */
     public String getIsleaf() {
         return super.get("isleaf");
     }

     public void setIsleaf(String isleaf) {
         super.set("isleaf", isleaf);
     }
     /**
      * 
      * 首字母拼音
      */
     public String getFirstpy(){
         return super.get("firstpy");
     }
     public void setFirstpy(String firstpy) {
         super.set("firstpy", firstpy);
     }
     /**
      * 
      *  全拼
      */
     public String getFullpy(){
         return super.get("fullpy");
     }
     public void setFulltpy(String fullpy) {
         super.set("fullpy", fullpy);
     }
     /**
      * 
      *  是否是部门根节点
      */
     public String getIsdepttop(){
         return super.get("isdepttop");
     }
     public void setIsdepttop(String isdepttop) {
         super.set("isdepttop", isdepttop);
     }
     
    
}
