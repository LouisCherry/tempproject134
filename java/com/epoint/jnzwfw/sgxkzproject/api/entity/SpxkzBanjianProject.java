package com.epoint.jnzwfw.sgxkzproject.api.entity;

import java.util.Date;
import com.epoint.core.annotation.Entity;
import com.epoint.core.BaseEntity;
/**
* 实体类 
* 
* @作者 张彬彬 
* @version [版本号, 2020-03-06 10:21:13] 
*/

@Entity(table = "spxkz_banjian_project", id = "rowguid")
public class SpxkzBanjianProject extends BaseEntity {
	private static final long serialVersionUID = 1L;
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
	public String getCertno() { 
 		return super.getStr("certno");
 	}
	/**
	* 设置
	*/
	public void setCertno(String CERTNO) { 
 		super.set("certno", CERTNO);
 	}
	/**
	* 获取
	*/
	public Date getAwarddate() { 
 		return super.getDate("awarddate");
 	}
	/**
	* 设置
	*/
	public void setAwarddate(Date AWARDDATE) { 
 		super.set("awarddate", AWARDDATE);
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
	public void setOuname(String OUName) { 
 		super.set("ouname", OUName);
 	}
	/**
	* 获取
	*/
	public String getXiangmumingchen() { 
 		return super.getStr("xiangmumingchen");
 	}
	/**
	* 设置
	*/
	public void setXiangmumingchen(String XiangMuMingChen) { 
 		super.set("xiangmumingchen", XiangMuMingChen);
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
	public void setXiangmufenlei(String XiangMuFenLei) { 
 		super.set("xiangmufenlei", XiangMuFenLei);
 	}
	/**
	* 获取
	*/
	public String getLixiangpifujiguan() { 
 		return super.getStr("lixiangpifujiguan");
 	}
	/**
	* 设置
	*/
	public void setLixiangpifujiguan(String LiXiangPiFuJiGuan) { 
 		super.set("lixiangpifujiguan", LiXiangPiFuJiGuan);
 	}
	/**
	* 获取
	*/
	public Date getLixiangpifushijian() { 
 		return super.getDate("lixiangpifushijian");
 	}
	/**
	* 设置
	*/
	public void setLixiangpifushijian(Date lixiangpifushijian) { 
 		super.set("lixiangpifushijian", lixiangpifushijian);
 	}
	/**
	* 获取
	*/
	public String getJianshegongchengguihuaxukezhen() { 
 		return super.getStr("jianshegongchengguihuaxukezhen");
 	}
	/**
	* 设置
	*/
	public void setJianshegongchengguihuaxukezhen(String JianSheGongChengGuiHuaXuKeZhen) { 
 		super.set("jianshegongchengguihuaxukezhen", JianSheGongChengGuiHuaXuKeZhen);
 	}
	/**
	* 获取
	*/
	public String getXiangmuzongjianzhumianjipingfa() { 
 		return super.getStr("xiangmuzongjianzhumianjipingfa");
 	}
	/**
	* 设置
	*/
	public void setXiangmuzongjianzhumianjipingfa(String xiangmuzongjianzhumianjipingfa) { 
 		super.set("xiangmuzongjianzhumianjipingfa", xiangmuzongjianzhumianjipingfa);
 	}
	/**
	* 获取
	*/
	public String getXiangmudishangjianzhumianjipin() { 
 		return super.getStr("xiangmudishangjianzhumianjipin");
 	}
	/**
	* 设置
	*/
	public void setXiangmudishangjianzhumianjipin(String xiangmudishangjianzhumianjipin) { 
 		super.set("xiangmudishangjianzhumianjipin", xiangmudishangjianzhumianjipin);
 	}
	/**
	* 获取
	*/
	public String getXiangmudixiajianzhumianjipingf() { 
 		return super.getStr("xiangmudixiajianzhumianjipingf");
 	}
	/**
	* 设置
	*/
	public void setXiangmudixiajianzhumianjipingf(String xiangmudixiajianzhumianjipingf) { 
 		super.set("xiangmudixiajianzhumianjipingf", xiangmudixiajianzhumianjipingf);
 	}
	/**
	* 获取
	*/
	public String getHetongjiagewanyuan() { 
 		return super.getStr("hetongjiagewanyuan");
 	}
	/**
	* 设置
	*/
	public void setHetongjiagewanyuan(String hetongjiagewanyuan) { 
 		super.set("hetongjiagewanyuan", hetongjiagewanyuan);
 	}
	/**
	* 获取
	*/
	public Date getHetongkaigongriqi() { 
 		return super.getDate("hetongkaigongriqi");
 	}
	/**
	* 设置
	*/
	public void setHetongkaigongriqi(Date hetongkaigongriqi) { 
 		super.set("hetongkaigongriqi", hetongkaigongriqi);
 	}
	/**
	* 获取
	*/
	public Date getHetongjungongriqi() { 
 		return super.getDate("hetongjungongriqi");
 	}
	/**
	* 设置
	*/
	public void setHetongjungongriqi(Date hetongjungongriqi) { 
 		super.set("hetongjungongriqi", hetongjungongriqi);
 	}
	/**
	* 获取
	*/
	public String getJianshedanweimingchen() { 
 		return super.getStr("jianshedanweimingchen");
 	}
	/**
	* 设置
	*/
	public void setJianshedanweimingchen(String jianshedanweimingchen) { 
 		super.set("jianshedanweimingchen", jianshedanweimingchen);
 	}
	/**
	* 获取
	*/
	public String getShigongzongchengbaoqiye() { 
 		return super.getStr("shigongzongchengbaoqiye");
 	}
	/**
	* 设置
	*/
	public void setShigongzongchengbaoqiye(String shigongzongchengbaoqiye) { 
 		super.set("shigongzongchengbaoqiye", shigongzongchengbaoqiye);
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
	public void setJianlidanwei(String jianlidanwei) { 
 		super.set("jianlidanwei", jianlidanwei);
 	}
	/**
	* 获取
	*/
	public String getShentudanwei() { 
 		return super.getStr("shentudanwei");
 	}
	/**
	* 设置
	*/
	public void setShentudanwei(String shentudanwei) { 
 		super.set("shentudanwei", shentudanwei);
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
	public void setShejidanwei(String shejidanwei) { 
 		super.set("shejidanwei", shejidanwei);
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
	public void setKanchadanwei(String kanchadanwei) { 
 		super.set("kanchadanwei", kanchadanwei);
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
	public void setContactperson(String contactperson) { 
 		super.set("contactperson", contactperson);
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
	public void setContactphone(String contactphone) { 
 		super.set("contactphone", contactphone);
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
}