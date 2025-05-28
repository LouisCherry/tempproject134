package com.epoint.xmz.spgl.job;

import java.lang.invoke.MethodHandles;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.epoint.auditproject.monitorsupervise.api.auditprojectold;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditresource.auditrsitembaseinfo.domain.AuditRsItemBaseinfo;
import com.epoint.basic.auditresource.auditrsitembaseinfo.inter.IAuditRsItemBaseinfo;
import com.epoint.basic.auditsp.auditspisubapp.domain.AuditSpISubapp;
import com.epoint.basic.auditsp.auditspisubapp.inter.IAuditSpISubapp;
import com.epoint.basic.auditsp.auditspspsgxk.domain.AuditSpSpSgxk;
import com.epoint.basic.spgl.domain.SpglXmspsxpfwjxxb;
import com.epoint.basic.spgl.inter.ISpglXmspsxpfwjxxb;
import com.epoint.cert.basic.certinfo.certinfoextension.domain.CertInfoExtension;
import com.epoint.cert.basic.certinfo.domain.CertInfo;
import com.epoint.cert.commonservice.DBServcie;
import com.epoint.cert.external.ICertInfoExternal;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.jdbc.connection.DataSourceConfig;
import com.epoint.database.util.MongodbUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.xmz.job.api.IGsProjectService;
import com.epoint.xmz.spgl.api.IPushSpglXmspsxpfwService;

//防止并发执行导致数据重复
@DisallowConcurrentExecution
public class PushYthHistoryInfoJob implements Job {
	
	transient static Logger log = LogUtil.getLog(MethodHandles.lookup().lookupClass());
	
	IPushSpglXmspsxpfwService iPushSpglXmspsxpfwService  = ContainerFactory.getContainInfo().getComponent(IPushSpglXmspsxpfwService.class);
	IAttachService iAttachService  = ContainerFactory.getContainInfo().getComponent(IAttachService.class);
	IAuditProject iAuditProject  = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
	IAuditSpISubapp iauditspisubapp = ContainerFactory.getContainInfo().getComponent(IAuditSpISubapp.class);
	IGsProjectService iGsProjectService = ContainerFactory.getContainInfo().getComponent(IGsProjectService.class);
	ISpglXmspsxpfwjxxb iSpglXmspsxpfwjxxb = ContainerFactory.getContainInfo().getComponent(ISpglXmspsxpfwjxxb.class); 
	ICertInfoExternal iCertInfoExternal = ContainerFactory.getContainInfo().getComponent(ICertInfoExternal.class); 
	IAuditRsItemBaseinfo iAuditRsItemBaseinfo = ContainerFactory.getContainInfo().getComponent(IAuditRsItemBaseinfo.class); 
	
