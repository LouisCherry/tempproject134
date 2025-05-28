package com.epoint.auditproject.guiji.dto;

import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 办件信息传输对象,办件受理人信息、办结信息、行政区划，数据采集版本号等信息放在这里
 * @author 刘雨雨
 * @time 2018年8月22日下午2:11:55
 */
public class ProjectDto implements Serializable {

	private static final long serialVersionUID = -1421205290277352518L;

	/**
	 * 受理行政区划 12位
	 */
	private String regionId;

	/**
	 * 办件查询密码
	 */
	private String searchPassword;

	/**
	 * 事项信息，承诺期限，受理期限，权利编码等
	 */
	private AuditTask auditTask;

	/**
	 * 事项材料guid和name的拼接字符串id:name,id2:name2,id3:name3....
	 */
	private String taskMaterialIdNames;

	/**
	 * 办件类型，承诺件还是上报件
	 */
	private String projectType;

	/**
	 * 默认承诺期限单位 1 工作日
	 */
	private String promiseLimitUnit;

	/**
	 * 法定期限单位 g 工作日
	 */
	private String legalLimitUnit;

	/**
	 * 办件提交方式
	 */
	private String applyFrom;

	/**
	 * 受理时间
	 */
	private Date shouliTime;

	/**
	 * 受理人name
	 */
	private String shouliRenName;

	/**
	 * 受理人guid
	 */
	private String shouliRenGuid;

	/**
	 * 办件受理部门
	 */
	private FrameOu acceptDept;

	/**
	 * 办结部门
	 */
	private FrameOu banjieDept;

	/**
	 * 办结人标识
	 */
	private String banjieRenGuid;

	/**
	 * 办结人姓名
	 */
	private String banjieRenName;

	/**
	 * 办结时间
	 */
	private Date banjieDate;

	/**
	 * 办结结果
	 */
	private BigDecimal doneResult;

	/**
	 * 数据交换标志位
	 */
	private String signState;

	/**
	 * 数据有效标识 默认1，有效  0无效
	 */
	private BigDecimal validityFlag;

	/**
	 * 办件的事项材料
	 */
	private List<AuditTaskMaterial> taskMaterials;

	/**
	 * 办件材料
	 */
	private List<AuditProjectMaterial> projectMaterials;

	/**
	 * 数据采集版本号
	 */
	private BigDecimal stdVer;

	/**
	 * 缴费信息
	 */
	private JiaoFeiDto jiaoFeiDto;

	/**
	 * 办件信息是否做过修改
	 */
	private boolean isModify;
	

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getSearchPassword() {
		return searchPassword;
	}

	public void setSearchPassword(String searchPassword) {
		this.searchPassword = searchPassword;
	}

	public AuditTask getAuditTask() {
		return auditTask;
	}

	public void setAuditTask(AuditTask auditTask) {
		this.auditTask = auditTask;
	}

	public String getTaskMaterialIdNames() {
		return taskMaterialIdNames;
	}

	public void setTaskMaterialIdNames(String taskMaterialIdNames) {
		this.taskMaterialIdNames = taskMaterialIdNames;
	}

	public FrameOu getAcceptDept() {
		return acceptDept;
	}

	public void setAcceptDept(FrameOu acceptDept) {
		this.acceptDept = acceptDept;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getPromiseLimitUnit() {
		return promiseLimitUnit;
	}

	public void setPromiseLimitUnit(String promiseLimitUnit) {
		this.promiseLimitUnit = promiseLimitUnit;
	}

	public String getLegalLimitUnit() {
		return legalLimitUnit;
	}

	public void setLegalLimitUnit(String legalLimitUnit) {
		this.legalLimitUnit = legalLimitUnit;
	}

	public String getApplyFrom() {
		return applyFrom;
	}

	public void setApplyFrom(String applyFrom) {
		this.applyFrom = applyFrom;
	}

	public Date getShouliTime() {
		return shouliTime;
	}

	public void setShouliTime(Date shouliTime) {
		this.shouliTime = shouliTime;
	}

	public String getShouliRenName() {
		return shouliRenName;
	}

	public void setShouliRenName(String shouliRenName) {
		this.shouliRenName = shouliRenName;
	}

	public String getShouliRenGuid() {
		return shouliRenGuid;
	}

	public void setShouliRenGuid(String shouliRenGuid) {
		this.shouliRenGuid = shouliRenGuid;
	}

	public String getSignState() {
		return signState;
	}

	public void setSignState(String signState) {
		this.signState = signState;
	}

	public BigDecimal getValidityFlag() {
		return validityFlag;
	}

	public void setValidityFlag(BigDecimal validityFlag) {
		this.validityFlag = validityFlag;
	}

	public List<AuditTaskMaterial> getTaskMaterials() {
		return taskMaterials;
	}

	public void setTaskMaterials(List<AuditTaskMaterial> taskMaterials) {
		this.taskMaterials = taskMaterials;
	}

	public List<AuditProjectMaterial> getProjectMaterials() {
		return projectMaterials;
	}

	public void setProjectMaterials(List<AuditProjectMaterial> projectMaterials) {
		this.projectMaterials = projectMaterials;
	}

	public FrameOu getBanjieDept() {
		return banjieDept;
	}

	public void setBanjieDept(FrameOu banjieDept) {
		this.banjieDept = banjieDept;
	}

	public String getBanjieRenGuid() {
		return banjieRenGuid;
	}

	public void setBanjieRenGuid(String banjieRenGuid) {
		this.banjieRenGuid = banjieRenGuid;
	}

	public String getBanjieRenName() {
		return banjieRenName;
	}

	public void setBanjieRenName(String banjieRenName) {
		this.banjieRenName = banjieRenName;
	}

	public BigDecimal getStdVer() {
		return stdVer;
	}

	public void setStdVer(BigDecimal stdVer) {
		this.stdVer = stdVer;
	}

	public Date getBanjieDate() {
		return banjieDate;
	}

	public void setBanjieDate(Date banjieDate) {
		this.banjieDate = banjieDate;
	}

	public BigDecimal getDoneResult() {
		return doneResult;
	}

	public void setDoneResult(BigDecimal doneResult) {
		this.doneResult = doneResult;
	}

	public JiaoFeiDto getJiaoFeiDto() {
		return jiaoFeiDto;
	}

	public void setJiaoFeiDto(JiaoFeiDto jiaoFeiDto) {
		this.jiaoFeiDto = jiaoFeiDto;
	}

	public boolean isModify() {
		return isModify;
	}

	public void setModify(boolean isModify) {
		this.isModify = isModify;
	}
	
}
