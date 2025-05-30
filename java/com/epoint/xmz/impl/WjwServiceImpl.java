package com.epoint.xmz.impl;

import java.util.Date;
import java.util.List;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.core.grammar.Record;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import com.epoint.xmz.api.IWjwService;
import com.epoint.xmz.api.IWjwService;

/**
 * 好差评相关接口的实现
 * @作者 atjiao
 * @version [版本号, 2020年6月8日]
 */
@Component
@Service
public class WjwServiceImpl implements IWjwService
{

    /**
     *
     */
    private static final long serialVersionUID = 3258268474743087728L;



	@Override
	public int insert(Record rec) {
		return new WjwService().insert(rec);
	}


	@Override
	public int insert(WorkflowWorkItem workflow) {
		return new WjwService().insert(workflow);
	}

	@Override
	public AuditTask getAuditBasicInfo(String taskName, String areacode) {
		return new WjwService().getAuditBasicInfo(taskName, areacode);
	}

	@Override
	public AuditTask getAuditBasicInfo(String item_id) {
		return new WjwService().getAuditBasicInfo(item_id);
	}

	@Override
	public Record getAuditProjectZjxtByRowguid(String rowguid) {
		return new WjwService().getAuditProjectZjxtByRowguid(rowguid);
	}

	@Override
	public Record getAuditRsApplyZjxtByRowguid(String rowguid) {
		return  new WjwService().getAuditRsApplyZjxtByRowguid(rowguid);
	}

    @Override
    public String getAreacodeByareaname(String areaname) {
        return  new WjwService().getAreacodeByareaname(areaname);
    }

	@Override
	public List<Record> getSCJGJApplyBaseInfo(Date beginOfDate, Date endOfDate) {
		return  new WjwService().getSCJGJApplyBaseInfo(beginOfDate, endOfDate);
	}

	@Override
	public List<Record> getSCJGJApplyProcess(Date beginOfDate, Date endOfDate) {
		return  new WjwService().getSCJGJApplyProcess(beginOfDate, endOfDate);
	}

	@Override
	public List<Record> getSCJGJApplyProcess(String projectid) {
		return  new WjwService().getSCJGJApplyProcess(projectid);
	}

	@Override
	public void upApplyBaseInfoSign(String rowguid,String status) {
		new WjwService().upApplyBaseInfoSign(rowguid,status);
	}

	@Override
	public void upApplyProcessSign(String rowguid) {
		new WjwService().upApplyProcessSign(rowguid);
	}

	@Override
	public AuditTask getAuditTaskInfo(String taskName, String areacode) {
		return new WjwService().getAuditTaskInfo(taskName, areacode);
	}

	@Override
	public List<Record> getSWJZQYDataAuditProjectZjxtByTime(Date startDate, Date endDate) {
		return new WjwService().getSWJZQYDataAuditProjectZjxtByTime(startDate, endDate);
	}

	@Override
	public AuditTask getAuditBasicInfoDetail(String taskName, String areacode) {
		return new WjwService().getAuditBasicInfoDetail(taskName, areacode);
	}


	@Override
	public int insertbyrecord(WorkflowWorkItem workflow) {
		return new WjwService().insertbyrecord(workflow);
	}


	public String getZjSlFlowsn(String custom) {
		return new WjwService().getZjSlFlowsn(custom);
	}


	@Override
	public void updateZjSlFlowsn(String flowsn,String custom){
		new WjwService().updateZjSlFlowsn(flowsn,custom);
	}

	public List<Record> getSCBDCRSData(Date beginOfDate, Date endOfDate){
		return  new WjwService().getSCBDCRSData(beginOfDate, endOfDate);
	}

	@Override
	public AuditTask getAuditBasicInfoByName(String taskname) {
		return new WjwService().getAuditBasicInfoByName(taskname);
	}
}
