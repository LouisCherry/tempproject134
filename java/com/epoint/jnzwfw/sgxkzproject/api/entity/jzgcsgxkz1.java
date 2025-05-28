package com.epoint.jnzwfw.sgxkzproject.api.entity;
import java.util.Date;
import com.epoint.core.annotation.Entity;
import com.epoint.core.BaseEntity;
/**
* 实体类 
* 
* @作者 张彬彬 
* @version [版本号, 2022-04-19 10:49:54] 
*/

@Entity(table = "jzgcsgxkz_1", id = "id")
public class jzgcsgxkz1 extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	* 获取
	*/
	public Date getRksj() { 
 		return super.getDate("rksj");
 	}
	/**
	* 设置
	*/
	public void setRksj(Date rksj) { 
 		super.set("rksj", rksj);
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
	/**
	* 获取
	*/
	public String getLicense_number() { 
 		return super.getStr("license_number");
 	}
	/**
	* 设置
	*/
	public void setLicense_number(String LICENSE_NUMBER) { 
 		super.set("license_number", LICENSE_NUMBER);
 	}
	/**
	* 获取
	*/
	public Date getCertificate_date() { 
 		return super.getDate("certificate_date");
 	}
	/**
	* 设置
	*/
	public void setCertificate_date(Date CERTIFICATE_DATE) { 
 		super.set("certificate_date", CERTIFICATE_DATE);
 	}
	/**
	* 获取
	*/
	public Date getValid_period_begin() { 
 		return super.getDate("valid_period_begin");
 	}
	/**
	* 设置
	*/
	public void setValid_period_begin(Date VALID_PERIOD_BEGIN) { 
 		super.set("valid_period_begin", VALID_PERIOD_BEGIN);
 	}
	/**
	* 获取
	*/
	public Date getValid_period_end() { 
 		return super.getDate("valid_period_end");
 	}
	/**
	* 设置
	*/
	public void setValid_period_end(Date VALID_PERIOD_END) { 
 		super.set("valid_period_end", VALID_PERIOD_END);
 	}
	/**
	* 获取
	*/
	public String getHolder_name() { 
 		return super.getStr("holder_name");
 	}
	/**
	* 设置
	*/
	public void setHolder_name(String HOLDER_NAME) { 
 		super.set("holder_name", HOLDER_NAME);
 	}
	/**
	* 获取
	*/
	public String getCertificate_type() { 
 		return super.getStr("certificate_type");
 	}
	/**
	* 设置
	*/
	public void setCertificate_type(String CERTIFICATE_TYPE) { 
 		super.set("certificate_type", CERTIFICATE_TYPE);
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
	public String getProject_name() { 
 		return super.getStr("project_name");
 	}
	/**
	* 设置
	*/
	public void setProject_name(String PROJECT_NAME) { 
 		super.set("project_name", PROJECT_NAME);
 	}
	/**
	* 获取
	*/
	public String getGongchengmingchen() { 
 		return super.getStr("gongchengmingchen");
 	}
	/**
	* 设置
	*/
	public void setGongchengmingchen(String GONGCHENGMINGCHEN) { 
 		super.set("gongchengmingchen", GONGCHENGMINGCHEN);
 	}
	/**
	* 获取
	*/
	public String getJianshedizhi() { 
 		return super.getStr("jianshedizhi");
 	}
	/**
	* 设置
	*/
	public void setJianshedizhi(String JIANSHEDIZHI) { 
 		super.set("jianshedizhi", JIANSHEDIZHI);
 	}
	/**
	* 获取
	*/
	public String getJiansheguimo() { 
 		return super.getStr("jiansheguimo");
 	}
	/**
	* 设置
	*/
	public void setJiansheguimo(String JIANSHEGUIMO) { 
 		super.set("jiansheguimo", JIANSHEGUIMO);
 	}
	/**
	* 获取
	*/
	public String getHetonggongqizhi() { 
 		return super.getStr("hetonggongqizhi");
 	}
	/**
	* 设置
	*/
	public void setHetonggongqizhi(String HETONGGONGQIZHI) { 
 		super.set("hetonggongqizhi", HETONGGONGQIZHI);
 	}
	/**
	* 获取
	*/
	public Date getJihuakaigongriqi() { 
 		return super.getDate("jihuakaigongriqi");
 	}
	/**
	* 设置
	*/
	public void setJihuakaigongriqi(Date JIHUAKAIGONGRIQI) { 
 		super.set("jihuakaigongriqi", JIHUAKAIGONGRIQI);
 	}
	/**
	* 获取
	*/
	public Date getJihuajungongriqi() { 
 		return super.getDate("jihuajungongriqi");
 	}
	/**
	* 设置
	*/
	public void setJihuajungongriqi(Date JIHUAJUNGONGRIQI) { 
 		super.set("jihuajungongriqi", JIHUAJUNGONGRIQI);
 	}
	/**
	* 获取
	*/
	public String getHetongjiage() { 
 		return super.getStr("hetongjiage");
 	}
	/**
	* 设置
	*/
	public void setHetongjiage(String HETONGJIAGE) { 
 		super.set("hetongjiage", HETONGJIAGE);
 	}
	/**
	* 获取
	*/
	public String getZhengzhaobeizhu() { 
 		return super.getStr("zhengzhaobeizhu");
 	}
	/**
	* 设置
	*/
	public void setZhengzhaobeizhu(String ZHENGZHAOBEIZHU) { 
 		super.set("zhengzhaobeizhu", ZHENGZHAOBEIZHU);
 	}
	/**
	* 获取
	*/
	public String getSuoshushengfen() { 
 		return super.getStr("suoshushengfen");
 	}
	/**
	* 设置
	*/
	public void setSuoshushengfen(String SUOSHUSHENGFEN) { 
 		super.set("suoshushengfen", SUOSHUSHENGFEN);
 	}
	/**
	* 获取
	*/
	public String getSuoshudishi() { 
 		return super.getStr("suoshudishi");
 	}
	/**
	* 设置
	*/
	public void setSuoshudishi(String SUOSHUDISHI) { 
 		super.set("suoshudishi", SUOSHUDISHI);
 	}
	/**
	* 获取
	*/
	public String getSuoshuxianqu() { 
 		return super.getStr("suoshuxianqu");
 	}
	/**
	* 设置
	*/
	public void setSuoshuxianqu(String SUOSHUXIANQU) { 
 		super.set("suoshuxianqu", SUOSHUXIANQU);
 	}
	/**
	* 获取
	*/
	public String getDept_organize_code() { 
 		return super.getStr("dept_organize_code");
 	}
	/**
	* 设置
	*/
	public void setDept_organize_code(String DEPT_ORGANIZE_CODE) { 
 		super.set("dept_organize_code", DEPT_ORGANIZE_CODE);
 	}
	/**
	* 获取
	*/
	public String getDept_name() { 
 		return super.getStr("dept_name");
 	}
	/**
	* 设置
	*/
	public void setDept_name(String DEPT_NAME) { 
 		super.set("dept_name", DEPT_NAME);
 	}
	/**
	* 获取
	*/
	public String getDistricts_code() { 
 		return super.getStr("districts_code");
 	}
	/**
	* 设置
	*/
	public void setDistricts_code(String DISTRICTS_CODE) { 
 		super.set("districts_code", DISTRICTS_CODE);
 	}
	/**
	* 获取
	*/
	public String getDistricts_name() { 
 		return super.getStr("districts_name");
 	}
	/**
	* 设置
	*/
	public void setDistricts_name(String DISTRICTS_NAME) { 
 		super.set("districts_name", DISTRICTS_NAME);
 	}
	/**
	* 获取
	*/
	public String getHolder_type() { 
 		return super.getStr("holder_type");
 	}
	/**
	* 设置
	*/
	public void setHolder_type(String HOLDER_TYPE) { 
 		super.set("holder_type", HOLDER_TYPE);
 	}
	/**
	* 获取
	*/
	public String getContact_phone() { 
 		return super.getStr("contact_phone");
 	}
	/**
	* 设置
	*/
	public void setContact_phone(String CONTACT_PHONE) { 
 		super.set("contact_phone", CONTACT_PHONE);
 	}
	/**
	* 获取
	*/
	public String getState() { 
 		return super.getStr("state");
 	}
	/**
	* 设置
	*/
	public void setState(String STATE) { 
 		super.set("state", STATE);
 	}
	/**
	* 获取
	*/
	public String getPermit_content() { 
 		return super.getStr("permit_content");
 	}
	/**
	* 设置
	*/
	public void setPermit_content(String PERMIT_CONTENT) { 
 		super.set("permit_content", PERMIT_CONTENT);
 	}
	/**
	* 获取
	*/
	public String getCertificate_level() { 
 		return super.getStr("certificate_level");
 	}
	/**
	* 设置
	*/
	public void setCertificate_level(String CERTIFICATE_LEVEL) { 
 		super.set("certificate_level", CERTIFICATE_LEVEL);
 	}
	/**
	* 获取
	*/
	public String getXiangmufenlei() { 
 		return super.getStr("xiangmufenlei");
 	}
	/**
	* 设置
	*/
	public void setXiangmufenlei(String XIANGMUFENLEI) { 
 		super.set("xiangmufenlei", XIANGMUFENLEI);
 	}
	/**
	* 获取
	*/
	public String getFujianbeizhu() { 
 		return super.getStr("fujianbeizhu");
 	}
	/**
	* 设置
	*/
	public void setFujianbeizhu(String FUJIANBEIZHU) { 
 		super.set("fujianbeizhu", FUJIANBEIZHU);
 	}
	/**
	* 获取
	*/
	public String getZongjianzhumianji() { 
 		return super.getStr("zongjianzhumianji");
 	}
	/**
	* 设置
	*/
	public void setZongjianzhumianji(String ZONGJIANZHUMIANJI) { 
 		super.set("zongjianzhumianji", ZONGJIANZHUMIANJI);
 	}
	/**
	* 获取
	*/
	public String getDixiajianzhumianji() { 
 		return super.getStr("dixiajianzhumianji");
 	}
	/**
	* 设置
	*/
	public void setDixiajianzhumianji(String DIXIAJIANZHUMIANJI) { 
 		super.set("dixiajianzhumianji", DIXIAJIANZHUMIANJI);
 	}
	/**
	* 获取
	*/
	public String getDishangjianzhumianji() { 
 		return super.getStr("dishangjianzhumianji");
 	}
	/**
	* 设置
	*/
	public void setDishangjianzhumianji(String DISHANGJIANZHUMIANJI) { 
 		super.set("dishangjianzhumianji", DISHANGJIANZHUMIANJI);
 	}
	/**
	* 获取
	*/
	public String getGongchengxiangmubianma() { 
 		return super.getStr("gongchengxiangmubianma");
 	}
	/**
	* 设置
	*/
	public void setGongchengxiangmubianma(String GONGCHENGXIANGMUBIANMA) { 
 		super.set("gongchengxiangmubianma", GONGCHENGXIANGMUBIANMA);
 	}
	/**
	* 获取
	*/
	public String getGongchengxiangmudaima() { 
 		return super.getStr("gongchengxiangmudaima");
 	}
	/**
	* 设置
	*/
	public void setGongchengxiangmudaima(String GONGCHENGXIANGMUDAIMA) { 
 		super.set("gongchengxiangmudaima", GONGCHENGXIANGMUDAIMA);
 	}
	/**
	* 获取
	*/
	public String getJianshedanwei() { 
 		return super.getStr("jianshedanwei");
 	}
	/**
	* 设置
	*/
	public void setJianshedanwei(String JIANSHEDANWEI) { 
 		super.set("jianshedanwei", JIANSHEDANWEI);
 	}
	/**
	* 获取
	*/
	public String getJianshedanweidaimayi() { 
 		return super.getStr("jianshedanweidaimayi");
 	}
	/**
	* 设置
	*/
	public void setJianshedanweidaimayi(String JIANSHEDANWEIDAIMAYI) { 
 		super.set("jianshedanweidaimayi", JIANSHEDANWEIDAIMAYI);
 	}
	/**
	* 获取
	*/
	public String getJianshedanweidaimaleixing() { 
 		return super.getStr("jianshedanweidaimaleixing");
 	}
	/**
	* 设置
	*/
	public void setJianshedanweidaimaleixing(String JIANSHEDANWEIDAIMALEIXING) { 
 		super.set("jianshedanweidaimaleixing", JIANSHEDANWEIDAIMALEIXING);
 	}
	/**
	* 获取
	*/
	public String getJianshedanweixiangmufuzeren() { 
 		return super.getStr("jianshedanweixiangmufuzeren");
 	}
	/**
	* 设置
	*/
	public void setJianshedanweixiangmufuzeren(String JIANSHEDANWEIXIANGMUFUZEREN) { 
 		super.set("jianshedanweixiangmufuzeren", JIANSHEDANWEIXIANGMUFUZEREN);
 	}
	/**
	* 获取
	*/
	public String getJsdwxmfzrsjh_29() { 
 		return super.getStr("jsdwxmfzrsjh_29");
 	}
	/**
	* 设置
	*/
	public void setJsdwxmfzrsjh_29(String JSDWXMFZRSJH_29) { 
 		super.set("jsdwxmfzrsjh_29", JSDWXMFZRSJH_29);
 	}
	/**
	* 获取
	*/
	public String getJsdwxmfzrsfzjhm_30() { 
 		return super.getStr("jsdwxmfzrsfzjhm_30");
 	}
	/**
	* 设置
	*/
	public void setJsdwxmfzrsfzjhm_30(String JSDWXMFZRSFZJHM_30) { 
 		super.set("jsdwxmfzrsfzjhm_30", JSDWXMFZRSFZJHM_30);
 	}
	/**
	* 获取
	*/
	public String getJsdwxmfzrsfzjhmlx() { 
 		return super.getStr("jsdwxmfzrsfzjhmlx");
 	}
	/**
	* 设置
	*/
	public void setJsdwxmfzrsfzjhmlx(String JSDWXMFZRSFZJHMLX) { 
 		super.set("jsdwxmfzrsfzjhmlx", JSDWXMFZRSFZJHMLX);
 	}
	/**
	* 获取
	*/
	public String getKanchadanwei() { 
 		return super.getStr("kanchadanwei");
 	}
	/**
	* 设置
	*/
	public void setKanchadanwei(String KANCHADANWEI) { 
 		super.set("kanchadanwei", KANCHADANWEI);
 	}
	/**
	* 获取
	*/
	public String getKanchadanweidaima() { 
 		return super.getStr("kanchadanweidaima");
 	}
	/**
	* 设置
	*/
	public void setKanchadanweidaima(String KANCHADANWEIDAIMA) { 
 		super.set("kanchadanweidaima", KANCHADANWEIDAIMA);
 	}
	/**
	* 获取
	*/
	public String getKanchaxiangmufuzeren() { 
 		return super.getStr("kanchaxiangmufuzeren");
 	}
	/**
	* 设置
	*/
	public void setKanchaxiangmufuzeren(String KANCHAXIANGMUFUZEREN) { 
 		super.set("kanchaxiangmufuzeren", KANCHAXIANGMUFUZEREN);
 	}
	/**
	* 获取
	*/
	public String getKcdwxmfzrsfzjhm_35() { 
 		return super.getStr("kcdwxmfzrsfzjhm_35");
 	}
	/**
	* 设置
	*/
	public void setKcdwxmfzrsfzjhm_35(String KCDWXMFZRSFZJHM_35) { 
 		super.set("kcdwxmfzrsfzjhm_35", KCDWXMFZRSFZJHM_35);
 	}
	/**
	* 获取
	*/
	public String getKcdwxmfzrsfzjhmlx() { 
 		return super.getStr("kcdwxmfzrsfzjhmlx");
 	}
	/**
	* 设置
	*/
	public void setKcdwxmfzrsfzjhmlx(String KCDWXMFZRSFZJHMLX) { 
 		super.set("kcdwxmfzrsfzjhmlx", KCDWXMFZRSFZJHMLX);
 	}
	/**
	* 获取
	*/
	public String getKcdwxmfzrsjh_37() { 
 		return super.getStr("kcdwxmfzrsjh_37");
 	}
	/**
	* 设置
	*/
	public void setKcdwxmfzrsjh_37(String KCDWXMFZRSJH_37) { 
 		super.set("kcdwxmfzrsjh_37", KCDWXMFZRSJH_37);
 	}
	/**
	* 获取
	*/
	public String getShejidanwei() { 
 		return super.getStr("shejidanwei");
 	}
	/**
	* 设置
	*/
	public void setShejidanwei(String SHEJIDANWEI) { 
 		super.set("shejidanwei", SHEJIDANWEI);
 	}
	/**
	* 获取
	*/
	public String getShejidanweidaima() { 
 		return super.getStr("shejidanweidaima");
 	}
	/**
	* 设置
	*/
	public void setShejidanweidaima(String SHEJIDANWEIDAIMA) { 
 		super.set("shejidanweidaima", SHEJIDANWEIDAIMA);
 	}
	/**
	* 获取
	*/
	public String getShejixiangmufuzeren() { 
 		return super.getStr("shejixiangmufuzeren");
 	}
	/**
	* 设置
	*/
	public void setShejixiangmufuzeren(String SHEJIXIANGMUFUZEREN) { 
 		super.set("shejixiangmufuzeren", SHEJIXIANGMUFUZEREN);
 	}
	/**
	* 获取
	*/
	public String getSjdwxmfzrsfzjhm_41() { 
 		return super.getStr("sjdwxmfzrsfzjhm_41");
 	}
	/**
	* 设置
	*/
	public void setSjdwxmfzrsfzjhm_41(String SJDWXMFZRSFZJHM_41) { 
 		super.set("sjdwxmfzrsfzjhm_41", SJDWXMFZRSFZJHM_41);
 	}
	/**
	* 获取
	*/
	public String getSjdwxmfzrsfzjhmlx() { 
 		return super.getStr("sjdwxmfzrsfzjhmlx");
 	}
	/**
	* 设置
	*/
	public void setSjdwxmfzrsfzjhmlx(String SJDWXMFZRSFZJHMLX) { 
 		super.set("sjdwxmfzrsfzjhmlx", SJDWXMFZRSFZJHMLX);
 	}
	/**
	* 获取
	*/
	public String getSjdwxmfzrsjh_43() { 
 		return super.getStr("sjdwxmfzrsjh_43");
 	}
	/**
	* 设置
	*/
	public void setSjdwxmfzrsjh_43(String SJDWXMFZRSJH_43) { 
 		super.set("sjdwxmfzrsjh_43", SJDWXMFZRSJH_43);
 	}
	/**
	* 获取
	*/
	public String getShigongdanwei() { 
 		return super.getStr("shigongdanwei");
 	}
	/**
	* 设置
	*/
	public void setShigongdanwei(String SHIGONGDANWEI) { 
 		super.set("shigongdanwei", SHIGONGDANWEI);
 	}
	/**
	* 获取
	*/
	public String getShigongdanweidaima() { 
 		return super.getStr("shigongdanweidaima");
 	}
	/**
	* 设置
	*/
	public void setShigongdanweidaima(String SHIGONGDANWEIDAIMA) { 
 		super.set("shigongdanweidaima", SHIGONGDANWEIDAIMA);
 	}
	/**
	* 获取
	*/
	public String getShigongxiangmufuzeren() { 
 		return super.getStr("shigongxiangmufuzeren");
 	}
	/**
	* 设置
	*/
	public void setShigongxiangmufuzeren(String SHIGONGXIANGMUFUZEREN) { 
 		super.set("shigongxiangmufuzeren", SHIGONGXIANGMUFUZEREN);
 	}
	/**
	* 获取
	*/
	public String getSgdwxmfzrsfzjhm_47() { 
 		return super.getStr("sgdwxmfzrsfzjhm_47");
 	}
	/**
	* 设置
	*/
	public void setSgdwxmfzrsfzjhm_47(String SGDWXMFZRSFZJHM_47) { 
 		super.set("sgdwxmfzrsfzjhm_47", SGDWXMFZRSFZJHM_47);
 	}
	/**
	* 获取
	*/
	public String getSgdwxmfzrsfzjhmlx() { 
 		return super.getStr("sgdwxmfzrsfzjhmlx");
 	}
	/**
	* 设置
	*/
	public void setSgdwxmfzrsfzjhmlx(String SGDWXMFZRSFZJHMLX) { 
 		super.set("sgdwxmfzrsfzjhmlx", SGDWXMFZRSFZJHMLX);
 	}
	/**
	* 获取
	*/
	public String getSgdwxmfzrsjh_49() { 
 		return super.getStr("sgdwxmfzrsjh_49");
 	}
	/**
	* 设置
	*/
	public void setSgdwxmfzrsjh_49(String SGDWXMFZRSJH_49) { 
 		super.set("sgdwxmfzrsjh_49", SGDWXMFZRSJH_49);
 	}
	/**
	* 获取
	*/
	public String getJianlidanwei() { 
 		return super.getStr("jianlidanwei");
 	}
	/**
	* 设置
	*/
	public void setJianlidanwei(String JIANLIDANWEI) { 
 		super.set("jianlidanwei", JIANLIDANWEI);
 	}
	/**
	* 获取
	*/
	public String getJianlidanweidaima() { 
 		return super.getStr("jianlidanweidaima");
 	}
	/**
	* 设置
	*/
	public void setJianlidanweidaima(String JIANLIDANWEIDAIMA) { 
 		super.set("jianlidanweidaima", JIANLIDANWEIDAIMA);
 	}
	/**
	* 获取
	*/
	public String getZongjianligongchengshi() { 
 		return super.getStr("zongjianligongchengshi");
 	}
	/**
	* 设置
	*/
	public void setZongjianligongchengshi(String ZONGJIANLIGONGCHENGSHI) { 
 		super.set("zongjianligongchengshi", ZONGJIANLIGONGCHENGSHI);
 	}
	/**
	* 获取
	*/
	public String getZjlgcssfzjhm_53() { 
 		return super.getStr("zjlgcssfzjhm_53");
 	}
	/**
	* 设置
	*/
	public void setZjlgcssfzjhm_53(String ZJLGCSSFZJHM_53) { 
 		super.set("zjlgcssfzjhm_53", ZJLGCSSFZJHM_53);
 	}
	/**
	* 获取
	*/
	public String getZjlgcssfzjhmlx_54() { 
 		return super.getStr("zjlgcssfzjhmlx_54");
 	}
	/**
	* 设置
	*/
	public void setZjlgcssfzjhmlx_54(String ZJLGCSSFZJHMLX_54) { 
 		super.set("zjlgcssfzjhmlx_54", ZJLGCSSFZJHMLX_54);
 	}
	/**
	* 获取
	*/
	public String getZjlgcssjh_55() { 
 		return super.getStr("zjlgcssjh_55");
 	}
	/**
	* 设置
	*/
	public void setZjlgcssjh_55(String ZJLGCSSJH_55) { 
 		super.set("zjlgcssjh_55", ZJLGCSSJH_55);
 	}
	/**
	* 获取
	*/
	public String getGongchengzongchengbaodanwei() { 
 		return super.getStr("gongchengzongchengbaodanwei");
 	}
	/**
	* 设置
	*/
	public void setGongchengzongchengbaodanwei(String GONGCHENGZONGCHENGBAODANWEI) { 
 		super.set("gongchengzongchengbaodanwei", GONGCHENGZONGCHENGBAODANWEI);
 	}
	/**
	* 获取
	*/
	public String getGczcbdwdm_57() { 
 		return super.getStr("gczcbdwdm_57");
 	}
	/**
	* 设置
	*/
	public void setGczcbdwdm_57(String GCZCBDWDM_57) { 
 		super.set("gczcbdwdm_57", GCZCBDWDM_57);
 	}
	/**
	* 获取
	*/
	public String getXiangmujingli() { 
 		return super.getStr("xiangmujingli");
 	}
	/**
	* 设置
	*/
	public void setXiangmujingli(String XIANGMUJINGLI) { 
 		super.set("xiangmujingli", XIANGMUJINGLI);
 	}
	/**
	* 获取
	*/
	public String getGczcbdwxmfzrsfzjhm() { 
 		return super.getStr("gczcbdwxmfzrsfzjhm");
 	}
	/**
	* 设置
	*/
	public void setGczcbdwxmfzrsfzjhm(String GCZCBDWXMFZRSFZJHM) { 
 		super.set("gczcbdwxmfzrsfzjhm", GCZCBDWXMFZRSFZJHM);
 	}
	/**
	* 获取
	*/
	public String getGczcbdwxmfzrsfzjhmlx() { 
 		return super.getStr("gczcbdwxmfzrsfzjhmlx");
 	}
	/**
	* 设置
	*/
	public void setGczcbdwxmfzrsfzjhmlx(String GCZCBDWXMFZRSFZJHMLX) { 
 		super.set("gczcbdwxmfzrsfzjhmlx", GCZCBDWXMFZRSFZJHMLX);
 	}
	/**
	* 获取
	*/
	public String getGczcbdwxmfzrsjh_61() { 
 		return super.getStr("gczcbdwxmfzrsjh_61");
 	}
	/**
	* 设置
	*/
	public void setGczcbdwxmfzrsjh_61(String GCZCBDWXMFZRSJH_61) { 
 		super.set("gczcbdwxmfzrsjh_61", GCZCBDWXMFZRSJH_61);
 	}
	/**
	* 获取
	*/
	public String getJzgcxmmxd_62() { 
 		return super.getStr("jzgcxmmxd_62");
 	}
	/**
	* 设置
	*/
	public void setJzgcxmmxd_62(String JZGCXMMXD_62) { 
 		super.set("jzgcxmmxd_62", JZGCXMMXD_62);
 	}
}