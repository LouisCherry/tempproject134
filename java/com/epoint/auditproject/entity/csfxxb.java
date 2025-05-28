package com.epoint.auditproject.entity;
import java.util.Date;
import com.epoint.core.annotation.Entity;
import com.epoint.core.BaseEntity;
/**
* 实体类 
* 
* @作者 张彬彬 
* @version [版本号, 2020-08-20 18:08:27] 
*/

@Entity(table = "csfxxb", id = "rowguid")
public class csfxxb extends BaseEntity {
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
	public String getXm() { 
 		return super.getStr("xm");
 	}
	/**
	* 设置
	*/
	public void setXm(String XM) { 
 		super.set("xm", XM);
 	}
	/**
	* 获取
	*/
	public String getZjhm() { 
 		return super.getStr("zjhm");
 	}
	/**
	* 设置
	*/
	public void setZjhm(String ZJHM) { 
 		super.set("zjhm", ZJHM);
 	}
	/**
	* 获取
	*/
	public String getZjlx() { 
 		return super.getStr("zjlx");
 	}
	/**
	* 设置
	*/
	public void setZjlx(String ZJLX) { 
 		super.set("zjlx", ZJLX);
 	}
	/**
	* 获取
	*/
	public Integer getXh() { 
 		return super.getInt("xh");
 	}
	/**
	* 设置
	*/
	public void setXh(Integer XH) { 
 		super.set("xh", XH);
 	}
	/**
	* 获取
	*/
	public String getMflx() { 
 		return super.getStr("mflx");
 	}
	/**
	* 设置
	*/
	public void setMflx(String MFLX) { 
 		super.set("mflx", MFLX);
 	}
	/**
	* 获取
	*/
	public String getMfgx() { 
 		return super.getStr("mfgx");
 	}
	/**
	* 设置
	*/
	public void setMfgx(String MFGX) { 
 		super.set("mfgx", MFGX);
 	}
	/**
	* 获取
	*/
	public String getGyqk() { 
 		return super.getStr("gyqk");
 	}
	/**
	* 设置
	*/
	public void setGyqk(String GYQK) { 
 		super.set("gyqk", GYQK);
 	}
	/**
	* 获取
	*/
	public String getGyfe() { 
 		return super.getStr("gyfe");
 	}
	/**
	* 设置
	*/
	public void setGyfe(String GYFE) { 
 		super.set("gyfe", GYFE);
 	}
	/**
	* 获取
	*/
	public String getLxdh() { 
 		return super.getStr("lxdh");
 	}
	/**
	* 设置
	*/
	public void setLxdh(String LXDH) { 
 		super.set("lxdh", LXDH);
 	}
	/**
	* 获取
	*/
	public String getZz() { 
 		return super.getStr("zz");
 	}
	/**
	* 设置
	*/
	public void setZz(String ZZ) { 
 		super.set("zz", ZZ);
 	}
	/**
	* 获取
	*/
	public String getHj() { 
 		return super.getStr("hj");
 	}
	/**
	* 设置
	*/
	public void setHj(String HJ) { 
 		super.set("hj", HJ);
 	}
	/**
	* 获取
	*/
	public String getXb() { 
 		return super.getStr("xb");
 	}
	/**
	* 设置
	*/
	public void setXb(String XB) { 
 		super.set("xb", XB);
 	}
	/**
	* 获取
	*/
	public Date getSr() { 
 		return super.getDate("sr");
 	}
	/**
	* 设置
	*/
	public void setSr(Date SR) { 
 		super.set("sr", SR);
 	}
}