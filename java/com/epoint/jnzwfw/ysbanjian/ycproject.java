package com.epoint.jnzwfw.ysbanjian;

import java.util.Date;
import com.epoint.core.annotation.Entity;
import com.epoint.core.BaseEntity;
/**
* 实体类 
* 
* @作者 张彬彬 
* @version [版本号, 2020-02-24 16:38:29] 
*/

@Entity(table = "yc_project", id = "rowguid")
public class ycproject extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	* 获取
	*/
	public String getTask_id() { 
 		return super.getStr("task_id");
 	}
	/**
	* 设置
	*/
	public void setTask_id(String task_id) { 
 		super.set("task_id", task_id);
 	}
	/**
	* 获取
	*/
	public String getTaskname() { 
 		return super.getStr("taskname");
 	}
	/**
	* 设置
	*/
	public void setTaskname(String taskname) { 
 		super.set("taskname", taskname);
 	}
	/**
	* 获取
	*/
	public String getOuname() { 
 		return super.getStr("ouname");
 	}
	/**
	* 设置
	*/
	public void setOuname(String ouname) { 
 		super.set("ouname", ouname);
 	}
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
	public String getProjectname() { 
 		return super.getStr("projectname");
 	}
	/**
	* 设置
	*/
	public void setProjectname(String projectname) { 
 		super.set("projectname", projectname);
 	}
	/**
	* 获取
	*/
	public String getApplyername() { 
 		return super.getStr("applyername");
 	}
	/**
	* 设置
	*/
	public void setApplyername(String applyername) { 
 		super.set("applyername", applyername);
 	}
	/**
	* 获取
	*/
	public Date getApplydate() { 
 		return super.getDate("applydate");
 	}
	/**
	* 设置
	*/
	public void setApplydate(Date applydate) { 
 		super.set("applydate", applydate);
 	}
}