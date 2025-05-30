
package com.epoint.xmz.shzzpt;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditonlineuser.auditonlineproject.inter.IAuditOnlineProject;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import com.epoint.xmz.api.IJnService;
import com.epoint.xmz.wjw.api.ICxBusService;

@RestController
@RequestMapping("/jnShzzpt")
public class JnShzzptProjectController {
	/**
	 * 日志
	 */
	private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	
	public static final String shzzpt_url= ConfigUtil.getConfigValue("epointframe", "shzzpturl");
	
	@Autowired
	private IJnService iJnService;
	
	@Autowired
	private ICxBusService iCxBusService;

	@Autowired
	private IAuditProject iAuditProject;

	@Autowired
	private IAuditOnlineProject iAuditOnlineProject;
	
	private Map<String,String> tokenmsg = new HashMap<String,String>();


	@RequestMapping(value = "/getProjectProcess", method = RequestMethod.POST)
	public String getProjectProcess(@RequestBody String params) {
		try {
			log.info("=======开始调用getProjectProcess接口=======");
			 // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
            String token = jsonObject.getString("token");
            if (ZwdtConstant.SysValidateData.equals(token)) {
                JSONObject obj = (JSONObject) jsonObject.get("params");
                String projectguid = obj.getString("projectguid");
                String ActivityName = obj.getString("activityname");
                String TransactorName = obj.getString("transactorname");
                String createdate = obj.getString("createdate");
                String StartDate = obj.getString("startdate");
                String EndDate = obj.getString("enddate");
                String opinion = obj.getString("opinion");
                
                AuditProject project = iAuditProject.getAuditProjectByRowGuid(projectguid, "370800").getResult();
                
                if (project == null) {
                	return JsonUtils.zwdtRestReturn("0", "办件未找到", "");
                }
                
                //办件状态 1：办件受理 2：办件审核 3：办件办结
                String status = obj.getString("status");
                
                WorkflowWorkItem workflow = new WorkflowWorkItem();
                
                
                workflow.setActivityName(ActivityName);
                workflow.setWorkItemGuid(UUID.randomUUID().toString());
                workflow.setProcessVersionInstanceGuid(project.getPviguid());
                workflow.setTransactorName(TransactorName);
                workflow.set("createdate", createdate);
                workflow.set("StartDate", StartDate);
                workflow.set("EndDate", EndDate);
                workflow.setOpinion(opinion);
                workflow.setOperatorForDisplayName(TransactorName);
                workflow.setOperationDate(new Date());
                workflow.set("projectguid", project.getRowguid());
                workflow.setOperatorName(opinion);
                
                int insertresult = iJnService.insertbyrecord(workflow);
                
                if("3".equals(status)) {
                	project.setStatus(90);
                	project.setBanjieresult(40);
                	project.set("Banjiedate", EndDate);
                	project.setBanjieusername(TransactorName);
                	iAuditProject.updateProject(project);
                	
                	 Map<String, String> updateDateFieldMap = new HashMap<String, String>();
                	 Map<String, String> updateFieldMap = new HashMap<>();
                     updateFieldMap.put("status=", "90");
                     updateFieldMap.put("Banjieresult=", "40");
                     updateFieldMap.put("banjiedate=", EndDate);
                	 SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
                     sqlConditionUtil.eq("sourceguid", projectguid);
                     iAuditOnlineProject.updateOnlineProject(updateFieldMap, updateDateFieldMap,
                             sqlConditionUtil.getMap());
                	
                }
                
                
                log.info("插入到workflow状态成功，insertresult:"+insertresult);
                if (insertresult == 1) {
                	 return JsonUtils.zwdtRestReturn("1", "办件信息同步成功！", "");
                }
                else {
                	 return JsonUtils.zwdtRestReturn("0", "办件信息同步失败！", "");
                }
                
                
            }else {
                return JsonUtils.zwdtRestReturn("0", "身份验证失败！", "");
            }
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======getProjectProcess接口参数：params【" + params + "】=======");
			log.info("=======getProjectProcess异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "获取办件信息失败：" + e.getMessage(), "");
		}
	}
	
	
	/**
	 * 添加 经营性道路货物运输驾驶员从业资格许可 申请信息
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 */
	@RequestMapping(value = "/submitProject", method = RequestMethod.POST)
	public String submitProject(@RequestBody String params) {
		try {
			log.info("=======开始调用submitProject接口=======");
			 // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
                String projectguid = jsonObject.getString("projectguid");
                
                AuditProject project = iAuditProject.getAuditProjectByRowGuid(projectguid, "370800").getResult();
                if (project == null) {
                	return JsonUtils.zwdtRestReturn("0", "办件未找到", "");
                }
                String projectname = project.getProjectname();
                String SORG_TYPE = "";
                String url= "";
                if (projectname.contains("社会团体住所变更登记")) {
                	SORG_TYPE = "000";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("社会团体名称核准")) {
                	SORG_TYPE = "001";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("社会团体成立登记")) {
                	SORG_TYPE = "002";
                	url = shzzpt_url;
                }
                else if (projectname.contains("社会团体名称变更登记")) {
                	SORG_TYPE = "003_0";
                	url = shzzpt_url;
                }
                else if (projectname.contains("社会团体住所变更登记")) {
                	SORG_TYPE = "003_1";
                	url = shzzpt_url;
                }
                else if (projectname.contains(" 社会团体法定代表人变更登记")) {
                	SORG_TYPE = "003_2";
                	url = shzzpt_url;
                }
                else if (projectname.contains(" 社会团体业务范围变更登记")) {
                	SORG_TYPE = "003_3";
                	url = shzzpt_url;
                }
                else if (projectname.contains("社会团体注册资金变更登记")) {
                	SORG_TYPE = "003_4";
                	url = shzzpt_url;
                }
                else if (projectname.contains("社会团体业务主管单位变更登记")) {
                	SORG_TYPE = "003_5";
                	url = shzzpt_url;
                }
                else if (projectname.contains("社会团体名称核准")) {
                	SORG_TYPE = "003_9";
                	url = shzzpt_url;
                }
                else if (projectname.contains("社会团体注销登记")) {
                	SORG_TYPE = "004";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("社会团体章程核准")) {
                	SORG_TYPE = "005";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("社会团体名称核准")) {
                	SORG_TYPE = "100";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("民办非企业单位成立登记")) {
                	SORG_TYPE = "101";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("民办非企业单位名称变更")) {
                	SORG_TYPE = "102_0";
                	url = shzzpt_url;
                }
                else if (projectname.contains("民办非企业单位住所变更")) {
                	SORG_TYPE = "102_1";
                	url = shzzpt_url;
                }
                else if (projectname.contains("民办非企业单位法定代表人变更")) {
                	SORG_TYPE = "102_2";
                	url = shzzpt_url;
                }
                else if (projectname.contains("民办非企业单位业务范围变更")) {
                	SORG_TYPE = "102_3";
                	url = shzzpt_url;
                }
                else if (projectname.contains("民办非企业单位开办资金变更")) {
                	SORG_TYPE = "102_4";
                	url = shzzpt_url;
                }
                else if (projectname.contains("民办非企业单位业务主管单位变更")) {
                	SORG_TYPE = "102_5";
                	url = shzzpt_url;
                }
                else if (projectname.contains("社会团体名称核准")) {
                	SORG_TYPE = "102_9";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("民办非企业单位注销登记")) {
                	SORG_TYPE = "103";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("民办非企业单位章程核准")) {
                	SORG_TYPE = "104";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("社会团体名称核准")) {
                	SORG_TYPE = "200";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("社会团体名称核准")) {
                	SORG_TYPE = "201";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("社会团体名称核准")) {
                	SORG_TYPE = "202";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("社会团体名称核准")) {
                	SORG_TYPE = "203_0";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("社会团体名称核准")) {
                	SORG_TYPE = "203_1";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("社会团体名称核准")) {
                	SORG_TYPE = "203_2";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("社会团体名称核准")) {
                	SORG_TYPE = "203_3";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("社会团体名称核准")) {
                	SORG_TYPE = "203_4";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("社会团体名称核准")) {
                	SORG_TYPE = "203_5";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("社会团体名称核准")) {
                	SORG_TYPE = "203_6";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("社会团体名称核准")) {
                	SORG_TYPE = "203_7";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("社会团体名称核准")) {
                	SORG_TYPE = "203_9";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("社会团体名称核准")) {
                	SORG_TYPE = "204";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("社会团体名称核准")) {
                	SORG_TYPE = "205";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                
               
                
                Record record = iCxBusService.getShttzsbgdjByRowguid(projectguid);
                
                if (record == null) {
                	return JsonUtils.zwdtRestReturn("0", "表单信息未找到", "");
                }
                
                String CN_NAME = record.getStr("shehzzmc");
                String CREDIT_CODE = record.getStr("tongyshxydm");
                String LEGAL_PEOPLE  = record.getStr("legal_people");
                
                String SORG_ID = getStringRandom(32).toLowerCase();
                String TASK_CODE = getStringRandom(32).toLowerCase();
                
                String CHANGE_ITEM = "1";
                String CHANGE_BEFORE = record.getStr("zhusbgq");
                String CHANGE_AFTER = record.getStr("zhusbgw");
                String CHANGE_DESC = record.getStr("biangly");
                String CHANGE_REASON = record.getStr("biangly");
                String PROCESS = record.getStr("neiblxcx");
                
                String CREATE_PEOPLE = "3";
                String CREATE_TIME = EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT);
                String APP_PEOPLE = record.getStr("shenqr2");
                String APP_PHONE = record.getStr("sqrdh");
                String APP_DATE = EpointDateUtil.convertDate2String(record.getDate("sqrq"), EpointDateUtil.DATE_TIME_FORMAT);
                String ACCOUNTING_FIRMS = "无";
                if(StringUtil.isBlank(CN_NAME) || StringUtil.isBlank(CREDIT_CODE) || StringUtil.isBlank(LEGAL_PEOPLE) || StringUtil.isBlank(SORG_ID) || StringUtil.isBlank(TASK_CODE) ||
                		StringUtil.isBlank(CHANGE_ITEM) || StringUtil.isBlank(CHANGE_BEFORE) || StringUtil.isBlank(CHANGE_AFTER) || StringUtil.isBlank(CHANGE_DESC) || StringUtil.isBlank(CHANGE_REASON) || 
                		StringUtil.isBlank(PROCESS) || StringUtil.isBlank(CREATE_PEOPLE) || StringUtil.isBlank(CREATE_TIME) || StringUtil.isBlank(APP_PEOPLE) || StringUtil.isBlank(APP_PHONE) 
                		|| StringUtil.isBlank(APP_DATE)|| StringUtil.isBlank(ACCOUNTING_FIRMS) || StringUtil.isBlank(SORG_TYPE)) {
                	 return JsonUtils.zwdtRestReturn("0", "字段信息缺失!", "");
                }
                
                
                JSONObject json = new JSONObject();
                json.put("CN_NAME", CN_NAME);
                json.put("CREDIT_CODE",CREDIT_CODE );
                json.put("LEGAL_PEOPLE", LEGAL_PEOPLE);
                json.put("ID", projectguid);
                json.put("SORG_ID", SORG_ID);
                json.put("TASK_CODE", TASK_CODE);
                json.put("CHANGE_ITEM", CHANGE_ITEM);
                json.put("CHANGE_BEFORE", CHANGE_BEFORE);
                json.put("CHANGE_AFTER", CHANGE_AFTER);
                json.put("CHANGE_DESC", CHANGE_DESC);
                json.put("CHANGE_REASON", CHANGE_REASON);
                json.put("PROCESS", PROCESS);
                json.put("CREATE_PEOPLE", CREATE_PEOPLE);
                json.put("CREATE_TIME", CREATE_TIME);
                json.put("APP_PEOPLE", APP_PEOPLE);
                json.put("APP_PHONE", APP_PHONE);
                json.put("APP_DATE", APP_DATE);
                json.put("ACCOUNTING_FIRMS", ACCOUNTING_FIRMS);
                json.put("APPLY_TYPE", SORG_TYPE);
                
                log.info("社会组织成品入参："+json.toJSONString());
                String result = HttpUtil.doPostJson(url, json.toJSONString());
                
                if (result == null) {
                	return JsonUtils.zwdtRestReturn("0", "办件信息提交失败！", "");
                }
                
                JSONObject datajson = JSON.parseObject(result);
                
                if ("2".equals(datajson.getString("code"))) {
                	 return JsonUtils.zwdtRestReturn("1", "办件信息提交成功！", "");
                }
                else {
                	 return JsonUtils.zwdtRestReturn("0", "办件信息提交失败！", "");
                }
                
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======submitProject接口参数：params【" + params + "】=======");
			log.info("=======submitProject异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "获取办件信息失败：" + e.getMessage(), "");
		}
	}
	
	
	/**
	 * 添加 经营性道路货物运输驾驶员从业资格许可 申请信息
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 */
	@RequestMapping(value = "/submitProjectShttdj", method = RequestMethod.POST)
	public String submitProjectShttdj(@RequestBody String params) {
		try {
			log.info("=======开始调用submitProjectShttdj接口=======");
			 // 1、接口的入参转化为JSON对象
            JSONObject jsonObject = JSONObject.parseObject(params);
                String projectguid = jsonObject.getString("projectguid");
                String id = jsonObject.getString("id");
                
                AuditProject project = iAuditProject.getAuditProjectByRowGuid(projectguid, "370800").getResult();
                if (project == null) {
                	return JsonUtils.zwdtRestReturn("0", "办件未找到", "");
                }
                String projectname = project.getProjectname();
                String SORG_TYPE = "";
                String url= "";
                String tablename = "";
                if (projectname.contains("社会团体住所变更登记")) {
                	SORG_TYPE = "000";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("社会团体名称核准")) {
                	SORG_TYPE = "001";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("社会团体成立登记")) {
                	SORG_TYPE = "002";
                	url = shzzpt_url;
                }
                else if (projectname.contains("社会团体名称变更登记")) {
                	SORG_TYPE = "003_0";
                	url = shzzpt_url;
                	tablename = "shttmcbg";
                }
                else if (projectname.contains("社会团体住所变更登记")) {
                	SORG_TYPE = "003_1";
                	url = shzzpt_url;
                }
                else if (projectname.contains("社会团体法定代表人变更登记")) {
                	SORG_TYPE = "003_2";
                	url = shzzpt_url;
                }
                else if (projectname.contains("社会团体业务范围变更登记")) {
                	SORG_TYPE = "003_3";
                	url = shzzpt_url;
                }
                else if (projectname.contains("社会团体注册资金变更登记")) {
                	SORG_TYPE = "003_4";
                	url = shzzpt_url;
                }
                else if (projectname.contains("社会团体业务主管单位变更登记")) {
                	SORG_TYPE = "003_5";
                	url = shzzpt_url;
                }
                else if (projectname.contains("社会团体名称核准")) {
                	SORG_TYPE = "003_9";
                	url = shzzpt_url;
                }
                else if (projectname.contains("社会团体注销登记")) {
                	SORG_TYPE = "004";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("社会团体章程核准")) {
                	SORG_TYPE = "005";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("社会团体名称核准")) {
                	SORG_TYPE = "100";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("民办非企业单位成立登记")) {
                	SORG_TYPE = "101";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("民办非企业单位名称变更")) {
                	SORG_TYPE = "102_0";
                	url = shzzpt_url;
                }
                else if (projectname.contains("民办非企业单位住所变更")) {
                	SORG_TYPE = "102_1";
                	url = shzzpt_url;
                }
                else if (projectname.contains("民办非企业单位法定代表人变更")) {
                	SORG_TYPE = "102_2";
                	url = shzzpt_url;
                }
                else if (projectname.contains("民办非企业单位业务范围变更")) {
                	SORG_TYPE = "102_3";
                	url = shzzpt_url;
                }
                else if (projectname.contains("民办非企业单位开办资金变更")) {
                	SORG_TYPE = "102_4";
                	url = shzzpt_url;
                }
                else if (projectname.contains("民办非企业单位业务主管单位变更")) {
                	SORG_TYPE = "102_5";
                	url = shzzpt_url;
                }
                else if (projectname.contains("社会团体名称核准")) {
                	SORG_TYPE = "102_9";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("民办非企业单位注销登记")) {
                	SORG_TYPE = "103";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("民办非企业单位章程核准")) {
                	SORG_TYPE = "104";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("社会团体名称核准")) {
                	SORG_TYPE = "200";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("社会团体名称核准")) {
                	SORG_TYPE = "201";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("社会团体名称核准")) {
                	SORG_TYPE = "202";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("社会团体名称核准")) {
                	SORG_TYPE = "203_0";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("社会团体名称核准")) {
                	SORG_TYPE = "203_1";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("社会团体名称核准")) {
                	SORG_TYPE = "203_2";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("社会团体名称核准")) {
                	SORG_TYPE = "203_3";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("社会团体名称核准")) {
                	SORG_TYPE = "203_4";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("社会团体名称核准")) {
                	SORG_TYPE = "203_5";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("社会团体名称核准")) {
                	SORG_TYPE = "203_6";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("社会团体名称核准")) {
                	SORG_TYPE = "203_7";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("社会团体名称核准")) {
                	SORG_TYPE = "203_9";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("社会团体名称核准")) {
                	SORG_TYPE = "204";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                else if (projectname.contains("社会团体名称核准")) {
                	SORG_TYPE = "205";
                	url = "http://60.208.61.158:8088/socialorg/Jining/cancelRegistration.action";
                }
                
                Record record = iCxBusService.getPorjectByRowguid(tablename,projectguid);
                
                if (record == null) {
                	return JsonUtils.zwdtRestReturn("0", "表单信息未找到", "");
                }
                
                String CN_NAME = record.getStr("CN_NAME");
                String CREDIT_CODE = record.getStr("CREDIT_CODE");
                String LEGAL_PEOPLE  = record.getStr("LEGAL_PEOPLE");
                
                String SORG_ID = getStringRandom(32).toLowerCase();
                String TASK_CODE = getStringRandom(32).toLowerCase();
                
                String CHANGE_ITEM = "1";
                String CHANGE_BEFORE = record.getStr("mcbgq");
                String CHANGE_AFTER = record.getStr("mcbgw");
                String CHANGE_DESC = record.getStr("CHANGE_REASON");
                String CHANGE_REASON = record.getStr("CHANGE_REASON");
                String PROCESS = record.getStr("PROCESS");
                
                String CREATE_PEOPLE = "3";
                String CREATE_TIME = EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_FORMAT);
                String APP_PEOPLE = record.getStr("APP_PEOPLE");
                String APP_PHONE = record.getStr("APP_PHONE");
                String APP_DATE = EpointDateUtil.convertDate2String(record.getDate("APP_DATE"), EpointDateUtil.DATE_FORMAT);
                String ACCOUNTING_FIRMS = "无";
                if(StringUtil.isBlank(CN_NAME) || StringUtil.isBlank(CREDIT_CODE) || StringUtil.isBlank(LEGAL_PEOPLE) || StringUtil.isBlank(SORG_ID) || StringUtil.isBlank(TASK_CODE) ||
                		StringUtil.isBlank(CHANGE_ITEM) || StringUtil.isBlank(CHANGE_BEFORE) || StringUtil.isBlank(CHANGE_AFTER) || StringUtil.isBlank(CHANGE_DESC) || StringUtil.isBlank(CHANGE_REASON) || 
                		StringUtil.isBlank(PROCESS) || StringUtil.isBlank(CREATE_PEOPLE) || StringUtil.isBlank(CREATE_TIME) || StringUtil.isBlank(APP_PEOPLE) || StringUtil.isBlank(APP_PHONE) 
                		|| StringUtil.isBlank(APP_DATE)|| StringUtil.isBlank(ACCOUNTING_FIRMS) || StringUtil.isBlank(SORG_TYPE)) {
                	 return JsonUtils.zwdtRestReturn("0", "字段信息缺失!", "");
                }
                
                
                JSONObject json = new JSONObject();
                json.put("CN_NAME", CN_NAME);
                json.put("CREDIT_CODE",CREDIT_CODE );
                json.put("LEGAL_PEOPLE", LEGAL_PEOPLE);
                json.put("ID", id);
                json.put("SORG_ID", SORG_ID);
                json.put("TASK_CODE", TASK_CODE);
                json.put("CHANGE_ITEM", CHANGE_ITEM);
                json.put("CHANGE_BEFORE", CHANGE_BEFORE);
                json.put("CHANGE_AFTER", CHANGE_AFTER);
                json.put("CHANGE_DESC", CHANGE_DESC);
                json.put("CHANGE_REASON", CHANGE_REASON);
                json.put("PROCESS", PROCESS);
                json.put("CREATE_PEOPLE", CREATE_PEOPLE);
                json.put("CREATE_TIME", CREATE_TIME);
                json.put("APP_PEOPLE", APP_PEOPLE);
                json.put("APP_PHONE", APP_PHONE);
                json.put("APP_DATE", APP_DATE);
                json.put("ACCOUNTING_FIRMS", ACCOUNTING_FIRMS);
                json.put("APPLY_TYPE", SORG_TYPE);
                
                log.info("submitProjectShttdj入参："+json.toJSONString());
                String result = HttpUtil.doPostJson(url, json.toJSONString());
                
                if (result == null) {
                	return JsonUtils.zwdtRestReturn("0", "办件信息提交失败！", "");
                }
                
                JSONObject datajson = JSON.parseObject(result);
                
                if ("2".equals(datajson.getString("code"))) {
                	 return JsonUtils.zwdtRestReturn("1", "办件信息提交成功！", "");
                }
                else {
                	 return JsonUtils.zwdtRestReturn("0", "办件信息提交失败！", "");
                }
                
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======submitProject接口参数：params【" + params + "】=======");
			log.info("=======submitProject异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "获取办件信息失败：" + e.getMessage(), "");
		}
	}
	
	
	//生成随机数字和字母,
    public static String getStringRandom(int length) {
        
        String val = "";
        Random random = new Random();
        
        //参数length，表示生成几位随机数
        for(int i = 0; i < length; i++) {
            
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

	
		
}
