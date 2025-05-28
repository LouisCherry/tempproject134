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
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.jn.inproject.api.IWebUploaderService;
import com.epoint.xmz.job.api.IGsProjectService;
import com.epoint.xmz.job.api.ISwService;

//防止并发执行导致数据重复
@DisallowConcurrentExecution
public class SwBaseinfotTsJob implements Job {
	
	transient static Logger log = LogUtil.getLog(MethodHandles.lookup().lookupClass());
	
	IGsProjectService iGsProjectService  = ContainerFactory.getContainInfo().getComponent(IGsProjectService.class);
	IAuditRsItemBaseinfo iAuditRsItemBaseinfo  = ContainerFactory.getContainInfo().getComponent(IAuditRsItemBaseinfo.class);
	IAuditTask iAuditTask  = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
	IAuditProject iAuditProject  = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
	IOuService iOuService  = ContainerFactory.getContainInfo().getComponent(IOuService.class);
	IWebUploaderService service  = ContainerFactory.getContainInfo().getComponent(IWebUploaderService.class);
	ISwService iSwService  = ContainerFactory.getContainInfo().getComponent(ISwService.class);

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
			log.info("===============开始推送供电局项目信息数据===============");
			List<AuditRsItemBaseinfo> baseinfos = iGsProjectService.getSwWaitItemList();
			if (baseinfos == null || baseinfos.size() == 0) {
				log.info("===============无需要推送的供电局项目信息数据，结束服务===============");
			} else {
				for (AuditRsItemBaseinfo baseinfo : baseinfos) {
					
					AuditRsItemBaseinfo baseinfoold = iSwService.getSwInteminfoByItemcode(baseinfo.getItemcode());
					if (baseinfoold != null) {
						baseinfoold.setBelongxiaqucode(baseinfo.getBelongxiaqucode());
						baseinfoold.setOperatedate(new Date());
						baseinfoold.setItemcode(baseinfo.getItemcode());
						baseinfoold.setItemname(baseinfo.getItemname());
						baseinfoold.setItemtype(baseinfo.getItemtype());
						baseinfoold.setConstructionproperty(baseinfo.getConstructionproperty());
						baseinfoold.setItemlegaldept(baseinfo.getItemlegaldept());
						baseinfoold.setItemlegalcreditcode(baseinfo.getItemlegalcreditcode());
						baseinfoold.setItemlegalcerttype(baseinfo.getItemlegalcerttype());
						baseinfoold.setItemlegalcertnum(baseinfo.getItemlegalcertnum());
						baseinfoold.setItemstartdate(baseinfo.getItemstartdate());
						baseinfoold.setItemfinishdate(baseinfo.getItemfinishdate());
						baseinfoold.setTotalinvest(baseinfo.getTotalinvest());
						baseinfoold.setConstructionsite(baseinfo.getConstructionsite());
						baseinfoold.setConstructionsitedesc(baseinfo.getConstructionsitedesc());
						baseinfoold.setBelongtindustry(baseinfo.getBelongtindustry());
						baseinfoold.setConstructionaddress(baseinfo.getConstructionaddress());
						baseinfoold.setContractperson(baseinfo.getContractperson());
						baseinfoold.setContractphone(baseinfo.getContractphone());
						baseinfoold.setContractemail(baseinfo.getContractemail());
						baseinfoold.setLegalproperty(baseinfo.getLegalproperty());
						baseinfoold.setLandarea(baseinfo.getLandarea());
						baseinfoold.setNewlandarea(baseinfo.getNewlandarea());
						baseinfoold.setAgriculturallandarea(baseinfo.getAgriculturallandarea());
						baseinfoold.setItemcapital(baseinfo.getItemcapital());
						baseinfoold.setFundsources(baseinfo.getFundsources());
						baseinfoold.setFinancialresources(baseinfo.getFinancialresources());
						baseinfoold.setQuantifyconstructtype(baseinfo.getQuantifyconstructtype());
						baseinfoold.setQuantifyconstructcount(baseinfo.getQuantifyconstructcount());
						baseinfoold.setQuantifyconstructdept(baseinfo.getQuantifyconstructdept());
						baseinfoold.setIsimprovement(baseinfo.getIsimprovement());
						baseinfoold.setVersiontime(baseinfo.getVersiontime());
						baseinfoold.setVersion(baseinfo.getVersion());
						baseinfoold.setIs_history(baseinfo.getIs_history());
						baseinfoold.setBiguid(baseinfo.getBiguid());
						baseinfoold.set("xmguid", baseinfo.getStr("xmguid"));
						baseinfoold.set("dlguid", baseinfo.getStr("dlguid"));
						baseinfoold.set("clientguid", baseinfo.getStr("clientguid"));
						baseinfoold.set("bbdb", baseinfo.getStr("bbdb"));
						baseinfoold.set("userguid", baseinfo.getStr("userguid"));
						baseinfoold.set("kcinput", baseinfo.getStr("kcinput"));
						baseinfoold.set("kc", baseinfo.getStr("kc"));
						baseinfoold.set("ps", baseinfo.getStr("ps"));
						baseinfoold.set("psinput", baseinfo.getStr("psinput"));
						baseinfoold.set("gg", baseinfo.getStr("gg"));
						baseinfoold.set("gginput", baseinfo.getStr("gginput"));
						baseinfoold.set("other", baseinfo.getStr("other"));
						baseinfoold.set("delve", baseinfo.getStr("delve"));
						baseinfoold.set("XiangmuSource", baseinfo.getStr("XiangmuSource"));
						baseinfoold.setDepartname(baseinfo.getDepartname());
						baseinfoold.setConstructionaddress(baseinfo.getConstructionaddress());
						baseinfoold.setLegalperson(baseinfo.getLegalperson());
						baseinfoold.setLegalpersonicardnum(baseinfo.getLegalpersonicardnum());
						baseinfoold.setFrphone(baseinfo.getFrphone());
						baseinfoold.setFremail(baseinfo.getFremail());
						baseinfoold.setContractidcart(baseinfo.getContractidcart());
						baseinfoold.setParentid(baseinfo.getParentid());
						baseinfoold.setTdhqfs(baseinfo.getTdhqfs());
						baseinfoold.setTdsfdsjfa(baseinfo.getTdsfdsjfa());
						baseinfoold.setSfwcqypg(baseinfo.getSfwcqypg());
						baseinfoold.setJzmj(baseinfo.getJzmj());
						baseinfoold.setXmtzly(baseinfo.getXmtzly());
						baseinfoold.set("XMZJLY", baseinfo.getStr("XMZJLY"));
						baseinfoold.setDraft(baseinfo.getDraft());
						baseinfoold.setIssendzj(baseinfo.getIssendzj());
						baseinfoold.setGbhy(baseinfo.getGbhy());
						baseinfoold.setXmzjsx(baseinfo.getXmzjsx());
						baseinfoold.set("xmzb", baseinfo.getStr("xmzb"));
						baseinfoold.setGcfl(baseinfo.getGcfl());
						baseinfoold.set("is_qianqi", baseinfo.getStr("is_qianqi"));
						baseinfoold.setChcode(baseinfo.getChcode());
						baseinfoold.set("isusepower", baseinfo.getStr("isusepower"));
						baseinfoold.set("powercap", baseinfo.getStr("powercap"));
						baseinfoold.set("powertime", EpointDateUtil.convertDate2String(baseinfo.getDate("powertime"), EpointDateUtil.DATE_TIME_FORMAT));
						iSwService.updatesw(baseinfoold);
						log.info("供电局项目信息数据修改成功");
					}else {
						log.info("供电局项目信息数据推送成功");
						iSwService.insertsw(baseinfo);
					}
					baseinfo.set("swstatus", "1");
					iAuditRsItemBaseinfo.updateAuditRsItemBaseinfo(baseinfo);
					
				}
				log.info("===============结束推送供电局项目信息数据===============");
			}
			
		} catch (Exception e) {
			log.info("===============推送失败，服务异常===============");
			e.printStackTrace();
		}
	}
}
