package com.epoint.zwdt.zwdtrest.yyyz.impl;

import java.util.List;

import org.xm.similarity.util.StringUtil;

import com.epoint.basic.auditonlineuser.auditonlineproject.domain.AuditOnlineProject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.dao.ICommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.esotericsoftware.minlog.Log;

public class DaTingService
{   
    //  6大领域的今天取号量 当前等待量
    public List<Record> getsixquhaoWaitCountToday(String centerguid){
        ICommonDao dao=CommonDao.getInstance();
        String sql="select b.HALLNAME,COUNT(1) as 'qhtoday',count(case when status=0 then 1 else null end) as 'currentWaitcount' from audit_queue a,audit_orga_hall b WHERE a.HALLGuid=b.RowGuid AND  a.CenterGuid=?1 AND to_days(GETNOTIME) = to_days(now()) GROUP BY a.HALLGuid";
        List<Record> list=dao.findList(sql, Record.class, centerguid);
        return list;
    }
    //1
    public List<Record> getsixhall(String centerguid){
        ICommonDao dao=CommonDao.getInstance();
        String sql="select rowguid,hallname from audit_orga_hall where centerguid=?1 order by ordernum desc";
        List<Record> list=dao.findList(sql, Record.class, centerguid);
        return list;
    }
    //2
    public int getsixquhao(String hallguid){
        ICommonDao dao=CommonDao.getInstance();
        String sql="select count(1) from audit_queue where hallguid=?1";
        int count=dao.queryInt(sql, hallguid);
        return count;
    }
    //3
    public int getsixwait(String hallguid){
        ICommonDao dao=CommonDao.getInstance();
        String sql="select count(1) from audit_queue where hallguid=?1 and status=0";
        int count=dao.queryInt(sql, hallguid);
        return count;
    }
    
    
    
    // 今日总取号业务量  今天的总取号量 和当前的等待量
    public Record getTodayQhAndWaitCount(String centerguid){
     ICommonDao dao=CommonDao.getInstance();
     String sql="SELECT COUNT(1) as 'qhtoday',count(case when status=0 then 1 else null end) as 'currentWaitcount' FROM audit_queue a WHERE a.CenterGuid=?1 and to_days(GETNOTIME) = to_days(now())";
     Record record=dao.find(sql, Record.class, centerguid);
     return record;
    }
    // 取号量最多的业务 事项分类 
    public Record getMaxCountTasktype(String centerguid){
        ICommonDao dao=CommonDao.getInstance();
        String sql="SELECT  d.TaskTypeName,COUNT(1) as qhcount  from (SELECT a.TASKGUID from audit_queue a UNION all SELECT b.TASKGUID FROM audit_queue_history b) c,audit_queue_tasktype d WHERE  c.TASKGUID=d.RowGuid and centerguid=?1  GROUP BY c.TASKGUID ORDER BY qhcount DESC limit 1";
        Record record=dao.find(sql, Record.class,centerguid);
        return record;        
    }
    public Record getMaxPeople(String centerguid){
        ICommonDao dao=CommonDao.getInstance();
        String sql="select d.HALLNAME,COUNT(1) as 'qhcount' from (SELECT a.CenterGuid,a.HALLGuid ,a.getnotime from audit_queue a UNION all SELECT b.CENTERGUID,b.HALLGuid,b.getnotime FROM audit_queue_history b) c,audit_orga_hall d WHERE c.HALLGuid=d.RowGuid AND  c.CenterGuid=?1 GROUP BY c.HALLGuid  ORDER BY qhcount DESC LIMIT 1 ";
        Record record=dao.find(sql, Record.class,centerguid);
        return record;        
    }
    
