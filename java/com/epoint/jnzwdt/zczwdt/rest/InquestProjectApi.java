package com.epoint.jnzwdt.zczwdt.rest;

import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import com.epoint.auditprojectunusual.api.IJNAuditProjectUnusual;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditworkingday.inter.IAuditOrgaWorkingDay;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.authenticator.module.OAuthCheckTokenInfo;
import com.epoint.authenticator.utils.CheckTokenUtil;
import com.epoint.basic.auditonlineuser.auditonlineindividual.domain.AuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineindividual.inter.IAuditOnlineIndividual;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditorga.auditwindow.domain.AuditOrgaWindowUser;
import com.epoint.basic.auditorga.auditwindow.inter.IAuditOrgaWindow;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectoperation.domain.AuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectoperation.inter.IAuditProjectOperation;
import com.epoint.basic.auditproject.auditprojectsparetime.domain.AuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectsparetime.inter.IAuditProjectSparetime;
import com.epoint.basic.auditproject.auditprojectunusual.domain.AuditProjectUnusual;
import com.epoint.basic.auditproject.auditprojectunusual.inter.IAuditProjectUnusual;
import com.epoint.basic.auditresource.company.domain.AuditRsCompanyBaseinfo;
import com.epoint.basic.auditresource.company.inter.IAuditRsCompanyBaseinfo;
import com.epoint.basic.audittask.basic.domain.AuditTask;
import com.epoint.basic.audittask.basic.inter.IAuditTask;
import com.epoint.basic.audittask.extension.domain.AuditTaskExtension;
import com.epoint.basic.audittask.extension.inter.IAuditTaskExtension;
import com.epoint.basic.audittask.material.domain.AuditTaskMaterial;
import com.epoint.basic.audittask.material.inter.IAuditTaskMaterial;
import com.epoint.common.service.AuditCommonResult;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwdtConstant;
import com.epoint.common.util.ZwfwConstant;
import com.epoint.common.zwdt.login.dhrsautil.AESUtil;
import com.epoint.core.EpointFrameDsManager;
import com.epoint.core.dao.CommonDao;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.container.ContainerFactory;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.database.peisistence.crud.impl.model.PageData;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.message.api.IMessagesCenterService;
import com.epoint.frame.service.message.api.IOnlineMessageInfoService;
import com.epoint.frame.service.message.entity.MessagesCenter;
import com.epoint.frame.service.message.remind.entity.MessagesMessage;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.jnzwdt.zczwdt.api.IZcCommonService;
import com.epoint.jnzwdt.zczwdt.api.entity.AuditTaskInquestsituation;
import com.epoint.workflow.service.common.custom.ProcessVersionInstance;
import com.epoint.workflow.service.common.entity.execute.WorkflowWorkItem;
import com.epoint.workflow.service.common.runtime.WorkflowParameter9;
import com.epoint.workflow.service.common.util.WorkflowKeyNames9;
import com.epoint.workflow.service.core.api.IWFEngineAPI9;
import com.epoint.workflow.service.core.api.IWFInstanceAPI9;

/**
 * 
 * 勘验事项相关API
 * 
 * @author yrchan
 * @version 2022年4月20日
 */
@RestController
@RequestMapping("/inquestProject")
public class InquestProjectApi {
	private static final Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * 办件API
	 */
	@Autowired
	private IAuditProject iAuditProject;

	/**
	 * 事项API
	 */
	@Autowired
	private IAuditTask iAuditTask;

	/**
	 * 事项扩展API
	 */
	@Autowired
	private IAuditTaskExtension iAuditTaskExtension;

	/**
	 * 事项材料API
	 */
	@Autowired
	private IAuditTaskMaterial iAuditTaskMaterial;

	/**
	 * 窗口API
	 */
	@Autowired
	private IAuditOrgaWindow iAuditOrgaWindow;

	/**
	 * 代码项API
	 */
	@Autowired
	private ICodeItemsService iCodeItemsService;

	/**
	 * 法人基本信息API
	 */
	@Autowired
	private IAuditRsCompanyBaseinfo iAuditRsCompanyBaseinfo;
	/**
	 * 办件剩余时间API
	 */
	@Autowired
	private IAuditProjectSparetime iAuditProjectSparetime;
	/**
	 * 办件特殊操作API
	 */
	@Autowired
	private IAuditProjectUnusual iAuditProjectUnusual;

	/**
	 * 办件操作API
	 */
	@Autowired
	private IAuditProjectOperation iAuditProjectOperation;

	/**
	 * 代办API
	 */
	@Autowired
	private IMessagesCenterService iMessagesCenterService;

	/**
	 * 系统消息
	 */
	@Autowired
	private IOnlineMessageInfoService iOnlineMessageInfoService;

	/**
	 * 获取流程版本实例操作API
	 */
	@Autowired
	private IWFInstanceAPI9 iWFInstanceAPI9;

	/**
	 * 邹城通用API
	 */
	@Autowired
	private IZcCommonService iZcCommonService;

	/**
	 * 附件API
	 */
	@Autowired
	private IAttachService iAttachService;
	/**
	 * 用户个人信息API
	 */
	@Autowired
	private IAuditOnlineIndividual iAuditOnlineIndividual;
	
	@Autowired
	private IAuditOrgaServiceCenter iAuditOrgaServiceCenter;
	
	@Autowired
	private IJNAuditProjectUnusual ijnAuditProjectUnusual;
	
	private String key="epoint@123!@#$";

