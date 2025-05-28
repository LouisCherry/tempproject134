package com.epoint.apimanage.job;

import com.epoint.apimanage.log.entity.ApiManageLog;
import com.epoint.apimanage.utils.api.IApiErroInfoSendService;
import com.epoint.apimanage.utils.api.ICommonDaoService;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.reflect.ReflectUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.lang.invoke.MethodHandles;
import java.util.List;

@DisallowConcurrentExecution
public class ApiManageJob implements Job {

    private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());




    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            EpointFrameDsManager.begin(null);
            log.info(">>>>>>>>>>.开始扫描api异常调用日志");
            ICommonDaoService iCdCommonService = ContainerFactory.getContainInfo().getComponent(ICommonDaoService.class);
            IConfigService iConfigService = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
            // 判断项目上有没有个性化通知实现类
            String path = iConfigService.getFrameConfigValue("apierrsendpath");
            IApiErroInfoSendService iApiErroInfoSendService;
            if (StringUtil.isBlank(path)) {
                // 走默认实现
                iApiErroInfoSendService = ContainerFactory.getContainInfo()
                        .getComponent(IApiErroInfoSendService.class);
            }
            else {
                // 走个性化实现
                iApiErroInfoSendService = (IApiErroInfoSendService) ReflectUtil.getObjByClassName(path);
            }

            //查询异常日志，scanned不为1（已扫描）的记录
            List<ApiManageLog> list = iCdCommonService.findLogList();

            EpointFrameDsManager.commit();//提交事务

            list.forEach(apiManageLog -> {
                EpointFrameDsManager.begin(null);//开启事务
                iApiErroInfoSendService.sendErroMsg(apiManageLog);
                //变更日志表scanned为1（已扫描）
                apiManageLog.set("scanned", "1");
                iCdCommonService.update(apiManageLog);
                EpointFrameDsManager.commit();//提交事务

            });

            //调用住建通知接口
            EpointFrameDsManager.commit();

        } catch (Exception ex) {
            EpointFrameDsManager.rollback();
            ex.printStackTrace();
            log.error(">>>>>>>>>>.扫描api异常调用日志 发生成了异常！");
        } finally {
            EpointFrameDsManager.close();
            log.info(">>>>>>>>>>.结束扫描api异常调用日志");
        }
    }

}
