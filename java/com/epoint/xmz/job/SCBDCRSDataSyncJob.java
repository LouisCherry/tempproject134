package com.epoint.xmz.job;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.common.util.ValidateUtil;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.entity.FrameOu;
import com.epoint.xmz.api.IJnService;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@DisallowConcurrentExecution
public class SCBDCRSDataSyncJob implements Job {
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    private IJnService jnService = ContainerFactory.getContainInfo().getComponent(IJnService.class);
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            log.info("==========开始执行SCBDCRSDataSyncJob数据同步推送服务==========");
            EpointFrameDsManager.begin(null);

            tbApplyBaseInfo();

            EpointFrameDsManager.commit();
            log.info("==========执行SCBDCRSDataSyncJob数据同步推送服务成功！！！==========");
        } catch (Exception e) {
            e.printStackTrace();
            EpointFrameDsManager.rollback();
            log.info("==========执行SCBDCRSDataSyncJob数据同步推送服务失败！！！==========");
        } finally {
            EpointFrameDsManager.close();
            log.info("==========执行SCBDCRSDataSyncJob数据同步推送服务结束==========");
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
        List<Record> SCBDCRSData = jnService.getSCBDCRSData(beginOfDate, endOfDate);
        if (ValidateUtil.isNotNull(SCBDCRSData)&&SCBDCRSData.size()>0){
            //判断前置库信息是否为空

            for (Record baseInfo:SCBDCRSData) {
                String rowGuid = baseInfo.getStr("RowGuid");
                Record auditProjectZjxt = jnService.getAuditProjectZjxtByRowguid(rowGuid);
                if (ValidateUtil.isNull(auditProjectZjxt)){

                    //根据事项名查询对应事项
                    String qlname = baseInfo.getStr("ITEMNAME");
                    /*String qlno = baseInfo.getStr("QLNO");
                    AuditTask auditTask = jnService.getAuditBasicInfo(qlno);*/
                    AuditTask auditTask = jnService.getAuditBasicInfoByName(qlname);
                    if(auditTask == null){

                        switch (qlname) {
                            case "企业养老保险关系转入":
                                auditTask = jnService.getAuditBasicInfoByName("企业养老保险关系转入申请");
                                break;
                            case "公益性岗位补贴申领":
                                auditTask = jnService.getAuditBasicInfoByName("公益性岗位补贴和公益性岗位安置就业困难人员申领社会保险补贴");
                                break;
                            case "小微企业吸纳高校毕业生社保补贴申领":
                                auditTask = jnService.getAuditBasicInfoByName("小微企业吸纳高校毕业生社会保险补贴申领");
                                break;
                            case "机关事业单位养老保险关系转入":
                                auditTask = jnService.getAuditBasicInfoByName("机关事业单位养老保险关系转入申请");
                                break;
                            case "用人单位吸纳和公益性岗位安置就业困难人员申领社会保险补贴":
                                auditTask = jnService.getAuditBasicInfoByName("用人单位吸纳就业困难人员申领岗位补贴和社会保险补贴");
                                break;
                            case "退役军人养老保险关系转入":
                                auditTask = jnService.getAuditBasicInfoByName("退役军人养老保险关系转入申请");
                                break;
                            case "随军家属养老保险关系转入":
                                auditTask = jnService.getAuditBasicInfoByName("随军家属养老保险关系转入申请");
                                break;

                            default:
                                auditTask = null;
                                break;
                        }
                    }

                    if(auditTask != null){
                        String taskguid = auditTask.getRowguid();
                        String taskname = auditTask.getTaskname();
                        String ouname = auditTask.getOuname();
                        String ouguid = auditTask.getOuguid();
                        String areacode = auditTask.getAreacode();
                        String task_id = auditTask.getTask_id();

                        String projectid = baseInfo.getStr("PROJID");
                        String applyername = baseInfo.getStr("APPLICANT");
                        String applyercerttype = baseInfo.getStr("APPLYERPAGETYPE");
                        String applyercertnum = baseInfo.getStr("APPLYERPAGECODE");
                        String contractperson = baseInfo.getStr("APPLICANTNAME");
                        String contractphone = baseInfo.getStr("APPLICANTMOBILE");
                        String legal = baseInfo.getStr("LEGALMAN");
                        //Date applyerdate = baseInfo.getDate("APPLYERDATE");
                        String itemno = baseInfo.getStr("ITEMNO");
                        String acceptusername = baseInfo.getStr("ACCEPTDEPTNAME");
                        //转换为audit_project_zjxt表的信息
                        Record record = new Record();
                        record.setSql_TableName("audit_project_zjxt");
                        record.set("datasource","004");//市场监管局，固定为004
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
                        //record.set("APPLYDATE",applyerdate);
                        record.set("RECEIVEUSERNAME",acceptusername);
                        //record.set("RECEIVEDATE",applyerdate);
                        record.set("OperateUserName","市场监管局数据同步服务");
                        record.set("OperateDate",date);
                        record.set("OUGUID",ouguid);
                        record.set("OUNAME",ouname);
                        record.set("AREACODE",areacode);
                        record.set("TASKID",task_id);
                        record.set("STATUS","90");
                        int i = jnService.insert(record);
                        if (i>0){
                            //更新事项同步状态
                            jnService.upApplyBaseInfoSign(rowGuid);
                            //调用办理环节方法
                            tbApplyProcess(projectid);

                            //推送到浪潮前置库里
                            putDatatoWave(record);
                        }
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

    public void putDatatoWave(Record record){
        if (record != null ) {
            if("EA_JC_STEP_BASICINFO_BDC".equals(record.getSql_TableName()) || "EA_JC_STEP_BASICINFO".equals(record.getSql_TableName())){
                record.setSql_TableName("EA_JC_STEP_BASICINFONEW");
            }else if("EA_JC_STEP_DONE_BDC".equals(record.getSql_TableName()) || "EA_JC_STEP_DONE".equals(record.getSql_TableName())){
                record.setSql_TableName("EA_JC_STEP_DONENEW");
            }else if("EA_JC_STEP_PROC_BDC".equals(record.getSql_TableName()) || "EA_JC_STEP_PROC".equals(record.getSql_TableName())){
                record.setSql_TableName("EA_JC_STEP_PROCNEW");
            }
            String Orgbusno = UUID.randomUUID().toString();
            record.set("rowguid", Orgbusno);
            if(StringUtil.isBlank(record.get("ACCEPTLIST"))){
                record.set("ACCEPTLIST","空");
            }

            jnService.insert(record);
        }
    }
}
