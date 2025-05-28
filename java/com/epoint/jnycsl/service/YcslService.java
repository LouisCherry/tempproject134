package com.epoint.jnycsl.service;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;

/**
 * 泰安一窗受理service
 * 受理/接件等操作，与部门自建系统的数据交互
 * @author 徐本能
 * @time 2018年12月11日下午6:00:55
 */
public interface YcslService {

	/**
	 * 推送办件接件/受理信息到部门自建系统
	 * @param project 办件对象
	 * @param auditTask 事项
	 * @param auditTaskExtension 事项扩展对象
	 * @return 成功推送返回true，否则返回false
	 */
	public boolean pushReceiveInfo2DeptSelfBuildSystem(AuditProject project, AuditTask auditTask,
			AuditTaskExtension auditTaskExtension);
	
	/**
	 * 上传文件到浪潮网盘，并把网盘路径id存入projectMaterial表中
	 * @param projectMaterial
	 */
	public void uploadFileToNetworkDisk(FrameAttachStorage storage, String projectMaterialGuid);
	
	/**
	 * 根据办件标识判断是否使用一窗受理
	 * @param projectGuid
	 * @return
	 */
	public boolean useYcslByProjectGuid(String projectGuid);
	
	/**
	 * 根据事项扩展对象判断是否使用一窗受理
	 * @param auditTaskExtension
	 * @return
	 */
	public boolean useYcslByTaskExtensionObj(AuditTaskExtension auditTaskExtension);
	
	/**
	 * 根据事项扩展对象判断是否使用一窗受理
	 * @param auditTaskExtension
	 * @return
	 */
	public boolean useYcslByTaskExtensionObj1(AuditTaskExtension auditTaskExtension);

	public boolean ycsllcbyprojectandtask(AuditTask audittask,AuditProject project);
			
}
