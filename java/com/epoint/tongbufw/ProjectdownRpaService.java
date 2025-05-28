package com.epoint.tongbufw;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;

public class ProjectdownRpaService
{
    transient Logger log = LogUtil.getLog(ProjectdownRpaService.class);

    /**
     * 数据库操作DAO
     */

    protected ICommonDao commonDaoTo;


    public ProjectdownRpaService() {
        commonDaoTo = CommonDao.getInstance();
    }

    public ICommonDao getCommonDaoTo() {
        return commonDaoTo;
    }

    public List<Record> getProjectbytsflag() {
        String sql = "select * from gsbz where status = '2' limit 50";
        List<Record> list = commonDaoTo.findList(sql, Record.class);
        return list;
    }
    
    public AuditProject getProjectbyflowsn(String flowsn) {
        String sql = "select * from audit_project where flowsn = ?";
        return commonDaoTo.find(sql, AuditProject.class, flowsn);
    }
    
    public int updateProjectByRowguid(String rowguid) {
    	String sql = "update audit_project set status = '90' where flowsn = '"+rowguid+"'";
    	int result = 0;
    	if(StringUtil.isNotBlank(rowguid)) {
    		 result = commonDaoTo.execute(sql);
    	} 
        return result;
    }
    
    public int updateGsbzByRowguid(String status,String rowguid) {
    	String sql = "update gsbz set status = ? where id = ?";
    	int result = commonDaoTo.execute(sql,status,rowguid);
        return result;
    }
    
}
