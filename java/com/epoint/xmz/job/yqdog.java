package com.epoint.xmz.job;
import java.util.Date;
import com.epoint.core.annotation.Entity;
import com.epoint.core.BaseEntity;
/**
* 实体类 
* 
* @作者 张彬彬 
* @version [版本号, 2022-12-05 11:10:29] 
*/

@Entity(table = "yq_dog", id = "rowguid")
public class yqdog extends BaseEntity {
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
	public String getRowguid() { 
 		return super.getStr("rowguid");
 	}
	/**
	* 设置
	*/
	public void setRowguid(String rowguid) { 
 		super.set("rowguid", rowguid);
 	}
	/**
	* 获取
	*/
	public String getId() { 
 		return super.getStr("id");
 	}
	/**
	* 设置
	*/
	public void setId(String id) { 
 		super.set("id", id);
 	}
}