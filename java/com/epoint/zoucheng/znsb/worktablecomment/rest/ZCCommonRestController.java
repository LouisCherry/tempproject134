package com.epoint.zoucheng.znsb.worktablecomment.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.epoint.basic.auditorga.auditarea.domain.AuditOrgaArea;
import com.epoint.basic.auditorga.auditarea.inter.IAuditOrgaArea;
import com.epoint.basic.auditorga.auditcenter.domain.AuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditcenter.inter.IAuditOrgaServiceCenter;
import com.epoint.basic.auditorga.auditconfig.domain.AuditOrgaConfig;
import com.epoint.basic.auditorga.auditconfig.inter.IAuditOrgaConfig;
import com.epoint.basic.auditqueue.auditznsbaccesscabinet.domain.AuditZnsbAccesscabinet;
import com.epoint.basic.auditqueue.auditznsbaccesscabinet.inter.IAuditZnsbAccesscabinet;
import com.epoint.basic.auditqueue.auditznsbequipment.domain.AuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbequipment.inter.IAuditZnsbEquipment;
import com.epoint.basic.auditqueue.auditznsbmodule.domain.FunctionModule;
import com.epoint.basic.auditqueue.auditznsbmodule.inter.IAuditZnsbModule;
import com.epoint.basic.auditqueue.auditznsbpay.inter.IAuditZnsbPay;
import com.epoint.basic.auditqueue.auditznsbselfmachineregion.domain.AuditZnsbSelfmachineregion;
import com.epoint.basic.auditqueue.auditznsbselfmachineregion.inter.IAuditZnsbSelfmachineregion;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.util.ZwfwRedisCacheUtil;
import com.epoint.common.znsb.util.QueueConstant;
import com.epoint.composite.auditorga.handleconfig.inter.IHandleConfig;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.security.crypto.sm.sm2.SM2ClientUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.metadata.code.api.ICodeItemsService;
import com.epoint.frame.service.metadata.code.entity.CodeItems;
import com.epoint.zoucheng.znsb.auditznsbselfmachinemodule.domain.ZCAuditZnsbSelfmachinemodule;
import com.epoint.zoucheng.znsb.auditznsbselfmachinemodule.inter.IZCAuditZnsbSelfmachinemoduleService;

@RestController
@RequestMapping("/zcselfservicecommon")
public class ZCCommonRestController {

	@Autowired
	private IAuditZnsbEquipment equipmentservice;

	@Autowired
	private IAuditOrgaServiceCenter servicecenterservice;

	@Autowired
	private IAuditOrgaConfig auditconfigservice;

	@Autowired
	private IAuditZnsbPay auditznsbpayapi;

	@Autowired
	private IAuditZnsbModule auditModuleservice;

	@Autowired
	private ICodeItemsService codeitemsservice;

	@Autowired
	private IZCAuditZnsbSelfmachinemoduleService moduleService;

	@Autowired
	private IHandleConfig handleConfigservice;
	@Autowired
	private IAuditZnsbAccesscabinet accessCabinetService;
	/*
	 * @Autowired private IConfigService configServcie;
	 */

	@Autowired
	private IAuditZnsbSelfmachineregion regionServcie;

	@Autowired
	private IAuditOrgaArea areaService;

	@Autowired
	private IAttachService attachService;


