package com.epoint.jn.inproject.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;

/**
* 实体类 
* 
* @作者 张彬彬 
* @version [版本号, 2020-05-26 14:45:32] 
*/

@Entity(table = "ea_jc_step_specialnode_gt", id = "rowguid")
public class eajcstepspecialnode extends BaseEntity {
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
	public String getSn() {
		return super.getStr("sn");
	}
	/**
	 * 设置
	 */
	public void setSn(String sn) {
		super.set("sn", sn);
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
	public void setNodename(String nodename) {
		super.set("nodename", nodename);
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
	public void setProcunitname(String procunitname) {
		super.set("procunitname", procunitname);
	}
	/**
	 * 获取
	 */
	public String getProcunitid() {
		return super.getStr("procunitid");
	}
	/**
	 * 设置
	 */
	public void setProcunitid(String procunitid) {
		super.set("procunitid", procunitid);
	}
	/**
	 * 获取
	 */
	public String getProcerid() {
		return super.getStr("procerid");
	}
	/**
	 * 设置
	 */
	public void setProcerid(String procerid) {
		super.set("procerid", procerid);
	}
	/**
	 * 获取
	 */
	public String getProcername() {
		return super.getStr("procername");
	}
	/**
	 * 设置
	 */
	public void setProcername(String procername) {
		super.set("procername", procername);
	}
	/**
	 * 获取
	 */
	public String getProcerremark() {
		return super.getStr("procerremark");
	}
	/**
	 * 设置
	 */
	public void setProcerremark(String procerremark) {
		super.set("procerremark", procerremark);
	}
	/**
	 * 获取
	 */
	public Date getNodestarttime() {
		return super.getDate("nodestarttime");
	}
	/**
	 * 设置
	 */
	public void setNodestarttime(Date nodestarttime) {
		super.set("nodestarttime", nodestarttime);
	}
	/**
	 * 获取
	 */
	public Date getNodeendtime() {
		return super.getDate("nodeendtime");
	}
	/**
	 * 设置
	 */
	public void setNodeendtime(Date nodeendtime) {
		super.set("nodeendtime", nodeendtime);
	}
	/**
	 * 获取
	 */
	public Date getNotetime() {
		return super.getDate("notetime");
	}
	/**
	 * 设置
	 */
	public void setNotetime(Date notetime) {
		super.set("notetime", notetime);
	}
	/**
	 * 获取
	 */
	public String getNodeprocadv() {
		return super.getStr("nodeprocadv");
	}
	/**
	 * 设置
	 */
	public void setNodeprocadv(String nodeprocadv) {
		super.set("nodeprocadv", nodeprocadv);
	}
	/**
	 * 获取
	 */
	public String getNodeprocaddr() {
		return super.getStr("nodeprocaddr");
	}
	/**
	 * 设置
	 */
	public void setNodeprocaddr(String nodeprocaddr) {
		super.set("nodeprocaddr", nodeprocaddr);
	}
	/**
	 * 获取
	 */
	public String getNodeprocaccord() {
		return super.getStr("nodeprocaccord");
	}
	/**
	 * 设置
	 */
	public void setNodeprocaccord(String nodeprocaccord) {
		super.set("nodeprocaccord", nodeprocaccord);
	}
	/**
	 * 获取
	 */
	public String getLists() {
		return super.getStr("lists");
	}
	/**
	 * 设置
	 */
	public void setLists(String lists) {
		super.set("lists", lists);
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
	public void setNoderesult(String noderesult) {
		super.set("noderesult", noderesult);
	}
	/**
	 * 获取
	 */
	public Date getNodetime() {
		return super.getDate("nodetime");
	}
	/**
	 * 设置
	 */
	public void setNodetime(Date nodetime) {
		super.set("nodetime", nodetime);
	}
	/**
	 * 获取
	 */
	public String getMaketime() {
		return super.getStr("maketime");
	}
	/**
	 * 设置
	 */
	public void setMaketime(String MAKETIME) {
		super.set("maketime", MAKETIME);
	}
	/**
	 * 获取
	 */
	public String getSignState() {
		return super.getStr("signState");
	}
	/**
	 * 设置
	 */
	public void setSignState(String signState) {
		super.set("signState", signState);
	}
	/**
	 * 获取
	 */
	public String getShengji_region() {
		return super.getStr("shengji_region");
	}
	/**
	 * 设置
	 */
	public void setShengji_region(String shengji_region) {
		super.set("shengji_region", shengji_region);
	}
	/**
	 * 获取
	 */
	public String getShiji_region() {
		return super.getStr("shiji_region");
	}
	/**
	 * 设置
	 */
	public void setShiji_region(String shiji_region) {
		super.set("shiji_region", shiji_region);
	}
	/**
	 * 获取
	 */
	public String getXianji_region() {
		return super.getStr("xianji_region");
	}
	/**
	 * 设置
	 */
	public void setXianji_region(String xianji_region) {
		super.set("xianji_region", xianji_region);
	}

}