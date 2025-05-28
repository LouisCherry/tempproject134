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
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.jn.inproject.api.IWebUploaderService;
import com.epoint.xmz.job.api.IGdjService;
import com.epoint.xmz.job.api.IGsProjectService;
import com.epoint.xmz.job.api.PreDataUpService;

//防止并发执行导致数据重复
@DisallowConcurrentExecution
public class GdjBaseinfotTsJob implements Job {
	
	transient static Logger log = LogUtil.getLog(MethodHandles.lookup().lookupClass());
	
	IGsProjectService iGsProjectService  = ContainerFactory.getContainInfo().getComponent(IGsProjectService.class);
	IAuditRsItemBaseinfo iAuditRsItemBaseinfo  = ContainerFactory.getContainInfo().getComponent(IAuditRsItemBaseinfo.class);
	IAuditTask iAuditTask  = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
	IAuditProject iAuditProject  = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
	IOuService iOuService  = ContainerFactory.getContainInfo().getComponent(IOuService.class);
	IWebUploaderService service  = ContainerFactory.getContainInfo().getComponent(IWebUploaderService.class);
	IGdjService iGdjService  = ContainerFactory.getContainInfo().getComponent(IGdjService.class);

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
			List<AuditRsItemBaseinfo> baseinfos = iGsProjectService.getWaitItemList();
			if (baseinfos == null || baseinfos.size() == 0) {
				log.info("===============无需要推送的供电局项目信息数据，结束服务===============");
			} else {
				for (AuditRsItemBaseinfo baseinfo : baseinfos) {
					log.info("修改供电局项目信息推送："+baseinfo.getRowguid());
					AuditRsItemBaseinfo baseinfoold = iGdjService.getSwInteminfoByRowguid(baseinfo.getRowguid());
					if(baseinfoold==null){
						baseinfoold = iGdjService.getSwInteminfoByItemcode(baseinfo.getItemcode());
					}
					if (baseinfoold != null) {
						log.info("修改供电局项目信息推送2："+baseinfoold.getRowguid());
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
						baseinfoold.setConstructionaddress(StringUtil.isNotBlank(baseinfo.getConstructionaddress())?baseinfo.getConstructionaddress():"");
						baseinfoold.setContractperson(baseinfo.getContractperson());
						baseinfoold.setContractphone(baseinfo.getContractphone());
						baseinfoold.setContractemail(baseinfo.getContractemail());
						baseinfoold.setLegalproperty(baseinfo.getLegalproperty());
						baseinfoold.setLandarea(baseinfo.getLandarea());
						baseinfoold.setNewlandarea(StringUtil.isNotBlank(baseinfo.getNewlandarea())?baseinfo.getNewlandarea():0);
						baseinfoold.setAgriculturallandarea(StringUtil.isNotBlank(baseinfo.getAgriculturallandarea())?baseinfo.getAgriculturallandarea():0);
						baseinfoold.setItemcapital(StringUtil.isNotBlank(baseinfo.getItemcapital())?baseinfo.getItemcapital():0);
						baseinfoold.setFundsources(StringUtil.isNotBlank(baseinfo.getFundsources())?baseinfo.getFundsources():"");
						baseinfoold.setFinancialresources(StringUtil.isNotBlank(baseinfo.getFinancialresources())?baseinfo.getFinancialresources():"");
						baseinfoold.setQuantifyconstructtype(StringUtil.isNotBlank(baseinfo.getQuantifyconstructtype())?baseinfo.getQuantifyconstructtype():"");
						baseinfoold.setQuantifyconstructcount(StringUtil.isNotBlank(baseinfo.getQuantifyconstructcount())?baseinfo.getQuantifyconstructcount():0);
						baseinfoold.setQuantifyconstructdept(StringUtil.isNotBlank(baseinfo.getQuantifyconstructdept())?baseinfo.getQuantifyconstructdept():"");
						baseinfoold.setIsimprovement(StringUtil.isNotBlank(baseinfo.getIsimprovement())?baseinfo.getIsimprovement():"");
						if (StringUtil.isNotBlank(baseinfo.getVersiontime())) {
							baseinfoold.setVersiontime(baseinfo.getVersiontime());
						}
						baseinfoold.setVersion(StringUtil.isNotBlank(baseinfo.getVersion())?baseinfo.getVersion():0);
						baseinfoold.setIs_history(StringUtil.isNotBlank(baseinfo.getIs_history())?baseinfo.getIs_history():"");
						baseinfoold.setBiguid(StringUtil.isNotBlank(baseinfo.getBiguid())?baseinfo.getBiguid():"");
						baseinfoold.set("xmguid", StringUtil.isNotBlank(baseinfo.getStr("xmguid"))?baseinfo.getStr("xmguid"):"");
						baseinfoold.set("dlguid", StringUtil.isNotBlank(baseinfo.getStr("dlguid"))?baseinfo.getStr("dlguid"):"");
						baseinfoold.set("clientguid", StringUtil.isNotBlank(baseinfo.getStr("clientguid"))?baseinfo.getStr("clientguid"):"");
						baseinfoold.set("bbdb", StringUtil.isNotBlank(baseinfo.getStr("bbdb"))?baseinfo.getStr("bbdb"):"");
						baseinfoold.set("userguid", StringUtil.isNotBlank(baseinfo.getStr("userguid"))?baseinfo.getStr("userguid"):"");
						baseinfoold.set("kcinput", StringUtil.isNotBlank(baseinfo.getStr("kcinput"))?baseinfo.getStr("kcinput"):"");
						baseinfoold.set("kc", StringUtil.isNotBlank(baseinfo.getStr("kc"))?baseinfo.getStr("kc"):"");
						baseinfoold.set("ps", StringUtil.isNotBlank(baseinfo.getStr("ps"))?baseinfo.getStr("ps"):"");
						baseinfoold.set("psinput", StringUtil.isNotBlank(baseinfo.getStr("psinput"))?baseinfo.getStr("psinput"):"");
						baseinfoold.set("gg", StringUtil.isNotBlank(baseinfo.getStr("gg"))?baseinfo.getStr("gg"):"");
						baseinfoold.set("gginput", StringUtil.isNotBlank(baseinfo.getStr("gginput"))?baseinfo.getStr("gginput"):"");
						baseinfoold.set("other", StringUtil.isNotBlank(baseinfo.getStr("other"))?baseinfo.getStr("other"):"");
						baseinfoold.set("delve", StringUtil.isNotBlank(baseinfo.getStr("delve"))?baseinfo.getStr("delve"):"");
						baseinfoold.set("XiangmuSource", StringUtil.isNotBlank(baseinfo.getStr("XiangmuSource"))?baseinfo.getStr("XiangmuSource"):"");
						baseinfoold.setDepartname(StringUtil.isNotBlank(baseinfo.getDepartname())?baseinfo.getDepartname():"");
						baseinfoold.setConstructionaddress(StringUtil.isNotBlank(baseinfo.getConstructionaddress())?baseinfo.getConstructionaddress():"");
						baseinfoold.setLegalperson(StringUtil.isNotBlank(baseinfo.getLegalperson())?baseinfo.getLegalperson():"");
						baseinfoold.setLegalpersonicardnum(StringUtil.isNotBlank(baseinfo.getLegalpersonicardnum())?baseinfo.getLegalpersonicardnum():"");
						baseinfoold.setFrphone(StringUtil.isNotBlank(baseinfo.getFrphone())?baseinfo.getFrphone():"");
						baseinfoold.setFremail(StringUtil.isNotBlank(baseinfo.getFremail())?baseinfo.getFremail():"");
						baseinfoold.setContractidcart(StringUtil.isNotBlank(baseinfo.getContractidcart())?baseinfo.getContractidcart():"");
						baseinfoold.setParentid(StringUtil.isNotBlank(baseinfo.getParentid())?baseinfo.getParentid():"");
						baseinfoold.setTdhqfs(StringUtil.isNotBlank(baseinfo.getTdhqfs())?baseinfo.getTdhqfs():0);
						baseinfoold.setTdsfdsjfa(StringUtil.isNotBlank(baseinfo.getTdsfdsjfa())?baseinfo.getTdsfdsjfa():0);
						baseinfoold.setSfwcqypg(StringUtil.isNotBlank(baseinfo.getSfwcqypg())?baseinfo.getSfwcqypg():0);
						baseinfoold.setJzmj(StringUtil.isNotBlank(baseinfo.getJzmj())?baseinfo.getJzmj():0);
						baseinfoold.setXmtzly(StringUtil.isNotBlank(baseinfo.getXmtzly())?baseinfo.getXmtzly():0);
						baseinfoold.set("XMZJLY", StringUtil.isNotBlank(baseinfo.getStr("XMZJLY"))?baseinfo.getStr("XMZJLY"):0);
						baseinfoold.setDraft(StringUtil.isNotBlank(baseinfo.getDraft())?baseinfo.getDraft():"");
						baseinfoold.setIssendzj(StringUtil.isNotBlank(baseinfo.getIssendzj())?baseinfo.getIssendzj():"");
						baseinfoold.setGbhy(StringUtil.isNotBlank(baseinfo.getGbhy())?baseinfo.getGbhy():"");
						baseinfoold.setXmzjsx(StringUtil.isNotBlank(baseinfo.getXmzjsx())?baseinfo.getXmzjsx():"");
						baseinfoold.set("xmzb", StringUtil.isNotBlank(baseinfo.getStr("xmzb"))?baseinfo.getStr("xmzb"):0);
						baseinfoold.setGcfl(StringUtil.isNotBlank(baseinfo.getGcfl())?baseinfo.getGcfl():"");
						baseinfoold.set("is_qianqi", StringUtil.isNotBlank(baseinfo.getStr("is_qianqi"))?baseinfo.getStr("is_qianqi"):0);
						baseinfoold.setChcode(StringUtil.isNotBlank(baseinfo.getChcode())?baseinfo.getChcode():"");
						baseinfoold.set("isusepower", StringUtil.isNotBlank(baseinfo.getStr("isusepower"))?baseinfo.getStr("isusepower"):0);
						baseinfoold.set("powercap", StringUtil.isNotBlank(baseinfo.getStr("powercap"))?baseinfo.getStr("powercap"):0);
						if (StringUtil.isNotBlank(baseinfo.getDate("powertime"))) {
							baseinfoold.set("powertime", EpointDateUtil.convertDate2String(baseinfo.getDate("powertime"), EpointDateUtil.DATE_TIME_FORMAT));
						}
						iGdjService.updatesw(baseinfoold);
						log.info("供电局项目信息数据修改成功");
					}else {
						log.info("供电局项目信息数据推送成功");
						log.info("新增供电局项目信息推送："+baseinfoold.getRowguid());
						iGdjService.insertsw(baseinfo);
					}
					baseinfo.set("gdjstatus", "1");
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
