package com.epoint.tongbufw;

import java.util.Date;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.frame.service.metadata.systemparameters.api.IConfigService;
import com.epoint.jn.inproject.api.IWebUploaderService;


@DisallowConcurrentExecution
public class InProjectJob implements Job
{
    transient Logger log = LogUtil.getLog(InProjectJob.class);


		@Override
		public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
        	InProjectService service = new InProjectService();
        	IWebUploaderService IWebUploaderService = ContainerFactory.getContainInfo().getComponent(IWebUploaderService.class);
        	IConfigService configServicce = ContainerFactory.getContainInfo().getComponent(IConfigService.class);
            EpointFrameDsManager.begin(null);
            Record total = IWebUploaderService.getTotalPorjectByNow();
        	
        	if(total != null) {
        		if(total.getInt("total") > Integer.parseInt(configServicce.getFrameConfigValue("lctotal"))) {
        			Record record = new Record();
        			record.setSql_TableName("in_project_record");
        			record.set("rowguid", UUID.randomUUID().toString());
        			record.setPrimaryKeys("rowguid");
        			record.set("status", "1");
        			record.set("todaytime", total.getDate("acceptuserdate"));
        			service.insert(record);
        		}
        	}
        	
            EpointFrameDsManager.commit();
        }
        catch (Exception e) {
        	EpointFrameDsManager.rollback();
            log.info("同步失败 =====" + e.getMessage());
        }finally {
            EpointFrameDsManager.close();
        }
    }
    
}
