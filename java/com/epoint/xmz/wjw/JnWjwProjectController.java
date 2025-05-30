
package com.epoint.xmz.wjw;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.authenticator.identity.Authenticator;
import com.epoint.authenticator.module.OAuthCheckTokenInfo;
import com.epoint.authenticator.utils.CheckTokenUtil;
import com.epoint.basic.auditonlineuser.auditonlineproject.inter.IAuditOnlineProject;
import com.epoint.basic.auditonlineuser.auditonlineregister.domain.AuditOnlineRegister;
import com.epoint.basic.auditonlineuser.auditonlineregister.inter.IAuditOnlineRegister;
import com.epoint.basic.auditproject.auditproject.domain.AuditProject;
import com.epoint.basic.auditproject.auditproject.inter.IAuditProject;
import com.epoint.basic.auditproject.auditprojectmaterial.domain.AuditProjectMaterial;
import com.epoint.basic.auditproject.auditprojectmaterial.inter.IAuditProjectMaterial;
import com.epoint.common.util.JsonUtils;
import com.epoint.common.util.SqlConditionUtil;
import com.epoint.common.zwdt.authentication.ZwdtUserSession;
import com.epoint.core.grammar.Record;
import com.epoint.core.utils.code.Base64Util;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.date.EpointDateUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.frame.service.attach.api.IAttachService;
import com.epoint.frame.service.attach.entity.FrameAttachInfo;
import com.epoint.frame.service.attach.entity.FrameAttachStorage;
import com.epoint.intermediary.sendmaterials.api.ISendMaterials;
import com.epoint.xmz.wjw.api.ICxBusService;

