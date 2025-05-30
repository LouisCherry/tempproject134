package com.epoint.xmz.job;

import java.util.Date;
import com.epoint.core.annotation.Entity;
import com.epoint.core.BaseEntity;
/**
* 实体类 
* 
* @作者 张彬彬 
* @version [版本号, 2022-12-09 16:49:19] 
*/

@Entity(table = "yq_bm", id = "rowguid")
public class yqbm extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	* 获取
	*/
	public String getName() { 
 		return super.getStr("name");
 	}
	/**
	* 设置
	*/
	public void setName(String name) { 
 		super.set("name", name);
 	}
	/**
	* 获取
	*/
	public String getValue() { 
 		return super.getStr("value");
 	}
	/**
	* 设置
	*/
	public void setValue(String value) { 
 		super.set("value", value);
 	}
}