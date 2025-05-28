package com.epoint.tongji.auditproject.domain;

import java.io.Serializable;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 
 *  办件按部门统计实体
 *  
 * @author zhangyu
 * @version [版本号, 2017年9月28日]
 
 
 */
@Entity(table = "statistics_project_byou", id = "rowguid")
public class JnAuditProjectTJ extends BaseEntity implements Cloneable, Serializable
{

    private static final long serialVersionUID = 1L;

    /**
     * 部门guid
     */
    public String getOuGuid() {
        return super.get("ouguid");
    }

    public void setOuGuid(String ouguid) {
        super.set("ouguid", ouguid);
    }

    /**
     * 部门名称
     */
    public String getOuName() {
        return super.get("ouname");
    }

    public void setOuName(String ouname) {
        super.set("ouname", ouname);
    }

    /**
     * 受理数
     */

    public String getShouLiNum() {
        return super.get("shoulinum");
    }

    public void setShouLiNum(String shoulinum) {
        super.set("shoulinum", shoulinum);
    }

    /**
     * 补正数
     */
    public String getBuZhengNum() {
        return super.get("buzhengnum");
    }

    public void setBuZhengNum(String buzhengnum) {
        super.set("buzhengnum", buzhengnum);
    }

    /**
     * 办结数
     */
    public String getBanJieNum() {
        return super.get("banjienum");
    }

    public void setBanJieNum(String banjienum) {
        super.set("banjienum", banjienum);
    }
    /**
     * 不予受理数
     */
    public String getBuShouNum() {
        return super.get("bushounum");
    }

    public void setBuShouNum(String bushounum) {
        super.set("bushounum", bushounum);
    }
    /**
     * 撤销申请数
     */
    public String getCheXiaoNum() {
        return super.get("chexiaonum");
    }

    public void setCheXiaoNum(String chexiaonum) {
        super.set("chexiaonum", chexiaonum);
    }
    /**
     * 异常办结数
     */
    public String getYiChangNum() {
        return super.get("yichangnum");
    }

    public void setYiChangNum(String yichangnum) {
        super.set("yichangnum", yichangnum);
    }
    
    public String getAreacode() {
        return super.get("areacode");
    }

    public void setAreacode(String areacode) {
        super.set("areacode", areacode);
    }
}
