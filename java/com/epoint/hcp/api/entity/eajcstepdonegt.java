package com.epoint.hcp.api.entity;
import java.util.Date;
import com.epoint.core.annotation.Entity;
import com.epoint.core.BaseEntity;
/**
* 实体类 
* 
* @作者 张彬彬 
* @version [版本号, 2020-05-26 14:45:32] 
*/

@Entity(table = "ea_jc_step_done_gt", id = "rowguid")
public class eajcstepdonegt extends BaseEntity {
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
	public String getDoneresult() { 
 		return super.getStr("doneresult");
 	}
	/**
	* 设置
	*/
	public void setDoneresult(String DONERESULT) { 
 		super.set("doneresult", DONERESULT);
 	}
	/**
	* 获取
	*/
	public String getExitres() { 
 		return super.getStr("exitres");
 	}
	/**
	* 设置
	*/
	public void setExitres(String EXITRES) { 
 		super.set("exitres", EXITRES);
 	}
	/**
	* 获取
	*/
	public String getApprovalvarchar() { 
 		return super.getStr("approvalvarchar");
 	}
	/**
	* 设置
	*/
	public void setApprovalvarchar(String APPROVALVARCHAR) { 
 		super.set("approvalvarchar", APPROVALVARCHAR);
 	}
	/**
	* 获取
	*/
	public Date getApprovallimit() { 
 		return super.getDate("approvallimit");
 	}
	/**
	* 设置
	*/
	public void setApprovallimit(Date APPROVALLIMIT) { 
 		super.set("approvallimit", APPROVALLIMIT);
 	}
	/**
	* 获取
	*/
	public String getCertificatenam() { 
 		return super.getStr("certificatenam");
 	}
	/**
	* 设置
	*/
	public void setCertificatenam(String CERTIFICATENAM) { 
 		super.set("certificatenam", CERTIFICATENAM);
 	}
	/**
	* 获取
	*/
	public String getCertificateno() { 
 		return super.getStr("certificateno");
 	}
	/**
	* 设置
	*/
	public void setCertificateno(String CERTIFICATENO) { 
 		super.set("certificateno", CERTIFICATENO);
 	}
	/**
	* 获取
	*/
	public String getCertificatelimit() { 
 		return super.getStr("certificatelimit");
 	}
	/**
	* 设置
	*/
	public void setCertificatelimit(String CERTIFICATELIMIT) { 
 		super.set("certificatelimit", CERTIFICATELIMIT);
 	}
	/**
	* 获取
	*/
	public String getPublisher() { 
 		return super.getStr("publisher");
 	}
	/**
	* 设置
	*/
	public void setPublisher(String PUBLISHER) { 
 		super.set("publisher", PUBLISHER);
 	}
	/**
	* 获取
	*/
	public String getIsfee() { 
 		return super.getStr("isfee");
 	}
	/**
	* 设置
	*/
	public void setIsfee(String ISFEE) { 
 		super.set("isfee", ISFEE);
 	}
	/**
	* 获取
	*/
	public String getFee() { 
 		return super.getStr("fee");
 	}
	/**
	* 设置
	*/
	public void setFee(String FEE) { 
 		super.set("fee", FEE);
 	}
	/**
	* 获取
	*/
	public String getFeestandard() { 
 		return super.getStr("feestandard");
 	}
	/**
	* 设置
	*/
	public void setFeestandard(String FEESTANDARD) { 
 		super.set("feestandard", FEESTANDARD);
 	}
	/**
	* 获取
	*/
	public String getFeestandaccord() { 
 		return super.getStr("feestandaccord");
 	}
	/**
	* 设置
	*/
	public void setFeestandaccord(String FEESTANDACCORD) { 
 		super.set("feestandaccord", FEESTANDACCORD);
 	}
	/**
	* 获取
	*/
	public String getPaypersonname() { 
 		return super.getStr("paypersonname");
 	}
	/**
	* 设置
	*/
	public void setPaypersonname(String PAYPERSONNAME) { 
 		super.set("paypersonname", PAYPERSONNAME);
 	}
	/**
	* 获取
	*/
	public String getPayperidcard() { 
 		return super.getStr("payperidcard");
 	}
	/**
	* 设置
	*/
	public void setPayperidcard(String PAYPERIDCARD) { 
 		super.set("payperidcard", PAYPERIDCARD);
 	}
	/**
	* 获取
	*/
	public String getPayermobile() { 
 		return super.getStr("payermobile");
 	}
	/**
	* 设置
	*/
	public void setPayermobile(String PAYERMOBILE) { 
 		super.set("payermobile", PAYERMOBILE);
 	}
	/**
	* 获取
	*/
	public String getPayertel() { 
 		return super.getStr("payertel");
 	}
	/**
	* 设置
	*/
	public void setPayertel(String PAYERTEL) { 
 		super.set("payertel", PAYERTEL);
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
	public String getEx_flag() { 
 		return super.getStr("ex_flag");
 	}
	/**
	* 设置
	*/
	public void setEx_flag(String EX_FLAG) { 
 		super.set("ex_flag", EX_FLAG);
 	}
}