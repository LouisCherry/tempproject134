package com.epoint.hcp.api.entity;

import java.util.Date;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

/**
 * 全省各地区好差评情况表实体
 * 
 * @author jizhi7
 * @version [版本号, 2019-12-18 12:13:29]
 */
@Entity(table = "Audit_Hcp_AreaInfo", id = "rowguid")
public class AuditHcpAreainfo extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

	/**
	 * 辖区名称
	 */
	
	public String getAreaname() {
		return super.get("areaname");
	}

	public void setAreaname (String areaname) {
		super.set("areaname",areaname);
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
	 * 辖区编码 4位
	 */
	
	public String getAreacode() {
		return super.get("areacode");
	}

	public void setAreacode (String areacode) {
		super.set("areacode",areacode);
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
    
    /**
     * 整改率
     */
    
    public String getZgl() {
        return super.get("zgl");
    }

    public void setZgl (String zgl) {
        super.set("zgl",zgl);
    }
    
    /**
     * 差评整改数
     */
    public Integer getCpzgcount() {
        return super.getInt("cpzgcount");
    }

    public void setCpzgcount (Integer cpzgcount) {
        super.set("cpzgcount",cpzgcount);
    }
    
    /**
     * 非常满意数
     */
    public Integer getFcmynum() {
        return super.getInt("fcmynum");
    }

    public void setFcmynum (Integer fcmynum) {
        super.set("fcmynum",fcmynum);
    }

    /**
     * 满意数
     */
    public Integer getMynum() {
        return super.getInt("mynum");
    }

    public void setMynum (Integer mynum) {
        super.set("mynum",mynum);
    }

    /**
     * 基本满意数
     */
    public Integer getJbmynum() {
        return super.getInt("jbmynum");
    }

    public void setJbmynum (Integer jbmynum) {
        super.set("jbmynum",jbmynum);
    }

    /**
     * 不满意数
     */
    public Integer getBmynum() {
        return super.getInt("bmynum");
    }

    public void setBmynum (Integer bmynum) {
        super.set("bmynum",bmynum);
    }

    /**
     * 非常不满意数
     */
    public Integer getFcbmynum() {
        return super.getInt("fcbmynum");
    }

    public void setFcbmynum (Integer fcbmynum) {
        super.set("fcbmynum",fcbmynum);
    }
    
    /**
     * PC端数量
     */
    public Integer getPcnum() {
        return super.getInt("pcnum");
    }

    public void setPcnum (Integer pcnum) {
        super.set("pcnum",pcnum);
    }

    /**
     * 移动端数量
     */
    public Integer getYdnum() {
        return super.getInt("ydnum");
    }

    public void setYdnum (Integer ydnum) {
        super.set("ydnum",ydnum);
    }

    /**
     * Pad端数量
     */
    public Integer getPadnum() {
        return super.getInt("padnum");
    }

    public void setPadnum (Integer padnum) {
        super.set("padnum",padnum);
    }

    /**
     * 一体机数量
     */
    public Integer getYtjnum() {
        return super.getInt("ytjnum");
    }

    public void setYtjnum (Integer ytjnum) {
        super.set("ytjnum",ytjnum);
    }

}