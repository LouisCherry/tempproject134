package com.epoint.hcp.api.entity;
import java.util.Date;
import com.epoint.core.annotation.Entity;
import com.epoint.core.BaseEntity;
/**
* 实体类 
* 
* @作者 张彬彬 
* @version [版本号, 2021-02-26 11:32:39] 
*/

@Entity(table = "lc_project_five", id = "rowguid")
public class lcprojectfive extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	* 获取
	*/
	public String getBelongxiaqucode() { 
 		return super.getStr("belongxiaqucode");
 	}
	/**
	* 设置
	*/
	public void setBelongxiaqucode(String BelongXiaQuCode) { 
 		super.set("belongxiaqucode", BelongXiaQuCode);
 	}
	/**
	* 获取
	*/
	public String getOperateusername() { 
 		return super.getStr("operateusername");
 	}
	/**
	* 设置
	*/
	public void setOperateusername(String OperateUserName) { 
 		super.set("operateusername", OperateUserName);
 	}
	/**
	* 获取
	*/
	public Date getOperatedate() { 
 		return super.getDate("operatedate");
 	}
	/**
	* 设置
	*/
	public void setOperatedate(Date OperateDate) { 
 		super.set("operatedate", OperateDate);
 	}
	/**
	* 获取
	*/
	public Integer getRow_id() { 
 		return super.getInt("row_id");
 	}
	/**
	* 设置
	*/
	public void setRow_id(Integer ROW_ID) { 
 		super.set("row_id", ROW_ID);
 	}
	/**
	* 获取
	*/
	public String getYearflag() { 
 		return super.getStr("yearflag");
 	}
	/**
	* 设置
	*/
	public void setYearflag(String YearFlag) { 
 		super.set("yearflag", YearFlag);
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
	public void setRowguid(String RowGuid) { 
 		super.set("rowguid", RowGuid);
 	}
	/**
	* 获取
	*/
	public String getYusheuserguid() { 
 		return super.getStr("yusheuserguid");
 	}
	/**
	* 设置
	*/
	public void setYusheuserguid(String YUSHEUSERGUID) { 
 		super.set("yusheuserguid", YUSHEUSERGUID);
 	}
	/**
	* 获取
	*/
	public String getContactpostcode() { 
 		return super.getStr("contactpostcode");
 	}
	/**
	* 设置
	*/
	public void setContactpostcode(String CONTACTPOSTCODE) { 
 		super.set("contactpostcode", CONTACTPOSTCODE);
 	}
	/**
	* 获取
	*/
	public String getBillno() { 
 		return super.getStr("billno");
 	}
	/**
	* 设置
	*/
	public void setBillno(String BILLNO) { 
 		super.set("billno", BILLNO);
 	}
	/**
	* 获取
	*/
	public String getZijianxitong_number() { 
 		return super.getStr("zijianxitong_number");
 	}
	/**
	* 设置
	*/
	public void setZijianxitong_number(String ZIJIANXITONG_NUMBER) { 
 		super.set("zijianxitong_number", ZIJIANXITONG_NUMBER);
 	}
	/**
	* 获取
	*/
	public String getOrg_id() { 
 		return super.getStr("org_id");
 	}
	/**
	* 设置
	*/
	public void setOrg_id(String ORG_ID) { 
 		super.set("org_id", ORG_ID);
 	}
	/**
	* 获取
	*/
	public Integer getIs_test() { 
 		return super.getInt("is_test");
 	}
	/**
	* 设置
	*/
	public void setIs_test(Integer IS_TEST) { 
 		super.set("is_test", IS_TEST);
 	}
	/**
	* 获取
	*/
	public String getIssign() { 
 		return super.getStr("issign");
 	}
	/**
	* 设置
	*/
	public void setIssign(String ISSIGN) { 
 		super.set("issign", ISSIGN);
 	}
	/**
	* 获取
	*/
	public String getBiguid() { 
 		return super.getStr("biguid");
 	}
	/**
	* 设置
	*/
	public void setBiguid(String BIGUID) { 
 		super.set("biguid", BIGUID);
 	}
	/**
	* 获取
	*/
	public String getBusinessguid() { 
 		return super.getStr("businessguid");
 	}
	/**
	* 设置
	*/
	public void setBusinessguid(String BUSINESSGUID) { 
 		super.set("businessguid", BUSINESSGUID);
 	}
	/**
	* 获取
	*/
	public String getAttach_sign() { 
 		return super.getStr("attach_sign");
 	}
	/**
	* 设置
	*/
	public void setAttach_sign(String ATTACH_SIGN) { 
 		super.set("attach_sign", ATTACH_SIGN);
 	}
	/**
	* 获取
	*/
	public String getApply_attach_sign() { 
 		return super.getStr("apply_attach_sign");
 	}
	/**
	* 设置
	*/
	public void setApply_attach_sign(String APPLY_ATTACH_SIGN) { 
 		super.set("apply_attach_sign", APPLY_ATTACH_SIGN);
 	}
	/**
	* 获取
	*/
	public Date getYushendate() { 
 		return super.getDate("yushendate");
 	}
	/**
	* 设置
	*/
	public void setYushendate(Date YUSHENDATE) { 
 		super.set("yushendate", YUSHENDATE);
 	}
	/**
	* 获取
	*/
	public Date getBubandate() { 
 		return super.getDate("bubandate");
 	}
	/**
	* 设置
	*/
	public void setBubandate(Date BUBANDATE) { 
 		super.set("bubandate", BUBANDATE);
 	}
	/**
	* 获取
	*/
	public String getRemark() { 
 		return super.getStr("remark");
 	}
	/**
	* 设置
	*/
	public void setRemark(String REMARK) { 
 		super.set("remark", REMARK);
 	}
	/**
	* 获取
	*/
	public String getLegal() { 
 		return super.getStr("legal");
 	}
	/**
	* 设置
	*/
	public void setLegal(String LEGAL) { 
 		super.set("legal", LEGAL);
 	}
	/**
	* 获取
	*/
	public Integer getIs_cert() { 
 		return super.getInt("is_cert");
 	}
	/**
	* 设置
	*/
	public void setIs_cert(Integer IS_CERT) { 
 		super.set("is_cert", IS_CERT);
 	}
	/**
	* 获取
	*/
	public Integer getHebingshoulishuliang() { 
 		return super.getInt("hebingshoulishuliang");
 	}
	/**
	* 设置
	*/
	public void setHebingshoulishuliang(Integer HEBINGSHOULISHULIANG) { 
 		super.set("hebingshoulishuliang", HEBINGSHOULISHULIANG);
 	}
	/**
	* 获取
	*/
	public String getCheckuserguid() { 
 		return super.getStr("checkuserguid");
 	}
	/**
	* 设置
	*/
	public void setCheckuserguid(String CHECKUSERGUID) { 
 		super.set("checkuserguid", CHECKUSERGUID);
 	}
	/**
	* 获取
	*/
	public String getAddress() { 
 		return super.getStr("address");
 	}
	/**
	* 设置
	*/
	public void setAddress(String ADDRESS) { 
 		super.set("address", ADDRESS);
 	}
	/**
	* 获取
	*/
	public Integer getSparetime() { 
 		return super.getInt("sparetime");
 	}
	/**
	* 设置
	*/
	public void setSparetime(Integer SPARETIME) { 
 		super.set("sparetime", SPARETIME);
 	}
	/**
	* 获取
	*/
	public Integer getSpendtime() { 
 		return super.getInt("spendtime");
 	}
	/**
	* 设置
	*/
	public void setSpendtime(Integer SPENDTIME) { 
 		super.set("spendtime", SPENDTIME);
 	}
	/**
	* 获取
	*/
	public String getOrgwn() { 
 		return super.getStr("orgwn");
 	}
	/**
	* 设置
	*/
	public void setOrgwn(String ORGWN) { 
 		super.set("orgwn", ORGWN);
 	}
	/**
	* 获取
	*/
	public Integer getIs_green() { 
 		return super.getInt("is_green");
 	}
	/**
	* 设置
	*/
	public void setIs_green(Integer IS_GREEN) { 
 		super.set("is_green", IS_GREEN);
 	}
	/**
	* 获取
	*/
	public Integer getIs_delay() { 
 		return super.getInt("is_delay");
 	}
	/**
	* 设置
	*/
	public void setIs_delay(Integer IS_DELAY) { 
 		super.set("is_delay", IS_DELAY);
 	}
	/**
	* 获取
	*/
	public Date getGuidangdate() { 
 		return super.getDate("guidangdate");
 	}
	/**
	* 设置
	*/
	public void setGuidangdate(Date GUIDANGDATE) { 
 		super.set("guidangdate", GUIDANGDATE);
 	}
	/**
	* 获取
	*/
	public String getGuidangusername() { 
 		return super.getStr("guidangusername");
 	}
	/**
	* 设置
	*/
	public void setGuidangusername(String GUIDANGUSERNAME) { 
 		super.set("guidangusername", GUIDANGUSERNAME);
 	}
	/**
	* 获取
	*/
	public String getGuidanguserguid() { 
 		return super.getStr("guidanguserguid");
 	}
	/**
	* 设置
	*/
	public void setGuidanguserguid(String GUIDANGUSERGUID) { 
 		super.set("guidanguserguid", GUIDANGUSERGUID);
 	}
	/**
	* 获取
	*/
	public Integer getIs_guidang() { 
 		return super.getInt("is_guidang");
 	}
	/**
	* 设置
	*/
	public void setIs_guidang(Integer IS_GUIDANG) { 
 		super.set("is_guidang", IS_GUIDANG);
 	}
	/**
	* 获取
	*/
	public String getCertawardeguid() { 
 		return super.getStr("certawardeguid");
 	}
	/**
	* 设置
	*/
	public void setCertawardeguid(String CERTAWARDEGUID) { 
 		super.set("certawardeguid", CERTAWARDEGUID);
 	}
	/**
	* 获取
	*/
	public Date getCertificatedate() { 
 		return super.getDate("certificatedate");
 	}
	/**
	* 设置
	*/
	public void setCertificatedate(Date CERTIFICATEDATE) { 
 		super.set("certificatedate", CERTIFICATEDATE);
 	}
	/**
	* 获取
	*/
	public String getCertificateusername() { 
 		return super.getStr("certificateusername");
 	}
	/**
	* 设置
	*/
	public void setCertificateusername(String CERTIFICATEUSERNAME) { 
 		super.set("certificateusername", CERTIFICATEUSERNAME);
 	}
	/**
	* 获取
	*/
	public String getCertificateuserguid() { 
 		return super.getStr("certificateuserguid");
 	}
	/**
	* 设置
	*/
	public void setCertificateuserguid(String CERTIFICATEUSERGUID) { 
 		super.set("certificateuserguid", CERTIFICATEUSERGUID);
 	}
	/**
	* 获取
	*/
	public Date getFeedate() { 
 		return super.getDate("feedate");
 	}
	/**
	* 设置
	*/
	public void setFeedate(Date FEEDATE) { 
 		super.set("feedate", FEEDATE);
 	}
	/**
	* 获取
	*/
	public Integer getIs_fee() { 
 		return super.getInt("is_fee");
 	}
	/**
	* 设置
	*/
	public void setIs_fee(Integer IS_FEE) { 
 		super.set("is_fee", IS_FEE);
 	}
	/**
	* 获取
	*/
	public Integer getIs_check() { 
 		return super.getInt("is_check");
 	}
	/**
	* 设置
	*/
	public void setIs_check(Integer IS_CHECK) { 
 		super.set("is_check", IS_CHECK);
 	}
	/**
	* 获取
	*/
	public Integer getCharge_when() { 
 		return super.getInt("charge_when");
 	}
	/**
	* 设置
	*/
	public void setCharge_when(Integer CHARGE_WHEN) { 
 		super.set("charge_when", CHARGE_WHEN);
 	}
	/**
	* 获取
	*/
	public Integer getIs_charge() { 
 		return super.getInt("is_charge");
 	}
	/**
	* 设置
	*/
	public void setIs_charge(Integer IS_CHARGE) { 
 		super.set("is_charge", IS_CHARGE);
 	}
	/**
	* 获取
	*/
	public Date getBanjiedate() { 
 		return super.getDate("banjiedate");
 	}
	/**
	* 设置
	*/
	public void setBanjiedate(Date BANJIEDATE) { 
 		super.set("banjiedate", BANJIEDATE);
 	}
	/**
	* 获取
	*/
	public Integer getBanjieresult() { 
 		return super.getInt("banjieresult");
 	}
	/**
	* 设置
	*/
	public void setBanjieresult(Integer BANJIERESULT) { 
 		super.set("banjieresult", BANJIERESULT);
 	}
	/**
	* 获取
	*/
	public String getBanjieusername() { 
 		return super.getStr("banjieusername");
 	}
	/**
	* 设置
	*/
	public void setBanjieusername(String BANJIEUSERNAME) { 
 		super.set("banjieusername", BANJIEUSERNAME);
 	}
	/**
	* 获取
	*/
	public String getBanjieuserguid() { 
 		return super.getStr("banjieuserguid");
 	}
	/**
	* 设置
	*/
	public void setBanjieuserguid(String BANJIEUSERGUID) { 
 		super.set("banjieuserguid", BANJIEUSERGUID);
 	}
	/**
	* 获取
	*/
	public Date getBanwandate() { 
 		return super.getDate("banwandate");
 	}
	/**
	* 设置
	*/
	public void setBanwandate(Date BANWANDATE) { 
 		super.set("banwandate", BANWANDATE);
 	}
	/**
	* 获取
	*/
	public Date getPromiseenddate() { 
 		return super.getDate("promiseenddate");
 	}
	/**
	* 设置
	*/
	public void setPromiseenddate(Date PROMISEENDDATE) { 
 		super.set("promiseenddate", PROMISEENDDATE);
 	}
	/**
	* 获取
	*/
	public Date getAcceptuserdate() { 
 		return super.getDate("acceptuserdate");
 	}
	/**
	* 设置
	*/
	public void setAcceptuserdate(Date ACCEPTUSERDATE) { 
 		super.set("acceptuserdate", ACCEPTUSERDATE);
 	}
	/**
	* 获取
	*/
	public String getAcceptusername() { 
 		return super.getStr("acceptusername");
 	}
	/**
	* 设置
	*/
	public void setAcceptusername(String ACCEPTUSERNAME) { 
 		super.set("acceptusername", ACCEPTUSERNAME);
 	}
	/**
	* 获取
	*/
	public String getAcceptuserguid() { 
 		return super.getStr("acceptuserguid");
 	}
	/**
	* 设置
	*/
	public void setAcceptuserguid(String ACCEPTUSERGUID) { 
 		super.set("acceptuserguid", ACCEPTUSERGUID);
 	}
	/**
	* 获取
	*/
	public Date getReceivedate() { 
 		return super.getDate("receivedate");
 	}
	/**
	* 设置
	*/
	public void setReceivedate(Date RECEIVEDATE) { 
 		super.set("receivedate", RECEIVEDATE);
 	}
	/**
	* 获取
	*/
	public String getReceiveusername() { 
 		return super.getStr("receiveusername");
 	}
	/**
	* 设置
	*/
	public void setReceiveusername(String RECEIVEUSERNAME) { 
 		super.set("receiveusername", RECEIVEUSERNAME);
 	}
	/**
	* 获取
	*/
	public String getReceiveuserguid() { 
 		return super.getStr("receiveuserguid");
 	}
	/**
	* 设置
	*/
	public void setReceiveuserguid(String RECEIVEUSERGUID) { 
 		super.set("receiveuserguid", RECEIVEUSERGUID);
 	}
	/**
	* 获取
	*/
	public Integer getApplyway() { 
 		return super.getInt("applyway");
 	}
	/**
	* 设置
	*/
	public void setApplyway(Integer APPLYWAY) { 
 		super.set("applyway", APPLYWAY);
 	}
	/**
	* 获取
	*/
	public String getContactfax() { 
 		return super.getStr("contactfax");
 	}
	/**
	* 设置
	*/
	public void setContactfax(String CONTACTFAX) { 
 		super.set("contactfax", CONTACTFAX);
 	}
	/**
	* 获取
	*/
	public String getContactemail() { 
 		return super.getStr("contactemail");
 	}
	/**
	* 设置
	*/
	public void setContactemail(String CONTACTEMAIL) { 
 		super.set("contactemail", CONTACTEMAIL);
 	}
	/**
	* 获取
	*/
	public String getContactmobile() { 
 		return super.getStr("contactmobile");
 	}
	/**
	* 设置
	*/
	public void setContactmobile(String CONTACTMOBILE) { 
 		super.set("contactmobile", CONTACTMOBILE);
 	}
	/**
	* 获取
	*/
	public String getContactphone() { 
 		return super.getStr("contactphone");
 	}
	/**
	* 设置
	*/
	public void setContactphone(String CONTACTPHONE) { 
 		super.set("contactphone", CONTACTPHONE);
 	}
	/**
	* 获取
	*/
	public String getContactperson() { 
 		return super.getStr("contactperson");
 	}
	/**
	* 设置
	*/
	public void setContactperson(String CONTACTPERSON) { 
 		super.set("contactperson", CONTACTPERSON);
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
	public void setApplydate(Date APPLYDATE) { 
 		super.set("applydate", APPLYDATE);
 	}
	/**
	* 获取
	*/
	public String getCertnum() { 
 		return super.getStr("certnum");
 	}
	/**
	* 设置
	*/
	public void setCertnum(String CERTNUM) { 
 		super.set("certnum", CERTNUM);
 	}
	/**
	* 获取
	*/
	public String getCerttype() { 
 		return super.getStr("certtype");
 	}
	/**
	* 设置
	*/
	public void setCerttype(String CERTTYPE) { 
 		super.set("certtype", CERTTYPE);
 	}
	/**
	* 获取
	*/
	public String getApplyeruserguid() { 
 		return super.getStr("applyeruserguid");
 	}
	/**
	* 设置
	*/
	public void setApplyeruserguid(String APPLYERUSERGUID) { 
 		super.set("applyeruserguid", APPLYERUSERGUID);
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
	public void setApplyername(String APPLYERNAME) { 
 		super.set("applyername", APPLYERNAME);
 	}
	/**
	* 获取
	*/
	public Integer getApplyertype() { 
 		return super.getInt("applyertype");
 	}
	/**
	* 设置
	*/
	public void setApplyertype(Integer APPLYERTYPE) { 
 		super.set("applyertype", APPLYERTYPE);
 	}
	/**
	* 获取
	*/
	public Integer getIs_pause() { 
 		return super.getInt("is_pause");
 	}
	/**
	* 设置
	*/
	public void setIs_pause(Integer IS_PAUSE) { 
 		super.set("is_pause", IS_PAUSE);
 	}
	/**
	* 获取
	*/
	public Integer getStatus() { 
 		return super.getInt("status");
 	}
	/**
	* 设置
	*/
	public void setStatus(Integer STATUS) { 
 		super.set("status", STATUS);
 	}
	/**
	* 获取
	*/
	public String getXiangmubh() { 
 		return super.getStr("xiangmubh");
 	}
	/**
	* 设置
	*/
	public void setXiangmubh(String XIANGMUBH) { 
 		super.set("xiangmubh", XIANGMUBH);
 	}
	/**
	* 获取
	*/
	public String getPviguid() { 
 		return super.getStr("pviguid");
 	}
	/**
	* 设置
	*/
	public void setPviguid(String PVIGUID) { 
 		super.set("pviguid", PVIGUID);
 	}
	/**
	* 获取
	*/
	public String getFlowsn() { 
 		return super.getStr("flowsn");
 	}
	/**
	* 设置
	*/
	public void setFlowsn(String FLOWSN) { 
 		super.set("flowsn", FLOWSN);
 	}
	/**
	* 获取
	*/
	public Integer getPromise_day() { 
 		return super.getInt("promise_day");
 	}
	/**
	* 设置
	*/
	public void setPromise_day(Integer PROMISE_DAY) { 
 		super.set("promise_day", PROMISE_DAY);
 	}
	/**
	* 获取
	*/
	public Integer getTasktype() { 
 		return super.getInt("tasktype");
 	}
	/**
	* 设置
	*/
	public void setTasktype(Integer TASKTYPE) { 
 		super.set("tasktype", TASKTYPE);
 	}
	/**
	* 获取
	*/
	public String getWindowname() { 
 		return super.getStr("windowname");
 	}
	/**
	* 设置
	*/
	public void setWindowname(String WINDOWNAME) { 
 		super.set("windowname", WINDOWNAME);
 	}
	/**
	* 获取
	*/
	public String getWindowguid() { 
 		return super.getStr("windowguid");
 	}
	/**
	* 设置
	*/
	public void setWindowguid(String WINDOWGUID) { 
 		super.set("windowguid", WINDOWGUID);
 	}
	/**
	* 获取
	*/
	public String getOuguid() { 
 		return super.getStr("ouguid");
 	}
	/**
	* 设置
	*/
	public void setOuguid(String OUGUID) { 
 		super.set("ouguid", OUGUID);
 	}
	/**
	* 获取
	*/
	public String getTaskcaseguid() { 
 		return super.getStr("taskcaseguid");
 	}
	/**
	* 设置
	*/
	public void setTaskcaseguid(String TASKCASEGUID) { 
 		super.set("taskcaseguid", TASKCASEGUID);
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
	public void setTaskguid(String TASKGUID) { 
 		super.set("taskguid", TASKGUID);
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
	public void setProjectname(String PROJECTNAME) { 
 		super.set("projectname", PROJECTNAME);
 	}
	/**
	* 获取
	*/
	public Date getInsertdate() { 
 		return super.getDate("insertdate");
 	}
	/**
	* 设置
	*/
	public void setInsertdate(Date INSERTDATE) { 
 		super.set("insertdate", INSERTDATE);
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
	public void setTask_id(String TASK_ID) { 
 		super.set("task_id", TASK_ID);
 	}
	/**
	* 获取
	*/
	public Integer getOrgnumber() { 
 		return super.getInt("orgnumber");
 	}
	/**
	* 设置
	*/
	public void setOrgnumber(Integer ORGNUMBER) { 
 		super.set("orgnumber", ORGNUMBER);
 	}
	/**
	* 获取
	*/
	public String getTaskid() { 
 		return super.getStr("taskid");
 	}
	/**
	* 设置
	*/
	public void setTaskid(String TASKID) { 
 		super.set("taskid", TASKID);
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
	public void setOuname(String OUNAME) { 
 		super.set("ouname", OUNAME);
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
	public void setAreacode(String AREACODE) { 
 		super.set("areacode", AREACODE);
 	}
	/**
	* 获取
	*/
	public String getCenterguid() { 
 		return super.getStr("centerguid");
 	}
	/**
	* 设置
	*/
	public void setCenterguid(String CENTERGUID) { 
 		super.set("centerguid", CENTERGUID);
 	}
	/**
	* 获取
	*/
	public String getContactcertnum() { 
 		return super.getStr("contactcertnum");
 	}
	/**
	* 设置
	*/
	public void setContactcertnum(String CONTACTCERTNUM) { 
 		super.set("contactcertnum", CONTACTCERTNUM);
 	}
	/**
	* 获取
	*/
	public String getSubappguid() { 
 		return super.getStr("subappguid");
 	}
	/**
	* 设置
	*/
	public void setSubappguid(String SUBAPPGUID) { 
 		super.set("subappguid", SUBAPPGUID);
 	}
	/**
	* 获取
	*/
	public Integer getIs_evaluat() { 
 		return super.getInt("is_evaluat");
 	}
	/**
	* 设置
	*/
	public void setIs_evaluat(Integer is_evaluat) { 
 		super.set("is_evaluat", is_evaluat);
 	}
	/**
	* 获取
	*/
	public String getXiangmuname() { 
 		return super.getStr("xiangmuname");
 	}
	/**
	* 设置
	*/
	public void setXiangmuname(String xiangmuname) { 
 		super.set("xiangmuname", xiangmuname);
 	}
	/**
	* 获取
	*/
	public String getXiangmuaddress() { 
 		return super.getStr("xiangmuaddress");
 	}
	/**
	* 设置
	*/
	public void setXiangmuaddress(String xiangmuaddress) { 
 		super.set("xiangmuaddress", xiangmuaddress);
 	}
	/**
	* 获取
	*/
	public String getZixiangmuname() { 
 		return super.getStr("zixiangmuname");
 	}
	/**
	* 设置
	*/
	public void setZixiangmuname(String zixiangmuname) { 
 		super.set("zixiangmuname", zixiangmuname);
 	}
	/**
	* 获取
	*/
	public String getSbdw() { 
 		return super.getStr("sbdw");
 	}
	/**
	* 设置
	*/
	public void setSbdw(String sbdw) { 
 		super.set("sbdw", sbdw);
 	}
	/**
	* 获取
	*/
	public String getSbdwaddress() { 
 		return super.getStr("sbdwaddress");
 	}
	/**
	* 设置
	*/
	public void setSbdwaddress(String sbdwaddress) { 
 		super.set("sbdwaddress", sbdwaddress);
 	}
	/**
	* 获取
	*/
	public String getSbdwphone() { 
 		return super.getStr("sbdwphone");
 	}
	/**
	* 设置
	*/
	public void setSbdwphone(String sbdwphone) { 
 		super.set("sbdwphone", sbdwphone);
 	}
	/**
	* 获取
	*/
	public String getSbdwguid() { 
 		return super.getStr("sbdwguid");
 	}
	/**
	* 设置
	*/
	public void setSbdwguid(String sbdwguid) { 
 		super.set("sbdwguid", sbdwguid);
 	}
	/**
	* 获取
	*/
	public String getXiangmuguid() { 
 		return super.getStr("xiangmuguid");
 	}
	/**
	* 设置
	*/
	public void setXiangmuguid(String xiangmuguid) { 
 		super.set("xiangmuguid", xiangmuguid);
 	}
	/**
	* 获取
	*/
	public String getBmoption() { 
 		return super.getStr("bmoption");
 	}
	/**
	* 设置
	*/
	public void setBmoption(String BmOption) { 
 		super.set("bmoption", BmOption);
 	}
	/**
	* 获取
	*/
	public String getXiangmubm() { 
 		return super.getStr("xiangmubm");
 	}
	/**
	* 设置
	*/
	public void setXiangmubm(String xiangmubm) { 
 		super.set("xiangmubm", xiangmubm);
 	}
	/**
	* 获取
	*/
	public String getScyj() { 
 		return super.get("scyj");
 	}
	/**
	* 设置
	*/
	public void setScyj(String scyj) { 
 		super.set("scyj", scyj);
 	}
	/**
	* 获取
	*/
	public Integer getIscanbookreason() { 
 		return super.getInt("iscanbookreason");
 	}
	/**
	* 设置
	*/
	public void setIscanbookreason(Integer IsCanBookReason) { 
 		super.set("iscanbookreason", IsCanBookReason);
 	}
	/**
	* 获取
	*/
	public Date getFadingbanjiedate() { 
 		return super.getDate("fadingbanjiedate");
 	}
	/**
	* 设置
	*/
	public void setFadingbanjiedate(Date fadingbanjiedate) { 
 		super.set("fadingbanjiedate", fadingbanjiedate);
 	}
	/**
	* 获取
	*/
	public String getIf_express() { 
 		return super.getStr("if_express");
 	}
	/**
	* 设置
	*/
	public void setIf_express(String if_express) { 
 		super.set("if_express", if_express);
 	}
	/**
	* 获取
	*/
	public String getOfficaldocguid() { 
 		return super.getStr("officaldocguid");
 	}
	/**
	* 设置
	*/
	public void setOfficaldocguid(String OfficalDocGuid) { 
 		super.set("officaldocguid", OfficalDocGuid);
 	}
	/**
	* 获取
	*/
	public String getIf_jz_hall() { 
 		return super.getStr("if_jz_hall");
 	}
	/**
	* 设置
	*/
	public void setIf_jz_hall(String IF_JZ_HALL) { 
 		super.set("if_jz_hall", IF_JZ_HALL);
 	}
	/**
	* 获取
	*/
	public String getComycontactphone() { 
 		return super.getStr("comycontactphone");
 	}
	/**
	* 设置
	*/
	public void setComycontactphone(String comycontactphone) { 
 		super.set("comycontactphone", comycontactphone);
 	}
	/**
	* 获取
	*/
	public String getCertrowguid() { 
 		return super.getStr("certrowguid");
 	}
	/**
	* 设置
	*/
	public void setCertrowguid(String CERTROWGUID) { 
 		super.set("certrowguid", CERTROWGUID);
 	}
	/**
	* 获取
	*/
	public String getIs_syncproject() { 
 		return super.getStr("is_syncproject");
 	}
	/**
	* 设置
	*/
	public void setIs_syncproject(String IS_SYNCPROJECT) { 
 		super.set("is_syncproject", IS_SYNCPROJECT);
 	}
	/**
	* 获取
	*/
	public String getIf_express_ma() { 
 		return super.getStr("if_express_ma");
 	}
	/**
	* 设置
	*/
	public void setIf_express_ma(String if_express_ma) { 
 		super.set("if_express_ma", if_express_ma);
 	}
	/**
	* 获取
	*/
	public String getCertdocguid() { 
 		return super.getStr("certdocguid");
 	}
	/**
	* 设置
	*/
	public void setCertdocguid(String CertDocGuid) { 
 		super.set("certdocguid", CertDocGuid);
 	}
	/**
	* 获取
	*/
	public String getAcceptareacode() { 
 		return super.getStr("acceptareacode");
 	}
	/**
	* 设置
	*/
	public void setAcceptareacode(String ACCEPTAREACODE) { 
 		super.set("acceptareacode", ACCEPTAREACODE);
 	}
	/**
	* 获取
	*/
	public String getHandleareacode() { 
 		return super.getStr("handleareacode");
 	}
	/**
	* 设置
	*/
	public void setHandleareacode(String HANDLEAREACODE) { 
 		super.set("handleareacode", HANDLEAREACODE);
 	}
	/**
	* 获取
	*/
	public String getBanjieareacode() { 
 		return super.getStr("banjieareacode");
 	}
	/**
	* 设置
	*/
	public void setBanjieareacode(String BANJIEAREACODE) { 
 		super.set("banjieareacode", BANJIEAREACODE);
 	}
	/**
	* 获取
	*/
	public String getCurrentareacode() { 
 		return super.getStr("currentareacode");
 	}
	/**
	* 设置
	*/
	public void setCurrentareacode(String CURRENTAREACODE) { 
 		super.set("currentareacode", CURRENTAREACODE);
 	}
	/**
	* 获取
	*/
	public String getLegalid() { 
 		return super.getStr("legalid");
 	}
	/**
	* 设置
	*/
	public void setLegalid(String LEGALID) { 
 		super.set("legalid", LEGALID);
 	}
	/**
	* 获取
	*/
	public String getYcsl_sign() { 
 		return super.getStr("ycsl_sign");
 	}
	/**
	* 设置
	*/
	public void setYcsl_sign(String ycsl_sign) { 
 		super.set("ycsl_sign", ycsl_sign);
 	}
	/**
	* 获取
	*/
	public Date getYcsl_sign_date() { 
 		return super.getDate("ycsl_sign_date");
 	}
	/**
	* 设置
	*/
	public void setYcsl_sign_date(Date ycsl_sign_date) { 
 		super.set("ycsl_sign_date", ycsl_sign_date);
 	}
	/**
	* 获取
	*/
	public String getYcsl_errortext() { 
 		return super.getStr("ycsl_errortext");
 	}
	/**
	* 设置
	*/
	public void setYcsl_errortext(String ycsl_errortext) { 
 		super.set("ycsl_errortext", ycsl_errortext);
 	}
	/**
	* 获取
	*/
	public String getIssynacwave() { 
 		return super.getStr("issynacwave");
 	}
	/**
	* 设置
	*/
	public void setIssynacwave(String ISSYNACWAVE) { 
 		super.set("issynacwave", ISSYNACWAVE);
 	}
	/**
	* 获取
	*/
	public String getClflag() { 
 		return super.getStr("clflag");
 	}
	/**
	* 设置
	*/
	public void setClflag(String clflag) { 
 		super.set("clflag", clflag);
 	}
	/**
	* 获取
	*/
	public String getDataid() { 
 		return super.getStr("dataid");
 	}
	/**
	* 设置
	*/
	public void setDataid(String dataid) { 
 		super.set("dataid", dataid);
 	}
	/**
	* 获取
	*/
	public String getFormid() { 
 		return super.getStr("formid");
 	}
	/**
	* 设置
	*/
	public void setFormid(String formid) { 
 		super.set("formid", formid);
 	}
	/**
	* 获取
	*/
	public String getGpyattachguid() { 
 		return super.getStr("gpyattachguid");
 	}
	/**
	* 设置
	*/
	public void setGpyattachguid(String gpyattachguid) { 
 		super.set("gpyattachguid", gpyattachguid);
 	}
	/**
	* 获取
	*/
	public String getAreaall() { 
 		return super.getStr("areaall");
 	}
	/**
	* 设置
	*/
	public void setAreaall(String AREAALL) { 
 		super.set("areaall", AREAALL);
 	}
	/**
	* 获取
	*/
	public String getAreabuild() { 
 		return super.getStr("areabuild");
 	}
	/**
	* 设置
	*/
	public void setAreabuild(String AREABUILD) { 
 		super.set("areabuild", AREABUILD);
 	}
	/**
	* 获取
	*/
	public String getInvestment() { 
 		return super.getStr("investment");
 	}
	/**
	* 设置
	*/
	public void setInvestment(String INVESTMENT) { 
 		super.set("investment", INVESTMENT);
 	}
	/**
	* 获取
	*/
	public String getProjectcontent() { 
 		return super.getStr("projectcontent");
 	}
	/**
	* 设置
	*/
	public void setProjectcontent(String PROJECTCONTENT) { 
 		super.set("projectcontent", PROJECTCONTENT);
 	}
	/**
	* 获取
	*/
	public String getProjectallowedno() { 
 		return super.getStr("projectallowedno");
 	}
	/**
	* 设置
	*/
	public void setProjectallowedno(String PROJECTALLOWEDNO) { 
 		super.set("projectallowedno", PROJECTALLOWEDNO);
 	}
	/**
	* 获取
	*/
	public String getIs_lczj() { 
 		return super.getStr("is_lczj");
 	}
	/**
	* 设置
	*/
	public void setIs_lczj(String is_lczj) { 
 		super.set("is_lczj", is_lczj);
 	}
	/**
	* 获取
	*/
	public String getZj_flowsn() { 
 		return super.getStr("zj_flowsn");
 	}
	/**
	* 设置
	*/
	public void setZj_flowsn(String zj_flowsn) { 
 		super.set("zj_flowsn", zj_flowsn);
 	}
	/**
	* 获取
	*/
	public String getXmnum() { 
 		return super.getStr("xmnum");
 	}
	/**
	* 设置
	*/
	public void setXmnum(String xmnum) { 
 		super.set("xmnum", xmnum);
 	}
	/**
	* 获取
	*/
	public String getXmname() { 
 		return super.getStr("xmname");
 	}
	/**
	* 设置
	*/
	public void setXmname(String xmname) { 
 		super.set("xmname", xmname);
 	}
	/**
	* 获取
	*/
	public String getWenhao() { 
 		return super.getStr("wenhao");
 	}
	/**
	* 设置
	*/
	public void setWenhao(String wenhao) { 
 		super.set("wenhao", wenhao);
 	}
	/**
	* 获取
	*/
	public String getOnlineapplyerguid() { 
 		return super.getStr("onlineapplyerguid");
 	}
	/**
	* 设置
	*/
	public void setOnlineapplyerguid(String ONLINEAPPLYERGUID) { 
 		super.set("onlineapplyerguid", ONLINEAPPLYERGUID);
 	}
	/**
	* 获取
	*/
	public String getForm_xml() { 
 		return super.get("form_xml");
 	}
	/**
	* 设置
	*/
	public void setForm_xml(String form_xml) { 
 		super.set("form_xml", form_xml);
 	}
	/**
	* 获取审批系统办件信息同步到浪潮前置库标识
	*/
	public String getSysnid() { 
 		return super.getStr("sysnid");
 	}
	/**
	* 设置审批系统办件信息同步到浪潮前置库标识
	*/
	public void setSysnid(String sysnid) { 
 		super.set("sysnid", sysnid);
 	}
	/**
	* 获取扫码枪流程信息推送前置库标识
	*/
	public String getSysnidlc() { 
 		return super.getStr("sysnidlc");
 	}
	/**
	* 设置扫码枪流程信息推送前置库标识
	*/
	public void setSysnidlc(String sysnidlc) { 
 		super.set("sysnidlc", sysnidlc);
 	}
	/**
	* 获取审批系统办件信息推送市评价系统标识
	*/
	public String getSysnidpj() { 
 		return super.getStr("sysnidpj");
 	}
	/**
	* 设置审批系统办件信息推送市评价系统标识
	*/
	public void setSysnidpj(String sysnidpj) { 
 		super.set("sysnidpj", sysnidpj);
 	}
	/**
	* 获取
	*/
	public String getIs_samecity() { 
 		return super.getStr("is_samecity");
 	}
	/**
	* 设置
	*/
	public void setIs_samecity(String IS_SAMECITY) { 
 		super.set("is_samecity", IS_SAMECITY);
 	}
	/**
	* 获取
	*/
	public String getSearchpwd() { 
 		return super.getStr("searchpwd");
 	}
	/**
	* 设置
	*/
	public void setSearchpwd(String searchpwd) { 
 		super.set("searchpwd", searchpwd);
 	}
	/**
	* 获取
	*/
	public String getSyncsgxkz() { 
 		return super.getStr("syncsgxkz");
 	}
	/**
	* 设置
	*/
	public void setSyncsgxkz(String syncsgxkz) { 
 		super.set("syncsgxkz", syncsgxkz);
 	}
	/**
	* 获取
	*/
	public String getDataobj_baseinfo() { 
 		return super.get("dataobj_baseinfo");
 	}
	/**
	* 设置
	*/
	public void setDataobj_baseinfo(String dataObj_baseinfo) { 
 		super.set("dataobj_baseinfo", dataObj_baseinfo);
 	}
	/**
	* 获取
	*/
	public String getDataobj_baseinfo_other() { 
 		return super.get("dataobj_baseinfo_other");
 	}
	/**
	* 设置
	*/
	public void setDataobj_baseinfo_other(String dataObj_baseinfo_other) { 
 		super.set("dataobj_baseinfo_other", dataObj_baseinfo_other);
 	}
	/**
	* 获取
	*/
	public String getQrcodeinfo() { 
 		return super.getStr("qrcodeinfo");
 	}
	/**
	* 设置
	*/
	public void setQrcodeinfo(String QRcodeinfo) { 
 		super.set("qrcodeinfo", QRcodeinfo);
 	}
	/**
	* 获取
	*/
	public String getDzyyqcode() { 
 		return super.getStr("dzyyqcode");
 	}
	/**
	* 设置
	*/
	public void setDzyyqcode(String dzyyqcode) { 
 		super.set("dzyyqcode", dzyyqcode);
 	}
	/**
	* 获取
	*/
	public String getHas_xycx() { 
 		return super.getStr("has_xycx");
 	}
	/**
	* 设置
	*/
	public void setHas_xycx(String has_xycx) { 
 		super.set("has_xycx", has_xycx);
 	}
	/**
	* 获取
	*/
	public String getHcpstatus() { 
 		return super.getStr("hcpstatus");
 	}
	/**
	* 设置
	*/
	public void setHcpstatus(String hcpstatus) { 
 		super.set("hcpstatus", hcpstatus);
 	}
	/**
	* 获取
	*/
	public String getZjsync() { 
 		return super.getStr("zjsync");
 	}
	/**
	* 设置
	*/
	public void setZjsync(String zjsync) { 
 		super.set("zjsync", zjsync);
 	}
	/**
	* 获取
	*/
	public String getCxcode() { 
 		return super.getStr("cxcode");
 	}
	/**
	* 设置
	*/
	public void setCxcode(String cxcode) { 
 		super.set("cxcode", cxcode);
 	}
	/**
	* 获取
	*/
	public String getJstcodeinfo() { 
 		return super.getStr("jstcodeinfo");
 	}
	/**
	* 设置
	*/
	public void setJstcodeinfo(String jstcodeinfo) { 
 		super.set("jstcodeinfo", jstcodeinfo);
 	}
}