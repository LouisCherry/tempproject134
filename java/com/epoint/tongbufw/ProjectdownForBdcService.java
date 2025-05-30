package com.epoint.tongbufw;

import java.util.List;

import org.apache.log4j.Logger;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.workflow.bizlogic.domain.execute.WorkflowWorkItemHistory;

public class ProjectdownForBdcService
{
    transient Logger log = LogUtil.getLog(ProjectdownForBdcService.class);

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

    public ProjectdownForBdcService() {
        commonDaoFrom = CommonDao.getInstance(dataSourceConfig);
        commonDaoTo = CommonDao.getInstance();
    }

    public ICommonDao getCommonDaoFrom() {
        return commonDaoFrom;
    }

    public ICommonDao getCommonDaoTo() {
        return commonDaoTo;
    }

    public int insertbyrecord(WorkflowWorkItemHistory workflow) {
        int result = commonDaoTo.insert(workflow);
        commonDaoTo.close();
        return result;
    }
    
    public AuditTask getAuditTask(String innerno,String areacode){
        String sql = "SELECT t.* from audit_task t "
                + " INNER JOIN audit_task_extension e on t.RowGuid = e.TASKGUID"
                + " where (IS_HISTORY=0 or IFNULL(IS_HISTORY,0)=0) and IS_EDITAFTERIMPORT=1"
                + " and IS_ENABLE=1 and innerno = ?1 and AREACODE=?2";
        AuditTask task = commonDaoTo.find(sql, AuditTask.class, innerno, areacode);
        commonDaoTo.close();
        return task;
    }
    

    public AuditTask gettaskbydeptask(String deptask,String areacode) {
        String sql = "SELECT b.* from zj_deptask a LEFT JOIN audit_task b ON a.itemid=b.item_id WHERE a.deptask=?1"
                + " and IFNULL(IS_HISTORY,0)=0 and IS_EDITAFTERIMPORT=1 and IS_ENABLE=1 and areacode = ?2";
        return CommonDao.getInstance().find(sql, AuditTask.class, deptask,areacode);
    }
    
    
    public AuditProject getProjectByflowsn(String flowsn){
        String sql = "SELECT * from audit_project WHERE flowsn =?";
        AuditProject project = commonDaoTo.find(sql, AuditProject.class, flowsn);
        commonDaoTo.close();
        return project;
    }
    
    
    /** 以下为获取浪潮前置库信息语句以及更新标识位语句**/
    
    public List<Record> getProcFromQzkByProjid(String flowsn){
        String sql = "SELECT * from EA_JC_STEP_PROC_BDC "
                + " where NVL(gtsync,'0')=0 and projid=? ORDER BY to_number(sn)";
        List<Record> recordList = commonDaoFrom.findList(sql, Record.class,flowsn);
        commonDaoFrom.close();
        return recordList;
    }
    
    public List<Record> getDoneFromQzkByProjid(String flowsn){
        String sql = "SELECT * from EA_JC_STEP_DONE_BDC "
                + " where NVL(gtsync,'0')=0 and projid=? ";
        List<Record> recordList = commonDaoFrom.findList(sql, Record.class,flowsn);
        commonDaoFrom.close();
        return recordList;
    }
    
    //办件同步状态，null：未同步，0：同步未办结，1：同步并办结，2：同步但事项不存在，3：同步但报错
    public List<Record> getDoneFromQzk(){
        String sql = "SELECT PROJID from EA_JC_STEP_DONE_BDC where NVL(gtsync,'0')='0' and rownum <= 20 and OCCURTIME >= to_date('2021/02/03 00:00:00', 'YYYY/MM/DD HH24:MI:SS') " ;
        List<Record> recordList = commonDaoFrom.findList(sql, Record.class);
        commonDaoFrom.close();
        return recordList;
    }

    //办件同步状态，null：未同步，0：同步未办结，1：同步并办结，2：同步但事项不存在，3：同步但报错
    public List<Record> getInfoFromQzk(String fields){
        String sql = "SELECT "+fields+" from EA_JC_STEP_BASICINFO_BDC where NVL(gtsync,'0')='0' and rownum <= 20 and OCCURTIME >= to_date('2021/02/03 00:00:00', 'YYYY/MM/DD HH24:MI:SS') ";
        List<Record> recordList = commonDaoFrom.findList(sql, Record.class);
        commonDaoFrom.close();
        return recordList;
    }

    public int updateInfoFlagByFlowsn(String flag,String flowsn){
        String sql = "update EA_JC_STEP_BASICINFO_BDC set gtsync=? where projid=? ";
        int rtn = commonDaoFrom.execute(sql, flag, flowsn);
        commonDaoFrom.close();
        return rtn;
    }
    
    public int updateProcFlagByFlowsn(String flag,String flowsn,int sn){
        String sql = "update EA_JC_STEP_PROC_BDC set gtsync=? where projid=? and sn=?";
        int rtn = commonDaoFrom.execute(sql, flag, flowsn,sn);
        commonDaoFrom.close();
        return rtn;
    }
    
    public int updateDoneFlagByFlowsn(String flag,String flowsn){
        String sql = "update EA_JC_STEP_DONE_BDC set gtsync=? where projid=? ";
        int rtn = commonDaoFrom.execute(sql, flag, flowsn);
        commonDaoFrom.close();
        return rtn;
    }

}