    // 获取一天最拥挤的时间段
    public Record getMaxTime(String centerguid){
        ICommonDao dao=CommonDao.getInstance();
        String sql="select hour(a.GETNOTIME) as fromdate ,COUNT(1) as Tcount from audit_queue a where DATE_FORMAT(a.GETNOTIME,'%Y-%m-%d')=DATE_FORMAT(NOW(),'%Y-%m-%d') and centerguid=?1 group by hour(a.GETNOTIME) ORDER BY Tcount DESC LIMIT 1";
        Record record=dao.find(sql, Record.class,centerguid);
        return record;        
    }
    public Record getMaxweek(String centerguid){
        ICommonDao dao=CommonDao.getInstance();
        String sql="select cnt,xqj from (select count(1) as cnt,DAYOFWEEK(a.getnotime) xqj from (select getnotime from audit_queue union all  select getnotime from audit_queue_history WHERE centerguid=?1) a GROUP BY xqj) c ORDER BY cnt DESC LIMIT 1";
        Record record=dao.find(sql, Record.class,centerguid);                  
        return record;        
    }
    /**
     *  [根据办件的申办流水号获取办件信息] 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public AuditProject getAuditProjectByFlowsn(String flowsn) {
        ICommonDao dao=CommonDao.getInstance();
        String sql = "select * from audit_project where flowsn = ?1";
        return dao.find(sql, AuditProject.class, flowsn);
    }
    /**
     *  [根据办件的主题标识获取BusinessbaseInfo] 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Record getBusinessLicenseBaseInfoByBiGuid(String biguid) {
        ICommonDao dao=CommonDao.getInstance();
        String sql = "select * from businesslicense_baseinfo where biguid = ?1";
        return dao.find(sql, Record.class, biguid);
    }
    /**
     *  [根据BusinessbaseInfo的标识获取Extension的信息] 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Record getBusinessLicenseExtensionByBaseInfoGuid(String baseInfoGuid) {
        ICommonDao dao=CommonDao.getInstance();
        String sql = "select * from businesslicense_extension where baseinfoGuid = ?1";
        return dao.find(sql, Record.class, baseInfoGuid);
    }
    /**
     *  [根据taskid获取泰安事项信息拓展表audit_task_taian的信息] 
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Record getAuditTaskTaianByTaskId(String taskId) {
        ICommonDao dao=CommonDao.getInstance();
        String sql = "select * from audit_task_extension where taskguid = ?1";
        return dao.find(sql, Record.class, taskId);
    }
    /**
     * 
     *  [根据主题实例标识和事项标识查询专项材料和通用材料] 
     *  @param businessGuid
     *  @param taskId
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<Record> getAuditSpIYyyzMaterialByBusinessGuidAndTaskId(String biGuid, String taskId) {
        ICommonDao dao=CommonDao.getInstance();
        String sql = "select * from audit_sp_i_yyyz_material where (biGuid = ?1 and task_id is null) or ( biGuid = ?1 and task_id =?2)";
        return dao.findList(sql, Record.class, biGuid,taskId);
    }
    /**
     * 根据上传附件cliengguid获取附件表数据
     *  [一句话功能简述] 
     *  @param cliengguid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public List<Record> getFrameAttachInfoByCliengguid(String cliengguid) {
        ICommonDao dao=CommonDao.getInstance();
        String sql = "select * from frame_attachinfo where CLIENGGUID = ?";
        return dao.findList(sql, Record.class, cliengguid);
    }
    /**
     * 根据ouguid获取部门信息
     *  [一句话功能简述] 
     *  @param ouGuid
     *  @return    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public Record getFramOuByOuGuid(String ouGuid) {
        ICommonDao dao=CommonDao.getInstance();
        String sql = "select * from frame_ou where OUGUID = ?";
        return dao.find(sql, Record.class, ouGuid);
    }
    public AuditOnlineProject getAuditOnlineProjectByProjectguid(String projectguid) {
        ICommonDao dao=CommonDao.getInstance();
        String sql = "select * from audit_online_project where sourceguid = ?1";
        return dao.find(sql, AuditOnlineProject.class, projectguid);
    }
    public void updateAuditProjectByProjectguid(String projectguid,int status,int banjieStatus) {
        ICommonDao dao=CommonDao.getInstance();
        Log.info("===人社接口办件状态为===："+status);
        String sql = "";
        if(0 != banjieStatus) {
            sql = "update audit_project set status = ?1,banjieresult = ?2 where rowguid = ?3 ";
            dao.execute(sql, status,banjieStatus,projectguid);
        } 
        else {
            sql = "update audit_project set status = ?1 where rowguid = ?2 ";
            dao.execute(sql, status,projectguid);
        }
        dao.commitTransaction();
        dao.close();
              
    }
    public void updateAuditOnlineProjectByProjectguid(String projectguid,int status) {
        ICommonDao dao=CommonDao.getInstance();
        Log.info("===人社接口网办件状态为online===："+status);
        String sql = "update audit_online_project set status = ?1 where sourceguid = ?2 ";
        dao.execute(sql, status,projectguid);
        dao.commitTransaction();
        dao.close();
    }
    
    
   
}
