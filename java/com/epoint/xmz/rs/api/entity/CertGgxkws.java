package com.epoint.xmz.rs.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 公共许可卫生证照库实体
 * 
 * @作者 1
 * @version [版本号, 2022-04-12 17:01:05]
 */
@Entity(table = "cert_ggxkws", id = "rowguid")
public class CertGgxkws extends BaseEntity implements Cloneable {
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
	 * 被监督单位
	 */
	public String getMonitorname() {
		return super.get("monitorname");
	}

	public void setMonitorname(String monitorname) {
		super.set("monitorname", monitorname);
	}

	/**
	 * 所属区县
	 */
	public String getAreacode() {
		return super.get("areacode");
	}

	public void setAreacode(String areacode) {
		super.set("areacode", areacode);
	}

	/**
	 * 经营地址
	 */
	public String getManageaddress() {
		return super.get("manageaddress");
	}

	public void setManageaddress(String manageaddress) {
		super.set("manageaddress", manageaddress);
	}

	/**
	 * 注册地址
	 */
	public String getRegisteraddress() {
		return super.get("registeraddress");
	}

	public void setRegisteraddress(String registeraddress) {
		super.set("registeraddress", registeraddress);
	}

	/**
	 * 社会信用代码
	 */
	public String getCreditcode() {
		return super.get("creditcode");
	}

	public void setCreditcode(String creditcode) {
		super.set("creditcode", creditcode);
	}

	/**
	 * 经济类型
	 */
	public String getEcontype() {
		return super.get("econtype");
	}

	public void setEcontype(String econtype) {
		super.set("econtype", econtype);
	}

	/**
	 * 法定代表人
	 */
	public String getLegal() {
		return super.get("legal");
	}

	public void setLegal(String legal) {
		super.set("legal", legal);
	}

	/**
	 * 证件名称
	 */
	public String getCerttype() {
		return super.get("certtype");
	}

	public void setCerttype(String certtype) {
		super.set("certtype", certtype);
	}

	/**
	 * 证件号码
	 */
	public String getCertnum() {
		return super.get("certnum");
	}

	public void setCertnum(String certnum) {
		super.set("certnum", certnum);
	}

	/**
	 * 专业类别
	 */
	public String getMajortype() {
		return super.get("majortype");
	}

	public void setMajortype(String majortype) {
		super.set("majortype", majortype);
	}

	/**
	 * 专业子类别
	 */
	public String getMajorchildtype() {
		return super.get("majorchildtype");
	}

	public void setMajorchildtype(String majorchildtype) {
		super.set("majorchildtype", majorchildtype);
	}

	/**
	 * 职工总数
	 */
	public String getWorkertotal() {
		return super.get("workertotal");
	}

	public void setWorkertotal(String workertotal) {
		super.set("workertotal", workertotal);
	}

	/**
	 * 从业人员数
	 */
	public String getPracticenum() {
		return super.get("practicenum");
	}

	public void setPracticenum(String practicenum) {
		super.set("practicenum", practicenum);
	}

	/**
	 * 持健康合格证明人数
	 */
	public String getQuailynum() {
		return super.get("quailynum");
	}

	public void setQuailynum(String quailynum) {
		super.set("quailynum", quailynum);
	}

	/**
	 * 营业面积
	 */
	public String getSellarea() {
		return super.get("sellarea");
	}

	public void setSellarea(String sellarea) {
		super.set("sellarea", sellarea);
	}

	/**
	 * 集中空调通风系统
	 */
	public String getIshvac() {
		return super.get("ishvac");
	}

	public void setIshvac(String ishvac) {
		super.set("ishvac", ishvac);
	}

	/**
	 * 饮用水
	 */
	public String getWatertype() {
		return super.get("watertype");
	}

	public void setWatertype(String watertype) {
		super.set("watertype", watertype);
	}

	/**
	 * 许可证号
	 */
	public String getCertno() {
		return super.get("certno");
	}

	public void setCertno(String certno) {
		super.set("certno", certno);
	}

	/**
	 * 申请类别
	 */
	public String getApplytype() {
		return super.get("applytype");
	}

	public void setApplytype(String applytype) {
		super.set("applytype", applytype);
	}

	/**
	 * 许可证开始日期
	 */
	public Date getBegintime() {
		return super.getDate("begintime");
	}

	public void setBegintime(Date begintime) {
		super.set("begintime", begintime);
	}

	/**
	 * 许可证截止日期
	 */
	public Date getEndtime() {
		return super.getDate("endtime");
	}

	public void setEndtime(Date endtime) {
		super.set("endtime", endtime);
	}

	/**
	 * 行政许可机关标识
	 */
	public String getOuguid() {
		return super.get("ouguid");
	}

	public void setOuguid(String ouguid) {
		super.set("ouguid", ouguid);
	}

	/**
	 * 行政许可机关名称
	 */
	public String getOuname() {
		return super.get("ouname");
	}

	public void setOuname(String ouname) {
		super.set("ouname", ouname);
	}

	/**
	 * 版本
	 */
	public String getVersion() {
		return super.get("version");
	}

	public void setVersion(String version) {
		super.set("version", version);
	}

	/**
	 * 是否在用
	 */
	public String getIs_enable() {
		return super.get("is_enable");
	}

	public void setIs_enable(String is_enable) {
		super.set("is_enable", is_enable);
	}

	/**
	 * 是否注销
	 */
	public String getIs_canale() {
		return super.get("is_canale");
	}

	public void setIs_canale(String is_canale) {
		super.set("is_canale", is_canale);
	}

}