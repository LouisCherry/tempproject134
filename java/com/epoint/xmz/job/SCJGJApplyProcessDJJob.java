package com.epoint.xmz.job;

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

public class SCJGJApplyProcessDJJob implements Job {
    private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    private IJnService jnService = ContainerFactory.getContainInfo().getComponent(IJnService.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            log.info("==========开始执行SCJGJApplyProcessDJJob数据同步推送服务==========");
            EpointFrameDsManager.begin(null);
            //获取当日时间
            Date date = new Date();
            //获取前一天的时间
            Date dateBefore = EpointDateUtil.getDateBefore(date, 1);
            //转化成前一天的开始时间，即00：00：00
            Date beginOfDate = EpointDateUtil.getBeginOfDate(dateBefore);
            //转化成前一天的结束时间，即23：59：59
            Date endOfDate = EpointDateUtil.getEndOfDate(dateBefore);

            List<Record> scjgjApplyProcess = jnService.getSCJGJApplyProcess(beginOfDate, endOfDate);
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
                        jnService.insert(record);
                        //更新事项同步状态
                        jnService.upApplyProcessSign(rowGuid);
                    }
                }
            }


            EpointFrameDsManager.commit();
            log.info("==========执行SCJGJApplyProcessDJJob数据同步推送服务成功！！！==========");
        } catch (Exception e) {
            e.printStackTrace();
            EpointFrameDsManager.rollback();
            log.info("==========执行SCJGJApplyProcessDJJob数据同步推送服务失败！！！==========");
        } finally {
            EpointFrameDsManager.close();
            log.info("==========执行SCJGJApplyProcessDJJob数据同步推送服务结束==========");
        }
    }
}
