
package com.epoint.xmz.cjrmzarea.rest;

import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.epoint.common.util.JsonUtils;
import com.epoint.core.utils.config.ConfigUtil;
import com.epoint.core.utils.httpclient.HttpUtil;
import com.epoint.core.utils.string.StringUtil;
import com.epoint.xmz.cjrmzarea.api.ICjrMzAreaService;
import com.epoint.xmz.cjrmzarea.api.entity.CjrMzArea;

@RestController
@RequestMapping("/cjrareacode")
public class JnGetareacodeController {
	/**
	 * 日志
	 */
	private Logger log = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	public static final String cx_url= ConfigUtil.getConfigValue("epointframe", "cxurl");
	
	@Autowired
	private ICjrMzAreaService iCjrMzAreaService ;
	
	/**
	 * 提交成品油变更的办件数据推送到
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 */
	@RequestMapping(value = "/getareacode", method = RequestMethod.POST)
	public void getareacode(@RequestBody String params) {
		try {
			log.info("=======开始调用getCxCode接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			 String vercode = jsonObject.getString("vercode");
			 String username = jsonObject.getString("username");
			 String password = jsonObject.getString("password");
			 
			 String sendCode = jsonObject.getString("username");
			 
			 
			String code = "http://172.20.58.47/qgclxinxihuafuwupingtai/?iw-apikey=dfb378cba6794bf2ba834e86d8d9f257&iw-cmd=login&username="+username+"&password="+password+"&verifycode="+vercode+"&dx_verifcode=";
			String coderesult = HttpUtil.doGet(code);
			log.info("登录信息：" + coderesult);
			
			 String nodeId = jsonObject.getString("nodeid");
	        // 1、获取受理首页信息
	        String url = "http://172.20.58.47/qgclxinxihuafuwupingtai/?iw-apikey=dfb378cba6794bf2ba834e86d8d9f257&iw-cmd=getNodeId&nodeId="+nodeId;
	        log.info("获取区县信息：" + url);
	        String result = HttpUtil.doGet(url);
	        
	        JSONObject result1 = JSONObject.parseObject(result);
	        if("success".equals(result1.getString("rtnMsg"))) {
	        	JSONObject data = result1.getJSONObject("data");
	        	JSONArray list = data.getJSONArray("list");
	        	for (int i=0;i<list.size();i++) {
	        		JSONObject object = list.getJSONObject(i);
	        		String modeid = object.getString("nodeId");
	        		if (StringUtil.isNotBlank(modeid)) {
	        			 String result2 = HttpUtil.doGet("http://172.20.58.47/qgclxinxihuafuwupingtai/?iw-apikey=dfb378cba6794bf2ba834e86d8d9f257&iw-cmd=getNodeId&nodeId="+modeid);
	        			 JSONObject result3 = JSONObject.parseObject(result2);
	        			 if("success".equals(result3.getString("rtnMsg"))) {
	        				 JSONObject data2 = result3.getJSONObject("data");
	        		        JSONArray list3 = data2.getJSONArray("list");
	        		        for (int j=0;j<list3.size();j++) {
	        	        		JSONObject object3 = list3.getJSONObject(j);
	        	        		String nodeId3 = object3.getString("nodeId");
	        	        		String nodeName3 = object3.getString("nodeName");
	        	        		CjrMzArea record = new CjrMzArea();
	        	        		record.setRowguid(UUID.randomUUID().toString());
	        	        		record.setOperatedate(new Date());
	        	        		record.setCjrname(nodeName3);
	        	        		record.setCjrareacode(nodeId3);
	        	        		iCjrMzAreaService.insert(record);
	        	        		
	        		        }
	        			 }
	        		}
	        	}
	        }
	        
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======getCxCode接口参数：params【" + params + "】=======");
			log.info("=======getCxCode异常信息：" + e.getMessage() + "=======");
		}
	}

	
	/**
	 * 提交成品油变更的办件数据推送到
	 * 
	 * @param params
	 *            接口的入参
	 * @return
	 */
	@RequestMapping(value = "/sendMsg", method = RequestMethod.POST)
	public String sendMsg(@RequestBody String params) {
		try {
			log.info("=======开始调用msggetareacode接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			
			 
			String code = "http://172.20.58.47/qgclxinxihuafuwupingtai/?iw-apikey=dfb378cba6794bf2ba834e86d8d9f257&iw-cmd=dx_verifycode";
			String coderesult = HttpUtil.doGet(code);
			log.info("短信验证码信息：" + coderesult);
			
			return JsonUtils.zwdtRestReturn("1", "短信发送成功", coderesult);
	        
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======msggetareacode接口参数：params【" + params + "】=======");
			log.info("=======msggetareacode异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "发送失败", "");
	        
		}
	}
	
	
	@RequestMapping(value = "/msgLogin", method = RequestMethod.POST)
	public String msgLogin(@RequestBody String params) {
		try {
			log.info("=======开始调用msgLogin接口=======");
			// 1、接口的入参转化为JSON对象
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			 String msgcode = jsonObject.getString("msgcode");
			 
			String code = "http://172.20.58.47/qgclxinxihuafuwupingtai/?iw-apikey=dfb378cba6794bf2ba834e86d8d9f257&iw-cmd=dx_login&dx_verifycode="+msgcode;
			String coderesult = HttpUtil.doGet(code);
			log.info("短信验证码信息：" + coderesult);
			
			return JsonUtils.zwdtRestReturn("1", "验证码登录成功", coderesult);
	        
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======msgLogin接口参数：params【" + params + "】=======");
			log.info("=======msgLogin异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "发送失败", "");
	        
		}
	}
	
	
	
	@RequestMapping(value = "/getAreacodeMsg", method = RequestMethod.POST)
	public String getAreacodeMsg(@RequestBody String params) {
		try {
			log.info("=======开始调用msggetareacode接口=======");
			// 1、接口的入参转化为JSON对象
			JSONObject jsonObject = JSONObject.parseObject(params);
			
			//370800000000
			 String nodeId = jsonObject.getString("nodeid");
		        // 1、获取受理首页信息
		        String url = "http://172.20.58.47/qgclxinxihuafuwupingtai/?iw-apikey=dfb378cba6794bf2ba834e86d8d9f257&iw-cmd=sl_xzqy&nodeId="+nodeId;
		        log.info("获取区县信息：" + url);
		        String result = HttpUtil.doGet(url);
		        
		        JSONObject result1 = JSONObject.parseObject(result);
		        if("success".equals(result1.getString("rtnMsg"))) {
		        	JSONObject data = result1.getJSONObject("data");
		        	JSONArray list = data.getJSONArray("list");
		        	for (int i=0;i<list.size();i++) {
		        		JSONObject object = list.getJSONObject(i);
		        		String modeid = object.getString("nodeId");
		        		if (StringUtil.isNotBlank(modeid)) {
		        			 String result2 = HttpUtil.doGet("http://172.20.58.47/qgclxinxihuafuwupingtai/?iw-apikey=dfb378cba6794bf2ba834e86d8d9f257&iw-cmd=sl_xzqy&nodeId="+modeid);
		        			 JSONObject result3 = JSONObject.parseObject(result2);
		        			 if("success".equals(result3.getString("rtnMsg"))) {
		        				 JSONObject data2 = result3.getJSONObject("data");
		        		        JSONArray list3 = data2.getJSONArray("list");
		        		        for (int j=0;j<list3.size();j++) {
		        	        		JSONObject object3 = list3.getJSONObject(j);
		        	        		String nodeId3 = object3.getString("nodeId");
		        	        		String nodeName3 = object3.getString("nodeName");
		        	        		CjrMzArea record = new CjrMzArea();
		        	        		record.setRowguid(UUID.randomUUID().toString());
		        	        		record.setOperatedate(new Date());
		        	        		record.setCjrname(nodeName3);
		        	        		record.setCjrareacode(nodeId3);
		        	        		iCjrMzAreaService.insert(record);
		        	        		
		        		        }
		        			 }
		        		}
		        	}
		        }
		        
			return JsonUtils.zwdtRestReturn("1", "辖区获取成功", "");
	        
		} catch (Exception e) {
			e.printStackTrace();
			log.info("=======msggetareacode接口参数：params【" + params + "】=======");
			log.info("=======msggetareacode异常信息：" + e.getMessage() + "=======");
			return JsonUtils.zwdtRestReturn("0", "发送失败", "");
	        
		}
	}

	
	

}
