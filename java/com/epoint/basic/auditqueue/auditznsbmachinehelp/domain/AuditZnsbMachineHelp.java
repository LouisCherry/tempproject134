package com.epoint.basic.auditqueue.auditznsbmachinehelp.domain;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 实例
 * 
 * @author Administrator
 * @version [版本号, 2017-05-01 13:50:10]
 */
@Entity(table = "audit_znsb_selfmachinehelp", id = "rowguid")
public class AuditZnsbMachineHelp extends BaseEntity implements Cloneable
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
	 * 一体机guid
	 */
	public String getMachineguid() {
		return super.get("machineguid");
	}

	public void setMachineguid(String machineguid) {
		super.set("machineguid", machineguid);
	}
	
	/**
     * 发送日期
     */
    public Date getSenddate() {
        return super.getDate("senddate");
    }

    public void setSenddate(Date Senddate) {
        super.set("senddate", Senddate);
    }
    
	/**
	 * 状态
	 */
	public String getCurrentstatus() {
		return super.get("currentstatus");
	}

	public void setCurrentstatus(String currentstatus) {
		super.set("currentstatus", currentstatus);
	}


	/**
     * 处理人员guid
     */
    public String getUserguid() {
        return super.get("userguid");
    }

    public void setUserguid(String userguid) {
        super.set("userguid", userguid);
    }
    
    /**
     * 发送日期
     */
    public Date getAcceptdate() {
        return super.getDate("acceptdate");
    }

    public void setAcceptdate(Date acceptdate) {
        super.set("acceptdate", acceptdate);
    }
	
}
