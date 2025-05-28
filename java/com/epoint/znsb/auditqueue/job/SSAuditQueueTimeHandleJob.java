package com.epoint.znsb.auditqueue.job;

import com.epoint.basic.auditqueue.auditqueue.inter.IAuditQueue;
import com.epoint.basic.auditqueue.auditqueuedefinition.inter.IAuditQueueDefinition;
import com.epoint.basic.auditqueue.auditqueueinstance.inter.IAuditQueueInstance;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.composite.auditqueue.handledevice.inter.IHandleDevice;
import com.epoint.composite.auditqueue.handlewindow.inter.IHandleWindow;
import com.epoint.composite.auditqueue.handlewindow.inter.ISSHandleWindow;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;

public class SSAuditQueueTimeHandleJob implements Job
{

    transient Logger log = LogUtil.getLog(AuditQueueTimeHandleJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            EpointFrameDsManager.begin(null);
            IAuditQueue auditqueueservice = ContainerFactory.getContainInfo().getComponent(IAuditQueue.class);
            IAuditQueueDefinition definitionservice = ContainerFactory.getContainInfo()
                    .getComponent(IAuditQueueDefinition.class);
            IAuditQueueInstance queueinstanceservice = ContainerFactory.getContainInfo()
                    .getComponent(IAuditQueueInstance.class);
            ISSHandleWindow handlewindowservice = ContainerFactory.getContainInfo().getComponent(ISSHandleWindow.class);
            IConfigService configservice = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
            IHandleDevice deviceservice = ContainerFactory.getContainInfo().getComponent(IHandleDevice.class);

            while (true) {
                try {
                    // 每天定时执行一次
                    if (QueueConstant.Common_yes_String.equals(configservice.getFrameConfigValue("AS_USE_QUEUE"))) {
                        // AuditQueue移历史表
                        log.info("AuditQueue开始移历史表");
                        auditqueueservice.MoveAuditQueueToHistory();
                        log.info("AuditQueue结束移历史表");
                        // 删除排队分表数据
                        log.info("开始删除排队分表数据");
                        List<String> queuevaluelist = definitionservice.getAllQueueValue("").getResult();
                        for (String queuevalue : queuevaluelist) {
                            queueinstanceservice.deleteByQueueValue(queuevalue);
                        }
                        log.info("结束删除排队分表数据");
                    }
                    // 重新初始化窗口redis
                    log.info("开始初始化窗口redis");
                    handlewindowservice.initAuditQueueOrgaWindow();
                    log.info("结束初始化窗口redis");
                    // 处理硬件管理平台数据
                    if (QueueConstant.Common_yes_String
                            .equals(configservice.getFrameConfigValue("AS_IS_USE_EPOINTDEVICE"))) {

                        log.info("开始处理硬件管理平台数据");
                        deviceservice.handleDeviceData();
                        log.info("结束处理硬件管理平台数据");

                    }
                    break;
                }
                catch (Exception e) {
                    log.error(e.toString());
                    // 休眠10分钟
                    Thread.sleep(600000);
                }
                EpointFrameDsManager.commit();
            }
        }
        catch (Exception ex) {
            EpointFrameDsManager.rollback();
        }
        finally {
            EpointFrameDsManager.close();
        }

    }

}
