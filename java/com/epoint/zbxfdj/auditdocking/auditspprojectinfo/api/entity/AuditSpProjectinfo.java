package com.epoint.zbxfdj.auditdocking.auditspprojectinfo.api.entity;

import com.epoint.core.BaseEntity;
import com.epoint.core.annotation.Entity;

import java.util.Date;


/**
 * 工程信息表实体
 * 
 * @author WZW
 * @version [版本号, 2022-12-07 14:07:28]
 */
@Entity(table = "AUDIT_SP_PROJECTINFO", id = "rowguid")
public class AuditSpProjectinfo extends BaseEntity implements Cloneable
{
    private static final long serialVersionUID = 1L;

	/**
	 * 对应主题实例唯一标识RowGuid
	 */
	public String getSpinstanceguid() {
		return super.get("spinstanceguid");
	}

	public void setSpinstanceguid (String spinstanceguid) {
		super.set("spinstanceguid",spinstanceguid);
	}

	/**
	 * 项目代码
	 */
	public String getProjectcode() {
		return super.get("projectcode");
	}

	public void setProjectcode (String projectcode) {
		super.set("projectcode",projectcode);
	}

	/**
	 * 工程名称
	 */
	public String getProjectname() {
		return super.get("projectname");
	}

	public void setProjectname (String projectname) {
		super.set("projectname",projectname);
	}

	/**
	 * 建设单位名称
	 */
	public String getConstructionname() {
		return super.get("constructionname");
	}

	public void setConstructionname (String constructionname) {
		super.set("constructionname",constructionname);
	}

	/**
	 * 建设单位统一社会信用代码
	 */
	public String getConstructionuscc() {
		return super.get("constructionuscc");
	}

	public void setConstructionuscc (String constructionuscc) {
		super.set("constructionuscc",constructionuscc);
	}

	/**
	 * 建设单位法定代表人（姓名和身份证号）
	 */
	public String getConstructionlegalrepresentative() {
		return super.get("constructionlegalrepresentative");
	}

	public void setConstructionlegalrepresentative (String constructionlegalrepresentative) {
		super.set("constructionlegalrepresentative",constructionlegalrepresentative);
	}

	/**
	 * 建设单位法定代表人联系电话
	 */
	public String getConstructionlegalcontact() {
		return super.get("constructionlegalcontact");
	}

	public void setConstructionlegalcontact (String constructionlegalcontact) {
		super.set("constructionlegalcontact",constructionlegalcontact);
	}

	/**
	 * 联系人
	 */
	public String getContact() {
		return super.get("contact");
	}

	public void setContact (String contact) {
		super.set("contact",contact);
	}

	/**
	 * 联系电话
	 */
	public String getContactno() {
		return super.get("contactno");
	}

	public void setContactno (String contactno) {
		super.set("contactno",contactno);
	}

	/**
	 * 项目所在区域编码
	 */
	public String getAreacode() {
		return super.get("areacode");
	}

	public void setAreacode (String areacode) {
		super.set("areacode",areacode);
	}

	/**
	 * 工程地址
	 */
	public String getConstructionsite() {
		return super.get("constructionsite");
	}

	public void setConstructionsite (String constructionsite) {
		super.set("constructionsite",constructionsite);
	}

	/**
	 * 经度
	 */
	public String getLongitude() {
		return super.getStr("longitude");
	}

	public void setLongitude (String longitude) {
		super.set("longitude",longitude);
	}

	/**
	 * 纬度
	 */
	public String getLatitude() {
		return super.getStr("latitude");
	}

	public void setLatitude (String latitude) {
		super.set("latitude",latitude);
	}

	/**
	 * 类别
	 */
	public String getBuildnaturecode() {
		return super.get("buildnaturecode");
	}

	public void setBuildnaturecode (String buildnaturecode) {
		super.set("buildnaturecode",buildnaturecode);
	}

	/**
	 * 建筑工程使用属性
	 */
	public String getProjecttype() {
		return super.get("projecttype");
	}

	public void setProjecttype (String projecttype) {
		super.set("projecttype",projecttype);
	}

