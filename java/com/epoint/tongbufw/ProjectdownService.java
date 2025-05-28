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

public class ProjectdownService
{
    transient Logger log = LogUtil.getLog(ProjectdownService.class);

    /**
     * 数据库操作DAO
     */
    protected ICommonDao commonDaoFrom;

    protected ICommonDao commonDaoTo;
    private static String URL = ConfigUtil.getConfigValue("datasyncjdbc", "oldqzurl");
    private static String NAME = ConfigUtil.getConfigValue("datasyncjdbc", "oldqzusername");
    private static String PASSWORD = ConfigUtil.getConfigValue("datasyncjdbc", "oldqzpassword");

    /**
     * 前置库数据源
     */
    private DataSourceConfig dataSourceConfig = new DataSourceConfig(URL, NAME, PASSWORD);

    public ProjectdownService() {
        commonDaoFrom = CommonDao.getInstance(dataSourceConfig);
        commonDaoTo = CommonDao.getInstance();
    }

    public ICommonDao getCommonDaoFrom() {
        return commonDaoFrom;
    }

    public ICommonDao getCommonDaoTo() {
        return commonDaoTo;
    }

    public List<AuditProject> getProjectbytsflag() {
        String sql = "select a.* from audit_project a join audit_task b on a.taskguid = b.rowguid WHERE ISSYNACWAVE='1' AND STATUS<> 90 and STATUS<> 99 and (is_lczj is null or is_lczj='4') and b.shenpilb = '01' and ACCEPTUSERDATE >= date_sub(NOW(), interval 2 Month)  ORDER BY ACCEPTUSERDATE desc limit 0,200";
        List<AuditProject> list = commonDaoTo.findList(sql, AuditProject.class);
        return list;
    }
    
    public List<AuditProject> getProjectbytsflag1() {
        String sql = "select a.* from audit_project a join audit_task b on a.taskguid = b.rowguid WHERE ISSYNACWAVE='1' AND STATUS<> 90 and STATUS<> 99 and (is_lczj is null or is_lczj='4') and b.shenpilb = '01' and ACCEPTUSERDATE >= date_sub(NOW(), interval 2 Month)  ORDER BY ACCEPTUSERDATE desc limit 200,300";
        List<AuditProject> list = commonDaoTo.findList(sql, AuditProject.class);
        return list;
    }
    

    public List<Record> getprocbyflowsn(String flowsn) {
        String sql = "select * from APPROVE_BUSINESS_COURSE t where Receive_number= '"+flowsn+"' and flag is null ORDER BY SEND_TIME ASC";
        List<Record> recordList = commonDaoFrom.findList(sql, Record.class, flowsn);
        return recordList;
    }

    public int insertbyrecord(WorkflowWorkItem workflow) {
        int result = commonDaoTo.insert(workflow);
        return result;
    }

    public int updateflag(String sn) {
        String sql = "update APPROVE_BUSINESS_COURSE set flag='1' where id=?";
        int result = commonDaoFrom.execute(sql, sn);
        return result;
    }
    
    public List<Record> getProjectFromLcqzk(){
        String sql = "SELECT DISTINCT RECEIVE_NUMBER,ITEM_NAME,REGION_CODE,ORG_CODE from APPROVE_BUSINESS_COURSE_ZJ where NVL(FLAG,'0')=0";
        List<Record> recordList = commonDaoFrom.findList(sql, Record.class);
        return recordList;
    }
    
    public int updateLcqzkFlagByFlowsn(String flowsn,String flag){
        String sql = "update APPROVE_BUSINESS_COURSE_ZJ set flag=?1 where RECEIVE_NUMBER=?2";
        int result = commonDaoFrom.execute(sql, flag, flowsn);
        return result;
    }
    
    public int updateLcqzkFlagByflowsnAndSn(String flowsn,String sn,String flag){
        String sql = "update APPROVE_BUSINESS_COURSE_ZJ set flag=?1 where RECEIVE_NUMBER=?2"
                + " and SN = ?3";
        int result = commonDaoFrom.execute(sql, flag, flowsn, sn);
        return result;
    }
    
