package com.epoint.hcp.api.entity;
import java.util.Date;
import com.epoint.core.annotation.Entity;
import com.epoint.core.BaseEntity;
/**
* 实体类 
* 
* @作者 张彬彬 
* @version [版本号, 2020-05-26 14:45:48] 
*/

@Entity(table = "ea_jc_step_proc_gt", id = "rowguid")
public class eajcstepprocgt extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	* 获取
	*/
	public String getOrgbusno() { 
 		return super.getStr("orgbusno");
 	}
	/**
	* 设置
	*/
	public void setOrgbusno(String ORGBUSNO) { 
 		super.set("orgbusno", ORGBUSNO);
 	}
	/**
	* 获取
	*/
	public String getProjid() { 
 		return super.getStr("projid");
 	}
	/**
	* 设置
	*/
	public void setProjid(String PROJID) { 
 		super.set("projid", PROJID);
 	}
	/**
	* 获取
	*/
	public String getProject_code() { 
 		return super.getStr("project_code");
 	}
	/**
	* 设置
	*/
	public void setProject_code(String PROJECT_CODE) { 
 		super.set("project_code", PROJECT_CODE);
 	}
	/**
	* 获取
	*/
	public String getValidity_flag() { 
 		return super.getStr("validity_flag");
 	}
	/**
	* 设置
	*/
	public void setValidity_flag(String VALIDITY_FLAG) { 
 		super.set("validity_flag", VALIDITY_FLAG);
 	}
	/**
	* 获取
	*/
	public String getDataver() { 
 		return super.getStr("dataver");
 	}
	/**
	* 设置
	*/
	public void setDataver(String DATAVER) { 
 		super.set("dataver", DATAVER);
 	}
	/**
	* 获取
	*/
	public String getStdver() { 
 		return super.getStr("stdver");
 	}
	/**
	* 设置
	*/
	public void setStdver(String STDVER) { 
 		super.set("stdver", STDVER);
 	}
	/**
	* 获取
	*/
	public String getSn() { 
 		return super.getStr("sn");
 	}
	/**
	* 设置
	*/
	public void setSn(String SN) { 
 		super.set("sn", SN);
 	}
	/**
	* 获取
	*/
	public String getNodename() { 
 		return super.getStr("nodename");
 	}
	/**
	* 设置
	*/
	public void setNodename(String NODENAME) { 
 		super.set("nodename", NODENAME);
 	}
	/**
	* 获取
	*/
	public String getNodecode() { 
 		return super.getStr("nodecode");
 	}
	/**
	* 设置
	*/
	public void setNodecode(String NODECODE) { 
 		super.set("nodecode", NODECODE);
 	}
	/**
	* 获取
	*/
	public String getNodetype() { 
 		return super.getStr("nodetype");
 	}
	/**
	* 设置
	*/
	public void setNodetype(String NODETYPE) { 
 		super.set("nodetype", NODETYPE);
 	}
	/**
	* 获取
	*/
	public String getNodeprocer() { 
 		return super.getStr("nodeprocer");
 	}
	/**
	* 设置
	*/
	public void setNodeprocer(String NODEPROCER) { 
 		super.set("nodeprocer", NODEPROCER);
 	}
	/**
	* 获取
	*/
	public String getNodeprocername() { 
 		return super.getStr("nodeprocername");
 	}
	/**
	* 设置
	*/
	public void setNodeprocername(String NODEPROCERNAME) { 
 		super.set("nodeprocername", NODEPROCERNAME);
 	}
	/**
	* 获取
	*/
	public String getNodeprocerarea() { 
 		return super.getStr("nodeprocerarea");
 	}
	/**
	* 设置
	*/
	public void setNodeprocerarea(String NODEPROCERAREA) { 
 		super.set("nodeprocerarea", NODEPROCERAREA);
 	}
	/**
	* 获取
	*/
	public String getRegion_id() { 
 		return super.getStr("region_id");
 	}
	/**
	* 设置
	*/
	public void setRegion_id(String REGION_ID) { 
 		super.set("region_id", REGION_ID);
 	}
	/**
	* 获取
	*/
	public String getProcunit() { 
 		return super.getStr("procunit");
 	}
	/**
	* 设置
	*/
	public void setProcunit(String PROCUNIT) { 
 		super.set("procunit", PROCUNIT);
 	}
	/**
	* 获取
	*/
	public String getProcunitname() { 
 		return super.getStr("procunitname");
 	}
	/**
	* 设置
	*/
	public void setProcunitname(String PROCUNITNAME) { 
 		super.set("procunitname", PROCUNITNAME);
 	}
	/**
	* 获取
	*/
	public String getNodestate() { 
 		return super.getStr("nodestate");
 	}
	/**
	* 设置
	*/
	public void setNodestate(String NODESTATE) { 
 		super.set("nodestate", NODESTATE);
 	}
	/**
	* 获取
	*/
	public String getNodestarttime() { 
 		return super.getStr("nodestarttime");
 	}
	/**
	* 设置
	*/
	public void setNodestarttime(String NODESTARTTIME) { 
 		super.set("nodestarttime", NODESTARTTIME);
 	}
	/**
	* 获取
	*/
	public String getNodeendtime() { 
 		return super.getStr("nodeendtime");
 	}
	/**
	* 设置
	*/
	public void setNodeendtime(String NODEENDTIME) { 
 		super.set("nodeendtime", NODEENDTIME);
 	}
	/**
	* 获取
	*/
	public String getNodeadv() { 
 		return super.getStr("nodeadv");
 	}
	/**
	* 设置
	*/
	public void setNodeadv(String NODEADV) { 
 		super.set("nodeadv", NODEADV);
 	}
	/**
	* 获取
	*/
	public String getTimelimit() { 
 		return super.getStr("timelimit");
 	}
	/**
	* 设置
	*/
	public void setTimelimit(String TIMELIMIT) { 
 		super.set("timelimit", TIMELIMIT);
 	}
	/**
	* 获取
	*/
	public String getPromisetimeunit() { 
 		return super.getStr("promisetimeunit");
 	}
	/**
	* 设置
	*/
	public void setPromisetimeunit(String PROMISETIMEUNIT) { 
 		super.set("promisetimeunit", PROMISETIMEUNIT);
 	}
	/**
	* 获取
	*/
	public String getNoderesult() { 
 		return super.getStr("noderesult");
 	}
	/**
	* 设置
	*/
	public void setNoderesult(String NODERESULT) { 
 		super.set("noderesult", NODERESULT);
 	}
	/**
	* 获取
	*/
	public Date getOccurtime() { 
 		return super.getDate("occurtime");
 	}
	/**
	* 设置
	*/
	public void setOccurtime(Date OCCURTIME) { 
 		super.set("occurtime", OCCURTIME);
 	}
	/**
	* 获取
	*/
	public Date getMaketime() { 
 		return super.getDate("maketime");
 	}
	/**
	* 设置
	*/
	public void setMaketime(Date MAKETIME) { 
 		super.set("maketime", MAKETIME);
 	}
	/**
	* 获取
	*/
	public String getSignstate() { 
 		return super.getStr("signstate");
 	}
	/**
	* 设置
	*/
	public void setSignstate(String SIGNSTATE) { 
 		super.set("signstate", SIGNSTATE);
 	}
	/**
	* 获取
	*/
	public String getItemregionid() { 
 		return super.getStr("itemregionid");
 	}
	/**
	* 设置
	*/
	public void setItemregionid(String ITEMREGIONID) { 
 		super.set("itemregionid", ITEMREGIONID);
 	}
	/**
	* 获取
	*/
	public String getZtbzw() { 
 		return super.getStr("ztbzw");
 	}
	/**
	* 设置
	*/
	public void setZtbzw(String ZTBZW) { 
 		super.set("ztbzw", ZTBZW);
 	}
	/**
	* 获取
	*/
	public Date getExchangetime() { 
 		return super.getDate("exchangetime");
 	}
	/**
	* 设置
	*/
	public void setExchangetime(Date EXCHANGETIME) { 
 		super.set("exchangetime", EXCHANGETIME);
 	}
}