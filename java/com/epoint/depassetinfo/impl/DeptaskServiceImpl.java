package com.epoint.depassetinfo.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.dao.CommonDao;
import com.epoint.depassetinfo.api.IDeptaskservice;
import com.epoint.frame.service.organ.ou.entity.FrameOu;

@Component
@Service
public class DeptaskServiceImpl implements IDeptaskservice {

	@Override
	public List<FrameOu> findAll() {
		String sql="SELECT OUGUID,OUNAME FROM frame_ou "
		        + "WHERE 1=1 and DESCRIPTION = 'ZJBM'";
		return CommonDao.getInstance().findList(sql, FrameOu.class);
	}

	@Override
	public int findCountByPid(String ouguid) {
		String sql="SELECT count(1) FROM audit_task WHERE IS_ENABLE=1 AND (IS_HISTORY=0 or IS_HISTORY='' OR IS_HISTORY IS NULL) and IS_EDITAFTERIMPORT=1 AND OUGUID=?";
		return CommonDao.getInstance().queryInt(sql, ouguid);
	}

	@Override
	public List<AuditTask> findAllByPid(String guid) {
		String sql="SELECT TaskName,RowGuid,task_id,ITEM_ID,unid FROM audit_task WHERE IS_ENABLE=1 AND (IS_HISTORY=0 or IS_HISTORY='' OR IS_HISTORY IS NULL) and IS_EDITAFTERIMPORT=1 AND OUGUID=?";
		return CommonDao.getInstance().findList(sql, AuditTask.class,guid);
	}

	@Override
	public AuditTask gettaskbyguid(String taskguid) {
		String sql="SELECT TaskName,RowGuid,task_id,ITEM_ID,unid FROM audit_task WHERE IS_ENABLE=1 AND (IS_HISTORY=0 or IS_HISTORY='' OR IS_HISTORY IS NULL) and IS_EDITAFTERIMPORT=1 AND item_id=?";
		return CommonDao.getInstance().find(sql,AuditTask.class, taskguid);
	}

}
