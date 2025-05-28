package com.epoint.basic.auditonlineuser.auditonlineconsult.domain;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 咨询扩展表，追问追答实体
 * 
 * @author yangjl
 * @version [版本号, 2017-04-19 09:24:27]
 */
@Entity(table = "AUDIT_DAIBAN_CONSULT_EXT", id = "rowguid")
public class AuditDaibanConsultExt extends BaseEntity implements Cloneable {
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
	 * 追问追答人名字
	 */
	public String getQausername() {
		return super.get("qausername");
	}

	public void setQausername(String qausername) {
		super.set("qausername", qausername);
	}

	/**
	 * 追问追答人guid
	 */
	public String getQauserguid() {
		return super.get("qauserguid");
	}

	public void setQauserguid(String qauserguid) {
		super.set("qauserguid", qauserguid);
	}

	/**
	 * 追问追答人登录guid
	 */
	public String getQaloginguid() {
		return super.get("qaloginguid");
	}

	public void setQaloginguid(String qaloginguid) {
		super.set("qaloginguid", qaloginguid);
	}

	/**
	 * 追问追答时间
	 */
	public Date getQadate() {
		return super.getDate("qadate");
	}

	public void setQadate(Date qadate) {
		super.set("qadate", qadate);
	}

	/**
	 * 状态 1.追问 2.追答
	 */
	public Integer getType() {
		return super.getInt("type");
	}

	public void setType(Integer type) {
		super.set("type", type);
	}

	/**
	 * Audit_Online_Consult表guid，咨询guid
	 */
	public String getConsultguid() {
		return super.get("consultguid");
	}

	public void setConsultguid(String consultguid) {
		super.set("consultguid", consultguid);
	}

	/**
	 * 追问追答内容
	 */
	public String getContent() {
		return super.get("content");
	}

	public void setContent(String content) {
		super.set("content", content);
	}

	/**
	 * 附件guid
	 */
	public String getClientguid() {
		return super.get("clientguid");
	}

	public void setClientguid(String clientguid) {
		super.set("clientguid", clientguid);
	}
	
	/**
     * 处理部门标识
     */
    public String getOuguid() {
        return super.get("ouguid");
    }

    public void setOuguid(String ouguid) {
        super.set("ouguid", ouguid);
    }


}