	/**
	 * 初始化
	 * 
	 * @params params
	 * @return
	 * 
	 * 
	 */
	@RequestMapping(value = "/init", method = RequestMethod.POST)
	public String init(@RequestBody String params, @Context HttpServletRequest request) {
		try {

			JSONObject json = JSON.parseObject(params);
			JsonUtils.checkUserAuth(json.getString("token"));
			JSONObject obj = (JSONObject) json.get("params");
			String macaddress = obj.getString("macaddress");
			String logomachinetype = obj.getString("logomachinetype");
			JSONObject dataJson = new JSONObject();
			String centerguid = "";

			// 根据Macaddress判断是否存在，不存在自动插入一条记录
			if (!equipmentservice.IsTerminalRegister(macaddress).getResult()) {
				AuditZnsbEquipment equipment = new AuditZnsbEquipment();
				equipment.setMacaddress(macaddress);
				equipment.setStatus(QueueConstant.EQUIPMENT_STATUS_ONLINE);
				equipment.setMachinetype(QueueConstant.EQUIPMENT_TYPE_YTJ);
				equipment.setRowguid(UUID.randomUUID().toString());
				equipment.setOperatedate(new Date());
				equipment.setXiaodayindevice("w80");
				equipment.setDadayindevice("Kyocera ECOSYS P4040dn KX");
				equipment.setColordayindevice("HP ColorLaserJet M253-M254 PCL 6");
				equipment.setBawleftpaperone(1);
				equipment.setBawleftpapertwo(1);
				equipment.setColorleftpaper(1);
				equipment.setRmleftpager("2");
				/*
				 * equipment.setXigu(100); equipment.setMohe(100.00);
				 */

				equipmentservice.insertEquipment(equipment);
			}

			AuditZnsbEquipment equipment = equipmentservice.getDetailbyMacaddress(macaddress).getResult();
			AuditZnsbAccesscabinet accesscabinet = accessCabinetService.getDetailByEquipmentMac(macaddress).getResult();
			if (StringUtil.isNotBlank(equipment)) {
				// 根据macaddress获取中心guid
				centerguid = equipment.getCenterguid();

				if (StringUtil.isNotBlank(centerguid)) {
					if (StringUtil.isNotBlank(accesscabinet)) {
						dataJson.put("cabinetguid", accesscabinet.getRowguid());
						dataJson.put("cabinetno", accesscabinet.getCabinetno());
						dataJson.put("cabinetmsg", accesscabinet.getLedcontent());
					} else {
						dataJson.put("cabinetguid", "");
					}
					dataJson.put("centerguid", centerguid);
					// 增加中心名称
					dataJson.put("centername",
							servicecenterservice.findAuditServiceCenterByGuid(centerguid).getResult().getCentername());
					//
					dataJson.put("xiaodayindevice", equipment.getXiaodayindevice());
					dataJson.put("dadayindevice", equipment.getDadayindevice());
					dataJson.put("colordayindevice", equipment.getColordayindevice());
					dataJson.put("machineguid", equipment.getRowguid());
					dataJson.put("machineno", equipment.getMachineno());
					dataJson.put("homepageurl", equipment.getHomepageurl());


					AuditOrgaServiceCenter auditCenter = servicecenterservice.findAuditServiceCenterByGuid(centerguid)
							.getResult();
					if (StringUtil.isNotBlank(auditCenter)) {
						dataJson.put("areacode", auditCenter.getBelongxiaqu());
						// 乡镇智能化（使用baseAreaCode替代areacode）
						AuditOrgaArea orga = areaService.getAreaByAreacode(auditCenter.getBelongxiaqu()).getResult();
						if (StringUtil.isNotBlank(orga)) {
							// 保存当前的areacode
							dataJson.put("currentareacode", auditCenter.getBelongxiaqu());
							// 保存父级areacode
							if (StringUtil.isNotBlank(orga.getBaseAreaCode())) {
								dataJson.put("areacode", orga.getBaseAreaCode());
							}
						}
					} else {
						return JsonUtils.zwdtRestReturn("0", "未分配辖区！", "");
					}
					// 判断是否启用
					if (QueueConstant.EQUIPMENT_STATUS_OFFLINE.equals(equipment.getStatus())) {
						return JsonUtils.zwdtRestReturn("0", "设备已离线！", "");
					}
					// 判断是否24小时无人值守设备
					if (QueueConstant.EQUIPMENT_TYPE_YTJ24.equals(equipment.getMachinetype())) {
						dataJson.put("is_24hours", QueueConstant.Common_yes_String);
					} else {
						dataJson.put("is_24hours", QueueConstant.Common_no_String);
					}
					// 上传申报材料方式类型 0 原来 1 扫码上传 2 云盘上传
					String useMobileUploadMaterial = handleConfigservice
							.getFrameConfig("AS_IS_USE_MOBILE_UPLOAD_MATERIAL", centerguid).getResult();
					dataJson.put("useMobileUploadMaterial", useMobileUploadMaterial);
					// 通知公告功能相关中心参数获取
					String usenotice = handleConfigservice.getFrameConfig("AS_IS_USE_NOTICE", centerguid).getResult();
					dataJson.put("usenotice", usenotice);
					String noticephone = handleConfigservice.getFrameConfig("NOTICE_PHONE", centerguid).getResult();
					dataJson.put("noticephone", noticephone);
					String noticeplaywaittime = handleConfigservice.getFrameConfig("NOTICE_PLAY_WAIT_TIME", centerguid)
							.getResult();
					dataJson.put("noticeplaywaittime", noticeplaywaittime);
					// 天气预报功能开启参数获取
					String usecityweather = handleConfigservice.getFrameConfig("AS_USE_CITY_WEATHER", centerguid)
							.getResult();
					dataJson.put("usecityweather", usecityweather);
					// 一体机事项搜索功能开启参数获取
					String usetasksearch = handleConfigservice.getFrameConfig("AS_USE_TASK_SEARCH", centerguid)
							.getResult();
					dataJson.put("usetasksearch", usetasksearch);
					String loginface = handleConfigservice.getFrameConfig("AS_SELFSERVICE_LOGINFACE", centerguid)
							.getResult();
					dataJson.put("loginface", StringUtil.isNotBlank(loginface) ? loginface : "0");
					// 存取柜
					String cqgloginface = handleConfigservice.getFrameConfig("AS_ACCESSCABINET_LOGINFACE", centerguid)
							.getResult();
					dataJson.put("cqgloginface", StringUtil.isNotBlank(cqgloginface) ? cqgloginface : "0");

					// 是否启ai审批
					String useaisp = handleConfigservice.getFrameConfig("AS_USE_AISP", centerguid).getResult();
					dataJson.put("useaisp", StringUtil.isNotBlank(useaisp) ? useaisp : "0");
					// 是否使用电子身份证
					String useecard = handleConfigservice.getFrameConfig("AS_USE_ECARD", centerguid).getResult();
					dataJson.put("useecard", StringUtil.isNotBlank(useecard) ? useecard : "0");
					// 是否启用自主申报二维码 1920版本
					String applyqrcode = handleConfigservice.getFrameConfig("AS_SELFSERVICE_APPLYQRCODE", centerguid)
							.getResult();
					dataJson.put("applyqrcode", StringUtil.isNotBlank(applyqrcode) ? applyqrcode : "1");

					String isusedahanlogin = handleConfigservice.getFrameConfig("AS_ZNSB_ISUSEDAHANLOGIN", centerguid)
							.getResult();
					dataJson.put("isusedahanlogin", StringUtil.isNotBlank(isusedahanlogin) ? isusedahanlogin : "0");
					// led屏幕显示内容
					String ledcontent = handleConfigservice.getFrameConfig("AS_ZNSB_LEDCONTENT", centerguid)
							.getResult();
					if (StringUtil.isBlank(ledcontent)) {
						// "第一行内容;第二行内容;1表示直接展示;3表示滚动展示"
						ledcontent = "国泰新点软件;股份有限公司;1;3";
					}
					dataJson.put("ledcontent", ledcontent);
					// 是否开启语音识别
					String usevoice = handleConfigservice.getFrameConfig("AS_ZNSB_USE_VOICERECOGNITION", centerguid)
							.getResult();
					dataJson.put("usevoice", usevoice);
				} else {
					return JsonUtils.zwdtRestReturn("0", "该设备已注册，但未配置，请先进系统配置！", "");
				}

			} else {
				return JsonUtils.zwdtRestReturn("0", "该设备未注册！", "");
			}
			return JsonUtils.zwdtRestReturn("1", "", dataJson);
		} catch (JSONException e) {
			return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
		}
	}


	/**
	 * 获取服务器时间
	 * 
	 * @params params
	 * @return
	 * 
	 * 
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value = "/getServiceTime", method = RequestMethod.POST)
	public String getServiceTime(@RequestBody String params) {
		try {

			JSONObject json = JSON.parseObject(params);
			JsonUtils.checkUserAuth(json.getString("token"));
			JSONObject obj = (JSONObject) json.get("params");
			JSONObject dataJson = new JSONObject();
			dataJson.put("servicetime", new Date());

			return JsonUtils.zwdtRestReturn("1", "", dataJson);
		} catch (JSONException e) {
			return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
		}
	}

	/**
	 * 循环扫描
	 * 
	 * @params params
	 * @return
	 * 
	 * 
	 */
	@RequestMapping(value = "/scan", method = RequestMethod.POST)
	public String scanProject(@RequestBody String params) {
		try {
			JSONObject json = JSON.parseObject(params);
			JsonUtils.checkUserAuth(json.getString("token"));
			JSONObject obj = (JSONObject) json.get("params");
			JSONObject dataJson = new JSONObject();
			String scanguid = obj.getString("scanguid");

			ZwfwRedisCacheUtil redisUtil = null;

			try {
				redisUtil = new ZwfwRedisCacheUtil(false);
				String scanstatus = redisUtil.getByString("scan_" + scanguid);
				if (StringUtil.isNotBlank(scanstatus)) {
					dataJson.put("scanstatus", scanstatus);
					redisUtil.del("scan_" + scanguid);
				} else {
					dataJson.put("scanstatus", "0");
				}
			} catch (Exception e) {
				throw new RuntimeException("redis执行发生了异常", e);
			} finally {
				if (redisUtil != null) {
					redisUtil.close();
				}
			}

			return JsonUtils.zwdtRestReturn("1", "", dataJson);
		} catch (JSONException e) {
			return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
		}

	}

