package com.epoint.auditproject.guiji.api;

import com.epoint.auditproject.guiji.dto.ReportResult;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;

import java.util.List;

/**
 * 办件信息上报service，通过接口
 */
public interface ProjectReportByInterfaceService {

	/**
	 * 	上报从未上报过的办件信息
	 */
	void reportNever();

	/**
	 * 	已上报失败的办件信息重新再上报
	 */
	void reportFailed();

	/**
	 * 	执行真正的上报逻辑
	 * @param projects 待上报的办件
	 */
	ReportResult startReport(List<AuditProject> projects);

	ReportResult reportBasic(AuditProject project);
}