    public AuditTask getAuditTask(String taskname,String areacode,String oucode){
        String sql = "SELECT t.* from audit_task t INNER JOIN frame_ou o on t.OUGUID = o.OUGUID"
                + " where IS_HISTORY=0 and IS_EDITAFTERIMPORT=1"
                + " and IS_ENABLE=1 and taskname = ?1 and AREACODE=?2 and OUCODE=?3";
        AuditTask task = commonDaoTo.find(sql, AuditTask.class, taskname,areacode,oucode);
        return task;
    }
    
    public List<Record> getProjectFromLcqzkByFlowsn(String fields,String flowsn){
        String sql = "SELECT DISTINCT id "+fields+" from APPROVE_BUSINESS_COURSE_ZJ "
                + " where NVL(FLAG,'0')=0 and RECEIVE_NUMBER=? ORDER BY to_number(sn)";
        List<Record> recordList = commonDaoFrom.findList(sql, Record.class,flowsn);
        return recordList;
    } 
    
    public AuditProject getProjectByflowsn(String flowsn){
        String sql = "SELECT * from audit_project WHERE flowsn =?";
        AuditProject project = commonDaoTo.find(sql, AuditProject.class, flowsn);
        return project;
    }
    
    /**************************************挂起办件同步逻辑  *********************************************/
    public List<Record> getProjectGqcqzkByFlag(){
        String sql = "SELECT id,RECEIVE_NUMBER,STATUS,ACTIVE from APPROVE_BUSINESS_COURSE_GQ "
                + " where NVL(FLAG,'0')=0 ";
        List<Record> recordList = commonDaoFrom.findList(sql, Record.class);
        return recordList;
    } 
    
    public int updateGqflag(String id,String flag) {
        String sql = "update APPROVE_BUSINESS_COURSE_GQ set flag=?1 where id=?2";
        int result = commonDaoFrom.execute(sql,flag, id);
        return result;
    }
    
    public Record getProjectByFlowsn(String flowsn){
        String sql = "SELECT p.rowguid as projectguid, p.status ,s.rowguid as spareguid,p.task_id,p.areacode from audit_project p inner join audit_project_sparetime s on p.rowguid = s.projectguid "
                + " WHERE flowsn =? and status < 90";
        Record project = commonDaoTo.find(sql, Record.class, flowsn);
        return project;
    }
    
    public int updatePauseByGuid(String projectguid, int pause) {
        String sql1 = "update audit_project set IS_PAUSE="+pause+",OperateUserName='浪潮自动挂起',OperateDate=now() where rowguid=?";
        int result = 0;
        if(StringUtil.isNotBlank(projectguid)){
            result += commonDaoTo.execute(sql1, projectguid);
        }
        return result;
    }
    
    public int updateProjectByRowguid(String projectguid, String banjiedate, int Status, String banjieusername, int Banjieresult, String operateusername) {
        String sql = "update audit_project set status = "+ Status + ",";
        if (StringUtil.isNotBlank(banjiedate)) {
            sql += "banjiedate= '" +banjiedate +"',";
        }
        if (StringUtil.isNotBlank(banjieusername)) {
            sql += "banjieusername= '" +banjieusername +"',";
        }
        if (StringUtil.isNotBlank(Banjieresult) && Banjieresult != 0) {
            sql +=  "Banjieresult= " +Banjieresult +" ,";
        }
        if (StringUtil.isNotBlank(operateusername)) {
            sql +=  "OperateUserName= '" +operateusername +"',";
        }
        if (sql.endsWith(",")) {
            sql = sql.substring(0, sql.length() -1);
        }
        sql += " where rowguid = '" + projectguid + "'";
        int result = commonDaoTo.execute(sql);
        return result;
    }
    
    public int updateOnlineProjectByRowguid( String status, String sourceguid) {
        String sql = "update audit_online_project set status = '"+status+"' where sourceguid = '"+sourceguid+"'";
        int result = commonDaoTo.execute(sql);
        return result;
    }
    
    public int updateProjectIssynacwaveByRowguid( String ISSYNACWAVE, String rowguid) {
        String sql = "update audit_project set ISSYNACWAVE = '"+ISSYNACWAVE+"' where rowguid = '"+rowguid+"'";
        int result = commonDaoTo.execute(sql);
        return result;
    }
    
}
