package com.epoint.xmz.job;
import java.util.Date;
import com.epoint.core.annotation.Entity;
import com.epoint.core.BaseEntity;
/**
* 实体类 
* 
* @作者 张彬彬 
* @version [版本号, 2022-12-05 09:51:41] 
*/

@Entity(table = "yq_area", id = "rowguid")
public class yqarea extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	* 获取
	*/
	public String getAreacode() { 
 		return super.getStr("areacode");
 	}
	/**
	* 设置
	*/
	public void setAreacode(String areacode) { 
 		super.set("areacode", areacode);
 	}
	/**
	* 获取
	*/
	public String getAreaname() { 
 		return super.getStr("areaname");
 	}
	/**
	* 设置
	*/
	public void setAreaname(String areaname) { 
 		super.set("areaname", areaname);
 	}
	/**
	* 获取
	*/
	public String getPid() { 
 		return super.getStr("pid");
 	}
	/**
	* 设置
	*/
	public void setPid(String pid) { 
 		super.set("pid", pid);
 	}
	/**
	* 获取
	*/
	public String getLevel() { 
 		return super.getStr("level");
 	}
	/**
	* 设置
	*/
	public void setLevel(String level) { 
 		super.set("level", level);
 	}
	/**
	* 获取
	*/
	public String getShortname() { 
 		return super.getStr("shortname");
 	}
	/**
	* 设置
	*/
	public void setShortname(String shortname) { 
 		super.set("shortname", shortname);
 	}
}