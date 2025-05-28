package com.epoint.auditproject.entity;
import java.util.Date;
import com.epoint.core.annotation.Entity;
import com.epoint.core.BaseEntity;
/**
* 实体类 
* 
* @作者 张彬彬 
* @version [版本号, 2020-08-20 18:08:53] 
*/

@Entity(table = "jbxxb", id = "rowguid")
public class jbxxb extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	* 获取
	*/
	public String getLsh() { 
 		return super.getStr("lsh");
 	}
	/**
	* 设置
	*/
	public void setLsh(String LSH) { 
 		super.set("lsh", LSH);
 	}
	/**
	* 获取
	*/
	public String getYwlx() { 
 		return super.getStr("ywlx");
 	}
	/**
	* 设置
	*/
	public void setYwlx(String YWLX) { 
 		super.set("ywlx", YWLX);
 	}
	/**
	* 获取
	*/
	public String getZmj() { 
 		return super.get("zmj");
 	}
	/**
	* 设置
	*/
	public void setZmj(String ZMJ) { 
 		super.set("zmj", ZMJ);
 	}
	/**
	* 获取
	*/
	public String getFcjz() { 
 		return super.get("fcjz");
 	}
	/**
	* 设置
	*/
	public void setFcjz(String FCJZ) { 
 		super.set("fcjz", FCJZ);
 	}
	/**
	* 获取
	*/
	public String getFwzl() { 
 		return super.getStr("fwzl");
 	}
	/**
	* 设置
	*/
	public void setFwzl(String FWZL) { 
 		super.set("fwzl", FWZL);
 	}
	/**
	* 获取
	*/
	public String getCqrxm() { 
 		return super.getStr("cqrxm");
 	}
	/**
	* 设置
	*/
	public void setCqrxm(String CQRXM) { 
 		super.set("cqrxm", CQRXM);
 	}
	/**
	* 获取
	*/
	public String getFsfwmj() { 
 		return super.get("fsfwmj");
 	}
	/**
	* 设置
	*/
	public void setFsfwmj(String FSFWMJ) { 
 		super.set("fsfwmj", FSFWMJ);
 	}
}