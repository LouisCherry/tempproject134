package com.epoint.tongbufw;

import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.log.LogUtil;

@DisallowConcurrentExecution
public class ProjectdownRpa implements Job
{
    transient Logger log = LogUtil.getLog(ProjectdownRpa.class);

    /**
     * 程序入口
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            EpointFrameDsManager.begin(null);
            doService();
            EpointFrameDsManager.commit();
        }
        finally {
            EpointFrameDsManager.close();
        }
    }

    //同步一窗受理系统的办件信息
    private void doService() {
        try {
        	ProjectdownRpaService service = new ProjectdownRpaService();
            List<Record> records = service.getProjectbytsflag();
            //办件状态更新办件办结信息
            log.info("开始同步RPA办件结果");
            for (Record record : records) {
            	String rowguid = record.getStr("id");
                AuditProject project = service.getProjectbyflowsn(rowguid);
                if (project != null) {
                	service.updateProjectByRowguid(rowguid);
                	
                	service.updateGsbzByRowguid("90", rowguid);
                	
                }else {
                	//sattus 1:未归档 2：归档  90：办结 80：失败
                	 service.updateGsbzByRowguid("80", rowguid);
                	 log.info("未查询到该办件，办件编号为："+rowguid);
                }
                
            }
            
        }
        catch (Exception e) {
            log.info("同步失败 =====" + e.getMessage());
        }
    }
    
}
