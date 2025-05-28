package com.epoint.synctask;

import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;

/**
 * 
 * @author swy
 * @version [版本号, 2018年8月24日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@DisallowConcurrentExecution
public class EpointSyncJob implements Job
{
    transient Logger log = LogUtil.getLog(EpointSyncJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            EpointFrameDsManager.begin(null);
            doService();
            EpointFrameDsManager.commit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            EpointFrameDsManager.close();
        }
    }

    /**
     * 
     * 执行主业务逻辑
     */
    private void doService() {
        try {
            log.info("------EpointSyncJob开始同步省前置库事项------");
            EpointSyncDone service = new EpointSyncDone();
            // 获取前置库待同步事项20条
            List<Record> rnew = service.getQLSXnew();
             log.info("------待同步事项：------"+rnew.size());
            // 开始循环同步事项
            for (Record info : rnew) {

                String new_item_code = info.getStr("ITEM_CODE");
                String ORG_CODE = info.getStr("ORG_CODE");
                String updateType = info.getStr("UPDATE_TYPE");
                String rowguid = info.getStr("ROWGUID");

                String itemInfoXml = info.getStr("ITEM_INFO_XML");
                // 事项编码
                String taskCode = "";
                String TASKHANDLEITEM = "";
                if (StringUtil.isNotBlank(itemInfoXml)) {
                    Document document = DocumentHelper.parseText(itemInfoXml);
                    Element root = document.getRootElement();

                    Node xntaskcode = root.selectSingleNode("key[@label='TASKCODE']");
                    if (xntaskcode != null) {
                        taskCode = xntaskcode.getText();
                    }

                    Node xnTASKHANDLEITEM = root.selectSingleNode("key[@label='TASKHANDLEITEM']");
                    if (xnTASKHANDLEITEM != null) {
                        TASKHANDLEITEM = xnTASKHANDLEITEM.getText();
                    }
                }

                // 如果TASKHANDLEITEM不为空
                if (StringUtil.isNotBlank(TASKHANDLEITEM)) {
                    taskCode = TASKHANDLEITEM;
                }
                if (StringUtil.isBlank(taskCode)) {
                    // 如果taskCode为空，标记省前置库数据异常
                    service.updateJnQlsxSync(rowguid, "9");
                    log.info("taskCode为空！结束同步");
                    continue;
                }
                log.info("开始同步事项taskCode=" + taskCode);
                if ("I".equals(updateType) || "U".equals(updateType)) {
                    // 根据省前置库的ORG_CODE，查询我们的frame_ou和extend信息
                    Record dvOuInfo = service.getOuInfo(ORG_CODE);
                    if (dvOuInfo == null) {
                        log.info("部门不存在  =====>" + ORG_CODE);
                        // 10-部门不存在， 9-代码报错
                        service.updateQlsxSync(ORG_CODE, "10");
                        continue;
                    }

                    try {
                        // 从我们库查询item_id等于taskcode,并且在用is_history为空或者等于0的，并且事项审核状态为1-审核通过，草稿-1，待确认4，已作废5
                        List<AuditTask> auditTask = service.getAuditTask1(taskCode);
                        // 事项不存在：新增到待确认；
                        if (auditTask == null || auditTask.size() == 0) {
                            // 新增事项到待确认,这边只取子事项最新版本的，然后相同的子事项都置成已同步
                            log.info("开始新增,事项taskCode：" + taskCode);
                            service.insertTask(info, dvOuInfo);
                            service.updateJnQlsxSync(rowguid, "1");
                            log.info("权力同步新增成功,事项innercode：" + new_item_code);
                        }
                        // 事项存在则更新
                        else {
                            log.info("开始更新,事项taskCode：" + taskCode);
                            // 把待确认中的老版本的置成历史
                            AuditTask auditTask4 = service.getAuditTask4(taskCode);
                            IAuditTask iAuditTask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
                            if (auditTask4 != null) {
                                auditTask4.setIs_history(1);
                                auditTask4.setIs_editafterimport(5);
                                auditTask4.setOperateusername("同步服务自动作废待确认");
                                iAuditTask.updateAuditTask(auditTask4);
                            }
                            log.info("同步更新事项rowguid=" + auditTask.get(0).getRowguid());
                            service.updateTask(info, dvOuInfo, auditTask.get(0));
                            // 更新成功后，将原版本事项置为历史
                            for (AuditTask taskold : auditTask) {
                                taskold.setIs_history(1);
                                taskold.setOperateusername("同步服务自动置为历史版本");
                                iAuditTask.updateAuditTask(taskold);
                            }
                            log.info("权力同步更新成功,事项innercode：" + new_item_code);
                            service.updateJnQlsxSync(rowguid, "1");
                        }
                    }
                    catch (Exception e) {
                        service.updateJnQlsxSync(rowguid, "9");
                        e.printStackTrace();
                    }
                }
                else if ("C".equals(updateType)) {
                    log.info("开始同步取消的事项：" + taskCode);
                    List<AuditTask> auditTask = service.getAuditTask1(taskCode);
                    IAuditTask iAuditTask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
                    for (AuditTask taskold : auditTask) {
                        taskold.setIs_history(1);
                        taskold.setOperateusername("同步服务置为取消事项");
                        iAuditTask.updateAuditTask(taskold);
                    }
                    service.updateJnQlsxSync(rowguid, "8");
                    log.info("同步取消事项成功，该事项为：" + new_item_code);
                }
                else {
                    service.updateJnQlsxSync(rowguid, "1");
                    log.info("该事项无需同步，该事项为：" + new_item_code + ";rowugid:" + rowguid);
                }
            }
            log.info("【结束同步事项】");
        }
        catch (Exception e) {
            e.printStackTrace();
            log.info("【同步失败】" + e.getMessage());
        }
    }
}
