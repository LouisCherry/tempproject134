package com.epoint.xmz.spgl.job;

import java.lang.invoke.MethodHandles;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.spgl.domain.SpglXmspsxpfwjxxb;
import com.epoint.basic.spgl.inter.ISpglXmspsxpfwjxxb;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.commonservice.DBServcie;
import com.epoint.cert.external.ICertInfoExternal;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.xmz.spgl.api.IPushSpglXmspsxpfwService;

//防止并发执行导致数据重复
@DisallowConcurrentExecution
public class PushSpglXmspsxpfwjxxbJob implements Job {
	
	transient static Logger log = LogUtil.getLog(MethodHandles.lookup().lookupClass());
	
	IPushSpglXmspsxpfwService iPushSpglXmspsxpfwService  = ContainerFactory.getContainInfo().getComponent(IPushSpglXmspsxpfwService.class);
	IAttachService iAttachService  = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
	IAuditProject iAuditProject  = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
	IAuditSpISubapp iauditspisubapp = ContainerFactory.getContainInfo().getComponent(IAuditSpISubapp.class);
	IAuditRsItemBaseinfo iauditrsitembaseinfo = ContainerFactory.getContainInfo().getComponent(IAuditRsItemBaseinfo.class);
	ISpglXmspsxpfwjxxb iSpglXmspsxpfwjxxb = ContainerFactory.getContainInfo().getComponent(ISpglXmspsxpfwjxxb.class); 
	ICertInfoExternal iCertInfoExternal = ContainerFactory.getContainInfo().getComponent(ICertInfoExternal.class); 
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			EpointFrameDsManager.begin(null);
			doService();
			EpointFrameDsManager.commit();
		} catch (Exception e) {
			log.info("===============推送失败，服务异常===============");
			EpointFrameDsManager.rollback();
			e.printStackTrace();
		} finally {
			EpointFrameDsManager.close();
		}
	}

	private void doService() {
		
			log.info("===============开始查询工改办件证照数据===============");
			List<AuditProject> projects = iPushSpglXmspsxpfwService.findProjectList();
			if (projects == null || projects.size() == 0) {
				log.info("===============无需要推送的工改办件证照数据，结束服务===============");
			} else {
				for (AuditProject project : projects) {
					 AuditSpISubapp sub = iauditspisubapp.getSubappByGuid(project.getSubappguid()).getResult();
			         if(sub == null){
			        	 log.info("工改办件不存在申报信息，办件编号:"+project.getFlowsn());
			            continue;
			         }
			         AuditRsItemBaseinfo rsitem = iauditrsitembaseinfo.getAuditRsItemBaseinfoByRowguid(sub.getYewuguid()).getResult();
			         if(rsitem == null){
			        	 log.info("工改办件不存在项目信息，办件编号:"+project.getFlowsn());
			        	 continue;
			         }
			         
			         if (StringUtil.isBlank(project.getBanjiedate())) {
			        	 log.info("工改办件不存在办结日期，办件编号:"+project.getFlowsn());
			        	 continue;
			         }
			         
			        String pfwh = getPfwh();
			        EpointFrameDsManager.commit();
			        
			        
			        //推送批复文书
					String officaldocguid = project.getOfficaldocguid();
					if (StringUtil.isNotBlank(officaldocguid)) {
						List<FrameAttachInfo> docattachs = iAttachService.getAttachInfoListByGuid(officaldocguid);
						if (!docattachs.isEmpty()) {
							for (FrameAttachInfo attach : docattachs) {
								SpglXmspsxpfwjxxb spgl = new SpglXmspsxpfwjxxb();
								spgl.setRowguid(UUID.randomUUID().toString());
								spgl.setDfsjzj(project.getRowguid());
								spgl.setXzqhdm("370800");
								spgl.setGcdm(rsitem.getItemcode());
								spgl.setSpsxslbm(project.getFlowsn());
								spgl.setPfrq(project.getBanjiedate());
								spgl.setPfwh(pfwh);
								spgl.setPfwjbt(attach.getAttachFileName());
								spgl.set("Pfwjyxqx", "9999-01-01");
								spgl.setFjmc(attach.getAttachFileName());
								spgl.setFjlx(attach.getContentType().replace(".", ""));
								spgl.setFjid(attach.getAttachGuid());
								spgl.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
					            spgl.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
					            iSpglXmspsxpfwjxxb.insert(spgl);
							}
						}
						
					}
					
					
					//推送结果文件
					String projectattachguid = project.getRowguid();
					if (StringUtil.isNotBlank(projectattachguid)) {
						List<FrameAttachInfo> docattachs = iAttachService.getAttachInfoListByGuid(projectattachguid);
						if (!docattachs.isEmpty()) {
							for (FrameAttachInfo attach : docattachs) {
								SpglXmspsxpfwjxxb spgl = new SpglXmspsxpfwjxxb();
								spgl.setRowguid(UUID.randomUUID().toString());
								spgl.setDfsjzj(project.getRowguid());
								spgl.setXzqhdm("370800");
								spgl.setGcdm(rsitem.getItemcode());
								spgl.setSpsxslbm(project.getFlowsn());
								spgl.setPfrq(project.getBanjiedate());
								spgl.setPfwh(pfwh);
								spgl.setPfwjbt(attach.getAttachFileName());
								spgl.set("Pfwjyxqx", "9999-01-01");
								spgl.setFjmc(attach.getAttachFileName());
								spgl.setFjlx(attach.getContentType().replace(".", ""));
								spgl.setFjid(attach.getAttachGuid());
								spgl.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
					            spgl.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
					            iSpglXmspsxpfwjxxb.insert(spgl);
							}
						}
						
					}
					
					
					//推送证照附件
					String certrowguid = project.getCertrowguid();
					CertInfo certinfo = iCertInfoExternal.getCertInfoByRowguid(certrowguid);
					if (certinfo != null) {
						if (StringUtil.isNotBlank(certinfo.getCertcliengguid())) {
							List<FrameAttachInfo> docattachs = iAttachService.getAttachInfoListByGuid(certinfo.getCertcliengguid());
							if (!docattachs.isEmpty()) {
								for (FrameAttachInfo attach : docattachs) {
									SpglXmspsxpfwjxxb spgl = new SpglXmspsxpfwjxxb();
									spgl.setRowguid(UUID.randomUUID().toString());
									spgl.setDfsjzj(project.getRowguid());
									spgl.setXzqhdm("370800");
									spgl.setGcdm(rsitem.getItemcode());
									spgl.setSpsxslbm(project.getFlowsn());
									spgl.setPfrq(project.getBanjiedate());
									spgl.setPfwh(pfwh);
									spgl.setPfwjbt(attach.getAttachFileName());
									spgl.set("Pfwjyxqx", "9999-01-01");
									spgl.setFjmc(attach.getAttachFileName());
									spgl.setFjlx(attach.getContentType().replace(".", ""));
									spgl.setFjid(attach.getAttachGuid());
									spgl.setSjyxbs(ZwfwConstant.CONSTANT_INT_ONE);
						            spgl.setSjsczt(ZwfwConstant.CONSTANT_INT_ZERO);
						            iSpglXmspsxpfwjxxb.insert(spgl);
								}
							}
							
						}
					}
					project.set("pfwj_sync", "1");
					iAuditProject.updateProject(project);
					EpointFrameDsManager.commit();
				}
				log.info("===============结束推送工改办件证照信息===============");
			}
	}
	
	 public String getPfwh() {
	        String numberName = "Jn_SpglXmspsxpfw";
	        Calendar calendar = Calendar.getInstance();
	        String numberFlag = "" + calendar.get(Calendar.YEAR) + String.format("%02d",calendar.get(Calendar.MONTH)+1) + calendar.get(Calendar.DAY_OF_MONTH);
	        int theYearLength = 0;
	        int snLength = 3;
	        String certno = new DBServcie().getFlowSn(numberName, numberFlag, theYearLength, false, snLength);
	        return certno;
	 }
	 
}
