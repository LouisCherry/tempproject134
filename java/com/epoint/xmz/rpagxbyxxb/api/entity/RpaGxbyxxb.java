package com.epoint.xmz.rpagxbyxxb.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 高校毕业信息表实体
 * 
 * @作者 1
 * @version [版本号, 2022-12-23 16:40:09]
 */
@Entity(table = "rpa_gxbyxxb", id = "rowguid")
public class RpaGxbyxxb extends BaseEntity implements Cloneable {
	private static final long serialVersionUID = 1L;

	/**
	 * 毕业院校
	 */
	public String getByyx() {
		return super.get("byyx");
	}

	public void setByyx(String byyx) {
		super.set("byyx", byyx);
	}

	/**
	 * 协议书编号
	 */
	public String getCertno() {
		return super.get("certno");
	}

	public void setCertno(String certno) {
		super.set("certno", certno);
	}

	/**
	 * 身份证号
	 */
	public String getCertnum() {
		return super.get("certnum");
	}

	public void setCertnum(String certnum) {
		super.set("certnum", certnum);
	}

	/**
	 * 档案接收情况
	 */
	public String getDajjqk() {
		return super.get("dajjqk");
	}

	public void setDajjqk(String dajjqk) {
		super.set("dajjqk", dajjqk);
	}

	/**
	 * 档案接收单位
	 */
	public String getDajsdw() {
		return super.get("dajsdw");
	}

	public void setDajsdw(String dajsdw) {
		super.set("dajsdw", dajsdw);
	}

	/**
	 * 单位隶属
	 */
	public String getDwls() {
		return super.get("dwls");
	}

	public void setDwls(String dwls) {
		super.set("dwls", dwls);
	}

	/**
	 * 单位名称
	 */
	public String getDwmc() {
		return super.get("dwmc");
	}

	public void setDwmc(String dwmc) {
		super.set("dwmc", dwmc);
	}

	/**
	 * 单位性质
	 */
	public String getDwxz() {
		return super.get("dwxz");
	}

	public void setDwxz(String dwxz) {
		super.set("dwxz", dwxz);
	}

	/**
	 * 单位注册所在地
	 */
	public String getDwzcszd() {
		return super.get("dwzcszd");
	}

	public void setDwzcszd(String dwzcszd) {
		super.set("dwzcszd", dwzcszd);
	}

	/**
	 * 姓名
	 */
	public String getName() {
		return super.get("name");
	}

	public void setName(String name) {
		super.set("name", name);
	}

	/**
	 * 拟聘职位
	 */
	public String getNpdw() {
		return super.get("npdw");
	}

	public void setNpdw(String npdw) {
		super.set("npdw", npdw);
	}

	/**
	 * 毕业年度
	 */
	public String getOveryear() {
		return super.get("overyear");
	}

	public void setOveryear(String overyear) {
		super.set("overyear", overyear);
	}

	/**
	 * 签约日期
	 */
	public Date getQyrq() {
		return super.getDate("qyrq");
	}

	public void setQyrq(Date qyrq) {
		super.set("qyrq", qyrq);
	}

	/**
	 * 审核时间
	 */
	public Date getShsj() {
		return super.getDate("shsj");
	}

	public void setShsj(Date shsj) {
		super.set("shsj", shsj);
	}

	/**
	 * 审核账号
	 */
	public String getShzh() {
		return super.get("shzh");
	}

	public void setShzh(String shzh) {
		super.set("shzh", shzh);
	}

	/**
	 * 审核状态
	 */
	public String getShzt() {
		return super.get("shzt");
	}

	public void setShzt(String shzt) {
		super.set("shzt", shzt);
	}

	/**
	 * 学历
	 */
	public String getXl() {
		return super.get("xl");
	}

	public void setXl(String xl) {
		super.set("xl", xl);
	}

	/**
	 * 专业
	 */
	public String getZy() {
		return super.get("zy");
	}

	public void setZy(String zy) {
		super.set("zy", zy);
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
	 * 年份标识
	 */
	public String getYearflag() {
		return super.get("yearflag");
	}

	public void setYearflag(String yearflag) {
		super.set("yearflag", yearflag);
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
	 * 操作日期
	 */
	public Date getOperatedate() {
		return super.getDate("operatedate");
	}

	public void setOperatedate(Date operatedate) {
		super.set("operatedate", operatedate);
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
	 * 所属辖区号
	 */
	public String getBelongxiaqucode() {
		return super.get("belongxiaqucode");
	}

	public void setBelongxiaqucode(String belongxiaqucode) {
		super.set("belongxiaqucode", belongxiaqucode);
	}

}