	/**
	 * 办件初始化
	 * 
	 * @param params
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/initProject", method = RequestMethod.POST)
	public String initProject(@RequestBody String params, @Context HttpServletRequest request) {
		try {
			log.info("=======开始调用initProject接口=======" + new Date());
			AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
			if (auditOnlineRegister == null || StringUtil.isBlank(auditOnlineRegister.getAccountguid())) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "当前用户不存在或未登录！", "");
			}
			AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
					.getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
			// 1、入参转化为JSON对象
			JSONObject paramsJson = JSONObject.parseObject(params);
			// 获取参数
			JSONObject paramss = paramsJson.getJSONObject("params");
			// 用户微信唯一标识
			String openId = paramss.getString("openid");
			String applyerGuid = auditOnlineIndividual.getApplyerguid();

			// 参数校验
			/*if (StringUtil.isBlank(openId)) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "参数openid为空,请联系管理员！", "");
			}*/
			if (StringUtil.isBlank(applyerGuid)) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "参数applyerguid为空,请联系管理员！", "");
			}

			// 2、初始化办件
			AuditProject auditProject = new AuditProject();
			auditProject.setRowguid(UUID.randomUUID().toString());
			auditProject.setOperatedate(new Date());
			auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_WWINIT);
			auditProject.setApplydate(new Date());
			auditProject.setIs_delay(20);
			auditProject.setInsertdate(new Date());
			auditProject.setApplyeruserguid(applyerGuid);

			auditProject.set("inquestcliengguid", UUID.randomUUID().toString());
			auditProject.set("promisecliengguid", UUID.randomUUID().toString());
			//auditProject.set("openid", StringUtil.isNotBlank(auditOnlineRegister.getOpenid())?auditOnlineRegister.getOpenid():openId);
			auditProject.set("openid","sdapp");
			auditProject.setIs_test(ZwfwConstant.CONSTANT_INT_ZERO);
			iAuditProject.addProject(auditProject);

			// 3、返回
			JSONObject resultJson = new JSONObject();
			resultJson.put("projectguid", auditProject.getRowguid());
			resultJson.put("inquestcliengguid", auditProject.getStr("inquestcliengguid"));
			return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ONE, "办件初始化成功！", resultJson.toString());
		} catch (Exception e) {
			log.error("=======initProject接口参数：params【" + params + "】=======");
			log.error("=======initProject异常信息：" + e.getMessage() + "=======", e);
			return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "办件初始化失败：" + e.getMessage(), "");
		}
	}

	/**
	 * 办件申报
	 * 
	 * @param params
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/submitProject", method = RequestMethod.POST)
	public String submitProject(@RequestBody String params, @Context HttpServletRequest request) {
		try {
			log.info("=======开始调用submitProject接口=======" + new Date());
			AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
			if (auditOnlineRegister == null || StringUtil.isBlank(auditOnlineRegister.getAccountguid())) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "当前用户不存在或未登录！", "");
			}
			AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
					.getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
			// 1、入参转化为JSON对象
			JSONObject paramsJson = JSONObject.parseObject(params);
			// 获取参数
			JSONObject paramss = JSONObject.parseObject(AESUtil.decrypt(paramsJson.getString("params"), key));
			//JSONObject paramss = JSONObject.parseObject(paramsJson.getString("params"));
			// 办件信息
			String taskGuid = paramss.getString("taskguid");
			String projectGuid = paramss.getString("projectguid");
			String applyerType = paramss.getString("applyertype");
			String applyerName = paramss.getString("applyername");
			String applyerGuid = auditOnlineIndividual.getApplyerguid();
			String applyerMobile = paramss.getString("applyermobile");
			String idCard = paramss.getString("idcard");
			String certType = paramss.getString("certtype");
			String contactName = auditOnlineRegister.getUsername();
			String contactMobile = auditOnlineRegister.getMobile();
			String areaCode = "370883";
			String addRess = paramss.getString("address");
			String isSign = paramss.getString("is_sign");
			// 参数校验
			if (StringUtil.isBlank(projectGuid)) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "参数projectguid为空,请联系管理员！", "");
			}
			if (StringUtil.isBlank(taskGuid)) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "参数taskguid为空,请联系管理员！", "");
			}
			if (StringUtil.isBlank(applyerType)) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "参数applyertype为空,请联系管理员！", "");
			}
			if (StringUtil.isBlank(applyerName)) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "参数applyername为空,请联系管理员！", "");
			}
			if (StringUtil.isBlank(applyerGuid)) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "参数applyerguid为空,请联系管理员！", "");
			}
			if (StringUtil.isBlank(applyerMobile)) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "参数applyermobile为空,请联系管理员！", "");
			}
			if (StringUtil.isBlank(idCard)) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "参数idcard为空,请联系管理员！", "");
			}
			if (StringUtil.isBlank(certType)) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "参数certtype为空,请联系管理员！", "");
			}

			// 是否存在事项、办件校验
			AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, false).getResult();
			if (auditTask == null) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "未查询到事项,请联系管理员！", "");
			}
			AuditTaskExtension auditTaskExtension = iAuditTaskExtension.getTaskExtensionByTaskGuid(taskGuid, false)
					.getResult();
			if (auditTaskExtension == null) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "未查询到事项,请联系管理员！", "");
			}
			AuditProject auditProject = iAuditProject.getAuditProjectByRowGuid("*", projectGuid, "").getResult();
			if (auditProject == null) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "未查询到办件,请联系管理员！", "");
			}
			if (auditProject.getStatus() > ZwfwConstant.BANJIAN_STATUS_WWINIT) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "办件已提交,不可再次提交！", "");
			}
			// 中心标识 centerguid 需根据事项标识 taskid 获取，若返回多个，只获取一个
			List<Record> centerList = iAuditOrgaWindow.selectCenterGuidsByTaskId(auditTask.getTask_id()).getResult();
			if (centerList.isEmpty()) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "事项未配置到窗口，请联系管理员进行配置！", "");
			}

			// 2、更新办件信息
			auditProject.setTaskguid(taskGuid);
			// 办件状态：26：已接件，待受理
			auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_YJJ);
			auditProject.setPromise_day(auditTask.getPromise_day());
			if(auditProject.getApplydate()==null){
				auditProject.setApplydate(new Date());
			}
			auditProject.setIs_delay(20);
			auditProject.setProjectname(auditTask.getTaskname());
			auditProject.setInsertdate(new Date());
			auditProject.setIs_charge(auditTask.getCharge_flag());
			auditProject.setCharge_when(auditTaskExtension.getCharge_when());// 收费的时间，受理前还是办结前
			auditProject.setTasktype(auditTask.getType());
			Integer applyWay = auditTaskExtension.getWebapplytype();
			if (Integer.parseInt(ZwdtConstant.WEBAPPLYWAY_ZJBL) == applyWay) {
				auditProject.setApplyway(Integer.parseInt(ZwfwConstant.APPLY_WAY_NETZJSB));
			} else if (Integer.parseInt(ZwdtConstant.WEBAPPLYWAY_SBYS) == applyWay) {
				auditProject.setApplyway(Integer.parseInt(ZwfwConstant.APPLY_WAY_NETSBYS));
			}
			auditProject.setOuguid(auditTask.getOuguid());
			auditProject.setOuname(auditTask.getOuname());
			auditProject.setAreacode(areaCode);
			// 申请人证照类型
			auditProject.setCerttype(certType);
			auditProject.setCertnum(idCard);

			auditProject.setApplyertype(Integer.parseInt(applyerType));
			auditProject.setApplyeruserguid(applyerGuid);
			auditProject.setApplyername(applyerName);
			auditProject.setContactperson(contactName);
			auditProject.setContactmobile(contactMobile);
			//auditProject.setContactphone(applyerMobile);
			auditProject.setTaskid(auditTask.getTask_id());
			auditProject.setTask_id(auditTask.getTask_id());
			auditProject.setAddress(addRess);
			auditProject.set("is_sign", isSign);
			auditProject.setReceivedate(new Date());
			log.info("centerguid:"+centerList.get(0).getStr("centerGuid"));
			if("undefined".equals(centerList.get(0).getStr("centerGuid"))){
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "中心标记有问题", "");
			}
			auditProject.setCenterguid(centerList.get(0).getStr("centerGuid"));

			// 4、发送待办，根据中心标识 centerguid，确定所选事项所在窗口，获取该窗口的关联业务人员，发送【待受理】待办
			// 根据指定条件获取窗口人员
			/*SqlConditionUtil windowUserSql = new SqlConditionUtil();
			windowUserSql.setSelectFields("distinct userguid,username");
			windowUserSql.in("windowguid",
					"(select a.rowguid from audit_orga_window a,audit_orga_windowtask b where a.RowGuid=b.WINDOWGUID AND b.TASKID = '"
							+ auditTask.getTask_id() + "' and a.centerguid = '" + auditProject.getCenterguid() + "')");
			List<AuditOrgaWindowUser> windowUserList = iAuditOrgaWindow.getWindowUser(windowUserSql.getMap())
					.getResult();
			if (windowUserList.isEmpty()) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "未查询到事项所在窗口的业务人员，请联系管理员进行配置！", "");
			}
			String title = "【待受理】" + auditProject.getProjectname() + "(" + auditProject.getApplyername() + ")";
			String formUrl = StringUtil.isNotBlank(auditTaskExtension.getCustomformurl())
					? auditTaskExtension.getCustomformurl()
					: ZwfwConstant.CONSTANT_FORM_URL;
			String handleUrl = formUrl + "?processguid=" + auditTask.getProcessguid() + "&taskguid="
					+ auditProject.getTaskguid() + "&projectguid=" + auditProject.getRowguid();
			Date nowDate = new Date();
			for (AuditOrgaWindowUser auditOrgaWindowUser : windowUserList) {
				iMessagesCenterService.insertWaitHandleMessage(UUID.randomUUID().toString(), title,
						IMessagesCenterService.MESSAGETYPE_WAIT, auditOrgaWindowUser.getUserguid(),
						auditOrgaWindowUser.getUsername(), auditProject.getApplyeruserguid(),
						auditProject.getApplyername(), "待受理", handleUrl, "", "", ZwfwConstant.CONSTANT_INT_ONE, "", "",
						auditProject.getRowguid(), auditProject.getRowguid().substring(0, 1), nowDate,
						auditProject.getPviguid(), auditProject.getApplyeruserguid(), "", "");
			}*/
			iAuditProject.updateProject(auditProject);

			// 5、返回
			JSONObject resultJson = new JSONObject();
			return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ONE, "办件提交成功！", resultJson.toString());
		} catch (Exception e) {
			log.error("=======submitProject接口参数：params【" + params + "】=======");
			log.error("=======submitProject异常信息：" + e.getMessage() + "=======", e);
			return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "办件提交失败：" + e.getMessage(), "");
		}
	}

	/**
	 * 获取字典值（代码项值）接口
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/getMaincodeByCodename", method = RequestMethod.POST)
	public String getMaincodeByCodename(@RequestBody String params, @Context HttpServletRequest request) {
		try {
			log.info("=======开始调用getMaincodeByCodename接口=======");
			AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
			if (auditOnlineRegister == null || StringUtil.isBlank(auditOnlineRegister.getAccountguid())) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "当前用户不存在或未登录！", "");
			}

			// 1、入参转化为JSON对象
			JSONObject paramsJson = JSONObject.parseObject(params);
			JSONObject paramss = paramsJson.getJSONObject("params");
			// 代码项名称
			String codename = paramss.getString("codename");
			if (StringUtil.isBlank(codename)) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "必填参数codename为空！", "");
			}
			JSONArray codeList = new JSONArray();
			List<CodeItems> codeItemsList = iCodeItemsService.listCodeItemsByCodeName(codename);
			for (CodeItems codeItems : codeItemsList) {
				JSONObject object = new JSONObject();
				object.put("itemvalue", codeItems.getItemValue());
				object.put("itemtext", codeItems.getItemText());
				codeList.add(object);
			}

			JSONObject dataJson = new JSONObject();
			dataJson.put("totalcount", codeList.size());
			dataJson.put("maincodeList", codeList);
			log.info("=======结束调用getMaincodeByCodename接口=======");
			return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ONE, "获取字典值成功!", dataJson);
		} catch (Exception e) {
			log.error("=======getMaincodeByCodename接口参数：params【" + params + "】=======");
			log.error("=======getMaincodeByCodename异常信息：" + e.getMessage() + "=======", e);
			return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "获取字典值失败！" + e.getMessage(), "");
		}
	}

	/**
	 * 获取法人企业列表接口
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/getEnterpriseList", method = RequestMethod.POST)
	public String getEnterpriseList(@RequestBody String params, @Context HttpServletRequest request) {
		try {
			log.info("=======开始调用getEnterpriseList接口=======");
			AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
			if (auditOnlineRegister == null || StringUtil.isBlank(auditOnlineRegister.getAccountguid())) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "当前用户不存在或未登录！", "");
			}

			// 1、入参转化为JSON对象
			JSONObject paramsJson = JSONObject.parseObject(params);
			JSONObject paramss = paramsJson.getJSONObject("params");
			// 法人身份证
			String legalidCard = paramss.getString("legalidcard");
			// 2、校验
			if (StringUtil.isBlank(legalidCard)) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "必填参数legalidcard为空!", "");
			}
			// 根 据 法 人 身 份 证 查 询 audit_rs_company_baseinfo （ 对
			// 应orgallegal_idnumer 字段），获取该法人名下所有企业；
			// 3、 获取用户对应的法人信息
			SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
			sqlConditionUtil.eq("orgalegal_idnumber", legalidCard);
			sqlConditionUtil.isBlankOrValue("is_history", "0"); // 在用版本的企业信息
			sqlConditionUtil.eq("isactivated", ZwfwConstant.CONSTANT_STR_ONE); // 激活的企业
			List<AuditRsCompanyBaseinfo> auditRsCompanyBaseinfos = iAuditRsCompanyBaseinfo
					.selectAuditRsCompanyBaseinfoByCondition(sqlConditionUtil.getMap()).getResult();

			List<JSONObject> organList = new ArrayList<JSONObject>();
			// 4、遍历用户对应的所有法人信息
			for (AuditRsCompanyBaseinfo auditRsCompanyBaseinfo : auditRsCompanyBaseinfos) {
				// 4.1、获取各个企业的法人信息，存入返回列表中
				JSONObject organListObject = new JSONObject();
				organListObject.put("organguid", auditRsCompanyBaseinfo.getRowguid());
				organListObject.put("organname", auditRsCompanyBaseinfo.getOrganname());
				organListObject.put("organcode", auditRsCompanyBaseinfo.getOrgancode());
				organListObject.put("organlegal", auditRsCompanyBaseinfo.getOrganlegal());
				organListObject.put("orgallegal_idnumer", auditRsCompanyBaseinfo.getOrgalegal_idnumber());
				organListObject.put("creditcode", auditRsCompanyBaseinfo.getCreditcode());
				organList.add(organListObject);
			}

			// 5、返回
			JSONObject dataJson = new JSONObject();
			dataJson.put("totalcount", organList.size());
			dataJson.put("organList", organList);
			log.info("=======结束调用getEnterpriseList接口=======");
			return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ONE, "获取法人企业列表成功!", dataJson);
		} catch (Exception e) {
			log.error("=======getEnterpriseList接口参数：params【" + params + "】=======");
			log.error("=======getEnterpriseList异常信息：" + e.getMessage() + "=======", e);
			return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "获取法人企业列表失败！" + e.getMessage(), "");
		}
	}

	/**
	 * 获取我的办件列表接口
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/getMyProjectList", method = RequestMethod.POST)
	public String getMyProjectList(@RequestBody String params, @Context HttpServletRequest request) {
		try {
			log.info("=======开始调用getMyProjectList接口=======");
			AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
			if (auditOnlineRegister == null || StringUtil.isBlank(auditOnlineRegister.getAccountguid())) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "当前用户不存在或未登录！", "");
			}

			AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
					.getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
			// 1、入参转化为JSON对象
			JSONObject paramsJson = JSONObject.parseObject(params);
			JSONObject paramss = paramsJson.getJSONObject("params");
			// 当前页码
			String currentPage = paramss.getString("currentpage");
			String pageSize = paramss.getString("pagesize");
			String applyUserGuid = auditOnlineIndividual.getApplyerguid();
			String status = paramss.getString("status");

			// 2、校验
			if (StringUtil.isBlank(currentPage)) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "必填参数currentpage为空!", "");
			}
			if (StringUtil.isBlank(pageSize)) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "必填参数pagesize为空!", "");
			}
			if (StringUtil.isBlank(applyUserGuid)) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "必填参数applyuserguid为空!", "");
			}

			// 3、分页查询办件信息
			SqlConditionUtil projectSql = new SqlConditionUtil();
			projectSql.eq("applyeruserguid", applyUserGuid);
			if (StringUtil.isNotBlank(status)) {
				if (ZwfwConstant.CONSTANT_STR_ZERO.equals(status)) {
					// 0（全部）：待受理、已受理、审核通过、审核不通过、撤销申请、不予受理、异常终止、已办结;
					projectSql.ge("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_YJJ));
				} else if (ZwfwConstant.CONSTANT_STR_ONE.equals(status)) {
					// 1:待受理
					projectSql.eq("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_YJJ));
				} else if (ZwfwConstant.CONSTANT_STR_TWO.equals(status)) {
					// 2:审核中:已受理、补正
					projectSql.in("status",
							"'" + ZwfwConstant.BANJIAN_STATUS_YSL + "','" + ZwfwConstant.BANJIAN_STATUS_DBB + "'");
				} else if (ZwfwConstant.CONSTANT_STR_THREE.equals(status)) {
					// 3:审核不通过
					projectSql.eq("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_SPBTG));
				} else if ("4".equals(status)) {
					// 4:审核通过
					projectSql.eq("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_SPTG));
				} else if ("5".equals(status)) {
					// 5:不予受理
					projectSql.eq("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_BYSL));
				} else if ("6".equals(status)) {
					// 6:异常终止
					projectSql.eq("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_YCZZ));
				} else if ("7".equals(status)) {
					// 7:撤销申请
					projectSql.eq("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_CXSQ));
				} else if ("8".equals(status)) {
					// 8:正常办结
					projectSql.eq("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_ZCBJ));
				}
			} else {
				// 0（全部）：待受理、已受理、审核通过、审核不通过、撤销申请、不予受理、异常终止、已办结;
				projectSql.ge("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_YJJ));
			}

			PageData<AuditProject> auditProjectData = iAuditProject.getAuditProjectPageDataByCondition(
					"rowguid projectguid,projectname,flowsn,applydate,status,taskguid", projectSql.getMap(),
					Integer.valueOf(currentPage) * Integer.valueOf(pageSize), Integer.valueOf(pageSize), "operatedate",
					"desc", "").getResult();
			// 4、返回
			JSONObject dataJson = new JSONObject();

			if (auditProjectData != null) {
				for (AuditProject auditProject : auditProjectData.getList()) {
					Date applyDate = auditProject.getApplydate();
					if (applyDate != null) {
						auditProject.put("applydate",
								EpointDateUtil.convertDate2String(applyDate, EpointDateUtil.DATE_TIME_FORMAT));
					}
					if (auditProject.getStatus() == ZwfwConstant.BANJIAN_STATUS_YJJ) {
						auditProject.put("projectstatus", "待受理");
					} else {
						String projectStatus = iCodeItemsService.getItemTextByCodeName("办件状态",
								String.valueOf(auditProject.getStatus()));
						if (StringUtil.isNotBlank(projectStatus)) {
							auditProject.put("projectstatus", projectStatus);
						} else {
							auditProject.put("projectstatus", auditProject.getStatus());
						}
					}
				}
				dataJson.put("totalcount", auditProjectData.getRowCount());
				dataJson.put("myProjectList", auditProjectData.getList());
			} else {
				dataJson.put("totalcount", ZwfwConstant.CONSTANT_STR_ZERO);
				dataJson.put("myProjectList", new ArrayList<>());
			}

			log.info("=======结束调用getMyProjectList接口=======");
			return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ONE, "获取我的办件成功!", dataJson.toJSONString());
		} catch (Exception e) {
			log.error("=======getMyProjectList接口参数：params【" + params + "】=======");
			log.error("=======getMyProjectList异常信息：" + e.getMessage() + "=======", e);
			return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "获取我的办件失败！" + e.getMessage(), "");
		}
	}

	/**
	 * 获取我的办件统计接口
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/getMyProjectCountList", method = RequestMethod.POST)
	public String getMyProjectCountList(@RequestBody String params, @Context HttpServletRequest request) {
		try {
			log.info("=======开始调用getMyProjectCountList接口=======");
			AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
			if (auditOnlineRegister == null || StringUtil.isBlank(auditOnlineRegister.getAccountguid())) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "当前用户不存在或未登录！", "");
			}

			AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
					.getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
			// 1、入参转化为JSON对象
			JSONObject paramsJson = JSONObject.parseObject(params);
			JSONObject paramss = paramsJson.getJSONObject("params");
			String applyUserGuid = auditOnlineIndividual.getApplyerguid();
			// 3、分页查询办件信息
			SqlConditionUtil projectSql = new SqlConditionUtil();
			AuditCommonResult<Integer> count;
			JSONObject dataJson = new JSONObject();
			List<JSONObject> myProjectCountList = new ArrayList<JSONObject>();
			JSONObject countJson = new JSONObject();

			// 0（全部）：待受理、已受理、审核通过、审核不通过、撤销申请、不予受理、异常终止、已办结;
			projectSql.eq("applyeruserguid", applyUserGuid);
			projectSql.ge("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_YJJ));
			count = iAuditProject.getAuditProjectCountByCondition(projectSql.getMap());
			countJson.put("bjstatus", "0");
			countJson.put("num", count.getResult());
			myProjectCountList.add(countJson);

			// 1:待受理
			projectSql.clear();
			projectSql.eq("applyeruserguid", applyUserGuid);
			projectSql.eq("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_YJJ));
			count = iAuditProject.getAuditProjectCountByCondition(projectSql.getMap());
			countJson = new JSONObject();
			countJson.put("bjstatus", "1");
			countJson.put("num", count.getResult());
			myProjectCountList.add(countJson);

			// 2:审核中:已受理、补正
			projectSql.clear();
			projectSql.eq("applyeruserguid", applyUserGuid);
			projectSql.in("status",
					"'" + ZwfwConstant.BANJIAN_STATUS_YSL + "','" + ZwfwConstant.BANJIAN_STATUS_DBB + "'");
			count = iAuditProject.getAuditProjectCountByCondition(projectSql.getMap());
			countJson = new JSONObject();
			countJson.put("bjstatus", "2");
			countJson.put("num", count.getResult());
			myProjectCountList.add(countJson);

			// 3:审核不通过
			projectSql.clear();
			projectSql.eq("applyeruserguid", applyUserGuid);
			projectSql.eq("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_SPBTG));
			count = iAuditProject.getAuditProjectCountByCondition(projectSql.getMap());
			countJson = new JSONObject();
			countJson.put("bjstatus", "3");
			countJson.put("num", count.getResult());
			myProjectCountList.add(countJson);

			// 4:审核通过
			projectSql.clear();
			projectSql.eq("applyeruserguid", applyUserGuid);
			projectSql.eq("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_SPTG));
			count = iAuditProject.getAuditProjectCountByCondition(projectSql.getMap());
			countJson = new JSONObject();
			countJson.put("bjstatus", "4");
			countJson.put("num", count.getResult());
			myProjectCountList.add(countJson);

			// 5:不予受理
			projectSql.clear();
			projectSql.eq("applyeruserguid", applyUserGuid);
			projectSql.eq("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_BYSL));
			count = iAuditProject.getAuditProjectCountByCondition(projectSql.getMap());
			countJson = new JSONObject();
			countJson.put("bjstatus", "5");
			countJson.put("num", count.getResult());
			myProjectCountList.add(countJson);

			// 6:异常终止
			projectSql.clear();
			projectSql.eq("applyeruserguid", applyUserGuid);
			projectSql.eq("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_YCZZ));
			count = iAuditProject.getAuditProjectCountByCondition(projectSql.getMap());
			countJson = new JSONObject();
			countJson.put("bjstatus", "6");
			countJson.put("num", count.getResult());
			myProjectCountList.add(countJson);

			// 7:撤销申请
			projectSql.clear();
			projectSql.eq("applyeruserguid", applyUserGuid);
			projectSql.eq("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_CXSQ));
			count = iAuditProject.getAuditProjectCountByCondition(projectSql.getMap());
			countJson = new JSONObject();
			countJson.put("bjstatus", "7");
			countJson.put("num", count.getResult());
			myProjectCountList.add(countJson);

			// 8:正常办结
			projectSql.clear();
			projectSql.eq("applyeruserguid", applyUserGuid);
			projectSql.eq("status", String.valueOf(ZwfwConstant.BANJIAN_STATUS_ZCBJ));
			count = iAuditProject.getAuditProjectCountByCondition(projectSql.getMap());
			countJson = new JSONObject();
			countJson.put("bjstatus", "8");
			countJson.put("num", count.getResult());
			myProjectCountList.add(countJson);
			
			dataJson.put("myProjectCountList", myProjectCountList);

			log.info("=======结束调用getMyProjectCountList接口=======");
			return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ONE, "获取我的办件成功!", dataJson.toJSONString());
		} catch (Exception e) {
			log.error("=======getMyProjectCountList接口参数：params【" + params + "】=======");
			log.error("=======getMyProjectCountList异常信息：" + e.getMessage() + "=======", e);
			return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "获取我的办件失败！" + e.getMessage(), "");
		}
	}

	/**
	 * 获取办件详情接口
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/getProjectDetail", method = RequestMethod.POST)
	public String getProjectDetail(@RequestBody String params, @Context HttpServletRequest request) {
		try {
			log.info("=======开始调用getProjectDetail接口=======");
			AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
			if (auditOnlineRegister == null || StringUtil.isBlank(auditOnlineRegister.getAccountguid())) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "当前用户不存在或未登录！", "");
			}

			// 1、入参转化为JSON对象
			JSONObject paramsJson = JSONObject.parseObject(params);
			JSONObject paramss = paramsJson.getJSONObject("params");
			// 办件唯一标识
			String projectGuid = paramss.getString("projectguid");
			// String areaCode = paramss.getString("areacode");

			// 2、校验
			if (StringUtil.isBlank(projectGuid)) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "必填参数projectguid为空!", "");
			}

			AuditProject auditProject = iAuditProject.getAuditProjectByRowGuid("*", projectGuid, "").getResult();
			if (auditProject == null) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "未查询到办件，请联系管理员!", "");
			}
			// 3、返回
			JSONObject dataJson = new JSONObject();
			dataJson.put("flowsn", auditProject.getFlowsn());
			dataJson.put("applyertype", auditProject.getApplyertype());
			String applyerTypeText = iCodeItemsService.getItemTextByCodeName("申请人类型",
					String.valueOf(auditProject.getApplyertype()));
			if (StringUtil.isNotBlank(applyerTypeText)) {
				dataJson.put("applyertypetext", applyerTypeText);
			} else {
				dataJson.put("applyertypetext", auditProject.getApplyertype());
			}

			// 不予受理、撤销申请、异常终止，意见和附件
			if (auditProject.getStatus() == ZwfwConstant.BANJIAN_STATUS_BYSL) {
				// 不予受理
				AuditProjectUnusual auditProjectUnusual = iAuditProjectUnusual.getProjectUnusualByProjectGuidAndType(
						auditProject.getRowguid(), ZwfwConstant.BANJIANOPERATE_TYPE_BYSL).getResult();
				if (auditProjectUnusual != null) {
					dataJson.put("reasondate", EpointDateUtil.convertDate2String(auditProjectUnusual.getOperatedate(), EpointDateUtil.DATE_TIME_FORMAT));
					dataJson.put("reason", auditProjectUnusual.getNote());
					if (StringUtil.isNotBlank(auditProjectUnusual.getStr("cliengguid"))) {
						dataJson.put("reasoncliengguid", auditProjectUnusual.getStr("cliengguid"));
					} else {
						dataJson.put("reasoncliengguid", "");
					}
				}
			} else if (auditProject.getStatus() == ZwfwConstant.BANJIAN_STATUS_CXSQ) {
				// 撤销申请
				AuditProjectUnusual auditProjectUnusual = iAuditProjectUnusual.getProjectUnusualByProjectGuidAndType(
						auditProject.getRowguid(), ZwfwConstant.BANJIANOPERATE_TYPE_CX).getResult();
				if (auditProjectUnusual != null) {
					dataJson.put("reasondate", EpointDateUtil.convertDate2String(auditProjectUnusual.getOperatedate(), EpointDateUtil.DATE_TIME_FORMAT));
					dataJson.put("reason", auditProjectUnusual.getNote());
					if (StringUtil.isNotBlank(auditProjectUnusual.getStr("cliengguid"))) {
						dataJson.put("reasoncliengguid", auditProjectUnusual.getStr("cliengguid"));
					} else {
						dataJson.put("reasoncliengguid", "");
					}
				}
			} else if (auditProject.getStatus() == ZwfwConstant.BANJIAN_STATUS_YCZZ) {
				// 异常终止
				AuditProjectUnusual auditProjectUnusual = iAuditProjectUnusual.getProjectUnusualByProjectGuidAndType(
						auditProject.getRowguid(), ZwfwConstant.SHENPIOPERATE_TYPE_ZZ).getResult();
				if (auditProjectUnusual != null) {
					dataJson.put("reasondate", EpointDateUtil.convertDate2String(auditProjectUnusual.getOperatedate(), EpointDateUtil.DATE_TIME_FORMAT));
					dataJson.put("reason", auditProjectUnusual.getNote());
					if (StringUtil.isNotBlank(auditProjectUnusual.getStr("cliengguid"))) {
						dataJson.put("reasoncliengguid", auditProjectUnusual.getStr("cliengguid"));
					} else {
						dataJson.put("reasoncliengguid", "");
					}
				}
			} else if (auditProject.getStatus() == ZwfwConstant.BANJIAN_STATUS_ZCBJ) {
				// 办结
				String strWhere = "projectguid = '" + auditProject.getRowguid() + "' and  operatetype = '"
						+ ZwfwConstant.OPERATE_BJ + "'";
				// 添加办结备注
				AuditProjectOperation auditProjectOperation = iAuditProjectOperation
						.getOperationFileldByProjectGuid(strWhere, " * ", "").getResult();
				if (auditProjectOperation != null) {
					dataJson.put("reasondate", EpointDateUtil.convertDate2String(auditProjectOperation.getOperatedate(), EpointDateUtil.DATE_TIME_FORMAT));
					dataJson.put("reason", auditProjectOperation.getRemarks());
					dataJson.put("reasoncliengguid", auditProjectOperation.getRowguid());
				}
			} else if (auditProject.getStatus() == ZwfwConstant.BANJIAN_STATUS_SPTG) {
				// 审核通过
				String strWhere = "projectguid = '" + auditProject.getRowguid() + "' and  operatetype = '"
						+ ZwfwConstant.OPERATE_SPTG + "'";
				// 
				AuditProjectOperation auditProjectOperation = iAuditProjectOperation
						.getOperationFileldByProjectGuid(strWhere, " * ", "").getResult();
				if (auditProjectOperation != null) {
					dataJson.put("reasondate", EpointDateUtil.convertDate2String(auditProjectOperation.getOperatedate(), EpointDateUtil.DATE_TIME_FORMAT));
					dataJson.put("reason", auditProjectOperation.getRemarks());
					dataJson.put("reasoncliengguid", auditProjectOperation.getRowguid());
				}
			} else if (auditProject.getStatus() == ZwfwConstant.BANJIAN_STATUS_SPBTG) {
				// 审核不通过
				/*if (StringUtil.isNotBlank(auditProject.getPviguid())) {
					// 获取流程版本实例
					ProcessVersionInstance pvi = iWFInstanceAPI9.getProcessVersionInstance(auditProject.getPviguid());
					// 根据过程版本标识和工作项的状态获取某过程版本实例中的工作项
					List<WorkflowWorkItem> lswi = iWFInstanceAPI9.getWorkItemListByPVIGuidAndStatus(pvi, null);
					if (!lswi.isEmpty()) {
						// 进行降序排列
						Collections.sort(lswi, new Comparator<WorkflowWorkItem>() {
							*//**
							 * 按照创建时间倒序排列
							 *//*
							public int compare(WorkflowWorkItem o1, WorkflowWorkItem o2) {
								return o2.getCreateDate().compareTo(o1.getCreateDate());
							}
						});

						for (WorkflowWorkItem workflowWorkItem : lswi) {
							if("审核".equals(workflowWorkItem.getActivityName())) {
								dataJson.put("reasondate", EpointDateUtil.convertDate2String(workflowWorkItem.getCreateDate(), EpointDateUtil.DATE_TIME_FORMAT));
								dataJson.put("reason", workflowWorkItem.getOpinion());
								dataJson.put("reasoncliengguid", workflowWorkItem.getActivityInstanceGuid());
							}
						}
					}
				}*/
				String strWhere = "projectguid = '" + auditProject.getRowguid() + "' and  operatetype = '"
						+ ZwfwConstant.OPERATE_SPBTG + "'";
				// 
				AuditProjectOperation auditProjectOperation = iAuditProjectOperation
						.getOperationFileldByProjectGuid(strWhere, " * ", "").getResult();
				if (auditProjectOperation != null) {
					dataJson.put("reasondate", EpointDateUtil.convertDate2String(auditProjectOperation.getOperatedate(), EpointDateUtil.DATE_TIME_FORMAT));
					dataJson.put("reason", auditProjectOperation.getRemarks());
					dataJson.put("reasoncliengguid", auditProjectOperation.getRowguid());
				}
			}

			dataJson.put("applydate",
					EpointDateUtil.convertDate2String(auditProject.getApplydate(), EpointDateUtil.DATE_TIME_FORMAT));
			dataJson.put("projectname", auditProject.getProjectname());
			dataJson.put("applyername", auditProject.getApplyername());
			dataJson.put("applyercertnum", auditProject.getCertnum());
			dataJson.put("applyercerttype", auditProject.getCerttype());

			String applyerCerttypeText = iCodeItemsService.getItemTextByCodeName("证件类型",
					String.valueOf(auditProject.getCerttype()));
			if (StringUtil.isNotBlank(applyerCerttypeText)) {
				dataJson.put("applyercerttypetext", applyerCerttypeText);
			} else {
				dataJson.put("applyercerttypetext", auditProject.getCerttype());
			}

			dataJson.put("applyermobile", auditProject.getContactphone());
			dataJson.put("contactperson", auditProject.getContactperson());
			dataJson.put("contactmobile", auditProject.getContactmobile());
			dataJson.put("address", auditProject.getAddress());
			dataJson.put("legal", auditProject.getLegal());

			dataJson.put("orgalegalidnumber", auditProject.getLegalid());
			dataJson.put("status", auditProject.getStatus());
			dataJson.put("taskguid", auditProject.getTaskguid());

			if (auditProject.getStatus() == ZwfwConstant.BANJIAN_STATUS_YJJ) {
				auditProject.put("statustext", "待受理");
			} else {
				String statusText = iCodeItemsService.getItemTextByCodeName("办件状态",
						String.valueOf(auditProject.getStatus()));
				if (StringUtil.isNotBlank(statusText)) {
					dataJson.put("statustext", statusText);
				} else {
					dataJson.put("statustext", auditProject.getStatus());
				}
			}

			// 勘验附件标识
			dataJson.put("cliengguid", auditProject.getStr("inquestcliengguid"));
			// 勘验承诺书标识
			dataJson.put("promisecliengguid", auditProject.getStr("promisecliengguid"));

			// 办件流程信息列表
			List<JSONObject> processlList = new ArrayList<JSONObject>();

			
			dataJson.put("processlList", processlList);

			log.info("=======结束调用getProjectDetail接口=======");
			dataJson.put("code", 1);
			dataJson.put("text", "获取办件成功!");
			return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ONE, "获取办件成功!", AESUtil.encrypt(dataJson.toJSONString(),key));
			//return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ONE, "获取办件成功!", dataJson.toJSONString());
		} catch (Exception e) {
			log.error("=======getProjectDetail接口参数：params【" + params + "】=======");
			log.error("=======getProjectDetail异常信息：" + e.getMessage() + "=======", e);
			return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "获取办件失败！" + e.getMessage(), "");
		}
	}

	/**
	 * 撤销申请接口
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/handleRevoke", method = RequestMethod.POST)
	public String handleRevoke(@RequestBody String params, @Context HttpServletRequest request) {
		try {
			log.info("=======开始调用handleRevoke接口=======");
			AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
			if (auditOnlineRegister == null || StringUtil.isBlank(auditOnlineRegister.getAccountguid())) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "当前用户不存在或未登录！", "");
			}

			// 1、入参转化为JSON对象
			JSONObject paramsJson = JSONObject.parseObject(params);
			JSONObject paramss = paramsJson.getJSONObject("params");
			// 办件标识
			String projectGuid = paramss.getString("projectguid");

			// 2、校验
			if (StringUtil.isBlank(projectGuid)) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "必填参数projectguid为空!", "");
			}

			AuditProject auditProject = iAuditProject.getAuditProjectByRowGuid("*", projectGuid, "").getResult();
			if (auditProject == null) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "未查询到办件，请联系管理员!", "");
			}
			// 已接件/待受理，才会有“撤销申请”按钮
			if (auditProject.getStatus() != ZwfwConstant.BANJIAN_STATUS_YJJ) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "办件状态不是“待受理”，不可“撤销申请”，请联系管理员!", "");
			}

			// 3、办件异常操作表（audit_project_unusual）新增一条记录,其中操作类型 operatetype 为 98，备注
			// note 默认为‘撤销申请’；
			iAuditProjectUnusual
					.insertProjectUnusual(auditProject.getApplyeruserguid(), auditProject.getApplyername(),
							auditProject, Integer.parseInt(ZwfwConstant.BANJIANOPERATE_TYPE_CX), "", "撤销申请")
					.getResult();
			// 4、办件操作表（audit_project_operation）新增一条记录，其中操作类型 operatetype 为 61，备注
			// remarks 默认为‘撤销申请’
			AuditProjectOperation auditProjectOperation = new AuditProjectOperation();
			auditProjectOperation.setRowguid(UUID.randomUUID().toString());
			auditProjectOperation.setOperatedate(new Date());
			auditProjectOperation.setOperateUserGuid(auditProject.getApplyeruserguid());
			auditProjectOperation.setOperateusername(auditProject.getApplyername());
			auditProjectOperation.setProjectGuid(auditProject.getRowguid());
			auditProjectOperation.setRemarks("撤销申请");
			auditProjectOperation.setPVIGuid(auditProject.getPviguid());
			auditProjectOperation.setApplyerGuid(auditProject.getApplyeruserguid());
			auditProjectOperation.setApplyerName(auditProject.getApplyername());
			auditProjectOperation.setTaskGuid(auditProject.getTaskguid());
			auditProjectOperation.setOperateType(ZwfwConstant.OPERATE_CXSQ);
			auditProjectOperation.setAreaCode(auditProject.getAreacode());
			iAuditProjectOperation.addProjectOperation(auditProjectOperation);

			// 5、更新办件表字段
			// 5.1、办结结果Banjieresult为98，
			// banjiedate为当前时间，banwandate为当前时间，办件状态status 为 98；
			auditProject.setBanjieresult(Integer.parseInt(ZwfwConstant.SHENPIOPERATE_TYPE_CX));
			auditProject.setBanjiedate(new Date());
			auditProject.setBanwandate(new Date());
			auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_CXSQ);
			if (StringUtil.isBlank(auditProject.getHandleareacode())) {
				auditProject.setHandleareacode(auditProject.getAreacode() + ",");
			}
			// 5.2、根据办件标识查询办件计时表（audit_project_sparetime），若能查询到记录，
			// 则更新办件表的 sparetime 和 spendtime，然后删除办件计时记录；
			// 更新办件状态
			int promiseday = auditProject.getPromise_day();
			int spendtime = 0;// 已用时间
			int sparetime = 0;// 剩余时间

			AuditProjectSparetime auditprojectsparetime = iAuditProjectSparetime
					.getSparetimeByProjectGuid(auditProject.getRowguid()).getResult();
			// 办件计时信息存在
			if (auditprojectsparetime != null) {
				sparetime = auditprojectsparetime.getSpareminutes();
				if (promiseday > 0) {
					if (promiseday * 24 * 60 > sparetime) {
						spendtime = promiseday * 24 * 60 - sparetime;
					} else {
						spendtime = auditprojectsparetime.getSpendminutes();
					}
				} else {
					spendtime = -1 * sparetime;
				}
				auditProject.setSparetime(sparetime);
				auditProject.setSpendtime(spendtime);
			}
			auditProject.setGuidangdate(new Date());
			auditProject.setGuidanguserguid(auditProject.getApplyeruserguid());
			auditProject.setGuidangusername(auditProject.getApplyername());
			auditProject.setIs_guidang(1);
			// 删除计时信息
			if (auditprojectsparetime != null) {
				iAuditProjectSparetime.deleteSpareTimeByRowGuid(auditprojectsparetime.getRowguid());
			}
			
			//查看有无centerguid
			if(StringUtils.isBlank(auditProject.getCenterguid())){
				AuditOrgaServiceCenter auditOrgaServiceCenter = iAuditOrgaServiceCenter.getAuditServiceCenterByBelongXiaqu(auditProject.getAreacode());
				if(auditOrgaServiceCenter!=null){
					log.info("centerguid:"+auditOrgaServiceCenter.getRowguid());
					if("undefined".equals(auditOrgaServiceCenter.getRowguid())){
						return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "中心标记有问题", "");
					}
					auditProject.setCenterguid(auditOrgaServiceCenter.getRowguid());
				}
			}
			//更新承诺办结时间
			AuditTask  auditTask = iAuditTask.getAuditTaskByGuid(auditProject.getTaskguid(),true).getResult();
			if(auditTask!=null) {
				List<AuditProjectUnusual> auditProjectUnusuals = ijnAuditProjectUnusual.getZantingData(auditProject.getRowguid());
				Date acceptdat = auditProject.getAcceptuserdate();
				Date shouldEndDate;
				if (auditTask.getPromise_day() != null && auditTask.getPromise_day() > 0) {
					IAuditOrgaWorkingDay auditCenterWorkingDayService = ContainerFactory.getContainInfo()
							.getComponent(IAuditOrgaWorkingDay.class);
					shouldEndDate = auditCenterWorkingDayService.getWorkingDayWithOfficeSet(
							auditProject.getCenterguid(), acceptdat, auditTask.getPromise_day()).getResult();
					log.info("shouldEndDate:"+shouldEndDate);
				} else {
					shouldEndDate = null;
				}
				if(auditProjectUnusuals != null && auditProjectUnusuals.size() > 0) {
					Duration totalDuration = Duration.ZERO;  // 用于累加时间差（以秒为单位）
					LocalDateTime currentTime = null;
					for(AuditProjectUnusual auditProjectUnusual:auditProjectUnusuals) {
						// 将Date转换为Instant
						Instant instant = auditProjectUnusual.getOperatedate().toInstant();
						if(10==auditProjectUnusual.getOperatetype()){
							// 通过Instant和系统默认时区获取LocalDateTime
							currentTime= LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
						}
						if(currentTime!=null && 11==auditProjectUnusual.getOperatetype()){
							LocalDateTime nextTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
							Duration danci = Duration.between(currentTime, nextTime);
							totalDuration = totalDuration.plus(danci);
							currentTime = null;
						}
					}
					// 将累加的时间差加到初始的Date类型的shouldEndDate上
					Instant instant = shouldEndDate.toInstant();
					Instant newInstant = instant.plus(totalDuration);
					shouldEndDate = Date.from(newInstant);
					log.info("shouldEndDate:"+shouldEndDate);
				}
				if(shouldEndDate!=null && !"1753-1-1".equals(EpointDateUtil.convertDate2String(shouldEndDate))){
    auditProject.setPromiseenddate(shouldEndDate);
}
			}
			
			iAuditProject.updateProject(auditProject);

			// 6、终 止 审 批 流 程 ，
			// 根 据 办 件 表 pviguid 获 取 流 程 实例ProcessVersionInstance ，
			// 若ProcessVersionInstance不为空，则调用auditWorkflowBizlogic.finish（）方法终止工作流;
			/*if (StringUtil.isNotBlank(auditProject.getPviguid())) {
				ProcessVersionInstance pvi = iWFInstanceAPI9.getProcessVersionInstance(auditProject.getPviguid());
				// 流程中止操作
				if (pvi != null) {
					IWFEngineAPI9 wfengine = ContainerFactory.getContainInfo().getComponent(IWFEngineAPI9.class);
					WorkflowParameter9 param = new WorkflowParameter9();
					param.setProcessVersionInstanceGuid(pvi.getPvi().getProcessVersionInstanceGuid());
					param.setOperateType(WorkflowKeyNames9.OperationType_TerminatePVI);
					param.setWorkItemGuid("");
					param.setOpinion("撤销申请");
					param.setOperationGuid(auditProject.getApplyeruserguid());
					wfengine.operate(param.ConvertToJson());
				}
			}*/

			// 7、删除该办件的待受理待办,并发送消息提醒，根据中心标识centerguid，确定所选事项所在窗口，获取该窗口的关联业务人员，发送“XX
			// 人员已撤销【办件名称】的申请，请知悉！”
			/*AuditTask auditTask = iAuditTask.getAuditTaskByGuid(auditProject.getTaskguid(), false).getResult();
			if (auditTask == null) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "查询事项失败,请联系管理员！", "");
			}
			SqlConditionUtil windowUserSql = new SqlConditionUtil();
			windowUserSql.setSelectFields("distinct userguid,username");
			windowUserSql.in("windowguid",
					"(select a.rowguid from audit_orga_window a,audit_orga_windowtask b where a.RowGuid=b.WINDOWGUID AND b.TASKID = '"
							+ auditTask.getTask_id() + "' and a.centerguid = '" + auditProject.getCenterguid() + "')");
			List<AuditOrgaWindowUser> windowUserList = iAuditOrgaWindow.getWindowUser(windowUserSql.getMap())
					.getResult();
			if (windowUserList.isEmpty()) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "未查询到事项所在窗口的窗口人员，请联系管理员进行配置！", "");
			}
			String content = auditProject.getApplyername() + "人员已撤销【" + auditProject.getProjectname() + "】的申请，请知悉！";
			Date nowDate = new Date();
			String messageGuid = UUID.randomUUID().toString();

			for (AuditOrgaWindowUser auditOrgaWindowUser : windowUserList) {
				// 7.1、删除该办件的待受理待办
				List<MessagesCenter> messagesCenterList = iMessagesCenterService.queryForList(
						auditOrgaWindowUser.getUserguid(), null, null, "", IMessagesCenterService.MESSAGETYPE_WAIT,
						auditProject.getRowguid(), "", -1, "", null, null, 0, -1);
				if (messagesCenterList != null && !messagesCenterList.isEmpty()) {
					for (MessagesCenter messagescenter : messagesCenterList) {
						iMessagesCenterService.deleteMessage(messagescenter.getMessageItemGuid(),
								auditOrgaWindowUser.getUserguid());
					}
				}

				// 7.2、发送消息提醒
				iOnlineMessageInfoService.insertMessage(messageGuid, auditProject.getApplyeruserguid(),
						auditProject.getApplyername(), auditOrgaWindowUser.getUserguid(),
						auditOrgaWindowUser.getUsername(), auditOrgaWindowUser.getUserguid(), content, nowDate);
			}*/

			// 8、返回
			JSONObject dataJson = new JSONObject();
			log.info("=======结束调用handleRevoke接口=======");
			return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ONE, "撤销申请成功！", dataJson.toJSONString());
		} catch (Exception e) {
			log.error("=======handleRevoke接口参数：params【" + params + "】=======");
			log.error("=======handleRevoke异常信息：" + e.getMessage() + "=======", e);
			return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "撤销申请失败！" + e.getMessage(), "");
		}
	}

	/**
	 * 补正接口
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/handleBuzheng", method = RequestMethod.POST)
	public String handleBuzheng(@RequestBody String params, @Context HttpServletRequest request) {
		try {
			log.info("=======开始调用handleBuzheng接口=======");
			AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
			if (auditOnlineRegister == null || StringUtil.isBlank(auditOnlineRegister.getAccountguid())) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "当前用户不存在或未登录！", "");
			}

			// 1、入参转化为JSON对象
			JSONObject paramsJson = JSONObject.parseObject(params);
			JSONObject paramss = paramsJson.getJSONObject("params");
			// 办件标识
			String projectGuid = paramss.getString("projectguid");
			// String areaCode = paramss.getString("areacode");
			String address = paramss.getString("address");

			// 2、校验
			if (StringUtil.isBlank(projectGuid)) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "必填参数projectguid为空!", "");
			}
			AuditProject auditProject = iAuditProject.getAuditProjectByRowGuid("*", projectGuid, "").getResult();
			if (auditProject == null) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "未查询到办件，请联系管理员!", "");
			}
			// 审核不通过才会有“补正”按钮
			if (auditProject.getStatus() != ZwfwConstant.BANJIAN_STATUS_SPBTG) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "办件状态不是“审核不通过”，不可“补正”，请联系管理员!", "");
			}
			AuditTaskExtension auditTaskExtension = iAuditTaskExtension
					.getTaskExtensionByTaskGuid(auditProject.getTaskguid(), false).getResult();
			if (auditTaskExtension == null) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "未查询到事项,请联系管理员！", "");
			}

			EpointFrameDsManager.begin(null);
			// 3、办件操作表（audit_project_operation）新增一条记录，其中操作类型operatetype 为 22，备注
			// remarks 默认为‘补正’
			AuditProjectOperation auditProjectOperation = new AuditProjectOperation();
			auditProjectOperation.setRowguid(UUID.randomUUID().toString());
			auditProjectOperation.setOperatedate(new Date());
			auditProjectOperation.setOperateUserGuid(auditProject.getApplyeruserguid());
			auditProjectOperation.setOperateusername(auditProject.getApplyername());
			auditProjectOperation.setProjectGuid(auditProject.getRowguid());
			auditProjectOperation.setRemarks("补正");
			auditProjectOperation.setPVIGuid(auditProject.getPviguid());
			auditProjectOperation.setApplyerGuid(auditProject.getApplyeruserguid());
			auditProjectOperation.setApplyerName(auditProject.getApplyername());
			auditProjectOperation.setTaskGuid(auditProject.getTaskguid());
			auditProjectOperation.setOperateType(ZwfwConstant.OPERATE_BZ);
			auditProjectOperation.setAreaCode(auditProject.getAreacode());
			iAuditProjectOperation.addProjectOperation(auditProjectOperation);

			// 删除审批不通过后发的代办
			/*if (StringUtil.isNotBlank(auditProject.getPviguid())) {
				IMessagesCenterService messagesCenterService = ContainerFactory.getContainInfo()
						.getComponent(IMessagesCenterService.class);
				IWFInstanceAPI9 wfinstance = ContainerFactory.getContainInfo().getComponent(IWFInstanceAPI9.class);
				ProcessVersionInstance pVersionInstance = wfinstance
						.getProcessVersionInstance(auditProject.getPviguid());

				List<Integer> status = new ArrayList<Integer>();
				status.add(WorkflowKeyNames9.WorkItemStatus_Active);
				status.add(WorkflowKeyNames9.WorkItemStatus_Inactive);
				List<WorkflowWorkItem> workflowWorkItems = wfinstance
						.getWorkItemListByPVIGuidAndStatus(pVersionInstance, status);
				if (workflowWorkItems != null && !workflowWorkItems.isEmpty()) {
					for (WorkflowWorkItem workflowWorkItem : workflowWorkItems) {
						if (WorkflowKeyNames9.WorkItemStatus_Active == workflowWorkItem.getStatus()) {
							messagesCenterService.deleteMessage(workflowWorkItem.getWaitHandleGuid(),
									workflowWorkItem.getTransactor());
						}
					}
				}
			}*/

			// 4、更新办件表字段：办件状态 status 为 26（待受理）
			auditProject.setStatus(ZwfwConstant.BANJIAN_STATUS_YJJ);
			if (StringUtil.isNotBlank(address)) {
				auditProject.setAddress(address);
			}
			// 点击受理后，重新启动工作流，不然会导致，点击“受理”直接办结的情况
			auditProject.setPviguid("");
			iAuditProject.updateProject(auditProject);

			// 5、发送待办，根据中心标识 centerguid，确定所选事项所在窗口，获取该窗口的关联业务人员，发送【补正】待办，待办 url
			// 同待受理待办
			/*AuditTask auditTask = iAuditTask.getAuditTaskByGuid(auditProject.getTaskguid(), false).getResult();
			if (auditTask == null) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "查询事项失败,请联系管理员！", "");
			}
			SqlConditionUtil windowUserSql = new SqlConditionUtil();
			windowUserSql.setSelectFields("distinct userguid,username");
			windowUserSql.in("windowguid",
					"(select a.rowguid from audit_orga_window a,audit_orga_windowtask b where a.RowGuid=b.WINDOWGUID AND b.TASKID = '"
							+ auditTask.getTask_id() + "' and a.centerguid = '" + auditProject.getCenterguid() + "')");
			List<AuditOrgaWindowUser> windowUserList = iAuditOrgaWindow.getWindowUser(windowUserSql.getMap())
					.getResult();
			if (windowUserList.isEmpty()) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "未查询到事项所在窗口的窗口人员，请联系管理员进行配置！", "");
			}
			String title = "【补正】" + auditProject.getProjectname() + "(" + auditProject.getApplyername() + ")";
			Date nowDate = new Date();
			String formUrl = StringUtil.isNotBlank(auditTaskExtension.getCustomformurl())
					? auditTaskExtension.getCustomformurl()
					: ZwfwConstant.CONSTANT_FORM_URL;
			String handleUrl = formUrl + "?processguid=" + auditTask.getProcessguid() + "&taskguid="
					+ auditProject.getTaskguid() + "&projectguid=" + auditProject.getRowguid();
			for (AuditOrgaWindowUser auditOrgaWindowUser : windowUserList) {
				iMessagesCenterService.insertWaitHandleMessage(UUID.randomUUID().toString(), title,
						IMessagesCenterService.MESSAGETYPE_WAIT, auditOrgaWindowUser.getUserguid(),
						auditOrgaWindowUser.getUsername(), auditProject.getApplyeruserguid(),
						auditProject.getApplyername(), "补正", handleUrl, "", "", ZwfwConstant.CONSTANT_INT_ONE, "", "",
						auditProject.getRowguid(), auditProject.getRowguid().substring(0, 1), nowDate,
						auditProject.getPviguid(), auditProject.getApplyeruserguid(), "", "");
			}*/
			EpointFrameDsManager.commit();
			// 6、返回
			JSONObject dataJson = new JSONObject();
			log.info("=======结束调用handleBuzheng接口=======");
			return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ONE, "补正成功！", dataJson.toJSONString());
		} catch (Exception e) {
			EpointFrameDsManager.rollback();
			log.error("=======handleBuzheng接口参数：params【" + params + "】=======");
			log.error("=======handleBuzheng异常信息：" + e.getMessage() + "=======", e);
			return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "补正失败！" + e.getMessage(), "");
		}
	}

	/**
	 * 获取事项列表接口
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/getTaskList", method = RequestMethod.POST)
	public String getTaskList(@RequestBody String params, @Context HttpServletRequest request) {
		try {
			log.info("=======开始调用getTaskList接口=======");
			AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
			if (auditOnlineRegister == null || StringUtil.isBlank(auditOnlineRegister.getAccountguid())) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "当前用户不存在或未登录！", "");
			}

			// 1、入参转化为JSON对象
			JSONObject paramsJson = JSONObject.parseObject(params);
			JSONObject paramss = paramsJson.getJSONObject("params");
			// 当前页码
			String currentPage = paramss.getString("currentpage");
			String pageSize = paramss.getString("pagesize");
			// 事项名称
			String keyWord = paramss.getString("keyword");

			// 2、校验
			if (StringUtil.isBlank(currentPage)) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "必填参数currentpage为空!", "");
			}
			if (StringUtil.isBlank(pageSize)) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "必填参数pagesize为空!", "");
			}

			// 3、分页查询事项信息
			String sql = "SELECT a.rowguid taskguid,a.item_id itemid,a.taskname from audit_task a, audit_task_extension b where a.RowGuid=b.TASKGUID " + 
					"and a.ISTEMPLATE=0 and IS_EDITAFTERIMPORT=1 and IFNULL(IS_HISTORY,0)=0 and b.is_inquest='1'";
			String sqlcount = "SELECT count(1) from audit_task a, audit_task_extension b where a.RowGuid=b.TASKGUID " + 
					"and a.ISTEMPLATE=0 and IS_EDITAFTERIMPORT=1 and IFNULL(IS_HISTORY,0)=0 and b.is_inquest='1'";
			if(StringUtil.isNotBlank(keyWord)) {
				sql+=" and a.TaskName like '%"+keyWord+"%'";
				sqlcount+=" and a.TaskName like '%"+keyWord+"%'";
			}
			List<Record> list= CommonDao.getInstance().findList(sql, Integer.valueOf(currentPage), Integer.valueOf(pageSize), Record.class);
			//int total=CommonDao.getInstance().find(sqlcount, Integer.class);
			// 4、返回
			JSONObject dataJson = new JSONObject();
			if (list != null&&list.size()>0) {
				com.epoint.core.utils.sql.SqlConditionUtil inquestSql = new com.epoint.core.utils.sql.SqlConditionUtil();
				for (Record auditTask : list) {
					// 勘验事项情形表
					inquestSql.clear();
					inquestSql.eq("taskguid", auditTask.getStr("taskguid"));
					inquestSql.setOrderDesc("createdate");
					inquestSql.setSelectFields("rowguid inquestguid,situationname inquestname,situationurl inquesturl");
					List<AuditTaskInquestsituation> inquestList = iZcCommonService.findList(inquestSql.getMap(),
							AuditTaskInquestsituation.class);
					auditTask.put("taskinquestlList", inquestList);
				}
				//dataJson.put("totalcount", total);
				dataJson.put("taskList", list);
			} else {
				dataJson.put("totalcount", ZwfwConstant.CONSTANT_STR_ZERO);
				dataJson.put("taskList", new ArrayList<>());
			}

			log.info("=======结束调用getTaskList接口=======");
			return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ONE, "获取清单事项措施列表成功!", dataJson.toJSONString());
		} catch (Exception e) {
			log.error("=======getTaskList接口参数：params【" + params + "】=======");
			log.error("=======getTaskList异常信息：" + e.getMessage() + "=======", e);
			return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "获取清单事项措施列表失败！" + e.getMessage(), "");
		}
	}
	
	/**
	 * 获取事项列表接口
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/getSelectTaskList", method = RequestMethod.POST)
	public String getSelectTaskList(@RequestBody String params, @Context HttpServletRequest request) {
		try {
			log.info("=======开始调用getTaskList接口=======");
			AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
			if (auditOnlineRegister == null || StringUtil.isBlank(auditOnlineRegister.getAccountguid())) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "当前用户不存在或未登录！", "");
			}

			// 1、入参转化为JSON对象
			JSONObject paramsJson = JSONObject.parseObject(params);
			JSONObject paramss = paramsJson.getJSONObject("params");
			// 当前页码
			String currentPage = paramss.getString("currentpage");
			String pageSize = paramss.getString("pagesize");
			// 事项名称
			String keyWord = paramss.getString("keyword");

			// 2、校验
			if (StringUtil.isBlank(currentPage)) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "必填参数currentpage为空!", "");
			}
			if (StringUtil.isBlank(pageSize)) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "必填参数pagesize为空!", "");
			}

			// 3、分页查询事项信息
			String sql = "SELECT a.rowguid taskguid,a.item_id itemid,a.taskname from audit_task a, audit_task_extension b where a.RowGuid=b.TASKGUID " + 
					"and a.ISTEMPLATE=0 and IS_EDITAFTERIMPORT=1 and IFNULL(IS_HISTORY,0)=0 and b.is_inquest='1'";
			
			if(StringUtil.isNotBlank(keyWord)) {
				sql+=" and a.TaskName like '%"+keyWord+"%'";
			}
			List<Record> list= CommonDao.getInstance().findList(sql, Integer.valueOf(currentPage), Integer.valueOf(pageSize), Record.class);
			
			// 4、返回
			JSONObject dataJson = new JSONObject();
			if (list != null&&list.size()>0) {
				dataJson.put("taskList", list);
			} else {
				dataJson.put("taskList", new ArrayList<>());
			}

			log.info("=======结束调用getTaskList接口=======");
			return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ONE, "获取清单事项措施列表成功!", dataJson.toJSONString());
		} catch (Exception e) {
			log.error("=======getTaskList接口参数：params【" + params + "】=======");
			log.error("=======getTaskList异常信息：" + e.getMessage() + "=======", e);
			return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "获取清单事项措施列表失败！" + e.getMessage(), "");
		}
	}

	/**
	 * 获取事项详情接口
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/getTaskDetail", method = RequestMethod.POST)
	public String getTaskDetail(@RequestBody String params, @Context HttpServletRequest request) {
		try {
			log.info("=======开始调用getTaskDetail接口=======");
			AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
			if (auditOnlineRegister == null || StringUtil.isBlank(auditOnlineRegister.getAccountguid())) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "当前用户不存在或未登录！", "");
			}

			// 1、入参转化为JSON对象
			JSONObject paramsJson = JSONObject.parseObject(params);
			JSONObject paramss = paramsJson.getJSONObject("params");
			// 事项唯一标识
			String taskGuid = paramss.getString("taskguid");

			// 2、校验
			if (StringUtil.isBlank(taskGuid)) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "必填参数taskguid为空!", "");
			}
			AuditTask auditTask = iAuditTask.getAuditTaskByGuid(taskGuid, false).getResult();
			if (auditTask == null) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "未查询到事项，请联系管理员!", "");
			}

			// 3、返回
			JSONObject dataJson = new JSONObject();
			// 3.1、事项基本信息
			dataJson.put("taskguid", auditTask.getRowguid());
			dataJson.put("taskname", auditTask.getTaskname());
			dataJson.put("itemid", auditTask.getItem_id());
			dataJson.put("type", auditTask.getType());

			String typeText = iCodeItemsService.getItemTextByCodeName("事项类型", String.valueOf(auditTask.getType()));
			if (StringUtil.isNotBlank(typeText)) {
				dataJson.put("typetext", typeText);
			} else {
				dataJson.put("typetext", auditTask.getType());
			}

			dataJson.put("anticipateday", auditTask.getAnticipate_day() + "工作日");
			dataJson.put("promiseday", auditTask.getPromise_day() + "工作日");
			dataJson.put("handleaddress", auditTask.getTransact_addr());
			dataJson.put("handletime", auditTask.getTransact_time());
			dataJson.put("applyertype", auditTask.getApplyertype());

			if (StringUtil.isNotBlank(auditTask.getApplyertype()) && auditTask.getApplyertype().contains(",")) {
				// 包含
				StringBuilder applyerTypeStr = new StringBuilder();
				String[] applyerTypes = auditTask.getApplyertype().split(",");
				for (int i = 0; i < applyerTypes.length; i++) {
					String text = iCodeItemsService.getItemTextByCodeName("申请人类型", applyerTypes[i]);
					if (StringUtil.isNotBlank(text)) {
						applyerTypeStr.append(text).append(",");
					}
				}
				dataJson.put("applyertypetext", applyerTypeStr.toString());
			} else {
				String text = iCodeItemsService.getItemTextByCodeName("申请人类型", auditTask.getApplyertype());
				if (StringUtil.isNotBlank(text)) {
					dataJson.put("applyertypetext", text);
				}
			}

			dataJson.put("handlecondition", auditTask.getAcceptcondition());
			dataJson.put("setting", auditTask.getBy_law());

			List<JSONObject> taskMaterialList = new ArrayList<JSONObject>();
			// 3.2、获取事项材料信息
			List<AuditTaskMaterial> auditTaskMaterials = iAuditTaskMaterial
					.selectTaskMaterialListByTaskGuid(taskGuid, true).getResult();
			// 3.2.1、对事项材料进行排序
			Collections.sort(auditTaskMaterials, new Comparator<AuditTaskMaterial>() {
				@Override
				public int compare(AuditTaskMaterial b1, AuditTaskMaterial b2) {
					// 优先对比材料必要性（必要在前）
					int comNecessity = b1.getNecessity().compareTo(b2.getNecessity());
					int ret = comNecessity;
					// 材料必要性一致的情况下对比排序号（排序号降序排）
					if (comNecessity == 0) {
						Integer ordernum1 = b1.getOrdernum() == null ? 0 : b1.getOrdernum();
						Integer ordernum2 = b2.getOrdernum() == null ? 0 : b2.getOrdernum();
						ret = ordernum2.compareTo(ordernum1);
					}
					return ret;
				}
			});
			// 3.2.2、拼接材料返回JSON
			for (AuditTaskMaterial auditTaskMaterial : auditTaskMaterials) {
				JSONObject materialJson = new JSONObject();
				String materialGuid = auditTaskMaterial.getRowguid();
				materialJson.put("materialguid", materialGuid);// 材料标识
				materialJson.put("materialname", auditTaskMaterial.getMaterialname());// 材料名称
				// 3.2.1、获取填报示例对应附件标识
				if (StringUtil.isNotBlank(auditTaskMaterial.getExampleattachguid())) {
					int exampleAttachCount = iAttachService
							.getAttachCountByClientGuid(auditTaskMaterial.getExampleattachguid());
					if (exampleAttachCount > 0) {
						materialJson.put("exampleattachguid", auditTaskMaterial.getExampleattachguid());
					}
				}
				// 3.2.2、获取空白模板对应附件标识
				if (StringUtil.isNotBlank(auditTaskMaterial.getTemplateattachguid())) {
					int templateAttachCount = iAttachService
							.getAttachCountByClientGuid(auditTaskMaterial.getTemplateattachguid());
					if (templateAttachCount > 0) {
						materialJson.put("templateattachguid", auditTaskMaterial.getTemplateattachguid());
					}
				}
				materialJson.put("submittype", iCodeItemsService.getItemTextByCodeName("提交方式",
						String.valueOf(auditTaskMaterial.getSubmittype()))); // 材料提交方式
				// 3.2.3、获取材料来源渠道
				String materialsource;
				if (StringUtil.isBlank(auditTaskMaterial.getFile_source())) {
					materialsource = "申请人自备";
				} else {
					materialsource = iCodeItemsService.getItemTextByCodeName("来源渠道",
							auditTaskMaterial.getFile_source());
				}
				materialJson.put("materialsource", materialsource);
				// 3.2.4、获取事项材料必要性// 是否必需
				String necessary = "0";
				necessary = ZwfwConstant.NECESSITY_SET_YES.equals(String.valueOf(auditTaskMaterial.getNecessity()))
						? "1"
						: "0";
				materialJson.put("necessary", necessary);
				// 份数
				materialJson.put("pagenum",
						StringUtil.isBlank(auditTaskMaterial.getPage_num()) ? "0" : auditTaskMaterial.getPage_num());
				// 受理标准
				materialJson.put("standard",
						StringUtil.isBlank(auditTaskMaterial.getStandard()) ? "无" : auditTaskMaterial.getStandard());
				// 填报须知
				materialJson.put("fileexplain", StringUtil.isBlank(auditTaskMaterial.getFile_explian()) ? "无"
						: auditTaskMaterial.getFile_explian());
				taskMaterialList.add(materialJson);
			}
			dataJson.put("taskmaterialList", taskMaterialList);
			log.info("=======结束调用getTaskDetail接口=======");
			return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ONE, "获取清单事项措施列表成功!", dataJson.toJSONString());
		} catch (Exception e) {
			log.error("=======getTaskDetail接口参数：params【" + params + "】=======");
			log.error("=======getTaskDetail异常信息：" + e.getMessage() + "=======", e);
			return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "获取清单事项措施列表失败！" + e.getMessage(), "");
		}
	}

	/**
	 * 获取消息列表接口
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/getMessageCentersList", method = RequestMethod.POST)
	public String getMessageCentersList(@RequestBody String params, @Context HttpServletRequest request) {
		try {
			log.info("=======开始调用getMessageCentersList接口=======");
			AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
			if (auditOnlineRegister == null || StringUtil.isBlank(auditOnlineRegister.getAccountguid())) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "当前用户不存在或未登录！", "");
			}
			AuditOnlineIndividual auditOnlineIndividual = iAuditOnlineIndividual
					.getIndividualByAccountGuid(auditOnlineRegister.getAccountguid()).getResult();
			// 1、入参转化为JSON对象
			JSONObject paramsJson = JSONObject.parseObject(params);
			JSONObject paramss = paramsJson.getJSONObject("params");
			// 当前页码
			String currentPage = paramss.getString("currentpage");
			String pageSize = paramss.getString("pagesize");
			// 人员标识
			String userGuid = auditOnlineIndividual.getApplyerguid();

			// 2、校验
			if (StringUtil.isBlank(userGuid)) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "必填参数userguid为空!", "");
			}

			// 3、查询消息列表
			String sql = "SELECT a.messageguid,a.messagecontent content,a.readtime,b.PROJECTNAME,b.RowGuid projectguid,b.FLOWSN,b.`STATUS`,b.APPLYDATE from messages_message a,audit_project b"
					+ " where a.clientidentifier=b.RowGuid and b.APPLYERUSERGUID=? ORDER BY a.sendtime DESC";
			List<Record> messagesList = CommonDao.getInstance().findList(sql,
					Integer.valueOf(currentPage) * Integer.valueOf(pageSize), Integer.valueOf(pageSize), Record.class,
					userGuid);

			if (!messagesList.isEmpty()) {
				for (Record messagesMessage : messagesList) {
					messagesMessage.put("applydate", EpointDateUtil
							.convertDate2String(messagesMessage.getDate("applydate"), EpointDateUtil.DATE_TIME_FORMAT));
					if (messagesMessage.get("readtime") != null) {
						messagesMessage.put("isread", "1");
					} else {
						messagesMessage.put("isread", "0");
					}
					
					if (messagesMessage.getInt("status") == ZwfwConstant.BANJIAN_STATUS_YJJ) {
						messagesMessage.put("projectstatus", "待受理");
					} else {
						String projectStatus = iCodeItemsService.getItemTextByCodeName("办件状态",
								String.valueOf(messagesMessage.getInt("status")));
						if (StringUtil.isNotBlank(projectStatus)) {
							messagesMessage.put("projectstatus", projectStatus);
						} else {
							messagesMessage.put("projectstatus", messagesMessage.getInt("status"));
						}
					}

				}
			}
			// 3、返回
			JSONObject dataJson = new JSONObject();
			dataJson.put("messagesList", messagesList);
			dataJson.put("totalcount", messagesList.size());

			log.info("=======结束调用getMessageCentersList接口=======");
			return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ONE, "获取消息列表成功!", dataJson.toJSONString());
		} catch (Exception e) {
			log.error("=======getMessageCentersList接口参数：params【" + params + "】=======");
			log.error("=======getMessageCentersList异常信息：" + e.getMessage() + "=======", e);
			return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "获取消息列表失败！" + e.getMessage(), "");
		}
	}

	/**
	 * 消息已读接口
	 *
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/updateMessageStatus", method = RequestMethod.POST)
	public String updateMessageStatus(@RequestBody String params, @Context HttpServletRequest request) {
		try {
			log.info("=======开始调用updateMessageStatus接口=======");
			AuditOnlineRegister auditOnlineRegister = this.getOnlineRegister(request);
			if (auditOnlineRegister == null || StringUtil.isBlank(auditOnlineRegister.getAccountguid())) {
				return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "当前用户不存在或未登录！", "");
			}
			// 1、入参转化为JSON对象
			JSONObject paramsJson = JSONObject.parseObject(params);
			JSONObject paramss = paramsJson.getJSONObject("params");
			// 消息标识
			String messagesguid = paramss.getString("messagesguid");

			// 2、更新消息
			String sql = "update messages_message set readtime=? where messageguid=?";
			CommonDao.getInstance().execute(sql,
					EpointDateUtil.convertDate2String(new Date(), EpointDateUtil.DATE_TIME_FORMAT), messagesguid);
			// 3、返回
			JSONObject dataJson = new JSONObject();

			log.info("=======结束调用updateMessageStatus接口=======");
			return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ONE, "更新消息状态成功!", dataJson.toJSONString());
		} catch (Exception e) {
			log.error("=======updateMessageStatus接口参数：params【" + params + "】=======");
			log.error("=======updateMessageStatus异常信息：" + e.getMessage() + "=======", e);
			return JsonUtils.zwdtRestReturn(ZwfwConstant.CONSTANT_STR_ZERO, "更新消息失败！" + e.getMessage(), "");
		}
	}

	public AuditOnlineRegister getOnlineRegister(HttpServletRequest httpServletRequest) {
		IAuditOnlineRegister iAuditOnlineRegister = ContainerFactory.getContainInfo()
				.getComponent(IAuditOnlineRegister.class);
		AuditOnlineRegister auditOnlineRegister = null;
		OAuthCheckTokenInfo oAuthCheckTokenInfo = CheckTokenUtil.getCheckTokenInfo(httpServletRequest);
		if (oAuthCheckTokenInfo != null) {
			// 手机端
			// 通过登录名获取用户
			auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(oAuthCheckTokenInfo.getLoginid())
					.getResult();
		}
		return auditOnlineRegister;
	}

}