	/**
	 * 创建扫描
	 * 
	 * @params params
	 * @return
	 * 
	 * 
	 */
	@RequestMapping(value = "/creatScan", method = RequestMethod.POST)
	public String creatScanProject(@RequestBody String params) {
		try {
			JSONObject json = JSON.parseObject(params);
			JsonUtils.checkUserAuth(json.getString("token"));
			JSONObject obj = (JSONObject) json.get("params");
			JSONObject dataJson = new JSONObject();
			String scanguid = obj.getString("scanguid");
			String scanstatus = obj.getString("scanstatus");

			ZwfwRedisCacheUtil redis = null;
			try {
				redis = new ZwfwRedisCacheUtil(false);
				redis.putByString("scan_" + scanguid, scanstatus, 300);
			} catch (Exception e) {
				throw new RuntimeException("redis执行发生了异常", e);
			} finally {
				if (redis != null) {
					redis.close();
				}
			}

			return JsonUtils.zwdtRestReturn("1", "", dataJson);
		} catch (JSONException e) {
			return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
		}

	}

	/**
	 * 获取中心相关信息
	 * 
	 * @params params
	 * @return
	 * 
	 * 
	 */
	@RequestMapping(value = "/getCenterDetail", method = RequestMethod.POST)
	public String getProjectPre(@RequestBody String params) {
		try {

			JSONObject json = JSON.parseObject(params);
			JsonUtils.checkUserAuth(json.getString("token"));
			JSONObject obj = (JSONObject) json.get("params");
			JSONObject dataJson = new JSONObject();
			String centerguid = obj.getString("centerguid");

			AuditOrgaConfig config = null;

			AuditOrgaServiceCenter auditCenter = servicecenterservice.findAuditServiceCenterByGuid(centerguid)
					.getResult();
			if (StringUtil.isNotBlank(auditCenter)) {
				dataJson.put("centername", auditCenter.getCentername());
			} else {
				dataJson.put("centername", "");
			}
			SqlConditionUtil sql = new SqlConditionUtil();
			sql.eq("centerguid", centerguid);
			sql.eq("configname", "AS_CENTER_MOBILE");
			config = auditconfigservice.getCenterConfig(sql.getMap()).getResult();
			if (StringUtil.isNotBlank(config)) {
				dataJson.put("centermobile", config.getConfigvalue());
			} else {
				dataJson.put("centermobile", "");
			}
			SqlConditionUtil sql1 = new SqlConditionUtil();
			sql1.eq("centerguid", centerguid);
			sql1.eq("configname", "AS_CENTER_WEB");
			config = auditconfigservice.getCenterConfig(sql1.getMap()).getResult();
			if (StringUtil.isNotBlank(config)) {
				dataJson.put("centerweb", config.getConfigvalue());
			} else {
				dataJson.put("centerweb", "");
			}

			return JsonUtils.zwdtRestReturn("1", "", dataJson);
		} catch (JSONException e) {
			return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
		}

	}

	
	/**
	 * 扫码后轮循数据库
	 * 
	 * @params params
	 * @return
	 * 
	 * 
	 */
	@RequestMapping(value = "/getQrcodeBack", method = RequestMethod.POST)
	public String getQrcodeBackProject(@RequestBody String params) {
		try {

			JSONObject json = JSON.parseObject(params);
			JsonUtils.checkUserAuth(json.getString("token"));
			JSONObject obj = (JSONObject) json.get("params");
			JSONObject dataJson = new JSONObject();
			String order_no = obj.getString("order_no");

			String success = auditznsbpayapi.getResultByOrdeono(order_no);

			dataJson.put("success", success);

			return JsonUtils.zwdtRestReturn("1", "", dataJson);
		} catch (JSONException e) {
			return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
		}

	}

	/**
	 * 插入点击模块信息
	 * 
	 * @params params
	 * @return
	 * 
	 * 
	 */
	@RequestMapping(value = "/insertOnClickModule", method = RequestMethod.POST)
	public String insertOnClickModule(@RequestBody String params) {
		try {
			JSONObject json = JSON.parseObject(params);
			JsonUtils.checkUserAuth(json.getString("token"));
			JSONObject obj = (JSONObject) json.get("params");
			String CardID = obj.getString("cardID");
			String Centerguid = obj.getString("centerguid");
			String Modulename = obj.getString("modulename");
			String Macaddress = obj.getString("macaddress");
			if (StringUtil.isBlank(Centerguid) || StringUtil.isBlank(Modulename) || StringUtil.isBlank(Macaddress)) {
				return JsonUtils.zwdtRestReturn("0", "Centerguid,ModuleName,MacAddress都不能为空", "");
			} else {
				FunctionModule module = new FunctionModule();
				module.setRowguid(UUID.randomUUID().toString());
				module.setCardid(CardID);
				module.setCenterguid(Centerguid);
				module.setModulename(Modulename);
				module.setMacaddress(Macaddress);
				module.setOnclicktime(new Date());
				auditModuleservice.insert(module);
				return JsonUtils.zwdtRestReturn("1", "", "");
			}
		} catch (JSONException e) {
			return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
		}
	}

