package com.epoint.hcp.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 省级部门好差评情况表实体
 * 
 * @author jizhi7
 * @version [版本号, 2019-12-18 12:10:53]
 */
@Entity(table = "Audit_Hcp_OUInfo", id = "rowguid")
public class AuditHcpOuinfo extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

	/**
	 * 部门名称
	 */
	
	public String getOuname() {
		return super.get("ouname");
	}

	public void setOuname (String ouname) {
		super.set("ouname",ouname);
	}

	/**
	 * 流程实例标识
	 */
	
	public String getPviguid() {
		return super.get("pviguid");
	}

	public void setPviguid (String pviguid) {
		super.set("pviguid",pviguid);
	}

	/**
	 * 操作人所属单位guid
	 */
	
	public String getOperateuserbaseouguid() {
		return super.get("operateuserbaseouguid");
	}

	public void setOperateuserbaseouguid (String operateuserbaseouguid) {
		super.set("operateuserbaseouguid",operateuserbaseouguid);
	}

	/**
	 * 操作人所属部门guid
	 */
	
	public String getOperateuserouguid() {
		return super.get("operateuserouguid");
	}

	public void setOperateuserouguid (String operateuserouguid) {
		super.set("operateuserouguid",operateuserouguid);
	}

	/**
	 * 操作人guid
	 */
	
	public String getOperateuserguid() {
		return super.get("operateuserguid");
	}

	public void setOperateuserguid (String operateuserguid) {
		super.set("operateuserguid",operateuserguid);
	}

	/**
	 * 所属辖区号
	 */
	
	public String getBelongxiaqucode() {
		return super.get("belongxiaqucode");
	}

	public void setBelongxiaqucode (String belongxiaqucode) {
		super.set("belongxiaqucode",belongxiaqucode);
	}

	/**
	 * 操作者名字
	 */
	
	public String getOperateusername() {
		return super.get("operateusername");
	}

	public void setOperateusername (String operateusername) {
		super.set("operateusername",operateusername);
	}

	/**
	 * 操作日期
	 */
	public Date getOperatedate() {
		return super.getDate("operatedate");
	}

	public void setOperatedate (Date operatedate) {
		super.set("operatedate",operatedate);
	}

	/**
	 * 序号
	 */
	public Integer getRow_id() {
		return super.getInt("row_id");
	}

	public void setRow_id (Integer row_id) {
		super.set("row_id",row_id);
	}

	/**
	 * 年份标识
	 */
	public String getYearflag() {
		return super.get("yearflag");
	}

	public void setYearflag (String yearflag) {
		super.set("yearflag",yearflag);
	}

	/**
	 * 默认主键字段
	 */
	
	public String getRowguid() {
		return super.get("rowguid");
	}

	public void setRowguid (String rowguid) {
		super.set("rowguid",rowguid);
	}

	/**
	 * 部门标识
	 */
	
	public String getOucode() {
		return super.get("oucode");
	}

	public void setOucode (String oucode) {
		super.set("oucode",oucode);
	}

	/**
	 * 办件数
	 */
	public Integer getBjcount() {
		return super.getInt("bjcount");
	}

	public void setBjcount (Integer bjcount) {
		super.set("bjcount",bjcount);
	}

	/**
	 * 评价数
	 */
	public Integer getPjcount() {
		return super.getInt("pjcount");
	}

	public void setPjcount (Integer pjcount) {
		super.set("pjcount",pjcount);
	}

	/**
	 * 满意度
	 */
	
	public String getMyd() {
		return super.get("myd");
	}

	public void setMyd (String myd) {
		super.set("myd",myd);
	}

	/**
     * 差评数
     */
    public Integer getCpcount() {
        return super.getInt("cpcount");
    }

    public void setCpcount (Integer cpcount) {
        super.set("cpcount",cpcount);
    }
    
    /**
     * 满意数
     */
    public Integer getMycount() {
        return super.getInt("mycount");
    }
    
    public void setMycount (Integer mycount) {
        super.set("mycount",mycount);
    }
}