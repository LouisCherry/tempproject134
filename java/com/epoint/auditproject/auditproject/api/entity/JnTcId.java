package com.epoint.auditproject.auditproject.api.entity;
import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;
/**
* 实体类 
* 
* @作者 张彬彬 
* @version [版本号, 2020-05-22 11:04:46] 
*/

@Entity(table = "jn_tc_jd", id = "rowguid")
public class JnTcId extends BaseEntity {
	private static final long serialVersionUID = 1L;
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
	public String getYewuguid() { 
 		return super.getStr("yewuguid");
 	}
	/**
	* 设置
	*/
	public void setYewuguid(String yewuguid) { 
 		super.set("yewuguid", yewuguid);
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
	public void setBusinessguid(String businessguid) { 
 		super.set("businessguid", businessguid);
 	}
	/**
	* 获取经济性质
	*/
	public String getEcon() { 
 		return super.getStr("econ");
 	}
	/**
	* 设置经济性质
	*/
	public void setEcon(String econ) { 
 		super.set("econ", econ);
 	}
	/**
	* 获取职工人数
	*/
	public String getZgnumber() { 
 		return super.getStr("zgnumber");
 	}
	/**
	* 设置职工人数
	*/
	public void setZgnumber(String zgnumber) { 
 		super.set("zgnumber", zgnumber);
 	}
	/**
	* 获取应体检人数
	*/
	public String getTjnumber() { 
 		return super.getStr("tjnumber");
 	}
	/**
	* 设置应体检人数
	*/
	public void setTjnumber(String tjnumber) { 
 		super.set("tjnumber", tjnumber);
 	}
	/**
	* 获取固定资产（万元）
	*/
	public String getGdzc() { 
 		return super.getStr("gdzc");
 	}
	/**
	* 设置固定资产（万元）
	*/
	public void setGdzc(String gdzc) { 
 		super.set("gdzc", gdzc);
 	}
	/**
	* 获取使用面积
	*/
	public String getSymj() { 
 		return super.getStr("symj");
 	}
	/**
	* 设置使用面积
	*/
	public void setSymj(String symj) { 
 		super.set("symj", symj);
 	}
	/**
	* 获取竣工验收认可书号
	*/
	public String getJgysxksh() { 
 		return super.getStr("jgysxksh");
 	}
	/**
	* 设置竣工验收认可书号
	*/
	public void setJgysxksh(String jgysxksh) { 
 		super.set("jgysxksh", jgysxksh);
 	}
	/**
	* 获取申请许可项目
	*/
	public String getSqxkxm() { 
 		return super.getStr("sqxkxm");
 	}
	/**
	* 设置申请许可项目
	*/
	public void setSqxkxm(String sqxkxm) { 
 		super.set("sqxkxm", sqxkxm);
 	}
	/**
	* 获取卫生设施
	*/
	public String getWsss() { 
 		return super.getStr("wsss");
 	}
	/**
	* 设置卫生设施
	*/
	public void setWsss(String wsss) { 
 		super.set("wsss", wsss);
 	}
	/**
	* 获取设置位置
	*/
	public String getSzwz() { 
 		return super.getStr("szwz");
 	}
	/**
	* 设置设置位置
	*/
	public void setSzwz(String szwz) { 
 		super.set("szwz", szwz);
 	}
	/**
	* 获取设置内容
	*/
	public String getSznr() { 
 		return super.getStr("sznr");
 	}
	/**
	* 设置设置内容
	*/
	public void setSznr(String sznr) { 
 		super.set("sznr", sznr);
 	}
	/**
	* 获取结构及材料
	*/
	public String getJgjcl() { 
 		return super.getStr("jgjcl");
 	}
	/**
	* 设置结构及材料
	*/
	public void setJgjcl(String jgjcl) { 
 		super.set("jgjcl", jgjcl);
 	}
	/**
	* 获取亮化效果
	*/
	public String getLhxg() { 
 		return super.getStr("lhxg");
 	}
	/**
	* 设置亮化效果
	*/
	public void setLhxg(String lhxg) { 
 		super.set("lhxg", lhxg);
 	}
	/**
	* 获取规格
	*/
	public String getGg() { 
 		return super.getStr("gg");
 	}
	/**
	* 设置规格
	*/
	public void setGg(String gg) { 
 		super.set("gg", gg);
 	}
	/**
	* 获取项目名称
	*/
	public String getItemname() { 
 		return super.getStr("itemname");
 	}
	/**
	* 设置项目名称
	*/
	public void setItemname(String itemname) { 
 		super.set("itemname", itemname);
 	}
	/**
	* 获取项目编码
	*/
	public String getItemcode() { 
 		return super.getStr("itemcode");
 	}
	/**
	* 设置项目编码
	*/
	public void setItemcode(String itemcode) { 
 		super.set("itemcode", itemcode);
 	}
	/**
	* 获取项目地点
	*/
	public String getAddress() { 
 		return super.getStr("address");
 	}
	/**
	* 设置项目地点
	*/
	public void setAddress(String address) { 
 		super.set("address", address);
 	}
	/**
	* 获取申请类别
	*/
	public String getApplytype() { 
 		return super.getStr("applytype");
 	}
	/**
	* 设置申请类别
	*/
	public void setApplytype(String applytype) { 
 		super.set("applytype", applytype);
 	}
	/**
	* 获取许可方式
	*/
	public String getXktype() { 
 		return super.getStr("xktype");
 	}
	/**
	* 设置许可方式
	*/
	public void setXktype(String xktype) { 
 		super.set("xktype", xktype);
 	}
}