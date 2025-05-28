package com.epoint.xmz.job;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.xmz.job.api.IGsProjectService;
import com.epoint.xmz.job.api.PreDataUpService;

//将环评办件数据同步到济宁办件库
@DisallowConcurrentExecution
public class HpDataSyncJob implements Job {

	transient static Logger log = LogUtil.getLog(MethodHandles.lookup().lookupClass());

	IGsProjectService iGsProjectService = ContainerFactory.getContainInfo().getComponent(IGsProjectService.class);
	IAuditRsItemBaseinfo iAuditRsItemBaseinfo = ContainerFactory.getContainInfo()
			.getComponent(IAuditRsItemBaseinfo.class);
	IAuditTask iAuditTask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
	IAuditProject iAuditProject = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
	IOuService iOuService = ContainerFactory.getContainInfo().getComponent(IOuService.class);

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
			log.info("===============开始获取环评办件数据===============");
			List<Record> baseinfos = iGsProjectService.getHpDataList();
			if (baseinfos == null || baseinfos.size() == 0) {
				log.info("===============无需要获取的环评办件数据，结束服务===============");
			} else {
				for (Record baseinfo : baseinfos) {
					String HJYXPJGLLB = baseinfo.getStr("baseinfo");
					SqlConditionUtil sql = new SqlConditionUtil();
					if ("BGB".equals(HJYXPJGLLB)) {
						sql.eq("taskname", "建设项目环境影响评价审批(环境影响报告表)");
					} else {
						sql.eq("taskname", "建设项目环境影响评价审批(环境影响报告书)");
					}

					sql.isBlankOrValue("is_history", "0");
					sql.eq("IS_EDITAFTERIMPORT", "1");
					sql.eq("IS_ENABLE", "1");
					sql.eq("ISTEMPLATE", "0");
					sql.eq("AREACODE", baseinfo.get("orgid"));

					List<AuditTask> tasklist = iAuditTask.getAuditTaskList(sql.getMap()).getResult();
					log.info("===============获取的环评办件tasklist==============="+tasklist);
					if (tasklist.get(0) != null) {
						Record rec = new Record();
						rec.setSql_TableName("audit_project_zjxt");
						rec.set("rowguid", UUID.randomUUID().toString());
						rec.set("OperateUserName", "环评数据同步服务");
						rec.set("OperateDate", new Date());
						rec.set("IS_TEST", "0");
						rec.set("BANJIEDATE", new Date());
						rec.set("BANJIERESULT", "40");
						rec.set("ACCEPTUSERDATE", new Date());
//						rec.set("ACCEPTUSERNAME", baseinfo.get("SLR"));
						
						rec.set("applyername", baseinfo.get("BLR"));
						rec.set("flowsn", baseinfo.get("PROJID"));
						rec.set("CONTACTMOBILE", baseinfo.get("BLRLXSJ"));
						rec.set("CONTACTPHONE", baseinfo.get("BLRLXSJ"));
						rec.set("CONTACTPERSON", baseinfo.get("BLR"));
						rec.set("APPLYDATE", new Date());

						rec.set("CERTNUM", baseinfo.get("SQRZJHM"));

						if ("111".equals(baseinfo.get("SQRZJLX"))) {
							rec.set("CERTTYPE", "16");
						}
						if ("02".equals(baseinfo.get("SQRZJLX"))) {
							rec.set("CERTTYPE", "22");
						}
						
						if("1".equals(baseinfo.get("SQRLX"))){
							rec.set("applyertype", "10");
						}else{
							rec.set("applyertype", "20");
						}
						
						
						
						rec.set("address", baseinfo.get("QYZCDZ"));
						rec.set("STATUS", "90");
						rec.set("TASKTYPE", "1");
						// 窗口信息先空着
						rec.set("WINDOWNAME", "");
						rec.set("WINDOWGUID", "");

						rec.set("OUGUID", tasklist.get(0).getOuguid());
						rec.set("OUname", tasklist.get(0).getOuname());
						rec.set("TASKGUID", tasklist.get(0).getRowguid());
						rec.set("PROJECTNAME", tasklist.get(0).getTaskname());
						rec.set("TASKID", tasklist.get(0).getTask_id());
						rec.set("AREACODE", baseinfo.get("ORGID"));
						// 中心guid 待议
						rec.set("CENTERGUID", baseinfo.get("ORGID"));
						rec.set("certnum", baseinfo.get("SQRZJHM"));
						// 数据来源
						rec.set("datasource", "002");
						iGsProjectService.insert(rec);
						log.info("插入环评办件成功");
					}

				}
				log.info("===============结束获取环评办件数据===============");
			}

		} catch (Exception e) {
			log.info("===============获取环评办件数据服务异常===============");
			e.printStackTrace();
		}
	}
}