@RestController
@RequestMapping("/jnwjw")
public class JnWjwProjectController {
	/**
	 * 日志
	 */
	private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());

	public static final String wjwzurl = ConfigUtil.getConfigValue("datasyncjdbc", "wjwzurl");
	public static final String clientId = ConfigUtil.getConfigValue("datasyncjdbc", "wjwclientId");
	public static final String key = ConfigUtil.getConfigValue("datasyncjdbc", "wjwkey");
	public static final String clientSecret = ConfigUtil.getConfigValue("datasyncjdbc", "wjwclientSecret");

	@Autowired
	private IAttachService attachService;

	/**
	 * 办件材料API
	 */
	@Autowired
	private IAuditProjectMaterial iAuditProjectMaterial;

	@Autowired
	private ISendMaterials sendMaterials;

	@Autowired
	private ICxBusService iCxBusService;

	@Autowired
	private IAuditProject iAuditProject;

	@Autowired
	private IAuditOnlineProject iAuditOnlineProject;

	/**
	 * 政务大厅注册用户API
	 */
	@Autowired
	private IAuditOnlineRegister iAuditOnlineRegister;

	public String uploadAttach(String dataid, String attachguid, String fieldname, String fieldzname) {
		try {
			log.info("=======开始调用wjwuploadAttach接口=======");
			// 1、接口的入参转化为JSON对象
			String type = "JN0008";

			FrameAttachStorage attachStorage = attachService.getAttach(attachguid);
			Map<String, Object> datajson = new HashMap<String, Object>();
			datajson.put("type", type);
			datajson.put("dataid", dataid);
			datajson.put("fieldname", fieldname);
			datajson.put("fieldzname", fieldzname);
			Map<String, String> result = CommUtil.sendMultipartFilePost(
					"http://ga.jiningdq.cn/jiningwjw/memberClient/commonfastdfs/UploadFile", attachStorage.getContent(),
					datajson, attachStorage.getAttachFileName(), 3000);
			String data = result.get("data");
			log.info("wjw上传附件接口返回结果：" + data);
			JSONObject dataresult = JSONObject.parseObject(data);
			if ("true".equals(dataresult.getString("success"))) {
				JSONArray files = dataresult.getJSONArray("list");
				JSONObject filetail = files.getJSONObject(0);
				return filetail.getString("id");
			} else {
				return "error";
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}

	}

	/**
	 * 添加申请信息
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/addApply", method = RequestMethod.POST)
	public String addApply(@RequestBody String params) {
		try {
			log.info("=======开始调用addApply接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			String projectguid = jsonObject.getString("projectguid");
			String memid = jsonObject.getString("dhuuid");
			String fformid = jsonObject.getString("formid");
			String servicename = "addAllowroad";
			String channel = "jnxzspj";
				log.info("wjwaddApply入参：" + jsonObject);
				// 8、定义返回JSON对象

				
				if (StringUtil.isBlank(memid)) {
					return JsonUtils.zwdtRestReturn("0", "用户的UUID未获取到", "");
				}

				AuditProject auditProject = sendMaterials.getAuditProjectByRowguid(projectguid);

				if (auditProject == null) {
					return JsonUtils.zwdtRestReturn("0", "该办件未查询到", "");
				}

				MultiValueMap map = new LinkedMultiValueMap();
				map.add("servicename", servicename);
				map.add("dataid", projectguid);
				map.add("memid", memid);
				map.add("channel", channel);

				String formid = "28";
				Record record = null;
				if ("194".equals(fformid)) {
					record = iCxBusService.getHdzzl10tyszxhcByRowguid(projectguid);
					record.set("txld", record.getStr("dxklb2"));
					formid = "28";
				} else if ("195".equals(fformid)) {
					record = iCxBusService.getHdzzl5tys10tyxzxhcByRowguid(projectguid);
					formid = "27";
				} else if ("196".equals(fformid)) {
					record = iCxBusService.getZxhcByRowguid(projectguid);
					formid = "26";
				} else if ("197".equals(fformid)) {
					record = iCxBusService.getWxpyscByRowguid(projectguid);
					formid = "15";
				} else if ("198".equals(fformid)) {
					record = iCxBusService.getCsgccByRowguid(projectguid);
					formid = "14";
				} else if ("199".equals(fformid)) {
					record = iCxBusService.getQxhcByRowguid(projectguid);
					formid = "25";
				}

				if (record == null) {
					return JsonUtils.zwdtRestReturn("0", "未找到该表单数据", "");
				}

				log.info("record:" + record);
				// 申请人姓名
				map.add("name", record.getStr("sqr"));
				// 身份证号码
				map.add("idcard", record.getStr("sfzh"));
				// 联系电话
				map.add("mobile", record.getStr("lxdh"));
				// 通行区域
				map.add("areacode", "370800");
				// 车辆号牌
				map.add("clhp", record.getStr("cph"));
				// 通行证类型
				map.add("txztype", formid);
				// 申请路段
				map.add("txroad", record.getStr("txld"));
				// 申请时间段
				map.add("txtimes", record.getStr("txsjd"));
				// 开始时间
				map.add("starttime",
						EpointDateUtil.convertDate2String(record.getDate("kssj"), EpointDateUtil.DATE_FORMAT));
				// 结束时间
				map.add("endtime",
						EpointDateUtil.convertDate2String(record.getDate("jssj"), EpointDateUtil.DATE_FORMAT));
				// 出发地
				// map.put("cfd", record.getStr("sqr"));
				// 目的地
				// map.put("mdd", record.getStr("sqr"));

				SqlConditionUtil materialSqlConditionUtil = new SqlConditionUtil();
				materialSqlConditionUtil.eq("projectguid", projectguid);
				List<AuditProjectMaterial> auditProjectMaterials = iAuditProjectMaterial
						.selectProjectMaterialByCondition(materialSqlConditionUtil.getMap()).getResult();

				log.info("获取到的材料信息为：" + auditProjectMaterials);
				boolean inattach = true;
				boolean uploadresult = false;
				String xszzpid = "";
				for (AuditProjectMaterial material : auditProjectMaterials) {
					if ("行驶证照片".equals(material.getTaskmaterial())) {
						List<FrameAttachInfo> attachs = attachService.getAttachInfoListByGuid(material.getCliengguid());
						if (attachs.size() != 0) {
							inattach = false;
						}
						for (FrameAttachInfo attach : attachs) {
							String result = uploadAttach(projectguid, attach.getAttachGuid(), "xszzp", "行驶证照片");
							if ("error".equals(result)) {
								uploadresult = true;
								break;
							}
							xszzpid += result + ",";
						}
						xszzpid = xszzpid.substring(0, xszzpid.length() - 1);
					}
				}

				if (inattach) {
					return JsonUtils.zwdtRestReturn("0", "办件材料不存在！", "");
				}
				if (uploadresult) {
					return JsonUtils.zwdtRestReturn("0", "办件附件上传失败", "");
				}

				// 行驶证照片
				map.add("xszzp", xszzpid);

				// 驾驶证照片
				// map.put("jszzp", record.getStr("sqr"));
				// 车辆照片
				// map.put("clap", record.getStr("sqr"));
				
				log.info("接口申请信息如下："+map);
				Map<String,Object> result = sendPost(map);
				log.info("返回结果如下："+result);

				
				if ("true".equals(result.get("success").toString())) {
					Map<String, String> updateFieldMap = new HashMap<>();
					Map<String, String> updateDateFieldMap = new HashMap<String, String>();
					updateFieldMap.put("wjwdataid=", result.get("id").toString());
					SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
					sqlConditionUtil.eq("sourceguid", projectguid);
					iAuditOnlineProject.updateOnlineProject(updateFieldMap, updateDateFieldMap,
							sqlConditionUtil.getMap());
					return JsonUtils.zwdtRestReturn("1", "添加申请信息成功", result.get("id").toString());
				} else {
					return JsonUtils.zwdtRestReturn("0", "添加申请信息失败", "");
				}

		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======addApply接口参数：params【" + params + "】=======");
			log.info("=======addApply异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "添加申请信息失败：" + e.getMessage(), "");
		}
	}

	/**
	 * 获取签章信息结果
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/getReuslt", method = RequestMethod.POST)
	public String getReuslt(@RequestBody String params) {
		try {
			log.info("=======开始调用getReuslt接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			log.info("getReuslt入参：" + jsonObject);
			// 8、定义返回JSON对象
			JSONObject dataJson = new JSONObject();
			String dataid = jsonObject.getString("dataid");
			String projectguid = jsonObject.getString("projectguid");

			AuditProject auditProject = iAuditProject.getAuditProjectByRowGuid(projectguid, "370800").getResult();

			if (auditProject == null) {
				return JsonUtils.zwdtRestReturn("0", "办件信息为空", "");
			}

			MultiValueMap map = new LinkedMultiValueMap();
			map.add("servicename", "getAllowroad");
			map.add("id", dataid);
			map.add("areacode", "370800");

			Map<String,Object> result = sendPost(map);
			
			if ("true".equals(result.get("success").toString())) {
				String status = result.get("status").toString();
				dataJson.put("status", status);
				if (StringUtil.isNotBlank(status)) {
					if ("20".equals(status)) {
						Map<String, String> updateFieldMap = new HashMap<>();
						Map<String, String> updateDateFieldMap = new HashMap<String, String>();
						updateFieldMap.put("status=", "90");
						updateFieldMap.put("Banjieresult=", "40");
						updateFieldMap.put("wjwsync=", "1");
						SqlConditionUtil sqlConditionUtil = new SqlConditionUtil();
						sqlConditionUtil.eq("sourceguid", projectguid);
						iAuditOnlineProject.updateOnlineProject(updateFieldMap, updateDateFieldMap,
								sqlConditionUtil.getMap());
						
						String attachcontent = result.get("file").toString(); 
		                byte[] pic = Base64Util.decodeBuffer(attachcontent);
		                String attachGuid = UUID.randomUUID().toString();
		                
		                if (pic.length > 0) {
		                    FrameAttachInfo frameAttachInfo = new FrameAttachInfo();
		                    frameAttachInfo.setAttachGuid(attachGuid);
		                    frameAttachInfo.setCliengGuid(auditProject.getRowguid());
		                    frameAttachInfo.setAttachFileName("通行证.pdf");
		                    frameAttachInfo.setCliengTag("微警务结果文件");
		                    frameAttachInfo.setUploadUserGuid("");
		                    frameAttachInfo.setUploadUserDisplayName("");
		                    frameAttachInfo.setUploadDateTime(new Date());
		                    frameAttachInfo.setContentType(".pdf");
		                    frameAttachInfo.setAttachLength(Long.valueOf((long) pic.length));
		                    ByteArrayInputStream input = new ByteArrayInputStream(pic);
		                    attachService.addAttach(frameAttachInfo, input);
		                    input.close();
		                }
						auditProject.setStatus(90);
						auditProject.setBanjieresult(40);
						iAuditProject.updateProject(auditProject);
						return JsonUtils.zwdtRestReturn("1", "办件办结", dataJson.toString());
					}else {
						return JsonUtils.zwdtRestReturn("0", "办件状态异常", status);
					}
				} else {
					return JsonUtils.zwdtRestReturn("0", "办件状态查询未办结", dataJson.toString());
				}
			} else {
				return JsonUtils.zwdtRestReturn("0", "查询信息失败", dataJson.toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======getReuslt接口参数：params【" + params + "】=======");
			log.info("=======getReuslt异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "查询办结信息结果失败：" + e.getMessage(), "");
		}
	}

	/**
	 * 获得服务器端的数据,以InputStream形式返回
	 * 
	 * @return
	 */
	public static InputStream getInputStream(String URLPATH, String header) {
		InputStream inputStream = null;
		HttpURLConnection httpURLConnection = null;
		try {
			URL url = new URL(URLPATH);
			if (url != null) {
				httpURLConnection = (HttpURLConnection) url.openConnection();
				// 设置连接网络的超时时间
				httpURLConnection.setConnectTimeout(3000);
				httpURLConnection.setDoInput(true);
				// 表示设置本次http请求使用GET方式请求
				httpURLConnection.setRequestMethod("GET");
				httpURLConnection.addRequestProperty("Authentication", header);
				int responseCode = httpURLConnection.getResponseCode();
				if (responseCode == 200) {
					// 从服务器获得一个输入流
					inputStream = httpURLConnection.getInputStream();
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inputStream;
	}

	/**
	 * 获取用户唯一标识
	 * 
	 * @param httpServletRequest
	 * @return
	 */
	private AuditOnlineRegister getOnlineRegister(HttpServletRequest httpServletRequest) {
		AuditOnlineRegister auditOnlineRegister;
		OAuthCheckTokenInfo oAuthCheckTokenInfo = CheckTokenUtil.getCheckTokenInfo(httpServletRequest);
		if (oAuthCheckTokenInfo != null) {
			// 手机端
			// 通过登录名获取用户
			auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(oAuthCheckTokenInfo.getLoginid())
					.getResult();
		} else {
			// PC端
			String accountGuid = ZwdtUserSession.getInstance("").getAccountGuid();
			if (StringUtil.isNotBlank(accountGuid)) {
				auditOnlineRegister = iAuditOnlineRegister.getRegisterByAccountguid(accountGuid).getResult();
			} else {
				// 通过登录名获取用户
				auditOnlineRegister = iAuditOnlineRegister.getRegisterByIdorMobile(Authenticator.getCurrentIdentity())
						.getResult();
			}
		}
		return auditOnlineRegister;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map<String,Object> sendPost(MultiValueMap map) {
		RestTemplate restTemplate = new RestTemplate();
		Map<String,Object> result = new HashMap<String,Object>();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("clientId", clientId);
		param.put("clientSecret", clientSecret);

		param.put("timestamp", System.currentTimeMillis());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("x-auth-client-id", clientId);
		headers.add("x-forwarded-for", "127.0.0.1");
		headers.add("x-auth-client-secret", CommUtil.encrypt(JSONObject.toJSONString(param), key));

		HttpEntity<MultiValueMap<String, String>> formEntity = new HttpEntity<>(map, headers);
		try {
			ResponseEntity<Map> responseEntity = restTemplate.postForEntity("http://ga.jiningdq.cn/api/zdfwApi",
					formEntity, Map.class);
			result = responseEntity.getBody();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;

	}

}