	/**
	 * 动态获取模板列表
	 * 
	 * @params params
	 * @return
	 * 
	 * 
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/getmodulelist", method = RequestMethod.POST)
	public String getModuleList(@RequestBody String params) {
		try {
			JSONObject json = JSON.parseObject(params);
			JsonUtils.checkUserAuth(json.getString("token"));
			JSONObject obj = (JSONObject) json.get("params");
			JSONObject dataJson = new JSONObject();
			String centerguid = obj.getString("centerguid");
			String macaddress = obj.getString("macaddress");
			String moduleconfigtype = obj.getString("moduleconfigtype");

			List<CodeItems> codeItemsList = codeitemsservice.listCodeItemsByCodeName("邹城首页左侧分类");

			List<Map> typelist = new ArrayList<>();
			List<List<ZCAuditZnsbSelfmachinemodule>> modulelist = new ArrayList<List<ZCAuditZnsbSelfmachinemodule>>();
			List<ZCAuditZnsbSelfmachinemodule> typemodulelist = new ArrayList<>();
			Map<String, Object> typemap = null;
			List<ZCAuditZnsbSelfmachinemodule> alllist;
			if (StringUtil.isNotBlank(moduleconfigtype)) {
				alllist = moduleService.getModuleListByMacAndType(macaddress, centerguid, moduleconfigtype).getResult();
			} else {
				alllist = moduleService.getModuleListByMacaddress(macaddress, centerguid).getResult();
			}
			if (alllist != null && !alllist.isEmpty()) {
				for (CodeItems codeItems : codeItemsList) {
					typemap = new HashMap<String, Object>();
					typemap.put("typename", codeItems.getItemText());
					typemap.put("typevalue", codeItems.getItemValue());
					typemap.put("picurl", codeItems.getDmAbr1());
					if (StringUtil.isNotBlank(moduleconfigtype)) {
						typemap.put("typecount", moduleService.getCountByModuleTypeAndMac(codeItems.getItemValue(),
								centerguid, macaddress, moduleconfigtype).getResult());
						typemodulelist = moduleService.getModuleListByModuleTypeAndMac(codeItems.getItemValue(),
								centerguid, macaddress, moduleconfigtype).getResult();
					} else {
						typemap.put("typecount",
								moduleService
										.getCountByModuleTypeAndMac(codeItems.getItemValue(), centerguid, macaddress)
										.getResult());
						typemodulelist = moduleService
								.getModuleListByModuleTypeAndMac(codeItems.getItemValue(), centerguid, macaddress)
								.getResult();
					}

					typelist.add(typemap);

					modulelist.add(typemodulelist);
				}
			} else {
				for (CodeItems codeItems : codeItemsList) {
					typemap = new HashMap<String, Object>();
					typemap.put("typename", codeItems.getItemText());
					typemap.put("typevalue", codeItems.getItemValue());
					typemap.put("picurl", codeItems.getDmAbr1());
					if (StringUtil.isNotBlank(moduleconfigtype)) {
						typemap.put("typecount", moduleService
								.getCountByModuleTypeAndConfig(codeItems.getItemValue(), centerguid, moduleconfigtype)
								.getResult());
						typemodulelist = moduleService.getModuleListByModuleTypeAndConfig(codeItems.getItemValue(),
								centerguid, moduleconfigtype).getResult();
					} else {
						typemap.put("typecount",
								moduleService.getCountByModuleType(codeItems.getItemValue(), centerguid).getResult());
						typemodulelist = moduleService.getModuleListByModuleType(codeItems.getItemValue(), centerguid)
								.getResult();
					}

					typelist.add(typemap);

					modulelist.add(typemodulelist);
				}
			}

			dataJson.put("typelist", typelist);
			dataJson.put("modulelist", modulelist);
			return JsonUtils.zwdtRestReturn("1", "", dataJson);
		} catch (JSONException e) {
			return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
		}

	}

	/**
	 * 获取工作台模板列表
	 * 
	 * @params params
	 * @return
	 * 
	 * 
	 */
	@RequestMapping(value = "/getWorkTableModuleListByParentModuleGuid", method = RequestMethod.POST)
	public String getWorkTableModuleListByParentModuleGuid(@RequestBody String params) {
		try {
			JSONObject json = JSON.parseObject(params);
			JsonUtils.checkUserAuth(json.getString("token"));
			JSONObject obj = (JSONObject) json.get("params");
			JSONObject dataJson = new JSONObject();
			String centerguid = obj.getString("centerguid");
			String parentmoduleguid = obj.getString("parentmoduleguid");
			String macaddress = obj.getString("macaddress");
			String addParentmodule = obj.getString("addParentmodule");
			ZCAuditZnsbSelfmachinemodule parentmodule = moduleService.find(parentmoduleguid).getResult();
			if (StringUtil.isNotBlank(parentmodule) && StringUtil.isBlank(parentmodule.getParentmoduleguid())) {
				parentmodule.setModulename("全部");
			}
			dataJson.put("parentmodule", parentmodule);
			// 查询出该设备个性化和通用模块
			List<ZCAuditZnsbSelfmachinemodule> modulelist = moduleService
					.getModuleListByParentmoduleguid(parentmoduleguid, centerguid).getResult();
			List<ZCAuditZnsbSelfmachinemodule> othermodulelist = moduleService
					.getModuleListByParentmoduleguidAndMac(parentmoduleguid, centerguid, macaddress).getResult();
			modulelist.addAll(othermodulelist);
			boolean havethree = true;
			// 表示是第一层模块，还需要把第三层模块查询出来
			List<ZCAuditZnsbSelfmachinemodule> childrentypemodulelist = new ArrayList<ZCAuditZnsbSelfmachinemodule>();
			for (ZCAuditZnsbSelfmachinemodule module : modulelist) {
				if (QueueConstant.CONSTANT_STR_ONE.equals(module.getIsParentmodule())) {
					// 第三层模块
					childrentypemodulelist.addAll(
							moduleService.getModuleListByParentmoduleguid(module.getRowguid(), centerguid).getResult());
					childrentypemodulelist.addAll(moduleService
							.getModuleListByParentmoduleguidAndMac(module.getRowguid(), centerguid, macaddress)
							.getResult());
				}
			}
			if (!childrentypemodulelist.isEmpty()) {
				dataJson.put("childrentypemodulelist", childrentypemodulelist);
				dataJson.put("childrentotalcount", childrentypemodulelist.size());
			} else {
				// 只有有第三层模块时，第二层模块才可以作为左侧分类
				havethree = false;
			}

			if (QueueConstant.CONSTANT_STR_ONE.equals(addParentmodule)) {
				if (!havethree) {
					// 清空原来想作为第二层的模块
					modulelist.clear();
				}
				// 否则，把全部添加到第二层模块最上面
				ZCAuditZnsbSelfmachinemodule result = moduleService.find(parentmoduleguid).getResult();
				result.setModulename("全部");
				modulelist.add(0, result);
			}
			dataJson.put("totalcount", modulelist.size());
			dataJson.put("modulelist", modulelist);
			return JsonUtils.zwdtRestReturn("1", "", dataJson);
		} catch (JSONException e) {
			return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
		}

	}

	/**
	 * 获取工作台模板列表
	 * 
	 * @params params
	 * @return
	 * 
	 * 
	 */
	@RequestMapping(value = "/getworktablemodulelist", method = RequestMethod.POST)
	public String getWorkTableModuleList(@RequestBody String params) {
		try {
			JSONObject json = JSON.parseObject(params);
			JsonUtils.checkUserAuth(json.getString("token"));
			JSONObject obj = (JSONObject) json.get("params");
			JSONObject dataJson = new JSONObject();
			String centerguid = obj.getString("centerguid");
			String macaddress = obj.getString("macaddress");
			String moduleconfigtype = obj.getString("moduleconfigtype");
			List<ZCAuditZnsbSelfmachinemodule> typemodulelist = new ArrayList<>();
			JSONArray moduleJsonlist = new JSONArray();
			// 对应的mac有个性化模块
			List<ZCAuditZnsbSelfmachinemodule> parentlist = moduleService
					.getParentListByConfigType(moduleconfigtype, centerguid).getResult();
			parentlist.addAll(moduleService.getParentListByConfigTypeAndMac(moduleconfigtype, centerguid, macaddress)
					.getResult());
			sortWorkTableListBySpeciallabelAndOrderNum(parentlist);
			for (ZCAuditZnsbSelfmachinemodule parentmodule : parentlist) {
				JSONObject moduleJson = new JSONObject();
				// 第一层模块
				moduleJson.put("typename", parentmodule.getModulename());
				moduleJson.put("typeguid", parentmodule.getRowguid());
				moduleJson.put("picurl", parentmodule.getPicturepath());
				moduleJson.put("pictureguid", parentmodule.getPictureguid());
				moduleJson.put("linenum", parentmodule.getLinenum());
				moduleJson.put("speciallabel", parentmodule.getSpeciallabel());
				moduleJson.put("parenturl", parentmodule.getHtmlurl());
				// 获取通用模块
				typemodulelist = moduleService.getModuleListByParentmoduleguid(parentmodule.getRowguid(), centerguid)
						.getResult();
				// 获取个性化模块
				typemodulelist.addAll(moduleService
						.getModuleListByParentmoduleguidAndMac(parentmodule.getRowguid(), centerguid, macaddress)
						.getResult());
				sortWorkTableListBySpeciallabelAndOrderNum(typemodulelist);
				// 遍历第二层模块，如果该模块下有第三层模块，就获取到第三层模块，并添加到该模块中
				for (ZCAuditZnsbSelfmachinemodule module : typemodulelist) {
					if (QueueConstant.CONSTANT_STR_ONE.equals(module.getIsParentmodule())) {
						// 第三层模块
						List<ZCAuditZnsbSelfmachinemodule> childrentypemodulelist = moduleService
								.getModuleListByParentmoduleguid(module.getRowguid(), centerguid).getResult();
						childrentypemodulelist.addAll(moduleService
								.getModuleListByParentmoduleguidAndMac(module.getRowguid(), centerguid, macaddress)
								.getResult());
						sortWorkTableListBySpeciallabelAndOrderNum(childrentypemodulelist);
						module.put("childrentypemodulelist",
								JSONArray.parseArray(JSON.toJSONString(childrentypemodulelist)));
					} else {
						module.put("childrentypemodulelist", "");
					}
				}

				moduleJson.put("typemodulelist", JSONArray.parseArray(JSON.toJSONString(typemodulelist)));
				moduleJsonlist.add(moduleJson);
			}
			dataJson.put("modulejsonlist", moduleJsonlist);
			return JsonUtils.zwdtRestReturn("1", "", dataJson);
		} catch (JSONException e) {
			return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
		}

	}

