package com.epoint.basic.audittask.basic.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.epoint.basic.audittask.basic.api.IJnAuditTaskService;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;

@Component
@Service
public class JnAuditTaskImpl implements IJnAuditTaskService
{

	@Override
	public PageData<AuditTask> getUseTaskList(String fileds, Map<String, String> map, int first, int pageSize,
			String sortField, String sortOrder) {
		  PageData<AuditTask> pageData = new PageData<AuditTask>();
	        SQLManageUtil sm = new SQLManageUtil("task", true);
	        String sqlbuild = sm.buildPatchSql(map);
	        String order = "";
	        if (StringUtil.isNotBlank(sortField) && StringUtil.isNotBlank(sortOrder)) {
	            order = " order by " + sortField + " " + sortOrder;
	        }
	        //String sqle = sm.buildSql(map);
	        String sqle = " where (IS_ENABLE = '1' AND ISTEMPLATE = '0' "
	                + sqlbuild + " AND ifnull(IS_HISTORY, '0') = '0' AND IS_EDITAFTERIMPORT = 1)";
	        //String sql = "select " + fileds + " from view_enabledtask " + sqle + order;
	        String sql = "select " + fileds + " from audit_task " + sqle + order;
	        String sqlcount = sql.replace(fileds, "count(*)");
	        List<AuditTask> auditTaskList = CommonDao.getInstance().findList(sql, first, pageSize, AuditTask.class);
	        pageData.setList(auditTaskList);
	        pageData.setRowCount(CommonDao.getInstance().queryInt(sqlcount));
	        return pageData;
	}
	
	
	@Override
	public List<Record> getXinYongTaskList() {
	        String sql = "select a.taskName,a.item_id,a.OUNAME from audit_task a where task_id is not null and IFNULL(IS_HISTORY,0)= 0 and is_enable = 1 and IS_EDITAFTERIMPORT = 1 ";
	        sql += " and areacode = '370800' and ouguid = 'cd64be32-6538-486f-8d8e-bf4b15bf4e47' and a.shenpilb in ('01','10','07','05','08','06','11') and taskcode <> '' and a.businesstype = '1'";
	        List<Record> auditTaskList = CommonDao.getInstance().findList(sql,Record.class);
	        return auditTaskList;
	}
    
}
