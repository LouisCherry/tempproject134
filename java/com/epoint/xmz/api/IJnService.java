package com.epoint.xmz.api;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.grammar.Record;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;

/**
 * 济宁通用service
 * @作者 zym
 * @version [版本号, 2021年8月20日]
 */
public interface IJnService extends Serializable
{


    public int insert(Record rec);

    public int insert(WorkflowWorkItem workflow);

	/**
	 * 获取办件基本信息
	 * @param taskName 事项名称
	 * @param areacode 辖区编码
	 * @return 返回事项基本信息
	 */
	public AuditTask getAuditBasicInfo(String taskName, String areacode);
	/**
	 * 获取办件基本信息
	 * @param item_id 事项编码
	 * @return 返回事项基本信息
	 */
	public AuditTask getAuditBasicInfo(String item_id);

	/**
	 * 通过rowguid查询表中是已有否有对应数据
	 * @param rowguid
	 * @return
	 */
	public Record getAuditProjectZjxtByRowguid(String rowguid);

	/**
	 * 通过rowguid查询表中是已有否有对应数据
	 * @param rowguid
	 * @return
	 */
	public Record getAuditRsApplyZjxtByRowguid(String rowguid);

    public String getAreacodeByareaname(String areaname);

    /**
     * 获取前置库inf_apply_baseinfo_r表中市场监管局的办件信息
     * @return
     */
    public List<Record> getSCJGJApplyBaseInfo(Date beginOfDate, Date endOfDate);

	/**
	 * 获取前置库inf_apply_process_r表中市场监管局的办件流程
	 * @param beginOfDate 开始时间
	 * @param endOfDate 结束时间
	 * @return 信息列表
	 */
	public List<Record> getSCJGJApplyProcess(Date beginOfDate,Date endOfDate);
	/**
	 * 根据办件projectid查询办件流程信息
	 * @param projectid
	 * @return
	 */
	public List<Record> getSCJGJApplyProcess(String projectid);
	/**
	 * 更新前置库inf_apply_baseinfo_r表中市场监管局的办件信息的同步标志
	 * @param rowguid
	 */
	public void upApplyBaseInfoSign(String rowguid);
	/**
	 * 更新前置库inf_apply_process_r表中市场监管局的办件信息的同步标志
	 * @param rowguid
	 */
	public void upApplyProcessSign(String rowguid);
	/**
	 * 根据事项名称，和区域编码获取事项基本信息
	 * @param taskName 事项名称
	 * @param areacode 区域编码
	 * @return 事项基本信息
	 */
	public AuditTask getAuditTaskInfo(String taskName,String areacode);

	/**
	 * 查询数据库当天是否已有【省外建筑业企业入鲁报送基本信息】的同步信息
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<Record> getSWJZQYDataAuditProjectZjxtByTime(Date startDate,Date endDate);

	public AuditTask getAuditBasicInfoDetail(String taskName, String areacode);

	public int insertbyrecord(WorkflowWorkItem workflow);

	public String getZjSlFlowsn(String custom);

	void updateZjSlFlowsn(String flowsn,String custom);

	public List<Record> getSCBDCRSData(Date beginOfDate, Date endOfDate);

	/**
	 * 获取办件基本信息
	 * @param
	 * @return 返回事项基本信息
	 */
	public AuditTask getAuditBasicInfoByName(String taskname);
}
