package com.epoint.hcp.api.entity;

import java.util.Date;
import com.epoint.core.annotation.Entity;
import com.epoint.core.BaseEntity;
/**
* 实体类 
* 
* @作者 张彬彬 
* @version [版本号, 2020-05-26 14:45:15] 
*/

@Entity(table = "ea_jc_step_basicinfo_gt", id = "rowguid")
public class eajcstepbasicinfogt extends BaseEntity {
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
	public String getProjpwd() { 
 		return super.getStr("projpwd");
 	}
	/**
	* 设置
	*/
	public void setProjpwd(String PROJPWD) { 
 		super.set("projpwd", PROJPWD);
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
	public String getItemno() { 
 		return super.getStr("itemno");
 	}
	/**
	* 设置
	*/
	public void setItemno(String ITEMNO) { 
 		super.set("itemno", ITEMNO);
 	}
	/**
	* 获取
	*/
	public String getSubitemno() { 
 		return super.getStr("subitemno");
 	}
	/**
	* 设置
	*/
	public void setSubitemno(String SUBITEMNO) { 
 		super.set("subitemno", SUBITEMNO);
 	}
	/**
	* 获取
	*/
	public String getItemname() { 
 		return super.getStr("itemname");
 	}
	/**
	* 设置
	*/
	public void setItemname(String ITEMNAME) { 
 		super.set("itemname", ITEMNAME);
 	}
	/**
	* 获取
	*/
	public String getSubitemname() { 
 		return super.getStr("subitemname");
 	}
	/**
	* 设置
	*/
	public void setSubitemname(String SUBITEMNAME) { 
 		super.set("subitemname", SUBITEMNAME);
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
	public String getApplicant() { 
 		return super.getStr("applicant");
 	}
	/**
	* 设置
	*/
	public void setApplicant(String APPLICANT) { 
 		super.set("applicant", APPLICANT);
 	}
	/**
	* 获取
	*/
	public String getApplicantmobile() { 
 		return super.getStr("applicantmobile");
 	}
	/**
	* 设置
	*/
	public void setApplicantmobile(String APPLICANTMOBILE) { 
 		super.set("applicantmobile", APPLICANTMOBILE);
 	}
	/**
	* 获取
	*/
	public String getApplicanttel() { 
 		return super.getStr("applicanttel");
 	}
	/**
	* 设置
	*/
	public void setApplicanttel(String APPLICANTTEL) { 
 		super.set("applicanttel", APPLICANTTEL);
 	}
	/**
	* 获取
	*/
	public String getApplicantCardtype() { 
 		return super.getStr("applicantcardtype");
 	}
	/**
	* 设置
	*/
	public void setApplicantCardtype(String ApplicantCardtype) { 
 		super.set("applicantcardtype", ApplicantCardtype);
 	}
	/**
	* 获取
	*/
	public String getApplicantCardCode() { 
 		return super.getStr("applicantcardcode");
 	}
	/**
	* 设置
	*/
	public void setApplicantCardCode(String ApplicantCardCode) { 
 		super.set("applicantcardcode", ApplicantCardCode);
 	}
	/**
	* 获取
	*/
	public String getApplicantemail() { 
 		return super.getStr("applicantemail");
 	}
	/**
	* 设置
	*/
	public void setApplicantemail(String APPLICANTEMAIL) { 
 		super.set("applicantemail", APPLICANTEMAIL);
 	}
	/**
	* 获取
	*/
	public String getAcceptlist() { 
 		return super.getStr("acceptlist");
 	}
	/**
	* 设置
	*/
	public void setAcceptlist(String ACCEPTLIST) { 
 		super.set("acceptlist", ACCEPTLIST);
 	}
	/**
	* 获取
	*/
	public String getAcceptdeptname() { 
 		return super.getStr("acceptdeptname");
 	}
	/**
	* 设置
	*/
	public void setAcceptdeptname(String ACCEPTDEPTNAME) { 
 		super.set("acceptdeptname", ACCEPTDEPTNAME);
 	}
	/**
	* 获取
	*/
	public String getAcceptdeptid() { 
 		return super.getStr("acceptdeptid");
 	}
	/**
	* 设置
	*/
	public void setAcceptdeptid(String ACCEPTDEPTID) { 
 		super.set("acceptdeptid", ACCEPTDEPTID);
 	}
	/**
	* 获取
	*/
	public String getAcceptdeptcode() { 
 		return super.getStr("acceptdeptcode");
 	}
	/**
	* 设置
	*/
	public void setAcceptdeptcode(String ACCEPTDEPTCODE) { 
 		super.set("acceptdeptcode", ACCEPTDEPTCODE);
 	}
	/**
	* 获取
	*/
	public String getAcceptdeptcode1() { 
 		return super.getStr("acceptdeptcode1");
 	}
	/**
	* 设置
	*/
	public void setAcceptdeptcode1(String ACCEPTDEPTCODE1) { 
 		super.set("acceptdeptcode1", ACCEPTDEPTCODE1);
 	}
	/**
	* 获取
	*/
	public String getAcceptdeptcode2() { 
 		return super.getStr("acceptdeptcode2");
 	}
	/**
	* 设置
	*/
	public void setAcceptdeptcode2(String ACCEPTDEPTCODE2) { 
 		super.set("acceptdeptcode2", ACCEPTDEPTCODE2);
 	}
	/**
	* 获取
	*/
	public String getApprovaltype() { 
 		return super.get("approvaltype");
 	}
	/**
	* 设置
	*/
	public void setApprovaltype(String APPROVALTYPE) { 
 		super.set("approvaltype", APPROVALTYPE);
 	}
	/**
	* 获取
	*/
	public String getPromisetimelimit() { 
 		return super.getStr("promisetimelimit");
 	}
	/**
	* 设置
	*/
	public void setPromisetimelimit(String PROMISETIMELIMIT) { 
 		super.set("promisetimelimit", PROMISETIMELIMIT);
 	}
	/**
	* 获取
	*/
	public String getPromisetimeunit() { 
 		return super.get("promisetimeunit");
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
	public  String getTimeunit() { 
 		return super.get("timeunit");
 	}
	/**
	* 设置
	*/
	public void setTimeunit(String TIMEUNIT) { 
 		super.set("timeunit", TIMEUNIT);
 	}
	/**
	* 获取
	*/
	public String getSubmit() { 
 		return super.get("submit");
 	}
	/**
	* 设置
	*/
	public void setSubmit(String SUBMIT) { 
 		super.set("submit", SUBMIT);
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
	public String getTransactor() { 
 		return super.getStr("transactor");
 	}
	/**
	* 设置
	*/
	public void setTransactor(String TRANSACTOR) { 
 		super.set("transactor", TRANSACTOR);
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
 		return super.get("signstate");
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
	public String getInnerno() { 
 		return super.getStr("innerno");
 	}
	/**
	* 设置
	*/
	public void setInnerno(String INNERNO) { 
 		super.set("innerno", INNERNO);
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
	/**
	* 获取
	*/
	public String getDhtofgsgs() { 
 		return super.getStr("dhtofgsgs");
 	}
	/**
	* 设置
	*/
	public void setDhtofgsgs(String DHTOFGSGS) { 
 		super.set("dhtofgsgs", DHTOFGSGS);
 	}
	/**
	* 获取
	*/
	public String getName() { 
 		return super.getStr("name");
 	}
	/**
	* 设置
	*/
	public void setName(String NAME) { 
 		super.set("name", NAME);
 	}
	/**
	* 获取
	*/
	public String getIdcard_no() { 
 		return super.getStr("idcard_no");
 	}
	/**
	* 设置
	*/
	public void setIdcard_no(String IDCARD_NO) { 
 		super.set("idcard_no", IDCARD_NO);
 	}
	/**
	* 获取
	*/
	public String getSeq_id() { 
 		return super.getStr("seq_id");
 	}
	/**
	* 设置
	*/
	public void setSeq_id(String SEQ_ID) { 
 		super.set("seq_id", SEQ_ID);
 	}
	/**
	* 获取
	*/
	public String getBusiness_object_type() { 
 		return super.getStr("business_object_type");
 	}
	/**
	* 设置
	*/
	public void setBusiness_object_type(String BUSINESS_OBJECT_TYPE) { 
 		super.set("business_object_type", BUSINESS_OBJECT_TYPE);
 	}
	/**
	* 获取
	*/
	public String getOrg_name() { 
 		return super.getStr("org_name");
 	}
	/**
	* 设置
	*/
	public void setOrg_name(String ORG_NAME) { 
 		super.set("org_name", ORG_NAME);
 	}
	/**
	* 获取
	*/
	public String getLegal_person() { 
 		return super.getStr("legal_person");
 	}
	/**
	* 设置
	*/
	public void setLegal_person(String LEGAL_PERSON) { 
 		super.set("legal_person", LEGAL_PERSON);
 	}
	/**
	* 获取
	*/
	public String getCertificate_no() { 
 		return super.getStr("certificate_no");
 	}
	/**
	* 设置
	*/
	public void setCertificate_no(String CERTIFICATE_NO) { 
 		super.set("certificate_no", CERTIFICATE_NO);
 	}
	/**
	* 获取
	*/
	public String getCredit_code() { 
 		return super.getStr("credit_code");
 	}
	/**
	* 设置
	*/
	public void setCredit_code(String CREDIT_CODE) { 
 		super.set("credit_code", CREDIT_CODE);
 	}
	/**
	* 获取
	*/
	public String getDc_project_name() { 
 		return super.getStr("dc_project_name");
 	}
	/**
	* 设置
	*/
	public void setDc_project_name(String DC_PROJECT_NAME) { 
 		super.set("dc_project_name", DC_PROJECT_NAME);
 	}
	/**
	* 获取
	*/
	public String getDc_project_code() { 
 		return super.getStr("dc_project_code");
 	}
	/**
	* 设置
	*/
	public void setDc_project_code(String DC_PROJECT_CODE) { 
 		super.set("dc_project_code", DC_PROJECT_CODE);
 	}
	/**
	* 获取
	*/
	public String getLink_man() { 
 		return super.getStr("link_man");
 	}
	/**
	* 设置
	*/
	public void setLink_man(String LINK_MAN) { 
 		super.set("link_man", LINK_MAN);
 	}
	/**
	* 获取
	*/
	public String getBj() { 
 		return super.getStr("bj");
 	}
	/**
	* 设置
	*/
	public void setBj(String BJ) { 
 		super.set("bj", BJ);
 	}
	/**
	* 获取
	*/
	public Date getDone_occurtime() { 
 		return super.getDate("done_occurtime");
 	}
	/**
	* 设置
	*/
	public void setDone_occurtime(Date DONE_OCCURTIME) { 
 		super.set("done_occurtime", DONE_OCCURTIME);
 	}
}