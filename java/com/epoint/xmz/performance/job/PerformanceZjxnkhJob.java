package com.epoint.xmz.performance.job;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.epoint.composite.auditorga.handlekaoqin.inter.IHandleKaoQin;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.xmz.performance.api.PerformanceService;
@DisallowConcurrentExecution
public class PerformanceZjxnkhJob implements Job
{
    transient Logger log = LogUtil.getLog(PerformanceZjxnkhJob.class);
    PerformanceService service = ContainerFactory.getContainInfo().getComponent(PerformanceService.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
        	EpointFrameDsManager.begin(null);
            log.info("效能考核数据统计服务启动开始！");
            service.zjxnkh();
            log.info("效能考核数据统计服务启动结束！");
            EpointFrameDsManager.commit();
        }
        catch (Exception e) {
            log.error(e.getMessage());
            EpointFrameDsManager.rollback();
        }
        finally {
        	 EpointFrameDsManager.close();
        }

    }

}
