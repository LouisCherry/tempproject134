package com.epoint.hcp.job;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.List;

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
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.hcp.api.IHcpService;
import com.epoint.zwdt.util.TARequestUtil;

//防止并发执行导致数据重复
@DisallowConcurrentExecution
public class HcpOneTwoEvaluateJob implements Job {
	transient static Logger log = LogUtil.getLog(MethodHandles.lookup().lookupClass());

	IAuditProject auditProjectServcie = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);

	IHcpService iHcpService = ContainerFactory.getContainInfo().getComponent(IHcpService.class);

	IAuditTask iAuditTask = ContainerFactory.getContainInfo().getComponent(IAuditTask.class);

	private static String ONETWOURL = ConfigUtil.getConfigValue("hcp", "ontwohcpurl");

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
			log.info("===============开始查询未评价的12345办件数据===============");
			List<Record> waitevaluates = iHcpService.getOneTwoEvaluateList();
			if (waitevaluates == null || waitevaluates.isEmpty()) {
				log.info("===============无需要推送12345的办件评价数据，结束服务===============");
			} else {
				for (Record record : waitevaluates) {
					
					//控制时间超过五天的进行推送评价办件服务数据
            		Date applydate = record.getDate("createdate");
            		int hours = (int) ((new Date().getTime() - applydate.getTime()) / (1000*3600));
            		if(hours >1){
            			String projectno = record.getStr("projectno");
    					String servicenumber = record.getStr("servicenumber");
    					Record evainstance = iHcpService.findEvaluateservice(projectno, servicenumber);
    					if (evainstance != null) {
    						if ("1".equals(evainstance.getStr("sbsign"))) {
    							record.set("sbsign", "4");
    							record.set("sberrordesc", "该评价数据已经正常推送");
    							iHcpService.updateProService(record,null);
    							log.info("===============该办件已经进行评价，无需推送，结束服务===============");
    						} else {
    							log.info("12345好差评评价数据失败，进行推送");
    							turnEvaluate(record);
    						}
    					} else {
    						log.info("12345好差评评价数据不存在，进行推送");
    						turnEvaluate(record);
    					}
            		}
				}
			}
			log.info("===============结束同步证照目录代码===============");
		} catch (Exception e) {
			log.info("===============推送失败，服务异常===============");
			e.printStackTrace();
		}
	}

	private void turnEvaluate(Record record) {
		SqlConditionUtil sql = new SqlConditionUtil();
		sql.eq("flowsn", record.getStr("projectno").substring(2));
		sql.isNotBlank("applydate");
		sql.isBlankOrValue("is_lczj", "0");
		AuditProject project = auditProjectServcie.getAuditProjectByCondition(sql.getMap());
		if (project != null) {
			AuditTask auditTask = iAuditTask.getAuditTaskByGuid(project.getTaskguid(), true).getResult();
			if (auditTask != null) {
				String taskCode = auditTask.getStr("taskcode");
				String userName = record.getStr("username");
				String mobile = record.getStr("mobile");
				String taskName = auditTask.getTaskname();
				String acceptDate = EpointDateUtil.convertDate2String(project.getApplydate(), "yyyy-MM-dd HH:mm:ss");
				String creditNum = record.getStr("certkey");
				String projectName = project.getProjectname();
				String remark = "一窗受理办结办件推送";
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("taskCode", taskCode);
				jsonObject.put("userName", userName);
				jsonObject.put("mobile", mobile);
				jsonObject.put("taskName", taskName);
				jsonObject.put("eventCode", "2");
				jsonObject.put("acceptDate", acceptDate);
				jsonObject.put("creditNum", creditNum);
				jsonObject.put("projectName", projectName);
				jsonObject.put("remark", remark);
				jsonObject.put("flowsn", record.getStr("projectno"));
				String resultsign = TARequestUtil.sendPostInner(ONETWOURL, jsonObject.toJSONString(), "", "");
				log.info("12345返回信息resultsign："+resultsign);
				JSONObject json = JSON.parseObject(resultsign);
				if ("保存成功".equals(json.getString("msg"))) {
					log.info("推送12345办件服务数据成功");
					record.set("sbsign", "4");
					record.set("sberrordesc", "评价数据推送成功了");
					iHcpService.updateProService(record,null);
				} else {
					record.set("sbsign", "3");
					record.set("sberrordesc", "推送12345失败，接口调用失败");
					iHcpService.updateProService(record,null);
				}

			} else {
				record.set("sbsign", "3");
				record.set("sberrordesc", "推送12345失败，事项信息未找到");
				iHcpService.updateProService(record,null);
			}
		} else {
			record.set("sbsign", "88");
			record.set("sberrordesc", "推送12345失败，办件信息未找到");
			iHcpService.updateProService(record,null);
		}
	}
}
