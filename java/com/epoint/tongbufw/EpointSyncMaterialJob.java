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
public class EpointSyncMaterialJob implements Job
{

    transient Logger log = LogUtil.getLog(EpointSyncMaterialJob.class);


    /**
     * 程序入口
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            EpointFrameDsManager.begin(null);
            //job作业比框架起的早 导致部分接口没有实例化
            Thread.sleep(30000);
            doMaterialService();
            EpointFrameDsManager.commit();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            EpointFrameDsManager.close();
        }
    }

    private void doMaterialService() {
        try {
            EpointSyncDone service = new EpointSyncDone();
            List<Record> rnew = service.getQLSXMaterialnew();

            //获取前置机所有存在待同步事项的部门ORG_CODE
            //Record r = service.getQLSX();

            log.info("开始同步事项 表格   =====>");
            for (Record info : rnew) {
                //循环开始同步事项
                String innerCode = info.getStr("INNER_CODE");
                String ORG_CODE = info.getStr("ORG_CODE");
                //获取子事项
                List<Record> dvList = service.selectRightByRowGuid(innerCode,ORG_CODE);
                try {
                    service.syncTaskMaterial(dvList.get(0));
                    service.updateJnQlsxSync(dvList.get(0).getStr("ROWGUID"), "2");
                    log.info("同步表格成功");
                }
                catch (Exception e) {
                    service.updateJnQlsxSync(dvList.get(0).getStr("ROWGUID"), "8");
                    log.info("同步表格失败");
                    e.printStackTrace();
                }

            }
            log.info("结束同步事项 表格   =====>");
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
