package com.epoint.zbxfdj.auditdocking.auditspsingle.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
 * 单体建筑信息表实体
 * 
 * @author WZW
 * @version [版本号, 2022-12-07 14:56:41]
 */
@Entity(table = "AUDIT_SP_SINGLE", id = "rowguid")
public class AuditSpSingle extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

    /**
     * 工程信息标识
     */
    public String getSpprojectguid() {
        return super.get("spprojectguid");
    }

    public void setSpprojectguid(String spprojectguid) {
        super.set("spprojectguid", spprojectguid);
    }

    /**
     * 建筑名称
     */
    public String getEngineeringname() {
        return super.get("engineeringname");
    }

    public void setEngineeringname(String engineeringname) {
        super.set("engineeringname", engineeringname);
    }

    /**
     * 结构类型
     */
    public String getStructuralsystemcode() {
        return super.get("structuralsystemcode");
    }

    public void setStructuralsystemcode(String structuralsystemcode) {
        super.set("structuralsystemcode", structuralsystemcode);
    }

    /**
     * 使用性质
     */
    public String getNature() {
        return super.get("nature");
    }

    public void setNature(String nature) {
        super.set("nature", nature);
    }

    /**
     * 耐火等级
     */
    public String getFireresisratecode() {
        return super.get("fireresisratecode");
    }

    public void setFireresisratecode(String fireresisratecode) {
        super.set("fireresisratecode", fireresisratecode);
    }

    /**
     * 地上层数
     */
    public Integer getLanduplayernumber() {
        return super.getInt("landuplayernumber");
    }

    public void setLanduplayernumber(Integer landuplayernumber) {
        super.set("landuplayernumber", landuplayernumber);
    }

    /**
     * 地下层数
     */
    public Integer getLanddownlayernumber() {
        return super.getInt("landdownlayernumber");
    }

    public void setLanddownlayernumber(Integer landdownlayernumber) {
        super.set("landdownlayernumber", landdownlayernumber);
    }

    /**
     * 高度（m）
     */
    public Double getBuildingheight() {
        return super.getDouble("buildingheight");
    }

    public void setBuildingheight(Double buildingheight) {
        super.set("buildingheight", buildingheight);
    }

    /**
     * 长度（m）
     */
    public Double getBuildinglength() {
        return super.getDouble("buildinglength");
    }

    public void setBuildinglength(Double buildinglength) {
        super.set("buildinglength", buildinglength);
    }

    /**
     * 占地面积（㎡）
     */
    public Double getFootprintarea() {
        return super.getDouble("footprintarea");
    }

    public void setFootprintarea(Double footprintarea) {
        super.set("footprintarea", footprintarea);
    }

    /**
     * 建筑面积（地上），单位：㎡
     */
    public Double getBuildinguparea() {
        return super.getDouble("buildinguparea");
    }

    public void setBuildinguparea(Double buildinguparea) {
        super.set("buildinguparea", buildinguparea);
    }

    /**
     * 建筑面积（地下），单位：㎡
     */
    public Double getBuildingdownarea() {
        return super.getDouble("buildingdownarea");
    }

    public void setBuildingdownarea(Double buildingdownarea) {
        super.set("buildingdownarea", buildingdownarea);
    }

    /**
     * 火宅危险性类别
     */
    public String getFiredangercategorycode() {
        return super.get("firedangercategorycode");
    }

    public void setFiredangercategorycode(String firedangercategorycode) {
        super.set("firedangercategorycode", firedangercategorycode);
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
     * 操作人所在独立部门Guid
     */
    public String getOperateuserbaseouguid() {
        return super.get("operateuserbaseouguid");
    }

    public void setOperateuserbaseouguid(String operateuserbaseouguid) {
        super.set("operateuserbaseouguid", operateuserbaseouguid);
    }

    /**
     * 所属辖区编号
     */
    public String getBelongxiaqucode() {
        return super.get("belongxiaqucode");
    }

    public void setBelongxiaqucode(String belongxiaqucode) {
        super.set("belongxiaqucode", belongxiaqucode);
    }

    /**
     * 主键guid
     */
    public String getRowguid() {
        return super.get("rowguid");
    }

    public void setRowguid(String rowguid) {
        super.set("rowguid", rowguid);
    }

    /**
     * 操作人所在部门Guid
     */
    public String getOperateuserouguid() {
        return super.get("operateuserouguid");
    }

    public void setOperateuserouguid(String operateuserouguid) {
        super.set("operateuserouguid", operateuserouguid);
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
     * 操作人姓名
     */
    public String getOperateusername() {
        return super.get("operateusername");
    }

    public void setOperateusername(String operateusername) {
        super.set("operateusername", operateusername);
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
     * 操作人Guid
     */
    public String getOperateuserguid() {
        return super.get("operateuserguid");
    }

    public void setOperateuserguid(String operateuserguid) {
        super.set("operateuserguid", operateuserguid);
    }

    /**
     * 流程标识
     */
    public String getPviguid() {
        return super.get("pviguid");
    }

    public void setPviguid(String pviguid) {
        super.set("pviguid", pviguid);
    }

    /**
     * 单体标识
     */
    public String getDantirowguid() {
        return super.get("dantirowguid");
    }

    public void setDantirowguid(String dantirowguid) {
        super.set("dantirowguid", dantirowguid);
    }
}
