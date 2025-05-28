/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. **/
package com.epoint.knowledge.common;

import java.util.Calendar;

import com.epoint.basic.bizlogic.sysconf.datasource.service.DataSourceService9;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.frame.service.metadata.datasource.entity.DataSource;
import com.epoint.workflow.service.common.entity.config.WorkflowActivity;
import com.epoint.workflow.service.common.entity.execute.WorkflowTransitionInstance;

public class CommonWorkflowImpl implements ICommonWorkflow {
	private ICommonDao commonDao;

	public CommonWorkflowImpl() {
		this.commonDao = CommonDao.getInstance();
	}

	public CommonWorkflowImpl(String dataSourceName) {
		//this.commonDao = CommonDao.getInstance(DataSourceService9.initDataSourceConfig(dataSourceName));
		this.commonDao = CommonDao.getInstance();
		
	}

	public CommonWorkflowImpl(DataSourceConfig dataSource) {
		this.commonDao = CommonDao.getInstance(dataSource);
	}

	public CommonWorkflowImpl(ICommonDao commonDao) {
		this.commonDao = commonDao;
	}

	@Override
    public WorkflowTransitionInstance getTansitionBySrcActIns(String activityInstanceGuid) {
		String sql = "select * from WORKFLOW_TRANSITION_INSTANCE where TGTACTINSTGUID =?1";
		return (WorkflowTransitionInstance) this.commonDao.find(sql, WorkflowTransitionInstance.class,
				new Object[]{activityInstanceGuid});
	}

	@Override
    public WorkflowActivity getActivityByGuid(String activiryGuid) {
		return (WorkflowActivity) this.commonDao.find(WorkflowActivity.class, activiryGuid);
	}

	@Override
    public String generateSerialNum(String rqstSource) {
		Calendar c = Calendar.getInstance();
		int year = c.get(1);
		int month = c.get(2) + 1;
		int day = c.get(5);
		Object[] args = new Object[]{"服务编号", rqstSource, Integer.valueOf(year), Integer.valueOf(4),
				Integer.valueOf(month), Integer.valueOf(day), Integer.valueOf(4)};
		String cserial = (String) this.commonDao.executeProcudureWithResult(8, -9, "CSP_Gen_Number", args);
		return cserial;
	}

	@Override
    public String getActivityGuidByTransitionGuid(String transitionguid) {
		String sql = "SELECT operationguid FROM WORKFLOW_ACTIVITY_OPERATION where transitionguid=\'" + transitionguid
				+ "\'";
		return (String) this.commonDao.find(sql, String.class, new Object[0]);
	}
}