	/**
	 * 
	 * [工作台模块排序，根据特殊标签，和热门排序值来排序]
	 * 
	 * @param list
	 * @exception/throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public void sortWorkTableListBySpeciallabelAndHotNum(List<ZCAuditZnsbSelfmachinemodule> list) {
		Collections.sort(list, new Comparator<ZCAuditZnsbSelfmachinemodule>() {
			public int compare(ZCAuditZnsbSelfmachinemodule a, ZCAuditZnsbSelfmachinemodule b) {
				if (StringUtil.isNotBlank(a.getSpeciallabel()) && StringUtil.isBlank(b.getSpeciallabel())) {
					return -1;
				} else if (StringUtil.isBlank(a.getSpeciallabel()) && StringUtil.isNotBlank(b.getSpeciallabel())) {
					return 1;
				} else if (StringUtil.isNotBlank(a.getSpeciallabel()) && StringUtil.isNotBlank(b.getSpeciallabel())) {
					return b.getHotnum() - a.getHotnum();
				} else {
					return b.getHotnum() - a.getHotnum();
				}
			}
		});
	}

	/**
	 * 
	 * [工作台模块排序，根据特殊标签，和普通排序值来排序]
	 * 
	 * @param list
	 * @exception/throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public void sortWorkTableListBySpeciallabelAndOrderNum(List<ZCAuditZnsbSelfmachinemodule> list) {
		Collections.sort(list, new Comparator<ZCAuditZnsbSelfmachinemodule>() {
			public int compare(ZCAuditZnsbSelfmachinemodule a, ZCAuditZnsbSelfmachinemodule b) {
				if (StringUtil.isNotBlank(a.getSpeciallabel()) && StringUtil.isBlank(b.getSpeciallabel())) {
					return -1;
				} else if (StringUtil.isBlank(a.getSpeciallabel()) && StringUtil.isNotBlank(b.getSpeciallabel())) {
					return 1;
				} else if (StringUtil.isNotBlank(a.getSpeciallabel()) && StringUtil.isNotBlank(b.getSpeciallabel())) {
					return b.getOrdernum() - a.getOrdernum();
				} else {
					return b.getOrdernum() - a.getOrdernum();
				}
			}
		});
	}

	/**
	 * 获取工作台模板列表
	 * 
	 * @params params
	 * @return
	 * 
	 * 
	 */
	@RequestMapping(value = "/gethotworktablemodulelist", method = RequestMethod.POST)
	public String getHotWorkTableModuleList(@RequestBody String params) {
		try {
			JSONObject json = JSON.parseObject(params);
			JsonUtils.checkUserAuth(json.getString("token"));
			JSONObject obj = (JSONObject) json.get("params");
			JSONObject dataJson = new JSONObject();
			String centerguid = obj.getString("centerguid");
			String macaddress = obj.getString("macaddress");
			String moduleconfigtype = obj.getString("moduleconfigtype");
			List<ZCAuditZnsbSelfmachinemodule> typemodulelist = new ArrayList<>();
			JSONArray moduleJsonlist = new JSONArray();
			// 对应的mac有个性化模块
			List<ZCAuditZnsbSelfmachinemodule> parentlist = moduleService
					.getParentListByConfigType(moduleconfigtype, centerguid).getResult();
			parentlist.addAll(moduleService.getParentListByConfigTypeAndMac(moduleconfigtype, centerguid, macaddress)
					.getResult());
			sortWorkTableListBySpeciallabelAndHotNum(parentlist);
			for (ZCAuditZnsbSelfmachinemodule parentmodule : parentlist) {
				JSONObject moduleJson = new JSONObject();
				// 第一层模块
				moduleJson.put("typename", parentmodule.getModulename());
				moduleJson.put("typeguid", parentmodule.getRowguid());
				moduleJson.put("picurl", parentmodule.getPicturepath());
				moduleJson.put("pictureguid", parentmodule.getPictureguid());
				moduleJson.put("linenum", parentmodule.getLinenum());
				moduleJson.put("speciallabel", parentmodule.getSpeciallabel());
				moduleJson.put("parenturl", parentmodule.getHtmlurl());
				// 获取通用模块
				typemodulelist = moduleService.getHotModuleListByParentmoduleguid(parentmodule.getRowguid(), centerguid)
						.getResult();
				// 获取个性化模块
				typemodulelist.addAll(moduleService
						.getHotModuleListByParentmoduleguidAndMac(parentmodule.getRowguid(), centerguid, macaddress)
						.getResult());
				sortWorkTableListBySpeciallabelAndHotNum(typemodulelist);
				// 遍历第二层模块，如果该模块下有第三层模块，就获取到第三层模块，并添加到该模块中
				for (ZCAuditZnsbSelfmachinemodule module : typemodulelist) {
					if (QueueConstant.CONSTANT_STR_ONE.equals(module.getIsParentmodule())) {
						// 第三层模块
						List<ZCAuditZnsbSelfmachinemodule> childrentypemodulelist = moduleService
								.getHotModuleListByParentmoduleguid(module.getRowguid(), centerguid).getResult();
						childrentypemodulelist.addAll(moduleService
								.getHotModuleListByParentmoduleguidAndMac(module.getRowguid(), centerguid, macaddress)
								.getResult());
						sortWorkTableListBySpeciallabelAndHotNum(childrentypemodulelist);
						module.put("childrentypemodulelist",
								JSONArray.parseArray(JSON.toJSONString(childrentypemodulelist)));
					} else {
						module.put("childrentypemodulelist", "");
					}
				}
				moduleJson.put("typemodulelist", JSONArray.parseArray(JSON.toJSONString(typemodulelist)));
				moduleJsonlist.add(moduleJson);
			}
			dataJson.put("modulejsonlist", moduleJsonlist);
			return JsonUtils.zwdtRestReturn("1", "", dataJson);
		} catch (JSONException e) {
			return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
		}

	}