	/**
	 * 有无建设工程规划许可
	 */
	public String getIsprojectplan() {
		return super.get("isprojectplan");
	}

	public void setIsprojectplan (String isprojectplan) {
		super.set("isprojectplan",isprojectplan);
	}

	/**
	 * 建设工程规划许可证件（依法需办理的）
	 */
	public String getBuildingpermit() {
		return super.get("buildingpermit");
	}

	public void setBuildingpermit (String buildingpermit) {
		super.set("buildingpermit",buildingpermit);
	}

	/**
	 * 临时性建筑批准文件（依法需办理的）
	 */
	public String getTempapprovepermit() {
		return super.get("tempapprovepermit");
	}

	public void setTempapprovepermit (String tempapprovepermit) {
		super.set("tempapprovepermit",tempapprovepermit);
	}

	/**
	 * 特殊消防设计
	 */
	public String getIsspecialdesign() {
		return super.get("isspecialdesign");
	}

	public void setIsspecialdesign (String isspecialdesign) {
		super.set("isspecialdesign",isspecialdesign);
	}

	/**
	 * 建筑高度大于250m的建筑采取加强性消防设计措施
	 */
	public String getIsstrengthen() {
		return super.get("isstrengthen");
	}

	public void setIsstrengthen (String isstrengthen) {
		super.set("isstrengthen",isstrengthen);
	}

	/**
	 * 审图编号
	 */
	public String getDrawingnumber() {
		return super.get("drawingnumber");
	}

	public void setDrawingnumber (String drawingnumber) {
		super.set("drawingnumber",drawingnumber);
	}

	/**
	 * 工程投资额(万元)
	 */
	public String getTotalinvestment() {
		return super.get("totalinvestment");
	}

	public void setTotalinvestment (String totalinvestment) {
		super.set("totalinvestment",totalinvestment);
	}

	/**
	 * 总建筑面积(㎡)
	 */
	public String getTotalbuildarea() {
		return super.get("totalbuildarea");
	}

	public void setTotalbuildarea (String totalbuildarea) {
		super.set("totalbuildarea",totalbuildarea);
	}

	/**
	 * 住建单位统一社会信用代码
	 */
	public String getCompanyuscc() {
		return super.get("companyuscc");
	}

	public void setCompanyuscc (String companyuscc) {
		super.set("companyuscc",companyuscc);
	}

	/**
	 * 住建单位名称
	 */
	public String getCompanyname() {
		return super.get("companyname");
	}

	public void setCompanyname (String companyname) {
		super.set("companyname",companyname);
	}

	/**
	 * 特殊建设工程情形
	 */
	public String getSpecialsituation() {
		return super.get("specialsituation");
	}

	public void setSpecialsituation (String specialsituation) {
		super.set("specialsituation",specialsituation);
	}

	/**
	 * 装修部位
	 */
	public String getDecorationpart() {
		return super.get("decorationpart");
	}

	public void setDecorationpart (String decorationpart) {
		super.set("decorationpart",decorationpart);
	}

	/**
	 * 装修面积（㎡）
	 */
	public String getDecorationarea() {
		return super.get("decorationarea");
	}

	public void setDecorationarea (String decorationarea) {
		super.set("decorationarea",decorationarea);
	}

	/**
	 * 装修所在层数
	 */
	public String getDecorationfloor() {
		return super.get("decorationfloor");
	}

	public void setDecorationfloor (String decorationfloor) {
		super.set("decorationfloor",decorationfloor);
	}

	/**
	 * 使用性质
	 */
	public String getNature() {
		return super.get("nature");
	}

	public void setNature (String nature) {
		super.set("nature",nature);
	}

	/**
	 * 原有用途
	 */
	public String getOriginalpurpose() {
		return super.get("originalpurpose");
	}

	public void setOriginalpurpose (String originalpurpose) {
		super.set("originalpurpose",originalpurpose);
	}

	/**
	 * 材料类别
	 */
	public String getMaterialscategory() {
		return super.get("materialscategory");
	}

	public void setMaterialscategory (String materialscategory) {
		super.set("materialscategory",materialscategory);
	}

