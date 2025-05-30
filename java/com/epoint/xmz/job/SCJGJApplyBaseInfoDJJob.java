package com.epoint.xmz.job;

import com.epoint.basic.audittask.basic.domain.AuditTask;

import com.epoint.common.util.ValidateUtil;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.xmz.api.IJnService;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.List;

public class SCJGJApplyBaseInfoDJJob implements Job {
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    private IJnService jnService = ContainerFactory.getContainInfo().getComponent(IJnService.class);


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            log.info("==========开始执行SCJGJApplyBaseInfoDJJob数据同步推送服务==========");
            EpointFrameDsManager.begin(null);
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
                        record.set("APPLYDATE",applyerdate);
                        record.set("RECEIVEUSERNAME",acceptusername);
                        record.set("RECEIVEDATE",applyerdate);
                        record.set("OperateUserName","市场监管局数据同步服务");
                        record.set("OperateDate",date);
                        record.set("OUGUID",ouguid);
                        record.set("OUNAME",ouname);
                        record.set("AREACODE",areacode);
                        record.set("TASKID",task_id);
                        record.set("STATUS","90");
                        jnService.insert(record);
                        //更新事项同步状态
                        jnService.upApplyBaseInfoSign(rowGuid);
                    }
                }
            }
            EpointFrameDsManager.commit();
            log.info("==========执行SCJGJApplyBaseInfoDJJob数据同步推送服务成功！！！==========");
        } catch (Exception e) {
            e.printStackTrace();
            EpointFrameDsManager.rollback();
            log.info("==========执行SCJGJApplyBaseInfoDJJob数据同步推送服务失败！！！==========");
        } finally {
            EpointFrameDsManager.close();
            log.info("==========执行SCJGJApplyBaseInfoDJJob数据同步推送服务结束==========");
        }
    }
}
