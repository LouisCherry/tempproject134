	
package com.epoint.xmz.cxcltxz;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.cert.commonutils.HttpUtil;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.utils.HttpRequestUtil;

@RestController
@RequestMapping("/jncxcltxzdj")
public class JnCxcltxzDJController {
	/**
	 * 日志
	 */
	private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	public static final String cx_url= ConfigUtil.getConfigValue("epointframe", "cxurl");
	
	/**
	 * 获取提交超限通行证的accesstoken值
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 */
	@RequestMapping(value = "/getaccesstoken", method = RequestMethod.POST)
	public String getAccessToken(@RequestBody String params) {
		try {
			log.info("=======开始调用getAccessToken接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			// 8、定义返回JSON对象
			JSONObject dataJson = new JSONObject();
			Map<String, Object> bodyRequest = new HashMap<String, Object>();
			bodyRequest.put("grant_type", "password");
			bodyRequest.put("username", "jining");
			bodyRequest.put("password", "6edJ4USw8ORhXBDncOOoYw==");
			Map<String,String> headers = new HashMap<String,String>();
        	headers.put("Authorization", "Basic b1I0c3ZjdjMyOm9mLWN4UjAtbDkxY2EyMw==");
			String res = HttpUtil.doPost(cx_url+"/oauth/token", bodyRequest, headers);
			if (StringUtil.isNotBlank(res)) {
				JSONObject json = JSON.parseObject(res);
				String accesstoken = json.getString("access_token");
				if (StringUtil.isNotBlank(accesstoken)) {
					dataJson.put("accesstoken", accesstoken);
					log.info("=======结束调用getAccessToken接口=======");
					return JsonUtils.zwdtRestReturn("1", "获取acctss_token成功", dataJson.toString());
				}else {
					return JsonUtils.zwdtRestReturn("0", "获取acctss_token失败", dataJson.toString());
				}
			}else {
				return JsonUtils.zwdtRestReturn("0", "获取acctss_token失败", "");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======getAccessToken接口参数：params【" + params + "】=======");
			log.info("=======getAccessToken异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "获取acctss_token失败：" + e.getMessage(), "");
		}
	}
	
	
	/**
	 * 新增企业信息接口
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 */
	@RequestMapping(value = "/submitcompany", method = RequestMethod.POST)
	public String submitCompany(@RequestBody String params) {
		try {
			log.info("=======开始调用submitCompany接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			log.info("submitCompany入参："+jsonObject);
			// 8、定义返回JSON对象
			JSONObject dataJson = new JSONObject();
			String accesstoken = jsonObject.getString("accesstoken");
			 String json = jsonObject.getString("detail");
        	Map<String,String> headers = new HashMap<String,String>();
        	headers.put("Authorization", "Bearer "+accesstoken);
			String result = HttpUtil.doPostJson(cx_url+"/licapplicant", json, headers);
			if (StringUtil.isNotBlank(result)) {
				JSONObject jsonresult = JSON.parseObject(result);
				String data = jsonresult.getString("DATA");
				if ("SUCCESS".equals(data)) {
					log.info("=======结束调用submitCompany接口=======");
					return JsonUtils.zwdtRestReturn("1", "新增企业信息接口成功", result);
				}
				else if ("ERROR".equals(data)) {
					dataJson.put("desc", jsonresult.getString("DESC"));
					return JsonUtils.zwdtRestReturn("1", "推送进度信息成功", dataJson.toString());
				}
				else {
					return JsonUtils.zwdtRestReturn("0", "新增企业信息接口失败", result);
				}
			}else {
				return JsonUtils.zwdtRestReturn("0", "新增企业信息接口失败", "");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======submitCompany接口参数：params【" + params + "】=======");
			log.info("=======submitCompany异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "新增企业信息接口失败：" + e.getMessage(), "");
		}
	}
	
	
	
	/**
	 * 新增申请单接口
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 */
	@RequestMapping(value = "/addapply", method = RequestMethod.POST)
	public String addApply(@RequestBody String params) {
		try {
			log.info("=======开始调用addApply接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			log.info("addApply入参："+jsonObject);
			// 8、定义返回JSON对象
			JSONObject dataJson = new JSONObject();
			String accesstoken = jsonObject.getString("accesstoken");
			String json = jsonObject.getString("detail");
        	Map<String,String> headers = new HashMap<String,String>();
        	headers.put("Authorization", "Bearer "+accesstoken);
			String result = HttpUtil.doPostJson(cx_url+"/licrequest", json, headers);
			if (StringUtil.isNotBlank(result)) {
				JSONObject jsonresult = JSON.parseObject(result);
				String data = jsonresult.getString("DATA");
				if ("SUCCESS".equals(data)) {
					log.info("=======结束调用addApply接口=======");
					return JsonUtils.zwdtRestReturn("1", "新增申请单成功", result);
				}
				else if ("ERROR".equals(data)) {
					dataJson.put("desc", jsonresult.getString("DESC"));
					return JsonUtils.zwdtRestReturn("0", "推送进度信息成功", dataJson.toString());
				}
				else {
					return JsonUtils.zwdtRestReturn("0", "新增申请单失败", result);
				}
			}else {
				return JsonUtils.zwdtRestReturn("0", "新增申请单失败", "");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======addApply接口参数：params【" + params + "】=======");
			log.info("=======addApply异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "新增申请单失败：" + e.getMessage(), "");
		}
	}
	
	
	/**
	 * 新增车辆信息
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 */
	@RequestMapping(value = "/addbus", method = RequestMethod.POST)
	public String addBus(@RequestBody String params) {
		try {
			log.info("=======开始调用addBus接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			log.info("addBus入参："+jsonObject);
			// 8、定义返回JSON对象
			JSONObject dataJson = new JSONObject();
			String accesstoken = jsonObject.getString("accesstoken");
			String json = jsonObject.getString("detail");
        	Map<String,String> headers = new HashMap<String,String>();
        	headers.put("Authorization", "Bearer "+accesstoken);
			String result = HttpUtil.doPostJson(cx_url+"/licvehicle", json, headers);
			if (StringUtil.isNotBlank(result)) {
				JSONObject jsonresult = JSON.parseObject(result);
				String data = jsonresult.getString("DATA");
				if ("SUCCESS".equals(data)) {
					log.info("=======结束调用addBus接口=======");
					return JsonUtils.zwdtRestReturn("1", "新增车辆信息成功", result);
				}
				else if ("ERROR".equals(data)) {
					dataJson.put("desc", jsonresult.getString("DESC"));
					return JsonUtils.zwdtRestReturn("1", "推送进度信息成功", dataJson.toString());
				}
				else {
					return JsonUtils.zwdtRestReturn("0", "新增车辆信息失败", result);
				}
			}else {
				return JsonUtils.zwdtRestReturn("0", "新增车辆信息失败", "");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======addBus接口参数：params【" + params + "】=======");
			log.info("=======addBus异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "新增车辆信息失败：" + e.getMessage(), "");
		}
	}
	
	
	/**
	 * 新增车辆信息
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 */
	@RequestMapping(value = "/updatebus", method = RequestMethod.POST)
	public String updateBus(@RequestBody String params) {
		try {
			log.info("=======开始调用updatebus接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			log.info("updatebus入参："+jsonObject);
			// 8、定义返回JSON对象
			JSONObject dataJson = new JSONObject();
			String accesstoken = jsonObject.getString("accesstoken");
			String json = jsonObject.getString("detail");
        	String token = "Bearer "+accesstoken;
			String result = HttpRequestUtil.doPut(cx_url+"/licvehicle", token, json);
			if (StringUtil.isNotBlank(result)) {
				JSONObject jsonresult = JSON.parseObject(result);
				String data = jsonresult.getString("DATA");
				if ("SUCCESS".equals(data)) {
					log.info("=======结束调用addBus接口=======");
					return JsonUtils.zwdtRestReturn("1", "修改车辆信息成功", result);
				}
				else {
					return JsonUtils.zwdtRestReturn("0", "修改车辆信息失败", result);
				}
			}else {
				return JsonUtils.zwdtRestReturn("0", "修改车辆信息失败", "");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======updatebus接口参数：params【" + params + "】=======");
			log.info("=======updatebus异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "新增车辆信息失败：" + e.getMessage(), "");
		}
	}
	
	/**
	 * 新增车辆信息
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 */
	@RequestMapping(value = "/updateapply", method = RequestMethod.POST)
	public String updateApply(@RequestBody String params) {
		try {
			log.info("=======开始调用updateApply接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			log.info("updatebus入参："+jsonObject);
			// 8、定义返回JSON对象
			JSONObject dataJson = new JSONObject();
			String accesstoken = jsonObject.getString("accesstoken");
			String json = jsonObject.getString("detail");
        	String token = "Bearer "+accesstoken;
			String result = HttpRequestUtil.doPut(cx_url+"/licrequest", token, json);
			if (StringUtil.isNotBlank(result)) {
				JSONObject jsonresult = JSON.parseObject(result);
				String data = jsonresult.getString("DATA");
				if ("SUCCESS".equals(data)) {
					log.info("=======结束调用updateApply接口=======");
					return JsonUtils.zwdtRestReturn("1", "修改申请单信息成功", result);
				}
				else {
					return JsonUtils.zwdtRestReturn("0", "修改申请单信息失败", result);
				}
			}else {
				return JsonUtils.zwdtRestReturn("0", "修改申请单信息失败", "");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======updateApply接口参数：params【" + params + "】=======");
			log.info("=======updateApply异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "修改申请单信息失败：" + e.getMessage(), "");
		}
	}
	
	
	/**
	 * 新增申请单接口
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 */
	@RequestMapping(value = "/addapplyuser", method = RequestMethod.POST)
	public String addApplyUser(@RequestBody String params) {
		try {
			log.info("=======开始调用addApplyUser接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			log.info("addApplyUser入参："+jsonObject);
			// 8、定义返回JSON对象
			JSONObject dataJson = new JSONObject();
			String accesstoken = jsonObject.getString("accesstoken");
			String json = jsonObject.getString("detail");
        	Map<String,String> headers = new HashMap<String,String>();
        	headers.put("Authorization", "Bearer "+accesstoken);
			String result = HttpUtil.doPostJson(cx_url+"/licproxy", json, headers);
			if (StringUtil.isNotBlank(result)) {
				JSONObject jsonresult = JSON.parseObject(result);
				String data = jsonresult.getString("DATA");
				if ("SUCCESS".equals(data)) {
					log.info("=======结束调用addApplyUser接口=======");
					return JsonUtils.zwdtRestReturn("1", "新增申请用户成功", result);
				}
				else if ("ERROR".equals(data)) {
					dataJson.put("desc", jsonresult.getString("DESC"));
					return JsonUtils.zwdtRestReturn("1", "推送进度信息成功", dataJson.toString());
				}
				else {
					return JsonUtils.zwdtRestReturn("0", "新增申请用户失败", result);
				}
			}else {
				return JsonUtils.zwdtRestReturn("0", "新增申请用户失败", "");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======addApplyUser接口参数：params【" + params + "】=======");
			log.info("=======addApplyUser异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "新增申请用户失败：" + e.getMessage(), "");
		}
	}
	
	
	/**
	 * 新增申请单接口
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 */
	@RequestMapping(value = "/addprocess", method = RequestMethod.POST)
	public String addProcess(@RequestBody String params) {
		try {
			log.info("=======开始调用addProcess接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			log.info("addProcess入参："+jsonObject);
			// 8、定义返回JSON对象
			JSONObject dataJson = new JSONObject();
			String accesstoken = jsonObject.getString("accesstoken");
			String json = jsonObject.getString("detail");
        	Map<String,String> headers = new HashMap<String,String>();
        	headers.put("Authorization", "Bearer "+accesstoken);
			String result = HttpUtil.doPostJson(cx_url+"/licprocess", json, headers);
			if (StringUtil.isNotBlank(result)) {
				JSONObject jsonresult = JSON.parseObject(result);
				String data = jsonresult.getString("DATA");
				if ("SUCCESS".equals(data)) {
					log.info("=======结束调用addProcess接口=======");
					return JsonUtils.zwdtRestReturn("1", "推送进度信息成功", result);
				}
				else if ("ERROR".equals(data)) {
					dataJson.put("desc", jsonresult.getString("DESC"));
					return JsonUtils.zwdtRestReturn("1", "推送进度信息成功", dataJson.toString());
				}
				else{
					return JsonUtils.zwdtRestReturn("0", "推送进度信息失败", result);
				}
			}else {
				return JsonUtils.zwdtRestReturn("0", "推送进度信息失败", "");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======addProcess接口参数：params【" + params + "】=======");
			log.info("=======addProcess异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "推送进度信息失败：" + e.getMessage(), "");
		}
	}
	

}
