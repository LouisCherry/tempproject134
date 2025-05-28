package com.epoint.jnycsl.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.epoint.basic.authentication.UserSession;
import com.epoint.core.annotation.Entity;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.jnycsl.constant.ApiType;
import com.epoint.jnycsl.dao.impl.TaianBaseDaoImpl;
import com.epoint.jnycsl.domain.DeptSelfBuildSystem;
import com.epoint.jnycsl.dto.PageParam;
import com.epoint.jnycsl.service.DeptSelfBuildSystemService;

/**
 * 部门自建系统service
 * @author 刘雨雨
 * @time 2018年9月12日上午11:08:33
 */
@Component
@Service
public class DeptSelfBuildSystemServiceImpl implements DeptSelfBuildSystemService {

	@Override
	public DeptSelfBuildSystem findDeptSelfBuildSystemByTaskId(String taskId) {
		CommonDao commonDao = CommonDao.getInstance();
		String sql = "select selfBuildSystemGuid from audit_task where task_id = ?"
				+ " and (IS_HISTORY=0 or IS_HISTORY is null) and IS_EDITAFTERIMPORT=1 and istemplate='0'";
		String selfBuildSystemGuid = commonDao.queryString(sql, taskId);
		return findSelfBuildSystemByRowGuid(selfBuildSystemGuid);
	}

	@Override
	public PageData<DeptSelfBuildSystem> getSelfBuildSystemPage(DeptSelfBuildSystem param, PageParam pageParam) {
		CommonDao commonDao = CommonDao.getInstance();
		String tableName = DeptSelfBuildSystem.class.getAnnotation(Entity.class).table();
		String sql = "select * from " + tableName + " order by createdate desc";
		int rowCount = commonDao.queryInt(sql.replace("*", "count(*)"));
		List<DeptSelfBuildSystem> deptSelfBuildSystems = commonDao.findList(sql, pageParam.getStartIndex(),
				pageParam.getPageSize(), DeptSelfBuildSystem.class);
		PageData<DeptSelfBuildSystem> pageData = new PageData<DeptSelfBuildSystem>();
		pageData.setRowCount(rowCount);
		pageData.setList(deptSelfBuildSystems);
		return pageData;
	}

	@Override
	public int saveOrUpdateSelfBuildSystem(DeptSelfBuildSystem selfBuildSystem) {
		if (selfBuildSystem == null) {
			return 0;
		}
		CommonDao commonDao = CommonDao.getInstance();
		int affectRows = 0;
		if (StringUtil.isBlank(selfBuildSystem.getRowguid())) {
			selfBuildSystem.setRowguid(UUID.randomUUID().toString());
			selfBuildSystem.setOperateUserGuid(UserSession.getInstance().getUserGuid());
			selfBuildSystem.setOperateUserName(UserSession.getInstance().getDisplayName());
			selfBuildSystem.setCreateDate(new Date());
			selfBuildSystem.setLastMofifyDate(selfBuildSystem.getCreateDate());
			affectRows = commonDao.insert(selfBuildSystem);
		} else {
			selfBuildSystem.setLastMofifyDate(new Date());
			affectRows = commonDao.update(selfBuildSystem);
		}
		return affectRows;
	}

	@Override
	public DeptSelfBuildSystem findSelfBuildSystemByRowGuid(String rowguid) {
		return new TaianBaseDaoImpl().findEntity(DeptSelfBuildSystem.class, rowguid);
	}

	@Override
	public List<SelectItem> getApiTypes() {
		List<SelectItem> apiTypes = new ArrayList<>();
		apiTypes.add(new SelectItem(ApiType.HTTP.toString(), ApiType.HTTP.toString()));
		apiTypes.add(new SelectItem(ApiType.WEB_SERVICE.toString(), ApiType.WEB_SERVICE.toString()));
		return apiTypes;
	}

	@Override
	public int deleteSelfBuildSystems(List<String> rowguids) {
		if (CollectionUtils.isEmpty(rowguids)) {
			return 0;
		} else {
			String[] rowguidArray = new String[rowguids.size()];
			return new TaianBaseDaoImpl().deleteEntities(DeptSelfBuildSystem.class, rowguids.toArray(rowguidArray));
		}
	}

	@Override
	public List<SelectItem> getSelfBuildSystemSIList() {
		List<SelectItem> selfBuildSystemSIList = new ArrayList<>();
		// 传100，足够大，查全部
		PageData<DeptSelfBuildSystem> pageData = getSelfBuildSystemPage(null, new PageParam(0, 100));
		List<DeptSelfBuildSystem> selfBuildSystems = pageData.getList();
		for (DeptSelfBuildSystem selfBuildSystem : selfBuildSystems) {
			selfBuildSystemSIList.add(new SelectItem(selfBuildSystem.getRowguid(), selfBuildSystem.getAppName()));
		}
		return selfBuildSystemSIList;
	}

	@Override
	public String querySelfBuildSystemGuidByTaskGuid(String taskGuid) {
		if (StringUtil.isBlank(taskGuid)) {
			return "";
		} else {
			CommonDao commonDao = CommonDao.getInstance();
			String sql = "select SelfBuildSystemGuid from audit_task where rowguid= ?";
			return commonDao.queryString(sql, taskGuid);
		}
	}

	@Override
	public DeptSelfBuildSystem findDeptSelfBuildSystemByTaskGuid(String taskGuid) {
		CommonDao commonDao = CommonDao.getInstance();
		String sql = "select selfBuildSystemGuid from audit_task where rowguid = ?";
		String selfBuildSystemGuid = commonDao.queryString(sql, taskGuid);
		if (selfBuildSystemGuid != null) {
			return findSelfBuildSystemByRowGuid(selfBuildSystemGuid);
		} else {
			return null;
		}
	}

}
