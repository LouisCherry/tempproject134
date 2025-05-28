package com.epoint.tongbufw;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.log.LogUtil;

/**
 * 
 * @author swy
 * @version [版本号, 2018年8月24日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@DisallowConcurrentExecution
public class OldEpointSyncJob implements Job
{

    transient Logger log = LogUtil.getLog(OldEpointSyncJob.class);

     

    private IAuditTask iAuditTask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);

    /**
     * 程序入口
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            EpointFrameDsManager.begin(null);
            //job作业比框架起的早 导致部分接口没有实例化
            Thread.sleep(30000);
            doService();
            EpointFrameDsManager.commit();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            EpointFrameDsManager.close();
        }
    }

    private void doService() {
        try {
            EpointSyncDone service = new EpointSyncDone();
            //获取前置机所有存在待同步事项的部门ORG_CODE
            //Record r = service.getQLSX();
            List<Record> rnew = service.getQLSXnew();
            log.info("开始同步旧事项    =====>");
            for (Record info :  rnew) {
                //循环开始同步事项
                String innerCode = info.getStr("INNER_CODE");
                String ORG_CODE = info.getStr("ORG_CODE");
                //获取子事项
                List<Record> dvList = service.selectRightByRowGuid(innerCode,ORG_CODE);
                if(dvList == null|| dvList.size() == 0){
                    log.info("事项不存在可更新的最高版本 =====>"+innerCode+"===ROWGUID=====>"+info.getStr("ROWGUID"));
                    service.updateJnQlsxSync(info.getStr("ROWGUID"),"1");
                    continue;
                }
                String updateType = dvList.get(0).getStr("UPDATE_TYPE");
                ORG_CODE = dvList.get(0).getStr("ORG_CODE");
                Record dvOuInfo = service.getOuInfo(ORG_CODE);
                if(dvOuInfo == null ){
                    log.info("部门不存在  =====>"+ORG_CODE);
                    //部门不存在的做10标识，用于后期配置后进行重新同步 9的为代码报错标识
                    service.updateQlsxSync(ORG_CODE,"10");
                    continue;
                }
                if ("3".equals(updateType) || "4".equals(updateType)) {
                    //取消和删除事项不同步，并且前置库此事项全部置为亦同步
                    service.updateJnQlsxSync(dvList.get(0).getStr("ROWGUID"),"2");
                }
                else if(dvList.get(0).getStr("QFSX") == null
                        ||ZwfwConstant.CONSTANT_STR_ZERO.equals(dvList.get(0).getStr("QFSX"))) {
                    if (null != dvList && dvList.size() > 0) {
                        try {
                            List<AuditTask> auditTask = service.getAuditTask1(innerCode);
                            //判断F9事项存不存在，不存在：新增到待确认；存在：更新
                            if (auditTask == null || auditTask.size()==0) {
                                //新增事项到待确认,这边只取子事项最新版本的，然后相同的子事项都置成已同步
                                    log.info("开始新增,旧事项innercode：" + innerCode);
                                    service.updateJnQlsxSync(dvList.get(0).getStr("ROWGUID"),"1");
                                    service.insertTask(dvList.get(0), dvOuInfo);
                                    log.info("权力同步新增成功,事项innercode：" + innerCode);
                            }
                            else {
                                //更新事项到待确认
                                log.info("开始更新,旧事项innercode：" + innerCode);
                                //service.updateSyncSign(innerCode,ORG_CODE);
                                //把待确认中的老版本的置成历史
                                AuditTask auditTask4 = service.getAuditTask4(innerCode);
                                if (auditTask4!=null) {
                                    auditTask4.setIs_history(1);
                                    auditTask4.setIs_editafterimport(5);
                                    auditTask4.setOperateusername("同步服务自动作废待确认");
                                    iAuditTask.updateAuditTask(auditTask4);
                                }
                                service.updateTask(dvList.get(0), dvOuInfo, auditTask.get(0));
                                //更新成功后，将原版本事项置为历史
                                for (AuditTask taskold : auditTask) {
                                    taskold.setIs_history(1);
                                    taskold.setOperateusername("同步服务自动置为历史版本");
                                    iAuditTask.updateAuditTask(taskold);
                                }
                                log.info("权力同步更新成功,事项innercode：" + innerCode);
                            service.updateJnQlsxSync(dvList.get(0).getStr("ROWGUID"),"1");
                            }
                        }
                        catch (Exception e) {
                            service.updateJnQlsxSync(dvList.get(0).getStr("ROWGUID"),"9");
                            e.printStackTrace();
                        }
                    }
                }else{
                    log.info("权力同步更新,低版本事项innercode：" + innerCode);
                    service.updateSyncSign(innerCode,ORG_CODE);
                }
            }
        }
        catch (Exception e) {
            log.info("同步失败 =====>时间：" + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
        }
    }
}
