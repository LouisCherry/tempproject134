package com.epoint.xmz.zjzcssp.api.entity;

import java.util.Date;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
import com.epoint.core.utils.EnumClazz.StrategyType;

/**
 * 邹城随手拍表实体
 * 
 * @作者 1
 * @version [版本号, 2020-10-10 11:34:40]
 */
@Entity(table = "zj_zcssp", id = "rowguid")
public class ZjZcssp extends BaseEntity implements Cloneable {
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
	 * 问题描述
	 */
	public String getContent() {
		return super.get("content");
	}

	public void setContent(String content) {
		super.set("content", content);
	}

	/**
	 * 上传附件
	 */
	public String getAttachguid() {
		return super.get("attachguid");
	}

	public void setAttachguid(String attachguid) {
		super.set("attachguid", attachguid);
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
	 * 用户姓名
	 */
	public String getUsername() {
		return super.get("username");
	}

	public void setUsername(String username) {
		super.set("username", username);
	}

	/**
	 * 手机号
	 */
	public String getPhone() {
		return super.get("phone");
	}

	public void setPhone(String phone) {
		super.set("phone", phone);
	}

	/**
	 * 地理位置
	 */
	public String getAddress() {
		return super.get("address");
	}

	public void setAddress(String address) {
		super.set("address", address);
	}

	/**
	 * 提出时间
	 */
	public Date getCreatetime() {
		return super.getDate("createtime");
	}

	public void setCreatetime(Date createtime) {
		super.set("createtime", createtime);
	}

}