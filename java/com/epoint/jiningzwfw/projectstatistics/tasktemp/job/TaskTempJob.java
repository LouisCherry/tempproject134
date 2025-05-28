package com.epoint.jiningzwfw.projectstatistics.tasktemp.job;

import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.jiningzwfw.projectstatistics.tasktemp.impl.AuditTaskOnTempService;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.lang.invoke.MethodHandles;

/**
 * 获取再用的临时事项表
 */
@DisallowConcurrentExecution
public class TaskTempJob implements Job {

    private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        try {
            log.info("开始执行获取临时事项表Job ,开始时间：" + EpointDateUtil.getCurrentDate("yyyy-MM-dd hh:mm:ss"));
            EpointFrameDsManager.begin(null);
            AuditTaskOnTempService auditTaskOnTempService = new AuditTaskOnTempService();
            //执行存储过程
            auditTaskOnTempService.doProcedure();
            log.info("结束执行获取临时事项表Job ,结束时间：" + EpointDateUtil.getCurrentDate("yyyy-MM-dd hh:mm:ss"));
            EpointFrameDsManager.commit();
        } 
        catch (Exception e) {
            e.printStackTrace();
            EpointFrameDsManager.rollback();
        } 
        finally {
            EpointFrameDsManager.close();
        }
    }


}