	/**
	 * 根据应用名称模糊查询出个数
	 * 
	 * @params params
	 * @return
	 * 
	 * 
	 */
	@RequestMapping(value = "/getworktablemodulesearchlistcount", method = RequestMethod.POST)
	public String getWorkTableModuleSearchListCount(@RequestBody String params) {
		try {
			JSONObject json = JSON.parseObject(params);
			JsonUtils.checkUserAuth(json.getString("token"));
			JSONObject obj = (JSONObject) json.get("params");
			JSONObject dataJson = new JSONObject();
			String centerguid = obj.getString("centerguid");
			String macaddress = obj.getString("macaddress");
			String modulename = obj.getString("modulename");
			String moduleconfigtype = obj.getString("moduleconfigtype");
			// Integer totalcount =
			// moduleService.getCountByModuleName(centerguid, macaddress,
			// moduleconfigtype,modulename).getResult();

			List<ZCAuditZnsbSelfmachinemodule> modulelist = new ArrayList<>();
			// 对应的mac有个性化模块
			List<ZCAuditZnsbSelfmachinemodule> parentlist = moduleService
					.getParentListByConfigType(moduleconfigtype, centerguid).getResult();
			parentlist.addAll(moduleService.getParentListByConfigTypeAndMac(moduleconfigtype, centerguid, macaddress)
					.getResult());
			for (ZCAuditZnsbSelfmachinemodule parentmodule : parentlist) {
				// 获取通用模块
				List<ZCAuditZnsbSelfmachinemodule> secondmodulelist = moduleService
						.getModuleListByParentmoduleguid(parentmodule.getRowguid(), centerguid).getResult();
				// 获取个性化模块
				secondmodulelist.addAll(moduleService
						.getModuleListByParentmoduleguidAndMac(parentmodule.getRowguid(), centerguid, macaddress)
						.getResult());
				// 遍历第二层模块，如果该模块下有第三层模块，就获取到第三层模块，并添加到该模块中
				boolean ishavethreemodule = false;
				for (ZCAuditZnsbSelfmachinemodule module : modulelist) {
					if (QueueConstant.CONSTANT_STR_ONE.equals(module.getIsParentmodule())) {
						ishavethreemodule = true;
						break;
					}
				}
				if (ishavethreemodule) {
					// 如果有第三层，那么第二层不会添加到模块list中
					for (ZCAuditZnsbSelfmachinemodule module : modulelist) {
						List<ZCAuditZnsbSelfmachinemodule> threemodulelist = moduleService
								.getModuleListByParentmoduleguid(module.getRowguid(), centerguid).getResult();
						threemodulelist.addAll(moduleService
								.getModuleListByParentmoduleguidAndMac(module.getRowguid(), centerguid, macaddress)
								.getResult());
						modulelist.addAll(threemodulelist);
					}
				} else {
					modulelist.addAll(secondmodulelist);
				}

			}
			int totalcount = 0;
			if (StringUtil.isNotBlank(modulename)) {
				for (ZCAuditZnsbSelfmachinemodule module : modulelist) {
					if (module.getModulename().indexOf(modulename) != -1) {
						totalcount++;
					}
				}
			} else {
				totalcount = modulelist.size();
			}

			dataJson.put("totalcount", totalcount);
			return JsonUtils.zwdtRestReturn("1", "", dataJson);
		} catch (JSONException e) {
			return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
		}

	}

	/**
	 * 获取工作台模板列表
	 * 
	 * @params params
	 * @return
	 * 
	 * 
	 */
	@RequestMapping(value = "/getworktablemodulesearchlist", method = RequestMethod.POST)
	public String getWorkTableModuleSearchList(@RequestBody String params) {
		try {
			JSONObject json = JSON.parseObject(params);
			JsonUtils.checkUserAuth(json.getString("token"));
			JSONObject obj = (JSONObject) json.get("params");
			JSONObject dataJson = new JSONObject();
			String centerguid = obj.getString("centerguid");
			String macaddress = obj.getString("macaddress");
			String modulename = obj.getString("modulename");
			String currentPage = obj.getString("currentPage");
			String pageSize = obj.getString("pageSize");
			String moduleconfigtype = obj.getString("moduleconfigtype");
			List<JSONObject> moduleJsonlist = new ArrayList<JSONObject>();

			List<ZCAuditZnsbSelfmachinemodule> modulelist = new ArrayList<>();
			// 对应的mac有个性化模块
			List<ZCAuditZnsbSelfmachinemodule> parentlist = moduleService
					.getParentListByConfigType(moduleconfigtype, centerguid).getResult();
			parentlist.addAll(moduleService.getParentListByConfigTypeAndMac(moduleconfigtype, centerguid, macaddress)
					.getResult());
			for (ZCAuditZnsbSelfmachinemodule parentmodule : parentlist) {
				// 获取通用模块
				List<ZCAuditZnsbSelfmachinemodule> secondmodulelist = moduleService
						.getModuleListByParentmoduleguid(parentmodule.getRowguid(), centerguid).getResult();
				// 获取个性化模块
				secondmodulelist.addAll(moduleService
						.getModuleListByParentmoduleguidAndMac(parentmodule.getRowguid(), centerguid, macaddress)
						.getResult());
				// 遍历第二层模块，如果该模块下有第三层模块，就获取到第三层模块，并添加到该模块中
				boolean ishavethreemodule = false;
				for (ZCAuditZnsbSelfmachinemodule module : secondmodulelist) {
					if (QueueConstant.CONSTANT_STR_ONE.equals(module.getIsParentmodule())) {
						ishavethreemodule = true;
						break;
					}
				}
				if (ishavethreemodule) {
					// 如果有第三层，那么第二层不会添加到模块list中
					for (ZCAuditZnsbSelfmachinemodule module : secondmodulelist) {
						List<ZCAuditZnsbSelfmachinemodule> threemodulelist = moduleService
								.getModuleListByParentmoduleguid(module.getRowguid(), centerguid).getResult();
						threemodulelist.addAll(moduleService
								.getModuleListByParentmoduleguidAndMac(module.getRowguid(), centerguid, macaddress)
								.getResult());
						modulelist.addAll(threemodulelist);
					}
				} else {
					modulelist.addAll(secondmodulelist);
				}

			}
			if (StringUtil.isNotBlank(modulename)) {
				for (ZCAuditZnsbSelfmachinemodule module : modulelist) {
					JSONObject moduleJson = new JSONObject();
					if (module.getModulename().indexOf(modulename) != -1) {
						moduleJson.put("modulename", module.getModulename());
						moduleJson.put("moduelguid", module.getRowguid());
						moduleJson.put("htmlurl", module.getHtmlurl());
						moduleJson.put("picturepath", module.getPicturepath());
						moduleJson.put("pictureguid", module.getPictureguid());
						moduleJson.put("isneedlogin", module.getIsneedlogin());
						moduleJson.put("linenum", module.getLinenum());
						moduleJsonlist.add(moduleJson);
					}
				}
			} else {
				for (ZCAuditZnsbSelfmachinemodule module : modulelist) {
					JSONObject moduleJson = new JSONObject();
					moduleJson.put("modulename", module.getModulename());
					moduleJson.put("moduelguid", module.getRowguid());
					moduleJson.put("htmlurl", module.getHtmlurl());
					moduleJson.put("picturepath", module.getPicturepath());
					moduleJson.put("pictureguid", module.getPictureguid());
					moduleJson.put("isneedlogin", module.getIsneedlogin());
					moduleJson.put("linenum", module.getLinenum());
					moduleJsonlist.add(moduleJson);
				}
			}
			// 截取对应页面的modulelist数据
			int firstint = Integer.parseInt(currentPage) * Integer.parseInt(pageSize);
			int endint = (firstint + Integer.parseInt(pageSize)) >= moduleJsonlist.size() ? moduleJsonlist.size()
					: (firstint + Integer.parseInt(pageSize));
			List<JSONObject> rtnlist = moduleJsonlist.subList(firstint, endint);
			dataJson.put("modulejsonlist", rtnlist);
			dataJson.put("totalcount", StringUtil.getNotNullString(moduleJsonlist.size()));
			return JsonUtils.zwdtRestReturn("1", "", dataJson);
		} catch (JSONException e) {
			return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
		}

	}

