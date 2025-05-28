package com.epoint.zoucheng.znsb.auditznsbselfmachinemodule.domain;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 自助服务一体机模块配置实体
 * 
 * @作者  54201
 * @version [版本号, 2018-06-14 15:50:54]
 */
@Entity(table = "Audit_Znsb_SelfmachineModule", id = "rowguid")
public class ZCAuditZnsbSelfmachinemodule extends BaseEntity implements Cloneable
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
    * 模块名称
    */
    public String getModulename() {
        return super.get("modulename");
    }

    public void setModulename(String modulename) {
        super.set("modulename", modulename);
    }

    /**
    * 模块类型
    */
    public String getModuletype() {
        return super.get("moduletype");
    }

    public void setModuletype(String moduletype) {
        super.set("moduletype", moduletype);
    }

    /**
    * 中心guid
    */
    public String getCenterguid() {
        return super.get("centerguid");
    }

    public void setCenterguid(String centerguid) {
        super.set("centerguid", centerguid);
    }

    /**
    * 图片路径
    */
    public String getPicturepath() {
        return super.get("picturepath");
    }

    public void setPicturepath(String picturepath) {
        super.set("picturepath", picturepath);
    }

    /**
    * 是否需要登录
    */
    public String getIsneedlogin() {
        return super.get("isneedlogin");
    }

    public void setIsneedlogin(String isneedlogin) {
        super.set("isneedlogin", isneedlogin);
    }

    /**
    * 页面路径
    */
    public String getHtmlurl() {
        return super.get("htmlurl");
    }

    public void setHtmlurl(String htmlurl) {
        super.set("htmlurl", htmlurl);
    }

    /**
     * 是否启用
     */
    public String getEnable() {
        return super.get("enable");
    }

    public void setEnable(String enable) {
        super.set("enable", enable);
    }

    /**
     * 序号
     */
    public Integer getOrdernum() {
        return super.getInt("ordernum");
    }

    public void setOrdernum(Integer ordernum) {
        super.set("ordernum", ordernum);
    }

    /**
     * Mac地址
     */
    public String getMacaddress() {
        return super.get("macaddress");
    }

    public void setMacaddress(String macaddress) {
        super.set("macaddress", macaddress);
    }

    /**
    * 图标路径
    */
    public String getIconpath() {
        return super.get("iconpath");
    }

    public void setIconpath(String iconpath) {
        super.set("iconpath", iconpath);
    }
	
	 /**
     * 热门排序
     */
    public Integer getHotnum() {
        return super.getInt("hotnum");
    }

    public void setHotnum(Integer hotnum) {
        super.set("hotnum", hotnum);
    }

    /**
     * 父模块guid
     */
    public String getParentmoduleguid() {
        return super.get("parentmoduleguid");
    }

    public void setParentmoduleguid(String parentmoduleguid) {
        super.set("parentmoduleguid", parentmoduleguid);
    }
    
    /**
     * 层级标识
     */
    public String getModulecodelevel() {
        return super.get("modulecodelevel");
    }

    public void setModulecodelevel(String modulecodelevel) {
        super.set("modulecodelevel", modulecodelevel);
    }
    
    /**
     * 模块配置类型
     */
    public String getModuleconfigtype() {
        return super.get("moduleconfigtype");
    }

    public void setModuleconfigtype(String moduleconfigtype) {
        super.set("moduleconfigtype", moduleconfigtype);
    }
    
    /**
     * 图片guid
     */
    public String getPictureguid() {
        return super.get("pictureguid");
    }

    public void setPictureguid(String pictureguid) {
        super.set("pictureguid", pictureguid);
    }
    
    /**
     * 特殊标签
     */
    public String getSpeciallabel() {
        return super.get("speciallabel");
    }

    public void setSpeciallabel(String speciallabel) {
        super.set("speciallabel", speciallabel);
    }
    
    /**
     * 行数
     */
    public String getLinenum() {
        return super.get("linenum");
    }

    public void setLinenum(String linenum) {
        super.set("linenum", linenum);
    }
    
    /**
     * 是否是父模块
     */
     public String getIsParentmodule() {
         return super.get("isparentmodule");
     }

     public void setIsParentmodule(String isparentmodule) {
         super.set("isparentmodule", isparentmodule);
     }
}
