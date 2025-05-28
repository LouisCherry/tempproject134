package com.epoint.domain;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 智能化一体机区域配置实体
 * 
 * @作者  54201
 * @version [版本号, 2019-09-27 11:16:41]
 */
@Entity(table = "Audit_Znsb_Selfmachineregion", id = "rowguid")
public class JnAuditZnsbSelfmachineregion extends BaseEntity implements Cloneable
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
    * 区域层级
    */
    public String getRegionlevel() {
        return super.get("regionlevel");
    }

    public void setRegionlevel(String level) {
        super.set("regionlevel", level);
    }

    /**
    * 父级guid
    */
    public String getParentguid() {
        return super.get("parentguid");
    }

    public void setParentguid(String parentguid) {
        super.set("parentguid", parentguid);
    }

    /**
    * 点击地址
    */
    public String getClickurl() {
        return super.get("clickurl");
    }

    public void setClickurl(String clickurl) {
        super.set("clickurl", clickurl);
    }

    /**
    * 图片地址
    */
    public String getPictureurl() {
        return super.get("pictureurl");
    }

    public void setPictureurl(String pictureurl) {
        super.set("pictureurl", pictureurl);
    }

    /**
    * 是否启用
    */
    public String getIsenable() {
        return super.get("isenable");
    }

    public void setIsenable(String isenable) {
        super.set("isenable", isenable);
    }

    /**
     * 是否启用
     */
    public String getRegionname() {
        return super.get("regionname");
    }

    public void setRegionname(String regionname) {
        super.set("regionname", regionname);
    }

    /**
     * 图片标识
     */
    public String getPicattachguid() {
        return super.get("picattachguid");
    }

    public void setPicattachguid(String picattachguid) {
        super.set("picattachguid", picattachguid);
    }

    /**
     * 辖区编码
     */
    public String getAreacode() {
        return super.get("areacode");
    }

    public void setAreacode(String areacode) {
        super.set("areacode", areacode);
    }

    /**
     * 生成时间
     */
    public Date getCreatedate() {
        return super.get("createdate");
    }

    public void setCreatedate(Date createdate) {
        super.set("createdate", createdate);
    }
    
    /**
     * 是否个性化
     */
    public String getIsindividuation() {
        return super.get("isindividuation");
    }

    public void setIsindividuation(String isindividuation) {
        super.set("isindividuation", isindividuation);
    }
    
    /**
     * 是否待更新
     */
    public String getIstobeupdated() {
        return super.get("istobeupdated");
    }

    public void setIstobeupdated(String istobeupdated) {
        super.set("istobeupdated", istobeupdated);
    }
    
    /**
     * 分辨率
     */
    public String getPixel() {
        return super.get("pixel");
    }

    public void setPixel(String pixel) {
        super.set("pixel", pixel);
    }
    
    /**
     * 排序号
     */
    public String getOrdernum() {
        return super.get("ordernum");
    }

    public void setOrdernum(String ordernum) {
        super.set("ordernum", ordernum);
    }

}