	/**
	 * 保温所在层数
	 */
	public String getInsulationfloor() {
		return super.get("insulationfloor");
	}

	public void setInsulationfloor (String insulationfloor) {
		super.set("insulationfloor",insulationfloor);
	}

	/**
	 * 保温部位
	 */
	public String getInsulationpart() {
		return super.get("insulationpart");
	}

	public void setInsulationpart (String insulationpart) {
		super.set("insulationpart",insulationpart);
	}

	/**
	 * 保温材料
	 */
	public String getInsulationmaterials() {
		return super.get("insulationmaterials");
	}

	public void setInsulationmaterials (String insulationmaterials) {
		super.set("insulationmaterials",insulationmaterials);
	}

	/**
	 * 消防设施及其他
	 */
	public String getFirefacilitiescategory() {
		return super.get("firefacilitiescategory");
	}

	public void setFirefacilitiescategory (String firefacilitiescategory) {
		super.set("firefacilitiescategory",firefacilitiescategory);
	}

	/**
	 * 工程简要说明
	 */
	public String getBrief() {
		return super.get("brief");
	}

	public void setBrief (String brief) {
		super.set("brief",brief);
	}

	/**
	 * 操作日期
	 */
	public Date getOperatedate() {
		return super.getDate("operatedate");
	}

	public void setOperatedate (Date operatedate) {
		super.set("operatedate",operatedate);
	}

	/**
	 * 操作人所在独立部门Guid
	 */
	public String getOperateuserbaseouguid() {
		return super.get("operateuserbaseouguid");
	}

	public void setOperateuserbaseouguid (String operateuserbaseouguid) {
		super.set("operateuserbaseouguid",operateuserbaseouguid);
	}

	/**
	 * 所属辖区编号
	 */
	public String getBelongxiaqucode() {
		return super.get("belongxiaqucode");
	}

	public void setBelongxiaqucode (String belongxiaqucode) {
		super.set("belongxiaqucode",belongxiaqucode);
	}

	/**
	 * 主键guid
	 */
	public String getRowguid() {
		return super.get("rowguid");
	}

	public void setRowguid (String rowguid) {
		super.set("rowguid",rowguid);
	}

	/**
	 * 操作人所在部门Guid
	 */
	public String getOperateuserouguid() {
		return super.get("operateuserouguid");
	}

	public void setOperateuserouguid (String operateuserouguid) {
		super.set("operateuserouguid",operateuserouguid);
	}

	/**
	 * 序号
	 */
	public Integer getRow_id() {
		return super.getInt("row_id");
	}

	public void setRow_id (Integer row_id) {
		super.set("row_id",row_id);
	}

	/**
	 * 操作人姓名
	 */
	public String getOperateusername() {
		return super.get("operateusername");
	}

	public void setOperateusername (String operateusername) {
		super.set("operateusername",operateusername);
	}

	/**
	 * 年份标识
	 */
	public String getYearflag() {
		return super.get("yearflag");
	}

	public void setYearflag (String yearflag) {
		super.set("yearflag",yearflag);
	}

	/**
	 * 操作人Guid
	 */
	public String getOperateuserguid() {
		return super.get("operateuserguid");
	}

	public void setOperateuserguid (String operateuserguid) {
		super.set("operateuserguid",operateuserguid);
	}

	/**
	 * 流程标识
	 */
	public String getPviguid() {
		return super.get("pviguid");
	}

	public void setPviguid (String pviguid) {
		super.set("pviguid",pviguid);
	}
	/**
	 * 对应子申报唯一标识
	 */
	public String getSubappguid() {
		return super.get("subappguid");
	}

	public void setSubappguid (String subappguid) {
		super.set("subappguid",subappguid);
	}

	/**
	 * 办件标识
	 */
	public String getProjectguid() {
		return super.get("projectguid");
	}

	public void setProjectguid (String projectguid) {
		super.set("projectguid",projectguid);
	}

	/**
	 * 事项版本标识
	 */
	public String getTaskid() {
		return super.get("taskid");
	}

	public void setTaskid (String taskid) {
		super.set("taskid",taskid);
	}
}