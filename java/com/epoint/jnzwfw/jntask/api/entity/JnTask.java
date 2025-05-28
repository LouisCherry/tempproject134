package com.epoint.jnzwfw.jntask.api.entity;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
/**
* 实体类 
* 
* @作者 张彬彬 
* @version [版本号, 2020-03-08 14:47:06] 
*/

@Entity(table = "jn_task", id = "rowguid")
public class JnTask extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	* 获取
	*/
	public String getOuguid() { 
 		return super.getStr("ouguid");
 	}
	/**
	* 设置
	*/
	public void setOuguid(String ouguid) { 
 		super.set("ouguid", ouguid);
 	}
	/**
	* 获取
	*/
	public String getShenpilb() { 
 		return super.getStr("shenpilb");
 	}
	/**
	* 设置
	*/
	public void setShenpilb(String shenpilb) { 
 		super.set("shenpilb", shenpilb);
 	}
	/**
	* 获取
	*/
	public String getApplyertype() { 
 		return super.getStr("applyertype");
 	}
	/**
	* 设置
	*/
	public void setApplyertype(String applyertype) { 
 		super.set("applyertype", applyertype);
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
	public String getUse_level() { 
 		return super.getStr("use_level");
 	}
	/**
	* 设置
	*/
	public void setUse_level(String Use_level) { 
 		super.set("use_level", Use_level);
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
	public String getReservationmanagement() { 
 		return super.getStr("reservationmanagement");
 	}
	/**
	* 设置
	*/
	public void setReservationmanagement(String Reservationmanagement) { 
 		super.set("reservationmanagement", Reservationmanagement);
 	}
	/**
	* 获取
	*/
	public String getType() { 
 		return super.getStr("type");
 	}
	/**
	* 设置
	*/
	public void setType(String type) { 
 		super.set("type", type);
 	}
	/**
	* 获取
	*/
	public String getDao_xc_num() { 
 		return super.getStr("dao_xc_num");
 	}
	/**
	* 设置
	*/
	public void setDao_xc_num(String Dao_xc_num) { 
 		super.set("dao_xc_num", Dao_xc_num);
 	}
	/**
	* 获取
	*/
	public String getItem_id() { 
 		return super.getStr("item_id");
 	}
	/**
	* 设置
	*/
	public void setItem_id(String Item_id) { 
 		super.set("item_id", Item_id);
 	}
	/**
	* 获取
	*/
	public String getSupervise_tel() { 
 		return super.getStr("supervise_tel");
 	}
	/**
	* 设置
	*/
	public void setSupervise_tel(String Supervise_tel) { 
 		super.set("supervise_tel", Supervise_tel);
 	}
	/**
	* 获取
	*/
	public String getPromise_day() { 
 		return super.getStr("promise_day");
 	}
	/**
	* 设置
	*/
	public void setPromise_day(String Promise_day) { 
 		super.set("promise_day", Promise_day);
 	}
	/**
	* 获取
	*/
	public String getTaskguid() { 
 		return super.getStr("taskguid");
 	}
	/**
	* 设置
	*/
	public void setTaskguid(String taskguid) { 
 		super.set("taskguid", taskguid);
 	}
	/**
	* 获取
	*/
	public String getTask_id() { 
 		return super.getStr("task_id");
 	}
	/**
	* 设置
	*/
	public void setTask_id(String Task_id) { 
 		super.set("task_id", Task_id);
 	}
	/**
	* 获取
	*/
	public String getProjectform() { 
 		return super.getStr("projectform");
 	}
	/**
	* 设置
	*/
	public void setProjectform(String projectform) { 
 		super.set("projectform", projectform);
 	}
	/**
	* 获取
	*/
	public String getAnticipate_day() { 
 		return super.getStr("anticipate_day");
 	}
	/**
	* 设置
	*/
	public void setAnticipate_day(String Anticipate_day) { 
 		super.set("anticipate_day", Anticipate_day);
 	}
	/**
	* 获取
	*/
	public String getLink_tel() { 
 		return super.getStr("link_tel");
 	}
	/**
	* 设置
	*/
	public void setLink_tel(String Link_tel) { 
 		super.set("link_tel", Link_tel);
 	}
	/**
	* 获取
	*/
	public String getWangbanshendu() { 
 		return super.getStr("wangbanshendu");
 	}
	/**
	* 设置
	*/
	public void setWangbanshendu(String wangbanshendu) { 
 		super.set("wangbanshendu", wangbanshendu);
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
	public String getIswtshow() { 
 		return super.getStr("iswtshow");
 	}
	/**
	* 设置
	*/
	public void setIswtshow(String iswtshow) { 
 		super.set("iswtshow", iswtshow);
 	}
	/**
	* 获取
	*/
	public String getIstemplate() { 
 		return super.getStr("istemplate");
 	}
	/**
	* 设置
	*/
	public void setIstemplate(String ISTEMPLATE) { 
 		super.set("istemplate", ISTEMPLATE);
 	}
	/**
	* 获取
	*/
	public String getIs_enable() { 
 		return super.getStr("is_enable");
 	}
	/**
	* 设置
	*/
	public void setIs_enable(String IS_ENABLE) { 
 		super.set("is_enable", IS_ENABLE);
 	}
	/**
	* 获取
	*/
	public String getIs_history() { 
 		return super.getStr("is_history");
 	}
	/**
	* 设置
	*/
	public void setIs_history(String IS_HISTORY) { 
 		super.set("is_history", IS_HISTORY);
 	}
	/**
	* 获取
	*/
	public String getOrdernum() { 
 		return super.getStr("ordernum");
 	}
	/**
	* 设置
	*/
	public void setOrdernum(String ordernum) { 
 		super.set("ordernum", ordernum);
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
	public String getProcessguid() { 
 		return super.getStr("processguid");
 	}
	/**
	* 设置
	*/
	public void setProcessguid(String PROCESSGUID) { 
 		super.set("processguid", PROCESSGUID);
 	}
	/**
	* 获取
	*/
	public String getCharge_flag() { 
 		return super.getStr("charge_flag");
 	}
	/**
	* 设置
	*/
	public void setCharge_flag(String charge_flag) { 
 		super.set("charge_flag", charge_flag);
 	}
	/**
	* 获取
	*/
	public String getJbjmode() { 
 		return super.getStr("jbjmode");
 	}
	/**
	* 设置
	*/
	public void setJbjmode(String JBJMODE) { 
 		super.set("jbjmode", JBJMODE);
 	}
	/**
	* 获取
	*/
	public String getBy_law() { 
 		return super.getStr("by_law");
 	}
	/**
	* 设置
	*/
	public void setBy_law(String BY_LAW) { 
 		super.set("by_law", BY_LAW);
 	}
	/**
	* 获取
	*/
	public String getTransact_addr() { 
 		return super.getStr("transact_addr");
 	}
	/**
	* 设置
	*/
	public void setTransact_addr(String transact_addr) { 
 		super.set("transact_addr", transact_addr);
 	}
	/**
	* 获取
	*/
	public String getTransact_time() { 
 		return super.getStr("transact_time");
 	}
	/**
	* 设置
	*/
	public void setTransact_time(String Transact_time) { 
 		super.set("transact_time", Transact_time);
 	}
	/**
	* 获取
	*/
	public String getIf_entrust() { 
 		return super.getStr("if_entrust");
 	}
	/**
	* 设置
	*/
	public void setIf_entrust(String if_entrust) { 
 		super.set("if_entrust", if_entrust);
 	}
	/**
	* 获取
	*/
	public String getWebapplytype() { 
 		return super.getStr("webapplytype");
 	}
	/**
	* 获取
	*/
	public String getTaskclass_forpersion() { 
 		return super.getStr("taskclass_forpersion");
 	}
	/**
	* 设置
	*/
	public void setTaskclass_forpersion(String Taskclass_forpersion) { 
 		super.set("taskclass_forpersion", Taskclass_forpersion);
 	}
	/**
	* 获取
	*/
	public String getTaskclass_forcompany() { 
 		return super.getStr("taskclass_forcompany");
 	}
	/**
	* 设置
	*/
	public void setTaskclass_forcompany(String Taskclass_forcompany) { 
 		super.set("taskclass_forcompany", Taskclass_forcompany);
 	}
	
	/**
	* 获取
	*/
	public String getStatus() { 
 		return super.getStr("status");
 	}
	/**
	* 设置
	*/
	public void setStatus(String STATUS) { 
 		super.set("status", STATUS);
 	}
	/**
	* 获取
	*/
	public String getIsallowaccept() { 
 		return super.getStr("isallowaccept");
 	}
	/**
	* 设置
	*/
	public void setIsallowaccept(String ISALLOWACCEPT) { 
 		super.set("isallowaccept", ISALLOWACCEPT);
 	}
	/**
	* 获取
	*/
	public Integer getIs_editafterimport() { 
 		return super.getInt("is_editafterimport");
 	}
	/**
	* 设置
	*/
	public void setIs_editafterimport(Integer IS_EDITAFTERIMPORT) { 
 		super.set("is_editafterimport", IS_EDITAFTERIMPORT);
 	}
	/**
	* 设置
	*/
	public void setWebapplytype(String webapplytype) { 
 		super.set("webapplytype", webapplytype);
 	}
}