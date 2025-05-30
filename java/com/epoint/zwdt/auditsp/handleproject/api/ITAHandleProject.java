package com.epoint.zwdt.auditsp.handleproject.api;

import java.util.List;

import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.common.service.AuditCommonResult;

public interface ITAHandleProject {

	AuditCommonResult<List<AuditProjectMaterial>> InitOnlineProjectReturnMaterials(String taskGuid, String centerGuid,
			String areaCode, String applyerGuid, String applyerUserName, String certNum, String projectguid,
			String taskCaseguid, String applyerType);

	AuditCommonResult<List<AuditProjectMaterial>> InitOnlineCompanyProjectReturnMaterials(String taskGuid,
			String centerGuid, String areaCode, String applyerGuid, String applyerUserName, String certNum,
			String projectguid, String taskCaseguid, String applyerType, String declarerName, String declarerGuid);
}
