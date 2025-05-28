package com.epoint.zoucheng.device.infopub.infopubmaterial.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 素材表实体
 * 
 * @作者  why
 * @version [版本号, 2019-09-16 19:57:24]
 */
@Entity(table = "InfoPub_Material", id = "rowguid")
public class InfopubMaterial extends BaseEntity implements Cloneable
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
    * 素材名称
    */
    public String getMaterialname() {
        return super.get("materialname");
    }

    public void setMaterialname(String materialname) {
        super.set("materialname", materialname);
    }

    /**
    * 素材分类
    */
    public String getMaterialtype() {
        return super.get("materialtype");
    }

    public void setMaterialtype(String materialtype) {
        super.set("materialtype", materialtype);
    }

    /**
    * 素材大小
    */
    public String getMaterialsize() {
        return super.get("materialsize");
    }

    public void setMaterialsize(String materialsize) {
        super.set("materialsize", materialsize);
    }

    /**
    * 素材虚拟地址
    */
    public String getVirtualpath() {
        return super.get("virtualpath");
    }

    public void setVirtualpath(String virtualpath) {
        super.set("virtualpath", virtualpath);
    }

    /**
    * 素材物理地址
    */
    public String getPhysicalpath() {
        return super.get("physicalpath");
    }

    public void setPhysicalpath(String physicalpath) {
        super.set("physicalpath", physicalpath);
    }

    /**
    * 上传时间
    */
    public Date getUploadtime() {
        return super.getDate("uploadtime");
    }

    public void setUploadtime(Date uploadtime) {
        super.set("uploadtime", uploadtime);
    }

    /**
    * 素材类型
    */
    public String getMaterialstyle() {
        return super.get("materialstyle");
    }

    public void setMaterialstyle(String materialstyle) {
        super.set("materialstyle", materialstyle);
    }

    /**
    * 排序值
    */
    public Integer getOrdernum() {
        return super.getInt("ordernum");
    }

    public void setOrdernum(Integer ordernum) {
        super.set("ordernum", ordernum);
    }

    /**
     * 轮播时间
     */
    public Integer getDelayTime() {
        return super.getInt("delaytime");
    }

    public void setDelayTime(Integer delaytime) {
        super.set("delaytime", delaytime);
    }
}