	/**
	 * 根据模块类别动态获取模板列表
	 * 
	 * @params params
	 * @return
	 * 
	 * 
	 */
	@RequestMapping(value = "/getmodulelistbytype", method = RequestMethod.POST)
	public String getModuleListByType(@RequestBody String params) {
		try {
			JSONObject json = JSON.parseObject(params);
			JsonUtils.checkUserAuth(json.getString("token"));
			JSONObject obj = (JSONObject) json.get("params");
			JSONObject dataJson = new JSONObject();
			String centerguid = obj.getString("centerguid");
			String macaddress = obj.getString("macaddress");
			String type = obj.getString("type");
			String moduleconfigtype = obj.getString("moduleconfigtype");

			List<ZCAuditZnsbSelfmachinemodule> typemodulelist = new ArrayList<>();
			if (StringUtil.isNotBlank(moduleconfigtype)) {
				typemodulelist = moduleService
						.getModuleListByModuleTypeAndMac(type, centerguid, macaddress, moduleconfigtype).getResult();
				if (typemodulelist == null || typemodulelist.isEmpty()) {
					typemodulelist = moduleService
							.getModuleListByModuleTypeAndConfig(type, centerguid, moduleconfigtype).getResult();
				}
			} else {
				typemodulelist = moduleService.getModuleListByModuleTypeAndMac(type, centerguid, macaddress)
						.getResult();
				if (typemodulelist == null || typemodulelist.isEmpty()) {
					typemodulelist = moduleService.getModuleListByModuleType(type, centerguid).getResult();
				}
			}

			dataJson.put("modulelist", typemodulelist);
			return JsonUtils.zwdtRestReturn("1", "", dataJson);
		} catch (JSONException e) {
			return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
		}

	}

	/**
	 * 获取热门模块
	 * 
	 * @params params
	 * @return
	 * 
	 * 
	 */
	@RequestMapping(value = "/gethotmodulelist", method = RequestMethod.POST)
	public String getHotModuleList(@RequestBody String params) {
		try {
			JSONObject json = JSON.parseObject(params);
			JsonUtils.checkUserAuth(json.getString("token"));
			JSONObject obj = (JSONObject) json.get("params");
			JSONObject dataJson = new JSONObject();
			String centerguid = obj.getString("centerguid");
			String macaddress = obj.getString("macaddress");
			int modulecount = Integer.valueOf(obj.getString("modulecount"));

			// 热门模块排序方式系统参数AS_ZNSB_HOTMODULE: 0为按点击量排序(排序由job完成) 1为手动配置热门排序值
			// 默认方式为0
			// String hotmoduleconfig =
			// handleConfigservice.getFrameConfig("AS_ZNSB_HOTMODULE",
			// centerguid).getResult();
			List<ZCAuditZnsbSelfmachinemodule> typemodulelist = new ArrayList<>();
			typemodulelist = moduleService.getHotModuleList(centerguid, macaddress, modulecount).getResult();

			dataJson.put("modulelist", typemodulelist);
			return JsonUtils.zwdtRestReturn("1", "", dataJson);
		} catch (JSONException e) {
			return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
		}

	}

	/**
	 * 获取模板查询总数
	 * 
	 * @params params
	 * @return
	 * 
	 * 
	 */
	@RequestMapping(value = "/getmodulesearchcount", method = RequestMethod.POST)
	public String getModuleSearchCount(@RequestBody String params) {
		try {
			JSONObject json = JSON.parseObject(params);
			JsonUtils.checkUserAuth(json.getString("token"));
			JSONObject obj = (JSONObject) json.get("params");
			JSONObject dataJson = new JSONObject();
			String centerguid = obj.getString("centerguid");
			String macaddress = obj.getString("macaddress");
			String modulename = obj.getString("modulename");

			List<ZCAuditZnsbSelfmachinemodule> alllist = moduleService.getModuleListByMacaddress(macaddress, centerguid)
					.getResult();
			int modulelistcount = 0;
			if (alllist != null && !alllist.isEmpty()) {
				List<ZCAuditZnsbSelfmachinemodule> searchlist = moduleService
						.getModuleListByMacaddressAndName(macaddress, centerguid, modulename).getResult();
				modulelistcount = searchlist.size();
			} else {
				List<ZCAuditZnsbSelfmachinemodule> searchlist = moduleService.getModuleListByName(centerguid, modulename)
						.getResult();
				modulelistcount = searchlist.size();
			}

			dataJson.put("modulelistcount", modulelistcount);
			return JsonUtils.zwdtRestReturn("1", "", dataJson);
		} catch (JSONException e) {
			return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
		}

	}

