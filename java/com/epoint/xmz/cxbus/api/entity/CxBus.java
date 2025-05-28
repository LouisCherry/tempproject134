package com.epoint.xmz.cxbus.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 车辆信息表实体
 * 
 * @作者 1
 * @version [版本号, 2021-02-03 15:42:45]
 */
@Entity(table = "cx_bus", id = "rowguid")
public class CxBus extends BaseEntity implements Cloneable {
	private static final long serialVersionUID = 1L;

	/**
	 * 图片地址
	 */
	public String getDriveimg() {
		return super.get("driveimg");
	}

	public void setDriveimg(String driveimg) {
		super.set("driveimg", driveimg);
	}

	/**
	 * 重量
	 */
	public String getWeight() {
		return super.get("weight");
	}

	public void setWeight(String weight) {
		super.set("weight", weight);
	}

	/**
	 * 高度
	 */
	public String getHeight() {
		return super.get("height");
	}

	public void setHeight(String height) {
		super.set("height", height);
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
	 * 宽度
	 */
	public String getWidth() {
		return super.get("width");
	}

	public void setWidth(String width) {
		super.set("width", width);
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
	 * 长度
	 */
	public String getcxlength() {
		return super.get("cxlength");
	}

	public void setcxlength(String cxlength) {
		super.set("cxlength", cxlength);
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
	 * 模型
	 */
	public String getModel() {
		return super.get("model");
	}

	public void setModel(String model) {
		super.set("model", model);
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
	 * 车辆标识
	 */
	public String getVehicleid() {
		return super.get("vehicleid");
	}

	public void setVehicleid(String vehicleid) {
		super.set("vehicleid", vehicleid);
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
	 * 车辆种类
	 */
	public String getVehiclekind() {
		return super.get("vehiclekind");
	}

	public void setVehiclekind(String vehiclekind) {
		super.set("vehiclekind", vehiclekind);
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
	 * 车辆编号
	 */
	public String getVehicleno() {
		return super.get("vehicleno");
	}

	public void setVehicleno(String vehicleno) {
		super.set("vehicleno", vehicleno);
	}

	/**
	 * 标识码
	 */
	public String getGcclsbdm() {
		return super.get("gcclsbdm");
	}

	public void setGcclsbdm(String gcclsbdm) {
		super.set("gcclsbdm", gcclsbdm);
	}

	/**
	 * 轮胎数
	 */
	public String getAxles() {
		return super.get("axles");
	}

	public void setAxles(String axles) {
		super.set("axles", axles);
	}

	/**
	 * 轴数
	 */
	public String getTyles() {
		return super.get("tyles");
	}

	public void setTyles(String tyles) {
		super.set("tyles", tyles);
	}

	/**
	 * 轴距
	 */
	public String getAxes() {
		return super.get("axes");
	}

	public void setAxes(String axes) {
		super.set("axes", axes);
	}

}