	 private MongodbUtil getMongodbUtil() {
	        DataSourceConfig dsc = new DataSourceConfig(ConfigUtil.getConfigValue("MongodbUrl"),
	                ConfigUtil.getConfigValue("MongodbUserName"), ConfigUtil.getConfigValue("MongodbPassword"));
	        return new MongodbUtil(dsc.getServerName(), dsc.getDbName(), dsc.getUsername(), dsc.getPassword());
	    }
	 
	 
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
			List<auditprojectold> projects = iPushSpglXmspsxpfwService.findOldYthProjectList();
			if (projects == null || projects.size() == 0) {
				log.info("===============无需要推送的工改办件证照数据，结束服务===============");
			} else {
				for (auditprojectold auditProject : projects) {
					
					if (StringUtil.isBlank(auditProject.getSubappguid())) {
						auditProject.set("yth_sync", "99");
//						iAuditProject.updateProject(auditProject);
						continue;
					}
					
					String projectguid = auditProject.getRowguid();
					if (auditProject.getProjectname().contains("建筑工程施工许可证核发")) {
	                	 Record iteminfo = iGsProjectService.getSpglSgxkIteminfoBySubappguid(projectguid);
	                	 if (iteminfo != null) {
	                		 log.info("一体化获取到施工许可信息"+iteminfo);
	                		 String itemrowguid = iteminfo.getStr("ID");
	                		 Record item = iGsProjectService.getProjectBasicInfoByRowguid(itemrowguid);
	                		 if (item == null) {
	                			 log.info("一体化开始新增项目信息");
	                			 iteminfo.setSql_TableName("TB_Project_Info");
	                			 iteminfo.setPrimaryKeys("id");
	                			 AuditSpSpSgxk sgxk =  iGsProjectService.getSpSgxkByRowguid(auditProject.getSubappguid());
	                			 log.info("一体化查询施工许可："+sgxk);
	                			 if (sgxk != null) {
	                				 iteminfo.set("Prj_Approval_Depart", sgxk.getStr("lxpfjg"));
	                				 iteminfo.set("Prj_Approval_Date", EpointDateUtil.convertDate2String(sgxk.getDate("lxpfsj"), EpointDateUtil.DATE_FORMAT));
	                			 }else {
	                				 AuditRsItemBaseinfo baseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(itemrowguid).getResult();
	                				 iteminfo.set("Prj_Approval_Depart", auditProject.getOuname());
	                				 iteminfo.set("Prj_Approval_Date", EpointDateUtil.convertDate2String(baseinfo.getItemstartdate(), EpointDateUtil.DATE_FORMAT));
	                			 }
	                			 log.info("iteminfo:"+iteminfo);
	                			 iGsProjectService.insertQzk(iteminfo);
	                		 }
	                	 }
	                	 
	                	 Record danti = iGsProjectService.getSpglDantiInfoBySubappguid(projectguid);
	                	 if (danti != null) {
	                		 danti.setSql_TableName("TB_Unit_Project_Info");
	                		 danti.setPrimaryKeys("id");
	                		 log.info("一体化danti:"+danti);
	                		 Record unitinfo = iGsProjectService.getProjectUnitInfoByRowguid(danti.getStr("id"));
	                		 if (unitinfo == null) {
		            			 iGsProjectService.insertQzk(danti);
	                		 }
	                	 }
	                	 
	                	 List<Record> Partics = iGsProjectService.getSpglParticipnListBySubappguid(projectguid);
	                	 if (!Partics.isEmpty()) {
	                		 for (Record Partic : Partics) {
	                			 Partic.setSql_TableName("TB_Project_Corp_Info");
	                			 Partic.setPrimaryKeys("id");
	                			 log.info("一体化Partic:"+Partic);
	                			 Record corpinfo = iGsProjectService.getProjectCorpInfoByRowguid(Partic.getStr("id"));
		                		 if (corpinfo == null) {
		                			 iGsProjectService.insertQzk(Partic);
		                		 }
	                    	 }
	                	 }
	                	 
	                	 Record sgxkz = iGsProjectService.getSpglSgxkInfoBySubappguid(projectguid);
	                	 if (sgxkz != null) {
	                		 sgxkz.setSql_TableName("TB_Builder_Licence_Manage");
	                		 sgxkz.setPrimaryKeys("id");
	                		 CertInfoExtension certinfoExtension = null;
	             			// 获取照面信息
	             			Map<String, Object> filter = new HashMap<>();
	             			// 设置基本信息guid
	             			filter.put("certinfoguid", auditProject.getCertrowguid());
	             			certinfoExtension = getMongodbUtil().find(CertInfoExtension.class, filter, false);
	             			String Builder_Licence_Num = "";
	             			if (certinfoExtension != null) {
	             				Builder_Licence_Num = certinfoExtension.getStr("bh");
	             			}else {
	             				Builder_Licence_Num = "无";
	             			}
	             			sgxkz.set("Builder_Licence_Num", Builder_Licence_Num);
	             			log.info("sgxkz:"+sgxkz);
	             			
	             			Record licenceinfo = iGsProjectService.getProjectLicenceByRowguid(sgxkz.getStr("id"));
	                		 if (licenceinfo == null) {
	 	             			iGsProjectService.insertQzk(sgxkz);
	                		 }
	             			
	                	 }
					}else if (auditProject.getProjectname().contains("房屋建筑工程和市政基础设施工程竣工验收备案")) {
	                	 String Prj_Type_Num =  "";
	                	 Record iteminfo = iGsProjectService.getSpglSgxkIteminfoBySubappguid(projectguid);
	                	 log.info("竣工项目信息"+iteminfo);
	                	 if (iteminfo != null) {
	                		 String itemrowguid = iteminfo.getStr("ID");
	                		 Prj_Type_Num = iteminfo.getStr("Prj_Type_Num");
	                		 Record item = iGsProjectService.getProjectBasicInfoByRowguid(itemrowguid);
	                		 if (item == null) {
	                			 log.info("一体化开始新增竣工信息");
	                			 iteminfo.setSql_TableName("TB_Project_Info");
	                			 iteminfo.setPrimaryKeys("id");
	                			 AuditSpSpSgxk sgxk =  iGsProjectService.getSpSgxkByRowguid(auditProject.getSubappguid());
	                			 log.info("一体化施工许可："+sgxk);
	                			 if (sgxk != null) {
	                				 iteminfo.set("Prj_Approval_Depart", sgxk.getStr("lxpfjg"));
	                				 iteminfo.set("Prj_Approval_Date", EpointDateUtil.convertDate2String(sgxk.getDate("lxpfsj"), EpointDateUtil.DATE_FORMAT));
	                			 }else {
	                				 AuditRsItemBaseinfo baseinfo = iAuditRsItemBaseinfo.getAuditRsItemBaseinfoByRowguid(itemrowguid).getResult();
	                				 iteminfo.set("Prj_Approval_Depart", auditProject.getOuname());
	                				 iteminfo.set("Prj_Approval_Date", EpointDateUtil.convertDate2String(baseinfo.getItemstartdate(), EpointDateUtil.DATE_FORMAT));
	                			 }
	                			 log.info("一体化iteminfo1:"+iteminfo);
	                			 iGsProjectService.insertQzk(iteminfo);
	                		 }
	                	 }
	                	 
	                	 Record  jgys = iGsProjectService.getSpglJgYsInfoBySubappguid(projectguid);
	                	 if (jgys != null) {
	                		 jgys.setSql_TableName("TB_Project_Finish_Manage");
	                		 jgys.setPrimaryKeys("id");
	                		 if (StringUtil.isBlank(jgys.getStr("Builder_Licence_Num"))) {
	                			 jgys.set("Builder_Licence_Num", "无");
	                		 }
	                		 if (!"01".equals(Prj_Type_Num)) {
	                			 Prj_Type_Num = "02";
	                		 }
	                		 String numberName = "Prj_Type_Num";
	            	         Calendar calendar = Calendar.getInstance();
	            	         String numberFlag = "" + calendar.get(Calendar.YEAR) + String.format("%02d",calendar.get(Calendar.MONTH)+1) + calendar.get(Calendar.DAY_OF_MONTH);
	            	         int theYearLength = 0;
	            	         int snLength = 2;
	            	         String certno = new DBServcie().getFlowSn(numberName, numberFlag, theYearLength, false, snLength);
	                		 String Prj_Finish_Num = "JB"+auditProject.getAreacode()+certno+Prj_Type_Num;
	                		 jgys.set("Prj_Finish_Num", Prj_Finish_Num);
	                		 log.info("一体化jgys:"+jgys);
	                		 
	                		 Record finishinfo = iGsProjectService.getProjectFinishByRowguid(jgys.getStr("id"));
	                		 if (finishinfo == null) {
		                		 iGsProjectService.insertQzk(jgys);
	                		 }
	                		 
	                	 }
					}
					auditProject.set("yth_sync", "1");
//					iAuditProject.updateProject(auditProject);
					EpointFrameDsManager.commit();
					
				}
				log.info("===============结束推送工改一体化证照信息===============");
			}
	}
	
	 
}