	/**
	 * 获取模板查询list
	 * 
	 * @params params
	 * @return
	 * 
	 * 
	 */
	@RequestMapping(value = "/getmodulesearchlist", method = RequestMethod.POST)
	public String getModuleSearchList(@RequestBody String params) {
		try {
			JSONObject json = JSON.parseObject(params);
			JsonUtils.checkUserAuth(json.getString("token"));
			JSONObject obj = (JSONObject) json.get("params");
			JSONObject dataJson = new JSONObject();
			String centerguid = obj.getString("centerguid");
			String macaddress = obj.getString("macaddress");
			String modulename = obj.getString("modulename");

			List<ZCAuditZnsbSelfmachinemodule> alllist = moduleService.getModuleListByMacaddress(macaddress, centerguid)
					.getResult();
			List<ZCAuditZnsbSelfmachinemodule> searchlist;
			if (alllist != null && !alllist.isEmpty()) {
				searchlist = moduleService.getModuleListByMacaddressAndName(macaddress, centerguid, modulename)
						.getResult();
			} else {
				searchlist = moduleService.getModuleListByName(centerguid, modulename).getResult();
			}

			dataJson.put("modulelist", searchlist);
			return JsonUtils.zwdtRestReturn("1", "", dataJson);
		} catch (JSONException e) {
			return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
		}

	}

	/**
	 * 获取个性化文件夹
	 * 
	 * @params params
	 * @return
	 * 
	 * 
	 */
	@RequestMapping(value = "/getprojectpagepath", method = RequestMethod.POST)
	public String getprojectPagePath(@RequestBody String params) {
		try {
			JSONObject json = JSON.parseObject(params);
			JsonUtils.checkUserAuth(json.getString("token"));
			JSONObject dataJson = new JSONObject();
			JSONObject param = json.getJSONObject("params");
			String centerguid =  param.getString("centerguid");
			String macaddress =  param.getString("macaddress");
            String noneedwaitecord = "0";
            String macAddresses = handleConfigservice.getFrameConfig("AS_ZNSB_NONEEDWRITECORD", centerguid).getResult();
            if(StringUtil.isNotBlank(macAddresses) && macAddresses.indexOf(macaddress) != -1){
                noneedwaitecord = "1";
            }
			dataJson.put("projectpagepath", ConfigUtil.getConfigValue("projectPagePath"));
			dataJson.put("noneedwaitecord", noneedwaitecord);
			return JsonUtils.zwdtRestReturn("1", "", dataJson);

		} catch (JSONException e) {
			return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
		}
	}

	/**
	 * 获取 区域list
	 * 
	 * @params params
	 * @return
	 * 
	 * 
	 */
	@RequestMapping(value = "/getregionlist", method = RequestMethod.POST)
	public String getRegionList(@RequestBody String params) {
		try {
			JSONObject json = JSON.parseObject(params);
			JsonUtils.checkUserAuth(json.getString("token"));
			JSONObject obj = (JSONObject) json.get("params");
			String level = obj.getString("level");
			String parentguid = obj.getString("parentguid");
			JSONObject dataJson = new JSONObject();
			List<AuditZnsbSelfmachineregion> regionlist = null;
			if (StringUtil.isNotBlank(parentguid)) {
				regionlist = regionServcie.getRegionListByLevelAndParent(level, parentguid).getResult();
			} else {
				regionlist = regionServcie.getRegionListByLevel(level).getResult();
			}

			dataJson.put("regionlist", regionlist);
			return JsonUtils.zwdtRestReturn("1", "", dataJson);
		} catch (JSONException e) {
			return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
		}

	}

	/**
	 * 获取 sm2公钥
	 * 
	 * @params params
	 * @return
	 * 
	 * 
	 */
	@RequestMapping(value = "/getsm2pubkey", method = RequestMethod.POST)
	public String getSm2PubKey(@RequestBody String params) {
		try {
			JSONObject json = JSON.parseObject(params);
			JsonUtils.checkUserAuth(json.getString("token"));
			SM2ClientUtil sm2Util = SM2ClientUtil.getInstance();

			JSONObject dataJson = new JSONObject();
			dataJson.put("sm2pubkey", sm2Util.getSm2PubKey());
			return JsonUtils.zwdtRestReturn("1", "", dataJson);
		} catch (JSONException e) {
			return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
		}

	}

	// /**
	// * 语音识别调试接口
	// *
	// * @params params
	// * @return
	// *
	// *
	// */
	// @RequestMapping(value = "/sendYYSB", method = RequestMethod.POST)
	// public String sendYYSB(@RequestBody String params) {
	// try {
	// JSONObject json = JSON.parseObject(params);
	// JsonUtils.checkUserAuth(json.getString("token"));
	// JSONObject obj = (JSONObject) json.get("params");
	// String savePath = obj.getString("savePath");
	// String Filename = obj.getString("Filename");
	// String UserId = obj.getString("UserId");
	// String sourl = obj.getString("sourl");
	// JSONObject dataJson = new JSONObject();
	//
	// //system.out.println("快鱼接口测试开始");
	//
	//
	//// String
	// savePath="D:/拷贝/2019_08_01到2019_10_31/语音识别相关/20191107_115729.mp3";
	//// String Filename="20191107_115729.mp3";
	//// String UserId="123";
	// ASRFactory factory = ASRFactory.getInstance();
	// factory.initHost("192.168.167.99",13903);
	// factory.initLibConfig(1, sourl);
	// ASRClientListener asrClientListener = new ASRClientListener();
	// asrClientListener.setFilename(Filename);
	// asrClientListener.setSavePath(savePath);
	// asrClientListener.setUserId(UserId);
	//// ASRListener asrClientListener2 = (ASRListener) asrClientListener;
	// // 创建客户端
	// ASRClient client = factory.getAsrClient("sdkid1",
	// asrClientListener,"12345678901234567890123456789012","12345678901234567890123456789012",7,"",
	// 3, 3, true, 1);
	//// ASRClientListener
	// asrClientListener3=(ASRClientListener)asrClientListener2;
	// asrClientListener.setClient(client);
	// client.getASRToken();
	// // 发送音频数据
	// String filePath = savePath + "/" + Filename;
	// try {
	// File file = new File(filePath);
	// if (file.exists()) {
	// FileInputStream fis = new FileInputStream(file);
	// byte[] b = new byte[1600];
	// int n;
	// int result;
	// while ((n = fis.read(b)) != -1) {
	// if (n < b.length) {
	// byte[] tempData = new byte[n];
	// for (int i = 0; i < n; i++) {
	// tempData[i] = b[i];
	// }
	// result = client.sendASRData(tempData);
	// }
	// else {
	// result = client.sendASRData(b);
	// }
	// if (result != 0) {
	// //system.out.println("发送数据异常：" + result);
	// break;
	// }
	// //system.out.println("用户：" + UserId + "发送音频数据成功,size:" + n);
	// Thread.sleep(1);
	// }
	// fis.close();
	// // 发送结束包
	// client.getFinalASRResult();
	// }
	// else {
	//
	// //system.out.println("音频文件为空");
	// client.recoveryToken();
	// //system.out.println("用户：" + UserId + "回收令牌");
	// }
	// }
	// catch (Exception e) {
	// //system.out.println("用户：" + UserId + "识别捕获异常");
	// e.printStackTrace();
	// }
	//
	//
	//
	//
	// return JsonUtils.zwdtRestReturn("1", "", dataJson);
	// } catch (JSONException e) {
	// return JsonUtils.zwdtRestReturn("0", "出现异常：" + e.getMessage(), "");
	// }
	//
	// }

}
