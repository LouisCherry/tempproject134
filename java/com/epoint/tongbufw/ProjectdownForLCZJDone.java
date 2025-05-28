package com.epoint.tongbufw;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.epoint.auditprojectunusual.api.IJNAuditProjectUnusual;
import com.epoint.auditprojectunusual.utils.AuditProjectUnusualUtils;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditworkingday.inter.IAuditOrgaWorkingDay;
import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.core.utils.date.EpointDateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.workflow.bizlogic.domain.execute.WorkflowWorkItemHistory;

@DisallowConcurrentExecution
public class ProjectdownForLCZJDone implements Job {
	transient Logger log = LogUtil.getLog(ProjectdownForLCZJDone.class);

	/**
	 * 程序入口
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			EpointFrameDsManager.begin(null);
			// job作业比框架起的早 导致部分接口没有实例化
			Thread.sleep(30000);
			syncProjectinfo();
			EpointFrameDsManager.commit();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			EpointFrameDsManager.close();
		}
	}

	// 同步浪潮对接的自建系统的办件信息
	private void syncProjectinfo() {
		ProjectdownForLCZJService service = new ProjectdownForLCZJService();
		IAuditOrgaServiceCenter iAuditOrgaServiceCenter = ContainerFactory.getContainInfo().getComponent(IAuditOrgaServiceCenter.class);
		IJNAuditProjectUnusual ijnAuditProjectUnusual = ContainerFactory.getContainInfo().getComponent(IJNAuditProjectUnusual.class);
		IAuditTask iaudittask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
		List<Record> list = service.getDoneFromQzk();
		log.info("开始同步浪潮对接的自建系统的办件结果信息");
		for (Record record : list) {
			String flowsn = record.getStr("PROJID");
			try {
				List<Record> donelist = service.getDoneFromQzkByProjid(flowsn);
				AuditProject project = service.getProjectByflowsn(flowsn);
				IAuditProject projectService = (IAuditProject) ContainerFactory.getContainInfo()
						.getComponent(IAuditProject.class);
				if (donelist != null && donelist.size() > 0 && project != null) {
					project.setStatus(90);
					project.setBanjieresult(40);
					Date banjiedate = donelist.get(0).getDate("OCCURTIME");
					Date acceptdate = project.getAcceptuserdate();
					project.setSpendtime((int) ((banjiedate.getTime() - acceptdate.getTime()) / 1000 / 60));
					project.setBanjiedate(banjiedate);
					project.setBanjieusername(donelist.get(0).getStr("USER_NAME"));

					//查看有无centerguid
					if(StringUtils.isBlank(project.getCenterguid())){
						AuditOrgaServiceCenter auditOrgaServiceCenter = iAuditOrgaServiceCenter.getAuditServiceCenterByBelongXiaqu(project.getAreacode());
						if(auditOrgaServiceCenter!=null){
							project.setCenterguid(auditOrgaServiceCenter.getRowguid());
						}
					}
					IAuditOrgaWorkingDay auditCenterWorkingDayService = ContainerFactory.getContainInfo()
							.getComponent(IAuditOrgaWorkingDay.class);
					//更新承诺办结时间
					AuditTask  auditTask = iaudittask.getAuditTaskByGuid(project.getTaskguid(),true).getResult();
					if(auditTask!=null) {
						List<AuditProjectUnusual> auditProjectUnusuals = ijnAuditProjectUnusual.getZantingData(project.getRowguid());
						Date acceptdat = project.getAcceptuserdate();
						Date shouldEndDate;
						if (auditTask.getPromise_day() != null && auditTask.getPromise_day() > 0) {

							shouldEndDate = auditCenterWorkingDayService.getWorkingDayWithOfficeSet(
									project.getCenterguid(), acceptdat, auditTask.getPromise_day()).getResult();
							log.info("shouldEndDate:"+shouldEndDate);
						} else {
							shouldEndDate = null;
						}
						if(auditProjectUnusuals != null && auditProjectUnusuals.size() > 0) {
							                AuditProjectUnusualUtils auditProjectUnusualUtils= new AuditProjectUnusualUtils();
                int totalWorkingDaysPaused  = auditProjectUnusualUtils.calculateTotalWorkingDaysPaused(auditProjectUnusuals,project.getCenterguid());
                if (totalWorkingDaysPaused > 0 && shouldEndDate != null) {
                    // 重新计算包含暂停时间的预计结束日期
                    shouldEndDate = auditCenterWorkingDayService.getWorkingDayWithOfficeSet(
							project.getCenterguid(), shouldEndDate, (int) totalWorkingDaysPaused).getResult();
                    log.info("考虑暂停时间后的预计结束日期 shouldEndDate: " + shouldEndDate);
                }
                log.info("shouldEndDate:"+shouldEndDate);
						}
						if(shouldEndDate!=null && !"1753-1-1".equals(EpointDateUtil.convertDate2String(shouldEndDate))){
       project.setPromiseenddate(shouldEndDate);
}
					}
					projectService.updateProject(project);
					EpointFrameDsManager.commit();
					service.updateDoneFlagByFlowsn("1", flowsn);
					log.info("该省下发已办结办件同步成功，办件号为：" + flowsn);
				} else {
					service.updateDoneFlagByFlowsn("9", flowsn);
					log.info("该省下发办件未找到,办件号为：" + flowsn);
				}
			} catch (Exception e) {
				e.printStackTrace();
				service.updateDoneFlagByFlowsn("3", flowsn);
				log.info("同步浪潮自建系统数据失败 =====" + e.getMessage() + record);

			}
			log.info("同步浪潮自建系统数据结束" + list.size());
		}
	}

}
