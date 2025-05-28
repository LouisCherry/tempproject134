package com.epoint.tongbufw;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.epoint.auditprojectunusual.api.IJNAuditProjectUnusual;
import com.epoint.auditprojectunusual.utils.AuditProjectUnusualUtils;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditworkingday.inter.IAuditOrgaWorkingDay;
import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.common.util.HttpRequestUtils;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.organ.ou.api.IOuService;
import com.epoint.frame.service.organ.ou.entity.FrameOuExtendInfo;
import com.epoint.hcp.api.IHcpService;
import com.epoint.util.TARequestUtil;

@DisallowConcurrentExecution
public class ProjectdownForBdcDone implements Job {
	transient Logger log = LogUtil.getLog(ProjectdownForBdcDone.class);

	private static String HCPEVALUATE = ConfigUtil.getConfigValue("hcp", "HcpEvaluateUrl");
	    
    private static String HCPCODEURL = ConfigUtil.getConfigValue("hcp", "HcpCodeUrl");
	private static String HCPARRAYCODEURL = ConfigUtil.getConfigValue("hcp", "HcpCodeArrayUrl");
	private static String HCPOFFLINETEMPURL = ConfigUtil.getConfigValue("hcp", "HcpOfflineTempUrl");
	private static String HCPAPPMARK = ConfigUtil.getConfigValue("hcp", "HcpAppMark");
	private static String HCPAPPWORD = ConfigUtil.getConfigValue("hcp", "HcpAppWord");
	
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
		ProjectdownForBdcService service = new ProjectdownForBdcService();
		IAuditOrgaServiceCenter iAuditOrgaServiceCenter = ContainerFactory.getContainInfo().getComponent(IAuditOrgaServiceCenter.class);
		IJNAuditProjectUnusual ijnAuditProjectUnusual = ContainerFactory.getContainInfo().getComponent(IJNAuditProjectUnusual.class);
		List<Record> list = service.getDoneFromQzk();
		log.info("开始同步浪潮对接的自建系统的办件结果信息");
		for (Record record : list) {
			String flowsn = record.getStr("PROJID");
			try {
				List<Record> donelist = service.getDoneFromQzkByProjid(flowsn);
				AuditProject project = service.getProjectByflowsn(flowsn);
				IAuditProject projectService = (IAuditProject) ContainerFactory.getContainInfo()
						.getComponent(IAuditProject.class);
				IAuditTask auditTaskService = ContainerFactory.getContainInfo()
						.getComponent(IAuditTask.class);
				if (donelist != null && donelist.size() > 0 && project != null) {
					AuditTask auditTask = auditTaskService.getAuditTaskByGuid(project.getTaskguid(), true).getResult();
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
					if (auditTask != null) {
						 // 推送好差评办件服务数据
						turnhcpevaluate(auditTask, project, 2,"出证办结");
					}
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
	
	
	  /**
     * 
     *  [推送好差评办件服务数据] 
     *  @param auditTask
     *  @param auditProject
     *  @param serviceNumber    
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void turnhcpevaluate(AuditTask auditTask, AuditProject auditProject, int serviceNumber,String servicename) {
        try {
            JSONObject json = new JSONObject();
            IOuService ouservice = ContainerFactory.getContainInfo().getComponent(IOuService.class);
            
            String ouguid = auditProject.getOuguid();
            FrameOuExtendInfo frameOuExten = ouservice.getFrameOuExtendInfo(ouguid);
            String deptcode = "";
            if (frameOuExten != null) {
                deptcode = frameOuExten.getStr("orgcode");
                if(StringUtil.isBlank(deptcode)) {
               	 deptcode = "11370900MB28449441";
               }
            }
            else {
                deptcode = "11370900MB28449441";
            }

            String areacode = auditProject.getAreacode();
            if ("370882".equals(areacode)) {
            	areacode = "370812";
            }
            json.put("taskCode", auditTask.getItem_id());
            json.put("areaCode", areacode+"000000");
            json.put("taskName", auditTask.getTaskname());
            json.put("projectNo", auditProject.getFlowsn());
            String proStatus = serviceNumber+"";
            String acceptdate = EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss");
            json.put("proStatus", proStatus);
            json.put("orgcode", areacode + "000000_" + deptcode);
            json.put("ouguid", auditProject.getOuguid());
            // json.put("orgcode", "370900000000_11370900004341048Y");
            json.put("orgName", auditProject.getOuname());
            json.put("acceptDate", acceptdate);
            Integer applyertype = auditProject.getApplyertype();
            if (applyertype == 10) {
                applyertype = 2;
            }
            else if (applyertype == 20) {
                applyertype = 1;
            }
            else if (applyertype == 30) {
                applyertype = 9;
            }
            else {
                applyertype = 9;
            }
            json.put("userProp", applyertype);
            json.put("userName", StringUtil.isNotBlank(auditProject.getApplyername()) ? auditProject.getApplyername().trim() : "无" );
            json.put("userPageType", "111");
            json.put("proManager", auditProject.getAcceptusername());
            json.put("certKey", auditProject.getCertnum());
            json.put("certKeyGOV", auditProject.getCertnum());
            String auditServiceName = "";
            json.put("serviceName", servicename);// 环节名称
            

            String serviceNumUrl = ConfigUtil.getConfigValue("hcp", "HcpServiceNumber");

            JSONObject evJson = new JSONObject();
            evJson.put("commented", "0");
            evJson.put("mark", "0");
            evJson.put("userName", auditProject.getApplyername());
            evJson.put("creditType", 111);
            evJson.put("creditNum", auditProject.getCertnum());
            // evJson.put("commented", "0");
            String getServiceNumber = HttpRequestUtils.sendPost(serviceNumUrl, evJson.toString(),
                    new String[] {"application/json;charset=utf-8" });
            JSONObject jsonN = JSONObject.parseObject(getServiceNumber);
            String content = jsonN.getString("content");
            JSONArray jsonArr = JSONArray.parseArray(content);
            json.put("serviceNumber", serviceNumber);
            
            Calendar c = new GregorianCalendar();
            Date date = new Date();
            c.setTime(date);//设置参数时间
            c.add(Calendar.SECOND,-20);//把日期往后增加SECOND 秒.整数往后推,负数往前移动
            date=c.getTime(); //这个时间就是日期往后推一天的结果
            json.put("serviceTime", EpointDateUtil.convertDate2String(date, "yyyy-MM-dd HH:mm:ss"));
            
            json.put("projectType", auditProject.getTasktype());
            if (3 == Integer.parseInt(proStatus)) {
                json.put("resultDate",
                        EpointDateUtil.convertDate2String(auditProject.getBanjiedate(), "yyyy-MM-dd HH:mm:ss"));
            }
            json.put("tasktype", auditProject.getTasktype());
            json.put("mobile", auditProject.getContactmobile());
            json.put("deptCode", deptcode);
            json.put("projectName", "关于" + auditProject.getApplyername() + auditTask.getTaskname() + "的业务");
            json.put("creditNum", auditProject.getCertnum());
            // 默认证照类型为身份证
            json.put("creditType", "111");
            json.put("promiseDay", auditTask.getPromise_day() + "");
            // 默认办结时间单位为工作日
            json.put("anticipateDay", "1");
            // 线上评价为1
            json.put("proChannel", "2");
            json.put("promiseTime", auditTask.getType() + "");
//            log.info("办件数据加密前入参：" + json.toString());
            JSONObject submit = new JSONObject();
            submit.put("params", json);
            String resultsign = TARequestUtil.sendPostInner(HCPEVALUATE, submit.toJSONString(), "", "");
            if (StringUtil.isNotBlank(resultsign)) {
                JSONObject result = JSONObject.parseObject(resultsign);
                if ("success".equals(result.getString("custom"))) {
                	Record record = new Record();
                	record.set("projectno", "37" + auditProject.getFlowsn());
                	record.set("userName", servicename);
                	record.set("serviceNumber", serviceNumber);
                	turnEvaluate(record);
                	
                	log.info("保存办件服务数据成功:" + auditProject.getFlowsn());
                }
                else {
                	log.info("保存办件服务数据失败:" + auditProject.getFlowsn());
                }
            }
            else {

            	log.info("保存办件服务数据失败：" + auditProject.getFlowsn());
            }
        }
        catch (Exception e) {
        	 e.printStackTrace();
        }
       
    }
    
    private void turnEvaluate(Record record) {
    	
    	IHcpService iHcpService = ContainerFactory.getContainInfo().getComponent(IHcpService.class);
    	
		String projectno = record.getStr("projectno");
		String userName = record.getStr("userName");
		String serviceNumber = record.getStr("serviceNumber");
		JSONObject json = new JSONObject();

		json.put("projectNo", projectno);

		json.put("satisfaction", "5");

		json.put("pf", "1");

		json.put("name", userName);

		json.put("evalDetail", "510,517");

		json.put("writingEvaluation", "");

		json.put("assessTime", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));

		json.put("serviceNumber", serviceNumber);

		JSONObject jsonObjectOnline = new JSONObject();
		JSONObject jsonObject1 = new JSONObject();
		List<JSONObject> list = new ArrayList<>();
		List<String> list1 = new ArrayList<>();
		list.add(json);
		//log.info("线下新增评价加密前入参：" + list.toString());
		JSONObject submit = new JSONObject();
		Map<String, String> contentsign = new HashMap<String, String>();
		contentsign.put("evaluate", list.toString());
		jsonObject1.put("content", contentsign);
		submit.put("params", jsonObject1);
		String resultsign = TARequestUtil.sendPostInner(HCPARRAYCODEURL, submit.toJSONString(), "", "");
		JSONObject json1 = new JSONObject();
		if (!"修改用户默认地址失败".equals(resultsign)) {
			json1 = JSON.parseObject(resultsign);
		}
		list1.add(json1.getString("signcontent"));
		jsonObjectOnline.put("evaluate", list1);

		//log.info("办件数据加密后入参：" + resultsign);
		JSONObject contentOnlineMap = new JSONObject();
		String time = EpointDateUtil.convertDate2String(new Date(), "yyyyMMddHHmmss");
		//log.info("办件数据加密前时间：" + time);

		JSONObject submit1 = new JSONObject();
		Map<String, String> contentsign1 = new HashMap<String, String>();
		contentsign1.put("content", time + HCPAPPMARK + HCPAPPWORD);
		submit1.put("params", contentsign1);

		String resultsign1 = TARequestUtil.sendPostInner(HCPCODEURL, submit1.toJSONString(), "", "");
		JSONObject json2 = new JSONObject();
		if (!"修改用户默认地址失败".equals(resultsign1)) {
			json2 = JSON.parseObject(resultsign1);
		}

		contentOnlineMap.put("sign", json2.getString("signcontent"));
		contentOnlineMap.put("params", jsonObjectOnline);
		contentOnlineMap.put("time", time);
		contentOnlineMap.put("appMark", HCPAPPMARK);
		JSONObject submitString = new JSONObject();
		submitString.put("txnBodyCom", contentOnlineMap);
		submitString.put("txnCommCom", new JSONObject());
		//log.info("办件数据所有入参：" + contentOnlineMap.toString());
		//log.info("办件数据url：" + HCPOFFLINETEMPURL);

		String resultOnline = "";
		try {
			resultOnline = HttpUtil.doPostJson(HCPOFFLINETEMPURL, submitString.toString());
			log.info("添加评价数据返回结果如下：" + resultOnline);
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject result = new JSONObject();
		JSONObject dataJson = new JSONObject();
		if (StringUtil.isNotBlank(resultOnline)) {
			result = JSONObject.parseObject(resultOnline);
			String code = result.getString("C-Response-Desc");
			if ("success".equals(code)) {
				Record r = new Record();
				r.setSql_TableName("evainstance");
				String[] primarykeys = { "projectno", "assessNumber" };
				r.setPrimaryKeys(primarykeys);
				r.set("Rowguid", UUID.randomUUID().toString());
				r.set("Flag", "I");
				r.set("Appstatus", Integer.valueOf(0));
				r.set("projectno", projectno);
				r.set("Datasource", "165");
				r.set("Assessnumber", Integer.valueOf(1));
				r.set("isdefault", "0");
				r.set("EffectivEvalua", "1");
				r.set("Areacode", record.getStr("areacode"));
				r.set("Prostatus", record.getStr("proStatus"));
				r.set("Evalevel", "5");
				r.set("Evacontant", "");
				r.set("evalDetail", "510,517");
				r.set("writingEvaluation", "");
				r.set("Taskname", record.getStr("Taskname"));
				r.set("Taskcode", record.getStr("Taskcode"));
				r.set("Promisetime", "1");
				r.set("Deptcode", record.getStr("orgcode"));
				r.set("Userprop", record.getStr("Userprop"));
				r.set("Username", record.getStr("Username"));
				r.set("Applicant", record.getStr("Username"));
				r.set("Certkey", record.getStr("Certkey"));
				r.set("Certkeygov", record.getStr("Certkeygov").trim());
				r.set("Acceptdate", record.getStr("Acceptdate"));
				r.set("createDate", new Date());
				r.set("sync_sign", "0");
				r.set("answerStatus", "0");
				r.set("pf", "1");
				r.set("satisfaction", "5");
				r.set("assessTime", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
				r.set("assessNumber", Integer.valueOf(serviceNumber));
				r.set("sbsign", "1");
				r.set("sberrordesc", "同步成功");
				iHcpService.addEvaluate(r);
				dataJson.put("success", "success");
				log.info("新增服务评价数据成功");
				record.set("sbsign", "2");
				record.set("sberrordesc", "评价数据重新推送成功");
				iHcpService.updateProService(record);
			}else
			{
				record.set("sbsign", "4");
				record.set("sberrordesc", "评价数据推送成功了");
				iHcpService.updateProService(record);
			}
		} else {
			//log.info("新增服务评价数据失败,result:" + resultOnline);
			record.set("sbsign", "3");
			record.set("sberrordesc", "评价数据重新推送失败");
			iHcpService.updateProService(record);
		}
	}

}
