package com.epoint.hcp.job;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
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
import com.epoint.zwdt.util.TARequestUtil;

//防止并发执行导致数据重复
@DisallowConcurrentExecution
public class HcpSbGjjEvaluateJob implements Job {
	transient static Logger log = LogUtil.getLog(MethodHandles.lookup().lookupClass());
	
	private static String HCPEVALUATE = ConfigUtil.getConfigValue("hcp", "HcpEvaluateUrl");
    private static String HCPARRAYCODEURL = ConfigUtil.getConfigValue("hcp", "HcpCodeArrayUrl");
	private static String HCPOFFLINETEMPURL = ConfigUtil.getConfigValue("hcp", "HcpOfflineTempUrl");
	private static String HCPAPPMARK = ConfigUtil.getConfigValue("hcp", "HcpAppMark");
	private static String HCPAPPWORD = ConfigUtil.getConfigValue("hcp", "HcpAppWord");
	private static String HCPCODEURL = ConfigUtil.getConfigValue("hcp", "HcpCodeUrl");
		

	IHcpService iHcpService = ContainerFactory.getContainInfo().getComponent(IHcpService.class);
	
	IOuService ouservice = ContainerFactory.getContainInfo().getComponent(IOuService.class);
	
	IAuditProject auditProjectServcie = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);
	
	IAuditTask iAuditTask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);
	
	

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
			log.info("===============开始同步未评价的办件数据===============");
			List<AuditProject> projects = iHcpService.getWaitEvaluateSbList();
			if (projects == null || projects.size() == 0) {
				log.info("===============无需要推送的办件评价数据，结束服务===============");
			} else {
				for (AuditProject project : projects) {
            			Date date = new Date();
			            Calendar c = new GregorianCalendar();
			            c.setTime(date);//设置参数时间
			            c.add(Calendar.MINUTE,-5);//把日期往后增加SECOND 秒.整数往后推,负数往前移动
			            date=c.getTime(); //这个时间就是日期往后推一天的结果
			            String newserviceTime = EpointDateUtil.convertDate2String(date, "yyyy-MM-dd HH:mm:ss");
			            
						String s = turnhcpevaluate(project, 1,"提交申请信息",newserviceTime);
						String status = "1";
						if("1".equals(s)) {
							status = "1";
						}else {
							status = "99";
						}
						project.set("hcpstatus", status);
						auditProjectServcie.updateProject(project);
						EpointFrameDsManager.commit();
				}
				log.info("===============结束同步证照目录代码===============");
			}
			
		} catch (Exception e) {
			log.info("===============推送失败，服务异常===============");
			e.printStackTrace();
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
    public String turnhcpevaluate(AuditProject auditProject, int serviceNumber,String servicename,String newserviceTime) {
		AuditTask auditTask = iAuditTask.getAuditTaskByGuid(auditProject.getTaskguid(), true).getResult();
		if (auditTask != null) {
				
				String taskType = auditTask.getShenpilb();
			    // 公共服务类型不一致，优先转换
			    if ("11".equals(taskType)) {
			        taskType = "20";
			    }
			    switch (taskType) {
			        case "01":
			        case "05":
			        case "07":
			        case "08":
			        case "09":
			        case "10":
			        case "20":
			            break;
			        default:
			            taskType = "99";
			            break;
			    }
			    
				//log.info("=====================开始推送社保办件服务数据=================");
	            JSONObject json = new JSONObject();
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
	            json.put("taskCode", auditTask.getItem_id());
	            json.put("areaCode", auditProject.getAreacode().replace("370882", "370812")+"000000");
	            json.put("taskName", auditTask.getTaskname());
	            json.put("projectNo", auditProject.getFlowsn());
	            String proStatus = serviceNumber+"";
	            String acceptdate = EpointDateUtil.convertDate2String(auditProject.getApplydate(), "yyyy-MM-dd HH:mm:ss");
	            json.put("proStatus", proStatus);
	            json.put("orgcode", auditProject.getAreacode().replace("370882", "370812") + "000000_" + deptcode);
	            json.put("ouguid",ouguid);
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
	            json.put("userName", StringUtil.isNotBlank(auditProject.getApplyername()) ? auditProject.getApplyername().trim() : "无");
	            json.put("userPageType", "111");
	            json.put("proManager", StringUtil.isNotBlank(auditProject.getAcceptusername()) ? auditProject.getAcceptusername().trim() : "无");
	            json.put("certKey", StringUtil.isNotBlank(auditProject.getCertnum()) ? auditProject.getCertnum().trim() : "0");
	            json.put("certKeyGOV", StringUtil.isNotBlank(auditProject.getCertnum()) ? auditProject.getCertnum().trim() : "0");
	            json.put("serviceName", servicename);// 环节名称
	            
	            json.put("serviceNumber", serviceNumber);
		        json.put("serviceTime", newserviceTime);
	            json.put("projectType", auditProject.getTasktype());
	            if (3 == Integer.parseInt(proStatus)) {
	                json.put("resultDate",
	                        EpointDateUtil.convertDate2String(auditProject.getBanjiedate(), "yyyy-MM-dd HH:mm:ss"));
	            }
	            json.put("taskType", auditProject.getTasktype());
	            json.put("mobile", StringUtil.isNotBlank(auditProject.getContactmobile()) ? auditProject.getContactmobile().trim() : "0");
	            json.put("deptCode", deptcode);
	            json.put("projectName", "关于" + auditProject.getApplyername().trim() + auditTask.getTaskname() + "的业务");
	            json.put("creditNum", StringUtil.isNotBlank(auditProject.getCertnum()) ? auditProject.getCertnum().trim() : "0");
	            // 默认证照类型为身份证
	            json.put("creditType", "111");
	            json.put("promiseDay", auditTask.getPromise_day() + "");
	            // 默认办结时间单位为工作日
	            json.put("anticipateDay", "1");
	            // 线上评价为1
	            json.put("proChannel", "2");
	            json.put("promiseTime", auditTask.getType() + "");
	            //log.info("社保办件数据加密前入参：" + json.toString());
	            JSONObject submit = new JSONObject();
	            submit.put("params", json);
	            String resultsign = TARequestUtil.sendPostInner(HCPEVALUATE, submit.toJSONString(), "", "");
	            //log.info("resultsign:"+resultsign);
	            if (StringUtil.isNotBlank(resultsign)) {
	                JSONObject result = JSONObject.parseObject(resultsign);
	                JSONObject custom = result.getJSONObject("custom");
	                if ("1".equals(custom.getString("code"))) {
	                	turnEvaluate(taskType,auditProject.getAreacode().replace("370882", "370812"),"37"+auditProject.getFlowsn(),StringUtil.isNotBlank(auditProject.getApplyername()) ? auditProject.getApplyername().trim() : "无", String.valueOf(serviceNumber));
	    				return "1";
	                }
	                else {
	                	turnEvaluate(taskType,auditProject.getAreacode().replace("370882", "370812"),"37"+auditProject.getFlowsn(),StringUtil.isNotBlank(auditProject.getApplyername()) ? auditProject.getApplyername().trim() : "无", String.valueOf(serviceNumber));
	                	log.info("社保保存办件服务数据失败:" + auditProject.getFlowsn()+"原因："+resultsign);
	                	return "0";
	                	
	                }
	            }
	            else {
                	turnEvaluate(taskType,auditProject.getAreacode().replace("370882", "370812"),"37"+auditProject.getFlowsn(),StringUtil.isNotBlank(auditProject.getApplyername()) ? auditProject.getApplyername().trim() : "无", String.valueOf(serviceNumber));
	            	log.info("社保保存办件服务数据失败：" + auditProject.getFlowsn()+"原因："+resultsign);
	            	return "0";
	            }
		}else {
			return "0";
		}
    }
    
    private void turnEvaluate(String taskType,String areacode,String projectno,String userName,String serviceNumber) {
		
		JSONObject json = new JSONObject();

		json.put("taskType", taskType);
		
		json.put("projectNo", projectno);

		json.put("satisfaction", "5");

		json.put("pf", "1");

		json.put("name", userName);

		json.put("evalDetail", "510,517");

		json.put("writingEvaluation", "");
		
		Date date = new Date();

        Calendar c = new GregorianCalendar();
        c.setTime(date);//设置参数时间
        c.add(Calendar.HOUR, -1);//把日期往后增加SECOND 秒.整数往后推,负数往前移动
        date = c.getTime(); //这个时间就是日期往后推一天的结果
        String newassessTime = EpointDateUtil.convertDate2String(date, "yyyy-MM-dd HH:mm:ss");
        

        
		json.put("assessTime", newassessTime);

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

		Record r = new Record();
		r.setSql_TableName("evainstance");
		String[] primarykeys = { "projectno", "assessNumber" };
		r.setPrimaryKeys(primarykeys);
		r.set("Rowguid", UUID.randomUUID().toString());
		r.set("Flag", "I");
		r.set("Appstatus", Integer.valueOf(0));
		r.set("projectno", projectno);
		r.set("areacode", areacode);
		r.set("Datasource", "165");
		r.set("Assessnumber", Integer.valueOf(1));
		r.set("isdefault", "0");
		r.set("EffectivEvalua", "1");
		r.set("Evalevel", "5");
		r.set("Evacontant", "");
		r.set("evalDetail", "510,517");
		r.set("writingEvaluation", "");
		r.set("Promisetime", "1");
		r.set("createDate", new Date());
		r.set("sync_sign", "0");
		r.set("answerStatus", "0");
		r.set("pf", "1");
		r.set("satisfaction", "5");
		r.set("assessTime",  newassessTime);
		r.set("assessNumber", Integer.valueOf(serviceNumber));
		
		JSONObject result = new JSONObject();
		String resultOnline = "";
		try {
			resultOnline = HttpUtil.doPostJson(HCPOFFLINETEMPURL, submitString.toString());
			//log.info("添加评价数据返回结果如下：" + resultOnline);
			if (StringUtil.isNotBlank(resultOnline)) {
				result = JSONObject.parseObject(resultOnline);
				String code = result.getString("C-Response-Desc");
				
				if ("success".equals(code)) {
					r.set("sbsign", "1");
					r.set("sberrordesc", "同步成功");
					iHcpService.addEvaluate(r);
					EpointFrameDsManager.commit();
				}else {
					r.set("sbsign", "99");
					r.set("sberrordesc", code);
					iHcpService.addEvaluate(r);
					EpointFrameDsManager.commit();
					log.info("评价数据推送失败！");
				}
			}
		} catch (Exception e) {
			r.set("sbsign", "98");
			r.set("sberrordesc", "接口调用失败");
			iHcpService.addEvaluate(r);
			EpointFrameDsManager.commit();
			log.info("评价数据推送失败！");
			e.printStackTrace();
		}
	}
    
       
}
