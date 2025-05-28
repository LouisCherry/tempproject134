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
import com.epoint.core.utils.string.StringUtil;

/**
 * 
 * @author swy
 * @version [版本号, 2018年8月24日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@DisallowConcurrentExecution
public class Epointsynunid implements Job
{

    transient Logger log = LogUtil.getLog(Epointsynunid.class);

     

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
            EpointSynunidService service = new EpointSynunidService();
            //获取前置机所有存在待同步事项的部门ORG_CODE
            //Record r = service.getQLSX();
            log.info("开始同步unid为null的办件了=====>时间：" + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            List<AuditTask> auditTasklist = service.getUnid();
            if(auditTasklist!=null && auditTasklist.size()>0){
            for(AuditTask task : auditTasklist){
            	String itemid=task.getItem_id();
            	String unid=service.getqzkunid(itemid);
            if(StringUtil.isNotBlank(unid)){
            	task.set("unid", unid);
              }else{
            	 task.set("unid", "1");	
              }
            	iAuditTask.updateAuditTask(task);
            }
            }else{
            	
             log.info("没有unid为null的办件了=====>时间：" + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
            }
        }
        catch (Exception e) {
            log.info("同步失败 =====>时间：" + EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT));
        }
    }
}
