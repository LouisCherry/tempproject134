package com.epoint.jnycsl.dao;

import org.springframework.stereotype.Repository;
import com.epoint.core.dao.CommonDao;
import com.epoint.jnycsl.constant.PushStatus;

/**
 * 一窗受理dao
 * @author 刘雨雨
 * @time 2018年9月19日上午9:37:11
 */
@Repository
public class YcslDao {
	
	public void updateNetworkDiskPathOfProjectMaterial(String fileId,String projectMaterialGuid) {
		CommonDao commonDao = CommonDao.getInstance();
		commonDao.beginTransaction();
		String sql = "update AUDIT_PROJECT_MATERIAL set networkDiskPath = ? where rowguid = ?";
		commonDao.execute(sql, fileId, projectMaterialGuid);
		commonDao.commitTransaction();
	}
	
	public Object getFiledOfTaskExtensionByProjectGuid(String columnName, String projectGuid) {
		CommonDao commonDao = CommonDao.getInstance();
		String sql = "select " + columnName + " from AUDIT_TASK_EXTENSION where taskguid = "
				+ "(select taskguid from audit_project where rowguid = ?)";
		Object obj = commonDao.find(sql, Object.class, projectGuid);
		return obj;
	}
	
	public void updateProjectPushStatus(PushStatus pushStatus, String errorText, String projectGuid) {
		CommonDao commonDao = CommonDao.getInstance();
		String sql = "update audit_project set ycsl_sign = ?,ycsl_sign_date=now(),ycsl_errortext =? where rowguid = ?";
		commonDao.execute(sql, pushStatus.getVal(), errorText, projectGuid);
	}
	
	
	
}
