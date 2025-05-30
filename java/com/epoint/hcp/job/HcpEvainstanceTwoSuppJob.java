package com.epoint.hcp.job;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.log.LogUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.hcp.api.IHcpService;
import com.epoint.hcp.api.entity.Evainstance;
import com.epoint.hcp.api.entity.EvainstanceRecord;
import com.epoint.zwdt.util.TARequestUtil;

//防止并发执行导致数据重复
@DisallowConcurrentExecution
public class HcpEvainstanceTwoSuppJob implements Job {
	transient static Logger log = LogUtil.getLog(MethodHandles.lookup().lookupClass());
	
    private static String HCPARRAYCODEURL = ConfigUtil.getConfigValue("hcp", "HcpCodeArrayUrl");
	private static String HCPOFFLINETEMPURL = ConfigUtil.getConfigValue("hcp", "HcpOfflineTempUrl");
	private static String HCPAPPMARK = ConfigUtil.getConfigValue("hcp", "HcpAppMark");
	private static String HCPAPPWORD = ConfigUtil.getConfigValue("hcp", "HcpAppWord");
	private static String HCPCODEURL = ConfigUtil.getConfigValue("hcp", "HcpCodeUrl");
		

	IAuditProject auditProjectServcie = ContainerFactory.getContainInfo().getComponent(IAuditProject.class);

	IHcpService iHcpService = ContainerFactory.getContainInfo().getComponent(IHcpService.class);

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
			log.info("===============开始查询待补推的办件评价数据===============");
			List<EvainstanceRecord> waitevaluates = iHcpService.getHcpInstacneTwoSuppList();
			if (waitevaluates == null || waitevaluates.isEmpty()) {
				log.info("===============无需要待补推的办件评价数据，结束服务===============");
			} else {
				for (EvainstanceRecord evainstan : waitevaluates) {
					turnEvaluate(evainstan.getStr("taskType"),evainstan.getProjectno(),evainstan.getUsername(),String.valueOf(evainstan.getAssessnumber()));
				}
			}
			log.info("===============结束同步证照目录代码===============");
		} catch (Exception e) {
			log.info("===============推送失败，服务异常===============");
			e.printStackTrace();
		}
	}

	 
    private void turnEvaluate(String taskType,String projectno,String userName,String serviceNumber) {
    	log.info("开始重推评价数据,flowsn:"+projectno);
		
		JSONObject json = new JSONObject();

		json.put("projectNo", projectno);
		
		json.put("taskType", taskType);

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
        

        log.info("评价时间："+newassessTime);
        
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

		String resultOnline = "";
		try {
			resultOnline = HttpUtil.doPostJson(HCPOFFLINETEMPURL, submitString.toString());
			//log.info("重推评价数据返回结果如下：" + resultOnline);
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject result = new JSONObject();
		if (StringUtil.isNotBlank(resultOnline)) {
			result = JSONObject.parseObject(resultOnline);
			String code = result.getString("C-Response-Desc");
			JSONObject body = result.getJSONObject("C-Response-Body");
			if ("success".equals(code)) {
				String status = body.getString("success");
				if ("false".equals(status)) {
					String message = body.getString("message");
					Record rec = new Record();
					rec.set("sbsign", "98");
					rec.set("sberrordesc", message);
					rec.set("projectno", projectno);
					rec.set("assessNumber", serviceNumber);
					iHcpService.updateEvaRecord(rec);
					log.info("评价数据重推成功！");
				}else {
					Record rec = new Record();
					rec.set("sbsign", "1");
					rec.set("sberrordesc", code);
					rec.set("projectno", projectno);
					rec.set("assessNumber", serviceNumber);
					iHcpService.updateEvaRecord(rec);
					log.info("评价数据重推成功！");
				}
			   
			}else {
			   Record rec = new Record();
               rec.set("sbsign", "99");
               rec.set("sberrordesc", code);
               rec.set("projectno", projectno);
               rec.set("assessNumber", serviceNumber);
               iHcpService.updateEvaRecord(rec);
				log.info("评价数据推送失败！");
			}
		}
	}
}
