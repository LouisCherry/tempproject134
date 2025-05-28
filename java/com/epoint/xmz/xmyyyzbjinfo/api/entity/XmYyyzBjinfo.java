package com.epoint.xmz.xmyyyzbjinfo.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 泰安一业一证对接办结信息表实体
 * 
 * @作者  LYA
 * @version [版本号, 2020-07-16 15:17:24]
 */
@Entity(table = "Xm_yyyz_bjinfo", id = "rowguid")
public class XmYyyzBjinfo extends BaseEntity implements Cloneable
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
    * 申报流水号
    */
    public String getFlowsn() {
        return super.get("flowsn");
    }

    public void setFlowsn(String flowsn) {
        super.set("flowsn", flowsn);
    }

    /**
    * 办结部门组织机构代码
    */
    public String getBjbmzzjddm() {
        return super.get("bjbmzzjddm");
    }

    public void setBjbmzzjddm(String bjbmzzjddm) {
        super.set("bjbmzzjddm", bjbmzzjddm);
    }

    /**
    * 办结结果描述
    */
    public String getBjjgms() {
        return super.get("bjjgms");
    }

    public void setBjjgms(String bjjgms) {
        super.set("bjjgms", bjjgms);
    }

    /**
    * 作废或退回原因
    */
    public String getZfhthyy() {
        return super.get("zfhthyy");
    }

    public void setZfhthyy(String zfhthyy) {
        super.set("zfhthyy", zfhthyy);
    }

    /**
    * 证件/盖章名称
    */
    public String getZjgzmc() {
        return super.get("zjgzmc");
    }

    public void setZjgzmc(String zjgzmc) {
        super.set("zjgzmc", zjgzmc);
    }

    /**
    * 证件编号
    */
    public String getZjbh() {
        return super.get("zjbh");
    }

    public void setZjbh(String zjbh) {
        super.set("zjbh", zjbh);
    }

    /**
    * 证件有效期限
    */
    public String getZjyxqx() {
        return super.get("zjyxqx");
    }

    public void setZjyxqx(String zjyxqx) {
        super.set("zjyxqx", zjyxqx);
    }

    /**
    * 发证/盖章单位
    */
    public String getFzgzdw() {
        return super.get("fzgzdw");
    }

    public void setFzgzdw(String fzgzdw) {
        super.set("fzgzdw", fzgzdw);
    }

    /**
    * 收费金额
    */
    public Double getSfje() {
        return super.getDouble("sfje");
    }

    public void setSfje(Double sfje) {
        super.set("sfje", sfje);
    }

    /**
    * 金额单位代码
    */
    public String getJedwdm() {
        return super.get("jedwdm");
    }

    public void setJedwdm(String jedwdm) {
        super.set("jedwdm", jedwdm);
    }

    /**
    * 备注
    */
    public String getBz() {
        return super.get("bz");
    }

    public void setBz(String bz) {
        super.set("bz", bz);
    }

    /**
    * 备用字段
    */
    public String getByzd() {
        return super.get("byzd");
    }

    public void setByzd(String byzd) {
        super.set("byzd", byzd);
    }
    
    /**
     * 办件标识
     */
     public String getProjectguid() {
         return super.get("projectguid");
     }

     public void setProjectguid(String projectguid) {
         super.set("projectguid", projectguid);
     }

}
