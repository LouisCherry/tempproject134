package com.epoint.jnycsl.service;

import java.util.List;
import com.epoint.core.dto.model.SelectItem;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.jnycsl.domain.DeptSelfBuildSystem;
import com.epoint.jnycsl.dto.PageParam;

/**
 * 部门自建系统service
 * @author 刘雨雨
 * @time 2018年9月12日上午11:08:33
 */
public interface DeptSelfBuildSystemService {

	/**
	 * 根据taskId查找部门自建系统
	 * @param taskId 事项的taskId
	 * @return
	 */
	DeptSelfBuildSystem findDeptSelfBuildSystemByTaskId(String taskId);

	/**
	 * 查询部门自建系统列表
	 * @param param 业务参数对象
	 * @param pageParam 分页查询参数对象
	 * @return
	 */
	PageData<DeptSelfBuildSystem> getSelfBuildSystemPage(DeptSelfBuildSystem param, PageParam pageParam);

	/**
	 * 保存或更新SelfBuildSystem
	 * @param selfBuildSystem
	 * @return
	 */
	int saveOrUpdateSelfBuildSystem(DeptSelfBuildSystem selfBuildSystem);

	/**
	 * 查找SelfBuildSystem对象 by主键
	 * @param rowguid
	 * @return
	 */
	DeptSelfBuildSystem findSelfBuildSystemByRowGuid(String rowguid);

	/**
	 * 接口类型 代码项
	 * @return
	 */
	List<SelectItem> getApiTypes();

	/**
	 * 删除
	 * @param rowguids
	 * @return
	 */
	int deleteSelfBuildSystems(List<String> rowguids);

	/**
	 * 获取自建系统下拉列表
	 * @return
	 */
	List<SelectItem> getSelfBuildSystemSIList();

	/**
	 * 根据事项标识查询部门自建系统
	 * @param taskGuid
	 * @return
	 */
	String querySelfBuildSystemGuidByTaskGuid(String taskGuid);

	/**
	 * 根据事项的主键查询部门自建系统对象
	 * @param rowguid
	 * @return
	 */
	DeptSelfBuildSystem findDeptSelfBuildSystemByTaskGuid(String rowguid);
}
