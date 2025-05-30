package com.epoint.basic.auditsp.auditspitask.service;

import java.util.List;
import java.util.Map;

import com.epoint.basic.auditsp.auditspitask.domain.AuditSpITask;
import com.epoint.common.util.SQLManageUtil;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;

public class AuditSpITaskService{

    /**
     * 通用dao
     */
    private ICommonDao commonDao;
    
    public AuditSpITaskService() {
        commonDao = CommonDao.getInstance("sp");
    }
	public void updateProjectGuid(String subappGuid, String taskGuid, String projectGuid){
		String sql = "update audit_sp_i_task set projectguid=?1,status=1  where taskguid=?2 and subappguid=?3";
		commonDao.execute(sql, projectGuid,taskGuid,subappGuid);
	}

	public void updateProjectGuid(String rowguid, String projectGuid){
	    String sql = "update audit_sp_i_task set projectguid=?1,status=1  where rowguid=?2";
	    commonDao.execute(sql, projectGuid,rowguid);
	}
	
    public List<String> getTaskIDBySubappGuid(String subappGuid) {
        String sql = "select b.task_id from audit_sp_i_task a ,audit_task b where a.taskguid = b.rowguid and a.SUBAPPGUID = ? ";
        return commonDao.findList(sql, String.class, subappGuid);
    }
    public List<String> getProjectguidsBySubappGuid(String subappGuid) {
        String sql = "select projectguid from audit_sp_i_task where subappguid=?";
        return commonDao.findList(sql, String.class, subappGuid);
    }
    public void updateAuditSpITask(AuditSpITask auditspitask) {
        commonDao.update(auditspitask);
    }
    public List<String> getStringListByCondition(String field, Map<String, String> condition) {
        String sqle = new SQLManageUtil().buildSql(condition);
        String sql = "select "+field+" from audit_sp_i_task "; 
        return  commonDao.findList(sql+sqle, String.class);
    }
    
    public Integer deleteSpITaskBySubappGuid(String subappGuid) {
        String sql = "delete from audit_sp_i_task where subappGuid = ?";
        return commonDao.execute(sql,subappGuid);
    }
    public Integer countByCondition(Map<String, String> conditionMap) {
        String sqle = new SQLManageUtil().buildSql(conditionMap);
        String sql = "select count(rowguid) from audit_sp_i_task " + sqle; 
        return  commonDao.queryInt(sql);
    }
}
