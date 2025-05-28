package com.epoint.tongbufw;

import java.util.List;

import org.apache.log4j.Logger;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.workflow.bizlogic.domain.execute.WorkflowWorkItemHistory;

public class ProjectdownForYCBHService
{
    transient Logger log = LogUtil.getLog(ProjectdownForYCBHService.class);


    protected ICommonDao commonDaoTo;


    public ProjectdownForYCBHService() {
        commonDaoTo = CommonDao.getInstance();
    }


    public ICommonDao getCommonDaoTo() {
        return commonDaoTo;
    }

    public int insertbyrecord(WorkflowWorkItemHistory workflow) {
        int result = commonDaoTo.insert(workflow);
        return result;
    }
    
    public List<Record> getAreaList(){
        String sql = "SELECT * from jining_banjian_ycbh";
        List<Record> list = commonDaoTo.findList(sql, Record.class);
        return list;
    }
    
    public AuditTask getAuditTask(String innerno,String areacode){
        String sql = "SELECT t.* from audit_task t "
                + " INNER JOIN audit_task_extension e on t.RowGuid = e.TASKGUID"
                + " where (IS_HISTORY=0 or IFNULL(IS_HISTORY,0)=0) and IS_EDITAFTERIMPORT=1"
                + " and IS_ENABLE=1 and innerno = ?1 and AREACODE=?2";
        AuditTask task = commonDaoTo.find(sql, AuditTask.class, innerno, areacode);
        return task;
    }
    
    public AuditProject getProjectByflowsn(String flowsn){
        String sql = "SELECT rowguid,ACCEPTUSERDATE,PVIGUID from audit_project WHERE flowsn =?";
        AuditProject project = commonDaoTo.find(sql, AuditProject.class, flowsn);
        return project;
    }
}
