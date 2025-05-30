package com.epoint.xmz.job;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.common.util.ValidateUtil;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.xmz.api.IJnService;
import com.epoint.xmz.api.IWjwService;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.List;

@DisallowConcurrentExecution
public class WjwProjectDataSyncJob implements Job {
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    private IWjwService jnService = ContainerFactory.getContainInfo().getComponent(IWjwService.class);
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            log.info("==========开始执行WjwProjectDataSyncJob数据同步推送服务==========");
            EpointFrameDsManager.begin(null);

            tbApplyBaseInfo();

            EpointFrameDsManager.commit();
            log.info("==========执行WjwProjectDataSyncJob数据同步推送服务成功！！！==========");
        } catch (Exception e) {
            e.printStackTrace();
            EpointFrameDsManager.rollback();
            log.info("==========执行WjwProjectDataSyncJob数据同步推送服务失败！！！==========");
        } finally {
            EpointFrameDsManager.close();
            log.info("==========执行WjwProjectDataSyncJob数据同步推送服务结束==========");
        }
    }

    /**
     * 同步办件基本信息
     */
    public void tbApplyBaseInfo(){
        //获取当日时间
        Date date = new Date();
        //获取前一天的时间
        Date dateBefore = EpointDateUtil.getDateBefore(date, 1);
        log.info("========dateBefore================"+dateBefore);
        //转化成前一天的开始时间，即00：00：00
        Date beginOfDate = EpointDateUtil.getBeginOfDate(dateBefore);
        log.info("========beginOfDate================"+beginOfDate);
        //转化成前一天的结束时间，即23：59：59
        Date endOfDate = EpointDateUtil.getEndOfDate(dateBefore);
        log.info("========endOfDate================"+endOfDate);
        List<Record> scjgjApplyBaseInfo = jnService.getSCJGJApplyBaseInfo(beginOfDate, endOfDate);
        if (ValidateUtil.isNotNull(scjgjApplyBaseInfo)&&scjgjApplyBaseInfo.size()>0){
            //判断前置库信息是否为空
            for (Record baseInfo:scjgjApplyBaseInfo) {
                String rowGuid = baseInfo.getStr("RowGuid");
                Record auditProjectZjxt = jnService.getAuditProjectZjxtByRowguid(rowGuid);
                if (ValidateUtil.isNull(auditProjectZjxt)){

                    //根据QLNO查询对应事项
                    String qlno = baseInfo.getStr("QLNO");
                    AuditTask auditTask = jnService.getAuditBasicInfo(qlno);
                    
                    if (auditTask == null) {
                    	 //更新事项同步状态
                        jnService.upApplyBaseInfoSign(rowGuid,"9");
                        continue;
                    }

                    String taskguid = auditTask.getRowguid();
                    String taskname = auditTask.getTaskname();
                    String ouname = auditTask.getOuname();
                    String ouguid = auditTask.getOuguid();
                    String areacode = auditTask.getAreacode();
                    String task_id = auditTask.getTask_id();
                    
                    //前置库中获取的具体办件详情
                    String projectid = baseInfo.getStr("PROJECTID");
                    String qlname = baseInfo.getStr("QLNAME");
                    String projecttype = baseInfo.getStr("PROJECTTYPE");
                    String applyername = baseInfo.getStr("APPLYERNAME");
                    String applyercerttype = baseInfo.getStr("APPLYERCERTTYPE");
                    String applyercertnum = baseInfo.getStr("APPLYERCERTNUM");
                    String contractperson = baseInfo.getStr("CONTRACTPERSON");
                    String contractcerttype = baseInfo.getStr("CONTRACTCERTTYPE");
                    String contractcertnum = baseInfo.getStr("CONTRACTCERTNUM");
                    String contractphone = baseInfo.getStr("CONTRACTPHONE");
                    String contractemail = baseInfo.getStr("CONTRACTEMAIL");
                    String contractaddress = baseInfo.getStr("CONTRACTADDRESS");
                    String legal = baseInfo.getStr("LEGAL");
                    String applyersource = baseInfo.getStr("APPLYERSOURCE");
                    Date applyerdate = baseInfo.getDate("APPLYERDATE");
                    String itemno = baseInfo.getStr("ITEMNO");
                    Date versiontime = baseInfo.getDate("VERSIONTIME");
                    String version = baseInfo.getStr("VERSION");
                    String acceptusername = baseInfo.getStr("ACCEPTUSERNAME");
                    Date acceptdate = baseInfo.getDate("ACCEPTDATE");
                    String acceptdocnumber = baseInfo.getStr("ACCEPTDOCNUMBER");
                    String sync_sign = baseInfo.getStr("SYNC_SIGN");
                    String sync_error_desc = baseInfo.getStr("SYNC_ERROR_DESC");
                    //转换为audit_project_zjxt表的信息
                    Record record = new Record();
                    record.setSql_TableName("audit_project_zjxt");
                    record.set("datasource","009");//市场监管局，固定为004
                    record.set("RowGuid",rowGuid);
                    record.set("FLOWSN",projectid);
                    record.set("taskguid",taskguid);
                    record.set("projectname",taskname);
                    record.set("APPLYERNAME",applyername);
                    record.set("CERTTYPE",applyercerttype);
                    record.set("CERTNUM",applyercertnum);
                    record.set("CONTACTPERSON",contractperson);
                    record.set("CONTACTPHONE",contractphone);
                    record.set("LEGAL",legal);
                    record.set("xmnum",itemno);
                    record.set("APPLYDATE",applyerdate);
                    record.set("RECEIVEUSERNAME",acceptusername);
                    record.set("RECEIVEDATE",applyerdate);
                    record.set("OperateUserName","卫健委数据同步服务");
                    record.set("OperateDate",date);
                    record.set("OUGUID",ouguid);
                    record.set("OUNAME",ouname);
                    record.set("AREACODE",areacode);
                    record.set("TASKID",task_id);
                    record.set("STATUS","90");
                    int i = jnService.insert(record);
                    EpointFrameDsManager.commit();
                    if (i>0){
                        //更新事项同步状态
                        jnService.upApplyBaseInfoSign(rowGuid,"1");
                        //调用办理环节方法
                        tbApplyProcess(projectid);
                    }

                }
            }
        }
    }

    /**
     * 同步办件流程环节
     * @param proid
     */
    public void tbApplyProcess(String proid){
//        //获取当日时间
//        Date date = new Date();
//        //获取前一天的时间
//        Date dateBefore = EpointDateUtil.getDateBefore(date, 1);
//        //转化成前一天的开始时间，即00：00：00
//        Date beginOfDate = EpointDateUtil.getBeginOfDate(dateBefore);
//        //转化成前一天的结束时间，即23：59：59
//        Date endOfDate = EpointDateUtil.getEndOfDate(dateBefore);
//
//        List<Record> scjgjApplyProcess = jnService.getSCJGJApplyProcess(beginOfDate, endOfDate);
        List<Record> scjgjApplyProcess = jnService.getSCJGJApplyProcess(proid);
        //判断前置库信息是否为空
        if (ValidateUtil.isNotNull(scjgjApplyProcess)&&scjgjApplyProcess.size()>0){
            for (Record applyProcess:scjgjApplyProcess) {
                String rowGuid = applyProcess.getStr("RowGuid");
                //判断前置库信息是否已进行过同步服务
                Record auditRsApplyZjxt = jnService.getAuditRsApplyZjxtByRowguid(rowGuid);
                if (ValidateUtil.isNull(auditRsApplyZjxt)){
                    //获取前置库事项详情，赋值并插入
                    String projectid = applyProcess.getStr("PROJECTID");
                    String action = applyProcess.getStr("ACTION");
                    String nodename = applyProcess.getStr("NODENAME");
                    String nextnodename = applyProcess.getStr("NEXTNODENAME");
                    String handleusername = applyProcess.getStr("HANDLEUSERNAME");
                    String handleopinion = applyProcess.getStr("HANDLEOPINION");
                    Date starttime = applyProcess.getDate("STARTTIME");
                    Date endtime = applyProcess.getDate("ENDTIME");
                    String note = applyProcess.getStr("NOTE");
                    Date versiontime = applyProcess.getDate("VERSIONTIME");
                    String version = applyProcess.getStr("VERSION");
                    String sync_sign = applyProcess.getStr("SYNC_SIGN");
                    String sync_error_desc = applyProcess.getStr("SYNC_ERROR_DESC");


                    //填充办件基本信息
                    Record record = new Record();
                    record.setSql_TableName("AUDIT_RS_APPLY_PROCESS_ZJXT");
                    record.set("RowGuid",rowGuid);
                    record.set("PROJECTID",projectid);
                    record.set("ACTION",action);
                    record.set("NODENAME",nodename);
                    record.set("NEXTNODENAME",nextnodename);
                    record.set("HANDLEUSERNAME",handleusername);
                    record.set("HANDLEOPINION",handleopinion);
                    record.set("STARTTIME",starttime);
                    record.set("ENDTIME",endtime);
                    record.set("NOTE",note);
                    record.set("VERSIONTIME",versiontime);
                    record.set("VERSION",version);
                    int i = jnService.insert(record);
                    if (i>0){
                        //更新事项同步状态
                        jnService.upApplyProcessSign(rowGuid);
                    }

                }
            }
        }
    }
}
