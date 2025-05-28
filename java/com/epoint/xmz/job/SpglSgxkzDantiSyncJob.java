package com.epoint.xmz.job;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.jn.inproject.api.IWebUploaderService;
import com.epoint.jnzwfw.auditproject.auditprojectformsgxkz.api.entity.AuditProjectFormSgxkz;
import com.epoint.xmz.job.api.IGsProjectService;
import com.epoint.xmz.job.api.SpglSyncService;

//防止并发执行导致数据重复
@DisallowConcurrentExecution
public class SpglSgxkzDantiSyncJob implements Job {
	
	transient static Logger log = LogUtil.getLog(MethodHandles.lookup().lookupClass());
	
	IGsProjectService iGsProjectService  = ContainerFactory.getContainInfo().getComponent(IGsProjectService.class);
	IAuditRsItemBaseinfo iAuditRsItemBaseinfo  = ContainerFactory.getContainInfo().getComponent(IAuditRsItemBaseinfo.class);
	IAuditTask iAuditTask  = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
	IAuditProject iAuditProject  = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
	IOuService iOuService  = ContainerFactory.getContainInfo().getComponent(IOuService.class);
	IWebUploaderService service  = ContainerFactory.getContainInfo().getComponent(IWebUploaderService.class);

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			EpointFrameDsManager.begin(null);
			doService();
			EpointFrameDsManager.commit();
		} catch (Exception e) {
			EpointFrameDsManager.rollback();
			e.printStackTrace();
		} finally {
			EpointFrameDsManager.close();
		}
	}

	private void doService() {
		
		try {
			log.info("===============开始推送施工许可证单体信息数据===============");
			List<Record> baseinfos = iGsProjectService.getSpglSgxkzDantiList();
			SpglSyncService preDataUpService = new SpglSyncService();
			if (baseinfos == null || baseinfos.size() == 0) {
				log.info("===============无需要推送的施工许可证单体信息数据，结束服务===============");
			} else {
				for (Record baseinfo : baseinfos) {
					Record record = preDataUpService.getBaseInfoDantiByRowguid(baseinfo.getStr("id"));
					
					if (record == null) {
						baseinfo.setSql_TableName("Project_Single_Info");
						log.info("施工许可证单体信息数据推送成功");
						preDataUpService.insert(baseinfo);
					}
					
					//baseinfo.set("sync", "1");
					//preDataUpService.update(baseinfo);
					
					preDataUpService.updateBaseInfoDanti("1",baseinfo.getStr("id"));
					
				}
				log.info("===============结束推送施工许可证单体信息数据===============");
			}
			
		} catch (Exception e) {
			log.info("===============推送失败，服务异常===============");
			e.printStackTrace();
		}
	}
}
