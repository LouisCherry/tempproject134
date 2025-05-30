package com.epoint.hcp.rest.controller;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.hcp.api.IHcpService;
import com.epoint.zwdt.util.TARequestUtil;

@RestController
@RequestMapping("/hcponetwoget")
public class HcpOneTwoGetController
{

    private static Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
    
    @Autowired
    private IHcpService iHcpService;
    
    private static String HCPCODEURL = ConfigUtil.getConfigValue("hcp", "HcpCodeUrl");
    private static String HCPARRAYCODEURL = ConfigUtil.getConfigValue("hcp", "HcpCodeArrayUrl");
    private static String HCPOFFLINETEMPURL = ConfigUtil.getConfigValue("hcp", "HcpOfflineTempUrl");
    private static String HCPAPPMARK = ConfigUtil.getConfigValue("hcp", "HcpAppMark");
    private static String HCPAPPWORD = ConfigUtil.getConfigValue("hcp", "HcpAppWord");
    
    //获取国家指标接口
    @RequestMapping(value = "/hcpevaluate", method = RequestMethod.POST)
    public String hcpEvaluate(@RequestBody String params) {
        try {
        	
        	 JSONObject jsonObject = JSONObject.parseObject(params);
             
             String projectno = jsonObject.getString("flowsn");
             String content = jsonObject.getString("content");
             String result1 = jsonObject.getString("result");
             String assessNumber = jsonObject.getString("eventCode");
             
             Record record = iHcpService.getServiceByProjectno(projectno, Integer.parseInt(assessNumber),null);
             log.info("办件服务信息：" + record);
             if(record == null){
                return JsonUtils.zwdtRestReturn("0", "没有对应的服务数据", "");
             }
             
    		String userName = record.getStr("userName");
    		String serviceNumber = record.getStr("serviceNumber");
    		JSONObject json = new JSONObject();

    		json.put("projectNo", projectno);

    		json.put("satisfaction", result1);

    		json.put("pf", "6");

    		json.put("name", userName);

    		json.put("evalDetail", "");

    		json.put("writingEvaluation", content);

    		json.put("assessTime", EpointDateUtil.convertDate2String(new Date(), "yyyy-MM-dd HH:mm:ss"));

    		json.put("serviceNumber", serviceNumber);

    		JSONObject jsonObjectOnline = new JSONObject();
    		JSONObject jsonObject1 = new JSONObject();
    		List<JSONObject> list = new ArrayList<>();
    		List<String> list1 = new ArrayList<>();
    		list.add(json);
    		log.info("线下新增评价加密前入参：" + list.toString());
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

    		log.info("办件数据加密后入参：" + resultsign);
    		JSONObject contentOnlineMap = new JSONObject();
    		String time = EpointDateUtil.convertDate2String(new Date(), "yyyyMMddHHmmss");
    		log.info("办件数据加密前时间：" + time);

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
    		log.info("办件数据所有入参：" + contentOnlineMap.toString());
    		log.info("办件数据url：" + HCPOFFLINETEMPURL);

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
    				r.set("Evalevel", result1);
    				r.set("Evacontant", "");
    				r.set("evalDetail", "");
    				r.set("writingEvaluation", content);
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
    				r.set("pf", "6");
    				r.set("satisfaction", result1);
    				r.set("assessTime", EpointDateUtil.convertDate2String(new Date(), ""));
    				r.set("assessNumber", Integer.valueOf(serviceNumber));
    				r.set("sbsign", "1");
    				r.set("sberrordesc", "同步成功");
    				iHcpService.addEvaluate(r);
    				dataJson.put("success", "success");
    				log.info("新增服务评价数据成功");
    				record.set("sbsign", "2");
    				record.set("sberrordesc", "评价数据重新推送成功");
    				iHcpService.updateProService(record,null);
    				return JsonUtils.zwdtRestReturn("1", "办件评价成功", "");
    			}
    			else if (code.contains("评价数据不可重复")) {
    				record.set("sbsign", "2");
    				record.set("sberrordesc", "办件已经被评价");
    				iHcpService.updateProService(record,null);
    				return JsonUtils.zwdtRestReturn("1", "办件已经被评价！", "");
    			}
    			else
    			{
    				record.set("sbsign", "4");
    				record.set("sberrordesc", "评价数据推送失败了");
    				iHcpService.updateProService(record,null);
    				return JsonUtils.zwdtRestReturn("0", "评价数据推送失败了，接口调用失败", "");
    			}
    		} else {
    			log.info("新增服务评价数据失败,result:" + resultOnline);
    			record.set("sbsign", "3");
    			record.set("sberrordesc", "评价数据重新推送失败");
    			iHcpService.updateProService(record,null);
    			return JsonUtils.zwdtRestReturn("0", "评价数据重新推送失败，接口调用失败", "");
    		}
    	
        }
        catch (Exception e) {
            log.debug("=======getnorm接口参数：params【" + params + "】=======");
            log.debug("=======getnorm异常信息：" + e.getMessage() + "=======");
            return JsonUtils.zwdtRestReturn("0", "修改用户默认地址失败" + e.getMessage(), "");
        }
    }
    
   
}
